package first.api.server.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 페이지가 필요없는 조회
 */
@Getter
@Setter
public class ListResponse<T> {
	
	private long totalCount = 0;

	private List<T> contents = new ArrayList<>();
	
	public ListResponse() {
		super();
	}
	
	public ListResponse(List<T> list) {
		this.totalCount = list.size();
		this.contents = list;
	}
}
