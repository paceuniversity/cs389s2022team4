package com.example.jetpack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostActivity extends AppCompatActivity {
    //Private variables of post function by Yuxiang
    private EditText pName,condition,quantity,brand,disc;
    private Button button;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // post function by Yuxiang
        pName = findViewById(R.id.product_name_edit);
        condition= findViewById(R.id.condition_edit);
        quantity= findViewById(R.id.quantity_edit);
        brand= findViewById(R.id.brand_edit);
        disc= findViewById(R.id.discription_edit);
        button = findViewById(R.id.button_submit);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String name = pName.getText().toString();
                root.setValue(name);
            }
        });
    }
}