package org.acq.lz.controller;


import cn.hutool.core.util.StrUtil;
import org.acq.lz.config.enums.QueryTypeEnum;
import org.acq.lz.service.ISearchProvider;
import org.acq.lz.service.query.CommonSearchResult;
import org.acq.lz.service.SearchEngin;
import org.acq.lz.vo.QueryReqeust;
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
    @Qualifier("ISearchProviderImpl")
    private ISearchProvider searchProvider;

    @Override
    @ResponseBody
    public RestResponse<List<CommonSearchResult>> querySpringner(@RequestBody QueryReqeust query) {
        if (logger.isInfoEnabled()){
            logger.info("received query quest:{}", query);
        }
        RestResponse response = new RestResponse<>();
        List<CommonSearchResult> data = null;

        if (query == null || StrUtil.isBlank(query.getQuery())){
            return response.buildSuccessResponse(null);
        }

        try {
            data = searchProvider.search(query);
        }catch (Exception e){
            logger.error("springSearch error.", e);
            return response.buildFailResponse("-1", e.getMessage());
        }
        return response.buildSuccessResponse(data);

    }
}
