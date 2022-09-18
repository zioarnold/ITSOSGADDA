package com.example.arnold.itsosgadda.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.arnold.itsosgadda.R;
import com.example.arnold.itsosgadda.main.MainActivity;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayerView;

@SuppressWarnings("FieldCanBeLocal")
public class YouTubeActivity extends YouTubeBaseActivity implements OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    // YouTube player view
    private YouTubePlayerView youTubeView;
    private Button skipVideo;
    private CheckBox checkBox;
    public static final String PREFS_NAME = "SkipYouTubePlayer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_you_tube);

        skipVideo = findViewById(R.id.skip_video);
        skipVideo.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        final SharedPreferences.Editor editor = settings.edit();

        if (settings.getBoolean("skipMessage", false)) {
            // skipping message, so eg. start another activity here
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            return;
        }
        checkBox = findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("skipMessage", isChecked);
            editor.apply();
        });

        youTubeView = findViewById(R.id.youtube_view);

        // Initializing video player with developer key
        youTubeView.initialize(getResources().getString(R.string.youtube_developer_key), this);

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = getString(R.string.error_player);
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            player.loadVideo(getResources().getString(R.string.youtube_video_code));

            // Hiding player controls
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(getResources().getString(R.string.youtube_developer_key), this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return findViewById(R.id.youtube_view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}