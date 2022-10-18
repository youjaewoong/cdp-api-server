package first.api.server.exception.custom;

import first.api.server.exception.error.ErrorCode;
import first.api.server.exception.error.PlatformErrorCode;

public class ResourceAlreadyExistExcepton extends CmpException {

	/* serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	public static String PARAM_KEY_RESOURCE_TYPE = "resourcType";
	
	
	public ResourceAlreadyExistExcepton(ErrorCode errorCode, String resourceType) {
		super(errorCode, null);
		addParam(PARAM_KEY_RESOURCE_TYPE, resourceType);
	}
	
	
	public ResourceAlreadyExistExcepton(String resourceType) {
		super(PlatformErrorCode.ResourceAlreadyExists, null);
		addParam(PARAM_KEY_RESOURCE_TYPE, resourceType);
	}
}
