package perfect_apps.sharkny.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.models.BubleItem;
import perfect_apps.sharkny.models.FinanceModel;

public class FinanceDetailActivity extends AppCompatActivity {
    @Bind(R.id.mainImage)
    ImageView mainImageView;
    @Bind(R.id.mainImageOwner) ImageView mainOwnerImageView;
    @Bind(R.id.certifiedImage) ImageView certifiedImage;
    @Bind(R.id.title)
    TextView titleOfMainImage;

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
    private static FinanceModel bubleItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_detail);
        ButterKnife.bind(this);
        setToolbar();

        bubleItem = (FinanceModel) getIntent().getExtras().get(ARG_ITEM_ID);
        fillData();

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

        titleOfMainImage.setText(bubleItem.getTitle());
        startDate.setText("");
        endDate.setText("");
        investment_value.setText(bubleItem.getInvestment_value());
        project_field.setText(bubleItem.getInvestment_Field());
        project_type.setText(bubleItem.getInance_type());
        investment_percentag.setText(bubleItem.getInvestment_percentage());
        guarantees.setText("");
        country.setText(bubleItem.getCountry());
        description.setText(bubleItem.getDescription());
        owner_name.setText(bubleItem.getOwnerUser().getFullname());
    }


    public void sendMessage(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", bubleItem.getOwnerUser().getMobile(), null)));
    }

}
