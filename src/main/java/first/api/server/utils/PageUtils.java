package first.api.server.utils;

import first.api.server.model.SearchCriteria;

public class PageUtils {
	
	/**
	 * 페이지목록 조회를 위한 필터를 설정한다
	 * @param searchCriteria 페이지목록 필터조건
	 */
	public static final void setPageable(SearchCriteria searchCriteria) {
		
		// limit가 1보다 작은면 전체 목록 조회
		if(searchCriteria.getSize() > 0) {
			com.github.pagehelper.PageHelper.startPage(searchCriteria.getPage() , searchCriteria.getSize());
		}
	}
}
