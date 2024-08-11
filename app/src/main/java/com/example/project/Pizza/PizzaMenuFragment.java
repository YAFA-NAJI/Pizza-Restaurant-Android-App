package com.example.project.Pizza;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.DatabaseHelper2;
import com.example.project.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PizzaMenuFragment extends Fragment {

    private ListView listView;
    private PizzaAdapter adapter;
    private SearchView searchView;
    private Spinner categorySpinner;
    private Spinner sizeSpinner;
    private Spinner priceSpinner;
    private String[] pizzaSizes = {"All", "Small", "Medium", "Large"};
    private String[] pizzaPrices = {"All", "Under $20", "Over $20"};
    private String[] categories = {"All", "Veggies", "Meat", "Seafood"};
    private String[] pizzaNames = {"Margarita", "Neapolitan", "Hawaiian", "Pepperoni", "New York Style", "Calzone", "Tandoori Chicken Pizza", "BBQ Chicken Pizza", "Seafood Pizza", "Vegetarian Pizza", "Buffalo Chicken Pizza", "Mushroom Truffle Pizza", "Pesto Chicken Pizza"};
    private List<String> pizzaList;
    private DatabaseHelper2 databaseHelper2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pizza_menu, container, false);

        listView = view.findViewById(R.id.listView);
        searchView = view.findViewById(R.id.searchView);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        sizeSpinner = view.findViewById(R.id.sizeSpinner);
        priceSpinner = view.findViewById(R.id.priceSpinner);
        databaseHelper2 = new DatabaseHelper2(getContext());

        pizzaList = new ArrayList<>(Arrays.asList(pizzaNames));
        adapter = new PizzaAdapter(getContext(), pizzaList);
        listView.setAdapter(adapter);

        setupSearchView();
        setupCategorySpinner();
        setupSizeSpinner();
        setupPriceSpinner();
        setupItemClickListener();

        // Initial filter with empty query and all categories selected
        filterPizzas("", "All", "All", "All");

        return view;
    }

    private void filterPizzas(String query, String selectedCategory, String selectedSize, String selectedPrice) {
        List<String> filteredPizzaList = new ArrayList<>();
        for (String pizzaName : pizzaNames) {
            boolean matchesQuery = pizzaName.toLowerCase().contains(query.toLowerCase());
            boolean matchesCategory = selectedCategory.equals("All") || getCategoryForPizza(pizzaName).equalsIgnoreCase(selectedCategory);
            boolean matchesSize = selectedSize.equals("All") || getSizeForPizza(pizzaName).equalsIgnoreCase(selectedSize);
            boolean matchesPrice = selectedPrice.equals("All") || getPriceRangeForPizza(pizzaName).equalsIgnoreCase(selectedPrice);

            // Check if the pizza matches the query, category, size, and price filter (if applicable)
            if (matchesQuery && matchesCategory && matchesSize && matchesPrice) {
                filteredPizzaList.add(pizzaName);
            }
        }
        adapter.clear();
        adapter.addAll(filteredPizzaList.isEmpty() ? Arrays.asList(pizzaNames) : filteredPizzaList);
        adapter.notifyDataSetChanged();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String selectedCategory = categorySpinner.getSelectedItem().toString();
                String selectedSize = sizeSpinner.getSelectedItem().toString();
                String selectedPrice = priceSpinner.getSelectedItem().toString();
                filterPizzas(newText, selectedCategory, selectedSize, selectedPrice);
                return true;
            }
        });
    }

    private void setupCategorySpinner() {
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                String selectedSize = sizeSpinner.getSelectedItem().toString();
                String selectedPrice = priceSpinner.getSelectedItem().toString();
                String query = searchView.getQuery().toString();
                filterPizzas(query, selectedCategory, selectedSize, selectedPrice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupSizeSpinner() {
        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, pizzaSizes);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);

        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSize = parent.getItemAtPosition(position).toString();
                String selectedCategory = categorySpinner.getSelectedItem().toString();
                String selectedPrice = priceSpinner.getSelectedItem().toString();
                String query = searchView.getQuery().toString();
                filterPizzas(query, selectedCategory, selectedSize, selectedPrice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupPriceSpinner() {
        ArrayAdapter<String> priceAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, pizzaPrices);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceSpinner.setAdapter(priceAdapter);

        priceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPrice = parent.getItemAtPosition(position).toString();
                String selectedCategory = categorySpinner.getSelectedItem().toString();
                String selectedSize = sizeSpinner.getSelectedItem().toString();
                String query = searchView.getQuery().toString();
                filterPizzas(query, selectedCategory, selectedSize, selectedPrice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupItemClickListener() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Update adapter's selected item
            adapter.setSelectedItem(position);

            // Delay for 5 seconds before navigating to another fragment
            new Handler().postDelayed(() -> {
                // Handle click event (e.g., navigate to details fragment)
                String selectedItem = pizzaList.get(position);
                Log.d("PizzaMenuFragment", "Selected item: " + selectedItem);

                // Example: Replace fragment with details fragment
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, PizzaDetailsFragment.newInstance(selectedItem));
                transaction.addToBackStack(null);
                transaction.commit();
            }, 2000);
        });
    }

    private String getCategoryForPizza(String pizzaName) {
        switch (pizzaName) {
            case "Margarita":
            case "Vegetarian Pizza":
            case "Mushroom Truffle Pizza":
                return "Veggies";
            case "Pepperoni":
            case "Hawaiian":
            case "New York Style":
            case "Calzone":
            case "Tandoori Chicken Pizza":
            case "BBQ Chicken Pizza":
            case "Buffalo Chicken Pizza":
            case "Pesto Chicken Pizza":
                return "Meat";
            case "Seafood Pizza":
                return "Seafood";
            default:
                return "Others";
        }
    }

    private String getSizeForPizza(String pizzaName) {
        switch (pizzaName) {
            case "Margarita":
            case "Neapolitan":
            case "Vegetarian Pizza":
            case "Mushroom Truffle Pizza":
            case "Pepperoni":
            case "Hawaiian":
            case "New York Style":
                return "Medium";
            case "Calzone":
            case "Tandoori Chicken Pizza":
            case "BBQ Chicken Pizza":
            case "Buffalo Chicken Pizza":
            case "Pesto Chicken Pizza":
            case "Seafood Pizza":
                return "Large";
            default:
                return "Small";
        }
    }

    private String getPriceRangeForPizza(String pizzaName) {
        switch (pizzaName) {
            case "Margarita":
            case "Pepperoni":
            case "Pesto Chicken Pizza":
            case "Mushroom Truffle Pizza":
            case "Hawaiian":
            case "New York Style":
                return "Under $20";
            case "Calzone":
            case "Tandoori Chicken Pizza":
            case "BBQ Chicken Pizza":
            case "Seafood Pizza":
            case "Vegetarian Pizza":
            case "Buffalo Chicken Pizza":
                return "Over $20";
            default:
                return "Neapolitan";
        }
    }
}
