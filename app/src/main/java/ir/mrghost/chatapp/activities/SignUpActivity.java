package ir.mrghost.chatapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import ir.mrghost.chatapp.databinding.ActivitySignUpBinding;
import ir.mrghost.chatapp.utils.Constants;
import ir.mrghost.chatapp.utils.PreferenceManager;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
    }

    private void setListeners() {
        binding.signInAccountText.setOnClickListener(v -> onBackPressed());
        binding.signUpBtn.setOnClickListener(v -> {
            if (isValidSignUpDetail()){
                signUp();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext() , message, Toast.LENGTH_SHORT).show();
    }

    private void signUp() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String , Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME , binding.inputName.getText().toString());
        user.put(Constants.KEY_EMAIL , binding.inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD , binding.inputPassword.getText().toString());
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN , true);
                    preferenceManager.putString(Constants.KEY_USER_ID , documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME , binding.inputName.getText().toString());
                    Intent intent = new Intent(this , MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }).addOnFailureListener(e -> {
                    loading(false);
                    showToast(e.getMessage());
                });
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult() , result -> {
                if (result.getResultCode() == RESULT_OK){
                    if (result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            });

    private Boolean isValidSignUpDetail() {
        if (binding.inputName.getText().toString().trim().isEmpty()) {
            showToast("Enter Name !");
            return false;
        } else if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter Email !");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Enter Valid Email !");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter Password !");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().length() < 8) {
            showToast("Password MUST be at least 8 Characters!");
            return false;
        } else if (binding.inputConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Confirm Password !");
            return false;
        } else if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())) {
            showToast("Password & Confirm password must be same !");
            return false;
        } else {
            return true;
        }

    }

    private void loading(boolean isLoading){
        if (isLoading){
            binding.signUpBtn.setVisibility(View.INVISIBLE);
            binding.prograssBar.setVisibility(View.VISIBLE);
        } else {
            binding.prograssBar.setVisibility(View.INVISIBLE);
            binding.signUpBtn.setVisibility(View.VISIBLE);
        }
    }

}