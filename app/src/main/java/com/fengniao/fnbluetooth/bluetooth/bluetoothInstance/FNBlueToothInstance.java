package com.fengniao.fnbluetooth.bluetooth.bluetoothInstance;

import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.fengniao.fnbluetooth.bluetooth.BluetoothLeService.BluetoothLeService;
import com.fengniao.fnbluetooth.bluetooth.BluetoothLeService.IBlueToothService;
import com.fengniao.fnbluetooth.bluetooth.CheckoutData;
import com.fengniao.fnbluetooth.bluetooth.FNPageConstant;
import com.fengniao.fnbluetooth.bluetooth.utils.MD5Utils;
import com.fengniao.fnbluetooth.bluetooth.utils.MLog;
import com.fengniao.fnbluetooth.bluetooth.adapter.FNBluetoothAdapter;
import com.fengniao.fnbluetooth.bluetooth.adapter.IBluetoothAdapter;
import com.fengniao.fnbluetooth.bluetooth.impl.BleResultCallback;
import com.fengniao.fnbluetooth.bluetooth.impl.ICommandCallbackListener;
import com.fengniao.fnbluetooth.bluetooth.recivier.GattUpdateReceiver;
import com.fengniao.fnbluetooth.bluetooth.recivier.observer.MyObserver;
import com.fengniao.fnbluetooth.bluetooth.tryagainHandler.GuardThread;
import com.fengniao.fnbluetooth.bluetooth.utils.NetContract;
import com.fengniao.fnbluetooth.bluetooth.utils.NumbricUtil;

import java.util.List;

import static com.fengniao.fnbluetooth.bluetooth.FNPageConstant.TAG_BLUETOOTH;
import static com.fengniao.fnbluetooth.bluetooth.utils.NetContract.CONNECT_TIME_OUT;


/**
 * Created by kim on 09/02/2018.
 */

public class FNBlueToothInstance implements IBlueToothInstance {


    GuardThread guardThreadInstance;

    private GattUpdateReceiver mGattUpdateReceiver;

    private IBluetoothAdapter myBluetoothAdapter;

    private IBlueToothService mBluetoothLeService;

    private String addres;


    boolean connect_status_bit = false;
    private boolean mConnected = false;
    int connect_count = 0;

    private Context context;

    private BleResultCallback bleResultCallback;    //蓝牙结果回调


    //透传指令回传数据回调
    private ICommandCallbackListener commandCallbackListener;


    //标志sertvice有没有绑定，true:绑定，false:未绑定
    public static boolean isBindService = false;


    public void init(Context mContext) {
        context = mContext;
        if (myBluetoothAdapter == null) {
            myBluetoothAdapter = new FNBluetoothAdapter(mContext);
        }
        if (mGattUpdateReceiver == null) {
            mGattUpdateReceiver = new GattUpdateReceiver();
            mGattUpdateReceiver.attach(new ObserverRecever());
        }
        guardThreadInstance = new GuardThread();
    }

    public void setBleResultCallback(BleResultCallback bleResultCallback){
        this.bleResultCallback = bleResultCallback;
    }


    @Override
    public IBlueToothService getBluetoothLeService() {
        return mBluetoothLeService;
    }

    @Override
    public IBluetoothAdapter getMyBluetoothAdapter() {
        return myBluetoothAdapter;
    }


    @Override
    public GattUpdateReceiver getGattUpdateReceiver() {
        return mGattUpdateReceiver;
    }

    @Override
    public ServiceConnection getServiceConnection() {
        return mServiceConnection;
    }

