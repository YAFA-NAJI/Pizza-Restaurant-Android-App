package com.example.project.order;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.OrderDatabaseHelper;
import com.example.project.R;

import java.util.List;

public class OrdersFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> orderList;
    private OrderDatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        listView = view.findViewById(R.id.ordersListView);

        // Initialize OrderDatabaseHelper
        dbHelper = new OrderDatabaseHelper(requireContext());

        // Retrieve user's email from SharedPreferences
        String userEmail = getUserEmail();
        if (userEmail != null) {
            // Retrieve all pizza names for the logged-in user
            orderList = dbHelper.getAllPizzaNamesForUser(userEmail);

            // Set up the adapter
            adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, orderList);
            listView.setAdapter(adapter);

            // Handle item click to show order details
            listView.setOnItemClickListener((parent, view1, position, id) -> {
                String selectedPizzaName = orderList.get(position);
                showOrderDetails(selectedPizzaName);
            });
        } else {
            // Handle case where user email is not available
            Toast.makeText(requireContext(), "User email not found", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    // Method to get user's email from SharedPreferences
    private String getUserEmail() {
        SharedPreferences preferences = requireContext().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        return preferences.getString("email", null);
    }


    // Method to show order details in a dialog
    private void showOrderDetails(String pizzaName) {
        // Retrieve order details from the database based on the pizza name
        List<String> orderDetails = dbHelper.getOrderDetailsByPizzaName(pizzaName);

        // Check if order details are available
        if (orderDetails != null && !orderDetails.isEmpty()) {
            // Construct the message to display in the dialog
            String message = "Pizza Name: " + pizzaName + "\n" +
                    "Size: " + orderDetails.get(0) + "\n" +
                    "Quantity: " + orderDetails.get(1) + "\n" +
                    "Unit Price: " + orderDetails.get(2) + "\n" +
                    "Total Price: " + orderDetails.get(3) + "\n" +
                    "Date/Time: " + orderDetails.get(4) ;
            // Show the dialog with order details
            new AlertDialog.Builder(requireContext())
                    .setTitle("Order Details")
                    .setMessage(message)
                    .setPositiveButton("OK", null)
                    .show();
        } else {
            // Show a message if order details are not available
            Toast.makeText(requireContext(), "Order details not found", Toast.LENGTH_SHORT).show();
        }
    }


}
