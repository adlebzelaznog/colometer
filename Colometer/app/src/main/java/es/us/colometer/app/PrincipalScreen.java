package es.us.colometer.app;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.widget.FrameLayout;

import es.us.colometer.app.camera.CameraManager;
import es.us.colometer.app.camera.CameraPreview;


public class PrincipalScreen extends Activity{

    // Attributes ----------------------------------------------------------------------------------
    Camera camera;
    CameraPreview camPreview;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_welcome);

        // Get a camera instance
        camera = CameraManager.getCameraInstance();

        // Create a CameraPreview and include it on our activity
        camPreview = new CameraPreview(this, camera);
        FrameLayout previewLayout = (FrameLayout) findViewById(R.id.camera_preview);
        previewLayout.addView(camPreview);
    }
}
