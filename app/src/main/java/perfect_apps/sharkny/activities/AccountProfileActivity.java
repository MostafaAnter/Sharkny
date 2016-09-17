package perfect_apps.sharkny.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.app.AppController;
import perfect_apps.sharkny.models.Countries;
import perfect_apps.sharkny.parse.JsonParser;
import perfect_apps.sharkny.store.SharknyPrefStore;
import perfect_apps.sharkny.utils.AppHelper;
import perfect_apps.sharkny.utils.Constants;
import perfect_apps.sharkny.utils.Utils;
import perfect_apps.sharkny.utils.VolleyMultipartRequest;

public class AccountProfileActivity extends LocalizationActivity {
    @Bind(R.id.select_profile_pic)
    LinearLayout selectProfilePic;
    @Bind(R.id.user_avatar)
    ImageView circleImageView;

    // for spinners
    @Bind(R.id.spinner1)
    Spinner spinner1;
    @Bind(R.id.spinner2)
    Spinner spinner2;

    // edit text
    @Bind(R.id.editText1)
    EditText userName;
    @Bind(R.id.editText4)
    EditText fullName;
    @Bind(R.id.editText5)
    EditText email;
    @Bind(R.id.editText6)
    EditText mobile;
    @Bind(R.id.editText7)
    EditText job;
    @Bind(R.id.editText8)
    EditText age;
    @Bind(R.id.editText9)
    EditText address;
    // radio button
    @Bind(R.id.button21)
    RadioButton radioButtonMale;

    @Bind(R.id.button22)
    RadioButton radioButtonFemale;

    private static int genderType;
    private static Uri profileImagePath;

    private static String nationality = "";
    private static String country = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_profile);
        setToolbar();
        ButterKnife.bind(this);
        setOnLinearSelected();
        fetchCounAnNationalityData();
        viewProfile();


        radioButtonMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    genderType = 1;

                }
            }
        });

        radioButtonFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    genderType = 2;
                }
            }
        });

    }


    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigate);
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
        if (getLanguage().equalsIgnoreCase("en")) {
            sharknyFont = Typeface.createFromAsset(getAssets(), "fonts/daisy.ttf");
        } else {
            sharknyFont = Typeface.createFromAsset(getAssets(), "fonts/daisy.ttf");
        }
        mTitle.setTypeface(sharknyFont);


    }

    // set on back button pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(AccountProfileActivity.this, MyAccountActivity.class)
