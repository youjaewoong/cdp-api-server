package first.api.server.exception.custom;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import first.api.server.exception.error.ErrorCode;
import first.api.server.exception.error.ErrorParam;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CmpException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private static final String KEY_EXT_MESSAGE = "mesage";

	private ErrorCode  errorCode;
	
	@Setter
	private String service;
	
	@Getter
	private List<ErrorParam> params;
	
	@Getter
	private String extMessage;
	
	public CmpException(ErrorCode errorCode) {
		this(errorCode, null);
	}
	
	public CmpException(ErrorCode errorCode, String extMessage) {
		this(errorCode, extMessage, null);
	}
	
	public CmpException(ErrorCode errorCode, String extMessage, Throwable cause) {
		super(errorCode.getDefaultMessage(), cause);
		this.errorCode = errorCode;
		setExtMessage(extMessage);
	}
	
	public void setExtMessage(String extMessage) {
		this.extMessage = extMessage;
		if (StringUtils.isNotEmpty(extMessage)) {
			addParam(KEY_EXT_MESSAGE, null, extMessage);
		}
	}
	
	public CmpException addParam(String key, String value) {
		return addParam(key, value, null);
	}
	
	public CmpException addParam(String key, String value, String message) {
		if(params == null) {
			params = new ArrayList<ErrorParam>();
		}
		params.add(new ErrorParam(key, value, message));
		
		return this;
	}
	
}
