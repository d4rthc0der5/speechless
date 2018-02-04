package com.sdsmdg.vishwas.speechless;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    private String TAG = "Camera Activity";

    private Camera mCamera;
    private CameraPreview mPreview;

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.e(TAG, "Picture taken and destroyed");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if (!checkCameraHardware(CameraActivity.this)) {
            Toast.makeText(this, "This device doesn't have camera!!", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera);

        FrameLayout preview = findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        // Add a listener to the Capture button
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);
                        try {
                            mCamera.reconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    // detecting the camera hardware, can be ignored as
    // camera hardware requirement is already specified in manifest
    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mCamera.release();
    }
}
