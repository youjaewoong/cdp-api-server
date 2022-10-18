package first.api.server.context;

import java.util.Locale;

public interface Language {

	/** Language tag : ko-KR **/
	String KOREAN = Locale.KOREA.toLanguageTag();
	
	/** Language tag : en-US **/
	String ENGLISH = Locale.US.toLanguageTag();
}
