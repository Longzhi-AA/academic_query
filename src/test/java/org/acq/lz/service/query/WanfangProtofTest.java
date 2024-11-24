package org.acq.lz.service.query;

import com.google.common.primitives.Bytes;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import org.acq.lz.proto.SearchServiceFactory;
import org.acq.lz.utils.BrowseUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WanfangProtofTest {

    @Test
    void queryTest() throws InvalidProtocolBufferException {

        SearchServiceFactory.SearchService.CommonRequest.Builder comReqBuider = SearchServiceFactory.SearchService.CommonRequest.newBuilder();
        SearchServiceFactory.SearchService.SearchScope searchScope = SearchServiceFactory.SearchService.SearchScope.A;
        SearchServiceFactory.SearchService.SearchFilter searchFilter = SearchServiceFactory.SearchService.SearchFilter.B;


        comReqBuider.setCurrentPage(1);
        comReqBuider.setPageSize(20);
        comReqBuider.setSearchWord("音乐生成");
        comReqBuider.setSearchType("paper");
        comReqBuider.setSearchScope(searchScope);
        SearchServiceFactory.SearchService.CommonRequest comReq = comReqBuider.build();

        ByteString reqString = comReq.toByteString();


        System.out.println(comReq.toByteString());

        byte[] reqBytes = comReq.toByteArray();

        ByteString headString = ByteString.copyFrom(Bytes.concat(new byte[0], new byte[0], new byte[0], new byte[comReq.getSerializedSize()]));

        ByteString data = headString.concat(reqString);

        System.out.println(data);

        SearchServiceFactory.SearchService.CommonRequest request = SearchServiceFactory.SearchService.CommonRequest.parseFrom(reqBytes);

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("content-type", "application/grpc-web+proto");
        headerMap.put("origin", "http://s.wanfangdata.com.cn");
        headerMap.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        headerMap.put("x-grpc-web", "1");
        headerMap.put("x-user-agent",  "grpc-web-javascript/0.1");

        String url = "https://s.wanfangdata.com.cn/SearchService.SearchService/search";

        byte[] respData = BrowseUtils.sendFormPost(url, headerMap, data.toByteArray());

        System.out.println(respData);

    }
}
