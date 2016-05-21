package com.cmdi.yjs.session;

import java.util.Enumeration;
import java.util.Set;

import javax.servlet.http.HttpSession;

/**
 * @author xiaoqingli
 *
 */
public class HttpSessionSidWrapper extends HttpSessionWrapper {

	private String sid = "";

	public HttpSessionSidWrapper(String sid, HttpSession session) {
		super(session);
		this.sid = sid;
	}

	public Object getAttribute(String key) {
		return SessionService.getInstance().hget(sid, key);
	}

	public Enumeration getAttributeNames() {
		Set<String> keys = SessionService.getInstance().hkeys(this.sid);
		return (new Enumerator(keys, true));
	}

	public void invalidate() {
		SessionService.getInstance().del(this.sid);
	}

	public void removeAttribute(String key) {
		SessionService.getInstance().hdel(this.sid,key);
	}

	public void setAttribute(String key, Object value) {
		SessionService.getInstance().hset(this.sid, key, value);
	}
	
	public String getId() {
		return sid;
	}

}
