package net.protyposis.android.spectaculum.sample.immersive;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import net.protyposis.android.spectaculum.InputSurfaceHolder;
import net.protyposis.android.spectaculum.SpectaculumView;
import net.protyposis.android.spectaculum.effects.ImmersiveEffect;
import net.protyposis.android.spectaculum.effects.ImmersiveTouchNavigation;

public class MainActivity extends AppCompatActivity implements InputSurfaceHolder.Callback {

    private SpectaculumView mSpectaculumView;
    private PlaybackControlView mPlaybackControlView;

    private String mVideoUri;

    private SimpleExoPlayer mExoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get view references from layout
        mSpectaculumView = (SpectaculumView) findViewById(R.id.spectaculumview);
        mPlaybackControlView = (PlaybackControlView) findViewById(R.id.playbackcontrolview);

        // Register callbacks to initialize and release player
        mSpectaculumView.getInputHolder().addCallback(this);

        // Set the playback control view duration to as long as possible so we do not have to
        // handle view visibility toggling in this example
        mPlaybackControlView.setShowDurationMs(Integer.MAX_VALUE);

        // Setup Spectaculum view for immersive content
        ImmersiveEffect immersiveEffect = new ImmersiveEffect(); // create effect instance
        mSpectaculumView.addEffect(immersiveEffect); // add effect to view
        mSpectaculumView.selectEffect(0); // activate effect

        // Setup Spectaculum immersive viewport touch navigation
        ImmersiveTouchNavigation immersiveTouchNavigation = new ImmersiveTouchNavigation(mSpectaculumView);
        immersiveTouchNavigation.attachTo(immersiveEffect);
        immersiveTouchNavigation.activate(); // enable touch navigation

        // Orion360 Test Video: http://www.finwe.mobi/main/360-degree/orion360-test-images-videos/
        // Url extracted from https://littlstar.com/videos/c9c27ffc
        mVideoUri = "https://360.littlstar.com/production/79f8bd2f-d137-46ac-a60a-f9b22f77b57d/download.mp4";

        // National Geographic Virtual Yellowstone: https://littlstar.com/videos/b541e0f4
        //mVideoUri = "https://360.littlstar.com/production/83ef40fe-6e8e-45ed-86f8-871c89c3a60f/download.mp4";
    }

    @Override
    public void surfaceCreated(InputSurfaceHolder holder) {
        // When the input surface (i.e. the surface that the player needs to write video frames)
        // has been successfully created, we can initialize the player
        initializePlayer();
    }

    @Override
    public void surfaceDestroyed(InputSurfaceHolder holder) {
        // When the input surface is gone, we cannot display video frames any more and release the player
        releasePlayer();
    }

    /**
     * Create an initialize an ExoPlayer instance that is ready for playback.
     */
    private void initializePlayer() {
        /*
         * Creating the player
         * Code taken from docs and simplified
         * https://google.github.io/ExoPlayer/guide.html#creating-the-player
         */
        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        TrackSelector trackSelector = new DefaultTrackSelector(mainHandler);

        // 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

        // 3. Create the player
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);


        /*
         * Attaching the player to a view
         * We do not use the SimpleExoPlayerView, because we want to use SpectaculumView. Instead,
         * we directly use a PlaybackControlView and attach the player to it.
         * https://google.github.io/ExoPlayer/guide.html#attaching-the-player-to-a-view
         */
        mPlaybackControlView.setPlayer(player);


        /*
         * Configure player for SpectaculumView
         */
        // Set Spectaculum view as playback surface
        player.setVideoSurface(mSpectaculumView.getInputHolder().getSurface());
        // Attach listener to listen to video size changed events
        player.setVideoListener(new SimpleExoPlayer.VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                // When the video size changes, we update the Spectaculum view
                mSpectaculumView.updateResolution(width, height);
            }

            @Override
            public void onRenderedFirstFrame() {
                // Hide loading indicator when video is ready for playback
                findViewById(R.id.loadingindicator).setVisibility(View.GONE);

                // Inform user that he can look around in the video
                Toast.makeText(MainActivity.this, R.string.drag, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onVideoTracksDisabled() {
            }
        });


        /*
         * Preparing the player
         * Code taken from docs and simplified
         * https://google.github.io/ExoPlayer/guide.html#preparing-the-player
         */
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "SpectaculumImmersiveSample"));
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(mVideoUri),
                dataSourceFactory, extractorsFactory, null, null);
        // Prepare the player with the source.
        player.prepare(videoSource, true);

        mExoPlayer = player;
    }

    /**
     * Release the ExoPlayer instance.
     */
    private void releasePlayer() {
        if(mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
