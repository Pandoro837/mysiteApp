package com.javaex.mysite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.javaex.vo.GuestBookVo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadActivity extends AppCompatActivity {

    private TextView txtGuestBookNo;
    private TextView txtGuestBookName;
    private TextView txtGuestBookRegDate;
    private TextView txtGuestBookContent;
    private Button btnList;
    private GuestBookVo requestVo;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        btnList = (Button) findViewById(R.id.btnList);
        txtGuestBookNo = (TextView) findViewById(R.id.txtGuestBookNo);
        txtGuestBookName = (TextView) findViewById(R.id.txtGuestBookName);
        txtGuestBookRegDate = (TextView) findViewById(R.id.txtGuestBookRegDate);
        txtGuestBookContent = (TextView) findViewById(R.id.txtGuestBookContent);

        //이전 액티비티에서 준 값을 꺼내온다
        Intent intent = getIntent();    //넘어온 인텐트를 호출하여 선언
        int no = intent.getExtras().getInt("no");

        Log.d("javaStudy", "onCreate: no " + no);

        //서버에 해당하는 게스트북 요청
        ReadAsyncTask readAsyncTask = new ReadAsyncTask();
        readAsyncTask.execute(no);

        //리스트로 돌아가기 버튼 이벤트
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("javaStudy", "onClick: 리스트로 돌아가기");
                finish();
            }
        });

    }

    //이너 클래스
    public class  ReadAsyncTask extends AsyncTask<Integer, Integer, GuestBookVo> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected GuestBookVo doInBackground(Integer... noArray) {
            Log.d("javaStudy", "doInBackground: no " + noArray[0]);

            gson = new Gson();

            int no = noArray[0];
            requestVo = new GuestBookVo();
            requestVo.setNo(no);

            Log.d("javaStudy", "doInBackground: " + requestVo.getNo()); //확인
            Log.d("javaStudy", "doInBackground: " + requestVo.toString());

            String requestJson = gson.toJson(requestVo);
            Log.d("javaStudy", "doInBackground: requestJson" + requestJson);

            try {
                URL url = new URL("http://192.168.0.199:8088/mysite5/api/guestbook/read"); //url 생성
                HttpURLConnection conn = (HttpURLConnection)url.openConnection(); //url 연결
                conn.setConnectTimeout(10000);  // 10초 동안 기다린 후 응답이 없으면 종료
                conn.setRequestMethod("POST");  // 요청방식 POST
                conn.setRequestProperty("Content-Type", "application/json"); //요청시 데이터 형식 json
                conn.setRequestProperty("Accept", "application/json"); //응답시 데이터 형식 json
                conn.setDoOutput(true);         //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                conn.setDoInput(true);          //InputStream으로 서버로 부터 응답을 받겠다는 옵션.



                //OutputStream 을 통해 json으로 변환된 데이터(파라미터로 사용 될 값)을 넣어 전달한다
                OutputStream os = conn.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                BufferedWriter bw = new BufferedWriter(osw);

                //데이터 형식은 json으로 한다.
                bw.write(requestJson);
                bw.flush();
                bw.close();

                int resCode = conn.getResponseCode(); // 응답코드 200이 정상

                Log.d("javaStudy", "doInBackground: resCode" + resCode);

                if(resCode == 200){ //정상이면

                }
            } catch (IOException e) {
                e.printStackTrace();
            }



            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(GuestBookVo guestBookVo) {
            super.onPostExecute(guestBookVo);
        }
    }

}