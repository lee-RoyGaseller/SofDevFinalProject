package com.google.engedu.anagrams;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

public class ChooseActivity extends android.app.Activity{


    private final String ACTIVITY_TAG = "ChooseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(ACTIVITY_TAG, "inside onStart()");
        setContentView(R.layout.choose_layout);
        updateView();
    }

    private void updateView() {
        AnagramDictionary ad = AnagramsActivity.dictionary;
        if (ad.isOML()) {
            RadioButton oml = (RadioButton) findViewById(R.id.oneMoreLetter);
            oml.setChecked(true);
        } else {
            RadioButton tml =  findViewById(R.id.twoMoreLetter);
            tml.setChecked(true);
        }
    }

    private void updateAnagramsDictionary() {
        AnagramDictionary ad = AnagramsActivity.dictionary;
        RadioButton oml = findViewById(R.id.oneMoreLetter);
        if (ad.isOML() != oml.isChecked()) {
            Log.w(ACTIVITY_TAG, "inside updateAnagramsDictionary, trying to change mode");
            ad.changeMode();
        }
    }

    public void goBack(View v) {
        updateAnagramsDictionary();
        this.finish();
    }
}
