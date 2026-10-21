package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchExpiryAlerts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchExpiryAlerts = findViewById(R.id.switchExpiryAlerts);

        switchExpiryAlerts.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "Expiry alerts enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Expiry alerts disabled", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
