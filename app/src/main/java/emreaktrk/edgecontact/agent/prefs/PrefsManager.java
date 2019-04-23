package emreaktrk.edgecontact.agent.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public final class PrefsManager implements IPrefManager {

  private static final String NAME = "EDGE_PREF";

  private static PrefsManager sInstance;
  private final SharedPreferences mPreferences;

  private PrefsManager(Context context) {
    mPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
  }

  public static PrefsManager getInstance(Context context) {
    if (sInstance == null) {
      sInstance = new PrefsManager(context);
    }

    return sInstance;
  }

  @Override
  public SharedPreferences.Editor save(String key, Object object) {
    if (object instanceof String) {
      return mPreferences.edit().putString(key, (String) object);
    }

    if (object instanceof Boolean) {
      return mPreferences.edit().putBoolean(key, (boolean) object);
    }

    throw new IllegalArgumentException("Undefined object type passed.");
  }

  @Override
  public <T> T read(String key, @NonNull Class<T> type) {
    if (type.equals(String.class)) {
      mPreferences.getString(key, null);
    }

    if (type.equals(Integer.class)) {
      mPreferences.getInt(key, -1);
    }

    if (type.equals(Boolean.class)) {
      mPreferences.getBoolean(key, false);
    }

    throw new IllegalArgumentException("Undefined object type passed.");
  }

  public boolean isFirst() {
    return mPreferences.getBoolean(PrefsManager.Keys.IS_FIRST, true);
  }

  public void setFirst(boolean value) {
    save(Keys.IS_FIRST, value).apply();
  }

  private static class Keys {
    private static final String IS_FIRST = "is_first";
  }
}
