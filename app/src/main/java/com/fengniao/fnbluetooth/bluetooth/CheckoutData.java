package com.fengniao.fnbluetooth.bluetooth;

import android.text.TextUtils;


import com.fengniao.fnbluetooth.bluetooth.utils.AsciiToStr;
import com.fengniao.fnbluetooth.bluetooth.utils.MD5Utils;
import com.fengniao.fnbluetooth.bluetooth.utils.NetContract;
import com.fengniao.fnbluetooth.bluetooth.utils.NumbricUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kim on 12/03/2018.
 */

public class CheckoutData {


    /**
     * 检测数据是tbox主动发送还是响应数据
     * byte[2] == 0x01  主动发送
     * byte[2] == 0x02  响应数据
     *
     * @param data
     * @return true 表明是主动发送数据
     * false 是其他情况
     */
    public static boolean checkDataType(byte[] data) {
        if (data != null) {
            if (data[2] == (byte) 0x01) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    /**
     * 透传数据返回值
     *
     * @param data
     */
//    public static boolean checkoutResponse(byte[] data) {
//        if (data != null) {
//            if (data[2] == (byte) 0x02) {//数据透传回复
//                if (data[4] == (byte) 0x01) {//成功
//                    return true;
//                } else if (data[4] == (byte) 0x02) {//校验失败
//                    return false;
//                } else {//失败
//                    return false;
//                }
//            }
//        }
//        return false;
//    }


    /**
     * 判断是否是主动透传出数据
     *
     * @param data
     * @return
     */
    public static boolean isActiveResponse(byte[] data) {
        if (data != null) {
            if (data.length >= 3) {
                if (data[1] == (byte) 0x32 && data[2] == 0x01) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 控制结果
     * 透传回复
     * 身份认证回复
     * 这第三种数据返回true
     * @param data
     * @return
     */
    public static boolean isResponseData(byte[] data){
        if (data !=null && data.length >= 3 && data[1] == 0x31 && data[2] == 0x01){
            return true;
        }

        if (data !=null && data.length >= 3 && data[1] == 0x32 && data[2] == 0x02){
            return true;
        }

        if (data !=null && data.length >= 3 && data[1] == 0x32 && data[2] == 0x03){
            return true;
        }

        return false;

    }

    /**
     * 判断数据是否响应正确
     *
     * @param data
     * @return
     */
    public static boolean checkoutResponse(byte[] data) {
        if (data != null) {
            if (data[1] == (byte) 0x31) {//控制数据
                return checkoutCMDResponse(data);
            } else if (data[1] == (byte) 0x32) {//透传数据
                if (data[2] == (byte) 0x01) { //主动透传

                } else if (data[2] == (byte) 0x02) {//响应透传
                    return checkoutTransReport(data);

                } else if (data[2] == (byte) 0x03) {//身份认证
                    return checkoutTransReport(data);
                }
            }
        }
        return false;
    }




    /**
     * 响应透传回复
     *
     * @param data
     * @return
     */
    public static boolean checkoutTransReport(byte[] data) {
        if (data != null) {
            if (data[4] == (byte) 0x01) {//成功
                return true;
            } else if (data[4] == (byte) 0x02) {//校验失败
                return false;
            }
        }
        return false;
    }


    /**
     * 指令数据（开、解锁,双闪）返回值
     *
     * @param data
     */
    public static boolean checkoutCMDResponse(byte[] data) {
        if (data != null) {
            if (data[4] == (byte) 0x01) {//成功
                return true;
            }
        }
        return false;
    }


    /**
     * 装载透传指令成 tbox能识别的指令
     *
     * @param commands
     * @return
     */
    public static List<String> transDatas(List<String> commands) {
        if (commands != null && commands.size() > 0) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < commands.size(); i++) {
                list.add(transData(commands.get(i)));
            }
            return list;
        }
        return null;
    }


    public static String transData(String command){
        //根据协议装载指令
        int size = command.length();
        StringBuilder fullCommand = new StringBuilder();
        fullCommand.append(NetContract.Command.TRANS)
                .append(NumbricUtil.toHex(4, size))
                .append(AsciiToStr.stringToAscii(command))
                .append(NumbricUtil.toHex(2, NumbricUtil.XOR(command)))
                .append(NetContract.Command.ENDDATA);

        return fullCommand.toString();
    }


    /**
     * 身份认证KEY 的装载
     *
     * @param command
     * @return
     */
    public static String transKeyData(String command) {
        //根据协议装载指令
        StringBuilder fullCommand = new StringBuilder();
        fullCommand.append(NetContract.Command.ID_HEADER)
                .append(NetContract.Command.ID_HEADER_SIZE)
                .append(AsciiToStr.stringToAscii(command))
                .append(NumbricUtil.toHex(2, NumbricUtil.XOR(command)))
                .append(NetContract.Command.ENDDATA);
        return fullCommand.toString();
    }


    public static String checkedBlueToothToAppRight(String data) {

        if (!TextUtils.isEmpty(data) && data.length() > 10) {
            int size = data.length();
            String result = data.substring(10, size - 4);
            return result;

        }
        return null;
    }


    public static StringBuilder resultData;
    public static int size;//长度位算出的数据长度
    public static int resetSize;//剩余长度





    public static String dataPools(byte[] internData) {
        if (internData != null) {
            if (internData.length > 5 && internData[0] == 0x40 && internData[1] == 0x32 && internData[2] == 0x01) {//透传数据头
                resultData = new StringBuilder();
//                Log.e(FNPageConstant.TAG_BLUETOOTH, "----------------接受到  init:");
            }
            if (resultData !=null){
                String temp = MD5Utils.byteArrayToHexString(internData);
                if (temp.contains("0d")  || temp.contains("0D") ){
                    int index = temp.toLowerCase().indexOf("0d");
                    String subStr = temp.substring(0,index+2);
                    String result = resultData.append(subStr).toString();
//                    Log.e(FNPageConstant.TAG_BLUETOOTH, "----------------接受到  resultData:" + resultData);
                    resultData = null;
                    return  result;
                }else{
                    resultData.append(temp);
//                    Log.e(FNPageConstant.TAG_BLUETOOTH, "----------------接受到 :" + resultData);
                }
            }
        }
        return null;
    }




    private static void clearData() {
        resultData = null;
        size = 0;
        resetSize = 0;
    }


}
