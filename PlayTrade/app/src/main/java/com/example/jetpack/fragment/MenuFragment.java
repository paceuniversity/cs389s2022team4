package com.example.jetpack.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.jetpack.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * 上传
 */
public class MenuFragment extends Fragment {
    private Activity myActivity;//上下文
    //Private variables of post function by Yuxiang
    private EditText pName,condition,quantity,brand,disc;
    private Button button;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");

    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity.setContentView(R.layout.activity_main);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity= (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_post,container,false);

        //获取控件
        initView();
        return view;

    }

    /**
     * 初始化页面
     */
    private void initView() {

    }
    private void loadData(){

    }
    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            loadData();//加载数据
        }
    }
}
