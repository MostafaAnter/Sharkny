package perfect_apps.sharkny.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;
import perfect_apps.sharkny.R;

public class RegisterActivity extends LocalizationActivity {

    @Bind(R.id.select_profile_pic) LinearLayout selectProfilePic;
    @Bind(R.id.user_avatar) ImageView circleImageView;

    private static final int REQUEST_CODE = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setToolbar();
        setOnLinearSelected();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, HomeActivity.class)
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
            sharknyFont = Typeface.createFromAsset(getAssets(), "fonts/daisy.ttf");
        }else {
            sharknyFont = Typeface.createFromAsset(getAssets(), "fonts/daisy.ttf");
        }
        mTitle.setTypeface(sharknyFont);


    }

    // set on back button pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, HomeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
        finish();
    }

    // for pick photo
    private void setOnLinearSelected(){
        selectProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPickerIntent intent = new PhotoPickerIntent(RegisterActivity.this);
                intent.setPhotoCount(1);
                intent.setShowCamera(true);
                intent.setShowGif(true);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                Uri uri = Uri.fromFile(new File(photos.get(0)));
                setSelectedPhotoInsideCircleShap(uri);
            }
        }
    }

    private void setSelectedPhotoInsideCircleShap(Uri uri){
        Glide.with(this)
                .load(uri)
                .centerCrop()
                .thumbnail(0.1f)
                .placeholder(R.drawable.__picker_ic_photo_black_48dp)
                .error(R.drawable.__picker_ic_broken_image_black_48dp)
                .into(circleImageView);
    }


}
