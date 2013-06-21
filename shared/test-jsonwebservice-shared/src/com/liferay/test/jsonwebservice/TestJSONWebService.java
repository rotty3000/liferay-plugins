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

package com.liferay.test.jsonwebservice;

import aQute.bnd.annotation.component.Component;

import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.FacetedSearcher;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.facet.AssetEntriesFacet;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.ScopeFacet;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Raymond Augé
 */
@Component(
	immediate=true,
	provide=Object.class
)
@JSONWebService
public class TestJSONWebService {

	public String doSomethingInteresting() {
		return "Something interesting!";
	}

	public Document[] search(String query) throws SearchException {
		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		SearchContext searchContext = new SearchContext();

		QueryConfig queryConfig = new QueryConfig();

		queryConfig.setHighlightEnabled(true);

		searchContext.setCompanyId(serviceContext.getCompanyId());
		searchContext.setQueryConfig(queryConfig);

		List<Indexer> indexers = IndexerRegistryUtil.getIndexers();

		List<String> entryClassNames = new ArrayList<String>();

		for (Indexer indexer : indexers) {
			for (String className : indexer.getClassNames()) {
				entryClassNames.add(className);
			}
		}

		searchContext.setEntryClassNames(
			entryClassNames.toArray(new String[entryClassNames.size()]));

		Facet assetEntriesFacet = new AssetEntriesFacet(searchContext);
		assetEntriesFacet.setStatic(true);
		searchContext.addFacet(assetEntriesFacet);

		Facet scopeFacet = new ScopeFacet(searchContext);
		scopeFacet.setStatic(true);
		searchContext.addFacet(scopeFacet);

		searchContext.setKeywords(query);

		Indexer instance = FacetedSearcher.getInstance();

		Hits hits = instance.search(searchContext);

		return hits.getDocs();
	}

}