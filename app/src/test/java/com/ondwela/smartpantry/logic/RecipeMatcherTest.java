package com.ondwela.smartpantry.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.ondwela.smartpantry.model.PantryItem;
import com.ondwela.smartpantry.model.Recipe;
import com.ondwela.smartpantry.model.RecipeIngredient;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeMatcherTest {

    private Recipe recipe(String name, RecipeIngredient... ings) {
        Recipe r = new Recipe(1, name, "steps", 10);
        r.getIngredients().addAll(Arrays.asList(ings));
        return r;
    }

    private PantryItem item(String name, double qty, String unit) {
        return new PantryItem(0, name, qty, unit, null);
    }

    @Test
    public void missingOneIngredient_isNotSuggested() {
        Recipe r = recipe("Omelette",
                new RecipeIngredient("egg", 3, "pcs"),
                new RecipeIngredient("cheese", 50, "g"));
        List<PantryItem> pantry = Arrays.asList(item("Eggs", 6, "pcs"));
        assertTrue(RecipeMatcher.strictMatches(Arrays.asList(r), pantry).isEmpty());
        assertEquals(1, RecipeMatcher.almostThere(Arrays.asList(r), pantry).size());
    }

    @Test
    public void allIngredientsPresent_isSuggested() {
        Recipe r = recipe("Omelette",
                new RecipeIngredient("egg", 3, "pcs"),
                new RecipeIngredient("cheese", 50, "g"));
        List<PantryItem> pantry = Arrays.asList(item("egg", 3, "pcs"), item("Cheese", 100, "g"));
        assertEquals(1, RecipeMatcher.strictMatches(Arrays.asList(r), pantry).size());
    }

    @Test
    public void pluralAndCaseDifferences_stillMatch() {
        Recipe r = recipe("Pasta", new RecipeIngredient("tomato", 2, "pcs"));
        List<PantryItem> pantry = Arrays.asList(item("  TOMATOES ", 4, "pcs"));
        assertEquals(1, RecipeMatcher.strictMatches(Arrays.asList(r), pantry).size());
    }

    @Test
    public void unitDifferences_areConverted() {
        Recipe r = recipe("Bread", new RecipeIngredient("flour", 500, "g"));
        assertEquals(1, RecipeMatcher.strictMatches(Arrays.asList(r),
                Arrays.asList(item("flour", 1, "kg"))).size());
        Recipe spoon = recipe("Toast", new RecipeIngredient("honey", 1, "tbsp"));
        assertEquals(1, RecipeMatcher.strictMatches(Arrays.asList(spoon),
                Arrays.asList(item("honey", 20, "ml"))).size());
    }

    @Test
    public void notEnoughQuantity_isRejected() {
        Recipe r = recipe("Pancakes", new RecipeIngredient("flour", 200, "g"));
        List<PantryItem> pantry = Arrays.asList(item("flour", 150, "g"));
        assertTrue(RecipeMatcher.strictMatches(Arrays.asList(r), pantry).isEmpty());
    }

    @Test
    public void duplicateEntries_areSummed() {
        Recipe r = recipe("Pancakes", new RecipeIngredient("flour", 200, "g"));
        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(item("flour", 120, "g"));
        pantry.add(item("Flour", 100, "g"));
        assertEquals(1, RecipeMatcher.strictMatches(Arrays.asList(r), pantry).size());
    }

    @Test
    public void emptyPantry_matchesNothing() {
        Recipe r = recipe("Toast", new RecipeIngredient("bread", 2, "pcs"));
        assertTrue(RecipeMatcher.strictMatches(Arrays.asList(r), new ArrayList<PantryItem>()).isEmpty());
    }
}
