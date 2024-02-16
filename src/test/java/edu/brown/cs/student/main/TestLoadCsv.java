package edu.brown.cs.student.main;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.CSV.CensusData;
import edu.brown.cs.student.main.server.CSV.LoadCsv;
import edu.brown.cs.student.main.server.backend.census.CensusHandler;
import org.junit.jupiter.api.BeforeEach;
import spark.Spark;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLoadCsv {

    MockCensusData mockData;
    @BeforeEach
    public void setup() {
        LoadCsv loadCsv = new LoadCsv(mockData);
    }

    public void testLoadCSVSuccessResponse(){
        LoadCsv.LoadCSVSuccessResponse success = new LoadCsv.LoadCSVSuccessResponse("success",
                "file/path");
        String sResponse = success.serialize();

        Moshi moshi = new Moshi.Builder().build();
        Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
        JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
        Map<String, Object> expected = new HashMap<>();
        expected.put("data", "file/path");
        expected.put("type", "success");
        String expected1 = adapter.toJson(expected);

        assertEquals(expected1, sResponse);

    }

    public void testLoadCSVFailureResponse(){
        LoadCsv.LoadCSVFailureResponse failure = new LoadCsv.LoadCSVFailureResponse("error");
        String fResponse = failure.serialize();

        Moshi moshi = new Moshi.Builder().build();
        String expected = moshi.adapter(LoadCsv.LoadCSVFailureResponse.class).toJson(failure);

        assertEquals(expected, fResponse);
    }

}

