package org.acq.lz.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.*;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * fastjson工具类(需要引入fastjson和dom4j的pom依赖)
 * @author hulei
 * @Date 2023/3/15 17:18
 */
public class FastJsonUtil {

    /**
     * xml转fastjson
     * @param xml 待转换数据xml字符串
     * @return JSONObject对象
     */
    public static JSONObject xmlToFastJson(String xml){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            Document document = DocumentHelper.parseText(xml);
            // 获取根节点元素对象
            Element root = document.getRootElement();
            iterateNodes(root, jsonObject);
        }catch (Exception e){
            return jsonObject;
        }
        return jsonObject;
    }

    /**
     * fastjson转xml
     * @param jsonStr 待转换json字符串
     * @param escape  是否忽略特殊字符(即特殊字符转xml后追加CDATA)
     * @return
     */
    public static String fastJsonToXml(String jsonStr,Boolean escape) {
        try {
            StringBuffer buffer = new StringBuffer();
            //带顺序
            JSONObject json = JSONObject.parseObject(jsonStr);
            jsonToXmlStr(json,buffer, escape != null && escape);
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * object转JSONObject
     * @param t
     * @param <T>
     * @return
     */
    public static <T> JSONObject objectToJSONObject(T  t){
        return  JSONObject.parseObject(JSON.toJSONString(t));
    }

    /**
     * json字符串转实体对象
     * @param jsonString
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonStrToEntity(String jsonString,Class<T> clazz){
        return JSONObject.parseObject(jsonString,clazz);
    }

    public static void iterateNodes(Element node, JSONObject json) {
        // 获取当前元素的名称
        String nodeName = node.getName();
        // 判断已遍历的JSON中是否已经有了该元素的名称
        if (json.containsKey(nodeName)) {
            // 该元素在同级下有多个
            Object Object = json.get(nodeName);
            JSONArray array;
            if (Object instanceof JSONArray) {
                array = (JSONArray) Object;
            }
            else {
                array = new JSONArray();
                array.add(Object);
            }
            // 获取该元素下所有子元素
            List<Element> listElement = node.elements();
            if (listElement.isEmpty()) {
                // 该元素无子元素，获取元素的值
                String nodeValue = node.getTextTrim();
                array.add(nodeValue);
                json.put(nodeName, array);
                return;
            }
            // 有子元素
            JSONObject newJson = new JSONObject();
            // 遍历所有子元素
            for (Element e : listElement) {
                // 递归
                iterateNodes(e, newJson);
            }
            array.add(newJson);
            json.put(nodeName, array);
            return;
        }
        // 该元素同级下第一次遍历
        // 获取该元素下所有子元素
        List<Element> listElement = node.elements();
        if (listElement.isEmpty()) {
            // 该元素无子元素，获取元素的值
            String nodeValue = node.getTextTrim();
            json.put(nodeName, nodeValue);
            return;
        }
        // 有子节点，新建一个JSONObject来存储该节点下子节点的值
        JSONObject object = new JSONObject();
        // 遍历所有一级子节点
        for (Element e : listElement) {
            // 递归
            iterateNodes(e, object);
        }
        json.put(nodeName, object);
        return;
    }

    private static void jsonToXmlStr(JSONObject json,StringBuffer buffer,Boolean isEscape){
        Iterator<Map.Entry<String,Object>> it = json.entrySet().iterator();
        Map.Entry<String,Object> en;
        while(it.hasNext()){
            en = it.next();
            if(en.getKey().startsWith("-")){
                continue;
            }
            if(en.getKey().equals("#text")){
                //直接输出文本
                buffer.append(en.getValue());
                continue;
            }
            if(en.getValue() instanceof JSONObject){
                buffer.append("<").append(en.getKey()).append(getAttr((JSONObject) en.getValue())).append(">");
                JSONObject jo = json.getJSONObject(en.getKey());
                jsonToXmlStr(jo,buffer,isEscape);
                buffer.append("</").append(en.getKey()).append(">");
            }else if(en.getValue() instanceof JSONArray){
                JSONArray jsonArray = json.getJSONArray(en.getKey());
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonobject =  jsonArray.getJSONObject(i);
                    buffer.append("<").append(en.getKey()).append(getAttr(jsonobject)).append(">");
                    jsonToXmlStr(jsonobject,buffer,isEscape);
                    buffer.append("</").append(en.getKey()).append(">");
                }
            }else{
                buffer.append("<").append(en.getKey()).append(">").append(isEscape ? escape(String.valueOf(en.getValue())) : String.valueOf(en.getValue())).append("</").append(en.getKey()).append(">");
            }
        }
    }

    /**
     * 拼当前节点属性
     * @param json
     * @return
     */
    private static String getAttr(JSONObject json){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,Object> entity:json.entrySet()){
            if(entity.getKey().startsWith("-")){
                sb.append(" ").append(entity.getKey().substring(1)).append("=\"").append(entity.getValue().toString()).append("\"");
            }
        }
        return sb.toString();
    }

    /**
     * 特殊字符pattern
     */
    private static Pattern pattern = Pattern.compile("[<>&\"',]");

    /**
     * json转xml中遇到特殊字符对内容追加![CDATA[]]
     * @param string
     * @return
     */
    private static String escape(String string) {
        return pattern.matcher(string).find() ? "<![CDATA[" + string + "]]>" : string;
    }

    /**
     * 判断是否是JSON数组
     * @param str
     * @return
     */
    public static boolean isJsonArray(String str) {
        if (StrUtil.isBlank(str)) {
            return false;
        }
        return isWrap(str.trim(), '[', ']');
    }

    public static boolean isWrap(CharSequence str, char prefixChar, char suffixChar) {
        if (null == str) {
            return false;
        }
        return str.charAt(0) == prefixChar && str.charAt(str.length() - 1) == suffixChar;
    }
}


