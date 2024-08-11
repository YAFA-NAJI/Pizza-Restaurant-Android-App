// callFragment.java
package com.example.project.call;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.project.R;

public class callFragment extends Fragment {

    public callFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_call, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize buttons
        View btnCall = view.findViewById(R.id.btn_call);
        View btnFind = view.findViewById(R.id.btn_find);
        View btnEmail = view.findViewById(R.id.btn_email);

        // Set click listeners
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRestaurant();
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findRestaurant();
            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    // Method to call the restaurant
    private void callRestaurant() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0599000000"));
        startActivity(intent);
    }

    // Method to find the restaurant using Google Maps
    private void findRestaurant() {
        Uri gmmIntentUri = Uri.parse("geo:31.961013,35.190483?q=restaurant");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    // Method to send email to the restaurant
    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"AdvancePizza@Pizza.com"});
        startActivity(Intent.createChooser(emailIntent, "Send email"));
    }
}
