package com.fengniao.fnbluetooth.bluetooth.handler;


import com.fengniao.fnbluetooth.bluetooth.impl.ICommandCallbackListener;

/**
 * Created by kim on 12/03/2018.
 */

public abstract class NextCMDHandler {

    public NextCMDHandler nextHandler;
    public abstract boolean handlerRequest(ICommandCallbackListener resultListener);

}
