package com.example.arnold.itsosgadda.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;

import com.example.arnold.itsosgadda.R;
import com.example.arnold.itsosgadda.adapter.PostAdapter;
import com.example.arnold.itsosgadda.post.PostData;
import com.example.arnold.itsosgadda.refresh.Refresher;
import com.example.arnold.itsosgadda.refresh.RefresherListView;

import org.apache.log4j.Logger;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.view.Window.FEATURE_ACTION_BAR;
import static com.example.arnold.itsosgadda.R.id.about_app;
import static java.lang.Boolean.TYPE;

public class RSSReaderActivity extends Activity implements Refresher {
    private ArrayList<PostData> listData;
    //private String urlString = "http://feeds.feedburner.com/muslimorid";
    //private String urlString = "http://feeds.reuters.com/reuters/technologyNews";
    private String urlString = "http://feeds.feedburner.com/IissCarloEmilioGadda?format=xml";
    //private String urlString = "http://feeds.feedburner.com/reuterstech?format=xml";
    private RefresherListView postListView;
    private PostAdapter postAdapter;
    private int pagnation = 1; //start dari 1
    private boolean isRefreshLoading = true;
    private boolean isLoading = false;
    private ArrayList<String> guidList;
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            PostData data = listData.get(arg2 - 1);

            Bundle postInfo = new Bundle();
            postInfo.putString("content", data.postContent);

            Intent postviewIntent = new Intent(getApplicationContext(), ContentPostActivity.class);
            postviewIntent.putExtras(postInfo);
            startActivity(postviewIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_reader);

        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffeb3b")));

