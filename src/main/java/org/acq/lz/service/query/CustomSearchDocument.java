package org.acq.lz.service.query;

import java.util.ArrayList;
import java.util.List;

public class CustomSearchDocument implements ICustomSearchDocument
{
	public String title;
	public List<String> authors = new ArrayList<String>();
	public int year;
	public String doi;
	public String url;
	public String journal;
	public ICustomSearchResult result;
	public String summary;

	public String unit;

	public String introduction;

	public String methodolgy;

	public String conclusion;

	public String publishTime;

	public String keyWord;

	public List<String> relatedPapers;

	public String getTitle() 
	{
		return title;
	}

	public List<String> getAuthors() 
	{
		return authors;
	}

	public int getYear() 
	{
		return year;
	}

	public String getDoi() 
	{
		return doi;
	}

	public String getUrl() 
	{
		return url;
	}

	public String getJournal() 
	{
		return journal;
	}

	public ICustomSearchResult getSearchProvider() {

		return result;
	}

	public String getSummary() 
	{
		return summary;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setJournal(String journal) {
		this.journal = journal;
	}

	public ICustomSearchResult getResult() {
		return result;
	}

	public void setResult(ICustomSearchResult result) {
		this.result = result;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getMethodolgy() {
		return methodolgy;
	}

	public void setMethodolgy(String methodolgy) {
		this.methodolgy = methodolgy;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public List<String> getRelatedPapers() {
		return relatedPapers;
	}

	public void setRelatedPapers(List<String> relatedPapers) {
		this.relatedPapers = relatedPapers;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
}
