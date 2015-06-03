package com.sporthenon.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sporthenon.android.R;

import java.util.Locale;

public class LangActivity extends Activity implements View.OnClickListener {

    protected RadioButton langEN;
    protected RadioButton langFR;

    protected void onCreate(Bundle state) {
        super.onCreate(state);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String lang = prefs.getString("lang", null);
        String env = getResources().getString(R.string.env);
        if (env.matches("local|test"))
            lang = null;
        if (lang != null) {
            setLocale(lang);
            nextActivity();
        }
        else {
            requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
            setContentView(R.layout.activity_lang);
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

            ((TextView) findViewById(R.id.title)).setText(R.string.select_language);
            findViewById(R.id.back_icon).setVisibility(View.GONE);
            findViewById(R.id.search_icon).setVisibility(View.GONE);
            langEN = (RadioButton) findViewById(R.id.langEN);
            langFR = (RadioButton) findViewById(R.id.langFR);
            findViewById(R.id.ok).setOnClickListener(this);
        }
    }

    public void nextActivity() {
        Intent i = new Intent(this, SportActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View v) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        String lang = langFR.isChecked() ? "fr" : "en";
        editor.putString("lang", lang);
        editor.apply();
        setLocale(lang);
        nextActivity();
    }

    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    public void onBackPressed() {
        finish();
    }

 }