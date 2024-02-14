package edu.brown.cs.student.main.server.backend;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
   private  Route wrappedRoute;
   private final LoadingCache<String, Collection<String>> cache;
    public  Proxy(){
        LoadingCache<String, Collection<String>> cache1;

//        CacheLoader<Key, DatabaseConnection> loader = new CacheLoader<Key, DatabaseConnection> () {
//            public DatabaseConnection load(Key key) throws Exception {
//                return openConnection(key);
//            }
//        };

        this.cache = CacheBuilder.newBuilder()
                .maximumSize(10)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats()
                .build(
                        new CacheLoader<>(){
                            @Override
                            public Collection<String> load(String s) throws Exception {
                                return wrappedRoute.handle();
                            }
                            //load(request, response)
                        }
                );


//        RemovalListener<Key, DatabaseConnection> removalListener = new RemovalListener<Key, DatabaseConnection>() {
//            public void onRemoval(RemovalNotification<Key, DatabaseConnection> removal) {
//                DatabaseConnection conn = removal.getValue();
//                conn.close(); // tear down properly
//            }
//        };
//        LoadingCache<Key, Graph> graphs = CacheBuilder.newBuilder()
//                .maximumSize(10000)
//                .expireAfterWrite(10, TimeUnit.MINUTES)
//                .recordStats()
//                //.removalListener(removalListener)
//                .build(
//                        new CacheLoader<>() {
//                            @Override
//                            public Graph load(String key) throws Exception {
//                                //return createExpensiveGraph(key);
//                                return wrapped
//                            }
//                        });
////        CacheBuilder<Object, Object> graphs = CacheBuilder.newBuilder()
////             //   .ticker(Ticker)
////                .expireAfterAccess(2,TimeUnit.MINUTES);
//
//                        //CacheBuilder.ticker(Ticker);
//                        //CacheBuilder.newBuilder().maximumWeight(100000).weigher(new Weigher<Key, Graph>() {
////            public int weigh(Key k, Graph g) {return g.vertices().size();}}).build(new CacheLoader<Key, Graph>() {
////                public Graph load(Key key) { // no checked exception
////                 return createExpensiveGraph(key);}
////            });

    }
    @Override
    public Collection<String> handle(Request request, Response response) throws Exception {
        Collection<String> result = this.cache.getUnchecked(response.toString());
        return result;
    }
}
