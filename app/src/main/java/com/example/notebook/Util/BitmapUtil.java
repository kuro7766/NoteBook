package com.example.notebook.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil{

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static void saveBitmapAsFile(Bitmap bmp, String path, Bitmap.CompressFormat format,int quality){
        File f = new File(path);
        File dir = new File(SharedPreferenceUtil.getPathDir(path));
        if (!dir.exists()) {
            dir.mkdir();
        }
        try (FileOutputStream out = new FileOutputStream(path)) {
            bmp.compress(format,quality, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveBitmapAsFile(Bitmap bmp, String path) {
        saveBitmapAsFile(bmp,path,Bitmap.CompressFormat.PNG, 100);
    }

    public static Bitmap loadBitmap(String path) {
        Bitmap bitmap = null;
        try {
            bitmap = decodeFile(path).copy(Bitmap.Config.ARGB_8888, true);
            bitmap = Bitmap.createBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * auto resize file if the bitmap is larger than the MAXSIZE of image
     * @param filePath
     * @return
     * @throws IOException
     */
    public static Bitmap decodeFile(String filePath) throws IOException {
        Bitmap b = null;
        int IMAGE_MAX_SIZE = Integer.MAX_VALUE;

        File f = new File(filePath);
        if (f == null) {
            return null;
        }
        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = new FileInputStream(f);
        BitmapFactory.decodeStream(fis, null, o);
        fis.close();

        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        fis = new FileInputStream(f);
        b = BitmapFactory.decodeStream(fis, null, o2);
        fis.close();
        return b;
    }

    /**
     * view转bitmap
     */
    public static Bitmap viewToBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    public static Bitmap restrictSize(Bitmap source,int maxWH){
        Bitmap bitmap=null;
        if(source.getWidth()>source.getHeight()&&source.getWidth()>maxWH){
            bitmap=BitmapUtil.scaleBitmap(source,maxWH,source.getHeight()*maxWH/source.getWidth());
        }else if(source.getWidth()<source.getHeight()&&source.getHeight()>maxWH){
            bitmap=BitmapUtil.scaleBitmap(source,source.getHeight()*maxWH/source.getWidth(),maxWH);
        }else {
            bitmap=source;
        }
        return bitmap;
    }

    public static Bitmap scaleBitmap(Bitmap bitmapToScale, float newWidth, float newHeight) {
        if (bitmapToScale == null)
            return null;
//get the original width and height
        int width = bitmapToScale.getWidth();
        int height = bitmapToScale.getHeight();
// create a matrix for the manipulation
        Matrix matrix = new Matrix();

// resize the bit map
        matrix.postScale(newWidth / width, newHeight / height);

// recreate the new Bitmap and set it back
        Bitmap resized=Bitmap.createBitmap(bitmapToScale, 0, 0, bitmapToScale.getWidth(), bitmapToScale.getHeight(), matrix, true);
        bitmapToScale.recycle();
        return resized;
    }

    private static final String TAG = "BitmapUtil";

    public static void pdfToBitmapCall(int startIndex,Context context, File pdfFile,TKCallBack<Bitmap,Boolean> callBack) {
        try {
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));
            Bitmap bitmap;
            final int pageCount = renderer.getPageCount();
            Log.e("test_sign", "图片de 张数： " +pageCount);
            if(startIndex>=pageCount)
                return;
            for (int i = startIndex; i < pageCount; i++) {
                PdfRenderer.Page page = renderer.openPage(i);
                int width = context.getResources().getDisplayMetrics().densityDpi / 72 * page.getWidth();
                int height = context.getResources().getDisplayMetrics().densityDpi / 72 * page.getHeight();
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                //todo 以下三行处理图片存储到本地出现黑屏的问题，这个涉及到背景问题
                Canvas canvas = new Canvas(bitmap);
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(bitmap, 0, 0, null);
                Rect r = new Rect(0, 0, width, height);
                page.render(bitmap, r, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                if(!callBack.call(bitmap)){
                    return;
                }
                // close the page
                page.close();
            }
            // close the renderer
            renderer.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void pdfToBitmapCall(Context context, File pdfFile,TKCallBack<Bitmap,Boolean> callBack) {
        pdfToBitmapCall(0,context,pdfFile,callBack);
    }
}
