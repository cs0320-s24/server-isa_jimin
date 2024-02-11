package edu.brown.cs.student.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/** The Main class of our project. This is where execution begins. */
public final class Main {
  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static <T> void main(String[] args) throws IOException {
    //String fileName = "C:\\Users\\isade\\OneDrive\\Desktop\\cs0320\\csv-IDelionado\\data\\census\\postsecondary_education.csv";
    String fileName = "outside.csv";
    FileReader fileReader = new FileReader(fileName);
    BufferedReader reader = new BufferedReader(fileReader);
    Create creator = new Create();
    Boolean headers = true;

    CSVParser csvParser = new CSVParser(fileReader, creator, headers);
    Search search = new Search<>(csvParser, reader);

    Scanner s = new Scanner(System.in);
    System.out.print("Enter a keyword to begin your search: ");
    String keyword = s.next();
    search.search(keyword);
  }
}

