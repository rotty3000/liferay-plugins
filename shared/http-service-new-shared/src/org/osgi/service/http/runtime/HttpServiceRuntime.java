/*
 * Copyright (c) OSGi Alliance (2012, 2013). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.osgi.service.http.runtime;

import aQute.bnd.annotation.ProviderType;

import java.util.Map;

import org.osgi.service.http.HttpConstants;

/**
 * The {@code HttpServiceRuntime} service represents the runtime information of
 * an Http Service implementation.
 *
 * <p>
 * It provides access to the servlet, listener, servlet filter, or resource
 * servlet services used by the Http Service runtime.
 *
 * @ThreadSafe
 * @author $Id$
 * @since 1.3
 */
@ProviderType
public interface HttpServiceRuntime {
	/**
	 * Returns the attributes of this Http Service runtime.
	 *
	 * <p>
	 * The attributes must always include the
	 * {@link HttpConstants#HTTP_SERVICE_ENDPOINT_ATTRIBUTE osgi.http.endpoint}
	 * attribute.
	 *
	 * @return The attributes of this Http Service runtime.
	 */
	public Map<String, Object> getAttributes();

	/**
	 * Returns the representations of the {@code HttpContext} services used by
	 * this Http Service runtime.
	 *
	 * @return The representations of the http context services used by this
	 *         Http Service runtime. The returned array will never be empty and
	 *         will contain at least the default http context.
	 */
	public HttpContextDTO[] getHttpContextDTOs();
}