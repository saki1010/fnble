package com.fengniao.fnbluetooth.bluetooth.utils;

/**
 * Created by kim on 09/03/2018.
 */

public class AsciiToStr {



    public static String asciiToString(String value)
    {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(" ");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i],16));
        }
        return sbu.toString();
    }


    public static String stringToAscii(String value)
    {
        byte[] b= value.getBytes();
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<b.length;i++){
            sb.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }



    public static String asciiToString2Nums(String value)
    {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(" ");
        for (int i = 0; i < chars.length; i=i+2) {
            sbu.append((char) Integer.parseInt(value.substring(i,i+2),16));
        }
        return sbu.toString();
    }


}
