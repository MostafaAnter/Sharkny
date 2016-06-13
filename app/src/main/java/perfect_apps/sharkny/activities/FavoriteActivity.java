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
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import perfect_apps.sharkny.BuildConfig;
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.adapters.CustomFavoritArrayAdapter;
import perfect_apps.sharkny.app.AppController;
import perfect_apps.sharkny.models.FavoritItem;
import perfect_apps.sharkny.parse.JsonParser;
import perfect_apps.sharkny.store.SharknyPrefStore;
import perfect_apps.sharkny.utils.Constants;
import perfect_apps.sharkny.utils.HorizontalListView;
import perfect_apps.sharkny.utils.Utils;

public class FavoriteActivity extends LocalizationActivity {

    // for recycler view
    private List<FavoritItem> mDataset;
    private HorizontalListView mHlvCustomList;
    private CustomFavoritArrayAdapter adapter;

    // for swipe to refresh
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        setToolbar();

        // populate mDataSet
        mDataset = new ArrayList<>();
        setRecyclerViewAndSwipe();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("swip", "onRefresh called from SwipeRefreshLayout");

                initiateRefresh();
            }
        });

    }

    private void setRecyclerViewAndSwipe(){
        // set added recycler view
        mHlvCustomList = (HorizontalListView) findViewById(R.id.hlvCustomList);
        mHlvCustomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FavoriteActivity.this, DetailViewActivity.class);
                Bundle arguments = new Bundle();
                arguments.putString("id",  ((FavoritItem) mHlvCustomList.getItemAtPosition(position)).getItem_id());
                arguments.putString("type",  ((FavoritItem) mHlvCustomList.getItemAtPosition(position)).getType());
                intent.putExtras(arguments);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);

            }
        });
        // adapter
        adapter = new CustomFavoritArrayAdapter(FavoriteActivity.this, mDataset);
        mHlvCustomList.setAdapter(adapter);

        // Retrieve the SwipeRefreshLayout and ListView instances
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        // Set the color scheme of the SwipeRefreshLayout by providing 4 color resource ids
        //noinspection ResourceAsColor
        mSwipeRefreshLayout.setColorScheme(
                R.color.swipe_color_1, R.color.swipe_color_2,
                R.color.swipe_color_3, R.color.swipe_color_4);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        24,
                        getResources().getDisplayMetrics()));
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavoriteActivity.this, HomeActivity.class)
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

    // set on back button pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FavoriteActivity.this, HomeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
        finish();
    }

    // populate recycler and Swip
    @Override
    protected void onStart() {
        super.onStart();
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }

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

        // Stop the refreshing indicator
        mSwipeRefreshLayout.setRefreshing(false);


    }



    private void makeNewsRequest(){

        if(Utils.isOnline(FavoriteActivity.this)){
            fetchData();
        }else {
            // show error message
            new SweetAlertDialog(FavoriteActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please check your Network connection!")
                    .show();
            fetchData();
        }
    }

    private void fetchData(){
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        int id = new SharknyPrefStore(this).getIntPreferenceValue(Constants.PREFERENCE_USER_AUTHENTICATION_STATE);
        String url = BuildConfig.GetFavoritList + id + "&expand_item=true";

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if(entry != null){
            try {
                String data = new String(entry.data, "UTF-8");
                // handle data, like converting it to xml, json, bitmap etc.,
                mDataset.clear();
                mDataset.addAll(0, JsonParser.parseFavoriteList(data));
                adapter.notifyDataSetChanged();
                onRefreshComplete();


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else{
            // Cached response doesn't exists. Make network call here
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    mDataset.clear();
                    mDataset.addAll(0, JsonParser.parseFavoriteList(response));
                    adapter.notifyDataSetChanged();
                    onRefreshComplete();

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
