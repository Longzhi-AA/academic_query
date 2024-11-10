package org.acq.lz.service.query.externals.arxiv;

import org.acq.lz.service.query.ExternalSearchProvider;
import org.acq.lz.service.query.ICustomSearchResult;

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
}
