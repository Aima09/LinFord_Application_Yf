/**
 * Executor.java[V 1.0.0]
 * classes: com.YF.YuanFang.YFServer.serial.rs485.Executor
 * xuie	create 2015-4-30 ����5:53:06
 */

package com.yf.serial;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Message;

import com.yf.serial.yfpublic.utils.CommonLog;
import com.yf.serial.yfpublic.utils.LogFactory;
import com.yf.serial.yfpublic.utils.WeakHandler;

import yf.com.gorgecommidemo.config_server;


@SuppressLint("InlinedApi")
public class Executor {

    private static final CommonLog log = LogFactory.createLog(Executor.class.getSimpleName());

    private static final int MSG_KF_PLAY = 0;
    private static final int MSG_KF_PAUSE = 1;
    private static final int MSG_KF_PLAYPAUSE = 2;
    private static final int MSG_KF_STOP = 3;
    private static final int MSG_KF_PRE = 4;
    private static final int MSG_KF_NEXT = 5;
    private static final int MSG_KF_PLAY_INT = 6;
    private static final int MSG_AUDIO_SRC_SWITCH = 7;
    private static final int MSG_AUDIO_SRC_SET_INT = 8;
    private static final int MSG_MUSIC_PLAYER_PAUSE = 9;

    private MyHandler handler = new MyHandler(this);

    private static class MyHandler extends WeakHandler<Executor> {
        public MyHandler(Executor owner) {
            super(owner);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Executor executor = getOwner();
            int roomNumber;
            String parameter;
            log.i("msg.what " + msg.what);
        /*switch (msg.what) {
			case MSG_KF_PLAY:
				roomNumber = msg.arg1;
				parameter = (String) msg.obj;
				if (executor.musicControlCenter.getMeidaSize() == 0)
					return;
				executor.musicControlCenter.replay();
				executor.sendPlayPauseState(roomNumber, parameter);
				break;
			case MSG_KF_PAUSE:
				roomNumber = msg.arg1;
				parameter = (String) msg.obj;
				if (executor.musicControlCenter.getMeidaSize() == 0)
					return;
				executor.musicControlCenter.pause();
				executor.sendPlayPauseState(roomNumber, parameter);
				break;
			case MSG_KF_PLAYPAUSE:
				roomNumber = msg.arg1;
				parameter = (String) msg.obj;
				if (executor.musicControlCenter.getMeidaSize() == 0)
					return;
				if (BaseActivity.mPEngineImpl.isPlaying()) {
					executor.musicControlCenter.pause();
				} else {
					executor.musicControlCenter.replay();
				}
				executor.sendPlayPauseState(roomNumber, parameter);
				break;
			case MSG_KF_STOP:
				roomNumber = msg.arg1;
				parameter = (String) msg.obj;
				if (executor.musicControlCenter.getMeidaSize() == 0)
					return;
				executor.musicControlCenter.stop();
				executor.sendPlayPauseState(roomNumber, parameter);
				break;
			case MSG_KF_PRE:
				roomNumber = msg.arg1;
				parameter = (String) msg.obj;
				if (executor.musicControlCenter.getMeidaSize() == 0)
					return;
				executor.musicControlCenter.btnPrev();
				executor.sendMusicState(parameter, roomNumber);
				break;
			case MSG_KF_NEXT:
				roomNumber = msg.arg1;
				parameter = (String) msg.obj;
				if (executor.musicControlCenter.getMeidaSize() == 0)
					return;
				executor.musicControlCenter.btnNext();
				executor.sendMusicState(parameter, roomNumber);
				break;
			case MSG_KF_PLAY_INT:
				roomNumber = msg.arg1;
				parameter = (String) msg.obj;
				int index = msg.arg2;
				// log.i("index " + index + ", size " +
				// executor.musicControlCenter.getMeidaSize());
				if (executor.musicControlCenter.getMeidaSize() >= index + 1) {
					executor.musicControlCenter.play(index);
				}

				executor.sendMusicState(parameter, roomNumber);
				break;
			case MSG_AUDIO_SRC_SWITCH:
				roomNumber = msg.arg1;
				parameter = (String) msg.obj;
				AudioSourceProxy.getInstance().nextAudioSource();
				executor.sendCurrentAux(parameter, roomNumber);
				break;
			case MSG_AUDIO_SRC_SET_INT:
				roomNumber = msg.arg1;
				parameter = (String) msg.obj;
				int as_audio = msg.arg2;
				AudioSourceProxy.getInstance().enableAudioSource(as_audio);
				executor.sendCurrentAux(parameter, roomNumber);
				break;
			case MSG_MUSIC_PLAYER_PAUSE:
				Utils.pausePlayerMusic();
				break;
			default:
				break;
		}*/
        }
    }

    // new add code

    private void sendPlayPauseState(int roomNumber, String parameter) {
		/*if (BaseActivity.mPEngineImpl != null) {
			sendPlayPauseState(roomNumber, parameter, BaseActivity.mPEngineImpl.getPlayState());
		} else {
			sendPlayPauseState(roomNumber, parameter, PlayState.MPS_INVALID);
		}*/
    }

    private void sendPlayPauseState(int roomNumber, String parameter, int state) {
        if (ISerial != null) {
            byte[] data = new byte[2];
            data[0] = Constants.getCurrentKeyCmd(parameter);
            data[1] = (byte) state;
            log.d("发出状态"+state);
            ISerial.sendDataToSerial(roomNumber, parameter, data, 2);
        }
    }

    // new add end

    private void handlerExe(int cmd, int roomNumber, String parameter) {
        Message playMessage = new Message();
        playMessage.what = cmd;
        playMessage.arg1 = roomNumber;
        playMessage.obj = parameter;
        handler.sendMessage(playMessage);
    }

	/*private MusicControlCenter musicControlCenter;
	// 这里专门来处理指定歌曲播放，当是同类型且同界面时，不进行切换页面
	private MediaType preMt = MediaType.INVALID;*/

