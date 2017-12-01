/**
 * Analyzer.java[V 1.0.0]
 * @author xujie@yf-space.com create on 2015-6-16
 */

package com.yf.serial;


import com.yf.serial.yfpublic.utils.CommonLog;
import com.yf.serial.yfpublic.utils.LogFactory;

public class Analyzer {
	private static final CommonLog log = LogFactory.createLog(Analyzer.class.getSimpleName());
	private ISerial ISerial;
	private byte[] buffer;
	private int size;
	private int roomNumber;
	private String func;
	private String parameter;

	private static final int MIN_LEN = 6;

	public Analyzer() {
	}

	private String setKeyFunc(char c) {
		String parameter = "";
		switch (c) {
			case 0x00:// 开机
				parameter = Constants.KF_WAKE_UP;
				break;
			case 0x01:// 关机
				parameter = Constants.KF_SHUTDOWN;
				break;
			case 0x02:// 播放
				parameter = Constants.KF_PLAYER_PLAY;
				break;
			case 0x03:// 暂停
				parameter = Constants.KF_PLAYER_PAUSE;
				break;
			case 0x04:// 播放暂停
				parameter = Constants.KF_PLAYER_PLAYPAUSE;
				break;
			case 0x05:// 停止
				parameter = Constants.KF_PLAYER_STOP;
				break;
			case 0x06:// 音量减
				parameter = Constants.KF_VOL_DOWN;
				break;
			case 0x07:// 音量加
				parameter = Constants.KF_VOL_UP;
				break;
			case 0x08:// 静音关
				parameter = Constants.KF_AUTO_MUTE_OFF;
				break;
			case 0x09:// 静音开
				parameter = Constants.KF_AUTO_MUTE_ON;
				break;
			case 0x0a:// 静音
				parameter = Constants.KF_AUTO_MUTE;
				break;
			case 0x0b:// 上一曲
				parameter = Constants.KF_PLAYER_PRE;
				break;
			case 0x0c:// 下一曲
				parameter = Constants.KF_PLAYER_NEXT;
				break;
			case 0x0d:// AUX（本地音源，line in 1,Line In 2）
				parameter = Constants.KF_AUX;
				break;
			case 0x10:// 主界面
				parameter = Constants.KF_ATY_MAIN;
				break;
			case 0x11:// 本地-全部界面
				parameter = Constants.KF_ATY_LM_TOTAL;
				break;
			case 0x12:// 本地-内存界面
				parameter = Constants.KF_ATY_LM_MEMORY;
				break;
			case 0x13:// 本地-SD卡界面
				parameter = Constants.KF_ATY_LM_SDCARD;
				break;
			case 0x14:// 本地-U盘界面`
				parameter = Constants.KF_ATY_LM_USB;
				break;
			case 0x15:// 本地-情景界面
				parameter = Constants.KF_ATY_LM_SITUATION;
				break;
			case 0x16:// 定时设置界面
				parameter = Constants.KF_ATY_TIMER_SETTING;
				break;
			case 0x17:// 设置界面
				parameter = Constants.KF_ATY_SETTINGS;
				break;
			case 0x18:// DLNA界面
				parameter = Constants.KF_ATY_DLNA;
				break;
			case 0x19:// 语音播报
				parameter = Constants.KF_ATY_VOICE_BROADCAST;
				break;
			case 0x1a:// 环境音效
				parameter = Constants.KF_ATY_EFFECTS;
				break;
			case 0x1b:// 定时设置设置界面
				parameter = Constants.KF_ATY_CLOCK_SET;
				break;
			case 0x1c:// 屏保界面
				parameter = Constants.KF_ATY_SCREEN_SAVE;
				break;
			case 0x1d:// 桌面
				parameter = Constants.KF_ATY_DESKTOP;
				break;
			case 0x20:// 确认
				parameter = Constants.KF_KEY_ENTER;
				break;
			case 0x21:// 返回
				parameter = Constants.KF_KEY_BACK;
				break;
			default:
				break;
		}
		// log.i("parameter - " + parameter);
		return parameter;
	}

