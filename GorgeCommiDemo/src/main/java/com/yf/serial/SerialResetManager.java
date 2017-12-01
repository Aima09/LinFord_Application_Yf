/**
 * SerialResetManager.java[V 1.0.0]
 * @author xujie@yf-space.com create on 2015-7-6
 */

package com.yf.serial;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.yf.serial.yfpublic.utils.CommonLog;
import com.yf.serial.yfpublic.utils.LogFactory;


public class SerialResetManager {
	private static final CommonLog log = LogFactory.createLog(SerialResetManager.class.getSimpleName());
	private static final int WAIT_SECOND = 15 * 60; //30; // 5 * 60;

	public static void start(Context con) {
		log.i("start");
		AlarmManager manager = (AlarmManager) (con.getSystemService(Context.ALARM_SERVICE));

		Intent intent = new Intent(con, SerialResetManagerBroadcastReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(con, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		long triggerAtTime = SystemClock.elapsedRealtime();
		manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime, WAIT_SECOND * 1000, sender);
	}

	public static void stop(Context con) {
		log.i("stop");
		AlarmManager manager = (AlarmManager) (con.getSystemService(Context.ALARM_SERVICE));
		Intent intent = new Intent(con, SerialResetManagerBroadcastReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(con, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		manager.cancel(sender);
	}
}
