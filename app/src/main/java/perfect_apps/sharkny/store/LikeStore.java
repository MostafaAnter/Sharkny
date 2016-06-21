package perfect_apps.sharkny.store;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import perfect_apps.sharkny.models.FavoriteModel;


/**
 * Created by mostafa on 22/03/16.
 */
public class LikeStore {
    private static final String PREFKEY = "likes";
    private SharedPreferences favoritePrefs;

    public LikeStore(Context context) {
        favoritePrefs = context.getSharedPreferences(PREFKEY, Context.MODE_PRIVATE);
    }

    public List<FavoriteModel> findAll() {

        Map<String, ?> notesMap = favoritePrefs.getAll();

        SortedSet<String> keys = new TreeSet<String>(notesMap.keySet());

        List<FavoriteModel> noteList = new ArrayList<>();
        for (String key : keys) {
            FavoriteModel note = new FavoriteModel();
            note.setTitleKey(key);
            note.setIdValue((String) notesMap.get(key));
            noteList.add(note);
        }

        return noteList;
    }

    public boolean findItem(String value, String key){
        return value.equalsIgnoreCase(favoritePrefs.getString(key, ""));
    }

    public boolean update(FavoriteModel note) {

        SharedPreferences.Editor editor = favoritePrefs.edit();
        editor.putString(note.getTitleKey(), note.getIdValue());
        editor.commit();
        return true;
    }

    public boolean remove(String key) {

        if (favoritePrefs.contains(key)) {
            SharedPreferences.Editor editor = favoritePrefs.edit();
            editor.remove(key);
            editor.commit();
        }

        return true;
    }
}