	/**
	 * 除了校验位验证，数据长度必须与length相吻合
	 * @param length
	 * @return true/false
	 */
	private boolean judgeCheckBit(int length) {
		if (size != length) {
			log.e("the length is not the same, " + size + " - " + length);
			func = Constants.ERROR_FEEDBACK;
			parameter = "" + Constants.ERROR_FB_LEN;
			return false;
		}

		byte bEccReal = 0;
		for (int i = 1; i < size - 2; i++) {
			bEccReal += buffer[i];
		}

		byte bEcc = buffer[size - 2];
		// 当校验位后两位大于0xF9时，统一判断为0xF9 - 与起始结束符相冲突
		if ((bEccReal & 0xFF) > 0xF9 && (bEcc & 0xFF) == 0xF9) {
			log.i("bEccReal " + bEccReal + ", bEcc " + bEcc);
			return true;
		}

		boolean ret = ((bEcc & 0xFF) == (bEccReal & 0xFF));
		if (!ret) {
			log.i("bEcc error " + (bEcc & 0xFF) + ", " + (bEccReal & 0xFF));
			func = Constants.ERROR_FEEDBACK;
			parameter = "" + Constants.ERROR_FB_ECC;
		}

		return ret;
	}

	private boolean analysz() {
		System.out.print("--> ");
		for (int i = 0; i < size; i++) {
			log.i("analysz : "+(buffer[i] & 0xFF) + " ");
		}
		System.out.println();

		func = "";
		parameter = null;
		// start analysz
		if (size < 6 || size > 256)
			return false;
		// [0]

		// [1] [2]
		// 房间号
		byte byte1 = buffer[1];
		byte byte2 = buffer[2];
		roomNumber = (byte1 & 0xFF) * 256 + (byte2 & 0xFF);
		// ignore

		// [3] data length
		byte byte3 = buffer[3];
		if ((byte3 & 0xFF) > 256 || byte3 == 0)
			return false;

		if (byte3 != size - 6) {
			System.out.println("byte3 - " + byte3 + ":" + (size - 6));
			return false;
		}

		// [4] ~ [size -2] : command && argv
		byte byte4 = buffer[4];

		switch (byte4 & 0xFF) {
			case 0x01: // 设备信息查询
				if (judgeCheckBit(MIN_LEN + 1)) {
					func = Constants.DEV_INFO;
				}
				break;
			case 0x07: // 查看当前波特率
				if (judgeCheckBit(MIN_LEN + 1)) {
					if (ISerial != null) {
						func = Constants.CURRENT_BUARDRATE;
						parameter = "" + ISerial.getBuradrate();
					}
				}
				break;
			case 0x09: // 初始化波特率
				if (judgeCheckBit(MIN_LEN + 3 + 1)) {
					if (ISerial != null) {
						// func = Constants.INIT_BUARDRATE;
						int buradrate = ((buffer[5] & 0xFF) << 16) + ((buffer[5 + 1] & 0xFF) << 8) + (buffer[5 + 2] & 0xFF);
						log.i("buffer[5]:" + ((buffer[5] & 0xFF) << 16) + ", buffer[6]:" + ((buffer[5 + 1] & 0xFF) << 8)
								+ ", buffer[7]:" + (buffer[5 + 2] & 0xFF) + ", buradrate " + buradrate);
						ISerial.initBuradrate(buradrate);
					}
					func = Constants.INIT_BUARDRATE;
				}
				break;
			case 0x0a:// 设备菜单位置
				if (judgeCheckBit(MIN_LEN + 1)) {
					func = Constants.DEV_MENU_POTION;
				}
				break;
			case 0x0c: // 查寻设备状态
				if (judgeCheckBit(MIN_LEN + 1)) {
					func = Constants.DEV_STATE_INFO;
				}
				break;
			case 0x10: // 按键指令
				if (judgeCheckBit(MIN_LEN + 2)) {
					func = Constants.KEY_FUNCTION;
					parameter = setKeyFunc((char) buffer[5]);
				}
				break;
			case 0x11: // 音量设置
				if (judgeCheckBit(MIN_LEN + 2)) {
					func = Constants.VOL_SETTIONGS;
					parameter = String.valueOf(buffer[5]);
				}
				break;
			case 0x13: // 获取开关机状态
				if (judgeCheckBit(MIN_LEN + 1)) {
					func = Constants.WAKE_SHUT_STATE;
				}
				break;
			case 0x15: // 获取当前音量值
				if (judgeCheckBit(MIN_LEN + 1)) {
					func = Constants.VOL_VALUE;
				}
				break;
			case 0x17: // 查寻静音状态
				if (judgeCheckBit(MIN_LEN + 1)) {
					func = Constants.MUTE_STATE_GET;
				}
				break;
			case 0x19: // 静音设置
				if (judgeCheckBit(MIN_LEN + 2)) {
					func = Constants.MUTE_STATE_SET_INT;
					parameter = String.valueOf(buffer[5]);
				}
				break;
			case 0x8b: // 查寻音乐资源数目
				if (judgeCheckBit(MIN_LEN + 1)) {
					func = Constants.MEDIA_RES;
				}
				break;
			case 0x80: // 查寻音乐播放器的状态
				if (judgeCheckBit(MIN_LEN + 1))
					func = Constants.MEDIA_PLAY_STATE;
				break;
			case 0x82: // 指定音乐的详细信息
				if (judgeCheckBit(MIN_LEN + 4)) {
					func = Constants.MEDIA_SPECIFY_DETAILS;
					parameter = Integer.toString(buffer[5]) + "-"
							+ Integer.toString((buffer[6] << 8) + (buffer[6 + 1] & 0xFF));
				}
				break;
			case 0x84: // 正在播放音乐的详细信息
				if (judgeCheckBit(MIN_LEN + 1))
					func = Constants.MEDIA_PLAYING_DETAILS;
				break;
			case 0x86: // 播放模式设置
				if (judgeCheckBit(MIN_LEN + 2)) {
					func = Constants.MODE_SETTING;
					byte t = buffer[5];
					if (0x00 == t) { // 顺序播放
						parameter = Constants.MODE_SET_NORMAL_;
					} else if (0x01 == t) { // 全部循环
						parameter = Constants.MODE_SET_REPEAT_ALL;
					} else if (0x02 == t) { // 单曲循环
						parameter = Constants.MODE_SET_REPEAT_ONE;
					} else if (0x03 == t) { // 随机播放
						parameter = Constants.MODE_SET_SHUFFLE;
					}
				}
				break;
			case 0x87: // 设置音效
				if (judgeCheckBit(MIN_LEN + 2)) {
					func = Constants.EFFECT_SET;
					parameter = buffer[5] + "";
				}
				break;
			case 0x88: // 选择U盘或SD卡播放
				if (judgeCheckBit(MIN_LEN + 2)) {
					func = Constants.SEL_MEDIA_RES;
					byte t = buffer[5];
					if (0x00 == t) { // 全部
						parameter = Constants.SEL_MEDIA_RES_ALL;
					} else if (0x01 == t) { // 内置SD卡
						parameter = Constants.SEL_MEDIA_RES_SDCARD;
					} else if (0x02 == t) { // 外置SD卡
						parameter = Constants.SEL_MEDIA_RES_EXT_SD;
					} else if (0x03 == t) { // USB
						parameter = Constants.SEL_MEDIA_RES_USB;
					} else if (0x04 == t) { // Situation
						parameter = Constants.SEL_MEDIA_RES_SITUATION;
					}
				}
				break;
			case 0x89: // 播放指定序号的歌曲
				if (judgeCheckBit(MIN_LEN + 4)) {
					func = Constants.MEDIA_PLAY_SPECIFIED;
					parameter = Integer.toString(buffer[5]) + "-"
							+ Integer.toString((buffer[6] << 8) + (buffer[6 + 1] & 0xFF));
				}

				break;
			case 0x8d: // 跳转本地歌曲且播放
				if (judgeCheckBit(MIN_LEN + 1 + 1)) {
					func = Constants.MEDIA_JUMP_AND_PLAY_LOC_TYPE;
					parameter = Integer.toString(buffer[5]);
				}
				break;
			case 0x8a: // 当前播放的歌曲跳转
				if (judgeCheckBit(MIN_LEN + 3)) {
					func = Constants.MEDIA_PLAYING_JUMP;
					parameter = Integer.toString(buffer[5]) + "-" + Integer.toString(buffer[5 + 1]);
				}
				break;
			case 0x60: // 查看语音播报
				if (judgeCheckBit(MIN_LEN + 1)) {
					func = Constants.VOICE_BROADCAST_GET;
				}
				break;
			case 0x62: // 播报指定序号语音
				if (judgeCheckBit(MIN_LEN + 2)) {
					func = Constants.VOICE_BROADCAST_ENABLE_INT;
					parameter = Integer.toString(buffer[5]);
				}
				break;
			case 0x6e: // 查看指定序号语音的名称
				if (judgeCheckBit(MIN_LEN + 2)) {
					func = Constants.VOICE_BROADCAST_GET_SPEC;
					parameter = Integer.toString(buffer[5]);
				}
				break;
			case 0x63: // 查看闹钟详细信息
				if (judgeCheckBit(MIN_LEN + 1)) {
					func = Constants.TIMER_GET_DETAILS;
				}
				break;
			case 0x65: // 添加闹钟
				if (judgeCheckBit(MIN_LEN + 6)) {
					func = Constants.TIMER_ADD_ID;
					parameter = Integer.toString(buffer[5]) + "-" + Integer.toString(buffer[6]) + "-" + buffer[7] + ":"
							+ buffer[8] + "-" + Integer.toString(buffer[9]);
				}
				break;
			case 0x66: // 开启或关闭某序列定时序列
				if (judgeCheckBit(MIN_LEN + 4)) {
					func = Constants.TIMER_ENABLE_ID;
					parameter = Integer.toString((buffer[5] << 8) + (buffer[6] & 0xFF)) + "-" + Integer.toString(buffer[7]);
				}
				break;
			case 0x67: // 开关HDMI
				if (judgeCheckBit(MIN_LEN + 2)) {
					func = Constants.HDMI_ENABLE;
					parameter = String.valueOf(buffer[5]);
				}
				break;
			case 0x68: // 查看房间名和房间序号
				if (judgeCheckBit(MIN_LEN + 1)) {
					func = Constants.ROOM_AND_NUMBER_GET;
				}
				break;
			case 0x6a: // 设置房间名和房间号
				if (judgeCheckBit(MIN_LEN + 3)) {
					func = Constants.ROOM_AND_NUMBER_SET;
					parameter = Integer.toString((buffer[5] << 8) + (buffer[6] & 0xFF));
				}
				break;
			case 0x6b: // 查看音源数
				if (judgeCheckBit(MIN_LEN + 1)) {
					func = Constants.AUDIO_SRC_NUMBER_GET;
				}
				break;
			case 0x6d: // 设置音源
				if (judgeCheckBit(MIN_LEN + 2)) {
					func = Constants.AUDIO_SRC_SET_INT;
					parameter = String.valueOf(buffer[5]);
				}
				break;
			case 0x70: // 设置四分区
				if (judgeCheckBit(MIN_LEN + 5)) {
					func = Constants.ZONING_SET;
					parameter = Integer.toString(buffer[5]) + "-" + Integer.toString(buffer[6]) + "-"
							+ Integer.toString(buffer[7]) + "-" + Integer.toString(buffer[8]);
				}
				break;
			case 0x72: // 查看四分区
				if (judgeCheckBit(MIN_LEN + 1)) {
					func = Constants.ZONING_GET;
				}
				break;
			case 0x73: // 查寻音乐界面指定序号音乐详细信息2
				if (judgeCheckBit(MIN_LEN + 4)) {
					func = Constants.MEDIA_SPECIFY_DETAILS2;
					parameter = Integer.toString(buffer[5]) + "-"
							+ Integer.toString((buffer[6] << 8) + (buffer[6 + 1] & 0xFF));
				}
				break;
			case 0x4b: // 查寻音乐资源数目
				if (judgeCheckBit(MIN_LEN + 1)) {
					func = Constants.MEDIA_RES_NEW;
				}
				break;
			case 0x4d: // 查看指定序号语音的名称2
				if (judgeCheckBit(MIN_LEN + 1 + 2)) {
					func = Constants.VOICE_BROADCAST_GET_SPEC2;
					parameter = Integer.toString((buffer[5] << 8) + (buffer[6] & 0xFF));
				}
				break;
			case 0xa0:
				if (ISerial != null) {
					ISerial.callAccess(true);
				}
				return false;
			default:
				log.e("error!");
				return false;
		}

		log.i("function = " + func + " mParameter = " + parameter);
		return true;
	}

	public void handle(byte[] data, int size) {
		if (data == null || size <= 0) {
			return;
		}

		this.buffer = data;
		this.size = size;
		if (analysz()) {
			if (ISerial != null) {
				ISerial.callBack(roomNumber, func, parameter);
			}
		}
	}

	public void setISerial(ISerial ISerial) {
		this.ISerial = ISerial;
	}

	public interface ISerial {
		void callBack(int roomNumber, String function, String partemterString);

		void initBuradrate(int buradRrate);

		int getBuradrate();

		// 添加一个入网的回调
		void callAccess(boolean suc);
	}

}
