package perfect_apps.sharkny.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import perfect_apps.sharkny.R;

/**
 * Created by mostafa on 23/05/16.
 */
public class FragmentFout extends Fragment {
    public FragmentFout(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_four, container, false);

        return view;
    }
}
