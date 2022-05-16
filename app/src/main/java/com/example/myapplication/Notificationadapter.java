package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class Notificationadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @NonNull
    private Context mContext;
    ArrayList<Event> 이벤트배열;
    private OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }


    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView 내용텍스트뷰;
        TextView 날짜텍스트뷰;
        TextView 삭제버튼;

        public myViewHolder(@NonNull View itemView) {
            super(Objects.requireNonNull(itemView));
            내용텍스트뷰 = itemView.findViewById(R.id.내용표시텍스트뷰);
            날짜텍스트뷰 = itemView.findViewById(R.id.날짜표시텍스트뷰);
            삭제버튼 = itemView.findViewById(R.id.이벤트삭제버튼);
            삭제버튼.setOnClickListener(new View.OnClickListener() {
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
        }
    }

    Notificationadapter(@NonNull ArrayList<Event> 생성시받아올배열, Context context) {
        // 출력할 객체들을 담고있는 ArrayList를 parameter로 받음
        이벤트배열 = 생성시받아올배열;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        myViewHolder myViewHolder = (Notificationadapter.myViewHolder) holder;
        myViewHolder.내용텍스트뷰.setText(이벤트배열.get(position).get내용());
        myViewHolder.날짜텍스트뷰.setText(이벤트배열.get(position).get날짜());
    }

    @Override
    public int getItemCount() {
        return 이벤트배열.size();
    }
}
