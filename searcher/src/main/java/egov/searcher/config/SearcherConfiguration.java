package egov.searcher.config;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import egov.searcher.models.Mapping;
import egov.searcher.models.SearcherConfig;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class SearcherConfiguration {

    @Value("${config.repo.path}")
    private String configPaths;

    @Autowired
    private ResourceLoader resourceLoader;

    private MustacheFactory mf = new DefaultMustacheFactory();

    @PostConstruct
    @Bean
    public Map<String, SearcherConfig> init(){
        Map<String, SearcherConfig> searcherConfigMap = new HashMap<>();
        Map<String, String> errorMap = new HashMap<>();

        log.info("====================== SEARCHER SERVICE ======================");
        log.info("LOADING CONFIGS: "+ configPaths);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        String[] yamlUrls = configPaths.split(",");
        for (String configPath : yamlUrls) {
            try {
                log.info("Attempting to load config: "+configPath);
                Resource resource = resourceLoader.getResource(configPath);
                SearcherConfig config = mapper.readValue(resource.getInputStream(), SearcherConfig.class);

                for(Mapping mapping : config.getMappings()){
                    Mustache mustache = mf.compile(new StringReader(mapping.getQuery()), mapping.getId());
                    mapping.setMustacheTemplate(mustache);
                }
                searcherConfigMap.put(config.getService(), config);
            }
            catch (JsonParseException e){
                log.error("Failed to parse yaml file: " + configPath, e);
                errorMap.put("PARSE_FAILED", configPath);
            }
            catch (IOException e) {
                log.error("Exception while fetching service map for: " + configPath, e);
                errorMap.put("FAILED_TO_FETCH_FILE", configPath);
            }
        }

        if( !  errorMap.isEmpty())
            throw new CustomException(errorMap);
        else
            log.info("====================== CONFIGS LOADED SUCCESSFULLY! ====================== ");

        return searcherConfigMap;
    }
}
