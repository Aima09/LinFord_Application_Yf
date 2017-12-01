/**
 * SERIAL.java[V 1.0.0]
 * classes: com.YF.YuanFang.YFServer.serial.rs485.Constants
 * xuie	create 2015-5-11 ����4:51:37
 */

package com.yf.serial;


public class Constants {
	public static final String DEV_INFO = "DeviceInfo";
	public static final String CURRENT_BUARDRATE = "CurrentBuardrate";
	public static final String INIT_BUARDRATE = "InitializeBuardrate";
	public static final String DEV_MENU_POTION = "DeviceMenuPosition";
	public static final String DEV_STATE_INFO = "DeviceStateInformation";

	public static final String KEY_FUNCTION = "KeyFunction";
	public static final String KF_SHUTDOWN = "shutdown";
	public static final String KF_WAKE_UP = "wakeup";
	public static final String KF_PLAYER_PLAY = "play";
	public static final String KF_PLAYER_PAUSE = "pause";
	public static final String KF_PLAYER_PLAYPAUSE = "playorpause";
	public static final String KF_PLAYER_STOP = "stop";
	public static final String KF_VOL_DOWN = "volumedown";
	public static final String KF_VOL_UP = "volumeup";
	public static final String KF_AUTO_MUTE_OFF = "automuteoff";
	public static final String KF_AUTO_MUTE_ON = "automuteon";
	public static final String KF_AUTO_MUTE = "automute";
	public static final String KF_PLAYER_PRE = "pre";
	public static final String KF_PLAYER_NEXT = "next";
	public static final String KF_AUX = "aux";
	public static final String KF_ATY_MAIN = "aty_main";
	public static final String KF_ATY_LM_TOTAL = "aty_lm_total";
	public static final String KF_ATY_LM_MEMORY = "aty_lm_memory";
	public static final String KF_ATY_LM_SDCARD = "aty_lm_sdcard";
	public static final String KF_ATY_LM_USB = "aty_lm_usb";
	public static final String KF_ATY_LM_SITUATION = "aty_lm_situation";
	public static final String KF_ATY_TIMER_SETTING = "aty_timer_setting";
	public static final String KF_ATY_SETTINGS = "aty_settings";
	public static final String KF_ATY_DLNA = "aty_dlna";
	public static final String KF_ATY_VOICE_BROADCAST = "aty_voice_broadcast";
	public static final String KF_ATY_EFFECTS = "aty_effects";
	public static final String KF_ATY_CLOCK_SET = "aty_clock_set";
	public static final String KF_ATY_SCREEN_SAVE = "aty_screen_save";
	public static final String KF_ATY_DESKTOP = "aty_desktop";
	public static final String KF_KEY_ENTER = "key_enter";
	public static final String KF_KEY_BACK = "key_back";

	public static final String VOL_SETTIONGS = "VolumeSettings";
	public static final String WAKE_SHUT_STATE = "GetWakeupOrShutdown";
	public static final String VOL_VALUE = "GetVolumeValue";
	public static final String MUTE_STATE_GET = "GetMuteState";
	public static final String MUTE_STATE_SET_INT = "SetMuteState";
	public static final String MEDIA_RES = "MediaResouce";
	public static final String MEDIA_RES_NEW = "MediaResouceNew";
	public static final String MEDIA_PLAY_STATE = "MediaPlayerState";
	public static final String MEDIA_SPECIFY_DETAILS = "SpecifyTheMediaDetails";
	public static final String MEDIA_PLAYING_DETAILS = "PlayingMediaDetails";

	public static final String MODE_SETTING = "ModeSetting";
	public static final String MODE_SET_REPEAT_ALL = "repeat_all";
	public static final String MODE_SET_REPEAT_ONE = "repeat_one";
	public static final String MODE_SET_NORMAL_ = "normal";
	public static final String MODE_SET_SHUFFLE = "shuffle";

	public static final String EFFECT_SET = "SetEffectVol";

	public static final String SEL_MEDIA_RES = "SelectMeidaResource";
	public static final String SEL_MEDIA_RES_ALL = "ALL";
	public static final String SEL_MEDIA_RES_SDCARD = "SDCard";
	public static final String SEL_MEDIA_RES_EXT_SD = "ExternalSD";
	public static final String SEL_MEDIA_RES_USB = "USB";
	public static final String SEL_MEDIA_RES_SITUATION = "Situation";

