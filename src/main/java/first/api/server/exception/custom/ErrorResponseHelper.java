package first.api.server.exception.custom;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import first.api.server.context.ApiVersion;
import first.api.server.context.BaseConstants;
import first.api.server.context.ContextHolder;
import first.api.server.exception.error.ErrorCode;
import first.api.server.exception.error.ErrorResponseCode;
import first.api.server.exception.error.RestApiContactError;
import first.api.server.exception.error.RestApiError;
import first.api.server.utils.ExceptionUtils;
import first.api.server.utils.MessageUtils;

/**
 * Error코드 관련 콩콩 기능을 정의 합니다.
 * 
 * @author jw
 * @since 2021. 12. 13.
 * @version 1.0
*/

public class ErrorResponseHelper implements ErrorResponseCode {
	
	/**
	 * Spring error attribute를 {@link RestApiError} 메시지로 변경
	 * @param code 에러코드
	 * @param servive MSA 서비스콛,
	 * @param attributes spring error attribute
	 * @return {@link RestApiError} 형식의 Map
	*/
	public static Map<String, Object> buildErrorResponseMap(String code, 
															String service, 
															Map<String, Object> attributes) {
		Map<String, Object> errorAttributes = new LinkedHashMap<>();
		
		errorAttributes.put(CODE, code);
		errorAttributes.put(SERVICE, service);
		errorAttributes.put(MESSAGE, attributes.get(ERROR));
		errorAttributes.put(PATH, attributes.get(PATH));
		errorAttributes.put(REQUEST_ID, ContextHolder.getRequestId());
		errorAttributes.put(TIMESTAMP, new Date());
		
		return errorAttributes;
	}
	
	
	public static RestApiError buildRestApiError(int httpStatus, 
												 String serviceCode, 
												 String path,
												 String message,
												 ErrorCode errorCode,
												 boolean addStackTrace,
												 Throwable th) {
		
		RestApiError restApiError = getInstance(httpStatus);
		if (StringUtils.isBlank(ContextHolder.getService())) {
			restApiError.setService(serviceCode);
		} else {
			restApiError.setService(ContextHolder.getService());
		}
		
		restApiError.setCode(getErrorCodeByUri(errorCode, path));
		restApiError.setPath(path);
		restApiError.setRequestId(ContextHolder.getRequestId());
		
		// 메시지 국제화
		String i18nMessage = message;
		if (StringUtils.isEmpty(i18nMessage)) {
			i18nMessage = MessageUtils.getLocalizedMessage(errorCode.getCode(), errorCode.getDefaultMessage());
		}
		restApiError.setMessage(i18nMessage);
		
		// StackTrace 정보 추가
		Throwable rootCause = ExceptionUtils.getRootCause(th);
		if (rootCause != null && addStackTrace) {
			restApiError.setStackTrace(ExceptionUtils.stackTraceString(rootCause, BaseConstants.PKG_PREFIX));
		}
		
		return restApiError;
	}
	
	/**
	 * Error response에 포함되는 에러코드를 리턴 한다.
	 * API runtime에서 사용
	 *    - V1 : <code>ErrorCode</<code> getCode() 메소드 값
	 *    - V2 : <code>ErrorCode</<code> getExternalCode() 메소드 값
	 * @param errorCode 에러코드
	 * @param uri request uri
	 * @return 에러코드
	 */
	public static String getErrorCodeByUri(ErrorCode errorCode, String uri) {
		if (errorCode == null) {
			return null;
		}
		
		if (StringUtils.containsIgnoreCase(uri, ApiVersion.V1)) {
			return errorCode.getCode();
		}
		
		return errorCode.getExternalCode();
	}
	
	/**
	 * Error response에 포함되는 에러코드를 리턴 한다.
	 * API runtime에서 사용, <code>CmpApiAspect</code> 호출되기전에 설정이 안되는 문제가 있어 사용보류
	 *    - V1 : <code>ErrorCode</<code> getCode() 메소드 값
	 *    - V2 : <code>ErrorCode</<code> getExternalCode() 메소드 값
	 * @param errorCode 에러코드
	 * @return 에러코드
	 */
	public static String getErrorCodeByCmpApi(ErrorCode errorCode) {
		if (errorCode == null) {
			return null;
		}
		
		if (StringUtils.equalsIgnoreCase(ApiVersion.V1, ContextHolder.getApiVersion())) {
			return errorCode.getCode();
		}
		
		return errorCode.getExternalCode();
	}
	
	
	/**
	 * Error response에 포함되는 에러코드를 리턴 한다.
	 * API meta정보를 수집할 때 사용
	 *    - V1 : <code>ErrorCode</<code> getCode() 메소드 값
	 *    - V2 : <code>ErrorCode</<code> getExternalCode() 메소드 값
	 * @param errorCode 에러코드
	 * @param version API version
	 * @return 에러코드
	 */
	public static String getErrorCodeByVersion(ErrorCode errorCode, String version) {
		if (errorCode == null) {
			return null;
		}
		
		if (StringUtils.equalsIgnoreCase(ApiVersion.V1, ContextHolder.getApiVersion())) {
			return errorCode.getCode();
		}
		
		return errorCode.getExternalCode();
	}
	
	
	public static RestApiError getInstance(int httpStatus) {
		RestApiError restApiError = null;
		if (HttpStatus.INTERNAL_SERVER_ERROR.value() == httpStatus) {
			restApiError = new RestApiContactError();
		} else {
			restApiError = new RestApiError();
		}
		
		return restApiError;
	}
	
	public static boolean isV1Ap(String url) {
		return StringUtils.contains(url, "/v1/");
	}
	
	public static void writeJsonErrorResponse(HttpServletResponse response,
											  ObjectMapper objectMapper,
											  String serviceCode,
											  String path,
											  String message, ErrorCode errorCode)
			throws JsonGenerationException, JsonMappingException, IOException {
		writeObjectResponse(response,
							objectMapper,
							HttpStatus.resolve(errorCode.getStatus()),
							buildRestApiError(errorCode.getStatus(), serviceCode, path, message, errorCode, false, null));
	}
	
	
	private static void writeObjectResponse(HttpServletResponse response, 
											ObjectMapper objectMapper, 
											HttpStatus httpStatis,
											Object object)
			throws JsonGenerationException, JsonMappingException, IOException {
		
		Assert.notNull(response, "Respone cannot be null");
		Assert.notNull(response, "ObjectMapper cannot be null");
		Assert.notNull(response, "HttpStatus cannot be null");
		
		response.setStatus(httpStatis.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		
		if (object != null) {
			PrintWriter writer = response.getWriter();
			objectMapper.writeValue(writer, object);
			writer.flush();
		}
	}

}
