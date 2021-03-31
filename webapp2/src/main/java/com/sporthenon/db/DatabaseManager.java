package com.sporthenon.db;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.sporthenon.db.entity.AbstractEntity;
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
import com.sporthenon.utils.StringUtils;
import com.sporthenon.utils.res.ResourceUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@SuppressWarnings("unchecked")
public class DatabaseManager {

	private static final Logger log = Logger.getLogger(DatabaseManager.class.getName());
	
	private static DataSource pool;
	
	public static final short FIRST = 0;
	public static final short PREVIOUS = 1;
	public static final short NEXT = 2;
	public static final short LAST = 3;

	static {
		try {
			pool = createConnectionPool();
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	private static DataSource createConnectionPool() throws ClassNotFoundException {
	    HikariConfig config = new HikariConfig();
	    boolean local = true;
	    Class.forName("org.postgresql.Driver");
	    if (local) {
		    config.setJdbcUrl(String.format("jdbc:postgresql://127.0.0.1:5433/%s", "shlocal2"));
		    config.setUsername("shadmin");
		    config.setPassword("password");
	    }
	    config.setMaximumPoolSize(5);
	    config.setMinimumIdle(5);
	    config.setConnectionTimeout(10000);
	    config.setIdleTimeout(600000);
	    config.setMaxLifetime(1800000);
	    return new HikariDataSource(config);
	  }

	private static Map<String, Method> getEntityMethods(Class<?> entity) {
		Map<String, Method> mapMethods = new HashMap<>();
		for (Method method : entity.getDeclaredMethods()) {
			mapMethods.put(method.getName().toLowerCase(), method);
		}
		return mapMethods;
	}
	
	private static Collection<Object> retrieveObjects(ResultSet rs, Class<?> class_) throws Exception {
		List<Object> results = new ArrayList<>();
		while (rs.next()) {
			ResultSetMetaData metadata = rs.getMetaData();
			if ("java.lang".equals(class_.getPackage().getName())) {
				results.add(rs.getObject(1));
			}
			else {
				Map<String, Method> mapMethods = getEntityMethods(class_);
				Map<String, Object> mapValues = new HashMap<>();
				Object obj = class_.newInstance();
				for (int j = 1 ; j <= metadata.getColumnCount() ; j++) {
					String col = metadata.getColumnName(j);
					Object value = rs.getObject(j);
					Method method = mapMethods.get("set" + col.replaceAll("\\_", ""));
					if (obj instanceof PicklistItem) {
						PicklistItem item = (PicklistItem) obj;
						if (j == 1) {
							item.setValue((int)value);
						}
						else if (j == 2) {
							item.setText((String)value);
						}
						else if (j == 3) {
							item.setParam(value);
						}
					}
					else if (obj instanceof AbstractEntity) {
						mapValues.put(col, value);
					}
					else if (method != null) {
						method.invoke(obj, value);
					}
				}
				if (obj instanceof AbstractEntity) {
					((AbstractEntity)obj).setValuesFromMap(mapValues);
				}
				results.add(obj);
			}
		}
		return results;
	}
	
	public static Collection<?> callFunction(final String functionName, Collection<Object> params, Class<?> class_) {
 		List<Object> results = new ArrayList<>();
		try (Connection conn = pool.getConnection()) {
 			conn.setAutoCommit(false);
 			final String sql = "{ ? = CALL " + functionName + "(" + StringUtils.repeat("?", params.size(), ",") + ") }";
 			try (CallableStatement cs = conn.prepareCall(sql);) {
 				int i = 1;
 				cs.registerOutParameter(i++, Types.OTHER);
 				for (Object param : params) {
 					cs.setObject(i++, param);
 				}
 				cs.execute();
 				ResultSet rs = (ResultSet) cs.getObject(1);
 				results.addAll(retrieveObjects(rs, class_));
 			}
 		}
 		catch (Exception e) {
 			log.log(Level.SEVERE, "Error occured with PSQL function '" + functionName + "'", e);
 		}
 		return results;
	}
	
	public static Collection<?> callFunctionSelect(final String functionName, Collection<Object> params, Class<?> class_, Object... options) {
		final String sql = "SELECT * FROM " + functionName
				+ (params != null ? "(" + StringUtils.repeat("?", params.size(), ",") + ")" : "")
				+ (options != null && options.length > 0 && options[0] != null ? options[0] : "")
				+ (options != null && options.length > 1 && options[1] != null ? "LIMIT" + options[1] : "");
		return executeSelect(sql, params, class_);
	}
	
	public static Collection<?> executeSelect(final String sql, Collection<Object> params, Class<?> class_) {
 		List<Object> results = new ArrayList<>();
		try (Connection conn = pool.getConnection()) {
 			try (PreparedStatement ps = conn.prepareStatement(sql);) {
 				int i = 1;
 				if (params != null) {
 					for (Object param : params) {
 	 					ps.setObject(i++, param);
 	 				}	
 				}
 				ResultSet rs = ps.executeQuery();
 				results.addAll(retrieveObjects(rs, class_));
 			}
 		}
 		catch (Exception e) {
 			log.log(Level.SEVERE, "Error occured with PSQL SELECT '" + sql + "'", e);
 		}
 		return results;
	}
	
	public static Collection<?> executeSelect(final String sql, Class<?> class_) {
 		List<Object> results = new ArrayList<>();
		try (Connection conn = pool.getConnection()) {
 			try (Statement st = conn.createStatement();) {
 				ResultSet rs = st.executeQuery(sql);
 				results.addAll(retrieveObjects(rs, class_));
 			}
 		}
 		catch (Exception e) {
 			log.log(Level.SEVERE, "Error occured with PSQL SELECT '" + sql + "'", e);
 		}
 		return results;
	}
	
	public static Collection<Object[]> executeSelect(final String sql) {
 		List<Object[]> results = new ArrayList<>();
		try (Connection conn = pool.getConnection()) {
 			try (Statement st = conn.createStatement();) {
 				ResultSet rs = st.executeQuery(sql);
 				while (rs.next()) {
 					ResultSetMetaData metadata = rs.getMetaData();
 					List<Object> values = new ArrayList<>();
 					for (int j = 1 ; j <= metadata.getColumnCount() ; j++) {
 						values.add(rs.getObject(j));
 					}
 					results.add(values.toArray());
 				}
 			}
 		}
 		catch (Exception e) {
 			log.log(Level.SEVERE, "Error occured with PSQL SELECT '" + sql + "'", e);
 		}
 		return results;
	}
	
	public static Object loadEntity(Class<?> class_, Object id) {
		Object result = null;
		try {
			final String table = (String) class_.getField("table").get(null);
			final String key = (String) class_.getField("key").get(null);
			String sql = null;
			try {
				sql = (String) class_.getField("query").get(null);
			}
			catch (NoSuchFieldException e) {
				sql = "SELECT * from " + table + " T";
			}
			sql += " WHERE T." + key + " = ?";
			List<?> results = (List<?>) executeSelect(sql, Arrays.asList(id), class_);
			if (results != null && !results.isEmpty()) {
				result = results.get(0);
			}
		}
		catch (Exception e) {
 			log.log(Level.SEVERE, "Error occured while loading entity #" + id + "", e);
 		}
		return result;
	}
	
	public static Object loadEntity(final String sql, Collection<Object> params, Class<?> class_) {
		List<Object> results = (List<Object>) executeSelect(sql, params, class_);
		return (results != null && !results.isEmpty() ? results.get(0) : null);
	}
	
	public static Class<? extends AbstractEntity> getClassFromAlias(String alias) {
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
	
	public static Object move(Class<?> class_, Object id, short l, String filter) throws Exception {
		final String table = (String) class_.getField("table").get(null);
		final String key = (String) class_.getField("key").get(null);
		String sql = "SELECT " + (l == FIRST || l == NEXT ? "MIN" : "MAX") + "(" + key + ") FROM " + table;
		if (l == PREVIOUS || l == NEXT)
			sql += " WHERE " + key + " " + (l == PREVIOUS ? "<" : ">") + id;
		if (StringUtils.notEmpty(filter))
			sql += (sql.indexOf(" WHERE ") != -1 ? " AND " : " WHERE ") + filter;
		Integer id_ = ((List<Integer>) executeSelect(sql, Integer.class)).get(0);
		id_ = (id_ == null ? 0 : id_);
		return loadEntity(class_, id_);
	}
	
	public static void executeUpdate(final String sql) throws Exception {
		try (Connection conn = pool.getConnection()) {
 			try (Statement st = conn.createStatement();) {
 				st.executeUpdate(sql);
 			}
 		}
 		catch (Exception e) {
 			log.log(Level.SEVERE, "Error occured with PSQL SELECT '" + sql + "'", e);
 		}
	}
	
	public static Object saveEntity(Object o, Contributor cb) throws Exception {
		final Timestamp currentDate = new Timestamp(System.currentTimeMillis());
		final Object id = o.getClass().getMethod("getId").invoke(o);
		final boolean isAdd = (id == null);
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

		// Create contribution item
		if (o instanceof Result && cb != null) {
			try {
				Object newId = o.getClass().getMethod("getId").invoke(o);
				Contribution co = new Contribution();
				co.setIdItem(new Integer(String.valueOf(newId)));
				co.setIdContributor(cb.getId());
				co.setType(isAdd ? 'A' : 'U');
				co.setDate(currentDate);
				executeUpdate("INSERT INTO _contribution ...");
			}
			catch (NoSuchMethodException e) {}
		}
		return o;
	}
	
	public static void removeEntity(Object o) throws Exception {
		final String table = (String) o.getClass().getField("table").get(null);
		final String key = (String) o.getClass().getField("key").get(null);
		final Object id = (Object) o.getClass().getMethod("getId").invoke(o);
		executeUpdate("DELETE FROM " + table + "WHERE " + key + " = " + id);
	}
	
	public static void saveExternalLinks(String alias, Integer id, String s) {
		try {
			executeUpdate("DELETE FROM _external_link WHERE entity = '" + alias + "' AND id_item = " + id);
			if (StringUtils.notEmpty(s) && !s.equals("null")) {
				for (String s_ : s.split("\\s")) {
					if (StringUtils.notEmpty(s_)) {
						ExternalLink link = new ExternalLink();
						link.setEntity(alias);
						link.setIdItem(id);
						link.setChecked(false);
						link.setUrl(s_);
						saveEntity(link, null);
					}
				}
			}
		}
		catch (Exception e_) {
			log.log(Level.FINE, e_.getMessage(), e_);
		}
	}
	
	public static Collection<PicklistItem> getPicklist(String sql, Collection<Object> params) throws Exception {
		return (Collection<PicklistItem>) executeSelect(sql, params, PicklistItem.class);
	}
	
	public static String getEntityLabel(String alias, int id) {
		List<String> results = null;
		try {
			Class<?> class_ = getClassFromAlias(alias);
			final String table = (String) class_.getField("table").get(null);
			final String key = (String) class_.getField("key").get(null);
			final String sql = "SELECT " + 
					(alias.equals(Athlete.alias) ? "firstName || ' ' || lastName" : 
					(alias.equals(Contributor.alias) ? "login" : 
					(alias.equals(Olympics.alias) ? "city.label || ' ' || year.label" :
					"label"))) +
					" FROM " + table + " WHERE " + key + " = ?";
			results = (List<String>) executeSelect(sql, Arrays.asList(id), String.class);
		}
		catch (Exception e_) {
			log.log(Level.SEVERE, e_.getMessage(), e_);
		}
		return (results != null && !results.isEmpty() ? results.get(0) : null);
	}
	
	public static Collection<String> loadLabels(Class<?> class_, String ids, String lang) throws Exception {
		final String table = (String) class_.getField("table").get(null);
		final String sql = "SELECT x.label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) && class_ != Team.class && class_ != Year.class ? "_" + lang : "")
				+ " FROM " + table + " x"
				+ (StringUtils.notEmpty(ids) ? " WHERE x.id IN (" + ids + ")" : "");
		return (Collection<String>) executeSelect(sql, String.class);
	}

}