    private void keyFunction(int roomNumber, String function, String parameter) {
		/*if (parameter.equals(Constants.KF_WAKE_UP)) {
			WiseUtils.boot();
			// 返回开机后状态
			if (ISerial != null) {
				byte[] data = new byte[2];
				data[0] = Constants.getCurrentKeyCmd(parameter);
				data[1] = (byte) config_server.IS_WAKE_UP;
				ISerial.sendDataToSerial(roomNumber, parameter, data, 2);
			}
		} else if (parameter.equals(Constants.KF_SHUTDOWN)) {
			WiseUtils.shutdown();
			log.i("justScreen = " + true);
			// 返回关机后状态
			handler.sendEmptyMessage(MSG_MUSIC_PLAYER_PAUSE);
			if (ISerial != null) {
				byte[] data = new byte[2];
				data[0] = Constants.getCurrentKeyCmd(parameter);
				data[1] = (byte) config_server.IS_SHUT_DOWN;
				ISerial.sendDataToSerial(roomNumber, parameter, data, 2);
			}
		} else*/
        //播放音乐按键
//        if(PlayerControllerManager.getInstance().getIAidlPlayerControllerInterface()==null){
//                return;
//            }
//            if (parameter.equals(Constants.KF_PLAYER_PLAY)) {//播放
//                log.i("播放");
//                try {
//                    PlayerControllerManager.getInstance().getIAidlPlayerControllerInterface().play();
//                    sendPlayPauseState(roomNumber, parameter,PlayerControllerManager.getInstance().getIAidlPlayerControllerInterface().getPlayStatus());
//                }catch (RemoteException e){
//                    e.printStackTrace();
//                }
//            } else if (parameter.equals(Constants.KF_PLAYER_PAUSE)) {//暂停
//                log.i("暂停");
//                try {
//                    PlayerControllerManager.getInstance().getIAidlPlayerControllerInterface().pause();
//                    sendPlayPauseState(roomNumber, parameter,PlayerControllerManager.getInstance().getIAidlPlayerControllerInterface().getPlayStatus());
//                }catch (RemoteException e){
//                    e.printStackTrace();
//                }
//            } else if (parameter.equals(Constants.KF_PLAYER_PLAYPAUSE)) {//播放暂停
//                log.i("播放暂停");
//                try {
//                    PlayerControllerManager.getInstance().getIAidlPlayerControllerInterface().playPause();
//                    sendPlayPauseState(roomNumber, parameter,PlayerControllerManager.getInstance().getIAidlPlayerControllerInterface().getPlayStatus());
//                }catch (RemoteException e){
//                    e.printStackTrace();
//                }
//            } else if (parameter.equals(Constants.KF_PLAYER_STOP)) {//停止
//                log.i("停止");
//                try {
//                    PlayerControllerManager.getInstance().getIAidlPlayerControllerInterface().stop();
//                    sendPlayPauseState(roomNumber, parameter,PlayerControllerManager.getInstance().getIAidlPlayerControllerInterface().getPlayStatus());
//                }catch (RemoteException e){
//                    e.printStackTrace();
//                }
//            } else if (parameter.equals(Constants.KF_VOL_DOWN)) {//音量-
//                log.i("音量-");
//                try {
//                    PlayerControllerManager.getInstance().getIAidlPlayerControllerInterface().volumeDown();
//                }catch (RemoteException e){
//                    e.printStackTrace();
//                }
//            /* // 返回音量降低后的值
//            if (ISerial != null) {
//                byte[] data = new byte[2];
//                data[0] = Constants.getCurrentKeyCmd(parameter);
//                data[1] = (byte) curVol;
//                ISerial.sendDataToSerial(roomNumber, parameter, data, 2);
//            }*/
//            } else if (parameter.equals(Constants.KF_VOL_UP)) {//音量+
//                log.i("音量+");
//                try {
//                    PlayerControllerManager.getInstance().getIAidlPlayerControllerInterface().volumeUp();
//                }catch (RemoteException e){
//                    e.printStackTrace();
//                }
//            }
//            //一下的两个指令还没做
//            else if (parameter.equals(Constants.KF_AUTO_MUTE_OFF)) {//静音关
//                log.i("静音关");
//                try {
//                    PlayerControllerManager.getInstance().getIAidlPlayerControllerInterface().stop();
//                }catch (RemoteException e){
//                    e.printStackTrace();
//                }
//            } else if (parameter.equals(Constants.KF_AUTO_MUTE_ON)) {//静音开
//                log.i("静音开");
//                try {
//                    PlayerControllerManager.getInstance().getIAidlPlayerControllerInterface().stop();
//                }catch (RemoteException e){
//                    e.printStackTrace();
//                }
//            }else if (parameter.equals(Constants.KF_PLAYER_PRE)) {//上一首
//                log.i("上一首");
//                try {
//                    PlayerControllerManager.getInstance().getIAidlPlayerControllerInterface().previous();
//                }catch (RemoteException e){
//                    e.printStackTrace();
//                }
//            } else if (parameter.equals(Constants.KF_PLAYER_NEXT)) {//下一首
//                log.i("下一首");
//                try {
//                    PlayerControllerManager.getInstance().getIAidlPlayerControllerInterface().next();
//                }catch (RemoteException e){
//                    e.printStackTrace();
//                }
//            }
        }

