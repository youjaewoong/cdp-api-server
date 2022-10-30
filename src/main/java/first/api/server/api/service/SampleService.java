package first.api.server.api.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import first.api.server.api.mapper.SampleMapper;
import first.api.server.api.model.SampleRequest;
import first.api.server.api.model.SampleResponse;
import first.api.server.model.ListResponse;
import first.api.server.model.PageResponse;
import first.api.server.utils.PageUtils;
import lombok.RequiredArgsConstructor;

/**
 * {@link first.api.server.model.PageResponse} page list 
 * {@link first.api.server.model.ListResponse} total list
 */
@Service
@RequiredArgsConstructor
public class SampleService {

	private final SampleMapper sampleMapper;

	
	public PageResponse<SampleResponse> selectSample(SampleRequest sampleRequest) {
		
		// 페이징 처리 시
		PageUtils.setPageable(sampleRequest);
		List<SampleResponse> response = sampleMapper.selectSample(sampleRequest);
		return new PageResponse<SampleResponse>(response, sampleRequest);
	}

	
	@Cacheable(value = "selectSampleSample")
	public ListResponse<SampleResponse> selectSampleSample() {
		
		return new ListResponse<SampleResponse>(sampleMapper.selectSample());
	}
}
