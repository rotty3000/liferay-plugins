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

package com.liferay.events.manager;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;

import com.liferay.portal.events.EventsProcessorUtil;

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
public class LifecycleEventsManager
	implements ServiceTrackerCustomizer<Object, Object> {

	@Activate
	public void activate(BundleContext bundleContext) throws Exception {
		System.out.println(getClass().getName() + " activated!");

		_bundleContext = bundleContext;

		Filter filter = _bundleContext.createFilter("(lifecycle.event=*)");

		_serviceTracker = new ServiceTracker<Object, Object>(
			_bundleContext, filter, this);

		_serviceTracker.open(true);
	}

	@Deactivate
	public void deactivate() {
		System.out.println(getClass().getName() + " deactivated!");

		_serviceTracker.close();

		_serviceTracker = null;
	}

	public Object addingService(ServiceReference<Object> reference) {
		Object service = _bundleContext.getService(reference);

		String[] keys = (String[])reference.getProperty("lifecycle.event");

		for (String key : keys) {
			EventsProcessorUtil.registerEvent(key, service);
		}

		return service;
	}

	public void modifiedService(
		ServiceReference<Object> reference, Object service) {

		// do nothing
	}

	public void removedService(
		ServiceReference<Object> reference, Object service) {

		String[] keys = (String[])reference.getProperty("lifecycle.event");

		for (String key : keys) {
			EventsProcessorUtil.unregisterEvent(key, service);
		}
	}

	private BundleContext _bundleContext;
	private ServiceTracker<Object, Object> _serviceTracker;

}