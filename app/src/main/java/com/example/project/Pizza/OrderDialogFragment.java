package com.example.project.Pizza;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.project.OrderDatabaseHelper;
import com.example.project.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderDialogFragment extends DialogFragment {

    private static final String ARG_PIZZA_NAME = "pizza_name";
    private static final String ARG_USER_EMAIL = "user_email";
    private String pizzaName;
    private String userEmail;
    private String[] pizzaPrices;

    public static OrderDialogFragment newInstance(String pizzaName, String userEmail) {
        OrderDialogFragment fragment = new OrderDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PIZZA_NAME, pizzaName);
        args.putString(ARG_USER_EMAIL, userEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pizzaName = getArguments().getString(ARG_PIZZA_NAME);
            userEmail = getArguments().getString(ARG_USER_EMAIL);
            pizzaPrices = getPricesForPizza(pizzaName); // Retrieve the pizza prices array
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_order_dialog, null);

        Spinner sizeSpinner = view.findViewById(R.id.sizeSpinner);
        Spinner quantitySpinner = view.findViewById(R.id.quantitySpinner);
        Button submitButton = view.findViewById(R.id.submitButton);

        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.pizza_sizes, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);

        ArrayAdapter<CharSequence> quantityAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.quantity_options, android.R.layout.simple_spinner_item);
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(quantityAdapter);

        submitButton.setOnClickListener(v -> {
            String size = sizeSpinner.getSelectedItem().toString();
            int quantity = Integer.parseInt(quantitySpinner.getSelectedItem().toString());
            if (userEmail != null && !userEmail.isEmpty()) {
                submitOrder(size, quantity);
                dismiss();
            } else {
                Toast.makeText(requireContext(), "User email not found", Toast.LENGTH_SHORT).show();
            }
        });

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(view);
        return dialog;
    }



    private String[] getPricesForPizza(String pizzaName) {
        String[] prices = new String[3]; // Array to store prices for small, medium, and large

        if (pizzaName.equals("Margarita")) {
            prices[0] = "$6"; // Small price
            prices[1] = "$10"; // Medium price
            prices[2] = "$15"; // Large price
        } else if (pizzaName.equals("Neapolitan")) {
            prices[0] = "$9"; // Small price
            prices[1] = "$13"; // Medium price
            prices[2] = "$18"; // Large price
        } else if (pizzaName.equals("Hawaiian")) {
            prices[0] = "$4"; // Small price
            prices[1] = "$6"; // Medium price
            prices[2] = "$10"; // Large price
        } else if (pizzaName.equals("Pepperoni")) {
            prices[0] = "$5"; // Small price
            prices[1] = "$8"; // Medium price
            prices[2] = "$12"; // Large price
        } else if (pizzaName.equals("New York Style")) {
            prices[0] = "$3"; // Small price
            prices[1] = "$6"; // Medium price
            prices[2] = "$10"; // Large price
        } else if (pizzaName.equals("Calzone")) {
            prices[0] = "$20"; // Small price
            prices[1] = "$25"; // Medium price
            prices[2] = "$30"; // Large price
        } else if (pizzaName.equals("Tandoori Chicken Pizza")) {
            prices[0] = "$20"; // Small price
            prices[1] = "$22"; // Medium price
            prices[2] = "$25"; // Large price
        } else if (pizzaName.equals("BBQ Chicken Pizza")) {
            prices[0] = "$20"; // Small price
            prices[1] = "$24"; // Medium price
            prices[2] = "$26"; // Large price
        } else if (pizzaName.equals("Seafood Pizza")) {
            prices[0] = "$21"; // Small price
            prices[1] = "$26"; // Medium price
            prices[2] = "$32"; // Large price
        } else if (pizzaName.equals("Vegetarian Pizza")) {
            prices[0] = "$25"; // Small price
            prices[1] = "$30"; // Medium price
            prices[2] = "$35"; // Large price
        } else if (pizzaName.equals("Buffalo Chicken Pizza")) {
            prices[0] = "$26"; // Small price
            prices[1] = "$29"; // Medium price
            prices[2] = "$33"; // Large price
        } else if (pizzaName.equals("Mushroom Truffle Pizza")) {
            prices[0] = "$9"; // Small price
            prices[1] = "$14"; // Medium price
            prices[2] = "$19"; // Large price
        } else if (pizzaName.equals("Pesto Chicken Pizza")) {
            prices[0] = "$7"; // Small price
            prices[1] = "$14"; // Medium price
            prices[2] = "$18"; // Large price
        } else {

        }

        return prices;
    }
    private void submitOrder(String size, int quantity) {
        OrderDatabaseHelper dbHelper = new OrderDatabaseHelper(requireContext());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dateTime = sdf.format(new Date());

        // Determine the price based on the selected size
        String price;
        if (size.equals("Small")) {
            price = pizzaPrices[0]; // Get the price for small pizza from the pizzaPrices array
        } else if (size.equals("Medium")) {
            price = pizzaPrices[1]; // Get the price for medium pizza from the pizzaPrices array
        } else {
            price = pizzaPrices[2]; // Get the price for large pizza from the pizzaPrices array
        }

        // Extract the numeric value from the price string
        double unitPrice = Double.parseDouble(price.replace("$", ""));
        double totalPrice = unitPrice * quantity;

        // Add the order to the database
        boolean success = dbHelper.addOrder(pizzaName, size, quantity, unitPrice, totalPrice, dateTime, userEmail);
        if (success) {
            Toast.makeText(requireContext(), "Order placed successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Failed to add order", Toast.LENGTH_SHORT).show();
        }
    }
}
