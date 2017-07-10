package com.manuelpayet.firefox_remote_debug.beans;

import java.util.Map;


public class Capabilities {
	private String from;
	private String applicationType;
	private String testConnectionPrefix;
	private Map<String, String> traits;
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getApplicationType() {
		return applicationType;
	}
	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}
	public String getTestConnectionPrefix() {
		return testConnectionPrefix;
	}
	public void setTestConnectionPrefix(String testConnectionPrefix) {
		this.testConnectionPrefix = testConnectionPrefix;
	}
	public Map<String, String> getTraits() {
		return traits;
	}
	public void setTraits(Map<String, String> traits) {
		this.traits = traits;
	}
	@Override
	public String toString() {
		return "Capabilities [from=" + from + ", applicationType=" + applicationType + ", testConnectionPrefix="
				+ testConnectionPrefix + ", traits=" + traits + "]";
	}
	

}
