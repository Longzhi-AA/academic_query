package org.acq.lz.service.query.externals.springer;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.acq.lz.service.query.*;
import org.acq.lz.utils.BrowseUtils;
import org.acq.lz.utils.FastJsonUtil;
import org.acq.lz.vo.springer.Creator;
import org.acq.lz.vo.springer.Record;
import org.acq.lz.vo.springer.SpringerResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("SpringerSearchJson")
public class SpringerSearchJson extends AbstractExternalSearch<SpringerResultVO>
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

	@Override
	protected SpringerResultVO doSearch(String query, boolean isAuthorRequest) {
		String content = null;
		try {
			String url = urlStart + URLEncoder.encode(query, "UTF-8") + pageSize() + apiKey;
			content = BrowseUtils.getResponse(url);
		}catch (Exception e){
			logger.error("failed to get connection to server.", e);
		}
		String json = null;
		SpringerResultVO resultVO = null;
		if (content != null) {
			json = JSONUtil.formatJsonStr(content);
			if (StrUtil.isNotBlank(json)) {
				resultVO = FastJsonUtil.jsonStrToEntity(content, SpringerResultVO.class);
			}
		}
		return resultVO;
	}

	@Override
	protected List<CommonSearchResult> buildSearchResult(SpringerResultVO resultVO) {
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
}
