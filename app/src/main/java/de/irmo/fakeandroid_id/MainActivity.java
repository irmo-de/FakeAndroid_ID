package de.irmo.fakeandroid_id;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "FakeAndroidIDPrefs";
    private static final String KEY_ANDROID_ID = "android_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button generateButton = findViewById(R.id.generate_button);
        TextView androidIdTextView = findViewById(R.id.android_id_textview);

        generateButton.setOnClickListener(v -> {
            String newAndroidID = generateRandomAndroidID();
            saveAndroidID(newAndroidID);
            androidIdTextView.setText(newAndroidID);
        });

        String savedAndroidID = getSavedAndroidID();
        if (savedAndroidID != null) {
            androidIdTextView.setText(savedAndroidID);
        }
    }

    private String generateRandomAndroidID() {
        return UUID.randomUUID().toString();
    }

    private void saveAndroidID(String androidID) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ANDROID_ID, androidID);
        editor.apply();
    }

    private String getSavedAndroidID() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ANDROID_ID, null);
    }
}
