package es.us.colometer.app.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Camera focus drawable
 */
public class CameraFocus extends View {

    public CameraFocus(Context context){
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        int x = getWidth();
        int y = getHeight();
        // TODO: get radius from shared preferences
        int radius = 20;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(Color.WHITE);

        canvas.drawCircle(x/2, y/2, radius, paint);
    }
}
