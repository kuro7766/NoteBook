package com.example.notebook.drawableview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.example.notebook.Util.TCallBack;
import com.example.notebook.drawableview.draw.CanvasDrawer;
import com.example.notebook.drawableview.draw.PathDrawer;
import com.example.notebook.drawableview.draw.SerializablePath;
import com.example.notebook.drawableview.gestures.creator.GestureCreator;
import com.example.notebook.drawableview.gestures.creator.GestureCreatorListener;
import com.example.notebook.drawableview.gestures.scale.GestureScaler;
import com.example.notebook.drawableview.gestures.scroller.GestureScroller;

import java.util.ArrayList;
import java.util.List;

public class DrawableView extends View
        implements View.OnTouchListener, GestureCreatorListener {

    //drawing path , important, restore all path in this list
    //inside this list is another list
    //the outside is your drawings in form of lines
    //the inner is points list in your line
    private final ArrayList<SerializablePath> paths = new ArrayList<>();

    //to scroll and scale , I don't need that
    private GestureScroller gestureScroller;
    private GestureScaler gestureScaler;


    private GestureCreator gestureCreator;

    /**
     * default should be instantiated as screen height and width
     */
    private int canvasHeight = Integer.MAX_VALUE;
    private int canvasWidth = Integer.MAX_VALUE;


    //    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private PathDrawer pathDrawer;

    //this class is to handle offset and scale by change canvas's object
    //configurations ,apart from other objects
    //this determines whether draw all paths or not
    private CanvasDrawer canvasDrawer;

    //not add to paths list yet, store it to draw it separately
    private SerializablePath currentDrawingPath;

    //onFinger up
    private TCallBack<ArrayList<SerializablePath>> onFingerUpListener;

    public DrawableView setOnFingerUpListener(TCallBack<ArrayList<SerializablePath>> onFingerUpListener) {
        this.onFingerUpListener = onFingerUpListener;
        return this;
    }

    public void setConfig(DrawableViewConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Paint configuration cannot be null");
        }
        gestureCreator.setConfig(config);
//        gestureScaler.setZooms(config.getMinZoom(), config.getMaxZoom());
//        gestureScroller.setCanvasBounds(canvasWidth, canvasHeight);
        canvasDrawer.setConfig(config);
    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        gestureScroller.setViewBounds(w, h);
//    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        scaleGestureDetector.onTouchEvent(event);
//        gestureDetector.onTouchEvent(event);
        gestureCreator.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP)
            if (onFingerUpListener != null) onFingerUpListener.call(paths);
        Log.d("ssss", "onTouch: "+paths.size());
        invalidate();
        return true;
    }

    public void undo() {
        if (paths.size() > 0) {
            paths.remove(paths.size() - 1);
            invalidate();
        }
    }


    /**
     * this is corresponding to pointer up action
     *
     * @param serializablePath new drawing path
     */
    @Override
    public void onNewPathCreated(SerializablePath serializablePath) {
        paths.add(serializablePath);
    }

    /**
     * corresponding to action move
     *
     * @param currentDrawingPath I give you its handler and you can get further changes of this
     *                           object
     */
    @Override
    public void onCurrentGestureChanged(SerializablePath currentDrawingPath) {
        //make sure that your real-time finger print are shown on your screen
        this.currentDrawingPath = currentDrawingPath;
    }


//
//    @Override
//    public void onViewPortChange(RectF currentViewport) {
//        gestureCreator.onViewPortChange(currentViewport);
//        canvasDrawer.onViewPortChange(currentViewport);
//    }
//
//    @Override
//    public void onCanvasChanged(RectF canvasRect) {
//        gestureCreator.onCanvasChanged(canvasRect);
//        canvasDrawer.onCanvasChanged(canvasRect);
//    }
//
//
//    @Override
//    public void onScaleChange(float scaleFactor) {
//        gestureScroller.onScaleChange(scaleFactor);
//        gestureCreator.onScaleChange(scaleFactor);
//        canvasDrawer.onScaleChange(scaleFactor);
//    }


    /*
     * some simple methods are ensured right
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        canvasDrawer.onDraw(canvas);
        pathDrawer.onDraw(canvas, currentDrawingPath, paths);
    }

    public void clear() {
        paths.clear();
        invalidate();
    }

    public Bitmap obtainBitmap(Bitmap emptyBitmap) {
        return pathDrawer.drawPathsOnBitmap(emptyBitmap, paths);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        DrawableViewSaveState state = new DrawableViewSaveState(super.onSaveInstanceState());
        state.setPaths(paths);
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof DrawableViewSaveState)) {
            super.onRestoreInstanceState(state);
        } else {
            DrawableViewSaveState ss = (DrawableViewSaveState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            paths.addAll(ss.getPaths());
        }
    }

    public void setPaths(List<SerializablePath> serializablePaths) {
        paths.clear();
        paths.addAll(serializablePaths);
        currentDrawingPath=null;
        invalidate();
    }

    public List<SerializablePath> getSerializablePaths() {
        return paths;
    }

    /*
     * init
     */

    public DrawableView(Context context) {
        super(context);
        init();
    }

    public DrawableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DrawableView(Context context, AttributeSet attrs,
                        int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
//        gestureScroller = new GestureScroller(this);
//        gestureDetector = new GestureDetector(getContext(), new GestureScrollListener(gestureScroller));
//        gestureScaler = new GestureScaler(this);
//        scaleGestureDetector =
//                new ScaleGestureDetector(getContext(), new GestureScaleListener(gestureScaler));
        gestureCreator = new GestureCreator(this);
        pathDrawer = new PathDrawer();
        canvasDrawer = new CanvasDrawer();
        setOnTouchListener(this);
    }

    /*
     * init
     */
}
