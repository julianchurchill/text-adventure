package com.chewielouie.textadventure_common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

// Based on answer to SO question - http://stackoverflow.com/questions/10630373/android-image-view-pinch-zooming
public class ZoomableImageView extends ImageView implements OnTouchListener {

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    static final float minimumFingerDistanceToTriggerZoom = 10f;

    private Matrix matrix;
    private Matrix savedMatrix;
    private int mode;
    private PointF mStartPoint;
    private PointF mMiddlePoint;
    private float oldDist;
    private float oldEventX;
    private float oldEventY;
    private float oldStartPointX;
    private float oldStartPointY;
    private boolean mDraggable;
    private int mViewWidth = -1;
    private int mViewHeight = -1;
    private int mBitmapWidth = -1;
    private int mBitmapHeight = -1;

    public ZoomableImageView(Context context) {
        this(context, null, 0);
        resetState();
    }

    private void resetState() {
        matrix = new Matrix();
        savedMatrix = new Matrix();
        mode = NONE;
        mStartPoint = new PointF();
        mMiddlePoint = new PointF();
        oldDist = 1f;
        oldEventX = 0;
        oldEventY = 0;
        oldStartPointX = 0;
        oldStartPointY = 0;
        mDraggable = false;
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
        mViewWidth = w;
        mViewHeight = h;
        centerBitmap();
        System.out.println("ZoomableImageView.onSizeChanged() mViewWidth = " + mViewWidth + ", mViewHeight = " + mViewHeight );
    }

    public void setBitmap(Bitmap bitmap) {
        if(bitmap != null) {
            setImageBitmap(bitmap);
            mBitmapWidth = bitmap.getWidth();
            mBitmapHeight = bitmap.getHeight();
            resetState();
            centerBitmap();
        }
    }

    private void centerBitmap() {
        if( haveValidViewSize() ) {
            Point bitmapMiddlePoint = new Point();
            bitmapMiddlePoint.x = (mViewWidth / 2) - (mBitmapWidth /  2);
            bitmapMiddlePoint.y = (mViewHeight / 2) - (mBitmapHeight / 2);

            System.out.println("ZoomableImageView.setBitmap() centering image by translating dx,dy = '" + bitmapMiddlePoint.x + "," + bitmapMiddlePoint.y + "'" );
            System.out.println("ZoomableImageView.setBitmap() mViewWidth = " + mViewWidth + ", mViewHeight = " + mViewHeight );
            matrix.postTranslate(bitmapMiddlePoint.x, bitmapMiddlePoint.y);

            // For testing drag - zoom in
            // float scale = 1.5f;
            // matrix.postScale(scale, scale, bitmapMiddlePoint.x, bitmapMiddlePoint.y);
            // also make draggable for testing
            // Does this code assume that the initial image fits in the view? If so then mDraggable must be set
            // based on whether this is true!
            // mDraggable = true;

            this.setImageMatrix(matrix);
        }
    }

    private boolean haveValidViewSize() {
        return mViewWidth != -1 && mViewHeight != -1;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                mStartPoint.set(event.getX(), event.getY());
                mode = DRAG;
                System.out.println("ZoomableImageView.onTouch() ACTION_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = distanceBetweenFingers(event);
                if(oldDist > minimumFingerDistanceToTriggerZoom) {
                    savedMatrix.set(matrix);
                    midPointBetweenFingers(mMiddlePoint, event);
                    mode = ZOOM;
                }
                System.out.println("ZoomableImageView.onTouch() ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("ZoomableImageView.onTouch() ACTION_UP");
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                System.out.println("ZoomableImageView.onTouch() ACTION_POINTER_UP");
                break;
            case MotionEvent.ACTION_MOVE:
                if(mode == DRAG)
                    drag(event);
                else if(mode == ZOOM)
                    zoom(event);
                System.out.println("ZoomableImageView.onTouch() ACTION_MOVE");
                break;
        }
        return true;
    }

    private void drag(MotionEvent event) {
        float matrixValues[] = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
        matrix.getValues(matrixValues);

        float left = matrixValues[2];
        float top = matrixValues[5];
        float bottom = (top + (matrixValues[0] * mBitmapHeight)) - mViewHeight;
        float right = (left + (matrixValues[0] * mBitmapWidth)) -mViewWidth;

        float eventX = event.getX();
        float eventY = event.getY();
        float spacingX = eventX - mStartPoint.x;
        float spacingY = eventY - mStartPoint.y;
        float newPositionLeft = (left  < 0 ? spacingX : spacingX * -1) + left;
        float newPositionRight = (spacingX) + right;
        float newPositionTop = (top  < 0 ? spacingY : spacingY * -1) + top;
        float newPositionBottom = (spacingY) + bottom;
        boolean x = true;
        boolean y = true;

        if(newPositionRight < 0.0f || newPositionLeft > 0.0f) {
            if(newPositionRight < 0.0f && newPositionLeft > 0.0f)
                x = false;
            else {
                eventX = oldEventX;
                mStartPoint.x = oldStartPointX;
            }
        }
        if(newPositionBottom < 0.0f || newPositionTop > 0.0f) {
            if(newPositionBottom < 0.0f && newPositionTop > 0.0f)
                y = false;
            else {
                eventY = oldEventY;
                mStartPoint.y = oldStartPointY;
            }
        }

        if(mDraggable) {
            matrix.set(savedMatrix);
            float dx = x? eventX - mStartPoint.x : 0;
            float dy = y? eventY - mStartPoint.y : 0;
            matrix.postTranslate(dx, dy);
            this.setImageMatrix(matrix);
            if(x) oldEventX = eventX;
            if(y) oldEventY = eventY;
            if(x) oldStartPointX = mStartPoint.x;
            if(y) oldStartPointY = mStartPoint.y;

            System.out.println("ZoomableImageView.drag() dx,dy = '" + dx + "," + dy + "'" );
        }
    }

    private void zoom(MotionEvent event) {
        float matrixValues[] = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
        matrix.getValues(matrixValues);

        float newDist = distanceBetweenFingers(event);
        float scaledBitmapWidth = matrixValues[0] * mBitmapWidth;
        float scaledBitmapHeight = matrixValues[0] * mBitmapHeight;
        boolean zoomingIn = newDist > oldDist;

        // Is this the condition that stops it zooming out more than original full size?
        // What does matrixValues[0] represent?
        if( !zoomingIn && matrixValues[0] < 1 )
            return;

        if(scaledBitmapWidth > mViewWidth || scaledBitmapHeight > mViewHeight)
            mDraggable = true;
        else
            mDraggable = false;

        float midX = (mViewWidth / 2);
        float midY = (mViewHeight / 2);

        matrix.set(savedMatrix);
        float scale = newDist / oldDist;
        matrix.postScale(scale, scale, scaledBitmapWidth > mViewWidth ? mMiddlePoint.x : midX, scaledBitmapHeight > mViewHeight ? mMiddlePoint.y : midY);

        System.out.println("ZoomableImageView.zoom() matrix='" + matrix + "'" );
        this.setImageMatrix(matrix);
    }

    private float distanceBetweenFingers(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    private void midPointBetweenFingers(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
}
