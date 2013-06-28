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

package com.liferay.osgi.annotations;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.lang.annotation.Annotation;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

/**
 * @author Raymond Augé
 */
@Component(
	immediate=true,
	provide=AnnotatedServiceListener.class
)
public class AnnotatedServiceListener implements ServiceListener {

	@Activate
	public void activate(BundleContext bundleContext) throws Exception {
		_bundleContext = bundleContext;

		_bundleContext.addServiceListener(this);

		if (_log.isDebugEnabled()) {
			_log.debug("Activated!");
		}
	}

	@Deactivate
	public void deactivate() {
		_bundleContext.removeServiceListener(this);

		if (_log.isDebugEnabled()) {
			_log.debug("Deactivated!");
		}
	}

	public void serviceChanged(ServiceEvent event) {
		int type = event.getType();

		System.out.println("Service Event: " + event);

		if (type != ServiceEvent.REGISTERED) {
			return;
		}

		ServiceReference<?> serviceReference = event.getServiceReference();

		Object service = _bundleContext.getService(serviceReference);

		Annotation[] annotations = service.getClass().getDeclaredAnnotations();

		// TODO: not complete
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AnnotatedServiceListener.class);

	private BundleContext _bundleContext;

}