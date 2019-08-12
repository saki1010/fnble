package com.fengniao.fnbluetooth.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;

import com.fengniao.fnbluetooth.bluetooth.BluetoothLeService.BluetoothLeService;
import com.fengniao.fnbluetooth.bluetooth.adapter.BleResultData;
import com.fengniao.fnbluetooth.bluetooth.bluetoothInstance.FNBlueToothInstance;
import com.fengniao.fnbluetooth.bluetooth.bluetoothInstance.IBlueToothInstance;
import com.fengniao.fnbluetooth.bluetooth.impl.BleResultCallback;
import com.fengniao.fnbluetooth.bluetooth.impl.CallbackListenter;
import com.fengniao.fnbluetooth.bluetooth.impl.ICommandCallbackListener;
import com.fengniao.fnbluetooth.bluetooth.utils.BleUtils;
import com.fengniao.fnbluetooth.bluetooth.utils.MLog;

import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.fengniao.fnbluetooth.bluetooth.FNPageConstant.TAG_BLUETOOTH;
import static com.fengniao.fnbluetooth.bluetooth.utils.NetContract.COMMAND_KEY_TIME_OUT;
import static com.fengniao.fnbluetooth.bluetooth.utils.NetContract.GuardType.CommandTimeOut;

/**
 *  蓝牙工具类
 */
public class BluetoothHelper implements CallbackListenter {

    public static int NOT_SUPPORT_BLLE = 777;   //不支持蓝牙
    public static int REQUEST_ENABLE_BT = 600;  //请求打开蓝牙

    private static volatile BluetoothHelper INSTANCE = null;

