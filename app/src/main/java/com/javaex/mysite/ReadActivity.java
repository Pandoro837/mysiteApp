package com.javaex.mysite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.javaex.vo.GuestBookVo;

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


    }

    //이너 클래스
    public class  ReadAsyncTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... noArray) {
            Log.d("javaStudy", "doInBackground: no " + noArray[0]);

            int no = noArray[0];
            requestVo = new GuestBookVo();
            requestVo.setNo(no);

            String requestJson = gson.toJson(requestVo);
            Log.d("javaStudy", "doInBackground: requestJson" + requestJson);


            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

}