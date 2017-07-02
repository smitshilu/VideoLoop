package in.smitshilu.videoloop;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class PermissionActivity extends AppCompatActivity {

    private static String TAG = "PermissionActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        // Request for Storage access
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == 0)
            startActivity(new Intent(this, FullscreenActivity.class));
        else {
            Log.e(TAG, "No Permission");
            Toast.makeText(getApplicationContext(), "Permission not granted", Toast.LENGTH_LONG).show();
        }
    }
}
