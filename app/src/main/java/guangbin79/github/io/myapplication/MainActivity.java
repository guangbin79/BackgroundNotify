package guangbin79.github.io.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    BackgroundNotify backgroundNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backgroundNotify = new BackgroundNotify(getApplication());
    }

    public void onStartClick(View v) {
        backgroundNotify.start(new BackgroundNotify.Listener() {
            @Override
            public void onForeground() {
                Log.e("***guangbin", "foreground");
            }

            @Override
            public void onBackground() {
                Log.e("***guangbin", "background");
            }

            @Override
            public void onLockscreen() {
                Log.e("***guangbin", "lockscreen");
            }

            @Override
            public void onUnlockscreen() {
                Log.e("***guangbin", "unlockscreen");
            }
        });
    }

    public void onStopClick(View v) {
       backgroundNotify.stop();
    }
}
