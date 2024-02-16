package edu.brown.cs.student.main;

import edu.brown.cs.student.main.server.CSV.CensusData;
import edu.brown.cs.student.main.server.CSV.LoadCsv;
import edu.brown.cs.student.main.server.backend.census.CensusHandler;
import org.junit.jupiter.api.BeforeEach;
import spark.Spark;

public class TestLoadCsv {

    MockCensusData mockData;
    @BeforeEach
    public void setup() {
        LoadCsv loadCsv = new LoadCsv(mockData);
    }


}

