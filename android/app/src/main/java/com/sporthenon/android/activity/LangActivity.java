package com.sporthenon.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import com.sporthenon.android.R;

public class LangActivity extends Activity implements View.OnClickListener {

    protected RadioButton langEN;
    protected RadioButton langFR;

    protected void onCreate(Bundle state) {
        super.onCreate(state);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String lang = prefs.getString("lang", null);
        if (lang != null)
            nextActivity();
        else {
            setContentView(R.layout.activity_lang);
            langEN = (RadioButton) findViewById(R.id.langEN);
            langFR = (RadioButton) findViewById(R.id.langFR);
            ((Button) findViewById(R.id.ok)).setOnClickListener(this);
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
        editor.putString("lang", langFR.isChecked() ? "fr" : "en");
        editor.commit();
        nextActivity();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

 }