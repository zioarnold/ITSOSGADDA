package com.example.arnold.itsosgadda;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import static com.example.arnold.itsosgadda.R.menu.reload;

public class ComActivity extends Activity {
    private static final String url = "jdbc:mysql://188.209.81.18:3306/app_db";
    private static final String user = "app";
    private static final String pass = "4826159g";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com_handler);
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffeb3b")));
        testDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(reload, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i) {
            case R.id.reload:
                testDB();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void testDB() {
        TextView textView = (TextView) findViewById(R.id.comTextView);
        ListView listView = (ListView) findViewById(R.id.listView);
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().
                    permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String result = "";
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM app_db.push ORDER BY ID DESC");
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
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
