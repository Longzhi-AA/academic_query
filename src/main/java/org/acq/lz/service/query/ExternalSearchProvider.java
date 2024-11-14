package org.acq.lz.service.query;

import java.util.List;

public interface ExternalSearchProvider
{
	ICustomSearchResult search(String query, boolean isAuthorRequest);

	List<CommonSearchResult> searchThruApi(String query, boolean isAuthorRequest);

	String getLabel();
}
