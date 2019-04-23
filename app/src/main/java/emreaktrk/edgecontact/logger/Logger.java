package emreaktrk.edgecontact.logger;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public final class Logger {

  private static final String TAG = "EDGE";

  public static void i(String message) {
    Log.i(TAG, message);
  }

  public static void v(String message) {
    Log.d(TAG, message);
  }

  public static void d(String message) {
    Log.d(TAG, message);
  }

  public static void e(String message) {
    Log.e(TAG, message);
  }

  public static void e(Exception exception) {
    Log.e(TAG, exception.getMessage());
  }

  public static void w(String message) {
    Log.w(TAG, message);
  }

  public static void json(Object object) {
    try {
      JSONObject json = new JSONObject(object.toString());
      i(object.getClass().getSimpleName() + ": " + json.toString(3));
    } catch (JSONException exception) {
      e(object.getClass().getSimpleName() + ".toString() methods returned invalid json");
    }
  }
}
