package first.api.server.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import first.api.server.model.SampleResponse;

@Mapper
public interface SampleMapper {

	List<SampleResponse> selectSample();
	
}