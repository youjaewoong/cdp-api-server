package first.api.server.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import first.api.server.api.model.SampleRequest;
import first.api.server.api.model.SampleResponse;


@Mapper
public interface SampleMapper {

	Page<SampleResponse> selectSample(SampleRequest sampleRequest);

	List<SampleResponse> selectSample();
	
}