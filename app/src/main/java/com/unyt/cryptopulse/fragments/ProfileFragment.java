package com.unyt.cryptopulse.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.unyt.cryptopulse.R;
import com.unyt.cryptopulse.activities.LoginActivity;
import com.unyt.cryptopulse.database.AppDatabase;
import com.unyt.cryptopulse.database.User;

import java.io.File;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    private ImageView profileImage;
    private TextView imageSourceText;

    private Uri cameraImageUri;

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;

    private ActivityResultLauncher<String> cameraPermissionLauncher;
    private ActivityResultLauncher<String> galleryPermissionLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();

                        if (selectedImageUri != null) {
                            profileImage.setImageURI(selectedImageUri);

                            String imageName = getDisplayNameFromUri(selectedImageUri);

                            SharedPreferences preferences = requireActivity()
                                    .getSharedPreferences("user_session", Context.MODE_PRIVATE);

                            preferences.edit()
                                    .putString("profile_image_uri", selectedImageUri.toString())
                                    .putString("profile_image_source", "Gallery: " + imageName)
                                    .apply();

                            imageSourceText.setText("Profile image source: Gallery\nFile: " + imageName);

                            Toast.makeText(requireContext(), "Profile picture uploaded from gallery", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    if (success && cameraImageUri != null) {
                        profileImage.setImageURI(cameraImageUri);

                        SharedPreferences preferences = requireActivity()
                                .getSharedPreferences("user_session", Context.MODE_PRIVATE);

                        preferences.edit()
                                .putString("profile_image_uri", cameraImageUri.toString())
                                .putString("profile_image_source", "Camera photo")
                                .apply();

                        imageSourceText.setText("Profile image source: Camera photo");

                        Toast.makeText(requireContext(), "Profile photo taken successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Camera action cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        cameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openCamera();
                    } else {
                        Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        galleryPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openGallery();
                    } else {
                        Toast.makeText(requireContext(), "Gallery permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = view.findViewById(R.id.profileImage);
        imageSourceText = view.findViewById(R.id.imageSourceText);

        TextView userNameText = view.findViewById(R.id.userNameText);
        TextView userEmailText = view.findViewById(R.id.userEmailText);

        EditText oldPasswordInput = view.findViewById(R.id.oldPasswordInput);
        EditText newPasswordInput = view.findViewById(R.id.newPasswordInput);
        EditText confirmNewPasswordInput = view.findViewById(R.id.confirmNewPasswordInput);

        Button takePhotoButton = view.findViewById(R.id.takePhotoButton);
        Button chooseGalleryButton = view.findViewById(R.id.chooseGalleryButton);
        Button changePasswordButton = view.findViewById(R.id.changePasswordButton);
        Button emailButton = view.findViewById(R.id.emailButton);
        Button logoutButton = view.findViewById(R.id.logoutButton);

        SharedPreferences preferences = requireActivity()
                .getSharedPreferences("user_session", Context.MODE_PRIVATE);

        String userName = preferences.getString("user_name", "Unknown user");
        String userEmail = preferences.getString("user_email", "No email found");

        userNameText.setText("Name: " + userName);
        userEmailText.setText("Email: " + userEmail);

        String savedImageUri = preferences.getString("profile_image_uri", null);
        String savedImageSource = preferences.getString("profile_image_source", "none");

        if (savedImageUri != null) {
            profileImage.setImageURI(Uri.parse(savedImageUri));
            imageSourceText.setText("Profile image source: " + savedImageSource);
        }

        takePhotoButton.setOnClickListener(v -> checkCameraPermissionAndOpen());

        chooseGalleryButton.setOnClickListener(v -> checkGalleryPermissionAndOpen());

        changePasswordButton.setOnClickListener(v -> {
            String oldPassword = oldPasswordInput.getText().toString().trim();
            String newPassword = newPasswordInput.getText().toString().trim();
            String confirmNewPassword = confirmNewPasswordInput.getText().toString().trim();

            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Please complete all password fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.length() < 4) {
                Toast.makeText(requireContext(), "New password must be at least 4 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmNewPassword)) {
                Toast.makeText(requireContext(), "New passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            User loggedInUser = AppDatabase
                    .getInstance(requireContext())
                    .userDao()
                    .login(userEmail, oldPassword);

            if (loggedInUser == null) {
                Toast.makeText(requireContext(), "Current password is incorrect", Toast.LENGTH_SHORT).show();
                return;
            }

            AppDatabase
                    .getInstance(requireContext())
                    .userDao()
                    .updatePassword(userEmail, newPassword);

            oldPasswordInput.setText("");
            newPasswordInput.setText("");
            confirmNewPasswordInput.setText("");

            Toast.makeText(requireContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
        });

        emailButton.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "CryptoPulse Project");

            startActivity(emailIntent);
        });

        logoutButton.setOnClickListener(v -> {
            preferences.edit().clear().apply();

            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }

    private void checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void checkGalleryPermissionAndOpen() {
        String permission;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(requireContext(), permission)
                == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            galleryPermissionLauncher.launch(permission);
        }
    }

    private void openCamera() {
        try {
            cameraImageUri = createImageUri();
            cameraLauncher.launch(cameraImageUri);
        } catch (IOException e) {
            Toast.makeText(requireContext(), "Could not create image file", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );

        galleryLauncher.launch(galleryIntent);
    }

    private Uri createImageUri() throws IOException {
        File imageDirectory = new File(requireContext().getExternalFilesDir(null), "Pictures");

        if (!imageDirectory.exists()) {
            imageDirectory.mkdirs();
        }

        File imageFile = File.createTempFile(
                "profile_photo_",
                ".jpg",
                imageDirectory
        );

        return FileProvider.getUriForFile(
                requireContext(),
                requireContext().getPackageName() + ".fileprovider",
                imageFile
        );
    }

    private String getDisplayNameFromUri(Uri uri) {
        String displayName = "Selected image";

        Cursor cursor = requireContext()
                .getContentResolver()
                .query(uri, null, null, null, null);

        if (cursor != null) {
            try {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

                if (nameIndex >= 0 && cursor.moveToFirst()) {
                    displayName = cursor.getString(nameIndex);
                }
            } finally {
                cursor.close();
            }
        }

        return displayName;
    }
}