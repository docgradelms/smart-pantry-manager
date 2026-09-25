package com.ondwela.smartpantry.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ondwela.smartpantry.model.PantryItem;
import com.ondwela.smartpantry.model.Recipe;
import com.ondwela.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLite access layer. Owns the schema, seeds the recipes on first run
 * and exposes CRUD methods for pantry items.
 */
public class PantryDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;

    public static final String T_PANTRY = "pantry_items";
    public static final String T_RECIPES = "recipes";
    public static final String T_RECIPE_ING = "recipe_ingredients";

    private static PantryDbHelper instance;

    /** Single shared helper so every screen talks to the same connection. */
    public static synchronized PantryDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new PantryDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    private PantryDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + T_PANTRY + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "quantity REAL NOT NULL, "
                + "unit TEXT NOT NULL, "
                + "expiry_date TEXT)");
        db.execSQL("CREATE TABLE " + T_RECIPES + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "steps TEXT NOT NULL, "
                + "prep_minutes INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE " + T_RECIPE_ING + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "recipe_id INTEGER NOT NULL REFERENCES " + T_RECIPES + "(id) ON DELETE CASCADE, "
                + "name TEXT NOT NULL, "
                + "quantity REAL NOT NULL, "
                + "unit TEXT NOT NULL)");
        SeedData.insertAll(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + T_RECIPE_ING);
        db.execSQL("DROP TABLE IF EXISTS " + T_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + T_PANTRY);
        onCreate(db);
    }

    // ---------- Pantry CRUD ----------

    public long insertItem(PantryItem item) {
        return getWritableDatabase().insert(T_PANTRY, null, toValues(item));
    }

    public List<PantryItem> getAllItems() {
        List<PantryItem> items = new ArrayList<>();
        try (Cursor c = getReadableDatabase().query(T_PANTRY, null, null, null,
                null, null, "name COLLATE NOCASE ASC")) {
            while (c.moveToNext()) {
                items.add(fromCursor(c));
            }
        }
        return items;
    }

    public PantryItem getItem(long id) {
        try (Cursor c = getReadableDatabase().query(T_PANTRY, null, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null)) {
            return c.moveToFirst() ? fromCursor(c) : null;
        }
    }

    public int updateItem(PantryItem item) {
        return getWritableDatabase().update(T_PANTRY, toValues(item), "id = ?",
                new String[]{String.valueOf(item.getId())});
    }

    public int deleteItem(long id) {
        return getWritableDatabase().delete(T_PANTRY, "id = ?", new String[]{String.valueOf(id)});
    }

    private ContentValues toValues(PantryItem item) {
        ContentValues v = new ContentValues();
        v.put("name", item.getName());
        v.put("quantity", item.getQuantity());
        v.put("unit", item.getUnit());
        v.put("expiry_date", item.getExpiryDate());
        return v;
    }

    private PantryItem fromCursor(Cursor c) {
        return new PantryItem(
                c.getLong(c.getColumnIndexOrThrow("id")),
                c.getString(c.getColumnIndexOrThrow("name")),
                c.getDouble(c.getColumnIndexOrThrow("quantity")),
                c.getString(c.getColumnIndexOrThrow("unit")),
                c.getString(c.getColumnIndexOrThrow("expiry_date")));
    }

    // ---------- Recipes (read only) ----------

    /** Loads every recipe together with its ingredient lines. */
    public List<Recipe> getAllRecipes() {
        SQLiteDatabase db = getReadableDatabase();
        List<Recipe> recipes = new ArrayList<>();
        try (Cursor c = db.query(T_RECIPES, null, null, null, null, null, "name ASC")) {
            while (c.moveToNext()) {
                recipes.add(recipeFromCursor(c));
            }
        }
        for (Recipe r : recipes) {
            loadIngredients(db, r);
        }
        return recipes;
    }

    public Recipe getRecipe(long id) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query(T_RECIPES, null, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null)) {
            if (!c.moveToFirst()) {
                return null;
            }
            Recipe r = recipeFromCursor(c);
            loadIngredients(db, r);
            return r;
        }
    }

    private Recipe recipeFromCursor(Cursor c) {
        return new Recipe(
                c.getLong(c.getColumnIndexOrThrow("id")),
                c.getString(c.getColumnIndexOrThrow("name")),
                c.getString(c.getColumnIndexOrThrow("steps")),
                c.getInt(c.getColumnIndexOrThrow("prep_minutes")));
    }

    private void loadIngredients(SQLiteDatabase db, Recipe recipe) {
        try (Cursor c = db.query(T_RECIPE_ING, null, "recipe_id = ?",
                new String[]{String.valueOf(recipe.getId())}, null, null, "id ASC")) {
            while (c.moveToNext()) {
                recipe.getIngredients().add(new RecipeIngredient(
                        c.getString(c.getColumnIndexOrThrow("name")),
                        c.getDouble(c.getColumnIndexOrThrow("quantity")),
                        c.getString(c.getColumnIndexOrThrow("unit"))));
            }
        }
    }
}
