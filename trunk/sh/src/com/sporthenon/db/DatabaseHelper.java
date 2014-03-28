package com.sporthenon.db;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;

import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.HallOfFame;
import com.sporthenon.db.entity.League;
import com.sporthenon.db.entity.OlympicRanking;
import com.sporthenon.db.entity.Olympics;
import com.sporthenon.db.entity.Record;
import com.sporthenon.db.entity.Result;
import com.sporthenon.db.entity.RetiredNumber;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.State;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.TeamStadium;
import com.sporthenon.db.entity.Type;
import com.sporthenon.db.entity.WinLoss;
import com.sporthenon.db.entity.Year;
import com.sporthenon.db.entity.meta.Member;
import com.sporthenon.db.entity.meta.Metadata;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.StringUtils;

public class DatabaseHelper {

	private static EntityManagerFactory emf;

	protected static Logger logger = Logger.getLogger("sh");

	public static final short FIRST = 0;
	public static final short PREVIOUS = 1;
	public static final short NEXT = 2;
	public static final short LAST = 3;

	static {
		try {
			String env = ConfigUtils.getProperty("env");
			if (!env.equalsIgnoreCase("standalone"))
				setFactory(null, null);
		}
		catch (Exception e) {
			Logger.getLogger("sh").fatal(e.getMessage(), e);
		}
	}

	public static void setFactory(Map<String, String> params, String env) throws Exception {
		try {
			String env_ = (env != null ? env : ConfigUtils.getProperty("env"));
			emf = Persistence.createEntityManagerFactory("shunit-" + env_, params);
		}
		catch (Exception e) {
			Logger.getLogger("sh").fatal(e.getMessage(), e);
		}
	}

	public static void unsetFactory() {
		emf = null;
	}

