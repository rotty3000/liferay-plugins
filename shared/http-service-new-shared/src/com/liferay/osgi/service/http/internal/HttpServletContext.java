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

import com.liferay.osgi.service.http.internal.servlet.BundleRequestDispatcher;
import com.liferay.osgi.service.http.internal.servlet.ServiceComparable;
import com.liferay.osgi.service.http.internal.servlet.ServiceComparator;
import com.liferay.portal.apache.bridges.struts.LiferayServletContext;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestListener;

import org.osgi.service.http.ServletContextHelper;
import org.osgi.service.http.runtime.ErrorPageDTO;
import org.osgi.service.http.runtime.FilterDTO;
import org.osgi.service.http.runtime.ListenerDTO;
import org.osgi.service.http.runtime.ResourceDTO;
import org.osgi.service.http.runtime.ServletContextDTO;
import org.osgi.service.http.runtime.ServletDTO;

/**
 * @author Raymond Augé
 */
public class HttpServletContext extends LiferayServletContext {

	public HttpServletContext(
		ServletContext servletContext,
		ServletContextHelper servletContextHelper,
		ServletContextHelperProperties schProperties, ClassLoader classLoader,
		LiferayHttpService liferayHttpService) {

		super(servletContext);

		_servletContextHelper = servletContextHelper;
		_schProperties = schProperties;
		_classLoader = classLoader;
		_liferayHttpService = liferayHttpService;

		_contextName = _schProperties.getContextName();
		_contextPath = _schProperties.getContextPath();
		_shared = _schProperties.getProps().
			osgi_http_whiteboard_context_shared();

		_attributes = new ConcurrentHashMap<String, Object>();
		_filterServiceComparables =
			new ConcurrentSkipListSet<ServiceComparable<Filter>>(
				new ServiceComparator<Filter>());
		_servlets = new ConcurrentHashMap<Servlet, ServletDTO>();
		_servletRequestAttributeListeners =
			new CopyOnWriteArrayList<ServletRequestAttributeListener>();
		_servletRequestListeners =
			new CopyOnWriteArrayList<ServletRequestListener>();
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

	public Map<String, Object> getAttributes() {
		return _attributes;
	}

	@Override
	public ClassLoader getClassLoader() {
		return _classLoader;
	}

	@Override
	public ServletContext getContext(String uriPath) {
		return _liferayHttpService.getServletContext(uriPath);
	}

	@Override
	public String getContextPath() {
		return _contextPath;
	}

	public Set<ServiceComparable<Filter>> getFilterServiceComparables() {
		return _filterServiceComparables;
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
		return String.valueOf(_schProperties.getRawProperties().get(name));
	}

	@Override
	public Enumeration<String> getInitParameterNames() {
		return Collections.enumeration(
			_schProperties.getRawProperties().keySet());
	}

	public Map<String, Object> getInitParameters() {
		return _schProperties.getRawProperties();
	}

	public long getServiceId() {
		return _schProperties.getProps().service_id();
	}

	public ServletContextHelper getServletContextHelper() {
		return _servletContextHelper;
	}

	@Override
	public String getMimeType(String file) {
		return _servletContextHelper.getMimeType(file);
	}

	@Override
	public RequestDispatcher getNamedDispatcher(String name) {
		if (Validator.isNull(name)) {
			return null;
		}

		return new BundleRequestDispatcher(this, null, null, null, name);
	}

	@Override
	public String getRealPath(String path) {
		return _servletContextHelper.getRealPath(path);
	}

	protected boolean isValidPath(String path) {
		if (!path.startsWith(StringPool.SLASH)) {
			path = StringPool.SLASH.concat(path);
		}

		for (String illegalPath : _ILLEGAL_PATHS) {
			if (path.startsWith(illegalPath)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		if (Validator.isNull(path)) {
			return null;
		}

		if (!isValidPath(path)) {
			return null;
		}

		try {
			path = URLDecoder.decode(path, _UTF_8);
			path = URI.create(path).normalize().getPath();
		}
		catch (UnsupportedEncodingException uee) {
			throw new RuntimeException(uee);
		}

		String uri = _contextPath.concat(path);

		return new BundleRequestDispatcher(this, uri, path, null, null);
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

	public ConcurrentMap<Servlet, ServletDTO> getServletMap() {
		return _servlets;
	}

	public List<ServletRequestAttributeListener>
		getServletRequestAttributeListeners() {

		return _servletRequestAttributeListeners;
	}

	public List<ServletRequestListener> getServletRequestListeners() {
		return _servletRequestListeners;
	}

	@Override
	public ServletRegistration getServletRegistration(String servletName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, ? extends ServletRegistration> getServletRegistrations() {
		throw new UnsupportedOperationException();
	}

	public boolean isShared() {
		return _shared;
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

	public ServletContextDTO toDTO() {
		ServletContextDTO contextDTO = new ServletContextDTO();

		contextDTO.attributes = Collections.unmodifiableMap(getAttributes());
		contextDTO.contextName = getServletContextName();
		contextDTO.contextPath = getContextPath();

		Map<String, String> initParams = new HashMap<String, String>();

		Iterator<Entry<String, Object>> iterator =
			getInitParameters().entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();

			initParams.put(entry.getKey(), String.valueOf(entry.getValue()));
		}

		contextDTO.initParams = initParams;
		contextDTO.serviceId = getServiceId();
		contextDTO.shared = isShared();

		Collection<ServletDTO> servletDTOs = getServletMap().values();

		contextDTO.servletDTOs = servletDTOs.toArray(
			new ServletDTO[servletDTOs.size()]);

		// TODO
		contextDTO.errorPageDTOs = new ErrorPageDTO[0];
		contextDTO.filterDTOs = new FilterDTO[0];
		contextDTO.listenerDTOs = new ListenerDTO[0];
		contextDTO.names = new String[] {getServletContextName()};
		contextDTO.resourceDTOs = new ResourceDTO[0];

		return contextDTO;
	}

	private static final String _UTF_8 = "UTF-8";

	private static final String[] _ILLEGAL_PATHS = new String[] {
		"/META-INF/", "/OSGI-INF/", "/OSGI-OPT/", "/WEB-INF/"
	};

	private final Map<String, Object> _attributes;
	private ClassLoader _classLoader;
	private final String _contextName;
	private final String _contextPath;
	private Set<ServiceComparable<Filter>> _filterServiceComparables;
	private final LiferayHttpService _liferayHttpService;
	private final ServletContextHelperProperties _schProperties;
	private ConcurrentMap<Servlet, ServletDTO> _servlets;
	private final List<ServletRequestAttributeListener>
		_servletRequestAttributeListeners;
	private final List<ServletRequestListener> _servletRequestListeners;
	private final ServletContextHelper _servletContextHelper;
	private boolean _shared;

}