    public void handle(int roomNumber, String function, String parameter) {

        log.i("roomNumber " + roomNumber + ", function " + function + ", partemter " + parameter);
        if (function == null || function.equals("")) {
            log.e("function is null");
            return;
        }

//        if (room_no == null) {
//            log.e("room_no is null");
//            return;
//        }
        int room_no = Integer.parseInt(config_server.room_no);
        if (roomNumber != 0 && roomNumber != room_no) {
            log.e("room number not equal. " + room_no);
            return;
        }

		/*if (BaseActivity.mPEngineImpl == null) {
			log.e("BaseActivity.mPEngineImpl == null");
			return;
		}*/

        if (function.equals(Constants.DEV_INFO)) {
            /** 0x01 设备信息查询 */
            String hdInfo = android.os.Build.MODEL;
            String softInfo = "1.0";//VersionUpdateManager.getInstance().getVerName(context);
            if (ISerial != null) {
                ISerial.sendDataToSerial(room_no, function, hdInfo + "%%" + softInfo);
            }
        } else if (function.equals(Constants.CURRENT_BUARDRATE)) {
            /** 0x07 查看当前波特率 */
            int burdrate = Integer.parseInt(parameter);
            byte[] bPar = new byte[3];
            bPar[0] = (byte) ((burdrate >> 16) & 0xFF);
            bPar[1] = (byte) ((burdrate >> 8) & 0xFF);
            bPar[2] = (byte) (burdrate & 0xFF);
            log.i("burdrate " + burdrate);
            if (ISerial != null) {
                ISerial.sendDataToSerial(room_no, function, bPar, bPar.length);
            }
        } else if (function.equals(Constants.INIT_BUARDRATE)) {
            /** 0x09 初始化波特率 */
            // ignore
        } else if (function.equals(Constants.KEY_FUNCTION)) {
            /** 0x10 按键指令 */
            if (roomNumber == 0) {
                return;
            }
            keyFunction(room_no, function, parameter);
        }
		/*else if (function.equals(Constants.DEV_MENU_POTION)) {
			//** 0x0a 查寻设备的菜单位置 *//*
			if (ISerial != null) {
				ISerial.sendDataToSerial(room_no, function, config_server.CURRENT_ATY);
			}
		} else if (function.equals(Constants.DEV_STATE_INFO)) {
			//** 0x0c 查寻设备状态 *//*
			byte[] bPar = new byte[6];

			int ms = config_server.CURRENT_ATY; // menu state
			int ss = AudioSourceProxy.getInstance().getAudioSource();
			int hs = Utils.isHdmiConnected() ? 1 : 0;
			bPar[0] = (byte) (((ms << 3) & 0xF8) + ((ss << 1) & 0x06) + (hs & 0x01));
			int volume = YF_AudioManager.getCurrentVolume(context);
			int wakeorshut = judgeCurrentSleepATY();
			int mute = (AudioUtils.isMute(context)) ? 0 : 1;
			bPar[1] = (byte) (((volume << 2) & 0xFC) + ((wakeorshut << 1) & 0x02) + (mute & 0x01));
			int pm; // play mode
			int mt; // media type
			int ps; // play state

			pm = musicControlCenter.getPlayMode();

			mt = musicControlCenter.getCurMediaType().ordinal();
			try {
				ps = BaseActivity.mPEngineImpl.getPlayState();
				if (ps == PlayState.MPS_PLAYING || ps == PlayState.MPS_PAUSE || ps == PlayState.MPS_STOP) {
				} else {
					ps = PlayState.MPS_STOP;
				}
			} catch (Exception e) {
				e.printStackTrace();
				ps = PlayState.MPS_STOP;
			}
			bPar[2] = (byte) ((pm << 4) & 0xF0);
			bPar[3] = (byte) (((mt << 2) & 0xFC) + ((ps) & 0x03));
			int pos = musicControlCenter.getCurPlayInex();
			bPar[4] = (byte) ((pos >> 8) & 0xFF);
			bPar[5] = (byte) (pos & 0xFF);
			if (ISerial != null) {
				ISerial.sendDataToSerial(room_no, function, bPar, bPar.length);
			}

		} else if (function.equals(Constants.KEY_FUNCTION)) {
			*//** 0x10 按键指令 *//*
			if (roomNumber == 0) {
				return;
			}
			keyFunction(room_no, function, parameter);
		} else if (function.equals(Constants.VOL_SETTIONGS)) {
			*//** 0x11 音量设置 *//*
			if (roomNumber == 0) {
				return;
			}
			int volume = Integer.parseInt(parameter);
			log.i("volume " + volume + ", cur volume " + YF_AudioManager.getCurrentVolume(context));
			// YF_AudioManager.setCurrentVolume(volume, context);
			AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
			log.i("volume " + volume + ", cur volume " + YF_AudioManager.getCurrentVolume(context));
			int curVol = YF_AudioManager.getCurrentVolume(context);
			// 返回音量值
			if (ISerial != null) {
				ISerial.sendDataToSerial(room_no, function, curVol);
			}
		} else if (function.equals(Constants.WAKE_SHUT_STATE)) {
			*//** 0x13 开机状态 *//*
			if (roomNumber == 0) {
				return;
			}
			int wakeorshut = judgeCurrentSleepATY();
			// 返回当前开关机状态
			if (ISerial != null) {
				ISerial.sendDataToSerial(room_no, function, wakeorshut);
			}
		} else if (function.equals(Constants.VOL_VALUE)) {
			*//** 0x15 当前音量值 *//*
			if (roomNumber == 0) {
				return;
			}
			int curVol = YF_AudioManager.getCurrentVolume(context);
			// 返回音量值
			if (ISerial != null) {
				ISerial.sendDataToSerial(room_no, function, curVol);
			}
		} else if (function.equals(Constants.MUTE_STATE_GET)) {
			*//** 0x17 查寻静音状态 *//*
			int state = (AudioUtils.isMute(context)) ? 0 : 1;
			if (ISerial != null) {
				ISerial.sendDataToSerial(room_no, function, state);
			}
		} else if (function.equals(Constants.MUTE_STATE_SET_INT)) {
			*//** 0x19 静音设置 *//*
			if (roomNumber == 0) {
				return;
			}

			int state = Integer.parseInt(parameter);
			if (0 == state) {
				// 静音开
				AudioUtils.setMute(context, true);
				retMuteInfomation(roomNumber, function);
			} else if (1 == state) {
				// 静音关
				AudioUtils.setMute(context, false);
				retMuteInfomation(roomNumber, function);
			} else {
				return;
			}
		} else if (function.equals(Constants.MEDIA_RES)) {
			*//** 0x8b 查寻音乐资源数目 *//*
			MediaSource mediaUtils = MediaSource.getInstance(context);
			// All
			int aA = mediaUtils.getItems(MediaType.MEDIA).size();
			int vA = mediaUtils.getItems(MediaType.VIDEO).size();
			int iA = mediaUtils.getItems(MediaType.IMAGE).size();
			int rA = mediaUtils.getItems(MediaType.RADIO).size();
			// Memory
			int aM = mediaUtils.getItems(MediaType.MEDIA_M).size();
			int vM = mediaUtils.getItems(MediaType.VIDEO_M).size();
			int iM = mediaUtils.getItems(MediaType.IMAGE_M).size();
			int rM = mediaUtils.getItems(MediaType.RADIO_M).size();
			// SDCARD
			int aS = mediaUtils.getItems(MediaType.MEDIA_S).size();
			int vS = mediaUtils.getItems(MediaType.VIDEO_S).size();
			int iS = mediaUtils.getItems(MediaType.IMAGE_S).size();
			int rS = mediaUtils.getItems(MediaType.RADIO_S).size();
			// USB
			int aU = mediaUtils.getItems(MediaType.MEDIA_U).size();
			int vU = mediaUtils.getItems(MediaType.VIDEO_U).size();
			int iU = mediaUtils.getItems(MediaType.IMAGE_U).size();
			int rU = mediaUtils.getItems(MediaType.RADIO_U).size();
			// Situation
			int easy = mediaUtils.getItems(MediaType.EASY).size();
			int popular = mediaUtils.getItems(MediaType.POPULAR).size();
			int classical = mediaUtils.getItems(MediaType.CLASSICAL).size();
			int rock = mediaUtils.getItems(MediaType.ROCK).size();
			int meeting = mediaUtils.getItems(MediaType.METTING).size();
			int repast = mediaUtils.getItems(MediaType.REPEAST).size();
			int joy = mediaUtils.getItems(MediaType.JOY).size();
			int relaxation = mediaUtils.getItems(MediaType.RELAXATION).size();

			byte[] bPar = new byte[24];
			bPar[0] = (byte) (aA & 0xFF);
			bPar[1] = (byte) (aM & 0xFF);
			bPar[2] = (byte) (aS & 0xFF);
			bPar[3] = (byte) (aU & 0xFF);

			bPar[4] = (byte) (vA & 0xFF);
			bPar[5] = (byte) (vM & 0xFF);
			bPar[6] = (byte) (vS & 0xFF);
			bPar[7] = (byte) (vU & 0xFF);

			bPar[8] = (byte) (iA & 0xFF);
			bPar[9] = (byte) (iM & 0xFF);
			bPar[10] = (byte) (iS & 0xFF);
			bPar[11] = (byte) (iU & 0xFF);

			bPar[12] = (byte) (rA & 0xFF);
			bPar[13] = (byte) (rM & 0xFF);
			bPar[14] = (byte) (rS & 0xFF);
			bPar[15] = (byte) (rU & 0xFF);

			bPar[16] = (byte) (easy & 0xFF);
			bPar[17] = (byte) (popular & 0xFF);
			bPar[18] = (byte) (classical & 0xFF);
			bPar[19] = (byte) (rock & 0xFF);
			bPar[20] = (byte) (meeting & 0xFF);
			bPar[21] = (byte) (repast & 0xFF);
			bPar[22] = (byte) (joy & 0xFF);
			bPar[23] = (byte) (relaxation & 0xFF);

			// for (int i = 0; i < bPar.length; i++) {
			// System.out.print((bPar[i] & 0xFF) + " ");
			// }
			// System.out.println();
			if (ISerial != null) {
				ISerial.sendDataToSerial(room_no, function, bPar, bPar.length);
			}
		} else if (function.equals(Constants.MEDIA_RES_NEW)) {
			*//**
         * 0x4b 查寻音乐资源数目 这个是用于取代0x8b的、之前的存在漏洞，bit位不足
         *//*
			MediaSource mediaUtils = MediaSource.getInstance(context);
			// All
			int aA = mediaUtils.getItems(MediaType.MEDIA).size();
			int vA = mediaUtils.getItems(MediaType.VIDEO).size();
			int iA = mediaUtils.getItems(MediaType.IMAGE).size();
			int rA = mediaUtils.getItems(MediaType.RADIO).size();
			// Memory
			int aM = mediaUtils.getItems(MediaType.MEDIA_M).size();
			int vM = mediaUtils.getItems(MediaType.VIDEO_M).size();
			int iM = mediaUtils.getItems(MediaType.IMAGE_M).size();
			int rM = mediaUtils.getItems(MediaType.RADIO_M).size();
			// SDCARD
			int aS = mediaUtils.getItems(MediaType.MEDIA_S).size();
			int vS = mediaUtils.getItems(MediaType.VIDEO_S).size();
			int iS = mediaUtils.getItems(MediaType.IMAGE_S).size();
			int rS = mediaUtils.getItems(MediaType.RADIO_S).size();
			// USB
			int aU = mediaUtils.getItems(MediaType.MEDIA_U).size();
			int vU = mediaUtils.getItems(MediaType.VIDEO_U).size();
			int iU = mediaUtils.getItems(MediaType.IMAGE_U).size();
			int rU = mediaUtils.getItems(MediaType.RADIO_U).size();
			// Situation
			int easy = mediaUtils.getItems(MediaType.EASY).size();
			int popular = mediaUtils.getItems(MediaType.POPULAR).size();
			int classical = mediaUtils.getItems(MediaType.CLASSICAL).size();
			int rock = mediaUtils.getItems(MediaType.ROCK).size();
			int meeting = mediaUtils.getItems(MediaType.METTING).size();
			int repast = mediaUtils.getItems(MediaType.REPEAST).size();
			int joy = mediaUtils.getItems(MediaType.JOY).size();
			int relaxation = mediaUtils.getItems(MediaType.RELAXATION).size();

			byte[] bPar = new byte[48];
			bPar[0] = (byte) ((aA >> 8) & 0xFF);
			bPar[1] = (byte) (aA & 0xFF);
			bPar[2] = (byte) ((aM >> 8) & 0xFF);
			bPar[3] = (byte) (aM & 0xFF);
			bPar[4] = (byte) ((aS >> 8) & 0xFF);
			bPar[5] = (byte) (aS & 0xFF);
			bPar[6] = (byte) ((aU >> 8) & 0xFF);
			bPar[7] = (byte) (aU & 0xFF);

			bPar[8] = (byte) ((vA >> 8) & 0xFF);
			bPar[9] = (byte) (vA & 0xFF);
			bPar[10] = (byte) ((vM >> 8) & 0xFF);
			bPar[11] = (byte) (vM & 0xFF);
			bPar[12] = (byte) ((vS >> 8) & 0xFF);
			bPar[13] = (byte) (vS & 0xFF);
			bPar[14] = (byte) ((vU >> 8) & 0xFF);
			bPar[15] = (byte) (vU & 0xFF);

			bPar[16] = (byte) ((iA >> 8) & 0xFF);
			bPar[17] = (byte) (iA & 0xFF);
			bPar[18] = (byte) ((iM >> 8) & 0xFF);
			bPar[19] = (byte) (iM & 0xFF);
			bPar[20] = (byte) ((iS >> 8) & 0xFF);
			bPar[21] = (byte) (iS & 0xFF);
			bPar[22] = (byte) ((iU >> 8) & 0xFF);
			bPar[23] = (byte) (iU & 0xFF);

			bPar[24] = (byte) ((rA >> 8) & 0xFF);
			bPar[25] = (byte) (rA & 0xFF);
			bPar[26] = (byte) ((rM >> 8) & 0xFF);
			bPar[27] = (byte) (rM & 0xFF);
			bPar[28] = (byte) ((rS >> 8) & 0xFF);
			bPar[29] = (byte) (rS & 0xFF);
			bPar[30] = (byte) ((rU >> 8) & 0xFF);
			bPar[31] = (byte) (rU & 0xFF);

			bPar[32] = (byte) ((easy >> 8) & 0xFF);
			bPar[33] = (byte) (easy & 0xFF);
			bPar[34] = (byte) ((popular >> 8) & 0xFF);
			bPar[35] = (byte) (popular & 0xFF);
			bPar[36] = (byte) ((classical >> 8) & 0xFF);
			bPar[37] = (byte) (classical & 0xFF);
			bPar[38] = (byte) ((rock >> 8) & 0xFF);
			bPar[39] = (byte) (rock & 0xFF);
			bPar[40] = (byte) ((meeting >> 8) & 0xFF);
			bPar[41] = (byte) (meeting & 0xFF);
			bPar[42] = (byte) ((repast >> 8) & 0xFF);
			bPar[43] = (byte) (repast & 0xFF);
			bPar[44] = (byte) ((joy >> 8) & 0xFF);
			bPar[45] = (byte) (joy & 0xFF);
			bPar[46] = (byte) ((relaxation >> 8) & 0xFF);
			bPar[47] = (byte) (relaxation & 0xFF);

			// for (int i = 0; i < bPar.length; i++) {
			// System.out.print((bPar[i] & 0xFF) + " ");
			// }
			// System.out.println();
			if (ISerial != null) {
				ISerial.sendDataToSerial(room_no, function, bPar, bPar.length);
			}
		} else if (function.equals(Constants.MEDIA_PLAY_STATE)) {
			*//** 0x80 查寻音乐播放器的状态 *//*
			log.d("1111111111111111111111111");
			sendMusicState(function, room_no);
		} else if (function.equals(Constants.MEDIA_SPECIFY_DETAILS)) {
			*//** 0x82 查寻音乐界面指定序号音乐详细信息 *//*
			int cutPos = parameter.indexOf('-');
			int iType = Integer.parseInt(parameter.substring(0, cutPos));
			int index = Integer.parseInt(parameter.substring(cutPos + 1));
			MediaType type = MediaType.values()[iType];
			log.i("itype " + iType + ", pos " + index + ", type " + type);
			MediaSource mediaUtils = MediaSource.getInstance(context);
			List<MediaItem> medias = mediaUtils.getItems(type);
			if (medias != null && medias.size() >= index + 1) {
				byte[] bTmp = medias.get(index).getTitle().getBytes();
				byte[] bPar = new byte[2 + bTmp.length];
				bPar[0] = (byte) ((index >> 8) & 0xFF);
				bPar[1] = (byte) (index & 0xFF);
				for (int i = 0; i < bTmp.length; i++) {
					bPar[i + 2] = bTmp[i];
				}
				if (ISerial != null) {
					ISerial.sendDataToSerial(room_no, function, bPar, bPar.length);
				}
			} else {
				log.e("...look...");
			}
		} else if (function.equals(Constants.MEDIA_PLAYING_DETAILS)) {
			*//** 0x84 查寻音乐播放器正播放的音乐详细信息 *//*
			cmd85_callback(function, room_no);
		} else if (function.equals(Constants.MODE_SETTING)) {
			*//** 0x86 播放模式设置 *//*
			if (roomNumber == 0) {
				return;
			}
			if (parameter.equals(Constants.MODE_SET_REPEAT_ALL)) {
				musicControlCenter.setPlayMode(PlayMode.MPM_REPEAT_ALL);
			} else if (parameter.equals(Constants.MODE_SET_REPEAT_ONE)) {
				musicControlCenter.setPlayMode(PlayMode.MPM_REPEAT_ONE);
			} else if (parameter.equals(Constants.MODE_SET_NORMAL_)) {
				musicControlCenter.setPlayMode(PlayMode.MPM_NORMAL);
			} else if (parameter.equals(Constants.MODE_SET_SHUFFLE)) {
				musicControlCenter.setPlayMode(PlayMode.MPM_SHUFFLE);
			}

			if (ISerial != null) {
				ISerial.sendDataToSerial(room_no, function, musicControlCenter.getPlayMode());
			}
			updateMainUi();
		} else if (function.equals(Constants.EFFECT_SET)) {
			*//** 0x87 音效设置 *//*
			if (roomNumber == 0) {
				return;
			}

			musicControlCenter.setEqualizer(Integer.parseInt(parameter));
			if (ISerial != null) {
				ISerial.sendDataToSerial(room_no, function, musicControlCenter.getEqType());
			}
		} else if (function.equals(Constants.MEDIA_PLAY_SPECIFIED) //
				|| function.equals(Constants.MEDIA_JUMP_AND_PLAY_LOC_TYPE) //
				) {
			if (roomNumber == 0) {
				return;
			}

			int iType = 0;
			int index = 0;
			if (function.equals(Constants.MEDIA_PLAY_SPECIFIED)) {
				*//** 0x89 播放指定序号的歌曲 *//*
				log.i("MEDIA_PLAY_SPECIFIED");
				try {
					int cutPos = parameter.indexOf('-');
					iType = Integer.parseInt(parameter.substring(0, cutPos));
					index = Integer.parseInt(parameter.substring(cutPos + 1));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				log.i("MEDIA_JUMP_AND_PLAY_LOC_TYPE");
				*//** 0x8d 跳转本地歌曲且播放 -- 改为随机 *//*
				// index = 0;
				iType = Integer.parseInt(parameter);
				MediaType type = MediaType.values()[iType];
				List<MediaItem> medias = MediaSource.getInstance(context).getItems(type);

				*//*
				 * 添加原因：因为产生的随机数是伪随机数，基数太小时，每次会都是同一首歌曲 所以每次切换，如果是同Type时，须Index不同
				 *//*
				boolean b = false;
				int cIndex = -1;
				if (musicControlCenter.getCurMediaType() == type) {
					cIndex = musicControlCenter.getCurPlayInex();
					if (medias.size() > 1 && cIndex > 0) {
						b = true;
					}
				}

				if (medias != null && medias.size() > 0) {
					if (b) {
						while ((index = (int) (Math.random() * medias.size())) == cIndex)
							;
					} else {
						index = (int) (Math.random() * medias.size());
					}
				} else {
					log.e("get index error!");
					return;
				}
			}
			MediaType mt = MediaType.MEDIA;

			try {
				mt = MediaType.values()[iType];
			} catch (Exception e) {
				log.e("iType = " + iType + ", " + e.getMessage());
			}
			log.i("itype " + iType + ", pos " + index + ", type " + mt);
			switch (mt) {
				case VIDEO:
				case VIDEO_M:
				case VIDEO_S:
				case VIDEO_U:
					List<MediaItem> videos = MediaSource.getInstance(context).getItems(mt);
					if (videos != null && videos.size() > index) {
						handlerExe(MSG_KF_PAUSE, roomNumber, parameter);
						Intent intent = new Intent(context, VideoPlayerActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
						intent.putExtra("path", videos.get(index).getRes());
						context.startActivity(intent);
					} else {
						log.e("video media not exist");
					}
					return;
				case IMAGE:
				case IMAGE_M:
				case IMAGE_S:
				case IMAGE_U:
					List<MediaItem> images = MediaSource.getInstance(context).getItems(mt);
					if (images != null && images.size() > index) {
						log.i("" + index);
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.setDataAndType(Uri.fromFile(new File(images.get(index).getRes())), "image*//*");
						context.startActivity(intent);
					} else {
						log.e("image media not exist");
					}
					return;
				default:
					// include audio radio and situation
					MediaSource mediaUtils = MediaSource.getInstance(context);

					List<MediaItem> list = mediaUtils.getItems(mt);
					if (list != null && list.size() > index) {
						musicControlCenter.updateMediaInfo(0, list, mt, true);
						Message msg = new Message();
						msg.what = MSG_KF_PLAY_INT;
						msg.arg1 = roomNumber;
						msg.obj = function;
						msg.arg2 = index;
						handler.sendMessage(msg);
					} else {
						log.e("default media not exist");
					}
					if (!preMt.equals(mt) || !judgeCurrentLocalATY()) {
						preMt = mt;
						startLocalMediaAty(mt);
					}
					preMt = mt;
					break;
			}
		} else if (function.equals(Constants.MEDIA_PLAYING_JUMP)) {
			*//** 0x8a 当前播放的歌曲跳转 *//*
			if (roomNumber == 0) {
				return;
			}

			if (judgeCurrentVideoATY()) {
				return;
			}

			int cutPos = parameter.indexOf('-');
			int minute = Integer.parseInt(parameter.substring(0, cutPos));
			int second = Integer.parseInt(parameter.substring(cutPos + 1));
			int time = (minute * 60 + second) * 1000;
			log.i("time " + time);

			if (BaseActivity.mPEngineImpl.getCurMediaInfo() != null) {
				musicControlCenter.skipTo(time);
			}
			cmd85_callback(function, room_no);
		} else if (function.equals(Constants.VOICE_BROADCAST_GET)) {
			*//** 0x60 查看语音播报 *//*
			int num = 0;
			File voiceDir = new File(FileHelper.getVoiceDir());
			num = voiceDir.listFiles().length;
			log.i("num " + num);
			if (ISerial != null) {
				ISerial.sendDataToSerial(room_no, function, num);
			}
		} else if (function.equals(Constants.VOICE_BROADCAST_ENABLE_INT)) {
			*//** 0x62 播报指定序号语音 *//*
			if (roomNumber == 0) {
				return;
			}
			int pos = Integer.parseInt(parameter);
			log.i("pos " + pos);
			Utils.startVoiceReadFile(pos);
			if (ISerial != null) {
				ISerial.sendDataToSerial(room_no, function, pos);
			}
		} else if (function.equals(Constants.TIMER_GET_DETAILS)) {
			*//** 0x63 查看闹钟详细信息 *//*
			int count = TimerDao.getDao(context).getCount();
			log.i("count " + count);
			if (count > 0) {
				List<TimerBean> records = TimerDao.getDao(context).getAll();
				byte[] bPar = new byte[1 + records.size() * 7];
				bPar[0] = (byte) (count & 0xFF);
				for (int pos = 0; pos < records.size(); pos++) {
					TimerBean record = records.get(pos);
					int id = record.getId();
					// id 2Byte
					bPar[7 * pos + 1] = (byte) (id >> 8);
					bPar[7 * pos + 2] = (byte) (id & 0xFF);
					// type 1Byte
					String title = record.getType();
					int t = 0;
					if (title.equals(config_server.TIMER_WAKE_UP)) {
						t = 0;
					} else if (title.equals(config_server.TIMER_SHUT_DOWN)) {
						t = 1;
					} else if (title.equals(config_server.TIMER_PLAYING)) {
						t = 2;
					} else if (title.equals(config_server.TIMER_STOP)) {
						t = 3;
					}
					bPar[7 * pos + 3] = (byte) (t & 0xFF);
					// hour 1Byte
					// minute 1Byte
					bPar[7 * pos + 4] = (byte) (record.getHour() & 0xFF);
					bPar[7 * pos + 5] = (byte) (record.getMinute() & 0xFF);
					// week
					int week = (record.getState() & 0x7F);
					bPar[7 * pos + 6] = (byte) (week & 0xFF);
					int repeat = (record.getState() & (1 << 7));
					bPar[7 * pos + 7] = (byte) (repeat & 0xFF);
					log.i(id + ", " + record.toString());
				}

				// for (int i = 0; i < bPar.length; i++) {
				// System.out.print(bPar[i] + " ");
				// }
				// System.out.println();

				if (ISerial != null) {
					ISerial.sendDataToSerial(room_no, function, bPar, bPar.length);
				}
			}
		} else if (function.equals(Constants.VOICE_BROADCAST_GET_SPEC)) {
			*//** 0x6e 查看指定序号语音的名称 *//*
			int pos = Integer.parseInt(parameter);
			log.i("pos " + pos);
			try {
				File file = new File(FileHelper.getVoiceDir()).listFiles()[pos];
				log.i("filePath " + file.getPath());
				if (ISerial != null) {
					ISerial.sendDataToSerial(room_no, function, file.getPath());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (function.equals(Constants.VOICE_BROADCAST_GET_SPEC2)) {
			*//** 0x4d 查看指定序号语音的名称2 用于替代0x6e *//*
			int pos = Integer.parseInt(parameter);
			log.i("pos " + pos);
			try {
				File file = new File(FileHelper.getVoiceDir()).listFiles()[pos];
				log.i("filePath " + file.getPath());
				byte[] bTmp = file.getPath().getBytes();
				byte[] bPar = new byte[bTmp.length + 2];
				bPar[0] = (byte) (pos >> 8);
				bPar[1] = (byte) (pos & 0xFF);
				for (int i = 0; i < bTmp.length; i++) {
					bPar[i + 2] = bTmp[i];
				}
				if (ISerial != null) {
					ISerial.sendDataToSerial(room_no, function, bPar, bPar.length);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (function.equals(Constants.TIMER_ADD_ID)) {
			*//** 0x65 添加闹钟 *//*
			if (roomNumber == 0) {
				return;
			}
			int cutPos = parameter.indexOf('-'), prePos;
			int number = Integer.parseInt(parameter.substring(0, cutPos));
			prePos = cutPos;
			cutPos = parameter.indexOf('-', cutPos + 1);
			int type = Integer.parseInt(parameter.substring(prePos + 1, cutPos));
			prePos = cutPos;
			cutPos = parameter.indexOf('-', cutPos + 1);
			String time = parameter.substring(prePos + 1, cutPos);
			int week = Integer.parseInt(parameter.substring(cutPos + 1));
			log.i("number " + number + ", type " + type + ", time " + time + ", week " + week);

			// add timer
			int id = number;// serial timer SQL id
			String title = config_server.TIMER_WAKE_UP;
			if (type == config_server.IS_SHUT_DOWN) {
				title = config_server.TIMER_SHUT_DOWN;
			} else if (type == config_server.IS_PLAYING) {
				title = config_server.TIMER_PLAYING;
			} else if (type == config_server.IS_STOP) {
				title = config_server.TIMER_STOP;
			}
			int mon = week & 1;
			int tue = (week >> 1) & 1;
			int wed = (week >> 2) & 1;
			int thr = (week >> 3) & 1;
			int fri = (week >> 4) & 1;
			int sta = (week >> 5) & 1;
			int sun = (week >> 6) & 1;
			log.i("id " + id + ", title " + title + ", time " + time);
			log.i(mon + ", " + tue + ", " + wed + ", " + thr + ", " + fri + ", " + sta + ", " + sun);

			int x = time.indexOf(':');
			int h = 0, m = 0;
			if (x != -1) {
				h = Integer.parseInt(time.substring(0, x));
				m = Integer.parseInt(time.substring(x + 1, time.length()));
				log.i(h + ":" + m);
				time = ((h < 10) ? "0" + h : "" + h) + ":" + ((m < 10) ? "0" + m : "" + m);
				log.i(time);
			} else {
				log.e("conversion time exp!");
			}

			TimerBean record = new TimerBean();
			record.setId(id);
			record.setType(title);
			record.setEnable(true);
			record.setHour(h);
			record.setMinute(m);
			record.setState(week);

			record.setFuncType(TCon.ATTR_TYPE_NONE);
			record.setMusicPath(null);
			// xujie unsupport record.setMode(0);
			record.setState(week | (2 << 7));

			TimerDao timerDao = TimerDao.getDao(context);
			timerDao.update(record);

			AlarmClockManager.get().enabled(record);
			context.sendBroadcast(new Intent(SettingsActivity.ATY_UPDATE_UI));
		} else if (function.equals(Constants.TIMER_ENABLE_ID)) {
			*//** 0x66 开启或关闭某序列定时序列 *//*
			if (roomNumber == 0) {
				return;
			}
			int cutPos = parameter.indexOf('-');
			int id = Integer.parseInt(parameter.substring(0, cutPos));
			int isEnabled = Integer.parseInt(parameter.substring(cutPos + 1));
			log.i("id " + id + ", isEnabled " + isEnabled);
			if (isEnabled != 0 && isEnabled != 1) {
				return;
			}
			if (TimerDao.getDao(context).judgeId(id)) {
				TimerBean tb = TimerDao.getDao(context).getTimer(id);
				tb.setEnable(true);
				TimerDao.getDao(context).update(tb);
			}
			context.sendBroadcast(new Intent(SettingsActivity.ATY_UPDATE_UI));
		} else if (function.equals(Constants.HDMI_ENABLE)) {
			*//** 0x67 开关HDMI *//*
			if (roomNumber == 0) {
				return;
			}
			int isOpenHdmi = Integer.parseInt(parameter);
			log.i("isOpenHdmi " + isOpenHdmi);
			Utils.controlHdmi(context, isOpenHdmi == 1);
			context.sendBroadcast(new Intent(SettingsActivity.ATY_UPDATE_UI));
		} else if (function.equals(Constants.ROOM_AND_NUMBER_GET)) {
			*//** 0x68 查看房间名和房间序号 *//*
			byte bHight = (byte) (room_no >> 8);
			byte bLow = (byte) (room_no & 0xFF);
			byte[] bTmp = config_server.room_name.getBytes();
			byte[] bPar = new byte[2 + bTmp.length];
			bPar[0] = bHight;
			bPar[1] = bLow;
			for (int i = 0; i < bTmp.length; i++) {
				bPar[i + 2] = bTmp[i];
			}

			if (ISerial != null) {
				ISerial.sendDataToSerial(room_no, function, bPar, bPar.length);
			}
		} else if (function.equals(Constants.ROOM_AND_NUMBER_SET)) {
			*//** 0x6a 设置房间号 *//*
			if (roomNumber == 0) {
				return;
			}
			int roomNo = Integer.parseInt(parameter);
			log.i("roomNo " + roomNo);
			LocalConfigSharePreference.commintDevName(context, config_server.room_name + "-" + roomNo);
			LocalConfigSharePreference.getDevName(context);
			context.sendBroadcast(new Intent(BaseActivity.RESTART_Engine));
			context.sendBroadcast(new Intent(SettingsActivity.ATY_UPDATE_UI));
			if (ISerial != null) {
				int rn = Integer.parseInt(config_server.room_no);
				byte[] data = new byte[2];
				data[0] = (byte) (rn & 0xFF);
				data[1] = (byte) (rn >> 8);
				ISerial.sendDataToSerial(room_no, function, data, 2);
			}
		} else if (function.equals(Constants.AUDIO_SRC_NUMBER_GET)) {
			*//** 0x6b 查看音源数 *//*
			int num = config_server.audioSrcNumber();
			if (ISerial != null) {
				ISerial.sendDataToSerial(room_no, function, num);
			}
		} else if (function.equals(Constants.AUDIO_SRC_SET_INT)) {
			*//** 0x6d 设置音源 *//*
			if (roomNumber == 0) {
				return;
			}
			int as = 0;
			try {
				as = Integer.parseInt(parameter);
			} catch (Exception e) {
				e.printStackTrace();
			}
			log.d("AUDIO_SRC_SET_INT as:" + as);
			Message msg = new Message();
			msg.what = MSG_AUDIO_SRC_SET_INT;
			msg.arg1 = roomNumber;
			msg.obj = function;
			msg.arg2 = as;
			handler.sendMessage(msg);
		} else if (function.equals(Constants.ZONING_SET)) {
			*//** 0x70 设置四分区 **//*
			if (roomNumber == 0) {
				return;
			}
			String[] zones = parameter.split("-");
			if (zones == null || zones.length != 4) {
				return;
			}

			ZoningControl.getInstance(context).execute485(zones);
			if (ISerial != null) {
				byte[] ret = ZoningControl.getInstance(context).getZones485();
				ISerial.sendDataToSerial(room_no, function, ret, ret.length);
			}

		} else if (function.equals(Constants.ZONING_GET)) {
			*//** 0x72 查看四分区 **//*
			if (roomNumber == 0) {
				return;
			}
			if (ISerial != null) {
				byte[] ret = ZoningControl.getInstance(context).getZones485();
				ISerial.sendDataToSerial(room_no, function, ret, ret.length);
			}
		} else if (function.equals(Constants.MEDIA_SPECIFY_DETAILS2)) {
			*//** 0x73 查寻音乐界面指定序号音乐详细信息 - 2 *//*
			int cutPos = parameter.indexOf('-');
			int iType = Integer.parseInt(parameter.substring(0, cutPos));
			int index = Integer.parseInt(parameter.substring(cutPos + 1));
			MediaType type = MediaType.values()[iType];
			log.i("itype " + iType + ", pos " + index + ", type " + type);
			MediaSource mediaUtils = MediaSource.getInstance(context);
			List<MediaItem> medias = mediaUtils.getItems(type);
			if (medias != null && medias.size() >= index + 1) {
				byte[] bTmp = medias.get(index).getTitle().getBytes();
				byte[] bPar = new byte[3 + bTmp.length];
				bPar[0] = (byte) iType;
				bPar[1] = (byte) ((index >> 8) & 0xFF);
				bPar[2] = (byte) (index & 0xFF);
				for (int i = 0; i < bTmp.length; i++) {
					bPar[i + 3] = bTmp[i];
				}
				if (ISerial != null) {
					ISerial.sendDataToSerial(room_no, function, bPar, bPar.length);
				}
			} else {
				log.e("...look...");
			}
		} else if (function.equals(Constants.ERROR_FEEDBACK)) {
			int error = Integer.parseInt(parameter);
			if (ISerial != null) {
				ISerial.sendDataToSerial(room_no, function, error);
			}
		} else {
			log.e("invalid function");
			if (ISerial != null) {
				ISerial.sendDataToSerial(room_no, Constants.ERROR_FEEDBACK, Constants.ERROR_FB_INVOILD);
			}
		}
	}

	private void cmd85_callback(String function, int room_no) {
		try {
			String sPar = "";
			MediaItem item = BaseActivity.mPEngineImpl.getCurMediaInfo();
			if (item == null) {
				item = musicControlCenter.getCurMediInfo();
			}

			if (item == null) {
				log.e("item == null");
				return;
			}

			String title = item.getTitle();
			String disname = item.getDisplayName();
			String singer = item.getArtist();
			sPar = title + "%%" + disname + "%%" + singer;
			byte[] bTmp = sPar.getBytes();
			log.d(String.valueOf(sPar));
			// byte[] bPar = new byte[6 + bTmp.length + 1];
			byte[] bPar = new byte[6 + bTmp.length];

			int curIndex = musicControlCenter.getCurPlayInex();
			bPar[0] = (byte) ((curIndex >> 8) & 0xFF);
			bPar[1] = (byte) (curIndex & 0xFF);
			int curPosM = (int) BaseActivity.mPEngineImpl.getCurPosition() / 1000;
			int durM = (int) item.getDuration() / 1000;
			bPar[2] = (byte) ((curPosM / 60) & 0xFF);
			bPar[3] = (byte) ((curPosM % 60) & 0xFF);
			bPar[4] = (byte) ((durM / 60) & 0xFF);
			bPar[5] = (byte) ((durM % 60) & 0xFF);

			for (int i = 0; i < bTmp.length; i++) {
				bPar[i + 6] = bTmp[i];
			}

			if (ISerial != null) {
				ISerial.sendDataToSerial(room_no, function, bPar, bPar.length);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}*/
    }

