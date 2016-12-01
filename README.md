Spectaculum Sample App: Immersive 360° video playback with ExoPlayer
====================================================================

This app demonstrates immersive video playback with [Spectaculum](https://github.com/protyposis/Spectaculum)
and [ExoPlayer 2](https://github.com/google/ExoPlayer) on Android.
Spectaculum provides a native view widget for picture and video content, in contrast to
alternatives building on WebViews.

By default, this app play back the stereoscopic 3D 360° video "[House Demo](http://www.360rize.com/2014/01/worlds-first-fully-spherical-3d-360-video-and-photo-gear/)".
To switch to the  monoscopic 360° high resolution test video "[Orion360 Test Video](http://www.finwe.mobi/main/360-degree/orion360-test-images-videos/)",
change `MainActivity#mSelectedVideoSource` to `0`.


Troubleshooting
---------------

=== Logcat error "E/ShaderProgram: Error linking program"

All of the Immersive effect setup is done in `onCreate` for the sake of simplicity in this example.
On some devices (e.g. Nexus 9), the OpenGL context does not yet exist at the time when the effect is activated.
This is not really a problem because the effect gets reinitialized once the video is loaded, and works as expected.
Still, you can get rid of the error by activating the effect (`mSpectaculumView.selectEffect(0)`) at a later time.
A good place is `onSurfaceCreated` because there the GL context is guaranteed to be ready, or somewhere later when the video is loaded.

License
-------

Copyright (C) 2016 Mario Guggenberger <mg@protyposis.net>.
Released under the Apache 2.0 license.