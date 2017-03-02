-- Function: "GetMedalCount"(character varying, integer, character varying)

-- DROP FUNCTION "GetMedalCount"(character varying, integer, character varying);

CREATE OR REPLACE FUNCTION "GetMedalCount"(
    _entity character varying,
    _id_sport integer,
    _idlist character varying)
  RETURNS SETOF "~RefItem" AS
$BODY$
declare
	_item "~RefItem"%rowtype;
	_ids integer[];
	_type1 integer;
	_type2 integer;
	_sport_txt varchar(3);
	_index smallint;
	_nfl_ev1 smallint;
	_nfl_ev2 smallint;
	_nba_ev smallint;
	_nhl_ev smallint;
	_mlb_ev smallint;
	_count1 integer;
	_count2 integer;
	_count3 integer;
	_label_tennis_slam1 varchar(45);
	_label_tennis_slam2 varchar(45);
	_label_tennis_slam3 varchar(45);
	_label_tennis_slam4 varchar(45);
	_label_golf_slam1 varchar(45);
	_label_golf_slam2 varchar(45);
	_label_golf_slam3 varchar(45);
	_label_golf_slam4 varchar(45);
	_label_cycling_tour1 varchar(45);
	_label_cycling_tour2 varchar(45);
	_label_cycling_tour3 varchar(45);
