package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class AddIngredientActivity extends AppCompatActivity {

    EditText editTextName, editTextQuantity, editTextUnit, editTextExpiry;
    Button buttonSave;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        dbHelper = new DataBaseHelper(this);

        editTextName = findViewById(R.id.editTextName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextUnit = findViewById(R.id.editTextUnit);
        editTextExpiry = findViewById(R.id.editTextExpiry);
        buttonSave = findViewById(R.id.buttonSave);

        // Expiry date picker
        editTextExpiry.setFocusable(false);
        editTextExpiry.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                        editTextExpiry.setText(date);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePicker.show();
        });

        buttonSave.setOnClickListener(v -> saveIngredient());
    }

    private void saveIngredient() {
        String name = editTextName.getText().toString().trim();
        String quantityStr = editTextQuantity.getText().toString().trim();
        String unit = editTextUnit.getText().toString().trim();
        String expiry = editTextExpiry.getText().toString().trim();

        if (name.isEmpty() || quantityStr.isEmpty() || unit.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use the helper method to insert
        Ingredient ingredient = new Ingredient(0, name, quantity, unit, expiry);
        long result = dbHelper.insertIngredient(ingredient);

        if (result != -1) {
            Toast.makeText(this, name + " added to pantry!", Toast.LENGTH_SHORT).show();
            finish(); // return to MainActivity
        } else {
            Toast.makeText(this, "Error saving ingredient", Toast.LENGTH_SHORT).show();
        }

    }
}
