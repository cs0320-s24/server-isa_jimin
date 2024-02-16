package edu.brown.cs.student.main.server.backend.parser;

import edu.brown.cs.student.main.server.backend.Exceptions.FactoryFailureException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Class responsible for searching the parsed Csv. Called upon in the main class to do different
 * searches based on the number of, and actual arguments that are passed in.
 */
public class Search {

  /**
   * The instance variables that are used throughout the Search class. Needed because they are
   * called upon in every search method of the class.
   */
  private CSVParser<List<String>> parser;

  private String target;
  private List<List<String>> parsedList;

  /**
   * Constructor for the Search class. Creates an instance of search.
   *
   * @param inputParser - parser that is used to parse the search file into an ArrayList of type
   *     List of Strings.
   * @param target - the string that is being searched for, what each search algorithm returns arrow
   *     for if it is found in its specified location.
   */
  public Search(CSVParser<List<String>> inputParser, String target)
      throws FactoryFailureException, IOException {
    this.parser = inputParser;
    this.target = target;
    //this.parsedList = this.parser.parse();
  }

  /**
   * Method that searches through the whole file for the target. Checking every column in every row.
   *
   * @return a list of all the rows printed so that it can be unit tested.
   */
  public ArrayList<List<String>> basicSearch() {

    ArrayList<List<String>> toReturn = new ArrayList<>();
    HashSet<List<String>> checker = new HashSet<>();

      for (int i = 0; i < this.parsedList.size(); i++) {
      for (int j = 0; j < this.parsedList.get(i).size(); j++) {

        String current = this.parsedList.get(i).get(j);

        // if the target that is being searched for is found, print the row.
        if (this.target.equals(current)) {
          if (!checker.contains(this.parsedList.get(i))) {
            toReturn.add(this.parsedList.get(i));
            checker.add(this.parsedList.get(i));
          }
        }
      }
    }
    return toReturn;
  }

  /**
   * Method that searches for the target in a specified col from the file.
   *
   * @param colAsStr - the column that will be searched.
   * @return - a list of all the rows printed to help with unit testing.
   */
  public ArrayList<List<String>> colSearch(String colAsStr, Integer colIndex) {
    // crates needed variables
    ArrayList<List<String>> toReturn = new ArrayList<>();
    int col = -1;

    // convert inputted col string to an int and then loop through rows
    if (colIndex == null) {
      col = Integer.parseInt(colAsStr);
    } else if (colAsStr == null) {
      col = colIndex;
    }

    // cant search cols that don't exist
    if (this.parsedList.isEmpty() || col > this.parsedList.get(0).size() - 1 || col < 0) {
      throw new IndexOutOfBoundsException();
    }

    // begin search
    for (int i = 0; i < this.parsedList.size(); i++) {
      // checks specific column for index
      if (this.target.equals(this.parsedList.get(i).get(col))) {
        toReturn.add(this.parsedList.get(i));
      }
    }
    return toReturn;
  }

  /**
   * Method that searches for the target based on a specified column. Column is specified through
   * the name of one of the header columns.
   *
   * @param headerName - the name of the header column that will be searched.
   * @return - a list of all the rows in the header column containing the target, allowing for
   *     testing of the method.
   */
  public ArrayList<List<String>> headerSearch(String headerName) throws IOException {
    // creates needed variables
    ArrayList<List<String>> toReturn = new ArrayList<>();
    List<String> header = this.parser.getHeaderList();
    // returns index and if not in it returns -1
    int index = header.indexOf(headerName);

    if (index == -1) {
      throw new NoSuchElementException();
    }

    // loop through all rows
    for (int i = 0; i < this.parsedList.size(); i++) {
      // checks only specific colum the header corresponds to
      if (this.target.equals(this.parsedList.get(i).get(index))) {
        toReturn.add(this.parsedList.get(i));
      }
    }

    return toReturn;
  }
}
