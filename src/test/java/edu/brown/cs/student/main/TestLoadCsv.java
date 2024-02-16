package edu.brown.cs.student.main;

import edu.brown.cs.student.main.server.CSV.LoadCsv;
import org.junit.jupiter.api.BeforeEach;

public class TestLoadCsv {

    MockCensusData mockData;
    @BeforeEach
    public void setup() {
        LoadCsv loadCsv = new LoadCsv(mockData);
    }


}

