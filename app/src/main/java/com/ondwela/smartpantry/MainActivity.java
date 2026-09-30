package com.ondwela.smartpantry;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ondwela.smartpantry.ui.PantryFragment;
import com.ondwela.smartpantry.ui.SettingsFragment;
import com.ondwela.smartpantry.ui.SuggestedFragment;

/**
 * Host activity. The bottom navigation bar swaps between the Pantry, Recipes
 * and Settings fragments; item screens open as separate activities via Intents.
 */
public class MainActivity extends AppCompatActivity {

    private static final String KEY_SELECTED = "selected_tab";

    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        BottomNavigationView nav = findViewById(R.id.bottomNav);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_pantry) {
                show(new PantryFragment(), R.string.title_pantry);
            } else if (id == R.id.nav_suggested) {
                show(new SuggestedFragment(), R.string.title_suggested);
            } else {
                show(new SettingsFragment(), R.string.title_settings);
            }
            return true;
        });

        // After a rotation the activity is recreated; restore the tab the user was on.
        int selected = savedInstanceState == null ? R.id.nav_pantry
                : savedInstanceState.getInt(KEY_SELECTED, R.id.nav_pantry);
        nav.setSelectedItemId(selected);
    }

    private void show(Fragment fragment, int titleRes) {
        toolbar.setTitle(titleRes);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        outState.putInt(KEY_SELECTED, nav.getSelectedItemId());
    }
}
