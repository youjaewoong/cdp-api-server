package first.api.server.exception.error;


import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorParam {
	
	@JsonInclude(Include.NON_NULL)
	private final String name;

	@JsonInclude(Include.NON_NULL)
	private final String value;
	
	@JsonInclude(Include.NON_NULL)
	private final String message;
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("name=").append(name);
		if(StringUtils.isNotEmpty(value)) {
			buf.append(", value=").append(value);
		}
		if(StringUtils.isNotEmpty(message)) {
			buf.append(", message=").append(message);
		}
		return buf.toString();
	}
}
