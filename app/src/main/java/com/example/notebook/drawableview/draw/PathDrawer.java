package com.example.notebook.drawableview.draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

public class PathDrawer {

    private Paint gesturePaint;

    public PathDrawer() {
        initGesturePaint();
    }

    public void onDraw(Canvas canvas, SerializablePath currentDrawingPath, List<SerializablePath> paths) {
        drawGestures(canvas, paths);
        if (currentDrawingPath != null) {
            drawPath(canvas, currentDrawingPath);
        }
    }

    // points wrapped in two lists are all what you see in canvas( or on your screen )
    public void drawGestures(Canvas canvas, List<SerializablePath> paths) {
        for (SerializablePath path : paths) {
            drawPath(canvas, path);
        }
    }

    // draw on bitmap
    public Bitmap drawPathsOnBitmap(Bitmap createdBitmap, List<SerializablePath> paths) {
        Canvas composeCanvas = new Canvas(createdBitmap);
        drawGestures(composeCanvas, paths);
        return createdBitmap;
    }

    private void drawPath(Canvas canvas, SerializablePath path) {
        gesturePaint.setStrokeWidth(path.getWidth());
        gesturePaint.setColor(path.getColor());
//        gesturePaint.setColor(Color.WHITE);
        canvas.drawPath(path, gesturePaint);
    }

    private void initGesturePaint() {
        gesturePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
        gesturePaint.setStyle(Paint.Style.STROKE);

        //really important , if you want your signature smooth , you must set this feature
        gesturePaint.setStrokeJoin(Paint.Join.ROUND);
        gesturePaint.setStrokeCap(Paint.Cap.ROUND);
    }
}
