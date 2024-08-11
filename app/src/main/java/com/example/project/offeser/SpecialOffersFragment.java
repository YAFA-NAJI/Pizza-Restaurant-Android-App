package com.example.project.offeser;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project.DatabaseHelper;
import com.example.project.R;

import java.util.ArrayList;
import java.util.List;

public class SpecialOffersFragment extends Fragment {

    private ViewPager2 viewPager;
    private SpecialOffersAdapter adapter;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offeser, container, false);

        dbHelper = new DatabaseHelper(getActivity());

        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        // Load special offers from the database
        loadSpecialOffers();

        Button prevButton = view.findViewById(R.id.prevButton);
        Button nextButton = view.findViewById(R.id.nextButton);

        prevButton.setOnClickListener(v -> moveToPreviousPage());
        nextButton.setOnClickListener(v -> moveToNextPage());

        return view;
    }

    private void loadSpecialOffers() {
        List<SpecialOfferUser> offers = getSpecialOffersFromDatabase();

        adapter = new SpecialOffersAdapter(requireActivity(), offers);
        viewPager.setAdapter(adapter);
    }

    private List<SpecialOfferUser> getSpecialOffersFromDatabase() {
        List<SpecialOfferUser> specialOffers = new ArrayList<>();
        Cursor cursor = dbHelper.getAllSpecialOffers();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String offerName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SPECIAL_OFFER_NAME));
                String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SPECIAL_OFFER_DESCRIPTION));
                String duration = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SPECIAL_OFFER_DURATION));
                double totalPrice  = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_SPECIAL_OFFER_PRICE));
                String size = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SPECIAL_OFFER_SIZE)); // تخزين حجم البيتزا من الكرسور

                // Construct the offer details string
                String offerDetails = "Total Price: $" + totalPrice;

                // Create the SpecialOfferUser object with size
                SpecialOfferUser offer = new SpecialOfferUser(offerName, description, offerDetails, duration, totalPrice, size);
                specialOffers.add(offer);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return specialOffers;
    }


    private void moveToPreviousPage() {
        int currentItem = viewPager.getCurrentItem();
        if (currentItem > 0) {
            viewPager.setCurrentItem(currentItem - 1, true);
        }
    }

    private void moveToNextPage() {
        int currentItem = viewPager.getCurrentItem();
        if (currentItem < viewPager.getAdapter().getItemCount() - 1) {
            viewPager.setCurrentItem(currentItem + 1, true);
        }
    }
}
