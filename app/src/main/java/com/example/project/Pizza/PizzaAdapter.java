package com.example.project.Pizza;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.project.R;

import java.util.List;

public class PizzaAdapter extends ArrayAdapter<String> {

    private List<String> pizzaList;
    private LayoutInflater inflater;
    private int selectedItem = -1; // Track selected item index

    public PizzaAdapter(@NonNull Context context, List<String> pizzaList) {
        super(context, 0, pizzaList);
        this.pizzaList = pizzaList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_pizza, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String pizzaName = pizzaList.get(position);
        viewHolder.pizzaNameTextView.setText(pizzaName);

        // Adjust item appearance based on selection
        if (position == selectedItem) {
            viewHolder.pizzaNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40); // Larger size
        } else {
            viewHolder.pizzaNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // Default size
        }

        return convertView;
    }

    // Method to set the selected item
    public void setSelectedItem(int position) {
        selectedItem = position;
        notifyDataSetChanged(); // Refresh list view
    }

    private static class ViewHolder {
        TextView pizzaNameTextView;

        ViewHolder(View view) {
            pizzaNameTextView = view.findViewById(R.id.pizzaNameTextView);
        }
    }
}
