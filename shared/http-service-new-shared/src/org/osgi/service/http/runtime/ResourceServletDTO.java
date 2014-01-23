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

import org.osgi.dto.DTO;
import org.osgi.dto.framework.ServiceReferenceDTO;
import org.osgi.service.http.HttpConstants;
import org.osgi.service.http.ResourceServlet;

/**
 * Represents a {@link ResourceServlet} servlet service used by the Http Service
 * runtime.
 * 
 * @NotThreadSafe
 * @author $Id: 50344e5333633271854796f102773c390a567f87 $
 * @since 1.3
 */
public class ResourceServletDTO extends DTO {
	/**
	 * The name of the resource servlet.
	 * 
	 * @see HttpConstants#HTTP_WHITEBOARD_SERVLET_NAME
	 */
	public String				name;

	/**
	 * The request mappings for the resource servlet.
	 * 
	 * <p>
	 * The specified patterns are used to determine whether a request should be
	 * mapped to the resource servlet.
	 * 
	 * @see HttpConstants#HTTP_WHITEBOARD_SERVLET_PATTERN
	 */
	public String[]				pattern;

	/**
	 * The prefix of the resource servlet.
	 * 
	 * @see HttpConstants#HTTP_WHITEBOARD_RESOURCE_PREFIX
	 */
	public String				prefix;

	/**
	 * A reference to this service.
	 */
	public ServiceReferenceDTO	serviceReference;
}
