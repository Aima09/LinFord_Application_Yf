/**
 * FeedBacker.java[V 1.0.0]
 * @author xujie@yf-space.com create on 2015-6-24
 */

package com.yf.serial;


import com.yf.serial.yfpublic.utils.CommonLog;
import com.yf.serial.yfpublic.utils.LogFactory;

public class FeedBacker implements Executor.ISerial {
	private static final CommonLog log = LogFactory.createLog(FeedBacker.class.getSimpleName());
	// 起始符--ID号---数据长度--数据包--校验码--结束符
	// 1字节---2字节--1字节-----<256---1字节---1字节
	// 校验码 = ID号+数据长度+数据=CRC
	private static final byte RS_HEAD = (byte) 0xFC;
	private static final byte RS_END = (byte) 0xFE;

	private ISSend IISend;

	public void setIISend(ISSend IISend) {
		this.IISend = IISend;
	}

	/**
	 * 处理String类型
	 *
	 * @see Executor.ISerial#sendDataToSerial(int,
	 *      String, String)
	 *      这里不要将Byte转化为String，再使用此方法，错误示例Constants.MEDIA_PLAY_STATE、
	 *      KF_PLAYER_PRE
	 */
	@Override
	public void sendDataToSerial(int roomNumber, String function, String partemterString) {
		log.i("roomNumber " + roomNumber + ", function " + function + ", p1 " + partemterString);
		byte numHight = (byte) (roomNumber >> 8);
		byte numLow = (byte) (roomNumber & 0xff);
		byte instruction = 0;
		switch (function) {
			case Constants.DEV_INFO:
				instruction = 0x02;
				break;
			case Constants.VOICE_BROADCAST_GET_SPEC:
				instruction = 0x6f;
				break;
			case Constants.MEDIA_PLAY_STATE:
			case Constants.MEDIA_JUMP_AND_PLAY_LOC_TYPE:
			case Constants.MEDIA_PLAY_SPECIFIED:
				instruction = (byte) 0x81;
				break;
			case Constants.KF_PLAYER_PRE:
			case Constants.KF_PLAYER_NEXT:
				instruction = 0x03;
				break;

			default:
				log.e("exp.");
				return;
		}

		byte[] data = partemterString.getBytes();
		byte dataLength = (byte) (data.length + 1);

		byte bECC = (byte) (numHight + numLow + dataLength + instruction);
		for (int i = 0; i < data.length; i++) {
			bECC += data[i];
		}

		byte[] buffer = new byte[dataLength + 6];
		buffer[0] = RS_HEAD;
		buffer[1] = numHight;
		buffer[2] = numLow;
		buffer[3] = dataLength;
		buffer[4] = instruction;
		for (int j = 0; j < data.length; j++) {
			buffer[5 + j] = data[j];
		}
		buffer[buffer.length - 2] = bECC;
		buffer[buffer.length - 1] = RS_END;
		if (IISend != null) {
			IISend.transferData(buffer, buffer.length);
		}
	}

