package edu.brown.cs.student.main.server.backend.census;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.brown.cs.student.main.server.CSV.CensusData;
import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.connect;

/**
 * This class is used to illustrate how to build and send a GET request then prints the response. It
 * will also demonstrate a simple Moshi deserialization from online data.
 */
public class CensusHandler implements Route {
  /**
   * This handle method needs to be filled by any class implementing Route. When the path set in
   * edu.brown.cs.examples.moshiExample.server.Server gets accessed, it will fire the handle method.
   *
   * <p>NOTE: beware this "return Object" and "throws Exception" idiom. We need to follow it because
   * the library uses it, but in general this lowers the protection of the type system.
   *
   * @param request The request object providing information about the HTTP request
   * @param response The response object providing functionality for modifying the response
   */
  @Override
  public Object handle(Request request, Response response) {
    // If you are interested in how parameters are received, try commenting out and
    // printing these lines! Notice that requesting a specific parameter requires that parameter
    // to be fulfilled.
    // If you specify a queryParam, you can access it by appending ?parameterName=name to the
    // endpoint
    // ex. http://localhost:3232/activity?participants=num
    Set<String> params = request.queryParams();
    System.out.println(params);
    String participants = request.queryParams("participants");
    System.out.println(participants);

    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();
    try {
      // Sends a request to the API and receives JSON back
      String activityJson = this.sendRequest(Integer.parseInt(participants));
      // Deserializes JSON into an Activity
      List<CensusData> activity = CensusAPIUtilities.deserializeData(activityJson);
      // Adds results to the responseMap
      responseMap.put("result", "success");
      responseMap.put("activity", activity);
      return responseMap;
    } catch (Exception e) {
      e.printStackTrace();
      // This is a relatively unhelpful exception message. An important part of this sprint will be
      // in learning to debug correctly by creating your own informative error messages where Spark
      // falls short.
      responseMap.put("result", "Exception");
    }
    return responseMap;
  }

  private String sendRequest(int participants)
      throws URISyntaxException, IOException, InterruptedException {

    URL requestURL = new URL("https", "api.census.gov",
            "/data/2010/dec/sf?get=NAME&for=state:*");
    //HttpURLConnection clientConnection = connect(requestURL);
    System.out.println("connected to client");

    HttpRequest buildBoredApiRequest =
            HttpRequest.newBuilder()
                    .uri(new URI("http://www.boredapi.com/api/activity?participants=" + participants))
                    .GET()
                    .build();


    // Send that API request then store the response in this variable. Note the generic type.
    HttpResponse<String> sentBoredApiResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildBoredApiRequest, HttpResponse.BodyHandlers.ofString());

    // What's the difference between these two lines? Why do we return the body? What is useful from
    // the raw response (hint: how can we use the status of response)?
    System.out.println(sentBoredApiResponse);
    System.out.println(sentBoredApiResponse.body());

    return sentBoredApiResponse.body();
  }

//  private loadCSV{
//    //load csv file if one is located at specific path
//    //requests must have filepath query parameter
//    //response must include filepath field
//    //share status of file loading with search and view
//  }
//
//  private viewCSV{
//    //sends back entire CSV file's conentnts as a json 2d array
//  }
//
//  private searchCSV{
//    //send back rows mathcing the given search criteria
//    //requests muts allow all search paramters needed to implement CSV
//  }
//
//  private broadband{
//    //sends back broadband data from ACS
//    //requests must have state & county query parameter
//    //response must include correspondng fields for all paramters in request recieved
//  }
}
