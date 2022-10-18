package first.api.server.context;

import org.apache.commons.lang3.StringUtils;

import first.api.server.utils.TraceUtils;

public class ContextHolder {

	public static final String CMP_DEF_USER_EMAIL = "todo@samsung.com";
	
	public static final String CMP_DEF_USER_ID = "1111";
	
	private static final ThreadLocal<Context> THREAD_LOCAL = new ThreadLocal<>();
	
	/**
	 * <code>CmpApi</code>에 정의된 version을 리턴한다.
	 * @return API version
	 */
	public static String getApiVersion() {
		Context cmpContext = THREAD_LOCAL.get();
		if(cmpContext == null) {
			return ApiVersion.V1;
		}
		return cmpContext.getApiVersion();
	}
	
	/**
	 * 클라이언트 유형을 리턴한다.
	 * @return the clientType
	 */
	public static String getClientType() {
		Context cmpContext = THREAD_LOCAL.get();
		if(cmpContext == null) {
			return CmpClientType.SYSTEM;
		}
		return cmpContext.getClientType();
	}
	
	/**
	 * Cmp context정보를 리턴한다
	 * @return
	 */
	public static Context getCmpContext() {
		return THREAD_LOCAL.get();
	}
	
	/**
	 * 회사 Id를 리턴한다
	 * @return
	 */
	public static String getCompanyId() {
		Context cmpContext = THREAD_LOCAL.get();
		if(cmpContext == null) {
			return null;
		}
		return cmpContext.getCompanyId();
	}
	
	/**
	 * OAUTH idToken을 리턴한다.
	 * @return the idToken
	 */
	public static String getIdToken() {
		Context cmpContext = THREAD_LOCAL.get();
		if(cmpContext == null) {
			return null;
		}
		return cmpContext.getIdToken();
	}
	
	/**
	 * language를 리턴한다.
	 * Default value : {@code:CmpLanguage.KOREA}
	 * @return the language
	 */
	public static String getLanguage() {
		Context cmpContext = THREAD_LOCAL.get();
		if(cmpContext == null) {
			return BaseConstants.DEF_LANGUAGE;
		}
		
		String language = cmpContext.getLanguage();
		if(StringUtils.isEmpty(language)) {
			language = BaseConstants.DEF_LANGUAGE;
		}
		return language;
	}
	
	/**
	 * Login Id를 리턴한다.
	 * @return Login Id
	 */
	public static String getLoginId() {
		Context cmpContext = THREAD_LOCAL.get();
		if(cmpContext == null) {
			return null;
		}
		return cmpContext.getLoginId();
	}
	
	public static String getPrincipalType() {
		Context cmpContext = THREAD_LOCAL.get();
		if(cmpContext == null) {
			return PrincipalType.USER;
		}
		return cmpContext.getPrincipalType();
	}
	
	/**
	 * Project Id를 리턴한다.
	 * @return the projectId
	 */
	public static String getProjectId() {
		Context cmpContext = THREAD_LOCAL.get();
		if (cmpContext == null) {
			return null;
		}
		return cmpContext.getProjectId();
	}
	
	/**
	 * Request 아이디를 리턴한다.
	 * Request 아이디가 없을 경우 새로 생성
	 * @return the requestId
	 */
	public static String getRequestId() {
		Context cmpContext = THREAD_LOCAL.get();
		if (cmpContext == null) {
			String requestId = TraceUtils.generateRequestId();
			cmpContext = new Context(requestId);
			setCmpContext(cmpContext);
		}
		return cmpContext.getRequestId();
	}
	
	/**
	 * 역확전화 역할Id를 리턴한다.
	 * @return the roleId
	 */
	public static String getRoleId() {
		Context cmpContext = THREAD_LOCAL.get();
		if (cmpContext == null) {
			return null;
		}
		return cmpContext.getRoleId();
	}
	
	/**
	 * CMP service 명을 리턴한다.
	 * @return the ervice
	 */
	public static String getService() {
		Context cmpContext = THREAD_LOCAL.get();
		if(cmpContext == null) {
			return null;
		}
		return cmpContext.getService();
	}
	
	/**
	 * User email을 리턴한다.
	 * @return User email
	 */
	public static String getUserEmail() {
		Context cmpContext = THREAD_LOCAL.get();
		if (cmpContext == null) {
			return null;
		}
		return cmpContext.getUserEmail();
	}
	
	/**
	 * 사용자 email을 리턴한다.
	 * 로그인되지 않은 사용자는 default user email 주소를 리턴
	 * @return 사용자 email
	 */
	public static String getUserEmailWithDefaultUser() {
		String userEmail = getUserEmail();
		if (StringUtils.isEmpty(userEmail)) {
			return CMP_DEF_USER_EMAIL;
		}
		return userEmail;
	}
	
	/**
	 * User 아이디를 리턴한다.
	 * @return User 아이디
	 */
	public static String getUserId() {
		Context cmpContext = THREAD_LOCAL.get();
		if (cmpContext == null) {
			return null;
		}
		return cmpContext.getUserId();
	}
	
	/**
	 * 사용자 아이디를 리턴한다.
	 * 로그인되지 않은 사용자는 default 사용자 아이디 주소를 리턴
	 * @return 사용자 아이디를
	 */
	public static String getUserIdWithDefaultUser() {
		String userId = getUserId();
		if(StringUtils.isEmpty(userId)) {
			return CMP_DEF_USER_ID;
		}
		return userId;
	}
	
	public static Long getUserUid() {
		Context cmpContext = THREAD_LOCAL.get();
		if(cmpContext == null) {
			return null;
		}
		return cmpContext.getUserUid();
	}
	
	public static String getClientIp() {
		Context cmpContext = THREAD_LOCAL.get();
		if(cmpContext == null) {
			return null;
		}
		return cmpContext.getClientIp();
	}
	
	public static String getMfa() {
		Context cmpContext = THREAD_LOCAL.get();
		if(cmpContext == null) {
			return null;
		}
		return cmpContext.getMfa();
	}
	
	/**
	 * Thread 종료시 반ㄷ시 호출하여 ThreadLocal 정보를 삭제해야 함
	 */
	public static void removeCmpContext() {
		THREAD_LOCAL.remove();
	}
	
	public static void setCmpContext(Context cmpContext) {
		THREAD_LOCAL.set(cmpContext);
	}
}