package com.example.project.AddAdmin;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.project.DatabaseHelper;
import com.example.project.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class Add_Admin extends Fragment {
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Spinner genderSpinner;
    private Button addButton;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_admin, container, false);

        // Initialize views
        firstNameEditText = view.findViewById(R.id.editTextFirstName);
        lastNameEditText = view.findViewById(R.id.editTextLastName);
        emailEditText = view.findViewById(R.id.editTextEmail);
        phoneEditText = view.findViewById(R.id.editTextPhone);
        passwordEditText = view.findViewById(R.id.editTextPassword);
        confirmPasswordEditText = view.findViewById(R.id.editTextConfirmPassword);
        genderSpinner = view.findViewById(R.id.spinnerGender);
        addButton = view.findViewById(R.id.btnAddAdmin);

        // Initialize database helper
        dbHelper = new DatabaseHelper(getContext());

        // Set click listener for the add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate input fields
                if (validateInputs()) {
                    // Add admin logic here
                    addAdminToDatabase();
                }
            }
        });

        return view;
    }

    // Method to validate input fields
    private boolean validateInputs() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String gender = genderSpinner.getSelectedItem() != null ? genderSpinner.getSelectedItem().toString() : "";

        // التحقق من الحروف فقط في حقول الاسم
        if (!TextUtils.isEmpty(firstName) && !firstName.matches("[a-zA-Z]+")) {
            Toast.makeText(getContext(), "First name should contain letters only", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!TextUtils.isEmpty(lastName) && !lastName.matches("[a-zA-Z]+")) {
            Toast.makeText(getContext(), "Last name should contain letters only", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidEmail(email)) {
            emailEditText.setError("Enter a valid email address (at least 4 characters before '@', ending with @yahoo.com)");
            return false;
        }

        if (!phone.matches("^05\\d{8}$")) {
            Toast.makeText(getContext(), "Phone number must be exactly 10 digits starting with 05", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (firstName.length() < 3) {
            Toast.makeText(getContext(), "First name must be at least 3 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (lastName.length() < 3) {
            Toast.makeText(getContext(), "Last name must be at least 3 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 8 || !password.matches(".*\\d.*") || !password.matches(".*[a-zA-Z].*")) {
            Toast.makeText(getContext(), "Password must be at least 8 characters long and include at least one letter and one number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Method to validate email address
    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^.{4,}@(?:yahoo)\\.com$");
        return pattern.matcher(email).matches();
    }

    // Method to encrypt password using SHA-256
    private String encryptPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to add admin to the database
    private void addAdminToDatabase() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String gender = genderSpinner.getSelectedItem() != null ? genderSpinner.getSelectedItem().toString() : "";

        // Validate email
        if (!isValidEmail(email)) {
            emailEditText.setError("Enter a valid email address (at least 4 characters before '@', ending with @yahoo.com)");
            return;
        }

        // Validate phone number
        if (!phone.matches("^05\\d{8}$")) {
            Toast.makeText(getContext(), "Phone number must be exactly 10 digits starting with 05", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate first name length
        if (firstName.length() < 3) {
            Toast.makeText(getContext(), "First name must be at least 3 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate last name length
        if (lastName.length() < 3) {
            Toast.makeText(getContext(), "Last name must be at least 3 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate password
        if (password.length() < 8 || !password.matches(".*\\d.*") || !password.matches(".*[a-zA-Z].*")) {
            Toast.makeText(getContext(), "Password must be at least 8 characters long and include at least one letter and one number", Toast.LENGTH_SHORT).show();
            return;
        }

        String encryptedPassword = encryptPassword(password);

        if (encryptedPassword == null) {
            Toast.makeText(getContext(), "Failed to encrypt password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert admin info into database
        boolean isInserted = dbHelper.insertAdmin(email, phone, firstName, lastName, gender, password);

        if (isInserted) {
            Toast.makeText(getContext(), "Admin added successfully", Toast.LENGTH_SHORT).show();
            clearInputs();
        } else {
            Toast.makeText(getContext(), "Failed to add admin", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to clear input fields after successful addition
    private void clearInputs() {
        firstNameEditText.setText("");
        lastNameEditText.setText("");
        emailEditText.setText("");
        phoneEditText.setText("");
        passwordEditText.setText("");
        confirmPasswordEditText.setText("");
        genderSpinner.setSelection(0);
    }


}
