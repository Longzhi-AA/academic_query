package org.acq.lz.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryReqeust {

    private String query;

    private String queryType;

    @Override
    public String toString() {
        return "QueryReqeust{" +
                "query='" + query + '\'' +
                ", queryType='" + queryType + '\'' +
                '}';
    }
}
