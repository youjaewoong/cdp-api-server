package first.api.server.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import first.api.server.model.SampleResponse;
import first.api.server.service.SampleService;

@RestController
@Validated
public class SampleController {

	@Autowired
    private SampleService sampleService;
	
    @GetMapping("sample")
    public List<SampleResponse> selectSample(@Valid @NotBlank String test) {
		
        return sampleService.selectSample();
    }
	
}
