package org.acq.lz.service.query.externals.springer;
import nu.xom.*;
import org.acq.lz.service.query.CustomSearchDocument;
import org.acq.lz.service.query.ExternalSearchProvider;
import org.acq.lz.service.query.ICustomSearchDocument;
import org.acq.lz.service.query.ICustomSearchResult;
import org.acq.lz.utils.BrowseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SpringerSearch implements ExternalSearchProvider
{

	private static final Logger logger = LoggerFactory.getLogger(SpringerSearch.class);
	private static final String urlStart = "http://api.springernature.com/metadata/pam?q=";
	
	private static String key = "";
	private static final String urlEnd()
	{
		return "&p=20";
	}

	private static final String apiKey = "&api_key=cfd1b47b8f35b8ad13df890ac03c826e";
	
	public ICustomSearchResult search(String query, boolean isAuthorRequest){
		try {
			URL url = new URL(urlStart + URLEncoder.encode(query, "UTF-8") + urlEnd() + apiKey);
			BrowseUtils browser = new BrowseUtils();
			browser.setXmlReader(null);
			Document doc = browser.getXML(url);
			return parse(doc);
		} 
		catch (Exception e) {
			logger.error("search error.", e);
		}
		
		return null;
	}
	
	private static XPathContext pam = new XPathContext("pam", "http://prismstandard.org/namespaces/pam/2.0/");
	private static XPathContext dc = new XPathContext("dc", "http://purl.org/dc/elements/1.1/");
	private static XPathContext prism = new XPathContext("prism", "http://prismstandard.org/namespaces/basic/2.0/");
	private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	public ICustomSearchResult parse(Document xml)
	{
		class SpringerResult implements ICustomSearchResult
		{
			String name = getLabel();
			int totalResults;
			List<CustomSearchDocument> pubs = new ArrayList<CustomSearchDocument>();

			public List<? extends ICustomSearchDocument> getResults()
			{
				return pubs;
			}

			public int getTotalResults() 
			{
				return totalResults;
			}

			public String getName() 
			{
				return name;
			}
		}
		
		SpringerResult results = new SpringerResult();
		
		Element response = xml.getRootElement();
		results.totalResults = new Integer(response.query("//result/total").get(0).getValue());
		Nodes records = response.query("//records/pam:message", pam);
		for (int i=0; i<records.size(); i++)
		{
			Element record = (Element) records.get(i);
			Elements children = record.getChildElements();
			Element head = children.get(0);
			Element body = children.get(1);		
			Element article = head.getChildElements().get(0);
			
			CustomSearchDocument result = new CustomSearchDocument();
			
			result.title = article.query("./dc:title", dc).get(0).getValue().replace("\n", " ").trim();
			try 
			{
				Date date = sf.parse(article.query("./prism:publicationDate", prism).get(0).getValue());
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				result.year = cal.get(Calendar.YEAR);
			} 
			catch (ParseException e) 
			{
				//todo:
			}

			result.url = article.query("./prism:url", prism).get(0).getValue();
			result.journal = article.query("./prism:publicationName", prism).get(0).getValue();
			Nodes authors = article.query("./dc:creator", dc);
			result.authors = new ArrayList<String>(authors.size());
			for (int a=0; a<authors.size(); a++)
			{
				result.authors.add(authors.get(a).getValue());
			}
			
			String sum = body.getValue();
			if (sum.startsWith("Abstract")) sum = sum.substring(8);
			else if (sum.startsWith("Abstract.")) sum = sum.substring(9);
			sum = sum.replace("\n", " ").replace("  ", " ").trim();
			result.summary = sum;
			
			result.result = results;
			
			results.pubs.add(result);
		}
		
		return results;
	}
	
	public String getLabel()
	{
		return "Springer";
	}



}
