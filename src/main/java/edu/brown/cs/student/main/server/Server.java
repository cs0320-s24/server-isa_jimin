package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.server.CSV.DataHandler;
import edu.brown.cs.student.main.server.CSV.LoadCsv;
import edu.brown.cs.student.main.server.CSV.SearchCsv;
import edu.brown.cs.student.main.server.CSV.ViewCsv;
import edu.brown.cs.student.main.server.backend.Soup;
import edu.brown.cs.student.main.server.backend.SoupAPIUtilities;
import edu.brown.cs.student.main.server.backend.handler.CensusHandler;

import java.util.ArrayList;
import java.util.List;
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

  final static int port = 3232;
  public static void main(String[] args) {
    Spark.port(port);


    DataHandler dataHander = new DataHandler("/Users/jiminryu/Desktop/cs0320/server-isa_jimin/src/main/java/edu/brown/cs/student/main/server/backend/data/RICensus.csv");

    // Allows clients to make requests to server
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    // Sets up data needed for the OrderHandler. You will likely not read from local
    // JSON in this sprint.
    String menuAsJson = SoupAPIUtilities.readInJson("data/menu.json");
    List<Soup> menu = new ArrayList<>();
    try {
      menu = SoupAPIUtilities.deserializeMenu(menuAsJson);
    } catch (Exception e) {
      // See note in ActivityHandler about this broad Exception catch... Unsatisfactory, but gets
      // the job done in the gearup where it is not the focus.
      e.printStackTrace();
      System.err.println("Errored while deserializing the menu");
    }

    // Setting up the handler for the loading, searching, and viewing endpoints
    Spark.get("loadCsv", new LoadCsv(dataHander));
    Spark.get("searchCsv", new SearchCsv(dataHander));
    Spark.get("viewCsv", new ViewCsv(dataHander));

    Spark.init();
    Spark.awaitInitialization();

    // Notice this link alone leads to a 404... Why is that?
    System.out.println("Server started at http://localhost:" + port);
  }
}
