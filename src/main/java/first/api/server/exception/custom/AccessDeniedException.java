package first.api.server.exception.custom;

import first.api.server.exception.error.PlatformErrorCode;

public class AccessDeniedException extends CmpException{

	private static final long serialVersionUID = 1L;
	public static String PARAM_KEY_SERVICE = "serive";
	public static String PARAM_KEY_ACTION = "action";
	public static String PARAM_KEY_RESOURCE = "resource";
	public static String PARAM_EXT_MESSAGE = "Access Denied.";

	public AccessDeniedException(String service, String action, String resource) {
		super(PlatformErrorCode.AccessDenied);
		addParam(PARAM_KEY_SERVICE, service);
		addParam(PARAM_KEY_ACTION, action);
		addParam(PARAM_KEY_RESOURCE, resource);
	}
	
	public AccessDeniedException(String action) {
		super(PlatformErrorCode.AccessDenied);
		addParam(PARAM_KEY_ACTION, action);
	}
	
	public AccessDeniedException() {
		super(PlatformErrorCode.AccessDenied);
	}
}
