package edu.brown.cs.student.main.server.backend.parser;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class CSVParser <T> {
    /**
     * This is the CSV Parser class.
     */
    BufferedReader reader;
    Boolean headers;
    CreatorFromRow<T> c;
    static final Pattern regexSplitCSVRow = Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");

  public CSVParser(Reader reader, CreatorFromRow<T> c) throws IOException {
      this.reader = new BufferedReader(reader);
      this.c = c;
  } //take in Reader instead of FileReader
    /**
     * This method parses given data and returns a list of objects of a given type.
     */
  public List<T> parse() throws IOException, FactoryFailureException {
      List<T> returnList = new ArrayList<>();
      String line = this.reader.readLine();

      while (line != null) {
          List<String> row = Arrays.asList(regexSplitCSVRow.split(line));
          T object = c.create(row);
          returnList.add(object);
          line = this.reader.readLine();
      }
      return returnList;
  }

//    private loadCSV{
//        //load csv file if one is located at specific path
//        //requests must have filepath query parameter
//        //response must include filepath field
//        //share status of file loading with search and view
//    }
//
//    private viewCSV{
//        //sends back entire CSV file's conentnts as a json 2d array
//    }
//
//    private searchCSV{
//        //send back rows mathcing the given search criteria
//        //requests muts allow all search paramters needed to implement CSV
//    }
//
//    private broadband{
//        //sends back broadband data from ACS
//        //requests must have state & county query parameter
//        //response must include correspondng fields for all paramters in request recieved
//    }

}





