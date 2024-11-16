package org.acq.lz.controller;


import org.acq.lz.service.query.CommonSearchResult;
import org.acq.lz.service.query.SearchEngin;
import org.acq.lz.vo.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CommonQueryConnectorImpl implements ICommonQueryConnector {

    private static final Logger logger = LoggerFactory.getLogger(CommonQueryConnectorImpl.class);

    @Autowired
    @Qualifier(("SpringerSearchJson"))
    private SearchEngin springSearch;

    @Override
    @ResponseBody
    public RestResponse<List<CommonSearchResult>> querySpringner(@RequestBody String query) {
        if (logger.isInfoEnabled()){
            logger.info("querySpringner received quest:{}", query);
        }
        RestResponse response = new RestResponse<>();
        List<CommonSearchResult> data = null;
        try {
            data = springSearch.search(query);
        }catch (Exception e){
            logger.error("springSearch error.", e);
            return response.buildFailResponse("-1", e.getMessage());
        }
        return response.buildSuccessResponse(data);

    }
}
