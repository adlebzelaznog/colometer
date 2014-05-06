package es.us.colometer.app.camera;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import es.us.colometer.app.exceptions.CameraNotAvailableException;

/** A basic Camera preview surface */
public class CameraPreview extends SurfaceView{
    // Attributes -----------------------------------------------------------------------------------
    PreviewSurfaceHolder surfaceHolder;

    // Constructor ---------------------------------------------------------------------------------
    public CameraPreview(Context context, Camera camera) {
        super(context);

        surfaceHolder = new PreviewSurfaceHolder(this, camera);
    }

}
