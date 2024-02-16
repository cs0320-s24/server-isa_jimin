package edu.brown.cs.student.main.server.backend.data;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

import edu.brown.cs.student.main.server.backend.Exceptions.DataSourceException;
import okio.Buffer;

/**
 * The BroadBandDatasource class serves as a data source for retrieving the percentage of
 * households with broadband access for a specific location. It obtains data from the U.S. Census by
 * first retrieving state and county codes, and then fetching and returning the percentage
 * statistic.
 */
public class BroadBand implements BroadbandDatasource {

    private final HashMap<String, String> stateCodeMap;

    public BroadBand() throws DataSourceException {
        this.stateCodeMap = this.resolveStateCode();
    }

    /**
     * Retrieves the percentage of households with broadband access in a specified state and county
     * from the Census API.
     *
     * @param location representing the state and county
     * @return an object containing the fetched percentage
     */
    @Override
    public BroadBandAccessPercent getBroadbandPercent(Location location) throws DataSourceException {
        return getBroadbandPercent(location.getState(), location.getCounty());
    }

    /**
     * Retrieves the percentage of households with broadband access in a specified state and county
     * from the Census API.
     *
     * @param stateName the name of the desired state
     * @param countyName the name of the desired county
     * @return an object containing the fetched percentage
     * @throws DataSourceException if any errors occur during data retrieval
     */
    public BroadBandAccessPercent getBroadbandPercent(String stateName, String countyName)
            throws DataSourceException {

        // Check for errors with the state and county names before determining them using helpers.
        int stateCode;
        if (stateName == null || !(this.stateCodeMap.containsKey(stateName)))
            throw new DataSourceException("Invalid state name: " + stateName);
        try {
            stateCode = Integer.parseInt(this.stateCodeMap.get(stateName));
        } catch (NumberFormatException e) {
            throw new DataSourceException("Invalid state code for state: " + stateName);
        }
        int countyCode = Integer.parseInt(this.resolveCountyCode(stateCode, stateName, countyName));

        try {
            // Build and connect to URL, feeding in above codes
            URL requestURL =
                    new URL(
                            "https",
                            "api.census.gov",
                            "/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_001E&for=county:"
                                    + countyCode
                                    + "&in=state:"
                                    + stateCode);
            HttpURLConnection clientConnection = connect(requestURL);

            // Build Moshi and adapter
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<List<List<String>>> adapter =
                    moshi.adapter(Types.newParameterizedType(List.class, List.class, String.class));
            List<List<String>> data =
                    adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

            // Disconnect from the URL
            clientConnection.disconnect();

            // Null and negative-checking
            if (data == null || data.size() > 2 || data.get(1) == null || data.get(1).get(1) == null)
                throw new DataSourceException("Malformed response from NWS");

            return new BroadBandAccessPercent(Double.parseDouble(data.get(1).get(1)));

        } catch (IOException e) {
            throw new DataSourceException(e.getMessage());
        }
    }

    /**
     * Creates and Builds a map of states to state codes by querying the Census API and adapting the
     * provided data array.
     *
     * @return a HashMap mapping state names to state codes
     * @throws DataSourceException if any errors occur during data retrieval
     */
    private HashMap<String, String> resolveStateCode() throws DataSourceException {
        try {
            URL requestURL =
                    new URL("https", "api.census.gov", "/data/2010/dec/sf1?get=NAME&for=state:*");
            HttpURLConnection clientConnection = connect(requestURL);
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<List<List<String>>> adapter =
                    moshi.adapter(Types.newParameterizedType(List.class, List.class, String.class));
            List<List<String>> stateCodeList =
                    adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
            clientConnection.disconnect();
            if (stateCodeList == null) throw new DataSourceException("Malformed response from NWS");

            // Turn List of Lists into HashSet, skipping header row
            HashMap<String, String> stateCodeMap = new HashMap<>();
            for (List<String> stateCode : stateCodeList.subList(1, stateCodeList.size())) {
                stateCodeMap.put(stateCode.get(0), stateCode.get(1));
            }
            return stateCodeMap;

        } catch (IOException | DataSourceException e) {
            throw new DataSourceException(e.getMessage());
        }
    }

    /**
     * Retrieves the code of a desired county by querying the Census API and adapting the provided
     * list of counties within the desired state.
     *
     * @param stateCode the code of the desired state
     * @param stateName the name of the desired state
     * @param countyName the name of the desired county
     * @return the code of the desired county
     * @throws DataSourceException if any errors occur during data retrieval
     */
    private String resolveCountyCode(int stateCode, String stateName, String countyName)
            throws DataSourceException {
        if (countyName == null) throw new DataSourceException("Invalid county name.");
        try {
            URL requestURL =
                    new URL(
                            "https",
                            "api.census.gov",
                            "/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + stateCode);
            HttpURLConnection clientConnection = connect(requestURL);
            Type stringMap = Types.newParameterizedType(List.class, List.class, String.class);
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<List<List<String>>> adapter = moshi.adapter(stringMap);
            List<List<String>> countyList =
                    adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
            clientConnection.disconnect();
            if (countyList == null)
                throw new DataSourceException(
                        "Received a malformed response from NWS when trying to "
                                + "resolve county code for "
                                + countyName
                                + " in "
                                + stateName);
            for (List<String> countyData : countyList) {
                String query = countyName + ", " + stateName;
                if (countyData.get(0).equals(query)) {
                    return countyData.get(2);
                }
            }
        } catch (IOException e) {
            throw new DataSourceException(e.getMessage());
        }
        throw new DataSourceException(countyName + " was not found in the state you requested.");
    }

    /**
     * The connect method is a helper method that throws an IOException so different callers can
     * handle it differently if needed.
     *
     * @param requestURL a URL to connect to
     * @return the client connection
     * @throws DataSourceException if the client connection response code is not 200
     * @throws IOException if an I/O error occurs during the connection
     */
    private static HttpURLConnection connect(URL requestURL) throws DataSourceException, IOException {
        URLConnection urlConnection = requestURL.openConnection();
        if (!(urlConnection instanceof HttpURLConnection))
            throw new DataSourceException("unexpected: result of connection wasn't HTTP");
        HttpURLConnection clientConnection = (HttpURLConnection) urlConnection;
        clientConnection.connect();
        if (clientConnection.getResponseCode() != 200)
            throw new DataSourceException(
                    "Unexpected API connection status: "
                            + clientConnection.getResponseMessage()
                            + "for URL: "
                            + requestURL);
        return clientConnection;
    }
}
