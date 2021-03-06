package perfect_apps.sharkny.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.adapters.CustomArrayAdapter;
import perfect_apps.sharkny.app.AppController;
import perfect_apps.sharkny.models.BubleItem;
import perfect_apps.sharkny.parse.JsonParser;
import perfect_apps.sharkny.store.SharknyPrefStore;
import perfect_apps.sharkny.utils.Constants;
import perfect_apps.sharkny.utils.HorizontalListView;
import perfect_apps.sharkny.utils.Utils;

public class SearchRessultPFinanActivity extends LocalizationActivity {

    private static String type;
    private static String country;
    private static String title;
    private static int flag;

    // for recycler view
    private List<BubleItem> mDataset;
    private HorizontalListView mHlvCustomList;
    private CustomArrayAdapter adapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ressult_pfinan);
        setToolbar();

        type = getIntent().getStringExtra("type");
        country = getIntent().getStringExtra("country");
        title = getIntent().getStringExtra("title");
        try {
            title = URLEncoder.encode(title, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        flag = getIntent().getIntExtra("flag", 0);
        // populate mDataSet
        mDataset = new ArrayList<>();
        setRecyclerViewAndSwipe();



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

    // populate recycler and Swipe
    private void setRecyclerViewAndSwipe(){
        // set added recycler view

        // set added recycler view
        mHlvCustomList = (HorizontalListView) findViewById(R.id.hlvCustomList);
        mHlvCustomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchRessultPFinanActivity.this, DetailViewActivity.class);
                Bundle arguments = new Bundle();
                arguments.putString("id",  ((BubleItem) mHlvCustomList.getItemAtPosition(position)).getId());
                arguments.putString("type",  ((BubleItem) mHlvCustomList.getItemAtPosition(position)).getGeneral_type());
                intent.putExtras(arguments);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);

            }
        });
        // adapter
        adapter = new CustomArrayAdapter(SearchRessultPFinanActivity.this, mDataset);
        mHlvCustomList.setAdapter(adapter);
        // Retrieve the SwipeRefreshLayout and ListView instances

    }

    @Override
    protected void onStart() {
        super.onStart();


        // Start our refresh background task
        initiateRefresh();
    }

    private void initiateRefresh() {
        /**
         * Execute the background task
         */
        makeNewsRequest();

    }

    private void onRefreshComplete() {



    }

    private void makeNewsRequest(){

        if(Utils.isOnline(SearchRessultPFinanActivity.this)){
            fetchData();
        }else {
            // show error message
            new SweetAlertDialog(SearchRessultPFinanActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please check your Network connection!")
                    .show();
            fetchData();
        }
    }

    private void fetchData(){

        int id = new SharknyPrefStore(this).getIntPreferenceValue(Constants.PREFERENCE_USER_AUTHENTICATION_STATE);
        String url = "";

        if (flag == 1){
            try {
                url = URLDecoder.decode(Utils.searchProjectUrl(title, country, type,""), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else {
            try {
                url = URLDecoder.decode(Utils.searchFinanceUrl(title, country, type,""), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if(entry != null){
            try {
                String data = new String(entry.data, "UTF-8");
                // handle data, like converting it to xml, json, bitmap etc.,
                mDataset.clear();
                mDataset.addAll(0, JsonParser.parseUserSearchp(data));
                adapter.notifyDataSetChanged();
                onRefreshComplete();

                Utils.setListViewHeightBasedOnItems(mHlvCustomList);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else{
            // Cached response doesn't exists. Make network call here
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("response", response);
                    if (response.equalsIgnoreCase("[]")){
                        // show error message
                        new SweetAlertDialog(SearchRessultPFinanActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("sorry")
                                .setContentText("there is no results")
                                .show();

                    }
                    mDataset.clear();
                    mDataset.addAll(0, JsonParser.parseUserSearchp(response));
                    adapter.notifyDataSetChanged();
                    onRefreshComplete();

                    Utils.setListViewHeightBasedOnItems(mHlvCustomList);

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    onRefreshComplete();
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq);
        }
    }

}
