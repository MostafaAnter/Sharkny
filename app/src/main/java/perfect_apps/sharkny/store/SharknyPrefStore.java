package perfect_apps.sharkny.store;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mostafa on 26/04/16.
 */
public class SharknyPrefStore {
    private static final String PREFKEY = "MAKPreferencesStore";
    private SharedPreferences sharknyPreferences;

    public SharknyPrefStore(Context context){
        sharknyPreferences = context.getSharedPreferences(PREFKEY, Context.MODE_PRIVATE);
    }

    public void addPreference(String key, String value){
        SharedPreferences.Editor editor = sharknyPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void addPreference(String key, int value){
        SharedPreferences.Editor editor = sharknyPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void removePreference(String key){
        SharedPreferences.Editor editor = sharknyPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public String getPreferenceValue(String key){
        return sharknyPreferences.getString(key, "");
    }

    public int getIntPreferenceValue(String key){
        return sharknyPreferences.getInt(key, 0);
    }

}
