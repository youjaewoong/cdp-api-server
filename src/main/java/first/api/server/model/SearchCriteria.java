package first.api.server.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class SearchCriteria {


	@Min(0)
	private int page = 0;
	

	@Min(-1)
	@Max(10000)
	private int size = 10;
	
	
	// 현재 페이지
	private int pageNum ;
	// 페이지당 수량
	private int pageSize ;

}
