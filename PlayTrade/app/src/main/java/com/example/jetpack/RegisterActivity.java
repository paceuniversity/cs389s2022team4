package com.example.jetpack;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jetpack.bean.User;
import com.example.jetpack.util.UccOpenHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

//import android.database.sqlite.SQLiteDatabase;


public class RegisterActivity extends AppCompatActivity {

    private Button Btn;
    private EditText emailTextView, passwordTextView, nameTextView, phoneTextView, addTextView;
    private ProgressBar progressbar;
    private UccOpenHelper dbHelper;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        nameTextView = findViewById(R.id.name);
        emailTextView = findViewById(R.id.email);
        phoneTextView = findViewById(R.id.re_phone);
        addTextView = findViewById(R.id.re_address);
        passwordTextView =  findViewById(R.id.pass);
        passwordTextView = findViewById(R.id.password);
        Btn = findViewById(R.id.btn_registered);
        progressbar = findViewById(R.id.progressbar);

        // Set on Click Listener on Registration button
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                registerNewUser();
            }
        });
    }

    @SuppressLint("ResourceType")
    private void registerNewUser()
    {

        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email = emailTextView.getText().toString().trim();
        String name = nameTextView.getText().toString().trim();
        String phone = phoneTextView.getText().toString().trim();
        String address = addTextView.getText().toString().trim();
        String password = passwordTextView.getText().toString().trim();
        View photo = findViewById(R.drawable.ic_default_man);

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        } else if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter your name!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter your phone number!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        } else if (TextUtils.isEmpty(address)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter your address information!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user = new User(name, email, password, phone, address, photo);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "User has been registered successful!", Toast.LENGTH_LONG).show();
                                        progressbar.setVisibility(View.GONE);

                                    }else{
                                        Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                        progressbar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    }
                });
    }

}
