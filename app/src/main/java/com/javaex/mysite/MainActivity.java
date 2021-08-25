package com.javaex.mysite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.javaex.vo.GuestBookVo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar toolbar;
    private Button btnSave;
    private EditText edtName;
    private EditText edtPassword;
    private EditText edtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //사용할 변수의 선언
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        edtName = (EditText) findViewById(R.id.edtName);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtContent = (EditText) findViewById(R.id.edtContent);
        btnSave = (Button) findViewById(R.id.btnSave);

        //툴바 선언 및 뒤로가기 버튼 활성화
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("방명록 작성하기");

        //저장 버튼 클릭 시
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("javaStudy", "onClick: btnSave");
                //데이터를 취합하여 VO로 만든다
                String name = edtName.getText().toString();
                String password = edtPassword.getText().toString();
                String content = edtContent.getText().toString();

                /* 확인용 log
                Log.d("javaStudy", "onClick: " +name);
                Log.d("javaStudy", "onClick: " +password);
                Log.d("javaStudy", "onClick: " +content);
                */

                GuestBookVo guestBookVo = new GuestBookVo(name, password, content);
                Log.d("javaStudy", "onClick: " + guestBookVo.toString());


                //서버에 전송
                Log.d("javaStudy", "onClick: 서버에 전송하기");
                WriteAsyncTask writeAsyncTask = new WriteAsyncTask();
                writeAsyncTask.execute(guestBookVo);


                //리스트 액티비티로 전환
//                Intent intent = new Intent(MainActivity.this, ListActivity.class);
//                startActivity(intent);

                //자신의 액티비티를 종료시킨다
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("javaStudy", "onOptionsItemSelected: 클릭");
        Log.d("javaStudy", "onOptionsItemSelected: " + item.getItemId());

        switch (item.getItemId()) {
            case android.R.id.home:

                Log.d("javaStudy", "onOptionsItemSelected: " + android.R.id.home);

                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //이너클래스 - AsyncTask

    public class WriteAsyncTask extends AsyncTask<GuestBookVo, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(GuestBookVo... guestBookVos) {

            Log.d("javaStudy", "doInBackground: " +guestBookVos[0].toString() );

            //Vo - > json
            Gson gson = new Gson();
            String json = gson.toJson(guestBookVos[0]);
            Log.d("javaStudy", "doInBackground: " + json);

            //데이터 전송 (json - > body)
            try{
                //접속 정보
                URL url = new URL("http://192.168.0.199:8088/mysite5/api/guestbook/write2");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                //데이터 요청
                conn.setDoOutput(true);
                conn.setDoInput(true);

                //OutputStream(json - > body)
                OutputStream out = conn.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(out);
                BufferedWriter bw = new BufferedWriter(osw);

                bw.write(json);
                bw.flush();
                bw.close();

                int resCode = conn.getResponseCode(); // 응답코드 200이 정상

                Log.d("javaStudy", "resCode: "+resCode);

                if(resCode == 200) { //정상이면
                    Intent intent = new Intent(MainActivity.this, ListActivity.class);
                    startActivity(intent);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }

    }

}