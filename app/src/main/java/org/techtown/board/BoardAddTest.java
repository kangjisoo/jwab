package org.techtown.board;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import org.techtown.loginactivity.MainActivity;
import org.techtown.loginactivity.R;
import org.techtown.projectmain.ProjectAdd;
import org.techtown.projectmain.ProjectHome;
import org.techtown.projectmain.ProjectHomeListAdapter;


public class BoardAddTest extends AppCompatActivity {
    EditText etName, etMsg;
    ImageView iv1, iv2, iv3, iv4, iv5;
    private ScrollView board_scrollView;
    private EditText insertText, insertText2;
    private static String pname, pkey;
    private int count = 0;
    Button image_bt, upload_bt;

    //업로드할 이미지의 절대경로(실제 경로)
    String imgPath1,imgPath2,imgPath3,imgPath4,imgPath5;
    Uri urione;
    ClipData clipData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_add);
        etName = (EditText) findViewById(R.id.board_name);
        etMsg = (EditText) findViewById(R.id.board_textView);

        image_bt = (Button) findViewById(R.id.board_add_picture);
        upload_bt = (Button) findViewById(R.id.board_upload_bt);

        iv1 = findViewById(R.id.imageView1);
        iv2 = findViewById(R.id.imageView2);
        iv3 = findViewById(R.id.imageView3);
        iv4 = findViewById(R.id.imageView4);
        iv5 = findViewById(R.id.imageView5);


        //업로드 하려면 외부저장소 권한 필요
        //동적 퍼미션 코드 필요..


        //동적퍼미션 작업
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionResult = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 10);
            }
        } else {
            //cv.setVisibility(View.VISIBLE);
        }
        board_scrollView = (ScrollView) findViewById(R.id.board_scrollView);
        insertText = (EditText) findViewById(R.id.board_textView);


        //텍스트에 스크롤 선언
        insertText.setMovementMethod(new ScrollingMovementMethod());
        insertText.setFocusableInTouchMode(true);
        insertText.setFocusable(true);
        insertText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //스크롤뷰가 텍스트뷰의 터치이벤트를 가져가지 못하게 함
                board_scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        //업로드 버튼 눌렀을 시 수행
        //게시물 제목과 내용 비어있는지 확인
        upload_bt.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(final View view) {
                String name = (String) etName.getText().toString();
                String contents = (String) etMsg.getText().toString();
                if (name.length()==0){
                    Toast.makeText(BoardAddTest.this,"제목을 입력해주세요",Toast.LENGTH_LONG).show();
                    // dialog.dismiss();
                }else if(contents.length()==0){
                    Toast.makeText(BoardAddTest.this,"내용을 입력해주세요",Toast.LENGTH_LONG).show();
                }else{
                    clickUpload();
                }


            }

        });
    }//onCreate() ..


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) //사용자가 허가 했다면
                {
                    Toast.makeText(this, "외부 메모리 읽기/쓰기 사용 가능", Toast.LENGTH_SHORT).show();

                } else {//거부했다면
                    Toast.makeText(this, "외부 메모리 읽기/쓰기 제한", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    public void clickBtn(View view) {

        //갤러리 or 사진 앱 실행하여 사진을 선택하도록..
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
//        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            iv1.setImageResource(0);
            iv2.setImageResource(0);
            iv3.setImageResource(0);
            iv4.setImageResource(0);
            iv5.setImageResource(0);

            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            return;
        }

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    //선택한 사진의 경로(Uri)객체 얻어오기
                    Uri uri = data.getData();
                    clipData = data.getClipData();

                    //사진첨부 했을 때
                    if(clipData==null){
                        iv1.setImageURI(uri);
                        imgPath1= getRealPathFromUri(uri);
                        break;
                    }
                    else if (clipData != null) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            if (i < clipData.getItemCount()) {
                                urione = clipData.getItemAt(i).getUri();

                                switch (i) {
                                    case 0:
                                        iv1.setImageURI(urione);
                                        imgPath1= getRealPathFromUri(urione);
                                        count+=1;
                                        break;
                                    case 1:
                                        iv2.setImageURI(urione);
                                        imgPath2= getRealPathFromUri(urione);
                                        count+=1;
                                        break;
                                    case 2:
                                        iv3.setImageURI(urione);
                                        imgPath3= getRealPathFromUri(urione);
                                        count+=1;
                                        break;
                                    case 3:
                                        iv4.setImageURI(urione);
                                        imgPath4= getRealPathFromUri(urione);
                                        count+=1;
                                        break;
                                    case 4:
                                        iv5.setImageURI(urione);
                                        imgPath5= getRealPathFromUri(urione);
                                        count+=1;
                                        break;
                                }

                            }
                        }
                    }
                    else{
                        Toast.makeText(this, "이미지를 선택하지 않았습니다.",Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
        }

    }//onActivityResult() END

        //Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
        private String getRealPathFromUri (Uri uri){
            String[] proj = {MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
            Cursor cursor = loader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(column_index);
            cursor.close();
            return result;
        }


        @SuppressLint("LongLogTag")
        public void clickUpload(){

            //서버로 보낼 데이터
            String name = etName.getText().toString();
            String contents = etMsg.getText().toString();
            String id = MainActivity.getsId();
            pname = ProjectHomeListAdapter.getProjectNameImsi();
            pkey = ProjectHomeListAdapter.getSee();


            //안드로이드에서 보낼 데이터를 받을 php 서버 주소
            String serverUrl = "http://jwab.dothome.co.kr/Android/boardContents.php";

            //Volley plus Library를 이용해서 파일전송
            //파일 전송 요청 객체 생성[결과를 String으로 받음]
            SimpleMultiPartRequest smpr = new SimpleMultiPartRequest(Request.Method.POST, serverUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            new AlertDialog.Builder(BoardAddTest.this).setMessage("응답:" + response).create().show();

                Intent intent = new Intent(BoardAddTest.this, BoardMainRecycler.class);
                startActivity(intent);
                            Toast.makeText(BoardAddTest.this, "업로드 되었습니다.", Toast.LENGTH_SHORT).show();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(BoardAddTest.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });


            //param값 php로 전송
            smpr.addStringParam("id",id);
            smpr.addStringParam("pname", pname);
            smpr.addStringParam("pkey", pkey);
            smpr.addStringParam("name", name);
            smpr.addStringParam("contents", contents);
            smpr.addStringParam("count", String.valueOf(count));
            smpr.addStringParam("imgTest",imgPath1);
            //이미지 파일 추가
            if(imgPath1 == null){
                smpr.addStringParam("img1", "null");
            }else{
                smpr.addFile("img1", imgPath1);
            }

            if(imgPath2 == null){
                smpr.addStringParam("img2", "null");
            }else{
                smpr.addFile("img2", imgPath2);
            }
            if(imgPath3 == null){
                smpr.addStringParam("img3", "null");
            }else{
                smpr.addFile("img3", imgPath3);
            }
            if(imgPath4 == null){
                smpr.addStringParam("img4", "null");
            }else{
                smpr.addFile("img4", imgPath4);
            }
            if(imgPath5 == null){
                smpr.addStringParam("img5", "null");
            }else{
                smpr.addFile("img5", imgPath5);
            }

            //요청객체를 서버로 보낼 우체통 같은 객체 생성
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(smpr);

            clickLoad();
        }

        public void clickLoad() {
            //안드로이드에서 보낼 데이터를 받을 php 서버 주소
            String serverUrl = "http://jwab.dothome.co.kr/Android/boardContents.php";



            Intent intent = new Intent(BoardAddTest.this, BoardMainRecycler.class);
            startActivity(intent);
        }
    }
