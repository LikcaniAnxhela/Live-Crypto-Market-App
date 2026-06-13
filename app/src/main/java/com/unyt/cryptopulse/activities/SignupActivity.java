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

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_signup);

        EditText nameInput = findViewById(R.id.nameInput);
        EditText emailInput = findViewById(R.id.emailInput);
        EditText passInput = findViewById(R.id.passInput);
        EditText confirmPassInput = findViewById(R.id.confirmPassInput);

        Button createButton = findViewById(R.id.createButton);
        Button backLoginButton = findViewById(R.id.backLoginButton);

        createButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passInput.getText().toString().trim();
            String confirmPassword = confirmPassInput.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.contains("@") || !email.contains(".")) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 4) {
                Toast.makeText(this, "Password must be at least 4 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            int exists = AppDatabase
                    .getInstance(this)
                    .userDao()
                    .emailExists(email);

            if (exists > 0) {
                Toast.makeText(this, "This email already has an account", Toast.LENGTH_LONG).show();
                return;
            }

            User newUser = new User(email, name, password);

            AppDatabase
                    .getInstance(this)
                    .userDao()
                    .insert(newUser);

            SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
            preferences.edit()
                    .putBoolean("logged_in", true)
                    .putString("user_email", email)
                    .putString("user_name", name)
                    .apply();

            Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        backLoginButton.setOnClickListener(v -> finish());
    }
}