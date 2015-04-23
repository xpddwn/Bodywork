package com.example.appmarket.adapter;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appmarket.R;
import com.example.appmarket.configs.MyAppMarket;
import com.example.appmarket.entity.AppMarket;
import com.example.appmarket.util.BitmapUtil;
import com.example.appmarket.util.HttpUtil;
import com.example.appmarket.util.JsonUtil;
import com.example.appmarket.util.ObjectUtils;
import com.example.appmarket.view.XListView;
import com.google.gson.JsonObject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
//import com.zhan_dui.download.DownloadManager;
//import com.zhan_dui.download.DownloadMission;

public class ApplicationClassifyGridadapter extends BaseAdapter {
	private static ImageCache ICON_CACHE = MyAppMarket.getImageCache();
	private final String TAG = "ApplicationClassifyGridadapter";
	private Context mContext;
	private List<AppMarket> applicationInfo;
	private LayoutInflater mInflater;
	private ViewHolder viewHolder;
	private XListView gridview;
	private int mChildCount;
	
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

					Bitmap cornorpic = BitmapUtil.drawRoundBitmap(mypic, 5);
					if (ObjectUtils.isEquals(imageurltag, imageUrl)) {
						// imageView.setImageDrawable(imageDrawable);
						imageView.setImageBitmap(cornorpic);
					}
				}
			}
		};
		ICON_CACHE.setOnImageCallbackListener(imageCallBack);
	}

	public ApplicationClassifyGridadapter(Context context,
			List<AppMarket> applicationInfos, XListView gridviews) {
		mContext = context;
		this.applicationInfo = applicationInfos;
		this.gridview = gridviews;
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
		AppMarket application = applicationInfo.get(position);
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

	public void setview(int position, final AppMarket info) {
		viewHolder.pictureImageView.setTag(info.geticon_url());
		if (ICON_CACHE.get(info.geticon_url(), viewHolder.pictureImageView) == false) {
			viewHolder.pictureImageView.setImageResource(R.drawable.car);
		} else {

		}
		viewHolder.nameTextView.setText(info.getapp_name());
		viewHolder.descriptionTextView.setText(String.valueOf(info.getsize())+"M");
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
						params.put("app_id", String.valueOf(info.getapp_id()));
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
//										DownloadMission downloadMission=new DownloadMission(url, Constant.SDCARD_APK_PATH, info.app_name+".apk");
//										DownloadManager.getInstance().addMission(downloadMission);
//										if(downloadMission.isFinished()){
//											new Thread(new Runnable() {
//												@Override
//												public void run() {
//													// TODO Auto-generated method stub
//													info.apk_path=Constant.SDCARD_APK_PATH+info.app_name+".apk";
//													int result=PackageUtils.installSilent(mContext, info.apk_path,"-r");
//													if (result==PackageUtils.INSTALL_SUCCEEDED) {
//														AppInfoService service=new AppInfoService(mContext);
//														service.insertApplicationInfo(info);
//														System.out.println("install scuccess");
//													}else{
//														System.out.println("install fail");
//													}
//												}
//											});
//										}
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
		viewHolder.pictureImageView = (ImageView) convertView
				.findViewById(R.id.picture);
		viewHolder.nameTextView = (TextView) convertView
				.findViewById(R.id.name);
		viewHolder.descriptionTextView = (TextView) convertView
				.findViewById(R.id.description);
		viewHolder.operationButton = (Button) convertView
				.findViewById(R.id.operation);
	}

	class ViewHolder {
		ImageView pictureImageView;
		TextView nameTextView;
		TextView descriptionTextView;
		Button operationButton;
	}
	
	public void refreshData(List<AppMarket> listItems) {
		this.applicationInfo = listItems;
		// YoukongService.setStatus();
		notifyDataSetChanged();

	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
		gridview.stopLoadMore();
		mChildCount = getCount();
	}
}
