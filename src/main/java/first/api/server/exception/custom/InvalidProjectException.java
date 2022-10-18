package first.api.server.exception.custom;

import first.api.server.exception.error.ErrorCode;
import first.api.server.exception.error.PlatformErrorCode;

public class InvalidProjectException extends CmpException{
	
	public static String PARAM_KEY_URL_TEMPLATE ="urlTemplate";
	public static String PARAM_KEY_HTTP_METHOD ="httpMethod";
	public static String PARAM_KEY_REQUEST_HEADER ="header";
	public static String PARAM_KEY_REQUEST_BODY ="body";
	public static String PARAM_KEY_RESOURCE_SRN ="resourceSrn";
	public static String PARAM_KEY_API_META ="apiMeta";
	public static String PARAM_KEY_PRINCIPAL_ID ="principalId";
	public static String PARAM_KEY_PRINCIPLA_TYPE ="principlaType";
	public static String PARAM_KEY_AUTHORIZATION ="authorization";
	public static String PARAM_KEY_ERROR_MESSAGE ="errorMessage";
	public static String PARAM_KEY_RESOURCE_PROJECT_ID ="resourceProjectId";
	public static String PARAM_KEY_PRINCIPAL_PROJECT_ID ="principalProjectId";
	
	private static final long serialVersionUID = 1L;
	
	private static final String PARAM_KEY_RESOURCE_TYPE = "resourcType";
	
	public InvalidProjectException() {
		super(PlatformErrorCode.InvalidProject);
	}
	
	public InvalidProjectException(ErrorCode erroCode, String resourceType) {
		super(erroCode);
		addParam(PARAM_KEY_RESOURCE_TYPE, resourceType);
	}
	
	public InvalidProjectException(String extMessage,String urlTemplate, String httpMethod, String header, String body, Exception e) {
		super(PlatformErrorCode.InvalidResource, extMessage);
		addParam(PARAM_KEY_URL_TEMPLATE, urlTemplate);
		addParam(PARAM_KEY_HTTP_METHOD, httpMethod);
		addParam(PARAM_KEY_REQUEST_HEADER, header);
		addParam(PARAM_KEY_REQUEST_BODY, body);
		if (null != e) {
			addParam(PARAM_KEY_ERROR_MESSAGE, e.getMessage());
		}
	}
	
}
