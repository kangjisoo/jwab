package org.techtown.board;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.PathUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.techtown.loginactivity.R;
import org.techtown.projectmain.ProjectAdd;
import org.techtown.projectmain.ProjectHomeListAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

public class BoardAdd extends AppCompatActivity {
    Context context;
    private ScrollView board_scrollView;
    private EditText insertText, insertText2;
    private static String  pname;
    private static String  pkey;

    public static String  getPname(){ return pname; }
    public static String getPkey(){
        return pkey;
    }

    private Boolean isPermission = true;
    private File tempFile;

    Button image_bt, upload_bt;
    ImageView image, image1, image2, image3, image4, image5;
    String board_name = null;
    String board_text = null;

    ArrayList imageListUri = new ArrayList();
    private static final int PICK_FROM_FILE = 1;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_add);

        tedPermission();

        image1 = (ImageView) findViewById(R.id.imageView1);
        image2 = (ImageView) findViewById(R.id.imageView2);
        image3 = (ImageView) findViewById(R.id.imageView3);
        image4 = (ImageView) findViewById(R.id.imageView4);
        image5 = (ImageView) findViewById(R.id.imageView5);

        board_scrollView = (ScrollView) findViewById(R.id.board_scrollView);
//        board_text = (EditText) findViewById(R.id.board_textView);
        insertText = (EditText)findViewById(R.id.board_textView);


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

        image_bt = (Button) findViewById(R.id.board_add_picture);

        upload_bt = (Button) findViewById(R.id.board_upload_bt);

        //사진 버튼 눌렀을 때
        image_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_FROM_FILE);
            }
        });


        //업로드 버튼 눌렀을 때
        upload_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertText2 = (EditText)findViewById(R.id.board_name);
                board_name= (String)insertText2.getText().toString();
                board_text = (String)insertText.getText().toString();
            if(board_name.equals("")){
                Toast.makeText(BoardAdd.this,"제목을 입력해주세요.",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(BoardAdd.this,"uploadDB실행.",Toast.LENGTH_LONG).show();
                UploadDB uploadDB = new UploadDB();
                uploadDB.execute();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            image1.setImageResource(0);
            image2.setImageResource(0);
            image3.setImageResource(0);
            image4.setImageResource(0);
            image5.setImageResource(0);

            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

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


        if (requestCode == PICK_FROM_FILE) {
            //단일파일->uri, 여러파일->clipData에 삽입됨
            Uri uri = data.getData();
            ClipData clipData = data.getClipData();

            //사진첨부 했을 때
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    if (i < clipData.getItemCount()) {
                        Uri urione = clipData.getItemAt(i).getUri();
                        Log.e("씨발꺼싸아안ㄹ씨발것", String.valueOf(urione));

                        switch (i) {
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

            Cursor cursor = null;

        }
    }


//    private void setImage() {
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
//        Log.d("TAG", "setImage : " + tempFile.getAbsolutePath());
//        image1.setImageBitmap(originalBm);
//        tempFile = null;
//
//    }

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

    public class UploadDB extends AsyncTask<Void, Integer, Void> {
        String data = "";

        @Override
        protected Void doInBackground(Void... unused) {
            pname = ProjectHomeListAdapter.getProjectNameImsi();
            pkey = ProjectHomeListAdapter.getSee();
            String param = "p_name=" + pname + "&p_key=" + pkey +"&title=" + board_name + "&contents=" + board_text + "&img=" + image1 +  "";
            Log.e("pkey Test..",pkey);
            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/getBoardContents.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;


                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("getBoardDBTestㅋㅋㅋㅋㅋ",data);
            return null;
        }
    }
}