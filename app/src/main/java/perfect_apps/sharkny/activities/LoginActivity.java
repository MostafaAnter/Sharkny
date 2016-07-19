package perfect_apps.sharkny.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import perfect_apps.sharkny.BuildConfig;
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.app.AppController;
import perfect_apps.sharkny.store.SharknyPrefStore;
import perfect_apps.sharkny.utils.Constants;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends LocalizationActivity{

    // ui elements
    private String TAG = LoginActivity.class.getSimpleName();
    private EditText inputMail, inputPassword;
    private TextInputLayout inputLayoutMail, inputLayoutPassword;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_parent);

        setToolbar();

        inputLayoutMail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputMail = (EditText) findViewById(R.id.txt_email);
        inputPassword = (EditText) findViewById(R.id.txt_password);

        inputMail.addTextChangedListener(new MyTextWatcher(inputMail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
    }

    public void signIn(View view) {
        login();
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.txt_email:
                    validateEmail();
                    break;
                case R.id.txt_password:
                    validatePassword();
                    break;
            }
        }
    }

    // Validating email
    private boolean validateEmail() {
        String email = inputMail.getText().toString().trim();

        if (email.isEmpty()) {
            inputLayoutMail.setError(getString(R.string.error_invalid_email));
            requestFocus(inputMail);
            return false;
        } else {
            inputLayoutMail.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    // Validating password
    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()
                || inputPassword.getText().toString().length() < 4) {
            inputLayoutPassword.setError(getString(R.string.error_invalid_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    //login
    private void login() {
        if (validateEmail() && validatePassword()) {
            if (isOnline()) {
                requestData();


            } else {
                // show error message
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Please check your Network connection!")
                        .show();
            }

        } else {

        }
    }

    private void requestData() {
        // Set up a progress dialog
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(getResources().getString(R.string.wait));
        pDialog.setCancelable(false);
        pDialog.show();

        // Tag used to cancel the request
        String tag_string_req = "string_req";
        String url = BuildConfig.BASE_URL + BuildConfig.Login_User;

        StringRequest strReq = new StringRequest(Request.Method.POST,
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
                // show error message
                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Incorrect username or password.")
                        .show();
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", inputMail.getText().toString().trim());
                params.put("password", inputPassword.getText().toString().trim());
                return params;

            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    /* parse feed and store id */
    private void parseFeed(String strJson) {

        JSONObject jsonRootObject = null;
        try {
            jsonRootObject = new JSONObject(strJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int id = Integer.parseInt(jsonRootObject.optString("id"));
        String profileImage = jsonRootObject.optString("image");
        String userName = jsonRootObject.optString("username");

        //debug
        Log.d("user_id", "" + id);

        // store user id in authenticated state & pic url
        new SharknyPrefStore(this).addPreference(Constants.PREFERENCE_USER_AUTHENTICATION_STATE, id);
        new SharknyPrefStore(this).addPreference(Constants.PREFERENCE_USER_IMAGE_URL, profileImage);
        new SharknyPrefStore(this).addPreference(Constants.PREFERENCE_USER_NAME, userName);
        // show success dialog and go to home
        startActivity(new Intent(LoginActivity.this, HomeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
    }
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
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



}

