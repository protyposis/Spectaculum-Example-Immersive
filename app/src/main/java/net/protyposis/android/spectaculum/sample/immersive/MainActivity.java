package net.protyposis.android.spectaculum.sample.immersive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.exoplayer2.ui.PlaybackControlView;

public class MainActivity extends AppCompatActivity {

    private SpectaculumView mSpectaculumView;
    private PlaybackControlView mPlaybackControlView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get view references from layout
        mSpectaculumView = (SpectaculumView) findViewById(R.id.spectaculumview);
        mPlaybackControlView = (PlaybackControlView) findViewById(R.id.playbackcontrolview);
    }
}
