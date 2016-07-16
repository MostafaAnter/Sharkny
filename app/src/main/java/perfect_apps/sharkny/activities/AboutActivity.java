package perfect_apps.sharkny.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.app.AppController;
import perfect_apps.sharkny.store.SharknyPrefStore;
import perfect_apps.sharkny.utils.Constants;
import perfect_apps.sharkny.utils.Utils;

public class AboutActivity extends LocalizationActivity {
    private static final String TAG = "AboutActivity";
    private static ProgressBar progressBar;


    @Bind(R.id.title)TextView title;
    @Bind(R.id.content)TextView content;
    @Bind(R.id.image)ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setToolbar();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        if (Utils.isOnline(this)) {
            String  tag_string_req = "string_req";

            String url = "http://sharkny.net/en/api/pages/about";
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response.toString());
                    progressBar.setVisibility(View.GONE);
                    parseResponse(response);

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    progressBar.setVisibility(View.GONE);
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        } else {
            // show error message
            progressBar.setVisibility(View.GONE);
            new SweetAlertDialog(AboutActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please check your Network connection!")
                    .show();
        }


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
        if (getLanguage().equalsIgnoreCase("en")) {
            sharknyFont = Typeface.createFromAsset(getAssets(), "fonts/daisy.ttf");
        } else {
            sharknyFont = Typeface.createFromAsset(getAssets(), "fonts/daisy.ttf");
        }
        mTitle.setTypeface(sharknyFont);

    }

    private void parseResponse(String feed){
        String imageUrl = "http://sharkny.net/";
        try {
            JSONObject rootObject = new JSONObject(feed);
            imageUrl  = imageUrl +  rootObject.optString("image");
            if (getLanguage().equalsIgnoreCase("en")){
                title.setText(rootObject.optString("title_en"));
                content.setText(Html.fromHtml(rootObject.optString("desc_en")).toString().replaceAll("</p>|</br>", ""));
            }else {
                title.setText(rootObject.optString("title_ar"));
                content.setText(Html.fromHtml(rootObject.optString("desc_ar")).toString().replaceAll("</p>|</br>", ""));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



        Glide.with(AboutActivity.this).load(imageUrl)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);

    }

}
