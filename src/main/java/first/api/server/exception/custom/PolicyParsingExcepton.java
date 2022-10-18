package first.api.server.exception.custom;

import first.api.server.exception.error.ErrorCode;

public class PolicyParsingExcepton extends CmpException {

	/* serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	public PolicyParsingExcepton(ErrorCode errorCode) {
		super(errorCode, "policy parsing error");
	}
}
