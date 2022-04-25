package com.example.jetpack.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.jetpack.MainActivity;
import com.example.jetpack.R;
import com.example.jetpack.Upload;
import com.example.jetpack.databinding.ActivityMainBinding;
import com.example.jetpack.util.ImageActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

/**
 * 上传
 */
public class MenuFragment extends Fragment {
    private Activity myActivity;//上下文

    private EditText product_name_edit;
    private EditText condition_edit;
    private EditText quantity_edit;
    private EditText brand_edit;
    private EditText description_edit;
    private static final int PICK_IMAGE_REQUEST =1;
    private Button mButtonChooseImage;
    private ImageView mImageView;
    private Uri mImageUri;
    private EditText email;

    private TextView mTextViewShowUploads;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity= (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_menu,container,false);
        //post page variables
        myActivity.setContentView(R.layout.fragment_menu);
        product_name_edit = myActivity.findViewById(R.id.product_name_edit);
        condition_edit = myActivity.findViewById(R.id.condition_edit);
        quantity_edit = myActivity.findViewById(R.id.quantity_edit);
        brand_edit = myActivity.findViewById(R.id.brand_edit);
        description_edit = myActivity.findViewById(R.id.description_edit);
        Button button_submit = myActivity.findViewById(R.id.button_submit);
        email = myActivity.findViewById(R.id.email_edit);
        //upload data when submit button clicked
        button_submit.setOnClickListener(v ->
        {
            uploadFile();
            /**UserPost emp = new UserPost(product_name_edit.getText().toString(),
                    condition_edit.getText().toString(),
                    quantity_edit.getText().toString(),
                    brand_edit.getText().toString(),
                    description_edit.getText().toString());

            dao.add(emp).addOnSuccessListener(suc ->
            {
                Toast.makeText(this.myActivity,"Post Submitted", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(er ->
            {
                Toast.makeText(this.myActivity, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
            });
            **/
        });

        Button mButtonChooseImage= myActivity.findViewById(R.id.button_choose_image);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase. getInstance().getReference("uploads");

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        mTextViewShowUploads= myActivity.findViewById(R.id.text_view_show_upload);
        mTextViewShowUploads. setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageActivity();
            }
        });
        //获取控件
        initView();
        return view;
    }
    private void openImageActivity(){
        Intent intent = new Intent (myActivity, ImageActivity.class);
        startActivity(intent);
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR= myActivity.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile(){
        if (mImageUri != null){
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    +"."+getFileExtension(mImageUri));
            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();

                            Toast.makeText(myActivity,"Upload successful",Toast.LENGTH_SHORT).show();
                            Upload upload = new Upload(product_name_edit.getText().toString(),
                                    condition_edit.getText().toString(),
                                    quantity_edit.getText().toString(),
                                    brand_edit.getText().toString(),
                                    description_edit.getText().toString(),
                                    downloadUrl.toString(),
                                    email.getText().toString());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(myActivity,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

        }else{
            Toast.makeText(this.myActivity,"No file selected", Toast.LENGTH_SHORT).show();
        }
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
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
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
        && data != null && data.getData() != null){
            mImageUri = data.getData();
            ImageView mImageView = myActivity.findViewById(R.id.image_view);
            Picasso.get().load(mImageUri).into(mImageView);

        }

    }
}
