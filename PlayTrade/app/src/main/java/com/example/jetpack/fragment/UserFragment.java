package com.example.jetpack.fragment;

import static com.example.jetpack.MainActivity.observer;

import android.app.Activity;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jetpack.LoginActivity;
import com.example.jetpack.MainActivity;
import com.example.jetpack.R;
import com.example.jetpack.UpdatePassWordActivity;
import com.example.jetpack.bean.User;
import com.example.jetpack.util.GlideEngine;
import com.example.jetpack.util.UccOpenHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.tools.PictureFileUtils;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.luck.picture.lib.tools.SPUtils;
import com.luck.picture.lib.tools.ToastUtils;

import java.util.List;
import java.util.Observable;


/**
 * 我的
 */
public class UserFragment extends Fragment {

    private static final String SPLIT_TAG = ",";
    private Activity mActivity;
    private ImageView ivPhoto;
    private TextView tvNickName;
    private TextView tv_email;
    private EditText et_phone;
    private EditText et_nickName;
    private Button btnLogout;
    private String imagePath = "";
    private Button updatePassword;
    private Button updateColor;
    private RequestOptions headerRO = new RequestOptions().circleCrop();//圆角变换

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ivPhoto = view.findViewById(R.id.iv_photo);
        tvNickName = view.findViewById(R.id.tv_nickName);
        tv_email = view.findViewById(R.id.tv_email);
        et_phone = view.findViewById(R.id.et_phone);
        et_nickName = view.findViewById(R.id.et_address);
        btnLogout = view.findViewById(R.id.logout);
        updatePassword = view.findViewById(R.id.updatePassword);
        updateColor = view.findViewById(R.id.updateColor);
        initData();
        initView();
        return view;
    }


    private void initData() {
        // 这里是我写的代码，获取昵称字段然后拆分出电话号码 Haonan
        tvNickName.setText(getNickName());
        tv_email.setText(currentUser.getEmail());
        String extra = currentUser.getDisplayName();
        et_nickName.setText(getNickName());
        if (!TextUtils.isEmpty(extra) && extra.contains(SPLIT_TAG)) {
            String models[] = extra.split(SPLIT_TAG);
            if (models.length >= 2 && models[1] != null) {
                et_phone.setText(models[1]);
            }
        }
//        Glide.with(mActivity)
//                .load(mUser.getPhoto())
//                .apply(headerRO.error(R.drawable.ic_default_man))
//                .into(ivPhoto);
    }

    private void initView() {
        //保存
        MainActivity.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //这里是我写的代码，获取昵称字段和电话字段 放在昵称中进行存储 Haonan
                String phone = et_phone.getText().toString();
                String nickName = et_nickName.getText().toString();
                String cache = nickName + SPLIT_TAG + phone;
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(cache).build();
                currentUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        ToastUtils.s(mActivity, "update Success");
                    }
                });
            }
        });
        //从相册中选择头像
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectClick();
            }
        });
        //退出登录
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
//                startActivity(new Intent(mActivity, MainActivity.class));
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        });

        // 修改密码
        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePassWordActivity.startUpdatePassWordActivity(mActivity);
            }
        });

        // 修改颜色
        updateColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseColor();
//
//                new ColorSheet().colorPicker(colors, 1, true, ).show(getFragmentManager());
            }
        });
    }


    /// 下面是我写的代码，选择颜色 Haonan
    void showChooseColor() {
        int[] colors = {R.color.colorThemeRed, R.color.colorThemeBlue, R.color.colorThemeGreen};
        int colorIndex = SPUtils.getInstance().getInt("colorIndex");
        colorIndex = colorIndex == -1 ? 0 : colorIndex;
        final CharSequence[] charSequence = new CharSequence[]{"Red", "Blue", "Green"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
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
                                    path = PictureFileUtils.getPath(mActivity, Uri.parse(media.getPath()));
                                } else {
                                    path = media.getPath();
                                }
                            }
                            imagePath = path;
                        }
                        Glide.with(mActivity)
                                .load(imagePath)
                                .apply(headerRO.error(R.drawable.ic_default_man))
                                .into(ivPhoto);
                        Toast.makeText(mActivity, "Update successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        // onCancel Callback
                    }
                });
    }


    /// 这是我写的代码，获取名称
    public String getNickName() {
        String extra = currentUser.getDisplayName();
        if (!TextUtils.isEmpty(extra) && extra.contains(SPLIT_TAG)) {
            String models[] = extra.split(SPLIT_TAG);
            //
            if (models.length >= 1 && models[0] != null) {
                return models[0];
            }
        }
        return "";
    }


}
