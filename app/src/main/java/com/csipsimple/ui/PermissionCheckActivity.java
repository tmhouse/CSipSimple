package com.csipsimple.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


/**
 *
 */
public class PermissionCheckActivity extends AppCompatActivity {
    private static final String[] permissions = {
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.PROCESS_OUTGOING_CALLS,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.WRITE_CALL_LOG,
        Manifest.permission.USE_SIP,
        "android.permission.CONFIGURE_SIP",
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
    };
    private static Class nextActivityClass = SipHome.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if( checkPermission() ) {
            startNextActivity();
        }
    }

    private void startNextActivity() {
        Intent it = new Intent();
        it.setClass(this, nextActivityClass);
        startActivity(it);

        finish();
    }

    private boolean checkPermission() {
        if( Build.VERSION.SDK_INT >= 23 ) {
            for( String p : permissions ) {
                if( ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED ) {
                    // パーミッションをリクエストする
                    ActivityCompat.requestPermissions(this, permissions, 1000);
                    return false;
                }
            }
        }
        return true;
    }

    // 結果の受け取り
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            if ( grantResults != null && grantResults.length > 1  ) {
                for( int result : grantResults ) {
                    if( result != PackageManager.PERMISSION_GRANTED) {
                        // NG
                        finish();
                        return;
                    }
                }
                // OK
                startNextActivity();
            } else {
                throw new RuntimeException("why no results?");
            }
        }
    }
}
