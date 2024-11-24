package org.acq.lz.utils;

import cn.hutool.http.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BrowseUtils {

    private static final Logger logger = LogManager.getLogger(BrowseUtils.class);

    public static String getResponse(String url){
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



    public static byte[] sendFormPost(String url, Map<String, String> headerMap, byte[] data) {

        //创建post请求对象
        HttpPost post = new HttpPost(url);
        try {
            //创建参数集合
            List<BasicNameValuePair> list = new ArrayList<>();
            //添加参数
            ByteArrayEntity entity = new ByteArrayEntity(data);
            //把参数放入请求对象，，post发送的参数list，指定格式
            post.setEntity(entity);

            if (headerMap != null) {
                for (String str : headerMap.keySet()
                ) {
                    post.addHeader(str, headerMap.get(str));
                }
            }

            CloseableHttpClient client = HttpClients.createDefault();
            //启动执行请求，并获得返回值
            CloseableHttpResponse response = client.execute(post);
            //得到返回的entity对象
            HttpEntity responseEntity = response.getEntity();
            //把实体对象转换为string
            byte[] respData = EntityUtils.toByteArray(responseEntity);
            //为防止频繁调用一个接口导致接口爆掉，每次调用完成后停留100毫秒
            Thread.sleep(100);
            //返回内容
            return respData;
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }
}
