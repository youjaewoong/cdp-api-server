package first.api.server.exception.error;

public interface PlatformErrorCode {

	ErrorCode InvalidQueryParmeterValue = 
			new ErrorCode("InvalidQueryParameterValue",
					"PLATFORM-CMN-000001",
					"Invalid query parameter value", 
					400);
	
	ErrorCode InvalidHeaderVlaue = 
			new ErrorCode("InvalidHeaderValue",
					"PLATFORM-CMN-000002",
					"Invalid header value", 
					400);
	
	ErrorCode InvalidInput = 
			new ErrorCode("InvalidInputData",
					"PLATFORM-CMN-000003",
					"Invalid input data", 
					400);
	
	ErrorCode ThresholdExceeded = 
			new ErrorCode("ThresholdOver",
					"PLATFORM-CMN-000004",
					"Request results exceeding limt", 
					400);
	
	ErrorCode Unauthorized = 
			new ErrorCode("Unauthorized",
					"PLATFORM-CMN-000005",
					"Unauthorized", 
					401);
	
	ErrorCode InvalidIdToken = 
			new ErrorCode("InvalidIdToken",
					"PLATFORM-CMN-000006",
					"Invalid Id-Token value", 
					401);
	
	ErrorCode SessionExpired = 
			new ErrorCode("SessionExpired",
					"PLATFORM-CMN-000007",
					"Session Expired", 
					401);
	
	ErrorCode SessionDuplicated = 
			new ErrorCode("SessionDuplicated",
					"PLATFORM-CMN-000008",
					"Session terminated due to login from another device", 
					401);
	
	ErrorCode LoginRequired = 
			new ErrorCode("LoginRequired",
					"PLATFORM-CMN-000009",
					"Login Required", 
					401);
	
	ErrorCode RoleExpired = 
			new ErrorCode("RoleExpired",
					"PLATFORM-CMN-000010",
					"Role Expired", 
					403);
	
	ErrorCode InvalidResource = 
			new ErrorCode("InvalidResource",
					"PLATFORM-CMN-000011",
					"Invalid Resource", 
					403);
	
	ErrorCode InvalidProject = 
			new ErrorCode("InvalidProject",
					"PLATFORM-CMN-000012",
					"Invalid Project", 
					403);
	
	ErrorCode AccessDenied = 
			new ErrorCode("AccessDenied",
					"PLATFORM-CMN-000013",
					"Access Denied", 
					403);
	
	ErrorCode BlockedMethod = 
			new ErrorCode("BlockedMethod",
					"PLATFORM-CMN-000014",
					"Method is Blocked", 
					404);
	
	ErrorCode ResourceNotFound = 
			new ErrorCode("ResourceNotFound",
					"PLATFORM-CMN-000015",
					"Resource not found", 
					404);

	ErrorCode ApiNotFound = 
			new ErrorCode("ApiNotFound",
					"PLATFORM-CMN-000001",
					"Api not found", 
					404);
	
	ErrorCode ApiGroupFound = 
			new ErrorCode("ApiGroupFound",
					"PLATFORM-CMN-000016",
					"Api group found", 
					404);
	
	ErrorCode ResourceAlreadyExists = 
			new ErrorCode("ResourceAlreadyExists",
					"PLATFORM-CMN-000017",
					"Resource already exists", 
					409);
	
	ErrorCode DataConflicted = 
			new ErrorCode("DataConflicted",
					"PLATFORM-CMN-000018",
					"Data conflicted", 
					409);
	
	ErrorCode RoleAlreadyAssigned = 
			new ErrorCode("RoleAlreadyAssigned",
					"PLATFORM-CMN-000019",
					"Role Already Assigned", 
					500);
	
	ErrorCode AssumeRoleFailed = 
			new ErrorCode("AssumeRoleFailed",
					"PLATFORM-CMN-000020",
					"Assume Role Failed", 
					500);
	
	ErrorCode ApiConnectionError = 
			new ErrorCode("ApiConnectionError",
					"PLATFORM-CMN-000021",
					"Internal server error", 
					500);
	
	ErrorCode ApiInvokeError = 
			new ErrorCode("ApiInvokeError",
					"PLATFORM-CMN-000022",
					"API Invoke Error", 
					500);
	
	ErrorCode CmpConfigurationError = 
			new ErrorCode("CmpConfigurationError",
					"PLATFORM-CMN-000023",
					"CmpConfiguration error", 
					500);
	
	ErrorCode DataAccessError = 
			new ErrorCode("DataAccessError",
					"PLATFORM-CMN-000024",
					"Data access error", 
					500);
	
	ErrorCode InternalServerError = 
			new ErrorCode("InternalServerError",
					"PLATFORM-CMN-000025",
					"Internal server error", 
					500);
	
}