    /**
     * @param function
     * @param roomNumber
     *//*
	private void sendMusicState(String function, int roomNumber) {
		String title = "";
		try {
			MediaItem item = BaseActivity.mPEngineImpl.getCurMediaInfo();
			if (item != null) {
				title = BaseActivity.mPEngineImpl.getCurMediaInfo().getTitle();
			} else if (musicControlCenter.getCurMediInfo() != null) {
				title = musicControlCenter.getCurMediInfo().getTitle();
			} else {
				log.e("item == null");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.i("title:" + title);

		byte[] bTitle = title.getBytes();

		int pm; // play mode
		int mt; // media type
		int ps; // play state
		int eq;

		pm = musicControlCenter.getPlayMode();
		mt = musicControlCenter.getCurMediaType().ordinal();
		eq = musicControlCenter.getEqType();
		try {
			ps = BaseActivity.mPEngineImpl.getPlayState();
			if (ps == PlayState.MPS_PLAYING || ps == PlayState.MPS_PAUSE || ps == PlayState.MPS_STOP) {
			} else {
				ps = PlayState.MPS_STOP;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ps = PlayState.MPS_STOP;
		}

		byte[] bPar;
		int pos = musicControlCenter.getCurPlayInex();
		switch (function) {
			case Constants.KF_PLAYER_PRE:
			case Constants.KF_PLAYER_NEXT:
				bPar = new byte[5 + bTitle.length];
				bPar[0] = Constants.getCurrentKeyCmd(function);
				bPar[1] = (byte) (((pm << 4) & 0xF0) + (eq & 0x0F));
				bPar[2] = (byte) (((mt << 2) & 0xFC) + (ps & 0x03));
				bPar[3] = (byte) ((pos >> 8) & 0xFF);
				// bPar[4] = (byte) (pos & 0xFF);
				bPar[4] = (byte) (((byte) pos) & 0xFF);
				log.i("pm " + pm + ", ps " + ps + ", mt " + mt + ", pos " + pos + ", eq " + eq);
				for (int i = 0; i < bTitle.length; i++) {
					bPar[4 + 1 + i] = bTitle[i];
				}
				break;
			case Constants.MEDIA_PLAY_STATE:
			default:
				bPar = new byte[4 + bTitle.length];
				bPar[0] = (byte) (((pm << 4) & 0xF0) + (eq & 0x0F));
				bPar[1] = (byte) (((mt << 2) & 0xFC) + (ps & 0x03));
				bPar[2] = (byte) ((pos >> 8) & 0xFF);
				// bPar[3] = (byte) (pos & 0xFF);
				bPar[3] = (byte) (((byte) pos) & 0xFF);
				for (int i = 0; i < bTitle.length; i++) {
					bPar[3 + 1 + i] = bTitle[i];
				}
				log.i("pm " + pm + ", ps " + ps + ", mt " + mt + ", pos " + pos + ", eq " + eq);
				break;
		}

		if (ISerial != null) {
			ISerial.sendDataToSerial(roomNumber, function, bPar, bPar.length);
		}
	}*/

