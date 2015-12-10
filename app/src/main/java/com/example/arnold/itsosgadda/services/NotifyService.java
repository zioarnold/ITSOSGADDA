package com.example.arnold.itsosgadda.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.arnold.itsosgadda.activities.ComActivity;
import com.example.arnold.itsosgadda.R;

import java.util.Timer;
import java.util.TimerTask;

import static android.media.RingtoneManager.TYPE_NOTIFICATION;


public class NotifyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Timer timer;
    private Uri sound;
    private Intent intent;
    private PendingIntent pendingIntent;
    private NotificationManager notificationManager;
    @Override
    public void onCreate() {
        super.onCreate();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                notificationLaunch();
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000 * 60 * 60 * 24);
    }

    private void notificationLaunch() {
        sound = RingtoneManager.getDefaultUri(TYPE_NOTIFICATION);
        intent = new Intent(getApplicationContext(), ComActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        Notification notification =
                new NotificationCompat.Builder(getApplicationContext())
                        .setTicker(getText(R.string.notification_something_arrived))
                        .setContentTitle(getText(R.string.notification_communication_arrived))
                        .setContentText(getText(R.string.notification_check_for_communication))
                        .setSound(sound)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pendingIntent)
                        .build();

        int countNotification = 0;
        notificationManager.notify(countNotification, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
    }
}
