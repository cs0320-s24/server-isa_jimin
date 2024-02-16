package edu.brown.cs.student.main;

import edu.brown.cs.student.main.server.CSV.CensusData;

public class MockCensusData extends CensusData {
    /**
     * Mock CensusData class to be used for testing.
     */
    public MockCensusData(String filePath) {
        super(filePath);
    }
}
