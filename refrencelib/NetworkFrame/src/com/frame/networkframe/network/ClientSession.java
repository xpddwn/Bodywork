package com.frame.networkframe.network;

import java.util.Vector;

import com.frame.networkframe.protocol.BaseHttpRequest;
import com.frame.networkframe.protocol.BaseResponse;



/**
 * ȫ��Ψһ�Ŀͻ��˻Ự�࣬���������������
 * 
 * ���ܣ� �ͻ��������������ʱ����ķ��ͼ��ظ��Ľ��գ� ���״̬������������֪ͨ��
 * 
 * @author wuwei
 * 
 */
public class ClientSession {

	private static ClientSession instance;

	/**
	 * Э�����������ջ�
	 */
	public IErrorReceiver defErrorReceiver;

	/**
	 * Э������״̬���ջ�
	 */
	public INetStateReceiver defStateReceiver;

	private final Vector<AsynRsqHandlerHelper> vecRsqHandlers = new Vector<AsynRsqHandlerHelper>();

	// public NetworkData networkData = new NetworkData();

	public ClientSession() {

	}

	public static synchronized ClientSession Instace() {
		if (instance == null) {
			instance = new ClientSession();
		}
		return instance;
	}

	/**
	 * �첽��ȡ����ظ��ӿ� ʹ��Ĭ�ϴ��������������״̬��������Ϊ֪ͨ�ӿ�,�����û��ⷽʽ����ֹ���̲߳�������
	 * 
	 * @param request
	 * @param receiver
	 *            return: ����һ�ӿ�˵��
	 */
	public int asynGetResponse(BaseHttpRequest request,
			IResponseReceiver receiver) {
		return asynGetResponse(request, receiver, defErrorReceiver,
				defStateReceiver);
	}

	/**
	 * �첽��ȡ����ظ��ӿ� ʹ��Ĭ�ϴ��������������״̬��������Ϊ֪ͨ�ӿ�,�����û��ⷽʽ����ֹ���̲߳�������
	 * 
	 * @param request
	 * @param receiver
	 *            return: ����һ�ӿ�˵��
	 */
	public int asynGetResponse(BaseHttpRequest request,
			IResponseReceiver receiver, IErrorReceiver defErrorReceiver) {
		return asynGetResponse(request, receiver, defErrorReceiver,
				defStateReceiver);
	}

	/**
	 * �첽��ȡ����ظ��ӿ� ���û��ⷽʽ����ֹ���̲߳�������
	 * 
	 * @param request
	 * @param rspReceiver
	 * @param errReceiver
	 * @param stateReceiver
	 *            return: ��ʶ�������cookie���ϲ�����ø�cookie��ȡ���˴�����
	 */
	synchronized public int asynGetResponse(BaseHttpRequest request,
			IResponseReceiver rspReceiver, IErrorReceiver errReceiver,
			INetStateReceiver stateReceiver) {
		synchronized (getAsynRsqLock()) {
			return asynGetResponseWithoutLock(request, rspReceiver,
					errReceiver, stateReceiver);
		}
	}

	/**
	 * �첽��ȡ����ظ��ӿ� ʹ��Ĭ�ϴ��������������״̬��������Ϊ֪ͨ�ӿ�,��������ʽ�����￼�ǵ��ϲ����һ�λ���������
	 * �ӿڶ�Σ�Ϊ���Ч�ʣ��ڵ���֮ǰ��Ҫ�ϲ㸺�����������½ӿ�ͬ����ˡ�
	 * 
	 * @param request
	 * @param receiver
	 *            return: ����һ�ӿ�˵��
	 */
	public int asynGetResponseWithoutLock(BaseHttpRequest request,
			IResponseReceiver receiver) {
		return asynGetResponseWithoutLock(request, receiver, defErrorReceiver,
				defStateReceiver);
	}

	/**
	 * �첽��ȡ����ظ��ӿ�
	 * 
	 * @param request
	 * @param rspReceiver
	 * @param errReceiver
	 * @param stateReceiver
	 *            return: ��ʶ�������cookie���ϲ�����ø�cookie��ȡ���˴�����
	 */
	public int asynGetResponseWithoutLock(BaseHttpRequest request,
			IResponseReceiver rspReceiver, IErrorReceiver errReceiver,
			INetStateReceiver stateReceiver) {

		// ExecutorService pool = Executors.newFixedThreadPool(4);

		AsynRsqHandlerHelper handler = null;

		// clear bad handler
		for (int index = 0; index < vecRsqHandlers.size();) {
			handler = vecRsqHandlers.elementAt(index);
			if (handler.isBad()) {
				vecRsqHandlers.removeElementAt(index);
			} else {
				++index;
			}
		}

		// find an idle handler to commit request
		for (int index = 0; index < vecRsqHandlers.size(); ++index) {
			handler = vecRsqHandlers.elementAt(index);
			if (!handler.isWorking()) {
				if (handler.commitRequest(index, request, rspReceiver,
						errReceiver, stateReceiver)) {
					return index;
				}
			}
		}

		// don't find, create new handler to commit request
		boolean commitSuccess = false;
		int index = vecRsqHandlers.size();
		do {
			handler = new AsynRsqHandlerHelper();
			commitSuccess = handler.commitRequest(index, request, rspReceiver,
					errReceiver, stateReceiver);
		} while (!commitSuccess);
		vecRsqHandlers.addElement(handler);
		return index;

		// �����Ļ�����������״�����õ�ʱ��ͻ᲻�������̵߳�����

	}

	/**
	 * ȡ��ָ������Ĵ���,������Чcookieֵ�ᱻ����
	 * 
	 * @param rsqCookie
	 *            : �ύ����ʱ���ص�cookie
	 */
	public void cancel(int rsqCookie) {
		synchronized (getAsynRsqLock()) {
			cancelWithoutLock(rsqCookie);
		}
	}

	/**
	 * ȡ����ǰ��������
	 */
	public void cancelAll() {
		AsynRsqHandlerHelper handler;
		synchronized (getAsynRsqLock()) {
			for (int index = 0; index < vecRsqHandlers.size(); ++index) {
				handler = (AsynRsqHandlerHelper) vecRsqHandlers
						.elementAt(index);
				if (handler.isWorking()) {
					handler.cancel();
				}
			}
		}
	}

	/**
	 * ȡ��ָ������Ĵ���,������Чcookieֵ�ᱻ����
	 * 
	 * @param rsqCookie
	 *            : �ύ����ʱ���ص�cookie
	 */
	public void cancelWithoutLock(int rsqCookie) {
		if ((rsqCookie >= vecRsqHandlers.size()) || (rsqCookie < 0)) {
			return;
		}

		AsynRsqHandlerHelper handler = (AsynRsqHandlerHelper) vecRsqHandlers
				.elementAt(rsqCookie);
		if (handler.isWorking()) {
			handler.cancel();
		}
	}

	/**
	 * ��ȡ�첽������
	 * 
	 * @return
	 */
	public Object getAsynRsqLock() {
		return this;
	}

	/**
	 * ͬ����ȡ����ظ��ӿ� ���ô˽ӿں��ڵõ��ظ�ǰ��һֱ����ע, �������κδ���ʱ��ȡ���ûظ�ʵ������ΪErrorResponse,
	 * �ʵ��ô˽ӿں���Ҫ����instanceof�ؼ����ж��Ƿ��Ǵ���ظ��� ��������Ӧ������
	 * 
	 * @param request
	 * @return
	 */
	public BaseResponse syncGetResponse(BaseHttpRequest request) {
		return RsqHandleHelper.getResponseImpl(-1, request, null);
	}

}
