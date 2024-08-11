package com.example.project.AllOrders;




import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.DatabaseHelper;
import com.example.project.AllOrders.Order;
import com.example.project.AllOrders.OrderAdapter;
import com.example.project.OrderDatabaseHelper;
import com.example.project.R;

import java.util.List;

public class ViewAllOrders extends Fragment {

    private ListView listView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private OrderDatabaseHelper orderDatabaseHelper;
    private SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_orders, container, false);

        listView = view.findViewById(R.id.ordersListView);

        // Initialize DatabaseHelper
        orderDatabaseHelper = new OrderDatabaseHelper(requireContext());

        // Retrieve user email from SharedPreferences
//        String userEmail = getUserEmail();
//        Log.d("ViewAllOrders", "User email from SharedPreferences: " + userEmail);
//
//        if (userEmail == null || userEmail.isEmpty()) {
//            // If the user email is null or empty, show an error message or take appropriate action.
//            Toast.makeText(requireContext(), "User email is null or empty", Toast.LENGTH_SHORT).show();
//            return view;
//        }

        // Retrieve all orders for the current user
        orderList = orderDatabaseHelper.getAllOrdersForUser();

        if (orderList.isEmpty()) {
            // If the order list is empty, you can show a toast message to the user or take appropriate action.
            Toast.makeText(requireContext(), "Order list is empty", Toast.LENGTH_SHORT).show();
        } else {
            // If there is data available, set the order list in the appropriate tool like ListView.
            orderAdapter = new OrderAdapter(requireContext(), orderList);
            listView.setAdapter(orderAdapter);

            // Handle item click to show order details
            listView.setOnItemClickListener((parent, view1, position, id) -> {
                Order selectedOrder = orderList.get(position);
                showOrderDetails(selectedOrder);
            });

            // Log the retrieved orders
            logOrders(orderList);
        }

        return view;
    }

    // Method to retrieve user email from SharedPreferences
    public String getUserEmail() {
        sharedPreferences = this.getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_email", null);
    }




    // Method to show order details in a dialog
    private void showOrderDetails(Order order) {
        // Construct the message to display in the dialog
        String message = "Pizza Name: " + order.getPizzaName() + "\n" +
                "Size: " + order.getSize() + "\n" +
                "Quantity: " + order.getQuantity() + "\n" +
                "Unit Price: " + order.getUnitPrice() + "\n" +
                "Total Price: " + order.getTotalPrice() + "\n" +
                "Date/Time: " + order.getDateTime() + "\n" +
                "Customer Name: " + order.getCustomerName();
        // Show the dialog with order details
        new AlertDialog.Builder(requireContext())
                .setTitle("Order Details")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    // Method to log the retrieved orders
    private void logOrders(List<Order> orders) {
        for (Order order : orders) {
            Log.d("OrderDetails", "Pizza Name: " + order.getPizzaName() +
                    ", Size: " + order.getSize() +
                    ", Quantity: " + order.getQuantity() +
                    ", Unit Price: " + order.getUnitPrice() +
                    ", Total Price: " + order.getTotalPrice() +
                    ", Date/Time: " + order.getDateTime() +
                    ", Customer Name: " + order.getCustomerName());
        }
    }
}
