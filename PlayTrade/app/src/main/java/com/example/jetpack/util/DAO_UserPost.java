package com.example.jetpack.util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DAO_UserPost {
    private DatabaseReference databaseReference;
    public DAO_UserPost(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(UserPost.class.getSimpleName());

    }
    public Task<Void> add(UserPost emp){

        return databaseReference.push().setValue(emp);
    }

}

