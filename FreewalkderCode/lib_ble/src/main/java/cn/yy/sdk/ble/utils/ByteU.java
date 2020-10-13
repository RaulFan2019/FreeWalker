package cn.yy.sdk.ble.utils;

import android.util.AndroidException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Raul.fan on 2017/6/20 0020.
 */

public class ByteU {

    private static final String TAG = "ByteU";

    /**
     * byte 数组转化成16进制的字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(final byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * @param b
     * @return
     */
    public static long bytesToLong(byte[] b) {

        int mask = 0xff;
        int temp = 0;
        long n = 0;
        for (int i = 0; i < b.length; i++) {
            n <<= 8;
            temp = b[i] & mask;
            n |= temp;
        }
        return n;
    }

    /**
     * @param b
     * @return
     */
    public static long bytesToLongUnsigned(byte[] b) {

        int mask = 0xff;
        int temp = 0;
        long n = 0;
        for (int i = 0; i < b.length; i++) {
            n <<= 8;
            temp = b[i] & mask;
            n |= temp;
        }
        n = n <<= 8;
        return n;
    }


    /**
     * byte array 类型转成byte数组
     *
     * @param data
     * @return
     */
    public static byte[] bytesArrayToByteGroup(List<Byte> data) {
        byte[] result = new byte[data.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = data.get(i);
        }
        return result;
    }


    /**
     * bytes 数组转化为 Int 数组
     *
     * @param data
     * @return
     */
    public static int[] bytesToInts(byte[] data) {
        int[] result = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = data[i] >= 0 ? data[i] : data[i] + 256;
        }
        return result;
    }


    /**
     * long 转成byte数组
     *
     * @param num
     * @return
     */
    public static byte[] longToBytes(long num) {
        byte[] buf = new byte[8];

        for (int i = 7; i > -1; i++) {
            buf[i] = (byte) (num & 0x00000000000000ff);
            num >>= 8;
        }
        return buf;
    }

    /**
     * int 转成byte数组
     *
     * @param data
     * @return
     */
    public static byte[] intToBytes(int data) {
        byte[] buf = new byte[4];

        for (int i = 3; i > -1; i--) {
            buf[i] = (byte) (data & 0x000000ff);
            data >>= 8;
        }
        return buf;
    }


    /**
     * byte 转ascii字符串
     *
     * @param bytes
     * @param offset
     * @param dateLen
     * @return
     */
    public static String bytesToAscii(byte[] bytes, int offset, int dateLen) {
        if ((bytes == null) || (bytes.length == 0) || (offset < 0) || (dateLen <= 0)) {
            return null;
        }
        if ((offset >= bytes.length) || (bytes.length - offset < dateLen)) {
            return null;
        }

        String asciiStr = null;
        byte[] data = new byte[dateLen];
        System.arraycopy(bytes, offset, data, 0, dateLen);
        try {
            asciiStr = new String(data, "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
        }
        return asciiStr;
    }

    /**
     * 计算crc 8
     *
     * @param bytes
     * @return
     */
    public static int calcCrc8(int[] bytes) {
        int crc = 0;
        for (int b : bytes) {
            b = b & 0xff;
            crc = crc8Table[(crc ^ b) & 0xff];
        }
        return crc;
    }


    /**
     * CRC-16/CCITT-FALSE x16+x12+x5+1 算法
     * <p>
     * info
     * Name:CRC-16/CCITT-FAI
     * Width:16
     * Poly:0x1021
     * Init:0xFFFF
     * RefIn:False
     * RefOut:False
     * XorOut:0x0000
     *
     * @param bytes
     * @return
     */
    public static int crc_16_CCITT_False(byte[] bytes) {
        int crc = 0; // initial value
        int polynomial = 0x1021; // poly value
        for (int index = 0; index < bytes.length; index++) {
            byte b = bytes[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) {
                    crc ^= polynomial;
                }
            }
        }
        crc &= 0xffff;
        return crc;
    }


    /**
     * CRC-16/CCITT-FALSE x16+x12+x5+1 算法
     * <p>
     * info
     * Name:CRC-16/CCITT-FAI
     * Width:16
     * Poly:0x1021
     * Init:0xFFFF
     * RefIn:False
     * RefOut:False
     * XorOut:0x0000
     *
     * @param pkg
     * @return
     */
    public static int crc_16_CCITT_False(int crc, final byte[] pkg) {
        int polynomial = 0x1021; // poly value
        int dataLength = pkg[0] - 1;
        int result = crc;

        byte[] crcB = new byte[dataLength];
        for (int index = 0; index < dataLength; index ++ ){
            byte b = pkg[index + 4];
            crcB[index] = b;
        }
//        Log.v(TAG,"hex:" + ByteU.bytesToHexString(crcB));
        for (int index = 0; index < crcB.length; index++) {
            byte b = crcB[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) {
                    crc ^= polynomial;
                }
            }
        }
        crc &= 0xffff;
