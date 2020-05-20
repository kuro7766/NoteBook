package com.example.notebook.drawableview.draw;

import android.graphics.Path;

import java.io.Serializable;
import java.util.ArrayList;

//this class can be draw directly through canvas
public class SerializablePath extends Path implements Serializable {

    public static final long serialVersionUID=8683452586662892189L;

    //index 0 and 1 are x and y points
    private ArrayList<float[]> pathPoints;
    private int color;
    private float width;

    public SerializablePath() {
        super();
        pathPoints = new ArrayList<>();
    }

    public SerializablePath(SerializablePath p) {
        super(p);
        pathPoints = p.pathPoints;
    }

    public void addPathPoints(float[] points) {
        this.pathPoints.add(points);
    }

    public void saveMoveTo(float x, float y) {
        super.moveTo(x, y);
        addPathPoints(new float[]{x, y});
    }

    public void saveLineTo(float x, float y) {
        super.lineTo(x, y);
        addPathPoints(new float[]{x, y});
    }

    public void saveReset() {
        super.reset();
        pathPoints.clear();
    }

    public void savePoint() {
//        if (pathPoints.size() > 0) {
//            float[] points = pathPoints.get(0);
//            saveLineTo(points[0] + 1, points[1] + 1);
//        }
    }

    @Override
    public String toString() {
        return pathPoints.toString();
    }

    //build path
    public void loadPathPointsAsCanvasPath() {
        if(pathPoints.size()==0){
            return;
        }
        float[] initPoints = pathPoints.get(0);
        this.moveTo(initPoints[0], initPoints[1]);
        for (int j = 1; j < pathPoints.size(); j++) {
            float[] pointSet = pathPoints.get(j);
            this.lineTo(pointSet[0], pointSet[1]);
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }
}
