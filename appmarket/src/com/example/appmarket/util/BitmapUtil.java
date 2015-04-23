package com.example.appmarket.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 功能描述：Bitmap加工处理工具类
 * @author android_ls
 *
 */
public class BitmapUtil {

    /**
     * 将图片变成圆角（方法一）
     * @param bitmap Bitmap
     * @param pixels 圆角的弧度
     * @return 圆角图片
     */
    public static Bitmap drawRoundBitmap(Bitmap bitmap, float pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        // paint.setColor()的参数，除不能为Color.TRANSPARENT外，可以任意写
        paint.setColor(Color.RED);
        canvas.drawRoundRect(rectF, pixels, pixels, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 将图片变成圆角（方法二）
     * @param bitmap Bitmap
     * @param pixels 圆角的弧度
     * @return 圆角图片
     */
    public static Bitmap drawRoundCorner(Bitmap bitmap, float pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        RectF outerRect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // paint.setColor()的参数，除不能为Color.TRANSPARENT外，可以任意写
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(outerRect, pixels, pixels, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Drawable drawable = new BitmapDrawable(bitmap);
        drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
        drawable.draw(canvas);
        canvas.restore();

        return output;
    }

    /**
     * 对Bitmap进行质量压缩
     * @param bitmap Bitmap
     * @return ByteArrayInputStream
     */
    public static Bitmap compressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 图片质量默认值为100，表示不压缩
        int quality = 100;
        // PNG是无损的，将会忽略质量设置。因此，这里设置为JPEG
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outStream);

        // 判断压缩后图片的大小是否大于100KB，大于则继续压缩
        while (outStream.toByteArray().length / 1024 > 100) {
            outStream.reset();

            // 压缩quality%，把压缩后的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outStream);
            quality -= 10;
        }

        System.out.println("quality = " + quality);

        byte[] data = outStream.toByteArray();
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    /**
     * 根据指定的压缩比例，获得合适的Bitmap（方法一）
     * @param file File
     * @param width 指定的宽度
     * @param height 指定的高度
     * @return Bitmap
     */
    public static Bitmap decodeStream(File file, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        int w = options.outWidth;
        int h = options.outHeight;

        // 从服务器端获取的图片大小为：80x120
        // 我们想要的图片大小为：40x40
        // 缩放比：120/40 = 3，也就是说我们要的图片大小为原图的1/3

        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int ratio = 1; // 默认为不缩放
        if (w >= h && w > width) {
            ratio = (int) (w / width);
        } else if (w < h && h > height) {
            ratio = (int) (h / height);
        }

        if (ratio <= 0) {
            ratio = 1;
        }

        System.out.println("图片的缩放比例值ratio = " + ratio);

        options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        // 属性值inSampleSize表示缩略图大小为原始图片大小的几分之一，即如果这个值为2，
        // 则取出的缩略图的宽和高都是原始图片的1/2，图片大小就为原始大小的1/4。
        options.inSampleSize = ratio;

        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    /**
     * 根据指定的压缩比例，获得合适的Bitmap（方法二）
     * @param inStream InputStream
     * @param width 指定的宽度
     * @param height 指定的高度
     * @return Bitmap
     * @throws IOException
     */
    public static Bitmap decodeStream(InputStream inStream, int width, int height) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        // 从输入流读取数据
        byte[] data = StreamTool.read(inStream);
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        int w = options.outWidth;
        int h = options.outHeight;

        // 从服务器端获取的图片大小为：80x120
        // 我们想要的图片大小为：40x40
        // 缩放比：120/40 = 3，也就是说我们要的图片大小为原图的1/3

        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int ratio = 1; // 默认为不缩放
        if (w >= h && w > width) {
            ratio = (int) (w / width);
        } else if (w < h && h > height) {
            ratio = (int) (h / height);
        }

        if (ratio <= 0) {
            ratio = 1;
        }

        System.out.println("图片的缩放比例值ratio = " + ratio);

        options.inJustDecodeBounds = false;
        // 属性值inSampleSize表示缩略图大小为原始图片大小的几分之一，即如果这个值为2，
        // 则取出的缩略图的宽和高都是原始图片的1/2，图片大小就为原始大小的1/4。
        options.inSampleSize = ratio;
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    /**
     * 根据指定的压缩比例，获得合适的Bitmap（会出错的方法，仅用于测试）
     * @param inStream
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    public static Bitmap decodeStreamError(InputStream inStream, int width, int height) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inStream, null, options);

        int w = options.outWidth;
        int h = options.outHeight;

        // 从服务器端获取的图片大小为：80x120
        // 我们想要的图片大小为：40x40
        // 缩放比：120/40 = 3，也就是说我们要的图片大小为原图的1/3

        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int ratio = 1; // 默认为不缩放
        if (w >= h && w > width) {
            ratio = (int) (w / width);
        } else if (w < h && h > height) {
            ratio = (int) (h / height);
        }

        if (ratio <= 0) {
            ratio = 1;
        }

        System.out.println("图片的缩放比例值ratio = " + ratio);

        options.inJustDecodeBounds = false;
        // 属性值inSampleSize表示缩略图大小为原始图片大小的几分之一，即如果这个值为2，
        // 则取出的缩略图的宽和高都是原始图片的1/2，图片大小就为原始大小的1/4。
        options.inSampleSize = ratio;

        return BitmapFactory.decodeStream(inStream, null, options);
    }
    
public static Bitmap drawableToBitmap(Drawable drawable) {
        
        Bitmap bitmap = Bitmap
                        .createBitmap(
                                        drawable.getIntrinsicWidth(),
                                        drawable.getIntrinsicHeight(),
                                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
}

}
