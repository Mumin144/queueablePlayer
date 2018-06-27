package com.qp.model;

public class YtRequest {

	private Boolean addAtEnd;
	
	private String url;
	
	private String name;
	
	public YtRequest() {
		
	}

	public YtRequest(String url, Boolean addAtEnd) {
		this.addAtEnd = addAtEnd;
		this.url = url;
	}
	
	public YtRequest(String url, Boolean addAtEnd, String name) {
		this.addAtEnd = addAtEnd;
		this.url = url;
		this.name = name;
	}

	public Boolean getAddAtEnd() {
		return addAtEnd;
	}

	public void setAddAtEnd(Boolean addAtEnd) {
		this.addAtEnd = addAtEnd;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
