package first.api.server.exception.error;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import first.api.server.context.BaseConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestApiError {

	private String service = BaseConstants.SERVICE_NAME;

	private String code;
	
	private String message;
	
	private String path;
	
	private String requestId;
	
	@JsonInclude(Include.NON_NULL)
	private List<ErrorParam> params;
	
	private String timestamp = DateFormatUtils.format(new Date(), BaseConstants.DEF_DATE_FORMAT);
	
	@JsonInclude(Include.NON_NULL)
	private Map<String, Object> cause;
	
	@JsonInclude(Include.NON_NULL)
	private String[] stackTrace;
	
	public RestApiError addParam(String key, String value, String message) {
		if (params == null) {
			params = new ArrayList<ErrorParam>();
		}
		params.add(new ErrorParam(key, value, message));
		
		return this;
	}
}
