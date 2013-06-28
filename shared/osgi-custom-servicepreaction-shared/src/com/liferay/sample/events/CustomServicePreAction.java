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

package com.liferay.sample.events;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;

import com.liferay.portal.events.ServicePreAction;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.theme.ThemeDisplay;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Raymond Augé
 */
@Component(
	immediate=true,
	properties={
		"lifecycle.event=servlet.service.events.pre",
		"service.ranking:Integer=100000"
	},
	provide={Action.class, CustomServicePreAction.class, ServicePreAction.class}
)
public class CustomServicePreAction extends ServicePreAction {

	@Activate
	public void activate() {
		System.out.println(getClass().getName() + " activated!");
	}

	@Deactivate
	public void deactivate() {
		System.out.println(getClass().getName() + " deactivated!");
	}

	@Override
	public ThemeDisplay initThemeDisplay(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		System.out.println("BLAM! initThemeDisplay in " + getClass().getName());

		return super.initThemeDisplay(request, response);
	}

}