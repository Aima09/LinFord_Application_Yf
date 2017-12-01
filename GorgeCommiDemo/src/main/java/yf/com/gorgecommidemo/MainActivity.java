package yf.com.gorgecommidemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yf.serial.RS485Service;
import com.yf.serial.SerialPort;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="MainActivity" ;
    SerialPort mSerialPort;
    OutputStream mOutputStream;
    InputStream mInputStream;

    private SendThread mSendThread;
    RS485Service.MyBinder myBinder;
    @BindView(R.id.tv_data)
    EditText mTvData;
    @BindView(R.id.send)
    Button mSend;
    @BindView(R.id.receive_data)
    TextView mReceiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Intent intent = new Intent(this, RS485Service.class);
        bindService(intent, new MySerialConnection(), Context.BIND_AUTO_CREATE);

    }

    class MySerialConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (RS485Service.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }


    @OnClick(R.id.send)
    public void onViewClicked() {
        // testInterface1();
       // transferData(new byte[]{0x00, 0x01}, 2);
        byte[] b = new byte[2];
        b[0] = 0x01;
        b[1] = 0x02;
        Log.i(TAG,"myBinder = "+myBinder);
//        Log.i(TAG,"myBinder = "+myBinder);
        myBinder.getISSend().transferData(b, b.length);


    }

    private class ReadThread1 extends Thread {
        @Override
        public void run() {
            while (true) {
                int size;
                try {
                    if (mInputStream == null)
                        return;
                    byte[] buffer = new byte[64];
                    size = mInputStream.read(buffer);
                    System.out.println("1:size:" + size);
                    if (size > 0) {
                        System.out.print("1:--> ");
                        for (int i = 0; i < size; i++) {
                            // System.out.print((buffer[i] & 0xFF) + " ");
                            mReceiveData.setText(new String(buffer, 0, size));
                        }
                        System.out.println();
                    } else {
                        mReceiveData.setText("对不起，没有数据返回");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private byte[] mSendBuffer;
    private boolean bReading = true;

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
                Log.e("Test", "mSendThread.isAlive() !!!");
                return;
            }
            mSendThread.run();
        }
        // new SendThread().start();
    }

/*
    private void init() {
        closeStream();
        bReading = false;

        // 获取波特率
        if (config_server.isSuokete()) {
            BAUDRATE = PreferenceUtils.getPrefInt(this, config_server.RS485_BAUDRATE, 4800);
        } else {
            BAUDRATE = PreferenceUtils.getPrefInt(this, config_server.RS485_BAUDRATE, 9600);
        }
        log.d("current baudrate is " + BAUDRATE);
        mSendBuffer = new byte[1024];

        try {
            mSerialPort = new SerialPort(new File("/dev/ttyS1"), BAUDRATE, 0);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

			*/
/* Create a receiving thread *//*

            mReadThread = new ReadThread();
            bReading = true;
            mReadThread.start();

            switchDirection(ENABLE_RCV);

            log.i("Create RS485 Port -> ttyS1, bps " + getBAUDRATE());
        } catch (SecurityException e) {
            log.e(getResources().getString(R.string.error_security));
        } catch (IOException e) {
            log.e(getResources().getString(R.string.error_unknown));
        } catch (InvalidParameterException e) {
            log.e(getResources().getString(R.string.error_configuration));
        }
    }
*/


    private void testInterface1() {
        try {
//            Log.e("Test","start send");
//            mSerialPort = new SerialPort(new File("/dev/ttyS1"), 9600, 0);
//            mOutputStream = mSerialPort.getOutputStream();
//            mInputStream = mSerialPort.getInputStream();
//            String testData=mTvData.getText().toString();
//
//            // enable 485 rcv
//            switchDirection(ENABLE_SEND);
//            byte[] b = {0x00, 0x01};
//            mOutputStream.write(b);
//
//            // enable 485 rcv
//            switchDirection(ENABLE_RCV);
//            Log.e("Test","start send");

        } catch (SecurityException e) {
            e.printStackTrace();
            return;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
        } catch (InvalidParameterException e) {
            e.printStackTrace();
            return;
        }

        new ReadThread1().start();
    }

    private final String CON_485_PATH = "/sys/bus/platform/drivers/rk29-keypad/rk29-keypad/control_485_func";
    private final String CON_485_V209_PATH = "/sys/bus/i2c/drivers/rt3261/control485Data";
    private static byte ENABLE_RCV = 1;
    private static byte ENABLE_SEND = 2;

    private int switchDirection(byte b) {
        try {
            OutputStream output = null;
//            if (config_server.is209()) {
//                output = new FileOutputStream(CON_485_V209_PATH);
//            } else {
            output = new FileOutputStream(CON_485_PATH);
//            }
            output.write(b);
            output.flush();
            output.close();
        } catch (IOException e) {
            Log.e("test", "open file error . " + e);
        }
        return 0;
    }

}
