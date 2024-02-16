package edu.brown.cs.student.main.server.backend.parser;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class CSVParser<T> {
  /** This is the CSV Parser class. */
  private BufferedReader reader;

  private CreatorFromRow<T> c;
  private List<String> headerList;
  static final Pattern regexSplitCSVRow =
      Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");

  public CSVParser(Reader reader, CreatorFromRow<T> c) throws IOException {
    this.reader = new BufferedReader(reader);
    this.c = c;
  } // take in Reader instead of FileReader
  /** This method parses given data and returns a list of objects of a given type. */
  public List<T> parse() throws IOException, FactoryFailureException {
    List<T> returnList = new ArrayList<>();
    String line = this.reader.readLine();
    System.out.println("CSV Parser parse call");

    while (line != null) {
      List<String> row = Arrays.asList(regexSplitCSVRow.split(line));
      T object = c.create(row);
      returnList.add(object);
      line = this.reader.readLine();
    }
    System.out.println(returnList);
    return returnList;
  }

  public Reader getReader() {
    return this.reader;
  }

  public List<String> getHeaderList() {
    return this.headerList;
  }

}
