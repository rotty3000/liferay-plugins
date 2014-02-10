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

package com.liferay.osgi.service.http.internal;

import com.liferay.portal.spring.context.PortalContextLoaderListener;

import java.io.IOException;

import java.util.Hashtable;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.http.HttpConstants;

/**
 * @author Raymond Augé
 */
@Component(
	immediate = true
)
public class HttpConfiguration {

	@Activate
	private void activate(ComponentContext componentContext)
		throws IOException {

		Configuration configuration = _configurationAdmin.getConfiguration(
			LiferayHttpService.class.getName());

		Hashtable<String, Object> properties = new Hashtable<String, Object>();

		String portalServletContextPath =
			PortalContextLoaderListener.getPortalServletContextPath();

		properties.put(
			HttpConstants.HTTP_SERVICE_ENDPOINT_ATTRIBUTE,
			portalServletContextPath.concat("/o"));

		configuration.update(properties);
	}

	@Deactivate
	private void deactivate() throws IOException {
		Configuration configuration = _configurationAdmin.getConfiguration(
			LiferayHttpService.class.getName());

		configuration.update(null);
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	private void setConfigAdmin(ConfigurationAdmin configurationAdmin) {
		_configurationAdmin = configurationAdmin;
	}

	@SuppressWarnings("unused")
	private void unsetConfigAdmin(ConfigurationAdmin configurationAdmin) {
		_configurationAdmin = null;
	}

	private ConfigurationAdmin _configurationAdmin;

}