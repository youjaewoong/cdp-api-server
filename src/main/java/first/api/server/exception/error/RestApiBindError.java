package first.api.server.exception.error;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestApiBindError extends RestApiError {
	
	@JsonInclude(Include.NON_NULL)
	private List<BindError> bindErros;
	
	public void addBindError(String[] names, String message, Object... values) {
		if (bindErros == null ) {
			bindErros = new ArrayList<>();
		}
		
		String name = null;
		if (names != null && names.length > 0) {
			name = names[0];
		}
		bindErros.add(new BindError(name, values, message));
	}
	
	@Getter
	@Setter
	@AllArgsConstructor
	private static class BindError {
		private String name;
		
		private Object[] value;
		
		private String message;
	}
}
