package edu.brown.cs.student.main;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
public class Search <T>{
    /**
     * This is the search class.
     */
    private HashMap<String, HashSet<Integer>> lineNumbers;
    private String keyword;
     CSVParser<T> parser;
    BufferedReader reader;

    public Search(CSVParser<T> parser, BufferedReader reader){
        this.parser = parser;
        this.reader = reader;
    }
    /**
     * This method takes in a keyword that the user inputs and goes through the file to return all the lines
     * the keyword appears.
     */
        public void search(String keyword){
        this.keyword = keyword;
        try {
            this.lineNumbers = new HashMap<String, HashSet<Integer>>();
            String line = this.reader.readLine();
            int counter = 0;

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
                if(line.contains(keyword)){
                    System.out.println("Result for " + keyword + " : " + lineNumbers);
                }
                line = this.reader.readLine();
            }
            this.parser.reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}

