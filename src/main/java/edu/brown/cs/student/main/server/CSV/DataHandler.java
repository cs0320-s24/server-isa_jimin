package edu.brown.cs.student.main.server.CSV;

import edu.brown.cs.student.main.server.backend.parser.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class DataHandler {
    private String filePath;

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
            CSVParser parser = new CSVParser(fileReader, new Create());
           System.out.println("File Parsed");
            return parser.parse();
       } catch (FactoryFailureException e) {
           System.err.println("Error Parsing CSV");
           throw new RuntimeException(e);
       }
    }

    public List<List<String>> searchColName(String query, String name) {

    }

    public List<List<String>> searchColIndex(String query, Integer index) {

    }

    public List<List<String>> searchNoHeader(String query, boolean hasHeaders) {

    }
}
