package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Anniversaryadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @NonNull
    private Context mContext;
    ArrayList<Event> 이벤트배열;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }




    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView 내용텍스트뷰;
        TextView 날짜텍스트뷰;
        Button 삭제버튼;

        myViewHolder(@NonNull View itemView) {
            super(itemView);
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

    Anniversaryadapter(@NonNull ArrayList<Event> 생성시받아올배열, Context context) {
        // 출력할 객체들을 담고있는 ArrayList를 parameter로 받음
        이벤트배열 = 생성시받아올배열;
        mContext=context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);    // context에서 LayoutInflater 객체를 얻는다.
        View v = inflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        Anniversaryadapter.myViewHolder vh = new Anniversaryadapter.myViewHolder(v);//뷰홀더 선언-인스턴스화
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        myViewHolder myViewHolder = (Anniversaryadapter.myViewHolder) holder;
        myViewHolder.내용텍스트뷰.setText(이벤트배열.get(position).get내용());
        myViewHolder.날짜텍스트뷰.setText(이벤트배열.get(position).get날짜());
    }

    @Override
    public int getItemCount() {
        return 이벤트배열.size();
    }
}
