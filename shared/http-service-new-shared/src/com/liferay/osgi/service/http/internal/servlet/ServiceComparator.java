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

import java.util.Comparator;

/**
 * @author Raymond Augé
 */
public class ServiceComparator<T> implements Comparator<ServiceComparable<T>> {

	@Override
	public int compare(
		ServiceComparable<T> serviceComparable1,
		ServiceComparable<T> serviceComparable2) {

		if (serviceComparable1.getServiceRanking() <
				serviceComparable2.getServiceRanking()) {

			return -1;
		}
		else if (serviceComparable1.getServiceRanking() >
					serviceComparable2.getServiceRanking()) {

			return 1;
		}

		if (serviceComparable1.getServiceId() <
				serviceComparable2.getServiceId()) {

			return -1;
		}
		else if (serviceComparable1.getServiceId() >
					serviceComparable2.getServiceId()) {

			return 1;
		}

		return 0;
	}

}