package com.sporthenon.test;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;

import com.sporthenon.db.DatabaseHelper;
import com.sporthenon.db.entity.meta.RefItem;
import com.sporthenon.db.entity.meta.TreeItem;
import com.sporthenon.db.function.HallOfFameBean;
import com.sporthenon.db.function.OlympicMedalsBean;
import com.sporthenon.db.function.OlympicRankingsBean;
import com.sporthenon.db.function.ResultsBean;
import com.sporthenon.db.function.RetiredNumberBean;
import com.sporthenon.db.function.TeamStadiumBean;
import com.sporthenon.db.function.USChampionshipsBean;
import com.sporthenon.db.function.USRecordsBean;

public class FunctionTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		BasicConfigurator.configure();
		DatabaseHelper.setFactory(null, "standalone");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testGetResults() throws Exception {
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new Integer(1));
		params.add(new Integer(1));
		params.add(new Integer(188));
		params.add(new Integer(0));
		params.add(new String("146,154"));
		params.add(new String(""));
		params.add(new Integer(0));
		Collection<Object> col = DatabaseHelper.call("GetResults", params);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		ArrayList<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof ResultsBean);
//		for (Object obj : lst)
//			System.out.println(obj);
	}
	
	public void testTreeResults() throws Exception {
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new String(""));
		params.add(new String(""));
		Collection col = DatabaseHelper.call("TreeResults", params);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		ArrayList<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof TreeItem);
//		for (Object obj : lst)
//			System.out.println(obj);
	}
	
	public void testSearch() throws Exception {
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new String("samp.*"));
		params.add(new String("CP,PR,TM"));
		params.add(new Short("1"));
		params.add(new String(""));
		Collection col = DatabaseHelper.call("Search", params);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		ArrayList<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof RefItem);
//		for (Object obj : lst)
//			System.out.println(obj);
	}
	
	public void testOlympicMedals() throws Exception {
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new String("1"));
		params.add(new Integer(1));
		params.add(new String("188,189"));
		params.add(new String("1"));
		params.add(new String(""));
		Collection col = DatabaseHelper.call("GetOlympicMedals", params);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		ArrayList<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof OlympicMedalsBean);
//		for (Object obj : lst)
//			System.out.println(obj);
	}
	
	public void testOlympicRankings() throws Exception {
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new String("0"));
		params.add(new String("0"));
		params.add(new String(""));
		Collection col = DatabaseHelper.call("GetOlympicRankings", params);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		ArrayList<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof OlympicRankingsBean);
//		for (Object obj : lst)
//			System.out.println(obj);
	}
	
	public void testHallOfFame() throws Exception {
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new Integer(1));
		params.add(new String("0"));
		params.add(new String("QB"));
		Collection col = DatabaseHelper.call("GetHallOfFame", params);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		ArrayList<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof HallOfFameBean);
//		for (Object obj : lst)
//			System.out.println(obj);
	}
	
	public void testRetiredNumber() throws Exception {
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new Integer(2));
		params.add(new String("0"));
		params.add(Short.valueOf("23"));
		Collection col = DatabaseHelper.call("GetRetiredNumber", params);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		ArrayList<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof RetiredNumberBean);
//		for (Object obj : lst)
//			System.out.println(obj);
	}
	
	public void testTeamStadium() throws Exception {
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new Integer(1));
		params.add(new String("0"));
		params.add(new String(""));
		Collection col = DatabaseHelper.call("GetTeamStadium", params);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		ArrayList<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof TeamStadiumBean);
//		for (Object obj : lst)
//			System.out.println(obj);
	}

	public void testWinRecords() throws Exception {
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new String("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26"));
		params.add(new String(""));
		Collection col = DatabaseHelper.call("WinRecords", params);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		ArrayList<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof RefItem);
//		for (Object obj : lst)
//			System.out.println(obj);
	}
	
	public void testEntityRef() throws Exception {
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new String("CN"));
		params.add(new Integer(1));
		params.add(new String(""));
		params.add(new String(""));
		Collection col = DatabaseHelper.call("EntityRef", params);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		ArrayList<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof RefItem);
//		for (Object obj : lst)
//			System.out.println(obj);
	}
	
	public void testUSChampionships() throws Exception {
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new Integer(51));
		params.add(new String("0"));
		params.add(new String(""));
		Collection col = DatabaseHelper.call("GetUSChampionships", params);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		ArrayList<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof USChampionshipsBean);
//		for (Object obj : lst)
//			System.out.println(obj);
	}
	
	public void testUSRecordsBean() throws Exception {
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(new Integer(51));
		params.add(new String("0"));
		params.add(new String("0"));
		params.add(new String("0"));
		params.add(new String("0"));
		params.add(new String(""));
		Collection col = DatabaseHelper.call("GetUSRecords", params);
		assertNotNull(col);
		assertTrue(col.size() > 0);
		ArrayList<Object> lst = new ArrayList<Object>(col);
		assertTrue(lst.get(0) instanceof USRecordsBean);
//		for (Object obj : lst)
//			System.out.println(obj);
	}
	
}
