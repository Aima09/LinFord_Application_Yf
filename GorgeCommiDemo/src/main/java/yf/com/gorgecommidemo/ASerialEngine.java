/**
 * ASerialEngine.java[V 1.0.0]
 * classes: com.YF.YuanFang.YFServer.serial.rs485.ASerialEngine
 * xuie	create 2015-4-30 ����9:40:29
 */

package yf.com.gorgecommidemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.yf.serial.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 定义接收数据和发送数据的线程
 * 设置串口的参数
 */
public class ASerialEngine extends Service implements SerialDataManager.SendData{
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private SendThread mSendThread;

    //定义比特率
    private int BAUDRATE = 38400;
    //定义串口程序路径
    private String path="/dev/ttyS0";
    private boolean bReading = true;
    private byte[] mSendBuffer = new byte[1024];

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        SerialDataManager.getInstance().setSendData(this);
        try {
            mSerialPort = new SerialPort(new File(path), BAUDRATE, 0);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

			/* Create a receiving thread */
            mReadThread = new ReadThread();
            mReadThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT;
    }

    @Nullable @Override public IBinder onBind(Intent intent) {
        return null;
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

    @Override public void sendData(byte[] buffer) {
        mSendBuffer = buffer;
        if (mSendThread == null) {
            mSendThread = new SendThread();
            mSendThread.start();
        } else {
            if (mSendThread.isAlive()) {
                System.out.println("mSendThread.isAlive() !!!");
            }
        }
        mSendThread.run();
    }

    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (bReading) {
                try {
                    synchronized (this) {
                        if (mInputStream == null)
                            return;
                        int i = -1;
                        while ((i = mInputStream.read()) != -1) {
                            SerialDataManager.getInstance().receiveData((byte) i);
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
            try {
                if (mOutputStream != null) {
                    if (mSendBuffer != null) {
                        byte[] buffer = mSendBuffer;
                        mSendBuffer = null;

                        System.out.print("接收数据 --> ");
                        for (int i = 0; i < buffer.length; i++) {
                            System.out.print((buffer[i] & 0xFF) + " ");
                        }
                        System.out.println();

                        mOutputStream.write(buffer);
                        mOutputStream.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
