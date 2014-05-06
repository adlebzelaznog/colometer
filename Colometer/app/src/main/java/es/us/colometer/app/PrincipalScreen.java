package es.us.colometer.app;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.io.IOException;

import es.us.colometer.app.camera.CameraManager;
import es.us.colometer.app.camera.CameraPreview;


public class PrincipalScreen extends Activity{

    // Attributes ----------------------------------------------------------------------------------
    Camera camera;
    CameraPreview camPreview;
    FrameLayout previewLayout; //Layout where camera preview will be displayed

    // Activity life-cycle methods -----------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState){
        System.out.println("onCreate -------------------------------------------------------------------");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_welcome);

        // Set camera preview layout
        camera = CameraManager.getCameraInstance();

        // Create a CameraPreview and include it on our activity
        camPreview = new CameraPreview(this, camera);
        previewLayout = (FrameLayout) findViewById(R.id.camera_preview);
        previewLayout.addView(camPreview);


        // Set options layout on click listener
        RelativeLayout controlsLayout = (RelativeLayout) findViewById(R.id.controls_layout);
        controlsLayout.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigateSettingsMenu();
                }
            }
        );
    }

    @Override
    public void onResume(){
        super.onResume();

        try{
            camera.reconnect();
        }
        catch(IOException oops){
            Log.e("ERROR","error trying to reconnect the camera",oops);
        }

    }

    @Override
    public void onStop(){
        super.onStop();

        camera.unlock();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        camera.release();
        camera = null;
    }

    // Ancillary methods ---------------------------------------------------------------------------
    private void navigateSettingsMenu(){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}
