package org.acq.lz.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowseJsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(BrowseJsonUtils.class);

    public static String getJsonResponse(String url){
        HttpRequest request = HttpUtil.createGet(url);
        HttpResponse response = request.execute();
        if (response == null){
            logger.error("cannot get response from {}", url);
        }
        if (response.getStatus() != 200){
            logger.error("get error code {} from response.", response.getStatus());
        }
        return response.body();
    }
}
