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
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;
import org.hibernate.StaleStateException;

import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Calendar;
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
import com.sporthenon.db.entity.Round;
import com.sporthenon.db.entity.RoundType;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.State;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.TeamStadium;
import com.sporthenon.db.entity.Type;
import com.sporthenon.db.entity.WinLoss;
import com.sporthenon.db.entity.Year;
import com.sporthenon.db.entity.meta.Contribution;
import com.sporthenon.db.entity.meta.Contributor;
import com.sporthenon.db.entity.meta.ExternalLink;
import com.sporthenon.db.entity.meta.Metadata;
import com.sporthenon.utils.ConfigUtils;
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;

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
			logger.fatal(e.getMessage(), e);
		}
	}

	public static void setFactory(Map<String, String> params, String env) throws Exception {
		try {
			String env_ = (env != null ? env : ConfigUtils.getProperty("env"));
			emf = Persistence.createEntityManagerFactory("shunit-" + env_, params);
		}
		catch (Exception e) {
			logger.fatal(e.getMessage(), e);
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
		EntityManager em = null;
		try {
			tr = getTransaction();
			em = getEntityManager();
			if (tr != null) tr.begin(); else em.getTransaction().begin();
			Query query = em.createNamedQuery(name);
			int i = 0;
			if (params != null)
				for (Object obj : params)
					query.setParameter(++i, obj != null && obj instanceof String && String.valueOf(obj).equalsIgnoreCase("_en") ? "" : obj);
			Collection c = query.getResultList();
			if (tr != null) tr.commit(); else em.getTransaction().commit();
			return c;
		}
		catch (Exception e) {
			if (tr != null)
				tr.rollback();
			throw e;
		}
		finally {
			if (em != null && em.isOpen())
				em.close();
		}
	}

	public static Collection<PicklistBean> getPicklist(Class source, String target, String filter, String concat, Object order, String lang) throws Exception {
		String hql = "select distinct x." + target + ".id, " + (concat != null ? concat + " || " : "") + "x." + target + ".label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) && !target.matches("team|year") ? lang.toUpperCase() : "") + (order instanceof String ? ", " + order : "");
		hql += " from " + source.getSimpleName() + " x";
		hql += (filter != null && !filter.equals("") ? " where " + filter : "");
		hql += " order by " + order;
		return getPicklistFromQuery(hql, false);
	}

	public static Collection<PicklistBean> getEntityPicklist(Class entity, String label, String param, String lang) throws Exception {
		label = (label != null ? label : "label") + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) && !label.equalsIgnoreCase("id") && entity != Team.class && entity != Year.class ? lang.toUpperCase() : "");
		param = (param != null ? "," + param : "");
		String hql = "select id," + label + param + " from " + entity.getSimpleName() + " order by " + label + param;
		return getPicklistFromQuery(hql, false);
	}

	public static Collection<PicklistBean> getPicklistFromQuery(String s, boolean native_) throws Exception {
		UserTransaction tr = null;
		EntityManager em = null;
		try {
			tr = getTransaction();
			if (tr != null) tr.begin();
			em = getEntityManager();
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
		finally {
			if (em != null && em.isOpen())
				em.close();
		}
	}

	public static Object loadEntity(Class entity, Object id) throws Exception {
		UserTransaction tr = null;
		EntityManager em = null;
		try {
			if (StringUtils.notEmpty(id)) {
				tr = getTransaction();
				if (tr != null) tr.begin();
				em = getEntityManager();
				Object o = em.find(entity, id instanceof Integer ? (Integer) id : (String.valueOf(id).matches("\\d+") ? StringUtils.toInt(id) : String.valueOf(id)));
				if (tr != null) tr.commit();
				return o;
			}
		}
		catch (Exception e) {
			if (tr != null)
				tr.rollback();
			throw e;
		}
		finally {
			if (em != null && em.isOpen())
				em.close();
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

	public static Object saveEntity(Object o, Contributor cb) throws Exception {
		UserTransaction tr = null;
		EntityManager em = null;
		Timestamp currentDate = new Timestamp(System.currentTimeMillis());
		boolean isAdd = true;
		Object id = null;
		try {
			em = getEntityManager();
			tr = getTransaction();
			if (tr != null) tr.begin(); else em.getTransaction().begin();
			try {
				id = o.getClass().getMethod("getId").invoke(o);
				isAdd = (id == null);
				if (cb != null) {
					Metadata md = null;
					if (isAdd) {
						md = new Metadata();
						md.setFirstUpdate(currentDate);
					}
					else
						md = (Metadata) o.getClass().getMethod("getMetadata").invoke(o);
					md.setContributor(cb);		
					md.setLastUpdate(currentDate);
					o.getClass().getMethod("setMetadata", Metadata.class).invoke(o, md);
				}
			}
			catch (NoSuchMethodException e) {}
			o = em.merge(o);
			// Contribution
			if (o instanceof Result && cb != null) {
				try {
					id = o.getClass().getMethod("getId").invoke(o);
					Contribution co = new Contribution();
					co.setIdItem(new Integer(String.valueOf(id)));
					co.setIdContributor(cb.getId());
					co.setType(isAdd ? 'A' : 'U');
					co.setDate(currentDate);
					em.persist(co);
				}
				catch (NoSuchMethodException e) {}
			}
			if (tr != null) {em.flush(); tr.commit();} else em.getTransaction().commit();
			return o;
		}
		catch (Exception e) {
			if (tr != null)
				tr.rollback();
			throw e;
		}
		finally {
			if (em != null && em.isOpen())
				em.close();
		}
	}

	public static void removeEntity(Object o) throws Exception {
		UserTransaction tr = null;
		EntityManager em = null;
		try {
			em = getEntityManager();
			tr = getTransaction();
			if (tr != null) tr.begin(); else em.getTransaction().begin();
			o = em.merge(o);
			em.remove(o);
			if (tr != null) {em.flush(); tr.commit();} else em.getTransaction().commit();
		}
		catch (Exception e) {
			if (e instanceof StaleStateException || e instanceof OptimisticLockException)
				logger.error(e.getMessage());
			else {
				if (tr != null)
					tr.rollback();
				throw e;
			}
		}
		finally {
			if (em != null && em.isOpen())
				em.close();
		}
	}

	public static Object move(Class entity, Object id, short l, String filter) throws Exception {
		UserTransaction tr = null;
		EntityManager em = null;
		try {
			tr = getTransaction();
			if (tr != null) tr.begin();
			String hql = "select " + (l == FIRST || l == NEXT ? "min" : "max") + "(id) from " + entity.getSimpleName();
			if (l == PREVIOUS || l == NEXT)
				hql += " where id" + (l == PREVIOUS ? "<" : ">") + id;
			if (StringUtils.notEmpty(filter))
				hql += (hql.indexOf(" where ") != -1 ? " and " : " where ") + filter;
			em = getEntityManager();
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
		finally {
			if (em != null && em.isOpen())
				em.close();
		}
	}

	public static ArrayList<String> loadLabels(Class entity, String ids, String lang) throws Exception {
		return loadLabelsFromQuery("select x.label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) && entity != Team.class && entity != Year.class ? lang.toUpperCase() : "") + " from " + entity.getSimpleName() + " x" + (StringUtils.notEmpty(ids) ? " where x.id in (" + ids + ")" : ""));
	}

	public static ArrayList<String> loadLabelsFromQuery(String s) throws Exception {
		UserTransaction tr = null;
		EntityManager em = null;
		try {
			tr = getTransaction();
			if (tr != null) tr.begin();
			em = getEntityManager();
			Query query = em.createQuery(s);
			ArrayList<String> l = new ArrayList<String>(query.getResultList());
			if (tr != null) tr.commit();
			return l;
		}
		catch (Exception e) {
			if (tr != null)
				tr.rollback();
			throw e;
		}
		finally {
			if (em != null && em.isOpen())
				em.close();
		}
	}

	public static List execute(String s) throws Exception {
		UserTransaction tr = null;
		EntityManager em = null;
		try {
			tr = getTransaction();
			if (tr != null) tr.begin();
			em = getEntityManager();
			List lResult = em.createQuery(s).getResultList();
			if (tr != null) tr.commit();
			return lResult;
		}
		catch (Exception e) {
			if (tr != null)
				tr.rollback();
			throw e;
		}
		finally {
			if (em != null && em.isOpen())
				em.close();
		}
	}
	
	public static List executeNative(String s) throws Exception {
		UserTransaction tr = null;
		EntityManager em = null;
		try {
			tr = getTransaction();
			em = getEntityManager();
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
		finally {
			if (em != null && em.isOpen())
				em.close();
		}
	}

	public static void executeUpdate(String s) throws Exception {
		UserTransaction tr = null;
		EntityManager em = null;
		try {
			if (StringUtils.notEmpty(s)) {
				tr = getTransaction();
				em = getEntityManager();
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
		finally {
			if (em != null && em.isOpen())
				em.close();
		}
	}
	
	public static String getEntityName(String alias, int id) {
		String s = "";
		UserTransaction tr = null;
		EntityManager em = null;
		try {
			tr = getTransaction();
			if (tr != null) tr.begin();
			em = getEntityManager();
			String hql = "select " + (alias.equals(Athlete.alias) ? "firstName || ' ' || lastName" : (alias.equals(Contributor.alias) ? "login" : "label"));
			hql += " from " + getClassFromAlias(alias).getName() + " where id=" + id;
			Query query = em.createQuery(hql);
			s = String.valueOf(query.getSingleResult()).trim();
			if (tr != null) tr.commit();
		}
		catch (Exception e) {
			try {
				if (tr != null)
					tr.rollback();	
			}
			catch (Exception e_) {}
			logger.error(e.getMessage(), e);
		}
		finally {
			if (em != null && em.isOpen())
				em.close();
		}
		return s;
	}
	
	public static Class getClassFromAlias(String alias) {
		return (alias.equalsIgnoreCase(Calendar.alias) ? Calendar.class :
			   (alias.equalsIgnoreCase(Championship.alias) ? Championship.class :
			   (alias.equalsIgnoreCase(City.alias) ? City.class :
			   (alias.equalsIgnoreCase(Complex.alias) ? Complex.class :
			   (alias.equalsIgnoreCase(Contributor.alias) ? Contributor.class :
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
			   (alias.equalsIgnoreCase(Round.alias) ? Round.class :
			   (alias.equalsIgnoreCase(RoundType.alias) ? RoundType.class :
			   (alias.equalsIgnoreCase(Sport.alias) ? Sport.class :
			   (alias.equalsIgnoreCase(State.alias) ? State.class :
			   (alias.equalsIgnoreCase(Team.alias) ? Team.class : 
			   (alias.equalsIgnoreCase(TeamStadium.alias) ? TeamStadium.class : 
			   (alias.equalsIgnoreCase(Type.alias) ? Type.class : 
			   (alias.equalsIgnoreCase(WinLoss.alias) ? WinLoss.class : 
			   (alias.equalsIgnoreCase(Year.alias) ? Year.class : null))))))))))))))))))))))));
	}
	
	public static Integer insertEvent(int row, String s, Contributor cb, StringBuffer sb, String lang) throws Exception {
		Integer id = null;
		Object o = null;
		String msg = null;
		try {
			String[] t = s.split("\\|");
			Object o_ = null;
			if (t.length > 1)
				o_ = loadEntityFromQuery("from Type tp where lower(tp.label) = '" + t[1].toLowerCase() + "'");
			if (o_ == null)
				o_ = loadEntityFromQuery("from Type tp where lower(tp.label) = 'single'");
			Event ev = new Event();
			ev.setLabel(t[0]);
			ev.setLabelFr(t[0]);
			ev.setIndex(Integer.MAX_VALUE);
			ev.setType((Type)o_);
			o = saveEntity(ev, cb);
			msg = "New Event";
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		finally {
			if (o != null) {
				if (sb != null)
					sb.append("Row " + (row + 1) + ": " + msg + " | " + o).append("<br/>");
				id = Integer.valueOf(String.valueOf(o.getClass().getMethod("getId").invoke(o)));
			}
		}
		return id;
	}
	
	public static Integer insertEntity(int row, int n, int spid, String s, String date, Contributor cb, StringBuffer sb, String lang) throws Exception {
		Integer id = null;
		Object o = null;
		String msg = null;
		try {
			if (n < 10) {
				s = s.replaceAll("\\[", "(").replaceAll("\\]", ")");
				if (!s.toLowerCase().matches(StringUtils.PATTERN_ATHLETE))
					throw new Exception(ResourceUtils.getText("err.invalid.athlete", lang).replaceAll("#S#", s));
				int p = s.indexOf(", ");
				int p_ = (s.indexOf(" (") > -1  ? s.indexOf(" (") : s.length());
				Athlete a = new Athlete();
				a.setSport((Sport)loadEntity(Sport.class, spid));
				a.setLastName(s.substring(0, p > -1 ? p : p_));
				a.setFirstName(p > -1 && s.charAt(p + 2) != '(' ? s.substring(p + 2, p_) : "");
				boolean isCountryTeam = s.toLowerCase().matches(".*\\([a-z]{3}\\,\\s.+\\)$");
				boolean isCountry = s.toLowerCase().matches(".*\\([a-z]{3}\\)$");
				boolean isTeam = (!isCountry && s.toLowerCase().matches(".*\\([^\\,\\(\\)]+\\)$")); 
				// Check if exists
				String regexp = StringUtils.toPatternString(s);
				String sql = "SELECT T.id FROM \"Athlete\" T LEFT JOIN \"Country\" CN ON T.id_country=CN.id LEFT JOIN \"Team\" TM ON T.id_team=TM.id WHERE T.id_sport=" + spid + " and lower(last_name) || ', ' || lower(first_name) " + (isCountryTeam ? " || ' (' || lower(CN.code) " + " || ', ' || lower(TM.label) || ')'" : (isCountry ? " || ' (' || lower(CN.code) || ')'" : (isTeam ? " || ' (' || lower(TM.label) || ')'" : ""))) + " ~ '" + regexp + "'";
				List<Integer> lId = (List<Integer>) DatabaseHelper.executeNative(sql);
				if (lId != null && lId.size() > 0)
					return lId.get(0);
				if (isCountry || isCountryTeam) { // Country set
					p = s.indexOf(" (") + 2;
					String countryCode = s.substring(p, p + 3).toLowerCase();
					Object o_ = loadEntityFromQuery("from Country cn where lower(cn.code) = '" + countryCode + "'");
					if (o_ == null)
						throw new Exception("Invalid Country: " + countryCode.toUpperCase());
					a.setCountry((Country)o_);
				}
				if (isTeam || isCountryTeam) { // Team set
					p = s.lastIndexOf(isCountryTeam ? ", " : " (") + 2;
					String tm = s.substring(p, s.length() - 1);
					if (StringUtils.notEmpty(date))
						date = (StringUtils.notEmpty(date) && date.matches(".*\\d{4}$") ? date.substring(date.length() - 4) : null);
					Object o_ = loadEntityFromQuery("from Team tm where sport.id=" + spid + " and lower(tm.label) = '" + tm.toLowerCase().replaceAll("'", "''") + "'" + (date != null ? " and '" + date + "' between year1 and (case year2 when null then '9999' when '' then '9999' else year2 end)" : ""));
					if (o_ == null) {
						Integer idTm = insertEntity(row, 50, spid, tm, null, cb, sb, lang);
						o_ = loadEntity(Team.class, idTm);
					}
					a.setTeam((Team)o_);
				}
				o = a;
				o = saveEntity(a, cb);
				msg = "New Athlete";
			}
			else if (n == 50) {
				s = s.replaceAll("\\[", "(").replaceAll("\\]", ")");
				if (!s.toLowerCase().matches(StringUtils.PATTERN_TEAM))
					throw new Exception(ResourceUtils.getText("err.invalid.team", lang).replaceAll("#S#", s));
				// Check if exists
				String regexp = StringUtils.toPatternString(s.indexOf(" (") > -1 ? s.substring(0, s.indexOf(" (")).toLowerCase() : s.toLowerCase());
				String sql = "SELECT T.id from \"Team\" T WHERE T.id_sport=" + spid + " AND lower(label) ~ '" + regexp + "' AND (link IS NULL OR link = 0)";
				List<Integer> lId = (List<Integer>) DatabaseHelper.executeNative(sql);
				if (lId != null && lId.size() > 0)
					return lId.get(0);
				Team t = new Team();
				t.setLabel(s);
				t.setSport((Sport)loadEntity(Sport.class, spid));
				if (s.matches(".*\\([A-Z]{3}\\)$")) {
					int p = s.indexOf(" (") + 2;
					String countryCode = s.substring(p, p + 3).toLowerCase();
					Object o_ = loadEntityFromQuery("from Country cn where lower(cn.code) = '" + countryCode + "'");
					if (o_ == null)
						throw new Exception("Invalid Country: " + countryCode.toUpperCase());
					t.setLabel(s.substring(0, p - 2));
					t.setCountry((Country)o_);
				}
				o = t;
				o = saveEntity(t, cb);
				msg = "New Team";
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		finally {
			if (o != null) {
				if (sb != null)
					sb.append("Row " + (row + 1) + ": " + msg + " | " + o).append("<br/>");
				id = Integer.valueOf(String.valueOf(o.getClass().getMethod("getId").invoke(o)));
			}
		}
		return id;
	}
	
	public static Integer insertPlace(int row, String s, Contributor cb, StringBuffer sb, String lang) throws Exception {
		Integer id = null;
		Object o = null;
		String msg = null;
		try {
			if (!s.toLowerCase().matches(StringUtils.PATTERN_PLACE))
				throw new Exception(ResourceUtils.getText("err.invalid.place", lang).replaceAll("#S#", s));
			String[] t = s.split("\\,\\s");
			String cx = null;
			String ct = null;
			String cn = t[t.length - 1];
			if (t.length > 2) {
				cx = t[0];
				ct = t[1];
			}
			else
				ct = t[0];
			City ct_ = null;
			if (cx != null) { // Set City (for complex)
				Object o_ = loadEntityFromQuery("from City ct where lower(ct.label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) ? lang.toUpperCase() : "") + ") like '" + ct.toLowerCase().replaceAll("'", "''") + "' and lower(country.code) = '" + cn.toLowerCase() + "'");
				if (o_ == null) {
					Integer idCt = insertPlace(row, ct + ", " + cn, cb, sb, lang);
					o_ = loadEntity(City.class, idCt);
				}
				ct_ = (City)o_;
			}
			Country cn_ = null;
			if (cn != null) { // Set Country
				Object o_ = loadEntityFromQuery("from Country cn where lower(cn.code) = '" + cn.toLowerCase() + "'");
				if (o_ == null)
					throw new Exception("Invalid Country: " + cn.toUpperCase());
				cn_ = (Country)o_;
			}
			if (cx != null) {
				// Check if exists
				String regexp1 = StringUtils.toPatternString(ct);
				String regexp2 = StringUtils.toPatternString(cx);
				String sql = "SELECT T.id FROM \"Complex\" T LEFT JOIN \"City\" CT ON T.id_city=CT.id LEFT JOIN \"Country\" CN ON CT.id_country=CN.id WHERE lower(CN.code) like '" + cn.toLowerCase() + "' and lower(CT.label) ~ '" + regexp1.replaceAll("'", "''") + "' and lower(T.label) ~ '" + regexp2.replaceAll("'", "''") + "'";
				List<Integer> lId = (List<Integer>) DatabaseHelper.executeNative(sql);
				if (lId != null && lId.size() > 0)
					return lId.get(0);
				
				Complex c = new Complex();
				c.setLabel(cx);
				c.setLabelFr(cx);
				c.setCity(ct_);
				o = c;
				o = saveEntity(c, cb);
				msg = "New Complex";
			}
			else if (ct != null) {
				// Check if exists
				String regexp = StringUtils.toPatternString(ct);
				String sql = "SELECT T.id FROM \"City\" T LEFT JOIN \"Country\" CN ON T.id_country=CN.id WHERE lower(CN.code) like '" + cn.toLowerCase() + "' and lower(T.label) ~ '" + regexp.replaceAll("'", "''") + "'";
				List<Integer> lId = (List<Integer>) DatabaseHelper.executeNative(sql);
				if (lId != null && lId.size() > 0)
					return lId.get(0);
				
				City c = new City();
				c.setLabel(ct);
				c.setLabelFr(ct);
				c.setCountry(cn_);
				o = c;
				o = saveEntity(c, cb);
				msg = "New City";
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		finally {
			if (o != null) {
				if (sb != null)
					sb.append("Row " + (row + 1) + ": " + msg + " | " + o).append("<br/>");
				id = Integer.valueOf(String.valueOf(o.getClass().getMethod("getId").invoke(o)));
			}
		}
		return id;
	}
	
	public static void saveExternalLinks(String alias, Integer id, String s) {
		try {
			executeUpdate("DELETE FROM \"~ExternalLink\" WHERE ENTITY='" + alias + "' AND ID_ITEM=" + id);
			if (StringUtils.notEmpty(s) && !s.equals("null")) {
				for (String s_ : s.split("\\s")) {
					if (StringUtils.notEmpty(s_)) {
						ExternalLink link = new ExternalLink();
						link.setEntity(alias);
						link.setIdItem(id);
						if (s_.indexOf("wikipedia.org") > -1)
							link.setType("wiki");
						else if (s_.indexOf("www.sports-reference.com/olympics") > -1)
							link.setType("oly-ref");
						else if (s_.indexOf("www.basketball-reference.com") > -1)
							link.setType("bkt-ref");
						else if (s_.indexOf("www.baseball-reference.com") > -1)
							link.setType("bb-ref");
						else if (s_.indexOf("www.pro-football-reference.com") > -1)
							link.setType("ft-ref");
						else if (s_.indexOf("www.hockey-reference.com") > -1)
							link.setType("hk-ref");
						else
							link.setType("others");
						link.setChecked(false);
						link.setUrl(s_);
						saveEntity(link, null);
					}
				}
			}
		}
		catch (Exception e_) {
			logger.error(e_.getMessage(), e_);
		}
	}

}