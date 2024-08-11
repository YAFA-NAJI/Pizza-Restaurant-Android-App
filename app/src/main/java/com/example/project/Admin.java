package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.project.AdminProfile.UpdateInfoActivity;
import com.example.project.offeser.AddSpecialOfferActivity;
import com.example.project.databinding.ActivityAdminBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class Admin extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityAdminBinding binding;
    private DrawerLayout drawer;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarAdmin.toolbar);
        binding.appBarAdmin.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });

        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.admin_profile_fragment, R.id.add_admin_fragment, R.id.view_all_orders_fragment,
                R.id.add_special_offers_fragment, R.id.order_statistics_fragment, R.id.logout_fragment)
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_admin);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_admin_profile) {
                    navController.navigate(R.id.admin_profile_fragment);
                } else if (id == R.id.nav_add_admin) {
                    navController.navigate(R.id.add_admin_fragment);
                } else if (id == R.id.nav_view_all_orders) {
                    navController.navigate(R.id.view_all_orders_fragment);
                } else if (id == R.id.nav_add_special_offers) {
                    navController.navigate(R.id.add_special_offers_fragment);
                } else if (id == R.id.nav_order_statistics) {
                    navController.navigate(R.id.order_statistics_fragment);
                } else if (id == R.id.nav_logout) {
                    logout();
                    Intent intent = new Intent(Admin.this, logginActivity.class);
                    startActivity(intent);
                    finish();
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void logout() {
        // Perform logout operation here
        // For example, clear user session, delete cached data, etc.
    }
}
