package perfect_apps.sharkny.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import perfect_apps.sharkny.R;
import perfect_apps.sharkny.models.MessageModel;

public class MessageDetailActivity extends AppCompatActivity {

    @Bind(R.id.message) TextView message;

    private static MessageModel messageModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ButterKnife.bind(this);

        messageModel = (MessageModel) getIntent().getExtras().get("message");
        message.setText(messageModel.getMessageBody());
        setToolbar();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_black_24dp);
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

        mTitle.setText(messageModel.getUserName());

    }

}
