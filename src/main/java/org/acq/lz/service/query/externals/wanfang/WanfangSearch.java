package org.acq.lz.service.query.externals.wanfang;

import org.acq.lz.service.query.AbstractExternalSearch;
import org.acq.lz.service.query.CommonSearchResult;
import org.acq.lz.vo.wanfang.WanFangResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("WanfangSearch")
public class WanfangSearch extends AbstractExternalSearch<WanFangResult> {

    private static final Logger logger = LogManager.getLogger(WanfangSearch.class);

    private static final String url = "https://s.wanfangdata.com.cn/SearchService.SearchService/search?";



    @Override
    protected String getLabel() {
        return "wanfang";
    }

    @Override
    protected WanFangResult doSearch(String query, boolean isAuthorRequest) {
        return null;
    }

    @Override
    protected List<CommonSearchResult> buildSearchResult(WanFangResult resultVO) {
        return null;
    }
}
