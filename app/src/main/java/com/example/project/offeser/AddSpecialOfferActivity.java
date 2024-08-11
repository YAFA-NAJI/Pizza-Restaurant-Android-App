package com.example.project.offeser;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.DatabaseHelper;
import com.example.project.R;

public class AddSpecialOfferActivity extends Fragment {
    private static final String ARG_PIZZA_NAME = "pizza_name";
    private static final String ARG_USER_EMAIL = "user_email";
    private String pizzaName;
    private String userEmail;
    private Spinner spinnerPizzaType;
    private Spinner spinnerPizzaSize;
    private EditText editTextOfferPeriod;
    private EditText editTextTotalPrice;
    private Button buttonAddOffer;
    private DatabaseHelper dbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pizzaName = getArguments().getString(ARG_PIZZA_NAME);
            userEmail = getArguments().getString(ARG_USER_EMAIL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_special_offers, container, false);

        // Initialize views
        spinnerPizzaType = view.findViewById(R.id.spinnerPizzaType);
        spinnerPizzaSize = view.findViewById(R.id.spinnerPizzaSize);
        editTextOfferPeriod = view.findViewById(R.id.editTextOfferPeriod);
        editTextTotalPrice = view.findViewById(R.id.editTextTotalPrice);
        buttonAddOffer = view.findViewById(R.id.buttonAddOffer);

        // Initialize database helper
        dbHelper = new DatabaseHelper(getActivity());

        // Set click listener for Add Offer button
        buttonAddOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSpecialOfferToDatabase();
            }
        });

        return view;
    }

    private void addSpecialOfferToDatabase() {
        // Get input values
        String pizzaName = spinnerPizzaType.getSelectedItem().toString();
        String pizzaSize = spinnerPizzaSize.getSelectedItem().toString();
        String offerPeriodText = editTextOfferPeriod.getText().toString();
        String totalPriceText = editTextTotalPrice.getText().toString();

        // Check if any input field is empty
        if (pizzaName.isEmpty() || pizzaSize.isEmpty() || offerPeriodText.isEmpty() || totalPriceText.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double offerPeriod = Double.parseDouble(offerPeriodText);
        double totalPrice = Double.parseDouble(totalPriceText);

        // Get description based on pizza type
        String description = getPizzaDescription(pizzaName);

        boolean success = dbHelper.addSpecialOffer(
                pizzaName,   // Offer name
                description, // Description of the offer
                pizzaSize,   // Size of the offer
                offerPeriod, // Duration of the offer
                String.valueOf(totalPrice) // Price of the offer
        );

        // Add special offer to database
        if (success) {
            Toast.makeText(getActivity(), "Special offer added successfully", Toast.LENGTH_SHORT).show();
            // Clear input fields
            editTextOfferPeriod.setText("");
            editTextTotalPrice.setText("");
        } else {
            Toast.makeText(getActivity(), "Failed to add special offer", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to get pizza description based on its type
    private String getPizzaDescription(String pizzaType) {
        switch (pizzaType) {
            case "Margarita,Pepperoni":
                return "Fresh tomato sauce, creamy mozzarella cheese, and fragrant basil leaves make up this classic Italian pizza. Simple yet delicious! A timeless favorite, this pizza boasts zesty tomato sauce, generous amounts of mozzarella cheese, and spicy slices of pepperoni.";
            case "Hawaiian,New York Style":
                return "Escape to the tropics with this Hawaiian delight! Enjoy the combination of tangy tomato sauce, creamy mozzarella cheese, savory ham, and sweet pineapple. Inspired by the bustling streets of New York City, this pizza features tangy tomato sauce, gooey mozzarella cheese, and big slices of pepperoni.";
            case "Calzone,Tandoori Chicken":
                return "A folded delight! Indulge in this savory calzone filled with rich tomato sauce, melty mozzarella cheese, and your choice of fillings. Experience the flavors of India with this pizza featuring spicy tandoori chicken, tangy tomato sauce, and creamy mozzarella cheese.";
            case "BBQ Chicken,Seafood Pizza":
                return "Satisfy your barbecue cravings with this pizza! Enjoy tender chunks of BBQ chicken, smoky barbecue sauce, and gooey mozzarella cheese. Dive into the sea with this seafood delight! Enjoy a flavorful combination of shrimp, squid, mussels, tangy tomato sauce, and mozzarella cheese.";
            case "Vegetarian,Buffalo Chicken":
                return "A feast for vegetarians! Indulge in fresh vegetables, tangy tomato sauce, and creamy mozzarella cheese on a crispy crust. Spice up your meal with this pizza featuring tender buffalo chicken, tangy buffalo sauce, and creamy mozzarella cheese.";
            default:
                return "Description not available";
        }
    }


}
