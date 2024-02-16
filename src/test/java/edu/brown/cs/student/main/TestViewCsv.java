package edu.brown.cs.student.main;

import edu.brown.cs.student.main.server.CSV.LoadCsv;
import edu.brown.cs.student.main.server.CSV.ViewCsv;
import org.junit.jupiter.api.BeforeEach;

public class TestViewCsv {
    MockCensusData mockData;
    @BeforeEach
    public void setup() {
        ViewCsv viewCsv = new ViewCsv(mockData);
    }

}
