package com.example.appmarket.adapter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.trinea.android.common.service.impl.ImageCache;
import cn.trinea.android.common.service.impl.ImageCache.OnImageCallbackListener;

import com.example.appmarket.R;
import com.example.appmarket.configs.MyAppMarket;
import com.example.appmarket.constant.Constant;
import com.example.appmarket.entity.AppMarket;
import com.example.appmarket.sqlite.model.ApplicationInfo;
import com.example.appmarket.sqlite.service.AppInfoService;
import com.example.appmarket.util.BitmapUtil;
import com.example.appmarket.util.HttpUtil;
import com.example.appmarket.util.JsonUtil;
import com.example.appmarket.util.ObjectUtils;
import com.example.appmarket.util.PackageUtils;
import com.example.appmarket.view.XListView;
import com.google.gson.JsonObject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ApplicationClassifyGridadapter extends BaseAdapter {
	private static ImageCache ICON_CACHE = MyAppMarket.getImageCache();
	private final String TAG = "ApplicationClassifyGridadapter";
	private Context mContext;
	private List<AppMarket> applicationInfo;
	private LayoutInflater mInflater;
	private ViewHolder viewHolder;
	private XListView gridview;
	private int mChildCount;
	private ProgressDialog mProgressDialog;
	
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

	public void setview(final int position, final AppMarket info) {
		viewHolder.pictureImageView.setTag(info.geticon_url());
		if (ICON_CACHE.get(info.geticon_url(), viewHolder.pictureImageView) == false) {
			viewHolder.pictureImageView.setImageResource(R.drawable.car);
		} else {

		}
		viewHolder.nameTextView.setText(info.getapp_name());
		DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		String p=decimalFormat.format(info.getsize());
		viewHolder.descriptionTextView.setText(p+"M");
		viewHolder.operationButton.setTag(position);
		viewHolder.operationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				((Button)arg0).setText("正在下载");
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
										Bundle bundle=new Bundle();
										bundle.putString("url", url);
										bundle.putInt("tag", position);
										bundle.putString("name", info.getapp_name());
										Message message=new Message();
										message.what=0;
										message.obj=info;
										message.setData(bundle);
										handler2.sendMessage(message);
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
	private Handler handler2=new Handler(){
		@Override
		public void handleMessage(android.os.Message msg){
			switch (msg.what) {
			case 0:
				mProgressDialog = new ProgressDialog(mContext);
				mProgressDialog.setMessage("A message");
				mProgressDialog.setIndeterminate(true);
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				mProgressDialog.setCancelable(true);
				
				// execute this when the downloader must be fired
				AppMarket infoAppMarket=(AppMarket)msg.obj;
				final DownloadTask downloadTask = new DownloadTask(mContext,infoAppMarket,msg.arg1);
				downloadTask.execute(msg.getData().getString("url"));

				/*mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				    @Override
				    public void onCancel(DialogInterface dialog) {
				        downloadTask.cancel(true);
				    }
				});*/
				break;
			default:
				break;
			}
		}
	};
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg){
			int tag=msg.arg1;
			View view= gridview.findViewWithTag(tag);
			Button button;
			if(view!=null){
				button=(Button)view;
				switch (msg.what) {
				case 1:
					button.setText("安装失败");
					break;
				case 2:
					button.setText("下载失败");
					break;
				case 4:
					button.setText("正在安装");
					break;
				case 5:
					button.setText("安装成功");
					break;
				case 6:
					button.setText("正在下载");
					break;
				default:
					break;
				}
			}
		}
	};

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
		System.out.println("数据已经更新");
		gridview.stopLoadMore();
		mChildCount = getCount();	
	}

	class DownloadTask extends AsyncTask<String, Integer, String> {

	    private Context context;
	    private PowerManager.WakeLock mWakeLock;
	    private AppMarket info;
	    private int tag;

	    public DownloadTask(Context context,AppMarket info,int tag) {
	    	this.info=info;
	        this.context = context;
	        this.tag=tag;
	    }
	    
	    @Override
	    protected String doInBackground(String... sUrl) {
	        InputStream input = null;
	        OutputStream output = null;
	        HttpURLConnection connection = null;
	        String outfilepath=Constant.SDCARD_APK_PATH+info.getapp_name()+".apk";
	        boolean flag=false;
	        try {
	            //URL url = new URL(sUrl[0]);
	        	URL url=new URL("http://219.239.26.14/files/408300000636FC47/www.renrendai.com/mobile/apk/rrdlc_v2.0.1.apk");
	            connection = (HttpURLConnection) url.openConnection();
	            connection.connect();
	            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
	                return "Server returned HTTP " + connection.getResponseCode()
	                        + " " + connection.getResponseMessage();
	            }
	            // this will be useful to display download percentage
	            // might be -1: server did not report the length
	            int fileLength = connection.getContentLength();
	            File apkFile=new File(outfilepath);
	            if(apkFile.exists()&&apkFile.length()==fileLength){//apk文件已经下载完成了
	            	
	            }else{
	            	 // download the file
		            input = connection.getInputStream();
		            File dir=new File(Constant.SDCARD_APK_PATH);
		            if (!dir.exists()){
		            	dir.mkdirs();
		            }
		            output = new FileOutputStream(outfilepath);

		            byte data[] = new byte[4096];
		            long total = 0;
		            int count;
		            while ((count = input.read(data)) != -1) {
		                // allow canceling with back button
		                if (isCancelled()) {
		                    input.close();
		                    return null;
		                }
		                total += count;
		                // publishing the progress....
		                if (fileLength > 0) // only if total length is known
		                    publishProgress((int) (total * 100 / fileLength));
		                output.write(data, 0, count);
		            }
	            }
	            flag=true;
	        } catch (Exception e) {
	        	System.out.println(e.toString());
	            return e.toString();
	        } finally {
	            try {
	                if (output != null)
	                    output.close();
	                if (input != null)
	                    input.close();
	            } catch (IOException ignored) {
	            }

	            if (connection != null)
	                connection.disconnect();
	            Message message=new Message();
            	message.arg1=tag;
            	message.what=4;
	            if(flag){
	            	handler.sendMessage(message);
	            	Message message1=new Message();
	             	message1.arg1=tag;
		            int result=PackageUtils.installSilent(mContext, outfilepath,"-r");
					if (result==PackageUtils.INSTALL_SUCCEEDED) {
						AppInfoService service=new AppInfoService(mContext);
						info.apk_path=outfilepath;
						info.install_state=1;			
						service.insertAppMarket(info);
						System.out.println("install scuccess");
						message1.what=5;
						handler.sendMessage(message1);
					}else{
						message1.what=1;
						handler.sendMessage(message1);
						System.out.println("install fail");
					}
	            }else{
	            	message.what=2;
	            	handler.sendMessage(message);
	            }
	        }
	        return null;
	    }
	    
	   /* protected void onPreExecute() {
	        super.onPreExecute();
	        // take CPU lock to prevent CPU from going off if the user 
	        // presses the power button during download
	        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
	        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
	             getClass().getName());
	        mWakeLock.acquire();
	        mProgressDialog.show();
	    }

	    @Override
	    protected void onProgressUpdate(Integer... progress) {
	        super.onProgressUpdate(progress);
	        // if we get here, length is known, now set indeterminate to false
	        mProgressDialog.setIndeterminate(false);
	        mProgressDialog.setMax(100);
	        mProgressDialog.setProgress(progress[0]);
	    }

	    @Override
	    protected void onPostExecute(String result) {
	        mWakeLock.release();
	        mProgressDialog.dismiss();
	        if (result != null)
	            Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
	        else
	            Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
	    }*/
	}
}
