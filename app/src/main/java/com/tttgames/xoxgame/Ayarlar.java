package com.tttgames.xoxgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Ayarlar extends AppCompatActivity {
    private Switch soundSwitch;
    private RadioGroup themeRadioGroup, xImageRadioGroup, oImageRadioGroup;
    private Button saveSettingsButton;
    private SharedPreferences sharedPreferences;
    private AudioManager audioManager;
    private SeekBar volumeSeekBar;
    private LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayarlar);

        rootLayout = findViewById(R.id.rootLayout);
        soundSwitch = findViewById(R.id.soundSwitch);
        themeRadioGroup = findViewById(R.id.themeRadioGroup);
        xImageRadioGroup = findViewById(R.id.xImageRadioGroup);
        oImageRadioGroup = findViewById(R.id.oImageRadioGroup);
        saveSettingsButton = findViewById(R.id.saveSettingsButton);
        volumeSeekBar = findViewById(R.id.volumeSeekBar);

        sharedPreferences = getSharedPreferences("game_settings", MODE_PRIVATE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        applyThemeBackgroundFromSettings(); // Bu satır kalmalı
        setupSoundSettings();
        setupThemeAndImageSettings(); // Tema ve imaj ayarlarını yükleme fonksiyonu

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

    private void setupSoundSettings() {
        boolean isSoundEnabled = sharedPreferences.getBoolean("sound_enabled", true);
        soundSwitch.setChecked(isSoundEnabled);

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar.setMax(maxVolume);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar.setProgress(currentVolume);

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setupThemeAndImageSettings() {
        // Tema ayarlarını yükle
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

        // X imaj ayarlarını yükle
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

        // O imaj ayarlarını yükle
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
        editor.putBoolean("sound_enabled", soundSwitch.isChecked());

        // Tema seçimi (Oyun Ekranı için)
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

        // Tema seçimi (Diğer Ekranlar için)
        String otherScreenTheme = "Varsayilan";
        if (selectedThemeId == R.id.themeBlackWhite) {
            otherScreenTheme = "SiyahBeyaz";
        } else if (selectedThemeId == R.id.themeRedPink) {
            otherScreenTheme = "KirmiziTema";
        } else if (selectedThemeId == R.id.themeBrainRot) {
            otherScreenTheme = "BrainRotTema";
        }
        editor.putString("other_screens_theme", otherScreenTheme);

        // X ikon seçimi... (Aynı kalabilir)
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

        // O ikon seçimi... (Aynı kalabilir)
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