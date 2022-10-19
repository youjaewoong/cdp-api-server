package first.api.server.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import first.api.server.api.mapper.SampleMapper;
import first.api.server.api.model.SampleRequest;
import first.api.server.api.model.SampleResponse;
import first.api.server.model.PageResponse;
import first.api.server.utils.PageUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SampleService {

	private final SampleMapper sampleMapper;

	public PageResponse<SampleResponse> selectSample(SampleRequest sampleRequest) {
		PageUtils.setPageable(sampleRequest);
		List<SampleResponse> response = sampleMapper.selectSample(sampleRequest);
		return new PageResponse<SampleResponse>(response, sampleRequest);
	}
}
