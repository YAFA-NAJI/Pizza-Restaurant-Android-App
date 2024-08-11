package com.example.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.AllOrders.Order;
import com.example.project.OrderDatabaseHelper;
import com.example.project.R;

import java.util.List;

import java.util.HashMap;
import java.util.Map;

public class OrderStatisticsFragment extends Fragment {

    private TextView statisticsTextView;
    private TextView totalPriceTextView;
    private OrderDatabaseHelper dbHelper;
    private TableLayout tableLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_statistics, container, false);
        statisticsTextView = view.findViewById(R.id.statisticsTextView);
        totalPriceTextView = view.findViewById(R.id.totalPriceTextView);
        tableLayout = view.findViewById(R.id.tableLayout);

        dbHelper = new OrderDatabaseHelper(requireContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayPizzaStatistics();
    }

    private void displayPizzaStatistics() {
        // Retrieve all orders from the database
        List<Order> orders = dbHelper.getAllOrders();

        // Initialize a map to store total quantity and total price per pizza type
        Map<String, Integer> totalQuantityPerPizza = new HashMap<>();
        Map<String, Double> totalPricePerPizza = new HashMap<>();

        // Calculate total quantity and total price per pizza type
        for (Order order : orders) {
            String pizzaType = order.getPizzaName();
            int quantity = Integer.parseInt(order.getQuantity());
            double unitPrice = Double.parseDouble(order.getUnitPrice());
            int totalQuantity = totalQuantityPerPizza.getOrDefault(pizzaType, 0);
            double totalPrice = totalPricePerPizza.getOrDefault(pizzaType, 0.0);

            totalQuantityPerPizza.put(pizzaType, totalQuantity + quantity);
            totalPricePerPizza.put(pizzaType, totalPrice + (quantity * unitPrice));
        }

        // Display pizza statistics in the table layout
        for (String pizzaType : totalQuantityPerPizza.keySet()) {
            TableRow newRow = new TableRow(requireContext());
            TextView pizzaTypeTextView = new TextView(requireContext());
            TextView ordersCountTextView = new TextView(requireContext());
            TextView pricePerOrderTextView = new TextView(requireContext());

            pizzaTypeTextView.setText(pizzaType);
            ordersCountTextView.setText(String.valueOf(totalQuantityPerPizza.get(pizzaType)));
            pricePerOrderTextView.setText(String.valueOf(totalPricePerPizza.get(pizzaType)));

            newRow.addView(pizzaTypeTextView);
            newRow.addView(ordersCountTextView);
            newRow.addView(pricePerOrderTextView);

            tableLayout.addView(newRow);
        }

        // Calculate and display total price
        double totalPrice = 0.0;
        for (double price : totalPricePerPizza.values()) {
            totalPrice += price;
        }
        totalPriceTextView.setText("Total Price: " + totalPrice);
    }
}