begin
	_ids := regexp_split_to_array(_idlist, E'\\-');	
	_label_tennis_slam1 := 'australian open';
	_label_tennis_slam2 := 'french open';
	_label_tennis_slam3 := 'wimbledon';
	_label_tennis_slam4 := 'us open';
	_label_golf_slam1 := 'masters';
	_label_golf_slam2 := 'us open';
	_label_golf_slam3 := 'open championship';
	_label_golf_slam4 := 'pga championship';
	_label_cycling_tour1 := 'giro d''italia';
	_label_cycling_tour2 := 'tour de france';
	_label_cycling_tour3 := 'vuelta a españa';
	_nfl_ev1 := 454;
	_nfl_ev2 := 453;
	_nba_ev := 909;
	_nhl_ev := 573;
	_mlb_ev := 624;
	_index := 1;
	_sport_txt := CAST(_id_sport AS varchar);
	IF (_entity = 'PR') THEN
		_type1 := 1;
		_type2 := 10;
	ELSIF (_entity = 'TM') THEN
		_type1 := 50;
		_type2 := 50;
	ELSIF (_entity = 'CN') THEN
		_type1 := 99;
		_type2 := 99;
	END IF;
	-- Olympics
	IF _type2 <> 50 THEN
		_item.id := _index;
		_item.label := 'OLYMP';
		_item.txt1 := '#GOLD#';
		_item.txt2 := '#SILVER#';
		_item.txt3 := '#BRONZE#';
		SELECT COUNT(*) INTO _item.count1 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, (CASE WHEN id_subevent2 IS NOT NULL THEN T3.number ELSE (CASE WHEN id_subevent IS NOT NULL THEN T2.number ELSE T1.number END) END), _ids) = 1;
		SELECT COUNT(*) INTO _item.count2 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, (CASE WHEN id_subevent2 IS NOT NULL THEN T3.number ELSE (CASE WHEN id_subevent IS NOT NULL THEN T2.number ELSE T1.number END) END), _ids) = 2;
		SELECT COUNT(*) INTO _item.count3 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, (CASE WHEN id_subevent2 IS NOT NULL THEN T3.number ELSE (CASE WHEN id_subevent IS NOT NULL THEN T2.number ELSE T1.number END) END), _ids) = 3;
		IF _type1 = 99 THEN
			SELECT COUNT(*) INTO _count1 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id LEFT JOIN "Athlete" PR1 ON RS.id_rank1=PR1.id LEFT JOIN "Athlete" PR2 ON RS.id_rank2=PR2.id LEFT JOIN "Athlete" PR3 ON RS.id_rank3=PR3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=10) OR (id_subevent IS NOT NULL AND T2.number<=10) OR (id_subevent IS NULL AND T1.number<=10))  AND (PR1.id_country = ANY(_ids) OR (PR2.id_country = ANY(_ids) AND (exa ~* '.*1-(2|3|4|5|6).*')) OR (PR3.id_country = ANY(_ids) AND (exa ~* '.*1-(3|4|5|6).*')));
			SELECT COUNT(*) INTO _count2 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id LEFT JOIN "Athlete" PR2 ON RS.id_rank2=PR2.id LEFT JOIN "Athlete" PR3 ON RS.id_rank3=PR3.id LEFT JOIN "Athlete" PR4 ON RS.id_rank4=PR4.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=10) OR (id_subevent IS NOT NULL AND T2.number<=10) OR (id_subevent IS NULL AND T1.number<=10))  AND (PR2.id_country = ANY(_ids) OR (PR3.id_country = ANY(_ids) AND (exa ~* '.*2-(3|4|5|6).*')) OR (PR4.id_country = ANY(_ids) AND (exa ~* '.*2-(4|5|6).*')));
			SELECT COUNT(*) INTO _count3 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id LEFT JOIN "Athlete" PR3 ON RS.id_rank3=PR3.id LEFT JOIN "Athlete" PR4 ON RS.id_rank4=PR4.id LEFT JOIN "Athlete" PR5 ON RS.id_rank5=PR5.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=10) OR (id_subevent IS NOT NULL AND T2.number<=10) OR (id_subevent IS NULL AND T1.number<=10))  AND (PR3.id_country = ANY(_ids) OR (PR4.id_country = ANY(_ids) AND (exa ~* '.*3-(4|5|6).*')) OR (PR5.id_country = ANY(_ids) AND (exa ~* '.*3-(5|6).*')));
			_item.count1 := _item.count1 + _count1;
			_item.count2 := _item.count2 + _count2;
			_item.count3 := _item.count3 + _count3;
		ELSE
			SELECT COUNT(*) INTO _count1 FROM "~PersonList" PL LEFT JOIN "Result" RS ON PL.id_result=RS.id WHERE id_championship=1 AND PL.rank=1 AND id_person = ANY(_ids);
			SELECT COUNT(*) INTO _count2 FROM "~PersonList" PL LEFT JOIN "Result" RS ON PL.id_result=RS.id WHERE id_championship=1 AND PL.rank=2 AND id_person = ANY(_ids);
			SELECT COUNT(*) INTO _count3 FROM "~PersonList" PL LEFT JOIN "Result" RS ON PL.id_result=RS.id WHERE id_championship=1 AND PL.rank=3 AND id_person = ANY(_ids);
			_item.count1 := _item.count1 + _count1;
			_item.count2 := _item.count2 + _count2;
			_item.count3 := _item.count3 + _count3;
		END IF;
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- World Championships
	IF _type2 <= 10 THEN
		_item.id := _index;	
		_item.label := 'WORLDCP';
		IF _sport_txt ~ '5|18|33|36' THEN
			_item.txt1 := 'rank.1';
			_item.txt2 := 'rank.2';
			_item.txt3 := 'rank.3';
		ELSE
			_item.txt1 := '#GOLD#';
			_item.txt2 := '#SILVER#';
			_item.txt3 := '#BRONZE#';
		END IF;
		SELECT COUNT(*) INTO _item.count1 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, (CASE WHEN id_subevent2 IS NOT NULL THEN T3.number ELSE (CASE WHEN id_subevent IS NOT NULL THEN T2.number ELSE T1.number END) END), _ids) = 1;
		SELECT COUNT(*) INTO _item.count2 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, (CASE WHEN id_subevent2 IS NOT NULL THEN T3.number ELSE (CASE WHEN id_subevent IS NOT NULL THEN T2.number ELSE T1.number END) END), _ids) = 2;
		SELECT COUNT(*) INTO _item.count3 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, (CASE WHEN id_subevent2 IS NOT NULL THEN T3.number ELSE (CASE WHEN id_subevent IS NOT NULL THEN T2.number ELSE T1.number END) END), _ids) = 3;
		SELECT COUNT(*) INTO _count1 FROM "~PersonList" PL LEFT JOIN "Result" RS ON PL.id_result=RS.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND PL.rank=1 AND id_person = ANY(_ids);
		SELECT COUNT(*) INTO _count2 FROM "~PersonList" PL LEFT JOIN "Result" RS ON PL.id_result=RS.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND PL.rank=2 AND id_person = ANY(_ids);
		SELECT COUNT(*) INTO _count3 FROM "~PersonList" PL LEFT JOIN "Result" RS ON PL.id_result=RS.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND PL.rank=3 AND id_person = ANY(_ids);
		_item.count1 := _item.count1 + _count1;
		_item.count2 := _item.count2 + _count2;
		_item.count3 := _item.count3 + _count3;
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- European Championships
	IF _type2 <= 10 THEN
		_item.id := _index;
		_item.label := 'EURCP';
		_item.txt1 := '#GOLD#';
		_item.txt2 := '#SILVER#';
		_item.txt3 := '#BRONZE#';
		SELECT COUNT(*) INTO _item.count1 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE id_championship = 4 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, (CASE WHEN id_subevent2 IS NOT NULL THEN T3.number ELSE (CASE WHEN id_subevent IS NOT NULL THEN T2.number ELSE T1.number END) END), _ids) = 1;
		SELECT COUNT(*) INTO _item.count2 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE id_championship = 4 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, (CASE WHEN id_subevent2 IS NOT NULL THEN T3.number ELSE (CASE WHEN id_subevent IS NOT NULL THEN T2.number ELSE T1.number END) END), _ids) = 2;
		SELECT COUNT(*) INTO _item.count3 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE id_championship = 4 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, (CASE WHEN id_subevent2 IS NOT NULL THEN T3.number ELSE (CASE WHEN id_subevent IS NOT NULL THEN T2.number ELSE T1.number END) END), _ids) = 3;
		SELECT COUNT(*) INTO _count1 FROM "~PersonList" PL LEFT JOIN "Result" RS ON PL.id_result=RS.id WHERE id_championship = 4 AND PL.rank=1 AND id_person = ANY(_ids);
		SELECT COUNT(*) INTO _count2 FROM "~PersonList" PL LEFT JOIN "Result" RS ON PL.id_result=RS.id WHERE id_championship = 4 AND PL.rank=2 AND id_person = ANY(_ids);
		SELECT COUNT(*) INTO _count3 FROM "~PersonList" PL LEFT JOIN "Result" RS ON PL.id_result=RS.id WHERE id_championship = 4 AND PL.rank=3 AND id_person = ANY(_ids);
		_item.count1 := _item.count1 + _count1;
		_item.count2 := _item.count2 + _count2;
		_item.count3 := _item.count3 + _count3;
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- World Cup
	IF _type2 <= 10 THEN
		_item.id := _index;
		_item.label := 'WLDCP';
		_item.txt1 := 'rank.1';
		_item.txt2 := 'rank.2';
		_item.txt3 := 'rank.3';	
		SELECT COUNT(*) INTO _item.count1 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE id_championship = 2 AND lower(EV.label) NOT LIKE '%standings%' AND lower(SE.label) NOT LIKE '%standings%' AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, (CASE WHEN id_subevent2 IS NOT NULL THEN T3.number ELSE (CASE WHEN id_subevent IS NOT NULL THEN T2.number ELSE T1.number END) END), _ids) = 1;
		SELECT COUNT(*) INTO _item.count2 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE id_championship = 2 AND lower(EV.label) NOT LIKE '%standings%' AND lower(SE.label) NOT LIKE '%standings%' AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, (CASE WHEN id_subevent2 IS NOT NULL THEN T3.number ELSE (CASE WHEN id_subevent IS NOT NULL THEN T2.number ELSE T1.number END) END), _ids) = 2;
		SELECT COUNT(*) INTO _item.count3 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE id_championship = 2 AND lower(EV.label) NOT LIKE '%standings%' AND lower(SE.label) NOT LIKE '%standings%' AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, (CASE WHEN id_subevent2 IS NOT NULL THEN T3.number ELSE (CASE WHEN id_subevent IS NOT NULL THEN T2.number ELSE T1.number END) END), _ids) = 3;
		SELECT COUNT(*) INTO _count1 FROM "~PersonList" PL LEFT JOIN "Result" RS ON PL.id_result=RS.id WHERE id_championship = 2 AND PL.rank=1 AND id_person = ANY(_ids);
		SELECT COUNT(*) INTO _count2 FROM "~PersonList" PL LEFT JOIN "Result" RS ON PL.id_result=RS.id WHERE id_championship = 2 AND PL.rank=2 AND id_person = ANY(_ids);
		SELECT COUNT(*) INTO _count3 FROM "~PersonList" PL LEFT JOIN "Result" RS ON PL.id_result=RS.id WHERE id_championship = 2 AND PL.rank=3 AND id_person = ANY(_ids);
		_item.count1 := _item.count1 + _count1;
		_item.count2 := _item.count2 + _count2;
		_item.count3 := _item.count3 + _count3;
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Grand Slam (Tennis)
	IF _id_sport = 22 AND _type2 <= 10 THEN
		_item.id := _index;
		_item.label := 'GSLAM';
		_item.txt1 := 'Aus';
		_item.txt2 := 'RG';
		_item.txt3 := 'Wim';
		_item.txt4 := 'US';
		SELECT COUNT(*) INTO _item.count1 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (lower(EV.label)=_label_tennis_slam1 OR lower(SE.label)=_label_tennis_slam1 OR lower(SE2.label)=_label_tennis_slam1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (lower(EV.label)=_label_tennis_slam2 OR lower(SE.label)=_label_tennis_slam2 OR lower(SE2.label)=_label_tennis_slam2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (lower(EV.label)=_label_tennis_slam3 OR lower(SE.label)=_label_tennis_slam3 OR lower(SE2.label)=_label_tennis_slam3) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count4 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (lower(EV.label)=_label_tennis_slam4 OR lower(SE.label)=_label_tennis_slam4 OR lower(SE2.label)=_label_tennis_slam4) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Majors (Golf)
	IF _id_sport = 20 AND _type2 <= 10 THEN
		_item.id := _index;
		_item.label := 'MAJORS';
		_item.txt1 := 'Mas';
		_item.txt2 := 'US';
		_item.txt3 := 'Brit';
		_item.txt4 := 'PGA';
		SELECT COUNT(*) INTO _item.count1 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (lower(EV.label)=_label_golf_slam1 OR lower(SE.label)=_label_golf_slam1 OR lower(SE2.label)=_label_golf_slam1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (lower(EV.label)=_label_golf_slam2 OR lower(SE.label)=_label_golf_slam2 OR lower(SE2.label)=_label_golf_slam2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (lower(EV.label)=_label_golf_slam3 OR lower(SE.label)=_label_golf_slam3 OR lower(SE2.label)=_label_golf_slam3) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count4 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (lower(EV.label)=_label_golf_slam4 OR lower(SE.label)=_label_golf_slam4 OR lower(SE2.label)=_label_golf_slam4) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Major Tours (Cycling)
	IF _id_sport = 19 AND _type2 <= 10 THEN
		_item.id := _index;
		_item.label := 'MTOURS';
		_item.txt1 := 'Gir';
		_item.txt2 := 'Tour';
		_item.txt3 := 'Vuel';
		SELECT COUNT(*) INTO _item.count1 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (lower(EV.label)=_label_cycling_tour1 OR lower(SE.label)=_label_cycling_tour1 OR lower(SE2.label)=_label_cycling_tour1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (lower(EV.label)=_label_cycling_tour2 OR lower(SE.label)=_label_cycling_tour2 OR lower(SE2.label)=_label_cycling_tour2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "Result" RS LEFT JOIN "Event" EV ON RS.id_event=EV.id LEFT JOIN "Event" SE ON RS.id_subevent=SE.id LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "Type" T1 ON EV.id_type=T1.id LEFT JOIN "Type" T2 ON SE.id_type=T2.id LEFT JOIN "Type" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (lower(EV.label)=_label_cycling_tour3 OR lower(SE.label)=_label_cycling_tour3 OR lower(SE2.label)=_label_cycling_tour3) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- NFL Titles
	IF _id_sport = 23 AND _type2 = 50 THEN
		-- (Super Bowl)
		_item.id := _index;
		_item.label := 'NFLCP1';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "Result" RS WHERE ((id_event=_nfl_ev1 AND id_subevent IS NULL) OR id_subevent=_nfl_ev1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "Result" RS WHERE ((id_event=_nfl_ev1 AND id_subevent IS NULL) OR id_subevent=_nfl_ev1) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
		-- (NFL Championships)
		_item.id := _index;
		_item.label := 'NFLCP2';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "Result" RS WHERE ((id_event=_nfl_ev2 AND id_subevent IS NULL) OR id_subevent=_nfl_ev2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "Result" RS WHERE ((id_event=_nfl_ev2 AND id_subevent IS NULL) OR id_subevent=_nfl_ev2) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	-- NBA Titles
	IF _id_sport = 24 AND _type2 = 50 THEN
		_item.id := _index;
		_item.label := 'NBACP';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "Result" RS WHERE ((id_event=_nba_ev AND id_subevent IS NULL) OR id_subevent=_nba_ev) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "Result" RS WHERE ((id_event=_nba_ev AND id_subevent IS NULL) OR id_subevent=_nba_ev) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	-- NHL Titles
	IF _id_sport = 25 AND _type2 = 50 THEN
		_item.id := _index;
		_item.label := 'NHLCP';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "Result" RS WHERE ((id_event=_nhl_ev AND id_subevent IS NULL) OR id_subevent=_nhl_ev) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "Result" RS WHERE ((id_event=_nhl_ev AND id_subevent IS NULL) OR id_subevent=_nhl_ev) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	-- MLB Titles
	IF _id_sport = 26 AND _type2 = 50 THEN
		_item.id := _index;
		_item.label := 'MLBCP';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "Result" RS WHERE ((id_event=_mlb_ev AND id_subevent IS NULL) OR id_subevent=_mlb_ev) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "Result" RS WHERE ((id_event=_mlb_ev AND id_subevent IS NULL) OR id_subevent=_mlb_ev) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	RETURN;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
