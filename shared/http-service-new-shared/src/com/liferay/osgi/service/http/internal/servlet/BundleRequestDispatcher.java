/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.osgi.service.http.internal.servlet;

import com.liferay.osgi.service.http.internal.HttpServletContext;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.util.ClassLoaderUtil;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.Filter;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.http.runtime.ServletDTO;

/**
 * @author Raymond Augé
 * @author Miguel Pastor
 */
public class BundleRequestDispatcher implements RequestDispatcher {

	public BundleRequestDispatcher(
		HttpServletContext httpServletContext, String requestURI,
		String pathInContext, String queryString, String name) {

		_httpServletContext = httpServletContext;
		_requestURI = requestURI;
		_pathInContext = pathInContext;
		_queryString = queryString;
		_name = name;

		if (_name != null) {
			_bundleFilterChain = getFilterChain();
		}
		else {
			_bundleFilterChain = getFilterChain();
		}

		_badRequest = false;

		_bundleFilterChain.setServlet(findTarget());
	}

	public void doDispatch(
			ServletRequest servletRequest, ServletResponse servletResponse)
		throws IOException, ServletException {

		if (_badRequest) {
			HttpServletResponse response = (HttpServletResponse)servletResponse;

			response.sendError(
				HttpServletResponse.SC_NOT_FOUND, _requestURI);

			return;
		}

		ClassLoader contextClassLoader =
			ClassLoaderUtil.getContextClassLoader();

		try {
			ClassLoader pluginClassLoader =
				_httpServletContext.getClassLoader();

			ClassLoaderUtil.setContextClassLoader(pluginClassLoader);

			List<ServletRequestListener> servletRequestListeners =
				_httpServletContext.getServletRequestListeners();

			for (ServletRequestListener servletRequestListener :
					servletRequestListeners) {

				ServletRequestEvent servletRequestEvent =
					new ServletRequestEvent(
						_httpServletContext, servletRequest);

				servletRequestListener.requestInitialized(servletRequestEvent);
			}

			_bundleFilterChain.doFilter(servletRequest, servletResponse);

			for (ServletRequestListener servletRequestListener :
					servletRequestListeners) {

				ServletRequestEvent servletRequestEvent =
					new ServletRequestEvent(
						_httpServletContext, servletRequest);

				servletRequestListener.requestDestroyed(servletRequestEvent);
			}
		}
		finally {
			ClassLoaderUtil.setContextClassLoader(contextClassLoader);
		}
	}

	@Override
	public void forward(
			ServletRequest servletRequest, ServletResponse servletResponse)
		throws IOException, ServletException {

		BundleServletRequest bundleServletRequest = new BundleServletRequest(
			this, (HttpServletRequest)servletRequest);

		doDispatch(bundleServletRequest, servletResponse);
	}

	@Override
	public void include(
			ServletRequest servletRequest, ServletResponse servletResponse)
		throws IOException, ServletException {

		BundleServletRequest bundleServletRequest = new BundleServletRequest(
			this, (HttpServletRequest)servletRequest);

		String contextPath = _httpServletContext.getContextPath();

		if (_requestURI != null) {
			bundleServletRequest.setAttribute(
				JavaConstants.JAVAX_SERVLET_INCLUDE_REQUEST_URI,
				_httpServletContext.getContextPath().concat(_requestURI));
		}

		if (contextPath != null) {
			bundleServletRequest.setAttribute(
				JavaConstants.JAVAX_SERVLET_FORWARD_CONTEXT_PATH, contextPath);
		}

		if (_servletPath != null) {
			bundleServletRequest.setAttribute(
				JavaConstants.JAVAX_SERVLET_FORWARD_SERVLET_PATH, _servletPath);
		}

		if (_queryString != null) {
			bundleServletRequest.setAttribute(
				JavaConstants.JAVAX_SERVLET_INCLUDE_QUERY_STRING, _queryString);
		}

		if (_pathInfo != null) {
			bundleServletRequest.setAttribute(
				JavaConstants.JAVAX_SERVLET_INCLUDE_PATH_INFO, _pathInfo);
		}

		doDispatch(bundleServletRequest, servletResponse);
	}

	protected HttpServletContext getHttpServletContext() {
		return _httpServletContext;
	}

	protected String getPathInfo() {
		return _pathInfo;
	}

	protected String getRequestURI() {
		return _requestURI;
	}

	protected String getServletPath() {
		return _servletPath;
	}

	private Servlet findTarget() {
		ConcurrentMap<Servlet, ServletDTO> servletMap =
			_httpServletContext.getServletMap();

		for (Map.Entry<Servlet, ServletDTO> entry : servletMap.entrySet()) {
			if (_name != null) {
				if (entry.getValue().name.equals(_name)) {
					return entry.getKey();
				}

				continue;
			}

			String[] patterns = entry.getValue().patterns;

			for (String pattern : patterns) {
				if ((pattern != null) && match(pattern, _requestURI, true)) {
					return entry.getKey();
				}
			}
		}

		_badRequest = true;

		return null;
	}

	private BundleFilterChain getFilterChain() {
		BundleFilterChain bundleFilterChain = new BundleFilterChain();

		for (ServiceComparable<Filter> serviceComparable :
				_httpServletContext.getFilterServiceComparables()) {

			if (_name != null) {
				if (serviceComparable.getName().equals(_name)) {
					bundleFilterChain.addFilter(serviceComparable.getT());
				}

				continue;
			}

			List<String> patterns = serviceComparable.getMappings();

			for (String pattern : patterns) {
				if ((pattern != null) && match(pattern, _requestURI, true)) {
					bundleFilterChain.addFilter(serviceComparable.getT());
				}
			}
		}

		return bundleFilterChain;
	}

	private boolean isPathWildcardMatch(String pattern, String path) {
		int cpl = pattern.length() - 2;

		if (pattern.endsWith("/*") && path.regionMatches(0, pattern, 0, cpl)) {
			if (path.length() == cpl || '/' == path.charAt(cpl)) {
				return true;
			}
		}

		return false;
	}

	private boolean match(String pattern, String path, boolean noDefault)
		throws IllegalArgumentException {

		char firstChar = pattern.charAt(0);

		if (firstChar == '/') {
			if (!noDefault && (pattern.length() == 1) || pattern.equals(path)) {
				return true;
			}

			if(isPathWildcardMatch(pattern, path)) {
				return true;
			}
		}
		else if (firstChar == '*') {
			return path.regionMatches(
				path.length() - pattern.length() + 1,
				pattern, 1, pattern.length() - 1);
		}

		return false;
	}

	private boolean _badRequest;
	private BundleFilterChain _bundleFilterChain;
	private final HttpServletContext _httpServletContext;
	private final String _name;
	private final String _pathInContext;
	private String _pathInfo;
	private final String _queryString;
	private final String _requestURI;
	private String _servletMapping;
	private String _servletPath;

}