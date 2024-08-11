package com.example.project.Pizza;

import android.app.Dialog;
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

import com.example.project.DatabaseHelper;
import com.example.project.OrderDatabaseHelper;
import com.example.project.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderDialogFragmentOffer extends DialogFragment {

    private static final String ARG_PIZZA_NAME = "pizza_name";
    private static final String ARG_USER_EMAIL = "user_email";
    private String pizzaName;
    private String userEmail;
    private OrderDatabaseHelper dbHelper;
    private DatabaseHelper dbHelper2;

    public static OrderDialogFragmentOffer newInstance(String pizzaName, String userEmail) {
        OrderDialogFragmentOffer fragment = new OrderDialogFragmentOffer();
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
            dbHelper = new OrderDatabaseHelper(requireContext());
            dbHelper2 = new DatabaseHelper(requireContext());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_order_dialog3, null);

        Spinner quantitySpinner = view.findViewById(R.id.quantitySpinner);
        Spinner sizeSpinner = view.findViewById(R.id.sizeSpinner);
        Button submitButton = view.findViewById(R.id.submitButton);

        ArrayAdapter<CharSequence> quantityAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.quantity_options, android.R.layout.simple_spinner_item);
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(quantityAdapter);

        String[] sizesOnOffer = dbHelper2.getSizesOnOfferForPizza(pizzaName); // Get sizes with offers
        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sizesOnOffer);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter); // Set adapter with sizes on offer

        submitButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(quantitySpinner.getSelectedItem().toString());
            String selectedSize = sizeSpinner.getSelectedItem().toString();
            if (userEmail != null && !userEmail.isEmpty()) {
                submitOrder(quantity, selectedSize);
                dismiss();
            } else {
                Toast.makeText(requireContext(), "User email not found", Toast.LENGTH_SHORT).show();
            }
        });

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(view);
        return dialog;
    }

    private void submitOrder(int quantity, String size) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dateTime = sdf.format(new Date());

        // Get the price for the selected size
        String price = dbHelper2.getPizzaPrice(pizzaName, size);

        if (price != null) {
            // Extract the numeric value from the price string
            double unitPrice = Double.parseDouble(price);
            double totalPrice = unitPrice * quantity;

            // Add the order to the database
            boolean success = dbHelper.addOrder(pizzaName, size, quantity, unitPrice, totalPrice, dateTime, userEmail);
            if (success) {
                Toast.makeText(requireContext(), "Order placed successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Failed to add order", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where the price for the selected size is not found
            Toast.makeText(requireContext(), "Price not available for selected size", Toast.LENGTH_SHORT).show();
        }
    }
}
