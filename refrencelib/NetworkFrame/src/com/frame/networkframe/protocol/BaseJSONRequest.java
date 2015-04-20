package com.frame.networkframe.protocol;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.json.JSONException;
import org.json.JSONObject;
import com.frame.networkframe.network.INetStateReceiver;
import com.frame.networkframe.network.NetworkData;



public abstract class BaseJSONRequest extends BaseHttpRequest {

	public BaseJSONRequest() {
		if (NetworkData.sessionId != null && !NetworkData.sessionId.equals("")) {
			setAbsoluteURI(NetworkData.accessPoint + ";jsessionid="
					+ NetworkData.sessionId);
		} else {
			setAbsoluteURI(NetworkData.accessPoint);
		}
	}

	@Override
	public void fillOutputStream(int cookie, OutputStream output,
			INetStateReceiver stateReceiver) throws IOException {
	}

	@Override
	public String[][] getExtraHeaders() {
		String[][] aryHeaders = new String[1][2];
		aryHeaders[0][0] = "Content-Type";
		aryHeaders[0][1] = "application/json; charset=UTF-8";
		return aryHeaders;
	}

	/**
	 * 请求包体填充抽象接口，子类实现此接口完成具体请求包体的构�?
	 * 
	 * @param serializer
	 *            ：xml流构建器
	 */
	protected abstract void fillBody(JSONObject jsonObject)
			throws JSONException;

	@SuppressWarnings("unused")
	private void fillHeader(JSONObject jsonObject) throws JSONException {

	}

	protected String requestVersion = BaseProtocolDef.PROTOCOL_VERSION_1;

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpEntity getPostEntity(int cookie, INetStateReceiver stateReceiver)
			throws IOException {
		// TODO Auto-generated method stub
		return super.getPostEntity(cookie, stateReceiver);
	}
	
	
	

}
