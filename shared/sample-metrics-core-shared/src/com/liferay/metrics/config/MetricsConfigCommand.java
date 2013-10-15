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

package com.liferay.metrics.config;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

import java.io.IOException;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

/**
 * @author Miguel Pastor
 */
@Component(
	properties = {
		"osgi.command.function=deleteConfig|showConfig|logMetricsRetrieval",
		"osgi.command.scope=metrics-config"
	},
	provide = Object.class
	)
public class MetricsConfigCommand {

	public void deleteConfig() {
		Configuration configuration = getConfiguration();

		try {
			configuration.delete();
		}
		catch (IOException e) {
			System.err.println(
				"An unexpected error has occurred while deleting the " +
					"configuration " + configuration.getPid());
		}
	}

	public void logMetricsRetrieval(Boolean logMetricsRetrieval) {
		Configuration configuration = getConfiguration();

		_update(configuration, logMetricsRetrieval);
	}

	@Reference(type = '?')
	public void setConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
		this.configurationAdmin = configurationAdmin;
	}

	public void showConfig() {
		Configuration configuration = getConfiguration();

		Dictionary<String, Object> properties = configuration.getProperties();

		Enumeration<String> keys = properties.keys();

		while (keys.hasMoreElements()) {
			String key = keys.nextElement();

			System.out.println(key + "=" + properties.get(key));
		}
	}

	protected ConfigurationAdmin configurationAdmin;

	private void _update(
		Configuration configuration, Boolean logMetricsRetrieval) {

		Dictionary<String, Object> properties = configuration.getProperties();

		if (properties == null) {
			properties = new Hashtable<String, Object>(2);
		}

		properties.put(
			"metrics.provider.log.metrics.retrieval", logMetricsRetrieval);

		try {
			configuration.update(properties);
		}
		catch (IOException e) {
			System.err.println("Configuration couldn't be updated");
		}
	}

	private Configuration getConfiguration() {
		if (configurationAdmin == null) {
			throw new IllegalStateException("Config admin is missing");
		}

		try {
			return configurationAdmin.getConfiguration(
				METRICS_SERVICE_CONFIGURATION);
		}
		catch (IOException e) {
			System.err.println(
				"An unexpected error has occurred while retrieving " +
					"the configuration " + METRICS_SERVICE_CONFIGURATION);
		}

		return null;
	}

	private static final String METRICS_SERVICE_CONFIGURATION =
		"metrics.service.configuration";

}