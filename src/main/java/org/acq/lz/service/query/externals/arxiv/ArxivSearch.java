package org.acq.lz.service.query.externals.arxiv;

import org.acq.lz.service.query.CommonSearchResult;
import org.acq.lz.service.query.ExternalSearchProvider;
import org.acq.lz.service.query.ICustomSearchResult;

import java.util.List;

public class ArxivSearch implements ExternalSearchProvider
{
	public ICustomSearchResult search(String query, boolean isAuthorRequest)
	{
		try 
		{
			return new ArxivAPIReader().query(query, 20, isAuthorRequest);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public String getLabel()
	{
		return ArxivData.name;
	}

	public List<CommonSearchResult> searchThruApi(String query, boolean isAuthorRequest) {
		return null;
	}
}
