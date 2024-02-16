package edu.brown.cs.student.main.server.backend.data;

import edu.brown.cs.student.main.server.CSV.CensusData;

public interface BroadbandDatasource {
  CensusData getBroadbandPercentage(String state, String county);
}
