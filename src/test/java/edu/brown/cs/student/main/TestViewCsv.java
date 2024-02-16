package edu.brown.cs.student.main;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.CSV.LoadCsv;
import edu.brown.cs.student.main.server.CSV.ViewCsv;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestViewCsv {
    MockCensusData mockData;
    @BeforeEach
    public void setup() {
        ViewCsv viewCsv = new ViewCsv(mockData);
    }

    public void testViewCSVSuccess(){


    }

    public void testLoadCSVFailureResponse(){

    }


}
