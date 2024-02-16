package edu.brown.cs.student.main.server.CSV;

import edu.brown.cs.student.main.server.backend.Exceptions.FactoryFailureException;
import edu.brown.cs.student.main.server.backend.parser.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CensusData {
  private String filePath;
  private CSVParser parser;
  private BufferedReader fileReader;

  /**
   * CensusData constructor which takes in a filepath to be accessed by other classes that have an
   * instance of CensusData
   *
   * @param filePath filepath that the user inserts into the query
   */
  public CensusData(String filePath) {
    this.filePath = filePath;
  }

  /**
   * Setter method to set the filepath when the user inserts it into the query
   *
   * @param filePath that is set by the user
   */
  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  /**
   * Gets the file path so other classes can utilize it
   *
   * @return the filepath
   */
  public String getFilePath() {
    return this.filePath;
  }

  /**
   * Parse method we used so that the search and view classes have access to parse, and we don't
   * need to have to create an instance of the parser every time we would like to parse.
   *
   * @param filepath filepath of document to parse
   */
  public List parseCsv(String filepath) throws IOException {
    try {
      FileReader fileReader1 = new FileReader(filepath);
      fileReader = new BufferedReader(fileReader1);
      parser = new CSVParser(fileReader, new Create());
      System.out.println("File Parsed");
      return parser.parse();
    } catch (FactoryFailureException e) {
      System.err.println("Error Parsing CSV");
      throw new RuntimeException(e);
    }
  }

  public List<List<String>> searchColName(String query, String colName) throws IOException,
   FactoryFailureException {
          Search search = new Search(this.parser, query);
          return search.colSearch(colName, null);
      }

      public List<List<String>> searchColIndex(String query, Integer index) throws IOException,
   FactoryFailureException {
          Search search = new Search(this.parser, query);
          return search.colSearch(null, index);
      }

  public List<List<String>> searchNoHeader(String query)
      throws IOException, FactoryFailureException {
    Search search = new Search(this.parser, query);
    return search.basicSearch();
  }



  /**
   * This is the Create class which we chose to nest in DataHandler so data could utilize a create
   * method without having to implement CreatorFromRow since it is not a create object.
   */
  private static class Create implements CreatorFromRow<List<String>> {

    /**
     * this method takes in data and returns them as a list of strings.
     *
     * @param row takes in a row
     * @return row of strings
     * @throws FactoryFailureException throws an exception if there is an error creating an object
     *     from a row
     */
    @Override
    public List<String> create(List<String> row) throws FactoryFailureException {
      return row;
    }
  }
}
