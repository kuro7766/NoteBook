package com.example.notebook.drawableview;

import java.io.Serializable;

public class DrawableViewConfig implements Serializable {

  private float strokeWidth;
  private int strokeColor;
  private int canvasWidth;
  private int canvasHeight;
  private float minZoom;
  private float maxZoom;
  private boolean showCanvasBounds;

  public float getStrokeWidth() {
    return strokeWidth;
  }

  public DrawableViewConfig setStrokeWidth(float strokeWidth) {
    this.strokeWidth = strokeWidth;
    return this;
  }

  public int getStrokeColor() {
    return strokeColor;
  }

  public DrawableViewConfig setStrokeColor(int strokeColor) {
    this.strokeColor = strokeColor;
    return this;
  }

  public int getCanvasWidth() {
    return canvasWidth;
  }

  public DrawableViewConfig setCanvasWidth(int canvasWidth) {
    this.canvasWidth = canvasWidth;
    return this;
  }

  public int getCanvasHeight() {
    return canvasHeight;
  }

  public DrawableViewConfig setCanvasHeight(int canvasHeight) {
    this.canvasHeight = canvasHeight;
    return this;
  }

  public float getMinZoom() {
    return minZoom;
  }

  public DrawableViewConfig setMinZoom(float minZoom) {
    this.minZoom = minZoom;
    return this;
  }

  public float getMaxZoom() {
    return maxZoom;
  }

  public DrawableViewConfig setMaxZoom(float maxZoom) {
    this.maxZoom = maxZoom;
    return this;
  }

  public boolean isShowCanvasBounds() {
    return showCanvasBounds;
  }

  public DrawableViewConfig setShowCanvasBounds(boolean showCanvasBounds) {
    this.showCanvasBounds = showCanvasBounds;
    return this;
  }
}
