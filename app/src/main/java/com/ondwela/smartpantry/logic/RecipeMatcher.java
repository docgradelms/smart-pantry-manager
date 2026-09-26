package com.ondwela.smartpantry.logic;

import com.ondwela.smartpantry.model.PantryItem;
import com.ondwela.smartpantry.model.Recipe;
import com.ondwela.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The core business rule of the app: a recipe is only suggested when EVERY
 * ingredient it needs is in the pantry in at least the required quantity.
 */
public final class RecipeMatcher {

    private RecipeMatcher() { }

    /** A recipe together with the ingredient names the pantry is missing for it. */
    public static class Result {
        public final Recipe recipe;
        public final List<String> missing;

        Result(Recipe recipe, List<String> missing) {
            this.recipe = recipe;
            this.missing = missing;
        }
    }

    /** Recipes the user can cook right now (nothing missing). */
    public static List<Recipe> strictMatches(List<Recipe> recipes, List<PantryItem> pantry) {
        List<Recipe> out = new ArrayList<>();
        Map<String, Double> stock = buildStock(pantry);
        for (Recipe r : recipes) {
            if (missingIngredients(r, stock).isEmpty()) {
                out.add(r);
            }
        }
        return out;
    }

    /** Recipes missing exactly one ingredient. Kept separate from the strict list. */
    public static List<Result> almostThere(List<Recipe> recipes, List<PantryItem> pantry) {
        List<Result> out = new ArrayList<>();
        Map<String, Double> stock = buildStock(pantry);
        for (Recipe r : recipes) {
            List<String> missing = missingIngredients(r, stock);
            if (missing.size() == 1) {
                out.add(new Result(r, missing));
            }
        }
        return out;
    }

    /**
     * Totals the pantry per normalised ingredient, keyed by "name|scale" so that
     * grams/millilitres and counted items are never mixed together.
     */
    private static Map<String, Double> buildStock(List<PantryItem> pantry) {
        Map<String, Double> stock = new HashMap<>();
        for (PantryItem item : pantry) {
            String key = stockKey(item.getName(), item.getUnit());
            double amount = IngredientNormalizer.toBaseAmount(item.getQuantity(), item.getUnit());
            Double existing = stock.get(key);
            stock.put(key, (existing == null ? 0.0 : existing) + amount);
        }
        return stock;
    }

    private static String stockKey(String name, String unit) {
        String scale = IngredientNormalizer.isCountUnit(unit) ? "count" : "measure";
        return IngredientNormalizer.normalizeName(name) + "|" + scale;
    }

    private static List<String> missingIngredients(Recipe recipe, Map<String, Double> stock) {
        List<String> missing = new ArrayList<>();
        for (RecipeIngredient need : recipe.getIngredients()) {
            Double have = stock.get(stockKey(need.getName(), need.getUnit()));
            double required = IngredientNormalizer.toBaseAmount(need.getQuantity(), need.getUnit());
            if (have == null || have + 1e-9 < required) {
                missing.add(need.getName());
            }
        }
        return missing;
    }
}
