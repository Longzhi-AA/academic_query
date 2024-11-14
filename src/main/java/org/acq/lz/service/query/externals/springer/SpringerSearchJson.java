package org.acq.lz.service.query.externals.springer;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import nu.xom.*;
import org.acq.lz.service.query.*;
import org.acq.lz.utils.BrowseJsonUtils;
import org.acq.lz.utils.BrowseUtils;
import org.acq.lz.utils.JsonUtils;
import org.acq.lz.vo.springer.Creator;
import org.acq.lz.vo.springer.Record;
import org.acq.lz.vo.springer.SpringerResultVO;
import org.acq.lz.vo.springer.UrlValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component("SpringerSearchJson")
public class SpringerSearchJson implements ExternalSearchProvider
{

	private static final Logger logger = LoggerFactory.getLogger(SpringerSearchJson.class);
	private static final String urlStart = "http://api.springernature.com/meta/v2/json?q=";

	private static final String pageSize()
	{
		return "&p=20";
	}

	private static final String apiKey = "&api_key=cfd1b47b8f35b8ad13df890ac03c826e";



	public String getLabel()
	{
		return "Springer";
	}

	public ICustomSearchResult search(String query, boolean isAuthorRequest) {
		String content = null;
		try {
			String url = urlStart + URLEncoder.encode(query, "UTF-8") + pageSize() + apiKey;
			content = BrowseJsonUtils.getJsonResponse(url);
		}catch (Exception e){
			logger.error("failed to getconnetion to server.", e);
		}
		String json = null;
		SpringerResultVO resultVO = null;
		if (content != null) {
			json = JSONUtil.formatJsonStr(content);
			System.out.println(json);
			if (StrUtil.isNotBlank(json)) {
				resultVO = JsonUtils.toBean(content, SpringerResultVO.class);
			}
		}

		return null;
	}


	private List<CommonSearchResult> buildSearchResult(SpringerResultVO resultVO){
		List<CommonSearchResult> results = new ArrayList<CommonSearchResult>();
		if (resultVO == null){
			return results;
		}
		List<Record> records = resultVO.getRecords();
		if (records == null || records.isEmpty()){
			return results;
		}
		for (Record record : records){
			if(record == null){
				continue;
			}
			CommonSearchResult result = new CommonSearchResult();
			result.setTitle(record.getTitle());
			List<Creator> creators = record.getCreators();
			result.setAuthors(creators.stream().map(Creator::getCreator).collect(Collectors.toList()));
			result.setPublicationDate(record.getPublicationDate());
			result.setAbstracts(record.getAbstracts());
			result.setKeyword(record.getKeyword());
			result.setUrl(record.getUrl().get(0).getValue());
			result.setSubjects(record.getSubjects());

			results.add(result);
		}
		return results;
	}

	public List<CommonSearchResult> searchThruApi(String query, boolean isAuthorRequest) {

		String content = null;
		try {
			String url = urlStart + URLEncoder.encode(query, "UTF-8") + pageSize() + apiKey;
			content = BrowseJsonUtils.getJsonResponse(url);
		}catch (Exception e){
			logger.error("failed to get connection to server.", e);
		}
		String json = null;
		SpringerResultVO resultVO = null;
		if (content != null) {
			json = JSONUtil.formatJsonStr(content);
			System.out.println(json);
			if (StrUtil.isNotBlank(json)) {
				resultVO = JsonUtils.toBean(content, SpringerResultVO.class);
			}
		}
		return buildSearchResult(resultVO);
	}
}
