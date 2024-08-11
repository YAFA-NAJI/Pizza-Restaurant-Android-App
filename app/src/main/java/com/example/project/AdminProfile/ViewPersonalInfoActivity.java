package com.example.project.AdminProfile;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Admin;
import com.example.project.DatabaseHelper;
import com.example.project.Profile.ProfileFragment;
import com.example.project.Profile.viewInfo;
import com.example.project.R;

public class ViewPersonalInfoActivity extends AppCompatActivity {

    private TextView emailTextView, phoneTextView, firstNameTextView, lastNameTextView, passwordTextView;
    private Button editInfoButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_personal_info);

        // Initialize views
        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        firstNameTextView = findViewById(R.id.firstNameTextView);
        lastNameTextView = findViewById(R.id.lastNameTextView);
        passwordTextView = findViewById(R.id.passwordTextView);
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
            viewAdminInfo(email);
        } else {
            Toast.makeText(this, "No email received", Toast.LENGTH_SHORT).show();
        }

        // Set up click listener for edit info button
        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ProfileFragment and pass data
                Intent intent = new Intent(ViewPersonalInfoActivity.this, UpdateInfoActivity.class);

                intent.putExtra("email", emailTextView.getText().toString());
                intent.putExtra("first_name", firstNameTextView.getText().toString());
                intent.putExtra("last_name", lastNameTextView.getText().toString());
                intent.putExtra("phone", phoneTextView.getText().toString());
                intent.putExtra("password", passwordTextView.getText().toString());

                startActivity(intent);
            }
        });
    }

    private void viewAdminInfo(String email) {
        Cursor cursor = dbHelper.getAdminInfoByEmail(email);
        if (cursor != null && cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FIRST_NAME_ADMINS));
            String lastName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_NAME_ADMINS));
            String phone = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE_ADMINS));
            String password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD_ADMINS));

            // Display user info in text views
            emailTextView.setText(email);
            phoneTextView.setText(phone);
            firstNameTextView.setText(firstName);
            lastNameTextView.setText(lastName);
            passwordTextView.setText(password);

            cursor.close();
        } else {
            Toast.makeText(this, "Admin not found", Toast.LENGTH_SHORT).show();
        }
    }

}