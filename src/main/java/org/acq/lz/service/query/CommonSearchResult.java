package org.acq.lz.service.query;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommonSearchResult {

    private String title;

    private List<String> authors;

    private String abstracts;

    private String url;

    private List<String> keyword;

    private List<String> subjects;

    private String publicationDate;

    private String source;
}
