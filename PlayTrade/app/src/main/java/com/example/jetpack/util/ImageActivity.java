package com.example.jetpack.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jetpack.R;
import com.example.jetpack.Upload;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private SearchView searchView;
    private ImageAdapter mAdapter;

    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;
    private String keyword = "";
    private DataSnapshot dataSnapshot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        mRecyclerView = findViewById(R.id.recycler_view);
        searchView = findViewById(R.id.search);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUploads = new ArrayList();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot datasnapshot) {
                dataSnapshot = datasnapshot;
                mUploads.addAll(findAllUpload());
                mAdapter = new ImageAdapter(ImageActivity.this, mUploads);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ImageActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //下面是我写的代码，搜索按钮点击
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                keyword = searchView.getQuery().toString();
                changeList();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    this.onQueryTextSubmit("");
                }
                return true;
            }
        });

    }

    public void detail_toy(View view) {
        Intent intent = new Intent(this, detail.class);
        startActivity(intent);
    }

    //下面是我写的代码,匹配字符串
    public boolean foundKeyWord(Upload upload) {
        if (keyword.isEmpty()) return true;
        return upload.getPName().toUpperCase().contains(keyword.toUpperCase());
    }


    //下面是我写的代码,获取所有上传对象
    public List<Upload> findAllUpload() {
        if (dataSnapshot == null) new ArrayList<Upload>();
        List<Upload> uploads = new ArrayList<>();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            Upload upload = postSnapshot.getValue(Upload.class);
            if (foundKeyWord(upload)) {
                uploads.add(upload);
            }
        }
        return uploads;
    }


    // 下面是我写的代码,清空列表数据
    void cleanData() {
        mUploads.clear();
        mAdapter.notifyDataSetChanged();
    }

    // 下面是我写的代码,刷新数据
    void changeList() {
        cleanData();
        mUploads.addAll(findAllUpload());
        mAdapter.notifyDataSetChanged();
    }

    // 下面是我写的代码,刷新数据
    public static void closeKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
