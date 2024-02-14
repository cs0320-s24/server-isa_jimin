package edu.brown.cs.student.main.server.backend;
import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.security.Key;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

//need to decide when/how to remove stale (outdated or irrelevant)
//entries from cache and allow developer to control how this is done

//class could assist in managing cached data and
//implementing configurable eviction policies
public class Proxy<Graph> extends Ticker {
    public  Proxy(){
//        CacheLoader<Key, DatabaseConnection> loader = new CacheLoader<Key, DatabaseConnection> () {
//            public DatabaseConnection load(Key key) throws Exception {
//                return openConnection(key);
//            }
//        };
        RemovalListener<Key, DatabaseConnection> removalListener = new RemovalListener<Key, DatabaseConnection>() {
            public void onRemoval(RemovalNotification<Key, DatabaseConnection> removal) {
                DatabaseConnection conn = removal.getValue();
                conn.close(); // tear down properly
            }
        };
        LoadingCache<Key, Graph> graphs = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .removalListener(removalListener)
                .build(
                        new CacheLoader<>() {
                            @Override
                            public Graph load(Key key) throws Exception {
                                return createExpensiveGraph(key);
                            }
                        });
//        CacheBuilder<Object, Object> graphs = CacheBuilder.newBuilder()
//             //   .ticker(Ticker)
//                .expireAfterAccess(2,TimeUnit.MINUTES);

                        //CacheBuilder.ticker(Ticker);
                        //CacheBuilder.newBuilder().maximumWeight(100000).weigher(new Weigher<Key, Graph>() {
//            public int weigh(Key k, Graph g) {return g.vertices().size();}}).build(new CacheLoader<Key, Graph>() {
//                public Graph load(Key key) { // no checked exception
//                 return createExpensiveGraph(key);}
//            });
    }
    @Override
    public long read() {
        return 0;
    }


}
