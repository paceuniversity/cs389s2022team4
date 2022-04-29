package com.example.jetpack;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button Btn_login;
    private Button Btn_register;
    private EditText emailTextView, passwordTextView;
    private UccOpenHelper dbHelper;
    private FirebaseAuth mAuth;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = (FirebaseAuth)FirebaseAuth.getInstance();

        emailTextView = (EditText) findViewById(R.id.email);
        passwordTextView = (EditText)findViewById(R.id.pass);
        Btn_login = (Button) findViewById(R.id.but_login);
        Btn_register = (Button) findViewById(R.id.but_register);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        // Set on Click Listener on Sign-in button
        Btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();
            }
        });

        Btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void loginUserAccount() {
        // show the visibility of progress bar to show loading
        //progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings

        String email = (String) emailTextView.getText().toString().trim();
        String password = (String) passwordTextView.getText().toString().trim();

        if(email.isEmpty()){
            emailTextView.setError("Please enter your email!");
            emailTextView.requestFocus();
            return;
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailTextView.setError("Please enter a valid email!");
            emailTextView.requestFocus();
            return;
        }

        else if(password.isEmpty()){
            passwordTextView.setError("Please enter your password!");
            passwordTextView.requestFocus();
            return;
        }

        else if(password.length() < 6){
            passwordTextView.setError("Minimum password length should be 6 characters!");
            passwordTextView.requestFocus();
            return;
        }

        // sign in existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();

                            // if sign-in is successful, intent to home activity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // sign-in failed
                            Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
