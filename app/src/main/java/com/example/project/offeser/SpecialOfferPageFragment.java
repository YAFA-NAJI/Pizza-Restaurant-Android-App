package com.example.project.offeser;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.project.DatabaseHelper;

import com.example.project.Pizza.OrderDialogFragmentOffer;
import com.example.project.R;

public class SpecialOfferPageFragment extends Fragment {
    private static final String ARG_OFFER_DETAILS = "offer_details";
    private String offerDetails;
    private String userEmail;

    public static SpecialOfferPageFragment newInstance(String offerDetails) {
        SpecialOfferPageFragment fragment = new SpecialOfferPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_OFFER_DETAILS, offerDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            offerDetails = getArguments().getString(ARG_OFFER_DETAILS);
            userEmail = getUserEmail();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special_offer_page, container, false);

        TextView offerTextView = view.findViewById(R.id.offerTextView);
        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
        TextView durationTextView = view.findViewById(R.id.durationTextView); // Add reference to duration TextView
        TextView totalPriceTextView = view.findViewById(R.id.totalPriceTextView); // Add reference to totalPrice TextView
        TextView sizeTextView = view.findViewById(R.id.sizeTextView); // Add reference to size TextView

        Button addToFavoritesButton = view.findViewById(R.id.addToFavoritesButton);
        Button orderButton = view.findViewById(R.id.orderButton);

        String[] detailsArray = offerDetails.split(","); // Split the offer details

        if (detailsArray.length >= 5) {
            offerTextView.setText(detailsArray[0]); // Set the offer name
            descriptionTextView.setText(detailsArray[4]); // Set the description
            durationTextView.setText(getString(R.string.Duration) + ": " + detailsArray[2] + " days");
            totalPriceTextView.setText(getString(R.string.total_price) + ": " + detailsArray[3]);
            sizeTextView.setText(detailsArray[1]); // Set the size
        } else {
            Toast.makeText(requireContext(), "Invalid offer details", Toast.LENGTH_SHORT).show();
        }

        addToFavoritesButton.setOnClickListener(v -> {
            if (userEmail != null) {
                addToFavorites(offerDetails, userEmail);
            } else {
                Toast.makeText(requireContext(), "Please log in to add to favorites", Toast.LENGTH_SHORT).show();
            }
        });

        orderButton.setOnClickListener(v -> showOrderDialog());

        return view;
    }

    private String getUserEmail() {
        SharedPreferences preferences = requireContext().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        return preferences.getString("email", null);
    }

    private void addToFavorites(String offerDetails, String userEmail) {
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        boolean success = dbHelper.addToFavorites(offerDetails, userEmail);
        if (success) {
            Toast.makeText(requireContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Failed to add to Favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private void showOrderDialog() {
        FragmentManager fragmentManager = getParentFragmentManager();
        OrderDialogFragmentOffer orderDialog = OrderDialogFragmentOffer.newInstance(offerDetails, userEmail);
        orderDialog.show(fragmentManager, "OrderDialog");
    }
}
