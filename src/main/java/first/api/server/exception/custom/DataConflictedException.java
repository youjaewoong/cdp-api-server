package first.api.server.exception.custom;

import first.api.server.exception.error.ErrorCode;
import first.api.server.exception.error.PlatformErrorCode;

public class DataConflictedException extends CmpException {
	
	/* serialVersionUID */
	private static final long serialVersionUID = 1L;

	public static String PARAM_KEY_FIELD = "field";
	
	public DataConflictedException(ErrorCode errorCode, String field) {
		super(errorCode, null);
		addParam(PARAM_KEY_FIELD, field);
	}
	
	public DataConflictedException(String field) {
		super(PlatformErrorCode.DataConflicted, null);
		addParam(PARAM_KEY_FIELD, field);
	}
}
