package edu.brown.cs.student.main.server.CSV;

import spark.Request;
import spark.Response;
import spark.Route;

public class SearchCsv implements Route {

    DataHandler dataHandler;
    public SearchCsv(DataHandler dataHandler){
        this.dataHandler = dataHandler;
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String keyword = request.queryParams("");
        //need to search column index

        return null;
    }
}
