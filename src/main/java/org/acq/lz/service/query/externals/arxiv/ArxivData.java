package org.acq.lz.service.query.externals.arxiv;


import org.acq.lz.service.query.ICustomSearchDocument;
import org.acq.lz.service.query.ICustomSearchResult;

import java.io.Serializable;
import java.util.List;



public class ArxivData implements ICustomSearchResult, Serializable
{
	public static final String name = "ArXiv";
	
	private static final long serialVersionUID = 4487000492105589382L;

	public int totalResults;
	public List<ArxivPublication> docs;
	
	public ArxivData()
	{
		
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
		return name;
	}
	
	public Object getSource()
	{
		return this;
	}
}
