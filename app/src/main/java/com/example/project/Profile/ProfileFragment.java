package com.example.project.Profile;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Admin;
import com.example.project.DatabaseHelper;
import com.example.project.R;
import com.example.project.normal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProfileFragment extends AppCompatActivity {

    // تعريف المتغيرات اللازمة
    private EditText emailEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText phoneEditText;
    private EditText passwordEditText;
    private Button updateButton;
    private Button changePictureButton;
    private ImageView imageProfile;
    private DatabaseHelper dbHelper;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        // ربط المتغيرات بعناصر الواجهة
        emailEditText = findViewById(R.id.editTextEmail);
        if (emailEditText == null) {
            throw new NullPointerException("EditText emailEditText is null. Please check your layout.");
        }
        imageProfile = findViewById(R.id.imageProfile);
        firstNameEditText = findViewById(R.id.editTextFirstName);
        lastNameEditText = findViewById(R.id.editTextLastName);
        phoneEditText = findViewById(R.id.editTextPhone);
        passwordEditText = findViewById(R.id.editTextPassword);
        updateButton = findViewById(R.id.btnUpdateProfile);
        changePictureButton = findViewById(R.id.btnChangePicture);
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

            // Load user profile picture from database
            loadProfilePicture(email);
        } else {
            Toast.makeText(this, "No data received", Toast.LENGTH_SHORT).show();
        }

        // Set up click listener for update button
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    String userEmail = emailEditText.getText().toString().trim();
                    updateUserProfile(userEmail);
                }
            }
        });

        // Set up click listener for change picture button
        changePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });
    }

    // دالة للتحقق من صحة المدخلات
    private boolean validateInput() {
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

    // دالة لتحميل صورة المستخدم من قاعدة البيانات وعرضها
    private void loadProfilePicture(String userEmail) {
        byte[] profilePicture = dbHelper.getUserProfilePicture(userEmail);
        if (profilePicture != null) {
            imageProfile.setImageBitmap(BitmapFactory.decodeByteArray(profilePicture, 0, profilePicture.length));
        } else {
            Toast.makeText(this, "No profile picture found", Toast.LENGTH_SHORT).show();
        }
    }

    // دالة لتحديث بيانات المستخدم في قاعدة البيانات
    private void updateUserProfile(String userEmail) {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        boolean isUpdated = dbHelper.updateUserInfo(userEmail, firstName, lastName, phone, password);
        if (isUpdated) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

            // تحديث بيانات المستخدم في viewInfo قبل الانتقال إليها
            Intent viewInfoIntent = new Intent(ProfileFragment.this, normal.class);
            viewInfoIntent.putExtra("email", userEmail);
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

    // دالة لفتح مستعرض الصور لاختيار صورة جديدة
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // معالجة نتيجة اختيار الصورة
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                byte[] imageBytes = getBytes(inputStream);
                imageProfile.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));

                // تحديث صورة الملف الشخصي في قاعدة البيانات
                String userEmail = emailEditText.getText().toString().trim();
                dbHelper.updateCustomerProfilePicture(userEmail, imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // دالة لتحويل InputStream إلى byte[]
    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
