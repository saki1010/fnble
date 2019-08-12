package com.fengniao.fnbluetooth.bluetooth.impl;

/**
 * Created by kim on 12/03/2018.
 */

public interface ICommandCallbackListener {
    /**
     * 指令结果回调
     * true，指令执行成功，false 执行失败
     * @param result
     */
    void commandCallback(boolean result);
}
