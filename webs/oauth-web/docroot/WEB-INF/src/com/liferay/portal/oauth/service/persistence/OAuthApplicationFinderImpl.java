package com.liferay.portal.oauth.service.persistence;

import java.util.Iterator;
import java.util.List;

import com.liferay.portal.kernel.dao.orm.QueryDefinition;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.exception.SystemException;

import com.liferay.portal.oauth.model.OAuthApplication;
import com.liferay.portal.oauth.model.impl.OAuthApplicationImpl;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

public class OAuthApplicationFinderImpl
	extends BasePersistenceImpl<OAuthApplication> implements OAuthApplicationFinder{
	public static final String COUNT_ALL =
			OAuthApplicationFinder.class.getName() + ".countAll";
	
	public static final String COUNT_BY_NAME =
			OAuthApplicationFinder.class.getName() + ".countByName";
	
	public static final String COUNT_BY_WEBSITE =
			OAuthApplicationFinder.class.getName() + ".countByWebsite";
	
	public static final String FIND_ALL =
			OAuthApplicationFinder.class.getName() + ".findAll";
	
	public static final String FIND_BY_NAME =
			OAuthApplicationFinder.class.getName() + ".findByName";
	
	public static final String FIND_BY_WEBSITE =
			OAuthApplicationFinder.class.getName() + ".findByWebsite";
	
	public int countAll()
			throws SystemException {
			
			Session session = null;
			try {
				session = openSession();
			
				SQLQuery query = session.createSQLQuery(COUNT_ALL);
				
				query.addScalar(COUNT_COLUMN_NAME, Type.LONG);
				
				Iterator<Long> itr = query.iterate();
				
				if (itr.hasNext()) {
					Long count = itr.next();
					
					if(null != count){
						return count.intValue();
					}
				}
				
				return 0;
			}
			catch(Exception e) {
				throw new SystemException();
			}
			finally {
				closeSession(session);
			}
		}
	
	public int countByName(String name)
		throws SystemException {
		
		Session session = null;
		try {
			session = openSession();
		
			SQLQuery query = session.createSQLQuery(COUNT_BY_NAME);
			
			query.addScalar(COUNT_COLUMN_NAME, Type.LONG);
			
			QueryPos qPos = QueryPos.getInstance(query);
			
			qPos.add(name);
			
			Iterator<Long> itr = query.iterate();
			
			if (itr.hasNext()) {
				Long count = itr.next();
				
				if(null != count){
					return count.intValue();
				}
			}
			
			return 0;
		}
		catch(Exception e) {
			throw new SystemException();
		}
		finally {
			closeSession(session);
		}
	}
	
	public int countByWebsite(String website)
		throws SystemException {
		
		Session session = null;
		try {
			session = openSession();
		
			SQLQuery query = session.createSQLQuery(COUNT_BY_WEBSITE);
			
			query.addScalar(COUNT_COLUMN_NAME, Type.LONG);
			
			QueryPos qPos = QueryPos.getInstance(query);
			
			qPos.add(website);
			
			Iterator<Long> itr = query.iterate();
			
			if (itr.hasNext()) {
				Long count = itr.next();
				
				if(null != count){
					return count.intValue();
				}
			}
			
			return 0;
		}
		catch(Exception e) {
			throw new SystemException();
		}
		finally {
			closeSession(session);
		}
	}
	
	public List<OAuthApplication> findAll()
			throws SystemException {
			Session session = null;
			try {
				session = openSession();
			
				SQLQuery q = session.createSQLQuery(FIND_ALL);
				
				q.addEntity("OAuthApplication", OAuthApplicationImpl.class);
				
				QueryDefinition qDefinition = new QueryDefinition();
				
				return (List<OAuthApplication>)QueryUtil.list(
						q, getDialect(), qDefinition.getStart(),
						qDefinition.getEnd());
			}
			catch(Exception e) {
				throw new SystemException();
			}
			finally {
				closeSession(session);
			}
		}
	
	public List<OAuthApplication> findByName(String name)
		throws SystemException {
		Session session = null;
		try {
			session = openSession();
		
			SQLQuery q = session.createSQLQuery(FIND_BY_NAME);
			
			q.addEntity("OAuthApplication", OAuthApplicationImpl.class);
			
			QueryPos qPos = QueryPos.getInstance(q);
			
			qPos.add(name);
			
			QueryDefinition qDefinition = new QueryDefinition();
			
			return (List<OAuthApplication>)QueryUtil.list(
					q, getDialect(), qDefinition.getStart(),
					qDefinition.getEnd());
		}
		catch(Exception e) {
			throw new SystemException();
		}
		finally {
			closeSession(session);
		}
	}
	
	public List<OAuthApplication> findByWebsite(String website)
		throws SystemException {
		Session session = null;
		try {
			session = openSession();
		
			SQLQuery q = session.createSQLQuery(FIND_BY_WEBSITE);
			
			q.addEntity("OAuthApplication", OAuthApplicationImpl.class);
			
			QueryPos qPos = QueryPos.getInstance(q);
			
			qPos.add(website);
			
			QueryDefinition qDefinition = new QueryDefinition();
			
			return (List<OAuthApplication>)QueryUtil.list(
					q, getDialect(), qDefinition.getStart(),
					qDefinition.getEnd());
		}
		catch(Exception e) {
			throw new SystemException();
		}
		finally {
			closeSession(session);
		}
	}
}
