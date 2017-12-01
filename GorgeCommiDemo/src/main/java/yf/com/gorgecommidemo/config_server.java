package yf.com.gorgecommidemo;

import android.util.Log;

public class config_server {
	/** ����Ϊȫ�ֱ��� */
	public static String room_name;
	public static String room_no;

	public static int curVerCode;
	public static String curVerName;

	public static int newVerCode;
	public static String newVerName;

	public static boolean dmrswitch = false;
	public static boolean dmshaveget = false;
	public static boolean voicePrompt = false; // ��������
	public static boolean searchPrompt = true;
	public static boolean isUDP = true;

	public static final int INVILD = -1;
	public static final int IS_WAKE_UP = 0;
	public static final int IS_SHUT_DOWN = 1;
	public static final int IS_PLAYING = 2;
	public static final int IS_STOP = 3;

	public static final String TIMER_WAKE_UP = "timer_wake_up";
	public static final String TIMER_SHUT_DOWN = "timer_shut_down";
	public static final String TIMER_PLAYING = "timer_playing";
	public static final String TIMER_STOP = "timer_stop";
	// ����ѡ����
	public static final String SCREEN_SAVER_SET = "screen_saver_set";
	// �����������ֹ���
	public static final String BOOT_PLAY = "boot_play_music";
	// ��������
	public static final String VOICE_PROMT = "voice_prompt";
	// when 50 percent, left==right
	public static final String LRVOL_PERCENT = "lr_vol_percent";
	// ������
	public static final String RS485_BAUDRATE = "rs485_baudrate";
	public static final String RS232_BAUDRATE = "rs232_baudrate";

	public static final String RS485_ENABLE = "rs485enable";
	public static final String RS232_ENABLE = "rs232enable";
	public static final String HDL_ENABLE = "hdlEnable";
	public static final String AIRPLAY_ENABLE = "airplayenable";

	/** RS485 local */
	public static final String SACTIVITY = "activity";
	// ��¼��ǰ���ĸ�activity
	public static int CURRENT_ATY = 0;
	public static final int ATY_MAIN = 0x01;
	public static final int ATY_LM_TOTAL = 0x02;
	public static final int ATY_LM_MEMORY = 0x03;
	public static final int ATY_LM_SDCARD = 0x04;
	public static final int ATY_LM_USB = 0x05;
	public static final int ATY_LM_SITUATION = 0x06;
	public static final int ATY_TIMER_SHOW = 0x07;
	public static final int ATY_SETTING = 0x08;
	public static final int ATY_DLNA = 0x09;
	public static final int ATY_VOICE = 0x0a;
	public static final int ATY_EFFECT = 0x0b;
	public static final int ATY_TIMER_SET = 0x0c;
	public static final int ATY_SLEEP = 0x0d;
	public static final int ATY_UNKNOWN = 0x00;

	public static final String ATY_LM_ACCARACY = "localmedia_activity_accuracy";
	public static String ATY_LM_HOTEL = "aty_lm_hotel";

	public static final int AUDIO_SOURCE_LOCAL = 0;
	public static final int AUDIO_SOURCE_LINEIN_1 = 1;
	public static final int AUDIO_SOURCE_LINEIN_2 = 2;
	public static final int AUDIO_SOURCE_BLUE = 3;
	public static final int AUDIO_SOURCE_UX = 4;

	public static int audioSrcNumber() {
		String model = android.os.Build.DEVICE;
		int num = 0;
		if (model.indexOf("0205") != -1) {
			num = 3;
		} else if (model.indexOf("205") != -1) {
			num = 4;
		} else if (model.indexOf("206") != -1) {
			num = 3;
		} else if (model.indexOf("207") != -1) {
			num = 3;
		} else if (model.indexOf("208") != -1) {
			num = 4;
		} else if (model.indexOf("209") != -1) {
			num = 3;
		}
		return num;
	}

	private static boolean isSpecVersion(String v) {
		return android.os.Build.DEVICE.indexOf(v) != -1;
	}

