package com.example.project.Pizza;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.project.DatabaseHelper;
import com.example.project.R;

public class PizzaDetailsFragment extends Fragment {
    private static final String ARG_PIZZA_NAME = "pizza_name";
    private String pizzaName;

    public static PizzaDetailsFragment newInstance(String pizzaName) {
        PizzaDetailsFragment fragment = new PizzaDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PIZZA_NAME, pizzaName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pizzaName = getArguments().getString(ARG_PIZZA_NAME);
        }
    }

    private int getImageResourceId(String pizzaName) {
        switch (pizzaName) {
            case "Margarita":
                return R.drawable.margarita;
            case "Neapolitan":
                return R.drawable.neapolitan;
            case "Hawaiian":
                return R.drawable.hawaiian;
            case "Pepperoni":
                return R.drawable.pepperoni;
            case "New York Style":
                return R.drawable.new_york_style;
            case "Calzone":
                return R.drawable.calzone;
            case "Tandoori Chicken Pizza":
                return R.drawable.tandoori;
            case "BBQ Chicken Pizza":
                return R.drawable.bbq;
            case "Seafood Pizza":
                return R.drawable.seafood;
            case "Vegetarian Pizza":
                return R.drawable.vegetarian;
            case "Buffalo Chicken Pizza":
                return R.drawable.buffalo;
            case "Mushroom Truffle Pizza":
                return R.drawable.mushroom;
            case "Pesto Chicken Pizza":
                return R.drawable.pesto;
            // Add cases for other pizza items
            default:
                return R.drawable.pizaa; // Provide a default image resource ID
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pizza_details, container, false);

        TextView nameTextView = view.findViewById(R.id.pizzaNameTextView);
        TextView componentsTextView = view.findViewById(R.id.componentsTextView);
        TextView priceTextView = view.findViewById(R.id.priceTextView);
        TextView priceTextView2 = view.findViewById(R.id.priceTextView2);
        TextView priceTextView3 = view.findViewById(R.id.priceTextView3);
        ImageView pizzaImageView = view.findViewById(R.id.pizzaImageView);

        Button addToFavoritesButton = view.findViewById(R.id.addToFavoritesButton);
        Button orderButton = view.findViewById(R.id.orderButton);

        // Set the pizza name
        nameTextView.setText(pizzaName);

        // Set the pizza components and prices based on the selected pizza item
        String components;
        String priceSmall;
        String priceMedium;
        String priceLarge;
        if (pizzaName.equals("Margarita")) {
            components = "Fresh tomato sauce, creamy mozzarella cheese, and fragrant basil leaves make up this classic Italian pizza. Simple yet delicious!";
            priceSmall = "$6";
            priceMedium = "$10";
            priceLarge = "$15";
            pizzaImageView.setImageResource(getImageResourceId("Margarita"));

        }
        else if (pizzaName.equals("Neapolitan")) {
            components = "Transport yourself to Naples with this traditional pizza featuring tangy tomato sauce, rich mozzarella cheese, briny anchovies, and aromatic oregano.";
            priceSmall = "$9";
            priceMedium = "$13";
            priceLarge = "$18";
            pizzaImageView.setImageResource(getImageResourceId("Neapolitan"));

        } else if (pizzaName.equals("Hawaiian")) {
            components = "Escape to the tropics with this Hawaiian delight! Enjoy the combination of tangy tomato sauce, creamy mozzarella cheese, savory ham, and sweet pineapple.";
            priceSmall = "$4";
            priceMedium = "$6";
            priceLarge = "$10";
            pizzaImageView.setImageResource(getImageResourceId("Hawaiian"));

        } else if (pizzaName.equals("Pepperoni")) {
            components = "A timeless favorite, this pizza boasts zesty tomato sauce, generous amounts of mozzarella cheese, and spicy slices of pepperoni.";
            priceSmall = "$5";
            priceMedium = "$8";
            priceLarge = "$12";
            pizzaImageView.setImageResource(getImageResourceId("Pepperoni"));

        } else if (pizzaName.equals("New York Style")) {
            components = "Inspired by the bustling streets of New York City, this pizza features tangy tomato sauce, gooey mozzarella cheese, and big slices of pepperoni.";
            priceSmall = "$3";
            priceMedium = "$6";
            priceLarge = "$10";
            pizzaImageView.setImageResource(getImageResourceId("New York Style"));

        } else if (pizzaName.equals("Calzone")) {
            components = "A folded delight! Indulge in this savory calzone filled with rich tomato sauce, melty mozzarella cheese, and your choice of fillings.";
            priceSmall = "$20";
            priceMedium = "$25";
            priceLarge = "$30";
            pizzaImageView.setImageResource(getImageResourceId("Calzone"));

        } else if (pizzaName.equals("Tandoori Chicken Pizza")) {
            components = "Experience the flavors of India with this pizza featuring spicy tandoori chicken, tangy tomato sauce, and creamy mozzarella cheese.";
            priceSmall = "$20";
            priceMedium = "$22";
            priceLarge = "$25";
            pizzaImageView.setImageResource(getImageResourceId("Tandoori Chicken Pizza"));

        } else if (pizzaName.equals("BBQ Chicken Pizza")) {
            components = "Satisfy your barbecue cravings with this pizza! Enjoy tender chunks of BBQ chicken, smoky barbecue sauce, and gooey mozzarella cheese.";
            priceSmall = "$20";
            priceMedium = "$24";
            priceLarge = "$26";
            pizzaImageView.setImageResource(getImageResourceId("BBQ Chicken Pizza"));

        } else if (pizzaName.equals("Seafood Pizza")) {
            components = "Dive into the sea with this seafood delight! Enjoy a flavorful combination of shrimp, squid, mussels, tangy tomato sauce, and mozzarella cheese.";
            priceSmall = "$21";
            priceMedium = "$26";
            priceLarge = "$32";
            pizzaImageView.setImageResource(getImageResourceId("Seafood Pizza"));

        } else if (pizzaName.equals("Vegetarian Pizza")) {
            components = "A feast for vegetarians! Indulge in fresh vegetables, tangy tomato sauce, and creamy mozzarella cheese on a crispy crust.";
            priceSmall = "$25";
            priceMedium = "$30";
            priceLarge = "$35";
            pizzaImageView.setImageResource(getImageResourceId("Vegetarian Pizza"));

        } else if (pizzaName.equals("Buffalo Chicken Pizza")) {
            components = "Spice up your meal with this pizza featuring tender buffalo chicken, tangy buffalo sauce, and creamy mozzarella cheese.";
            priceSmall = "$26";
            priceMedium = "$29";
            priceLarge = "$33";
            pizzaImageView.setImageResource(getImageResourceId("Buffalo Chicken Pizza"));

        } else if (pizzaName.equals("Mushroom Truffle Pizza")) {
            components = "Elevate your taste buds with this gourmet pizza! Indulge in earthy mushrooms, truffle oil, creamy mozzarella cheese, and tangy tomato sauce.";
            priceSmall = "$9";
            priceMedium = "$14";
            priceLarge = "$19";
            pizzaImageView.setImageResource(getImageResourceId("Mushroom Truffle Pizza"));

        } else if (pizzaName.equals("Pesto Chicken Pizza")) {
            components = "Experience the vibrant flavors of Italy with this pizza featuring fragrant pesto sauce, tender chicken, and creamy mozzarella cheese.";
            priceSmall = "$7";
            priceMedium = "$14";
            priceLarge = "$18";
            pizzaImageView.setImageResource(getImageResourceId("Pesto Chicken Pizza"));

        } else {
            // Default values if no specific pizza item matches
            components = "Tomato sauce, mozzarella cheese, pepperoni, mushrooms, bell peppers";
            priceSmall = "$10";
            priceMedium = "$15";
            priceLarge = "$20";
            pizzaImageView.setImageResource(getImageResourceId(""));

        }

        // Display components and prices
        componentsTextView.setText("Components: " + components);
        priceTextView.setText("Price (Small): " + priceSmall);
        priceTextView2.setText("Price (Medium): " + priceMedium);
        priceTextView3.setText("Price (Large): " + priceLarge);

        // Add to favorites button click listener
        addToFavoritesButton.setOnClickListener(v -> {
            String userEmail = getUserEmail();
            if (userEmail != null) {
                addToFavorites(pizzaName, userEmail);
            } else {
                // Handle user not logged in
                Toast.makeText(requireContext(), "Please log in to add to favorites", Toast.LENGTH_SHORT).show();
            }
        });


        // Order button click listener
        orderButton.setOnClickListener(v -> showOrderDialog());

        return view;
    }

    // Method to get user email from SharedPreferences
    private String getUserEmail() {
        SharedPreferences preferences = requireContext().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        return preferences.getString("email", null);
    }

    // Method to add pizza to favorites
    private void addToFavorites(String pizzaName, String userEmail) {
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        boolean success = dbHelper.addToFavorites(pizzaName, userEmail);
        if (success) {
            Toast.makeText(requireContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Failed to add to Favorites", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to show order dialog
    private void showOrderDialog() {
        FragmentManager fragmentManager = getParentFragmentManager();
        // Create and show the order dialog fragment
        OrderDialogFragment orderDialog = OrderDialogFragment.newInstance(pizzaName, getUserEmail());
        orderDialog.show(fragmentManager, "OrderDialog");
    }
}