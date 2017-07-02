package in.smitshilu.videoloop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.TimeZone;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ask for permissino if not granted
        if (checkPermission()) {
            Intent intent = new Intent(this, PermissionActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_fullscreen);
        final VideoView videoView = (VideoView) findViewById(R.id.videoView);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/video.mp4";
        videoView.setVideoPath(path);
        videoView.setMediaController(new MediaController(this));
        videoView.start();

        // Repeat video again after completion
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    Toast.makeText(getApplicationContext(), getTime(), Toast.LENGTH_LONG).show();
                    videoView.start();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Sorry couldn't play it again", Toast.LENGTH_LONG).show();
                    Log.e("Play Again", "Error: " + e);
                }
            }
        });

    }

    // Check for storage access permission
    protected boolean checkPermission() {
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED)
            return true;

        return false;
    }

    // Get UTC time from API ("http://tycho.usno.navy.mil/timer.html")
    protected String getTime() {
        final StringBuilder time = new StringBuilder();
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://tycho.usno.navy.mil/timer.html");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    if(httpURLConnection.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        String temp = "";
                        while ((temp = br.readLine()) != null) {
                            if(temp.contains("UTC")) {
                                time.append(temp.substring(temp.indexOf(',')+2, temp.indexOf(',')+14));
                            }
                        }
                    }
                }
                catch (Exception e) {
                    Log.e("connection", "Error: " + e);
                    Toast.makeText(getApplicationContext(), "Error fetching time", Toast.LENGTH_LONG).show();
                }
            }
        };
        t.start();
        try {
            t.join();
        } catch (Exception e) {
            Log.e("thread", "Error: " + e);
            Toast.makeText(getApplicationContext(), "Error Joining Thread", Toast.LENGTH_LONG).show();
        }
        return time.toString().trim();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
