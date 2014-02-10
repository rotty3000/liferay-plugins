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

import com.liferay.osgi.service.http.internal.servlet.ServletProperties;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.http.HttpConstants;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.ServletContextHelper;
import org.osgi.service.http.runtime.HttpServiceRuntime;
import org.osgi.service.http.runtime.ServletContextDTO;

/**
 * @author Raymond Augé
 */
@Component(
	configurationPolicy = ConfigurationPolicy.REQUIRE,
	immediate = true,
	property = {
		"provider=Liferay Inc."
	},
	service = {
		HttpService.class, HttpServlet.class, LiferayHttpService.class
	}
)
@SuppressWarnings("deprecation")
public class LiferayHttpService extends HttpServlet
	implements HttpService, HttpServiceRuntime {

	public LiferayHttpService() throws InvalidSyntaxException {
		_contextMap = new ConcurrentHashMap<String, HttpServletContext>();
		_lock = new ReentrantLock(true);
		_servletContext = new AtomicReference<ServletContext>();
		_serviceRegistrations =
			new ConcurrentHashMap<String, ServiceRegistration<?>>();
	}

	@Deprecated
	@Override
	public HttpContext createDefaultHttpContext() {
		return (HttpContext)_defaultServletContextHelper;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return _httpServiceProperties.getRawProperties();
	}

	@Override
	public ServletContextDTO[] getServletContextDTOs() {
		_lock.lock();

		try {
			List<ServletContextDTO> servletContextDTOs =
				new ArrayList<ServletContextDTO>();

			for (Map.Entry<String, HttpServletContext> entry :
					_contextMap.entrySet()) {

				if (entry.getKey().startsWith(_PATH_PREFIX)) {
					continue;
				}

				HttpServletContext context = entry.getValue();

				servletContextDTOs.add(context.toDTO());
			}

			return servletContextDTOs.toArray(
				new ServletContextDTO[servletContextDTOs.size()]);
		}
		finally {
			_lock.unlock();
		}
	}

	@Deprecated
	@Override
	public void registerResources(
		String alias, String name, HttpContext httpContext) {

		if (name == null) {
			name = "";
		}

		BundleContext bundleContext = _componentContext.getBundleContext();

		String contextName = identifyContextNameLegacy(
			bundleContext, httpContext);

		Hashtable<String,String> properties = new Hashtable<String, String>();

		properties.put(HttpConstants.HTTP_WHITEBOARD_CONTEXT_NAME, contextName);
		properties.put(HttpConstants.HTTP_WHITEBOARD_RESOURCE_PREFIX, name);
		properties.put(HttpConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, alias);

		ServiceRegistration<Servlet> serviceRegistration =
			bundleContext.registerService(
				Servlet.class, new HttpServlet() {/**/}, properties);

		_serviceRegistrations.put(alias, serviceRegistration);
	}

	@Deprecated
	@Override
	@SuppressWarnings("rawtypes")
	public void registerServlet(
		String alias, Servlet servlet, Dictionary initparams,
		HttpContext httpContext) {

		BundleContext bundleContext = _componentContext.getBundleContext();

		String contextName = identifyContextNameLegacy(
			bundleContext, httpContext);

		Hashtable<String, Object> properties = new Hashtable<String, Object>();

		properties.put(HttpConstants.HTTP_WHITEBOARD_CONTEXT_NAME, contextName);
		properties.put(
			HttpConstants.HTTP_WHITEBOARD_SERVLET_ASYNC_SUPPORTED,
			initparams.get(
				HttpConstants.HTTP_WHITEBOARD_SERVLET_ASYNC_SUPPORTED));
		properties.put(
			HttpConstants.HTTP_WHITEBOARD_SERVLET_ERROR_PAGE,
			initparams.get(
				HttpConstants.HTTP_WHITEBOARD_SERVLET_ERROR_PAGE));
		properties.put(
			HttpConstants.HTTP_WHITEBOARD_SERVLET_NAME,
			servlet.getClass().getName());
		properties.put(HttpConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, alias);

		for (Enumeration em = initparams.keys(); em.hasMoreElements();) {
			String key = String.valueOf(em.nextElement());

			properties.put(key, initparams.get(key));
		}

		ServiceRegistration<Servlet> serviceRegistration =
			bundleContext.registerService(Servlet.class, servlet, properties);

		_serviceRegistrations.put(alias, serviceRegistration);
	}

	@Deprecated
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
			ComponentContext componentContext, Map<String, Object> properties)
		throws InvalidSyntaxException {

		_lock.lock();

		try {
			_componentContext = componentContext;

			_httpServiceProperties = HttpServiceProperties.cnv(properties);

			Bundle bundle = _componentContext.getBundleContext().getBundle();

			BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

			HttpServletContext servletContext = new HttpServletContext(
				_servletContext.get(), _defaultServletContextHelper,
				_dschProperties, bundleWiring.getClassLoader(), this);

			_contextMap.put(_NAME_PREFIX, servletContext);
			_contextMap.put(_PATH_PREFIX, servletContext);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			_lock.unlock();
		}
	}

	@Deactivate
	protected void deactivate() {
		_lock.lock();

		try {
			Iterator<Entry<String, ServiceRegistration<?>>> iterator =
				_serviceRegistrations.entrySet().iterator();

			while (iterator.hasNext()) {
				Entry<String, ServiceRegistration<?>> entry = iterator.next();

				entry.getValue().unregister();
				iterator.remove();
			}

			_contextMap.clear();

			_componentContext = null;
		}
		finally {
			_lock.unlock();
		}
	}

	protected String getContextPath() {
		return _httpServiceProperties.getContextPath();
	}

	protected HttpServletContext getServletContext(String contextPath) {
		return _contextMap.get(_PATH_PREFIX.concat(contextPath));
	}

	protected HttpServletContext getServletContext(
		ServletProperties properties) {

		String contextName = properties.getContextName();

		return _contextMap.get(_NAME_PREFIX.concat(contextName));
	}

	@Reference(
		cardinality = ReferenceCardinality.MANDATORY
	)
	protected void setDefaultServletContextHelper(
		DefaultServletContextHelper defaultServletContextHelper,
		Map<String, Object> properties) {

		_lock.lock();

		try {
			_dschProperties = ServletContextHelperProperties.cnv(properties);

			_defaultServletContextHelper = defaultServletContextHelper;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			_lock.unlock();
		}
	}

	@Reference(
		cardinality = ReferenceCardinality.MANDATORY,
		target="(&(bean.id=javax.servlet.ServletContext)(original.bean=*))"
	)
	protected void setSerlvetContext(ServletContext servletContext) {
		_lock.lock();

		try {
			_servletContext.set(servletContext);
		}
		finally {
			_lock.unlock();
		}
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(osgi.http.whiteboard.context.name=*)"
	)
	protected void setServletContextHelper(
		ServletContextHelper servletContextHelper,
		Map<String, Object> properties) {

		_lock.lock();

		try {
			if (_defaultServletContextHelper == servletContextHelper) {
				return;
			}

			ServletContextHelperProperties schProperties =
				ServletContextHelperProperties.cnv(properties);

			String contextName = schProperties.getContextName();
			String contextPath = schProperties.getContextPath();

			if (_contextMap.containsKey(_NAME_PREFIX.concat(contextName)) ||
				_contextMap.containsKey(_PATH_PREFIX.concat(contextPath))) {

				return;
			}

			Bundle bundle = FrameworkUtil.getBundle(
				servletContextHelper.getClass());

			BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

			HttpServletContext servletContext = new HttpServletContext(
				_servletContext.get(), servletContextHelper, schProperties,
				bundleWiring.getClassLoader(), this);

			_contextMap.put(_NAME_PREFIX.concat(contextName), servletContext);
			_contextMap.put(_PATH_PREFIX.concat(contextPath), servletContext);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			_lock.unlock();
		}
	}

	protected void unsetDefaultServletContextHelper(
		DefaultServletContextHelper defaultServletContextHelper) {

		_lock.lock();

		try {
			_contextMap.remove(_NAME_PREFIX);
			_contextMap.remove(_PATH_PREFIX);

			_defaultServletContextHelper = null;
		}
		finally {
			_lock.unlock();
		}
	}

	protected void unsetSerlvetContext(ServletContext servletContext) {
		_lock.lock();

		try {
			_servletContext.set(null);
		}
		finally {
			_lock.unlock();
		}
	}

	protected void unsetServletContextHelper(
		ServletContextHelper servletContextHelper,
		Map<String, Object> properties) {

		_lock.lock();

		try {
			if (_defaultServletContextHelper == servletContextHelper) {
				return;
			}

			ServletContextHelperProperties props =
				ServletContextHelperProperties.cnv(properties);

			_contextMap.remove(_NAME_PREFIX.concat(props.getContextName()));
			_contextMap.remove(_PATH_PREFIX.concat(props.getContextPath()));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			_lock.unlock();
		}
	}

	private String identifyContextNameLegacy(
		BundleContext bundleContext, HttpContext httpContext) {

		if ((httpContext == null) ||
			(httpContext instanceof DefaultServletContextHelper)) {

			return "";
		}

		Bundle bundle = FrameworkUtil.getBundle(httpContext.getClass());

		registerHttpContextLegacy(
			bundleContext, httpContext, bundle.getSymbolicName());

		return bundle.getSymbolicName();
	}

	private void registerHttpContextLegacy(
		BundleContext bundleContext, HttpContext httpContext,
		String contextName) {

		if (_contextMap.containsKey(_NAME_PREFIX.concat(contextName))) {
			return;
		}

		Hashtable<String, Object> properties = new Hashtable<String, Object>();

		properties.put(HttpConstants.HTTP_WHITEBOARD_CONTEXT_NAME, contextName);
		properties.put(
			HttpConstants.HTTP_WHITEBOARD_CONTEXT_SHARED,
			Boolean.FALSE.toString());

		if (!(httpContext instanceof ServletContextHelper)) {
			Bundle bundle = FrameworkUtil.getBundle(httpContext.getClass());

			httpContext = new HttpContextWrapper(httpContext, bundle);
		}

		String[] classNames = new String[] {
			HttpContext.class.getName(),
			ServletContextHelper.class.getName()
		};

		bundleContext.registerService(classNames, httpContext, properties);
	}

	private static final String _PATH_PREFIX = "path://";
	private static final String _NAME_PREFIX = "name://";

	private ComponentContext _componentContext;
	private Map<String, HttpServletContext> _contextMap;
	private ServletContextHelper _defaultServletContextHelper;
	private ServletContextHelperProperties _dschProperties;
	private ReentrantLock _lock;
	private HttpServiceProperties _httpServiceProperties;
	private Map<String, ServiceRegistration<?>> _serviceRegistrations;
	private AtomicReference<ServletContext> _servletContext;

}