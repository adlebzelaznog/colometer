package es.us.colometer.app;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.widget.FrameLayout;

import java.io.IOException;

import es.us.colometer.app.camera.CameraManager;
import es.us.colometer.app.camera.CameraPreview;


public class PrincipalScreen extends Activity{
    //TODO:
    // - Move camera initialization ops to onResume method. Only surface initialization must remain into onCreate method.
    // - Move camera release to onStop method.
    // - Add all neccessary processing from https://github.com/commonsguy/cw-advandroid/blob/master/Camera/Picture/src/com/commonsware/android/picture/PictureDemo.java

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

        // Get a camera instance
        // TODO: try-catch to get the camera instance and, if cameraNotAvailableException is thrown, fill camera_preview layout with an informational message
        camera = CameraManager.getCameraInstance();

        // Create a CameraPreview and include it on our activity
        camPreview = new CameraPreview(this, camera);
        previewLayout = (FrameLayout) findViewById(R.id.camera_preview);
        previewLayout.addView(camPreview);
    }

    @Override
    public void onResume(){
        super.onResume();

        System.out.println("onResume -------------------------------------------------------------------");
        try{
            camera.reconnect();
        }
        catch(IOException oops){
            /*TODO: show a message to the user telling him/her that the camera is locked by other process
                and it must be killed in order to use colometer.*/
            System.out.println("IOException on the onResume method!");
        }

    }

    @Override
    public void onStop(){
        super.onStop();

        System.out.println("onStop -------------------------------------------------------------------");
        camera.unlock();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        System.out.println("onDestroy -------------------------------------------------------------------");
        camera.release();
        camera = null;
    }

    // Ancillary methods ---------------------------------------------------------------------------
    private void goNoCameraActivity(){
        //TODO: show into previewLayout a message telling him that this app must be
        //runned on a device with a camera, then closes the app.
    }
}
