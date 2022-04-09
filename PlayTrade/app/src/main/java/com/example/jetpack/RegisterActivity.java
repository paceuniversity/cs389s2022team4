package com.example.jetpack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
//import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import com.example.jetpack.util.UccOpenHelper;


public class RegisterActivity extends AppCompatActivity {

    private Button Btn;
    private EditText emailTextView, passwordTextView;
    private ProgressBar progressbar;
    private UccOpenHelper dbHelper;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email);
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

//        initView();
//        initData();
//        dbHelper = new UccOpenHelper(this, "BookStore.db", null, 2);
    }

    private void registerNewUser()
    {

        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = emailTextView.getText().toString().trim();
        password = passwordTextView.getText().toString().trim();

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // create new user or register new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(),"Registration successful!",
                                    Toast.LENGTH_LONG).show();

                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);

                            // if the user created intent to login activity
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            // Registration failed
                            Toast.makeText(getApplicationContext(),"Registration failed!!"
                                            + " Please try again later", Toast.LENGTH_LONG).show();
                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);
                        }
                    }
                });
    }

//    private void initView() {
//        btn_registered = findViewById(R.id.btn_registered);
//        ed_name = findViewById(R.id.ed_name);
//        ed_pass = findViewById(R.id.ed_pass);
//        ed_password = findViewById(R.id.ed_password);
//    }

//    private void initData() {
//
//        btn_registered.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                ContentValues values = new ContentValues();
//                values.put("name",ed_name.getText().toString());
//                values.put("password",ed_pass.getText().toString());
//                db.insert("user", null, values);
////                Toast.makeText(RegisterActivity.this, ""+values, Toast.LENGTH_SHORT).show();
//                if (!TextUtils.equals(ed_pass.getText().toString(),ed_password.getText().toString())){
//                    Toast.makeText(RegisterActivity.this, "Password not Same", Toast.LENGTH_SHORT).show();
//                }
//                else if (TextUtils.isEmpty(ed_name.getText()) && TextUtils.isEmpty(ed_pass.getText()) && TextUtils.isEmpty(ed_password.getText())) {
//                    Toast.makeText(RegisterActivity.this, "The account password cannot be empty", Toast.LENGTH_SHORT).show();
//                } else {
//                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//                }
//
//                values.clear();
//
//            }

//
}
