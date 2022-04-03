package com.example.jetpack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jetpack.util.UccOpenHelper;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private Button register;
    private EditText et_name;
    private EditText et_pass;
    private UccOpenHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
        dbHelper = new UccOpenHelper(this, "BookStore.db", null, 2);

    }

    //找控件的id
    private void initView() {
        login = findViewById(R.id.but_login);
        register = findViewById(R.id.but_register);
        et_name = findViewById(R.id.et_name);
        et_pass = findViewById(R.id.et_pass);
    }

    //通过id进行点击事件
    private void initData() {
        login.setOnClickListener(new View.OnClickListener() {

            private Integer userId;
            private String password;
            private String name;

            @Override
            public void onClick(View v) {
//                Toast.makeText(LoginActivity.this, "" + et_name.getText(), Toast.LENGTH_SHORT).show();
//                Log.i("TAG1", "onClick: "+et_name.getText());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("user", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        userId = cursor.getInt(cursor.getColumnIndex("id"));
                        name = cursor.getString(cursor.getColumnIndex("name"));
                        password = cursor.getString(cursor.getColumnIndex("password"));

                    } while (
                            cursor.moveToNext()
                    );
                }
                if (name == null) {
                    Toast.makeText(LoginActivity.this, "Can not found this account", Toast.LENGTH_SHORT).show();
                } else if (et_name.getText().toString().length() != name.length()) {
                    Toast.makeText(LoginActivity.this, "The account password cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (et_pass.getText().toString().length()!=password.length()){
                    Toast.makeText(LoginActivity.this, "The account password cannot be empty", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
//                  //
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userId",userId);
                    startActivity(intent);
                }
                cursor.close();
//                if (TextUtils.isEmpty(et_name.getText()) && TextUtils.isEmpty(et_pass.getText())) {
//                    Toast.makeText(MainActivity.this, "The account password cannot be empty", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
//
//                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

}