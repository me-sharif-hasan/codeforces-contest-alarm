package com.iishanto.cpalarm.AlarmReceivers;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.iishanto.cpalarm.MainActivity;
import com.iishanto.cpalarm.R;
import com.iishanto.cpalarm.ToolsSingleton;

public class AlarmHandler extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context,"Alarm fired",Toast.LENGTH_LONG).show();
        System.out.println("ii: receviers");


        String contestName = intent.getStringExtra("name");
        int id = intent.getIntExtra("id", 101);
        long time = intent.getLongExtra("time", 0);
        checkAndCancel(context,intent, id, time);

        System.out.println("ii: " + contestName + ";" + id);

        notificationHandler(context, contestName, id);

        MediaPlayer mp = ToolsSingleton.getInstance().getMediaPlayer(context);
        mp.start();
    }

    void checkAndCancel(Context c,Intent i, int code, long t) {
        if ((System.currentTimeMillis() - t) / (1000 * 60) > 2) {
            AlarmManager am = (AlarmManager) ToolsSingleton.getInstance().getMainContext().getSystemService(ToolsSingleton.getInstance().getMainContext().ALARM_SERVICE);
            PendingIntent pi = ToolsSingleton.getInstance().createPendingIntent(code, i, 1);
            am.cancel(pi);
            pi.cancel();
            Toast.makeText(c,"The alarm is cancelled!",Toast.LENGTH_LONG).show();
        }
    }

    void notificationHandler(Context context, String contestName, int id) {
        String nch = "iishanto_notify";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "iishanto-com").setContentTitle(contestName)
                .setContentText(contestName)
                .setSmallIcon(R.drawable.alarm_icon_foreground)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Contest will be started soon!"))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = nch;
            String description = "iishanto notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("iishanto-com", name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(id, builder.build());
    }
}
