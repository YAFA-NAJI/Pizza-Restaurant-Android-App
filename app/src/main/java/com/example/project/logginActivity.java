package com.example.project;

import static com.example.project.DatabaseHelper.COLUMN_EMAIL_ADMINS;
import static com.example.project.DatabaseHelper.COLUMN_PASSWORD_ADMINS;
import static com.example.project.DatabaseHelper.TABLE_NAME_ADMINS;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.text.method.PasswordTransformationMethod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;


public class logginActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "reset_password_channel";

    // Define a constant for notification ID
    private static final int NOTIFICATION_ID = 1001;

    private EditText editTextEmail, editTextPassword, editTextPhoneNumber;
    private CheckBox checkBoxRememberMe;
    private SharedPreferences sharedPreferences;
    private ToggleButton toggleButtonShowPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logginactivity);

        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        checkBoxRememberMe = findViewById(R.id.checkBoxRememberMe);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonSignUp = findViewById(R.id.SignUp);
        toggleButtonShowPassword = findViewById(R.id.toggleButtonShowPassword);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Add admin information to the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = dbHelper.addAdminInfo(db);
        if (result != -1) {
            // Admin info added successfully
            Log.d("AdminInfo", "Admin info added to database");
        } else {
            // Failed to add admin info
            Log.d("AdminInfo", "Failed to add admin info to database");
        }

        // Check if Remember Me is checked and load email
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);
        if (rememberMe) {
            String savedEmail = sharedPreferences.getString("email", "");
            editTextEmail.setText(savedEmail);
            checkBoxRememberMe.setChecked(true);

        }

        // Handle Login button click
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // Handle SignUp button click
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });



        // Toggle password visibility
        toggleButtonShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Show password
                    editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // Hide password
                    editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                // Move cursor to the end of the text
                editTextPassword.setSelection(editTextPassword.getText().length());
            }
        });
    }

    // Method to send verification notification
    private void sendVerificationNotification(int verificationCode) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Reset Password")
                .setContentText("Your verification code is: " + verificationCode)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }


    // Method to check if email exists in the database
    private boolean isEmailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String tableName = userinfo.UserEntry.TABLE_NAME;

        String[] projection = {
                userinfo.UserEntry.COLUMN_EMAIL
        };

        String selection = userinfo.UserEntry.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(
                tableName,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean emailExists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return emailExists;
    }

    // Method to generate a random verification code
    private int generateVerificationCode() {
        Random random = new Random();
        return 10000 + random.nextInt(90000); // Random number between 10000 and 99999
    }

    // Method to handle login functionality
    private void login() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }
        logAllAdmins();

        if (verifyAdmin(email, password)) {
            Intent intent = new Intent(logginActivity.this, Admin.class); // توجيه المستخدم الإداري إلى صفحة الإدارة
            startActivity(intent);
            finish();

        } else if (verifyaddAdmin(email, password)) {
            // New admin login successful
            Intent intent = new Intent(logginActivity.this, Admin.class);
            startActivity(intent);
            finish();

        }else if (verifyUser(email, password)) {
            Intent intent = new Intent(logginActivity.this, normal.class); // توجيه المستخدم العادي إلى صفحة عادية
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }

        if (checkBoxRememberMe.isChecked()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email", email);
            editor.putBoolean("rememberMe", true);
            editor.apply();
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("email");
            editor.putBoolean("rememberMe", false);
            editor.apply();
        }
    }

    // Method to verify user credentials
    private boolean verifyUser(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                userinfo.UserEntry.COLUMN_EMAIL
        };

        String selection = userinfo.UserEntry.COLUMN_EMAIL + " = ? AND " +
                userinfo.UserEntry.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(
                userinfo.UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean userExists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return userExists;
    }

    // Method to verify admin credentials
    private boolean verifyAdmin(String email, String password) {
        // Retrieve admin information from the database based on the provided email
        Cursor cursor = dbHelper.getAdminInfoByEmail(email);

        // Check if the cursor is not null and move to the first row
        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve the password from the database
            String storedPassword = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD_ADMINS));

            // Check if the provided password matches the stored password
            boolean passwordMatches = password.equals(storedPassword);

            // Close the cursor
            cursor.close();

            // Return true if both email and password match, otherwise false
            return passwordMatches;
        } else {
            // If no admin with the provided email is found, return false
            return false;
        }
    }

    // Method to verify admin credentials
    private boolean verifyaddAdmin(String email, String password) {
        // Check if email ends with @yahoo.com
        if (!email.endsWith("@yahoo.com")) {
            return false; // If email does not end with @yahoo.com, return false immediately
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {COLUMN_EMAIL_ADMINS};
        String selection = COLUMN_EMAIL_ADMINS + " = ? AND " +
                COLUMN_PASSWORD_ADMINS + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_NAME_ADMINS, projection, selection, selectionArgs, null, null, null);

        boolean adminExists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return adminExists;
    }


    public void logAllAdmins() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Update the query to include a WHERE clause for emails ending with @yahoo.com
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_ADMINS + " WHERE " + COLUMN_EMAIL_ADMINS + " LIKE '%@yahoo.com'", null);

        if (cursor.moveToFirst()) {
            do {
                String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_ADMINS));
                String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD_ADMINS));
                Log.d("AdminData", "Email: " + email + ", Password: " + password);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }


}