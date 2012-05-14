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

package com.liferay.portal.oauth.model.impl;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.oauth.model.OAuthApplications_Users;
import com.liferay.portal.oauth.model.OAuthApplications_UsersModel;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import java.io.Serializable;

import java.sql.Types;

import java.util.HashMap;
import java.util.Map;

/**
 * The base model implementation for the OAuthApplications_Users service. Represents a row in the &quot;OAuthApplications_Users&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.portal.oauth.model.OAuthApplications_UsersModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link OAuthApplications_UsersImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OAuthApplications_UsersImpl
 * @see com.liferay.portal.oauth.model.OAuthApplications_Users
 * @see com.liferay.portal.oauth.model.OAuthApplications_UsersModel
 * @generated
 */
public class OAuthApplications_UsersModelImpl extends BaseModelImpl<OAuthApplications_Users>
	implements OAuthApplications_UsersModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a o auth applications_ users model instance should use the {@link com.liferay.portal.oauth.model.OAuthApplications_Users} interface instead.
	 */
	public static final String TABLE_NAME = "OAuthApplications_Users";
	public static final Object[][] TABLE_COLUMNS = {
			{ "oaauid", Types.BIGINT },
			{ "accessToken", Types.VARCHAR },
			{ "accessSecret", Types.VARCHAR },
			{ "applicationId", Types.BIGINT },
			{ "authorized", Types.BOOLEAN },
			{ "userId", Types.BIGINT }
		};
	public static final String TABLE_SQL_CREATE = "create table OAuthApplications_Users (oaauid LONG not null primary key,accessToken VARCHAR(75) null,accessSecret VARCHAR(75) null,applicationId LONG,authorized BOOLEAN,userId LONG)";
	public static final String TABLE_SQL_DROP = "drop table OAuthApplications_Users";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
				"value.object.entity.cache.enabled.com.liferay.portal.oauth.model.OAuthApplications_Users"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
				"value.object.finder.cache.enabled.com.liferay.portal.oauth.model.OAuthApplications_Users"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
				"value.object.column.bitmask.enabled.com.liferay.portal.oauth.model.OAuthApplications_Users"),
			true);
	public static long ACCESSTOKEN_COLUMN_BITMASK = 1L;
	public static long APPLICATIONID_COLUMN_BITMASK = 2L;
	public static long USERID_COLUMN_BITMASK = 4L;
	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.util.service.ServiceProps.get(
				"lock.expiration.time.com.liferay.portal.oauth.model.OAuthApplications_Users"));

	public OAuthApplications_UsersModelImpl() {
	}

	public long getPrimaryKey() {
		return _oaauid;
	}

	public void setPrimaryKey(long primaryKey) {
		setOaauid(primaryKey);
	}

	public Serializable getPrimaryKeyObj() {
		return new Long(_oaauid);
	}

	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	public Class<?> getModelClass() {
		return OAuthApplications_Users.class;
	}

	public String getModelClassName() {
		return OAuthApplications_Users.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("oaauid", getOaauid());
		attributes.put("accessToken", getAccessToken());
		attributes.put("accessSecret", getAccessSecret());
		attributes.put("applicationId", getApplicationId());
		attributes.put("authorized", getAuthorized());
		attributes.put("userId", getUserId());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long oaauid = (Long)attributes.get("oaauid");

		if (oaauid != null) {
			setOaauid(oaauid);
		}

		String accessToken = (String)attributes.get("accessToken");

		if (accessToken != null) {
			setAccessToken(accessToken);
		}

		String accessSecret = (String)attributes.get("accessSecret");

		if (accessSecret != null) {
			setAccessSecret(accessSecret);
		}

		Long applicationId = (Long)attributes.get("applicationId");

		if (applicationId != null) {
			setApplicationId(applicationId);
		}

		Boolean authorized = (Boolean)attributes.get("authorized");

		if (authorized != null) {
			setAuthorized(authorized);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}
	}

	public long getOaauid() {
		return _oaauid;
	}

	public void setOaauid(long oaauid) {
		_oaauid = oaauid;
	}

	public String getAccessToken() {
		if (_accessToken == null) {
			return StringPool.BLANK;
		}
		else {
			return _accessToken;
		}
	}

	public void setAccessToken(String accessToken) {
		_columnBitmask |= ACCESSTOKEN_COLUMN_BITMASK;

		if (_originalAccessToken == null) {
			_originalAccessToken = _accessToken;
		}

		_accessToken = accessToken;
	}

	public String getOriginalAccessToken() {
		return GetterUtil.getString(_originalAccessToken);
	}

	public String getAccessSecret() {
		if (_accessSecret == null) {
			return StringPool.BLANK;
		}
		else {
			return _accessSecret;
		}
	}

	public void setAccessSecret(String accessSecret) {
		_accessSecret = accessSecret;
	}

	public long getApplicationId() {
		return _applicationId;
	}

	public void setApplicationId(long applicationId) {
		_columnBitmask |= APPLICATIONID_COLUMN_BITMASK;

		if (!_setOriginalApplicationId) {
			_setOriginalApplicationId = true;

			_originalApplicationId = _applicationId;
		}

		_applicationId = applicationId;
	}

	public long getOriginalApplicationId() {
		return _originalApplicationId;
	}

	public boolean getAuthorized() {
		return _authorized;
	}

	public boolean isAuthorized() {
		return _authorized;
	}

	public void setAuthorized(boolean authorized) {
		_authorized = authorized;
	}

	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		_columnBitmask |= USERID_COLUMN_BITMASK;

		if (!_setOriginalUserId) {
			_setOriginalUserId = true;

			_originalUserId = _userId;
		}

		_userId = userId;
	}

	public String getUserUuid() throws SystemException {
		return PortalUtil.getUserValue(getUserId(), "uuid", _userUuid);
	}

	public void setUserUuid(String userUuid) {
		_userUuid = userUuid;
	}

	public long getOriginalUserId() {
		return _originalUserId;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public OAuthApplications_Users toEscapedModel() {
		if (_escapedModelProxy == null) {
			_escapedModelProxy = (OAuthApplications_Users)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelProxyInterfaces,
					new AutoEscapeBeanHandler(this));
		}

		return _escapedModelProxy;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		if (_expandoBridge == null) {
			_expandoBridge = ExpandoBridgeFactoryUtil.getExpandoBridge(0,
					OAuthApplications_Users.class.getName(), getPrimaryKey());
		}

		return _expandoBridge;
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		getExpandoBridge().setAttributes(serviceContext);
	}

	@Override
	public Object clone() {
		OAuthApplications_UsersImpl oAuthApplications_UsersImpl = new OAuthApplications_UsersImpl();

		oAuthApplications_UsersImpl.setOaauid(getOaauid());
		oAuthApplications_UsersImpl.setAccessToken(getAccessToken());
		oAuthApplications_UsersImpl.setAccessSecret(getAccessSecret());
		oAuthApplications_UsersImpl.setApplicationId(getApplicationId());
		oAuthApplications_UsersImpl.setAuthorized(getAuthorized());
		oAuthApplications_UsersImpl.setUserId(getUserId());

		oAuthApplications_UsersImpl.resetOriginalValues();

		return oAuthApplications_UsersImpl;
	}

	public int compareTo(OAuthApplications_Users oAuthApplications_Users) {
		long primaryKey = oAuthApplications_Users.getPrimaryKey();

		if (getPrimaryKey() < primaryKey) {
			return -1;
		}
		else if (getPrimaryKey() > primaryKey) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		OAuthApplications_Users oAuthApplications_Users = null;

		try {
			oAuthApplications_Users = (OAuthApplications_Users)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		long primaryKey = oAuthApplications_Users.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public void resetOriginalValues() {
		OAuthApplications_UsersModelImpl oAuthApplications_UsersModelImpl = this;

		oAuthApplications_UsersModelImpl._originalAccessToken = oAuthApplications_UsersModelImpl._accessToken;

		oAuthApplications_UsersModelImpl._originalApplicationId = oAuthApplications_UsersModelImpl._applicationId;

		oAuthApplications_UsersModelImpl._setOriginalApplicationId = false;

		oAuthApplications_UsersModelImpl._originalUserId = oAuthApplications_UsersModelImpl._userId;

		oAuthApplications_UsersModelImpl._setOriginalUserId = false;

		oAuthApplications_UsersModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<OAuthApplications_Users> toCacheModel() {
		OAuthApplications_UsersCacheModel oAuthApplications_UsersCacheModel = new OAuthApplications_UsersCacheModel();

		oAuthApplications_UsersCacheModel.oaauid = getOaauid();

		oAuthApplications_UsersCacheModel.accessToken = getAccessToken();

		String accessToken = oAuthApplications_UsersCacheModel.accessToken;

		if ((accessToken != null) && (accessToken.length() == 0)) {
			oAuthApplications_UsersCacheModel.accessToken = null;
		}

		oAuthApplications_UsersCacheModel.accessSecret = getAccessSecret();

		String accessSecret = oAuthApplications_UsersCacheModel.accessSecret;

		if ((accessSecret != null) && (accessSecret.length() == 0)) {
			oAuthApplications_UsersCacheModel.accessSecret = null;
		}

		oAuthApplications_UsersCacheModel.applicationId = getApplicationId();

		oAuthApplications_UsersCacheModel.authorized = getAuthorized();

		oAuthApplications_UsersCacheModel.userId = getUserId();

		return oAuthApplications_UsersCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(13);

		sb.append("{oaauid=");
		sb.append(getOaauid());
		sb.append(", accessToken=");
		sb.append(getAccessToken());
		sb.append(", accessSecret=");
		sb.append(getAccessSecret());
		sb.append(", applicationId=");
		sb.append(getApplicationId());
		sb.append(", authorized=");
		sb.append(getAuthorized());
		sb.append(", userId=");
		sb.append(getUserId());
		sb.append("}");

		return sb.toString();
	}

	public String toXmlString() {
		StringBundler sb = new StringBundler(22);

		sb.append("<model><model-name>");
		sb.append("com.liferay.portal.oauth.model.OAuthApplications_Users");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>oaauid</column-name><column-value><![CDATA[");
		sb.append(getOaauid());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>accessToken</column-name><column-value><![CDATA[");
		sb.append(getAccessToken());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>accessSecret</column-name><column-value><![CDATA[");
		sb.append(getAccessSecret());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>applicationId</column-name><column-value><![CDATA[");
		sb.append(getApplicationId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>authorized</column-name><column-value><![CDATA[");
		sb.append(getAuthorized());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userId</column-name><column-value><![CDATA[");
		sb.append(getUserId());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = OAuthApplications_Users.class.getClassLoader();
	private static Class<?>[] _escapedModelProxyInterfaces = new Class[] {
			OAuthApplications_Users.class
		};
	private long _oaauid;
	private String _accessToken;
	private String _originalAccessToken;
	private String _accessSecret;
	private long _applicationId;
	private long _originalApplicationId;
	private boolean _setOriginalApplicationId;
	private boolean _authorized;
	private long _userId;
	private String _userUuid;
	private long _originalUserId;
	private boolean _setOriginalUserId;
	private transient ExpandoBridge _expandoBridge;
	private long _columnBitmask;
	private OAuthApplications_Users _escapedModelProxy;
}