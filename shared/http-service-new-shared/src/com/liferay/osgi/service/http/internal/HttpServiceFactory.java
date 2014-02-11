/**
 * Copyright (c) 2000-2014 Liferay, Inc. All rights reserved.
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

import com.liferay.osgi.property.UnmodifiableMapDictionary;
import com.liferay.osgi.service.http.internal.servlet.ServletProperties;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.http.HttpConstants;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.service.http.ServletContextHelper;

/**
 * @author Raymond Augé
 */
@Component(
	factory = "http.service",
	property = {
		"provider=Liferay Inc."
	}
)
@SuppressWarnings("deprecation")
public class HttpServiceFactory implements HttpService {

	@Override
	public HttpContext createDefaultHttpContext() {
		synchronized (_httpContextLock) {
			return _httpContext;
		}
	}

	@Override
	public void registerResources(
			String alias, String name, HttpContext httpContext)
		throws NamespaceException {

		if (name == null) {
			throw new IllegalArgumentException("name cannot be null");
		}

		Bundle bundle = _componentContext.getUsingBundle();

		Hashtable<String, Object> properties = new Hashtable<String, Object>();

		synchronized (_httpContextLock) {
			String contextName = _httpContext.getContextName();

			if ((httpContext != null) &&
				(!(httpContext instanceof HttpContextWrapper))) {

				httpContext = new HttpContextWrapper(
					httpContext, (ServletContextHelper)_httpContext,
					contextName);

				_httpContext = (HttpContextNameAware)httpContext;
			}

			properties.put(
				HttpConstants.HTTP_WHITEBOARD_CONTEXT_NAME, contextName);
		}

		properties.put(HttpConstants.HTTP_WHITEBOARD_RESOURCE_PREFIX, name);
		properties.put(HttpConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, alias);

		ServiceRegistration<Servlet> serviceRegistration =
			bundle.getBundleContext().registerService(
				Servlet.class, new HttpServlet() {/**/}, properties);

		_serviceRegistrations.put(alias, serviceRegistration);
	}

	@Override
	public void registerServlet(
			String alias, Servlet servlet,
			Dictionary<String, String> initparams, HttpContext httpContext)
		throws ServletException, NamespaceException {

		Bundle bundle = _componentContext.getUsingBundle();

		Hashtable<String, Object> properties = new Hashtable<String, Object>();

		if (initparams != null) {
			for (Enumeration<String> em = initparams.keys();
					em.hasMoreElements();) {

				String key = em.nextElement();

				properties.put(key, initparams.get(key));
			}
		}

		synchronized (_httpContextLock) {
			String contextName = _httpContext.getContextName();

			if ((httpContext != null) &&
				(!(httpContext instanceof HttpContextWrapper))) {

				httpContext = new HttpContextWrapper(
					httpContext, (ServletContextHelper)_httpContext,
					contextName);

				_httpContext = (HttpContextNameAware)httpContext;
			}

			properties.put(
				HttpConstants.HTTP_WHITEBOARD_CONTEXT_NAME, contextName);
		}

		properties.put(HttpConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, alias);

		try {
			ServletProperties servletProperties = ServletProperties.cnv(
				properties, servlet);

			properties.put(
				HttpConstants.HTTP_WHITEBOARD_SERVLET_ASYNC_SUPPORTED,
				servletProperties.getProps().
					osgi_http_whiteboard_servlet_asyncSupported());

			List<String> errorPage = servletProperties.getProps().
				osgi_http_whiteboard_servlet_errorPage();

			if (errorPage != null) {
				properties.put(
					HttpConstants.HTTP_WHITEBOARD_SERVLET_ERROR_PAGE,
					errorPage);
			}

			properties.put(
				HttpConstants.HTTP_WHITEBOARD_SERVLET_NAME,
				servletProperties.getServletName());
			properties.put(
				HttpConstants.HTTP_WHITEBOARD_SERVLET_PATTERN,
				servletProperties.getProps().
					osgi_http_whiteboard_servlet_pattern());
		}
		catch (Exception e) {
			throw new IllegalArgumentException(e);
		}

		ServiceRegistration<Servlet> serviceRegistration =
			bundle.getBundleContext().registerService(
				Servlet.class, servlet, properties);

		_serviceRegistrations.put(alias, serviceRegistration);
	}

	@Override
	public void unregister(String alias) {
		ServiceRegistration<?> serviceRegistration =
			_serviceRegistrations.remove(alias);

		if (serviceRegistration == null) {
			return;
		}

		serviceRegistration.unregister();
	}

	@Activate
	protected void activate(
		ComponentContext componentContext, Map<String, Object> properties) {

		try {
			_componentContext = componentContext;

			_serviceRegistrations =
				new ConcurrentHashMap<Object, ServiceRegistration<?>>();

			ServletContextHelperProperties schProperties =
				ServletContextHelperProperties.cnv(properties);

			HttpServletContext httpServletContext =
				_liferayHttpService.getServletContext(schProperties);

			generateHttpContext(
				httpServletContext, schProperties.getContextName(), properties);
		}
		catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Deactivate
	protected void deactivate() {
		Iterator<Entry<Object, ServiceRegistration<?>>> iterator =
			_serviceRegistrations.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<Object, ServiceRegistration<?>> entry = iterator.next();

			entry.getValue().unregister();
			iterator.remove();
		}

		_serviceRegistrations.clear();

		_serviceRegistrations = null;
		_componentContext = null;
	}

	@Reference(
		cardinality = ReferenceCardinality.MANDATORY,
		target="(provider=Liferay Inc.)"
	)
	protected void setLiferayHttpService(
		LiferayHttpService liferayHttpService) {

		_liferayHttpService = liferayHttpService;
	}

	protected void unsetLiferayHttpService(
		LiferayHttpService liferayHttpService) {

		_liferayHttpService = null;
	}

	private void generateHttpContext(
		HttpServletContext httpServletContext,
		String contextName, Map<String, Object> properties) {

		synchronized (_httpContextLock) {
			if (httpServletContext != null) {
				_httpContext =
					(HttpContextNameAware)httpServletContext.
						getServletContextHelper();
			}
			else {
				Bundle bundle = _componentContext.getUsingBundle();

				DefaultServletContextHelper httpContext =
					new DefaultServletContextHelper(
						_componentContext.getUsingBundle(), contextName);

				_httpContext = httpContext;

				ServiceRegistration<ServletContextHelper> serviceRegistration =
					bundle.getBundleContext().registerService(
						ServletContextHelper.class, httpContext,
						new UnmodifiableMapDictionary<String, Object>(
							properties));

				_serviceRegistrations.put(httpContext, serviceRegistration);
			}
		}
	}

	private Object _httpContextLock = new Object();
	private ComponentContext _componentContext;
	private HttpContextNameAware _httpContext;
	private LiferayHttpService _liferayHttpService;
	private Map<Object, ServiceRegistration<?>> _serviceRegistrations;

}