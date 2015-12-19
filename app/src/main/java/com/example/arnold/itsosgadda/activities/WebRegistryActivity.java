package com.example.arnold.itsosgadda.activities;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.arnold.itsosgadda.R;
import com.example.arnold.itsosgadda.handlers.NavigationDrawerFragment;
import com.example.arnold.itsosgadda.handlers.NavigationDrawerFragment.NavigationDrawerCallbacks;
import com.example.arnold.itsosgadda.utilities.Log4jHelper;

import org.apache.log4j.Logger;

import static android.webkit.WebSettings.ZoomDensity.FAR;
import static com.example.arnold.itsosgadda.R.id.container;
import static com.example.arnold.itsosgadda.R.layout.fragment_main_navitagion_drawer;

public class WebRegistryActivity extends Activity implements NavigationDrawerCallbacks{
    private final WebRegistryActivity activity = this;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private final String url = "https://web.spaggiari.eu/home/app/default/login.php?custcode=prit0007";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
            //Shows me status bar progress
            ActionBar actionBar = getActionBar();
            assert actionBar != null;
            actionBar.setIcon(R.mipmap.ic_launcher);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffeb3b")));
            setContentView(R.layout.e_registry_layout);
            mNavigationDrawerFragment = (NavigationDrawerFragment)
                    getFragmentManager().findFragmentById(R.id.navigation_drawer);
            mNavigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));
            WebView webView = (WebView) findViewById(R.id.webView_Web_registry);
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
                        activity.setTitle(R.string.app_name);
                }
            });

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    // Handle the error
                    Logger log = Log4jHelper.getLogger("WebView");
                    log.warn("WEB_VIEW_SPAGGIARI, some error");
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
            Logger log = Log4jHelper.getLogger("WebRegistryActivity");
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
            Logger log = Log4jHelper.getLogger("WebRegistryActivity");
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
                Logger log = Log4jHelper.getLogger("WebRegistryActivity");
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
        Log.v("WBA", "++ ON START ++");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("WBA", "+ ON RESUME +");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("WBA", "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        finish();
        Log.v("WBA", "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("WBA", "- ON DESTROY -");
    }
}


