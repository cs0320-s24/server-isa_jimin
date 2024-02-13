package edu.brown.cs.student.main.server.backend;

/**
 * This is a class that models an Activity received from the BoredAPI. It doesn't have a lot but
 * there are a few fields that you could filter on if you wanted!
 */
public class Census {
  private String activity;
  private String type;
  private int participants;
  private double price;
  private double accessibility;

  public Census() {}

  @Override
  public String toString() {
    return this.activity + " with " + this.participants + " people.";
  }
}
