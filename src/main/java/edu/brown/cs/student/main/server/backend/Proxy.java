package edu.brown.cs.student.main.server.backend;
import com.google.common.cache.*;
import edu.brown.cs.student.main.server.backend.handler.CensusHandler;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

//need to decide when/how to remove stale (outdated or irrelevant)
//entries from cache and allow developer to control how this is done

//class could assist in managing cached data and
//implementing configurable eviction policies

//public class Proxy<Graph> extends Ticker{
public class Proxy implements Route{
   private final CensusHandler wrappedHandler;
   private final LoadingCache<String, Object> cache;
    public  Proxy(CensusHandler wrappedHandler) {
        this.wrappedHandler = wrappedHandler;
        RemovalListener<String, Object> removalListener = removalNotification -> {

        };

        this.cache = CacheBuilder.newBuilder()
                .maximumSize(10)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats()
                .removalListener(removalListener)
                .build(
                        new CacheLoader<>() {
                            @Override
                            public Collection<String> load(String s) throws Exception {
                                return wrappedHandler.handle(request);
                            }
                            //load(request, response)
                        }
                );
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String cacheKey = generateCacheKey(request);
        return this.cache.get(cacheKey);
    }

    private String generateCacheKey(Request request){
        //figure out how to do this
        return null;
    }
}
