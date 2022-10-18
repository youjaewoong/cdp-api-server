package first.api.server.exception.error;


import com.fasterxml.jackson.core.JsonLocation;

import lombok.Getter;

@Getter
public class RestApiJsonError extends RestApiError{
	
	private Location locationl;
	
	public void setJsonLocation(JsonLocation jsonLocation) {
		locationl = new Location(jsonLocation);
	}
	
	@Getter
	public static class Location {
		private int lineNr;
		
		private int columnNr;
		
		public Location(JsonLocation jsonLocation) {
			lineNr = jsonLocation.getLineNr();
			columnNr = jsonLocation.getColumnNr();
		}
	}
}
