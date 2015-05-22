package com.examples.dota2tv.loadMore;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.examples.dota2tv.R;
import com.examples.dota2tv.data.Video;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnFullscreenListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class YoutubeActionBarActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {

    private Video video;
    private String videoId;
    private TextView title;
    private TextView desc;
    private View myContent;
    private boolean isfullscreen;
    private boolean isFullscreenMode;
    private AppCompatActivity sfa;
    private YouTubePlayerSupportFragment fragment;
    private ActionBar mActionBar;

    private static final int LANDSCAPE_ORIENTATION = Build.VERSION.SDK_INT < 9 ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

    private static final int RECOVERY_DIALOG_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sfa = this;


//		mActionBar = getSupportActionBar();
//
//		mActionBar.setTitle("Watch a Video");
//
//		mActionBar.setHomeButtonEnabled(true);
//		mActionBar.setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        if (isFullscreenMode = intent.getBooleanExtra("isfullscreen", false)) {
            videoId = intent.getStringExtra("videoId");
            setRequestedOrientation(LANDSCAPE_ORIENTATION);
            setContentView(R.layout.fullscreenyoutube);

        } else {
            video = intent.getParcelableExtra("video");

            setContentView(R.layout.videoplayer);

            myContent = findViewById(R.id.videoContent);

            title = (TextView) findViewById(R.id.videotitle);
            title.setText(video.getTitle());

            desc = (TextView) findViewById(R.id.videodesc);
            desc.setText(video.getVideoDesc());

        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        fragment = new YouTubePlayerSupportFragment();
        fragmentTransaction.add(R.id.youtubeplayer, fragment);
        fragmentTransaction.commit();
        fragment.initialize("AIzaSyAuEa3bIKbSYiXVWbHU_zueVzEBv9p2r_Y", this);
        doLayout();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate your menu.
        getMenuInflater().inflate(R.menu.share_action_provider, menu);

        // Set file with share history to the provider and set the share intent.
        MenuItem actionItem = menu.findItem(R.id.menu_item_share_action_provider_action_bar);
        ShareActionProvider actionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(actionItem);
        actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        // Note that you can set/change the intent any time,
        // say when the user has selected an image.
        actionProvider.setShareIntent(createShareIntent());

        return super.onCreateOptionsMenu(menu);
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setType("image/*");
//        Uri uri = Uri.fromFile(getFileStreamPath("shared.png"));
//        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setAction(Intent.ACTION_SEND);
        if (isFullscreenMode) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + videoId + " via @Dota2TV1");
        } else {
            shareIntent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + video.getVideoId() + " via @Dota2TV1");
        }
        shareIntent.setType("text/plain");
        return shareIntent;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (isfullscreen) {
            // Checks the orientation of the screen for landscape and portrait
            // and set portrait mode always
            //System.out.println("FULL!!!!!!!!!!!!!!!!!!!!!!!!");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        if (!isFullscreenMode)
            doLayout();
    }

    @SuppressWarnings("deprecation")
    private void doLayout() {
        if (isfullscreen) {
            if (title != null)
                title.setVisibility(TextView.GONE);
            if (myContent != null)
                myContent.setVisibility(View.GONE);
            if (desc != null)
                desc.setVisibility(View.GONE);
            if (fragment != null) {
                if (fragment.getView() != null)
                    fragment.getView().setLayoutParams(new RelativeLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
//            mActionBar.hide();

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        } else {
            if (title != null)
                title.setVisibility(TextView.VISIBLE);
            if (myContent != null)
                myContent.setVisibility(View.VISIBLE);
            if (desc != null)
                desc.setVisibility(View.VISIBLE);
            if (fragment != null) {
                if (fragment.getView() != null)
                    fragment.getView().setLayoutParams(new RelativeLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            }

//            mActionBar.show();

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize("AIzaSyAuEa3bIKbSYiXVWbHU_zueVzEBv9p2r_Y", this);
        }
    }

    public YouTubePlayer.Provider getYouTubePlayerProvider() {
        return fragment;
    }

    @Override
    public void onInitializationSuccess(Provider arg0,
                                        YouTubePlayer ytp, boolean wasRestored) {

        ytp.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        ytp.setOnFullscreenListener(new OnFullscreenListener() {


            @Override
            public void onFullscreen(boolean _isFullScreen) {
                isfullscreen = _isFullScreen;
                if (isfullscreen)
                    setRequestedOrientation(LANDSCAPE_ORIENTATION);
                else {
                    if (isFullscreenMode)
                        finish();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//							doLayout();
                }

                doLayout();


            }
        });
//				Toast.makeText(sfa, "Initialization  Success",
//						Toast.LENGTH_LONG).show();
        if (!wasRestored) {
            if (isFullscreenMode) {
                ytp.setFullscreen(true);
                ytp.loadVideo(videoId);
            } else {
                ytp.setFullscreen(true);
                ytp.loadVideo(video.getVideoId());
            }
        }


    }


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(sfa, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(getString(R.string.error_player), errorReason.toString());
//		      Toast.makeText(sfa, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

}
