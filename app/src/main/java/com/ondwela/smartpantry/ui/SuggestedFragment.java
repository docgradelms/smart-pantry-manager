package com.ondwela.smartpantry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.ondwela.smartpantry.R;
import com.ondwela.smartpantry.adapter.RecipeAdapter;
import com.ondwela.smartpantry.db.PantryDbHelper;
import com.ondwela.smartpantry.logic.RecipeMatcher;
import com.ondwela.smartpantry.logic.SettingsStore;
import com.ondwela.smartpantry.model.PantryItem;
import com.ondwela.smartpantry.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows only recipes the pantry can fully satisfy. An optional, clearly
 * separate "Almost there" view lists recipes missing exactly one ingredient.
 */
public class SuggestedFragment extends Fragment implements RecipeAdapter.Listener {

    private PantryDbHelper db;
    private SettingsStore settings;
    private RecipeAdapter adapter;
    private RecyclerView recycler;
    private TextView textEmpty;
    private MaterialButtonToggleGroup toggle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_suggested, container, false);
        db = PantryDbHelper.getInstance(requireContext());
        settings = new SettingsStore(requireContext());

        recycler = root.findViewById(R.id.recyclerRecipes);
        textEmpty = root.findViewById(R.id.textEmpty);
        toggle = root.findViewById(R.id.toggleMode);

        adapter = new RecipeAdapter(this);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setAdapter(adapter);

        toggle.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                refresh();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        toggle.setVisibility(settings.isAlmostThereOn() ? View.VISIBLE : View.GONE);
        if (!settings.isAlmostThereOn()) {
            toggle.check(R.id.buttonReady);
        }
        refresh();
    }

    private void refresh() {
        List<Recipe> allRecipes = db.getAllRecipes();
        List<PantryItem> pantry = db.getAllItems();
        boolean showAlmost = toggle.getCheckedButtonId() == R.id.buttonAlmost;

        if (showAlmost) {
            List<RecipeMatcher.Result> results = RecipeMatcher.almostThere(allRecipes, pantry);
            List<Recipe> recipes = new ArrayList<>();
            List<String> missing = new ArrayList<>();
            for (RecipeMatcher.Result r : results) {
                recipes.add(r.recipe);
                missing.add(r.missing.get(0));
            }
            adapter.setRecipes(recipes, missing);
            showEmptyState(recipes.isEmpty(), R.string.empty_almost);
        } else {
            List<Recipe> recipes = RecipeMatcher.strictMatches(allRecipes, pantry);
            adapter.setRecipes(recipes, null);
            showEmptyState(recipes.isEmpty(), R.string.empty_suggested);
        }
    }

    private void showEmptyState(boolean empty, int messageRes) {
        textEmpty.setText(messageRes);
        textEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        recycler.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(requireContext(), RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }
}
