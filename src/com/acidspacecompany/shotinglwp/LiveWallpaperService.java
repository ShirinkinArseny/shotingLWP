package com.acidspacecompany.shotinglwp;

import android.content.Context;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.LifecycleRenderer;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.OpenGLES20LiveWallpaperService;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class LiveWallpaperService extends OpenGLES20LiveWallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new OpenGLES20LiveWallpaperService.GLEngine();
    }

    public LifecycleRenderer getRenderer() {
        //Создаем рекламу
        // AdBuilder.initAd(getBaseContext());
        return new MyRenderer();
    }

    class MyRenderer implements LifecycleRenderer {

        private World world;

        @Override
        public void onCreate(Context context) {
            world = new World(context);
        }

        @Override
        public void onPause() {
            world.pausePainting();
        }

        @Override
        public void onDestroy() {
            world.stopPainting();
            Graphic.destroy();
        }

        @Override
        public void onResume() {
            world.resumePainting();
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            world.init();
            Graphic.init(getBaseContext());
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            World.resize(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            world.updateAndDraw();
        }
    }
}