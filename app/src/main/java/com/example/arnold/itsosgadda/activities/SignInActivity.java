package com.example.arnold.itsosgadda.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
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
    private static final int RC_SIGN_IN = 0;

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            com.example.arnold.itsosgadda.databinding.SignInLayoutBinding binding = SignInLayoutBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            android.Manifest.permission.GET_ACCOUNTS,
                            android.Manifest.permission.LOCATION_HARDWARE,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    1);

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
            updateUI(account);
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
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            } catch (ApiException e) {
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }

    public void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Log.d(TAG, "You didn't signed in");
        }
    }
}
