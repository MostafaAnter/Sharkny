package perfect_apps.sharkny.activities;


import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.android.volley.Cache;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import perfect_apps.sharkny.BuildConfig;
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.app.AppController;
import perfect_apps.sharkny.dialog.ImageViewerDialog;
import perfect_apps.sharkny.models.FavoriteModel;
import perfect_apps.sharkny.models.FranchisesModel;
import perfect_apps.sharkny.models.OwnerUser;
import perfect_apps.sharkny.models.ViewItemProject;
import perfect_apps.sharkny.store.FavoriteStore;
import perfect_apps.sharkny.store.LikeStore;
import perfect_apps.sharkny.store.SharknyPrefStore;
import perfect_apps.sharkny.utils.Constants;

public class DetailViewActivity extends LocalizationActivity {

    // belong like button animations
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
    private final Map<ImageView, AnimatorSet> likeAnimations = new HashMap<>();



    @Bind(R.id.linear1)LinearLayout linearLayout1;
    @Bind(R.id.linear2)LinearLayout linearLayout2;
    @Bind(R.id.grantee) TextView grantee;
    @Bind(R.id.view1) View view1;
    @Bind(R.id.view2) View view2;

    @Bind(R.id.mainImage) ImageView mainImageView;
    @Bind(R.id.mainImageOwner) ImageView mainOwnerImageView;
    @Bind(R.id.certifiedImage) ImageView certifiedImage;
    @Bind(R.id.title) TextView titleOfMainImage;
    @Bind(R.id.like_count) TextView likeCount;
    @Bind(R.id.comment_count) TextView commentCount;

    @Bind(R.id.start_date) TextView startDate;
    @Bind(R.id.end_date) TextView endDate;
    @Bind(R.id.investment_value) TextView investment_value;
    @Bind(R.id.project_field) TextView project_field;
    @Bind(R.id.project_type) TextView project_type;
    @Bind(R.id.investment_percentag) TextView investment_percentag;
    @Bind(R.id.guarantees) TextView guarantees;
    @Bind(R.id.country) TextView country;
    @Bind(R.id.description) TextView description;
    @Bind(R.id.owner_name) TextView owner_name;

    @Bind(R.id.like_btn) ImageView likeBtn;
    @Bind(R.id.favoriteImage) ImageView favoritImage;

    @Bind(R.id.send_message)
    Button sendMessageBtn;
    private static ViewItemProject bubleItem;
    private static FranchisesModel franchisesItem;
    private static OwnerUser ownerUser;

    private static String id;
    private static String type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setToolbar();


        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");

        fetchItem();
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
                finish();
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



        likeCount.setText(bubleItem.getLikes_count());
        commentCount.setText(bubleItem.getComments_count());
        titleOfMainImage.setText(bubleItem.getTitle());
        if (bubleItem.getStart_date() != null) {
            if (bubleItem.getStart_date().trim().isEmpty()){
                startDate.setText("__");
                linearLayout1.setVisibility(View.GONE);
                view1.setVisibility(View.GONE);
            }else {
                startDate.setText(bubleItem.getStart_date());
            }
        }

        if (bubleItem.getEnd_date() != null) {
            if (bubleItem.getEnd_date().trim().isEmpty()){
                endDate.setText("--");
                linearLayout2.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
            }else {
                endDate.setText(bubleItem.getEnd_date());
            }
        }

        if (bubleItem.getInvestment_value() != null) {
            if (bubleItem.getInvestment_value().trim().isEmpty()){
                investment_value.setText("__");

            }else{
                investment_value.setText(bubleItem.getInvestment_value());
            }
        }

        if (bubleItem.getProject_field() != null) {
            project_field.setText(bubleItem.getProject_field());
        }

        if (bubleItem.getProject_type() != null) {
            project_type.setText(bubleItem.getProject_type());
        }

        if (bubleItem.getInvestment_percentage() != null) {
            if (bubleItem.getInvestment_percentage().trim().isEmpty()){
                investment_percentag.setText("__ %");
            }else {
                investment_percentag.setText(bubleItem.getInvestment_percentage() + " %");
            }
        }

