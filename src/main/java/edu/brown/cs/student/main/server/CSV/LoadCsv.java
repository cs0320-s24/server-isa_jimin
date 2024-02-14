package edu.brown.cs.student.main.server.CSV;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.CSV.LoadCsv.LoadCSVFailureResponse;
import edu.brown.cs.student.main.server.CSV.LoadCsv.LoadCSVSuccessResponse;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class LoadCsv implements Route {

    private DataHandler dataHandler;
    private String filePath;
    public LoadCsv(DataHandler dataHandler){
        this.dataHandler = dataHandler;
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {

        this.filePath = request.queryParams("fileName");

        String folderPath = "data";
        Path absoluteFolderPath = Paths.get(folderPath).toAbsolutePath();
        Path csvPath =
                Paths.get(
                        absoluteFolderPath
                                + "/"
                                + filePath);

        FileReader fileReader = new FileReader(csvPath.toString());
        //BufferedReader reader = new BufferedReader(fileReader);
        try(fileReader){
            return LoadCSVSuccessResponse("success", ).serialize();
        }catch(IOException e){
            return LoadCSVFailureResponse("failure", ).serialize();
        }
    }

    public record LoadCSVSuccessResponse(String response, String filepath) {
        /**
         * @return this response, serialized as Json
         */
        String serialize() {
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

    /** Response object to send if someone requested soup from an empty Menu */
    public record LoadCSVFailureResponse(String response_type) {
        public LoadCSVFailureResponse() {
            this("error");
        }

        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            Moshi moshi = new Moshi.Builder().build();
            return moshi.adapter(LoadCsv.LoadCSVFailureResponse.class).toJson(this);
        }
    }
}

