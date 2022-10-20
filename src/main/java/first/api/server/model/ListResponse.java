package first.api.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 페이지가 필요없는 조회
 */
@Getter
@Setter
public class ListResponse<T>  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private long total = 0;

	private List<T> contents = new ArrayList<>();
	
	
	public ListResponse(List<T> list) {
		this.total = list.size();
		this.contents = list;
	}
}
