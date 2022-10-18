package first.api.server.exception.error;

public interface ErrorResponseCode {
	/** request uri */
	String PATH = "path";
	
	/** Error code */
	String CODE = "code";
	
	String ERROR =  "error";
	
	/** Error message */
	String MESSAGE = "message";
	
	/** Transaction timestamp*/
	String TIMESTAMP = "timestamp";
	
	/** Transaction request id */
	String REQUEST_ID = "requestId";
	
	/** MSA 서비스 ID */
	String SERVICE = "service";
}
