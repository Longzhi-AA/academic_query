package org.acq.lz.config.enums;

public enum QueryProviderEnum {
    SpringerSearch("SpringerSearchJson", QueryTypeEnum.SPRINGER, "SpringerSearchThreads"),
    ArxivSearch("ArxivSearchJson", QueryTypeEnum.ARXIV, "ArxivSearchThreads"),
    ;

    private String provideName;

    private QueryTypeEnum queryType;

    private String executor;

    QueryProviderEnum(String provideName, QueryTypeEnum queryType, String executor) {
        this.provideName = provideName;
        this.queryType = queryType;
        this.executor = executor;
    }

    public String getProvideName() {
        return provideName;
    }

    public QueryTypeEnum getQueryType() {
        return queryType;
    }

    public String getExecutor() {
        return executor;
    }
}
