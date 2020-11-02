package egov.searcher.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import egov.searcher.models.Mapping;
import egov.searcher.models.Param;
import egov.searcher.models.SearcherConfig;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Slf4j
public class SearcherService {

    private final Map<String, SearcherConfig> configMap;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private Map<String, Object> extensions = new HashMap<>();

    @Autowired
    public SearcherService(Map<String, SearcherConfig> configMap) {
        this.configMap = configMap;
    }

    public List<Map<String, Object>> search(String service, String template, Map<String, Object> searchParams){
        try {
            Mapping mapping = getMappingForTemplate(service, template);

            Map<String, Object> scopes = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

            if(mapping.getParams() != null) {
                for (Param parameter : mapping.getParams()) {
                    if (searchParams.containsKey(parameter.getName()))
                        scopes.put(parameter.getName().trim(), searchParams.get(parameter.getName()));
                }
            }

            StringWriter stringWriter = new StringWriter();
            mapping.getMustacheTemplate().execute(stringWriter, scopes);
            String query = stringWriter.toString();
            log.info(query);

            List<Map<String, Object>> maps = null;

            MapSqlParameterSource params =  new MapSqlParameterSource(scopes);
            log.info("final query:" + query);
            try {

                maps = namedParameterJdbcTemplate.queryForList(query, params);

            } catch (DataAccessResourceFailureException ex) {
                log.info("Query Execution Failed Due To Timeout: ", ex);
                PSQLException cause = (PSQLException) ex.getCause();
                if (cause != null && cause.getSQLState().equals("57014")) {
                    throw new CustomException("QUERY_EXECUTION_TIMEOUT",
                            "Query failed, as it took more than x seconds to execute");
                } else {
                    throw ex;
                }
            } catch (Exception e) {
                log.info("Query Execution Failed: ", e);
                throw e;
            }
            return maps;
        }finally {
        }
    }


    private Mapping getMappingForTemplate(String service, String template){
        SearcherConfig config = configMap.get(service);
        if(config != null) {
            for (Mapping mapping : config.getMappings()) {
                if (mapping.getId().equalsIgnoreCase(template))
                    return mapping;
            }
        }
        throw new CustomException("TEMPLATE_NOT_FOUND", "No template configured for given service and " +
                "template!");
    }

}
