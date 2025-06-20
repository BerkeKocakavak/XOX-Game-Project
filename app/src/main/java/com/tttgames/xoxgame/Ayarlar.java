package com.tttgames.xoxgame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Ayarlar extends AppCompatActivity {

    private RadioGroup themeRadioGroup, xImageRadioGroup, oImageRadioGroup;
    private Button saveSettingsButton;
    private SharedPreferences sharedPreferences;
    private LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayarlar);

        rootLayout = findViewById(R.id.rootLayout);
        themeRadioGroup = findViewById(R.id.themeRadioGroup);
        xImageRadioGroup = findViewById(R.id.xImageRadioGroup);
        oImageRadioGroup = findViewById(R.id.oImageRadioGroup);
        saveSettingsButton = findViewById(R.id.saveSettingsButton);

        sharedPreferences = getSharedPreferences("game_settings", MODE_PRIVATE);

        applyThemeBackgroundFromSettings();
        setupThemeAndImageSettings(); //Theme and Image Settıngs applying func.

        saveSettingsButton.setOnClickListener(v -> {
            saveSettings();
            Toast.makeText(this, "Ayarlar kaydedildi", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void applyThemeBackgroundFromSettings() {
        String selectedTheme = sharedPreferences.getString("other_screens_theme", "Varsayilan");
        if (rootLayout != null) {
            switch (selectedTheme) {
                case "SiyahBeyaz":
                    rootLayout.setBackgroundResource(R.drawable.tema_siyahbeyaz1);
                    break;
                case "KirmiziTema":
                    rootLayout.setBackgroundResource(R.drawable.tema_kirmizipembe1);
                    break;
                case "BrainRotTema":
                    rootLayout.setBackgroundResource(R.drawable.tema_brainrot1);
                    break;
                default:
                    rootLayout.setBackgroundResource(R.drawable.tema_varsayilan1);
                    break;
            }
        }
    }

    private void setupThemeAndImageSettings() {

        String currentTheme = sharedPreferences.getString("current_theme", "Varsayilan");
        switch (currentTheme) {
            case "SiyahBeyaz":
                themeRadioGroup.check(R.id.themeBlackWhite);
                break;
            case "KirmiziTema":
                themeRadioGroup.check(R.id.themeRedPink);
                break;
            case "BrainRotTema":
                themeRadioGroup.check(R.id.themeBrainRot);
                break;
            default:
                themeRadioGroup.check(R.id.themeDefault);
                break;
        }

        // Load x images
        String xImage = sharedPreferences.getString("x_image", "x_image");
        if ("x_image".equals(xImage)) {
            xImageRadioGroup.check(R.id.xImageDefault);
        } else if ("x_pembe".equals(xImage)) {
            xImageRadioGroup.check(R.id.xImageRedPink);
        } else if ("x_br".equals(xImage)) {
            xImageRadioGroup.check(R.id.xImageBrainRot);
        } else if ("x_gri".equals(xImage)) {
            xImageRadioGroup.check(R.id.xImageGray);
        }

        // Load O images
        String oImage = sharedPreferences.getString("o_image", "o_image");
        if ("o_image".equals(oImage)) {
            oImageRadioGroup.check(R.id.oImageDefault);
        } else if ("o_pembe".equals(oImage)) {
            oImageRadioGroup.check(R.id.oImageRedPink);
        } else if ("o_br".equals(oImage)) {
            oImageRadioGroup.check(R.id.oImageBrainRot);
        } else if ("o_gri".equals(oImage)) {
            oImageRadioGroup.check(R.id.oImageGray);
        }
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Theme Selection for Game Screen
        int selectedThemeId = themeRadioGroup.getCheckedRadioButtonId();
        String selectedTheme = "Varsayilan";
        if (selectedThemeId == R.id.themeBlackWhite) {
            selectedTheme = "SiyahBeyaz";
        } else if (selectedThemeId == R.id.themeRedPink) {
            selectedTheme = "KirmiziTema";
        } else if (selectedThemeId == R.id.themeBrainRot) {
            selectedTheme = "BrainRotTema";
        }
        editor.putString("current_theme", selectedTheme);

        // Theme Selection for other screens
        String otherScreenTheme = "Varsayilan";
        if (selectedThemeId == R.id.themeBlackWhite) {
            otherScreenTheme = "SiyahBeyaz";
        } else if (selectedThemeId == R.id.themeRedPink) {
            otherScreenTheme = "KirmiziTema";
        } else if (selectedThemeId == R.id.themeBrainRot) {
            otherScreenTheme = "BrainRotTema";
        }
        editor.putString("other_screens_theme", otherScreenTheme);

        // X Icon selection
        int selectedXImageId = xImageRadioGroup.getCheckedRadioButtonId();
        String xImageValue = "x_image";
        if (selectedXImageId == R.id.xImageRedPink) {
            xImageValue = "x_pembe";
        } else if (selectedXImageId == R.id.xImageBrainRot) {
            xImageValue = "x_br";
        } else if (selectedXImageId == R.id.xImageGray) {
            xImageValue = "x_gri";
        } else if (selectedXImageId == R.id.xImageDefault) {
            xImageValue = "x_image";
        }
        editor.putString("x_image", xImageValue);

        // O Icon selection
        int selectedOImageId = oImageRadioGroup.getCheckedRadioButtonId();
        String oImageValue = "o_image";
        if (selectedOImageId == R.id.oImageRedPink) {
            oImageValue = "o_pembe";
        } else if (selectedOImageId == R.id.oImageBrainRot) {
            oImageValue = "o_br";
        } else if (selectedOImageId == R.id.oImageGray) {
            oImageValue = "o_gri";
        } else if (selectedOImageId == R.id.oImageDefault) {
            oImageValue = "o_image";
        }
        editor.putString("o_image", oImageValue);

        editor.apply();
    }

}