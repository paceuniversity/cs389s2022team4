package com.example.jetpack.util;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jetpack.R;
import com.example.jetpack.Upload;
import com.example.jetpack.fragment.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class detail extends AppCompatActivity {
    ImageView Image;
    TextView pName;
    TextView condition;
    TextView quantity;
    TextView brand;
    TextView description;
    TextView email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Image = findViewById(R.id.image_detail);
        pName = findViewById(R.id.car_topic);
        condition = findViewById(R.id.condition_detail);
        quantity = findViewById(R.id.quantity_datail);
        brand = findViewById(R.id.brand_detail);
        description = findViewById(R.id.description);
        email= findViewById(R.id.email_detail);
        //Passing data from ImageAdapter to detail
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        Upload upload = (Upload)bundle.getSerializable("key");
        // Showing images and details
        Picasso.get()
                .load(upload.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(Image);
        pName.setText(upload.getPName());
        condition.setText(upload.getCondition());
        quantity.setText(upload.getQuantity());
        brand.setText(upload.getBrand());
        description.setText(upload.getDescription());
        email.setText(upload.getEmail());

    }
}
