/*
 * *
 *  * Created by Shipan Liu, Ludovico Ferrara, Minhua Liu, Rodolphe Loic Souassi Tatang and Daeun Jung
 *  * Copyright (c) 2023 . All rights reserved.
 *  *  Last modified 11.07.23, 08:04
 *
 */

package com.example.a07;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;

public class ConsentActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private MaterialCheckBox consentCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent);

        sharedPreferences = getSharedPreferences("my_app_preferences", MODE_PRIVATE);
        consentCheckBox = findViewById(R.id.consentCheckBox);
        TextView privacyPolicyTextView = findViewById(R.id.privacyPolicyTextView);

        // Set privacy policy text
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            privacyPolicyTextView.setText(Html.fromHtml(getString(R.string.privacy_policy), Html.FROM_HTML_MODE_COMPACT));
        } else {
            privacyPolicyTextView.setText(Html.fromHtml(getString(R.string.privacy_policy)));
        }

        // Check if the user has already given consent
        if (sharedPreferences.getBoolean("hasUserGivenConsent", false)) {
            goToMainApp();
        }

        MaterialButton continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(view -> onContinueClicked());
    }

    private void onContinueClicked() {
        // Check if the user has given consent
        if (consentCheckBox.isChecked()) {
            // Save that the user has given consent
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("hasUserGivenConsent", true);
            editor.apply();
            goToMainApp();
        } else {
            Toast.makeText(this, "You must agree to the terms to continue", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToMainApp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private long backKeyPressedTime = 0;
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast toast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            System.exit(0);
        }
    }
}