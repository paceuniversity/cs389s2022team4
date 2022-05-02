package com.example.jetpack.fragment;

import static com.example.jetpack.MainActivity.observer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jetpack.LoginActivity;
import com.example.jetpack.R;
import com.example.jetpack.UpdateProfile;
import com.example.jetpack.bean.User;
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
import com.luck.picture.lib.tools.SPUtils;

import java.util.List;
import java.util.Observable;


/**
 * My Profile
 */
public class UserFragment extends Fragment {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RequestOptions headerRO = new RequestOptions().circleCrop();//圆角变换

    private ImageView ivPhoto;
    private String imagePath = "";
    Activity context;
    private FirebaseUser user;
    private String userID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater ,@NonNull ViewGroup container , @NonNull Bundle savedInstanceState) {
        context = getActivity();
        //data();
        return inflater.inflate(R.layout.fragment_user ,container,false);
    }

    public void onStart(){
        super.onStart();
        data();
    }

    public void data() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        TextView nameTextView = context.findViewById(R.id.name_onfile);
        TextView emailTextView = context.findViewById(R.id.email_onfile);
        TextView phoneTextView = context.findViewById(R.id.phone_onfile);
        TextView addressTextView = context.findViewById(R.id.address_onfile);
        Button editButton = context.findViewById(R.id.btn_edit_profile);
        Button logoutButton = context.findViewById(R.id.btn_logout);
        Button updateColor = context.findViewById(R.id.btn_changeColor);

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
                    phoneTextView.setText(phone);
                    addressTextView.setText(address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Edit profile
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateProfile.class);
                startActivity(intent);
            }
        });
//

//        // Change Color
//        updateColor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showChooseColor();
//            }
//        });


        // 从相册中选择头像
//        ivPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectClick();
//            }
//        });

        // Logout
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    /// 下面是我写的代码，选择颜色 Haonan
    public void showChooseColor() {
        int[] colors = {R.color.colorThemeRed, R.color.colorThemeBlue, R.color.colorThemeGreen};
        int colorIndex = SPUtils.getInstance().getInt("colorIndex");
        colorIndex = colorIndex == -1 ? 0 : colorIndex;
        final CharSequence[] charSequence = new CharSequence[]{"Red", "Blue", "Green"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose color")
                .setSingleChoiceItems(charSequence, colorIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        observer.update(new Observable(), colors[which]);
                        SPUtils.getInstance().put("color", colors[which]);
                        SPUtils.getInstance().put("colorIndex", which);
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }


    /**
     * 选择图片
     */

    private void selectClick() {
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
                        Glide.with(context)
                                .load(imagePath)
                                .apply(headerRO.error(R.drawable.ic_default_man))
                                .into(ivPhoto);
                        Toast.makeText(context, "Update successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        // onCancel Callback
                    }
                });
    }


}
