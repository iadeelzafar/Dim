package com.example.dim;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import static com.example.dim.Constants.KEY_DIM;
import static com.example.dim.Constants.TAG_STOP;

public class MainActivity extends AppCompatActivity {

  private SuperPrefs superPrefs;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    superPrefs = SuperPrefs.newInstance(this);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (!PermissionChecker.checkDrawOverlayPermission(this))
        permissionCheck(this);
      else {
        superPrefs.setBoolNot(KEY_DIM);
        // equals to true means we need to turn on the dim light
        if (superPrefs.getBool(KEY_DIM).equals(true)) {
          startService(new Intent(getApplicationContext(), ScreenDimmer.class));
        } else {
          Intent intent = new Intent(getApplicationContext(), ScreenDimmer.class);
          intent.setAction(TAG_STOP);
          startService(intent);
        }

      }
    } else {
      superPrefs.setBoolNot(KEY_DIM);
      if (superPrefs.getBool(KEY_DIM).equals(true)) {
        startService(new Intent(getApplicationContext(), ScreenDimmer.class));
      } else {
        //fabDim.setImageResource(R.drawable.anim_cross_tick);
        stopServiceIntent();
      }
      //animate(view);
    }
  }

  private void stopServiceIntent() {
    Intent intent = new Intent(getApplicationContext(), ScreenDimmer.class);
    intent.setAction(TAG_STOP);
    startService(intent);
  }

  private void permissionCheck(final Activity activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (!PermissionChecker.checkDrawOverlayPermission(activity)) {
        new AlertDialog.Builder(activity)
            .setTitle("Permission Request")
            .setMessage("To use this application Draw Overlay Permission is required")
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                PermissionChecker.requestDrawOverlayPermission(activity);
              }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
              }
            })
            .show();
      }
    }
  }
}
