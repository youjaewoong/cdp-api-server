package first.api.server.exception.custom;

import first.api.server.exception.error.ErrorCode;
import first.api.server.exception.error.PlatformErrorCode;

public class IopsApiExcepton extends CmpException {

	/* serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	
	public IopsApiExcepton(String extMessage) {
		super(PlatformErrorCode.InternalServerError, extMessage);
	}
	
	public IopsApiExcepton(ErrorCode errorCode) {
		super(errorCode, null);
	}
}
