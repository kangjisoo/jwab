package org.techtown.projectmain;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.R;

import java.util.ArrayList;

public class ProjectHomeListAdapter extends RecyclerView.Adapter<ProjectHomeListAdapter.ViewHolder>
        implements ItemTouchHelperListener {
    ArrayList<ProjectHomeList> items = new ArrayList<ProjectHomeList>();

    Context context;
    public ProjectHomeListAdapter(Context context) {
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView, textView2;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.project_name);
            textView2 = itemView.findViewById(R.id.project_person);
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
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) {

    }

    //오른쪽 버튼 누르면 아이템 삭제
    @Override
    public void onRightClick(final int position, RecyclerView.ViewHolder viewHolder) {
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

}

//projectDeleteDB deleteDB = new projectDeleteDB();
//deleteDB.execute();



//    public class projectDeleteDB extends AsyncTask<Void, Integer, Void> {
//        String data = "";
//        @Override
//        protected Void doInBackground(Void... unused) {
//
//            /* 인풋 파라메터값 생성 */
//            String param = "param1 = "+MainActivity.getsId();
//            Log.e("POST",param);
//            try {
//                /* 서버연결 */
//                URL url = new URL(
//                        "http://rtemd.suwon.ac.kr/guest/login.php");
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                conn.setRequestMethod("POST");
//                conn.setDoInput(true);
//                conn.connect();
//
//                /* 안드로이드 -> 서버 파라메터값 전달 */
//                OutputStream outs = conn.getOutputStream();
//                outs.write(param.getBytes("UTF-8"));
//                outs.flush();
//                outs.close();
//
//                /* 서버 -> 안드로이드 파라메터값 전달 */
//                InputStream is = null;
//                BufferedReader in = null;
//
//                is = conn.getInputStream();
//                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
//                String line = null;
//                StringBuffer buff = new StringBuffer();
//                while ((line = in.readLine()) != null) {
//                    buff.append(line + "\n");
//                }
//                data = buff.toString().trim();
//
//                /* 서버에서 응답 */
//                Log.e("RECV DATA", data);
//
//                if (data.equals("0")) {
//                    Log.e("RESULT", "성공적으로 처리되었습니다!");
//                } else {
//                    Log.e("RESULT", "에러 발생! ERRCODE = " + data);
//                }
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
////        @Override
////        protected void onPostExecute(Void aVoid) {
////            super.onPostExecute(aVoid);
////
////            /* 서버에서 응답 */
////            Log.e("RECV DATA", data);
////
////            // AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
////
////            String projectName = data;
////            String[]splited = projectName.split("@");
////
////            Log.e("projectNameTest= ", Arrays.toString(splited));
////            //ItemTouchHelper 생성
////            helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
////            //RecyclerView에 ItemTouchHelper 붙이기
////            helper.attachToRecyclerView(recyclerView);
////
////            for(int i = 1; i < splited.length; i++){
////                adapter.addItem(new ProjectHomeList(splited[i], "kangjisoo"));
////            }
////            recyclerView.setAdapter(adapter);
////
////        }
////
////    }
//
//
//     //}


