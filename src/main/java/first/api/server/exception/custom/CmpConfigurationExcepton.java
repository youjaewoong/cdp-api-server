package first.api.server.exception.custom;

import first.api.server.exception.error.PlatformErrorCode;

public class CmpConfigurationExcepton extends CmpException{
	
	/* serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	
	public CmpConfigurationExcepton(String detailMessage) {
		super(PlatformErrorCode.CmpConfigurationError, detailMessage);
	}
}
