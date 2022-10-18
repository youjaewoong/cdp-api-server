package first.api.server.utils;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.Assert;
import org.springframework.validation.FieldError;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class ExceptionUtils {

	public static List<FieldError> getFieldErrors(ConstraintViolationException ex, MessageSource messageSource) {
		List<FieldError> fieldErrors = new ArrayList<>();
		String fieldName = null;
		String message = null;
		for (ConstraintViolation<?> cv : ex.getConstraintViolations()) {
			fieldName = StringUtils.substringAfterLast(cv.getPropertyPath().toString(), ".");
			message = cv.getMessage();
			if (MessageUtils.isMessageCode(message)) {
				message = getMessageByCode(StringUtils.substringBetween(message, "{", "}"), messageSource);
			}
			
			fieldErrors.add(new FieldError(
					"",
					fieldName,
					cv.getInvalidValue(),
					false,
					new String[] { fieldName },
					null,
					message));
		}
		return fieldErrors;
	}

	
	/**
	 * 
	 * @param ex InvalidFormatException
	 * @param messageSource message source
	 * @return 필드에러목록
	 */
	public static List<FieldError> getFieldErrors(InvalidFormatException ex, MessageSource messageSource) {
		List<Reference> paths = ex.getPath();
		String fromStr = null;
		
		List<FieldError> fieldErrors = new ArrayList<>();
		String rejectVal = null;
		if (ex.getValue() != null) {
			rejectVal = ex.getValue().toString();
		}
		String message = ex.getOriginalMessage();
		String targetType = "";
		if (ex.getTargetType() != null) {
			targetType = ClassUtils.getShortCanonicalName(ex.getTargetType());
			message = String.format("Not a valid %s value", targetType);
		}
		
		String fieldName = null;
		for (Reference reference : paths) {
			if(StringUtils.isEmpty(reference.getFieldName())) {
				continue;
			}
			
			if (reference.getFrom() != null) {
				fromStr = reference.getFrom().toString();
			}
			
			fieldName = reference.getFieldName();
			if (reference.getIndex() > -1) {
				fieldName = String.format("%s[%d]", fieldName, reference.getIndex());
			}
			
			fieldErrors.add(
					new FieldError(
							fromStr,
							reference.getFieldName(),
							rejectVal,
							false,
							new String[] { String.format("%s.%s", targetType, fieldName)},
							null,
							message));
		}
		
		return fieldErrors;
	}
	
	
	
	/**
	 * 
	 * @param ex JsonParseException
	 * @param messageSource message source
	 * @return 필드에러목록
	 */
	public static List<FieldError> getFieldErrors(JsonParseException ex, MessageSource messageSource) {
		JsonLocation jsonLocation = ex.getLocation();
		List<FieldError> fieldErrors = new ArrayList<>();
		
		String srcRefStr = "";
		if (jsonLocation.getSourceRef() != null) {
			srcRefStr = jsonLocation.getSourceRef().toString();
		}
		fieldErrors.add(
				new FieldError(
						"Json",
						String.format("Location[%d, %d]", 
								jsonLocation.getLineNr(),
								jsonLocation.getColumnNr()),
						srcRefStr,
						false,
						null,
						null,
						ex.getOriginalMessage()));
		return fieldErrors;
	}
	
	
	
	/**
	 * @param ex MethodArgumentTypeMismatchException
	 * @param messageSource message source
	 * @return 필드목록에러
	 */
	public static List<FieldError> getFieldErrors(MethodArgumentTypeMismatchException ex, MessageSource messageSource) {
		Assert.notNull(ex, "MethodArgumentTypeMismatchException cannot be null");
		Assert.notNull(messageSource, "messageSource cannot be null");
		List<FieldError> fieldErrors = new ArrayList<>();
		
		String requiredTypeName = "";
		Class<?> requiredType = ex.getRequiredType();
		if (requiredType != null) {
			requiredTypeName = requiredType.getName();
		}
		
		String message = getMessageByCode(String.format("%s.%s", ex.getErrorCode(), requiredTypeName), messageSource);
		fieldErrors.add(new FieldError(ex.getName(), ex.getName(), ex.getValue(), false, null, null, message));
		
		return fieldErrors;
	}
	
	
	
	/**
	 * 개발 패키지에서 exception 발생 위치를 리턴한다.
	 * @param th throwable
	 * @param packagePrefix 개발소스 패키지 prefix
	 * @return exception 발생 위치문자열 (class-name.method-name.line-number)
	 */
	public static String getFirstErrorLocation(final Throwable th, String packagePrefix) {
		StringBuilder sb = new StringBuilder();
		for(StackTraceElement ste : th.getStackTrace()) {
			if (!ste.getClassName().startsWith(packagePrefix)) {
				sb.append(ste.getClassName())
				.append(".")
				.append(ste.getMethodName())
				.append("(")
				.append(ste.getLineNumber())
				.append(")");
				
				break;
			}
		}
		return sb.toString();
	}
	
	
	/**
	 * @param message code
	 * @param messageSource
	 * @return
	 */
	public static String getMessageByCode(String messageCode, MessageSource messageSource, Object... args) {
		if (StringUtils.isEmpty(messageCode)) {
			return messageCode;
		}
		
		String message = messageCode;
		try {
			message = messageSource.getMessage(messageCode, args, LocaleContextHolder.getLocale());
		}catch(NoSuchMessageException e) {
			message = String.format("Message undefiend : %s", messageCode);
		}
		return message;
	}
	
	
	/**
	 * 
	 * @param message
	 * @param messageSource
	 * @return
	 */
	public static String getMessage(String message, MessageSource messageSource) {
		if(MessageUtils.isMessageCode(message)) {
			return getMessageByCode(StringUtils.substringBetween(message, "{", "}"), messageSource);
		}else {
			return message;
		}
	}
	
	
	/**
	 * Root cause을 리턴한다
	 * @param th throwable
	 * @return root cause
	 */
	public static Throwable getRootCause(final Throwable th) {
		Throwable cause = th;
		while (cause != null) {
			if (cause.getCause() == null) {
				break;
			}
			cause = cause.getCause();
		}
		return cause;
	}

	
	public static String[] stackTraceString(Throwable th, String packagePrefix) {
		List<String> stackList = new ArrayList<>();
		stackList.add(th.toString());
		for (StackTraceElement ste : th.getStackTrace()) {
			if(!ste.getClassName().startsWith(packagePrefix) || ste.getClassName().indexOf("CGLIB$$") > 0) {
				continue;
			}
			
			stackList.add(ste.toString());
		}
		return stackList.toArray(new String[stackList.size()]);
	}


}
