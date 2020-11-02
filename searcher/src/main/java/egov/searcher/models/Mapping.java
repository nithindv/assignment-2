package egov.searcher.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mustachejava.Mustache;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Mapping {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("version")
    private String version = null;
    
    @JsonProperty("description")
    private String description = null;

    @JsonProperty("params")
    private List<Param> params = null;

    @JsonProperty("query")
    private String query = null;

    @JsonIgnore
    private Mustache mustacheTemplate = null;

}
