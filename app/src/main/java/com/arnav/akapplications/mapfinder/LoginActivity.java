package com.arnav.akapplications.mapfinder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    SignInButton gmailSignInButton;
    GoogleSignInClient googleSignInClient;
    private static final String TAG = "LoginActivity";
    public static final String GOOGLE_ACCOUNT = "google_account";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gmailSignInButton = findViewById(R.id.gmailSignInButton);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
        gmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent,101);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
            switch (requestCode)
            {
                case 101:
                    try
                    {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                        onLoggedIn(googleSignInAccount);
                    } catch (ApiException e) {
                        Log.w(TAG,"SIGN IN FAILED WITH CODE:"+ e.getStatusCode());
                    }
                    break;
            }
    }

    private void onLoggedIn(GoogleSignInAccount googleSignInAccount)
    {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(MainActivity.GOOGLE_ACCOUNT,googleSignInAccount);

        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(googleSignInAccount != null)
        {
//            Toast.makeText(this,"already logged in",Toast.LENGTH_LONG).show();
            onLoggedIn(googleSignInAccount);
        }
        else
        {
            Log.d(TAG, "not logged in");
        }
    }

}
