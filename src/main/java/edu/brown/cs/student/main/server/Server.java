package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.server.CSV.CensusData;
import edu.brown.cs.student.main.server.CSV.LoadCsv;
import edu.brown.cs.student.main.server.CSV.SearchCsv;
import edu.brown.cs.student.main.server.CSV.ViewCsv;
import edu.brown.cs.student.main.server.backend.Exceptions.DataSourceException;
import edu.brown.cs.student.main.server.backend.data.BroadBand;
import edu.brown.cs.student.main.server.backend.data.BroadBandHandler;
import spark.Spark;

/**
 * Top-level class for this program. Contains the main() method which starts Spark and runs the various
 * handlers
 */
public class Server {
  static final int port = 4040;

  public static void main(String[] args) throws DataSourceException {

    Spark.port(port);

    CensusData censusData = new CensusData("");

    // Setting up the handler for the loading, searching, and viewing endpoints
    Spark.get("loadCsv", new LoadCsv(censusData));
    Spark.get("viewCsv", new ViewCsv(censusData));
    Spark.get("searchCsv", new SearchCsv(censusData));
    Spark.get("broadBand", new BroadBandHandler(new BroadBand()));
    http://localhost:4040/broadBand?state=idaho&county=ada_county

    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);
  }
}
