package com.fengniao.fnbluetooth.bluetooth.utils;

/**
 * Created by kim on 11/03/2018.
 */

public class NumbricUtil {


    /**
     *将10进制数转成指定位数的16进制数
     * @param length
     * @param num
     * @return
     */
    public static String toHex(int length, Integer num) {
        String hex = num.toHexString(num);
        if (hex.length() < length) {
            int size = length - hex.length();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < size; i++) {
                builder.append("0");
            }
            builder.append(hex);

            return builder.toString();
        }
        return hex;
    }



    public static int XOR(String command){
        int v = 0;
        if (command !=null){
            byte[] bytes = command.getBytes();
            if (bytes!=null && bytes.length >0){
                for (int i=0;i<bytes.length;i++){
                     v  ^= bytes[i];
                }
            }
        }
        return v;
    }


    public static int XOR(byte[] command){
        int v = 0;
        if (command !=null){
            if (command!=null && command.length >0){
                for (int i=0;i<command.length;i++){
                    v  ^= command[i];
                }
            }
        }
        return v;
    }


    /**
     * 16进制转byte
     * @param s
     * @return
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        try {
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                        + Character.digit(s.charAt(i+1), 16));
            }
        } catch (Exception e) {
            //Log.d("", "Argument(s) for hexStringToByteArray(String s)"+ "was not a hex string");
        }
        return data;
    }


}
