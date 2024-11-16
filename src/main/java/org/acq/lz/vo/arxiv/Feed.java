package org.acq.lz.vo.arxiv;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Feed {

    @JsonProperty("entry")
    private List<Entry> entries;
}