	private static UserTransaction getTransaction() {
		try {
			return (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");	
		}
		catch (NamingException e) {
			return null;
		}
	}

	private static EntityManager getEntityManager() {
		return emf.createEntityManager();
	}

	public static Collection call(String name, Collection<Object> params) throws Exception {
		UserTransaction tr = null;
		try {
			tr = getTransaction();
			EntityManager em = getEntityManager();
			if (tr != null) tr.begin(); else em.getTransaction().begin();
			Query query = em.createNamedQuery(name);
			int i = 0;
			if (params != null)
				for (Object obj : params)
					query.setParameter(++i, obj);
			Collection c = query.getResultList();
			if (tr != null) tr.commit(); else em.getTransaction().commit();
			return c;
		}
		catch (Exception e) {
			if (tr != null)
				tr.rollback();
			throw e;
		}
	}

	public static Collection<PicklistBean> getPicklist(Class source, String target, String filter, String concat, Object order) throws Exception {
		String hql = "select distinct x." + target + ".id, " + (concat != null ? concat + " || " : "") + "x." + target + ".label" + (order instanceof String ? ", " + order : "");
		hql += " from " + source.getSimpleName() + " x";
		hql += (filter != null && !filter.equals("") ? " where " + filter : "");
		hql += " order by " + order;
		return getPicklistFromQuery(hql, false);
	}

	public static Collection<PicklistBean> getEntityPicklist(Class entity, String label, String param) throws Exception {
		label = (label != null ? label : "label");
		param = (param != null ? "," + param : "");
		String hql = "select id," + label + param + " from " + entity.getSimpleName() + " order by " + label + param;
		return getPicklistFromQuery(hql, false);
	}

	public static Collection<PicklistBean> getPicklistFromQuery(String s, boolean native_) throws Exception {
		UserTransaction tr = null;
		try {
			tr = getTransaction();
			if (tr != null) tr.begin();
			EntityManager em = getEntityManager();
			ArrayList<PicklistBean> lPicklist = new ArrayList<PicklistBean>();
			Query query = (native_ ? em.createNativeQuery(s) : em.createQuery(s));
			Collection<Object[]> cResult = query.getResultList();
			for (Object[] tObj : cResult)
				if (tObj[0] != null)
					lPicklist.add(new PicklistBean(new Integer(tObj[0].toString()), tObj[1].toString(), tObj.length > 2 ? tObj[2].toString() : null));
			if (tr != null) tr.commit();
			return lPicklist;
		}
		catch (Exception e) {
			if (tr != null)
				tr.rollback();
			throw e;
		}
	}

	public static Object loadEntity(Class entity, Object id) throws Exception {
		UserTransaction tr = null;
		try {
			if (id != null) {
				tr = getTransaction();
				if (tr != null) tr.begin();
				Object o = getEntityManager().find(entity, new Integer(String.valueOf(id)));
				if (tr != null) tr.commit();
				return o;
			}
		}
		catch (Exception e) {
			if (tr != null)
				tr.rollback();
			throw e;
		}
		return null;
	}
	
	public static Object loadEntityFromQuery(String hql) throws Exception {
		try {
			List<Object> list = execute(hql);
			if (list != null && !list.isEmpty())
				return list.get(0);
		}
		catch (Exception e) {
			throw e;
		}
		return null;
	}

	public static Object saveEntity(Object o, Member m) throws Exception {
		UserTransaction tr = null;
		try {
			EntityManager em = getEntityManager();
			tr = getTransaction();
			if (tr != null) tr.begin(); else em.getTransaction().begin();
			if (m != null) {
				Object id = o.getClass().getMethod("getId").invoke(o);
				Metadata md = null;
				if (id == null) {
					md = new Metadata();
					md.setFirstUpdate(new Timestamp(System.currentTimeMillis()));
				}
				else
					md = (Metadata) o.getClass().getMethod("getMetadata").invoke(o);
				md.setMember(m);		
				md.setLastUpdate(new Timestamp(System.currentTimeMillis()));
				o.getClass().getMethod("setMetadata", Metadata.class).invoke(o, md);
			}
			o = em.merge(o);
			if (tr != null) {em.flush(); tr.commit();} else em.getTransaction().commit();
			return o;
		}
		catch (Exception e) {
			if (tr != null)
				tr.rollback();
			throw e;
		}
	}

	public static void removeEntity(Object o) throws Exception {
		UserTransaction tr = null;
		try {
			EntityManager em = getEntityManager();
			tr = getTransaction();
			if (tr != null) tr.begin(); else em.getTransaction().begin();
			o = em.merge(o);
			em.remove(o);
			if (tr != null) {em.flush(); tr.commit();} else em.getTransaction().commit();
		}
		catch (Exception e) {
			if (tr != null)
				tr.rollback();
			throw e;
		}
	}

	public static Object move(Class entity, Object id, short l) throws Exception {
		UserTransaction tr = null;
		try {
			tr = getTransaction();
			if (tr != null) tr.begin();
			String hql = "select " + (l == FIRST || l == NEXT ? "min" : "max") + "(id) from " + entity.getSimpleName();
			if (l == PREVIOUS || l == NEXT)
				hql += " where id" + (l == PREVIOUS ? "<" : ">") + id;
			EntityManager em = getEntityManager();
			Integer id_ = (Integer) em.createQuery(hql).getSingleResult();
			id_ = (id_ == null ? 0 : id_);
			Object o = em.find(entity, new Integer(String.valueOf(id_)));
			if (tr != null) tr.commit();
			return o;
		}
		catch (Exception e) {
			if (tr != null)
				tr.rollback();
			throw e;
		}
	}

	public static ArrayList<String> loadLabels(Class entity, String ids) throws Exception {
		return loadLabelsFromQuery("select x.label from " + entity.getSimpleName() + " x" + (StringUtils.notEmpty(ids) ? " where x.id in (" + ids + ")" : ""));
	}

	public static ArrayList<String> loadLabelsFromQuery(String s) throws Exception {
		UserTransaction tr = null;
		try {
			tr = getTransaction();
			if (tr != null) tr.begin();
			Query query = getEntityManager().createQuery(s);
			ArrayList<String> l = new ArrayList<String>(query.getResultList());
			if (tr != null) tr.commit();
			return l;
		}
		catch (Exception e) {
			if (tr != null)
				tr.rollback();
			throw e;
		}
	}

	public static List execute(String s) throws Exception {
		UserTransaction tr = null;
		try {
			tr = getTransaction();
			if (tr != null) tr.begin();
			EntityManager em = getEntityManager();
			List lResult = em.createQuery(s).getResultList();
			if (tr != null) tr.commit();
			return lResult;
		}
		catch (Exception e) {
			if (tr != null)
				tr.rollback();
			throw e;
		}
	}
	
	public static List executeNative(String s) throws Exception {
		UserTransaction tr = null;
		try {
			tr = getTransaction();
			EntityManager em = getEntityManager();
			if (tr != null) tr.begin(); else em.getTransaction().begin();
			List lResult = em.createNativeQuery(s).getResultList();
			if (tr != null) tr.commit(); else em.getTransaction().commit();
			return lResult;
		}
		catch (Exception e) {
			if (tr != null)
				tr.rollback();
			throw e;
		}
	}

	public static void executeUpdate(String s) throws Exception {
		UserTransaction tr = null;
		try {
			if (StringUtils.notEmpty(s)) {
				tr = getTransaction();
				EntityManager em = getEntityManager();
				if (tr != null) tr.begin(); else em.getTransaction().begin();
				em.createNativeQuery(s).executeUpdate();
				if (tr != null) tr.commit(); else em.getTransaction().commit();				
			}
		}
		catch (Exception e) {
			if (tr != null)
				tr.rollback();
			throw e;
		}
	}
	
	public static Class getClassFromAlias(String alias) {
		return (alias.equalsIgnoreCase(Championship.alias) ? Championship.class :
			   (alias.equalsIgnoreCase(City.alias) ? City.class :
			   (alias.equalsIgnoreCase(Complex.alias) ? Complex.class :
			   (alias.equalsIgnoreCase(Country.alias) ? Country.class : 
			   (alias.equalsIgnoreCase(Event.alias) ? Event.class : 
	           (alias.equalsIgnoreCase(HallOfFame.alias) ? HallOfFame.class : 
		       (alias.equalsIgnoreCase(League.alias) ? League.class : 
			   (alias.equalsIgnoreCase(OlympicRanking.alias) ? OlympicRanking.class :
			   (alias.equalsIgnoreCase(Olympics.alias) ? Olympics.class :
			   (alias.equalsIgnoreCase(Athlete.alias) ? Athlete.class :
			   (alias.equalsIgnoreCase(Record.alias) ? Record.class : 
			   (alias.equalsIgnoreCase(Result.alias) ? Result.class :
			   (alias.equalsIgnoreCase(RetiredNumber.alias) ? RetiredNumber.class :
			   (alias.equalsIgnoreCase(Sport.alias) ? Sport.class :
			   (alias.equalsIgnoreCase(State.alias) ? State.class :
			   (alias.equalsIgnoreCase(Team.alias) ? Team.class : 
			   (alias.equalsIgnoreCase(TeamStadium.alias) ? TeamStadium.class : 
			   (alias.equalsIgnoreCase(Type.alias) ? Type.class : 
			   (alias.equalsIgnoreCase(WinLoss.alias) ? WinLoss.class : 
			   (alias.equalsIgnoreCase(Year.alias) ? Year.class : null))))))))))))))))))));
	}

}