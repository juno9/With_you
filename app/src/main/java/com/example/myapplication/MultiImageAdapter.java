package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class MultiImageAdapter extends RecyclerView.Adapter<MultiImageAdapter.ViewHolder>{
    private ArrayList<Uri> mData = null ;//Uri를 넣은 어레이 리스트를 만듦
    private Context mContext = null ;//컨텍스트를 생성하고


    // 생성자에서 데이터 리스트 객체, Context를 전달받음.
    MultiImageAdapter(ArrayList<Uri> list, Context context) {
        mData = list ;
        mContext = context;
    }//멀티이미지어댑터 생성자. 만들때 리스트와 컨텍스트를 받는다.

    // 아이템 뷰를 저장하는 뷰홀더 클래스.

    @Override
    public int getItemCount() {
        return mData.size() ;
    }


    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    // LayoutInflater - XML에 정의된 Resource(자원) 들을 View의 형태로 반환.
    @Override
    public MultiImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;    // context에서 LayoutInflater 객체를 얻는다.
        View view = inflater.inflate(R.layout.multi_image_item, parent, false) ;	// 리사이클러뷰에 들어갈 아이템뷰의 레이아웃을 inflate.
        MultiImageAdapter.ViewHolder vh = new MultiImageAdapter.ViewHolder(view) ;//뷰홀더 선언-인스턴스화
        return vh ;
    }




    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(MultiImageAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Uri image_uri = mData.get(position) ;
        Glide.with(mContext).load(image_uri).into(holder.아이템이미지);//아이템이미지 뷰에 이미지를 넣는 역할
        ViewHolder viewholder=(ViewHolder) holder;
        viewholder.수정삭제버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(mContext, (position+1) + "번째 사진 삭제", Toast.LENGTH_SHORT).show();//누르자마자 토스트로 확인
                mData.remove(position);//데이터 지움(리스트에서 삭제)
                notifyItemRemoved(position);//아이템 지웠다고 알림(누구한테?)
                notifyItemRangeChanged(position, getItemCount() - position);//아이템 범위가 바뀌어있다는걸 알림림
            }
       });
    }


    // getItemCount() - 전체 데이터 갯수 리턴.
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView 아이템이미지;
        TextView 아이템텍스트뷰;
        Button 수정삭제버튼;

        ViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조.
            아이템이미지 = itemView.findViewById(R.id.아이템이미지);
            아이템텍스트뷰 = itemView.findViewById(R.id.아이템텍스트뷰);
            수정삭제버튼 = itemView.findViewById(R.id.수정삭제버튼);

        }
    }
//내부 클래스로 커스텀 뷰홀더를 만듦
}