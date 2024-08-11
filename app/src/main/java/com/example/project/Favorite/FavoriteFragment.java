package com.example.project.Favorite;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.project.DatabaseHelper;
import com.example.project.Pizza.OrderDialogFragment;
import com.example.project.R;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private static final String TAG = "FavoriteFragment";

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> favoritePizzaList;
    private String selectedPizzaName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        listView = view.findViewById(R.id.ListView);
        favoritePizzaList = new ArrayList<>();

        // Retrieve user's favorite pizzas from the user database
        String userEmail = getUserEmail();
        if (userEmail != null) {
            try {
                favoritePizzaList = getUserFavoritePizzas(userEmail);
                // Set up the adapter
                adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, favoritePizzaList);
                listView.setAdapter(adapter);

                // Handle item click to select a pizza
                listView.setOnItemClickListener((parent, view1, position, id) -> {
                    selectedPizzaName = favoritePizzaList.get(position);
                    showOptionsDialog(selectedPizzaName);
                });
            } catch (Exception e) {
                Log.e(TAG, "Error retrieving favorite pizzas: " + e.getMessage());
                Toast.makeText(requireContext(), "Error retrieving favorite pizzas", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle user not logged in
            Toast.makeText(requireContext(), "Please log in to view favorites", Toast.LENGTH_SHORT).show();
        }

        return view;
    }



    // Method to get user email from SharedPreferences
    private String getUserEmail() {
        SharedPreferences preferences = requireContext().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        return preferences.getString("email", null);
    }

    // Method to get user's favorite pizzas from the favorites table
    private List<String> getUserFavoritePizzas(String userEmail) {
        List<String> favoritePizzaList = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {DatabaseHelper.COLUMN_PIZZA_NAME};
        String selection = DatabaseHelper.COLUMN_USER_EMAIL + "=?";
        String[] selectionArgs = {userEmail};
        Cursor cursor = db.query(DatabaseHelper.TABLE_FAVORITES, projection, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String pizzaName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PIZZA_NAME));
                favoritePizzaList.add(pizzaName);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return favoritePizzaList;
    }

    private void showOptionsDialog(String pizzaName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(pizzaName)
                .setMessage("What would you like to do?")
                .setPositiveButton("Add to Order", (dialog, which) -> {
                    showOrderDialog(pizzaName);
                })
                .setNegativeButton("Remove from Favorites", (dialog, which) -> {
                    removeFavorite(pizzaName, getUserEmail());
                })
                .setNeutralButton("Cancel", null)
                .show();
    }

    private void showOrderDialog(String pizzaName) {
        String userEmail = getUserEmail();
        if (userEmail != null && !userEmail.isEmpty()) {
            OrderDialogFragment dialogFragment = OrderDialogFragment.newInstance(pizzaName, userEmail);
            dialogFragment.show(getChildFragmentManager(), "OrderDialogFragment");
        } else {
            // Handle case where user email is not available
            Toast.makeText(requireContext(), "User email not found", Toast.LENGTH_SHORT).show();
        }
    }


    private void removeFavorite(String pizzaName, String userEmail) {
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        boolean isRemoved = dbHelper.removeFromFavorites(pizzaName, userEmail);

        if (isRemoved) {
            Toast.makeText(requireContext(), "Pizza removed from favorites", Toast.LENGTH_SHORT).show();
            // Update the UI to reflect the removal
            updateFavoriteList(userEmail);
        } else {
            Toast.makeText(requireContext(), "Failed to remove pizza from favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateFavoriteList(String userEmail) {
        favoritePizzaList = getUserFavoritePizzas(userEmail);
        adapter.clear();
        adapter.addAll(favoritePizzaList);
        adapter.notifyDataSetChanged();
    }
}

