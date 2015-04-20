package com.example.appmarket.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.example.appmarket.constant.Constant;

/**
 * 
 * �첽����ͼƬ ���Կ�����ЩͼƬ���� ��ЩͼƬ������
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
	private boolean getFromLocal = false;

	private static HashMap<String, SoftReference<Bitmap>> caches = new HashMap<String, SoftReference<Bitmap>>();

	public interface OnImageLoadListener {
		/**
		 * 
		 * @param indentify
		 *            imageview ͨ��settag������Ψһ��indentify
		 *            �����÷��ص�ʱ��ͨ���identify�ҵ���view
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
	 * Ҫ��ͼƬ���ش洢����ƺ������ϴ洢�������һ�µ�
	 * 
	 * �ȳ��Դӱ��ؼ������
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
				// ȡ��������
				SoftReference<Bitmap> rf = caches.get(imageUrl);
				// ͨ�������ã���ȡͼƬ
				bitmap = rf.get();
				if (bitmap != null) {// ����֮�д��ڸ�bitmapze
					return bitmap;
				}
			}
			if (getFromLocal) {
				String name = imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
						imageUrl.length());
				// �ӱ��ؼ���ͼƬ
				String pathString = Constant.SDCARD_IMAGE_PATH + name;
				bitmap = BitmapFactory.decodeFile(pathString);
				if (bitmap != null) {// ���ز����ڸ�ͼƬ
					caches.put(imageUrl, new SoftReference<Bitmap>(bitmap));
					return bitmap;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} catch (OutOfMemoryError e) {
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
						if (bitmap == null) {// ͼƬδ�ܼ��سɹ�
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
