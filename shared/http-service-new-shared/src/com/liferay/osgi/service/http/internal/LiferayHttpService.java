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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
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
	implements HttpServiceRuntime {

	public LiferayHttpService() throws InvalidSyntaxException {
		_contextMap = new ConcurrentHashMap<String, HttpServletContext>();
		_lock = new ReentrantLock(true);
		_servletContext = new AtomicReference<ServletContext>();
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

	@Activate
	protected void activate(
			ComponentContext componentContext, Map<String, Object> properties)
		throws InvalidSyntaxException {

		_lock.lock();

		try {
			_componentContext = componentContext;

			_dschProperties = ServletContextHelperProperties.cnv(properties);

			_defaultServletContextHelper = new DefaultServletContextHelper(
				_componentContext.getBundleContext().getBundle(),
				_dschProperties.getContextName());

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
		ServletContextHelperProperties properties) {

		String contextName = properties.getContextName();

		return _contextMap.get(_NAME_PREFIX.concat(contextName));
	}

	protected HttpServletContext getServletContext(
		ServletProperties properties) {

		String contextName = properties.getContextName();

		return _contextMap.get(_NAME_PREFIX.concat(contextName));
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

	private static final String _PATH_PREFIX = "path://";
	private static final String _NAME_PREFIX = "name://";

	private ComponentContext _componentContext;
	private Map<String, HttpServletContext> _contextMap;
	private ServletContextHelper _defaultServletContextHelper;
	private ServletContextHelperProperties _dschProperties;
	private ReentrantLock _lock;
	private HttpServiceProperties _httpServiceProperties;
	private AtomicReference<ServletContext> _servletContext;

}