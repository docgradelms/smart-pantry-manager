# Smart Pantry Manager

Android app (Java) for Mobile App Development 700, Richfield Graduate Institute of Technology.

Smart Pantry Manager helps reduce food waste. The user records the ingredients they have at home
and the app suggests recipes they can cook **right now**, with no shopping trip. A recipe is only
suggested when **every** ingredient is in the pantry in at least the required quantity.

## Features
- Add, edit and delete pantry items (name, quantity, unit, optional expiry date) with input validation
- Pantry list (RecyclerView + custom adapter) bound to the database
- 20 recipes pre-loaded on first run
- Suggested Recipes screen using the strict-matching rule
- Optional separate "Almost there" list (recipes missing exactly one ingredient)
- Recipe detail screen with ingredients and method
- Settings: expiring-soon highlighting and "Almost there" toggle
- Friendly message when no recipe matches the pantry

## Strict matching
Implemented in `logic/RecipeMatcher.java`:
- Names are normalised (lower case, plurals removed) so `Tomatoes` matches `tomato`.
- Units are converted to a common base (kg to g, l to ml, tsp, tbsp, cup). Weight and volume share one
  scale (1 g is treated as 1 ml); counted items (`pcs`) are a separate scale and never mix with weights.
- Duplicate pantry entries for the same ingredient are added together.
- If one ingredient is missing or the quantity is too small, the recipe is excluded.

Unit tests: `app/src/test/java/com/ondwela/smartpantry/logic/RecipeMatcherTest.java`.

## Database choice: SQLite
SQLite through `SQLiteOpenHelper` (`db/PantryDbHelper.java`). I chose it because the app is personal,
single-user and must work offline. It needs no server or account, data survives closing and reopening
the app, and it is the approach covered in the module's persistent data chapter.

Tables: `pantry_items`, `recipes`, `recipe_ingredients` (foreign key to `recipes`, cascade delete).

## Setup and run
1. Install Android Studio (Koala or newer) with an Android SDK for API 34.
2. Clone the repository: `git clone <repo-url>` and choose **File > Open** on the folder.
3. Wait for the Gradle sync to finish.
4. Create or start an emulator (API 24 or higher) or connect a phone with USB debugging.
5. Press **Run** on the `app` configuration.
6. Run the unit tests with **Run > Run tests in RecipeMatcherTest** or `./gradlew test`.

## Project structure
```
app/src/main/java/com/ondwela/smartpantry/
  MainActivity.java        host activity with bottom navigation
  ui/                      fragments and activities (Pantry, Suggested, Settings, Add/Edit, Detail)
  adapter/                 PantryAdapter, RecipeAdapter
  db/                      PantryDbHelper, SeedData
  logic/                   RecipeMatcher, IngredientNormalizer, ExpiryUtil, SettingsStore
  model/                   PantryItem, Recipe, RecipeIngredient
```

Out of scope by design: maps, GPS or any location features.

Author: Ondwela Rambuda (401902015)
