package com.zhuorui.securities.base2app.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/29
 * Desc: base64转码工具
 */
public class FileToBase64Util {

    /**
     * BitMap转Base64字符串
     *
     * @param bitmap 位图
     */
    public static String bitmapBase64String(Base64Enum base64Enum, Bitmap bitmap) {
        if (bitmap == null) return null;
        try {
            //缩放压缩
            float scale = calculateScale(bitmap.getWidth(), bitmap.getHeight(), 480, 800);
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            //质量压缩
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap.CompressFormat format = null;
            String imgFormat = base64Enum.getDesc();
            switch (imgFormat) {
                case "png":
                    format = Bitmap.CompressFormat.PNG;
                    break;
                case "jpg":
                case "jpeg":
                    format = Bitmap.CompressFormat.JPEG;
                    break;
            }
            bm.compress(format, 50, baos);
            //进行Base64编码;
            return base64Enum.getCode() + Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 图片文件转换成String
     *
     * @param filePath
     * @return
     */
    public static String bitmapBase64String(String filePath) {
        Bitmap bm = getSmallBitmap(filePath);
        //质量压缩
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        return Base64Enum.JPEG.getCode() + Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * byte 转Base64字符串
     *
     * @param bytes 字节
     */
    public static String getBase64String(Base64Enum base64Enum, byte[] bytes) {
        if (bytes == null || bytes.length == 0) return null;
        try {
            //进行Base64编码;
            return base64Enum.getCode() + Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据路径获得图片并压缩，返回bitmap用于显示
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        //计算采样率
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options.outWidth, options.outHeight, 480, 800);
        options.inJustDecodeBounds = false;
        //设置RGB_565
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算图片的缩放值
     *
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(int width, int height, int reqWidth, int reqHeight) {
        int inSampleSize = 1;
        if (width > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    /**
     * 计算图片的缩放xx
     *
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static float calculateScale(int width, int height, int reqWidth, int reqHeight) {
        float inSampleSize = 1f;
        if (width > reqHeight || width > reqWidth) {
            final float heightRatio = (float) reqHeight / (float) height;
            final float widthRatio = (float) reqWidth / (float) width;
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree 旋转角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle  旋转角度
     * @param bitmap 原图
     * @return bitmap 旋转后的图片
     */
    public static Bitmap rotateImage(int angle, Bitmap bitmap) {
        // 图片旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 得到旋转后的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

//    /**
//     //     * 图片文件转Base64字符串
//     //     *
//     //     * @param path 文件所在的绝对路径加文件名
//     //     */
//    public static String fileBase64String(String path) {
//        try {
//            FileInputStream fis = new FileInputStream(path);//转换成输入流
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int count = 0;
//            while ((count = fis.read(buffer)) >= 0) {
//                baos.write(buffer, 0, count);//读取输入流并写入输出字节流中
//            }
//            fis.close();//关闭文件输入流
//            String uploadBuffer = "data:video/avi;base64," + Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);  //进行Base64编码
//            return uploadBuffer;
//        } catch (Exception e) {
//            return null;
//        }
//    }

}