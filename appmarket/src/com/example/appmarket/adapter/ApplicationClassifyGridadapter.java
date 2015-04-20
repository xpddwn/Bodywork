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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appmarket.R;
import com.example.appmarket.entity.AppMarket;
import com.example.appmarket.sqlite.model.ApplicationInfo;
import com.example.appmarket.util.SyncImageLoader;
import com.example.appmarket.util.SyncImageLoader.OnImageLoadListener;
import com.makeramen.roundedimageview.RoundedImageView;

public class ApplicationClassifyGridadapter extends BaseAdapter {
	private final String TAG = "ApplicationClassifyGridadapter";
	private Context mcontext;
	private ArrayList<ApplicationInfo> applicationInfo;
	private LayoutInflater mInflater;
	private GridView gridview;
	private ViewHolder viewHolder;
	private SyncImageLoader imageLoader;

	public ApplicationClassifyGridadapter(Context context,
			ArrayList<ApplicationInfo> applicationInfos, GridView gridviews,
			SyncImageLoader imageLoaders) {
		mcontext = context;
		this.applicationInfo = applicationInfos;
		this.gridview = gridviews;
		this.imageLoader = imageLoaders;
		mInflater = (LayoutInflater) mcontext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return applicationInfo.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return applicationInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ApplicationInfo application = applicationInfo.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.app_grid_items, null);
			findView(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		setview(position, application);
		return convertView;
	}

	public void setview(int position, ApplicationInfo info) {
		
		try {
			Bitmap bitmap = imageLoader.getBitmapFromLocal(info.getIcon_url());
			if (bitmap == null) {
				viewHolder.pictureImageView.setImageResource(R.drawable.car);
				imageLoader.showImageAsyn(position, info.getIcon_url(),
						imageLoadListener);
			} else {
				viewHolder.pictureImageView.setImageBitmap(bitmap);
			}
		} catch (Exception e) {
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
		}
		viewHolder.pictureImageView.setTag(position);
		viewHolder.nameTextView.setText(info.getApp_name());
		viewHolder.descriptionTextView.setText(info.getDescription());
		viewHolder.operationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void findView(View convertView) {
		// TODO Auto-generated method stub
		viewHolder = new ViewHolder();
		viewHolder.pictureImageView = (RoundedImageView) convertView
				.findViewById(R.id.picture);
		viewHolder.nameTextView = (TextView) convertView
				.findViewById(R.id.name);
		viewHolder.descriptionTextView = (TextView) convertView
				.findViewById(R.id.description);
		viewHolder.operationButton = (Button) convertView
				.findViewById(R.id.operation);
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
			ImageView view = (ImageView) gridview.findViewWithTag(indentify);
			if (view != null) {
				view.setImageBitmap(bitmap);
			} else {
				Log.e(TAG, "not find imageview by tag");
			}
		}

		@Override
		public void onError(int identify) {
			// TODO Auto-generated method stub
			ImageView view = (ImageView) gridview.findViewWithTag(identify);
			if (view != null) {
				// Log.e(TAG, "cat not load image");
			} else {
				// Log.e(TAG, "not find imageview by tag");
			}
		}
	};

}
