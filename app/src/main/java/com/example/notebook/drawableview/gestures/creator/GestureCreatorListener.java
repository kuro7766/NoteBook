package com.example.notebook.drawableview.gestures.creator;

import com.example.notebook.drawableview.draw.SerializablePath;

public interface GestureCreatorListener {
    //to make serializable path
  void onNewPathCreated(SerializablePath serializablePath);

  void onCurrentGestureChanged(SerializablePath currentDrawingPath);
}
