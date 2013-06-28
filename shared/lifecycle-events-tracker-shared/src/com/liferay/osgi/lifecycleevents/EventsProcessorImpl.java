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

package com.liferay.osgi.lifecycleevents;

import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SessionAction;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.InstancePool;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Raymond Augé
 */
public class EventsProcessorImpl
	extends com.liferay.portal.events.EventsProcessorImpl {

	public EventsProcessorImpl(
		BundleContext bundleContext,
		ServiceTracker<Object, Object> serviceTracker) {

		_bundleContext = bundleContext;
		_serviceTracker = serviceTracker;
	}

	public void clear() {
		_tracherCache.clear();

		Iterator<Entry<String, Map<Object, ServiceRegistration<?>>>> iterator =
			_registeredServices.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<String, Map<Object, ServiceRegistration<?>>> entry =
				iterator.next();

			for (ServiceRegistration<?> serviceRegistration :
					entry.getValue().values()) {

				serviceRegistration.unregister();
			}

			iterator.remove();
		}
	}

	@Override
	public void process(
			String key, String[] classes, String[] ids,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session)
		throws ActionException {

		for (int i = (classes.length - 1), j = 1; i >= 0; i--, j++) {
			String className = classes[i];

			if (Validator.isNull(className)) {
				continue;
			}

			try {
				ServiceReference<?>[] serviceReferences =
					_bundleContext.getServiceReferences(
						className,
						"(lifecycle.event=".concat(key).concat(")"));

				if ((serviceReferences != null) &&
					(serviceReferences.length > 0)) {

					continue;
				}
			}
			catch (InvalidSyntaxException e) {
				_log.error(e, e);
			}

			if (_log.isDebugEnabled()) {
				_log.debug("Registering event " + className);
			}

			Object event = InstancePool.get(className);

			registerEvent(key, (j * 1000), className, event);
		}

		Collection<Object> events = _getEvents(key);

		for (Object event : events) {
			processEvent(event, ids, request, response, session);
		}
	}

	@Override
	public void registerEvent(String key, Object event) {
		registerEvent(key, 0, event.getClass().getName(), event);
	}

	public void registerEvent(
		String key, int serviceRank, String className, Object event) {

		Hashtable<String, Object> properties = new Hashtable<String, Object>();

		properties.put("lifecycle.event", key);
		properties.put(Constants.SERVICE_RANKING, serviceRank);

		String eventClassName = _getClassName(event);

		ServiceRegistration<?> registerService = _bundleContext.registerService(
			new String[] {eventClassName, className}, event, properties);

		Map<Object, ServiceRegistration<?>> map = _registeredServices.get(key);

		if (map == null) {
			map = new HashMap<Object, ServiceRegistration<?>>();

			_registeredServices.put(key, map);
		}

		map.put(event, registerService);
	}

	@Override
	public void unregisterEvent(String key, Object event) {
		Map<Object, ServiceRegistration<?>> map = _registeredServices.get(key);

		if ((map == null) || !map.containsKey(event)) {
			return;
		}

		ServiceRegistration<?> serviceRegistration = map.remove(event);

		serviceRegistration.unregister();
	}

	protected String _getClassName(Object event) {
		String className = Action.class.getName();

		if (event instanceof SessionAction) {
			className = SessionAction.class.getName();
		}
		else if (event instanceof SimpleAction) {
			className = SimpleAction.class.getName();
		}

		return className;
	}

	protected Collection<Object> _getEvents(String key) throws ActionException {
		Collection<Object> collection;

		if (_serviceTracker.getTrackingCount() == _trackingCount) {
			collection = _tracherCache.get(key);

			if (collection != null) {
				return collection;
			}

			_lock.lock();
		}
		else {
			_lock.lock();

			_trackingCount = _serviceTracker.getTrackingCount();
			_tracherCache.clear();
		}

		try {
			Filter filter;

			try {
				filter = _bundleContext.createFilter(
					"(lifecycle.event=".concat(key).concat(")"));
			}
			catch (InvalidSyntaxException e) {
				throw new ActionException(e);
			}

			SortedMap<ServiceReference<Object>, Object> tracked =
				_serviceTracker.getTracked();

			collection = new ArrayList<Object>();

			for (Map.Entry<ServiceReference<Object>, Object> entry :
					tracked.entrySet()) {

				if (filter.match(entry.getKey())) {
					collection.add(entry.getValue());
				}
			}

			_tracherCache.put(key, collection);

			return collection;
		}
		finally {
			if (_lock.isLocked()) {
				_lock.unlock();
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		EventsProcessorImpl.class);

	private final ReentrantLock _lock = new ReentrantLock(true);

	private BundleContext _bundleContext;
	private Map<String, Map<Object, ServiceRegistration<?>>>
		_registeredServices =
			new HashMap<String, Map<Object, ServiceRegistration<?>>>();
	private ServiceTracker<Object, Object> _serviceTracker;
	private Map<String, Collection<Object>> _tracherCache =
		new HashMap<String, Collection<Object>>();
	private int _trackingCount = -1;

}