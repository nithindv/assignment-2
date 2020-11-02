package egov.searcher.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import egov.searcher.service.SearcherService;
import egov.searcher.web.models.SearchRequest;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class SearcherController {

    private ObjectMapper objectMapper;
    private SearcherService searcherService;

    @Autowired
    public SearcherController(ObjectMapper objectMapper, SearcherService searcherService) {
        this.searcherService = searcherService;
        this.objectMapper = objectMapper;
    }

    @RequestMapping(value = "/{service}/{mapping}/_search", method = RequestMethod.POST)
    public ResponseEntity<?> upload(@PathVariable("service") String service,
                                                       @PathVariable("mapping") String module,
                                                       @RequestBody @Valid final SearchRequest searchRequest) {
        try {
            List<Map<String, Object>> results = searcherService.search(service, module,
                    searchRequest.getSearchParams());
            return ResponseEntity.ok().body(results);
        } catch (Exception e) {
            log.error("Error in getting Report data ver1", e);
            throw new CustomException("ERROR_IN_RETRIEVING_REPORT_DATA", e.getMessage());
        }
    }
}
