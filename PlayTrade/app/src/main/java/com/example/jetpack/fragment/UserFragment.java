package com.example.jetpack.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jetpack.LoginActivity;
import com.example.jetpack.MainActivity;
import com.example.jetpack.R;
import com.example.jetpack.bean.User;
import com.example.jetpack.util.GlideEngine;
import com.example.jetpack.util.UccOpenHelper;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.util.List;


/**
 * 我的
 */
public class UserFragment extends Fragment {
    private Activity mActivity;
    private ImageView ivPhoto;
    private TextView tvNickName;
    private TextView tv_name;
    private EditText et_phone;
    private EditText et_address;
    private Button btnLogout;
    private UccOpenHelper dbHelper;
    private String imagePath = "";
    private User mUser = null;
    private RequestOptions headerRO = new RequestOptions().circleCrop();//圆角变换
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user,container,false);
        ivPhoto = view.findViewById(R.id.iv_photo);
        tvNickName = view.findViewById(R.id.tv_nickName);
        tv_name = view.findViewById(R.id.tv_name);
        et_phone = view.findViewById(R.id.et_phone);
        et_address = view.findViewById(R.id.et_address);
        btnLogout = view.findViewById(R.id.logout);
        dbHelper = new UccOpenHelper(mActivity, "BookStore.db", null, 2);
        initData();
        initView();
        return view;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Integer userId = MainActivity.userId;
        String sql = "select * from user where id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                String dbName = cursor.getString(1);
                String dbPassword = cursor.getString(2);
                String dbPhone = cursor.getString(3);
                String dbAddress= cursor.getString(4);
                String dbPhoto = cursor.getString(5);
                mUser = new User(dbId, dbName, dbPassword,dbPhone,dbAddress,dbPhoto);
            }
        }
        db.close();
        tvNickName.setText(mUser.getName());
        tv_name.setText(mUser.getName());
        et_phone.setText(mUser.getPhone());
        et_address.setText(mUser.getAddress());
        Glide.with(mActivity)
                .load(mUser.getPhoto())
                .apply(headerRO.error( R.drawable.ic_default_man))
                .into(ivPhoto);
    }

    private void initView() {
        //保存
        MainActivity.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String phone = et_phone.getText().toString();
                String address = et_address.getText().toString();
                db.execSQL("update user set phone = ?,address = ? where id = ?", new Object[]{phone,address,mUser.getId()});
                Toast.makeText(mActivity,"Update successful",Toast.LENGTH_SHORT).show();

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
                startActivity(new Intent(mActivity, LoginActivity.class));
                mActivity.finish();

            }
        });
    }
    /**
     * 选择图片
     */
    private void selectClick() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
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
                                .apply(headerRO.error(R.drawable.ic_default_man ))
                                .into(ivPhoto);
                        db.execSQL("update user set photo = ? where id = ?", new Object[]{imagePath,mUser.getId()});
                        db.close();
                        Toast.makeText(mActivity,"Update successful",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        // onCancel Callback
                    }
                });
    }

}
