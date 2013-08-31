package com.chewielouie.textadventure_common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

// Based on answer to SO question - http://stackoverflow.com/questions/10630373/android-image-view-pinch-zooming
public class ZoomableImageView extends ImageView implements OnTouchListener {

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    static final float minimumFingerDistanceToTriggerZoom = 5f;

    private Matrix matrix;
    private Matrix savedMatrix;
    private int mode;
    private PointF startPoint;
    private PointF middlePoint;
    private float oldDist;
    private int viewWidth = -1;
    private int viewHeight = -1;
    private Bitmap ourBitmap = null;

    public ZoomableImageView(Context context) {
        this(context, null, 0);
        resetState();
    }

    private void resetState() {
        matrix = new Matrix();
        savedMatrix = new Matrix();
        mode = NONE;
        startPoint = new PointF();
        middlePoint = new PointF();
        oldDist = 1f;
    }

    public ZoomableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        resetState();
    }

    public ZoomableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setOnTouchListener(this);
        resetState();
    }

    @Override
    public void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        centerBitmap();
    }

    public void setBitmap(Bitmap bitmap) {
        if(bitmap != null) {
            setImageBitmap( bitmap );
            this.ourBitmap = bitmap;
            resetState();
            centerBitmap();
        }
    }

    private void centerBitmap() {
        if( haveValidViewSize() && ourBitmap != null ) {
            Point bitmapMiddlePoint = new Point();
            bitmapMiddlePoint.x = (viewWidth / 2) - (ourBitmap.getWidth() /  2);
            bitmapMiddlePoint.y = (viewHeight / 2) - (ourBitmap.getHeight() / 2);
            matrix.postTranslate(bitmapMiddlePoint.x, bitmapMiddlePoint.y);
            this.setImageMatrix(matrix);
        }
    }

    private boolean haveValidViewSize() {
        return viewWidth != -1 && viewHeight != -1;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                startPoint.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = distanceBetweenFingers(event);
                if(oldDist > minimumFingerDistanceToTriggerZoom) {
                    savedMatrix.set(matrix);
                    midPointBetweenFingers(middlePoint, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if(mode == DRAG)
                    drag(event);
                else if(mode == ZOOM)
                    zoom(event);
                break;
        }
        return true;
    }

    private void drag( MotionEvent event ) {
        matrix.set(savedMatrix);
        matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);
        this.setImageMatrix(matrix);
    }

    private void zoom( MotionEvent event ) {
        float newDist = distanceBetweenFingers(event);
        float scale = newDist / oldDist;
        matrix.set(savedMatrix);
        matrix.postScale(scale, scale, middlePoint.x, middlePoint.y);
        this.setImageMatrix(matrix);
    }

    private float distanceBetweenFingers(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    private void midPointBetweenFingers(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
}
