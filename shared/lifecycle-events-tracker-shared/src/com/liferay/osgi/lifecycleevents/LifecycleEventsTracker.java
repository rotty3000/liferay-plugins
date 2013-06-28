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

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;

import com.liferay.portal.events.EventsProcessor;
import com.liferay.portal.events.EventsProcessorUtil;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.SessionAction;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringBundler;

import java.lang.reflect.Field;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Raymond Augé
 */
@Component(
	immediate=true,
	provide=LifecycleEventsTracker.class
)
public class LifecycleEventsTracker {

	@Activate
	public void activate(BundleContext bundleContext) throws Exception {
		_bundleContext = bundleContext;

		StringBundler sb = new StringBundler(7);

		sb.append("(&(|(objectClass=");
		sb.append(Action.class.getName());
		sb.append(")(objectClass=");
		sb.append(SessionAction.class.getName());
		sb.append(")(objectClass=");
		sb.append(SimpleAction.class.getName());
		sb.append("))(lifecycle.event=*))");

		Filter filter = _bundleContext.createFilter(sb.toString());

		_serviceTracker = new ServiceTracker<Object, Object>(
			_bundleContext, filter, null);

		_serviceTracker.open(true);

		processOriginalEventsProcessor();

		if (_log.isDebugEnabled()) {
			_log.debug("Activated!");
		}
	}

	@Deactivate
	public void deactivate() {
		EventsProcessorUtil.setEventsProcessor(_originalEventsProcessor);

		_newEventsProcessorImpl.clear();

		_newEventsProcessorImpl = null;

		_serviceTracker.close();

		_serviceTracker = null;

		if (_log.isDebugEnabled()) {
			_log.debug("Deactivated!");
		}
	}

	@SuppressWarnings("unchecked")
	protected void processOriginalEventsProcessor()
		throws IllegalAccessException, IllegalArgumentException,
			NoSuchFieldException, SecurityException  {

		Class<?> clazz = EventsProcessorUtil.class;

		Field instanceField = clazz.getDeclaredField("_instance");

		try {
			instanceField.setAccessible(true);

			_originalEventsProcessor = (EventsProcessor)instanceField.get(null);

			clazz = _originalEventsProcessor.getClass();

			Field eventsMapField = clazz.getDeclaredField("_eventsMap");

			try {
				eventsMapField.setAccessible(true);

				synchronized (_originalEventsProcessor) {
					Map<String, List<Object>> eventsMap =
						(Map<String, List<Object>>)eventsMapField.get(
							_originalEventsProcessor);

					registerExistingEvents(eventsMap);
				}
			}
			finally {
				eventsMapField.setAccessible(false);
			}
		}
		finally {
			instanceField.setAccessible(false);
		}
	}

	protected void registerExistingEvents(Map<String, List<Object>> eventsMap) {
		_newEventsProcessorImpl =
			new EventsProcessorImpl(_bundleContext, _serviceTracker);

		for (Map.Entry<String, List<Object>> entry : eventsMap.entrySet()) {
			String key = entry.getKey();
			List<Object> events = entry.getValue();

			Collections.reverse(events);

			for (int i = 0; i < events.size(); i++) {
				Object event = events.get(i);

				Class<?> clazz = event.getClass();

				_newEventsProcessorImpl.registerEvent(
					key, ((i + 1) * 1000), clazz.getName(), event);
			}
		}

		EventsProcessorUtil.setEventsProcessor(
			_newEventsProcessorImpl);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LifecycleEventsTracker.class);

	private BundleContext _bundleContext;
	private EventsProcessorImpl _newEventsProcessorImpl;
	private EventsProcessor _originalEventsProcessor;
	private ServiceTracker<Object, Object> _serviceTracker;

}