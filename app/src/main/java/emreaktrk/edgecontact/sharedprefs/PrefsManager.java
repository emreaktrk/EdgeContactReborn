package emreaktrk.edgecontact.sharedprefs;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public final class PrefsManager implements IPrefManager {

    private static final String NAME = "EDGE_PREF";

    private final SharedPreferences mPreferences;
    private PrefsManager sInstance;

    private PrefsManager(Context context) {
        mPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public PrefsManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PrefsManager(context);
        }

        return sInstance;
    }

    @Override public SharedPreferences.Editor save(String key, Object object) {
        if (object instanceof String) {
            return mPreferences
                    .edit()
                    .putString(key, (String) object);
        }

        throw new IllegalArgumentException("Undefined object type passed.");
    }

    @Override public <T> T read(String key, @NonNull Class<T> type) {
        if (type
                .getClass()
                .equals(String.class)) {
            mPreferences.getString(key, null);
        }

        if (type
                .getClass()
                .equals(Integer.class)) {
            mPreferences.getInt(key, -1);
        }

        throw new IllegalArgumentException("Undefined object type passed.");
    }
}