	public static final String MEDIA_PLAY_SPECIFIED = "PlaySpecifiedMedia";
	public static final String MEDIA_JUMP_AND_PLAY_LOC_TYPE = "JumpToAndPlayLocalMedia";
	public static final String MEDIA_PLAYING_JUMP = "CurrenPlayingMediaJump";
	public static final String VOICE_BROADCAST_GET = "GetVoiceBroadcast";
	public static final String VOICE_BROADCAST_ENABLE_INT = "EnableVoiceBroadcast";
	public static final String VOICE_BROADCAST_GET_SPEC = "GetSpecifiedVoiceBroadcast";
	public static final String VOICE_BROADCAST_GET_SPEC2 = "GetSpecifiedVoiceBroadcast2";
	public static final String TIMER_GET_DETAILS = "GetTimerDetails";
	public static final String TIMER_ADD_ID = "AddTimerId";
	public static final String TIMER_ENABLE_ID = "EnableTimerId";
	public static final String HDMI_ENABLE = "HdmiEnable";

	public static final String ROOM_AND_NUMBER_GET = "GetRoomAndNumber";
	public static final String ROOM_AND_NUMBER_SET = "SetRoomAndNumber";
	public static final String AUDIO_SRC_NUMBER_GET = "GetAudioSourceNumber";
	public static final String AUDIO_SRC_SET_INT = "SetAudioSource";

	public static final String ZONING_SET = "SetZoning";
	public static final String ZONING_GET = "GetZoning";
	public static final String MEDIA_SPECIFY_DETAILS2 = "SpecifyTheMediaDetails2";

	public static final String ERROR_FEEDBACK = "ErrorFeedback";
	public static final int ERROR_FB_INVOILD = 1;// ָ�����
	public static final int ERROR_FB_ECC = 2;// У�������
	public static final int ERROR_FB_LEN = 3;// ���ȴ���
	public static final int ERROR_FB_ROOM_NUMBER = 4;// ����Ŵ���
	public static final int ERROR_FB_UNUSUAL = 5;// �쳣����

	public static byte getCurrentKeyCmd(String Key_Func) {
		byte cmd = 0;
		switch (Key_Func) {
		case KF_WAKE_UP:
			cmd = 0;
			break;
		case KF_SHUTDOWN:
			cmd = 1;
			break;
		case KF_PLAYER_PLAY:
			cmd = 2;
			break;
		case KF_PLAYER_PAUSE:
			cmd = 3;
			break;
		case KF_PLAYER_PLAYPAUSE:
			cmd = 4;
			break;
		case KF_PLAYER_STOP:
			cmd = 5;
			break;
		case KF_VOL_DOWN:
			cmd = 6;
			break;
		case KF_VOL_UP:
			cmd = 7;
			break;
		case KF_AUTO_MUTE_OFF:
			cmd = 8;
			break;
		case KF_AUTO_MUTE_ON:
			cmd = 9;
			break;
		case KF_AUTO_MUTE:
			cmd = 0x0a;
			break;
		case KF_PLAYER_PRE:
			cmd = 0x0b;
			break;
		case KF_PLAYER_NEXT:
			cmd = 0x0c;
			break;
		case KF_AUX:
			cmd = 0x0d;
			break;
		case KF_ATY_MAIN:
			cmd = 0x10;
			break;
		case KF_ATY_LM_TOTAL:
			cmd = 0x11;
			break;
		case KF_ATY_LM_MEMORY:
			cmd = 0x12;
			break;
		case KF_ATY_LM_SDCARD:
			cmd = 0x13;
			break;
		case KF_ATY_LM_USB:
			cmd = 0x14;
			break;
		case KF_ATY_LM_SITUATION:
			cmd = 0x15;
			break;
		case KF_ATY_TIMER_SETTING:
			cmd = 0x16;
			break;
		case KF_ATY_SETTINGS:
			cmd = 0x17;
			break;
		case KF_ATY_DLNA:
			cmd = 0x18;
			break;
		case KF_ATY_VOICE_BROADCAST:
			cmd = 0x19;
			break;
		case KF_ATY_EFFECTS:
			cmd = 0x1a;
			break;
		case KF_ATY_CLOCK_SET:
			cmd = 0x1b;
			break;
		case KF_ATY_SCREEN_SAVE:
			cmd = 0x1c;
			break;
		case KF_ATY_DESKTOP:
			cmd = 0x1d;
			break;
		case KF_KEY_ENTER:
			cmd = 0x20;
			break;
		case KF_KEY_BACK:
			cmd = 0x21;
			break;
		}
		return cmd;
	}
}
