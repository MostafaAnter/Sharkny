package perfect_apps.sharkny.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import info.hoang8f.android.segmented.SegmentedGroup;
import perfect_apps.sharkny.BuildConfig;
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.activities.HomeActivity;
import perfect_apps.sharkny.activities.SearchActivity;
import perfect_apps.sharkny.adapters.ForecastViewAdapter;
import perfect_apps.sharkny.app.AppController;
import perfect_apps.sharkny.listener.OnLoadMoreListener;
import perfect_apps.sharkny.models.BubleItem;
import perfect_apps.sharkny.models.ForecastView;
import perfect_apps.sharkny.parse.JsonParser;
import perfect_apps.sharkny.utils.Utils;

/**
 * Created by mostafa on 23/05/16.
 */
public class FragmentTwo extends Fragment {

    private static int pageNumber = 5;

    @Bind(R.id.button21) RadioButton radioButton1;
    @Bind(R.id.button22) RadioButton radioButton2;
    @Bind(R.id.button23) RadioButton radioButton3;
    @Bind(R.id.button24) RadioButton radioButton4;

    @Bind(R.id.segmented2) SegmentedGroup segmentedGroup;

    // for recycler view
    private RecyclerView mRecyclerView;
    private ForecastViewAdapter mAdapter;
    private List<BubleItem> mDataset;

    // for swipe to refresh
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public FragmentTwo(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // populate mDataSet
        mDataset = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        ButterKnife.bind(this, view);

        // set added recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new ForecastViewAdapter(getActivity(), mDataset);
        mRecyclerView.setAdapter(mAdapter);

        // Retrieve the SwipeRefreshLayout and ListView instances
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
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


        changeFont();
        return view;
    }

    // called immediately after onViewCreate
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("swip", "onRefresh called from SwipeRefreshLayout");

                initiateRefresh();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    // remove all item from RecyclerView
    private void clearDataSet() {
        if (mDataset != null){
            mDataset.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    private void makeNewsRequest(){
        clearDataSet();
        if(Utils.isOnline(getActivity())){
            fetchData();
        }else {
            // show error message
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please check your Network connection!")
                    .show();
        }

    }

    private void changeFont(){
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/thin.ttf");

        radioButton1.setTypeface(font);
        radioButton2.setTypeface(font);
        radioButton3.setTypeface(font);
        radioButton4.setTypeface(font);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            startActivity(new Intent(getActivity(), SearchActivity.class));
            getActivity().overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchData(){
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        // load first 5 item
        String url = BuildConfig.Get_Projects + pageNumber;

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if(entry != null){
            try {
                String data = new String(entry.data, "UTF-8");
                // handle data, like converting it to xml, json, bitmap etc.,
                onRefreshComplete();
                List<BubleItem> mList = JsonParser.parseBublesItem(data);
                for (int i = 0; i < mList.size(); i++) {
                    mDataset.add(mList.get(i));
                    mAdapter.notifyDataSetChanged();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else{
        // Cached response doesn't exists. Make network call here
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    onRefreshComplete();
                    List<BubleItem> mList = JsonParser.parseBublesItem(response);
                    for (int i = 0; i < mList.size(); i++) {
                        mDataset.add(mList.get(i));
                        mAdapter.notifyDataSetChanged();
                    }

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
