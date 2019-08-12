package com.fengniao.fnbluetooth.bluetooth.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

/**
 * Created by kim on 16-10-20.
 * 这是一个关于MD5加密的方法类，方法如下：
 * 1.encodeByMD5            //对字符串进行MD5加密
 * 2.validatePassword       //验证输入的密码是否正确
 */
public class MD5Utils {

    //十六进制下数字到字符的映射数组
    private final static String[] hexDigits = {"0", "1", "2", "3", "4",
            "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 验证输入的密码是否正确
     * @param password    加密后的密码
     * @param inputString    输入的字符串
     * @return    验证结果，TRUE:正确 FALSE:错误
     */
    public static boolean validatePassword(String password, String inputString){
        if(password.equals(encodeByMD5(inputString))){
            return true;
        } else{
            return false;
        }
    }

    /**
     * 对字符串进行MD5加密
     * @param sourceStr
     * @return
     */
    public static String encodeByMD5(String sourceStr){
        String resultStr = null;
        if (sourceStr != null){
            try{
                //创建具有指定算法名称的信息摘要
                MessageDigest md = MessageDigest.getInstance("MD5");
                //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md.digest(sourceStr.getBytes());
                //将得到的字节数组变成字符串返回
                 resultStr = byteArrayToHexString(results);
//               resultString.toUpperCase();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return resultStr;
    }

    /**
     * SHA算法
     * @param sourceStr
     * @return
     */
    public static String encodeBySha(String sourceStr){
        String resultStr = null;
        if (sourceStr != null){
            try {
                MessageDigest sha = MessageDigest.getInstance("SHA");
                sha.update(sourceStr.getBytes());
                byte[] shaBytes = sha.digest();
                resultStr = byteArrayToHexString(shaBytes);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return resultStr;
    }

    /**
     * 转换字节数组为十六进制字符串
     * @param
     * @return    十六进制字符串
     */
    public static String byteArrayToHexString(byte[] b){
        StringBuffer resultSb = new StringBuffer();
        if (b != null){ //主动排除b为null的情况
            for (int i = 0; i < b.length; i++){
                resultSb.append(byteToHexString(b[i]));
            }
        }
        return resultSb.toString();
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     * @param b
     * @return
     */
    private static String byteToHexString(byte b){
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     *  获取文件的md5值--解析不准确
     * @param file
     * @return
     */
//    public static String getFileMD5(File file) {
//        if (!file.isFile()) {
//            return null;
//        }
//        MessageDigest digest = null;
//        FileInputStream in = null;
//        byte buffer[] = new byte[1024];
//        int len;
//        try {
//            digest = MessageDigest.getInstance("MD5");
//            in = new FileInputStream(file);
//            while ((len = in.read(buffer, 0, 1024)) != -1) {
//                digest.update(buffer, 0, len);
//            }
//            in.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        BigInteger bigInt = new BigInteger(1, digest.digest());
//        return bigInt.toString(16);
//    }

    public static String getFileMD5(File file) {
        if (file == null || !file.isFile() || !file.exists()) {
            return "";
        }
        FileInputStream in = null;
        String result = "";
        byte buffer[] = new byte[1024];
        int len;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                md5.update(buffer, 0, len);
            }
            byte[] bytes = md5.digest();

            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null!=in){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
