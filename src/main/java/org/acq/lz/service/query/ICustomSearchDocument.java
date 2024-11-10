package org.acq.lz.service.query;

import java.util.List;

public interface ICustomSearchDocument
{
	// 文章名称
	String getTitle();
	//作者
	List<String> getAuthors();

	//发布时间
	int getYear();

	String getDoi();

	//url地址
	String getUrl();
	//一级标题
	String getJournal();

	//单位
	String getUnit();
	
	ICustomSearchResult getSearchProvider();

	
	String getSummary(); // optional
	

}
