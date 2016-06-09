package perfect_apps.sharkny.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
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
import perfect_apps.sharkny.models.BubleItem;

public class DetailActivity extends LocalizationActivity {

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




    public static final String ARG_ITEM_ID = "item_id";
    private static BubleItem bubleItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setToolbar();

        bubleItem = (BubleItem) getIntent().getExtras().get(ARG_ITEM_ID);
        fillData();

    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this, MyAccountActivity.class)
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

        likeCount.setText(bubleItem.getLikes_count());
        commentCount.setText(bubleItem.getComments_count());
        titleOfMainImage.setText(bubleItem.getTitle());
        startDate.setText(bubleItem.getStart_date());
        endDate.setText(bubleItem.getEnd_date());
        investment_value.setText(bubleItem.getInvestment_value());
        project_field.setText(bubleItem.getProject_field());
        project_type.setText(bubleItem.getProject_type());
        investment_percentag.setText(bubleItem.getInvestment_percentage());
        guarantees.setText(bubleItem.getGuarantees());
        country.setText(bubleItem.getGuarantees());
        description.setText(bubleItem.getDescription());
        owner_name.setText(bubleItem.getOwnerUser().getFullname());
    }


    public void sendMessage(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", bubleItem.getOwnerUser().getMobile(), null)));
    }

    public void like(View view) {
        updateHeartButton(likeBtn, true);
    }

    public void comment(View view) {
        Intent intent = new Intent(this, CommentsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
    }

    public void favorite(View view) {
        updateStarImage(favoritImage, true);
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



}
