package com.example.arnold.itsosgadda.activities;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.arnold.itsosgadda.R;
import com.example.arnold.itsosgadda.services.NotifyService;
import com.example.arnold.itsosgadda.utilities.Log4jHelper;

import org.apache.log4j.Logger;

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
        try {
            setContentView(R.layout.com_handler);
            ActionBar actionBar = getActionBar();
            assert actionBar != null;
            actionBar.setIcon(R.mipmap.ic_launcher);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffeb3b")));

            dataBaseConnect();
        } catch (Exception ex) {
            Logger log = Log4jHelper.getLogger("MainActivity");
            log.error("Error", ex);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(reload, menu);
        } catch (Exception ex) {
            Logger log = Log4jHelper.getLogger("MainActivity");
            log.error("Error", ex);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int v = item.getItemId();
            switch (v) {
                case R.id.reload:
                    dataBaseConnect();
                    break;
            }
        } catch (Exception ex) {
            Logger log = Log4jHelper.getLogger("MainActivity");
            log.error("Error", ex);
        }
        return super.onOptionsItemSelected(item);
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
            resultSet = statement.executeQuery("SELECT * FROM app_db.push " +
                    "ORDER BY ID DESC");
            resultSetMetaData = resultSet.getMetaData();
            while (resultSet.next()) {
                result += resultSetMetaData.getColumnName(2) + " : "
                        + resultSet.getString(2) + "\n";
                result += resultSetMetaData.getColumnName(3) + " : "
                        + resultSet.getString(3) + "\n\n";
            }
            textView.setText(result);
            textView.setMovementMethod(new ScrollingMovementMethod());
            Linkify.addLinks(textView, Linkify.WEB_URLS);
        } catch (Exception ex) {
            Logger log = Log4jHelper.getLogger("ComActivity");
            log.error("Error", ex);
        }
    }
}
