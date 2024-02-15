package edu.brown.cs.student.main.server.backend.data;

/**
 * Record to hold a certain broadband percentage for a given area
 * @param percentage
 */
public record BroadBandAccessPercent(Double percentage){

    public BroadBandAccessPercent{
        if(!isValidPercentage(percentage)){
            throw new IllegalArgumentException("Invalid percentage");
        }
    }
    public static boolean isValidPercentage(Double percentage){
        return percentage <= 100 && percentage >= 0;
    }
}

