/**
 * RS485Service.java[V 1.0.0]
 * classes: com.YF.YuanFang.YFServer.serial.rs485.RS485Service
 * xuie	create 2015-4-30 ����9:51:17
 */

package com.yf.serial;

import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.yf.serial.rs485.ASerialEngine;

import java.util.Arrays;

public class RS485Service extends ASerialEngine {
	private byte[] mStoreBuffer; // 存储BUFFER容器
	private int mPosition; // BUFFER末尾位置
	private String mCommand;
	private String mParameter; // 参数功能
	private int mRoomNumber; // 房间号
	private Analyzer mAnalyzer;
	private Executor mExecutor;
	private FeedBacker mFeedBacker;

	private String recevedData;

	@Override
	public void onCreate() {
		super.onCreate();
		Logger.d("onCreate");
		mFeedBacker = new FeedBacker();
		mFeedBacker.setIISend(ISend);

		mExecutor = new Executor(this);
		mExecutor.setISerial(mFeedBacker);

		mAnalyzer = new Analyzer();
		mAnalyzer.setISerial(IAnalyzer);

		mStoreBuffer = new byte[256];
		clearMask();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		Logger.d("onDestroy");
		if (mExecutor != null) {
			mExecutor.exit();
		}
		super.onDestroy();
	}

	private Analyzer.ISerial IAnalyzer = new Analyzer.ISerial() {
		@Override
		public void callBack(int roomNumber, String function, String partemterString) {
			mRoomNumber = roomNumber;
			mCommand = function;
			mParameter = partemterString;
		}

		@Override
		public void initBuradrate(int buradRrate) {
			int burad = getBAUDRATE();
			if (burad != buradRrate) {
				setBAUDRATE(buradRrate);
				Intent intent = new Intent(RS485Service.this, RS485Service.class);
				intent.putExtra("buradRrate", true);
				startService(intent);
			}
		}

		@Override
		public int getBuradrate() {
			return getBAUDRATE();
		}

		@Override
		public void callAccess(boolean suc) {
			if (suc) {
				new Handler(getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
//						Toast.makeText(RS485Service.this, //
//								getResources().getString(R.string.zigbee_net_suc), Toast.LENGTH_LONG).show();
					}
				});
			}
		}
	};

	/**
	 * 存储数据函数 --- 即收集以0xFA为开头，0xFE为结尾的数据
	 * @return true:成功; false：失败
	 */

	private boolean storeData(byte[] buffer, int size) {
		// 比较起始符
		if (0xFA == (buffer[0] & 0xFF)) {
			Arrays.fill(mStoreBuffer, (byte) 0xff);
			for (int i = 0; i < size; i++) {
				mStoreBuffer[i] = buffer[i];
			}
			mPosition = size;
			// logger.i("[rs485 start] " + mPosition);
		} else {
			if (mPosition == 0) {
				Logger.d("[mPosition = 0] " + size);// ERROR_FB_UNUSUAL
				return false;
			}

			if (mPosition + size > 16/*1023*/) {
				Logger.e("[exp data!!!] " + mPosition);
				return true;
			}

			for (int i = 0; i < size; i++) {
				mStoreBuffer[mPosition + i] = buffer[i];
			}
			mPosition += size;
		}

		// 验证结束符
		if (0xFE == (buffer[size - 1] & 0xFF)) {
			// logger.i("[End] " + mPosition);
			return true;// 正确结束标记，发送一次成功命令
		}
		// logger.i("[Continue] " + mPosition);

		return false;
	}

	private void clearMask() {
		mPosition = 0;
		Arrays.fill(mStoreBuffer, (byte) 0xff);
	}

	private long preIniTime = System.currentTimeMillis();
	private long curIniTime;

	private void filterInitialize() {
		curIniTime = System.currentTimeMillis();
		if (curIniTime - preIniTime > 150 * 1000) {
			Logger.i("" + (curIniTime - preIniTime));
			clearMask();
		}
		preIniTime = curIniTime;
	}

	/** 屏蔽恶意行为 */
	private long preInstructionTime = System.currentTimeMillis();
	private long curInstructionTime;

	private boolean filterFrequentlyInstruction() {
		curInstructionTime = System.currentTimeMillis();
		if (curInstructionTime - preInstructionTime < 49) {
			Logger.d("[frequently instruction " + (curInstructionTime - preInstructionTime) + "ms]");
			clearMask();
			return true;
		}
		preInstructionTime = curInstructionTime;
		return false;
	}

	@Override
	protected synchronized void analyzeData(final byte[] buffer, final int size) {
		// true - 收集成功；false - 收集不全
		// filterInitialize();

		if (storeData(buffer, size)) {
			mCommand = null;

			if (filterFrequentlyInstruction()) {
				clearMask();
				return;
			}

			mAnalyzer.handle(mStoreBuffer, mPosition);

			clearMask();
			Logger.i("mCommand = "+mCommand);
			mExecutor.handle(mRoomNumber, mCommand, mParameter);

			for (int i = 0; i < size; i++) {
				recevedData=(buffer[i] & 0xFF) + " ";
				Log.i(TAG,"engine received===============>"+(buffer[i] & 0xFF));
				Logger.i(" engine received===============>"+(buffer[i] & 0xFF) + " ");
			}
		}
	}

	private ISSend ISend = new ISSend() {
		@Override
		public void transferData(byte[] buffer, int size) {
			RS485Service.this.transferData(buffer, size);
		}
	};

	private final IBinder binder = new MyBinder();

	public class MyBinder extends Binder {
		public RS485Service getService() {
			return RS485Service.this;
		}
		public ISSend getISSend(){
			return RS485Service.this.ISend;
		}
		public String getReceivedData(){
			return  recevedData;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
}
