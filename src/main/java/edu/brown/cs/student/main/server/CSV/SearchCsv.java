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
        if(this.dataHandler.getFilePath() == null || this.dataHandler.getFilePath().isEmpty()){
            System.err.println("No filepath is given");
            return new ViewCsv.ViewFailure("failure").serialize();
        }

        String keyword = request.queryParams("");

        return null;
    }
}
