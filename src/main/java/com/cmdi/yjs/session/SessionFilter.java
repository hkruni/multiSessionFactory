package com.cmdi.yjs.session;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionFilter extends HttpServlet implements Filter {

	private static final long serialVersionUID = -365105405910803550L;

	private String sessionId = "sid";

	private String cookieDomain = "";

	private String cookiePath = "/";

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		Cookie cookies[] = request.getCookies();
		Cookie sCookie = null;

		// 获取sessionId
		String sid = "";
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				sCookie = cookies[i];
				if (sCookie.getName().equals(sessionId)) {
					sid = sCookie.getValue();
				}
			}
		}

		// 如果sessionId不存在则创建
		if (sid == null || sid.length() == 0) {
			sid = java.util.UUID.randomUUID().toString();
			Cookie mycookies = new Cookie(sessionId, sid);
			mycookies.setMaxAge(-1);
			if (this.cookieDomain != null && this.cookieDomain.length() > 0) {
				mycookies.setDomain(this.cookieDomain);
			}
			mycookies.setPath(this.cookiePath);
			response.addCookie(mycookies);
		}

		// 用HttpServletRequestWrapper代理request
		filterChain.doFilter(new HttpServletRequestWrapper(sid, request),
				servletResponse);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.sessionId = filterConfig.getInitParameter("sessionId");
		this.cookieDomain = filterConfig.getInitParameter("cookieDomain");
		if (this.cookieDomain == null) {
			this.cookieDomain = "";
		}

		this.cookiePath = filterConfig.getInitParameter("cookiePath");
		if (this.cookiePath == null || this.cookiePath.length() == 0) {
			this.cookiePath = "/";
		}
	}

}
