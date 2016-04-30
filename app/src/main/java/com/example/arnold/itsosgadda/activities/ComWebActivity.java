package com.example.arnold.itsosgadda.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.arnold.itsosgadda.R;
import com.example.arnold.itsosgadda.handlers.NavigationDrawerFragment;

import static android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED;
import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.webkit.WebSettings.ZoomDensity.FAR;
import static com.example.arnold.itsosgadda.R.id.container;
import static com.example.arnold.itsosgadda.R.layout.fragment_main_navitagion_drawer;
import static com.example.arnold.itsosgadda.handlers.NavigationDrawerFragment.NavigationDrawerCallbacks;

@SuppressWarnings("FieldCanBeLocal")
public class ComWebActivity extends Activity implements NavigationDrawerCallbacks {
    private final ComWebActivity activity = this;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private final String url = "http://www.iissgadda.it/pvw/app/PRIT0007/pvw_sito.php?sede_codice=PRIT0007&page=1823314";
    private static final String TAG = "ComWebActivity";
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
            //Shows me status bar progress
            ActionBar actionBar = getActionBar();
            assert actionBar != null;
            actionBar.setIcon(R.mipmap.ic_launcher);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffeb3b")));
            setContentView(R.layout.activity_com_web);
            mNavigationDrawerFragment = (NavigationDrawerFragment)
                    getFragmentManager().findFragmentById(R.id.navigation_drawer);
            mNavigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));
            WebView webView = (WebView) findViewById(R.id.webView_ComWeb_registry);
            //Enables JS
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
            //noinspection deprecation
            webView.getSettings().setDefaultZoom(FAR);
            webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    activity.setTitle(R.string.loading_progress);
                    activity.setProgress(progress * 100);

                    if (progress == 100)
                        activity.setTitle(R.string.communications_news);
                }
            });
            webView.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                            String mimetype, long contentLength) {
                    String filename = URLUtil.guessFileName(url, contentDisposition, mimetype);
                    Request request = new Request(Uri.parse(url));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, filename);
                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    downloadManager.enqueue(request);
                }
            });

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    // Handle the error
                    Log.w(TAG, "Some error!");
                    super.onReceivedError(view, errorCode, description, failingUrl);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            webView.loadUrl(url);
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
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
            Log.d(TAG, ex.getMessage());
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
                Log.d(TAG, ex.getMessage());
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
        }
    }
}


