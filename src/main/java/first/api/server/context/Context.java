package first.api.server.context;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Context {

	private String requestId;
	
	private String projectId;
	
	private String userId;
	
	private Long userUid;
	
	private String loginId;
	
	private String userEmail;
	
	private String language;
	
	private String companyId;
	
	private String clientType;
	
	private String idToken;
	
	private String roleId;
	
	private String service;
	
	private String apiVersion = ApiVersion.V2;
	
	private String clientIp;
	
	private String mfa;
	
	public Context() {
	}
	
	public Context(String requestId) {
		this.requestId = requestId;
	}
	
	
	@Override
	public String toString() {
		return String.format(
				"projectId=%s, clientType=%s, userEmail=%s, languageTag=%s, principalType=",
				projectId,
				clientType,
				userEmail,
				language,
				getPrincipalType());
	}
	
	public String getPrincipalType() {
		return StringUtils.isEmpty(roleId) ? PrincipalType.USER : PrincipalType.ROLE;
	}
}
