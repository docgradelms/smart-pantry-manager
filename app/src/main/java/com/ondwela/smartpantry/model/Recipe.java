package com.ondwela.smartpantry.model;

import java.util.ArrayList;
import java.util.List;

/** A recipe with its required ingredients and preparation steps. */
public class Recipe {
    private final long id;
    private final String name;
    private final String steps;
    private final int prepMinutes;
    private final List<RecipeIngredient> ingredients = new ArrayList<>();

    public Recipe(long id, String name, String steps, int prepMinutes) {
        this.id = id;
        this.name = name;
        this.steps = steps;
        this.prepMinutes = prepMinutes;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getSteps() { return steps; }
    public int getPrepMinutes() { return prepMinutes; }
    public List<RecipeIngredient> getIngredients() { return ingredients; }
}
