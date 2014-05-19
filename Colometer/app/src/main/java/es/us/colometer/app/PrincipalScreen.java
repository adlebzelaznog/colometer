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
import android.widget.TextView;

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
        Log.d("APP LIFECYCLE","Principal screen onCreate");
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
        Log.d("APP LIFECYCLE","Principal screen onResume");
        super.onResume();

        try {
            camera.reconnect();
        } catch (IOException oops) {
            Log.e("ERROR", "error trying to reconnect the camera", oops);
        }

    }

    @Override
    public void onStop() {
        Log.d("APP LIFECYCLE","Principal screen onStop");
        super.onStop();
        camera.unlock();
    }

    @Override
    public void onDestroy() {
        Log.d("APP LIFECYCLE","Principal screen onDestroy");
        super.onDestroy();

        camera.release();
        Log.d("DEBUG","Camera released!");
        camera = null;
    }

    // Ancillary methods ---------------------------------------------------------------------------
    private void navigateSettingsMenu() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }


    // Camera Callbacks ----------------------------------------------------------------------------
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        // By default preview data is in NV21 format, if needed it must be converted
        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        int height = previewSize.height;
        int width = previewSize.width;

        ColorModelConverter converter = new ColorModelConverter(height, width);
        // TODO: only convert data if color model is not NV21
        int[] pixels = converter.convert(data, ColorFormats.RGB);

        int color = pickColor(pixels, height, width);
        updateColorData(color);
    }


    // SurfaceHolder Callbacks ---------------------------------------------------------------------
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
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
        // nothing to do
    }

    // Color picker methods ------------------------------------------------------------------------
    private int pickColor (int[] pixels, int height, int width){
        //TODO: get scope radius from user preferences
        int res;

        int radius = 2;
        int total = 0;  // Total of picked pixels
        int sum = 0;    // Sum of picked pixels
        int centerX = height/2;
        int centerY = width/2;

        for(int i = centerX-radius; i <= centerX+radius; i++){
            for(int j = centerY-radius; j <= centerY+radius; j++){
                if( Math.abs((i-centerX)+(j-centerY)) <= radius){
                    total++;
                    sum += pixels[ i + j*width ];
                }

            }
        }

        res = sum/total;

        return res;
    }

    /**
     * Update color name, color value and color displayed into the layout
     * */
    private void updateColorData(int color){
        // Update color value
        TextView colorValue = (TextView) findViewById(R.id.colorValue);
        // TODO: get color model from shared preferences
        String colorModel = "RGB";
        colorValue.setText(colorModel+"\n#"+String.format("%x",color));

        // Update color
        FrameLayout colorSample = (FrameLayout) findViewById(R.id.colorSample);
        colorSample.setBackgroundColor(color);
    }


}
