package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.server.CSV.CensusData;
import edu.brown.cs.student.main.server.CSV.LoadCsv;
import edu.brown.cs.student.main.server.CSV.SearchCsv;
import edu.brown.cs.student.main.server.CSV.ViewCsv;
import spark.Spark;

/**
 * Top-level class for this demo. Contains the main() method which starts Spark and runs the various
 * handlers (2).
 *
 * <p>Notice that the OrderHandler takes in a state (menu) that can be shared if we extended the
 * restaurant They need to share state (a menu). This would be a great opportunity to use dependency
 * injection. If we needed more endpoints, more functionality classes, etc. we could make sure they
 * all had the same shared state.
 */
public class Server {
  static final int port = 4040;

  public static void main(String[] args) {

    Spark.port(port);

    CensusData censusData = new CensusData("");

    // Setting up the handler for the loading, searching, and viewing endpoints
    Spark.get("loadCsv", new LoadCsv(censusData));
    Spark.get("searchCsv", new SearchCsv(censusData));
    Spark.get("viewCsv", new ViewCsv(censusData));

    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);
  }
}
