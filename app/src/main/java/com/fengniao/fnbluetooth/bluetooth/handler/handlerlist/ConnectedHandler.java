package com.fengniao.fnbluetooth.bluetooth.handler.handlerlist;


import com.fengniao.fnbluetooth.bluetooth.handler.NextHandler;
import com.fengniao.fnbluetooth.bluetooth.impl.ResultListener;

/**
 * Created by kim on 22/01/2018.
 */

public class ConnectedHandler extends NextHandler {
    @Override
    public void handlerRequest(ResultListener resultListener) {

        if (true){

        }else {
            nextHandler.handlerRequest(resultListener);
        }
    }

}
