package first.api.server.exception.custom;

import first.api.server.exception.error.ErrorCode;
import first.api.server.exception.error.PlatformErrorCode;

public class DataNotFoundExcepton extends CmpException {

	/* serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	public static String PARAM_KEY_RESOURCE_TYPE = "resourceType";
	
	public DataNotFoundExcepton(ErrorCode errorCode, String resourceType) {
		super(errorCode, null);
		addParam(PARAM_KEY_RESOURCE_TYPE, resourceType);
	}
	
	public DataNotFoundExcepton(String resourceType) {
		super(PlatformErrorCode.ResourceNotFound, null);
		addParam(PARAM_KEY_RESOURCE_TYPE, resourceType);
	}

}
