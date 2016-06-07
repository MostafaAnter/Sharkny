package perfect_apps.sharkny.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.activities.DetailActivity;
import perfect_apps.sharkny.listener.OnLoadMoreListener;
import perfect_apps.sharkny.models.ForecastView;
import perfect_apps.sharkny.utils.Utils;

/**
 * Created by mostafa on 15/04/16.
 */
public class ForecastViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = "CustomAdapter";
    private static Context mContext;
    private List<ForecastView> mDataSet;
    // manage enter animate
    private static final int ANIMATED_ITEMS_COUNT = 2; // number of item that animated is 1
    private int lastAnimatedPosition = -1;


    // for load more
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;


    public ForecastViewAdapter(Context mContext, List<ForecastView> mDataSet ,RecyclerView mRecyclerView) {
        this.mDataSet = mDataSet;
        this.mContext = mContext;

        // for load more data
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }
    // for load more
    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }
    // for load more
    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoaded() {
        isLoading = false;
    }

    /**
     * Provide a reference to the type of views (custom ViewHolder)
     */
    public static class BubleItemHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title) TextView titelView;
        @Bind(R.id.description) TextView descriptionView;
        @Bind(R.id.mainImage) ImageView imageView;
        @Bind(R.id.certifiedImage) ImageView certifiedImage;

        public BubleItemHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getPosition() + " clicked.");
                   Context context = v.getContext();
                   Intent intent = new Intent(context, DetailActivity.class);
//                    Bundle arguments = new Bundle();
//                    arguments.putParcelable(DetailsFragment.ARG_ITEM_ID, mDataSet.get(getPosition()));
//                    intent.putExtras(arguments);
                   context.startActivity(intent);
                   ((FragmentActivity)context).overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);
                }
            });
        }

        public TextView getTitelView() {
            return titelView;
        }

        public TextView getDescriptionView() {
            return descriptionView;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public ImageView getCertifiedImage() {
            return certifiedImage;
        }

    }

    /**
     * for loadMore option
     */
    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }

        public ProgressBar getProgressBar() {
            return progressBar;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            // Create a new view.
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.forcast_item, viewGroup, false);
            return new BubleItemHolder(v);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_loading_item, viewGroup, false);
            return new LoadingViewHolder(v);
        }
      return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof BubleItemHolder) {
            // cast view holder
            BubleItemHolder bubleItemHolder = (BubleItemHolder) viewHolder;



            Log.d(TAG, "Element " + position + " set.");
            // active animation when Enter
            runEnterAnimation(viewHolder.itemView, position);

            // set title if title not null
            if (mDataSet.get(position).getTitle() != null) {
                bubleItemHolder.getTitelView().setText(mDataSet.get(position).getTitle());
            }

            // set description if description not null
            if (mDataSet.get(position).getDescription() != null) {
                bubleItemHolder.getDescriptionView().setText(mDataSet.get(position).getDescription());
            }
        } else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) viewHolder;
            loadingViewHolder.getProgressBar().setIndeterminate(true);
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    // manage enter animation function
    private void runEnterAnimation(View view, int position) {
        if (position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(Utils.getScreenHeight(mContext));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }

}
