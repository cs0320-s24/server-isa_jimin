package edu.brown.cs.student.main.server.backend;

import com.google.common.cache.*;

import edu.brown.cs.student.main.server.backend.data.BroadBand;
import edu.brown.cs.student.main.server.backend.data.BroadBandAccessPercent;
import edu.brown.cs.student.main.server.backend.data.Location;

import java.util.concurrent.TimeUnit;

// need to decide when/how to remove stale (outdated or irrelevant)
// entries from cache and allow developer to control how this is done

// class could assist in managing cached data and
// implementing configurable eviction policies

// public class Proxy<Graph> extends Ticker{

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

