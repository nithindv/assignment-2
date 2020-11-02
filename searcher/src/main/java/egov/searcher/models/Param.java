package egov.searcher.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Param {

    private String name = null;
    private TYPE type = null;

    public enum TYPE{
        STRING,EPOCH
    }

}
