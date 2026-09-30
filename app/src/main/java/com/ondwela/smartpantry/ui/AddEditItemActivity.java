package com.ondwela.smartpantry.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ondwela.smartpantry.R;
import com.ondwela.smartpantry.db.PantryDbHelper;
import com.ondwela.smartpantry.logic.IngredientNormalizer;
import com.ondwela.smartpantry.model.PantryItem;

import java.util.Calendar;

/**
 * Add a new pantry item, or edit an existing one when launched with EXTRA_ITEM_ID.
 * The form is validated before anything is written to the database.
 */
public class AddEditItemActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "item_id";

    private PantryDbHelper db;
    private long itemId = -1;

    private TextInputLayout layoutName;
    private TextInputLayout layoutQuantity;
    private TextInputEditText editName;
    private TextInputEditText editQuantity;
    private TextInputEditText editExpiry;
    private Spinner spinnerUnit;
    private String expiryDate; // yyyy-MM-dd or null

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);
        db = PantryDbHelper.getInstance(this);

        layoutName = findViewById(R.id.layoutName);
        layoutQuantity = findViewById(R.id.layoutQuantity);
        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        editExpiry = findViewById(R.id.editExpiry);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        Button save = findViewById(R.id.buttonSave);
        Button clearDate = findViewById(R.id.buttonClearDate);

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, IngredientNormalizer.UNITS);
        spinnerUnit.setAdapter(unitAdapter);

        // The Intent tells us whether this is an add (no id) or an edit (id of the row).
        itemId = getIntent().getLongExtra(EXTRA_ITEM_ID, -1);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(itemId == -1 ? R.string.title_add_item : R.string.title_edit_item);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        if (itemId != -1) {
            loadExisting();
        }

        editExpiry.setOnClickListener(v -> showDatePicker());
        clearDate.setOnClickListener(v -> {
            expiryDate = null;
            editExpiry.setText("");
        });
        save.setOnClickListener(v -> saveItem());
    }

    private void loadExisting() {
        PantryItem item = db.getItem(itemId);
        if (item == null) {
            finish();
            return;
        }
        editName.setText(item.getName());
        double q = item.getQuantity();
        editQuantity.setText(q == Math.floor(q) ? String.valueOf((long) q) : String.valueOf(q));
        String unit = IngredientNormalizer.normalizeUnit(item.getUnit());
        for (int i = 0; i < IngredientNormalizer.UNITS.length; i++) {
            if (IngredientNormalizer.UNITS[i].equals(unit)) {
                spinnerUnit.setSelection(i);
            }
        }
        expiryDate = item.getExpiryDate();
        editExpiry.setText(expiryDate == null ? "" : expiryDate);
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            expiryDate = com.ondwela.smartpantry.logic.ExpiryUtil.format(year, month, day);
            editExpiry.setText(expiryDate);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    /** Returns true only when every field passes validation; shows errors otherwise. */
    private boolean validate() {
        boolean ok = true;
        layoutName.setError(null);
        layoutQuantity.setError(null);

        String name = editName.getText() == null ? "" : editName.getText().toString().trim();
        if (name.isEmpty()) {
            layoutName.setError(getString(R.string.err_name_required));
            ok = false;
        } else if (IngredientNormalizer.normalizeName(name).isEmpty()) {
            layoutName.setError(getString(R.string.err_name_letters));
            ok = false;
        }

        String qtyText = editQuantity.getText() == null ? "" : editQuantity.getText().toString().trim();
        if (qtyText.isEmpty()) {
            layoutQuantity.setError(getString(R.string.err_quantity_required));
            ok = false;
        } else {
            try {
                double qty = Double.parseDouble(qtyText);
                if (qty <= 0) {
                    layoutQuantity.setError(getString(R.string.err_quantity_positive));
                    ok = false;
                }
            } catch (NumberFormatException e) {
                layoutQuantity.setError(getString(R.string.err_quantity_invalid));
                ok = false;
            }
        }
        return ok;
    }

    private void saveItem() {
        if (!validate()) {
            return;
        }
        String name = editName.getText().toString().trim();
        double qty = Double.parseDouble(editQuantity.getText().toString().trim());
        String unit = (String) spinnerUnit.getSelectedItem();

        if (itemId == -1) {
            db.insertItem(new PantryItem(0, name, qty, unit, expiryDate));
            Toast.makeText(this, R.string.msg_item_added, Toast.LENGTH_SHORT).show();
        } else {
            db.updateItem(new PantryItem(itemId, name, qty, unit, expiryDate));
            Toast.makeText(this, R.string.msg_item_updated, Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
