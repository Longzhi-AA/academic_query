package org.acq.lz.service.query;


import org.acq.lz.service.query.externals.springer.SpringerSearch;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {SpringerSearch.class, ExternalSearchProvider.class})
public class ExternalSearchTest {

//    @Autowired
//    @Qualifier("SpringerSearch")
//    private ExternalSearchProvider springerSearch;

    @Test
    public void testSpringerSearch(){
        SpringerSearch springerSearch = new SpringerSearch();
        ICustomSearchResult result = springerSearch.search("spring", false);
        System.out.println(result.getTotalResults());


    }
}
