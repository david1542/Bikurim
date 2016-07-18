package bikurim.silverfix.com.bikurim;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import bikurim.silverfix.com.bikurim.utils.managers.MediaPlayerManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing the sound files
        MediaPlayerManager.initiate(getApplicationContext());

        final Intent intent = new Intent(this, ListActivity.class);
        int TIME_SECONDS = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, TIME_SECONDS);
    }
}
