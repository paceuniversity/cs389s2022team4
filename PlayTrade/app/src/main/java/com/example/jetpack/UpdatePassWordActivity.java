package com.example.jetpack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.luck.picture.lib.tools.ToastUtils;

//This activity ↓ is detail views by Haonan
public class UpdatePassWordActivity extends AppCompatActivity {

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    EditText etOldPassWord;
    EditText etPassWordNew;
    EditText etConfirmPassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        etPassWordNew = findViewById(R.id.etPassWordNew);
        etConfirmPassWord = findViewById(R.id.etConfirmPassWord);
        initView();
    }


    void initView() {
        // 这是我写的代码， 修改密码的逻辑  Haonan
        findViewById(R.id.updatePassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etPassWordNewContent = etPassWordNew.getText().toString();
                String etConfirmPassWordContent = etConfirmPassWord.getText().toString();
                if (!etPassWordNewContent.equals(etConfirmPassWordContent)) {
                    ToastUtils.s(UpdatePassWordActivity.this, "The passwords are inconsistent twice.");
                } else {
                    currentUser.updatePassword(etConfirmPassWordContent).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ToastUtils.s(UpdatePassWordActivity.this, "reset success");
                        }
                    });
                }
            }
        });
    }

    public static void startUpdatePassWordActivity(Context context) {
        Intent starter = new Intent(context, UpdatePassWordActivity.class);
        context.startActivity(starter);
    }
}


//    This activity ↓ is detail views by Haonan