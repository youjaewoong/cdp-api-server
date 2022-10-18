package first.api.server.exception.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestApiContactError extends RestApiError {
	
	private String contactName;
	
	private String contactEmail;
	
	private String contactUrl;
	
}