//                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
        finish();
    }

    // for pick photo
    private void setOnLinearSelected() {
        selectProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setShowGif(false)
                        .setPreviewEnabled(false)
                        .start(AccountProfileActivity.this, PhotoPicker.REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    private void setSelectedPhotoInsideCircleShap(Uri uri) {
        Glide.with(this)
                .load(uri)
                .centerCrop()
                .thumbnail(0.1f)
                .placeholder(R.drawable.__picker_ic_photo_black_48dp)
                .error(R.drawable.__picker_ic_broken_image_black_48dp)
                .into(circleImageView);
    }

    private void populateSpinner1(List<String> mlist) {

        // you will just change R.array.search & spinner1 reference :)

        //final List<String> plantsList = Arrays.asList(getResources().getStringArray(R.array.national));
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, mlist) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner1.setAdapter(spinnerArrayAdapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    nationality = selectedItemText;
                    Toast.makeText
                            (getApplicationContext(), selectedItemText + " Done!", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void populateSpinner2(List<String> mList) {

        // you will just change R.array.search & spinner1 reference :)

        // final List<String> plantsList = Arrays.asList(getResources().getStringArray(R.array.search3));
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, mList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner2.setAdapter(spinnerArrayAdapter);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    country = selectedItemText;
                    Toast.makeText
                            (getApplicationContext(), selectedItemText + "Done!", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    // fetch country and nationality
    private void fetchCounAnNationalityData() {
        String url;
        if (getLanguage().equalsIgnoreCase("en")) {
            url = "http://sharkny.net/en/api/countries/";
        } else {
            url = "http://sharkny.net/en/api/countries/index";
        }


        final Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");
                // handle data, like converting it to xml, json, bitmap etc.,
                List<String> nationalityList = new ArrayList<>();
                for (Countries countriy : JsonParser.parseNationalitiesFeed(data, AccountProfileActivity.this)) {
                    nationalityList.add(countriy.getTitle());
                }
                populateSpinner1(nationalityList);
                if (country != null || !country.trim().isEmpty())
                    selectValue(spinner1, nationality);

                // populate second spinner
                List<String> countryList = new ArrayList<>();
                for (Countries countriy : JsonParser.parseCountriesFeed(data, AccountProfileActivity.this)) {
                    countryList.add(countriy.getTitle());
                }
                populateSpinner2(countryList);
                if (country != null || !country.trim().isEmpty())
                    selectValue(spinner2, country);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            // Cached response doesn't exists. Make network call here
            if (Utils.isOnline(AccountProfileActivity.this)) {
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
                        for (Countries countriy : JsonParser.parseNationalitiesFeed(response, AccountProfileActivity.this)) {
                            nationalityList.add(countriy.getTitle());
                        }
                        populateSpinner1(nationalityList);
                        if (country != null || !country.trim().isEmpty())
                            selectValue(spinner1, nationality);
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


                //fetch countries
                String tag_contries_req = "string_req";
                String countriesurl;
                if (getLanguage().equalsIgnoreCase("en")) {
                    countriesurl = "http://sharkny.net/en/api/countries/";
                } else {
                    countriesurl = "http://sharkny.net/en/api/countries/index";
                }
                StringRequest strCountriesReq = new StringRequest(Request.Method.GET,
                        countriesurl, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        List<String> countryList = new ArrayList<>();
                        for (Countries countriy : JsonParser.parseCountriesFeed(response, AccountProfileActivity.this)) {
                            countryList.add(countriy.getTitle());
                        }
                        populateSpinner2(countryList);
                        if (country != null || !country.trim().isEmpty())
                            selectValue(spinner2, country);
                        pDialog.dismissWithAnimation();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
                    }
                });
                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strCountriesReq, tag_contries_req);

            } else {
                // show error message
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Please check your Network connection!")
                        .show();
            }
        }


    }

    // fetch view profile
    private void viewProfile() {
        int id = new SharknyPrefStore(this).getIntPreferenceValue(Constants.PREFERENCE_USER_AUTHENTICATION_STATE);
        String url = BuildConfig.View_Profile + id;
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");
                // handle data, like converting it to xml, json, bitmap etc.,
                parseFeed(data);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            // Cached response doesn't exists. Make network call here
            if (Utils.isOnline(AccountProfileActivity.this)) {
                final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText(getResources().getString(R.string.wait));
                pDialog.setCancelable(false);
                pDialog.show();

                StringRequest strReq = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();
                        parseFeed(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
                    }
                });

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq);
            } else {
                // show error message
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Please check your Network connection!")
                        .show();
            }

        }
    }

    private void parseFeed(String strJson) {

        JSONObject jsonRootObject = null;
        try {
            jsonRootObject = new JSONObject(strJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assert jsonRootObject != null;
        int id = Integer.parseInt(jsonRootObject.optString("id"));
        userName.setText(jsonRootObject.optString("username"));
        fullName.setText(jsonRootObject.optString("fullname"));
        job.setText(jsonRootObject.optString("job"));
        address.setText(jsonRootObject.optString("address"));
        mobile.setText(jsonRootObject.optString("mobile"));
        email.setText(jsonRootObject.optString("email"));
        if (jsonRootObject.optString("gender").equalsIgnoreCase("male"))
            genderType = 1;
        if (jsonRootObject.optString("gender").equalsIgnoreCase("female"))
            genderType = 2;
        nationality = jsonRootObject.optString("nationality");
        country = jsonRootObject.optString("country");

        Glide.with(AccountProfileActivity.this).load(jsonRootObject.optString("image"))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.profile)
                .into(circleImageView);

        if (genderType == 1) {
            radioButtonMale.setChecked(true);
        } else {
            radioButtonFemale.setChecked(true);
        }

        selectValue(spinner1, nationality);
        selectValue(spinner2, country);


    }

    private void selectValue(Spinner spinner, String value) {
        for (int i = 1; i < spinner.getCount(); i++) {
            if (((String) spinner.getItemAtPosition(i)).equalsIgnoreCase(value)) {
                spinner.setSelection(i-1);
                break;
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


    private boolean attempRegister() {
        if (!fullName.getText().toString().trim().isEmpty()
                && !email.getText().toString().trim().isEmpty()) {
            return true;

        } else {
            // show error message
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please complete data!")
                    .show();
            return false;
        }
    }


    public void updateData(View view) {
        // check on required data
        if (attempRegister()) {
            if (Utils.isOnline(AccountProfileActivity.this)) {

                // make request
                final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText(getResources().getString(R.string.wait));
                pDialog.setCancelable(false);
                pDialog.show();

                // post data
                // Tag used to cancel the request
                int id = new SharknyPrefStore(this).getIntPreferenceValue(Constants.PREFERENCE_USER_AUTHENTICATION_STATE);
                String tag_string_req = "string_req";
                String url = BuildConfig.Update_Profile + id;
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
                       // params.put("password", password.getText().toString());
                        params.put("email", email.getText().toString());
                        params.put("fullname", fullName.getText().toString());
                        params.put("nationality", String.valueOf(returnIndex(spinner1, nationality)));
                        params.put("country", String.valueOf(returnIndex(spinner2, country)));
                        params.put("mobile", mobile.getText().toString());
                        params.put("job", job.getText().toString());
                        params.put("age", age.getText().toString());
                        params.put("address", address.getText().toString());
                        params.put("gender", String.valueOf(genderType));
                        return params;
                    }

                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        // file name could found file base or direct access from real path
                        // for now just get bitmap data from ImageView
                        if (profileImagePath != null) {
                            params.put("image", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(AccountProfileActivity.this, profileImagePath), "image/jpeg"));
                        } else {
                            circleImageView.buildDrawingCache();
                            Bitmap bmap = circleImageView.getDrawingCache();
                            params.put("image", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromImage(AccountProfileActivity.this, bmap), "image/jpeg"));
                        }
                        return params;
                    }
                };

                AppController.getInstance().addToRequestQueue(multipartRequest);
                // last of request


            } else {
                // show error message
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Please check your Network connection!")
                        .show();
            }
        }
    }

    private void parseFeed2(String strJson) {

        JSONObject jsonRootObject = null;
        try {
            jsonRootObject = new JSONObject(strJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonRootObject.optString("id") != null || !jsonRootObject.optString("id").trim().isEmpty()) {
            int id = Integer.parseInt(jsonRootObject.optString("id").toString());
            String profileImage = jsonRootObject.optString("image").toString();

            //debug
            Log.d("user_id", "" + id);

            // store user id in authenticated state & pic url
            new SharknyPrefStore(this).addPreference(Constants.PREFERENCE_USER_AUTHENTICATION_STATE, id);
            new SharknyPrefStore(this).addPreference(Constants.PREFERENCE_USER_IMAGE_URL, profileImage);
            // show success dialog and go to home
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Good job!")
                    .setContentText("Your profile successfully update!")
                    .setConfirmText("Done!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startActivity(new Intent(AccountProfileActivity.this, HomeActivity.class)
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
