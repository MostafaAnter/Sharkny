package perfect_apps.sharkny.dialog;


import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import perfect_apps.sharkny.R;


/**
 * Created by mostafa on 11/06/16.
 */
public class ImageViewerDialog extends DialogFragment {

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static ImageViewerDialog newInstance(String imageUrl) {
        ImageViewerDialog f = new ImageViewerDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pick a style based on the num.
        int style , theme ;

           style = DialogFragment.STYLE_NO_TITLE;
//           DialogFragment.STYLE_NO_FRAME;
//           DialogFragment.STYLE_NO_INPUT;
//           DialogFragment.STYLE_NORMAL;
//           DialogFragment.STYLE_NORMAL;
//           DialogFragment.STYLE_NO_TITLE;
//           DialogFragment.STYLE_NO_FRAME;
//           DialogFragment.STYLE_NORMAL;


          theme =  android.R.style.Theme_Holo;
//         android.R.style.Theme_Holo_Light_Dialog; break;
//         android.R.style.Theme_Holo_Light; break;
//         android.R.style.Theme_Holo_Light_Panel; break;
//         android.R.style.Theme_Holo_Light; break;

        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_fragment, container, false);

        ImageView view = (ImageView) v.findViewById(R.id.imageView);

        Glide.with(getActivity())
                .load(getArguments().getString("url"))
                .placeholder(R.drawable.placeholder)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);

        return v;
    }
}