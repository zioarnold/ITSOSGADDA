package com.example.arnold.itsosgadda;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.arnold.itsosgadda.R.menu.reload;

@SuppressWarnings("deprecation")
public class CommunicationsActivity extends Activity {
    private String jsonResult;
    //private String url = "https://drive.google.com/open?id=0By57NyAMoNAhWDl2ckJvMy1qLUU";
    //private String url = "http://192.168.168.229/config.php";
    private String url = "http://www.iissgadda.it/app2/config.php";
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communications_news_layout);
        listView = (ListView) findViewById(R.id.listView);
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffeb3b")));

        accessWebService();
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
                accessWebService();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class JsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(params[0]);

            try {
                HttpResponse response = httpClient.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream inputStream) {
            String rLine;
            StringBuilder answer = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                while ((rLine = bufferedReader.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Error..." + ex.toString(), Toast.LENGTH_LONG).show();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String s) {
            ListDrwaer();
        }


    }

    private void ListDrwaer() {
        try {
            JSONObject jsonResponse = new JSONObject(jsonResult.substring(jsonResult.indexOf("{"),
                    jsonResult.lastIndexOf("}") + 1));
            JSONArray jsonMainNode = jsonResponse.optJSONArray("app_db");
            final ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<>();
            HashMap<String, String> map;
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonObject = jsonMainNode.getJSONObject(i);
                map = new HashMap<>();
                map.put("ID", jsonObject.getString("ID"));
                map.put("push_name", jsonObject.getString("push_name"));
                map.put("link", jsonObject.getString("link"));
                hashMapArrayList.add(map);
                SimpleAdapter adapter = new SimpleAdapter(CommunicationsActivity.this,
                        hashMapArrayList,
                        R.layout.communications_news_handler,
                        new String[]{"ID", "push_name", "link"},
                        new int[]{R.id.newsID, R.id.newsName, R.id.nameLink});
                listView.setAdapter(adapter);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Error..." + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void accessWebService() {
        JsonReadTask task = new JsonReadTask();
        task.execute(url);
    }
}