package com.example.mvvm_base.util;

import android.text.TextUtils;

/**
 * desc:byte工具类
 * date:2021-4-25 11:04
 * author:bWang
 * <p>
 * 主要是日常的byte数组拼接、byte与string
 */
public class ByteUtil {

    /**
     * @param data1
     * @param data2
     * @return data1 与 data2拼接的结果
     */
    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }

    /**
     * 多个byte[]拼接
     *
     * @param values
     * @return
     */
    public static byte[] byteMergerAll(byte[]... values) {
        int length_byte = 0;
        for (int i = 0; i < values.length; i++) {
            length_byte += values[i].length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (int i = 0; i < values.length; i++) {
            byte[] b = values[i];
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }

    /**
     * bytes 转 String
     *
     * @param bytes
     * @return
     */
    public static String byteToString(byte[] bytes) {
        if (bytes == null) return null;
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        // 一个字节对应两个16进制数，所以长度为字节数组乘2
        char[] resultCharArray = new char[bytes.length * 2];
        int index = 0;
        for (byte b : bytes) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }

        String result = new String(resultCharArray);
        return result;
    }

    /**
     * 10进制转16进制
     *
     * @param ten
     * @return
     */
    public static int tenToHex(int ten) {
        int result = 0;
        String sixteen = Integer.toHexString(ten);
        if (!TextUtils.isEmpty(sixteen)) {
            result = Integer.parseInt(sixteen);
        }
        return result;
    }
}
