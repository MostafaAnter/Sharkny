package perfect_apps.sharkny.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.sharkny.R;

public class MyAccountActivity extends LocalizationActivity {

    @Bind(R.id.text1) TextView textView1;
    @Bind(R.id.text2) TextView textView2;
    @Bind(R.id.text3) TextView textView3;
    @Bind(R.id.text4) TextView textView4;
    @Bind(R.id.text5) TextView textView5;

    @Bind(R.id.linear1) LinearLayout linearLayout1;
    @Bind(R.id.linear2) LinearLayout linearLayout2;
    @Bind(R.id.linear3) LinearLayout linearLayout3;
    @Bind(R.id.linear4) LinearLayout linearLayout4;
    @Bind(R.id.linear5) LinearLayout linearLayout5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ButterKnife.bind(this);
        setToolbar();
        changeTextFont();
        setOnViewClick();

    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAccountActivity.this, HomeActivity.class)
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


    }

    // set on back button pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MyAccountActivity.this, HomeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
        finish();
    }

    private void changeTextFont(){
        Typeface sharknyFont = Typeface.createFromAsset(getAssets(), "fonts/thin.ttf");

        textView1.setTypeface(sharknyFont);
        textView2.setTypeface(sharknyFont);
        textView3.setTypeface(sharknyFont);
        textView4.setTypeface(sharknyFont);
        textView5.setTypeface(sharknyFont);
    }

    private void setOnViewClick(){
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAccountActivity.this, AccountProfileActivity.class));
                overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
            }
        });

        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAccountActivity.this, AccountFinanceActivity.class));
                overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
            }
        });

        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAccountActivity.this, AccountAdvanageActivity.class));
                overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
            }
        });

        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAccountActivity.this, AccountServicesActivity.class));
                overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
            }
        });

        linearLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAccountActivity.this, AccountProjectActivity.class));
                overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
            }
        });
    }

}
