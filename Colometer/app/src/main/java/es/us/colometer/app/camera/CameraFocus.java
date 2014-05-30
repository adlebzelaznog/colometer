package es.us.colometer.app.camera;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Camera focus drawable
 */
public class CameraFocus extends View {

    private int mRadius;

    public CameraFocus(Context context, int radius){
        super(context);

        mRadius = radius;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        int x = getWidth();
        int y = getHeight();
        int radius = mRadius;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(Color.WHITE);

        canvas.drawCircle(x/2, y/2, radius, paint);
    }
}
