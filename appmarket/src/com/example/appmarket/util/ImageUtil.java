package com.example.appmarket.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ImageUtil {

	private static String TAG = "IMAGE_UTIL";

	// �Ŵ���СͼƬ
	/**
	 * 
	 * 
	 * @param bitmap
	 * @param w
	 *            ���ź��ͼƬ�Ŀ��
	 * @param h
	 *            ���ź�ͼƬ�ĸ߶�
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w / width);
		float scaleHeight = ((float) h / height);
		// �����������Ƿ����̵ı���
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newbmp;
	}

	// ��Drawableת��ΪBitmap
	/**
	 * ��Drawableת��ΪBitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		// bitmapconfig ������λͼ�Ĵ洢��ʽ������
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;

	}

	// ���Բ��ͼƬ�ķ���
	/**
	 * 
	 * 
	 * @param bitmap
	 * @param roundPx
	 *            Բ�ǰ뾶�ĵĴ�С
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	// ��ô�Ӱ��ͼƬ����
	/***
	 * ��ô�Ӱ��ͼƬ����
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
				width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);
		return bitmapWithReflection;
	}

	// ѹ��ͼƬ��С
	/**
	 * 
	 * image.compress(Bitmap.CompressFormat.JPEG, 100, baos); //
	 * ����ѹ������������100��ʾ��ѹ������ѹ�������ݴ�ŵ�baos��
	 * 
	 * @param image
	 * @param type
	 * @param quality
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image, CompressFormat format,
			int quality) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(format, quality, baos);
		// image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//
		// ����ѹ������������100��ʾ��ѹ������ѹ�������ݴ�ŵ�baos��
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��
			baos.reset();// ����baos�����baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// ����ѹ��options%����ѹ�������ݴ�ŵ�baos��
			options -= 10;// ÿ�ζ�����10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// ��ѹ��������baos��ŵ�ByteArrayInputStream��
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ��ByteArrayInputStream������ͼƬ
		return bitmap;
	}

	/**
	 * ����ɫͼת��Ϊ�Ҷ�ͼ
	 * 
	 * @param img
	 *            λͼ
	 * @return ����ת���õ�λͼ
	 */
	public static Bitmap convertGreyImg(Bitmap img) {

		int width = img.getWidth(); // ��ȡλͼ�Ŀ�
		int height = img.getHeight(); // ��ȡλͼ�ĸ�

		int[] pixels = new int[width * height]; // ͨ��λͼ�Ĵ�С�������ص�����

		// ��������� getPixels (int[] pixels, int offset, int stride, int x, int
		// y,
		// int width, int height)
		/**
		 * 
		 * 1.piexls ��ȡ�����ص���Ϣ���õ����� 2.offect ��ʼ���������صĵ�һ��Ϊֹ
		 * eg��offect=0������ piexls[0] 3.stride ÿ���ڽ�����һ�е�����ʱ��Ӧ�üӵĳ���
		 * stride>=width eg�� stride=10 ��
		 * ��һ��Ϊpiexls[0,9],�ڶ���Ϊpiexls[10,19];
		 */
		img.getPixels(pixels, 0, width, 0, 0, width, height);
		// int alpha = 0xFF << 24;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int grey = pixels[width * i + j];
				int red = ((grey & 0x00FF0000) >> 16);
				int green = ((grey & 0x0000FF00) >> 8);
				int blue = (grey & 0x000000FF);

				// ����Ҷȣ����������㣬���ٸ���������㣬�Ҷ�Ҳ��Ϊ�ҽ֣�ֱ��ͼ�ô���ͳ��
				grey = (int) ((red * 30 + green * 59 + blue * 11) / 100);
				// Color��reb(grey,grey,grey)��ʾ�Ҷ�ͼ�� ���������͸����
				grey = Color.rgb(grey, grey, grey);
				pixels[width * i + j] = grey;
			}
		}
		/*
		 * int threshhold=GetOSTUThreshold(); Log.e(tag, threshhold+"");
		 * 
		 * for(int i=0;i<width*height;i++){ Log.e("piex", pixels[i]+"");
		 * if(pixels[i]>=threshhold){ pixels[i]=Color.rgb(255, 255, 255); }else{
		 * pixels[i]=Color.rgb(0, 0, 0); } } for(int i=0;i<255;i++){
		 * Log.e("his", HistGram[i]+""); }
		 */
		Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		result.setPixels(pixels, 0, width, 0, 0, width, height);
		return result;
	}

	// ��ȡͼ��Ļҽ���Ϣ
	public static void getGreyInfos(Bitmap img, int[] greyInfos) {
		int width = img.getWidth(); // ��ȡλͼ�Ŀ�
		int height = img.getHeight(); // ��ȡλͼ�ĸ�

		int[] pixels = new int[width * height]; // ͨ��λͼ�Ĵ�С�������ص�����
		// ��������� getPixels (int[] pixels, int offset, int stride, int x, int
		// y,
		// int width, int height)
		/**
		 * 
		 * 1.piexls ��ȡ�����ص���Ϣ���õ����� 2.offect ��ʼ���������صĵ�һ��Ϊֹ
		 * eg��offect=0������ piexls[0] 3.stride ÿ���ڽ�����һ�е�����ʱ��Ӧ�üӵĳ���
		 * stride>=width eg�� stride=10 ��
		 * ��һ��Ϊpiexls[0,9],�ڶ���Ϊpiexls[10,19];
		 */
		img.getPixels(pixels, 0, width, 0, 0, width, height);
		// int alpha = 0xFF << 24;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int grey = pixels[width * i + j];

				int red = ((grey & 0x00FF0000) >> 16);
				int green = ((grey & 0x0000FF00) >> 8);
				int blue = (grey & 0x000000FF);

				// ����Ҷȣ����������㣬���ٸ���������㣬�Ҷ�Ҳ��Ϊ�ҽ֣�ֱ��ͼ�ô���ͳ��
				grey = (int) ((red * 30 + green * 59 + blue * 11) / 100);
				// Color��reb(grey,grey,grey)��ʾ�Ҷ�ͼ�� ���������͸����

				greyInfos[width * i + j] = grey;
			}
		}
	}

	// ͼƬUrl����Ϊλͼ���������Ų���
	// ͨ����ͼƬurl��ȡλͼ����
	public static Bitmap getBitmap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
			Log.i(TAG, "image download finished." + url);
			return bitmap;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			// TODO: handle exception
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * ���ر���ͼƬ
	 * 
	 * @param context
	 *            �������к���ʵ��
	 * @param bitAdress
	 *            ��ͼƬ��ַ��һ��ָ��R�µ�drawableĿ¼
	 * @return
	 */
	public static Bitmap CreatImage(Context context, int bitAdress) {
		Bitmap bitmaptemp = null;
		bitmaptemp = BitmapFactory.decodeResource(context.getResources(),
				bitAdress);
		return bitmaptemp;
	}

	/***
	 * 2.ͼƬƽ��ָ��������ͼƽ��ָ�ΪN��N�У������û�ʹ�� ͼƬ�ָ�
	 * 
	 * @param g
	 *            ������
	 * @param paint
	 *            ������
	 * @param imgBit
	 *            ��ͼƬ
	 * @param x
	 *            ��X��������
	 * @param y
	 *            ��Y��������
	 * @param w
	 *            ����һͼƬ�Ŀ��
	 * @param h
	 *            ����һͼƬ�ĸ߶�
	 * @param line
	 *            ���ڼ���
	 * @param row
	 *            ���ڼ���
	 */
	public static void cuteImage(Canvas g, Paint paint, Bitmap imgBit, int x,
			int y, int w, int h, int line, int row) {
		g.clipRect(x, y, x + w, h + y);
		g.drawBitmap(imgBit, x - line * w, y - row * h, paint);
		g.restore();
	}

	/***
	 * 4.���ƴ��б߿�����֣�һ������Ϸ�������ֵ��������� ���ƴ��б߿������
	 * 
	 * @param strMsg
	 *            ����������
	 * @param g
	 *            ������
	 * @param paint
	 *            ������
	 * @param setx
	 *            ����X����ʼ���
	 * @param sety
	 *            ��Y�����ʼ���
	 * @param fg
	 *            ��ǰ��ɫ
	 * @param bg
	 *            ������ɫ
	 */
	public static void drawText(String strMsg, Canvas g, Paint paint, int setx,
			int sety, int fg, int bg) {
		paint.setColor(bg);
		g.drawText(strMsg, setx + 1, sety, paint);
		g.drawText(strMsg, setx, sety - 1, paint);
		g.drawText(strMsg, setx, sety + 1, paint);
		g.drawText(strMsg, setx - 1, sety, paint);
		paint.setColor(fg);
		g.drawText(strMsg, setx, sety, paint);
		g.restore();
	}

	/**
	 * 
	 * ͨ����bitmap����ָ����bitmap���м���
	 * 
	 * @param bitmap
	 *            Դbitmap
	 * @param x
	 *            ���е���ʼλ�õ����
	 * @param y
	 * @param width
	 *            ���еĳ��ȺͿ��
	 * @param height
	 * @return
	 */
	public static Bitmap cuteBitmap(Bitmap bitmap, int x, int y, int width,
			int height) {
		Bitmap resultBitmap = Bitmap.createBitmap(bitmap, x, y, width, height);
		return resultBitmap;
	}

	/**
	 * 
	 * 
	 * @param saveDir
	 *            �����Ŀ¼
	 * @param saveName
	 *            ������ļ���
	 * @param bitmap
	 *            λͼ
	 * @return
	 */

	public static boolean saveBitmap(String saveDir, String saveName,
			Bitmap bitmap) {
		boolean nowbol = false;
		try {
			// ���Ŀ¼�ļ�
			File dir = new File(saveDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			// ���Ŀ���ļ�
			File saveFile = new File(saveDir, saveName);
			if (!saveFile.exists()) {
				saveFile.createNewFile();
			}

			FileOutputStream saveFileOutputStream;
			saveFileOutputStream = new FileOutputStream(saveFile);
			nowbol = bitmap.compress(Bitmap.CompressFormat.PNG, 100,
					saveFileOutputStream);
			saveFileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowbol;
	}

	// ��ͼƬ��Դת��Ϊbitmap
	public static Bitmap resourceTobitmap(int resource, Context context) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				resource);
		return bitmap;
	}
}
