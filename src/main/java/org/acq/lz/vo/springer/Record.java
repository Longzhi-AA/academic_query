package org.acq.lz.vo.springer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Record {

    private String contentType;

    private String identifier;

    private String title;

    private List<Creator> creators;

    private String publicationDate;

    @JsonProperty("abstract")
    private String abstracts;

    private List<UrlValue> url;

    private List<String> keyword;

    private List<String> subjects;
}
