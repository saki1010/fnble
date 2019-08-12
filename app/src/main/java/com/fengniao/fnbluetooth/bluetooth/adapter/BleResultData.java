package com.fengniao.fnbluetooth.bluetooth.adapter;

import android.support.annotation.Keep;

/**
 *  蓝牙操作结果
 */
@Keep
public class BleResultData {
    private boolean result; //执行结果
    private String commendResult;   //盒子返回数据
    private boolean authResult; //是否是认证指令
    private boolean isConnect;  //是否连接

    /**
     *  用于连接蓝牙
     * @param isConnect
     */
    public BleResultData(boolean isConnect) {
        this.isConnect = isConnect;
    }

    /**
     *  用于执行指令
     * @param result
     * @param authResult
     */
    public BleResultData(boolean result, boolean authResult) {
        this.authResult = authResult;
        this.result = result;
    }

    /**
     *  用于执行指令，带返回结果
     */
    public BleResultData(boolean result, boolean authResult, String commendResult) {
        this.authResult = authResult;
        this.result = result;
        this.commendResult = commendResult;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getCommendResult() {
        return commendResult;
    }

    public void setCommendResult(String commendResult) {
        this.commendResult = commendResult;
    }

    public boolean isAuthResult() {
        return authResult;
    }

    public void setAuthResult(boolean authResult) {
        this.authResult = authResult;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }
}
