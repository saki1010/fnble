# fnble
蓝牙操作  
Step 1. Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.saki1010:fnble:版本号'
	}
Step 3. 在Manifest文件中添加

	<service android:name="com.fengniao.fnbluetooth.bluetooth.BluetoothLeService.BluetoothLeService" android:enabled="true"/>
初始化操作

	BluetoothHelper.getInstance().initBlueTooth(this)
注销操作

	BluetoothHelper.getInstance().destoryBluetooth()
调用示例
	
	BluetoothHelper.getInstance().tryToConnectBlueTooth(true, address, new BleResultCallback() {
                    @Override
                    public void onUserReject() {  //用户拒绝开启蓝牙
                        MLog.e("TestActivity==> 用户拒绝开启蓝牙");
                    }

                    @Override
                    public void onBleConnect(boolean isConnect) {  //蓝牙连接状态
                        MLog.e("TestActivity==>" + (isConnect ? "蓝牙连接成功" : "蓝牙连接失败"));
                        if (isConnect){
			    	//销毁守护线程
                            BluetoothHelper.getInstance().destoryGuardThread();
			    	//执行指令等操作
                            List<String> list = new ArrayList<>();
                            list.add(command);
                            BluetoothHelper.getInstance().executeCommandsNew(list, 0);
                        }
                    }

                    @Override
                    public void onBleCommands(BleResultData bleData) {	//指令执行结果
                        BluetoothHelper.getInstance().destoryGuardThread();
			    //bleData.isResult() 指令执行结果，是否成功
			    //bleData.isAuthResult() 认证结果，有认证操作时做此判断
                        if (bleData.isAuthResult()){	
                            MLog.e("TestActivity==>" + (bleData.isAuthResult() ? "蓝牙认证成功" : "蓝牙认证失败"));
                        }else {
                            MLog.e("TestActivity==>" + (bleData.isResult() ? "蓝牙执行成功" : "蓝牙执行失败"));
                        }
                    }
                });
