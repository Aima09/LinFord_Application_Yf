package yf.com.gorgecommidemo;

/**
 * @author wuhuai
 * @class name：yf.com.gorgecommidemo
 * @time 2017/12/6 15:21
 * @change
 * @chang time
 * @class describe
 */

public class SerialDataManager {

    private SerialDataManager(){

    }
    private static SerialDataManager instance;

    private SendData sendData;
    private ReceiveData receiveData;

    public static SerialDataManager getInstance() {
        if (instance == null) {
            synchronized (SerialDataManager.class) {
                if (instance == null)
                    instance = new SerialDataManager();
            }
        }
        return instance;
    }

    public interface SendData{
        void sendData(byte[] buffer);
    }

    public interface ReceiveData{
        void receiveData(byte buffer);
    }

    public void setReceiveData(ReceiveData receiveData) {
        this.receiveData = receiveData;
    }

    public void setSendData(SendData sendData) {
        this.sendData = sendData;
    }

    public void sendData(byte[] buffer){
        if (null != sendData){
            sendData.sendData(buffer);
        }
    }

    public void receiveData(byte buffer){
        if (null != receiveData){
            receiveData.receiveData(buffer);
        }
    }
}
