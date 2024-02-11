package edu.brown.cs.student.main;

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

  public CSVParser(FileReader fileReader, CreatorFromRow<T> c, boolean headers) throws IOException {
      this.reader = new BufferedReader(fileReader);
      this.headers = headers;
      this.c = c;
  } //take in Reader instead of FileReader
    /**
     * This method parses given data and returns a list of objects of a given type.
     */
  public List<T> parse() throws IOException, FactoryFailureException {
      List<T> returnList = new ArrayList<>();
      String line = this.reader.readLine();

      if (this.headers) {
          this.reader.readLine();
      }
      while (line != null) {
          List<String> row = Arrays.asList(regexSplitCSVRow.split(line));
          T object = c.create(row);
          returnList.add(object);
          line = this.reader.readLine();
      }
      return returnList;
  }
}





