package com.ondwela.smartpantry.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.ondwela.smartpantry.R;
import com.ondwela.smartpantry.logic.SettingsStore;

/** Settings screen: two toggles saved in SharedPreferences. */
public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        SettingsStore settings = new SettingsStore(requireContext());

        MaterialSwitch alerts = root.findViewById(R.id.switchAlerts);
        MaterialSwitch almost = root.findViewById(R.id.switchAlmost);

        alerts.setChecked(settings.isExpiryAlertsOn());
        almost.setChecked(settings.isAlmostThereOn());

        alerts.setOnCheckedChangeListener((button, checked) -> settings.setExpiryAlertsOn(checked));
        almost.setOnCheckedChangeListener((button, checked) -> settings.setAlmostThereOn(checked));
        return root;
    }
}
