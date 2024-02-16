package edu.brown.cs.student.main.server.backend.data;
import com.google.common.cache.*;
import edu.brown.cs.student.main.server.backend.data.BroadBand;
import edu.brown.cs.student.main.server.backend.data.BroadBandAccessPercent;
import edu.brown.cs.student.main.server.backend.data.Location;
import java.util.concurrent.TimeUnit;

/**
 * This class removes outdated entries from the BroadBand.
 * Caching technique from Google Guava cache library.
 */
public class Proxy {
   private final BroadBand wrappedHandler;
   private final LoadingCache<Location, BroadBandAccessPercent> cache;
    public Proxy(BroadBand wrappedHandler, String state, String county) {
        this.wrappedHandler = wrappedHandler;
        RemovalListener<Location, BroadBandAccessPercent> removalListener = removalNotification -> {};

        this.cache = CacheBuilder.newBuilder()
                .maximumSize(10)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats()
                .removalListener(removalListener)
                .build(
                        new CacheLoader<Location, BroadBandAccessPercent>() {
                            public BroadBandAccessPercent load(Location location) throws Exception {
                                return wrappedHandler.getBroadbandPercent(location);
                            }
                        });
    }}

