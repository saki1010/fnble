package com.fengniao.fnbluetooth.bluetooth.BluetoothLeService;

import android.bluetooth.BluetoothGattService;

import java.util.List;

/**
 * Created by kim on 08/03/2018.
 */

public interface IBlueToothService {


    boolean initialize();

    void function_data(byte[] data);

    void enable_JDY_ble(int p);


    void disconnect();

    /**
     * 执行指令
     * @param g
     * @param string_or_hex_data
     * @return
     */
    int command(String g, boolean string_or_hex_data);


    void Delay_ms(int ms);

    /**
     * 设置密码
     * @param pss
     */
    void set_APP_PASSWORD(String pss);


    boolean connect(String address);


    /**
     * 获取连接的ble设备所提供的服务列表
     * @return
     */
    List<BluetoothGattService> getSupportedGattServices();


    int get_connected_status(List<BluetoothGattService> gattServices);


}
