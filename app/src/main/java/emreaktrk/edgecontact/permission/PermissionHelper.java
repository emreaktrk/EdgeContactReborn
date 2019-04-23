package emreaktrk.edgecontact.permission;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;

public final class PermissionHelper {

  private static final int PERMISSION_CALL = 320;
  private static final int PERMISSION_CONTACT = 598;
  private static final int PERMISSION_STORAGE = 654;

  public static void call(Activity activity, Callback callback) {
    String[] permissions = {Manifest.permission.CALL_PHONE};

    int result = PermissionChecker.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);
    if (result == PermissionChecker.PERMISSION_DENIED) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(
          activity, Manifest.permission.CALL_PHONE)) {
        callback.onRationale();
        return;
      }

      ActivityCompat.requestPermissions(activity, permissions, PERMISSION_CALL);
      return;
    }

    callback.onGranted();
  }

  public static void contact(Activity activity, Callback callback) {
    String[] permissions = {Manifest.permission.READ_CONTACTS};

    int result = PermissionChecker.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
    if (result == PermissionChecker.PERMISSION_DENIED) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(
          activity, Manifest.permission.READ_CONTACTS)) {
        callback.onRationale();
        return;
      }

      ActivityCompat.requestPermissions(activity, permissions, PERMISSION_CONTACT);
      return;
    }

    callback.onGranted();
  }

  public static void storage(Activity activity, Callback callback) {
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

    int result =
        PermissionChecker.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
    if (result == PermissionChecker.PERMISSION_DENIED) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(
          activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
        callback.onRationale();
        return;
      }

      ActivityCompat.requestPermissions(activity, permissions, PERMISSION_STORAGE);
      return;
    }

    callback.onGranted();
  }

  public interface Callback {
    void onRationale();

    void onGranted();
  }
}
