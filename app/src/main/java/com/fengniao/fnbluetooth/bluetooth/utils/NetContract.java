package com.fengniao.fnbluetooth.bluetooth.utils;

public interface NetContract {

    interface  Command {
        String OPEN = "4031010101010D";//解锁
        String CLOSE = "4031010102020D";//关门
        String DOUBLEFLUSH = "4031010106060D";//双闪+鸣笛
        String TRANS = "403202"; // 透传数据请求头
        String ID_HEADER ="403203";//身份认证头
        String ID_HEADER_SIZE= "10"; //身份认证 长度
        String ENDDATA = "0D"; //结尾
    }

    interface  GuardType{
        int connectTimeOut = 1; // 蓝牙连接超时
        int CommandTimeOut = 2; // 蓝牙指令执行超时
    }

    int COMMAND_KEY_TIME_OUT = 5;   //单条指令执行超时时长
    int CONNECT_TIME_OUT = 5;   //连接蓝牙超时时间
}
