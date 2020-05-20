package com.example.notebook.Util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class BufferOperator {
    private String filePath;
    public BufferOperator(String filePath){
        this.filePath=filePath;
    }
    public Buffer read(){
        File file = new File(filePath);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ByteBuffer.wrap(bytes);
    }
    public void write(Buffer buffer){
        //默认覆盖
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(filePath));
            outputStream.write((byte[]) buffer.array());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
