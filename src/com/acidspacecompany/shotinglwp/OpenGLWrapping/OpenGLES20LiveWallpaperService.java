package com.acidspacecompany.shotinglwp.OpenGLWrapping;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public abstract class OpenGLES20LiveWallpaperService extends WallpaperService {
    public class GLEngine extends Engine {
        class MyGLSurfaceView extends GLSurfaceView {

            private LifecycleRenderer renderer;

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
                        renderer.onDestroy();
                    }
                });
                super.onDetachedFromWindow();
            }

            @Override
            public void onPause() {
                super.onPause();
                renderer.onPause();
            }

            @Override
            public void onResume() {
                super.onResume();
                //todo: if (renderer == null)  BicycleDebugger.e("GLSurfaceView", "Renderer hasn't been set up.");
                renderer.onResume();
            }

            public void setRenderer(LifecycleRenderer renderer) {
                super.setRenderer(renderer);
                this.renderer = renderer;
                this.renderer.onCreate(getContext());
            }
        }

        private MyGLSurfaceView glSurfaceView;
        private boolean renderHasBeenSet;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);
            glSurfaceView = new MyGLSurfaceView(OpenGLES20LiveWallpaperService.this);

            setEGLContextClientVersion(2);
            setPreserveEGLContextOnPause(true);
            setEGLConfigChooser(new MultisampleConfigChooser());
            setRenderer(getRenderer());
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

        protected void setRenderer(LifecycleRenderer renderer) {
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

    protected abstract LifecycleRenderer getRenderer();
}
