package es.us.colometer.app.camera;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

import es.us.colometer.app.exceptions.CameraNotAvailableException;

/**
 * Class that gives a camera instance in a secure way.
 */
public class CameraManager {
    //TODO: this class must provide a singleton object.

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            throw new CameraNotAvailableException("camera could not be opened, " +
                    "maybe it's being used by other process or it's disabled by the device policy manager");
        }
        return c; // returns null if camera is unavailable
    }


    // Ancillary methods ---------------------------------------------------------------------------
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }



}

