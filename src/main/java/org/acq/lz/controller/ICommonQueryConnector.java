package org.acq.lz.controller;

import org.acq.lz.service.query.CommonSearchResult;
import org.acq.lz.vo.QueryReqeust;
import org.acq.lz.vo.RestResponse;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public interface ICommonQueryConnector {

    @PostMapping("/v1.0/query/querySpringner")
    RestResponse<List<CommonSearchResult>> querySpringner(QueryReqeust query);
}
