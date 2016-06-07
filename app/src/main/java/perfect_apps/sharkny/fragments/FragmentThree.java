package perfect_apps.sharkny.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.activities.SearchActivity;
import perfect_apps.sharkny.adapters.ForecastViewAdapter;
import perfect_apps.sharkny.models.ForecastView;

/**
 * Created by mostafa on 23/05/16.
 */
public class FragmentThree extends Fragment {

    @Bind(R.id.button21) RadioButton radioButton1;
    @Bind(R.id.button22) RadioButton radioButton2;

    // for recycler view
    private RecyclerView mRecyclerView;
    private ForecastViewAdapter mAdapter;
    private List<ForecastView> mDataset;

    // for swipe to refresh
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public FragmentThree(){

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
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        ButterKnife.bind(this, view);

        // set added recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new ForecastViewAdapter(getActivity(), mDataset, mRecyclerView);
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

    private void changeFont(){
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
        // add some fake data
        ForecastView forecastView = new ForecastView("Bank Masr", "The Worest Bank for ever", 0, true);


        for (int i = 0; i < 3; i++) {
            mDataset.add(i, forecastView);
            mAdapter.notifyItemInserted(i);
        }


        onRefreshComplete();
    }
}
