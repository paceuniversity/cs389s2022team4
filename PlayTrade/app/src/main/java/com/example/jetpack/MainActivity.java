package com.example.jetpack;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.jetpack.fragment.HomeFragment;
import com.example.jetpack.fragment.MenuFragment;
import com.example.jetpack.fragment.UserFragment;
import com.example.jetpack.util.ImageActivity;
import com.jaeger.library.StatusBarUtil;
import com.luck.picture.lib.tools.SPUtils;

import java.util.Observable;
import java.util.Observer;


/**
 * 主页面
 */
public class MainActivity extends AppCompatActivity {
    private Activity myActivity;
    private TextView tvTitle;
    private LinearLayout llContent;
    private RadioButton rbHome;
    private RadioButton rbUpload;
    private RadioButton rbUser;
    final Fragment[] fragments = new Fragment[]{null, null,null};//存放Fragment
    public static Integer userId;
    public static TextView tvSave;
    public RelativeLayout titleParent;
    public static Observer observer;


    @Override
    public void onCreate( Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            myActivity = this;
            setContentView(R.layout.activity_main);
            tvTitle = (TextView) findViewById(R.id.title);
            llContent = (LinearLayout) findViewById(R.id.ll_main_content);
            rbHome = (RadioButton) findViewById(R.id.rb_main_home);
            rbUpload = (RadioButton) findViewById(R.id.rb_main_upload);
            rbUser = (RadioButton) findViewById(R.id.rb_main_user);
            tvSave = findViewById(R.id.save);
            userId = getIntent().getIntExtra("userId", 0);

        initView();
        setViewListener();

        /// 下面是我写的代码,收到通知修改颜色
        observer = (o, arg) -> {
            int color = (int) arg;
            StatusBarUtil.setColor(MainActivity.this, ContextCompat.getColor(myActivity, color), 0);
            titleParent.setBackgroundColor(ContextCompat.getColor(myActivity,color));
        };
        int cacheColor = SPUtils.getInstance().getInt("color");
        if (cacheColor != -1) {
            observer.update(new Observable(), cacheColor);
        }

    }
    private void setViewListener() {
        rbHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTitle.setText("Home");
                switchFragment(0);
                tvSave.setVisibility(View.GONE);
            }
        });
        rbUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTitle.setText("Upload");
                switchFragment(1);
                tvSave.setVisibility(View.GONE);
            }
        });
        rbUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTitle.setText("My Profile");
                switchFragment(2);
                tvSave.setVisibility(View.VISIBLE);
            }
        });


    }

    private void initView() {
        //设置导航栏图标样式
        Drawable iconHome=getResources().getDrawable(R.drawable.selector_main_rb_home);//设置主页图标样式
        iconHome.setBounds(0,0,68,68);//设置图标边距 大小
        rbHome.setCompoundDrawables(null,iconHome,null,null);//设置图标位置
        rbHome.setCompoundDrawablePadding(5);//设置文字与图片的间距
        Drawable iconUpload=getResources().getDrawable(R.drawable.selector_main_rb_upload);//设置主页图标样式
        iconUpload.setBounds(0,0,68,68);//设置图标边距 大小
        rbUpload.setCompoundDrawables(null,iconUpload,null,null);//设置图标位置
        rbUpload.setCompoundDrawablePadding(5);//设置文字与图片的间距
        Drawable iconUser=getResources().getDrawable(R.drawable.selector_main_rb_user);//设置主页图标样式
        iconUser.setBounds(0,0,68,68);//设置图标边距 大小
        rbUser.setCompoundDrawables(null,iconUser,null,null);//设置图标位置
        rbUser.setCompoundDrawablePadding(5);//设置文字与图片的间距
        switchFragment(0);
        rbHome.setChecked(true);
    }

//     方法 - 切换Fragment
//     @param fragmentIndex 要显示Fragment的索引

    private void switchFragment(int fragmentIndex) {
        //在Activity中显示Fragment
        //1、获取Fragment管理器 FragmentManager
        FragmentManager fragmentManager=this.getSupportFragmentManager();
        //2、开启fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //懒加载 - 如果需要显示的Fragment为null，就new。并添加到Fragment事务中
        if (fragments[fragmentIndex] == null) {
            switch (fragmentIndex) {
                case 0:
                    fragments[fragmentIndex] = new HomeFragment();
                    break;
                case 1:
                    fragments[fragmentIndex] = new MenuFragment();
                    break;
                case 2:
                    fragments[fragmentIndex] = new UserFragment();
                    break;
            }
            //==添加Fragment对象到Fragment事务中
            //参数：显示Fragment的容器的ID，Fragment对象
            transaction.add(R.id.ll_main_content, fragments[fragmentIndex]);

        }

        //隐藏其他的Fragment
        for (int i = 0; i < fragments.length; i++) {
            if (fragmentIndex != i && fragments[i] != null) {
                //隐藏指定的Fragment
                transaction.hide(fragments[i]);
            }
        }
        //4、显示Fragment
        transaction.show(fragments[fragmentIndex]);

        //5、提交事务
        transaction.commit();
    }

//     * 双击退出
//     *
//     * @param keyCode
//     * @param event
//     * @return

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }

        return false;
    }

    private long time = 0;

    public void exit() {
        if (System.currentTimeMillis() - time > 2000) {
            time = System.currentTimeMillis();
            Toast.makeText(myActivity,"Click again to exit the app", Toast.LENGTH_LONG).show();
        } else {
            finish();
        }

    }

    // item details method made by Yuxiang
    public void car_detailActivity(View view) {
        Intent intent = new Intent(this, activity_car_detail.class);
        startActivity(intent);
    }
    //item details method made by Yuxiang
    public void stack_detailActivity(View view) {
        Intent intent = new Intent(this, activity_stack_detail.class);
        startActivity(intent);
    }
    //Top right back button on post page
    public void back(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    public void openImageActivity(View view) {
        Intent intent = new Intent (this, ImageActivity.class);
        startActivity(intent);
    }
}
