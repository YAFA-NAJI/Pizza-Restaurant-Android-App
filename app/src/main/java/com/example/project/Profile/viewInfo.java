package com.example.project.Profile;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.DatabaseHelper;
import com.example.project.R;

public class viewInfo extends AppCompatActivity {

    private TextView emailTextView, phoneTextView, firstNameTextView, lastNameTextView, passwordEditText;
    private Button editInfoButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_view);

        // Initialize views
        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        firstNameTextView = findViewById(R.id.firstNameTextView);
        lastNameTextView = findViewById(R.id.lastNameTextView);
        passwordEditText = findViewById(R.id.passwordTextView);
        editInfoButton = findViewById(R.id.btnBack);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Retrieve user's email from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("email")) {
            String email = intent.getStringExtra("email");
            String firstName = intent.getStringExtra("first_name");
            String lastName = intent.getStringExtra("last_name");
            String phone = intent.getStringExtra("phone");
            String password = intent.getStringExtra("password");

            // Load additional user information
            viewUserInfo(email);
        } else {
            Toast.makeText(this, "No email received", Toast.LENGTH_SHORT).show();
        }

        // Set up click listener for edit info button
        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ProfileFragment and pass data
                Intent intent = new Intent(viewInfo.this, ProfileFragment.class);

                intent.putExtra("email", emailTextView.getText().toString());
                intent.putExtra("first_name", firstNameTextView.getText().toString());
                intent.putExtra("last_name", lastNameTextView.getText().toString());
                intent.putExtra("phone", phoneTextView.getText().toString());
                intent.putExtra("password", passwordEditText.getText().toString());

                startActivity(intent);
            }
        });
    }


    // Method to retrieve user info from database
    // Method to retrieve and display user info from database
    private void viewUserInfo(String email) {
        Cursor cursor = dbHelper.getUserInfoByEmail(email);
        if (cursor != null && cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE));
            String password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD));

            // Display user info in text views
            emailTextView.setText(email);
            phoneTextView.setText(phone);
            firstNameTextView.setText(firstName);
            lastNameTextView.setText(lastName);
            passwordEditText.setText(password);

            cursor.close();
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
        }
    }

}
