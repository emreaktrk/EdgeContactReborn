package emreaktrk.edgecontact.logger;


import android.util.Log;

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

    public static void w(String message) {
        Log.w(TAG, message);
    }
}