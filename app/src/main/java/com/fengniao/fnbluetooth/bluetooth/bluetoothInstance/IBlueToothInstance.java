package com.fengniao.fnbluetooth.bluetooth.bluetoothInstance;

import android.content.Context;
import android.content.ServiceConnection;

import com.fengniao.fnbluetooth.bluetooth.BluetoothLeService.IBlueToothService;
import com.fengniao.fnbluetooth.bluetooth.adapter.IBluetoothAdapter;
import com.fengniao.fnbluetooth.bluetooth.impl.ICommandCallbackListener;
import com.fengniao.fnbluetooth.bluetooth.recivier.GattUpdateReceiver;


/**
 * Created by kim on 08/03/2018.
 */

public interface IBlueToothInstance {


    void init(Context mContext);

    IBlueToothService getBluetoothLeService();

    IBluetoothAdapter getMyBluetoothAdapter();

    GattUpdateReceiver getGattUpdateReceiver();

    ServiceConnection getServiceConnection();


    void connectBluetooth();



    boolean isSupportBlueTooth();

    void openBlueTooth(final ICommandCallbackListener openCallbackListener);

    void connected();


    /**
     * 下发操作指令
     *
     * @param cmd
     */
//    void operate(final String cmd);

    /**
     * 下发操作指令
     *
     * @param cmd
     */
    void operate(final String cmd, ICommandCallbackListener iCommandCallbackListener);


    boolean isConnected();

    void setConnected(boolean status);


    /**
     * 传入MAC地址
     *
     * @param address
     */
    void setCarAddress(String address);




    void clearCommandCallbackListener();


    /**
     * 断开蓝牙
     */
    void disconnectBluetooth();


    /**
     * 开启守护线程
     * @param type
     * @param timeOut
     */
    void startGuardThread(int type, int timeOut);
}
