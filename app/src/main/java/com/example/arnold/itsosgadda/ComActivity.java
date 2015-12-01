package com.example.arnold.itsosgadda;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.arnold.itsosgadda.services.NotifyService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.media.RingtoneManager.TYPE_NOTIFICATION;
import static com.example.arnold.itsosgadda.R.menu.reload;

public class ComActivity extends Activity {
    private static final String url = "jdbc:mysql://188.209.81.18:3306/app_db",
            user = "app", pass = "4826159g";
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private ResultSetMetaData resultSetMetaData = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com_handler);
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffeb3b")));

        dataBaseConnect();
        /*SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int i = sharedPreferences.getInt("numberOfLaunches", 1);
        if (i < 2) {
            alarmMethod();
            i++;
            editor.putInt("numberOfLaunches", i);
            editor.apply();
        } else {
            alarmMethod();
        }*/
    }

    private void alarmMethod() {
        Intent intent = new Intent(this, NotifyService.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 13);
        calendar.set(Calendar.AM_PM, Calendar.PM);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                1000 * 60 * 60 * 24,
                pendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(reload, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int v = item.getItemId();
        switch (v) {
            case R.id.reload:
                dataBaseConnect();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void notification() {
        Uri sound = RingtoneManager.getDefaultUri(TYPE_NOTIFICATION);
        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(getApplicationContext())
                        .setTicker("You have something to check")
                        .setContentTitle("New communication arrived")
                        .setContentText("Please check for some new communications.")
                        .setSound(sound)
                        .setSmallIcon(R.mipmap.ic_launcher);
        Intent intent = new Intent(getApplicationContext(), ComActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(ComActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification.build());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static int countRows(Connection conn, String tableName) throws SQLException {
        // select the number of rows in the table
        ResultSet rs = null;
        int rowCount = -1;
        try (Statement stmt = conn.createStatement()) {
            rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName);
            // get the number of rows from the result set
            rs.next();
            rowCount = rs.getInt(1);
        } finally {
            assert rs != null;
            rs.close();

        }
        return rowCount;
    }

    public void dataBaseConnect() {

        final TextView textView = (TextView) findViewById(R.id.comTextView);
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().
                    permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String result = "";
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, pass);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery("SELECT * FROM app_db.push ORDER BY ID DESC");
            resultSetMetaData = resultSet.getMetaData();
            while (resultSet.next()) {
                result += resultSetMetaData.getColumnName(2) + " : " + resultSet.getString(2) + "\n";
                result += resultSetMetaData.getColumnName(3) + " : " + resultSet.getString(3) + "\n\n";
            }
            textView.setText(result);
            textView.setMovementMethod(new ScrollingMovementMethod());
            Linkify.addLinks(textView, Linkify.WEB_URLS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
