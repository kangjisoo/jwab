package org.techtown.calendar;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialCalendar;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


import org.techtown.loginactivity.R;
import org.techtown.projectinner.InnerMainRecycler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class innercalendar extends AppCompatActivity {

    private String fname = null;
    private String str = null;
    private MaterialCalendarView calendarView;
    private Button cha_Btn, del_Btn, save_Btn, terminit_bt, termstore_bt;
    private TextView diaryTextView, textView2, textView9;
    private EditText contextEditText;
    private RadioGroup radioGroup;

    private RadioButton cal_rb_single, cal_rb_mutiple;
    private ArrayList<calendar_item> calendar_item_ArrayList;
    private boolean radioSingleOrMulti = true;
    private Calendar todaCal, ddayCal, calendarCurrent;
    private int sYear, sMonth, sDay, eYear, eMonth, eDay;
    //-----
    ArrayList<CalendarDay> termDates;


    Calendar calendar;
    //현재 날짜, 마감날짜, 시작날짜, 디데이 카운트 저장 변수
    private long currentDay, dday, today = 0, count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_main_activity);

        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        diaryTextView = findViewById(R.id.diaryTextView);
        save_Btn = findViewById(R.id.save_Btn);
        del_Btn = findViewById(R.id.del_Btn);
        cha_Btn = findViewById(R.id.cha_Btn);
        textView2 = findViewById(R.id.textView2);
        textView9 = findViewById(R.id.textView3);
        contextEditText = findViewById(R.id.contextEditText);
        terminit_bt = findViewById(R.id.terminit_bt);
        termstore_bt = findViewById(R.id.termstore_bt);

        radioGroup = findViewById(R.id.cal_radio_group);
        cal_rb_single = findViewById(R.id.cal_rb_single);
        cal_rb_mutiple = findViewById(R.id.cal_rb_multiple);
        calendar_item_ArrayList = new ArrayList<>();

        //점찍는 클래스 실행
        ApiSimulator apiSimulator = new ApiSimulator();
        apiSimulator.execute();

        //오늘 날짜 저장
        calendarCurrent = Calendar.getInstance();
        calendarCurrent.getTime();


        //라디오버튼 클릭 이벤트
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.cal_rb_single) {
                    Toast.makeText(innercalendar.this, "single", Toast.LENGTH_LONG).show();
                    radioSingleOrMulti = true;
                } else if (i == R.id.cal_rb_multiple) {
                    Toast.makeText(innercalendar.this, "multiple", Toast.LENGTH_LONG).show();
                    radioSingleOrMulti = false;

                }

            }
        });

        //로그인 및 회원가입 엑티비티에서 이름을 받아옴
