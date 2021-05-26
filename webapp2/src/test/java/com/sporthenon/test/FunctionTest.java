package com.sporthenon.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.meta.RefItem;
import com.sporthenon.db.entity.meta.TreeItem;
import com.sporthenon.db.function.HallOfFameBean;
import com.sporthenon.db.function.OlympicMedalsBean;
import com.sporthenon.db.function.OlympicRankingsBean;
import com.sporthenon.db.function.PersonListBean;
import com.sporthenon.db.function.ResultsBean;
import com.sporthenon.db.function.RetiredNumberBean;
import com.sporthenon.db.function.RoundsBean;
import com.sporthenon.db.function.TeamStadiumBean;
import com.sporthenon.db.function.USChampionshipsBean;
import com.sporthenon.db.function.USRecordsBean;
import com.sporthenon.db.function.YearlyStatsBean;
import com.sporthenon.utils.StringUtils;

import junit.framework.TestCase;

@SuppressWarnings({"rawtypes", "unchecked"})
public class FunctionTest extends TestCase {

	private static final Logger log = Logger.getLogger(FunctionTest.class.getName());
	
	@Override
	protected void setUp() throws Exception {
		final String dbHost = 	System.getenv("SHDB_HOST");
		final String dbPort = 	System.getenv("SHDB_PORT");
		final String dbName = 	System.getenv("SHDB_NAME");
		final String dbUser = 	System.getenv("SHDB_USER");
		final String dbPwd = 	System.getenv("SHDB_PWD");
		try {
			DatabaseManager.createConnectionPool(dbHost, dbPort, dbName, dbUser, dbPwd);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		super.setUp();
	}
	
	public void testCountRef() throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(String.valueOf(Athlete.alias));
		params.add(StringUtils.toInt(1));
		Collection col = DatabaseManager.callFunctionSelect("count_ref", params, Integer.class);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		List<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof Integer);
	}
	
	public void testEntityRef() throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(String.valueOf(Country.alias));
		params.add(StringUtils.toInt(92));
		params.add(String.valueOf(""));
		params.add(String.valueOf("10"));
		params.add(StringUtils.toInt(0));
		params.add(String.valueOf(""));
		Collection col = DatabaseManager.callFunctionSelect("entity_ref", params, RefItem.class);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		List<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof RefItem);
	}
	
	public void testCalendarResults() throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(String.valueOf("19100101"));
		params.add(String.valueOf("19120201"));
		params.add(StringUtils.toInt(0));
		params.add(String.valueOf("_fr"));
		Collection col = DatabaseManager.callFunctionSelect("get_calendar_results", params, RefItem.class, "entity DESC, date2 DESC");
		assertNotNull(col);
		assertTrue(col.size() > 0);
		List<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof RefItem);
	}
	
	public void testHallOfFame() throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(StringUtils.toInt(1));
		params.add(String.valueOf("0"));
		params.add(String.valueOf("QB"));
		Collection col = DatabaseManager.callFunction("get_hall_of_fame", params, HallOfFameBean.class);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		List<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof HallOfFameBean);
	}
	
	public void testMedalCount() throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(Athlete.alias);
		params.add(StringUtils.toInt(1));
		params.add(String.valueOf("1"));
		Collection col = DatabaseManager.callFunctionSelect("get_medal_count", params, RefItem.class);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		List<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof RefItem);
	}
	
	public void testOlympicMedals() throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(String.valueOf("1"));
		params.add(StringUtils.toInt(1));
		params.add(String.valueOf("188,189"));
		params.add(String.valueOf("1"));
		params.add(String.valueOf("0"));
		params.add(String.valueOf(""));
		Collection col = DatabaseManager.callFunction("get_olympic_medals", params, OlympicMedalsBean.class);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		List<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof OlympicMedalsBean);
	}
	
	public void testOlympicRankings() throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(String.valueOf("0"));
		params.add(String.valueOf("0"));
		params.add(String.valueOf(""));
		Collection col = DatabaseManager.callFunction("get_olympic_rankings", params, OlympicRankingsBean.class);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		List<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof OlympicRankingsBean);
	}
	
	public void testPersonList() throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(String.valueOf("4798"));
		Collection col = DatabaseManager.callFunction("get_person_list", params, PersonListBean.class);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		List<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof PersonListBean);
	}
	
	public void testGetResults() throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(StringUtils.toInt(1));
		params.add(StringUtils.toInt(1));
		params.add(StringUtils.toInt(188));
		params.add(StringUtils.toInt(0));
		params.add(StringUtils.toInt(0));
		params.add(String.valueOf("146,154"));
		params.add(StringUtils.toInt(0));
		params.add(String.valueOf(""));
		Collection col = DatabaseManager.callFunction("get_results", params, ResultsBean.class);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		List<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof ResultsBean);
	}
	
	public void testRetiredNumbers() throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(StringUtils.toInt(2));
		params.add(String.valueOf("0"));
		params.add(Short.valueOf("23"));
		Collection col = DatabaseManager.callFunction("get_retired_numbers", params, RetiredNumberBean.class);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		List<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof RetiredNumberBean);
	}
	
	public void testRounds() throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(StringUtils.toInt(32847));
		params.add(String.valueOf("_fr"));
		Collection col = DatabaseManager.callFunction("get_rounds", params, RoundsBean.class);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		List<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof RoundsBean);
	}
	
	public void testTeamStadiums() throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(StringUtils.toInt(1));
		params.add(String.valueOf("0"));
		params.add(String.valueOf(""));
		Collection col = DatabaseManager.callFunction("get_team_stadiums", params, TeamStadiumBean.class);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		List<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof TeamStadiumBean);
	}
	
	public void testUSChampionships() throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(StringUtils.toInt(51));
		params.add(String.valueOf("0"));
		params.add(String.valueOf(""));
		Collection col = DatabaseManager.callFunction("get_us_championships", params, USChampionshipsBean.class);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		List<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof USChampionshipsBean);
	}
	
	public void testUSRecords() throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(StringUtils.toInt(51));
		params.add(String.valueOf("0"));
		params.add(String.valueOf("0"));
		params.add(String.valueOf("0"));
		params.add(String.valueOf("0"));
		params.add(String.valueOf(""));
		Collection col = DatabaseManager.callFunction("get_us_records", params, USRecordsBean.class);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		List<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof USRecordsBean);
	}
	
	public void testYearlyStats() throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(StringUtils.toInt(51));
		params.add(String.valueOf("0"));
		params.add(String.valueOf("0"));
		params.add(Boolean.valueOf(true));
		params.add(Boolean.valueOf(false));
		params.add(String.valueOf(""));
		Collection col = DatabaseManager.callFunction("get_yearly_stats", params, YearlyStatsBean.class);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		List<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof YearlyStatsBean);
	}
	
	public void testSearch() throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(String.valueOf("samp.*"));
		params.add(String.valueOf("CP,PR,TM"));
		params.add(Short.valueOf("10"));
		params.add(Boolean.valueOf(false));
		params.add(String.valueOf(""));
		//params.add(Short.valueOf("10"));
		Collection col = DatabaseManager.callFunctionSelect("search", params, RefItem.class);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		List<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof RefItem);
	}
	
	public void testTreeMonths() throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(String.valueOf("RS.id_year=1"));
		params.add(String.valueOf("_fr"));
		Collection col = DatabaseManager.callFunctionSelect("tree_months", params, TreeItem.class);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		List<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof TreeItem);
	}
	
	public void testTreeResults() throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(String.valueOf(""));
		params.add(String.valueOf(""));
		Collection col = DatabaseManager.callFunctionSelect("tree_results", params, TreeItem.class);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		List<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof TreeItem);
	}
	
	public void testWinRecords() throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(String.valueOf("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26"));
		params.add(String.valueOf(""));
		Collection col = DatabaseManager.callFunctionSelect("win_records", params, RefItem.class);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		List<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof RefItem);
	}
	
}
