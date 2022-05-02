package com.example.jetpack.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.jetpack.R;


/**
 *首页
 */
public class HomeFragment extends Fragment {
    private Activity myActivity;//上下文
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newhome, container, false);

        return view;
    }


    /**
     * 初始化
     */
    private void initView() {

        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData(){

    }
}
