package edu.brown.cs.student.main.server.backend.data;

public record Location(String state, String county) {

    /**
     * gets the state name
     * @return the state name
     */
    public String getState() {
        return state;
    }

    /**
     * gets the county name
     * @return county name
     */
    public String getCounty() {
        return county;
    }

    /**
     * getStateCounty returns the state and county names
     * @return state and county names
     */
    public String getStateCounty() {
        return "state=" + state + "&county=" + county;
    }
}
