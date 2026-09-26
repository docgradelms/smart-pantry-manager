package com.ondwela.smartpantry.logic;

import java.util.Locale;

/**
 * Cleans up ingredient names and units so that simple real-world differences
 * ("Tomatoes" vs "tomato", "Kg" vs "g") do not break matching.
 */
public final class IngredientNormalizer {

    /** Units offered in the Add/Edit spinner. */
    public static final String[] UNITS = {"g", "kg", "ml", "l", "tsp", "tbsp", "cup", "pcs"};

    private IngredientNormalizer() { }

    /** Lower-case, strip symbols, collapse spaces and make every word singular. */
    public static String normalizeName(String raw) {
        if (raw == null) {
            return "";
        }
        String cleaned = raw.toLowerCase(Locale.ROOT).replaceAll("[^a-z ]", " ").trim();
        if (cleaned.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String word : cleaned.split("\\s+")) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(singular(word));
        }
        return sb.toString();
    }

    private static String singular(String w) {
        if (w.length() <= 3) {
            return w;
        }
        if (w.endsWith("ies")) {
            return w.substring(0, w.length() - 3) + "y";      // berries -> berry
        }
        if (w.endsWith("oes")) {
            return w.substring(0, w.length() - 2);            // tomatoes -> tomato
        }
        if (w.endsWith("ches") || w.endsWith("shes") || w.endsWith("sses") || w.endsWith("xes")) {
            return w.substring(0, w.length() - 2);            // sandwiches -> sandwich
        }
        if (w.endsWith("ss") || w.endsWith("us")) {
            return w;
        }
        if (w.endsWith("s")) {
            return w.substring(0, w.length() - 1);            // eggs -> egg
        }
        return w;
    }

    /** Maps the many ways of writing a unit to one of: g, kg, ml, l, tsp, tbsp, cup, pcs. */
    public static String normalizeUnit(String raw) {
        if (raw == null) {
            return "pcs";
        }
        String u = raw.toLowerCase(Locale.ROOT).replace(".", "").trim();
        switch (u) {
            case "g": case "gram": case "grams": case "gm":
                return "g";
            case "kg": case "kilo": case "kilos": case "kilogram": case "kilograms":
                return "kg";
            case "ml": case "millilitre": case "millilitres": case "milliliter": case "milliliters":
                return "ml";
            case "l": case "litre": case "litres": case "liter": case "liters":
                return "l";
            case "tsp": case "teaspoon": case "teaspoons":
                return "tsp";
            case "tbsp": case "tablespoon": case "tablespoons":
                return "tbsp";
            case "cup": case "cups":
                return "cup";
            default:
                return "pcs"; // pcs, piece, can, tin, whole ... all count as individual items
        }
    }

    /**
     * Converts a quantity to a base amount so that different units can be compared.
     * Weight and volume share one scale (1 g is treated as 1 ml, a common kitchen
     * approximation). Counted items use a separate scale.
     */
    public static double toBaseAmount(double quantity, String unit) {
        switch (normalizeUnit(unit)) {
            case "kg":   return quantity * 1000.0;
            case "l":    return quantity * 1000.0;
            case "tsp":  return quantity * 5.0;
            case "tbsp": return quantity * 15.0;
            case "cup":  return quantity * 240.0;
            default:     return quantity; // g, ml, pcs
        }
    }

    /** True when the unit counts whole items rather than weight or volume. */
    public static boolean isCountUnit(String unit) {
        return "pcs".equals(normalizeUnit(unit));
    }
}
