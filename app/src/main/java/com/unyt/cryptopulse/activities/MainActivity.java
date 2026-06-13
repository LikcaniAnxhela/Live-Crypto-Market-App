package com.unyt.cryptopulse.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.unyt.cryptopulse.R;
import com.unyt.cryptopulse.fragments.FavoritesFragment;
import com.unyt.cryptopulse.fragments.HomeFragment;
import com.unyt.cryptopulse.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        boolean loggedIn = preferences.getBoolean("logged_in", false);

        if (!loggedIn) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        load(new HomeFragment());

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_favorites) {
                load(new FavoritesFragment());
            } else if (id == R.id.nav_profile) {
                load(new ProfileFragment());
            } else {
                load(new HomeFragment());
            }

            return true;
        });
    }

    private void load(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}