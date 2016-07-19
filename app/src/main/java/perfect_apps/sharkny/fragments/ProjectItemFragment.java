package perfect_apps.sharkny.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import perfect_apps.sharkny.R;
import perfect_apps.sharkny.activities.DetailActivity;
import perfect_apps.sharkny.activities.FinanceDetailActivity;
import perfect_apps.sharkny.models.BubleItem;
import perfect_apps.sharkny.models.FinanceModel;

/**
 * Created by zeyadgholmish on 6/24/16.
 */
public class ProjectItemFragment extends Fragment {


    TextView titelView;
    TextView descriptionView;
    ImageView imageView;
    ImageView certifiedImage;
    FrameLayout parent_layout;

    BubleItem bubleItem;

    FinanceModel financeModel;

    int type = 0;

    public ProjectItemFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forcast_item2, container,
                false);

        titelView = (TextView) view.findViewById(R.id.title);
        descriptionView = (TextView) view.findViewById(R.id.description);
        imageView = (ImageView) view.findViewById(R.id.mainImage);
        certifiedImage = (ImageView) view.findViewById(R.id.certifiedImage);

        parent_layout = (FrameLayout) view.findViewById(R.id.parent_layout);

        type = getArguments().getInt("type");

        if (type == 0) {

            bubleItem = getArguments().getParcelable(DetailActivity.ARG_ITEM_ID);

            // Populate the title text
            titelView.setText(bubleItem.getTitle());

            // populate description
            descriptionView.setText(Html.fromHtml(bubleItem.getDescription()));

            // populate mainImage
            Glide.with(getActivity())
                    .load(bubleItem.getImage())
                    .placeholder(R.drawable.placeholder)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);

            // populate certificated image
            if (Integer.parseInt(bubleItem.getIs_verified()) == 0) {
                certifiedImage.setVisibility(View.GONE);

            } else {
                certifiedImage.setVisibility(View.VISIBLE);
            }

        } else {
            financeModel = getArguments().getParcelable(FinanceDetailActivity.ARG_ITEM_ID);

            // Populate the title text
            titelView.setText(financeModel.getTitle());

            // populate description
            descriptionView.setText(Html.fromHtml(financeModel.getDescription().replaceAll("<|>|/|p", "")));

            // populate mainImage
            Glide.with(getActivity())
                    .load(financeModel.getImage())
                    .placeholder(R.drawable.placeholder)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);

            // populate certificated image
            if (Integer.parseInt(financeModel.getIs_verified()) == 0) {
                certifiedImage.setVisibility(View.GONE);

            } else {
                certifiedImage.setVisibility(View.VISIBLE);
            }

        }


        parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (type == 0) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(DetailActivity.ARG_ITEM_ID, bubleItem);
                    intent.putExtras(arguments);
                    getActivity().startActivity(intent);
                    (getActivity()).overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);

                } else if (type == 1) {

                    Intent intent = new Intent(getActivity(), FinanceDetailActivity.class);
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(FinanceDetailActivity.ARG_ITEM_ID, financeModel);
                    intent.putExtras(arguments);
                    getActivity().startActivity(intent);
                    (getActivity()).overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);
                }
            }
        });


        return view;

    }

}
