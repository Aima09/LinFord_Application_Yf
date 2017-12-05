package yf.com.gorgecommidemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.yf.serial.RS485Service;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    RS485Service.MyBinder myBinder;
    @BindView(R.id.tv_data)
    EditText mTvData;
    @BindView(R.id.send)
    Button mSend;
    @BindView(R.id.received_message_lv)
    RecyclerView receivedMessageLv;

    //RecyclerView布局格式
    private LinearLayoutManager mLayoutManager;
    //装载leftMenu的集合
    private List<String> reMessageList=new ArrayList<String>();
    private ReceivedAdapter receivedAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Intent intent = new Intent(this, RS485Service.class);
        bindService(intent, new MySerialConnection(), Context.BIND_AUTO_CREATE);
        initListView();
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

/*  // testInterface1();
       // transferData(new byte[]{0x00, 0x01}, 2);
        byte[] b = new byte[2];
        b[0] = 0x01;
        b[1] = 0x02;
        Log.i(TAG,"myBinder = "+myBinder);
//        Log.i(TAG,"myBinder = "+myBinder);
        myBinder.getISSend().transferData(b, b.length);*/


        // testInterface1();
        // transferData(new byte[]{0x00, 0x01}, 2);
//        byte[] b = new byte[1024];
//       String[] str=mTvData.getText().toString().split("\\s+");
//      for (int i=0;i<=str.length;i++){
//          b[i]= Byte.parseByte(str[i]);
//      }
        //    Log.i(TAG,b+"");
//        for(String ss : arr){
//            System.out.println(ss);
//        }
        byte[] b = new byte[7];
        b[0] = 0x01;
        b[1] = 0x02;
        b[0] = (byte) 0xAA;
        b[1] = (byte) 0xDD;
        b[2] = 0x03;
        b[3] = 0x01;
        b[4] = 0x2D;
        b[5] = 0x01;
        b[6] = (byte) 0xB9;
//        b[0] = 0x1E;
//        b[1] = 0x02;
        Log.i(TAG, "myBinder =======> " + myBinder);
//        Log.i(TAG,"myBinder = "+myBinder);
        // 0xAA, 0xDD, 0x03, 0x01, 0x2D, 0x01 0xB9

        myBinder.getISSend().transferData(b, b.length);

    }

    private void initListView(){
        mLayoutManager = new LinearLayoutManager(this);
        receivedMessageLv.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        receivedMessageLv.setHasFixedSize(true);
        //为RecyclerView添加默认动画效果
        receivedMessageLv.setItemAnimator(new DefaultItemAnimator());
        reMessageList.add(myBinder.getReceivedData());
        receivedAdapter=new ReceivedAdapter(reMessageList,this);
        receivedMessageLv.setAdapter(receivedAdapter);
    }

}
