package com.ondwela.smartpantry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ondwela.smartpantry.R;
import com.ondwela.smartpantry.adapter.PantryAdapter;
import com.ondwela.smartpantry.db.PantryDbHelper;
import com.ondwela.smartpantry.logic.SettingsStore;
import com.ondwela.smartpantry.model.PantryItem;

import java.util.List;

/** Pantry list screen: shows every ingredient and lets the user add, edit or delete. */
public class PantryFragment extends Fragment implements PantryAdapter.Listener {

    private PantryDbHelper db;
    private SettingsStore settings;
    private PantryAdapter adapter;
    private RecyclerView recycler;
    private TextView textEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pantry, container, false);
        db = PantryDbHelper.getInstance(requireContext());
        settings = new SettingsStore(requireContext());

        recycler = root.findViewById(R.id.recyclerPantry);
        textEmpty = root.findViewById(R.id.textEmpty);
        FloatingActionButton fab = root.findViewById(R.id.fabAdd);

        adapter = new PantryAdapter(this);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setAdapter(adapter);

        // Explicit Intent: open the Add/Edit screen with no id, meaning "create new".
        fab.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), AddEditItemActivity.class)));
        return root;
    }

    /** Reload from the database every time we come back, so edits show immediately. */
    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        List<PantryItem> items = db.getAllItems();
        adapter.setItems(items, settings.isExpiryAlertsOn());
        boolean empty = items.isEmpty();
        textEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        recycler.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onEdit(PantryItem item) {
        Intent intent = new Intent(requireContext(), AddEditItemActivity.class);
        intent.putExtra(AddEditItemActivity.EXTRA_ITEM_ID, item.getId());
        startActivity(intent);
    }

    @Override
    public void onDelete(PantryItem item) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.confirm_delete_title)
                .setMessage(getString(R.string.confirm_delete_message, item.getName()))
                .setNegativeButton(R.string.action_cancel, null)
                .setPositiveButton(R.string.action_delete, (d, which) -> {
                    db.deleteItem(item.getId());
                    Toast.makeText(requireContext(), R.string.msg_item_deleted, Toast.LENGTH_SHORT).show();
                    refresh();
                })
                .show();
    }
}
