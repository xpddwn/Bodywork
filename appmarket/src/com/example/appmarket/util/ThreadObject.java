package com.example.appmarket.util;

/**
 * �߳�ִ�ж���
 * 
 * @���� komojoemary
 * @version [�汾��, 2011-2-24]
 * @see [�����/����]
 * @since [��Ʒ/ģ��汾]
 */
public abstract class ThreadObject {

	/**
	 * �Ƿ������
	 */
	private boolean isOk = false;

	/**
	 * �̴߳������
	 * 
	 * @exception/throws [Υ������] [Υ��˵��]
	 * @see [�ࡢ��#��������#��Ա]
	 */

	public abstract Object handleOperation();

	public boolean isOk() {
		return isOk;
	}

	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}

}
