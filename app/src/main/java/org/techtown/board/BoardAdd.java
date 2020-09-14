package org.techtown.board;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.techtown.loginactivity.R;
import org.techtown.projectmain.ProjectAdd;

import java.io.File;
import java.util.ArrayList;

public class BoardAdd extends AppCompatActivity {

    private ScrollView board_scrollView;
    private EditText board_text;

    private Boolean isPermission = true;
    private File tempFile;

    Button image_bt;
    ImageView image1, image2, image3, image4, image5;
    ArrayList imageListUri = new ArrayList();
    private static final int PICK_FROM_ALBUM = 1;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_add);
        tedPermission();

        //스크롤뷰와 스크롤뷰 안의 텍스트 객체화
        image1 = (ImageView)findViewById(R.id.board_imageView1);
        image2 = (ImageView)findViewById(R.id.board_imageView2);
        image3 = (ImageView)findViewById(R.id.board_imageView3);
        image4 = (ImageView)findViewById(R.id.board_imageView4);
        image5 = (ImageView)findViewById(R.id.board_imageView5);

        board_scrollView = (ScrollView) findViewById(R.id.board_scrollView);
        board_text = (EditText) findViewById(R.id.board_textView);

        //텍스트에 스크롤 선언
        board_text.setMovementMethod(new ScrollingMovementMethod());
        board_text.setFocusableInTouchMode(true);
        board_text.setFocusable(true);
        board_text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //스크롤뷰가 텍스트뷰의 터치이벤트를 가져가지 못하게 함
                board_scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        image_bt = (Button) findViewById(R.id.board_add_picture);

        image_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });
    }//onCreate() 끝



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            image1.setImageResource(0);
            image2.setImageResource(0);
            image3.setImageResource(0);
            image4.setImageResource(0);

            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e("TAG", tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }
            return;
        }


        if (requestCode == PICK_FROM_ALBUM) {
            Uri photoUri = data.getData();
            ClipData clipData = data.getClipData();
            if(clipData!=null)
            {
                for(int i = 0; i < clipData.getItemCount(); i++)
                {
                    if(i<clipData.getItemCount()){
                        Uri urione =  clipData.getItemAt(i).getUri();
                        switch (i){
                            case 0:
                                image1.setImageURI(urione);
                                break;
                            case 1:
                                image2.setImageURI(urione);
                                break;
                            case 2:
                                image3.setImageURI(urione);
                                break;
                            case 3:
                                image4.setImageURI(urione);
                                break;
                            case 4:
                                image5.setImageURI(urione);
                                break;
                        }
                    }
                }
            }
            else if(photoUri != null)
            {
                image1.setImageURI(photoUri);
            }

            Cursor cursor = null;

//            try {
//                String[] proj = { MediaStore.Images.Media.DATA };
//
//                assert photoUri != null;
//                cursor = getContentResolver().query(photoUri, proj, null, null, null);
//
//                assert cursor != null;
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//
//                cursor.moveToFirst();
//
//                tempFile = new File(cursor.getString(column_index));
//
//            } finally {
//                if (cursor != null) {
//                    cursor.close();
//                }
//            }
//
//            setImage();

        }
        }

    private void setImage() {

            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
            Log.d("TAG", "setImage : " + tempFile.getAbsolutePath());
            image1.setImageBitmap(originalBm);
            tempFile = null;

    }

    //권한설정
    private void tedPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                isPermission = true;
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
                isPermission = false;
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }
}

