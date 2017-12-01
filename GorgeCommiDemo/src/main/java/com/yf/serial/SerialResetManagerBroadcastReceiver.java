/**
 * SerialResetManagerBroadcastReceiver.java[V 1.0.0]
 * @author xujie@yf-space.com create on 2015-7-6
 */

package com.yf.serial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yf.serial.yfpublic.utils.CommonLog;
import com.yf.serial.yfpublic.utils.LogFactory;


public class SerialResetManagerBroadcastReceiver extends BroadcastReceiver {
	private static final CommonLog log = LogFactory
			.createLog(SerialResetManagerBroadcastReceiver.class.getSimpleName());

	/** (non-Javadoc)
	 * @see BroadcastReceiver#onReceive(Context, Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		log.i("start service");
		// x x x x 将485 Service重启 x x x x
		// context.stopService(new Intent(context, RS485Service.class));
		context.startService(new Intent(context, RS485Service.class));
		// 将zigbee Service重启
		// ??
	}

}

