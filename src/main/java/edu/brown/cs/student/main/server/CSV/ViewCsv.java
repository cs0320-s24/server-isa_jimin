package edu.brown.cs.student.main.server.CSV;

import spark.Request;
import spark.Response;
import spark.Route;

public class ViewCsv implements Route {

    DataHandler dataHandler;
    public ViewCsv(DataHandler dataHandler){
        this.dataHandler = dataHandler;
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
       //convert csv data to json
        return null;
    }
}
