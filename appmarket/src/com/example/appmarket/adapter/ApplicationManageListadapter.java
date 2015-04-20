package com.example.appmarket.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.appmarket.R;
import com.example.appmarket.sqlite.model.ApplicationInfo;
import com.example.appmarket.util.SyncImageLoader;
import com.example.appmarket.util.SyncImageLoader.OnImageLoadListener;
import com.example.appmarket.util.ThreadManageUtil;
import com.example.appmarket.util.ThreadObject;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * 
 * Ӧ�ù����������
 * 
 * @author tuomao
 * 
 */
public class ApplicationManageListadapter extends BaseAdapter {
	private static final String TAG = "ApplicationManageListadapter";
	private Context mContext;
	private ArrayList<ApplicationInfo> applicationInfos;
	private LayoutInflater mInflater;
	private ViewHolder viewHolder;
	private SyncImageLoader imageLoader;
	private ListView listView;
	private int flag;

	public ApplicationManageListadapter(Context context,
			ArrayList<ApplicationInfo> applicationInfos, ListView listView,
			SyncImageLoader imageLoader, int flag) {
		mContext = context;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageLoader = imageLoader;
		this.listView = listView;
		this.applicationInfos = applicationInfos;
		this.flag = flag;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return applicationInfos.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public View getView(int position, View convertview, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ApplicationInfo applicationInfo = applicationInfos.get(position);
		if (convertview == null) {
			convertview = mInflater.inflate(R.layout.manage__list_item, null);
			findView(convertview);
			convertview.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertview.getTag();
		}
		setView(position, applicationInfo);
		return convertview;
	}

	public void findView(View arg1) {
		viewHolder = new ViewHolder();
		viewHolder.pictureImageView = (RoundedImageView) arg1
				.findViewById(R.id.picture);
		viewHolder.nameTextView = (TextView) arg1.findViewById(R.id.name);
		viewHolder.descriptionTextView = (TextView) arg1
				.findViewById(R.id.description);
		viewHolder.operationButton = (Button) arg1.findViewById(R.id.operation);
	}

	public void setView(int position, ApplicationInfo info) {
		// �첽����ͼƬ ����û��ͼƬ ������ΪĬ�ϵ�ͼƬ
		try {
			Bitmap bitmap = imageLoader.getBitmapFromLocal(info.icon_url);
			if (bitmap == null) {
				viewHolder.pictureImageView.setImageResource(R.drawable.car);
				imageLoader.showImageAsyn(position, info.icon_url,
						imageLoadListener);
			} else {
				viewHolder.pictureImageView.setImageBitmap(bitmap);
			}
		} catch (Exception e) {
			Log.e(TAG, "��ȡͼƬ����");
			// TODO: handle exception
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			Log.e(TAG, "�ڴ����");
		}
		viewHolder.pictureImageView.setTag(position);
		viewHolder.nameTextView.setText(info.app_name);
		viewHolder.descriptionTextView.setText(info.description);
		if (flag == 0) {
			viewHolder.operationButton.setText("����");
		} else if (flag == 1) {
			viewHolder.operationButton.setText("ж��");
		} else if (flag == 2) {
			viewHolder.operationButton.setText("��װ");
		}
		viewHolder.operationButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ThreadManageUtil.sendRequest(new ThreadObject() {

					@Override
					public Object handleOperation() {
						// TODO Auto-generated method stub
						return null;
					}
				});
				if (flag == 0) {// ����

				} else if (flag == 1) {// ж��

				} else if (flag == 2) {// ��װ

				}
			}
		});
	}

	class ViewHolder {
		RoundedImageView pictureImageView;
		TextView nameTextView;
		TextView descriptionTextView;
		Button operationButton;
	}

	private OnImageLoadListener imageLoadListener = new OnImageLoadListener() {

		@Override
		public void onImageLoad(int indentify, Bitmap bitmap) {
			// TODO Auto-generated method stub
			ImageView view = (ImageView) listView.findViewWithTag(indentify);
			if (view != null) {
				view.setImageBitmap(bitmap);
			} else {
				Log.e(TAG, "not find imageview by tag");
			}
		}

		@Override
		public void onError(int identify) {
			// TODO Auto-generated method stub
			ImageView view = (ImageView) listView.findViewWithTag(identify);
			if (view != null) {
				// Log.e(TAG, "cat not load image");
			} else {
				// Log.e(TAG, "not find imageview by tag");
			}
		}
	};
}
