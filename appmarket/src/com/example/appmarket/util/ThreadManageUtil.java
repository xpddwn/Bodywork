package com.example.appmarket.util;

import java.util.ArrayList;
import java.util.List;

/**
 * �̹߳��?����
 * 
 * @���� komojoemary
 * @version [�汾��, 2011-2-24]
 * @see [�����/����]
 * @since [��Ʒ/ģ��汾]
 */
public class ThreadManageUtil implements Runnable {
	/**
	 * �������(���ڵ��߳�)
	 */
	private static List<ThreadObject> requestList = null;

	/**
	 * ͬ������(���ڵ��߳�)
	 */
	private static Object object = new Object();

	/**
	 * ThreadManageUtil�����ʵ��(���ڵ��߳�)
	 */
	private static ThreadManageUtil instance = null;

	/**
	 * ��ǰ���������
	 */
	private ThreadObject currentRequest = null;

	/**
	 * �����߳�
	 * 
	 * @param ��
	 * @return ��
	 * @exception/throws ��
	 * @see ��
	 */
	public void run() {
		// ���Ϊ���߳�
		// if (isMultiThread) {
		// runHandle();
		// }
		// �Ƕ��߳�
		// else {
		while (true) {
			// ���Ϊ����Ϊ��ʱ��Ҫ���������󣬲��Ӷ��������»�ȡ
			if (currentRequest == null) {
				synchronized (object) {
					// ���в�Ϊ��
					if (requestList.size() > 0) {
						// �Ӷ����л�ȡ����
						currentRequest = (ThreadObject) requestList.get(0);
						// ����������н���ǰ����ɾ��
						requestList.remove(0);
					} else {
						try {
							// ����Ϊ���򽻳�������ȴ���
							object.wait();
							continue;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			runHandle();
			currentRequest = null;
		}
		// }
	}

	/**
	 * 
	 * �����̴��� ���
	 * 
	 * @param ��
	 * @return ��
	 * @exception/throws �� ��
	 * @see ��
	 */
	private void runHandle() {
		try {
			currentRequest.handleOperation();
			Thread.yield();
		} catch (Exception ce) {
			ce.printStackTrace();
		}
	}

	/**
	 * 
	 * �����߳�
	 * ���õ��̶߳������Ŷ�����ʱ���������������������С���������Ƕ��߳�ʱ������һ���µ��߳�
	 * 
	 * @param request
	 *            �������
	 * @return ��
	 * @exception/throws ��
	 */
	public static void sendRequest(ThreadObject request) {
		// ���߳�
		// if (request.isMultiThread()) {
		// ThreadManageUtil downloadHttp = new ThreadManageUtil();
		// downloadHttp.isMultiThread = true;
		// downloadHttp.currentRequest = request;
		// new Thread(downloadHttp).start();
		// }
		// else {
		// ���߳������Ŷ�
		if (instance == null) {
			// �ڵ�ǰ�̶߳���Ϊ��ʱ�Ž����̳߳�ʼ��������ͬʱ��ʼ������
			instance = new ThreadManageUtil();
			requestList = new ArrayList<ThreadObject>();
			new Thread(instance).start();
		}
		insertReqList(request);
		// }
	}

	/**
	 * 
	 * ������ӷ���
	 * 
	 * @param request
	 *            ��Ҫ��ӵ�����
	 * @return ��
	 * @exception/throws ��
	 * @see ��
	 */
	private static void insertReqList(ThreadObject request) {
		synchronized (object) {
			requestList.add(request);
			object.notify();
		}
	}

}
