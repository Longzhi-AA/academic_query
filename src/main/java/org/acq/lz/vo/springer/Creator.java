package org.acq.lz.vo.springer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Creator {

    @JsonProperty("ORCID")
    private String ORCID;

    private String creator;
}
