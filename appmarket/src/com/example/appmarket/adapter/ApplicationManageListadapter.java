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
import com.example.appmarket.entity.ApplicationInfo;
import com.example.appmarket.util.SyncImageLoader;
import com.example.appmarket.util.SyncImageLoader.OnImageLoadListener;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * 
 * 应用管理的适配器
 * @author tuomao
 *
 */
public class ApplicationManageListadapter extends BaseAdapter{
	private static final String TAG="ApplicationManageListadapter";
	private Context mContext;
	private ArrayList<ApplicationInfo> applicationInfos;
	private LayoutInflater mInflater;
	private ViewHolder viewHolder;
	private SyncImageLoader imageLoader;
	private ListView listView;
	private int flag;

	public ApplicationManageListadapter(Context context, ArrayList<ApplicationInfo> applicationInfos,ListView listView
			,SyncImageLoader imageLoader,int flag) {
		mContext = context;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageLoader=imageLoader;
		this.listView=listView;
		this.applicationInfos=applicationInfos;
		this.flag=flag;
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
		ApplicationInfo applicationInfo=applicationInfos.get(position);
		if (convertview == null) {
			convertview = mInflater.inflate(R.layout.manage__list_item, null);
			findView(convertview);
			convertview.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertview.getTag();  
		}
		setView(position,applicationInfo);
		return convertview;
	}
	public void findView(View arg1){
		viewHolder = new ViewHolder();
		viewHolder.pictureImageView=(RoundedImageView)arg1.findViewById(R.id.picture);
		viewHolder.nameTextView=(TextView)arg1.findViewById(R.id.name);
		viewHolder.descriptionTextView=(TextView)arg1.findViewById(R.id.description);
		viewHolder.operationButton=(Button)arg1.findViewById(R.id.operation);
	}
	
	public void setView(int position,ApplicationInfo info){
		// 异步加载图片 如果本地没有图片 则设置为默认的图片
		try {
			Bitmap bitmap=imageLoader.getBitmapFromLocal(info.picturePath);
			if(bitmap==null){
				viewHolder.pictureImageView.setImageResource(R.drawable.car);
				imageLoader.showImageAsyn(position, info.picturePath,imageLoadListener);
			}else{
				viewHolder.pictureImageView.setImageBitmap(bitmap);
			}
		} catch (Exception e) {
			Log.e(TAG, "获取图片错误");
			// TODO: handle exception
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			Log.e(TAG, "内存溢出");
		}
		viewHolder.pictureImageView.setTag(position);		
		viewHolder.nameTextView.setText(info.name);
		viewHolder.descriptionTextView.setText(info.description);
		if(flag==0){
			viewHolder.operationButton.setText("更新");
		}else if(flag==1) {
			viewHolder.operationButton.setText("卸载");
		}else if(flag==2){
			viewHolder.operationButton.setText("安装");
		}
		viewHolder.operationButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	class ViewHolder {
		RoundedImageView pictureImageView ;
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