	/*private void sendCurrentAux(String parameter, int roomNumber) {
		if (ISerial != null) {
			byte[] data = new byte[2];
			data[0] = Constants.getCurrentKeyCmd(parameter);
			data[1] = (byte) AudioSourceProxy.getInstance().getAudioSource();
			ISerial.sendDataToSerial(roomNumber, parameter, data, 2);
		}
	}*/

    public static final String INTENT_KEY_CODE = "com.android.musicfx.keycode.value";
    public static final String PARTEMTER = "partemter";

    /**
     * do not allow the operation frequency
     */
    private long upre;
    private long ucurrent;

	/*private void updateMainUi() {
		ucurrent = System.currentTimeMillis();
		if (ucurrent - upre < 1050L) {
			log.i((ucurrent - upre) + ", ucurrent " + ucurrent + ", upre " + upre);
			return;
		}
		upre = ucurrent;

		try {
			Intent intent = new Intent(SmartPlayerActivity.UPDATE_STATE);
			context.sendBroadcast(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/*private void retMuteFromParameter(int roomNumber, String parameter) {
		try {
			Intent intent = new Intent(SmartPlayerActivity.UPDATE_STATE);
			context.sendBroadcast(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (ISerial != null) {
			byte[] data = new byte[2];
			data[0] = Constants.getCurrentKeyCmd(parameter);
			data[1] = (byte) (AudioUtils.isMute(context) ? 0 : 1);
			ISerial.sendDataToSerial(roomNumber, parameter, data, 2);
		}
	}*/

