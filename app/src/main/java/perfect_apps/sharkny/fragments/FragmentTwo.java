package perfect_apps.sharkny.fragments;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.hoang8f.android.segmented.SegmentedGroup;
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.activities.HomeActivity;

/**
 * Created by mostafa on 23/05/16.
 */
public class FragmentTwo extends Fragment {

    @Bind(R.id.button21) RadioButton radioButton1;
    @Bind(R.id.button22) RadioButton radioButton2;
    @Bind(R.id.button23) RadioButton radioButton3;
    @Bind(R.id.button24) RadioButton radioButton4;

    @Bind(R.id.segmented2) SegmentedGroup segmentedGroup;

    public FragmentTwo(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        ButterKnife.bind(this, view);


        changeFont();
        return view;
    }

    private void changeFont(){
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/thin.ttf");

        radioButton1.setTypeface(font);
        radioButton2.setTypeface(font);
        radioButton3.setTypeface(font);
        radioButton4.setTypeface(font);
    }
}
