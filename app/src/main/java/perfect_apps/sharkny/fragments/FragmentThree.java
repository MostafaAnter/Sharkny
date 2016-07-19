package perfect_apps.sharkny.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import perfect_apps.sharkny.app.AppController;
import perfect_apps.sharkny.models.FinanceModel;
import perfect_apps.sharkny.parse.JsonParser;
import perfect_apps.sharkny.utils.Utils;

/**
 * Created by zeyadgholmish on 6/24/16.
 */
public class FragmentThree extends Fragment {
    @Bind(R.id.button21)
    RadioButton radioButton1;

    @Bind(R.id.button22)
    RadioButton radioButton2;



    @Bind(R.id.projects_view_pager)
    ViewPager projects_view_pager;

    SampleFragmentPagerAdapter pagerAdapter;

    private int PAGE_COUNT = 0;

    // for recycler view
    private List<FinanceModel> mDataset;
    private List<FinanceModel> mDataOrigine;

    public FragmentThree() {

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
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        ButterKnife.bind(this, view);

        PAGE_COUNT = mDataset.size();

        projects_view_pager.setClipToPadding(false);
        projects_view_pager.setPadding(AppController.getDPasPIXILS(40), 0, AppController.getDPasPIXILS(40), 0);
        projects_view_pager.setPageMargin(AppController.getDPasPIXILS(10));

        pagerAdapter =
                new SampleFragmentPagerAdapter(getChildFragmentManager(), getActivity());

        projects_view_pager.setAdapter(pagerAdapter);


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
                    filterProjectsWithType("1");

                }
            }
        });

        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    filterProjectsWithType("2");

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

        Log.e("reponse", PAGE_COUNT + "");

        pagerAdapter =
                new SampleFragmentPagerAdapter(getChildFragmentManager(), getActivity());

        projects_view_pager.setAdapter(pagerAdapter);
    }

    private void makeNewsRequest() {
        if (Utils.isOnline(getActivity())) {
            fetchData();
        } else {
            // show error message
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please check your Network connection!")
                    .show();
            fetchData();
        }


    }

    private void changeFont() {
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/thin.ttf");

        radioButton1.setTypeface(font);
        radioButton2.setTypeface(font);

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

    private void fetchData() {
        String url = BuildConfig.Get_Finance_List;

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");
                // handle data, like converting it to xml, json, bitmap etc.,
                mDataset.clear();
                mDataOrigine.clear();
                mDataset.addAll(0, JsonParser.parseFinanceList(data));
                mDataOrigine.addAll(0, mDataset);

                PAGE_COUNT = mDataset.size();
                onRefreshComplete();


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            // Cached response doesn't exists. Make network call here
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    mDataset.clear();
                    mDataOrigine.clear();
                    mDataset.addAll(0, JsonParser.parseFinanceList(response));
                    mDataOrigine.addAll(0, mDataset);

                    PAGE_COUNT = mDataset.size();
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

    private void filterProjectsWithType(String type) {

        mDataset.clear();

        for (FinanceModel FinanceModel : mDataOrigine) {
            if (FinanceModel.getInance_type().equalsIgnoreCase(type)) {
                mDataset.add(FinanceModel);
            }
        }

        PAGE_COUNT = mDataset.size();

        pagerAdapter.notifyDataSetChanged();
    }


    public class SampleFragmentPagerAdapter extends FragmentStatePagerAdapter {


        private Context context;

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {


            ProjectItemFragment fragment = new ProjectItemFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelable(DetailActivity.ARG_ITEM_ID, mDataset.get(position));
            bundle.putInt("type", 1);
            fragment.setArguments(bundle);
            return fragment;
        }


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


    }

}