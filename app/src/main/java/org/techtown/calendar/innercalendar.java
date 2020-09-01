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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class innercalendar extends AppCompatActivity {

    private String fname=null;
    private String str=null;
    private MaterialCalendarView calendarView;
    private Button cha_Btn,del_Btn,save_Btn;
    private TextView diaryTextView,textView2,textView9;
    private EditText contextEditText;
    private RadioGroup radioGroup;

    private RadioButton cal_rb_single, cal_rb_mutiple;
    private ArrayList<calendar_item> calendar_item_ArrayList;
    private boolean radioSingleOrMulti =true;
    private Calendar todaCal,ddayCal;
    private long position=0;
    //-----
    ArrayList<CalendarDay> dates = new ArrayList<>();

    Calendar calendar;
    //현재 날짜, 마감날짜, 시작날짜, 디데이 카운트 저장 변수
    private long currentDay, dday,today=0,count;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_main_activity);

        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        diaryTextView=findViewById(R.id.diaryTextView);
        save_Btn=findViewById(R.id.save_Btn);
        del_Btn=findViewById(R.id.del_Btn);
        cha_Btn=findViewById(R.id.cha_Btn);
        textView2=findViewById(R.id.textView2);
        textView9=findViewById(R.id.textView3);
        contextEditText=findViewById(R.id.contextEditText);

        radioGroup=findViewById(R.id.cal_radio_group);
        cal_rb_single = findViewById(R.id.cal_rb_single);
        cal_rb_mutiple = findViewById(R.id.cal_rb_multiple);
        calendar_item_ArrayList = new ArrayList<>();



        //오늘 날짜 저장
        final Calendar calendarCurrent = Calendar.getInstance();
        currentDay = calendarCurrent.getTimeInMillis()/86400000;



        //빨간 점 찍는 이벤트 실행


        //라디오버튼 클릭 이벤트
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.cal_rb_single){
                    Toast.makeText(innercalendar.this,"single",Toast.LENGTH_LONG).show();
                    radioSingleOrMulti=true;
                }
                else if (i==R.id.cal_rb_multiple){
                    Toast.makeText(innercalendar.this,"multiple",Toast.LENGTH_LONG).show();
                    radioSingleOrMulti=false;

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

                ApiSimulator apiSimulator = new ApiSimulator();
                apiSimulator.execute();


                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                diaryTextView.setText(String.format("%d / %d / %d",date.getYear(),date.getMonth(),date.getDay()));
                contextEditText.setText("");
                checkDay(date.getYear(),date.getMonth(),date.getDay(),"userID");
                Log.e("진짜 짜증나게 하네",date.getYear()+""+date.getMonth()+""+date.getDay()+"");
            }
        });

        //저장 클릭 이벤트
        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDiary(fname);
                Log.e("저장 fname",fname+"");
                str=contextEditText.getText().toString();
                textView2.setText(str);
                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.VISIBLE);

            }
        });
    }

    //라디오 버튼 single선택
    public void radio_bt_check_single(int year, int month, int dayOfMonth) {

        if (radioSingleOrMulti == true) {
            diaryTextView.setText(String.format("%d / %d / %d", year, month+1, dayOfMonth) + "\n" + "-오늘의 할 일 List-");
            diaryTextView.setVisibility(View.VISIBLE);
            save_Btn.setVisibility(View.VISIBLE);
            contextEditText.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.INVISIBLE);
            del_Btn.setVisibility(View.INVISIBLE);
            contextEditText.setText("");
            checkDay(year, month, dayOfMonth, "userID");

        } else {

            diaryTextView.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth) + "\n");
            diaryTextView.setVisibility(View.VISIBLE);

            save_Btn.setVisibility(View.INVISIBLE);
            contextEditText.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.INVISIBLE);
            del_Btn.setVisibility(View.INVISIBLE);

//            Calendar c = Calendar.getInstance();
//            c.set(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_WEEK_IN_MONTH);
//            calendarView.getMaxDate();
//            calendarView.setMinDate(currentDay);
        }
    }

    //선택한 날짜에 자료가 입력될때
    public void  checkDay(int cYear,int cMonth,int cDay,String userID){
        fname=""+userID+cYear+"-"+cMonth+"-"+cDay+".txt";//저장할 파일 이름설정

        CalendarDay day = CalendarDay.from(cYear,cMonth,cDay);
        dates.add(day);

        FileInputStream fis=null;   //FileStream fis 변수

        try{
            fis=openFileInput(fname);

            byte[] fileData=new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            str=new String(fileData);

            //이미 비워져 있을 경우
            if(textView2.getText()==null){
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
                        // Log.e("삭제하는 거",fname+"");

                    }
                });

            }

        }catch (Exception e){

            e.printStackTrace();

        }
    }

    //초기화 메소드
    public void initViews(){

    }

    //파일 삭제 메소드
    @SuppressLint("WrongConstant")
    public void removeDiary(String readDay){
        FileOutputStream fos=null;

        try{
            fos=openFileOutput(readDay,MODE_NO_LOCALIZED_COLLATORS);
            String content="";
            fos.write((content).getBytes());
            fos.close();
            deleteFile(fname);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //저장 메소드
    @SuppressLint("WrongConstant")
    public void saveDiary(String readDay){
        FileOutputStream fos=null;
        try{
            fos=openFileOutput(readDay,MODE_NO_LOCALIZED_COLLATORS);
            String content=contextEditText.getText().toString();
            fos.write((content).getBytes());
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //달력에 빨간 점 표시해주는 클래스
  private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

       @Override
        //List<CalendarDay>
      protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
           try {
             Thread.sleep(2000);

            } catch (InterruptedException e) {
               e.printStackTrace();
           }

       Calendar calendarPre = Calendar.getInstance();
       Calendar calendar = Calendar.getInstance();
       calendarPre.add(Calendar.MONTH, -2);
       ArrayList<CalendarDay> dates = new ArrayList<>();
//          for (int i = 0; i < 30; i++) {
////
//
//
//                 CalendarDay day = CalendarDay.from(calendar.get(Calendar.YEAR),(calendar.get(Calendar.MONTH)+1),i+1);
//              if (textView2.getText()!=null){
//                  Log.e("텍스트가 안비어져있다냐",textView2.getText()+"");
////                  if (dates.get(i)==day){
////                      break;
////                  }
////                  else {
//                    //  dates.add(day);
//
////
//
////                  }
//           }
//            //  Log.e("캘린더 이봐",calendar.get(Calendar.YEAR)+" "+calendar.get(Calendar.MONTH)+" "+calendar.get(Calendar.DAY_OF_MONTH));
//             // calendar.add(Calendar.DAY_OF_MONTH, +5);


            Log.e("캘린더 이봐 자네 신사답게 행동해",dates+"");

            return dates;
     }

        @Override
       protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
           super.onPostExecute(calendarDays);

           if (isFinishing()) {
               return;
          }

           calendarView.addDecorator(new CalendarEventDecorator(Color.RED, calendarDays,innercalendar.this));
            }
    }

}

