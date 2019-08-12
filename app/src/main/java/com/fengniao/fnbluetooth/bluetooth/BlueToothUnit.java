package com.fengniao.fnbluetooth.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.lang.reflect.Method;


/**
 * Created by kim on 20/11/2017.
 */

public class BlueToothUnit {


    private final static String TAG = "--BlueToothUnit--";


    /**
     * 配对指定的设备
     */
    public static void bindTargetDevice(BluetoothAdapter localBluetoothAdapter, BluetoothDevice device) {
        //在配对之前，一定要停止搜搜
        if (localBluetoothAdapter.isDiscovering()) {
            localBluetoothAdapter.cancelDiscovery();
        }
        //获取配对的设备
        BluetoothDevice btDev = localBluetoothAdapter.getRemoteDevice("C4:07:2F:84:E5:EF");
        Boolean returnValue;
        if (btDev.getBondState() == BluetoothDevice.BOND_NONE) {
            try {
                //利用反射方法调用BluetoothDevice.createBond(BluetoothDevice remoteDevice);
                Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                Log.d(TAG, "开始配对");
                returnValue = (Boolean) createBondMethod.invoke(btDev);
                if (returnValue){
                    Log.d(TAG, "bindTargetDevice "+"配对成功");

                }
            }catch (Exception e){

            }

        }
    }




}
