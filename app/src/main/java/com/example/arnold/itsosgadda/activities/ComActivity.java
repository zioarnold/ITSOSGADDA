package com.example.arnold.itsosgadda.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arnold.itsosgadda.R;
import com.example.arnold.itsosgadda.handlers.NavigationDrawerFragment;
import com.example.arnold.itsosgadda.handlers.NavigationDrawerFragment.NavigationDrawerCallbacks;
import com.example.arnold.itsosgadda.utilities.Log4jHelper;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import static com.example.arnold.itsosgadda.R.id.container;
import static com.example.arnold.itsosgadda.R.layout.fragment_main_navitagion_drawer;
import static com.example.arnold.itsosgadda.R.menu.reload;
import static com.example.arnold.itsosgadda.R.string.error_connection;

public class ComActivity extends Activity implements NavigationDrawerCallbacks {
    private static final String url = "jdbc:mysql://188.209.81.18:3306/app_db",
            user = "app", pass = "4826159g";
    private NavigationDrawerFragment mNavigationDrawerFragment;
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
            mNavigationDrawerFragment = (NavigationDrawerFragment)
                    getFragmentManager().findFragmentById(R.id.navigation_drawer);
            // Set up the drawer.
            mNavigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));
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

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        try {
            // update the main content by replacing fragments
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(container, PlaceholderFragment.newInstance(position + 1))
                    .commit();
        } catch (Exception ex) {
            Logger log = Log4jHelper.getLogger("ComActivity");
            log.error("Error", ex);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            try {
                Bundle args = new Bundle();
                args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                fragment.setArguments(args);
            } catch (Exception ex) {
                Logger log = Log4jHelper.getLogger("MainActivity");
                log.error("Error", ex);
            }
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(fragment_main_navitagion_drawer, container, false);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            /*((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));*/
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("Com", "++ ON START ++");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("Com", "+ ON RESUME +");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("Com", "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        finish();
        Log.v("Com", "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("Com", "- ON DESTROY -");
    }
}
