package es.us.colometer.app;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import es.us.colometer.app.exceptions.CameraNotAvailableException;

/**
 * Class that performs all camera related operations into a thread separated
 * from the GUI thread (the principal thread).
 * */
public class CameraThread extends Thread {

    // Attributes ----------------------------------------------------------------------------------------------------------
    private int currentCamera;
    private int numberOfCameras;

    // Constructor ---------------------------------------------------------------------------------------------------------
    public CameraThread(){
        this.currentCamera = -1;
        this.numberOfCameras = Camera.getNumberOfCameras();
    }

    // Thread methods ------------------------------------------------------------------------------------------------------
    @Override
    public void run(){

    }

    // Application logic methods -------------------------------------------------------------------------------------------
    // Camera start methods ------------------------------------------------------------------------------------------------
    /**
     * Get an instance of the principal camera so we can work with it.
     *
     * throws CameraNotAvailableException when the camera doesn't exists
     * or it's unavailable at the moment.
     * */
    public Camera getPpalCameraInstance(){
        Camera c = null;

        currentCamera = 0;
        c = getCameraInstance(currentCamera);

        return c;
    }

    /**
     * Switches to other available cameras
     *
     * throws CameraNotAvailableException when the camera with the specified Id doesn't exists
     * or it's unavailable at the moment.
     * */
    public Camera switchCamera(){
        Camera res = null;
        int nextCamera = currentCamera;

        if (currentCamera == numberOfCameras-1)
            nextCamera = 0;

        res = getCameraInstance(nextCamera);
        currentCamera = nextCamera; // Next camera was available so it is the current camera now. currentCamera id must be updated too.

        return res;
    }

    // Ancillary methods -----------------------------------------------------------------------------------------------------
    /**
     * Check if the device executing the app has a camera
     * */
    private boolean checkCameraHardware(Context context){
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get the camera associated to the specified id.
     *
     * throws CameraNotAvailableException when the camera with the specified Id doesn't exists
     * or it's unavailable at the moment.
     * */
    private Camera getCameraInstance(int cameraId){
        Camera c = null;
        try {
            c = Camera.open(cameraId);
        }
        catch (Exception e){
            throw new CameraNotAvailableException(e.getMessage());
        }
        return c;
    }
}
