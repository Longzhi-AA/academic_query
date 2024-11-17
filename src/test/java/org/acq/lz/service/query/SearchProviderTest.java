package org.acq.lz.service.query;


import org.acq.lz.config.ThreadPoolManagment;
import org.acq.lz.service.ISearchProvider;
import org.acq.lz.service.SearchEngin;
import org.acq.lz.service.SearchProviderImpl;
import org.acq.lz.service.query.externals.arxiv.ArxivSearchJson;
import org.acq.lz.service.query.externals.springer.SpringerSearchJson;
import org.acq.lz.utils.SpringContextUtil;
import org.acq.lz.vo.QueryReqeust;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = {SearchProviderImpl.class, SpringContextUtil.class, SpringerSearchJson.class, ArxivSearchJson.class, ThreadPoolManagment.class})
public class SearchProviderTest {


    @Autowired
    @Qualifier("ISearchProviderImpl")
    private ISearchProvider searchProvider;

    @Autowired
    @Qualifier("SpringerSearchJson")
    private SearchEngin springSearch;

    @Autowired
    @Qualifier("ArxivSearchJson")
    private SearchEngin arxivSearch;

    @Test
    void testSingleSearch(){
        QueryReqeust request = new QueryReqeust();
        request.setQuery("AI");
        request.setQueryType("ARXIV");

        List<CommonSearchResult> results = searchProvider.search(request);
        Assertions.assertNotNull(results);
        Assertions.assertEquals(10, results.size());
    }

    @Test
    void testMuiltSearch(){
        QueryReqeust request = new QueryReqeust();
        request.setQuery("AI");
        request.setQueryType("");
        List<CommonSearchResult> results = searchProvider.search(request);
        Assertions.assertNotNull(results);
        Assertions.assertEquals(30, results.size());
    }


}
