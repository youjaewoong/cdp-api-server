package first.api.server.exception.custom;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import first.api.server.exception.error.PlatformErrorCode;
import lombok.Getter;

@Getter
public class OpenApiInvokeException extends CmpException{

	/* serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	/** API HTTP status */
	private final int httpStatus;
	
	/** Remote error API map */
	private final Map<String, Object> remoteError;
	
	
	public OpenApiInvokeException(int httpStatus, Map<String, Object> remoteError) {
		super(PlatformErrorCode.ApiInvokeError, null);
		this.httpStatus = httpStatus;
		this.remoteError = remoteError;
	}
	
	
	public OpenApiInvokeException(int httpStatus, String extMessage) {
		super(PlatformErrorCode.ApiInvokeError, extMessage);
		this.httpStatus = httpStatus;
		this.remoteError = null;
	}
	
	
	@Override
	public String getMessage() {
		String message = super.getMessage();
		
		String causeMessage = null;
		if (remoteError != null) {
			Object objMesage = remoteError.get("message");
			causeMessage = objMesage != null ? objMesage.toString() : null;
		}
		
		if (StringUtils.isNotEmpty(causeMessage)) {
			message += " : cause " + message;
		}
		return message;
	}
}
