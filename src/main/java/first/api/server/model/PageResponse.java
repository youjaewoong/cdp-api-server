package first.api.server.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


/**
 * 페이지 포함 조회
 */
@Getter
@Setter
public class PageResponse<T> extends SearchCriteria {

	private long total = 0;
	
	private List<T> content = new ArrayList<T>();
	
	private List<String> sortableFields = new ArrayList<>();
	
	
	public PageResponse(List<T> content, long total, int page, int size) {
		this.total = total;
		this.content = content;
		this.setPage(page);
		this.setSize(size);
	}
	
	
	public PageResponse(List<T> content, SearchCriteria searchCriteria) {
		this.content = content;
		this.setPage(searchCriteria.getPage());
		this.setSize(searchCriteria.getSize());
	}

}