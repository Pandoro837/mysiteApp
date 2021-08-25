package com.javaex.mysite;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.javaex.vo.GuestBookVo;

import java.util.List;

public class GuestBookListAdapter extends ArrayAdapter<GuestBookVo> {

    //필드
    private TextView txtNo;
    private TextView txtName;
    private TextView txtDate;
    private TextView txtContent;

    //생성자
    public GuestBookListAdapter(Context context, int textViewResourceId, List<GuestBookVo> items) {
        super(context, textViewResourceId, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //리스트 하나를 그려내는 코드, position = 리스트에서 현재 위치에 해당하는 인덱스 넘버(첫번째 리스트 뷰 = 0번 인덱스)
        //Log.d("javaStudy", "getView: position - " + position);

        //리스트 View에 리스트 아이템 객체가 없을 경우, view == null일때 리스트 아이템 객체 생성
        if(convertView == null) {
            //레이아웃
            LayoutInflater layoutInflater = (LayoutInflater) LayoutInflater.from(getContext());
            //그려넣을 items를 view에 입력
            convertView = layoutInflater.inflate(R.layout.activity_list_item, null);
        }


        //데이터 처리(레이아웃의 item의 정보와 매칭)
        txtNo = convertView.findViewById(R.id.txtNo);
        txtName = convertView.findViewById(R.id.txtName);
        txtDate = convertView.findViewById(R.id.txtDate);
        txtContent = convertView.findViewById(R.id.txtContent);

        //데이터 가져오기(하나의 데이터)  -->  부모쪽에 전체 리스트를 포지션(인덱스)로 호출하여 객체에 담는다
        GuestBookVo guestBookVo = super.getItem(position);

        //가져온 Vo의 값을 List_items의 텍스트에 넣어준다
        txtNo.setText(""+guestBookVo.getNo());          //no값은 int형이므로, 텍스트에 넣기 위해 문자열로 바꿔준다
        txtName.setText(guestBookVo.getName());
        txtDate.setText(guestBookVo.getRegDate());
        txtContent.setText(guestBookVo.getContent());

        return convertView;
    }
}
