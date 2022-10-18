package first.api.server.utils;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import first.api.server.context.ContextHolder;
import first.api.server.exception.error.ErrorCode;


@Component
public class MessageUtils {

	/** Message code prefix */
	private static final String MSG_CODE_PREFIX = "{";
	
	/** Message code suffix */
	private static final String MSG_CODE_SUFFIX = "}";
	
	@Autowired
	private static MessageSource messageSource;
	
	
	public static String getLocalizedMessage(ErrorCode errorCode) {
		return getLocalizedMessage(errorCode.getCode(), null, null);
	}
	
	
	public static String getLocalizedMessage(ErrorCode errorCode, Object[] messageArgs) {
		return getLocalizedMessage(errorCode.getCode(), null, null, messageArgs);
	}
	
	public static String getLocalizedMessage(MessageSource messageSource, String messageCode) {
		return getLocalizedMessage(messageCode, null, null, messageSource);
	}
	
	public static String getLocalizedMessage(MessageSource messageSource, String messageCode, Object[] messageArgs) {
		return getLocalizedMessage(messageCode, null, null, messageSource, messageArgs);
	}
	
	public static String getLocalizedMessage(MessageSource messageSource, String messageCode, String defaultMessage) {
		return getLocalizedMessage(messageCode, null, defaultMessage, messageSource);
	}
	
	public static String getLocalizedMessage(String messageCode) {
		return getLocalizedMessage(messageCode, null, null);
	}
	
	public static String getLocalizedMessage(String messageCode, Object[] messageArgs) {
		return getLocalizedMessage(messageCode, null, null, messageArgs);
	}
	
	public static String getLocalizedMessage(String messageCode, String defaultMessage){
		return getLocalizedMessage(messageCode, null, defaultMessage);
	}
	
	
	/**
	 * i18n 메세지를 조회한다.
	 * @param messageCode 메세지코드
	 * @param languageTag language tag
	 * @param defaultMessage 기본메세지
	 * @param messageSource 메세지소스
	 * @param messageArgs 메세지인자목록
	 * @return 메세지
	 */
	public static String getLocalizedMessage(
			String messageCode,
			String languageTag,
			String defaultMessage,
			MessageSource messageSource,
			Object... messageArgs) {
		if(StringUtils.isEmpty(messageCode)) {
			return "";
		}
		
		// 사용자 context의 languageTag 사용
		String language = languageTag;
		if (StringUtils.isEmpty(language)) {
			language = ContextHolder.getLanguage();
		}
		
		String message = null;
		
		MessageSource trgtMessageSource = messageSource;
		if(trgtMessageSource == null) {
			trgtMessageSource = MessageUtils.messageSource;
		}
		try {
			message = trgtMessageSource.getMessage(messageCode, messageArgs, Locale.forLanguageTag(language));
		
		}catch(NoSuchMessageException e) {
			// defaultMessage가 공백이면 공백문자 리턴
			if (defaultMessage == null) {
				message = "";
			}else {
				message = defaultMessage;
			}
		}
		
		return message;
	}
	
	/**
	 * i18n 메세지를 조회한다.
	 * @param messageCode 메세지코드
	 * @param languageTag language tag
	 * @param defaultMessage 기본메세지
	 * @param messageArgs 메세지인자목록
	 * @return 메세지
	 */
	public static String getLocalizedMessage(
			String messageCode,
			String languageTag,
			String defaultMessage,
			Object... messageArgs) {
		return getLocalizedMessage(messageCode, languageTag, defaultMessage, messageSource, messageArgs);
	}
	
	
	/**
	 * 메세지 코드를 리턴한다.
	 * @param messagevalue 메시지 값
	 * @return 메세지 코드
	 */
	public static String getMessageCode(String messageCode) {
		if (StringUtils.isEmpty(messageCode)) {
			return messageCode;
		}
		
		if (isMessageCode(messageCode)) {
			return StringUtils.substringBetween(messageCode, MSG_CODE_PREFIX, MSG_CODE_SUFFIX);
		}else {
			return messageCode;
		}
	}
	
	
	/**
	 * 메세지코드 여부를 리턴한다.
	 * @param message 메세지 값
	 * @return 메세지코드 여부
	 */
	public static boolean isMessageCode(String message) {
		if (StringUtils.isEmpty(message)) {
			return false;
		}
		return StringUtils.startsWith(message, MSG_CODE_PREFIX) && StringUtils.endsWith(message, MSG_CODE_SUFFIX)
				&& message.length() > 2;
	}
	
	@Autowired
	private MessageUtils(MessageSource messageSource) {
		MessageUtils.messageSource = messageSource;
	}
}
