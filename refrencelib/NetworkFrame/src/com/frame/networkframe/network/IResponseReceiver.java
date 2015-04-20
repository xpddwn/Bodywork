package com.frame.networkframe.network;

import com.frame.networkframe.protocol.BaseHttpRequest;
import com.frame.networkframe.protocol.BaseHttpResponse;



public interface IResponseReceiver {
	void onResponse(BaseHttpResponse response, BaseHttpRequest request,
			int rspCookie);
}
