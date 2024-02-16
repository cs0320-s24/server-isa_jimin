package edu.brown.cs.student.main;

import edu.brown.cs.student.main.server.CSV.CensusData;

public class MockCensusData extends CensusData {
    /**
     * DataHandler constructor which takes in a filepath to be accessed by other classes that have an
     * instance of DataHandler
     *
     * @param filePath filepath that the user inserts into the query
     */
    public MockCensusData(String filePath) {
        super(filePath);
    }

    //might have to write getters and setters
}