	/**
	 * 处理bytes类型
	 *
	 * @see Executor.ISerial#sendDataToSerial(int,
	 *      String, byte[], int)
	 */
	@Override
	public void sendDataToSerial(int roomNumber, String function, byte[] buf, int size) {
		log.i("roomNumber " + roomNumber + ", function " + function + ", size " + size);
		byte numHight = (byte) (roomNumber >> 8);
		byte numLow = (byte) (roomNumber & 0xff);
		byte instruction = 0;
		switch (function) {
			case Constants.CURRENT_BUARDRATE:
				instruction = 0x08;
				break;
			case Constants.KF_PLAYER_PRE:
			case Constants.KF_PLAYER_NEXT:
				instruction = 0x03;
				break;
			case Constants.DEV_STATE_INFO:
				instruction = (byte) 0x0d;
				break;
			case Constants.MEDIA_PLAY_STATE:
			case Constants.MEDIA_JUMP_AND_PLAY_LOC_TYPE:
			case Constants.MEDIA_PLAY_SPECIFIED:
				instruction = (byte) 0x81;
				break;
			case Constants.MEDIA_RES:
				instruction = (byte) 0x8c;
				break;
			case Constants.MEDIA_RES_NEW:
				instruction = (byte) 0x4c;
				break;
			case Constants.MEDIA_SPECIFY_DETAILS:
				instruction = (byte) 0x83;
				break;
			case Constants.MEDIA_SPECIFY_DETAILS2:
				instruction = (byte) 0x73;
				break;
			case Constants.MEDIA_PLAYING_DETAILS:
			case Constants.MEDIA_PLAYING_JUMP:
				instruction = (byte) 0x85;
				break;
			case Constants.ROOM_AND_NUMBER_GET:
				instruction = (byte) 0x69;
				break;
			case Constants.ROOM_AND_NUMBER_SET:
				instruction = (byte) 0x6a;
				break;
			case Constants.TIMER_GET_DETAILS:
				instruction = (byte) 0x64;
				break;
			case Constants.ZONING_GET:
			case Constants.ZONING_SET:
				instruction = 0x71;
				break;
			case Constants.KF_PLAYER_PLAY:
			case Constants.KF_PLAYER_PAUSE:
			case Constants.KF_PLAYER_PLAYPAUSE:
			case Constants.KF_PLAYER_STOP:

			case Constants.KF_AUTO_MUTE_OFF:
			case Constants.KF_AUTO_MUTE_ON:
			case Constants.KF_AUTO_MUTE:

			case Constants.AUDIO_SRC_SET_INT:
			case Constants.KF_AUX:
			case Constants.KF_ATY_MAIN:
			case Constants.KF_ATY_LM_TOTAL:
			case Constants.KF_ATY_LM_MEMORY:
			case Constants.KF_ATY_LM_SDCARD:
			case Constants.KF_ATY_LM_USB:
			case Constants.KF_ATY_LM_SITUATION:
			case Constants.KF_ATY_TIMER_SETTING:
			case Constants.KF_ATY_SETTINGS:
			case Constants.KF_ATY_DLNA:
			case Constants.KF_ATY_VOICE_BROADCAST:
			case Constants.KF_ATY_EFFECTS:
			case Constants.KF_ATY_SCREEN_SAVE:
			case Constants.KF_ATY_DESKTOP:
			case Constants.KF_KEY_ENTER:
			case Constants.KF_KEY_BACK:

			case Constants.KF_VOL_DOWN:
			case Constants.KF_VOL_UP:

			case Constants.KF_SHUTDOWN:
			case Constants.KF_WAKE_UP:
				instruction = 0x03;
				break;
			case Constants.VOICE_BROADCAST_GET_SPEC2:
				instruction = 0x4d;
				break;

			default:
				log.e("exp.");
				return;
		}

		byte dataLength = (byte) (size + 1);

		byte bECC = (byte) (numHight + numLow + dataLength + instruction);
		for (int i = 0; i < size; i++) {
			bECC += buf[i];
		}

		byte[] buffer = new byte[dataLength + 6];
		buffer[0] = RS_HEAD;
		buffer[1] = numHight;
		buffer[2] = numLow;
		buffer[3] = dataLength;
		buffer[4] = instruction;
		for (int j = 0; j < size; j++) {
			buffer[5 + j] = buf[j];
		}

		buffer[buffer.length - 2] = bECC;
		buffer[buffer.length - 1] = RS_END;
		if (IISend != null) {
			IISend.transferData(buffer, buffer.length);
		}
	}

	/**
	 * 处理Integer类型
	 *
	 * @see Executor.ISerial#sendDataToSerial(int,
	 *      String, int)
	 */
	@Override
	public void sendDataToSerial(int roomNumber, String function, int partemterInt) {
		log.i("roomNumber " + roomNumber + ", function " + function + ", partemterInt " + partemterInt);
		byte numHight = (byte) (roomNumber >> 8);
		byte numLow = (byte) (roomNumber & 0xff);
		byte instruction = 0;

		switch (function) {
			case Constants.DEV_MENU_POTION:
				instruction = 0x0b;
				break;
			case Constants.VOICE_BROADCAST_GET:
				instruction = (byte) 0x61;
				break;
			case Constants.AUDIO_SRC_NUMBER_GET:
				instruction = (byte) 0x6c;
				break;
			case Constants.WAKE_SHUT_STATE:
				instruction = (byte) 0x14;
				break;
			case Constants.VOL_SETTIONGS:
				instruction = (byte) 0x12;
				break;
			case Constants.VOL_VALUE:
				instruction = (byte) 0x16;
				break;
			case Constants.MUTE_STATE_GET:
			case Constants.MUTE_STATE_SET_INT:
				instruction = (byte) 0x18;
				break;
			case Constants.VOICE_BROADCAST_ENABLE_INT:
				instruction = (byte) 0x62;
				break;
			case Constants.ROOM_AND_NUMBER_SET:
				instruction = (byte) 0x6a;
				break;
			case Constants.MODE_SETTING:
				instruction = (byte) 0x86;
				break;
			case Constants.EFFECT_SET:
				instruction = (byte) 0x87;
				break;
			case Constants.ERROR_FEEDBACK:
				instruction = (byte) 0x99;
				break;
			default:
				log.e("exp.");
				return;
		}

		byte bPar = (byte) partemterInt;
		byte bECC = (byte) (numHight + numLow + 0x02 + instruction + partemterInt);
		byte[] buffer = { RS_HEAD, numHight, numLow, 0x02, instruction, bPar, bECC, RS_END };
		if (IISend != null) {
			IISend.transferData(buffer, buffer.length);
		}

	}

}
