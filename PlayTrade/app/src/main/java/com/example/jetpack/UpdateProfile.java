package com.example.jetpack;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jetpack.bean.User;
import com.example.jetpack.fragment.UserFragment;
import com.example.jetpack.util.GlideEngine;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.util.List;

public class UpdateProfile extends AppCompatActivity {

    Activity context;
    private FirebaseUser user;
    private String userID;

    private String imagePath = "";
    private User mUser = null;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RequestOptions headerRO = new RequestOptions().circleCrop();//圆角变换

    private ImageView ivPhoto;
    private TextView emailTextView, nameTextView;
    private EditText changePhone, changeAddress;
    private Button btnSave;
    private ProgressBar progressbar;

    private FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.activity_update_profile));
       // context = mActivity;
        mAuth = FirebaseAuth.getInstance();

        this.ivPhoto = findViewById(R.id.iv_photo);
        this.changePhone = findViewById(R.id.et_phone);
        this.changeAddress = findViewById(R.id.et_address);
        this.btnSave = findViewById(R.id.saveProfile);

        progressbar = findViewById(R.id.progressbar);

        initView();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        //dbHelper = new UccOpenHelper(mActivity, "BookStore.db", null, 2);
        //initData();
        //initView();
        //updateProfile();
        //return view;
    }

    private void updateProfile() {

        if(isPhoneChanged() || isAddressChanged()){
            Toast.makeText(this,"Update Successful!", Toast.LENGTH_LONG).show();
        }else Toast.makeText(this, "Update Failed!", Toast.LENGTH_LONG).show();


        //dbHelper = new UccOpenHelper(mActivity, "BookStore.db", null, 2);
        //initData();
        //initView();
        //updateProfile();
        //return view;
    }

    private boolean isPhoneChanged() {
        EditText changePhone = findViewById(R.id.et_phone);
        final boolean[] ans = {false};
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String phone = userProfile.getPhone();
                    String address = userProfile.getAddress();

                    if (phone.equals(changePhone.getText().toString())) {
                        reference.child(userID).child("phone").setValue(changePhone.getEditableText().toString());
                        ans[0] = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String PHONE = reference.child(userID).child("phone").getDatabase().toString();


//        if(PHONE.equals(changePhone.getText().toString())){
//            reference.child(userID).child("phone").setValue(changePhone.getEditableText().toString());
//            return true;
//        }else{
//            return false;
//        }

        return ans[0];
    }

    private boolean isAddressChanged() {
        String ADDRESS = reference.child(userID).child("address").getKey().toString();
        EditText changeAddress = findViewById(R.id.et_address);
        if(ADDRESS.equals(changeAddress.getText().toString())){
            reference.child(userID).child("address").setValue(changeAddress.getText().toString());
            return true;
        }else{
            return false;
        }

    }
/**
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = mAuth.getCurrentUser();
        String currentEmail = user.getEmail();

        documentReference.collection("user").document(currentEmail);

        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            String nameResult = task.getResult().getString("name");
                            String emailResult = task.getResult().getString("email");

                            tv_name.setText(nameResult);
                            tv_email.setText(emailResult);
                        }else {
                            Toast.makeText(UpdateProfile.this, "No Profile", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }*/


    private void initView() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        TextView nameTextView = findViewById(R.id.name_onfile);
        TextView emailTextView = findViewById(R.id.email_onfile);
        EditText changePhone = findViewById(R.id.et_phone);
        EditText changeAddress = findViewById(R.id.et_address);
        Button saveChange = findViewById(R.id.saveProfile);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String name = userProfile.getName();
                    String email = userProfile.getEmail();
                    String phone = userProfile.getPhone();
                    String address = userProfile.getAddress();

                    nameTextView.setText(name);
                    emailTextView.setText(email);
                    changePhone.setText(phone);
                    changeAddress.setText(address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //从相册中选择头像
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePhoto();
            }
        });

        // Save profile
        saveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateProfile.this, UserFragment.class);
                startActivity(intent);
            }
        });
    }

    private void changePhoto() {
        //SQLiteDatabase db = dbHelper.getWritableDatabase();
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofAll())
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(1)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        for (int i = 0; i < result.size(); i++) {
                            // onResult Callback
                            LocalMedia media = result.get(i);
                            String path;
                            // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                            boolean compressPath = media.isCompressed() || (media.isCut() && media.isCompressed());
                            // 裁剪过
                            boolean isCutPath = media.isCut() && !media.isCompressed();
                            if (isCutPath) {
                                path = media.getCutPath();
                            } else if (compressPath) {
                                path = media.getCompressPath();
                            } else if (!TextUtils.isEmpty(media.getAndroidQToPath())) {
                                // AndroidQ特有path
                                path = media.getAndroidQToPath();
                            } else if (!TextUtils.isEmpty(media.getRealPath())) {
                                // 原图
                                path = media.getRealPath();
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    path = PictureFileUtils.getPath(context, Uri.parse(media.getPath()));
                                } else {
                                    path = media.getPath();
                                }
                            }
                            imagePath = path;
                        }
//                        if (context == null) {
//                            return;
//                        }
                        Glide.with(context)
                                .load(imagePath)
                                .apply(headerRO.error(R.drawable.ic_default_man ))
                                .into(ivPhoto);
                        //db.execSQL("update user set photo = ? where id = ?", new Object[]{imagePath,mUser.getId()});
                        //db.close();
                        Toast.makeText(context,"Update successful",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        // onCancel Callback
                    }
                });
    }

}