        if (bubleItem.getGuarantees() != null) {
            if (bubleItem.getGuarantees().trim().isEmpty()){
                guarantees.setText("__");
            }else {
                guarantees.setText(bubleItem.getGuarantees());
            }
        }

        if (bubleItem.getCountry() != null) {
            country.setText(bubleItem.getCountry());
        }


        description.setText(bubleItem.getDescription().replaceAll("<|>|p|/", ""));


    }

    private void fillFranchiseData(){

        // populate mainImage
        Glide.with(this)
                .load(franchisesItem.getImage())
                .placeholder(R.drawable.placeholder)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mainImageView);

        // populate certified image
        if (Integer.parseInt(franchisesItem.getIs_verified()) == 0)
            certifiedImage.setVisibility(View.GONE);



        likeCount.setText(franchisesItem.getLikes_count());
        commentCount.setText(franchisesItem.getComments_count());
        titleOfMainImage.setText(franchisesItem.getTitle());

        startDate.setText("__");
        linearLayout1.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);
        endDate.setText("--");
        linearLayout2.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);


                investment_value.setText("__");


        if (franchisesItem.getFranchise_field() != null) {
            project_field.setText(franchisesItem.getFranchise_field());
        }

        if (franchisesItem.getFranchise_type() != null) {
            project_type.setText(franchisesItem.getFranchise_type());
        }
        investment_percentag.setText("__ %");
        guarantees.setText(franchisesItem.getTerms());
        grantee.setText("Terms  ");
        if (franchisesItem.getCountry() != null) {
            country.setText(franchisesItem.getCountry());
        }
        description.setText(franchisesItem.getDescription().replaceAll("<|>|p|/", ""));

    }

    private void fillDataOfOwner(){
        // populate Owner image
        Glide.with(this)
                .load(ownerUser.getImage())
                .placeholder(R.drawable.placeholder)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mainOwnerImageView);

        owner_name.setText(ownerUser.getFullname());
    }


    public void sendMessage(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", ownerUser.getMobile(), null)));
    }

    public void like(View view) {
        if (Integer.parseInt(type) == 1 || Integer.parseInt(type) == 2 || Integer.parseInt(type) == 4) {
            updateHeartButton(likeBtn, true);
            addItemToLike();
            int likeCountNumber = Integer.parseInt(bubleItem.getLikes_count()) + 1;
            likeCount.setText(String.valueOf(likeCountNumber));
            // call api
            likeIt();
        } else {
            updateHeartButton(likeBtn, true);
            addItemToLike();
            int likeCountNumber = Integer.parseInt(franchisesItem.getLikes_count()) + 1;
            likeCount.setText(String.valueOf(likeCountNumber));
            // call api
            likeIt();
        }
    }

    public void comment(View view) {

        if (Integer.parseInt(type) == 1 || Integer.parseInt(type) == 2 || Integer.parseInt(type) == 4) {
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
        } else {
            String type;
            String id;
            if (franchisesItem.getGeneral_type() == null) {
                type = "";
            } else {
                type = franchisesItem.getGeneral_type();
            }
            if (franchisesItem.getId() == null) {
                id = "";
            } else {
                id = franchisesItem.getId();
            }

            Intent intent = new Intent(this, CommentsActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("id", id);
            startActivity(intent);
            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
        }


    }

    public void favorite(View view) {
        updateStarImage(favoritImage, true);
        addItemToFavo();
        // call api

        favoriteIt();
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
        if (Integer.parseInt(type) == 1 || Integer.parseInt(type) == 2 || Integer.parseInt(type) == 4) {
            //add item to favorite
            FavoriteModel item = new FavoriteModel();
            item.setTitleKey(bubleItem.getId());
            item.setIdValue(bubleItem.getId());
            new FavoriteStore(this).update(item);
        } else {
            //add item to favorite
            FavoriteModel item = new FavoriteModel();
            item.setTitleKey(franchisesItem.getId());
            item.setIdValue(franchisesItem.getId());
            new FavoriteStore(this).update(item);
        }
    }

    private void addItemToLike() {
        if (Integer.parseInt(type) == 1 || Integer.parseInt(type) == 2 || Integer.parseInt(type) == 4) {
            //add item to favorite
            FavoriteModel item = new FavoriteModel();
            item.setTitleKey(bubleItem.getId());
            item.setIdValue(bubleItem.getId());
            new LikeStore(this).update(item);
        } else {
            //add item to favorite
            FavoriteModel item = new FavoriteModel();
            item.setTitleKey(franchisesItem.getId());
            item.setIdValue(franchisesItem.getId());
            new LikeStore(this).update(item);
        }
    }

    private void changeLikeFavoriteState(){

        if (Integer.parseInt(type) != 3) {
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
        } else {
            // for favorite
            if (new FavoriteStore(this).findItem(franchisesItem.getId(),
                    franchisesItem.getId())){
                // this item is in my database
                favoritImage.setImageResource(R.drawable.favorite_solid);
                AnimatorSet animatorSet = new AnimatorSet();
                likeAnimations.put(favoritImage, animatorSet);
            }else {
                favoritImage.setImageResource(R.drawable.favorite_not_solid);
            }

            // for like
            if (new LikeStore(this).findItem(franchisesItem.getId(),
                    franchisesItem.getId())){
                // this item is in my database
                likeBtn.setImageResource(R.drawable.like_solide);
                AnimatorSet animatorSet = new AnimatorSet();
                likeAnimations.put(likeBtn, animatorSet);
            }else {
                likeBtn.setImageResource(R.drawable.like_not_solid);
            }
        }
    }

    private void likeIt(){
        final int iduser = new SharknyPrefStore(DetailViewActivity.this).getIntPreferenceValue(Constants.PREFERENCE_USER_AUTHENTICATION_STATE);
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

    private void favoriteIt(){
        final int iduser = new SharknyPrefStore(DetailViewActivity.this).getIntPreferenceValue(Constants.PREFERENCE_USER_AUTHENTICATION_STATE);
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
        DialogFragment newFragment = ImageViewerDialog.newInstance(ownerUser.getImage());
        newFragment.show(ft, "tag");
    }

    private void fetchItem(){
        String url = "";
        if(Integer.parseInt(type) == 1){
            url = "http://sharkny.net/en/api/projects/view?id=" + id;
        }else if (Integer.parseInt(type) == 2){
            url = "http://sharkny.net/en/api/finance/view?id="  + id;
        }else if(Integer.parseInt(type) == 3){
            url= "http://sharkny.net/en/api/franchises/view?id=" + id;
        }else if (Integer.parseInt(type) == 4)
            url = "http://sharkny.net/en/api/other-services/view?id=" + id;

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if(entry != null){
            try {
                String data = new String(entry.data, "UTF-8");
                if (Integer.parseInt(type) == 1 ||
                        Integer.parseInt(type) == 2 ||
                        Integer.parseInt(type) == 4) {
                    bubleItem = parseProject(data);
                    fillData();
                    fetchUser();
                    changeLikeFavoriteState();
                } else if (Integer.parseInt(type) == 3){
                    franchisesItem = searchFranchisParse(data);
                    fillFranchiseData();
                    fetchUser();
                    changeLikeFavoriteState();
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else{
            // Cached response doesn't exists. Make network call here
            final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText(getResources().getString(R.string.wait));
            pDialog.setCancelable(false);
            pDialog.show();
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    if (Integer.parseInt(type) == 1 ||
                            Integer.parseInt(type) == 2||
                            Integer.parseInt(type) == 4) {
                        bubleItem = parseProject(response);
                        fillData();
                        fetchUser();
                        changeLikeFavoriteState();
                    } else if (Integer.parseInt(type) == 3){
                        franchisesItem = searchFranchisParse(response);
                        fillFranchiseData();
                        fetchUser();
                        changeLikeFavoriteState();
                    }
                    pDialog.dismissWithAnimation();


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismissWithAnimation();

                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq);
        }
    }

    // fetch view profile
    private void fetchUser() {
        String url = "";
        if (Integer.parseInt(type) == 1 || Integer.parseInt(type) == 2 || Integer.parseInt(type) == 4)
            url = BuildConfig.View_Profile + bubleItem.getCreated_by();
        if (Integer.parseInt(type) == 3)
            url = BuildConfig.View_Profile + franchisesItem.getCreated_by();

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");
                // handle data, like converting it to xml, json, bitmap etc.,
                ownerUser = parseOwnerUser(data);
                fillDataOfOwner();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            // Cached response doesn't exists. Make network call here
                StringRequest strReq = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        ownerUser = parseOwnerUser(response);
                        fillDataOfOwner();


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq);

        }
    }

    public static ViewItemProject parseProject(String feed){

        try {
            JSONObject jsonObject = new JSONObject(feed);//done
                String id = jsonObject.optString("id");
                String title = jsonObject.optString("title");
                String description = jsonObject.optString("description");
                String start_date = jsonObject.optString("start_date");
                String end_date = jsonObject.optString("end_date");
                String investment_value = jsonObject.optString("investment_value");
                String investment_percentag = jsonObject.optString("investment_percentag");
                String guarantees = jsonObject.optString("guarantees");
                String send_to_mobile = jsonObject.optString("send_to_mobile");
                String project_type = jsonObject.optString("project_type");
                String project_field = jsonObject.optString("project_field");
                String country = jsonObject.optString("country");
                String image = jsonObject.optString("image") ;
                String likes_count = jsonObject.optString("likes_count");
                String comments_count = jsonObject.optString("comments_count");
                String general_type = jsonObject.optString("general_type");
                String is_verified = jsonObject.optString("is_verified");
                String created_by = jsonObject.optString("created_by");
                ViewItemProject bubleItem = new ViewItemProject(id, title, description,start_date, end_date,
                        investment_value,investment_percentag,guarantees,
                        send_to_mobile, project_type,project_field, country,
                        image,likes_count,comments_count, general_type, is_verified, created_by);
            return bubleItem;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static OwnerUser parseOwnerUser(String feed){
        try {
            JSONObject  ownerObject = new JSONObject(feed);//done
                String idOwner = ownerObject.optString("id") ;
                String username = ownerObject.optString("username");
                String fullname = ownerObject.optString("fullname");
                String job = ownerObject.optString("job");
                String address = ownerObject.optString("address");
                String mobile = ownerObject.optString("mobile");
                String email = ownerObject.optString("email");
                String imageOwner = ownerObject.optString("image");
                String gender = ownerObject.optString("gender");
                String nationality = ownerObject.optString("nationality");
                String countryOwner = ownerObject.optString("countryOwner");
                OwnerUser ownerUser = new OwnerUser(idOwner, username, fullname, job, address, mobile, email, imageOwner, gender, nationality, countryOwner);
            return ownerUser;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static FranchisesModel searchFranchisParse(String feed){

        try {
            JSONObject  jsonObject = new JSONObject(feed);//done
                String id = jsonObject.optString("id");
                String title = jsonObject.optString("title");
                String description = jsonObject.optString("description") ;
                String terms = jsonObject.optString("terms");
                String franchise_type = jsonObject.optString("franchise_type");
                String franchise_field = jsonObject.optString("franchise_field");
                String country = jsonObject.optString("country");
                String image = jsonObject.optString("image");
                String general_type = jsonObject.optString("general_type");
                String likes_count = jsonObject.optString("likes_count");
                String comments_count = jsonObject.optString("comments_count");
                String is_verified = jsonObject.optString("is_verified");
                String created_by = jsonObject.optString("created_by");

                FranchisesModel franchisesModel = new FranchisesModel(id,title,description,terms,franchise_type,franchise_field,country,image
                        , general_type,likes_count,comments_count,is_verified,created_by);

            return franchisesModel;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }
}
