package com.sporthenon.utils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.Athlete;
import com.sporthenon.db.entity.Championship;
import com.sporthenon.db.entity.City;
import com.sporthenon.db.entity.Complex;
import com.sporthenon.db.entity.Country;
import com.sporthenon.db.entity.Event;
import com.sporthenon.db.entity.HallOfFame;
import com.sporthenon.db.entity.OlympicRanking;
import com.sporthenon.db.entity.Olympics;
import com.sporthenon.db.entity.Record;
import com.sporthenon.db.entity.RetiredNumber;
import com.sporthenon.db.entity.RoundType;
import com.sporthenon.db.entity.Sport;
import com.sporthenon.db.entity.State;
import com.sporthenon.db.entity.Team;
import com.sporthenon.db.entity.TeamStadium;
import com.sporthenon.db.entity.WinLoss;
import com.sporthenon.db.entity.Year;
import com.sporthenon.db.entity.meta.Contributor;

public class UpdateUtils {
	
	private static final Logger log = Logger.getLogger(UpdateUtils.class.getName());
	
	public static Object saveEntity(String alias, Object id, Map<?, ?> params, Contributor cb) throws Exception {
		Class<?> c = DatabaseManager.getClassFromAlias(alias);
		Object o = (StringUtils.toInt(id) > 0 ? DatabaseManager.loadEntity(c, StringUtils.toInt(id)) : c.getConstructor().newInstance());
		if (alias.equalsIgnoreCase(Athlete.alias)) {
			Athlete en = (Athlete) o;
			en.setIdSport(StringUtils.toInt(params.get("pr-sport")));
			en.setIdTeam(StringUtils.toInt(params.get("pr-team")));
			en.setIdCountry(StringUtils.toInt(params.get("pr-country")));
			en.setLastName(String.valueOf(params.get("pr-lastname")).trim());
			en.setFirstName(String.valueOf(params.get("pr-firstname")).trim());
			en.setLink(StringUtils.notEmpty(params.get("pr-link")) ? StringUtils.toInt(params.get("pr-link")) : null);
			if (en.getLink() != null && en.getLink() > 0) {
				try {
					Athlete a = (Athlete) DatabaseManager.loadEntity(Athlete.class, en.getLink());
					while (a.getLink() != null && a.getLink() > 0) {
						a = (Athlete) DatabaseManager.loadEntity(Athlete.class, a.getLink());
					}
					en.setLink(a.getId());
					DatabaseManager.executeUpdate("UPDATE athlete SET LINK = 0 WHERE ID = ?", Arrays.asList(en.getLink()));
				}
				catch (Exception e) {
					log.log(Level.WARNING, e.getMessage());
				}
			}
		}
		else if (alias.equalsIgnoreCase(com.sporthenon.db.entity.Calendar.alias)) {
			com.sporthenon.db.entity.Calendar en = (com.sporthenon.db.entity.Calendar) o;
			en.setIdSport(StringUtils.toInt(params.get("cl-sport")));
			en.setIdChampionship(StringUtils.toInt(params.get("cl-championship")));
			en.setIdEvent(StringUtils.toInt(params.get("cl-event")));
			en.setIdSubevent(StringUtils.toInt(params.get("cl-subevent")));
			en.setIdSubevent2(StringUtils.toInt(params.get("cl-subevent2")));
			en.setIdComplex(StringUtils.toInt(params.get("cl-complex")));
			en.setIdCity(StringUtils.toInt(params.get("cl-city")));
			en.setIdCountry(StringUtils.toInt(params.get("cl-country")));
			en.setDate1(StringUtils.notEmpty(params.get("cl-date1")) ? String.valueOf(params.get("cl-date1")) : null);
			en.setDate2(StringUtils.notEmpty(params.get("cl-date2")) ? String.valueOf(params.get("cl-date2")) : null);
		}
		else if (alias.equalsIgnoreCase(Championship.alias)) {
			Championship en = (Championship) o;
			en.setLabel(String.valueOf(params.get("cp-label")));
			en.setLabelFr(String.valueOf(params.get("cp-labelfr")));
			en.setIndex(StringUtils.notEmpty(params.get("cp-index")) ? Double.valueOf(String.valueOf(params.get("cp-index"))) : Double.MAX_VALUE);
			en.setNopic(StringUtils.notEmpty(params.get("cp-nopic")) ? String.valueOf(params.get("cp-nopic")).equals("1") : null);
		}
		else if (alias.equalsIgnoreCase(City.alias)) {
			City en = (City) o;
			en.setLabel(String.valueOf(params.get("ct-label")));
			en.setLabelFr(String.valueOf(params.get("ct-labelfr")));
			en.setIdState(StringUtils.toInt(params.get("ct-state")));
			en.setIdCountry(StringUtils.toInt(params.get("ct-country")));
			en.setLink(StringUtils.notEmpty(params.get("ct-link")) ? StringUtils.toInt(params.get("ct-link")) : null);
			if (en.getLink() != null && en.getLink() > 0) {
				try {
					City c_ = (City) DatabaseManager.loadEntity(City.class, en.getLink());
					while (c_.getLink() != null && c_.getLink() > 0) {
						c_ = (City) DatabaseManager.loadEntity(City.class, c_.getLink());
					}
					en.setLink(c_.getId());
					DatabaseManager.executeUpdate("UPDATE city SET LINK = 0 WHERE ID = ?", Arrays.asList(en.getLink()));
				}
				catch (Exception e) {
					log.log(Level.WARNING, e.getMessage());
				}
			}
		}
		else if (alias.equalsIgnoreCase(Complex.alias)) {
			Complex en = (Complex) o;
			en.setLabel(String.valueOf(params.get("cx-label")));
			en.setIdCity(StringUtils.toInt(params.get("cx-city")));
			en.setLink(StringUtils.notEmpty(params.get("cx-link")) ? StringUtils.toInt(params.get("cx-link")) : null);
			if (en.getLink() != null && en.getLink() > 0) {
				try {
					Complex c_ = (Complex) DatabaseManager.loadEntity(Complex.class, en.getLink());
					while (c_.getLink() != null && c_.getLink() > 0) {
						c_ = (Complex) DatabaseManager.loadEntity(Complex.class, c_.getLink());
					}
					en.setLink(c_.getId());
					DatabaseManager.executeUpdate("UPDATE complex SET LINK = 0 WHERE ID = ?", Arrays.asList(en.getLink()));
				}
				catch (Exception e) {
					log.log(Level.WARNING, e.getMessage());
				}
			}
		}
		else if (alias.equalsIgnoreCase(Contributor.alias)) {
			Contributor en = (Contributor) o;
			en.setLogin(String.valueOf(params.get("cb-login")));
			en.setPublicName(String.valueOf(params.get("cb-name")));
			en.setEmail(String.valueOf(params.get("cb-email")));
			en.setActive(String.valueOf(params.get("cb-active")).equals("true"));
			en.setAdmin(String.valueOf(params.get("cb-admin")).equals("true"));
			en.setSports(StringUtils.notEmpty(params.get("cb-sports")) ? String.valueOf(params.get("cb-sports")) : null);
		}
		else if (alias.equalsIgnoreCase(Country.alias)) {
			Country en = (Country) o;
			en.setLabel(String.valueOf(params.get("cn-label")));
			en.setLabelFr(String.valueOf(params.get("cn-labelfr")));
			en.setCode(String.valueOf(params.get("cn-code")));
			en.setNopic(StringUtils.notEmpty(params.get("cn-nopic")) ? String.valueOf(params.get("cn-nopic")).equals("1") : null);
		}
		else if (alias.equalsIgnoreCase(Event.alias)) {
			Event en = (Event) o;
			en.setLabel(String.valueOf(params.get("ev-label")));
			en.setLabelFr(String.valueOf(params.get("ev-labelfr")));
			en.setIdType(StringUtils.toInt(params.get("ev-type")));
			en.setIndex(StringUtils.notEmpty(params.get("ev-index")) ? Double.valueOf(String.valueOf(params.get("ev-index"))) : Double.MAX_VALUE);
			en.setNopic(StringUtils.notEmpty(params.get("ev-nopic")) ? String.valueOf(params.get("ev-nopic")).equals("1") : null);
		}
		else if (alias.equalsIgnoreCase(HallOfFame.alias)) {
			HallOfFame en = (HallOfFame) o;
			en.setIdLeague(StringUtils.toInt(params.get("hf-league")));
			en.setIdYear(StringUtils.toInt(params.get("hf-year")));
			en.setIdPerson(StringUtils.toInt(params.get("hf-person")));
			en.setPosition(StringUtils.notEmpty(params.get("hf-position")) ? String.valueOf(params.get("hf-position")) : null);
		}
		else if (alias.equalsIgnoreCase(Olympics.alias)) {
			Olympics en = (Olympics) o;
			en.setIdYear(StringUtils.toInt(params.get("ol-year")));
			en.setIdCity(StringUtils.toInt(params.get("ol-city")));
			en.setType(StringUtils.notEmpty(params.get("ol-type")) ? StringUtils.toInt(params.get("ol-type")) : 0);
			en.setDate1(String.valueOf(params.get("ol-start")));
			en.setDate2(String.valueOf(params.get("ol-end")));
			en.setCountSport(StringUtils.notEmpty(params.get("ol-sports")) ? StringUtils.toInt(params.get("ol-sports")) : 0);
			en.setCountEvent(StringUtils.notEmpty(params.get("ol-events")) ? StringUtils.toInt(params.get("ol-events")) : 0);
			en.setCountCountry(StringUtils.notEmpty(params.get("ol-countries")) ? StringUtils.toInt(params.get("ol-countries")) : 0);
			en.setCountPerson(StringUtils.notEmpty(params.get("ol-persons")) ? StringUtils.toInt(params.get("ol-persons")) : 0);
			en.setNopic(StringUtils.notEmpty(params.get("ol-nopic")) ? String.valueOf(params.get("ol-nopic")).equals("1") : null);
		}
		else if (alias.equalsIgnoreCase(OlympicRanking.alias)) {
			OlympicRanking en = (OlympicRanking) o;
			en.setIdOlympics(StringUtils.toInt(params.get("or-olympics")));
			en.setIdCountry(StringUtils.toInt(params.get("or-country")));
			en.setCountGold(StringUtils.notEmpty(params.get("or-gold")) ? StringUtils.toInt(params.get("or-gold")) : 0);
			en.setCountSilver(StringUtils.notEmpty(params.get("or-silver")) ? StringUtils.toInt(params.get("or-silver")) : 0);
			en.setCountBronze(StringUtils.notEmpty(params.get("or-bronze")) ? StringUtils.toInt(params.get("or-bronze")) : 0);
		}
		else if (alias.equalsIgnoreCase(Record.alias)) {
			Record en = (Record) o;
			en.setIdSport(StringUtils.toInt(params.get("rc-sport")));
			en.setIdChampionship(StringUtils.toInt(params.get("rc-championship")));
			en.setIdEvent(StringUtils.toInt(params.get("rc-event")));
			en.setIdSubevent(StringUtils.toInt(params.get("rc-subevent")));
			en.setIdCity(StringUtils.toInt(params.get("rc-city")));
			en.setType1(StringUtils.notEmpty(params.get("rc-type1")) ? String.valueOf(params.get("rc-type1")) : null);
			en.setType2(StringUtils.notEmpty(params.get("rc-type2")) ? String.valueOf(params.get("rc-type2")) : null);
			en.setLabel(StringUtils.notEmpty(params.get("rc-label")) ? String.valueOf(params.get("rc-label")) : null);
			en.setIdRank1(StringUtils.notEmpty(params.get("rc-rank1")) ? StringUtils.toInt(params.get("rc-rank1")) : null);
			en.setRecord1(StringUtils.notEmpty(params.get("rc-record1")) ? String.valueOf(params.get("rc-record1")) : null);
			en.setDate1(StringUtils.notEmpty(params.get("rc-date1")) ? String.valueOf(params.get("rc-date1")) : null);
			en.setIdRank2(StringUtils.notEmpty(params.get("rc-rank2")) ? StringUtils.toInt(params.get("rc-rank2")) : null);
			en.setRecord2(StringUtils.notEmpty(params.get("rc-record2")) ? String.valueOf(params.get("rc-record2")) : null);
			en.setDate2(StringUtils.notEmpty(params.get("rc-date2")) ? String.valueOf(params.get("rc-date2")) : null);
			en.setIdRank3(StringUtils.notEmpty(params.get("rc-rank3")) ? StringUtils.toInt(params.get("rc-rank3")) : null);
			en.setRecord3(StringUtils.notEmpty(params.get("rc-record3")) ? String.valueOf(params.get("rc-record3")) : null);
			en.setDate3(StringUtils.notEmpty(params.get("rc-date3")) ? String.valueOf(params.get("rc-date3")) : null);
			en.setIdRank4(StringUtils.notEmpty(params.get("rc-rank4")) ? StringUtils.toInt(params.get("rc-rank4")) : null);
			en.setRecord4(StringUtils.notEmpty(params.get("rc-record4")) ? String.valueOf(params.get("rc-record4")) : null);
			en.setDate4(StringUtils.notEmpty(params.get("rc-date4")) ? String.valueOf(params.get("rc-date4")) : null);
			en.setIdRank5(StringUtils.notEmpty(params.get("rc-rank5")) ? StringUtils.toInt(params.get("rc-rank5")) : null);
			en.setRecord5(StringUtils.notEmpty(params.get("rc-record5")) ? String.valueOf(params.get("rc-record5")) : null);
			en.setDate5(StringUtils.notEmpty(params.get("rc-date5")) ? String.valueOf(params.get("rc-date5")) : null);
			en.setCounting(StringUtils.notEmpty(params.get("rc-counting")) ? String.valueOf(params.get("rc-counting")).equals("1") : null);
			en.setIndex(StringUtils.notEmpty(params.get("rc-index")) ? new BigDecimal(String.valueOf(params.get("rc-index"))) : null);
			en.setExa(StringUtils.notEmpty(params.get("rc-tie")) ? String.valueOf(params.get("rc-tie")) : null);
			en.setComment(StringUtils.notEmpty(params.get("rc-comment")) ? String.valueOf(params.get("rc-comment")) : null);
		}
		else if (alias.equalsIgnoreCase(RetiredNumber.alias)) {
			RetiredNumber en = (RetiredNumber) o;
			en.setIdLeague(StringUtils.toInt(params.get("rn-league")));
			en.setIdTeam(StringUtils.toInt(params.get("rn-team")));
			en.setIdPerson(StringUtils.toInt(params.get("rn-person")));
			en.setIdYear(StringUtils.toInt(params.get("rn-year")));
			en.setNumber(StringUtils.notEmpty(params.get("rn-number")) ? StringUtils.toInt(params.get("rn-number")) : null);
		}
		else if (alias.equalsIgnoreCase(RoundType.alias)) {
			RoundType en = (RoundType) o;
			en.setLabel(String.valueOf(params.get("rt-label")));
			en.setLabelFr(String.valueOf(params.get("rt-labelfr")));
			en.setIndex(StringUtils.notEmpty(params.get("rt-index")) ? Double.valueOf(String.valueOf(params.get("rt-index"))) : Double.MAX_VALUE);
		}
		else if (alias.equalsIgnoreCase(Sport.alias)) {
			Sport en = (Sport) o;
			en.setLabel(String.valueOf(params.get("sp-label")));
			en.setLabelFr(String.valueOf(params.get("sp-labelfr")));
			en.setType(StringUtils.notEmpty(params.get("sp-type")) ? StringUtils.toInt(params.get("sp-type")) : 0);
			en.setIndex(StringUtils.notEmpty(params.get("sp-index")) ? Double.valueOf(String.valueOf(params.get("sp-index"))) : Double.MAX_VALUE);
			en.setNopic(StringUtils.notEmpty(params.get("sp-nopic")) ? String.valueOf(params.get("sp-nopic")).equals("1") : null);
		}
		else if (alias.equalsIgnoreCase(State.alias)) {
			State en = (State) o;
			en.setLabel(String.valueOf(params.get("st-label")));
			en.setLabelFr(String.valueOf(params.get("st-labelfr")));
			en.setCode(String.valueOf(params.get("st-code")));
			en.setCapital(String.valueOf(params.get("st-capital")));
			en.setNopic(StringUtils.notEmpty(params.get("st-nopic")) ? String.valueOf(params.get("st-nopic")).equals("1") : null);
		}
		else if (alias.equalsIgnoreCase(Team.alias)) {
			Team en = (Team) o;
			en.setLabel(String.valueOf(params.get("tm-label")).trim());
			en.setIdSport(StringUtils.toInt(params.get("tm-sport")));
			en.setIdCountry(StringUtils.toInt(params.get("tm-country")));
			en.setIdLeague(StringUtils.toInt(params.get("tm-league")));
			en.setConference(String.valueOf(params.get("tm-conference")));
			en.setDivision(String.valueOf(params.get("tm-division")));
			en.setComment(String.valueOf(params.get("tm-comment")));
			en.setYear1(String.valueOf(params.get("tm-year1")));
			en.setYear2(String.valueOf(params.get("tm-year2")));
			en.setInactive(StringUtils.notEmpty(params.get("tm-inactive")) ? String.valueOf(params.get("tm-inactive")).equals("1") : null);
			en.setNopic(StringUtils.notEmpty(params.get("tm-nopic")) ? String.valueOf(params.get("tm-nopic")).equals("1") : null);
			en.setLink(StringUtils.notEmpty(params.get("tm-link")) ? StringUtils.toInt(params.get("tm-link")) : null);
			if (en.getLink() != null && en.getLink() > 0) {
				try {
					Team t = (Team) DatabaseManager.loadEntity(Team.class, en.getLink());
					while (t.getLink() != null && t.getLink() > 0) {
						t = (Team) DatabaseManager.loadEntity(Team.class, t.getLink());
					}
					en.setLink(t.getId());
					DatabaseManager.executeUpdate("UPDATE team SET LINK = 0 WHERE ID = ?", Arrays.asList(en.getLink()));
				}
				catch (Exception e) {
					log.log(Level.WARNING, e.getMessage());
				}
			}
		}
		else if (alias.equalsIgnoreCase(TeamStadium.alias)) {
			TeamStadium en = (TeamStadium) o;
			en.setIdLeague(StringUtils.toInt(params.get("ts-league")));
			en.setIdTeam(StringUtils.toInt(params.get("ts-team")));
			en.setIdComplex(StringUtils.toInt(params.get("ts-complex")));
			en.setDate1(StringUtils.notEmpty(params.get("ts-date1")) ? StringUtils.toInt(params.get("ts-date1")) : null);
			en.setDate2(StringUtils.notEmpty(params.get("ts-date2")) ? StringUtils.toInt(params.get("ts-date2")) : null);
			en.setRenamed(StringUtils.notEmpty(params.get("ts-renamed")) ? String.valueOf(params.get("ts-renamed")).equals("1") : null);
		}
		else if (alias.equalsIgnoreCase(WinLoss.alias)) {
			WinLoss en = (WinLoss) o;
			en.setIdLeague(StringUtils.toInt(params.get("wl-league")));
			en.setIdTeam(StringUtils.toInt(params.get("wl-team")));
			en.setType(StringUtils.notEmpty(params.get("wl-type")) ? String.valueOf(params.get("wl-type")) : null);
			en.setCountWin(StringUtils.notEmpty(params.get("wl-win")) ? StringUtils.toInt(params.get("wl-win")) : null);
			en.setCountLoss(StringUtils.notEmpty(params.get("wl-loss")) ? StringUtils.toInt(params.get("wl-loss")) : null);
			en.setCountTie(StringUtils.notEmpty(params.get("wl-tie")) ? StringUtils.toInt(params.get("wl-tie")) : null);
			en.setCountOtloss(StringUtils.notEmpty(params.get("wl-otloss")) ? StringUtils.toInt(params.get("wl-otloss")) : null);
		}
		else if (alias.equalsIgnoreCase(Year.alias)) {
			Year en = (Year) o;
			en.setLabel(String.valueOf(params.get("yr-label")));
		}
		o = DatabaseManager.saveEntity(o, cb);
		String id_ = String.valueOf(c.getMethod("getId").invoke(o, new Object[0]));
		if (alias.matches(Athlete.alias + "|" + Championship.alias + "|" + City.alias + "|" + Complex.alias + "|" + Country.alias + "|" + Event.alias + "|" + Olympics.alias + "|" + Sport.alias + "|" + State.alias + "|" + Team.alias)) {
			DatabaseManager.saveExternalLinks(alias, Integer.parseInt(id_), String.valueOf(params.get("exl")));
		}
		return o;
	}
	
}