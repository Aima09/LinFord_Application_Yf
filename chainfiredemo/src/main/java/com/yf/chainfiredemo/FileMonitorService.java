package com.yf.chainfiredemo;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.FileObserver;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.google.common.collect.Lists;

import org.androidannotations.annotations.EService;

import java.io.File;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author:LinFord
 * @create at:5/10 0010 16:12
 * @email: glf@yf-space.com
 * @technique point:
 * @description:
 * @modify:
 */
@EService
public class FileMonitorService extends Service {
    //监测的文件路径
    public static final String CHAIN_FIRE_PATH = "/data/data/eu.chainfire.supersu/files/supersu.cfg";
    //判断文件是否包含相关该包的信息
    public static final String KEY_PACKAGE = "com.sudiyi.systemapps";
    private static final String TAG = FileMonitorService.class.getSimpleName();
    //文件修改监听事件
    private SdCardListener mSdCardListener;
    ReentrantReadWriteLock mLock = new ReentrantReadWriteLock();

    public FileMonitorService() {
    }

    @Override public void onCreate() {
        super.onCreate();
        init();
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override public void onDestroy() {
        Log.d(TAG, "onDestroy() called");
        super.onDestroy();
        if (mSdCardListener != null) {
            mSdCardListener.stopWatching();
        }
    }

    @Override public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void init() {
        Log.d(TAG, "init() called");
        mSdCardListener = new SdCardListener(CHAIN_FIRE_PATH);
        //1.监听文件动态事件
        mSdCardListener.startWatching();
        //2.初始化文件内容,如果文件不存在的话
        checkFile();

    }

    /**
     * 获取所有应用信息
     * 过滤 com.sudiyi.systemapps
     *
     * @return com.sudiyi.systemapps相关应用的packageName, uid, access
     */
    private List<AppDataInfo> getPackageUid() {
        Log.d(TAG, "getPackageUid() called");
        List<AppUtils.AppInfo> appInfos = AppUtils.getAppsInfo();
        List<AppDataInfo> appDataInfos = Lists.newArrayList();
        String packageName;
        try {
            for (AppUtils.AppInfo appInfo : appInfos) {
                if (appInfo.getPackageName().contains(KEY_PACKAGE)) {
                    packageName = appInfo.getPackageName();
                    PackageManager pm = getPackageManager();
                    @SuppressLint("WrongConstant")
                    ApplicationInfo ai = pm.getApplicationInfo(packageName, PackageManager.GET_ACTIVITIES);
                    appDataInfos.add(new AppDataInfo(packageName, ai.uid, 1));
                }
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "getPackageUid: exception=", e);
        }
        Log.d(TAG, "getPackageUid() returned: " + appDataInfos);
        return appDataInfos;
    }

    /**
     * the default content for the file
     */
    private static final String DEFAULT_CONTENT =
            "[default]\n" +
                    "notify=1\n" +
                    "log=1\n" +
                    "wait=10\n" +
                    "access=2\n" +
                    "respectcm=1\n" +
                    "trustsystem=0\n" +
                    "enablemultiuser=0\n" +
                    "enableduringboot=0\n" +
                    "enablemountnamespaceseparation=1\n" +
                    "\n" +
                    "[ADB shell [UID]]\n" +
                    "access=1\n" +
                    "\n" +
                    "[jackpal.androidterm]\n" +
                    "uid=75\n" +
                    "access=1";

    private void checkFile() {
        Log.d(TAG, "checkFile() called");
        ThreadUtils.executeByIo(new ThreadUtils.SimpleTask<File>() {
            @Nullable @Override public File doInBackground() throws Throwable {
                File cfgFile = new File(CHAIN_FIRE_PATH);
                //判断文件是否存在
                if (!cfgFile.exists() || cfgFile.length() == 0) {
                    //不存在新建一个
                    cfgFile.createNewFile();
                    //写入默认任何和com.sudiyi.systemapps包名相关app的信息
                    writeFile(cfgFile);
                } else {
                    //存在则读取该文件内容
                    List<String> dataList = FileIOUtils.readFile2List(cfgFile);
                    int count = 0;
                    //计算该文件包含com.sudiyi.systemapps的行数量
                    for (String line : dataList) {
                        if (line.contains(KEY_PACKAGE)) {
                            count++;
                        }
                    }
                    //如果该数据跟查询的数量不一致,则重写更新该文件
                    if (count != getPackageUid().size()) {
                        File newFile = new File(cfgFile.getPath());
                        cfgFile.delete();
                        writeFile(newFile);
                    }
                }
                return cfgFile;
            }

            @Override public void onSuccess(@Nullable File result) {
                Log.d(TAG, "onSuccess() called with: result = [" + (result != null ? result.length() : 0) + "]");
            }
        });

    }

    /**
     * 写入文件
     *
     * @param cfgFile
     */
    private void writeFile(File cfgFile) {
        Log.d(TAG, "writeFile() called with: cfgFile = [" + cfgFile + "]");
        mLock.writeLock().lock();
        try {
            StringBuilder stringBuilder = new StringBuilder(DEFAULT_CONTENT);
            List<AppDataInfo> appDataInfos = getPackageUid();
            for (AppDataInfo info : appDataInfos) {
                stringBuilder.append("\n")
                        .append(info.packageName)
                        .append("\n")
                        .append(info.uid)
                        .append("\n")
                        .append(info.access);
            }
            FileIOUtils.writeFileFromString(cfgFile, stringBuilder.toString());
        } finally {
            mLock.writeLock().unlock();
        }

        Log.d(TAG, "writeFile() returned: file exist=" + cfgFile.exists() + ",file.length()=" + cfgFile.length());
        LogUtils.file(TAG, FileIOUtils.readFile2String(cfgFile));
    }

    /**
     * SD卡中的目录创建监听器。
     *
     * @author mayingcai
     */
    private class SdCardListener extends FileObserver {

        SdCardListener(String path) {
            /*
             * 这种构造方法是默认监听所有事件的,如果使用 super(String,int)这种构造方法，
             * 则int参数是要监听的事件类型.
             */
            super(path);
        }

        @Override
        public void onEvent(int event, String path) {
            Log.d(TAG, "onEvent() called with: event = [" + event + "], path = [" + path + "]");
            switch (event) {
                case FileObserver.ALL_EVENTS:
                    checkFile();
                    break;
                case FileObserver.CREATE:
                    break;
                default:
                    break;
            }
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class AppDataInfo {
        String packageName;
        int uid;
        int access;
    }
}
