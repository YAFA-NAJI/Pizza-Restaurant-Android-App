package com.example.project.offeser;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.DatabaseHelper;
import com.example.project.Pizza.OrderDialogFragmentOffer;
import com.example.project.R;

import java.util.List;

public class SpecialOffersAdapter extends RecyclerView.Adapter<SpecialOffersAdapter.OfferViewHolder> {

    private List<SpecialOfferUser> offers;
    private Context context;

    public SpecialOffersAdapter(FragmentActivity context, List<SpecialOfferUser> offers) {
        this.context = context;
        this.offers = offers;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_special_offer_page, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        SpecialOfferUser offer = offers.get(position);
        holder.offerTextView.setText(offer.getName());
        holder.descriptionTextView.setText(offer.getDescription());
        holder.durationTextView.setText(context.getString(R.string.Duration) + ": " + offer.getDuration() + " days");
        holder.totalPriceTextView.setText(context.getString(R.string.total_price) + ": " + String.valueOf(offer.getTotalPrice()));
        holder.sizeTextView.setText(offer.getSize()); // عرض حجم البيتزا

        holder.addToFavoritesButton.setOnClickListener(v -> {
            String userEmail = getUserEmail();
            if (userEmail != null) {
                addToFavorites(offer.getName(), userEmail);
            } else {
                Toast.makeText(context, "Please log in to add to favorites", Toast.LENGTH_SHORT).show();
            }
        });

        holder.orderButton.setOnClickListener(v -> showOrderDialog(offer.getName()));
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    private String getUserEmail() {
        SharedPreferences preferences = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        return preferences.getString("email", null);
    }

    private void addToFavorites(String offerDetails, String userEmail) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        boolean success = dbHelper.addToFavorites(offerDetails, userEmail);
        if (success) {
            Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to add to Favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private void showOrderDialog(String offerDetails) {
        OrderDialogFragmentOffer orderDialog = OrderDialogFragmentOffer.newInstance(offerDetails, getUserEmail());
        orderDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "OrderDialog");
    }

    static class OfferViewHolder extends RecyclerView.ViewHolder {
        TextView offerTextView;
        TextView descriptionTextView;
        TextView durationTextView;
        TextView totalPriceTextView;  // Add this line
        TextView sizeTextView;  // Add this line

        Button addToFavoritesButton;
        Button orderButton;

        OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            offerTextView = itemView.findViewById(R.id.offerTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            totalPriceTextView = itemView.findViewById(R.id.totalPriceTextView);  // Initialize totalPriceTextView
            sizeTextView = itemView.findViewById(R.id.sizeTextView);  // Initialize sizeTextView
            addToFavoritesButton = itemView.findViewById(R.id.addToFavoritesButton);
            orderButton = itemView.findViewById(R.id.orderButton);
        }
    }
}
