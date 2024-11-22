package de.irmo.fakeandroid_id;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "FakeAndroidIDPrefs";
    private static final String KEY_ANDROID_ID = "android_id";
    private static final String KEY_ANDROID_ID_LIST = "android_id_list";
    private static final String KEY_SKIP_RANDOM_ID = "skip_random_id";

    private SharedPreferences sharedPreferences;
    private List<String> androidIdList;
    private ArrayAdapter<String> adapter;


    private void showErrorAndExit() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("The module is not activated in LSPosed. Please activate it and select at least one app where the Android ID should be faked. The app will now close.")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> finish()) // Close the app when the user clicks "OK"
                .show();
    }

    private void wiggleLogo(View view) {
        ObjectAnimator wiggleAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, -10f, 10f, -10f, 10f, -10f, 0f);
        wiggleAnimator.setDuration(500);
        wiggleAnimator.start();
    }

    // Check if the LSPosed module is loaded
    private boolean isLSPosedModuleLoaded() {
        try {
            sharedPreferences = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_WORLD_READABLE);
        } catch (SecurityException ignored) {
            // The new XSharedPreferences is not enabled or module's not loading
            sharedPreferences = null;
        }
        return sharedPreferences != null; // Returns true if the module is loaded and preferences can be accessed
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isLSPosedModuleLoaded()) {
            showErrorAndExit();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        Button generateButton = findViewById(R.id.generate_button);
        Button saveButton = findViewById(R.id.save_button);
        TextView androidIdTextView = findViewById(R.id.android_id_textview);
        EditText customIdEditText = findViewById(R.id.custom_id_edittext);
        Spinner androidIdSpinner = findViewById(R.id.android_id_spinner);
        CheckBox skipRandomIdCheckBox = findViewById(R.id.skip_random_id_checkbox);

        androidIdList = getSavedAndroidIdList();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, androidIdList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        androidIdSpinner.setAdapter(adapter);

        generateButton.setOnClickListener(v -> {
            String newAndroidID = generateRandomAndroidID();
            androidIdTextView.setText(newAndroidID);
            ImageView launcherLogo = findViewById(R.id.ic_launcher_logo);
            wiggleLogo(launcherLogo);
            updateAndroidIdList(newAndroidID);
        });

        saveButton.setOnClickListener(v -> {
            String customId = customIdEditText.getText().toString();
            if (isValidHex(customId)) {
                saveAndroidID(customId);
                androidIdTextView.setText(customId);
                updateAndroidIdList(customId);
            }
        });

        customIdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String customId = s.toString();
                saveButton.setEnabled(isValidHex(customId) && !customId.equals(getSavedAndroidID()));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        String savedAndroidID = getSavedAndroidID();
        if (savedAndroidID != null) {
            androidIdTextView.setText(savedAndroidID);
        } else {
            androidIdTextView.setText("No fake Android ID defined");
        }

        skipRandomIdCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> saveSkipRandomId(isChecked));
        skipRandomIdCheckBox.setChecked(getSkipRandomId());

        sharedPreferences.registerOnSharedPreferenceChangeListener((sharedPrefs, key) -> {
            if (KEY_ANDROID_ID.equals(key)) {
                String updatedAndroidID = sharedPrefs.getString(KEY_ANDROID_ID, "No fake Android ID defined");
                androidIdTextView.setText(updatedAndroidID);
            }
        });

        androidIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedAndroidID = androidIdList.get(position);
                customIdEditText.setText(selectedAndroidID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private String generateRandomAndroidID() {
        long random64BitInt = (long) (Math.random() * Long.MAX_VALUE);
        return String.format("%016x", random64BitInt);
    }

    private void saveAndroidID(String androidID) {
        sharedPreferences.edit().putString(KEY_ANDROID_ID, androidID).apply();
    }

    private String getSavedAndroidID() {
        return sharedPreferences.getString(KEY_ANDROID_ID, null);
    }

    private List<String> getSavedAndroidIdList() {
        String androidIdListString = sharedPreferences.getString(KEY_ANDROID_ID_LIST, "");
        String[] androidIdArray = androidIdListString.split(",");
        List<String> androidIdList = new ArrayList<>();
        for (String androidId : androidIdArray) {
            if (!androidId.isEmpty()) {
                androidIdList.add(androidId);
            }
        }
        return androidIdList;
    }

    private void saveAndroidIdList(List<String> androidIdList) {
        StringBuilder androidIdListString = new StringBuilder();
        for (String androidId : androidIdList) {
            androidIdListString.append(androidId).append(",");
        }
        sharedPreferences.edit().putString(KEY_ANDROID_ID_LIST, androidIdListString.toString()).apply();
    }

    private void updateAndroidIdList(String newAndroidID) {
        // Check if the ID is already in the list
        if (androidIdList.contains(newAndroidID)) {
            // Remove the existing ID
            androidIdList.remove(newAndroidID);
        }
        // Add the new ID to the top
        androidIdList.add(0, newAndroidID);

        // Ensure the list doesn't exceed 20 entries
        if (androidIdList.size() > 20) {
            androidIdList.remove(androidIdList.size() - 1);
        }

        // Save the updated list and notify the adapter
        saveAndroidIdList(androidIdList);
        adapter.notifyDataSetChanged();
    }

    private void saveSkipRandomId(boolean skipRandomId) {
        sharedPreferences.edit().putBoolean(KEY_SKIP_RANDOM_ID, skipRandomId).apply();
    }

    private boolean getSkipRandomId() {
        return sharedPreferences.getBoolean(KEY_SKIP_RANDOM_ID, false);
    }

    private boolean isValidHex(String input) {
        return input.matches("[0-9a-fA-F]{16}");
    }
}
