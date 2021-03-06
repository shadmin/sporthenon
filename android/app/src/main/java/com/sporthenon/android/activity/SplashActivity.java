package com.sporthenon.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

import com.sporthenon.android.R;

public class SplashActivity extends Activity {

    private final int DISPLAY_TIME = 5000;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        TextView version = (TextView) findViewById(R.id.version);
        version.setText(getString(R.string.app_version));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, LangActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, DISPLAY_TIME);
    }

}