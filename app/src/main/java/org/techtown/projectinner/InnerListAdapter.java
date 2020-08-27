package org.techtown.projectinner;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.MainActivity;
import org.techtown.loginactivity.R;
import org.techtown.projectmain.ProjectHomeListAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class InnerListAdapter  extends RecyclerView.Adapter<InnerListAdapter.ViewHolder> {

    ArrayList<InnerList> items = new ArrayList<InnerList>();
    Context context;

    public InnerListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.inner_project_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        InnerList item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(InnerList item) {
        items.add(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView, textView2;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){


                    Log.e("ClickTest","Ok");
                }

            });
            textView = itemView.findViewById(R.id.person_name);
            textView2 = itemView.findViewById(R.id.person_message);

        }

        public void setItem(InnerList item) {
            textView.setText(item.getPersonName());
            textView2.setText(item.getMessage());
        }
    }
    public class FindMyIdDB extends AsyncTask<Void, Integer, Void> {
        String data = "";

        @Override
        protected Void doInBackground(Void... unused) {


            String param = "param1=" + MainActivity.getsId() +"";
            Log.e("FindMyIdDB 확인ㅋㅋㅋ ", param);

            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/getPersonName.php");
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
                Log.e("InnerDB DATA", data);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            InnerListAdapter adapter = new InnerListAdapter(getActivity());
//            RecyclerView recyclerView = getView().findViewById(R.id.inner_recycler);
//
//            /* 서버에서 응답 */
//            Log.e("PersonName", data);
//            String personNameString = data;
//
//            //String으로 받아온 프로젝트 이름을 "@"로 구분
//            String[] splited = personNameString.split("@");
//
//            //personName만을 추출
//            for(int i = 1; i < splited.length; i++){
//
//                adapter.addItem(new InnerList(splited[i],"ㅎㅇ"));
//            }
//            recyclerView.setAdapter(adapter);
//
//        }
    }


}
