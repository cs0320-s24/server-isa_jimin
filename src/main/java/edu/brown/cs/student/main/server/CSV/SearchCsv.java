package edu.brown.cs.student.main.server.CSV;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import spark.Request;
import spark.Response;
import spark.Route;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.eclipse.jetty.webapp.MetaDataComplete.True;

public class SearchCsv implements Route {

<<<<<<< HEAD
    CensusData censusData;
    public SearchCsv(CensusData censusData){
        this.censusData = censusData;
=======
    Boolean headers;
    DataHandler dataHandler;
    public SearchCsv(DataHandler dataHandler){
        this.dataHandler = dataHandler;
>>>>>>> 58ec38c6160a991c0e5cdf59dcc0ba703bf3ebd9
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(this.censusData.getFilePath() == null || this.censusData.getFilePath().isEmpty()){
            System.err.println("No filepath is given");
            return new ViewCsv.ViewFailure("failure").serialize();
        }

        String query = request.queryParams("query");
        String hasHeaders = request.queryParams("hasHeaders");

        if (query == null) {
            System.err.println("No query to search");
            return new SearchFailure("error_bad_request", query).serialize();
        }
        if (hasHeaders == null) {
            return new SearchFailure("error_bad_request", query).serialize();
        }
        System.out.println(hasHeaders);
        if (hasHeaders.equalsIgnoreCase("yes")) {
            String nameIndex = request.queryParams("nameIndex");
            System.out.println(nameIndex);
            if (nameIndex.equalsIgnoreCase("name")) {
                String name = request.queryParams("name");
                // http://localhost:3232/SearchCSV?query= has headers with index name and town
                try {
<<<<<<< HEAD
                    List<List<String>> searchResult = this.censusData.searchColName(query, name);
=======
                    List<List<String>> searchResult = this.dataHandler.searchColName(query, name, headers);
>>>>>>> 58ec38c6160a991c0e5cdf59dcc0ba703bf3ebd9
                    return new SearchSuccess(searchResult, query, hasHeaders, nameIndex, name, null)
                            .serialize();
                } catch (Exception e) {
                    return new SearchFailure("error_bad_request", query).serialize();
                }
            } else if (nameIndex.equalsIgnoreCase("index")) {
                Integer index = Integer.parseInt(request.queryParams("index"));
                try {
<<<<<<< HEAD
                    List<List<String>> searchResult = this.censusData.searchColIndex(query, index);
=======
                    List<List<String>> searchResult = this.dataHandler.searchColIndex(query, index, headers);
>>>>>>> 58ec38c6160a991c0e5cdf59dcc0ba703bf3ebd9
                    return new SearchSuccess(searchResult, query, hasHeaders, nameIndex, null, index)
                            .serialize();

                } catch (Exception e) {
                    return new SearchFailure("error_bad_request", query).serialize();
                }
            }
        } else if (hasHeaders.equalsIgnoreCase("no")) {
            // http://localhost:3232/SearchCSV?query= does not have headers
            try {
                List<List<String>> result = this.censusData.searchNoHeader(query);
                return new SearchSuccess(result, query, hasHeaders, null, null, null).serialize();

            } catch (Exception e) {
                return new SearchFailure("error_bad_request", query).serialize();
            }
        }
        return new SearchFailure("error_bad_request", query).serialize();
    }

    /**
     * Response object to send, containing a serialized Json response and spitting back out the user
     * inputs.
     *
     * @param result - the result of the search
     * @param query - spits back out the intended word the user wants to search for
     * @param hasHeaders - spits back out the users response to if the file has headers or not
     * @param nameIndex - spits back out the answer to the name or index question
     * @param name - spits back out the column name inputted by the user if there is one
     * @param index - spits back out the index of the column if there is one inputted by the users
     */
    public record SearchSuccess(
            List<List<String>> result,
            String query,
            String hasHeaders,
            String nameIndex,
            String name,
            Integer index) {
        String serialize() {
            Moshi moshi = new Moshi.Builder().build();
            Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
            JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
            Map<String, Object> responseMap = new HashMap<>();
            try {
                responseMap.put("data", result);
                responseMap.put("type", "success");
                responseMap.put("query", query);
                responseMap.put("hasHeaders", hasHeaders);
                responseMap.put("nameIndex", nameIndex);
                responseMap.put("name", name);
                responseMap.put("index", index);

                return adapter.toJson(responseMap);
            } catch (Exception e) {
                responseMap.put("type", "error");
                responseMap.put("error_type", "error_bad_json");
                responseMap.put("details", e.getMessage());
                return adapter.toJson(responseMap);
            }
        }
    }

    /**
     * Handles the failure response when a user inputs an incorrect input in the query
     *
     * @param result
     * @param query
     */
    public record SearchFailure(String result, String query) {

        String serialize() {
            Moshi moshi = new Moshi.Builder().build();
            Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
            JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
            Map<String, Object> responseMap = new HashMap<>();
            try {
                responseMap.put("type", "error_bad_request");
                return adapter.toJson(responseMap);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }
}
