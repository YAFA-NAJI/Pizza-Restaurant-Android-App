package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Registration extends AppCompatActivity {

    private Button loginButton;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Find the Login button
        loginButton = findViewById(R.id.buttonLogin);
        // Set click listener for the Login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Login activity
                Intent intent = new Intent(Registration.this, logginActivity.class);
                startActivity(intent);
            }
        });

        // Find the Sign Up button
        signUpButton = findViewById(R.id.buttonSignUp);
        // Set click listener for the Sign Up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Sign Up activity
                Intent intent = new Intent(Registration.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // Start the horizontal animation for buttons
        startHorizontalAnimation(loginButton);
        startHorizontalAnimation(signUpButton);
    }

    private void startHorizontalAnimation(final Button button) {
        // Calculate the movement distance for animation
        final int moveDistance = 100; // Adjust this value as needed

        // Create translate animation
        TranslateAnimation animation = new TranslateAnimation(0, moveDistance, 0, 0);
        animation.setDuration(1000); // Duration in milliseconds
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); // Repeat indefinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse the animation direction on repeat

        // Apply animation to button
        button.startAnimation(animation);
    }
}
