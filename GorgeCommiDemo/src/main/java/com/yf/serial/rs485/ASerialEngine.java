/**
 * ASerialEngine.java[V 1.0.0]
 * classes: com.YF.YuanFang.YFServer.serial.rs485.ASerialEngine
 * xuie	create 2015-4-30 ����9:40:29
 */

package com.yf.serial.rs485;

import android.app.Service;
import android.content.Intent;

import com.orhanobut.logger.Logger;
import com.yf.serial.SerialPort;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;


/**
 * com.YF.YuanFang.YFServer.serial.rs485.ASerialEngine
 * 
 * @author xuie <br/>
 *         create at 2015-4-30 ����9:40:29
 */

public abstract class ASerialEngine extends Service {
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private SendThread mSendThread;

    private byte[] mSendBuffer;
    private boolean bReading = true;

    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (bReading) {
                int size;
                try {
                    synchronized (this) {
                        byte[] buffer = new byte[64];
                        if (mInputStream == null)
                            return;
                        size = mInputStream.read(buffer);
                        // ////////
                        for (int i = 0; i < size; i++) {
                            Logger.i(" engine= "+(buffer[i] & 0xFF) + " ");
                        }
                        // /////
                        if (size > 0) {
                            analyzeData(buffer, size);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private class SendThread extends Thread {
        @Override
        public void run() {
            // while (!isInterrupted()) ;
            try {
                if (mOutputStream != null) {

                    if (mSendBuffer != null) {
                        byte[] buffer = mSendBuffer;
                        mSendBuffer = null;
                        // enable 485 rcv
                        switchDirection(ENABLE_SEND);

                        // ///////////////
                        System.out.print("--> ");
                        for (int i = 0; i < buffer.length; i++) {
                            System.out.print((buffer[i] & 0xFF) + " ");
                        }
                        System.out.println();
                        // ////////////

                        mOutputStream.write(buffer);
                        mOutputStream.flush();

                        try {
                            int time = buffer.length + 10;
                            System.out.println("sleep " + time + "ms");
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        buffer = null;

                        // enable 485 rcv
                        switchDirection(ENABLE_RCV);
                        // System.out.println("send end " + val);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;

            }
        }
    }

    protected abstract void analyzeData(final byte[] buffer, final int size);

    protected synchronized void transferData(final byte[] buffer, final int size) {
        for (int i = 0; i < buffer.length; i++) {
            System.out.print((buffer[i] & 0xFF) + " ");
        }
        System.out.println();
        System.out.println("rs485 send data");
        mSendBuffer = buffer;
        if (mSendThread == null) {
            mSendThread = new SendThread();
            mSendThread.start();
        } else {
            if (mSendThread.isAlive()) {
                Logger.e("mSendThread.isAlive() !!!");
                return;
            }
            mSendThread.run();
        }
        // new SendThread().start();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        closeStream();
        bReading = false;

        // 获取波特率
        //if (config_server.isSuokete()) {
        //    BAUDRATE = PreferenceUtils.getPrefInt(this, config_server.RS485_BAUDRATE, 4800);
        //} else {
        //    BAUDRATE = PreferenceUtils.getPrefInt(this, config_server.RS485_BAUDRATE, 9600);
        //}
        //sjt
        BAUDRATE=9600;
        Logger.d("current baudrate is " + BAUDRATE);
        mSendBuffer = new byte[1024];

        try {
            mSerialPort = new SerialPort(new File("/dev/ttyS1"), BAUDRATE, 0);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

			/* Create a receiving thread */
            mReadThread = new ReadThread();
            bReading = true;
            mReadThread.start();

            switchDirection(ENABLE_RCV);

            Logger.i("Create RS485 Port -> ttyS1, bps " + getBAUDRATE());
        } catch (SecurityException e) {
            Logger.e("getResources().getString(R.string.error_security)");
        } catch (IOException e) {
            Logger.e("getResources().getString(R.string.error_unknown)");
        } catch (InvalidParameterException e) {
            Logger.e("getResources().getString(R.string.error_configuration)");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d("onStartCommand");
        if (intent != null && intent.getBooleanExtra("buradRrate", false)) {
            Logger.d("onStartCommand init");
            init();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("onDestroy!");
        closeStream();
    }

    private void closeStream() {
        try {
            mInputStream.close();
            mInputStream = null;

            mOutputStream.close();
            mOutputStream = null;
        } catch (Exception e) {
        }
    }

    private final String CON_485_PATH = "/sys/bus/platform/drivers/rk29-keypad/rk29-keypad/control_485_func";
    private final String CON_485_V209_PATH = "/sys/bus/i2c/drivers/rt3261/control485Data";
    private static byte ENABLE_RCV = 1;
    private static byte ENABLE_SEND = 2;

    private int switchDirection(byte b) {
        try {
            OutputStream output = null;
            //if (config_server.is209()) {
            //    output = new FileOutputStream(CON_485_V209_PATH);
            //} else {
            //    output = new FileOutputStream(CON_485_PATH);
            //}
            //sjt
            output = new FileOutputStream(CON_485_PATH);

            output.write(b);
            output.flush();
            output.close();
        } catch (IOException e) {
            Logger.e("open file error . " + e);
        }
        return 0;
    }

    private int BAUDRATE;

    public int getBAUDRATE() {
        return BAUDRATE;
    }

    public void setBAUDRATE(int bAUDRATE) {
        BAUDRATE = bAUDRATE;
        //PreferenceUtils.setPref(this, config_server.RS485_BAUDRATE, bAUDRATE);
    }

}
