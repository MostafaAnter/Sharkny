package perfect_apps.sharkny.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.fragments.FragmentFout;
import perfect_apps.sharkny.fragments.FragmentOne;
import perfect_apps.sharkny.fragments.FragmentThree;
import perfect_apps.sharkny.fragments.FragmentTwo;
import perfect_apps.sharkny.store.SharknyPrefStore;
import perfect_apps.sharkny.utils.Constants;
import perfect_apps.sharkny.utils.CustomTypefaceSpan;

public class HomeActivity extends LocalizationActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public BottomNavigationView bottomNavigationView;
    ViewPager pager;


    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.nav_view) NavigationView navigationView;
    private TextView mTitle;
    private RadioButton radioButtonEn, radioButtonAr;
    private CircleImageView user_pic;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getLanguage().equalsIgnoreCase("en")){
            setLanguage("ar");
        }

        // set bottom navigation
        int[] image = {R.drawable.home_icon,
                R.drawable.new_projects_icon,
                R.drawable.new_finance_icon,
                R.drawable.news_other_services};
        int[] color = {ContextCompat.getColor(this, R.color.firstColor), ContextCompat.getColor(this, R.color.secondColor),
                ContextCompat.getColor(this, R.color.thirdColor), ContextCompat.getColor(this, R.color.fourthColor)};
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        Toolbar toolbar = getToolbar();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);
        changeFontOfNavigation();
        // if user is authenticated change menu :)
        if (isAuthenticated()) {
            navigationView.getMenu().clear(); //clear old inflated items.
            navigationView.inflateMenu(R.menu.activity_home_drawer_authenticated_user);
            changeFontOfNavigation();
        }


        // access components inside header
        // to access item inside header
        View header = navigationView.getHeaderView(0);
        user_pic = (CircleImageView) header.findViewById(R.id.user_avatar);
        if (isAuthenticated()){
            Glide.with(HomeActivity.this).load(new SharknyPrefStore(this).getPreferenceValue(Constants.PREFERENCE_USER_IMAGE_URL))
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(user_pic);
        }
        radioButtonEn = (RadioButton) header.findViewById(R.id.button21);
        radioButtonAr = (RadioButton) header.findViewById(R.id.button22);
        // change font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/thin.ttf");
        radioButtonAr.setTypeface(font);
        radioButtonEn.setTypeface(font);
        if(getLanguage().equalsIgnoreCase("en")){
            radioButtonEn.setChecked(true);
        }else {
            radioButtonAr.setChecked(true);
        }
        radioButtonEn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setLanguage("en");
                    changeFirstTimeOpenAppState();
                }
            }
        });
        radioButtonAr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setLanguage("ar");
                    changeFirstTimeOpenAppState();
                }
            }
        });

        // this section for bottom navigation
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        pager = (ViewPager) findViewById(R.id.viewPager);
        /*if you use tablet mode uncomment this*/
        /*RelativeLayout.LayoutParams pagerParams = (RelativeLayout.LayoutParams)pager.getLayoutParams();
        pagerParams.setMargins(BottomNavigationUtils.getActionbarSize(this),pagerParams.topMargin,
                pagerParams.rightMargin,pagerParams.bottomMargin);
        pager.setLayoutParams(pagerParams);*/
        if (bottomNavigationView != null) {
            bottomNavigationView.isWithText(true);
            //bottomNavigationView.activateTabletMode();
            bottomNavigationView.isColoredBackground(false);
            bottomNavigationView.setItemActiveColorWithoutColoredBackground(ContextCompat.getColor(this, R.color.colorAccent));
        }
        setupViewPager(pager);
        bottomNavigationView.setUpWithViewPager(pager, color, image);
        changeToolbarTitlePrograming();
    }

    @NonNull
    private Toolbar getToolbar() {
        setSupportActionBar(toolbar);
        /*
        * hide title
        * */
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        Typeface sharknyFont ;
        if (getLanguage().equalsIgnoreCase("en")){
            sharknyFont = Typeface.createFromAsset(getAssets(), "fonts/daisy.ttf");
        }else {
            sharknyFont = Typeface.createFromAsset(getAssets(), "fonts/daisy.ttf");
        }
        mTitle.setTypeface(sharknyFont);

        return toolbar;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.login) {
            // Handle the camera action
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
        } else if (id == R.id.register) {
            startActivity(new Intent(HomeActivity.this, RegisterActivity.class));
            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);

        } else if (id == R.id.about) {

        } else if (id == R.id.contact) {
            sendFeedBack();

        }else if (id == R.id.my_profile) {
            startActivity(new Intent(HomeActivity.this, AccountProfileActivity.class));
            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);

        }else if (id == R.id.inbox) {
            startActivity(new Intent(HomeActivity.this, InboxActivity.class));
            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);

        }else if (id == R.id.sent_mail) {
            startActivity(new Intent(HomeActivity.this, SentMailActivity.class));
            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);

        }else if (id == R.id.sign_out) {
            signOut();
            navigationView.getMenu().clear(); //clear old inflated items.
            navigationView.inflateMenu(R.menu.activity_home_drawer);
            changeFontOfNavigation();
        }else if (id == R.id.favorite) {
            startActivity(new Intent(HomeActivity.this, FavoriteActivity.class));
            overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentOne(), getString(R.string.home));
        adapter.addFragment(new FragmentTwo(), getString(R.string.projects));
        adapter.addFragment(new FragmentThree(), getString(R.string.finance));
        adapter.addFragment(new FragmentFout(), getString(R.string.other_services));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
    private void changeToolbarTitlePrograming(){
        bottomNavigationView.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                if (getLanguage().equalsIgnoreCase("en")) {
                    switch (index) {
                        case 0:
                            mTitle.setText(getString(R.string.home));
                            break;
                        case 1:
                            mTitle.setText(getString(R.string.projects));
                            break;
                        case 2:
                            mTitle.setText(getString(R.string.finance));
                            break;
                        case 3:
                            mTitle.setText(getString(R.string.other_services));
                            break;
                    }
                }else {
                    switch (index) {
                        case 0:
                            mTitle.setText(getString(R.string.home));
                            break;
                        case 1:
                            mTitle.setText(getString(R.string.projects));
                            break;
                        case 2:
                            mTitle.setText(getString(R.string.finance));
                            break;
                        case 3:
                            mTitle.setText(getString(R.string.other_services));
                            break;
                    }
                }
            }
        });
    }

    //change font of drawer
    private void changeFontOfNavigation(){
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/thin.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void changeFirstTimeOpenAppState(){
        new SharknyPrefStore(this).addPreference(Constants.PREFERENCE_FIRST_TIME_OPEN_APP_STATE, 1);
    }

    @Override
    public void onAfterLocaleChanged() {
        super.onAfterLocaleChanged();
        startActivity(new Intent(this, SplashActivity.class));
    }

    private boolean isAuthenticated(){
        int authenticatedState = new SharknyPrefStore(this).getIntPreferenceValue(Constants.PREFERENCE_USER_AUTHENTICATION_STATE);

        if ((authenticatedState != 0)) {
            return true;
        }
        return false;
    }

    private void signOut(){
        new SharknyPrefStore(this).addPreference(Constants.PREFERENCE_USER_AUTHENTICATION_STATE, 0);
    }

    private void sendFeedBack(){
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setType("plain/text");
        sendIntent.setData(Uri.parse("support@sharkny.net"));
        sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "support@sharkny.net" });
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "FeedBack");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "hello. this is a message sent from my Android app :-)");
        startActivity(sendIntent);
    }
}
