package perfect_apps.sharkny.parse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import perfect_apps.sharkny.models.BubleItem;
import perfect_apps.sharkny.models.Countries;
import perfect_apps.sharkny.models.OwnerUser;

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

    public static List<BubleItem> parseBublesItem(String feed){

        try {
            JSONObject  jsonRootObject = new JSONObject(feed);//done
            JSONArray jsonNewsArray = jsonRootObject.optJSONArray("data");
            List<BubleItem> newsList = new ArrayList<>();
            for (int i = 0; i < jsonNewsArray.length(); i++) {
                JSONObject jsonObject = jsonNewsArray.getJSONObject(i);

                String id = jsonObject.optString("id");
                String title = jsonObject.optString("title");
                String description = jsonObject.optString("description");
                String start_date = jsonObject.optString("start_date");
                String end_date = jsonObject.optString("end_date");
                String investment_value = jsonObject.optString("investment_value");
                String investment_percentag = jsonObject.optString("investment_percentag");
                String guarantees = jsonObject.optString("guarantees");
                String send_to_mobile = jsonObject.optString("send_to_mobile");
                String project_type = jsonObject.optString("project_type");
                String project_field = jsonObject.optString("project_field");
                String country = jsonObject.optString("country");
                String image = jsonObject.optString("image") ;
                String likes_count = jsonObject.optString("likes_count");
                String comments_count = jsonObject.optString("comments_count");
                String general_type = jsonObject.optString("general_type");
                String is_verified = jsonObject.optString("is_verified");
                String created_by = jsonObject.optString("created_by");

                JSONObject ownerObject = jsonObject.optJSONObject("createdby");
                String idOwner = ownerObject.optString("id") ;
                String username = ownerObject.optString("username");
                String fullname = ownerObject.optString("fullname");
                String job = ownerObject.optString("job");
                String address = ownerObject.optString("address");
                String mobile = ownerObject.optString("mobile");
                String email = ownerObject.optString("email");
                String imageOwner = ownerObject.optString("image");
                String gender = ownerObject.optString("gender");
                String nationality = ownerObject.optString("nationality");
                String countryOwner = ownerObject.optString("countryOwner");

                OwnerUser ownerUser = new OwnerUser(idOwner, username, fullname, job, address, mobile, email, imageOwner, gender, nationality, countryOwner);
                BubleItem bubleItem = new BubleItem(id, title, description,start_date, end_date,
                        investment_value,investment_percentag,guarantees,
                        send_to_mobile, project_type,project_field, country,
                        image,likes_count,comments_count, general_type, is_verified, created_by,ownerUser);
                newsList.add(bubleItem);
            }
            return newsList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

}
