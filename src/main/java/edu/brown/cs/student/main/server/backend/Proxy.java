package edu.brown.cs.student.main.server.backend;

import com.google.common.cache.*;
import edu.brown.cs.student.main.server.CSV.DataHandler;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import spark.Request;
import spark.Response;
import spark.Route;

// need to decide when/how to remove stale (outdated or irrelevant)
// entries from cache and allow developer to control how this is done

// class could assist in managing cached data and
// implementing configurable eviction policies

// public class Proxy<Graph> extends Ticker{
public class Proxy implements Route {
  private final DataHandler wrappedHandler;
  private final LoadingCache<String, Object> cache;

  public Proxy(DataHandler wrappedHandler) {
    this.wrappedHandler = wrappedHandler;
    RemovalListener<String, Object> removalListener = removalNotification -> {};

    this.cache =
        CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .recordStats()
            .removalListener(removalListener)
            .build(
                new CacheLoader<>() {
                  @Override
                  public Collection<String> load(String s) throws Exception {
                    // return wrappedHandler.handle(request);
                    return null;
                  }
                  // load(request, response)
                });
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String cacheKey = generateCacheKey(request);
    return this.cache.get(cacheKey);
  }

  private String generateCacheKey(Request request) {
    // Start with the request path
    String path = request.pathInfo();

    String queryParams =
        request.queryMap().toMap().entrySet().stream()
            .sorted(Map.Entry.comparingByKey()) // Ensure consistent order
            .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
            .collect(Collectors.joining("&", "?", "")); // Concatenate all query params

    // Combine path and queryParams to form the cache key
    return path + queryParams;
  }
}
