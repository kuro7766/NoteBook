package com.example.notebook.Entity;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.notebook.drawableview.draw.SerializablePath;

import java.util.ArrayList;
import java.util.List;

public class TreeStatusControl {

    public String txt() {
        return RvTreeViewItemBean.fromId(indicatingId()).txt();
    }

    public void setTxt(String s) {
        RvTreeViewItemBean.fromId(indicatingId()).setTxt(s);
    }

    public Bitmap camera() {
        return RvTreeViewItemBean.fromId(indicatingId()).camera();
    }

    public Bitmap pic() {
        return RvTreeViewItemBean.fromId(indicatingId()).pic();
    }

    public void setCamera(Bitmap bmp) {
        RvTreeViewItemBean.fromId(indicatingId()).setCamera(bmp);
    }

    public void setPic(Bitmap bmp) {
        RvTreeViewItemBean.fromId(indicatingId()).setPic(bmp);
    }

    public ArrayList<SerializablePath> picDraw(){
        return (ArrayList<SerializablePath>) RvTreeViewItemBean.fromId(indicatingId()).getPicDraw();
    }

    public void setPicDraw(List<SerializablePath> list){
        RvTreeViewItemBean.fromId(indicatingId()).setPicDraw(list);
    }
    
    
    @NonNull
    public Long currentRoot;

    @Nullable
    public Long currentSelection;

    public int selected = -1;

    public void inner(){
        currentRoot=currentSelection;
        currentSelection=null;
        selected=-1;
    }
    public void back(){
        currentRoot=RvTreeViewItemBean.fromId(currentRoot).parentId;
        currentSelection=null;
        selected=-1;
    }
    
    public long indicatingId(){
        if(selected==-1){
            return currentRoot;
        }else {
            return currentSelection;
        }
    }

    public void removeChild(long childId){
        RvTreeViewItemBean b=(RvTreeViewItemBean.fromId(currentRoot));
        b.childs.remove(childId);
        b.save();
        selected=-1;
    }
}
