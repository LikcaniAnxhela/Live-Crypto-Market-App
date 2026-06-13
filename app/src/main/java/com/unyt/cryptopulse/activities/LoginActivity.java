package com.unyt.cryptopulse.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.unyt.cryptopulse.R;
import com.unyt.cryptopulse.database.AppDatabase;
import com.unyt.cryptopulse.database.User;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_login);

        EditText usernameInput = findViewById(R.id.usernameInput);
        EditText passwordInput = findViewById(R.id.passwordInput);

        Button loginButton = findViewById(R.id.loginButton);
        Button signupButton = findViewById(R.id.signupButton);

        loginButton.setOnClickListener(v -> {
            String email = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            User existingUser = AppDatabase
                    .getInstance(this)
                    .userDao()
                    .findByEmail(email);

            if (existingUser == null) {
                Toast.makeText(this, "User does not exist. Please create an account.", Toast.LENGTH_LONG).show();
                return;
            }

            User loggedInUser = AppDatabase
                    .getInstance(this)
                    .userDao()
                    .login(email, password);

            if (loggedInUser == null) {
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
            preferences.edit()
                    .putBoolean("logged_in", true)
                    .putString("user_email", loggedInUser.email)
                    .putString("user_name", loggedInUser.name)
                    .apply();

            Toast.makeText(this, "Welcome, " + loggedInUser.name, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        });
    }
}