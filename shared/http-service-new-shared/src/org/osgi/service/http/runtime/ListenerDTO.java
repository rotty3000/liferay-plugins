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

/**
 * Represents a listener service used by the Http Service runtime.
 * 
 * @NotThreadSafe
 * @author $Id$
 * @since 1.3
 */
public class ListenerDTO extends DTO {

	/**
	 * The fully qualified type name the listener.
	 */
	public String				type;

	/**
	 * A reference to this service.
	 */
	public ServiceReferenceDTO	serviceReference;
}