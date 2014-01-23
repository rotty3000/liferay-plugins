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

import com.liferay.osgi.service.http.internal.servlet.DynamicFilterRegistration;
import com.liferay.osgi.service.http.internal.servlet.DynamicServletRegistration;
import com.liferay.portal.apache.bridges.struts.LiferayServletContext;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.util.JS;

import java.io.IOException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.osgi.service.http.HttpConstants;
import org.osgi.service.http.HttpContext;

/**
 * @author Raymond Augé
 */
public class HttpServletContext extends LiferayServletContext {

	public HttpServletContext(
		ServletContext parentServletContext, HttpContext httpContext,
		Map<String, Object> properties, LiferayHttpService liferayHttpService) {

		super(parentServletContext);

		_httpContext = httpContext;
		_properties = properties;
		_liferayHttpService = liferayHttpService;

		_attributes = new ConcurrentHashMap<String, Object>();
		_contextName = MapUtil.getString(
			_properties, HttpConstants.HTTP_WHITEBOARD_CONTEXT_NAME);

		String parentContextPath = parentServletContext.getContextPath();

		_contextPath = parentContextPath + "/o";

		if (!_contextName.equals("default")) {
			_contextPath += StringPool.SLASH + JS.getSafeName(_contextName);
		}

		_contextShared = MapUtil.getBoolean(
			_properties, HttpConstants.HTTP_WHITEBOARD_CONTEXT_SHARED);
		_filterRegistrations =
			new ConcurrentHashMap<String, DynamicFilterRegistration>();
		_servletRegistrations =
			new ConcurrentHashMap<String, DynamicServletRegistration>();
	}

	@Override
	public javax.servlet.FilterRegistration.Dynamic addFilter(
		String filterName, Class<? extends Filter> clazz) {

		throw new UnsupportedOperationException();
	}

	@Override
	public javax.servlet.FilterRegistration.Dynamic addFilter(
		String filterName, Filter filter) {

		DynamicFilterRegistration dynamicFilterRegistration =
			_filterRegistrations.get(filterName);

		if (dynamicFilterRegistration == null) {
			dynamicFilterRegistration = new DynamicFilterRegistration(
				filterName, filter);

			_filterRegistrations.put(filterName, dynamicFilterRegistration);
		}
		else if (dynamicFilterRegistration.getClassName() != null) {
			return null;
		}

		return dynamicFilterRegistration;
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
		//TODO
		//ServletContextListener
		//ServletContextAttributeListener
		//ServletRequestListener
		//ServletRequestAttributeListener
		//HttpSessionListener
		//HttpSessionAttributeListener
		//AsyncListener

		//super.addListener(eventListener);
	}

	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(
		String servletName, Class<? extends Servlet> clazz) {

		throw new UnsupportedOperationException();
	}

	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(
		String servletName, Servlet servlet) {

		DynamicServletRegistration dynamicServletRegistration =
			_servletRegistrations.get(servletName);

		if (dynamicServletRegistration == null) {
			dynamicServletRegistration = new DynamicServletRegistration(
				servletName, servlet);

			_servletRegistrations.put(servletName, dynamicServletRegistration);
		}
		else if (dynamicServletRegistration.getClassName() != null) {
			return null;
		}

		return dynamicServletRegistration;
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
		if (!_contextShared) {
			return _httpContext.getClass().getClassLoader();
		}

		// TODO do we need an aggregate classLoader here?

		return _httpContext.getClass().getClassLoader();
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
	public int getEffectiveMajorVersion() {
		return getMajorVersion();
	}

	@Override
	public int getEffectiveMinorVersion() {
		return getMinorVersion();
	}

	@Override
	public FilterRegistration getFilterRegistration(String filterName) {
		return _filterRegistrations.get(filterName);
	}

	@Override
	public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
		return _filterRegistrations;
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
	public int getMajorVersion() {
		return 3;
	}

	@Override
	public String getMimeType(String file) {
		return _httpContext.getMimeType(file);
	}

	@Override
	public int getMinorVersion() {
		return 0;
	}

	@Override
	public RequestDispatcher getNamedDispatcher(String name) {
		//TODO
		return null;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		//TODO
		return null;
	}

	@Override
	public URL getResource(String path) throws MalformedURLException {
		return _httpContext.getResource(path);
	}

	@Override
	public InputStream getResourceAsStream(String path) {
		URL resource = _httpContext.getResource(path);

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

	@Deprecated
	@Override
	public Servlet getServlet(String name) {
		return null;
	}

	@Override
	public String getServletContextName() {
		return _contextName;
	}

	@Deprecated
	@Override
	public Enumeration<String> getServletNames() {
		return Collections.enumeration(Collections.<String>emptyList());
	}

	@Override
	public ServletRegistration getServletRegistration(String servletName) {
		return _servletRegistrations.get(servletName);
	}

	@Override
	public Map<String, ? extends ServletRegistration> getServletRegistrations() {
		return _servletRegistrations;
	}

	@Deprecated
	@Override
	public Enumeration<Servlet> getServlets() {
		return Collections.enumeration(Collections.<Servlet>emptyList());
	}

	@Override
	public void removeAttribute(String name) {
		_attributes.remove(name);
	}

	public void removeServlet(Servlet servlet) {
		Collection<DynamicServletRegistration> values =
			_servletRegistrations.values();

		DynamicServletRegistration dynamicServletRegistration = null;

		for (DynamicServletRegistration curDynamicServletRegistration : values) {
			if (curDynamicServletRegistration.getServlet() == servlet) {
				dynamicServletRegistration = curDynamicServletRegistration;

				break;
			}
		}

		if (dynamicServletRegistration == null) {
			return;
		}

		_servletRegistrations.remove(dynamicServletRegistration.getName());

		try {
			servlet.destroy();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setAttribute(String name, Object value) {
		_attributes.put(name, value);
	}

	private Map<String, Object> _attributes;
	private String _contextName;
	private String _contextPath;
	private boolean _contextShared = false;
	private Map<Class<? extends EventListener>, Object> _eventListeners;
	private Map<String, DynamicFilterRegistration> _filterRegistrations;
	private HttpContext _httpContext;
	private LiferayHttpService _liferayHttpService;
	private Map<String, Object> _properties;
	private Map<String, DynamicServletRegistration> _servletRegistrations;

}