package org.acq.lz.service.query;

import java.util.List;

public interface ICustomSearchResult
{
//	Object getSource();
	List<? extends ICustomSearchDocument> getResults();
	int getTotalResults();
	String getName();
}
