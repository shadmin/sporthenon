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
import com.sporthenon.db.entity.meta.Config;
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
		final String dbHost = System.getenv("SHDB_HOST");
		final String dbPort = System.getenv("SHDB_PORT");
		final String dbName = System.getenv("SHDB_NAME");
		final String dbUser = System.getenv("SHDB_USER");
		final String dbPwd = System.getenv("SHDB_PWD");
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(String.format("jdbc:postgresql://%s:%s/%s", dbHost, dbPort, dbName));
		config.setUsername(dbUser);
		config.setPassword(dbPwd);
		config.setMaximumPoolSize(10);
		config.setMinimumIdle(3);
		config.setConnectionTimeout(10000);
		config.setIdleTimeout(600000);
		config.setMaxLifetime(1800000);
		Class.forName("org.postgresql.Driver");
		return new HikariDataSource(config);
	}

	private static Map<String, Method> getEntityMethods(Class<?> entity, String prefix) {
		Map<String, Method> mapMethods = new HashMap<>();
		for (Method method : entity.getDeclaredMethods()) {
			if (method.getName().startsWith(prefix)
					&& (!"get".equals(prefix) || method.getParameterTypes() == null || method.getParameterTypes().length == 0)) {
				mapMethods.put(method.getName().toLowerCase(), method);	
			}
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
				Map<String, Method> mapMethods = getEntityMethods(class_, "set");
				Map<String, Object> mapValues = new HashMap<>();
				Object obj = class_.getConstructor().newInstance();
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
	
	public static Collection<?> callFunction(final String functionName, Collection<?> params, Class<?> class_) {
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
	
	public static Collection<?> callFunctionSelect(final String functionName, Collection<?> params, Class<?> class_, Object... options) {
		final String sql = "SELECT * FROM " + functionName
				+ (params != null ? "(" + StringUtils.repeat("?", params.size(), ",") + ")" : "")
				+ (options != null && options.length > 0 && options[0] != null ? " ORDER BY " + options[0] : "")
				+ (options != null && options.length > 1 && options[1] != null ? " LIMIT " + options[1] : "");
		return executeSelect(sql, params, class_);
	}
	
	public static Collection<?> executeSelect(final String sql, Collection<?> params, Class<?> class_) {
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
			final String table = getTable(class_);
			String sql = null;
			try {
				sql = (String) class_.getField("query").get(null);
			}
			catch (NoSuchFieldException e) {
				sql = "SELECT * from " + table + " T";
			}
			sql += " WHERE T.id = ?";
			id = StringUtils.toInt(id);
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
	
	public static Object loadEntity(final String sql, Collection<?> params, Class<?> class_) {
		List<Object> results = (List<Object>) executeSelect(sql, params, class_);
		return (results != null && !results.isEmpty() ? results.get(0) : null);
	}
	
	public static String getTable(Class<?> class_) throws NoSuchFieldException, IllegalAccessException {
		return (String) class_.getField("table").get(null);
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
			   (alias.equalsIgnoreCase(Year.alias) ? Year.class :
			   (alias.equalsIgnoreCase(Config.alias) ? Config.class : null)))))))))))))))))))))))));
	}
	
	public static Object move(Class<?> class_, Object id, short l, String filter) throws Exception {
		String sql = "SELECT " + (l == FIRST || l == NEXT ? "MIN" : "MAX") + "(id) FROM " + getTable(class_);
		if (l == PREVIOUS || l == NEXT)
			sql += " WHERE id " + (l == PREVIOUS ? "<" : ">") + id;
		if (StringUtils.notEmpty(filter))
			sql += (sql.indexOf(" WHERE ") != -1 ? " AND " : " WHERE ") + filter;
		Integer id_ = ((List<Integer>) executeSelect(sql, Integer.class)).get(0);
		id_ = (id_ == null ? 0 : id_);
		return loadEntity(class_, id_);
	}
	
	public static Integer executeUpdate(final String sql, Collection<?> params) throws Exception {
		Integer id = null;
		try (Connection conn = pool.getConnection()) {
 			try (PreparedStatement ps = conn.prepareStatement(sql);) {
 				int i = 1;
 				if (params != null) {
 					for (Object param : params) {
 	 					ps.setObject(i++, param);
 	 				}	
 				}
 				if (sql.matches("^(INSERT).*")) {
 					ResultSet rs = ps.executeQuery();
 	 				rs.next();
 	 				id = rs.getInt(1);
 				}
 				else {
 					ps.executeUpdate();
 				}
 			}
 		}
 		catch (Exception e) {
 			log.log(Level.SEVERE, "Error occured with PSQL SELECT '" + sql + "'", e);
 		}
		return id;
	}
	
	public static Object saveEntity(Object o, Contributor cb) throws Exception {
		final Timestamp currentDate = new Timestamp(System.currentTimeMillis());
		final String table = getTable(o.getClass());
		final String cols = (String) o.getClass().getField("cols").get(null);
		final Object id = o.getClass().getMethod("getId").invoke(o);
		final boolean isAdd = (id == null);
		final boolean isMetadata = (cb != null);

		// Get params for query
		Map<String, Method> mapMethods = getEntityMethods(o.getClass(), "get");
		List<Object> params = new ArrayList<>();
		String[] tcols = cols.split("\\,");
		for (int i = 0 ; i < tcols.length ; i++) {
			Method method = mapMethods.get("get" + tcols[i].replaceAll("\\_", ""));
			Object value = method.invoke(o);
			params.add(value);
		}
		if (isMetadata) {
			params.add(cb.getId());
			params.add(currentDate);
			if (isAdd) {
				params.add(currentDate);
			}
		}
		
		// Write SQL query
		String sql;
		if (isAdd) {
			sql = "INSERT INTO " + table + " (id," + cols + (isMetadata ? "," + Metadata.cols : "") + ") "
					+ "VALUES (NEXTVAL('" + (table.startsWith("_") ? "_s" : "s_") + table + "'),"
					+ StringUtils.repeat("?", tcols.length + (isMetadata ? 3 : 0), ",") + ")"
					+ " RETURNING id";
		}
		else {
			sql = "UPDATE " + table + " SET ";
			for (int i = 0 ; i < tcols.length ; i++) {
				tcols[i] += " = ?";
			}
			sql += StringUtils.join(Arrays.asList(tcols), ",");
			if (isMetadata) {
				sql += ", id_contributor = ?, last_update = ?";
			}
			sql += " WHERE id = ?";
			params.add(id);
		}
		Integer newId = executeUpdate(sql, params);
		if (newId != null) {
			o.getClass().getMethod("setId", Integer.class).invoke(o, newId);	
		}

		// Create contribution item
		if (o instanceof Result && cb != null) {
			try {
				Contribution co = new Contribution();
				co.setIdContributor(cb.getId());
				if (isAdd) {
					co.setIdItem(newId);
					co.setType('A');	
				}
				else {
					co.setIdItem(StringUtils.toInt(id));
					co.setType('U');
				}
				co.setDate(currentDate);
				saveEntity(co, null);
			}
			catch (NoSuchMethodException e) {}
		}
		return o;
	}
	
	public static void removeEntity(Object o) throws Exception {
		final String table = getTable(o.getClass());
		final Object id = (Object) o.getClass().getMethod("getId").invoke(o);
		String sql = "DELETE FROM " + table + " WHERE id = ?";
		executeUpdate(sql, Arrays.asList(id));
	}
	
	public static void saveExternalLinks(String alias, Integer id, String urls) {
		try {
			executeUpdate("DELETE FROM _external_link WHERE entity = ? AND id_item = ?", Arrays.asList(alias, id));
			if (StringUtils.notEmpty(urls) && !urls.equals("null")) {
				for (String url : urls.split("\\s")) {
					if (StringUtils.notEmpty(url)) {
						ExternalLink link = new ExternalLink();
						link.setEntity(alias);
						link.setIdItem(id);
						link.setChecked(false);
						link.setUrl(url);
						saveEntity(link, null);
					}
				}
			}
		}
		catch (Exception e_) {
			log.log(Level.FINE, e_.getMessage(), e_);
		}
	}
	
	public static Collection<PicklistItem> getPicklist(String sql, Collection<?> params) throws Exception {
		return (Collection<PicklistItem>) executeSelect(sql, params, PicklistItem.class);
	}
	
	public static String getEntityLabel(String alias, int id) {
		List<String> results = null;
		try {
			Class<?> class_ = getClassFromAlias(alias);
			final String table = getTable(class_);
			final String sql = "SELECT " + 
					(alias.equals(Athlete.alias) ? "first_name || ' ' || last_name" : 
					(alias.equals(Contributor.alias) ? "login" : 
					(alias.equals(Olympics.alias) ? "CT.label || ' ' || YR.label" : //TODO le join
					"label"))) +
					" FROM " + table + " WHERE id = ?";
			results = (List<String>) executeSelect(sql, Arrays.asList(id), String.class);
		}
		catch (Exception e_) {
			log.log(Level.SEVERE, e_.getMessage(), e_);
		}
		return (results != null && !results.isEmpty() ? results.get(0) : null);
	}
	
	public static Collection<String> loadLabels(Class<?> class_, String ids, String lang) throws Exception {
		final String table = getTable(class_);
		final String sql = "SELECT x.label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) && class_ != Team.class && class_ != Year.class ? "_" + lang : "")
				+ " FROM " + table + " x"
				+ (StringUtils.notEmpty(ids) ? " WHERE x.id IN (" + ids + ")" : "");
		return (Collection<String>) executeSelect(sql, String.class);
	}

}