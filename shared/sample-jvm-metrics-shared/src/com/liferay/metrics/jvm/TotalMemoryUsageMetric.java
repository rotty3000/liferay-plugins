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

import aQute.bnd.annotation.component.Component;

import com.liferay.metrics.api.model.Metric;

/**
 * @author Miguel Pastor
 */
@Component(provide = Metric.class)
public class TotalMemoryUsageMetric extends AbstractMemoryMetric {

	@Override
	public Long data() {
		return memoryMXBean.getHeapMemoryUsage().getUsed() +
			memoryMXBean.getNonHeapMemoryUsage().getUsed();
	}

	@Override
	public String name() {
		return "jvm-total-memory-usage";
	}

}