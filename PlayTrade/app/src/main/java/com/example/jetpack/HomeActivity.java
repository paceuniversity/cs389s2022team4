package com.example.jetpack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void car_detailActivity(View view) {
        Intent intent = new Intent(this, activity_car_detail.class);
        startActivity(intent);
    }

    public void stack_detailActivity(View view) {
        Intent intent = new Intent(this, activity_stack_detail.class);
        startActivity(intent);
    }
}