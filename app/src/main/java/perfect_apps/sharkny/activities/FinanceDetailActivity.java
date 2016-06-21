package perfect_apps.sharkny.activities;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.app.AppController;
import perfect_apps.sharkny.dialog.ImageViewerDialog;
import perfect_apps.sharkny.models.FavoriteModel;
import perfect_apps.sharkny.models.FinanceModel;
import perfect_apps.sharkny.store.FavoriteStore;
import perfect_apps.sharkny.store.LikeStore;
import perfect_apps.sharkny.store.SharknyPrefStore;
import perfect_apps.sharkny.utils.Constants;

public class FinanceDetailActivity extends LocalizationActivity {
    // belong like button animations
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
    private final Map<ImageView, AnimatorSet> likeAnimations = new HashMap<>();



    @Bind(R.id.mainImage) ImageView mainImageView;
    @Bind(R.id.mainImageOwner) ImageView mainOwnerImageView;
    @Bind(R.id.certifiedImage) ImageView certifiedImage;
    @Bind(R.id.title) TextView titleOfMainImage;
    @Bind(R.id.like_count) TextView likeCount;
    @Bind(R.id.comment_count) TextView commentCount;

    @Bind(R.id.investment_value) TextView investment_value;
    @Bind(R.id.project_field) TextView project_field;
    @Bind(R.id.project_type) TextView project_type;
    @Bind(R.id.investment_percentag) TextView investment_percentag;
    @Bind(R.id.country) TextView country;
    @Bind(R.id.description) TextView description;
    @Bind(R.id.owner_name) TextView owner_name;

    @Bind(R.id.like_btn) ImageView likeBtn;
    @Bind(R.id.favoriteImage) ImageView favoritImage;

    @Bind(R.id.send_message)
    Button sendMessageBtn;




