package perfect_apps.sharkny.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.models.BubleItem;

public class DetailActivity extends LocalizationActivity {
    @Bind(R.id.mainImage) ImageView mainImageView;
    @Bind(R.id.mainImageOwner) ImageView mainOwnerImageView;
    @Bind(R.id.certifiedImage) ImageView certifiedImage;
    @Bind(R.id.title) TextView titleOfMainImage;

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
}
