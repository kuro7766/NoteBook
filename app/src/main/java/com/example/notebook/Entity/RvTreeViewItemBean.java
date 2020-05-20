package com.example.notebook.Entity;

import android.graphics.Bitmap;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.notebook.Constant;
import com.example.notebook.Util.BitmapUtil;
import com.example.notebook.Util.FileOperator;
import com.example.notebook.Util.SharedPreferenceUtil;
import com.example.notebook.drawableview.draw.SerializablePath;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class RvTreeViewItemBean extends BaseFileLock {
    //this class does not hold any properties,all of their entities are
    //stored on disk
    private static final String TAG = "RvTreeViewItemBean";
    protected long id;
    protected List<Long> childs;
    protected long parentId;

    public RvTreeViewItemBean() {
    }

    public RvTreeViewItemBean(long id, long parentId) {
        this.id = id;
        this.parentId = parentId;
    }

    public RvTreeViewItemBean(long id) {
        this.id = id;
        childs = new ArrayList<>();
        parentId = -1;
    }

    public long getParentId() {
        return parentId;
    }

    public long getId() {
        return id;
    }

    public RvTreeViewItemBean setParentId(long parentId) {
        this.parentId = parentId;
        return this;
    }

    public RvTreeViewItemBean setId(long id) {
        this.id = id;
        return this;
    }

    public List<Long> getChilds() {
        return childs;
    }

    public RvTreeViewItemBean setChilds(List<Long> childs) {
        this.childs = childs;
        return this;
    }


    public String txt() {
        synchronized (txtLock) {
            return new FileOperator(txtPath()).read();
        }

    }

    public void setTxt(String s) {
        synchronized (txtLock) {
            new FileOperator(txtPath()).write(s);
        }
    }

    public Bitmap camera() {
        synchronized (cameraLock) {
            return BitmapUtil.loadBitmap(cameraPath());
        }
    }

    public Bitmap pic() {
        synchronized (picLock) {
            return BitmapUtil.loadBitmap(picPath());
        }
    }

    public void setCamera(Bitmap bmp) {
        synchronized (cameraLock) {
            BitmapUtil.saveBitmapAsFile(bmp, cameraPath());
        }
    }

    public void setPic(Bitmap bmp) {
        synchronized (picLock) {
            BitmapUtil.saveBitmapAsFile(bmp, cameraPath());
        }
    }

    public void setPicDraw(List<SerializablePath> serializablePaths) {
        synchronized (picLock_drawBoard) {
            File file = new File(picDrawPath());
            OutputStream out = null;
            try {
                out = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
                objectOutputStream.writeObject(serializablePaths);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<SerializablePath> getPicDraw() {
        synchronized (picLock_drawBoard) {
            File file = new File(picDrawPath());
            if(!file.exists()){
                return null;
            }
            InputStream in = null;
            try {
                in = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(in);
                Object o=objectInputStream.readObject();
                return (List<SerializablePath>) o;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //1.连接
    //2.保存子类
    public void addChild(RvTreeViewItemBean bean) {
        bean.parentId = id;
        bean.save();
        childs.add(bean.id);
        new FileOperator(relationShipPath()).write(JSON.toJSONString(this));
    }

    protected String picPath() {
        return context().getFilesDir() + Constant.pic_dir + id;

    }

    protected String picDrawPath() {
        return context().getFilesDir() + Constant.pic_draw_dir + id;

    }

    protected String cameraPath() {
        return context().getFilesDir() + Constant.camera_dir + id;


    }

    protected String txtPath() {
        return context().getFilesDir() + Constant.txt_dir + id;


    }

    protected String relationShipPath() {
        return context().getFilesDir() + Constant.relationship_dir + id;
    }

    @Override
    public String toString() {
        return "RvTreeViewItemBean{" +
                "id=" + id +
                ", childs=" + childs +
                ", parentId=" + parentId +
                '}';
    }

    public static RvTreeViewItemBean fromId(long id) {
        String s;
        synchronized (randomAccessLock) {
            s = new FileOperator(context().getFilesDir() + Constant.relationship_dir + id).read();
        }

        Log.d(TAG, "read" + id + "," + s);
        if (s == null) {
            return null;
        }
        RvTreeViewItemBean baseTreeBean = JSON.parseObject(s, RvTreeViewItemBean.class);
        if (baseTreeBean.childs == null) {
            baseTreeBean.childs = new ArrayList<>();
        }
        return baseTreeBean;
    }


    public void save() {
        synchronized (randomAccessLock) {
            new FileOperator(relationShipPath()).write(JSON.toJSONString(this));
        }
    }

}
