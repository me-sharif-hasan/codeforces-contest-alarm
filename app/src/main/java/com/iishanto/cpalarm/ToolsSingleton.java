package com.iishanto.cpalarm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.Settings;

public class ToolsSingleton {
    private Context mainContext;
    private static ToolsSingleton instance;
    MediaPlayer mediaPlayer;
    private ToolsSingleton(){

    }

    public MediaPlayer getMediaPlayer(Context context) {
        if(mediaPlayer==null) mediaPlayer=new MediaPlayer();
        else {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        try {
            mediaPlayer.setDataSource(context, Settings.System.DEFAULT_ALARM_ALERT_URI);
            mediaPlayer.prepare();
        }catch (Exception e){
            System.out.println("ii: "+e.getLocalizedMessage());
        }
        return mediaPlayer;
    }

    public Context getMainContext() {
        return mainContext;
    }

    public void setMainContext(Context mainContext) {
        this.mainContext = mainContext;
    }

    public PendingIntent createPendingIntent(int code, Intent i, int action){
            if(action==0){
                return PendingIntent.getBroadcast(ToolsSingleton.getInstance().getMainContext(), (int)code,i,PendingIntent.FLAG_NO_CREATE);
            }else if(action==1){
                return PendingIntent.getBroadcast(ToolsSingleton.getInstance().getMainContext(), (int)code,i,PendingIntent.FLAG_CANCEL_CURRENT);
            }else{
                return PendingIntent.getBroadcast(ToolsSingleton.getInstance().getMainContext(), (int)code,i,0);
            }
    }

    public static ToolsSingleton getInstance() {
        if(instance==null) instance=new ToolsSingleton();
        return instance;
    }
}
