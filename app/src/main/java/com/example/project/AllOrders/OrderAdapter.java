package com.example.project.AllOrders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.project.AllOrders.Order;
import com.example.project.R;

import java.util.List;

public class OrderAdapter extends BaseAdapter {
    private Context context;
    private List<Order> orders;

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_order, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.pizzaNameTextView = convertView.findViewById(R.id.textViewPizzaName);
            viewHolder.customerNameTextView = convertView.findViewById(R.id.textViewCustomerName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Order order = (Order) getItem(position);

        // التعامل مع القيم الافتراضية إذا كانت هناك قيم مفقودة في الطلب
        String pizzaName = order.getPizzaName() != null ? order.getPizzaName() : "";
        String customerName = order.getCustomerName() != null ? order.getCustomerName() : "";

        viewHolder.pizzaNameTextView.setText(pizzaName);
        viewHolder.customerNameTextView.setText(customerName);

        return convertView;
    }

    // ViewHolder pattern
    private static class ViewHolder {
        TextView pizzaNameTextView;
        TextView customerNameTextView;
    }
}
