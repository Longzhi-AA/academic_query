package org.acq.lz.service;

import org.acq.lz.service.query.CommonSearchResult;
import org.acq.lz.vo.QueryReqeust;

import java.util.List;

public interface ISearchProvider {

    List<CommonSearchResult> search(QueryReqeust request);
}
