package first.api.server.model;

import java.util.ArrayList;
import java.util.List;

import com.github.pagehelper.Page;

import lombok.Getter;
import lombok.Setter;


/**
 * 페이지 기능
 */
@Getter
@Setter
public class PageResponse<T> extends SearchCriteria {

	private long total = 0;
	
	private List<T> contents = new ArrayList<T>();
	
	
	public PageResponse(List<T> contents, SearchCriteria searchCriteria) {
		
		Page<T> page = (Page<T>) contents;
		
		// 요청 page값이 더 클경우 빈값 처리
		if (searchCriteria.getPage() > page.getPageNum()) {
			contents = this.contents;
			this.setPage(searchCriteria.getPage());
			this.setEnablePage(false);
		} else {
			this.contents = contents;
			this.setPage(page.getPageNum());
		}
		
		this.setTotal(page.getTotal());
		this.setSize(page.getPageSize());
	}

}