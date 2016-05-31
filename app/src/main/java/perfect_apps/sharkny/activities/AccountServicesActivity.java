package perfect_apps.sharkny.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;

import perfect_apps.sharkny.R;

public class AccountServicesActivity extends LocalizationActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_services);
        setToolbar();

    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountServicesActivity.this, MyAccountActivity.class)
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

        /*
        * change font of title
        * */
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface sharknyFont;
        if (getLanguage().equalsIgnoreCase("en")){
            sharknyFont = Typeface.createFromAsset(getAssets(), "fonts/daisy2.ttf");
        }else {
            sharknyFont = Typeface.createFromAsset(getAssets(), "fonts/daisy.ttf");
        }
        mTitle.setTypeface(sharknyFont);

        /*
        * change action of image in bar
        * */
        ImageView adds = (ImageView) toolbar.findViewById(R.id.adds);
        adds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountServicesActivity.this, AddServeiceActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);
            }
        });


    }

    // set on back button pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AccountServicesActivity.this, MyAccountActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
        finish();
    }

}