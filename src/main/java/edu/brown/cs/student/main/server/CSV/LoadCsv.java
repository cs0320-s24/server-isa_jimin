package edu.brown.cs.student.main.server.CSV;

import spark.Request;
import spark.Response;
import spark.Route;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoadCsv implements Route {

    private DataHandler dataHandler;
    public LoadCsv(DataHandler dataHandler){
        this.dataHandler = dataHandler;
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String filepath = request.queryParams("");
        try(load file in maybe my making new reader){
            return "File loaded successfully";
        }catch(IOException e){
            return "Error loading file";
        }
}}

