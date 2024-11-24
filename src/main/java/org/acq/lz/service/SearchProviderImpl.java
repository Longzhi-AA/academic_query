package org.acq.lz.service;

import cn.hutool.core.util.StrUtil;
import org.acq.lz.config.enums.QueryProviderEnum;
import org.acq.lz.config.enums.QueryTypeEnum;
import org.acq.lz.service.query.CommonSearchResult;
import org.acq.lz.utils.SpringContextUtil;
import org.acq.lz.vo.QueryReqeust;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service("ISearchProviderImpl")
public class SearchProviderImpl implements ISearchProvider{

    private static final Logger logger = LogManager.getLogger("SearchProviderImpl");

    private static final Long TIME_OUT = 200 * 1000L;

    @Autowired
    @Qualifier("SpringerSearchJson")
    private SearchEngin springSearch;

    @Autowired
    @Qualifier("ArxivSearchJson")
    private SearchEngin arxivSearch;


    @Override
    public List<CommonSearchResult> search(QueryReqeust request) {
        if (request == null || StrUtil.isBlank(request.getQuery())){
            return null;
        }

        String queryType = request.getQueryType();
        if (StrUtil.isBlank(queryType) || QueryTypeEnum.ALL.name().equals(queryType)){
            return doMuiltQuery(request.getQuery());
        }
        SearchEngin searchEngin = getQueryProvider(queryType);

        return searchEngin.search(request.getQuery());
    }

    private SearchEngin getQueryProvider(String queryType){
        if (QueryTypeEnum.SPRINGER.name().equals(queryType)){
            return springSearch;
        }
        if (QueryTypeEnum.ARXIV.name().equals(queryType)) {
            return arxivSearch;
        }
        return springSearch;
    }

    private static ThreadPoolTaskExecutor getExecutor(String executorName){
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) SpringContextUtil.getBean(executorName);
        if (executor == null){
            logger.error("can not get ThreadPoolTaskExecutor thru {}", executorName);
            throw new RuntimeException();
        }
        return executor;
    }

    private List<CommonSearchResult> doMuiltQuery(String query){
        List<CompletableFuture<List<CommonSearchResult>>> futureList = new ArrayList<>();
        for (QueryProviderEnum provider : QueryProviderEnum.values()){
            SearchEngin searchEngin = getQueryProvider(provider.getQueryType().name());
            ThreadPoolTaskExecutor executor = getExecutor(provider.getExecutor());

            CompletableFuture<List<CommonSearchResult>> future = CompletableFuture.supplyAsync(() -> searchEngin.search(query),executor);
            futureList.add(future);
        }

        if (futureList.isEmpty()){
            return null;
        }
        long startTm = System.currentTimeMillis();
        futureList.stream().map(CompletableFuture::join);
        int unCompletedCnt = futureList.size();
        List<CommonSearchResult> resultList = new ArrayList<>();
        while (unCompletedCnt != 0){
            List<Integer> indexs = new ArrayList<>();
            for (int i = 0; i < unCompletedCnt; i++){
                CompletableFuture<List<CommonSearchResult>> future = futureList.get(i);
                if (System.currentTimeMillis() - startTm > TIME_OUT){
                    unCompletedCnt = 0;
                }
                if (!future.isDone()){
                    continue;
                }
                try {
                    List<CommonSearchResult> results = future.get();
                    indexs.add(i);
                    resultList.addAll(results);
                }catch (Exception e){
                    logger.error("get thread search result error.", e);
                }

            }
            for (int i : indexs){
                futureList.remove(i);
            }
            unCompletedCnt -= indexs.size();
        }

        return resultList;
    }


}
