package org.acq.lz.service.query.externals.google;

import org.acq.lz.service.query.CommonSearchResult;
import org.acq.lz.service.query.ExternalSearchProvider;
import org.acq.lz.service.query.ICustomSearchResult;

import java.util.List;

public class ScholarSearch implements ExternalSearchProvider
{
	public ICustomSearchResult search(String query, boolean isAuthorRequest)
	{
		GoogleScholarSearch search = new GoogleScholarSearch();
		
		try 
		{
			GoogleScholarData data = search.execQueryNonProxy(isAuthorRequest ? 
					GoogleScholarSearch.getAuthorFieldQuery(query, 20, 0) : 
					GoogleScholarSearch.getQuery(query, 20, 0));
//			if (data.totalResults > GoogleScholarSearch.maxResultsPerPage)
//			{
//				for (int i=1; i<=Math.min(2, data.totalResults / GoogleScholarSearch.maxResultsPerPage); i++)
//				{
//					GoogleScholarData dataAdd = search.execQueryNonProxy(isAuthorRequest ? GoogleScholarSearch.getAuthorFieldQuery(params.query, GoogleScholarSearch.maxResultsPerPage, i*GoogleScholarSearch.maxResultsPerPage) : GoogleScholarSearch.getQuery(params.query, GoogleScholarSearch.maxResultsPerPage, i*GoogleScholarSearch.maxResultsPerPage));
//					data.merge(dataAdd);
//				}
//			}
			return data;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public String getLabel()
	{
		return "Google Scholar";
	}

	public List<CommonSearchResult> searchThruApi(String query, boolean isAuthorRequest) {
		return null;
	}
}
