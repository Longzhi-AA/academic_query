package org.acq.lz.service.query;

public interface ExternalSearchProvider 
{
	ICustomSearchResult search(String query, boolean isAuthorRequest);
	String getLabel();
}
