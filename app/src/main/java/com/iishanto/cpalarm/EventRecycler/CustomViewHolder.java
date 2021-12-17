package com.iishanto.cpalarm.EventRecycler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iishanto.cpalarm.AlarmReceivers.AlarmHandler;
import com.iishanto.cpalarm.R;
import com.iishanto.cpalarm.ToolsSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

public class CustomViewHolder extends RecyclerView.ViewHolder {
    View v;
    Calendar cal=Calendar.getInstance(Locale.ENGLISH);
    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);
        v=itemView;
    }
    private void setName(String s){
        TextView tv=v.findViewById(R.id.name);
        tv.setText(s);
    }
    private void setTime(long time){
        TextView tv=v.findViewById(R.id.time);

        tv.setText(getTime(time));
    }
    private void setDate(long time){
        TextView tv=v.findViewById(R.id.date);

        tv.setText(getDate(time));
    }



    PendingIntent pi;
    private void setSwitch(long time,int id,String contestName){
        Switch sw=v.findViewById(R.id.alarm);

        AlarmManager am=(AlarmManager) ToolsSingleton.getInstance().getMainContext().getSystemService(ToolsSingleton.getInstance().getMainContext().ALARM_SERVICE);


        Intent i=new Intent(ToolsSingleton.getInstance().getMainContext(), AlarmHandler.class);

        i.putExtra("name",contestName);
        i.putExtra("time",time);
        i.putExtra("id",id);

        pi=ToolsSingleton.getInstance().createPendingIntent(id, i, 0);
        if(pi==null){
            sw.setChecked(false);
        }else{
            System.out.println("ii: "+Calendar.getInstance().getTimeInMillis()+";"+time);
            if(System.currentTimeMillis()>time){
                sw.setChecked(false);
                pi=ToolsSingleton.getInstance().createPendingIntent(id,i,1);
                System.out.println("ii: alarm expired");
                am.cancel(pi);
                pi.cancel();
            }else {
                sw.setChecked(true);
            }
        }

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    pi=ToolsSingleton.getInstance().createPendingIntent(id,i,2);
                    am.setRepeating(AlarmManager.RTC_WAKEUP,time,60*1000,pi);
                    System.out.println("ii: alarm on");
                }else{
                    pi=ToolsSingleton.getInstance().createPendingIntent(id,i,1);
                    System.out.println("ii: alarm off");
                    am.cancel(pi);
                    pi.cancel();
                    ToolsSingleton.getInstance().getMediaPlayer(ToolsSingleton.getInstance().getMainContext()).stop();
                }
            }
        });
    }
    public void update(JSONObject jsonObject){
        try {
            setName(jsonObject.getString("name"));
            setTime(jsonObject.getInt("startTimeSeconds"));
            setDate(jsonObject.getInt("startTimeSeconds"));
            setSwitch((long) jsonObject.getInt("startTimeSeconds")*1000-5*60*1000/*System.currentTimeMillis()+8*1000*/,jsonObject.getInt("id"),jsonObject.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getTime(long time){
        cal.setTimeInMillis(time * 1000);
        String t = DateFormat.format("h:mm a", cal).toString();
        return t;
    }

    private String getDate(long time) {
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("EEEE, MMMM d", cal).toString();
        return date;
    }
}
