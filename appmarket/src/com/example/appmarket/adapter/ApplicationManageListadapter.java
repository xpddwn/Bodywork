package com.example.appmarket.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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

import cn.trinea.android.common.service.impl.ImageCache;
import cn.trinea.android.common.service.impl.ImageCache.OnImageCallbackListener;

import com.example.appmarket.R;
import com.example.appmarket.configs.MyAppMarket;
import com.example.appmarket.entity.AppMarket;
import com.example.appmarket.sqlite.model.ApplicationInfo;
import com.example.appmarket.util.BitmapUtil;
import com.example.appmarket.util.ObjectUtils;
import com.example.appmarket.util.SyncImageLoader;
import com.example.appmarket.util.SyncImageLoader.OnImageLoadListener;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * 
 * 
 * 
 * @author tuomao
 * 
 */
public class ApplicationManageListadapter extends BaseAdapter {
	private static final String TAG = "ApplicationManageListadapter";
	private static ImageCache ICON_CACHE = MyAppMarket.getImageCache();
	private Context mContext;
	private List<AppMarket> applicationInfos;
	private LayoutInflater mInflater;
	private ViewHolder viewHolder;
	private ListView listView;
	private int flag;
	
	static {
		OnImageCallbackListener imageCallBack = new OnImageCallbackListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onImageLoaded(String imageUrl, Drawable imageDrawable,
					View view, boolean isInCache) {
				// TODO Auto-generated method stub
				if (view != null && imageDrawable != null) {
					ImageView imageView = (ImageView) view;
					String imageurltag = (String) imageView.getTag();

					Bitmap mypic = BitmapUtil.drawableToBitmap(imageDrawable);

					Bitmap cornorpic = BitmapUtil.drawRoundBitmap(mypic, 12);
					if (ObjectUtils.isEquals(imageurltag, imageUrl)) {
						// imageView.setImageDrawable(imageDrawable);
						imageView.setImageBitmap(cornorpic);
					}
				}
			}
		};
		ICON_CACHE.setOnImageCallbackListener(imageCallBack);
	}

	public ApplicationManageListadapter(Context context,
			List<AppMarket> applicationInfos, ListView listView, int flag) {
		mContext = context;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		AppMarket applicationInfo = applicationInfos.get(position);
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
		viewHolder.pictureImageView = (ImageView) arg1
				.findViewById(R.id.picture);
		viewHolder.nameTextView = (TextView) arg1.findViewById(R.id.name);
		viewHolder.descriptionTextView = (TextView) arg1
				.findViewById(R.id.description);
		viewHolder.operationButton = (Button) arg1.findViewById(R.id.operation);
	}

	public void setView(int position, AppMarket info) {
		viewHolder.pictureImageView.setTag(position);
		viewHolder.pictureImageView.setTag(info.geticon_url());
		if (ICON_CACHE.get(info.geticon_url(), viewHolder.pictureImageView) == false) {
			viewHolder.pictureImageView.setImageResource(R.drawable.car);
		} else {

		}
		
		viewHolder.nameTextView.setText(info.getapp_name());
		DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		String p=decimalFormat.format(info.getsize());
		
		viewHolder.descriptionTextView.setText(p+"M");
		
		if (flag == 0) {
			viewHolder.operationButton.setText("更新");
		} else if (flag == 1) {
			viewHolder.operationButton.setText("安装");
		} else if (flag == 2) {
			viewHolder.operationButton.setText("卸载");
		}
		viewHolder.operationButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if (flag == 0) {//

				} else if (flag == 1) {// 

				} else if (flag == 2) {// 
					
				}
			}
		});
	}

	class ViewHolder {
		ImageView pictureImageView;
		TextView nameTextView;
		TextView descriptionTextView;
		Button operationButton;
	}

	public void refreshData(List<AppMarket> infos) {
		// TODO Auto-generated method stub
		
	}
}
