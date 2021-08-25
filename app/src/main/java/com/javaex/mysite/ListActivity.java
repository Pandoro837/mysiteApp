package com.javaex.mysite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.javaex.vo.GuestBookVo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView lstGuestBook;
    private Button btnWriteForm;

    //액티비티가 시작될 때
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //ListView를 객체화한다
        lstGuestBook = (ListView) findViewById(R.id.lstGuestBook);
        btnWriteForm = (Button) findViewById(R.id.btnWriteForm);

        ////////////////////////////////////////////////////////////////////////////////////////////

        //writeForm 버튼의 클릭 이벤트
        btnWriteForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("javaStudy", "onClick: btnWrite");

                //인텐트를 이용, 화면 이동
                Intent intent = new Intent(ListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    //액티비티가 다시 보일 때
    @Override
    protected void onResume() {
        super.onResume();

        //데이터 요청 및 화면에 그리기
        ListAsyncTask listAsyncTask = new ListAsyncTask();
        listAsyncTask.execute();

        //리스트 뷰의 온 클릭 이벤트
        lstGuestBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //현재 클릭한 뷰의 리스트의 인덱스 값
                Log.d("javaStudy", "onItemClick: index : " + i);

                //화면에 있는 값을 읽어온다
                TextView txtContent = (TextView) view.findViewById(R.id.txtContent);
                //Log.d("javaStudy", "onItemClick: content : " + txtContent.getText().toString());

                //화면에 출력되지 않는 데이터를 가져올 때 --> 리스트의 값을 사용할 때
                GuestBookVo guestBookVo = (GuestBookVo) adapterView.getItemAtPosition(i);
                //Log.d("javaStudy", "onItemClick: Vo : " + guestBookVo.toString());
                //Log.d("javaStudy", "onItemClick: Vo.date : " + guestBookVo.getRegDate());

                //클릭한 아이템의 pk값을 읽어온다
                int no = guestBookVo.getNo();
                String name = guestBookVo.getName();
                String regDate = guestBookVo.getRegDate();
                String content = guestBookVo.getContent();

                ////////////////////////////////////////////////////////////////////////////////////
                //글 읽기 액티비티로 이동
                ////////////////////////////////////////////////////////////////////////////////////
                Intent intent = new Intent(ListActivity.this, ReadActivity.class);
                intent.putExtra("no",no);

                startActivity(intent);

                Log.d("javaStudy", "onItemClick: Vo.no : " + no);
            }
        });

    }

    //이너클래스
    public class ListAsyncTask extends AsyncTask<Void, Integer, List<GuestBookVo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<GuestBookVo> doInBackground(Void... voids) {

            List<GuestBookVo> guestBookVoList = null;

            try {
                //서버에 연결
                URL url = new URL("http://192.168.0.199:8088/mysite5/api/guestbook/list"); //url 생성
                HttpURLConnection conn = (HttpURLConnection)url.openConnection(); //url 연결
                conn.setConnectTimeout(10000);  // 10초 동안 기다린 후 응답이 없으면 종료
                conn.setRequestMethod("POST");  // 요청방식 POST
                conn.setRequestProperty("Content-Type", "application/json"); //요청시 데이터 형식 json
                conn.setRequestProperty("Accept", "application/json"); //응답시 데이터 형식 json
                //데이터 요청
                conn.setDoOutput(true);         //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                conn.setDoInput(true);          //InputStream으로 서버로 부터 응답을 받겠다는 옵션.
                int resCode = conn.getResponseCode(); // 응답코드 200이 정상
                if(resCode == 200){ //정상이면

                    //Stream 을 통해 통신한다
                    //Log.d("javaStudy", ""+resCode);

                    InputStream input = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(input, "UTF-8");
                    BufferedReader br = new BufferedReader(isr);

                    String jsonData = "";

                    while (true) {
                        String line = br.readLine();
                        if (line == null) {
                            break;
                        } else {
                            jsonData+= jsonData + line;
                        }
                    }
                    //데이터 형식은 json으로 한다.
                    //Log.d("javaStudy", "doInBackground: " + jsonData);

                    //응답을 받는다(데이터) json -> java객체로 변환
                    Gson gson = new Gson();
                    guestBookVoList = gson.fromJson(jsonData, new TypeToken<List<GuestBookVo>>(){}.getType());

                    //Log.d("javaStudy", "doInBackground: size" + guestBookVoList.size());

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return guestBookVoList;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<GuestBookVo> guestBookVoList) {

            //표현하기 위해 리스트에 담을 정보와 리스트 뷰를 어댑터에 전송한다,                                    정보를 담을 뷰의 아이디, 전달할 정보
            GuestBookListAdapter guestBookListAdapter = new GuestBookListAdapter(getApplicationContext(), R.id.lstGuestBook, guestBookVoList);

            //리스트 뷰에 어댑터를 세팅한다
            lstGuestBook.setAdapter(guestBookListAdapter);

//            Log.d("javaStudy", "onPostExecute: " + guestBookVoList.size());
//            Log.d("javaStudy", "onPostExecute: " + guestBookVoList.get(0).getName());

            super.onPostExecute(guestBookVoList);
        }
    }

    //리스트 표현용 더미 데이터
    /*public List<GuestBookVo> getList(){

        List<GuestBookVo> guestBookVoList = new ArrayList<GuestBookVo>();

        for(int i = 0; i < 50; i++) {
            GuestBookVo guestBookVo = new GuestBookVo();
            guestBookVo.setNo(i);
            guestBookVo.setName("정우성");
            guestBookVo.setContent("안녕하세요");
            guestBookVo.setRegDate("2021/08/19");

            guestBookVoList.add(guestBookVo);
        }


        return guestBookVoList;
    }*/

}