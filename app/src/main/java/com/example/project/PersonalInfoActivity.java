package com.example.project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PersonalInfoActivity extends AppCompatActivity {

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextPhone;
    private EditText editTextPassword;
    private Button btnSaveChanges;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        // Initialize views
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Set onClickListener for Save Changes button
        btnSaveChanges.setOnClickListener(v -> saveChanges());
    }

    private void saveChanges() {
        // Get values from EditText fields
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Check if the input fields meet the required conditions
        if (firstName.length() < 3) {
            Toast.makeText(this, "First name must be at least 3 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        if (lastName.length() < 3) {
            Toast.makeText(this, "Last name must be at least 3 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPhoneNumber(phone)) {
            Toast.makeText(this, "Invalid phone number format. It must start with '05' and be exactly 10 digits long", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPassword(password)) {
            Toast.makeText(this, "Invalid password format. It must be at least 8 characters long and contain at least 1 letter and 1 digit", Toast.LENGTH_SHORT).show();
            return;
        }

        // Proceed with saving changes if all conditions are met
        dbHelper.updateAdminInfo("admin@example.com", firstName, lastName, phone, password);

        // Set result and finish the activity
        setResult(RESULT_OK);
        finish();
    }

    private boolean isValidPhoneNumber(String phone) {
        // Check if phone starts with '05' and has exactly 10 digits
        return phone.matches("^05\\d{8}$");
    }

    private boolean isValidPassword(String password) {
        // Check if password is at least 8 characters long and contains at least 1 letter and 1 digit
        return password.length() >= 8 && password.matches(".*[a-zA-Z]+.*") && password.matches(".*\\d+.*");
    }
}
