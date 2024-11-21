package de.irmo.fakeandroid_id;

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

    private List<String> androidIdList;
    private ArrayAdapter<String> adapter;

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String customId = s.toString();
                saveButton.setEnabled(isValidHex(customId) && !customId.equals(getSavedAndroidID()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        String savedAndroidID = getSavedAndroidID();
        if (savedAndroidID != null) {
            androidIdTextView.setText(savedAndroidID);
        } else {
            androidIdTextView.setText("No fake Android ID defined");
        }

        skipRandomIdCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveSkipRandomId(isChecked);
        });

        boolean skipRandomId = getSkipRandomId();
        skipRandomIdCheckBox.setChecked(skipRandomId);

        // Keep the android id field always up to date with the prefs file
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener((sharedPrefs, key) -> {
            if (KEY_ANDROID_ID.equals(key)) {
                String updatedAndroidID = sharedPrefs.getString(KEY_ANDROID_ID, "No fake Android ID defined");
                androidIdTextView.setText(updatedAndroidID);
            }
        });

        // Update customIdEditText when an item from androidIdSpinner is selected
        androidIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedAndroidID = androidIdList.get(position);
                customIdEditText.setText(selectedAndroidID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed when nothing is selected.
            }
        });
    }

    private String generateRandomAndroidID() {
        long random64BitInt = (long) (Math.random() * Long.MAX_VALUE);
        return String.format("%016x", random64BitInt);
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

    private List<String> getSavedAndroidIdList() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
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
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder androidIdListString = new StringBuilder();
        for (String androidId : androidIdList) {
            androidIdListString.append(androidId).append(",");
        }
        editor.putString(KEY_ANDROID_ID_LIST, androidIdListString.toString());
        editor.apply();
    }

    private void updateAndroidIdList(String newAndroidID) {
        if (androidIdList.size() >= 20) {
            androidIdList.remove(androidIdList.size() - 1);
        }
        androidIdList.add(0, newAndroidID); // Add new ID at the top of the list
        saveAndroidIdList(androidIdList);
        adapter.notifyDataSetChanged();
    }

    private void saveSkipRandomId(boolean skipRandomId) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_SKIP_RANDOM_ID, skipRandomId);
        editor.apply();
    }

    private boolean getSkipRandomId() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_SKIP_RANDOM_ID, false);
    }

    private boolean isValidHex(String input) {
        return input.matches("[0-9a-fA-F]{16}");
    }
}