//        Log.v(TAG,"hex result:" + crc);
        return crc;
    }


    /**
     * CRC-16/CCITT-FALSE x16+x12+x5+1 算法
     * @param crc
     * @param dataLength
     * @return
     */
    public static int crc_16_CCITT_False(int crc, final byte[] data, final int dataLength) {
        int polynomial = 0x1021; // poly value
        int result = crc;

        byte[] crcB = new byte[dataLength];
        if (data.length == dataLength){
            crcB = data;
        }else {
            //把有效数据抽出来
            for (int index = 0; index < dataLength; index ++ ){
                byte b = data[index];
                crcB[index] = b;
            }
        }
//        Log.v(TAG,"hex:" + ByteU.bytesToHexString(crcB));
        for (int index = 0; index < crcB.length; index++) {
            byte b = crcB[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) {
                    crc ^= polynomial;
                }
            }
        }
        crc &= 0xffff;
        return crc;
    }


    /**
     * byte 转成二进制字符串
     *
     * @param b
     * @return
     */
    public static String byte2binString(int b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            char c = ((b >> (7 - i)) & 1) == 1 ? '1' : '0';
            sb.append(c);
        }
        return sb.toString();
    }

    public static char code2char(int code) throws AndroidException {
        if (code >= 0 && code <= 25) {
            return (char) (97 + code);
        } else if (code == 26) {
            return '-';
        } else if (code == 27) {
            return '_';
        } else if (code == 28) {
            return '\'';
        } else {
            throw new AndroidException(String.format("illegal code: %d", code));
        }
    }


    private static final int[] crc8Table = {
            0x00, 0x07, 0x0e, 0x09, 0x1c, 0x1b, 0x12, 0x15, 0x38, 0x3f, 0x36, 0x31, 0x24, 0x23, 0x2a, 0x2d,
            0x70, 0x77, 0x7e, 0x79, 0x6c, 0x6b, 0x62, 0x65, 0x48, 0x4f, 0x46, 0x41, 0x54, 0x53, 0x5a, 0x5d,
            0xe0, 0xe7, 0xee, 0xe9, 0xfc, 0xfb, 0xf2, 0xf5, 0xd8, 0xdf, 0xd6, 0xd1, 0xc4, 0xc3, 0xca, 0xcd,
            0x90, 0x97, 0x9e, 0x99, 0x8c, 0x8b, 0x82, 0x85, 0xa8, 0xaf, 0xa6, 0xa1, 0xb4, 0xb3, 0xba, 0xbd,
            0xc7, 0xc0, 0xc9, 0xce, 0xdb, 0xdc, 0xd5, 0xd2, 0xff, 0xf8, 0xf1, 0xf6, 0xe3, 0xe4, 0xed, 0xea,
            0xb7, 0xb0, 0xb9, 0xbe, 0xab, 0xac, 0xa5, 0xa2, 0x8f, 0x88, 0x81, 0x86, 0x93, 0x94, 0x9d, 0x9a,
            0x27, 0x20, 0x29, 0x2e, 0x3b, 0x3c, 0x35, 0x32, 0x1f, 0x18, 0x11, 0x16, 0x03, 0x04, 0x0d, 0x0a,
            0x57, 0x50, 0x59, 0x5e, 0x4b, 0x4c, 0x45, 0x42, 0x6f, 0x68, 0x61, 0x66, 0x73, 0x74, 0x7d, 0x7a,
            0x89, 0x8e, 0x87, 0x80, 0x95, 0x92, 0x9b, 0x9c, 0xb1, 0xb6, 0xbf, 0xb8, 0xad, 0xaa, 0xa3, 0xa4,
            0xf9, 0xfe, 0xf7, 0xf0, 0xe5, 0xe2, 0xeb, 0xec, 0xc1, 0xc6, 0xcf, 0xc8, 0xdd, 0xda, 0xd3, 0xd4,
            0x69, 0x6e, 0x67, 0x60, 0x75, 0x72, 0x7b, 0x7c, 0x51, 0x56, 0x5f, 0x58, 0x4d, 0x4a, 0x43, 0x44,
            0x19, 0x1e, 0x17, 0x10, 0x05, 0x02, 0x0b, 0x0c, 0x21, 0x26, 0x2f, 0x28, 0x3d, 0x3a, 0x33, 0x34,
            0x4e, 0x49, 0x40, 0x47, 0x52, 0x55, 0x5c, 0x5b, 0x76, 0x71, 0x78, 0x7f, 0x6a, 0x6d, 0x64, 0x63,
            0x3e, 0x39, 0x30, 0x37, 0x22, 0x25, 0x2c, 0x2b, 0x06, 0x01, 0x08, 0x0f, 0x1a, 0x1d, 0x14, 0x13,
            0xae, 0xa9, 0xa0, 0xa7, 0xb2, 0xb5, 0xbc, 0xbb, 0x96, 0x91, 0x98, 0x9f, 0x8a, 0x8d, 0x84, 0x83,
            0xde, 0xd9, 0xd0, 0xd7, 0xc2, 0xc5, 0xcc, 0xcb, 0xe6, 0xe1, 0xe8, 0xef, 0xfa, 0xfd, 0xf4, 0xf3
    };


    public static byte[] toByteArray(final InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }
}
