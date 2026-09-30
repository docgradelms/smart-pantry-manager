package com.ondwela.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ondwela.smartpantry.R;
import com.ondwela.smartpantry.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/** Lists recipes on the Suggested Recipes screen, with an optional "Missing: x" line. */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    public interface Listener {
        void onRecipeClick(Recipe recipe);
    }

    private final List<Recipe> recipes = new ArrayList<>();
    private final List<String> missingNotes = new ArrayList<>();
    private final Listener listener;

    public RecipeAdapter(Listener listener) {
        this.listener = listener;
    }

    /** missing may be null for the strict list, or one entry per recipe for "Almost there". */
    public void setRecipes(List<Recipe> newRecipes, List<String> missing) {
        recipes.clear();
        missingNotes.clear();
        recipes.addAll(newRecipes);
        for (int i = 0; i < newRecipes.size(); i++) {
            missingNotes.add(missing == null ? null : missing.get(i));
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder h, int position) {
        final Recipe recipe = recipes.get(position);
        h.name.setText(recipe.getName());
        h.meta.setText(h.itemView.getContext().getString(R.string.prep_minutes, recipe.getPrepMinutes())
                + "  |  " + recipe.getIngredients().size() + " ingredients");

        String missing = missingNotes.get(position);
        if (missing == null) {
            h.missing.setVisibility(View.GONE);
        } else {
            h.missing.setVisibility(View.VISIBLE);
            h.missing.setText(h.itemView.getContext().getString(R.string.almost_missing, missing));
        }
        h.itemView.setOnClickListener(v -> listener.onRecipeClick(recipe));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class RecipeHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView meta;
        final TextView missing;

        RecipeHolder(View v) {
            super(v);
            name = v.findViewById(R.id.textRecipeName);
            meta = v.findViewById(R.id.textRecipeMeta);
            missing = v.findViewById(R.id.textRecipeMissing);
        }
    }
}
