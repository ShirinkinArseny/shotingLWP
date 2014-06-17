package com.acidspacecompany.shotinglwp;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.LifecycleRenderer;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.MultisampleConfigChooser;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.OpenGLES20LiveWallpaperService;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class LiveWallpaperService extends WallpaperService {

    private World world;


    @Override
    public Engine onCreateEngine() {
        return new GLEngine();
    }

    public class GLEngine extends Engine {
        class MyGLSurfaceView extends GLSurfaceView {

            private Renderer renderer;

            public MyGLSurfaceView(Context context) {
                super(context);
            }

            @Override
            public SurfaceHolder getHolder() {
                return getSurfaceHolder();
            }

            public void onDestroy() {
                this.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        world.stopPainting();
                        Graphic.destroy();
                    }
                });
                super.onDetachedFromWindow();
            }

            @Override
            public void onPause() {
                super.onPause();
                world.pausePainting();
            }

            @Override
            public void onResume() {
                super.onResume();
                if (renderer == null)
                    Log.e("GLSurfaceView", "Renderer hasn't been set up.");
                world.resumePainting();
            }

            public void setRenderer(Renderer renderer) {
                super.setRenderer(renderer);
                world = new World();
            }
        }

        private MyGLSurfaceView glSurfaceView;
        private boolean renderHasBeenSet;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);
            glSurfaceView = new MyGLSurfaceView(LiveWallpaperService.this);

            setEGLContextClientVersion(2);
            setPreserveEGLContextOnPause(true);
            setEGLConfigChooser(new MultisampleConfigChooser());
            setRenderer(new MyRenderer());
        }


        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (renderHasBeenSet) {
                if (visible)
                    glSurfaceView.onResume();
                else
                    glSurfaceView.onPause();
            }
        }

        @Override
        public void onDestroy(){
            super.onDestroy();
            glSurfaceView.onDestroy();
        }

        protected void setRenderer(GLSurfaceView.Renderer renderer) {
            glSurfaceView.setRenderer(renderer);
            renderHasBeenSet=true;
        }
        protected void setEGLContextClientVersion(int version) {
            glSurfaceView.setEGLContextClientVersion(version);
        }
        protected void setPreserveEGLContextOnPause(boolean preserve) {
            glSurfaceView.setPreserveEGLContextOnPause(preserve);
        }
        protected void setEGLConfigChooser(GLSurfaceView.EGLConfigChooser configChooser) {
            glSurfaceView.setEGLConfigChooser(configChooser);
        }
    }

    class MyRenderer implements GLSurfaceView.Renderer {


        public void onCreate(Context context) {
        }

        public void onPause() {
        }

        public void onDestroy() {
        }

        public void onResume() {
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            Graphic.init(getBaseContext());
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            world.setSurface(width, height);
        }

        public void onDrawFrame(GL10 gl) {
            world.updateAndDraw();
        }
    }
}
