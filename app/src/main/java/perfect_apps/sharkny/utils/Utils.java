package perfect_apps.sharkny.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by mostafa on 26/05/16.
 */
public class Utils {

    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

    public static void hideKeyboard(EditText editText, Context context)
    {
        if(editText != null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public static boolean isOnline(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public static String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public static Bitmap getBitmapFromUri(Context mContext, Uri uri){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String searchProjectUrl(String title, String country, String type, String field){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("sharkny.net")
                .appendPath("en")
                .appendPath("api")
                .appendPath("projects")
                .appendPath("search")
                .appendQueryParameter("title", title)
                .appendQueryParameter("country", country)
                .appendQueryParameter("type", type)
                .appendQueryParameter("field", field);

        return builder.build().toString();
    }

    public static String searchFinanceUrl(String title, String country, String type, String field){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("sharkny.net")
                .appendPath("en")
                .appendPath("api")
                .appendPath("finance")
                .appendPath("search")
                .appendQueryParameter("title", title)
                .appendQueryParameter("country", country)
                .appendQueryParameter("type", type)
                .appendQueryParameter("field", field);

        return builder.build().toString();
    }

    public static String searchFranchisUrl(String title, String country, String type, String field){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("sharkny.net")
                .appendPath("en")
                .appendPath("api")
                .appendPath("franchises")
                .appendPath("search")
                .appendQueryParameter("title", title)
                .appendQueryParameter("country", country)
                .appendQueryParameter("type", type)
                .appendQueryParameter("field", field);

        return builder.build().toString();
    }

    public static String searchServiceUrl(String title){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("sharkny.net")
                .appendPath("en")
                .appendPath("api")
                .appendPath("other-services")
                .appendPath("search")
                .appendQueryParameter("title", title);

        return builder.build().toString();
    }

    public static boolean setListViewHeightBasedOnItems(HorizontalListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null && listAdapter.getCount() == 1) {
            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.width = 600;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return false;
        }

    }


}