        guidList = new ArrayList<>();
        listData = new ArrayList<>();
        postListView = (RefresherListView) this.findViewById(R.id.postListView);
        postAdapter = new PostAdapter(this, R.layout.list_item_article, listData);
        postListView.setAdapter(postAdapter);
        postListView.setOnRefresh(this);
        postListView.onRefreshStart();
        postListView.setOnItemClickListener(onItemClickListener);
        makeActionOverflowMenuShown();
    }

    private void makeActionOverflowMenuShown() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            Logger log = Logger.getLogger("MATActivity");
            log.warn(ex.getMessage());
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        try {
            if (featureId == FEATURE_ACTION_BAR && menu != null) {
                if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
            }
        } catch (Exception ex) {
            Logger log = Logger.getLogger("MATActivity");
            log.warn(ex.getMessage());
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void startFresh() {
        if (!isLoading) {
            isRefreshLoading = true;
            isLoading = true;
            new RssDataController().execute(urlString + 1);
        } else {
            postListView.onRefreshComplete();
        }
    }

    @Override
    public void startLoadMore() {
        if (!isLoading) {
            isRefreshLoading = false;
            isLoading = true;
            new RssDataController().execute(urlString + (++pagnation));
        } else {
            postListView.onLoadingMoreComplete();
        }
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        try {
            int id = item.getItemId();
            switch (id) {
                case R.id.dev_team:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setIcon(R.mipmap.icon_dev_team)
                            .setTitle(R.string.dev_team)
                            .setView(getLayoutInflater().inflate(R.layout.handler_dev_team, null))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show().setCanceledOnTouchOutside(true);
                    break;
                case about_app:
                    builder = new AlertDialog.Builder(this);
                    builder.setIcon(R.mipmap.icon_about)
                            .setTitle(R.string.created_for)
                            .setView(getLayoutInflater().inflate(R.layout.handler_version_app, null))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show().setCanceledOnTouchOutside(true);
                    AlertDialog dialog = builder.create();
                    dialog.dismiss();
                    break;
                case R.id.subscribe:
                    startActivity(new Intent(getApplicationContext(), SendBugCrashReport.class));
                    break;
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception ex) {
            Logger log = Logger.getLogger("MATActivity");
            log.warn(ex.getMessage());
        }
        return false;
    }

    private enum RSSXMLTag {
        TITLE, DATE, LINK, CONTENT, GUID, IGNORETAG
    }

    private class RssDataController extends
            AsyncTask<String, Integer, ArrayList<PostData>> {
        private RSSXMLTag currentTag;

        @Override
        protected ArrayList<PostData> doInBackground(String... params) {
            String urlStr = params[0];
            InputStream is;
            ArrayList<PostData> postDataList = new ArrayList<>();
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setReadTimeout(10 * 1000);
                connection.setConnectTimeout(10 * 1000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int response = connection.getResponseCode();
                Log.d("debug", "The response is: " + response);
                is = connection.getInputStream();

                // parse xml
                XmlPullParserFactory factory = XmlPullParserFactory
                        .newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(is, null);

                int eventType = xpp.getEventType();
                PostData pdData = null;
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "EEE, DD MMM yyyy HH:mm:ss", Locale.US);
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    //noinspection StatementWithEmptyBody
                    if (eventType == XmlPullParser.START_DOCUMENT) {

                    } else if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equals("item")) {
                            pdData = new PostData();
                            currentTag = RSSXMLTag.IGNORETAG;
                        } else if (xpp.getName().equals("title")) {
                            currentTag = RSSXMLTag.TITLE;
                        } else if (xpp.getName().equals("link")) {
                            currentTag = RSSXMLTag.LINK;
                        } else if (xpp.getName().equals("pubDate")) {
                            currentTag = RSSXMLTag.DATE;
                        } else if (xpp.getName().equals("encoded")) {
                            currentTag = RSSXMLTag.CONTENT;
                        } else if (xpp.getName().equals("guid")) {
                            currentTag = RSSXMLTag.GUID;
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (xpp.getName().equals("item")) {
                            // format data disini
                            // dan lainnya pada adapter
                            assert pdData != null;
                            Date postDate = dateFormat.parse(pdData.postDate);
                            pdData.postDate = dateFormat.format(postDate);
                            postDataList.add(pdData);
                        } else {
                            currentTag = RSSXMLTag.IGNORETAG;
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        String content = xpp.getText();
                        content = content.trim();
                        if (pdData != null) {
                            switch (currentTag) {
                                case TITLE:
                                    if (content.length() != 0) {
                                        if (pdData.postTitle != null) {
                                            pdData.postTitle += content;
                                        } else {
                                            pdData.postTitle = content;
                                        }
                                    }
                                    break;
                                case LINK:
                                    if (content.length() != 0) {
                                        if (pdData.postLink != null) {
                                            pdData.postLink += content;
                                        } else {
                                            pdData.postLink = content;
                                        }
                                    }
                                    break;
                                case DATE:
                                    if (content.length() != 0) {
                                        if (pdData.postDate != null) {
                                            pdData.postDate += content;
                                        } else {
                                            pdData.postDate = content;
                                        }
                                    }
                                    break;
                                case CONTENT:
                                    if (content.length() != 0) {
                                        if (pdData.postContent != null) {
                                            pdData.postContent += content;
                                        } else {
                                            pdData.postContent = content;
                                        }
                                    }
                                    break;
                                case GUID:
                                    if (content.length() != 0) {
                                        if (pdData.postGuid != null) {
                                            pdData.postGuid += content;
                                        } else {
                                            pdData.postGuid = content;
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                    eventType = xpp.next();
                }
                Log.v("tst", String.valueOf((postDataList.size())));
            } catch (Exception ex) {
                Logger log = Logger.getLogger("MATActivity");
                log.warn(ex.getMessage());
            }
            return postDataList;
        }

        @Override
        protected void onPostExecute(ArrayList<PostData> result) {
            boolean isUpdated = false;
            for (int i = 0; i < result.size(); i++) {
                //cek apakah post telah ada pada list
                if (guidList.contains(result.get(i).postGuid)) {
                    continue;
                } else {
                    isUpdated = true;
                    guidList.add(result.get(i).postGuid);
                }

                if (isRefreshLoading) {
                    listData.add(i, result.get(i));
                } else {
                    listData.add(result.get(i));
                }
            }

            if (isUpdated) {
                postAdapter.notifyDataSetChanged();
            }

            isLoading = false;

            if (isRefreshLoading) {
                postListView.onRefreshComplete();
            } else {
                postListView.onLoadingMoreComplete();
            }

            super.onPostExecute(result);
        }
    }
}
