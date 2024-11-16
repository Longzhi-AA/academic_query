package org.acq.lz.vo.arxiv;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Entry {

    @JsonProperty("id")
    private String url;

    @JsonProperty("published")
    private String publicationDate;

    @JsonProperty("title")
    private String title;

    @JsonProperty("summary")
    private String abstracts;

    @JsonProperty("author")
    private List<Author> authors;
}
