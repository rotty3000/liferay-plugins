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

package com.liferay.sample.indexer;

import com.liferay.portal.kernel.search.BaseIndexer;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Summary;

import java.util.HashMap;
import java.util.Locale;

import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;

/**
 * @author Raymond Augé
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=58"
	},
	service = Indexer.class
)
public class LoginIndexer extends BaseIndexer {

	private final static String[] _CLASSNAMES = new String[] {
		HashMap.class.getName()};

	@Override
	public String[] getClassNames() {
		System.out.println(getClass() + " getClassNames");

		return _CLASSNAMES;
	}

	@Override
	public String getPortletId() {
		System.out.println(getClass() + " getPortletId");

		return "58";
	}

	@Override
	protected void doDelete(Object object) throws Exception {
		System.out.println(getClass() + " doDelete");
	}

	@Override
	protected Document doGetDocument(Object object) throws Exception {
		System.out.println(getClass() + " doGetDocument");

		return null;
	}

	@Override
	protected Summary doGetSummary(
			Document document, Locale locale, String string,
			PortletURL portletURL)
		throws Exception {

		System.out.println(getClass() + " doGetSummary");

		return null;
	}

	@Override
	protected void doReindex(Object object) throws Exception {
		System.out.println(getClass() + " doReindex");
	}

	@Override
	protected void doReindex(String[] strings) throws Exception {
		System.out.println(getClass() + " doReindex");
	}

	@Override
	protected void doReindex(String string, long l) throws Exception {
		System.out.println(getClass() + " doReindex");
	}

	@Override
	protected String getPortletId(SearchContext searchContext) {
		System.out.println(getClass() + " getPortletId");

		return "58";
	}

}