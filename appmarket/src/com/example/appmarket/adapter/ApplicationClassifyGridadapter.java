package com.example.appmarket.adapter;

import java.util.ArrayList;

import org.json.JSONObject;

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
import com.example.appmarket.configs.MyAppMarket;
import com.example.appmarket.constant.Constant;
import com.example.appmarket.sqlite.model.ApplicationInfo;
import com.example.appmarket.sqlite.service.AppInfoService;
import com.example.appmarket.util.HttpUtil;
import com.example.appmarket.util.JsonUtil;
import com.example.appmarket.util.PackageUtils;
import com.example.appmarket.util.SyncImageLoader;
import com.example.appmarket.util.SyncImageLoader.OnImageLoadListener;
import com.google.gson.JsonObject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhan_dui.download.DownloadManager;
import com.zhan_dui.download.DownloadMission;

public class ApplicationClassifyGridadapter extends BaseAdapter {
	private final String TAG = "ApplicationClassifyGridadapter";
	private Context mContext;
	private ArrayList<ApplicationInfo> applicationInfo;
	private LayoutInflater mInflater;
	private GridView gridview;
	private ViewHolder viewHolder;
	private SyncImageLoader imageLoader;

	public ApplicationClassifyGridadapter(Context context,
			ArrayList<ApplicationInfo> applicationInfos, GridView gridviews,
			SyncImageLoader imageLoaders) {
		mContext = context;
		this.applicationInfo = applicationInfos;
		this.gridview = gridviews;
		this.imageLoader = imageLoaders;
		mInflater = (LayoutInflater) mContext
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

	public void setview(int position, final ApplicationInfo info) {
		
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
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						RequestParams params = new RequestParams();
						String username = MyAppMarket.getUsername();
						String password = MyAppMarket.getpapapa();
						params.put("username", username);
						params.put("password", password);
						params.put("app_id", String.valueOf(info.getApp_id()));
						try {
							HttpUtil.post("download_app", params, new JsonHttpResponseHandler() {
								@Override
								protected void handleFailureMessage(Throwable arg0, String arg1) {
									super.handleFailureMessage(arg0, arg1);
									
							
								};

								@Override
								public void onFailure(Throwable arg0, JSONObject arg1) {
									// TODO Auto-generated method stub
									super.onFailure(arg0, arg1);
									
								}

								@Override
								public void onSuccess(JSONObject jsonobject) {
									// TODO Auto-generated method stub
									super.onSuccess(jsonobject);
									try {
										JsonObject object=JsonUtil.stringToJsonObject(jsonobject.toString());
										int statuscode = object.get("status").getAsInt();
										String url=object.get("app_url").getAsString();
										DownloadMission downloadMission=new DownloadMission(url, Constant.SDCARD_APK_PATH, info.app_name+".apk");
										DownloadManager.getInstance().addMission(downloadMission);
										if(downloadMission.isFinished()){
											new Thread(new Runnable() {
												@Override
												public void run() {
													// TODO Auto-generated method stub
													info.apk_path=Constant.SDCARD_APK_PATH+info.app_name+".apk";
													int result=PackageUtils.installSilent(mContext, info.apk_path,"-r");
													if (result==PackageUtils.INSTALL_SUCCEEDED) {
														AppInfoService service=new AppInfoService(mContext);
														service.insertApplicationInfo(info);
														System.out.println("install scuccess");
													}else{
														System.out.println("install fail");
													}
												}
											});
										}
									} catch (Exception e) {
										// TODO: handle exception
									}
								}
							});
						} catch (Exception e) {
							// TODO: handle exception
						}
						
					}
				}).start();
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
