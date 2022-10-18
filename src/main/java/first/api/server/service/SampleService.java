package first.api.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import first.api.server.dao.SampleMapper;
import first.api.server.model.SampleResponse;

@Service
public class SampleService {

	@Autowired
	private SampleMapper sampleMapper;

	public List<SampleResponse> selectSample() {
		return sampleMapper.selectSample();
	}
}
