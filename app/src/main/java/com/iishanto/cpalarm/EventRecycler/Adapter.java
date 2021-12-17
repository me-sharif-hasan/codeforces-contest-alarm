package com.iishanto.cpalarm.EventRecycler;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iishanto.cpalarm.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<CustomViewHolder> {

    ArrayList <JSONObject> items= new ArrayList<>();

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("ii: Called here");
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View v=layoutInflater.inflate(R.layout.event_schedule,parent,false);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        try {
            holder.update(items.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public void clear(){
        items.clear();
    }

    public void addItem(JSONObject s){

        items.add(0,s);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }
}
