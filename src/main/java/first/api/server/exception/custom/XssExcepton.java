package first.api.server.exception.custom;

import first.api.server.exception.error.PlatformErrorCode;

public class XssExcepton extends CmpException {

	/* serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	public XssExcepton() {
		super(PlatformErrorCode.InvalidInput);
	}
}
