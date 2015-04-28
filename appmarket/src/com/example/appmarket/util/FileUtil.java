package com.example.appmarket.util;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class FileUtil {
	private static final String TAG = "file util";
	public static final String UPLOAD_IMAGE_NAME = "image";

	/**
	 * 
	 * 
	 * @param in  輸入流
	 * @param desDir 目标文件目录
	 * @param name 目标文件名称
	 * @throws IOException 
	 */
	public static File saveFile(InputStream in, String desDir, String name)
			throws IOException {
		File file=null;
		if (in == null) {
			return file;
		}
		// 生成目录文件
		File dir = new File(desDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// 生成目标文件
		File saveFile = new File(desDir, name);
		if (!saveFile.exists()) {
			saveFile.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(saveFile);
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.flush();
		out.close();
		return file;
	}

	/**
	 * 
	 * 不同的文件之间用分隔符来隔开，每个文件的开始位置需要加上
	 * Content-Disposition: form-data; name="upfile"; filename="a.txt" 
	 * Content-Type: text/plain
	 * 这两个个字段
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 一次请求的样式
	 * 
	 * Content-Length	341
	   Content-Type	multipart/form-data; 
	   boundary=---------------------------16388281363600
	 * 
	 * 
	 * 
	 * -----------------------------16388281363600 Content-Disposition: form-data; name="upfile"; filename="post.txt" 
	 * Content-Type: text/plain 
	 * 111
	 *  -----------------------------16388281363600 
	 *  Content-Disposition: form-data; name="file2"; filename="" 
	 *  Content-Type: application/octet-stream 
	 *  -----------------------------16388281363600--
	 * 
	 * 
	 */
	/**
	 * 
	 * 
	 * @param requestUrl 请求的长传的地址
	 * @param file 需要上传的文件
	 * @return
	 */
	public static boolean uploadFile(String requestUrl, File file) {
		int TIME_OUT = 60 * 10000; // 超时时间
		String CHARSET = "utf-8"; // 设置编码
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型  
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			//设置超时时间
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);

			conn.setDoInput(true); // 允许输入流  
			conn.setDoOutput(true); // 允许输出流  
			conn.setUseCaches(false); // 不允许使用缓存  
			conn.setRequestMethod("POST"); // 请求方式  
			conn.setRequestProperty("Charset", CHARSET); // 设置编码  
			conn.setRequestProperty("connection", "keep-alive");

			//添加form请求头
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);

			if (file != null) {
				/** 
				 * 当文件不为空，把文件包装并且上传 
				 */
				OutputStream outputSteam = conn.getOutputStream();
				DataOutputStream dos = new DataOutputStream(outputSteam);
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/** 
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件 
				 * filename是文件的名字，包含后缀名的 比如:abc.png 
				 */
				sb.append("Content-Disposition: form-data; name=\"+"
						+ UPLOAD_IMAGE_NAME + "\"; filename=\""
						+ file.getName() + "\"" + LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());

				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				/**
				 * 
				 * Form每个部分用分隔符分割，分隔符之前必须加上"--"着两个字符
				 * (即--{boundary})才能被http协议认为是Form的分隔符，
				 * 表示结束的话用在正确的分隔符后面添加"--"表示结束
				 */
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();

				/** 
				 * 获取响应码 200=成功 当响应成功，获取响应的流 
				 */
				int res = conn.getResponseCode();
				if (res == 200) {
					return true;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * 
	 * @param requestUrl 请求的网络地址
	 * @param in 文件的输入流
	 * @param fileName  保存的文件的名称
	 * @return
	 */
	public static boolean uploadFile(String requestUrl, InputStream in,
			String fileName) {
		int TIME_OUT = 60 * 10000; // 超时时间
		String CHARSET = "utf-8"; // 设置编码
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型 
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			//设置超时时间
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);

			conn.setDoInput(true); // 允许输入流  
			conn.setDoOutput(true); // 允许输出流  
			conn.setUseCaches(false); // 不允许使用缓存  
			conn.setRequestMethod("POST"); // 请求方式  
			conn.setRequestProperty("Charset", CHARSET); // 设置编码  
			conn.setRequestProperty("connection", "keep-alive");

			//添加form请求头
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);

			/** 
			 * 当文件不为空，把文件包装并且上传 
			 */
			OutputStream outputSteam = conn.getOutputStream();
			DataOutputStream dos = new DataOutputStream(outputSteam);
			StringBuffer sb = new StringBuffer();
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINE_END);
			/** 
			 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件 
			 * filename是文件的名字，包含后缀名的 比如:abc.png 
			 */
			sb.append("Content-Disposition: form-data; name=\"+"
					+ UPLOAD_IMAGE_NAME + "\"; filename=\"" + fileName + "\""
					+ LINE_END);
			sb.append("Content-Type: application/octet-stream; charset="
					+ CHARSET + LINE_END);
			sb.append(LINE_END);
			dos.write(sb.toString().getBytes());

			byte[] bytes = new byte[1024];
			int len = 0;
			while ((len = in.read(bytes)) != -1) {
				dos.write(bytes, 0, len);
			}
			in.close();
			dos.write(LINE_END.getBytes());
			/**
			 * 
			 * Form每个部分用分隔符分割，分隔符之前必须加上"--"着两个字符
			 * (即--{boundary})才能被http协议认为是Form的分隔符，
			 * 表示结束的话用在正确的分隔符后面添加"--"表示结束
			 */
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
					.getBytes();
			dos.write(end_data);
			dos.flush();

			/** 
			 * 获取响应码 200=成功 当响应成功，获取响应的流 
			 */
			int res = conn.getResponseCode();
			if (res == 200) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * 
	 * @param url 文件的网络地址
	 * @param desDir 保存的目标目录
	 * @param saveName  保存的文件的名称
	 * @return
	 */
	public static File downFile(String url, String desDir, String saveName) {
		URL myFileUrl = null;
		File file=null;
		try {
			myFileUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.setRequestMethod("HEAD");
			conn.setRequestProperty("Accept-Encoding",
					"");
			//conn.setReadTimeout(5*60*1000);
			//conn.setReadTimeout(6*60*1000);
			System.out.println(conn.getContentLength());
			InputStream is = conn.getInputStream();
			file=saveFile(is, desDir, saveName);
			is.close();
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}
	
	public static final int BUFFER_SIZE = 1024 * 4;

	/**
	 * 
	 * 
	 * @param srcPath  源文件地址
	 * @param desDir  目标文件目录
	 * @param desName  目标文件名称
	 * @return
	 */
	public static boolean copyFile(String srcPath, String desDir, String desName) {
		File file = new File(srcPath);
		if (!file.exists()) {
			System.out.println("源文件不存在,文件名： " + desName);
			return false;
		} else {
			try {
				File dirFile = new File(desDir);
				if (!dirFile.exists()) {
					dirFile.mkdirs();
				}
				InputStream inputStream = new FileInputStream(file);
				OutputStream outputStream = new FileOutputStream(new File(
						dirFile, desName));
				byte[] buffer = new byte[BUFFER_SIZE];
				int length = 0;
				while ((length = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, length);
				}
				inputStream.close();
				outputStream.close();
				return true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 
	 * 
	 * @param file 源文件
	 * @param desDir 目标地址目录
	 * @param desName 目标文件的名称
	 * @return
	 * @throws Exception 
	 */
	public static boolean copyFile(File file, String desDir, String desName)
			throws Exception {
		if (!file.exists()) {
			throw new Exception("源文件不存在,文件名： " + desName);
		} else {
			try {
				File dirFile = new File(desDir);
				if (!dirFile.exists()) {
					dirFile.mkdirs();
				}
				InputStream inputStream = new FileInputStream(file);
				OutputStream outputStream = new FileOutputStream(new File(
						dirFile, desName));
				byte[] buffer = new byte[BUFFER_SIZE];
				int length = 0;
				while ((length = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, length);
				}
				inputStream.close();
				outputStream.close();
				return true;
			} catch (Exception e) {
				throw new Exception("读写文件错误");
			}
		}
	}

	/**
	 * 
	 * 
	 * @param dir  目录
	 * @param name 名字
	 * @param info  文件的信息
	 * @return
	 */
	public static boolean saveFile(String dir, String name, String info) {
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		try {
			File file = new File(dir, name);
			if (!file.exists()) {
				file.createNewFile();
			}
			//定义编码
			OutputStreamWriter write = new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8");
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(info);
			writer.close();

			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * 获取不同系统下的换行符
	 */
	public static String getNewLineSymbol() {
		String string = System.getProperty("os.name").toLowerCase();
		if (string.startsWith("win")) {
			return "\r\n";
		} else if (string.startsWith("lin")) {
			return "\n";
		} else if (string.startsWith("mac")) {
			return "\n";
		} else {
			return "\n";
		}
	}

	/** 
	 *  根据路径删除指定的目录或文件，无论存在与否 
	 *@param sPath  要删除的目录或文件 
	 *@return 删除成功返回 true，否则返回 false。 
	 */
	public static boolean DeleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存在  
		if (!file.exists()) { // 不存在返回 false  
			return flag;
		} else {
			// 判断是否为文件  
			if (file.isFile()) { // 为文件时调用删除文件方法  
				return deleteFile(sPath);
			} else { // 为目录时调用删除目录方法  
				return deleteDirectory(sPath);
			}
		}
	}

	/** 
	 * 删除目录（文件夹）以及目录下的文件 
	 * @param   sPath 被删除目录的文件路径 
	 * @return  目录删除成功返回true，否则返回false 
	 */
	public static boolean deleteDirectory(String sPath) {
		//如果sPath不以文件分隔符结尾，自动添加文件分隔符  
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		//如果dir对应的文件不存在，或者不是一个目录，则退出  
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		//删除文件夹下的所有文件(包括子目录)  
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			//删除子文件  
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} //删除子目录  
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		//删除当前目录  
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/** 
	 * 删除单个文件 
	 * @param   sPath    被删除文件的文件名 
	 * @return 单个文件删除成功返回true，否则返回false 
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除  
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

}
