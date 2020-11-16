package org.techtown.projectmain;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.misc.AsyncTask;

import org.techtown.loginactivity.MainActivity;
import org.techtown.loginactivity.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class ProjectHomeFragment2 extends Fragment {

    private TextView profile_name, profile_id, profile_pw, profile_pw_ch;
    private Button profile_bt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup profileDP = (ViewGroup)inflater.inflate(R.layout.update_profile, container, false);

        profile_name = profileDP.findViewById(R.id.profile_name);
        profile_id = profileDP.findViewById(R.id.profile_id);
        profile_pw = profileDP.findViewById(R.id.profile_pw);
        profile_pw_ch =profileDP.findViewById(R.id.profile_pw_ch);
        profile_bt = profileDP.findViewById(R.id.profile_bt);

        GetPrivateProfile getPrivateProfile = new GetPrivateProfile();
        getPrivateProfile.execute();

        profile_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String myNameCh = profile_name.getText().toString();
                String password1 = profile_pw.getText().toString();
                String password2 = profile_pw_ch.getText().toString();

                if (spaceCheck(myNameCh) || spaceCheck(password1)==true || spaceCheck(password2)){
                    Toast.makeText(getActivity(), "이름과 비밀번호에 공백이 존재하면 안됩니다. ",Toast.LENGTH_LONG).show();
                }
                else if (profile_pw.getText().toString().equals(profile_pw_ch.getText().toString())){
                    SetProfile setProfile = new SetProfile();
                    setProfile.execute();

                    Toast.makeText(getActivity(), "정보 수정 완료. ",Toast.LENGTH_LONG).show();

                    //현재 내 프래그먼트 닫기
                    removeFragment(ProjectHomeFragment2.this);

                }

                else{
                    Toast.makeText(getActivity(), "비밀번호가 일치하지 않습니다. ",Toast.LENGTH_LONG).show();
                }
            }
        });


        return profileDP;

    }

    //프래그먼트 닫는 메소드
    private void removeFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
            final FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.remove(fragment);
            mFragmentTransaction.commit();
            fragment.onDestroy();
            fragment.onDetach();
            fragment = null;
        }
    }


    //공백이 있는지 없는지 검출해주는 메소드(공백이 있으면 true 없으면 false)
    public boolean spaceCheck(String spaceCheck)
    {
        for(int i = 0 ; i < spaceCheck.length() ; i++)
        {
            if(spaceCheck.charAt(i) == ' ')
                return true;
        }
        return false;
    }

    //자신의 정보 가져오는 스레드
    public class GetPrivateProfile extends AsyncTask<Void, Integer, Void> {
        String data = "";
        String pId = MainActivity.getsId();

        @Override
        protected Void doInBackground(Void... unused) {

            String param = "u_id=" + pId + "";

            //Check param
            Log.e("ProjectHomeFragment2 : ", param);

            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/getProfile.php");
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
                Log.e("profile data: ", data);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String profileData = "";
            profileData = data;

            int sequence1 = profileData.indexOf("/");
            String myName = profileData.substring(0,sequence1);

            profile_name.setText(myName);

            int sequence2= profileData.indexOf("!");
            String myId = profileData.substring(sequence1+1,sequence2);

            profile_id.setText(myId);

            int sequence3 = profileData.indexOf("?");
            String myPw1 = profileData.substring(sequence2+1,sequence3);

            profile_pw.setText(myPw1);

            String myPw2 = profileData.substring(sequence3+1);

            profile_pw_ch.setText(myPw2);

        }
    }

    //정보 수정 업데이트 스레드
    public class SetProfile extends AsyncTask<Void, Integer, Void>{
        String data ="";
        String pId = MainActivity.getsId();

        @Override
        protected Void doInBackground(Void... unused) {

            String gName = profile_name.getText().toString();
            String gPw1 = profile_pw.getText().toString();
            String gPw2 = profile_pw_ch.getText().toString();

            String param = "u_id="+pId+"&u_name=" + gName +"&u_pw1="+ gPw1 +"&u_pw2="+ gPw2+"";

            //Check param
            Log.e("setProfile : ", param);

            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://rtemd.suwon.ac.kr/guest/setProfile.php");
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
                Log.e("profile data: ", data);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
