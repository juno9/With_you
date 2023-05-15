package com.example.with_you;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<Place> 장소목록;
    private OnItemClickListener onItemClickListener = null;


    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public void setOnItemClickListener(PlaceAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    PlaceAdapter(ArrayList<Place> list, Context context) {
        this.장소목록 = list;
        this.mcontext = context;
    }//멀티이미지어댑터 생성자. 만들때 리스트와 컨텍스트를 받는다.

    @NonNull
    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);    // context에서 LayoutInflater 객체를 얻는다.
        View view = inflater.inflate(R.layout.placeitem, parent, false);    // 리사이클러뷰에 들어갈 아이템뷰의 레이아웃을 inflate.
        PlaceAdapter.ViewHolder vh = new PlaceAdapter.ViewHolder(view);//뷰홀더 선언-인스턴스화
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.ViewHolder holder, int position) {
        String 장소명 = 장소목록.get(position).get이름();
        String 주소 = 장소목록.get(position).get주소();
        holder.장소명텍스트뷰.setText(장소명);
        holder.주소텍스트뷰.setText(주소);
    }

    @Override
    public int getItemCount() {
        return 장소목록.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView 장소명텍스트뷰;
        TextView 주소텍스트뷰;

        ViewHolder(View itemView) {
            super(itemView);
            장소명텍스트뷰 = itemView.findViewById(R.id.장소명텍스트뷰);
            장소명텍스트뷰.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
            주소텍스트뷰 = itemView.findViewById(R.id.주소텍스트뷰);
        }
    }//내부클래스로 커스텀뷰홀더 만듦
}
