package com.fengniao.fnbluetooth.bluetooth.recivier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.fengniao.fnbluetooth.bluetooth.BluetoothLeService.BluetoothLeService;
import com.fengniao.fnbluetooth.bluetooth.FNPageConstant;
import com.fengniao.fnbluetooth.bluetooth.recivier.observer.MyObserver;
import com.fengniao.fnbluetooth.bluetooth.recivier.observer.Subject;


/**
 * Created by kim on 15/01/2018.
 */

public class GattUpdateReceiver extends BroadcastReceiver implements Subject {

    private MyObserver obserber;


    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();
        if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {

            notify(BluetoothLeService.ACTION_GATT_CONNECTED,null);

        } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {

            notify(BluetoothLeService.ACTION_GATT_DISCONNECTED,null);

        } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
            // Show all the supported services and characteristics on the user interface.
            //
            notify(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED,null);

        } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)){

            //byte data1;
            //intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);//  .getByteExtra(BluetoothLeService.EXTRA_DATA, data1);
            notify(BluetoothLeService.ACTION_DATA_AVAILABLE,intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA));
        }else if (BluetoothLeService.ACTION_DATA_AVAILABLE1.equals(action)){
            notify(BluetoothLeService.ACTION_DATA_AVAILABLE1,intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA));
        }
    }


    @Override
    public void attach(MyObserver observer) {
        this.obserber = observer;
    }

    @Override
    public void detach(MyObserver observer) {
        this.obserber = null;

    }

    @Override
    public void notify(String message,byte[] data) {

        Log.e(FNPageConstant.TAG_BLUETOOTH,message);
        obserber.update(message,data);

    }
}
