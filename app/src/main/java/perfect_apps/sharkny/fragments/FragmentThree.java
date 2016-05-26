package perfect_apps.sharkny.fragments;

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
import perfect_apps.sharkny.R;

/**
 * Created by mostafa on 23/05/16.
 */
public class FragmentThree extends Fragment {

    @Bind(R.id.button21) RadioButton radioButton1;
    @Bind(R.id.button22) RadioButton radioButton2;

    public FragmentThree(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        ButterKnife.bind(this, view);

        changeFont();

        return view;
    }

    private void changeFont(){
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/thin.ttf");

        radioButton1.setTypeface(font);
        radioButton2.setTypeface(font);
    }
}