//         Intent intent=getIntent();
//        String namey=intent.getStringExtra("userName");
//        final String userID=intent.getStringExtra("userID");
//        textView9.setText(namey+"님의 달력 일기장");


        //캘린더 클릭이벤트
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                radio_bt_check_single(date.getYear(), date.getMonth(), date.getDay());
                Log.e("선택된 날짜", date.getYear() + "" + date.getMonth() + "" + date.getDay() + "");


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


                currentDay = calendarCurrent.getTimeInMillis() / 86400000;
                Log.e("현재날짜", simpleDateFormat.format(calendarCurrent.getTime()) + "");


            }
        });

        //저장 클릭 이벤트
        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDiary(fname);
                Log.e("저장 fname", fname + "");
                str = contextEditText.getText().toString();
                textView2.setText(str);
                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.VISIBLE);
                terminit_bt.setVisibility(View.INVISIBLE);
                termstore_bt.setVisibility(View.INVISIBLE);

                //저장누르면 빨간점 표시 해주는 클래스 실행
                ApiSimulator apiSimulator = new ApiSimulator();
                apiSimulator.execute();

                TermDisplay termDisplay = new TermDisplay();
                termDisplay.execute();

            }
        });
    }

    //라디오 버튼 single선택
    public void radio_bt_check_single(int year, int month, int dayOfMonth) {

        if (radioSingleOrMulti == true) {
            diaryTextView.setText(String.format("%d / %d / %d", year, month, dayOfMonth) + "\n" + "-오늘의 할 일 List-");
            diaryTextView.setVisibility(View.VISIBLE);
            save_Btn.setVisibility(View.VISIBLE);
            contextEditText.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.INVISIBLE);
            del_Btn.setVisibility(View.INVISIBLE);
            terminit_bt.setVisibility(View.INVISIBLE);
            termstore_bt.setVisibility(View.INVISIBLE);
            contextEditText.setText("");
            checkDay(year, month, dayOfMonth, "userID");

        } else {

            diaryTextView.setText(String.format("%d / %d / %d", year, month, dayOfMonth) + "\n");
            diaryTextView.setVisibility(View.VISIBLE);
            save_Btn.setVisibility(View.INVISIBLE);
            contextEditText.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.INVISIBLE);
            del_Btn.setVisibility(View.INVISIBLE);
            terminit_bt.setVisibility(View.INVISIBLE);
            termstore_bt.setVisibility(View.INVISIBLE);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            if (today == 0) {
                todaCal = Calendar.getInstance();
                todaCal.set(year, month - 1, dayOfMonth);
                Log.e("시작날짜", simpleDateFormat.format(todaCal.getTime()) + "");

                today = todaCal.getTimeInMillis() / 86400000; //->(24 * 60 * 60 * 1000) 24시간 60분 60초 * (ms초->초 변환 1000)

                sYear = year;
                sMonth = month;
                sDay = dayOfMonth;

                //  Log.e("현재날짜", simpleDateFormat.format(currentDay)+"");
            } else {
                ddayCal = Calendar.getInstance();
                ddayCal.set(year, month - 1, dayOfMonth);
                dday = ddayCal.getTimeInMillis() / 86400000;
                Log.e("프로젝트 기간 확인", simpleDateFormat.format(ddayCal.getTime()) + "");

                if (dday <= currentDay) {
                    Toast.makeText(this, "이미 만료된 프로젝트기간 입니다.", Toast.LENGTH_LONG).show();
                    Log.e("만료 프로젝트 기간 확인", simpleDateFormat.format(ddayCal.getTime()) + "");
                } else {
                    count = dday - currentDay;
                    Log.e("남은 D-day기간은?", count + "");

                    long projectCount = dday - today;

                    ProjectTermCheck(projectCount, sYear, sMonth, sDay);

                    eYear= year;
                    eMonth= month;
                    eDay=dayOfMonth;

                    TermDisplay termDisplay = new TermDisplay();
                    termDisplay.execute();


                    termstore_bt.setVisibility(View.VISIBLE);

                    //기간 저장 버튼 클릭이벤트
                    termstore_bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Toast.makeText(innercalendar.this, "기간 저장 완료!", Toast.LENGTH_LONG).show();
                            TermDB termDB = new TermDB();
                            termDB.execute();

                            TermFullOrEmpty();
                        }
                    });
                }
            }
        }

    }

    //프로젝트 기간이 정해 졌을때 기간 초기화를 시킬 수 있는 버튼 생성
    public void TermFullOrEmpty() {
        if (today != 0 && dday != 0) {
            diaryTextView.setVisibility(View.VISIBLE);
            save_Btn.setVisibility(View.INVISIBLE);
            contextEditText.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.INVISIBLE);
            del_Btn.setVisibility(View.INVISIBLE);
            terminit_bt.setVisibility(View.VISIBLE);
            termstore_bt.setVisibility(View.INVISIBLE);

            //기간 초기화 버튼 눌렀을 때 이벤트
            terminit_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ArrayList<CalendarDay> copyTermDates = new ArrayList<>();

                    today = 0;
                    dday = 0;

                    sYear=0;
                    sMonth=0;
                    sDay=0;
                    eYear=0;
                    eMonth=0;
                    eDay =0;

                    termDates = copyTermDates;
                    Log.e("뭐가 잘못된거야 -- ", termDates + "");
                    ApiSimulator apiSimulator = new ApiSimulator();
                    apiSimulator.execute();

                }
            });

        }
    }

    //프로젝트의 기간설정을 하며 달력에 표시 해주는 메소드
    public void ProjectTermCheck(long projectCount, int sYear, int sMonth, int sDay) {
        termDates = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendarStartCheck = Calendar.getInstance();

        String[] termArray;

        for (int i = 0; i <= projectCount; i++) {

            calendarStartCheck.set(sYear, sMonth - 1, sDay + i);

            //2020-09-07 -> 2020, 09, 07을 각각 배열에 저장(-을 기준으로 분활)
            termArray = simpleDateFormat.format(calendarStartCheck.getTime()).split("-");
            Log.e("진짜 값이 들어가야하는 변수", termArray[0] + termArray[1] + termArray[2] + "");

            //Integer.parseInt(String s) -> string형을 int형으로 변환
            CalendarDay days = CalendarDay.from(Integer.parseInt(termArray[0]), Integer.parseInt(termArray[1]), Integer.parseInt(termArray[2]));

            //CalendarDay days의 기간 날짜 저장
            termDates.add(days);

        }

        ApiSimulator apiSimulator = new ApiSimulator();
        apiSimulator.execute();
        Log.e("기간 배열 봐보자 여기여기~~", termDates + "");

    }

    //선택한 날짜에 자료가 입력될때
    public void checkDay(final int cYear, final int cMonth, final int cDay, String userID) {
        fname = "" + userID + cYear + "-" + cMonth + "-" + cDay + ".txt";//저장할 파일 이름설정

        FileInputStream fis = null;   //FileStream fis 변수

        try {
            fis = openFileInput(fname);

            byte[] fileData = new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            str = new String(fileData);

            //이미 비워져 있을 경우
            if (textView2.getText() == null) {
                textView2.setVisibility(View.INVISIBLE);
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
            }

            //파일이 존재할때
            else {
                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView2.setText(str);

                //수정,삭제 버튼만 활성화
                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);

                //수정버튼 클릭 이벤트
                cha_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        contextEditText.setVisibility(View.VISIBLE);
                        textView2.setVisibility(View.INVISIBLE);
                        contextEditText.setText(str);

                        //저장버튼만 활성화
                        save_Btn.setVisibility(View.VISIBLE);
                        cha_Btn.setVisibility(View.INVISIBLE);
                        del_Btn.setVisibility(View.INVISIBLE);
                        textView2.setText(contextEditText.getText());
                    }

                });

                //삭제버튼 클릭 이벤트
                del_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        textView2.setVisibility(View.INVISIBLE);
                        contextEditText.setText("");
                        contextEditText.setVisibility(View.VISIBLE);

                        //저장버튼만 활성화, 파일 삭제
                        save_Btn.setVisibility(View.VISIBLE);
                        cha_Btn.setVisibility(View.INVISIBLE);
                        del_Btn.setVisibility(View.INVISIBLE);
                        removeDiary(fname);

                        //삭제누르면 빨간점 삭제
                        ApiSimulator apiSimulator = new ApiSimulator();
                        apiSimulator.execute();

                        TermDisplay termDisplay = new TermDisplay();
                        termDisplay.execute();
                        // Log.e("삭제하는 거",fname+"");
                    }
                });
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    //초기화 메소드
    public void initViews() {

    }

    //파일 삭제 메소드
    @SuppressLint("WrongConstant")
    public void removeDiary(String readDay) {
        FileOutputStream fos = null;

        try {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
            String content = "";
            fos.write((content).getBytes());
            fos.close();
            deleteFile(fname);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //저장 메소드
    @SuppressLint("WrongConstant")
    public void saveDiary(String readDay) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
            String content = contextEditText.getText().toString();
            fos.write((content).getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //달력에 빨간 점 표시해주는 클래스
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {

        /*   쓰레드 잠시후에 시작하게 해주는
           try {
             Thread.sleep(2000);

            } catch (InterruptedException e) {
               e.printStackTrace();
           }
        */

            //오늘 날짜
            Calendar calendar = Calendar.getInstance();

            //빨간점 표시할 날짜 저장배열
            ArrayList<CalendarDay> dates = new ArrayList<>();

            //한달값만 출력
            for (int i = 1; i <= 31; i++) {

                FileInputStream ffos = null;

                try {
                    //파일을 읽기
                    ffos = openFileInput("userID" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + i + ".txt");
                    int story = ffos.read();

                    //존재하는 파일에 내용이 있을 때 배열에 추가
                    if (story >= -1) {
                        CalendarDay day = CalendarDay.from(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1), i);
                        dates.add(day);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            Log.e("빨간점 표시할 날짜 배열", dates + "");
            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }
            calendarView.removeDecorators();
            calendarView.addDecorator(new CalendarEventDecorator(Color.RED, calendarDays, innercalendar.this));
        }
    }


    private class TermDisplay extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {

            return termDates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            calendarView.addDecorator(new TermEventDecorator(Color.DKGRAY, calendarDays, innercalendar.this));
        }
    }


    //기간 날짜를 DB에 저장하는 클래스
    public class TermDB extends AsyncTask<Void, Integer, Void> {
        String data = "";

        String pname = InnerMainRecycler.getPname();
        String pkey = InnerMainRecycler.getPkey();


        @Override
        protected Void doInBackground(Void... unused) {

            //param값이 하나만 넘어가는 것 같아 param하나의 조원과 프로젝트 이름, 총조원수, 프로젝트비밀번호를 한꺼번에 넘겨줌

            String param = "&u_term=" + sYear+"-"+sMonth+"-"+sDay+"~"+eYear+"-"+eMonth+"-"+eDay+"&u_projectTableName="+pname+"_"+pkey+"";
            Log.e("POST", param);

            try {

                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/createTerm.php");
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

                /* 서버에서 응답 */
                Log.e("RECV DATA", data);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
