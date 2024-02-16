package edu.brown.cs.student.main.server.backend.data;

import edu.brown.cs.student.main.server.backend.Exceptions.DataSourceException;

/**
 * Interface for obtaining BroadBand data
 */
public interface BroadbandDatasource {
  BroadBandAccessPercent getBroadbandPercent(Location location) throws DataSourceException;

  BroadBandAccessPercent getBroadbandPercent(String state, String county)
          throws DataSourceException;
}
