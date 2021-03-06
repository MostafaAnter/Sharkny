package perfect_apps.sharkny.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.activities.DetailActivity;
import perfect_apps.sharkny.activities.MessageDetailActivity;
import perfect_apps.sharkny.models.ForecastView;
import perfect_apps.sharkny.models.MessageModel;
import perfect_apps.sharkny.utils.Utils;

/**
 * Created by mostafa on 15/04/16.
 */
public class MessageViewAdapter extends RecyclerView.Adapter<MessageViewAdapter.ViewHolder>{
    private static final String TAG = "CustomAdapter";
    private static Context mContext;
    private static List<MessageModel> mDataSet;

    // manage enter animate
    private static final int ANIMATED_ITEMS_COUNT = 2; // number of item that animated is 1
    private int lastAnimatedPosition = -1;


    public MessageViewAdapter(Context mContext, List<MessageModel> mDataSet) {
        this.mDataSet = mDataSet;
        this.mContext = mContext;
    }

    /**
     * Provide a reference to the type of views (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name) TextView userName;
        @Bind(R.id.message) TextView messageBody;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getPosition() + " clicked.");

                    Context context = v.getContext();
                    Intent intent = new Intent(context, MessageDetailActivity.class);
                    Bundle arguments = new Bundle();
                    arguments.putParcelable("message", mDataSet.get(getPosition()));
                    intent.putExtras(arguments);
                    context.startActivity(intent);
                    ((FragmentActivity)context).overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);
                }
            });
        }

        public TextView getUserName() {
            return userName;
        }

        public TextView getMessageBody() {
            return messageBody;
        }

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.sent_message_row, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");
        // active animation when Enter
        runEnterAnimation(viewHolder.itemView, position);

        // set title if title not null
        if (mDataSet.get(position).getUserName() != null) {
            viewHolder.getUserName().setText(mDataSet.get(position).getUserName());
        }

        // set description if description not null
        if (mDataSet.get(position).getMessageBody() != null) {
            viewHolder.getMessageBody().setText(mDataSet.get(position).getMessageBody());
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