    public static BluetoothHelper getInstance(){
        if (INSTANCE == null){
            synchronized (BluetoothHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new BluetoothHelper();
                }
            }
        }
        return INSTANCE;
    }

    private FNBlueToothInstance blueToothInstance;
    private Context mContext;
    public IBlueToothInstance initBlueTooth(Context context){
        //初始化蓝牙
        mContext = context;
        if (blueToothInstance == null) {
            blueToothInstance = new FNBlueToothInstance();
            blueToothInstance.init(context);
            context.registerReceiver(blueToothInstance.getGattUpdateReceiver(), makeGattUpdateIntentFilter());
            blueToothInstance.getMyBluetoothAdapter().addCallbackListenter(this);
        }
        return blueToothInstance;
    }

    /**
     *  用户是否允许打开蓝牙
     * @param isAllow
     */
    public void userAllowBle(boolean isAllow){
        if (isAllow) {//开启蓝牙成功，连接设备
            MLog.e("开启蓝牙成功");
            getBlueToothInstance().connected();
        } else {
            MLog.e("拒绝开启蓝牙");
            if (callBackListener !=null){
                callBackListener.onUserReject();
            }
        }
    }

    private BleResultCallback callBackListener;
    /**
     * 监测是否连接上蓝牙
     * @param isStartTimeOut 是否开启超时线程  false 不发送消息
     */
    public void tryToConnectBlueTooth(boolean isStartTimeOut, String mac, BleResultCallback callBackListener) {
        //是否支持蓝牙
        if (!blueToothInstance.isSupportBlueTooth()) {//不支持蓝牙，走接口
            if (isStartTimeOut && callBackListener != null){
                callBackListener.onBleConnect(false);
            }
            return;
        }
        this.callBackListener = callBackListener;
        blueToothInstance.setBleResultCallback(callBackListener);
        blueToothInstance.setCarAddress(mac);
        MLog.e("isEnabled=" + blueToothInstance.getMyBluetoothAdapter().getBluetoothAdapter().isEnabled());
        MLog.e("isConnected=" + isConnected());
        //支持蓝牙
        if (blueToothInstance.getMyBluetoothAdapter().getBluetoothAdapter().isEnabled() && isConnected()) {//蓝牙已连接
            Log.e(TAG_BLUETOOTH, "---------蓝牙已连接");
//            FileUtils.whiteBLELog(String.valueOf(User.get().getCurOrderDetail().getOrder_info().getOrder_id()),
//                    "蓝牙启动状态", "连接", false);
            if (isStartTimeOut && callBackListener != null){
                callBackListener.onBleConnect(true);
            }
            return;
        }else {
//            FileUtils.whiteBLELog(String.valueOf(User.get().getCurOrderDetail().getOrder_info().getOrder_id()),
//                    "蓝牙启动状态", "未连接", false);
        }
        setConnected(false);
        if (blueToothInstance == null) {//未初始化
            if (isStartTimeOut && callBackListener != null){
                callBackListener.onBleConnect(false);
            }
            return;
        }
        //是否开启超时线程
        FNBlueToothInstance.isStartTimeOut = isStartTimeOut;
        //打开蓝牙

        blueToothInstance.openBlueTooth(new ICommandCallbackListener() {
            @Override
            public void commandCallback(boolean result) {
                if (result) {//蓝牙已开启，连接设备,启动守护线程（5s）
                    blueToothInstance.connected();
                } else {
                    openBluetooth();
                }
            }
        });
    }

    /**
     * 执行蓝牙指令--新的逻辑,只执行指令，不在此方法做认证
     * @param commands
     * @param position
     */
    public void executeCommandsNew(final List<String> commands, final int position) {
        if (commands == null || commands.size() == 0) {
            if (callBackListener != null){
                callBackListener.onBleCommands(new BleResultData(false, false));
            }
            return;
        }
        if (position >= commands.size()) {
            //指令全部执行成功，发通知，执行下一步操作
            if (callBackListener != null){
                callBackListener.onBleCommands(new BleResultData(true, false));
            }
            return;
        }
        if (blueToothInstance == null && callBackListener != null) {
            callBackListener.onBleCommands(new BleResultData(false, false));
        }
        //执行指令,根据回调判断是否执行下一条指令
        Log.e(TAG_BLUETOOTH, "position：" + position + "  ###commands 下发指令:" + commands.get(position));
//        FileUtils.whiteBLELog(String.valueOf(User.get().getCurOrderDetail().getOrder_info().getOrder_id()),
//                "透传指令" + FileUtils.getTime(), commands.get(position), false);
        blueToothInstance.operate(commands.get(position), new ICommandCallbackListener() {
            @Override
            public void commandCallback(boolean result) {
                Log.e(TAG_BLUETOOTH, "bluetooth result: 执行结果" + result);
//                FileUtils.whiteBLELog(String.valueOf(User.get().getCurOrderDetail().getOrder_info().getOrder_id()),
//                        "指令执行结果", result ? "成功" : "失败", false);
                if (result) {
                    //true 指令执行成功 执行下一条指令
                    executeCommandsNew(commands, position + 1);
                } else {
                    if (callBackListener != null){
                        callBackListener.onBleCommands(new BleResultData(false, false));
                    }
                }
            }
        });

    }

    /**
     * 执行key校验
     */
    public void executeCommandsKey(final boolean hasKey, final String keyCommend, final String resetCommend, final boolean isReset){
        if (blueToothInstance == null) {
            if (callBackListener != null){
                callBackListener.onBleCommands(new BleResultData(false, false));
            }
        }
        final String commend = isReset ? resetCommend : keyCommend;
        Log.e(TAG_BLUETOOTH, "是否重置密码认证： " + isReset + "  ###commands 下发指令:" + commend);
        destoryGuardThread();   //先销毁再启动
        startGuardThread(CommandTimeOut, COMMAND_KEY_TIME_OUT);    //每条4秒超时
//        FileUtils.whiteBLELog(String.valueOf(User.get().getCurOrderDetail().getOrder_info().getOrder_id()),
//                isReset ? "重置密码指令" : "认证指令" +FileUtils.getTime(), commend, false);
        blueToothInstance.operate(commend, new ICommandCallbackListener() {
            @Override
            public void commandCallback(boolean result) {
//                if (hasKey && !isReset){//key结果,有key并且不是重置的
                if (hasKey){//key结果,有key并且不是重置的
                    BleUtils.setExcuteKeyResult(mContext, result);
                }
                //position为0时失败，表示key验证失败，此时应进行二次校验key_commend，二次失败后才发送EventBlueCommand(false)
                if (result) {   //此处加重置密码成功后的逻辑
                    //true 指令认证成功
                    MLog.e(FNPageConstant.TAG_BLUETOOTH, "认证成功");
                    if (isReset){  //再次认证key,传空重置密码，防止死循环
//                        FileUtils.whiteBLELog(String.valueOf(User.get().getCurOrderDetail().getOrder_info().getOrder_id()),
//                                "蓝牙执行状态", "重置密码成功", false);
                        executeCommandsKey(hasKey,keyCommend, "", false);
                    }else {
//                        FileUtils.whiteBLELog(String.valueOf(User.get().getCurOrderDetail().getOrder_info().getOrder_id()),
//                                "蓝牙执行状态","认证成功", false);
                        if (callBackListener != null){
                            callBackListener.onBleCommands(new BleResultData(true, true));
                        }
                    }
                } else {
                    if (isReset){   //二次认证失败
                        MLog.e(FNPageConstant.TAG_BLUETOOTH, "二次认证失败");
//                        FileUtils.whiteBLELog(String.valueOf(User.get().getCurOrderDetail().getOrder_info().getOrder_id()),
//                                "蓝牙执行状态", "重置密码失败", false);
                        if (callBackListener != null){
                            callBackListener.onBleCommands(new BleResultData(false, true));
                        }
                    }else {     //进行二次认证
//                        FileUtils.whiteBLELog(String.valueOf(User.get().getCurOrderDetail().getOrder_info().getOrder_id()),
//                                "蓝牙执行状态", "认证失败", false);
                        if (!TextUtils.isEmpty(resetCommend)){  //不为空
                            MLog.e(FNPageConstant.TAG_BLUETOOTH, "进行二次认证");
                            executeCommandsKey(hasKey,keyCommend, resetCommend, true);
                        }else { //没有二次认证指令，直接失败
                            if (callBackListener != null){
                                callBackListener.onBleCommands(new BleResultData(false, true));
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * 销毁守护线程
     */
    public void destoryGuardThread() {
        if (blueToothInstance == null) {
            return;
        }
        blueToothInstance.clearCommandCallbackListener();
    }

    /**
     *  开启守护线程
     * @param type
     * @param timeout
     */
    public void startGuardThread(int type, int timeout) {
        if (blueToothInstance == null) {
            return;
        }
        blueToothInstance.startGuardThread(type, timeout);
    }

    /**
     *  初始化广播接收内容
     * @return
     */
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE1);
        return intentFilter;
    }

    @Override
    public void callback(String address) {
        //1.查找到设备，开始连接设备
        scanDevice(address);
    }

    private void scanDevice(String addr) {
        Log.e(TAG_BLUETOOTH, "查找到设备，开始连接设备");
        if (blueToothInstance.getServiceConnection() == null) {
            Log.d(TAG_BLUETOOTH, "ServiceConnection is null");
            return;
        } else {
            Intent gattServiceIntent = new Intent(mContext, BluetoothLeService.class);
            mContext.bindService(gattServiceIntent, blueToothInstance.getServiceConnection(), BIND_AUTO_CREATE);
        }
        //关闭查找
        blueToothInstance.getMyBluetoothAdapter().enable_scan(false);
    }

    private void openBluetooth() {
        Log.e(TAG_BLUETOOTH, "打开蓝牙");
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        ((Activity)mContext).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    private void removeCallbackListener() {
        if (blueToothInstance != null && blueToothInstance.getMyBluetoothAdapter() != null) {
            blueToothInstance.getMyBluetoothAdapter().removeCallbackListener();
        }
    }

    /**
     * 蓝牙是否已连接
     *
     * @return
     */
    public boolean isConnected() {
        if (blueToothInstance != null) {
            return blueToothInstance.isConnected();
        }
        return false;
    }

    public void setConnected(boolean status) {
        if (blueToothInstance != null) {
            blueToothInstance.setConnected(status);
            blueToothInstance.getMyBluetoothAdapter().setScanEnable();
        }
    }

    public IBlueToothInstance getBlueToothInstance() {
        return blueToothInstance;
    }

    /**
     * 销毁蓝牙相关
     */
    public void destoryBluetooth(){
        if (blueToothInstance != null && blueToothInstance.getServiceConnection() != null && FNBlueToothInstance.isBindService) {
            //防止出现service not registered
            String serviceName = "com.fengniao.fnbluetooth.bluetooth.BluetoothLeService.BluetoothLeService";
            if (BleUtils.isServiceRunning(mContext, serviceName)){
                mContext.unbindService(blueToothInstance.getServiceConnection());
            }
        }
        if (blueToothInstance != null && blueToothInstance.getGattUpdateReceiver() != null)
            mContext.unregisterReceiver(blueToothInstance.getGattUpdateReceiver());
        removeCallbackListener();
        blueToothInstance = null;
        mContext = null;
    }

    /**
     *  断开连接
     */
    public void disConnectBle(){
        if (blueToothInstance != null &&  blueToothInstance.getMyBluetoothAdapter().getBluetoothAdapter().isEnabled()) {
            Log.e(TAG_BLUETOOTH, "-----------断开连接--------------：");
            blueToothInstance.disconnectBluetooth();
        }
    }

}
