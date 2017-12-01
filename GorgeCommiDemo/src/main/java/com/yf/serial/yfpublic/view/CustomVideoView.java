package com.yf.serial.yfpublic.view;


import android.content.Context;
import android.widget.VideoView;

public class CustomVideoView extends VideoView {
	public static final String TAG = CustomVideoView.class.getSimpleName();
	Context mContext;

	public CustomVideoView(Context context) {
		super(context);
		this.mContext = context;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

//	public void setVideoLayout(int layout, float aspectRatio) {
//		LayoutParams lp = getLayoutParams();
//		Pair<Integer, Integer> res = ScreenResolution.getResolution(mContext);
//		int windowWidth = res.first.intValue(), windowHeight = res.second.intValue();
//		float windowRatio = windowWidth / (float) windowHeight;
//		float videoRatio = aspectRatio <= 0.01f ? mVideoAspectRatio : aspectRatio;
//		mSurfaceHeight = mVideoHeight;
//		mSurfaceWidth = mVideoWidth;
//		if (VIDEO_LAYOUT_ORIGIN == layout && mSurfaceWidth < windowWidth && mSurfaceHeight < windowHeight) {
//			lp.width = (int) (mSurfaceHeight * videoRatio);
//			lp.height = mSurfaceHeight;
//		} else if (layout == VIDEO_LAYOUT_ZOOM) {
//			lp.width = windowRatio > videoRatio ? windowWidth : (int) (videoRatio * windowHeight);
//			lp.height = windowRatio < videoRatio ? windowHeight : (int) (windowWidth / videoRatio);
//		} else {
//			boolean full = layout == VIDEO_LAYOUT_STRETCH;
//			lp.width = (full || windowRatio < videoRatio) ? windowWidth : (int) (videoRatio * windowHeight);
//			lp.height = (full || windowRatio > videoRatio) ? windowHeight : (int) (windowWidth / videoRatio);
//		}
//		android.util.Log
//		setLayoutParams(lp);
//		getHolder().setFixedSize(mSurfaceWidth, mSurfaceHeight);
//		Log.d("VIDEO: %dx%dx%f, Surface: %dx%d, LP: %dx%d, Window: %dx%dx%f", mVideoWidth, mVideoHeight,
//				mVideoAspectRatio, mSurfaceWidth, mSurfaceHeight, lp.width, lp.height, windowWidth, windowHeight,
//				windowRatio);
//		mVideoLayout = layout;
//		mAspectRatio = aspectRatio;
//	}

}
