package first.api.server.exception.custom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.FieldError;

import first.api.server.exception.error.PlatformErrorCode;
import lombok.Getter;


public class BindingResultException extends CmpException {

	/* serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	@Getter
	private final String objName;
	
	@Getter
	private List<FieldError> objectErrors;
	
	
	public BindingResultException(String objName, String extMessage) {
		super(PlatformErrorCode.InvalidInput, extMessage);
		this.objName = objName;
	}
	
	
	public BindingResultException(String objName, String extMessage, List<FieldError> objectErrors) {
		super(PlatformErrorCode.InvalidInput, extMessage);
		this.objectErrors = objectErrors;
		this.objName = objName;
	}
	
	
	public BindingResultException addObjectErrors(final String field, final Object value) {
		return addObjectErrors(field, value, null);
	}
	
	public BindingResultException addObjectErrors(final String field, final Object value, final String message) {
		if (objectErrors == null) {
			objectErrors = new ArrayList<FieldError>();
		}
		
		String[] code = new String[] { objName + "." + field };
		objectErrors.add(new FieldError(objName, field, value, false, code, new Object[] { field, value }, message));
		
		return this;
	}
}
