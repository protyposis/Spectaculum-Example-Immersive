package net.protyposis.android.spectaculum.sample.immersive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.exoplayer2.ui.PlaybackControlView;

import net.protyposis.android.spectaculum.SpectaculumView;
import net.protyposis.android.spectaculum.effects.ImmersiveEffect;
import net.protyposis.android.spectaculum.effects.ImmersiveTouchNavigation;

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

        // Setup Spectaculum view for immersive content
        ImmersiveEffect immersiveEffect = new ImmersiveEffect(); // create effect instance
        mSpectaculumView.addEffect(immersiveEffect); // add effect to view
        mSpectaculumView.selectEffect(0); // activate effect

        // Setup Spectaculum immersive viewport touch navigation
        ImmersiveTouchNavigation immersiveTouchNavigation = new ImmersiveTouchNavigation(mSpectaculumView);
        immersiveTouchNavigation.attachTo(immersiveEffect);
        immersiveTouchNavigation.activate(); // enable touch navigation
    }
}
