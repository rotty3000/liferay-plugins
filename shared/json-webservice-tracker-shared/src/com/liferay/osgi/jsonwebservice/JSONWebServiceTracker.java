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

package com.liferay.osgi.jsonwebservice;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

import com.liferay.portal.jsonwebservice.JSONWebServiceRegistrator;
import com.liferay.portal.kernel.annotation.AnnotationLocator;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.jsonwebservice.JSONWebServiceActionsManagerUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.spring.context.PortalContextLoaderListener;
import com.liferay.portal.util.PropsValues;

import javax.servlet.ServletContext;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Raymond Augé
 */
@Component(
	immediate=true,
	provide=JSONWebServiceTracker.class
)
public class JSONWebServiceTracker
	implements ServiceTrackerCustomizer<Object, Object> {

	@Activate
	public void activate(BundleContext bundleContext) throws Exception {
		if (!PropsValues.JSON_WEB_SERVICE_ENABLED) {
			if (_log.isInfoEnabled()) {
				_log.info("JSON web services are not enabled.");
			}

			return;
		}

		_bundleContext = bundleContext;

		_jsonWebServiceRegistrator = new JSONWebServiceRegistrator();

		Filter filter = _bundleContext.createFilter("(webservice.json=*)");

		_serviceTracker = new ServiceTracker<Object, Object>(
			_bundleContext, filter, this);

		_serviceTracker.open();

		if (_log.isDebugEnabled()) {
			_log.debug("Activated!");
		}
	}

	@Deactivate
	public void deactivate() {
		if (_serviceTracker == null) {
			return;
		}

		_serviceTracker.close();

		_serviceTracker = null;

		if (_log.isDebugEnabled()) {
			_log.debug("Deactivated!");
		}
	}

	public Object addingService(ServiceReference<Object> reference) {
		Object service = _bundleContext.getService(reference);

		if (service == null) {
			return service;
		}

		JSONWebService jsonWebService = AnnotationLocator.locate(
			service.getClass(), JSONWebService.class);

		if (jsonWebService == null) {
			return service;
		}

		String contextPath = GetterUtil.getString(
			reference.getProperty("context.path"),
			PortalContextLoaderListener.getPortalServletContextPath());

		_jsonWebServiceRegistrator.processBean(contextPath, service);

		return service;
	}

	public void modifiedService(
		ServiceReference<Object> reference, Object service) {

		removedService(reference, service);
		addingService(reference);
	}

	public void removedService(
		ServiceReference<Object> reference, Object service) {

		if (service == null) {
			return;
		}

		JSONWebService jsonWebService = AnnotationLocator.locate(
			service.getClass(), JSONWebService.class);

		if (jsonWebService == null) {
			return;
		}

		JSONWebServiceActionsManagerUtil.unregisterJSONWebServiceActions(
			service);
	}

	/**
	 * This reference creates a requirement which forces this component to wait
	 * for the portal's servlet context before it gets fully initialized.
	 */
	@Reference(
		target="(original.bean=*)"
	)
	protected void setServletContext(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	protected void unsetServletContext(ServletContext servletContext) {
		_servletContext = null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		JSONWebServiceTracker.class);

	private BundleContext _bundleContext;
	private JSONWebServiceRegistrator _jsonWebServiceRegistrator;
	private ServiceTracker<Object, Object> _serviceTracker;
	private ServletContext _servletContext;

}