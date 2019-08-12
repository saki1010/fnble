package com.fengniao.fnbluetooth.bluetooth.handler.cmdHandlerList;


import com.fengniao.fnbluetooth.bluetooth.handler.NextCMDHandler;
import com.fengniao.fnbluetooth.bluetooth.impl.ICommandCallbackListener;

/**
 * Created by kim on 12/03/2018.
 */

public class CmdHandler extends NextCMDHandler {

    @Override
    public boolean handlerRequest(ICommandCallbackListener resultListener) {

//        resultListener.commandCallback(true);
        return false;
    }


}
