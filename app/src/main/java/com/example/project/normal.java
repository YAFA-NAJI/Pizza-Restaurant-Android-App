package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.project.databinding.ActivityNormalBinding;
import com.google.android.material.navigation.NavigationView;

public class normal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNormalBinding binding;
    private DrawerLayout drawer;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNormalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNormal.toolbar);
        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_menu, R.id.nav_order,
                R.id.nav_favorite, R.id.nav_offeser, R.id.customer_profile_fragment, R.id.nav_call, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_normal);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    navController.navigate(R.id.nav_home);
                } else if (id == R.id.nav_menu) {
                    navController.navigate(R.id.nav_menu);
                } else if (id == R.id.nav_order) {
                    navController.navigate(R.id.nav_order);
                } else if (id == R.id.nav_favorites) {
                    navController.navigate(R.id.nav_favorite);
                } else if (id == R.id.nav_offeser) {
                    navController.navigate(R.id.nav_offeser);
                } else if (id == R.id.customer_profile_fragment) {
                    navController.navigate(R.id.customer_profile_fragment);
                }else if (id == R.id.nav_call) {
                    navController.navigate(R.id.nav_call);
                } else if (id == R.id.nav_logout) {
                    logout();
                    Intent intent = new Intent(normal.this, logginActivity.class);
                    startActivity(intent);
                    finish();
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void logout() {
        // Implement logout functionality here
        // For example: clear user session, etc.
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
    }
}
