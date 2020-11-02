package egov.searcher.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearcherConfig {


    @JsonProperty("service")
    private String service = null;

    @JsonProperty("mappings")
    private List<Mapping> mappings = null;
}
