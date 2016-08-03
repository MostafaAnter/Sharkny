package perfect_apps.sharkny.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.app.AppController;
import perfect_apps.sharkny.store.SharknyPrefStore;
import perfect_apps.sharkny.utils.Constants;

public class AnswerMessagActivity extends AppCompatActivity {

    @Bind(R.id.reply) EditText reply;

    private static String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_messag);
        ButterKnife.bind(this);
        setToolbar();

        id = getIntent().getStringExtra("id");
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigate);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText("Reply");

    }

    public void sendReply(View view) {
        // send message to user with id
        final int iduser = new SharknyPrefStore(AnswerMessagActivity.this).getIntPreferenceValue(Constants.PREFERENCE_USER_AUTHENTICATION_STATE);

        // Set up a progress dialog
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(getResources().getString(R.string.wait));
        pDialog.setCancelable(false);
        pDialog.show();

        // Tag used to cancel the request
        String tag_string_req = "string_req";
        String url = "http://sharkny.net/en/api/contacts/send-email";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                pDialog.dismissWithAnimation();
                new SweetAlertDialog(AnswerMessagActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Good job!")
                        .setContentText("Your message sent successfully!")
                        .setConfirmText("Done!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                finish();
                                overridePendingTransition(R.anim.push_up_enter, R.anim.push_up_exit);
                            }
                        })
                        .show();

                Log.d("response", response);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismissWithAnimation();
                // show error message
                new SweetAlertDialog(AnswerMessagActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("some thing went wrong try again")
                        .show();
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sender_id", String.valueOf(iduser));
                params.put("recipient", id);
                params.put("subject", "no subject");
                params.put("content", reply.getText().toString());

                return params;

            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
