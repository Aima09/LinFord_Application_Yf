package yf.com.gorgecommidemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用于串口测试通信
 *
 */
public class MainActivity extends AppCompatActivity implements SerialDataManager.ReceiveData {
    private static final String TAG = "MainActivity";
    @BindView(R.id.tv_data)
    EditText mTvData;
    @BindView(R.id.send)
    Button mSend;
    @BindView(R.id.received_data) TextView receivedData;

    private SerialDataManager serialDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        serialDataManager = SerialDataManager.getInstance();
        serialDataManager.setReceiveData(this);
        startService(new Intent(this, ASerialEngine.class));
    }

    /**
     * 点击发送数据
     */
    @OnClick(R.id.send)
    public void onViewClicked() {
        byte[] b = new byte[7];
        String[] str = mTvData.getText().toString().split("\\s+");
        Log.i(TAG, "====>" + str.length);

            try {
                int a = getI(String.valueOf(str[0].toLowerCase().charAt(0))) * 16 + getI(String.valueOf(str[0].charAt(1)));
                int c = getI(String.valueOf(str[1].toLowerCase().charAt(0))) * 16 + getI(String.valueOf(str[1].charAt(1)));
                b[0] = (byte) 0xAA;
                b[1] = (byte) 0xDD;
                b[2] = 0x03;
                b[3] = 0x01;
                b[4] = (byte) a;
                b[5] = (byte) c;
                b[6] = (byte) 0xB9;

                serialDataManager.sendData(b);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "对不起,请输入两个byte数据", Toast.LENGTH_SHORT).show();

            }

    }

    @Override public void receiveData(byte buffer) {
        Log.e(TAG, "receiveData");
        combiner(buffer);
    }

    private int getI(String a) {
        int b = 0;
        switch (a) {
            case "0":
                b = 0;
                break;
            case "1":
                b = 1;
                break;
            case "2":
                b = 2;
                break;
            case "3":
                b = 3;
                break;
            case "4":
                b = 4;
                break;
            case "5":
                b = 5;
                break;
            case "6":
                b = 6;
                break;
            case "7":
                b = 7;
                break;
            case "8":
                b = 8;
                break;
            case "9":
                b = 9;
                break;
            case "a":
                b = 10;
                break;
            case "b":
                b = 11;
                break;
            case "c":
                b = 12;
                break;
            case "d":
                b = 13;
                break;
            case "e":
                b = 14;
                break;
            case "f":
                b = 15;
                break;
        }
        return b;
    }

    /**
     * bytes转换成十六进制字符串
     *
     * @param b byte数组
     * @return String 每个Byte值之间空格分隔
     */
    public static String byte2HexStr(byte[] b) {
        String stmp = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

    private int pos = 0;
    private boolean dataStartCombiner = false;
    private byte[] buffer = new byte[64];

    public void combiner(byte b) {
        if (b == (byte) 0xFA && pos == 0) {
            Log.e(TAG, "combiner");
            dataStartCombiner = true;
        }
        if (dataStartCombiner) {
            if (pos <= 64) {
                buffer[pos] = b;
                ++pos;
            }
        }
        if (pos == 8) {
            pos = 0;
            dataStartCombiner = false;
            byte[] buff = new byte[8];
            System.arraycopy(buffer, 0, buff, 0, 8);
            final String a = byte2HexStr(buff);
            Log.e(TAG, "a = " + a);
            runOnUiThread(new Runnable() {
                @Override public void run() {
                    CharSequence str = receivedData.getText();
                    receivedData.setText(str + a + "\n");
                }
            });
        }
    }

}
