package edu.brown.cs.student.main.server.CSV;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.backend.handler.OrderHandler;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class LoadCsv implements Route {

    private DataHandler dataHandler;
    public LoadCsv(DataHandler dataHandler){
        this.dataHandler = dataHandler;
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String filePath = request.queryParams("");

        FileReader fileReader = new FileReader(filePath);
        //BufferedReader reader = new BufferedReader(fileReader);
        try(fileReader){
            return LoadValidResponse("success", ).serialize();
        }catch(IOException e){
            return LoadInvalidResponse("failure", ).serialize();
        }

    }

    public record LoadValidResponse(String response, Map<String, Object> responseMap) {
        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            try {
                // Initialize Moshi which takes in this class and returns it as JSON!
                Moshi moshi = new Moshi.Builder().build();
                JsonAdapter<OrderHandler.SoupSuccessResponse> adapter = moshi.adapter(OrderHandler.SoupSuccessResponse.class);
                return adapter.toJson();
            } catch (Exception e) {
                //should return a type error, added with e.getMessage()
            }
        }
    }

    /** Response object to send if someone requested soup from an empty Menu */
    public record LoadInvalidResponse(String response_type, ) {
        public LoadInvalidResponse(String response, Map<String, Object> responseMap) {
            this("error");
        }

        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            Moshi moshi = new Moshi.Builder().build();
            return moshi.adapter(OrderHandler.SoupNoRecipesFailureResponse.class).toJson(this);
        }
    }
}

