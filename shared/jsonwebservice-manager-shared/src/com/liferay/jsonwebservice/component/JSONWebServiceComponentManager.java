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

package com.liferay.jsonwebservice.component;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;

import com.liferay.portal.jsonwebservice.JSONWebServiceRegistrator;
import com.liferay.portal.kernel.annotation.AnnotationLocator;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.jsonwebservice.JSONWebServiceActionsManagerUtil;
import com.liferay.portal.spring.context.PortalContextLoaderListener;
import com.liferay.portal.util.PropsValues;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Raymond Augé
 */
@Component(
	immediate=true
)
public class JSONWebServiceComponentManager
	implements ServiceTrackerCustomizer<Object, Object> {

	@Activate
	public void activate(BundleContext bundleContext) throws Exception {
		System.out.println(getClass().getName() + " activated!");

		if (!PropsValues.JSON_WEB_SERVICE_ENABLED) {
			System.out.println("JSON web services are not enabled.");

			return;
		}

		_bundleContext = bundleContext;

		_jsonWebServiceRegistrator = new JSONWebServiceRegistrator();

		Filter filter = _bundleContext.createFilter(
			"(&(objectClass=*)(!(original.bean=*)))");

		_serviceTracker = new ServiceTracker<Object, Object>(
			_bundleContext, filter, this);

		_serviceTracker.open(true);
	}

	@Deactivate
	public void deactivate() {
		System.out.println(getClass().getName() + " deactivated!");

		if (_serviceTracker == null) {
			return;
		}

		_serviceTracker.close();

		_serviceTracker = null;
	}

	public Object addingService(ServiceReference<Object> reference) {
		Object service = _bundleContext.getService(reference);

		JSONWebService jsonWebService = AnnotationLocator.locate(
			service.getClass(), JSONWebService.class);

		if (jsonWebService == null) {
			return service;
		}

		try {
			_jsonWebServiceRegistrator.processBean(
				PortalContextLoaderListener.getPortalServletContextPath(),
				service);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return service;
	}

	public void modifiedService(
		ServiceReference<Object> reference, Object service) {

		// do nothing
	}

	public void removedService(
		ServiceReference<Object> reference, Object service) {

		JSONWebServiceActionsManagerUtil.unregisterJSONWebServiceActions(
			service);
	}

	private BundleContext _bundleContext;
	private JSONWebServiceRegistrator _jsonWebServiceRegistrator;
	private ServiceTracker<Object, Object> _serviceTracker;

}