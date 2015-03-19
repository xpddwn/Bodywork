package com.example.appmarket.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.example.appmarket.constant.Constant;

/**
 * 
 * 异步加载图片 可以控制那些图片加载 那些图片不加载
 * 
 * @author tuomao
 * 
 */
public class SyncImageLoader {

	private Object lock = new Object();

	private boolean mAllowLoad = true;

	private boolean firstLoad = true;

	private int mStartLoadLimit = 0;

	private int mStopLoadLimit = 0;

	final Handler handler = new Handler();
	private boolean getFromLocal=false;

	private static HashMap<String, SoftReference<Bitmap>> caches = new HashMap<String, SoftReference<Bitmap>>();

	public interface OnImageLoadListener {
		/**
		 * 
		 * @param indentify
		 *            imageview 通过settag设置其唯一的indentify
		 *            当调用返回的时候，通过该identify找到该view
		 * @param bitmap
		 * 
		 */
		public void onImageLoad(int indentify, Bitmap bitmap);

		public void onError(int identify);
	}

	public void setLoadLimit(int startLoadLimit, int stopLoadLimit) {
		if (startLoadLimit > stopLoadLimit) {
			return;
		}
		mStartLoadLimit = startLoadLimit;
		mStopLoadLimit = stopLoadLimit;
	}

	public void restore() {
		mAllowLoad = true;
		firstLoad = true;
	}

	public void lock() {
		mAllowLoad = false;
		firstLoad = false;
	}

	public void unlock() {
		mAllowLoad = true;
		synchronized (lock) {
			lock.notifyAll();
		}
	}

	/**
	 * 
	 * 
	 * 要求图片本地存储的名称和网络上存储的名称是一致的
	 * 
	 * 先尝试从本地加载数据
	 * 
	 * @param imageUrl
	 * @return
	 */
	public Bitmap getBitmapFromLocal(String imageUrl) {
		try {
			if (imageUrl == null) {
				return null;
			}
			Bitmap bitmap = null;
			if (caches.containsKey(imageUrl)) {
				// 取出软引用
				SoftReference<Bitmap> rf = caches.get(imageUrl);
				// 通过软引用，获取图片
				bitmap = rf.get();
				if (bitmap != null) {// 缓存之中存在该bitmapze
					return bitmap;
				}
			}
			if(getFromLocal){
				String name = imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
						imageUrl.length());
				// 从本地加载图片
				String pathString = Constant.SDCARD_IMAGE_PATH + name;
				bitmap = BitmapFactory.decodeFile(pathString);
				if (bitmap != null) {// 本地不存在该图片
					caches.put(imageUrl, new SoftReference<Bitmap>(bitmap));
					return bitmap;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	public void showImageAsyn(int position, String imageUrl,
			OnImageLoadListener listener) {
		try {
			final OnImageLoadListener mListener = listener;
			final String mImageUrl = imageUrl;
			final int mPosition = position;

			new Thread(new Runnable() {

				Bitmap bitmap;

				@Override
				public void run() {
					if (!mAllowLoad) {
						synchronized (lock) {
							try {
								lock.wait();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					if ((mAllowLoad && firstLoad)
							|| (mAllowLoad && mPosition <= mStopLoadLimit && mPosition >= mStartLoadLimit)) {
						bitmap = loadImage(mImageUrl, mPosition, mListener);
						if (bitmap == null) {// 图片未能加载成功
							handler.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									mListener.onError(mPosition);
								}
							});

						} else {
							handler.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									mListener.onImageLoad(mPosition, bitmap);
								}
							});
						}
					}
				}

			}).start();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private Bitmap loadImage(final String path, final int indentify,
			final OnImageLoadListener mListener) {
		Bitmap bitmap = ImageUtil.getBitmap(path);
		if (bitmap != null) {
			caches.put(path, new SoftReference<Bitmap>(bitmap));
			String fileName = path.substring(path.lastIndexOf("/") + 1,
					path.length());
			ImageUtil.saveBitmap(Constant.SDCARD_IMAGE_PATH, fileName, bitmap);
		}
		return bitmap;
	}

}
