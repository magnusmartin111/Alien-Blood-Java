package com.brox.livewallpaper.alien_blood;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;
import com.badlogic.gdx.backends.android.AndroidWallpaperListener;

public class LiveWallpaper extends AndroidLiveWallpaperService {

    @Override
    public void onCreateApplication() {
        super.onCreateApplication();

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.getTouchEventsForLiveWallpaper = true;
        config.useAccelerometer = false;
        config.useCompass = false;
        config.useRotationVectorSensor = false;
        config.useGyroscope = false;
        config.useWakelock = false;
        config.disableAudio = true;
        ApplicationListener listener = new LWSpaceListener();
        initialize(listener, config);
    }

    @Override
    public Engine onCreateEngine() {
        return new AndroidWallpaperEngine() {
            @Override
            public void onPause() {
                super.onPause();
            }

            @Override
            public void onDestroy() {
                super.onDestroy();
            }

            @Override
            public void onResume() {
                super.onResume();
            }
        };
    }

    public static class LWSpaceListener extends LWSpace implements AndroidWallpaperListener, ApplicationListener {

        @Override
        public void offsetChange(float xOffset, float yOffset,
                                 float xOffsetStep, float yOffsetStep, int xPixelOffset,
                                 int yPixelOffset) {
            this.offset.x = xOffset;
            this.offset.y = yOffset;
        }

        @Override
        public void previewStateChange(boolean isPreview) {
            LWSpaceScreen.container.setVisible(isPreview);
        }

        @Override
        public void iconDropped(int x, int y) {

        }
    }
}