package perfect_apps.sharkny.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.activities.HomeActivity;
import perfect_apps.sharkny.activities.MyAccountActivity;

/**
 * Created by mostafa on 23/05/16.
 */
public class FragmentOne extends Fragment {
    @Bind(R.id.button1) Button button1;
    @Bind(R.id.button2) Button button2;
    @Bind(R.id.button3) Button button3;
    @Bind(R.id.button4) Button button4;
    @Bind(R.id.button5) Button button5;
    @Bind(R.id.button6) Button button6;

    public FragmentOne(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        ButterKnife.bind(this, view);

        // hide search view


        changeTypeFace();
        setButtonsClick();
        return view;
    }

    private void changeTypeFace(){
        Typeface sharknyFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/thin.ttf");
        button1.setTypeface(sharknyFont);
        button2.setTypeface(sharknyFont);
        button3.setTypeface(sharknyFont);
        button4.setTypeface(sharknyFont);
        button5.setTypeface(sharknyFont);
        button6.setTypeface(sharknyFont);
    }

    private void setButtonsClick(){
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).bottomNavigationView.selectTab(1);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).bottomNavigationView.selectTab(1);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).bottomNavigationView.selectTab(2);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).bottomNavigationView.selectTab(1);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).bottomNavigationView.selectTab(3);
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyAccountActivity.class));
                getActivity().overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
            }
        });
    }
}
