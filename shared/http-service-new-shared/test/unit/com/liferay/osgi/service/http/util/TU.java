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

package com.liferay.osgi.service.http.util;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Raymond Augé
 */
public class TU {

	public static HandyMap serviceMap() {
		return serviceMap(null, null);
	}

	public static HandyMap serviceMap(String key, Object value) {
		HandyMap map = new HandyMap();

		map.put("provider", "Liferay Inc.");
		map.put("service.id",  integer.incrementAndGet());
		map.put("service.pid",  UUID.randomUUID().toString());
		map.put("service.ranking",  0);

		if ((key != null) && (value != null)) {
			map.put(key,  value);
		}

		return map;
	}

	public static class HandyMap extends HashMap<String, Object> {

		public HandyMap add(String key, Object value) {
			put(key, value);

			return this;
		}

	}

	public static final AtomicInteger integer = new AtomicInteger();

}