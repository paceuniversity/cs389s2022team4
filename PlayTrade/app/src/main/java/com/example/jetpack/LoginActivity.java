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

//import android.database.sqlite.SQLiteDatabase;
//import com.example.jetpack.util.UccOpenHelper;

public class LoginActivity extends AppCompatActivity {

    private Button Btn_login;
    private Button Btn_register;
    private EditText emailTextView, passwordTextView;
    private UccOpenHelper dbHelper;
    private FirebaseAuth mAuth;
    private ProgressBar progressbar;

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }
//
//    private void updateUI(FirebaseUser currentUser) {
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = (FirebaseAuth)FirebaseAuth.getInstance();

        emailTextView = (EditText) findViewById(R.id.email);
        passwordTextView = (EditText)findViewById(R.id.pass);
        Btn_login = (Button) findViewById(R.id.but_login);
        Btn_register = (Button) findViewById(R.id.but_register);
   

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
//        initView();
//        initData();
//        dbHelper = new UccOpenHelper(this, "BookStore.db", null, 2);

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
            emailTextView.setError("Email is required!");
            emailTextView.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailTextView.setError("Please enter a valid email!");
            emailTextView.requestFocus();
            return;
        }

        if(password.isEmpty()){
            passwordTextView.setError("Password is required!");
            passwordTextView.requestFocus();
            return;
        }

        if(password.length() < 6){
            passwordTextView.setError("Minimum password length should be 6 characters!");
            passwordTextView.requestFocus();
            return;
        }

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // sign in existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                            // hide the progress bar
                            //progressbar.setVisibility(View.GONE);
                            // if sign-in is successful, intent to home activity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // sign-in failed
                            Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_LONG).show();
                            // hide the progress bar

                        }
                    }
                });
    }

}
//
//    //找控件的id
//    private void initView() {
//        login = findViewById(R.id.but_login);
//        register = findViewById(R.id.but_register);
//        et_name = findViewById(R.id.et_name);
//        et_pass = findViewById(R.id.et_pass);
//    }
//
//    //通过id进行点击事件
//    private void initData() {
//        login.setOnClickListener(new View.OnClickListener() {
//
//            private Integer userId;
//            private String password;
//            private String name;
//
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(LoginActivity.this, "" + et_name.getText(), Toast.LENGTH_SHORT).show();
////                Log.i("TAG1", "onClick: "+et_name.getText());
//                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                Cursor cursor = db.query("user", null, null, null, null, null, null);
//                if (cursor.moveToFirst()) {
//                    do {
//                        userId = cursor.getInt(cursor.getColumnIndex("id"));
//                        name = cursor.getString(cursor.getColumnIndex("name"));
//                        password = cursor.getString(cursor.getColumnIndex("password"));
//
//                    } while (
//                            cursor.moveToNext()
//                    );
//                }
//                if (name == null) {
//                    Toast.makeText(LoginActivity.this, "Can not found this account", Toast.LENGTH_SHORT).show();
//                } else if (et_name.getText().toString().length() != name.length()) {
//                    Toast.makeText(LoginActivity.this, "The account password cannot be empty", Toast.LENGTH_SHORT).show();
//                } else if (et_pass.getText().toString().length()!=password.length()){
//                    Toast.makeText(LoginActivity.this, "The account password cannot be empty", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
////                  //
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    intent.putExtra("userId",userId);
//                    startActivity(intent);
//                }
//                cursor.close();
////                if (TextUtils.isEmpty(et_name.getText()) && TextUtils.isEmpty(et_pass.getText())) {
////                    Toast.makeText(MainActivity.this, "The account password cannot be empty", Toast.LENGTH_SHORT).show();
////                } else {
////                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
////
////                }
//            }
//        });
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(intent);
//            }
//        });
//    }

