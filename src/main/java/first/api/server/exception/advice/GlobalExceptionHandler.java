

package first.api.server.exception.advice;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.cert.cmp.CMPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;


import feign.FeignException;
import first.api.server.context.BaseConstants;
import first.api.server.context.ContextHolder;
import first.api.server.exception.custom.AccessDeniedException;
import first.api.server.exception.custom.BindingResultException;
import first.api.server.exception.custom.CmpException;
import first.api.server.exception.custom.ErrorResponseHelper;
import first.api.server.exception.custom.InvalidProjectException;
import first.api.server.exception.custom.OpenApiInvokeException;
import first.api.server.exception.error.ErrorCode;
import first.api.server.exception.error.ErrorResponseCode;
import first.api.server.exception.error.PlatformErrorCode;
import first.api.server.exception.error.RestApiBindError;
import first.api.server.exception.error.RestApiContactError;
import first.api.server.exception.error.RestApiError;
import first.api.server.exception.error.RestApiJsonError;
import first.api.server.properties.AppProperties;
import first.api.server.properties.ServiceProperties;
import first.api.server.utils.ExceptionUtils;
import first.api.server.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler implements ErrorResponseCode {
	
	private MessageSource messageSource;
	
	@Autowired
	private AppProperties appProperties;
	
	@Autowired
	private ServiceProperties serviceProperties;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Value("${spring.config.activate.on-profile:Unknown}")
	private String activeProfile;
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<RestApiError> handleException(final AccessDeniedException ex) {
		return processApiError(PlatformErrorCode.AccessDenied, ex, false);
	}
	
	
	@ExceptionHandler(InvalidProjectException.class)
	public ResponseEntity<RestApiError> handleException(final InvalidProjectException ex) {
		return processApiError(PlatformErrorCode.InvalidProject, ex, false);
	}
	
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<RestApiError> handleException(final AuthenticationException ex) {
		return processApiError(PlatformErrorCode.Unauthorized, ex, false);
	}
	
	
	@ExceptionHandler(BindException.class)
	public ResponseEntity<RestApiError> handleException(final BindException ex) {
		return processApiBindError(ex.getMessage(), ex.getAllErrors());
	}
	
	
	/**
	 * CMP define BindingResultException 처리
	 */
	@ExceptionHandler(BindingResultException.class)
	public ResponseEntity<RestApiError> handleException(final BindingResultException ex) {
		return processApiBindError(ex.getMessage(), ex.getObjectErrors());
	}
	

	@ExceptionHandler(HttpMediaTypeException.class)
	public ResponseEntity<RestApiError> handleException(final HttpMediaTypeException ex) {
		return processApiError(PlatformErrorCode.InvalidInput, ex, false);
	}
	
	
	@ExceptionHandler(CmpException.class)
	public ResponseEntity<RestApiError> handleException(final CmpException th) {
		return processApiError(th, true);
	}
	
	
	/**
	 * DataBaee foreign key 에러 발생 시 :400
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<RestApiError> handleException(final ConstraintViolationException ex) {
		List<FieldError> fieldErrors = ExceptionUtils.getFieldErrors(ex, messageSource);
		if (fieldErrors.isEmpty()) {
			return processApiError(PlatformErrorCode.InvalidInput, ex, false);
			
		} else {
			return processApiBindError("Invalid reuqest parameters", fieldErrors);
		}
	}
	
	
	/**
	 * DataBaee foreign key 에러 발생 시 :500
	 */
	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<RestApiError> handleException(final DataAccessException ex) {
		Throwable rootCause = ExceptionUtils.getRootCause(ex); 
		if (rootCause instanceof BindingResultException) {
			return handleException((BindingResultException) rootCause);
		
		} else {
			return processApiError(PlatformErrorCode.DataAccessError, ex, true);
		}
	}
	
	
	
	/**
	 * FeignException 처리
	 */
	@ExceptionHandler(FeignException.class)
	public ResponseEntity<RestApiError> handleException(final FeignException th) {
		Throwable cause = ExceptionUtils.getRootCause(th); 
		if (cause instanceof IOException) {
			return processApiError(PlatformErrorCode.ApiConnectionError,cause,true);
		} else {
			return processRemoteApiError(th);
		}
	}

	
	
	/**
	 * HttpMessageConversionException, HttpMessageNotReadableException 오류 처리함수.
	 * reuqest JSon body 객체 맵핑 오류
	 */
	@ExceptionHandler({HttpMessageConversionException.class, HttpMessageNotReadableException.class})
	public ResponseEntity<RestApiError> handleException(final HttpMessageConversionException ex) {
		Throwable cause = ex.getMostSpecificCause();
		String message = cause.getMessage();
		List<FieldError> fieldErrors = null;
		if (cause instanceof InvalidFormatException) {
			fieldErrors = ExceptionUtils.getFieldErrors((InvalidFormatException) cause, messageSource);
			
		} else if (cause instanceof JsonParseException) {
			String requestUri = getRequestURI();
			ErrorCode errorCode = PlatformErrorCode.InvalidInput;
			RestApiJsonError restApiError = new RestApiJsonError();
			restApiError.setService(serviceProperties.getCode());
			restApiError.setCode(ErrorResponseHelper.getErrorCodeByUri(errorCode, requestUri));
			restApiError.setMessage(MessageUtils.getLocalizedMessage(errorCode));
			restApiError.setPath(requestUri);
			restApiError.setJsonLocation(((JsonParseException) cause).getLocation());
			new ResponseEntity<RestApiError>(restApiError, HttpStatus.BAD_REQUEST);
			
		} else {
			fieldErrors = new ArrayList<>();
		}
		return processApiBindError(message, fieldErrors);
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<RestApiError> handleException(final HttpRequestMethodNotSupportedException th) {
		return processApiError(PlatformErrorCode.InvalidInput, th, true);
	}
	
	
	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<RestApiError> handleException(final InvalidFormatException ex) {
		List<FieldError> fieldErrors = ExceptionUtils.getFieldErrors(ex, messageSource);
		if (fieldErrors.isEmpty()) {
			return processApiError(PlatformErrorCode.InvalidInput, ex, false);
		} else {
			return processApiBindError(ex.getMessage(), fieldErrors);
		}
	}
	
	
	/**
	 * JsonParseException 오류 처리 함수.
	 */
	@ExceptionHandler(JsonParseException.class)
	public ResponseEntity<RestApiError> handleException(final JsonParseException ex) {
		List<FieldError> fieldErrors = ExceptionUtils.getFieldErrors(ex, messageSource); 
		if (fieldErrors.isEmpty()) {
			return processApiError(PlatformErrorCode.InvalidInput, ex, false);
		
		} else {
			return processApiBindError(ex.getMessage(), fieldErrors);
		}
	}
	

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<RestApiError> handleException(final MethodArgumentNotValidException ex) {
		return processApiBindError(ex.getMessage(), ex.getBindingResult().getFieldErrors());
	}
	

	@ExceptionHandler({MethodArgumentTypeMismatchException.class})
	public ResponseEntity<RestApiError> handleException(final MethodArgumentTypeMismatchException ex) {
		List<FieldError> fieldErrors = ExceptionUtils.getFieldErrors(ex, messageSource); 
		if (fieldErrors.isEmpty()) {
			return processApiError(PlatformErrorCode.InvalidInput, ex, false);
		
		} else {
			return processApiBindError("Type mismatched", fieldErrors);
		}
	}
	
	@ExceptionHandler(MissingRequestHeaderException.class)
	public ResponseEntity<RestApiError> handleException(final MissingRequestHeaderException th) {
		return processApiError(PlatformErrorCode.InvalidInput, th, true);
	}
	
	
	/**
	 * spring.mvc.throw-exception-if-no-handler-found = true 설정
	 *  - NoHandlerFoundException 발생
	 * spring.resources.add-mappings = false 설정
	 *  - static resoruce 사용 안함
	 *
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<RestApiError> handleException(final NoHandlerFoundException ex) {
		return processApiError(PlatformErrorCode.ApiNotFound, ex, false);
	}
	
	
	@ExceptionHandler(Throwable.class)
	public ResponseEntity<RestApiError> handleException(final Throwable th) {
		return processApiError(PlatformErrorCode.ApiNotFound, th, true);
	}
	
	
	private String getRejectValue(Object obj) {
		if (obj == null || !isAddRejectValue()) {
			return null;
		}
		
		return obj.toString();
	}
	
	
	/**
	 * BindException reject value 처리
	 * prod 프로파일에서는 null 리턴
	 * @param objs
	 * @return reject value 문자열
	 */
	private String getRejectValue(Object[] objs) {
		if (ArrayUtils.isEmpty(objs) || !isAddRejectValue()) {
			return null;
		}
		
		// Reject value XSS 처리
		StringBuffer buf = new StringBuffer();
		for (Object object : objs) {
			if (buf.length() > 0) {
				buf.append(",");
			}
			buf.append(getRejectValue(object));
		}
		return buf.toString();
	}
	
	
	/**
	 * Request path
	 * @return request path
	 */
	private String getRequestURI() {
		ServletRequestAttributes requestAttributes = 
				(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (requestAttributes == null) {
			return null;
		}
		
		HttpServletRequest request = requestAttributes.getRequest();
		if (request != null) {
			return request.getRequestURI();
		}
		
		return "";
	}
	
	
	/**
	 * Request path
	 * @return request path
	 */
	private String getContextPath() {
		ServletRequestAttributes requestAttributes = 
				(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if(requestAttributes == null) {
			return null;
		}
		
		HttpServletRequest request = requestAttributes.getRequest();
		if (request != null) {
			return request.getContextPath();
		}
		
		return "";
	}
	
	
	/**
	 * Exceptiuon 상세정보 추가 여부을 리턴한다.
	 *  - prod ,stg : false
	 *  - dev, local : true
	 * @parm th
	 * @return rejectValue 추가 여부
	 */
	private boolean isAddExceptionDetail(Throwable th) {
		if (BaseConstants.PROFILE_PROD.equals(activeProfile)) {
			return false;
		}
		
		return !(th instanceof CMPException || th instanceof NoHandlerFoundException);
	}
	
	
	/**
	 * Binding exception 발생 시 rejectValue 추가 여부을 리턴한다
	 *  - prod, stg      : false
	 *  - dev, local : true
	 *  @return rejectValue 추가 여부
	 */
	private boolean isAddRejectValue() {
		return BaseConstants.PROFILE_LOCAL.equals(activeProfile)
				|| StringUtils.startsWithIgnoreCase(activeProfile, BaseConstants.PROFILE_DEV);
	}
	
	
	/**
	 * 에러메시지에 exception stack trace 정보 추가여부를 리턴한
	 * local, dev* : true
	 * @return exception stack trace 정보 추가여부
	 */
	private boolean isAddStackTrace() {
		return BaseConstants.PROFILE_LOCAL.equals(activeProfile)
				|| StringUtils.startsWithIgnoreCase(activeProfile, BaseConstants.PROFILE_DEV);
	}

	
	private void logError(int httpStatus, RestApiError restApiError, Throwable rootCause, boolean logStackTrace) {
		Map<String, Object> remoteError = restApiError.getCause();
		if (log.isDebugEnabled() || logStackTrace) {
			if (remoteError == null) {
				logLocalError(httpStatus, restApiError, rootCause);
			} else {
				logRemoteError(httpStatus, restApiError, remoteError, rootCause);
			}
		} else {
			if (remoteError == null) {
				logLocalError(httpStatus, restApiError, null);
			} else {
				logRemoteError(httpStatus, restApiError, remoteError, null);
			}
		}
	}

	private void logLocalError(int httpStatus, RestApiError restApiError, Throwable rootCause) {
		log.error("[Error] : {}\nt-status : {}\nt-code : {}\n\t-message : {}",
					restApiError.getPath(),
					httpStatus,
					restApiError.getCode(),
					restApiError.getMessage(),
					rootCause);
	}
	
	private void logRemoteError(int httpStatus, 
								RestApiError restApiError, 
								Map<String, Object> remoteError,
								Throwable rootCause) {
		StringBuffer stackTraceBuf = new StringBuffer();
		String[] stackTraces = (String[]) remoteError.get("stackTrace");
		if (ArrayUtils.isNotEmpty(stackTraces)) {
			for (String stackTrace : stackTraces) {
				if (stackTraceBuf.length() > 0) {
					stackTraceBuf.append("\n    ");
				}
				stackTraceBuf.append(stackTrace);
			}
		}
		
		if (stackTraceBuf.length() > 0) {
			log.error("[Error] : {}\nt-status : {}\n\t-code : {}\n\t-message : {}\n\t-remote-code : {}\n\t-remote-message : {}\n\t-stack-trace : {}",
					restApiError.getPath(),
					httpStatus,
					restApiError.getCode(),
					restApiError.getMessage(),
					remoteError.get(CODE),
					remoteError.get(MESSAGE),
					stackTraceBuf.toString(),
					rootCause);
		} else {
			log.error("[Error] : {}\nt-status : {}\n\t-code : {}\n\t-message : {}\n\t-remote-code : {}\n\t-remote-message : {} ",
					restApiError.getPath(),
					httpStatus,
					restApiError.getCode(),
					restApiError.getMessage(),
					remoteError.get(CODE),
					remoteError.get("message"),
					rootCause);
		}
	}
	
	private ResponseEntity<RestApiError> processApiBindError(String message, List<? extends ObjectError> objectErrors) {
		RestApiBindError restApiError = new RestApiBindError();
		restApiError.setService(serviceProperties.getCode());
		
		String requestUri = getRequestURI();
		ErrorCode errorCode = PlatformErrorCode.InvalidInput;
		restApiError.setCode(ErrorResponseHelper.getErrorCodeByUri(errorCode, requestUri));
		restApiError.setMessage(MessageUtils.getLocalizedMessage(errorCode));
		restApiError.setPath(requestUri);
		restApiError.setRequestId(ContextHolder.getRequestId());
		
		if (CollectionUtils.isNotEmpty(objectErrors)) {
			for (ObjectError objectError : objectErrors) {
				if (objectError instanceof FieldError) {
					FieldError fieldError = (FieldError) objectError;
					
					if(fieldError.isBindingFailure()) {
						restApiError.addParam(fieldError.getField(), getRejectValue(fieldError.getRejectedValue()), "");
					} else {
						restApiError.addParam(fieldError.getField(),
											  getRejectValue(fieldError.getRejectedValue()),
											  localizedBindErrorMessage(objectError.getDefaultMessage()));
					}
				} else {
					restApiError.addParam(StringUtils.join(objectError.getCodes(), ","), 
										  getRejectValue(objectError.getArguments()),
										  localizedBindErrorMessage(objectError.getDefaultMessage()));
				}
			}
		}
		
		return new ResponseEntity<RestApiError>(restApiError, HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<RestApiError> processApiError(CmpException ex,  boolean logStackTrace) {
		ErrorCode errorCode = ex.getErrorCode();
		int httpStatus = errorCode.getStatus();
		
		// Remote API error
		Map<String, Object> remoteError = null;
		if (ex instanceof OpenApiInvokeException) {
			remoteError = ((OpenApiInvokeException) ex).getRemoteError();
			httpStatus = ((OpenApiInvokeException) ex).getHttpStatus();
		}
		
		// Build rest API error
		String requestUri = getRequestURI();
		RestApiError restApiError = ErrorResponseHelper.buildRestApiError(httpStatus, 
																		  serviceProperties.getCode(),
																		  requestUri, 
																		  null, 
																		  errorCode, 
																		  isAddStackTrace(), 
																		  ex);
		
		// HTTP 500에러 일경우
		if (restApiError instanceof RestApiContactError) {
			RestApiContactError error = (RestApiContactError) restApiError;
			error.setContactName(appProperties.getContactName());
			error.setContactEmail(appProperties.getContactEmail());
			error.setContactUrl(appProperties.getContactUrl());
		}
		
		restApiError.setMessage(MessageUtils.getLocalizedMessage(errorCode));
		restApiError.setParams(ex.getParams());
		restApiError.setCause(remoteError);
		
		// 로그 출력
		/** swagger 404 에러 ignore 처리 **/
		if (!isIgnoreLoggingUri(requestUri)) {
			logError(httpStatus, restApiError, ExceptionUtils.getRootCause(ex), logStackTrace);
		}
		
		return new ResponseEntity<RestApiError>(restApiError, HttpStatus.BAD_REQUEST);
	}
	

	private boolean isIgnoreLoggingUri(String requestUri) {
		String contextPath =  getContextPath();
		return StringUtils.equals(contextPath + "/", requestUri) || StringUtils.equals(contextPath + "/csrf", requestUri);
	}
	
	private ResponseEntity<RestApiError> processApiError(ErrorCode errorCode,Throwable th, boolean logStackTrace) {
		int httpStatus = errorCode.getStatus();
		String requestUri = getRequestURI();
		RestApiError restApiError = ErrorResponseHelper.buildRestApiError(httpStatus, 
																		  serviceProperties.getCode(),
																		  requestUri, 
																		  null, 
																		  errorCode, 
																		  isAddStackTrace(), 
																		  th);
		
		// HTTP 500에러 일경우
		if (restApiError instanceof RestApiContactError) {
			RestApiContactError error = (RestApiContactError) restApiError;
			error.setContactName(appProperties.getContactName());
			error.setContactEmail(appProperties.getContactEmail());
			error.setContactUrl(appProperties.getContactUrl());
		}
		
		// 에러메시지
		restApiError.setMessage(MessageUtils.getLocalizedMessage(errorCode));
		
		// CmpException이 아니면 "message" 파라메터에 exception 정보 추가
		Throwable cause = ExceptionUtils.getRootCause(th);
		if (cause != null && isAddExceptionDetail(th)) {
			restApiError.addParam(MESSAGE, cause.getClass().toString(), cause.getMessage());
		}
		
		// 로그 출력, SpringFox csrf NotFound ignore
		if (!isIgnoreLoggingUri(requestUri)) {
			logError(httpStatus, restApiError, ExceptionUtils.getRootCause(th), logStackTrace);
		}
		
		return new ResponseEntity<RestApiError>(restApiError, HttpStatus.resolve(httpStatus));
	}
	
	/**
	 * Feign ErrorDecoder 처리가 안된경우 <code>OpenApiInvokeException</code>로 변한
	 * @return
	 */
	private ResponseEntity<RestApiError> processRemoteApiError(FeignException th) {
		OpenApiInvokeException ex = null;
		
		String body = th.contentUTF8();
		if (StringUtils.isNotEmpty(body)) {
			try {
				// Json parsing
				Map<String, Object> remoteError = 
						objectMapper.readValue(body, new TypeReference<Map<String, Object>>() {
						});
				
				// JSon "requestId"가 있을경우만 reomteError로 변환 처리함
				if (remoteError != null && remoteError.containsKey("requestId")) {
					ex = new OpenApiInvokeException(th.status(), remoteError);
				}
			} catch (IOException e) {
				log.warn("Faild to parse remote error", e);
			}
		}
		
		// Remote error형식이 아닐경우 메시지 바디의 1000사용
		if (ex == null) {
			ex = new OpenApiInvokeException(th.status(), StringUtils.substring(body.toString(), 0, 1000));
		}
		return processApiError(ex, true);
	}


	private String localizedBindErrorMessage(String message) {
		if (StringUtils.isBlank(message)) {
			return message;
		}
		
		if (MessageUtils.isMessageCode(message)) {
			return MessageUtils.getLocalizedMessage(MessageUtils.getMessageCode(message));
		} else {
			return message;
		}
	}
}
