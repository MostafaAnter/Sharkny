package perfect_apps.sharkny.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.models.ForecastView;

/**
 * Created by mostafa on 15/04/16.
 */
public class ForecastViewAdapter extends RecyclerView.Adapter<ForecastViewAdapter.ViewHolder>{
    private static final String TAG = "CustomAdapter";
    private static Context mContext;
    private List<ForecastView> mDataSet;

    /**
     * Initialize the constructor of the Adapter.
     *
     * @param mDataSet String[] containing the data to populate views to be used by RecyclerView.
     * @param mContext Context hold context
     */
    public ForecastViewAdapter(Context mContext, List<ForecastView> mDataSet) {
        this.mDataSet = mDataSet;
        this.mContext = mContext;
    }

    /**
     * Provide a reference to the type of views (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title) TextView titelView;
        @Bind(R.id.description) TextView descriptionView;
        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getPosition() + " clicked.");
//                    Context context = v.getContext();
//                    Intent intent = new Intent(context, DetailsActivity.class);
//                    Bundle arguments = new Bundle();
//                    arguments.putParcelable(DetailsFragment.ARG_ITEM_ID, mDataSet.get(getPosition()));
//                    intent.putExtras(arguments);
//                    context.startActivity(intent);
//                    ((FragmentActivity)context).overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);
                }
            });
        }

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.forcast_item, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
       // viewHolder.getTextView().setText(mDataSet[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
