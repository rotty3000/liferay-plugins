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

package com.liferay.oauth.service.base;

import com.liferay.counter.service.CounterLocalService;

import com.liferay.oauth.model.Application;
import com.liferay.oauth.service.ApplicationLocalService;
import com.liferay.oauth.service.ApplicationUserLocalService;
import com.liferay.oauth.service.persistence.ApplicationPersistence;
import com.liferay.oauth.service.persistence.ApplicationUserFinder;
import com.liferay.oauth.service.persistence.ApplicationUserPersistence;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.bean.IdentifiableBean;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.model.PersistedModel;
import com.liferay.portal.service.BaseLocalServiceImpl;
import com.liferay.portal.service.PersistedModelLocalServiceRegistryUtil;
import com.liferay.portal.service.ResourceLocalService;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.service.UserService;
import com.liferay.portal.service.persistence.UserPersistence;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

/**
 * The base implementation of the application local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.oauth.service.impl.ApplicationLocalServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.oauth.service.impl.ApplicationLocalServiceImpl
 * @see com.liferay.oauth.service.ApplicationLocalServiceUtil
 * @generated
 */
public abstract class ApplicationLocalServiceBaseImpl
	extends BaseLocalServiceImpl implements ApplicationLocalService,
		IdentifiableBean {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.oauth.service.ApplicationLocalServiceUtil} to access the application local service.
	 */

	/**
	 * Adds the application to the database. Also notifies the appropriate model listeners.
	 *
	 * @param application the application
	 * @return the application that was added
	 * @throws SystemException if a system exception occurred
	 */
	@Indexable(type = IndexableType.REINDEX)
	public Application addApplication(Application application)
		throws SystemException {
		application.setNew(true);

		return applicationPersistence.update(application);
	}

	/**
	 * Creates a new application with the primary key. Does not add the application to the database.
	 *
	 * @param applicationId the primary key for the new application
	 * @return the new application
	 */
	public Application createApplication(long applicationId) {
		return applicationPersistence.create(applicationId);
	}

	/**
	 * Deletes the application with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param applicationId the primary key of the application
	 * @return the application that was removed
	 * @throws PortalException if a application with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Indexable(type = IndexableType.DELETE)
	public Application deleteApplication(long applicationId)
		throws PortalException, SystemException {
		return applicationPersistence.remove(applicationId);
	}

	/**
	 * Deletes the application from the database. Also notifies the appropriate model listeners.
	 *
	 * @param application the application
	 * @return the application that was removed
	 * @throws SystemException if a system exception occurred
	 */
	@Indexable(type = IndexableType.DELETE)
	public Application deleteApplication(Application application)
		throws SystemException {
		return applicationPersistence.remove(application);
	}

	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(Application.class,
			clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 * @throws SystemException if a system exception occurred
	 */
	@SuppressWarnings("rawtypes")
	public List dynamicQuery(DynamicQuery dynamicQuery)
		throws SystemException {
		return applicationPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 * @throws SystemException if a system exception occurred
	 */
	@SuppressWarnings("rawtypes")
	public List dynamicQuery(DynamicQuery dynamicQuery, int start, int end)
		throws SystemException {
		return applicationPersistence.findWithDynamicQuery(dynamicQuery, start,
			end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 * @throws SystemException if a system exception occurred
	 */
	@SuppressWarnings("rawtypes")
	public List dynamicQuery(DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		return applicationPersistence.findWithDynamicQuery(dynamicQuery, start,
			end, orderByComparator);
	}

	/**
	 * Returns the number of rows that match the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows that match the dynamic query
	 * @throws SystemException if a system exception occurred
	 */
	public long dynamicQueryCount(DynamicQuery dynamicQuery)
		throws SystemException {
		return applicationPersistence.countWithDynamicQuery(dynamicQuery);
	}

	public Application fetchApplication(long applicationId)
		throws SystemException {
		return applicationPersistence.fetchByPrimaryKey(applicationId);
	}

	/**
	 * Returns the application with the primary key.
	 *
	 * @param applicationId the primary key of the application
	 * @return the application
	 * @throws PortalException if a application with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Application getApplication(long applicationId)
		throws PortalException, SystemException {
		return applicationPersistence.findByPrimaryKey(applicationId);
	}

	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException, SystemException {
		return applicationPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns a range of all the applications.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param start the lower bound of the range of applications
	 * @param end the upper bound of the range of applications (not inclusive)
	 * @return the range of applications
	 * @throws SystemException if a system exception occurred
	 */
	public List<Application> getApplications(int start, int end)
		throws SystemException {
		return applicationPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of applications.
	 *
	 * @return the number of applications
	 * @throws SystemException if a system exception occurred
	 */
	public int getApplicationsCount() throws SystemException {
		return applicationPersistence.countAll();
	}

	/**
	 * Updates the application in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param application the application
	 * @return the application that was updated
	 * @throws SystemException if a system exception occurred
	 */
	@Indexable(type = IndexableType.REINDEX)
	public Application updateApplication(Application application)
		throws SystemException {
		return applicationPersistence.update(application);
	}

	/**
	 * Returns the application local service.
	 *
	 * @return the application local service
	 */
	public ApplicationLocalService getApplicationLocalService() {
		return applicationLocalService;
	}

	/**
	 * Sets the application local service.
	 *
	 * @param applicationLocalService the application local service
	 */
	public void setApplicationLocalService(
		ApplicationLocalService applicationLocalService) {
		this.applicationLocalService = applicationLocalService;
	}

	/**
	 * Returns the application persistence.
	 *
	 * @return the application persistence
	 */
	public ApplicationPersistence getApplicationPersistence() {
		return applicationPersistence;
	}

	/**
	 * Sets the application persistence.
	 *
	 * @param applicationPersistence the application persistence
	 */
	public void setApplicationPersistence(
		ApplicationPersistence applicationPersistence) {
		this.applicationPersistence = applicationPersistence;
	}

	/**
	 * Returns the application user local service.
	 *
	 * @return the application user local service
	 */
	public ApplicationUserLocalService getApplicationUserLocalService() {
		return applicationUserLocalService;
	}

	/**
	 * Sets the application user local service.
	 *
	 * @param applicationUserLocalService the application user local service
	 */
	public void setApplicationUserLocalService(
		ApplicationUserLocalService applicationUserLocalService) {
		this.applicationUserLocalService = applicationUserLocalService;
	}

	/**
	 * Returns the application user persistence.
	 *
	 * @return the application user persistence
	 */
	public ApplicationUserPersistence getApplicationUserPersistence() {
		return applicationUserPersistence;
	}

	/**
	 * Sets the application user persistence.
	 *
	 * @param applicationUserPersistence the application user persistence
	 */
	public void setApplicationUserPersistence(
		ApplicationUserPersistence applicationUserPersistence) {
		this.applicationUserPersistence = applicationUserPersistence;
	}

	/**
	 * Returns the application user finder.
	 *
	 * @return the application user finder
	 */
	public ApplicationUserFinder getApplicationUserFinder() {
		return applicationUserFinder;
	}

	/**
	 * Sets the application user finder.
	 *
	 * @param applicationUserFinder the application user finder
	 */
	public void setApplicationUserFinder(
		ApplicationUserFinder applicationUserFinder) {
		this.applicationUserFinder = applicationUserFinder;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public ResourceLocalService getResourceLocalService() {
		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		ResourceLocalService resourceLocalService) {
		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public UserLocalService getUserLocalService() {
		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user remote service.
	 *
	 * @return the user remote service
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user remote service.
	 *
	 * @param userService the user remote service
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	public void afterPropertiesSet() {
		Class<?> clazz = getClass();

		_classLoader = clazz.getClassLoader();

		PersistedModelLocalServiceRegistryUtil.register("com.liferay.oauth.model.Application",
			applicationLocalService);
	}

	public void destroy() {
		PersistedModelLocalServiceRegistryUtil.unregister(
			"com.liferay.oauth.model.Application");
	}

	/**
	 * Returns the Spring bean ID for this bean.
	 *
	 * @return the Spring bean ID for this bean
	 */
	public String getBeanIdentifier() {
		return _beanIdentifier;
	}

	/**
	 * Sets the Spring bean ID for this bean.
	 *
	 * @param beanIdentifier the Spring bean ID for this bean
	 */
	public void setBeanIdentifier(String beanIdentifier) {
		_beanIdentifier = beanIdentifier;
	}

	public Object invokeMethod(String name, String[] parameterTypes,
		Object[] arguments) throws Throwable {
		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		if (contextClassLoader != _classLoader) {
			currentThread.setContextClassLoader(_classLoader);
		}

		try {
			return _clpInvoker.invokeMethod(name, parameterTypes, arguments);
		}
		finally {
			if (contextClassLoader != _classLoader) {
				currentThread.setContextClassLoader(contextClassLoader);
			}
		}
	}

	protected Class<?> getModelClass() {
		return Application.class;
	}

	protected String getModelClassName() {
		return Application.class.getName();
	}

	/**
	 * Performs an SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) throws SystemException {
		try {
			DataSource dataSource = applicationPersistence.getDataSource();

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(dataSource,
					sql, new int[0]);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = ApplicationLocalService.class)
	protected ApplicationLocalService applicationLocalService;
	@BeanReference(type = ApplicationPersistence.class)
	protected ApplicationPersistence applicationPersistence;
	@BeanReference(type = ApplicationUserLocalService.class)
	protected ApplicationUserLocalService applicationUserLocalService;
	@BeanReference(type = ApplicationUserPersistence.class)
	protected ApplicationUserPersistence applicationUserPersistence;
	@BeanReference(type = ApplicationUserFinder.class)
	protected ApplicationUserFinder applicationUserFinder;
	@BeanReference(type = CounterLocalService.class)
	protected CounterLocalService counterLocalService;
	@BeanReference(type = ResourceLocalService.class)
	protected ResourceLocalService resourceLocalService;
	@BeanReference(type = UserLocalService.class)
	protected UserLocalService userLocalService;
	@BeanReference(type = UserService.class)
	protected UserService userService;
	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	private String _beanIdentifier;
	private ClassLoader _classLoader;
	private ApplicationLocalServiceClpInvoker _clpInvoker = new ApplicationLocalServiceClpInvoker();
}