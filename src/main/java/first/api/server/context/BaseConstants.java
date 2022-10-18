package first.api.server.context;


public interface BaseConstants {
	
	String SERVICE_NAME = "api";

	String PROFILE_LOCAL = "local";
	
	String PROFILE_DEV = "dev";
	
	String PROFILE_PROD = "prod";
	
	String MDC_REQUEST_ID = "MDC.REQUEST_ID";
	
	String DEF_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	
	String DEF_LANGUAGE = Language.KOREAN;
	
	String[] SUPPORT_LANGUAGES = new String[] { Language.KOREAN, Language.ENGLISH };
	
	String REQUEST_ID_PREFIX = "REQUEST";
	
	String PK_APP_HOME = "app.home";
	
	String PKG_PREFIX = "first.api.server";
	
	String PKG_MYBATIS_MAPPER = PKG_PREFIX + ".mapper";
	
	String LOCALE_RESOLVER_COOKIE = "cookie";
	
	String LOCALE_RESOLVER_SESSION = "session";
	
	String LOG_APPENDER_CONSOLE = "console";
	
	String LOG_APPENDER_FILE = "file";
	
	String JSON_DATE_FORMAT = BaseConstants.DEF_DATE_FORMAT;
}
