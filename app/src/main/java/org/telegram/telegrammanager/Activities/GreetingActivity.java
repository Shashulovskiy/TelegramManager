package org.telegram.telegrammanager.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import org.telegram.telegrammanager.R;

public class GreetingActivity extends Activity {

    static{
        System.loadLibrary("tdjni");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_authorisation_greeting);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(loginButtonClickListener);
    }

    private View.OnClickListener loginButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startNextActivity();
        }
    };

    public void startNextActivity() {
        Intent intent = new Intent(GreetingActivity.this, PhoneNumberActivity.class);
        startActivity(intent);
    }
}