	/*private void retMuteInfomation(int roomNumber, String function) {
		ucurrent = System.currentTimeMillis();
		if (ucurrent - upre < 1050L) {
			log.i((ucurrent - upre) + ", ucurrent " + ucurrent + ", upre " + upre);
			return;
		}
		upre = ucurrent;

		try {
			Intent intent = new Intent(SmartPlayerActivity.UPDATE_STATE);
			context.sendBroadcast(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (ISerial != null) {
			ISerial.sendDataToSerial(roomNumber, function, AudioUtils.isMute(context) ? 0 : 1);
		}
	}*/

	/*private void retActivityPosition(final int roomNumber, final String parameter) {
		if (ISerial != null) {

			new Handler(context.getMainLooper()).postAtTime(new Runnable() {
				@Override
				public void run() {
					byte[] data = new byte[2];
					data[0] = Constants.getCurrentKeyCmd(parameter);
					data[1] = (byte) config_server.CURRENT_ATY;
					ISerial.sendDataToSerial(roomNumber, parameter, data, 2);
				}
			}, 800);
		}
	}*/

	/*private void startActivity(Class<?> cls) {
		try {
			Intent intent = new Intent(context, cls);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("force", true);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/*private void startLocalMediaAty(int iMedia) {
		try {
			Intent intent = new Intent(context, LocalMediaResourceActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra(config_server.SACTIVITY, iMedia);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/*private void startLocalMediaAty(MediaType mediaType) {
		try {
			Intent intent = new Intent(context, LocalMediaResourceActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra(config_server.ATY_LM_ACCARACY, mediaType);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/*private boolean judgeCurrentLocalATY() {
		return Utils.judgeCurrentActivity(LocalMediaResourceActivity.class);
	}

	private boolean judgeCurrentVideoATY() {
		return Utils.judgeCurrentActivity(VideoPlayerActivity.class);
	}

	private boolean judgeCurrentVoiceReadFileATY() {
		return Utils.judgeCurrentActivity(VoiceReadFile.class);
	}

	private int judgeCurrentSleepATY() {
		// return judgeCurrentActivity(SleepActivity.class) ?
		// config_server.IS_SHUT_DOWN : config_server.IS_WAKE_UP;
		return ScreenSaverManager.getInstance().isShutDown() ? config_server.IS_SHUT_DOWN : config_server.IS_WAKE_UP;
	}*/

	/*private void fakeKeyboard(int keyValue) {
		String keyCommand = "input keyevent " + keyValue;
		try {
			Runtime.getRuntime().exec(keyCommand);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

    private Context context;
    private ISerial ISerial;

    public void setISerial(ISerial ISerial) {
        this.ISerial = ISerial;
    }

    public Executor(Context context) {
        this.context = context;
        //musicControlCenter = MusicControlCenter.getInstance();
    }

    public void exit() {
    }

    public interface ISerial {
        public void sendDataToSerial(int roomNumber, String function, String partemterString);

        public void sendDataToSerial(int roomNumber, String function, byte[] buf, int size);

        public void sendDataToSerial(int roomNumber, String function, int partemterInt);
    }
}