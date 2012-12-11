/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.oauth.model;

import com.liferay.portal.model.PersistedModel;

/**
 * The extended model interface for the Application service. Represents a row in the &quot;OAuth_Application&quot; database table, with each column mapped to a property of this class.
 *
 * @author Brian Wing Shun Chan
 * @see ApplicationModel
 * @see com.liferay.oauth.model.impl.ApplicationImpl
 * @see com.liferay.oauth.model.impl.ApplicationModelImpl
 * @generated
 */
public interface Application extends ApplicationModel, PersistedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to {@link com.liferay.oauth.model.impl.ApplicationImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
}