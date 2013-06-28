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

package com.liferay.metrics.portlet;

import com.liferay.metrics.api.model.Metric;
import com.liferay.metrics.api.model.MetricsProvider;
import com.liferay.metrics.portlet.internal.MetricsProviderFactory;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.util.bridges.freemarker.FreeMarkerPortlet;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

/**
 * @author Miguel Pastor
 */
public class SampleMetricsPortlet extends FreeMarkerPortlet {

	@Override
	public void serveResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws IOException, PortletException {

		writeJSON(resourceRequest, resourceResponse, collect());
	}

	protected JSONArray collect() {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		for (Metric metric : _metricsProvider.collect()) {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

			jsonObject.put(metric.name(), metric.prettyPrint());

			jsonArray.put(jsonObject);
		}

		return jsonArray;
	}

	private MetricsProvider _metricsProvider =
		MetricsProviderFactory.getMetricsProvider();

}