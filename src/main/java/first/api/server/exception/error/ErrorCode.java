package first.api.server.exception.error;

import lombok.Getter;

@Getter
public class ErrorCode {
	
	private final String code;
	
	private final String externalCode;
	
	private final String defaultMessage;
	
	private final int status;
	
	public ErrorCode(String code, String defaultMessage, int status) {
		this(code, code, defaultMessage, status);
	}

	public ErrorCode(String code, String externalCode, String defaultMessage, int status) {
		this.code = code;
		this.externalCode = externalCode;
		this.defaultMessage = defaultMessage;
		this.status = status;
	}
	
	public String toString() {
		return String.format("%d : %s(%s) -%s", status, code, externalCode, defaultMessage);
	}
}
