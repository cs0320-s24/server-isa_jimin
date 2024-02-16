package edu.brown.cs.student.main.server.backend.data;

/**
 * Record to hold a certain broadband percentage for a given area
 *
 * @param percentage
 */
public record BroadBandAccessPercent(Double percentage) {

  /**
   * BroadBandAccessPercent checks if the percentage is a valid value, else it will through an
   * IllegalArgumentException
   * @param percentage the percentage
   */
  public BroadBandAccessPercent {
    if (!(percentage <= 100 && percentage >= 0)) {
      throw new IllegalArgumentException("Invalid percentage");
    }
  }

  public double getPercentage(){
    return percentage;
  }
}


