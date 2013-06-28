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

package com.liferay.metrics.internal.manager;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

import com.liferay.metrics.api.model.Metric;
import com.liferay.metrics.api.model.MetricsProvider;

import java.util.Date;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

/**
 * @author Miguel Pastor
 */
@Component(
	properties = {"service.pid=metrics.service.configuration"}
)
public class MetricsProviderManager implements ManagedService, MetricsProvider {

	@Override
	public List<Metric> collect() {
		_logMetricsRetrieval();

		return _metrics;
	}

	@Reference(type = '*', unbind = "unregisterMetric")
	public void registerMetric(Metric metric) {
		_metrics.add(metric);
	}

	public void unregisterMetric(Metric metric) {
		_metrics.remove(metric);
	}

	@Override
	public void updated(Dictionary properties) throws ConfigurationException {
		if (properties == null) {
			return;
		}

		_logMetricsRetrieval = (Boolean)properties.get(
			"metrics.provider.log.metrics.retrieval");
	}

	private void _logMetricsRetrieval() {
		if (!_logMetricsRetrieval) {
			return;
		}

		System.out.println("Metrics has been retrieved at " + new Date());
	}

	private static Boolean _logMetricsRetrieval = false;

	private List<Metric> _metrics = new CopyOnWriteArrayList<Metric>();

}