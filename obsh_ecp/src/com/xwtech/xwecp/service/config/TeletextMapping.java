package com.xwtech.xwecp.service.config;

import org.apache.log4j.Logger;

public class TeletextMapping {
	private static final Logger logger = Logger.getLogger(TeletextMapping.class);

	private String paramName;

	private String match;

	private String teletextTemplate;

	private String resolverClass;

	private String directImplClass;

	//2011-11-15 maofw增加resultMappingId字段
	private String resultMappingId;

	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getResolverClass() {
		return resolverClass;
	}

	public void setResolverClass(String resolverClass) {
		this.resolverClass = resolverClass;
	}

	public String getTeletextTemplate() {
		return teletextTemplate;
	}

	public void setTeletextTemplate(String teletextTemplate) {
		this.teletextTemplate = teletextTemplate;
	}

	public String getDirectImplClass() {
		return directImplClass;
	}

	public void setDirectImplClass(String directImplClass) {
		this.directImplClass = directImplClass;
	}

	public String getResultMappingId() {
		return resultMappingId;
	}

	public void setResultMappingId(String resultMappingId) {
		this.resultMappingId = resultMappingId;
	}
}
