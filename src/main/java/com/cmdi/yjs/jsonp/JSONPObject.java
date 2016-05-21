package com.cmdi.yjs.jsonp;



public class JSONPObject {

	private String function;
	private Object json;

	public JSONPObject(String function, Object json) {
		this.function = function;
		this.json = json;
	}

	public JSONPObject(Object json) {
		this.json = json;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public Object getJson() {
		return json;
	}

	public void setJson(Object json) {
		this.json = json;
	}

	
}