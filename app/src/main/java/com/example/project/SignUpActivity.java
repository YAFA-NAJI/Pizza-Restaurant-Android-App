package com.example.project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPhone, editTextFirstName, editTextLastName, editTextPassword, editTextConfirmPassword;
    private Spinner spinnerGender;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupactivity);

        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        spinnerGender = findViewById(R.id.spinnerGender);

        // Set up gender spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        // Set up sign-up button click listener
        Button buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);
    }

    private void signUp() {
        // Get input values
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        String gender = spinnerGender.getSelectedItem().toString();

        // Perform validation
        if (!isValidEmail(email)) {
            editTextEmail.setError("Enter a valid email address (at least 4 characters before '@', ending with @gmail.com or @hotmail.com)");
            return;
        }

        // Validate phone number
        if (!isValidPhoneNumber(phone)) {
            editTextPhone.setError("Enter a valid phone number starting with '05'");
            return;
        }

        // Validate first name
        if (!isValidName(firstName)) {
            editTextFirstName.setError("Enter a valid first name");
            return;
        }

        // Validate last name
        if (!isValidName(lastName)) {
            editTextLastName.setError("Enter a valid last name");
            return;
        }

        // Validate password
        if (!isValidPassword(password)) {
            editTextPassword.setError("Password must be at least 8 characters long and contain at least 1 letter and 1 number");
            return;
        }

        // Validate confirm password
        if (!confirmPassword.equals(password)) {
            editTextConfirmPassword.setError("Passwords do not match");
            return;
        }

        // Register user in the database
        registerUser(email, phone, firstName, lastName, gender, password);
    }

    private boolean isValidEmail(String email) {
        // Ensure the local-part (before the '@') has at least 4 characters and ends with '@gmail.com' or '@hotmail.com'
        Pattern pattern = Pattern.compile("^.{4,}@(?:gmail|hotmail)\\.com$");
        boolean isValid = pattern.matcher(email).matches();

        if (!isValid) {
            Toast.makeText(this, "Enter a valid email address (at least 4 characters before '@', ending with @gmail.com or @hotmail.com)", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if the email is already in use by a user
        if (emailExists(email)) {
            Toast.makeText(this, "Email already exists, please use a different email", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if the email is already in use by an admin
        if (email.equals(Admininfo.DEFAULT_ADMIN_EMAIL)) {
            Toast.makeText(this, "Email already exists, please use a different email", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isValidPhoneNumber(String phone) {
        return Pattern.matches("^05\\d{8}$", phone);
    }

    private boolean isValidName(String name) {
        // التحقق من أن الاسم لا يحتوي على أرقام
        if (name.matches(".*\\d.*")) {
            return false; // يحتوي على أرقام
        }

        // التحقق من أن الاسم يحتوي على 3 أحرف على الأقل
        return name.length() >= 3;
    }

    private boolean isValidPassword(String password) {
        // Password must be at least 8 characters long and contain at least 1 letter and 1 number
        return Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$").matcher(password).matches();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] byteData = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : byteData) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerUser(String email, String phone, String firstName, String lastName, String gender, String hashedPassword) {
        // Get the database in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a ContentValues object to store the user data
        ContentValues values = new ContentValues();
        values.put(userinfo.UserEntry.COLUMN_EMAIL, email);
        values.put(userinfo.UserEntry.COLUMN_PHONE, phone);
        values.put(userinfo.UserEntry.COLUMN_FIRST_NAME, firstName);
        values.put(userinfo.UserEntry.COLUMN_LAST_NAME, lastName);
        values.put(userinfo.UserEntry.COLUMN_GENDER, gender);
        values.put(userinfo.UserEntry.COLUMN_PASSWORD, hashedPassword);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(userinfo.UserEntry.TABLE_NAME, null, values);

        // Check if the insertion was successful
        if (newRowId != -1) {
            // Registration successful, navigate to login activity
            Intent intent = new Intent(SignUpActivity.this, logginActivity.class);
            startActivity(intent);
            finish(); // Finish SignUpActivity so it's not in the back stack
            Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();
        } else {
            // Failed to register user
            Toast.makeText(this, "Failed to register user", Toast.LENGTH_SHORT).show();
        }

        // Close the database connection
        db.close();
    }

    private boolean emailExists(String email) {
        // Get a readable database
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {userinfo.UserEntry.COLUMN_EMAIL};
        String selection = userinfo.UserEntry.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};

        // Query the database to check if the email exists
        Cursor cursor = db.query(
                userinfo.UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean exists = cursor.getCount() > 0;

        // Close the cursor and database
        cursor.close();
        db.close();

        return exists;
    }
}
