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

package com.liferay.sample.collaborator;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.jsonwebservice.JSONWebServiceMode;
import com.liferay.sample.jsonwebservice.SampleService;

/**
 * @author Raymond Augé
 */
@Component(
	immediate=true,
	properties={
		"webservice.json=true"
	},
	provide=Collaborator.class
)
@JSONWebService
public class Collaborator {

	@Activate
	@JSONWebService(mode=JSONWebServiceMode.IGNORE)
	public void activate() {
		System.out.println(getClass().getName() + " activated!");
	}

	@Deactivate
	@JSONWebService(mode=JSONWebServiceMode.IGNORE)
	public void deactivate() {
		System.out.println(getClass().getName() + " deactivated!");
	}

	public String collaborate() {
		return _sampleService.doSomethingInteresting() +
			" I have collaborated!";
	}

	@Reference
	protected void setSampleService(SampleService sampleService) {
		_sampleService = sampleService;
	}

	private SampleService _sampleService;

}