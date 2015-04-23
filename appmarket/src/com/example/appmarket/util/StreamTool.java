package com.example.appmarket.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 功能描述：数据流处理工具类
 * @author android_ls
 */
public final class StreamTool {

    /**
     * 从输入流读取数据
     * 
     * @param inStream
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static byte[] read(InputStream inStream) throws IOException {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

}