	public static void pringCurrentDEVICE() {
		Log.d("device", android.os.Build.DEVICE);
	}

	public static String get209Type() {
		return isSpecVersion("209") ? "209" : "";
	}

	// ����
	public static boolean isHuaLi() {
		return isSpecVersion("HuaLi");
	}

	// �Ƶ�
	public static boolean isHotel() {
		return isSpecVersion("HOTEL");
	}

	public static boolean is206() {
		return isSpecVersion("206");
	}

	public static boolean is207() {
		return isSpecVersion("207");
	}

	public static boolean is209() {
		return isSpecVersion("209");
	}

	// ������
	public static boolean isSuokete() {
		return isSpecVersion("suokete");
	}

	// ����
	// android:icon="@drawable/ic_launcher_aoyin"
	// android:label="@string/app_name_aoyin"
	public static boolean isAoyin() {
		return isSpecVersion("aoyin");
	}

	// ΢+��
	// android:icon="@drawable/ws_app_logo"
	public static boolean isWsapp() {
		return isSpecVersion("ws-plus");
	}

	// ����ͻ�
	public static boolean isLaoWai() {
		return isSpecVersion("genious") || isSpecVersion("English");
	}

	// �۾� - ���α���
	public static boolean isNeedPlay() {
		return isSpecVersion("play");
	}

	// �ϰ�
	public static boolean isDiBai() {
		return isSpecVersion("HOTEL");
	}

	// ����
	public static boolean isHangBang() {
		return isSpecVersion("HANGBANG");
	}

	public static boolean is205() {
		return isSpecVersion("205");
	}

	/**
	 * �޸Ĳο�s-resource/readme.txt
	 */
	public static boolean isAoge() {
		return isSpecVersion("Aoge");
	}

	public static boolean isWuLian() {
		return false;
		// return isSpecVersion("WuLian");
	}

	public static boolean isBoer() {
		return isSpecVersion("Boer");
	}

	// U8 U9�ͺ�
	public static boolean isU8U9() {
		return isSpecVersion("208");
	}
	
	public static boolean isBM208_MIC() {
		return isSpecVersion("BM208_MIC");
	}

	public static boolean isWifiMic(int position) {
		return position == 2 && isU8U9();
	}

	/*// ά����Ӱ
	*//**
	 * �޸İ汾��android:versionName="1.8.4_weijing"
	 * �޸�ICON��android:icon="@drawable/ic_launcher_weijing"
	 * �޸�name��android:label="@string/app_name_weijing"
	 *//*
	public static boolean isWeiJing() {
		return Utils.getVerName(RenderApplication.getContext()).indexOf("weijing") != -1;
	}

	private static boolean isNeedSerial() {
		return !isSpecVersion("unused-serial");
	}

	public static Boolean isStart485Service() {
		return isNeedSerial()//
				&& PreferenceUtils.getPrefBoolean(RenderApplication.getContext(), RS485_ENABLE, true);
	}

	public static Boolean isStartZigbeeService() {
		return isNeedSerial() //
				&& PreferenceUtils.getPrefBoolean(RenderApplication.getContext(), RS232_ENABLE, true) //
				&& !isHuaLi();
	}

	public static Boolean isStartHDLService() {
		return PreferenceUtils.getPrefBoolean(RenderApplication.getContext(), HDL_ENABLE, true);
	}

	public static Boolean isStartAirplayService() {
		return PreferenceUtils.getPrefBoolean(RenderApplication.getContext(), AIRPLAY_ENABLE, true);
	}

	public static Boolean isUpgrading() {
		Boolean b = isAoyin() || isNeedPlay() || isWeiJing();
		return !b;
	}

	public static boolean isExistZone() {
		return is209() || isU8U9() || is206() || is207();
	}

	public static boolean isExistZoneLeftRight() {
		return WiseUtils.isExistZoneLeftRight();
	}

	public static boolean isExistSceenSaverApp() {
		return Util.checkApkExist("com.yf.screensaver");
	}*/
}
