package com.frame.networkframe.network;

import com.frame.networkframe.protocol.BaseHttpRequest;
import com.frame.networkframe.protocol.ErrorResponse;

/**
 * 
 * @author wuwei
 * 
 */
public interface IErrorReceiver {

	void onError(ErrorResponse errorResponse, BaseHttpRequest request,
			int rspCookie);
}
