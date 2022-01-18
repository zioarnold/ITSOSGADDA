package com.example.arnold.itsosgadda.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import com.example.arnold.itsosgadda.R;
import com.example.arnold.itsosgadda.databinding.SignInLayoutBinding;
import com.example.arnold.itsosgadda.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private SignInLayoutBinding binding;
    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("USER_EMAIL", account.getEmail());
            intent.putExtra("USER_NAME", account.getDisplayName());
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = SignInLayoutBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            android.Manifest.permission.GET_ACCOUNTS,
                            android.Manifest.permission.LOCATION_HARDWARE,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                    }, 1);

            findViewById(R.id.sign_in_button).setOnClickListener(view -> {
                int id = view.getId();
                if (id == R.id.sign_in_button) {
                    signIn();
                }
            });
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            googleSignInClient = GoogleSignIn.getClient(this, gso);
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if (account != null) {
                updateUI(account);
            } else {
                Log.d(TAG, "User didn't signed in!");
                Toast.makeText(this, "User didn't signed in", Toast.LENGTH_LONG).show();
            }
        } catch (Exception exception) {
            Log.d(TAG, exception.getMessage());
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("USER_EMAIL", account.getEmail());
                    intent.putExtra("USER_NAME", account.getDisplayName());
                    startActivity(intent);
                }
            } catch (ApiException e) {
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }

    public void updateUI(GoogleSignInAccount account) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USER_EMAIL", account.getEmail());
        intent.putExtra("USER_NAME", account.getDisplayName());
        startActivity(intent);
    }
}
