package perfect_apps.sharkny.parse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import perfect_apps.sharkny.models.Countries;

/**
 * Created by mostafa on 06/06/16.
 */
public class JsonParser {

    public static List<Countries> parseNationalitiesFeed(String feed){

        try {
            JSONArray jsonNewsArray = new JSONArray(feed);
            List<Countries> newsList = new ArrayList<>();
            newsList.add(new Countries("0", "Nationality"));
            for (int i = 0; i < jsonNewsArray.length(); i++) {
                JSONObject jsonObject = jsonNewsArray.getJSONObject(i);

                // retrieve all metadata
                String id = jsonObject.optString("id");
                String title = jsonObject.optString("title");

                // put all item inside one object
                Countries country = new Countries(id, title);
                newsList.add(country);
            }
            return newsList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static List<Countries> parseCountriesFeed(String feed){

        try {
            JSONArray jsonNewsArray = new JSONArray(feed);
            List<Countries> newsList = new ArrayList<>();
            newsList.add(new Countries("0", "Country"));
            for (int i = 0; i < jsonNewsArray.length(); i++) {
                JSONObject jsonObject = jsonNewsArray.getJSONObject(i);

                // retrieve all metadata
                String id = jsonObject.optString("id");
                String title = jsonObject.optString("title");

                // put all item inside one object
                Countries country = new Countries(id, title);
                newsList.add(country);
            }
            return newsList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

}
