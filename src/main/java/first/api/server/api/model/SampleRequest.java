package first.api.server.api.model;

import first.api.server.model.SearchCriteria;
import lombok.Getter;

@Getter
public class SampleRequest extends SearchCriteria {

	private String id;
	
	private String name;

}
