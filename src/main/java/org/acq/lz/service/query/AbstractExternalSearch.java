package org.acq.lz.service.query;


import org.acq.lz.service.SearchEngin;

import java.util.List;

public abstract class AbstractExternalSearch<T> implements SearchEngin {

    @Override
    public List<CommonSearchResult> search(String query) {
        T result = doSearch(query, false);
        return buildSearchResult(result);
    }

    protected abstract T doSearch(String query, boolean isAuthorRequest);


    protected abstract List<CommonSearchResult> buildSearchResult(T resultVO);

    protected String getLabel(){
        return "";
    };
}
