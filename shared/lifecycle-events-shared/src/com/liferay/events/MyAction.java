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

package com.liferay.events;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.service.UserLocalService;

/**
 * @author Raymond Augé
 */
@Component(
	immediate=true,
	properties={
		"lifecycle.event=global.startup.events|application.startup.events|global.shutdown.events|application.shutdown.events|servlet.session.create.events|servlet.session.destroy.events|servlet.service.events.pre|servlet.service.events.post|login.events.pre|login.events.post|logout.events.pre|logout.events.post"
	},
	provide=Object.class
)
public class MyAction extends SimpleAction {

	@Activate
	public void activate() {
		System.out.println(getClass().getName() + " activated!");

		if (_userLocalService == null) {
			System.out.println("No userLocalService was found.");

			return;
		}

		try {
			System.out.println(
				"Liferay has " + _userLocalService.getUsersCount() +
					" users in it's database");
		}
		catch (SystemException e) {
			e.printStackTrace();
		}
	}

	@Deactivate
	public void deactivate() {
		System.out.println(getClass().getName() + " deactivated!");
	}

	@Override
	public void run(String[] ids) throws ActionException {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

		StackTraceElement stackTraceElement = stackTrace[5];

		System.out.println(
			stackTraceElement.getClassName() + " called my action!");

		if (_userLocalService == null) {
			System.out.println("No userLocalService was found.");

			return;
		}

		if ((ids != null) && (ids.length > 0)) {
			for (String id : ids ) {
				long companyId = GetterUtil.getLong(id);

				try {
					int count = _userLocalService.getCompanyUsersCount(
						companyId);

					System.out.println(
						"Liferay has " + count + " users in it's database " +
							"for company " + companyId);
				}
				catch (SystemException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Reference
	public void setUserLocalService(UserLocalService userLocalService) {
		_userLocalService = userLocalService;
	}

	public void unsetUserLocalService(UserLocalService userLocalService) {
		_userLocalService = null;
	}

	private UserLocalService _userLocalService;

}