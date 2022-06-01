package com.example.myRefri02;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ItemViewHolder> {

    private ArrayList<Data> listData = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.refri_item,parent,false);
        if(listData.isEmpty()){
        }
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItem(Data data){
        listData.add(data);
    }



    class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle;
        private TextView tvContent;

        ItemViewHolder(View itemView){
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvtitle);
            tvContent = itemView.findViewById(R.id.tvcontent);
        }

        void onBind(Data data){
            tvTitle.setText(data.getTitle());
            tvContent.setText(data.getContent());
        }

    }

}