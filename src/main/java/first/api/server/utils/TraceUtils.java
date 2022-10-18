package first.api.server.utils;

import java.util.UUID;

import first.api.server.context.BaseConstants;

public class TraceUtils {

	public static String generateRequestId() {
		return String.format("%s-%s", BaseConstants.REQUEST_ID_PREFIX,
				UUID.randomUUID().toString().toUpperCase().replace("-", ""));
	}
}
