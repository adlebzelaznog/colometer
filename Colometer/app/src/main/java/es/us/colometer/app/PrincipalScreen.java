package es.us.colometer.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.io.IOException;

import es.us.colometer.app.camera.CameraManager;
import es.us.colometer.app.Color.ColorFormats;
import es.us.colometer.app.Color.ColorModelConverter;


public class PrincipalScreen extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback {

    // Attributes ----------------------------------------------------------------------------------
    Camera camera;
    SurfaceView camPreview;
    FrameLayout previewLayout; //Layout where camera preview will be displayed
    private Bitmap currentImage;


    // Activity life-cycle methods -----------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("onCreate -------------------------------------------------------------------");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_welcome);

        // Set camera preview layout
        camera = CameraManager.getCameraInstance();

        // Create a CameraPreview and include it on our activity
        camPreview = new SurfaceView(this);

        previewLayout = (FrameLayout) findViewById(R.id.camera_preview);
        previewLayout.addView(camPreview);

        camera.setPreviewCallback(this);
        camPreview.getHolder().addCallback(this);

        // Set options layout on click listener
        RelativeLayout controlsLayout = (RelativeLayout) findViewById(R.id.controls_layout);
        controlsLayout.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                                  navigateSettingsMenu();
                                              }
                                          }
        );

        // Estaba en el onSurfaceCreated
        camera.setPreviewCallback(this);
        camera.getParameters().setPreviewSize(480, 640);
        camera.startPreview();
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            camera.reconnect();
        } catch (IOException oops) {
            Log.e("ERROR", "error trying to reconnect the camera", oops);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        camera.unlock();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        camera.release();
        camera = null;
    }

    // Ancillary methods ---------------------------------------------------------------------------
    private void navigateSettingsMenu() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }


    // Camera Callbacks
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        // lo que sea
        // Sacas el bitmap
        // Lees el color o lo que tengas que hacer
        // Y desde este método le pasas los datos al TextView que tengas en la activity,
        // o lo que sea que hagas para mostrar el color.

        /*
        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        int size = previewSize.height * previewSize.width;

        if(currentImage!=null) currentImage.recycle();
        currentImage = BitmapFactory.decodeByteArray(data, 0, size);
        */

        // By default preview data is in NV21 format, if needed it must be converted
        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        ColorModelConverter converter = new ColorModelConverter(previewSize.height, previewSize.width);
        int[] pixels = converter.convert(data, ColorFormats.RGB);


    }


    // SurfaceHolder Callbacks

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        Log.d("David", "surfaceCreated");
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("David", "surfaceChanged");
        // no sé de qué va esto, pero lo copio
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        if (holder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();

        } catch (Exception e) {
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // nose
        Log.d("David", "surfaceDestroyed");
    }




}
