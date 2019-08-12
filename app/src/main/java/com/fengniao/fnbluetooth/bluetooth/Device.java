package com.fengniao.fnbluetooth.bluetooth;

/**
 * Created by kim on 20/11/2017.
 */

public class Device {

    //蓝牙地址
    private String address;
    //设备名称
    private String devName;

    public Device(String devName, String address) {
        this.address = address;
        this.devName = devName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }
}
