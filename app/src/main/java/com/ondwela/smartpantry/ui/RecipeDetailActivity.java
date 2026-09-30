package com.ondwela.smartpantry.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.ondwela.smartpantry.R;
import com.ondwela.smartpantry.db.PantryDbHelper;
import com.ondwela.smartpantry.model.Recipe;
import com.ondwela.smartpantry.model.RecipeIngredient;

/** Full ingredient list and method for the recipe id passed in the Intent. */
public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        long id = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1);
        Recipe recipe = PantryDbHelper.getInstance(this).getRecipe(id);
        if (recipe == null) {
            finish();
            return;
        }

        ((TextView) findViewById(R.id.textName)).setText(recipe.getName());
        ((TextView) findViewById(R.id.textMeta)).setText(getString(R.string.prep_minutes, recipe.getPrepMinutes()));
        ((TextView) findViewById(R.id.textSteps)).setText(recipe.getSteps());

        StringBuilder sb = new StringBuilder();
        for (RecipeIngredient ing : recipe.getIngredients()) {
            if (sb.length() > 0) {
                sb.append('\n');
            }
            double q = ing.getQuantity();
            String qty = q == Math.floor(q) ? String.valueOf((long) q) : String.valueOf(q);
            sb.append("• ").append(qty).append(' ').append(ing.getUnit())
                    .append(' ').append(ing.getName());
        }
        ((TextView) findViewById(R.id.textIngredients)).setText(sb.toString());
    }
}
