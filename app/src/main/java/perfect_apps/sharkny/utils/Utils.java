package perfect_apps.sharkny.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by mostafa on 26/05/16.
 */
public class Utils {

    public static void hideKeyboard(EditText editText, Context context)
    {
        if(editText != null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

}
