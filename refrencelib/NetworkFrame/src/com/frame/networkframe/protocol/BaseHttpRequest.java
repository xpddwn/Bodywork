package com.frame.networkframe.protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import com.frame.networkframe.network.INetStateReceiver;
import com.frame.networkframe.runtimedata.RuntimeManager;






/**
 * http����������࣬�ṩ�����ض�http����Ļ�ӿ�
 * 
 * @author zhouyin
 * 
 */
public abstract class BaseHttpRequest {

	// ϣ�����󱻷��͵ķ�ʽ
	public final static int GET = 1;
	public final static int POST = 2;

	protected boolean needGZIP = true;

	public boolean isGetCache = true;

	private String absoluteURI;
	private int method = POST;
	private int connectionTimeout = 10*1000;

	public boolean getNeedGZip() {
		return needGZIP;
	}

	// http����Э��ͷ
	public Header[] headers;

	/**
	 * ����������Դ��Ե�ַ ��ʽΪ��http://www.zhangxinda.cn/xxxx/xxx.xx
	 * 
	 * @param absoluteURI
	 */
	public final void setAbsoluteURI(String absoluteURI) {
		this.absoluteURI = absoluteURI;
	}

	/**
	 * �������󷽷�
	 */
	public final void setMethod(int method) {
		this.method = method;
	}

	/**
	 * ��ȡ���������ַ�ӿ� ��ʽ������: 1. IP��ַ��eg��192.168.12.12[:port] 2.
	 * ������ʽ��eg��www.zhangxinda.cn[:port]
	 * 
	 * @return
	 */
	public final String getHost() {
		try {
			URL parser = new URL(absoluteURI);
			if (parser != null) {
				return parser.getHost();
			} else {
				return "";
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * ��ȡ������Դ��Ե�ַ�ӿ� ��ʽΪ��/xxxx/xxx.xx
	 * 
	 * @return
	 */
	public final String getRelativeURI() {
		try {
			URL parser = new URL(absoluteURI);
			if (parser != null) {
				return parser.getPath();
			} else {
				return "";
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * ��ȡ������Դ�ľ�Ե�ַ�ӿ� ��ʽΪ��http://www.zhangxinda.cn/xxxx/xxx.xx
	 * 
	 * @return
	 */
	public final String getAbsoluteURI() {
		return absoluteURI;
	}

	/**
	 * �����������ض��ظ��ӿ�
	 * 
	 * @return
	 */
	public abstract BaseHttpResponse createResponse();

	/**
	 * ��ȡ�����ͷ�ʽ�ӿڣ�Ĭ�Ϸ���POST���� ��ʱֻ�ṩGET,POST���ַ�ʽ
	 * 
	 * @return
	 */
	public int getMethod() {
		return method;
	}

	/**
	 * ��ȡ���󸽼�ͷ�ӿڣ�Ĭ�Ϸ���null ���ض�ά����ӦΪ2�У���һ��Ϊͷ������ڶ���Ϊͷ����ֵ�� ������ֵӦ���httpЭ��ı�׼ͷ��ʽ��eg:
	 * ������Content-Type ����ֵ��text/html;charset=utf-8
	 * 
	 * @return
	 */
	public String[][] getExtraHeaders() {
		return null;
	}

	/**
	 * �����͵��������ݽӿڣ�Ĭ�ϲ�����κ�����
	 * 
	 * @param cookie
	 *            : ��ʶ�������cookie
	 * @param output
	 *            : http�����
	 * @param stateReceiver
	 *            : ״̬������
	 * @throws IOException
	 */
	public void fillOutputStream(int cookie, OutputStream output,
			INetStateReceiver stateReceiver) throws IOException {
	}

	/**
	 * �Ƿ���Ҫ���汾����ظ��ӿڣ�Ĭ�ϲ���Ҫ ע����Ҫ����ظ���������Ҫ��дhashCode����������hashtable����
	 * 
	 * @return
	 */
	public boolean needCacheResponse() {
		return false;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	protected int requestId = -1;

	protected int requestMsg = -1;

	public String getRequestId() {
		if (requestId == -1) {
			return "";
		} else {
			return RuntimeManager.getString(requestId);
		}
	}

	public String getRequestMsg() {
		if (requestMsg == -1) {
			return "";
		} else {
			return RuntimeManager.getString(requestMsg);
		}
	}
	
	public HttpEntity getPostEntity(int cookie,INetStateReceiver stateReceiver) throws IOException {
		return null;
	}
	
	

}