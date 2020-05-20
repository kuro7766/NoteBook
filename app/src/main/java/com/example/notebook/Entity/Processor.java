package com.example.notebook.Entity;

import com.example.notebook.Constant;
import com.example.notebook.Util.GlobalVar;

import java.io.File;

public class Processor extends BaseContextWrapper{

    public synchronized long newId(){
        Long i= (Long) GlobalVar.get(Constant.id_generator);
        if(i==null){
            ensureFirstOpen();
            GlobalVar.set(Constant.id_generator,1L);
            return 1;
        }else {
            GlobalVar.set(Constant.id_generator, i + 1);
            return i+1;
        }
    }

    public void ensureFirstOpen(){

        new File(context().getFilesDir()+Constant.pic_dir).mkdirs();
        new File(context().getFilesDir()+Constant.relationship_dir).mkdirs();
        new File(context().getFilesDir()+Constant.txt_dir).mkdirs();
        new File(context().getFilesDir()+Constant.camera_dir).mkdirs();
        new File(context().getFilesDir()+Constant.pic_draw_dir).mkdirs();

        if(GlobalVar.get(Constant.id_generator)!=null)
            return;
        GlobalVar.set(Constant.root_id,0L);
        new RvTreeViewItemBean(0).save();
    }

    public long getRootId(){
        return (Long) GlobalVar.get(Constant.root_id);
    }

    public void setRootId(long id){
        GlobalVar.set(Constant.root_id,id);
    }

    public RvTreeViewItemBean getRoot(){
        return RvTreeViewItemBean.fromId(getRootId());
    }
}
