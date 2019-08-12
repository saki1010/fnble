package com.fengniao.fnbluetooth.bluetooth.adapter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.fengniao.fnbluetooth.bluetooth.JDY_type;
import com.fengniao.fnbluetooth.bluetooth.utils.MLog;
import com.fengniao.fnbluetooth.bluetooth.impl.CallbackListenter;

import static com.fengniao.fnbluetooth.bluetooth.FNPageConstant.TAG_BLUETOOTH;


/**
 * Created by kim on 15/01/2018.
 */

public class FNBluetoothAdapter implements IBluetoothAdapter {

    private Context mContext;
    //
    BluetoothAdapter bluetoothAdapter;
    //
    BluetoothManager bluetoothManager;

    private CallbackListenter callbackListenter;

    //MAC地址
    private String mac;

    public FNBluetoothAdapter(Context context) {
        this.mContext = context;
        bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    @Override
    public void addCallbackListenter(CallbackListenter callbackListenter){
        this.callbackListenter = callbackListenter;
    }

    @Override
    public void removeCallbackListener() {
        this.callbackListenter = null;
    }

    @Override
    public void setCarMacAddress(String address) {
        mac = address;
    }

    @Override
    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    @Override
    public void enable_scan(boolean enable) {
        MLog.e("是否在查找设备" + bluetoothAdapter.isDiscovering());
        if (enable) {
            Log.e(TAG_BLUETOOTH,"开始搜索");
            bluetoothAdapter.startLeScan(leScanCallback);//开始搜索
        } else {
            Log.e(TAG_BLUETOOTH,"结束搜索");
            bluetoothAdapter.stopLeScan(leScanCallback);
        }
    }


    /**
     * 查找设备
     */
    private int scan_int = 0;

    public void setScanEnable(){
        isStop = true;
    }

    private boolean isStop = true;
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            ++scan_int;
            if (isStop) {
                scan_int = 0;
//                MLog.e("onLeScan==>" + Arrays.toString(scanRecord));
//                MLog.e("onLeScan mac==>" + device.getAddress());
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    JDY_type m_tyep = dv_type(scanRecord);
                    if (m_tyep == JDY_type.JDY && m_tyep != null) {
                        if (!TextUtils.isEmpty(mac)){
                            MLog.e(TAG_BLUETOOTH,"mac:" + mac);
                            MLog.e(TAG_BLUETOOTH,"device address:" + device.getAddress());

                            String address =device.getAddress();

                            if (mac.equals(device.getAddress())) {
                                //查找成功
                                Log.e(TAG_BLUETOOTH,"查找成功");

                                callbackListenter.callback(device.getAddress());
                                //结束查找
                                isStop = false;

                            }
                        }
                    }
                } else {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JDY_type m_tyep = dv_type(scanRecord);
                            if (m_tyep == JDY_type.JDY && m_tyep != null) {
                                Toast.makeText(mContext, "name:" + device.getName() + "address:" + device.getAddress(), Toast.LENGTH_SHORT).show();

                                if (!TextUtils.isEmpty(mac)){
                                    if (mac.equals(device.getAddress())) {//A4:C1:38:77:1D:FD
                                        Log.e(TAG_BLUETOOTH,"查找成功");
                                        //查找成功
                                        callbackListenter.callback(device.getAddress());
                                        //结束查找
                                        isStop = false;
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }
    };


    byte dev_VID = -120;
    @Override
    public JDY_type dv_type(byte[] p) {
        if (p.length != 62) {
            return null;
        } else {
            boolean ib1_major = false;
            boolean ib1_minor = false;
            if (p[52] == -1 && p[53] == -1) {
                ib1_major = true;
            }

            if (p[54] == -1 && p[55] == -1) {
                ib1_minor = true;
            }

            //原來第13位只匹配-120，现在加上了52
            if (p[5] == -32 && p[6] == -1 && (this.dev_VID == p[13] || 52 == p[13])) {
                byte[] WriteBytes = new byte[]{p[13], p[14], (byte) 0, (byte) 0};
                return p[14] == -96 ? JDY_type.JDY : (p[14] == -91 ? JDY_type.JDY_AMQ : (p[14] == -79 ? JDY_type.JDY_LED1 : (p[14] == -78 ? JDY_type.JDY_LED2 : (p[14] == -60 ? JDY_type.JDY_KG : (p[14] == -59 ? JDY_type.JDY_KG1 : JDY_type.JDY)))));
            } else {
                return p[44] == 16 && p[45] == 22 && (ib1_major || ib1_minor) ? JDY_type.sensor_temp : (p[44] == 16 && p[45] == 22 ? (p[57] == -32 ? JDY_type.JDY_iBeacon : (p[57] == -31 ? JDY_type.sensor_temp : (p[57] == -30 ? JDY_type.sensor_humid : (p[57] == -29 ? JDY_type.sensor_temp_humid : (p[57] == -28 ? JDY_type.sensor_fanxiangji : (p[57] == -27 ? JDY_type.sensor_zhilanshuibiao : (p[57] == -26 ? JDY_type.sensor_dianyabiao : (p[57] == -25 ? JDY_type.sensor_dianliu : (p[57] == -24 ? JDY_type.sensor_zhonglian : (p[57] == -23 ? JDY_type.sensor_pm2_5 : JDY_type.JDY_iBeacon)))))))))) : JDY_type.UNKW);
            }
        }
    }
}
