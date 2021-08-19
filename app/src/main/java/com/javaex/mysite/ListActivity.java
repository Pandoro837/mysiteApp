package com.javaex.mysite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.javaex.vo.GuestBookVo;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView lstGuestBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        lstGuestBook = (ListView) findViewById(R.id.lstGuestBook);

        //데이터를 서버에서 가져온다
        List<GuestBookVo> guestBookVoList = getList();
        Log.d("javaStudy", "list: " + guestBookVoList.toString());

        //표현하기 위해 리스트에 담을 정보와 리스트 뷰를 어댑터에 전송한다,                                    정보를 담을 뷰의 아이디, 전달할 정보
        GuestBookListAdapter guestBookListAdapter = new GuestBookListAdapter(getApplicationContext(), R.id.lstGuestBook, guestBookVoList);

        //리스트 뷰에 어댑터를 세팅한다
        lstGuestBook.setAdapter(guestBookListAdapter);

        lstGuestBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //현재 클릭한 뷰의 리스트의 인덱스 값
                Log.d("javaStudy", "onItemClick: index : " + i);

                //화면에 있는 값을 읽어온다
                TextView txtContent = (TextView) view.findViewById(R.id.txtContent);
                Log.d("javaStudy", "onItemClick: content : " + txtContent.getText().toString());

                //화면에 출력되지 않는 데이터를 가져올 때 --> 리스트의 값을 사용할 때
                GuestBookVo guestBookVo = (GuestBookVo) adapterView.getItemAtPosition(i);
                Log.d("javaStudy", "onItemClick: Vo : " + guestBookVo.toString());
                Log.d("javaStudy", "onItemClick: Vo.date : " + guestBookVo.getRegDate());

                //클릭한 아이템의 pk값을 읽어온다
                int no = guestBookVo.getNo();

                Log.d("javaStudy", "onItemClick: Vo.no : " + no);
            }
        });

    }

    //리스트 표현용 더미 데이터
    public List<GuestBookVo> getList(){

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
    }

}