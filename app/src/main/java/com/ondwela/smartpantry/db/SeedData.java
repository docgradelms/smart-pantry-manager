package com.ondwela.smartpantry.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Pre-loads the recipe collection the first time the database is created.
 * Each ingredient is written as "name|quantity|unit".
 */
final class SeedData {

    private SeedData() { }

    static void insertAll(SQLiteDatabase db) {
        add(db, "Scrambled Eggs on Toast", 10,
                "1. Whisk the eggs with the milk and a pinch of salt.\n"
                + "2. Melt the butter in a pan on medium heat.\n"
                + "3. Pour in the eggs and stir gently until just set.\n"
                + "4. Toast the bread and serve the eggs on top.",
                "egg|3|pcs", "milk|50|ml", "butter|10|g", "bread|2|pcs", "salt|1|tsp");

        add(db, "Plain Pancakes", 20,
                "1. Mix flour, sugar and salt in a bowl.\n"
                + "2. Whisk in the egg and milk until the batter is smooth.\n"
                + "3. Heat a little oil in a pan.\n"
                + "4. Pour in a ladle of batter and cook both sides until golden.",
                "flour|200|g", "egg|2|pcs", "milk|300|ml", "sugar|1|tbsp", "salt|1|tsp", "oil|1|tbsp");

        add(db, "Cheese Omelette", 10,
                "1. Beat the eggs with salt and pepper.\n"
                + "2. Melt the butter in a pan.\n"
                + "3. Pour in the eggs and cook until the base sets.\n"
                + "4. Add grated cheese, fold over and serve.",
                "egg|3|pcs", "cheese|50|g", "butter|10|g", "salt|1|tsp", "pepper|1|tsp");

        add(db, "Tomato Pasta", 25,
                "1. Boil the pasta in salted water until soft.\n"
                + "2. Fry the onion and garlic in oil until soft.\n"
                + "3. Add chopped tomatoes and simmer for 10 minutes.\n"
                + "4. Mix the sauce through the drained pasta.",
                "pasta|250|g", "tomato|4|pcs", "onion|1|pcs", "garlic|2|pcs", "oil|2|tbsp", "salt|1|tsp");

        add(db, "Garlic Butter Rice", 20,
                "1. Cook the rice in salted water until fluffy.\n"
                + "2. Melt the butter and fry the garlic for one minute.\n"
                + "3. Stir the garlic butter through the hot rice.",
                "rice|200|g", "butter|30|g", "garlic|3|pcs", "salt|1|tsp");

        add(db, "Egg Fried Rice", 15,
                "1. Heat the oil in a pan and fry the onion.\n"
                + "2. Push the onion aside and scramble the eggs.\n"
                + "3. Add cooked rice, salt and pepper and stir-fry for 3 minutes.",
                "rice|250|g", "egg|2|pcs", "onion|1|pcs", "oil|2|tbsp", "salt|1|tsp", "pepper|1|tsp");

        add(db, "Pap and Tomato Gravy", 30,
                "1. Bring water and salt to the boil and stir in the maize meal.\n"
                + "2. Cover and cook on low heat, stirring now and then, until thick.\n"
                + "3. Fry the onion in oil, add chopped tomatoes and cook into a gravy.\n"
                + "4. Serve the gravy over the pap.",
                "maize meal|250|g", "tomato|3|pcs", "onion|1|pcs", "oil|2|tbsp", "salt|1|tsp");

        add(db, "Mashed Potatoes", 25,
                "1. Peel and cut the potatoes, then boil until soft.\n"
                + "2. Drain and mash with butter and warm milk.\n"
                + "3. Season with salt and pepper.",
                "potato|500|g", "butter|30|g", "milk|100|ml", "salt|1|tsp", "pepper|1|tsp");

        add(db, "Cheese Toastie", 8,
                "1. Butter the outside of the bread slices.\n"
                + "2. Place the cheese between the slices.\n"
                + "3. Toast in a pan until golden and melted.",
                "bread|2|pcs", "cheese|60|g", "butter|10|g");

        add(db, "Banana Oat Porridge", 10,
                "1. Simmer the oats in milk, stirring until creamy.\n"
                + "2. Slice the banana on top.\n"
                + "3. Drizzle with honey.",
                "oats|80|g", "milk|300|ml", "banana|1|pcs", "honey|1|tbsp");

        add(db, "Banana Pancakes", 15,
                "1. Mash the bananas and mix with the eggs.\n"
                + "2. Stir in the flour until just combined.\n"
                + "3. Fry spoonfuls in a little oil until golden on both sides.",
                "banana|2|pcs", "egg|2|pcs", "flour|100|g", "oil|1|tbsp");

        add(db, "Tuna Mayo Sandwich", 5,
                "1. Drain the tuna and mix with the mayonnaise.\n"
                + "2. Season with pepper.\n"
                + "3. Spread on the bread and close the sandwich.",
                "tuna|1|can", "mayonnaise|2|tbsp", "bread|2|pcs", "pepper|1|tsp");

        add(db, "Peanut Butter Toast", 5,
                "1. Toast the bread.\n"
                + "2. Spread the peanut butter on top.\n"
                + "3. Add sliced banana and honey if you like.",
                "bread|2|pcs", "peanut butter|2|tbsp", "honey|1|tsp");

        add(db, "Chicken and Rice Bowl", 35,
                "1. Season the chicken with salt and pepper and fry in oil until cooked.\n"
                + "2. Cook the rice in salted water.\n"
                + "3. Slice the chicken and serve on the rice with fried onion.",
                "chicken|300|g", "rice|200|g", "onion|1|pcs", "oil|2|tbsp", "salt|1|tsp", "pepper|1|tsp");

        add(db, "Bean and Tomato Stew", 30,
                "1. Fry the onion and garlic in oil.\n"
                + "2. Add chopped tomatoes and cook for 5 minutes.\n"
                + "3. Stir in the beans and simmer for 15 minutes.\n"
                + "4. Season with salt and pepper.",
                "beans|400|g", "tomato|3|pcs", "onion|1|pcs", "garlic|2|pcs", "oil|1|tbsp", "salt|1|tsp");

        add(db, "Potato Wedges", 40,
                "1. Cut the potatoes into wedges.\n"
                + "2. Toss with oil, salt and pepper.\n"
                + "3. Bake at 200 degrees Celsius for 30 minutes, turning once.",
                "potato|500|g", "oil|2|tbsp", "salt|1|tsp", "pepper|1|tsp");

        add(db, "Lemon Sugar Crepes", 20,
                "1. Whisk the flour, egg and milk into a thin batter.\n"
                + "2. Fry thin layers in buttered pan.\n"
                + "3. Sprinkle with sugar and squeeze lemon juice over.",
                "flour|150|g", "egg|2|pcs", "milk|250|ml", "butter|20|g", "sugar|2|tbsp", "lemon|1|pcs");

        add(db, "Carrot and Onion Soup", 35,
                "1. Fry the chopped onion in butter.\n"
                + "2. Add sliced carrots and water and simmer until soft.\n"
                + "3. Blend until smooth and season with salt and pepper.",
                "carrot|4|pcs", "onion|1|pcs", "butter|20|g", "salt|1|tsp", "pepper|1|tsp");

        add(db, "Avocado Toast", 5,
                "1. Toast the bread.\n"
                + "2. Mash the avocado with lemon juice and salt.\n"
                + "3. Spread over the toast.",
                "bread|2|pcs", "avocado|1|pcs", "lemon|1|pcs", "salt|1|tsp");

        add(db, "Yoghurt Banana Bowl", 5,
                "1. Spoon the yoghurt into a bowl.\n"
                + "2. Top with sliced banana and oats.\n"
                + "3. Finish with honey.",
                "yoghurt|200|g", "banana|1|pcs", "oats|30|g", "honey|1|tbsp");

        add(db, "Cheesy Mashed Potato Bake", 45,
                "1. Boil and mash the potatoes with butter and milk.\n"
                + "2. Spread in a dish and cover with grated cheese.\n"
                + "3. Bake at 180 degrees Celsius for 20 minutes until golden.",
                "potato|600|g", "cheese|100|g", "butter|30|g", "milk|100|ml", "salt|1|tsp");
    }

    private static void add(SQLiteDatabase db, String name, int minutes, String steps,
                            String... ingredients) {
        ContentValues r = new ContentValues();
        r.put("name", name);
        r.put("steps", steps);
        r.put("prep_minutes", minutes);
        long recipeId = db.insert(PantryDbHelper.T_RECIPES, null, r);

        for (String line : ingredients) {
            String[] p = line.split("\\|");
            ContentValues v = new ContentValues();
            v.put("recipe_id", recipeId);
            v.put("name", p[0]);
            v.put("quantity", Double.parseDouble(p[1]));
            v.put("unit", p[2]);
            db.insert(PantryDbHelper.T_RECIPE_ING, null, v);
        }
    }
}
