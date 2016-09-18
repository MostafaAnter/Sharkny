package perfect_apps.sharkny.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;
import perfect_apps.sharkny.BuildConfig;
import perfect_apps.sharkny.Manifest;
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.app.AppController;
import perfect_apps.sharkny.models.Countries;
import perfect_apps.sharkny.parse.JsonParser;
import perfect_apps.sharkny.store.SharknyPrefStore;
import perfect_apps.sharkny.utils.AppHelper;
import perfect_apps.sharkny.utils.Constants;
import perfect_apps.sharkny.utils.Utils;
import perfect_apps.sharkny.utils.VolleyMultipartRequest;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class AddProjectActivity extends LocalizationActivity {

    @Bind(R.id.select_profile_pic)
    LinearLayout selectProfilePic;
    @Bind(R.id.user_avatar)
    ImageView circleImageView;

    // for spinners
    @Bind(R.id.spinner3) Spinner spinner3;
    @Bind(R.id.spinner4) Spinner spinner4;
    @Bind(R.id.spinner1) Button spinner1;
    @Bind(R.id.spinner2) Button spinner2;
    @Bind(R.id.spinner5) Spinner spinner5;

    // editText
    @Bind(R.id.editText1) EditText title;
    @Bind(R.id.editText2) EditText describtion;
    @Bind(R.id.editText3) EditText grantue;
    @Bind(R.id.editText4) EditText investValue;
    @Bind(R.id.editText5) EditText investPrec;


    private static String type;
    private static String field;
    private static String startDate;
    private static String country;
    private static String endDate;
    private static Uri profileImagePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        ButterKnife.bind(this);
        setToolbar();
        setOnLinearSelected();
        fetchFranchisField();
        fetchprojectsType();
        fetchcountry();

    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
                finish();
            }
        });

        /*
        * hide title
        * */
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        //toolbar.setLogo(R.drawable.ic_toolbar);

        /*
        * change font of title
        * */
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface sharknyFont;
        if (getLanguage().equalsIgnoreCase("en")){
            sharknyFont = Typeface.createFromAsset(getAssets(), "fonts/daisy.ttf");
        }else {
            sharknyFont = Typeface.createFromAsset(getAssets(), "fonts/daisy.ttf");
        }
        mTitle.setTypeface(sharknyFont);


    }

    // set on back button pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
        finish();
    }

    // for pick photo
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void setOnLinearSelected(){
        selectProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setShowGif(false)
                        .setPreviewEnabled(false)
                        .start(AddProjectActivity.this, PhotoPicker.REQUEST_CODE);
            }
        });
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                Uri uri = Uri.fromFile(new File(photos.get(0)));
                profileImagePath = uri;
                setSelectedPhotoInsideCircleShap(uri);
            }
        }
    }

    private void setSelectedPhotoInsideCircleShap(Uri uri){
        Glide.with(this)
                .load(uri)
                .centerCrop()
                .thumbnail(0.1f)
                .placeholder(R.drawable.__picker_ic_photo_black_48dp)
                .error(R.drawable.__picker_ic_broken_image_black_48dp)
                .into(circleImageView);
    }

    private void populateSpinner3(List<String> mlist){

        // you will just change R.array.search & spinner1 reference :)

        //final List<String> plantsList = Arrays.asList(getResources().getStringArray(R.array.national));
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item, mlist){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner3.setAdapter(spinnerArrayAdapter);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // hide keyboard
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    type = selectedItemText;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populateSpinner4(List<String> mlist){

        // you will just change R.array.search & spinner1 reference :)

        //final List<String> plantsList = Arrays.asList(getResources().getStringArray(R.array.national));
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item, mlist){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner4.setAdapter(spinnerArrayAdapter);

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // hide keyboard
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    field = selectedItemText;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populateSpinner5(List<String> mlist){

        // you will just change R.array.search & spinner1 reference :)

        //final List<String> plantsList = Arrays.asList(getResources().getStringArray(R.array.national));
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item, mlist){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner5.setAdapter(spinnerArrayAdapter);

        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // hide keyboard
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    country = position +"";
                    if (position != 1){
                        if (position < 42){
                            country = ++position + "";
                        }else {
                            country = position + 2 +"";
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void pickStartDate(View view) {
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(AddProjectActivity.this, new DatePickerPopWin.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                //Toast.makeText(AddProjectActivity.this, dateDesc, Toast.LENGTH_SHORT).show();
                startDate = dateDesc;
                spinner1.setText(dateDesc);
            }
        }).textConfirm("CONFIRM") //text of confirm button
                .textCancel("CANCEL") //text of cancel button
                .btnTextSize(16) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                .minYear(1990) //min year in loop
                .maxYear(2550) // max year in loop
                .dateChose("2016-06-01") // date chose when init popwindow
                .build();
        pickerPopWin.showPopWin(AddProjectActivity.this);
    }

    public void pickEndDate(View view) {
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(AddProjectActivity.this, new DatePickerPopWin.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
               // Toast.makeText(AddProjectActivity.this, dateDesc, Toast.LENGTH_SHORT).show();
                endDate = dateDesc;
                spinner2.setText(dateDesc);
            }
        }).textConfirm("CONFIRM") //text of confirm button
                .textCancel("CANCEL") //text of cancel button
                .btnTextSize(16) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                .minYear(1990) //min year in loop
                .maxYear(2550) // max year in loop
                .dateChose("2016-06-01") // date chose when init popwindow
                .build();
        pickerPopWin.showPopWin(AddProjectActivity.this);
    }

    private void fetchFranchisField(){

        String url;
        if (getLanguage().equalsIgnoreCase("en")) {
            url = "http://sharkny.net/en/api/fields";
        } else {
            url = "http://sharkny.net/ar/api/fields";
        }


        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");
                // handle data, like converting it to xml, json, bitmap etc.,
                List<String> franchisType = new ArrayList<>();
                for (Countries countriy : JsonParser.parseField(data,  AddProjectActivity.this)) {
                    franchisType.add(countriy.getTitle());
                }
                populateSpinner4(franchisType);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            // Cached response doesn't exists. Make network call here
            if (Utils.isOnline(AddProjectActivity.this)) {
                final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText(getResources().getString(R.string.wait));
                pDialog.setCancelable(false);
                pDialog.show();
                String tag_string_req = "string_req";

                StringRequest strReq = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        List<String> nationalityList = new ArrayList<>();
                        for (Countries countriy : JsonParser.parseField(response, AddProjectActivity.this)) {
                            nationalityList.add(countriy.getTitle());
                        }
                        populateSpinner4(nationalityList);
                        pDialog.dismissWithAnimation();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
                    }
                });
                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

            } else {
            }
        }


    }
    private void fetchcountry() {
        String url;
        if (getLanguage().equalsIgnoreCase("en")) {
            url = "http://sharkny.net/en/api/countries/";
        } else {
            url = "http://sharkny.net/en/api/countries/index";
        }


        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");
                // handle data, like converting it to xml, json, bitmap etc.,
                List<String> nationalityList = new ArrayList<>();
                for (Countries countriy : JsonParser.parseCountriesFeed(data, AddProjectActivity.this)) {
                    nationalityList.add(countriy.getTitle());
                }
                populateSpinner5(nationalityList);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            // Cached response doesn't exists. Make network call here
            if (Utils.isOnline(AddProjectActivity.this)) {
                String tag_string_req = "string_req";

                StringRequest strReq = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        List<String> nationalityList = new ArrayList<>();
                        for (Countries countriy : JsonParser.parseCountriesFeed(response, AddProjectActivity.this)) {
                            nationalityList.add(countriy.getTitle());
                        }
                        populateSpinner5(nationalityList);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            } else {
            }
        }


    }

    private void fetchprojectsType(){

        String url;
        if (getLanguage().equalsIgnoreCase("en")) {
            url = "http://sharkny.net/en/api/project-types";
        } else {
            url = "http://sharkny.net/ar/api/project-types";
        }


        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");
                // handle data, like converting it to xml, json, bitmap etc.,
                List<String> franchisType = new ArrayList<>();
                for (Countries countriy : JsonParser.parseProjectTypes(data, AddProjectActivity.this)) {
                    franchisType.add(countriy.getTitle());
                }
                populateSpinner3(franchisType);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            // Cached response doesn't exists. Make network call here
            if (Utils.isOnline(AddProjectActivity.this)) {
                String tag_string_req = "string_req";

                StringRequest strReq = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        List<String> nationalityList = new ArrayList<>();
                        for (Countries countriy : JsonParser.parseProjectTypes(response, AddProjectActivity.this)) {
                            nationalityList.add(countriy.getTitle());
                        }
                        populateSpinner3(nationalityList);


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

            } else {
            }
        }


    }

    private boolean attempAdd(){
        String regex = "[0-9]+";

        if (!investValue.getText().toString().trim().matches(regex)||
                investPrec.getText().toString().trim().matches(regex)){
            // show error message
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Enter Numbers only")
                    .show();
            return false;

        }


        if (!title.getText().toString().trim().isEmpty()
                && !describtion.getText().toString().trim().isEmpty()
                && !startDate.trim().isEmpty()
                && !type.trim().isEmpty()
                && !field.trim().isEmpty()
                && !investValue.getText().toString().trim().isEmpty()
                && !country.trim().isEmpty()
                && !investPrec.getText().toString().trim().isEmpty()){
            return true;

        }else {
            // show error message
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please complete data!")
                    .show();
            return false;
        }
    }


    public void addProject(View view) {
        // check on required data
        if (attempAdd()) {
            if (Utils.isOnline(AddProjectActivity.this)) {

                // make request
                final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText(getResources().getString(R.string.wait));
                pDialog.setCancelable(false);
                pDialog.show();

                // post data
                // Tag used to cancel the request
                final int id = new SharknyPrefStore(this).getIntPreferenceValue(Constants.PREFERENCE_USER_AUTHENTICATION_STATE);
                String tag_string_req = "string_req";
                String url = BuildConfig.CreateProject;
                // begin of request
                VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        pDialog.dismissWithAnimation();
                        String resultResponse = new String(response.data);
                        try {
                            JSONObject result = new JSONObject(resultResponse);
                            /*String status = result.getString("status");
                            String message = result.getString("message");*/

                            Log.d("response", resultResponse);
                            parseFeed2(resultResponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();

                        // show error message
                        new SweetAlertDialog(AddProjectActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("some thing went wrong try again!")
                                .show();

                        NetworkResponse networkResponse = error.networkResponse;
                        String errorMessage = "Unknown error";
                        if (networkResponse == null) {
                            if (error.getClass().equals(TimeoutError.class)) {
                                errorMessage = "Request timeout";
                            } else if (error.getClass().equals(NoConnectionError.class)) {
                                errorMessage = "Failed to connect server";
                            }
                        } else {
                            String result = new String(networkResponse.data);
                            try {
                                JSONObject response = new JSONObject(result);
                                String status = response.getString("status");
                                String message = response.getString("message");

                                Log.e("Error Status", status);
                                Log.e("Error Message", message);

                                if (networkResponse.statusCode == 404) {
                                    errorMessage = "Resource not found";
                                } else if (networkResponse.statusCode == 401) {
                                    errorMessage = message + " Please login again";
                                } else if (networkResponse.statusCode == 400) {
                                    errorMessage = message + " Check your inputs";
                                } else if (networkResponse.statusCode == 500) {
                                    errorMessage = message + " Something is getting wrong";
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("Error", errorMessage);
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
//                        params.put("username", userName.getText().toString());
                        params.put("title", title.getText().toString());
                        params.put("description", describtion.getText().toString());
                        params.put("start_date", startDate);
                        params.put("end_date", endDate);
                        params.put("guarantees", grantue.getText().toString());
                        params.put("project_type", String.valueOf(returnIndex(spinner3, type)));
                        params.put("project_field", String.valueOf(returnIndex(spinner4, field)));
                        params.put("investment_value", investValue.getText().toString());
                        params.put("country", country);
                        params.put("investment_percentage", investPrec.getText().toString());
                        params.put("created_by", String.valueOf(id));
                        return params;
                    }

                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        // file name could found file base or direct access from real path
                        // for now just get bitmap data from ImageView
                        if (profileImagePath != null) {
                            params.put("image", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(AddProjectActivity.this, profileImagePath), "image/jpeg"));
                        } else {
                            circleImageView.buildDrawingCache();
                            Bitmap bmap = circleImageView.getDrawingCache();
                            params.put("image", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromImage(AddProjectActivity.this, bmap), "image/jpeg"));
                        }
                        return params;
                    }
                };

                AppController.getInstance().addToRequestQueue(multipartRequest);
                // last of request



            }else {
                // show error message
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Please check your Network connection!")
                        .show();
            }
        }
    }

    private int returnIndex(Spinner spinner, String value){
        for (int i = 1; i < spinner.getCount(); i++) {
            if (((String) spinner.getItemAtPosition(i)).equalsIgnoreCase(value))
                return i;
        }
        return 0;
    }

    private void parseFeed2(String strJson) {

        JSONObject jsonRootObject = null;
        try {
            jsonRootObject = new JSONObject(strJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonRootObject.optBoolean("success") == true ) {
            // show success dialog and go to home
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Good job!")
                    .setContentText("Your profile successfully update!")
                    .setConfirmText("Done!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startActivity(new Intent(AddProjectActivity.this, HomeActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
                        }
                    })
                    .show();

        } else {
            // show error message
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("some thing went wrong try again!")
                    .show();
        }
    }
}
