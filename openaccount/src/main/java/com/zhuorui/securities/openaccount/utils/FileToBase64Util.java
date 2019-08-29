package com.zhuorui.securities.openaccount.utils;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/29
 * Desc:
 */
public class FileToBase64Util {
    /**
     * 图片文件转Base64字符串
     * @param path 文件所在的绝对路径加文件名　
     * @return
     */
   public static String fileBase64String(String path){
        try {
            FileInputStream fis = new FileInputStream(path);//转换成输入流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int count = 0;
            while((count = fis.read(buffer)) >= 0){
                baos.write(buffer, 0, count);//读取输入流并写入输出字节流中
            }
            fis.close();//关闭文件输入流
            String uploadBuffer = "data:video/avi;base64,"+Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT);  //进行Base64编码
            return uploadBuffer;
        } catch (Exception e) {
            return null;
        }

    }
}
