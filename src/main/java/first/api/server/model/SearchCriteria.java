package first.api.server.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public abstract class SearchCriteria {

	@Min(1)
	private int page = 1;

	@Min(-1)
	@Max(10000)
	private int size = 10;
	
	
	private boolean enablePage = true;

}
