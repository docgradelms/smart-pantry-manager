package com.ondwela.smartpantry.logic;

import android.content.Context;
import android.content.SharedPreferences;

/** Thin wrapper around SharedPreferences for the two Settings toggles. */
public final class SettingsStore {

    private static final String FILE = "smart_pantry_settings";
    private static final String KEY_ALERTS = "expiry_alerts";
    private static final String KEY_ALMOST = "show_almost_there";

    private final SharedPreferences prefs;

    public SettingsStore(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(FILE, Context.MODE_PRIVATE);
    }

    public boolean isExpiryAlertsOn() { return prefs.getBoolean(KEY_ALERTS, true); }
    public void setExpiryAlertsOn(boolean on) { prefs.edit().putBoolean(KEY_ALERTS, on).apply(); }

    public boolean isAlmostThereOn() { return prefs.getBoolean(KEY_ALMOST, true); }
    public void setAlmostThereOn(boolean on) { prefs.edit().putBoolean(KEY_ALMOST, on).apply(); }
}
