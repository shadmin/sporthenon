package com.sporthenon.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RadioButton;

import com.sporthenon.android.R;

import java.util.Locale;

public class LangActivity extends Activity implements View.OnClickListener {

    protected RadioButton langEN;
    protected RadioButton langFR;

    protected void onCreate(Bundle state) {
        super.onCreate(state);

        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //String lang = prefs.getString("lang", null);
        String lang = Locale.getDefault().getLanguage().equalsIgnoreCase("fr") ? "fr" : "en";
        String env = getResources().getString(R.string.env);
        if (env.matches("local")) {
            lang = null;
        }
        if (lang != null) {
            setLocale(lang);
            nextActivity();
        }
        else {
            setContentView(R.layout.activity_lang);

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