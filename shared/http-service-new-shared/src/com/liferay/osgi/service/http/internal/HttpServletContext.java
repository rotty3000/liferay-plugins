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

package com.liferay.osgi.service.http.internal;

import com.liferay.portal.apache.bridges.struts.LiferayServletContext;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.util.JS;

import java.io.IOException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.osgi.service.http.HttpConstants;
import org.osgi.service.http.ServletContextHelper;

/**
 * @author Raymond Augé
 */
public class HttpServletContext extends LiferayServletContext {

	public HttpServletContext(
		ServletContext servletContext,
		ServletContextHelper servletContextHelper,
		Map<String, Object> properties, LiferayHttpService liferayHttpService) {

		super(servletContext);

		_servletContextHelper = servletContextHelper;
		_properties = properties;
		_liferayHttpService = liferayHttpService;

		_attributes = new ConcurrentHashMap<String, Object>();
		_contextName = MapUtil.getString(
			_properties, HttpConstants.HTTP_WHITEBOARD_CONTEXT_NAME);

		String parentContextPath = servletContext.getContextPath();

		_contextPath = parentContextPath + "/o";

		if (!_contextName.equals("default")) {
			_contextPath += StringPool.SLASH + JS.getSafeName(_contextName);
		}

		_contextShared = MapUtil.getBoolean(
			_properties, HttpConstants.HTTP_WHITEBOARD_CONTEXT_SHARED);
	}

	@Override
	public javax.servlet.FilterRegistration.Dynamic addFilter(
		String filterName, Class<? extends Filter> clazz) {

		throw new UnsupportedOperationException();
	}

	@Override
	public javax.servlet.FilterRegistration.Dynamic addFilter(
		String filterName, Filter filter) {

		throw new UnsupportedOperationException();
	}

	@Override
	public javax.servlet.FilterRegistration.Dynamic addFilter(
		String filterName, String className) {

		throw new UnsupportedOperationException();
	}

	@Override
	public void addListener(Class<? extends EventListener> clazz) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addListener(String className) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends EventListener> void addListener(T eventListener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(
		String servletName, Class<? extends Servlet> clazz) {

		throw new UnsupportedOperationException();
	}

	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(
		String servletName, Servlet servlet) {

		throw new UnsupportedOperationException();
	}

	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(
		String servletName, String className) {

		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends Filter> T createFilter(Class<T> clazz)
		throws ServletException {

		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends EventListener> T createListener(Class<T> clazz)
		throws ServletException {

		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends Servlet> T createServlet(Class<T> clazz)
		throws ServletException {

		throw new UnsupportedOperationException();
	}

	@Override
	public Object getAttribute(String name) {
		return _attributes.get(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return Collections.enumeration(_attributes.keySet());
	}

	@Override
	public ClassLoader getClassLoader() {
		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public ServletContext getContext(String uriPath) {
		return _liferayHttpService.getServletContext(uriPath);
	}

	@Override
	public String getContextPath() {
		return _contextPath;
	}

	@Override
	public FilterRegistration getFilterRegistration(String filterName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getInitParameter(String name) {
		return String.valueOf(_properties.get(name));
	}

	@Override
	public Enumeration<String> getInitParameterNames() {
		return Collections.enumeration(_properties.keySet());
	}

	@Override
	public String getMimeType(String file) {
		return _servletContextHelper.getMimeType(file);
	}

	@Override
	public RequestDispatcher getNamedDispatcher(String name) {
		//TODO
		return null;
	}

	@Override
	public String getRealPath(String path) {
		return _servletContextHelper.getRealPath(path);
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		//TODO
		return null;
	}

	@Override
	public URL getResource(String path) throws MalformedURLException {
		return _servletContextHelper.getResource(path);
	}

	@Override
	public InputStream getResourceAsStream(String path) {
		URL resource = _servletContextHelper.getResource(path);

		if (resource == null) {
			return null;
		}

		try {
			return resource.openStream();
		}
		catch (IOException e) {
			return null;
		}
	}

	@Override
	public Set<String> getResourcePaths(String path) {
		return _servletContextHelper.getResourcePaths(path);
	}

	@Override
	public String getServletContextName() {
		return _contextName;
	}

	@Override
	public ServletRegistration getServletRegistration(String servletName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, ? extends ServletRegistration> getServletRegistrations() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeAttribute(String name) {
		_attributes.remove(name);
	}

	public void removeServlet(Servlet servlet) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAttribute(String name, Object value) {
		_attributes.put(name, value);
	}

	private Map<String, Object> _attributes;
	private String _contextName;
	private String _contextPath;
	private boolean _contextShared = false;
	private LiferayHttpService _liferayHttpService;
	private Map<String, Object> _properties;
	private ServletContextHelper _servletContextHelper;

}