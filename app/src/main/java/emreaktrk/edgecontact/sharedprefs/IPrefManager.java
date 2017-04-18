package emreaktrk.edgecontact.sharedprefs;


import android.content.SharedPreferences;
import android.support.annotation.NonNull;

interface IPrefManager {

    SharedPreferences.Editor save(String key, Object object);

    <T> T read(String key, @NonNull Class<T> type);
}
