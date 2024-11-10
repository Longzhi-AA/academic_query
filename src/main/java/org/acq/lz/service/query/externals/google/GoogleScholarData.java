package org.acq.lz.service.query.externals.google;


import org.acq.lz.service.query.ICustomSearchDocument;
import org.acq.lz.service.query.ICustomSearchResult;

import java.io.Serializable;
import java.util.List;


public class GoogleScholarData implements ICustomSearchResult, Serializable
{
	private static final long serialVersionUID = -7373624418165676742L;
	public static final String name = "Google Scholar";
	
	public int totalResults;
	public List<GoogleScholarResult> docs;
	
	public String altName;
	
	public GoogleScholarData()
	{
		
	}
	
	public void merge(GoogleScholarData additionalData)
	{
		docs.addAll(additionalData.docs);
	}

	public List<? extends ICustomSearchDocument> getResults()
	{
		return docs;
	}
	
	public int getTotalResults()
	{
		return totalResults;
	}
	
	public String getName()
	{
		return altName != null ? altName : name;
	}

	public Object getSource()
	{
		return this;
	}
	
	public void addSearchProviderInfo()
	{
		if (docs == null) return;
		for (int i=0; i<docs.size(); i++)
		{
			GoogleScholarResult pub = docs.get(i);
			pub.setSearchProvider(this, i);
		}
	}
}