    public static final String ARG_ITEM_ID = "item_id";
    private static FinanceModel bubleItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_detail);
        ButterKnife.bind(this);
        setToolbar();

        bubleItem = (FinanceModel) getIntent().getExtras().get(ARG_ITEM_ID);
        fillData();

        changeLikeFavoriteState();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (! isAuthenticated())
            sendMessageBtn.setVisibility(View.GONE);

    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FinanceDetailActivity.this, MyAccountActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
                finish();
            }
        });
        /*
        * hide title
        * */
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        //toolbar.setLogo(R.drawable.ic_toolbar);

    }

    private void fillData(){

        // populate mainImage
        Glide.with(this)
                .load(bubleItem.getImage())
                .placeholder(R.drawable.placeholder)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mainImageView);

        // populate certified image
        if (Integer.parseInt(bubleItem.getIs_verified()) == 0)
            certifiedImage.setVisibility(View.GONE);

        // populate Owner image
        Glide.with(this)
                .load(bubleItem.getOwnerUser().getImage())
                .placeholder(R.drawable.placeholder)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mainOwnerImageView);

        commentCount.setText(bubleItem.getComments_count());
        likeCount.setText(bubleItem.getLikes_count());
        titleOfMainImage.setText(bubleItem.getTitle());


        if (bubleItem.getInvestment_value().trim().isEmpty()){
            investment_value.setText("__");
        }else {
            investment_value.setText(bubleItem.getInvestment_value());
        }
        project_field.setText(bubleItem.getInvestment_Field());


        if (bubleItem.getInance_type().equalsIgnoreCase("1")){
            project_type.setText(getResources().getString(R.string.personally));
        }else {
            project_type.setText(getResources().getString(R.string.company));
        }


        if (bubleItem.getInvestment_percentage().trim().isEmpty()){
            investment_percentag.setText("__ %");
        }else {
            investment_percentag.setText(bubleItem.getInvestment_percentage() + "%");
        }
        country.setText(bubleItem.getCountry());
        description.setText(bubleItem.getDescription());
        owner_name.setText(bubleItem.getOwnerUser().getFullname());
    }


    public void sendMessage(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", bubleItem.getOwnerUser().getMobile(), null)));
    }

    public void like(View view) {
        if (!new LikeStore(this).findItem(bubleItem.getId(),
                bubleItem.getId())) {
            updateHeartButton(likeBtn, true);
            addItemToLike();
            int likeCountNumber = Integer.parseInt(bubleItem.getLikes_count()) + 1;
            likeCount.setText(String.valueOf(likeCountNumber));
            // call api
            likeIt();
        } else {
            updateHeartButton(likeBtn, true);
            removeItemFromLike();
            int likeCountNumber = Integer.parseInt(bubleItem.getLikes_count()) - 1;
            likeCount.setText(String.valueOf(likeCountNumber));
            // call api
            unLikeIt();
        }
    }


    public void comment(View view) {

        String type;
        String id;
        if (bubleItem.getGeneral_type() == null) {
            type = "";
        } else {
            type = bubleItem.getGeneral_type();
        }
        if (bubleItem.getId() == null) {
            id = "";
        } else {
            id = bubleItem.getId();
        }

        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("id", id);
        startActivity(intent);
        overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);


    }

    public void favorite(View view) {
        if (!new FavoriteStore(this).findItem(bubleItem.getId(),
                bubleItem.getId())) {

            updateStarImage(favoritImage, true);
            addItemToFavo();
            // call api

            favoriteIt();
        } else {
            updateStarImage(favoritImage, true);
            removeItmFromFavorite();
            // call api

            unFavoriteIt();
        }
    }

    private void updateHeartButton(final ImageView holder, boolean animated) {
        if (animated) {
            if (!likeAnimations.containsKey(holder)) {
                AnimatorSet animatorSet = new AnimatorSet();
                likeAnimations.put(holder, animatorSet);

                ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder, "rotation", 0f, 360f);
                rotationAnim.setDuration(300);
                rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

                ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder, "scaleX", 0.2f, 1f);
                bounceAnimX.setDuration(300);
                bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

                ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder, "scaleY", 0.2f, 1f);
                bounceAnimY.setDuration(300);
                bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
                bounceAnimY.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        holder.setImageResource(R.drawable.like_solide);
                    }
                });

                animatorSet.play(rotationAnim);
                animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }
                });

                animatorSet.start();
            }else {
                AnimatorSet animatorSet = new AnimatorSet();
                likeAnimations.remove(holder);

                ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder, "rotation", 0f, 360f);
                rotationAnim.setDuration(300);
                rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

                ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder, "scaleX", 0.2f, 1f);
                bounceAnimX.setDuration(300);
                bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

                ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder, "scaleY", 0.2f, 1f);
                bounceAnimY.setDuration(300);
                bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
                bounceAnimY.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        holder.setImageResource(R.drawable.like_not_solid);
                    }
                });

                animatorSet.play(rotationAnim);
                animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }
                });

                animatorSet.start();

            }
        }
    }
    private void updateStarImage(final ImageView holder, boolean animated) {
        if (animated) {
            if (!likeAnimations.containsKey(holder)) {
                AnimatorSet animatorSet = new AnimatorSet();
                likeAnimations.put(holder, animatorSet);

                ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder, "rotation", 0f, 360f);
                rotationAnim.setDuration(300);
                rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

                ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder, "scaleX", 0.2f, 1f);
                bounceAnimX.setDuration(300);
                bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

                ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder, "scaleY", 0.2f, 1f);
                bounceAnimY.setDuration(300);
                bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
                bounceAnimY.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        holder.setImageResource(R.drawable.favorite_solid);
                    }
                });

                animatorSet.play(rotationAnim);
                animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }
                });

                animatorSet.start();
            }else {
                AnimatorSet animatorSet = new AnimatorSet();
                likeAnimations.remove(holder);

                ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder, "rotation", 0f, 360f);
                rotationAnim.setDuration(300);
                rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

                ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder, "scaleX", 0.2f, 1f);
                bounceAnimX.setDuration(300);
                bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

                ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder, "scaleY", 0.2f, 1f);
                bounceAnimY.setDuration(300);
                bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
                bounceAnimY.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        holder.setImageResource(R.drawable.favorite_not_solid);
                    }
                });

                animatorSet.play(rotationAnim);
                animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }
                });

                animatorSet.start();
            }
        }
    }

    private boolean isAuthenticated(){
        int authenticatedState = new SharknyPrefStore(this).getIntPreferenceValue(Constants.PREFERENCE_USER_AUTHENTICATION_STATE);

        if ((authenticatedState != 0)) {
            return true;
        }
        return false;
    }

    private void addItemToFavo() {
        //add item to favorite
        FavoriteModel item = new FavoriteModel();
        item.setTitleKey(bubleItem.getId());
        item.setIdValue(bubleItem.getId());
        new FavoriteStore(this).update(item);
    }

    private void removeItmFromFavorite(){
        //add item to favorite
        FavoriteModel item = new FavoriteModel();
        item.setTitleKey(bubleItem.getId());
        item.setIdValue(bubleItem.getId());
        new FavoriteStore(this).remove(item);

    }

    private void addItemToLike() {
        //add item to favorite
        FavoriteModel item = new FavoriteModel();
        item.setTitleKey(bubleItem.getId());
        item.setIdValue(bubleItem.getId());
        new LikeStore(this).update(item);
    }

    private void removeItemFromLike(){
        FavoriteModel item = new FavoriteModel();
        item.setTitleKey(bubleItem.getId());
        item.setIdValue(bubleItem.getId());
        new LikeStore(this).remove(item.getTitleKey());

    }

    private void changeLikeFavoriteState(){

        // for favorite
        if (new FavoriteStore(this).findItem(bubleItem.getId(),
                bubleItem.getId())){
            // this item is in my database
            favoritImage.setImageResource(R.drawable.favorite_solid);
            AnimatorSet animatorSet = new AnimatorSet();
            likeAnimations.put(favoritImage, animatorSet);
        }else {
            favoritImage.setImageResource(R.drawable.favorite_not_solid);
        }

        // for like
        if (new LikeStore(this).findItem(bubleItem.getId(),
                bubleItem.getId())){
            // this item is in my database
            likeBtn.setImageResource(R.drawable.like_solide);
            AnimatorSet animatorSet = new AnimatorSet();
            likeAnimations.put(likeBtn, animatorSet);
        }else {
            likeBtn.setImageResource(R.drawable.like_not_solid);
        }
    }

    private void likeIt(){
        final int iduser = new SharknyPrefStore(FinanceDetailActivity.this).getIntPreferenceValue(Constants.PREFERENCE_USER_AUTHENTICATION_STATE);
        // Tag used to cancel the request
        String tag_string_req = "string_req";
        String url = "http://sharkny.net/en/api/likes/likeit";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("response", response);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("item_id", bubleItem.getId());
                params.put("type", bubleItem.getGeneral_type());
                params.put("created_by", String.valueOf(iduser));

                return params;

            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void unLikeIt(){
        final int iduser = new SharknyPrefStore(FinanceDetailActivity.this).getIntPreferenceValue(Constants.PREFERENCE_USER_AUTHENTICATION_STATE);
        // Tag used to cancel the request
        String tag_string_req = "string_req";
        String url = "http://sharkny.net/en/api/likes/undolike";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("response", response);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("item_id", bubleItem.getId());
                params.put("type", bubleItem.getGeneral_type());
                params.put("created_by", String.valueOf(iduser));

                return params;

            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void favoriteIt(){
        final int iduser = new SharknyPrefStore(FinanceDetailActivity.this).getIntPreferenceValue(Constants.PREFERENCE_USER_AUTHENTICATION_STATE);
        // Tag used to cancel the request
        String tag_string_req = "string_req";
        String url = "http://sharkny.net/en/api/favourites/favit";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("response", response);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("item_id", bubleItem.getId());
                params.put("type", bubleItem.getGeneral_type());
                params.put("created_by", String.valueOf(iduser));

                return params;

            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void unFavoriteIt(){
        final int iduser = new SharknyPrefStore(FinanceDetailActivity.this).getIntPreferenceValue(Constants.PREFERENCE_USER_AUTHENTICATION_STATE);
        // Tag used to cancel the request
        String tag_string_req = "string_req";
        String url = "http://sharkny.net/en/api/favourites/undofav";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("response", response);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("item_id", bubleItem.getId());
                params.put("type", bubleItem.getGeneral_type());
                params.put("created_by", String.valueOf(iduser));

                return params;

            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void viewImage(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = ImageViewerDialog.newInstance(bubleItem.getImage());
        newFragment.show(ft, "tag");
    }

    public void viewImage1(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = ImageViewerDialog.newInstance(bubleItem.getOwnerUser().getImage());
        newFragment.show(ft, "tag");
    }
}
