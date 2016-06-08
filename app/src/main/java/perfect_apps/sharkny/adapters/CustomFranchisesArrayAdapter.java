package perfect_apps.sharkny.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import perfect_apps.sharkny.R;
import perfect_apps.sharkny.models.BubleItem;
import perfect_apps.sharkny.models.FranchisesModel;

/**
 * Created by mostafa on 07/11/15.
 */

public class CustomFranchisesArrayAdapter extends ArrayAdapter<FranchisesModel> {
    private LayoutInflater mInflater;
    protected List<FranchisesModel> mDataSet;
    protected Context mContext;
    Holder holder;



    public CustomFranchisesArrayAdapter(Context context, List<FranchisesModel> users) {
        super(context, R.layout.forcast_item, users);
        this.mContext = context;
        this.mDataSet = users;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            // Inflate the view since it does not exist
            convertView = mInflater.inflate(R.layout.forcast_item, parent, false);

            // Create and save off the holder in the tag so we get quick access to inner fields
            // This must be done for performance reasons
            holder = new Holder();
            holder.titelView = (TextView) convertView.findViewById(R.id.title);
            holder.descriptionView = (TextView) convertView.findViewById(R.id.description);
            holder.imageView = (ImageView) convertView.findViewById(R.id.mainImage);
            holder.certifiedImage = (ImageView) convertView.findViewById(R.id.certifiedImage);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final FranchisesModel bubleItem = mDataSet.get(position);

        // Populate the title text
        holder.titelView.setText(bubleItem.getTitle());

        // populate description
        holder.descriptionView.setText(bubleItem.getDescription());

        // populate mainImage
        Glide.with(mContext)
                .load(bubleItem.getImage())
                .placeholder(R.drawable.placeholder)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);

        // populate certificated image
        if(Integer.parseInt(bubleItem.getIs_verified()) == 0)
            holder.certifiedImage.setVisibility(View.GONE);

        return convertView;
    }

    /** View holder for the views we need access to */
    private static class Holder {
        public TextView titelView;
        public TextView descriptionView;
        public ImageView imageView;
        public ImageView certifiedImage;
    }
}