    @Override
    public void connectBluetooth() {
        if (!mConnected) {
            //没有建立连接，先建立连接
            if (myBluetoothAdapter.getBluetoothAdapter() == null) {
                Toast.makeText(context, "该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!myBluetoothAdapter.getBluetoothAdapter().isEnabled()) {
                //蓝牙未开启，在此回调中开启蓝牙
//                callBackListener.callback();
            } else {
                //蓝牙已打开，直接搜索设备，建立连接
                myBluetoothAdapter.enable_scan(true);

            }
        }
    }


    @Override
    public boolean isSupportBlueTooth(){
        if (myBluetoothAdapter.getBluetoothAdapter() == null) {
            Toast.makeText(context, "该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void  openBlueTooth(final ICommandCallbackListener openCallbackListener){
        if (!myBluetoothAdapter.getBluetoothAdapter().isEnabled()) {
            //蓝牙未开启，在此回调中开启蓝牙
            openCallbackListener.commandCallback(false);
        }else {//蓝牙已开启，尝试连接设备
            openCallbackListener.commandCallback(true);
        }
    }



    public  static  boolean isStartTimeOut = true;
    @Override
    public void connected( ){
        //蓝牙已打开，直接搜索设备，建立连接
        myBluetoothAdapter.enable_scan(true);
        //开启守护线程
        if (isStartTimeOut){
            guardThreadInstance.startThread(NetContract.GuardType.connectTimeOut, CONNECT_TIME_OUT, bleResultCallback);
        }
    }



    @Override
    public void operate(final String cmd, final ICommandCallbackListener iCommandCallbackListener) {
        //如果已经连接，直接执行指令
        this.commandCallbackListener = iCommandCallbackListener;
        if (mConnected) {
            Log.e(TAG_BLUETOOTH, "operate2 cmd:" + cmd );
            mBluetoothLeService.command(cmd, false);
            return;
        }
    }

    @Override
    public boolean isConnected() {
        return mConnected;
    }

    @Override
    public void setConnected(boolean status) {
        mConnected = status;
    }


    @Override
    public void setCarAddress(String address) {
        this.addres = address;
        if (myBluetoothAdapter != null) {
            myBluetoothAdapter.setCarMacAddress(address);
        } else {
            Log.d(TAG_BLUETOOTH, "IBluetoothAdapter 未初始化！");
        }
    }

    //设备连接状态监测
    public class ObserverRecever implements MyObserver {
        @Override
        public void update(String message, byte[] data) {
            switch (message) {
                case BluetoothLeService.ACTION_GATT_CONNECTED:
                    //设备已连接
                    connect_status_bit = true;
                    Log.e(TAG_BLUETOOTH, "ACTION_GATT_CONNECTED");
                    break;
                case BluetoothLeService.ACTION_GATT_DISCONNECTED:
                    //设备已断开
                    mConnected = false;
                    connect_status_bit = false;
//                    context.unbindService(mServiceConnection);
                    if (isBindService){ //只有绑定后才能解绑
                        context.unbindService(mServiceConnection);
                    }
                    if (connect_count == 0) {
                        connect_count = 1;
                    }
                    //主动断开连接
                    disconnectBluetooth();
                    break;
                case BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED:
                    //
                    if (mBluetoothLeService != null)
                        displayGattServices(mBluetoothLeService.getSupportedGattServices());
                    break;
                case BluetoothLeService.ACTION_DATA_AVAILABLE://透传数据
                    //
                    displayData(data);
                    break;

                case BluetoothLeService.ACTION_DATA_AVAILABLE1://透传数据
                    //
                    Log.e(TAG_BLUETOOTH, "ACTION_DATA_AVAILABLE1");
//                    displayData(data);
                    break;
            }
        }
    }


    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            //成功绑定service
            isBindService = true;
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG_BLUETOOTH, "Unable to initialize Bluetooth");
            }
            boolean isConnect = mBluetoothLeService.connect(addres);
//            mConnected = isConnect;
            MLog.e("onServiceConnected==> " + isConnect);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBindService = false;//解绑
            mBluetoothLeService = null;
        }
    };


    private void displayGattServices(List<BluetoothGattService> gattServices) {

        if (gattServices == null) return;

        if (gattServices.size() > 0 && mBluetoothLeService.get_connected_status(gattServices) == 2) {
            connect_count = 0;
            if (connect_status_bit) {
                mConnected = true;
                Log.e(TAG_BLUETOOTH, "蓝牙连接成功---------" );
                if (isStartTimeOut){
//                    EventBus.getDefault().post(new EventBlueConnect(true));
                    if (bleResultCallback != null){
                        bleResultCallback.onBleConnect(true);
                    }

                }

                mBluetoothLeService.Delay_ms(100);
                mBluetoothLeService.enable_JDY_ble(0);
                mBluetoothLeService.Delay_ms(100);
                mBluetoothLeService.enable_JDY_ble(1);
                mBluetoothLeService.Delay_ms(100);

                byte[] WriteBytes = new byte[2];
                WriteBytes[0] = (byte) 0xE7;
                WriteBytes[1] = (byte) 0xf6;
                mBluetoothLeService.function_data(WriteBytes);
                //连接成功了才去设置密码？
                enable_pass();
                //TODO 连接成功，返回回调
                mBluetoothLeService.Delay_ms(100);


            } else {
                Toast toast = Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else if (gattServices.size() > 0 && mBluetoothLeService.get_connected_status(gattServices) == 1) {
            connect_count = 0;
            if (connect_status_bit) {
                mConnected = true;
//                show_view( true );

                mBluetoothLeService.Delay_ms(100);
                mBluetoothLeService.enable_JDY_ble(0);

//                updateConnectionState(R.string.connected);

                //enable_pass();
            } else {
                //Toast.makeText(this, "Deleted Successfully!", Toast.LENGTH_LONG).show();
            }
        } else {

        }
    }


    /**
     * 透传数据
     *
     * @param data1
     */
    private void displayData(byte[] data1) {

        //tbox向APP发送的数据分两种
        //1.主动发送的数据
        //2.tbox接受APP发送的数据后响应的数据
        if (CheckoutData.isResponseData(data1)) {//指令响应数据--蓝牙结果

            //回调返回结果
            if (commandCallbackListener != null) {
//                FileUtils.whiteBLELog(String.valueOf(User.get().getCurOrderDetail().getOrder_info().getOrder_id()),
//                        "响应数据" + FileUtils.getTime(),
//                        MD5Utils.byteArrayToHexString(data1), false);
//                User.get().setCommendResult(CheckoutData.transData(MD5Utils.byteArrayToHexString(data1)));
                Log.e(TAG_BLUETOOTH, "指令响应数据 data1：" + MD5Utils.byteArrayToHexString(data1)  + "   " + CheckoutData.checkoutResponse(data1));
                commandCallbackListener.commandCallback(CheckoutData.checkoutResponse(data1));
//                UploadLogUtils.writeLog("指令响应数据 " + CheckoutData.checkoutResponse(data1), MD5Utils.byteArrayToHexString(data1));
            }
        } else {    //蓝牙返回数据
            Log.e(TAG_BLUETOOTH, "接受到主动发送的数据：" + MD5Utils.byteArrayToHexString(data1));
            //接受tbox主动发送的数据，做处理

            //TODO 先注销主动数据上报处理逻辑
//            String data = MD5Utils.byteArrayToHexString(data1);
//            StreamUtils.write("bytesToHexString : " + data);
//            Log.e(FNPageConstant.TAG_BLUETOOTH, "接受到主动发送的数据:" + data);

            //建立数据池，完全接受tbox数据
            String tempData = CheckoutData.dataPools(data1);
            if (!TextUtils.isEmpty(tempData)) {
                //检验数据
                if (tempData.length() > 10) {
//                    Log.e(FNPageConstant.TAG_BLUETOOTH, "上报数据:" + tempData);
                    String command = tempData.substring(10, tempData.length() - 4);

                    String last = tempData.substring(tempData.length() - 4, tempData.length() - 2);
                    byte[] cccc = NumbricUtil.hexStringToByteArray(command);
                    String xor = NumbricUtil.toHex(2, NumbricUtil.XOR(cccc));
                    //校验数据
                    if (tempData.substring(tempData.length() - 4, tempData.length() - 2).equals(NumbricUtil.toHex(2, NumbricUtil.XOR(cccc)))) {
                        //数据正确，上传
                        Log.e(TAG_BLUETOOTH, "校验数据正确:" + command);
                        Log.e(TAG_BLUETOOTH, "蓝牙透传回来数据:" + tempData);
//                        EventBus.getDefault().post(new BaseEventMsg(1, command));
                    }
                }
            }
        }
    }


    public void enable_pass() {
        mBluetoothLeService.Delay_ms(100);
        mBluetoothLeService.set_APP_PASSWORD("123456");

    }

    public void clearCommandCallbackListener() {
        commandCallbackListener = null;
        //清除守护线程
        if (guardThreadInstance != null){
            guardThreadInstance.destoryGuardThread();
        }
    }


    public void startGuardThread(int type,int timeOut){
        if (guardThreadInstance != null){
            guardThreadInstance.startThread(type,timeOut, bleResultCallback);
        }
    }

    @Override
    public void disconnectBluetooth() {
        if (myBluetoothAdapter.getBluetoothAdapter() != null  && myBluetoothAdapter.getBluetoothAdapter().enable()) {
            if (getBluetoothLeService() !=null){
                getBluetoothLeService().disconnect();
            }
        }
    }

}
