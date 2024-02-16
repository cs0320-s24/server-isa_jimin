package edu.brown.cs.student.main.server.backend.data;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Moshi.Builder;
import com.squareup.moshi.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

import edu.brown.cs.student.main.server.backend.Exceptions.DataSourceException;

/**
 * The BroadbandHandler class handles incoming HTTP requests for the percentage of households with
 * broadband access in a specific state and county.
 */
public class BroadBandHandler implements Route {

    private final BroadBand datasource;

    /**
     * This is the constructor for the BroadbandHandler class. It takes in a BroadBandDatasource
     * Datasource.
     *
     * @param datasource BroadbandDatasource object that can contact the Census API
     */
    public BroadBandHandler(BroadBand datasource) {
        this.datasource = datasource;
    }

    /**
     * This is our handle method which handles incoming HTTP requests by getting the broadband data
     * for a given state and county and sending back a response containing the requested data.
     *
     * @param request incoming HTTP request
     * @param response response to be sent back
     * @return a JSON string containing the broadband data or error details.
     */
    @Override
    public Object handle(Request request, Response response) {

        // Get the state and county that the request is for.
        String requestState = request.queryParams("state").replace("_", " ");
        String requestCounty = request.queryParams("county").replace("_", " ");
        Location location = new Location(requestState, requestCounty);

        // Build Moshi in preparation to send reply
        Moshi moshi = new Builder().build();

        // Create an adapter for the response map
        JsonAdapter<Map<String, Object>> responseAdapter =
                moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
        Map<String, Object> responseMap = new HashMap<>();

        // Create an adapter for the percent class
        JsonAdapter<BroadBandAccessPercent> percentAdapter =
                moshi.adapter(BroadBandAccessPercent.class);

        // Generate the reply
        try {
            // Ask the Datasource to get the percent.
            BroadBandAccessPercent data = this.datasource.getBroadbandPercent(location.getState(), location.county());

            // Build response if this is a success. Error will be thrown and caught otherwise.
            responseMap.put("type", "success");
            responseMap.put("percent", percentAdapter.toJson(data));
            responseMap.put("state", requestState);
            responseMap.put("county", requestCounty);
            responseMap.put("access_time", this.getDateTime());

            return responseAdapter.toJson(responseMap);

            // Catch all the exceptions
        } catch (DataSourceException e) {
            responseMap.put("type", "error");
            responseMap.put("error_type", "datasource");
            responseMap.put("details", e.getMessage());
            return responseAdapter.toJson(responseMap);
        } catch (Throwable e) {
            responseMap.put("type", "error");
            responseMap.put("error_type", e.getCause());
            responseMap.put("details", e.getMessage());
            return responseAdapter.toJson(responseMap);
        }
    }

    /**
     * This method is of type string and gets the current date and time of a call to the Census API
     *
     * @return returns the current date and time
     */
    private String getDateTime() {
        LocalDateTime currTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currTime.format(dateTimeFormatter);
    }
}
