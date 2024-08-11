package com.example.project.AdminProfile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Admin;
import com.example.project.DatabaseHelper;
import com.example.project.R;

public class UpdateInfoActivity extends AppCompatActivity {

    // تعريف المتغيرات اللازمة
    private EditText emailEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText phoneEditText;
    private EditText passwordEditText;
    private Button btnSaveChanges;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        // ربط المتغيرات بعناصر الواجهة
        emailEditText = findViewById(R.id.editTextEmail);
        firstNameEditText = findViewById(R.id.editTextFirstName);
        lastNameEditText = findViewById(R.id.editTextLastName);
        phoneEditText = findViewById(R.id.editTextPhone);
        passwordEditText = findViewById(R.id.editTextPassword);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        dbHelper = new DatabaseHelper(this);

        // Retrieve data from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("email")) {
            String email = intent.getStringExtra("email");
            String firstName = intent.getStringExtra("first_name");
            String lastName = intent.getStringExtra("last_name");
            String phone = intent.getStringExtra("phone");
            String password = intent.getStringExtra("password");

            // Display user info in EditTexts
            emailEditText.setText(email);
            firstNameEditText.setText(firstName);
            lastNameEditText.setText(lastName);
            phoneEditText.setText(phone);
            passwordEditText.setText(password);
        }

        // Set up click listener for update button
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adminEmail = emailEditText.getText().toString().trim();
                if (validateInput()) {
                    updateProfile(adminEmail);
                }
            }
        });
    }

    // دالة لتحديث بيانات المستخدم في قاعدة البيانات
    private void updateProfile(String adminEmail) {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        boolean isUpdated = dbHelper.updateAdminInfo(adminEmail, firstName, lastName, phone, password);
        if (isUpdated) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

            // تحديث بيانات المستخدم في viewInfo قبل الانتقال إليها
            Intent viewInfoIntent = new Intent(UpdateInfoActivity.this, Admin.class);
            viewInfoIntent.putExtra("email", adminEmail);
            viewInfoIntent.putExtra("first_name", firstName);
            viewInfoIntent.putExtra("last_name", lastName);
            viewInfoIntent.putExtra("phone", phone);
            viewInfoIntent.putExtra("password", password);

            startActivity(viewInfoIntent);

            // إغلاق النشاط الحالي إذا كان هناك حاجة لذلك
            finish();
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }

    // دالة للتحقق من صحة المدخلات
    private boolean validateInput() {
        // Validate first name
        // التحقق من أن الاسم الأول يحتوي على حروف فقط ولا يقل عن 3 أحرف
        String firstName = firstNameEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(firstName)) {
            if (!firstName.matches("[a-zA-Z]+")) {
                firstNameEditText.setError("First name should contain letters only");
                return false;
            } else if (firstName.length() < 3) {
                firstNameEditText.setError("First name must be at least 3 characters");
                return false;
            }
        } else {
            firstNameEditText.setError("Please enter your first name");
            return false;
        }

        // التحقق من أن اسم العائلة يحتوي على حروف فقط ولا يقل عن 3 أحرف
        String lastName = lastNameEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(lastName)) {
            if (!lastName.matches("[a-zA-Z]+")) {
                lastNameEditText.setError("Last name should contain letters only");
                return false;
            } else if (lastName.length() < 3) {
                lastNameEditText.setError("Last name must be at least 3 characters");
                return false;
            }
        } else {
            lastNameEditText.setError("Please enter your last name");
            return false;
        }

        // التحقق من صحة كلمة المرور
        String password = passwordEditText.getText().toString();
        if (password.length() < 8 || !password.matches(".*\\d.*") || !password.matches(".*[a-zA-Z].*")) {
            passwordEditText.setError("Password must be at least 8 characters and contain at least one letter and one number");
            return false;
        }

        // التحقق من صحة رقم الهاتف
        String phoneNumber = phoneEditText.getText().toString();
        if (!phoneNumber.startsWith("05") || phoneNumber.length() != 10) {
            phoneEditText.setError("Phone number must start with '05' and contain exactly 10 digits");
            return false;
        }

        return true;
    }
}
