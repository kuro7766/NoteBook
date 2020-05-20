package com.example.notebook.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notebook.Entity.RvTreeViewItemBean;
import com.example.notebook.R;
import com.example.notebook.Util.TCallBack;
import com.example.notebook.drawableview.DrawableView;
import com.example.notebook.drawableview.DrawableViewConfig;
import com.example.notebook.drawableview.draw.SerializablePath;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FragmentDrawBoard extends Fragment {

    public static FragmentDrawBoard newInstance() {
        Bundle args = new Bundle();
        FragmentDrawBoard fragment = new FragmentDrawBoard();
        fragment.setArguments(args);
        return fragment;
    }

    private DrawableView drawableView;

    public void clear() {
        drawableView.clear();
    }

    public void setPaths(List<SerializablePath> serializablePaths) {
        if (serializablePaths == null) {
            clear();
        } else{
            for(SerializablePath serializablePath:serializablePaths){
                serializablePath.loadPathPointsAsCanvasPath();
            }
            drawableView.setPaths(serializablePaths);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.draw_board, null);
        drawableView = v.findViewById(R.id.drawable_view);
        drawableView.setConfig(new DrawableViewConfig()
                .setCanvasHeight(Integer.MAX_VALUE)
                .setCanvasWidth(Integer.MAX_VALUE)
                .setStrokeColor(Color.WHITE)
                .setStrokeWidth(6.5f));
        drawableView.setOnFingerUpListener(new TCallBack<ArrayList<SerializablePath>>() {
            @Override
            public void call(ArrayList<SerializablePath> aVoid) {
                if (onFingerUpListener != null) {
                    onFingerUpListener.call(aVoid);
                }
            }
        });
        return v;
//        return super.onCreateView(inflater, container, savedInstanceState);
    }

    TCallBack<ArrayList<SerializablePath>> onFingerUpListener;

    public FragmentDrawBoard setOnFingerUpListener(TCallBack<ArrayList<SerializablePath>> onFingerUpListener) {
        this.onFingerUpListener = onFingerUpListener;
        return this;
    }

    public void postInvalidate() {
        drawableView.postInvalidate();
    }
}
