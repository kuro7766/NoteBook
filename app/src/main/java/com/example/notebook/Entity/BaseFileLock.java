package com.example.notebook.Entity;

public class BaseFileLock extends BaseContextWrapper {
    protected final Object picLock = new Object();
    protected final Object cameraLock = new Object();
    protected final Object txtLock = new Object();
    protected final Object relationLock = new Object();
    protected static final Object randomAccessLock=new Object();
    protected final Object picLock_drawBoard=new Object();
}
