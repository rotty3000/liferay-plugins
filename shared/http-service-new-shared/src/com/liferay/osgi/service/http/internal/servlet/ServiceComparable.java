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

package com.liferay.osgi.service.http.internal.servlet;

import java.util.List;
import java.util.Map;

/**
 * @author Raymond Augé
 */
public class ServiceComparable<T> implements Comparable<ServiceComparable<T>> {

	public ServiceComparable(
		String name, long serviceId, long serviceRanking,
		Map<String, String> parameters, List<String> mappings, T t) {

		_name = name;
		_serviceId = serviceId;
		_serviceRanking = serviceRanking;
		_parameters = parameters;
		_mappins = mappings;
		_t = t;
	}

	public boolean appliesTo(String path) {

		return false;
	}

	@Override
	public int compareTo(ServiceComparable<T> serviceComparable) {
		return new ServiceComparator<T>().compare(this, serviceComparable);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object object) {
		ServiceComparable<T> serviceRanking = (ServiceComparable<T>)object;

		if (_serviceId == serviceRanking.getServiceId()) {
			return true;
		}

		return false;
	}

	public List<String> getMappings() {
		return _mappins;
	}

	public String getName() {
		return _name;
	}

	public Map<String, String> getParameters() {
		return _parameters;
	}

	public long getServiceId() {
		return _serviceId;
	}

	public long getServiceRanking() {
		return _serviceRanking;
	}

	public T getT() {
		return _t;
	}

	private final String _name;
	private final List<String> _mappins;
	private final Map<String, String> _parameters;
	private final long _serviceRanking;
	private final long _serviceId;
	private final T _t;

}