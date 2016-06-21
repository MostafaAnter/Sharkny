package perfect_apps.sharkny.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import perfect_apps.sharkny.activities.DetailActivity;
import perfect_apps.sharkny.activities.SearchActivity;
import perfect_apps.sharkny.adapters.CustomArrayAdapter;
import perfect_apps.sharkny.app.AppController;
import perfect_apps.sharkny.models.BubleItem;
import perfect_apps.sharkny.parse.JsonParser;
import perfect_apps.sharkny.utils.HorizontalListView;
import perfect_apps.sharkny.utils.Utils;

/**
 * Created by mostafa on 23/05/16.
 */
public class FragmentTwo extends Fragment {
    @Bind(R.id.button21) RadioButton radioButton1;
    @Bind(R.id.button22) RadioButton radioButton2;
    @Bind(R.id.button23) RadioButton radioButton3;
    @Bind(R.id.button24) RadioButton radioButton4;

    @Bind(R.id.segmented2) SegmentedGroup segmentedGroup;

    // for recycler view
    private List<BubleItem> mDataset;
    private List<BubleItem> mDataOrigine;
    private HorizontalListView mHlvCustomList;
    private CustomArrayAdapter adapter;

    public FragmentTwo(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // populate mDataSet
        mDataset = new ArrayList<>();
        mDataOrigine = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        ButterKnife.bind(this, view);

        // set added recycler view
        mHlvCustomList = (HorizontalListView) view.findViewById(R.id.hlvCustomList);
        mHlvCustomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle arguments = new Bundle();
                arguments.putParcelable(DetailActivity.ARG_ITEM_ID, (Parcelable) mHlvCustomList.getItemAtPosition(position));
                intent.putExtras(arguments);
                getActivity().startActivity(intent);
                (getActivity()).overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);

            }
        });
        // adapter
        adapter = new CustomArrayAdapter(getActivity(), mDataset);
        mHlvCustomList.setAdapter(adapter);
        changeFont();
        return view;
    }

    // called immediately after onViewCreate
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    filterProjectsWithType("Current Projects");

                }
            }
        });

        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    filterProjectsWithType("New Ideas");

                }
            }
        });

        radioButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    filterProjectsWithType("Deals");

                }
            }
        });

        radioButton4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    filterProjectsWithType("Franchise");

                }
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        if(Utils.isOnline(getActivity())){
            fetchData();
        }else {
            // show error message
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please check your Network connection!")
                    .show();
            fetchData();
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
        String url = BuildConfig.Get_Projects;

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if(entry != null){
            try {
                String data = new String(entry.data, "UTF-8");
                // handle data, like converting it to xml, json, bitmap etc.,
                mDataset.clear();
                mDataOrigine.clear();
                mDataset.addAll(0, JsonParser.parseBublesItem(data));
                mDataOrigine.addAll(0, mDataset);
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
                    mDataOrigine.clear();
                    mDataset.addAll(0, JsonParser.parseBublesItem(response));
                    mDataOrigine.addAll(0, mDataset);
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

    private void filterProjectsWithType(String type){
        mDataset.clear();
        adapter.notifyDataSetChanged();
        for (BubleItem bubleItem : mDataOrigine){
            if (bubleItem.getProject_type().equalsIgnoreCase(type)){
                mDataset.add(bubleItem);
                adapter.notifyDataSetChanged();
            }
        }
    }

}
