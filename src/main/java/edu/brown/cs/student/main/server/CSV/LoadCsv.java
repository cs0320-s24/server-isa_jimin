package edu.brown.cs.student.main.server.CSV;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoadCsv implements Route {
  /**
   * The LoadCsv class takes in CensusData and serializes a given file in order
   * to perform parsing and searching.
   */
  private CensusData censusData;
  private String filePath;

  /**
   * LoadCsv constructor, takes in a CensusData object
   * @param censusData returns the censusData
   */
  public LoadCsv(CensusData censusData) {
    this.censusData = censusData;
  }

  /**
   * Handles the file loading, produces a success response if the filereader is able to find the given file
   * via the file path
   * @param request from the user
   * @param response from the server
   * @return either a success or failure response
   * @throws Exception if a file is invalid or cannot be read
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    System.out.println("file loading");
    this.filePath = request.queryParams("fileName");

    String folderPath = "data";
    Path absoluteFolderPath = Paths.get(folderPath).toAbsolutePath();
    Path csvPath = Paths.get(absoluteFolderPath + "/" + filePath);
    System.out.println("file loaded");

    censusData.setFilePath(csvPath.toString());

    FileReader fileReader = new FileReader(csvPath.toString());
    try (fileReader) {
      System.out.println("success");
      return new LoadCSVSuccessResponse("success", filePath).serialize();
    } catch (IOException e) {
      System.out.println(e.getMessage());
      return new LoadCSVFailureResponse(filePath).serialize();
    }
  }

  /**
   * Success response given by the handler if the given file is successfully loaded
   * @param response from the server
   * @param filepath given from the user
   */
  public record LoadCSVSuccessResponse(String response, String filepath) {
    /**
     * @return this response, serialized as Json
     */
    public String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
      JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
      Map<String, Object> responseMap = new HashMap<>();

      try {
        responseMap.put("data", filepath);
        responseMap.put("type", "success");
        System.out.println("Filepath is: " + filepath);
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
   * Failure response if the csv cannot be loaded
   * @param response_type type of error from the invalid file
   */
  public record LoadCSVFailureResponse(String response_type) {
    public LoadCSVFailureResponse() {
      this("error");
    }

    /**
     * @return this response, serialized as Json
     */
    public String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(LoadCsv.LoadCSVFailureResponse.class).toJson(this);
    }
  }
}
