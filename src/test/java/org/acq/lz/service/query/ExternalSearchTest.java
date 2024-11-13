package org.acq.lz.service.query;


import org.acq.lz.service.query.externals.springer.SpringerSearchJson;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {SpringerSearchJson.class, ExternalSearchProvider.class})
public class ExternalSearchTest {

//    @Autowired
//    @Qualifier("SpringerSearch")
//    private ExternalSearchProvider springerSearch;

    @Test
    public void testSpringerSearch(){
        SpringerSearchJson springerSearch = new SpringerSearchJson();
        ICustomSearchResult result = springerSearch.search("spring", false);
        System.out.println(result.getTotalResults());


    }
}
