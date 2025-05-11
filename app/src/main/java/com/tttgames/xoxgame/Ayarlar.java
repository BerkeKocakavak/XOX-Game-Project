package com.tttgames.xoxgame;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.widget.SeekBar;
import android.widget.Button;

public class Ayarlar extends AppCompatActivity {

    private Switch soundSwitch;
    private RadioGroup themeRadioGroup;
    private Button saveSettingsButton;
    private SharedPreferences sharedPreferences;
    private AudioManager audioManager;
    private SeekBar volumeSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayarlar
        );

        // Layout elemanlarını bul
        soundSwitch = findViewById(R.id.soundSwitch);
        themeRadioGroup = findViewById(R.id.themeRadioGroup);
        saveSettingsButton = findViewById(R.id.saveSettingsButton);
        volumeSeekBar = findViewById(R.id.volumeSeekBar);

        // SharedPreferences'ı başlat
        sharedPreferences = getSharedPreferences("game_settings", MODE_PRIVATE);

        // AudioManager'ı başlat
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Ses ayarlarını yap
        setupSoundSettings();

        // Temayı ayarlarını yap
        setupThemeSettings();

        // Kaydet butonuna tıklama olayını ayarla
        saveSettingsButton.setOnClickListener(v -> saveSettings());
    }

    private void setupSoundSettings() {
        // Kaydedilmiş ses ayarını yükle veya varsayılan değeri kullan
        boolean isSoundEnabled = sharedPreferences.getBoolean("sound_enabled", true);
        soundSwitch.setChecked(isSoundEnabled);

        // Ses seviyesi ayarını yap
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar.setMax(maxVolume);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar.setProgress(currentVolume);

        // Ses seviyesi değiştiğinde yapılacaklar
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Gerekirse burada bir şeyler yapabilirsiniz
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Gerekirse burada bir şeyler yapabilirsiniz
            }
        });
    }

    private void setupThemeSettings() {
        // Kaydedilmiş tema ayarını yükle veya varsayılan değeri kullan
        String currentTheme = sharedPreferences.getString("current_theme", "default");
        switch (currentTheme) {
            case "default":
                themeRadioGroup.check(R.id.themeDefault);
                break;
            case "circle":
                themeRadioGroup.check(R.id.themeCircle);
                break;
            // Diğer temalar için case'ler ekleyebilirsiniz
        }
    }

    private void saveSettings() {
        // Ayarları SharedPreferences'a kaydet
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("sound_enabled", soundSwitch.isChecked());

        int selectedThemeId = themeRadioGroup.getCheckedRadioButtonId();
        String selectedTheme = "default"; // Varsayılan değer
        if (selectedThemeId == R.id.themeDefault) {
            selectedTheme = "default";
        } else if (selectedThemeId == R.id.themeCircle) {
            selectedTheme = "circle";
        }        editor.putString("current_theme", selectedTheme);
        editor.apply();

        Toast.makeText(this, "Ayarlar kaydedildi", Toast.LENGTH_SHORT).show();

        // Ayarları uygula (örneğin, ses seviyesini ve temayı değiştir)
        applySettings();
        finish(); // Ayarlar ekranını kapat
    }

    private void applySettings() {
        // Ses ayarını uygula
        boolean isSoundEnabled = sharedPreferences.getBoolean("sound_enabled", true);
        if (isSoundEnabled) {
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        } else {
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        }

        // Tema ayarını uygula
        String currentTheme = sharedPreferences.getString("current_theme", "default");
        // Burada tema değiştirme kodunu yazın. Örneğin:
        // if (currentTheme.equals("circle")) {
        //     // Tema çember olarak ayarla
        // } else {
        //     // Varsayılan temayı ayarla
        // }
    }
}
