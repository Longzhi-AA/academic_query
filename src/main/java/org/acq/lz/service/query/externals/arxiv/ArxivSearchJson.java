package org.acq.lz.service.query.externals.arxiv;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import org.acq.lz.service.query.AbstractExternalSearch;
import org.acq.lz.service.query.CommonSearchResult;
import org.acq.lz.service.query.externals.springer.SpringerSearchJson;
import org.acq.lz.utils.BrowseUtils;
import org.acq.lz.utils.FastJsonUtil;
import org.acq.lz.vo.arxiv.ArxivResult;
import org.acq.lz.vo.arxiv.Author;
import org.acq.lz.vo.arxiv.Entry;
import org.acq.lz.vo.arxiv.Feed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("ArxivSearchJson")
public class ArxivSearchJson extends AbstractExternalSearch<ArxivResult> {

    private static final Logger logger = LoggerFactory.getLogger(SpringerSearchJson.class);
    private static final String urlStart = "http://export.arxiv.org/api/query?search_query=all:";
    private static final String urlEnd = "&start=0&max_results=";
    private static final String pageSize()
    {
        return "10";
    }

    private static final String apiKey = "";

    @Override
    protected ArxivResult doSearch(String query, boolean isAuthorRequest) {
        String content = null;
        ArxivResult resultVO = null;
        try {
            String url = urlStart + URLEncoder.encode(query, "UTF-8") + urlEnd + pageSize();
            content = BrowseUtils.getResponse(url);
        }catch (Exception e){
            logger.error("failed to getconnetion to server.", e);
        }


        JSONObject result = FastJsonUtil.xmlToFastJson(content);

        if (StrUtil.isNotBlank(result.toJSONString())) {
            resultVO = FastJsonUtil.jsonStrToEntity(result.toJSONString(), ArxivResult.class);
        }
        return resultVO;
    }


    @Override
    protected List<CommonSearchResult> buildSearchResult(ArxivResult resultVO) {
        List<CommonSearchResult> results = new ArrayList<CommonSearchResult>();
        if (resultVO == null) {
            return results;
        }
        Feed records = resultVO.getFeed();
        if (records == null || records.getEntries().isEmpty()) {
            return results;
        }

        for (Entry record : records.getEntries()) {
            if (record == null) {
                continue;
            }
            CommonSearchResult result = new CommonSearchResult();
            result.setTitle(record.getTitle());
            List<Author> creators = record.getAuthors();
            result.setAuthors(creators.stream().map(Author::getName).collect(Collectors.toList()));
            result.setPublicationDate(record.getPublicationDate());
            result.setAbstracts(record.getAbstracts());
            result.setKeyword(null);
            result.setUrl(record.getUrl());
            result.setSubjects(null);

            results.add(result);
        }
        return results;

    }
}
