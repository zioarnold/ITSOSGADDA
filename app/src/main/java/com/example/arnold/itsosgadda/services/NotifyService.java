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

import com.example.arnold.itsosgadda.ComActivity;
import com.example.arnold.itsosgadda.R;

import static android.media.RingtoneManager.TYPE_NOTIFICATION;


public class NotifyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Uri sound = RingtoneManager.getDefaultUri(TYPE_NOTIFICATION);
        Intent intent = new Intent(getApplicationContext(), ComActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationManager notificationManager = (NotificationManager)
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

        notificationManager.notify(1, notification);
    }
}
