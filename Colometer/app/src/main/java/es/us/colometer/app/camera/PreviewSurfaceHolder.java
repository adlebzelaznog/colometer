package es.us.colometer.app.camera;

import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import es.us.colometer.app.exceptions.CameraNotAvailableException;

/* Holder for the camera preview surface.
* This holder allow us to control the images drawn into the surface.*/
public class PreviewSurfaceHolder  implements SurfaceHolder.Callback {
    // Attributes ----------------------------------------------------------------------------------
    private SurfaceHolder holder;
    private Camera camera;


    // Constructor ---------------------------------------------------------------------------------
    public PreviewSurfaceHolder(SurfaceView previewSurface, Camera camera){
        this.camera = camera;

        holder = previewSurface.getHolder();
        holder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    // Callback functionality methods --------------------------------------------------------------
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            camera.setPreviewDisplay(holder);
            camera.getParameters().setPreviewSize(480,640);
            camera.startPreview();
        } catch (Exception e) {
            throw new CameraNotAvailableException(e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (holder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();

        }
        catch (Exception e){}
    }
}
