package com.example.jetpack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class RegisterActivity extends AppCompatActivity {

    private Button btn_registered;
    private EditText ed_name;
    private EditText ed_pass;
    private EditText ed_password;
    private UccOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initData();
        dbHelper = new UccOpenHelper(this, "BookStore.db", null, 2);
    }

    private void initView() {
        btn_registered = findViewById(R.id.btn_registered);
        ed_name = findViewById(R.id.ed_name);
        ed_pass = findViewById(R.id.ed_pass);
        ed_password = findViewById(R.id.ed_password);
    }

    private void initData() {

        btn_registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name",ed_name.getText().toString());
                values.put("password",ed_pass.getText().toString());
                db.insert("Book", null, values);
//                Toast.makeText(RegisterActivity.this, ""+values, Toast.LENGTH_SHORT).show();
                if (!TextUtils.equals(ed_pass.getText().toString(),ed_password.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "Password not Same", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(ed_name.getText()) && TextUtils.isEmpty(ed_pass.getText()) && TextUtils.isEmpty(ed_password.getText())) {
                    Toast.makeText(RegisterActivity.this, "The account password cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }

                values.clear();

            }
        });
    }
}