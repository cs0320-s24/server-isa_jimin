package edu.brown.cs.student.main.server.backend.census;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CensusAPIUtilities {
  private CensusAPIUtilities(){}

  /**
   * Creates a list of census data from a JSON.
   *
   * @param jsonList
   * @return
   * @throws IOException
   */
  public static List<Census> deserializeData(String jsonList) throws IOException {
    List<Census> censusList = new ArrayList<>();
    try {
      // Initializes Moshi
      Moshi moshi = new Moshi.Builder().build();

      Type listType = Types.newParameterizedType(List.class, Census.class);
      JsonAdapter<List<Census>> adapter = moshi.adapter(listType);

      List<Census> deserializedList = adapter.fromJson(jsonList);
      return deserializedList;
    }
    // Returns an empty activity... Probably not the best handling of this error case...
    // Notice an alternative error throwing case to the one done in OrderHandler. This catches
    // the error instead of pushing it up.
    catch (IOException e) {
      // In a real system, we wouldn't println like this, but it's useful for demonstration:
      System.err.println("OrderHandler: string wasn't valid JSON.");
      throw e;
    } catch (JsonDataException e) {
      // In a real system, we wouldn't println like this, but it's useful for demonstration:
      System.err.println("OrderHandler: JSON wasn't in the right format.");
      throw e;
    }
  }
}
