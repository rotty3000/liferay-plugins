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

import java.io.IOException;

import java.util.List;
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

import org.osgi.dto.DTO;

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

		_bundleFilterChain = buildFilterChain();
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

	private BundleFilterChain buildFilterChain() {
		BundleFilterChain bundleFilterChain = new BundleFilterChain();

		Filter filter;

		for (FilterHolder holder : _httpServletContext.getFilters()) {
			if ((filter = holder.match(_requestURI, _name)) != null) {
				bundleFilterChain.addFilter(filter);
			}
		}

		bundleFilterChain.setServlet(matchServlet());

		return bundleFilterChain;
	}

	private void doDispatch(
			ServletRequest servletRequest, ServletResponse servletResponse)
		throws IOException, ServletException {

		if (_bundleFilterChain.getServlet() == null) {
			HttpServletResponse response = (HttpServletResponse)servletResponse;

			response.sendError(
				HttpServletResponse.SC_NOT_FOUND, _requestURI);

			return;
		}

		List<ServletRequestListener> servletRequestListeners =
			_httpServletContext.getServletRequestListeners();

		executePreListeners(servletRequest, servletRequestListeners);

		_bundleFilterChain.doFilter(servletRequest, servletResponse);

		executePostListeners(servletRequest, servletRequestListeners);
	}

	private void executePostListeners(
		ServletRequest servletRequest,
		List<ServletRequestListener> servletRequestListeners) {

		for (ServletRequestListener servletRequestListener :
				servletRequestListeners) {

			ServletRequestEvent servletRequestEvent =
				new ServletRequestEvent(
					_httpServletContext, servletRequest);

			servletRequestListener.requestDestroyed(servletRequestEvent);
		}
	}

	private void executePreListeners(
		ServletRequest servletRequest,
		List<ServletRequestListener> servletRequestListeners) {

		for (ServletRequestListener servletRequestListener :
				servletRequestListeners) {

			ServletRequestEvent servletRequestEvent =
				new ServletRequestEvent(
					_httpServletContext, servletRequest);

			servletRequestListener.requestInitialized(servletRequestEvent);
		}
	}

	private Servlet matchServlet() {
		ConcurrentMap<Servlet, Holder<Servlet, ? extends DTO>> servletMap =
			_httpServletContext.getServletMap();

		Servlet servlet;

		for (Holder<Servlet, ? extends DTO> holder : servletMap.values()) {
			if ((servlet = holder.match(_requestURI, _name)) != null) {
				return servlet;
			}
		}

		return null;
	}

	private BundleFilterChain _bundleFilterChain;
	private final HttpServletContext _httpServletContext;
	private final String _name;
	private final String _pathInContext;
	private String _pathInfo;
	private final String _queryString;
	private final String _requestURI;
	private String _servletPath;

}