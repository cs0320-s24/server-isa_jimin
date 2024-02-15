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

public class ViewCsv implements Route {

    private CensusData censusData;
    public ViewCsv(CensusData censusData){
        this.censusData = censusData;
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        if (this.censusData.getFilePath() == null || this.censusData.getFilePath().isEmpty()) {
            return new ViewFailure("failure").serialize();
        }
        List<List<String>> csvData = this.censusData.parseCsv(this.censusData.getFilePath());

        System.out.println(csvData);
        return new ViewSuccess(csvData).serialize();
    }

    public record ViewSuccess(List<List<String>> csvData) {

        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            Moshi moshi = new Moshi.Builder().build();
            Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
            JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
            Map<String, Object> responseMap = new HashMap<>();
            try {
                responseMap.put("type", "success");
                responseMap.put("data", csvData);
                return adapter.toJson(responseMap);
            } catch (Exception e) {
                responseMap.put("type", "error");
                responseMap.put("error_type", "error_bad_json");
                responseMap.put("details", e.getMessage());
                return adapter.toJson(responseMap);
            }
        }
    }

    public record ViewFailure(String result) {

        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            Moshi moshi = new Moshi.Builder().build();
            Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
            JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("result", "failure");
            responseMap.put("error_type", "error_data_source");
            return adapter.toJson(responseMap);
        }
    }
}
