package com.example.smartpantrymanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddIngredientDialog extends DialogFragment {

    public interface AddIngredientListener {
        void onIngredientAdded(Ingredient ingredient);
    }

    private AddIngredientListener listener;

    public void setAddIngredientListener(AddIngredientListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_ingredient, null);

        EditText editName = view.findViewById(R.id.editIngredientName);
        EditText editQuantity = view.findViewById(R.id.editIngredientQuantity);
        EditText editUnit = view.findViewById(R.id.editIngredientUnit);
        EditText editExpiry = view.findViewById(R.id.editIngredientExpiry);

        builder.setView(view)
                .setTitle("Add Ingredient")
                .setPositiveButton("Add", (dialog, id) -> {
                    String name = editName.getText().toString().trim();
                    double quantity = Double.parseDouble(editQuantity.getText().toString().trim());
                    String unit = editUnit.getText().toString().trim();
                    String expiry = editExpiry.getText().toString().trim();

                    Ingredient ingredient = new Ingredient(0, name, quantity, unit, expiry);
                    if (listener != null) {
                        listener.onIngredientAdded(ingredient);
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        return builder.create();
    }
}

