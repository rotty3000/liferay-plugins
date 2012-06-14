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

package com.liferay.portal.oauth.service.persistence;

import com.liferay.portal.oauth.model.OAuthApplications_Users;
import com.liferay.portal.service.persistence.BasePersistence;

/**
 * The persistence interface for the o auth applications_ users service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OAuthApplications_UsersPersistenceImpl
 * @see OAuthApplications_UsersUtil
 * @generated
 */
public interface OAuthApplications_UsersPersistence extends BasePersistence<OAuthApplications_Users> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link OAuthApplications_UsersUtil} to access the o auth applications_ users persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Caches the o auth applications_ users in the entity cache if it is enabled.
	*
	* @param oAuthApplications_Users the o auth applications_ users
	*/
	public void cacheResult(
		com.liferay.portal.oauth.model.OAuthApplications_Users oAuthApplications_Users);

	/**
	* Caches the o auth applications_ userses in the entity cache if it is enabled.
	*
	* @param oAuthApplications_Userses the o auth applications_ userses
	*/
	public void cacheResult(
		java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> oAuthApplications_Userses);

	/**
	* Creates a new o auth applications_ users with the primary key. Does not add the o auth applications_ users to the database.
	*
	* @param oaauid the primary key for the new o auth applications_ users
	* @return the new o auth applications_ users
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users create(
		long oaauid);

	/**
	* Removes the o auth applications_ users with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param oaauid the primary key of the o auth applications_ users
	* @return the o auth applications_ users that was removed
	* @throws com.liferay.portal.oauth.NoSuchApplications_UsersException if a o auth applications_ users with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users remove(
		long oaauid)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.oauth.NoSuchApplications_UsersException;

	public com.liferay.portal.oauth.model.OAuthApplications_Users updateImpl(
		com.liferay.portal.oauth.model.OAuthApplications_Users oAuthApplications_Users,
		boolean merge)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the o auth applications_ users with the primary key or throws a {@link com.liferay.portal.oauth.NoSuchApplications_UsersException} if it could not be found.
	*
	* @param oaauid the primary key of the o auth applications_ users
	* @return the o auth applications_ users
	* @throws com.liferay.portal.oauth.NoSuchApplications_UsersException if a o auth applications_ users with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users findByPrimaryKey(
		long oaauid)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.oauth.NoSuchApplications_UsersException;

	/**
	* Returns the o auth applications_ users with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param oaauid the primary key of the o auth applications_ users
	* @return the o auth applications_ users, or <code>null</code> if a o auth applications_ users with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users fetchByPrimaryKey(
		long oaauid) throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the o auth applications_ users where applicationId = &#63; and userId = &#63; or throws a {@link com.liferay.portal.oauth.NoSuchApplications_UsersException} if it could not be found.
	*
	* @param applicationId the application ID
	* @param userId the user ID
	* @return the matching o auth applications_ users
	* @throws com.liferay.portal.oauth.NoSuchApplications_UsersException if a matching o auth applications_ users could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users findByA_U(
		long applicationId, long userId)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.oauth.NoSuchApplications_UsersException;

	/**
	* Returns the o auth applications_ users where applicationId = &#63; and userId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param applicationId the application ID
	* @param userId the user ID
	* @return the matching o auth applications_ users, or <code>null</code> if a matching o auth applications_ users could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users fetchByA_U(
		long applicationId, long userId)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the o auth applications_ users where applicationId = &#63; and userId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	*
	* @param applicationId the application ID
	* @param userId the user ID
	* @param retrieveFromCache whether to use the finder cache
	* @return the matching o auth applications_ users, or <code>null</code> if a matching o auth applications_ users could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users fetchByA_U(
		long applicationId, long userId, boolean retrieveFromCache)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns all the o auth applications_ userses where applicationId = &#63; and authorized = &#63;.
	*
	* @param applicationId the application ID
	* @param authorized the authorized
	* @return the matching o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> findByA_A(
		long applicationId, boolean authorized)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns a range of all the o auth applications_ userses where applicationId = &#63; and authorized = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param applicationId the application ID
	* @param authorized the authorized
	* @param start the lower bound of the range of o auth applications_ userses
	* @param end the upper bound of the range of o auth applications_ userses (not inclusive)
	* @return the range of matching o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> findByA_A(
		long applicationId, boolean authorized, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns an ordered range of all the o auth applications_ userses where applicationId = &#63; and authorized = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param applicationId the application ID
	* @param authorized the authorized
	* @param start the lower bound of the range of o auth applications_ userses
	* @param end the upper bound of the range of o auth applications_ userses (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> findByA_A(
		long applicationId, boolean authorized, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the first o auth applications_ users in the ordered set where applicationId = &#63; and authorized = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param applicationId the application ID
	* @param authorized the authorized
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching o auth applications_ users
	* @throws com.liferay.portal.oauth.NoSuchApplications_UsersException if a matching o auth applications_ users could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users findByA_A_First(
		long applicationId, boolean authorized,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.oauth.NoSuchApplications_UsersException;

	/**
	* Returns the last o auth applications_ users in the ordered set where applicationId = &#63; and authorized = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param applicationId the application ID
	* @param authorized the authorized
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching o auth applications_ users
	* @throws com.liferay.portal.oauth.NoSuchApplications_UsersException if a matching o auth applications_ users could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users findByA_A_Last(
		long applicationId, boolean authorized,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.oauth.NoSuchApplications_UsersException;

	/**
	* Returns the o auth applications_ userses before and after the current o auth applications_ users in the ordered set where applicationId = &#63; and authorized = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param oaauid the primary key of the current o auth applications_ users
	* @param applicationId the application ID
	* @param authorized the authorized
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next o auth applications_ users
	* @throws com.liferay.portal.oauth.NoSuchApplications_UsersException if a o auth applications_ users with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users[] findByA_A_PrevAndNext(
		long oaauid, long applicationId, boolean authorized,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.oauth.NoSuchApplications_UsersException;

	/**
	* Returns all the o auth applications_ userses that the user has permission to view where applicationId = &#63; and authorized = &#63;.
	*
	* @param applicationId the application ID
	* @param authorized the authorized
	* @return the matching o auth applications_ userses that the user has permission to view
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> filterFindByA_A(
		long applicationId, boolean authorized)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns a range of all the o auth applications_ userses that the user has permission to view where applicationId = &#63; and authorized = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param applicationId the application ID
	* @param authorized the authorized
	* @param start the lower bound of the range of o auth applications_ userses
	* @param end the upper bound of the range of o auth applications_ userses (not inclusive)
	* @return the range of matching o auth applications_ userses that the user has permission to view
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> filterFindByA_A(
		long applicationId, boolean authorized, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns an ordered range of all the o auth applications_ userses that the user has permissions to view where applicationId = &#63; and authorized = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param applicationId the application ID
	* @param authorized the authorized
	* @param start the lower bound of the range of o auth applications_ userses
	* @param end the upper bound of the range of o auth applications_ userses (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching o auth applications_ userses that the user has permission to view
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> filterFindByA_A(
		long applicationId, boolean authorized, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the o auth applications_ userses before and after the current o auth applications_ users in the ordered set of o auth applications_ userses that the user has permission to view where applicationId = &#63; and authorized = &#63;.
	*
	* @param oaauid the primary key of the current o auth applications_ users
	* @param applicationId the application ID
	* @param authorized the authorized
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next o auth applications_ users
	* @throws com.liferay.portal.oauth.NoSuchApplications_UsersException if a o auth applications_ users with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users[] filterFindByA_A_PrevAndNext(
		long oaauid, long applicationId, boolean authorized,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.oauth.NoSuchApplications_UsersException;

	/**
	* Returns all the o auth applications_ userses where accessToken = &#63;.
	*
	* @param accessToken the access token
	* @return the matching o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> findByAccessToken(
		java.lang.String accessToken)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns a range of all the o auth applications_ userses where accessToken = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param accessToken the access token
	* @param start the lower bound of the range of o auth applications_ userses
	* @param end the upper bound of the range of o auth applications_ userses (not inclusive)
	* @return the range of matching o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> findByAccessToken(
		java.lang.String accessToken, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns an ordered range of all the o auth applications_ userses where accessToken = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param accessToken the access token
	* @param start the lower bound of the range of o auth applications_ userses
	* @param end the upper bound of the range of o auth applications_ userses (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> findByAccessToken(
		java.lang.String accessToken, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the first o auth applications_ users in the ordered set where accessToken = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param accessToken the access token
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching o auth applications_ users
	* @throws com.liferay.portal.oauth.NoSuchApplications_UsersException if a matching o auth applications_ users could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users findByAccessToken_First(
		java.lang.String accessToken,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.oauth.NoSuchApplications_UsersException;

	/**
	* Returns the last o auth applications_ users in the ordered set where accessToken = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param accessToken the access token
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching o auth applications_ users
	* @throws com.liferay.portal.oauth.NoSuchApplications_UsersException if a matching o auth applications_ users could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users findByAccessToken_Last(
		java.lang.String accessToken,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.oauth.NoSuchApplications_UsersException;

	/**
	* Returns the o auth applications_ userses before and after the current o auth applications_ users in the ordered set where accessToken = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param oaauid the primary key of the current o auth applications_ users
	* @param accessToken the access token
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next o auth applications_ users
	* @throws com.liferay.portal.oauth.NoSuchApplications_UsersException if a o auth applications_ users with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users[] findByAccessToken_PrevAndNext(
		long oaauid, java.lang.String accessToken,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.oauth.NoSuchApplications_UsersException;

	/**
	* Returns all the o auth applications_ userses that the user has permission to view where accessToken = &#63;.
	*
	* @param accessToken the access token
	* @return the matching o auth applications_ userses that the user has permission to view
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> filterFindByAccessToken(
		java.lang.String accessToken)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns a range of all the o auth applications_ userses that the user has permission to view where accessToken = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param accessToken the access token
	* @param start the lower bound of the range of o auth applications_ userses
	* @param end the upper bound of the range of o auth applications_ userses (not inclusive)
	* @return the range of matching o auth applications_ userses that the user has permission to view
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> filterFindByAccessToken(
		java.lang.String accessToken, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns an ordered range of all the o auth applications_ userses that the user has permissions to view where accessToken = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param accessToken the access token
	* @param start the lower bound of the range of o auth applications_ userses
	* @param end the upper bound of the range of o auth applications_ userses (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching o auth applications_ userses that the user has permission to view
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> filterFindByAccessToken(
		java.lang.String accessToken, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the o auth applications_ userses before and after the current o auth applications_ users in the ordered set of o auth applications_ userses that the user has permission to view where accessToken = &#63;.
	*
	* @param oaauid the primary key of the current o auth applications_ users
	* @param accessToken the access token
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next o auth applications_ users
	* @throws com.liferay.portal.oauth.NoSuchApplications_UsersException if a o auth applications_ users with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users[] filterFindByAccessToken_PrevAndNext(
		long oaauid, java.lang.String accessToken,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.oauth.NoSuchApplications_UsersException;

	/**
	* Returns all the o auth applications_ userses where applicationId = &#63;.
	*
	* @param applicationId the application ID
	* @return the matching o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> findByApplicationId(
		long applicationId)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns a range of all the o auth applications_ userses where applicationId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param applicationId the application ID
	* @param start the lower bound of the range of o auth applications_ userses
	* @param end the upper bound of the range of o auth applications_ userses (not inclusive)
	* @return the range of matching o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> findByApplicationId(
		long applicationId, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns an ordered range of all the o auth applications_ userses where applicationId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param applicationId the application ID
	* @param start the lower bound of the range of o auth applications_ userses
	* @param end the upper bound of the range of o auth applications_ userses (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> findByApplicationId(
		long applicationId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the first o auth applications_ users in the ordered set where applicationId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param applicationId the application ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching o auth applications_ users
	* @throws com.liferay.portal.oauth.NoSuchApplications_UsersException if a matching o auth applications_ users could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users findByApplicationId_First(
		long applicationId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.oauth.NoSuchApplications_UsersException;

	/**
	* Returns the last o auth applications_ users in the ordered set where applicationId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param applicationId the application ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching o auth applications_ users
	* @throws com.liferay.portal.oauth.NoSuchApplications_UsersException if a matching o auth applications_ users could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users findByApplicationId_Last(
		long applicationId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.oauth.NoSuchApplications_UsersException;

	/**
	* Returns the o auth applications_ userses before and after the current o auth applications_ users in the ordered set where applicationId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param oaauid the primary key of the current o auth applications_ users
	* @param applicationId the application ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next o auth applications_ users
	* @throws com.liferay.portal.oauth.NoSuchApplications_UsersException if a o auth applications_ users with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users[] findByApplicationId_PrevAndNext(
		long oaauid, long applicationId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.oauth.NoSuchApplications_UsersException;

	/**
	* Returns all the o auth applications_ userses that the user has permission to view where applicationId = &#63;.
	*
	* @param applicationId the application ID
	* @return the matching o auth applications_ userses that the user has permission to view
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> filterFindByApplicationId(
		long applicationId)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns a range of all the o auth applications_ userses that the user has permission to view where applicationId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param applicationId the application ID
	* @param start the lower bound of the range of o auth applications_ userses
	* @param end the upper bound of the range of o auth applications_ userses (not inclusive)
	* @return the range of matching o auth applications_ userses that the user has permission to view
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> filterFindByApplicationId(
		long applicationId, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns an ordered range of all the o auth applications_ userses that the user has permissions to view where applicationId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param applicationId the application ID
	* @param start the lower bound of the range of o auth applications_ userses
	* @param end the upper bound of the range of o auth applications_ userses (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching o auth applications_ userses that the user has permission to view
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> filterFindByApplicationId(
		long applicationId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the o auth applications_ userses before and after the current o auth applications_ users in the ordered set of o auth applications_ userses that the user has permission to view where applicationId = &#63;.
	*
	* @param oaauid the primary key of the current o auth applications_ users
	* @param applicationId the application ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next o auth applications_ users
	* @throws com.liferay.portal.oauth.NoSuchApplications_UsersException if a o auth applications_ users with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users[] filterFindByApplicationId_PrevAndNext(
		long oaauid, long applicationId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.oauth.NoSuchApplications_UsersException;

	/**
	* Returns all the o auth applications_ userses where userId = &#63;.
	*
	* @param userId the user ID
	* @return the matching o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> findByUserId(
		long userId) throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns a range of all the o auth applications_ userses where userId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param userId the user ID
	* @param start the lower bound of the range of o auth applications_ userses
	* @param end the upper bound of the range of o auth applications_ userses (not inclusive)
	* @return the range of matching o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> findByUserId(
		long userId, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns an ordered range of all the o auth applications_ userses where userId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param userId the user ID
	* @param start the lower bound of the range of o auth applications_ userses
	* @param end the upper bound of the range of o auth applications_ userses (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> findByUserId(
		long userId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the first o auth applications_ users in the ordered set where userId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param userId the user ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching o auth applications_ users
	* @throws com.liferay.portal.oauth.NoSuchApplications_UsersException if a matching o auth applications_ users could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users findByUserId_First(
		long userId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.oauth.NoSuchApplications_UsersException;

	/**
	* Returns the last o auth applications_ users in the ordered set where userId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param userId the user ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching o auth applications_ users
	* @throws com.liferay.portal.oauth.NoSuchApplications_UsersException if a matching o auth applications_ users could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users findByUserId_Last(
		long userId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.oauth.NoSuchApplications_UsersException;

	/**
	* Returns the o auth applications_ userses before and after the current o auth applications_ users in the ordered set where userId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param oaauid the primary key of the current o auth applications_ users
	* @param userId the user ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next o auth applications_ users
	* @throws com.liferay.portal.oauth.NoSuchApplications_UsersException if a o auth applications_ users with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users[] findByUserId_PrevAndNext(
		long oaauid, long userId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.oauth.NoSuchApplications_UsersException;

	/**
	* Returns all the o auth applications_ userses that the user has permission to view where userId = &#63;.
	*
	* @param userId the user ID
	* @return the matching o auth applications_ userses that the user has permission to view
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> filterFindByUserId(
		long userId) throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns a range of all the o auth applications_ userses that the user has permission to view where userId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param userId the user ID
	* @param start the lower bound of the range of o auth applications_ userses
	* @param end the upper bound of the range of o auth applications_ userses (not inclusive)
	* @return the range of matching o auth applications_ userses that the user has permission to view
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> filterFindByUserId(
		long userId, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns an ordered range of all the o auth applications_ userses that the user has permissions to view where userId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param userId the user ID
	* @param start the lower bound of the range of o auth applications_ userses
	* @param end the upper bound of the range of o auth applications_ userses (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching o auth applications_ userses that the user has permission to view
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> filterFindByUserId(
		long userId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the o auth applications_ userses before and after the current o auth applications_ users in the ordered set of o auth applications_ userses that the user has permission to view where userId = &#63;.
	*
	* @param oaauid the primary key of the current o auth applications_ users
	* @param userId the user ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next o auth applications_ users
	* @throws com.liferay.portal.oauth.NoSuchApplications_UsersException if a o auth applications_ users with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users[] filterFindByUserId_PrevAndNext(
		long oaauid, long userId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.oauth.NoSuchApplications_UsersException;

	/**
	* Returns all the o auth applications_ userses.
	*
	* @return the o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> findAll()
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns a range of all the o auth applications_ userses.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param start the lower bound of the range of o auth applications_ userses
	* @param end the upper bound of the range of o auth applications_ userses (not inclusive)
	* @return the range of o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> findAll(
		int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns an ordered range of all the o auth applications_ userses.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param start the lower bound of the range of o auth applications_ userses
	* @param end the upper bound of the range of o auth applications_ userses (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.liferay.portal.oauth.model.OAuthApplications_Users> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes the o auth applications_ users where applicationId = &#63; and userId = &#63; from the database.
	*
	* @param applicationId the application ID
	* @param userId the user ID
	* @return the o auth applications_ users that was removed
	* @throws SystemException if a system exception occurred
	*/
	public com.liferay.portal.oauth.model.OAuthApplications_Users removeByA_U(
		long applicationId, long userId)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.liferay.portal.oauth.NoSuchApplications_UsersException;

	/**
	* Removes all the o auth applications_ userses where applicationId = &#63; and authorized = &#63; from the database.
	*
	* @param applicationId the application ID
	* @param authorized the authorized
	* @throws SystemException if a system exception occurred
	*/
	public void removeByA_A(long applicationId, boolean authorized)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes all the o auth applications_ userses where accessToken = &#63; from the database.
	*
	* @param accessToken the access token
	* @throws SystemException if a system exception occurred
	*/
	public void removeByAccessToken(java.lang.String accessToken)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes all the o auth applications_ userses where applicationId = &#63; from the database.
	*
	* @param applicationId the application ID
	* @throws SystemException if a system exception occurred
	*/
	public void removeByApplicationId(long applicationId)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes all the o auth applications_ userses where userId = &#63; from the database.
	*
	* @param userId the user ID
	* @throws SystemException if a system exception occurred
	*/
	public void removeByUserId(long userId)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes all the o auth applications_ userses from the database.
	*
	* @throws SystemException if a system exception occurred
	*/
	public void removeAll()
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of o auth applications_ userses where applicationId = &#63; and userId = &#63;.
	*
	* @param applicationId the application ID
	* @param userId the user ID
	* @return the number of matching o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public int countByA_U(long applicationId, long userId)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of o auth applications_ userses where applicationId = &#63; and authorized = &#63;.
	*
	* @param applicationId the application ID
	* @param authorized the authorized
	* @return the number of matching o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public int countByA_A(long applicationId, boolean authorized)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of o auth applications_ userses that the user has permission to view where applicationId = &#63; and authorized = &#63;.
	*
	* @param applicationId the application ID
	* @param authorized the authorized
	* @return the number of matching o auth applications_ userses that the user has permission to view
	* @throws SystemException if a system exception occurred
	*/
	public int filterCountByA_A(long applicationId, boolean authorized)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of o auth applications_ userses where accessToken = &#63;.
	*
	* @param accessToken the access token
	* @return the number of matching o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public int countByAccessToken(java.lang.String accessToken)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of o auth applications_ userses that the user has permission to view where accessToken = &#63;.
	*
	* @param accessToken the access token
	* @return the number of matching o auth applications_ userses that the user has permission to view
	* @throws SystemException if a system exception occurred
	*/
	public int filterCountByAccessToken(java.lang.String accessToken)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of o auth applications_ userses where applicationId = &#63;.
	*
	* @param applicationId the application ID
	* @return the number of matching o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public int countByApplicationId(long applicationId)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of o auth applications_ userses that the user has permission to view where applicationId = &#63;.
	*
	* @param applicationId the application ID
	* @return the number of matching o auth applications_ userses that the user has permission to view
	* @throws SystemException if a system exception occurred
	*/
	public int filterCountByApplicationId(long applicationId)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of o auth applications_ userses where userId = &#63;.
	*
	* @param userId the user ID
	* @return the number of matching o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public int countByUserId(long userId)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of o auth applications_ userses that the user has permission to view where userId = &#63;.
	*
	* @param userId the user ID
	* @return the number of matching o auth applications_ userses that the user has permission to view
	* @throws SystemException if a system exception occurred
	*/
	public int filterCountByUserId(long userId)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of o auth applications_ userses.
	*
	* @return the number of o auth applications_ userses
	* @throws SystemException if a system exception occurred
	*/
	public int countAll()
		throws com.liferay.portal.kernel.exception.SystemException;
}