package com.example.arnold.itsosgadda.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.arnold.itsosgadda.utilities.Log4jHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.log4j.Logger;

import static com.example.arnold.itsosgadda.R.id.imageViewStd;
import static com.example.arnold.itsosgadda.R.mipmap.loading;
import static com.example.arnold.itsosgadda.R.mipmap.loading_failed;

public class PhotoGalleryActivity extends Activity {

    private ImageLoader imageLoader;
    private String url = "content://media/external/images/media/13";
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .showImageOnLoading(loading)
                    .showImageForEmptyUri(loading_failed)
                    .showImageOnFail(loading_failed)
                    .build();
            ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext())
                    .defaultDisplayImageOptions(displayImageOptions)
                    .build();
            ImageLoader.getInstance().init(configuration);
            imageView = (ImageView) findViewById(imageViewStd);
            imageLoader.displayImage(url, imageView, displayImageOptions);
        } catch (Exception ex) {
            Logger log = Log4jHelper.getLogger("PhotoGalleryActivity");
            log.error("Error", ex);
        }
    }
}
