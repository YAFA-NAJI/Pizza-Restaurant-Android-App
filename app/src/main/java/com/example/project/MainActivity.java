package com.example.project;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Connection;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        linearLayout = findViewById(R.id.layout);
        progressBar = findViewById(R.id.progressBar);

        setProgress(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform rotation animation on button click
                Animation animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_animation);
                button.startAnimation(animRotate);
                // Delay for 2 seconds before executing the connection task
                button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Connection connectionAsyncTask = new Connection(MainActivity.this);
                        connectionAsyncTask.execute("https://18fbea62d74a40eab49f72e12163fe6c.api.mockbin.io/");
                    }
                }, 2000); // 2000 milliseconds = 2 seconds delay
            }
        });
    }

    public void setButtonText(String text) {
        button.setText(text);
    }

    public void fillTypes(List<String> types) {
        // Clear previous views if any
        linearLayout.removeAllViews();
        for (String type : types) {
            // Create a new TextView for each type and add it to the LinearLayout
            TextView textView = new TextView(this);
            textView.setText(type);
            linearLayout.addView(textView);
        }
    }

    public void setProgress(boolean progress) {
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public void reset() {
        setButtonText("Get Started");

        linearLayout.removeAllViews();
        setProgress(false);
    }
}
