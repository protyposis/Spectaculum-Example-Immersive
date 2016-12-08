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

### Logcat error "E/ShaderProgram: Error linking program"

Happens when the effect is activated (`mSpectaculumView.selectEffect(0)`) before the OpenGL surface is ready.
This is not really a problem because the effect gets reinitialized later once the surface is ready, and works as expected.
You can get rid of the error by activating the effect at a later time e.g. in `onSurfaceCreated` where
the GL surface is guaranteed to be ready, or when the video is loaded.

This error happened in earlier versions of this example app where `selectEffect` was called from `onCreate`.
The call was moved to `onSurfaceCreated` in commit 95705cc to fix this error.


### EffectException: java.util.ConcurrentModificationException

This exception comes from a concurrent modification of the list of parameters that the shader effect manages internally.
This list is modified during initialization of the effect, and when a viewport navigation method (e.g. viewport touch navigation) is attached.
Initialization happens when selecting the effect for the first time (with `selectEffect(index)`),
or when it is automatically reinitialized because the surface changes (e.g. surface size change when video rendering starts).

To avoid a collision of these events, it is best to not select the effect until the surface is ready (`onSurfaceCreated`).


License
-------

Copyright (C) 2016 Mario Guggenberger <mg@protyposis.net>.
Released under the Apache 2.0 license.