package guangbin79.github.io.myapplication;

import android.app.Activity;
import android.app.Application;
import android.app.KeyguardManager;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.view.Display;

import java.util.Timer;
import java.util.TimerTask;

public class BackgroundNotify {
    private Application.ActivityLifecycleCallbacks callback;
    private Timer timer;
    private Application app;
    private Listener lsn;
    private boolean lockscreen, background;

    interface Listener {
        void onForeground();
        void onBackground();
        void onLockscreen();
        void onUnlockscreen();
    }

    public BackgroundNotify(Application application) {
        assert (application != null);

        app = application;

        callback = new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }

                if (lockscreen) {
                    lockscreen = false;
                    lsn.onUnlockscreen();
                } else if (background) {
                    background = false;
                    lsn.onForeground();
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        KeyguardManager manager = (KeyguardManager) app.getSystemService(app.KEYGUARD_SERVICE);
                        Display display = ((DisplayManager) app.getSystemService(app.DISPLAY_SERVICE)).getDisplay(Display.DEFAULT_DISPLAY);

                        if (manager.isKeyguardLocked() || display.getState() == 1) {
                            lockscreen = true;
                            lsn.onLockscreen();
                        } else {
                            background = true;
                            lsn.onBackground();
                        }
                    }
                }, 500);
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        };
    }

    public void start(final Listener listener) {
        assert(listener != null);

        if (lsn == null) {
            lsn = listener;
            app.registerActivityLifecycleCallbacks(callback);
        }
    }

    public void stop() {
        lsn = null;
        app.unregisterActivityLifecycleCallbacks(callback);
    }
}
