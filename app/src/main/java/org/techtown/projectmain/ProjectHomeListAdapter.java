package org.techtown.projectmain;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import org.techtown.loginactivity.MainActivity;
import org.techtown.loginactivity.R;
import org.techtown.projectinner.InnerMainRecycler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ProjectHomeListAdapter extends RecyclerView.Adapter<ProjectHomeListAdapter.ViewHolder>
        implements ItemTouchHelperListener {
    private String name;
    private String key;
    ArrayList<ProjectHomeList> items = new ArrayList<ProjectHomeList>();
    private static String projectNameImsi;
    private static String see;
    InnerMainRecycler innerMainRecycler;

    Context context;

    public ProjectHomeListAdapter(Context context) {
        this.context = context;
    }

    public static String getProjectNameImsi(){return projectNameImsi;}
    public static String getSee(){return see;}

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView, textView2;

        public ViewHolder(View itemView) {
            super(itemView);
//
            
            //itemView클릭 시 InnerMainRecycler로 화면 전환
            itemView.setOnClickListener(new View.OnClickListener(){
            @Override
                public void onClick(View v){
                innerMainRecycler = new InnerMainRecycler();
                //클릭한 아이템의 position을 찾아 프로젝트이름과 키값 저장
               projectNameImsi= items.get(ViewHolder.super.getAdapterPosition()).getProjectName();
              see = items.get(ViewHolder.super.getAdapterPosition()).getKey();

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, innerMainRecycler).commit();

                Log.e("ClickTest","Ok");
            }

        });

        textView = itemView.findViewById(R.id.person_name);
            textView2 = itemView.findViewById(R.id.person_message);
        }
        public void setItem(ProjectHomeList item) {
            textView.setText(item.getProjectName());
            textView2.setText(item.getPerson());
        }
    }

    @NonNull
    @Override
    //LayoutInflater를 이용해서 원하는 레이아웃을 띄워줌
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.project_home_project_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    //뷰홀더가 재사용될 때 호출되므로 뷰객체는 기존 것을 그대로 사용하고 데이터만 바꿔줌
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ProjectHomeList item = items.get(position);
        viewHolder.setItem(item);

    }

    @Override
    //전체 아이템 갯수 확인 후 그 값 반환
    public int getItemCount() {
        return items.size();
    }

    public void addItem(ProjectHomeList item) {
        items.add(item);
    }


    @Override
    public boolean onItemMove(int from_position, int to_position) {
        //이동할 객체 저장
        ProjectHomeList projectHomeList = items.get(from_position);

        //이동할 객체 삭제
        items.remove(from_position);

        //이동하고 싶은 position에 추가
        items.add(to_position, projectHomeList);

        //Adapter에 데이터 이동알림
        notifyItemMoved(from_position, to_position);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) { }

    //오른쪽 버튼 누르면 아이템 삭제
    @Override
    public void onRightClick(final int position, final RecyclerView.ViewHolder viewHolder) {
        name = items.get(position).projectName;
        key = items.get(position).getKey();

        Toast.makeText(context," "+name,Toast.LENGTH_LONG).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle("프로젝트 삭제")
                .setMessage("삭제 시 복구할 수 없습니다.")
                .setCancelable(true)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        items.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_LONG).show();

                        deleteProjectDB deleteDB = new deleteProjectDB();
                        deleteDB.execute();
                    }

                });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "취소되었습니다.", Toast.LENGTH_LONG).show();


            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    //삭제버튼 누를 시 DB의 프로젝트 정보 삭제
    public class deleteProjectDB extends AsyncTask<Void, Integer, Void> {
        String data = "";


        @Override
        protected Void doInBackground(Void... unused) {
            String param = "param1=" + MainActivity.getsId() + "&param2=" + name + "&param3=" + key + "";


            try {

                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/deleteProject.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();
                Log.e("ServerTest","test");
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
                Log.e("DeleteDBparamTest", data);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }
}