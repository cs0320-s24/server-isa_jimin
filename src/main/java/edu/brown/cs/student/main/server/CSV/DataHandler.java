package edu.brown.cs.student.main.server.CSV;

import edu.brown.cs.student.main.server.backend.parser.*;
import org.eclipse.jetty.server.RequestLogCollection;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class DataHandler {
    private String filePath;
    private HashMap<String, HashSet<Integer>> lineNumbers;
    private String query;
    private BufferedReader reader;

    CSVParser parser;
    private List<List<String>> returnList;

    private LinkedList list;

    /**
     * DataHandler constructor which takes in a filepath to be accessed
     * by other classes that have an instance of DataHandler
     *
     * @param filePath filepath that the user inserts into the query
     */
    public DataHandler(String filePath){
        this.filePath = filePath;
    }

    /**
     * Setter method to set the filepath when the user inserts it into the query
     * @param filePath that is set by the user
     */
    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    /**
     * Gets the file path so other classes can utilize it
     * @return the filepath
     */
    public String getFilePath(){
        return this.filePath;
    }

    /**
     * Parse method we used so that the search and view classes have access to parse,
     * and we don't need to have to create an instance of the parser every time we
     * would like to parse.
     *
     * @param filepath filepath of document to parse
     */
    public List parseCsv(String filepath) throws IOException {
       try(FileReader fileReader = new FileReader(filepath)){
           //Boolean headers = true;
           CSVParser parser = new CSVParser(fileReader, new Create());
           System.out.println("File Parsed");
            return parser.parse();
       } catch (FactoryFailureException e) {
           System.err.println("Error Parsing CSV");
           throw new RuntimeException(e);
       }
    }

    public List<List<String>> searchColName(String query, String name, boolean hasHeaders) {
        //currently not dealing with headers boolean
        //what is the name parameter
        try {
            this.lineNumbers = new HashMap<String, HashSet<Integer>>();
            String line = this.reader.readLine();
            int counter = 0;

            List<List<String>> returnList = new LinkedList<List<String>>();
            List<String> list = new LinkedList<>();
            while ((line != null)) {
                counter++;
                String[] word1 = line.split(" ");

                for (String word : word1) {
                    if (!this.lineNumbers.containsKey(word)) {
                        HashSet<Integer> value = new HashSet<>();
                        value.add(counter);
                        this.lineNumbers.put(word, value);
                    } else {
                        HashSet<Integer> newValue = this.lineNumbers.get(word);
                        newValue.add(counter);
                        this.lineNumbers.put(word, newValue);
                    }
                }
                if(line.contains(query)){
                   // System.out.println("Result for " + query + " : " + lineNumbers);
                    this.list.add(line);
                    this.returnList.add(list);
                }
                line = this.reader.readLine();
            }
            this.parser.reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return returnList;
    }

    public List<List<String>> searchColIndex(String query, Integer index, boolean hasHeaders) {

    }

    public List<List<String>> searchNoHeader(String query, boolean hasHeaders) {
        try{
            this.lineNumbers = new HashMap<String, HashSet<Integer>>();
            String line = this.reader.readLine();
            int counter = 0;

            List<List<String>> returnList = new LinkedList<List<String>>();
            List<String> list = new LinkedList<>();
            while ((line != null)) {
                counter++;
                String[] word1 = line.split(" ");

                for (String word : word1) {
                    if (!this.lineNumbers.containsKey(word)) {
                        HashSet<Integer> value = new HashSet<>();
                        value.add(counter);
                        this.lineNumbers.put(word, value);
                    } else {
                        HashSet<Integer> newValue = this.lineNumbers.get(word);
                        newValue.add(counter);
                        this.lineNumbers.put(word, newValue);
                    }
                }
                if(line.contains(query)){
                    // System.out.println("Result for " + query + " : " + lineNumbers);
                    this.list.add(line);
                    this.returnList.add(list);
                }
                line = this.reader.readLine();
            }
            this.parser.reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return returnList;
    }
}
