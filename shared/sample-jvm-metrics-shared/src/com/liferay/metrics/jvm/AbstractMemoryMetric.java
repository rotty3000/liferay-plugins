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

package com.liferay.metrics.jvm;

import com.liferay.metrics.api.model.BaseMetric;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

/**
 * @author Miguel Pastor
 */
public abstract class AbstractMemoryMetric extends BaseMetric<Long> {

	@Override
	public String prettyPrint() {
		final long data = data() / _BYTES_IN_A_GIGA;

		return Long.toString(data);
	}

	protected final MemoryMXBean memoryMXBean =
		ManagementFactory.getMemoryMXBean();

	private int _BYTES_IN_A_GIGA = 1024 * 1024;

}