package perfect_apps.sharkny.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.akexorcist.localizationactivity.LocalizationActivity;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.sharkny.HomeActivity;
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.store.SharknyPrefStore;
import perfect_apps.sharkny.utils.Constants;

public class SplashActivity extends LocalizationActivity {
    @Bind(R.id.splash_image_logo) ImageView image0;
    private Animation fade0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set default language of activity
        setDefaultLanguage("en");

        if(checkFirstTimeOpenApp() == 0){
            setLanguage(Locale.getDefault().getLanguage());
        }

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        fade0 = AnimationUtils.loadAnimation(this, R.anim.fade_in_enter);

        image0.startAnimation(fade0);
        fade0.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private int checkFirstTimeOpenApp(){
        return new SharknyPrefStore(this).getIntPreferenceValue(Constants.PREFERENCE_FIRST_TIME_OPEN_APP_STATE);
    }
}
