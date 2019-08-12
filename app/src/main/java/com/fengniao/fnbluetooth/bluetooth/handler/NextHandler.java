package com.fengniao.fnbluetooth.bluetooth.handler;


import com.fengniao.fnbluetooth.bluetooth.impl.ResultListener;

/**
 * Created by kim on 22/01/2018.
 */

public abstract class NextHandler {
    public NextHandler nextHandler;
    public abstract void handlerRequest(ResultListener resultListener);
}
