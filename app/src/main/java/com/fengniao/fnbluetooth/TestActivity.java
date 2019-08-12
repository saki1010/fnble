package com.fengniao.fnbluetooth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.fengniao.fnbluetooth.bluetooth.BluetoothHelper;
import com.fengniao.fnbluetooth.bluetooth.adapter.BleResultData;
import com.fengniao.fnbluetooth.bluetooth.bluetoothInstance.IBlueToothInstance;
import com.fengniao.fnbluetooth.bluetooth.impl.BleResultCallback;
import com.fengniao.fnbluetooth.bluetooth.utils.MLog;
import com.fengniao.fnbluetooth.bluetooth.utils.PermissionHelper;

import java.util.ArrayList;
import java.util.List;

import static com.fengniao.fnbluetooth.bluetooth.BluetoothHelper.REQUEST_ENABLE_BT;


/**
 * Created by kim on 28/12/2017.
 */

public class TestActivity extends AppCompatActivity {



    IBlueToothInstance blueToothInstance;
    String address = "A4:C1:38:04:02:F1";   //测试--395
    String command = "4031010102020D"; //关锁命令
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        context = this;
        initBle();
    }

    private void initBle(){
        blueToothInstance = BluetoothHelper.getInstance().initBlueTooth(this);

        findViewById(R.id.tv_service_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothHelper.getInstance().tryToConnectBlueTooth(true, address, new BleResultCallback() {
                    @Override
                    public void onUserReject() {
                        MLog.e("TestActivity==> 用户拒绝开启蓝牙");
                    }

                    @Override
                    public void onBleConnect(boolean isConnect) {
                        MLog.e("TestActivity==>" + (isConnect ? "蓝牙连接成功" : "蓝牙连接失败"));
                        if (isConnect){
                            BluetoothHelper.getInstance().destoryGuardThread();
                            List<String> list = new ArrayList<>();
                            list.add(command);
                            BluetoothHelper.getInstance().executeCommandsNew(list, 0);
                        }
                    }

                    @Override
                    public void onBleCommands(BleResultData bleData) {
                        BluetoothHelper.getInstance().destoryGuardThread();
                        if (bleData.isAuthResult()){
                            MLog.e("TestActivity==>" + (bleData.isAuthResult() ? "蓝牙认证成功" : "蓝牙认证失败"));
                        }else {
                            MLog.e("TestActivity==>" + (bleData.isResult() ? "蓝牙执行成功" : "蓝牙执行失败"));
                        }
                    }
                });
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            BluetoothHelper.getInstance().userAllowBle(resultCode == RESULT_OK);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        BluetoothHelper.getInstance().destoryBluetooth();
    }
}
