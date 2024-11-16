package org.acq.lz.service.query;


import org.acq.lz.service.query.externals.arxiv.ArxivSearchJson;
import org.acq.lz.service.query.externals.springer.SpringerSearchJson;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = {SpringerSearchJson.class})
public class ExternalSearchTest {

//    @Autowired
//    @Qualifier("SpringerSearchJson")
//    private SpringerSearchJson springerSearch;

    @Test
    public void testSpringerSearch(){
        SpringerSearchJson springerSearch = new SpringerSearchJson();
        List<CommonSearchResult> result = springerSearch.searchThruApi("spring", false);
        System.out.println(result);


    }

    @Test
    public void testArxivSearch(){
        ArxivSearchJson arxivSearchJson = new ArxivSearchJson();
        List<CommonSearchResult> result = arxivSearchJson.searchThruApi("AI", false);
        System.out.println(result);
    }
}
