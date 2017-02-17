ALTER TABLE "Result" add no_place boolean;

ALTER TABLE "Result" add no_date boolean;

CREATE OR REPLACE FUNCTION "~Overview"(
    _entity character varying,
    _id_sport integer,
    _count integer,
    _pattern character varying,
    _id1 integer,
    _id2 integer,
    _lang character varying)
  RETURNS SETOF "~RefItem" AS
$BODY$
declare
	_item "~RefItem"%rowtype;
	_c refcursor;
	_index integer;
	_query text;
begin
	_index := 1;
	-- Results
	IF (_entity = 'RS' OR _entity = '') THEN
		_query = 'SELECT RS.id, YR.label, SP.label' || _lang || ', CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', SE2.label' || _lang || ',';
		_query = _query || 'concat_ws('','', RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, RS.id_rank11, RS.id_rank12, RS.id_rank13, RS.id_rank14, RS.id_rank15, RS.id_rank16, RS.id_rank17, RS.id_rank18, RS.id_rank19, RS.id_rank20) AS ranks,';
		_query = _query || 'concat_ws('','', RS.result1, RS.result2, RS.result3, RS.result4, RS.result5, RS.result6, RS.result7, RS.result8, RS.result9, RS.result10, RS.result11, RS.result12, RS.result13, RS.result14, RS.result15, RS.result16, RS.result17, RS.result18, RS.result19, RS.result20) AS results,';
		_query = _query || 'concat_ws('','', coalesce(RS.id_complex1, ''0''), coalesce(RS.id_complex2, ''0''), coalesce(RS.id_city1, ''0''), coalesce(RS.id_city2, ''0'')) AS places, concat_ws('','', coalesce(RS.date1, ''0''), coalesce(RS.date2, ''0'')) AS dates, TP1.number, TP2.number, TP3.number, string_agg(DISTINCT(CAST (EL.id AS VARCHAR)), '', ''), string_agg(DISTINCT(CAST (RD.id AS VARCHAR)), '', ''), (CASE WHEN RS.no_date=true THEN 1 ELSE 0 END), (CASE WHEN RS.no_place=true THEN 1 ELSE 0 END)';
		_query = _query || ' FROM "Result" RS';
		_query = _query || ' LEFT JOIN "Year" YR ON RS.id_year = YR.id';
		_query = _query || ' LEFT JOIN "Sport" SP ON RS.id_sport = SP.id';
		_query = _query || ' LEFT JOIN "Championship" CP ON RS.id_championship = CP.id';
		_query = _query || ' LEFT JOIN "Event" EV ON RS.id_event = EV.id';
		_query = _query || ' LEFT JOIN "Event" SE ON RS.id_subevent = SE.id';
		_query = _query || ' LEFT JOIN "Event" SE2 ON RS.id_subevent2 = SE2.id';
		_query = _query || ' LEFT JOIN "Type" TP1 ON EV.id_type = TP1.id';
		_query = _query || ' LEFT JOIN "Type" TP2 ON SE.id_type = TP2.id';
		_query = _query || ' LEFT JOIN "Type" TP3 ON SE2.id_type = TP3.id';
		_query = _query || ' LEFT JOIN "~ExternalLink" EL ON (EL.id_item = RS.id AND EL.entity=''RS'')';
		_query = _query || ' LEFT JOIN "Round" RD ON RD.id_result = RS.id';
		_query = _query || ' WHERE 0=1';
		IF _id_sport > 0 THEN
			_query = _query || ' OR SP.id = ' || _id_sport;
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		IF (_id1 > 0 AND _id2 > 0) THEN
			_query = _query || ' AND RS.id BETWEEN ' || _id1 || ' AND ' || _id2;
		ELSIF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' AND (lower(SP.label' || _lang || ') like ''' || lower(_pattern) || '%'' OR lower(EV.label' || _lang || ') like ''' || lower(_pattern) || '%'' OR lower(EV.label' || _lang || ') like ''' || lower(_pattern) || '%'' OR lower(YR.label) = ''' || _pattern || ''')';
		END IF;
		_query = _query || ' GROUP BY RS.id, YR.id, YR.label, SP.label' || _lang || ', CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', SE2.label' || _lang || ', ranks, results, places, dates, TP1.number, TP2.number, TP3.number, CP.index, EV.index, SE.index';
		_query = _query || ' ORDER BY YR.id DESC, RS.first_update DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _count;

		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label_rel2, _item.label_rel3, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.txt3, _item.txt4, _item.txt1, _item.txt2, _item.id_rel1, _item.id_rel2, _item.id_rel3, _item.label, _item.txt6, _item.count1, _item.count2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'RS';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- Athletes
	IF (_entity = 'PR' OR _entity = '') THEN
		_query = 'SELECT PR.id, PR.last_name, PR.first_name, CN.code, TM.label, SP.label' || _lang || ', string_agg(CAST (EL.id AS VARCHAR), '',''), PR2.last_name || '', '' || PR2.first_name || '', '' || TM2.label, PR.ref';
		_query = _query || ' FROM "Athlete" PR';
		_query = _query || ' LEFT JOIN "Country" CN ON PR.id_country = CN.id';
		_query = _query || ' LEFT JOIN "Team" TM ON PR.id_team = TM.id';
		_query = _query || ' LEFT JOIN "Sport" SP ON PR.id_sport = SP.id';
		_query = _query || ' LEFT JOIN "Athlete" PR2 ON PR.link = PR2.id';
		_query = _query || ' LEFT JOIN "Team" TM2 ON PR2.id_team = TM2.id';
		_query = _query || ' LEFT JOIN "~ExternalLink" EL ON (EL.id_item = PR.id AND EL.entity=''PR'')';
		_query = _query || ' WHERE 0=1';
		IF _id_sport > 0 THEN
			_query = _query || ' OR PR.id_sport = ' || _id_sport;
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		IF (_id1 > 0 AND _id2 > 0) THEN
			_query = _query || ' AND PR.id BETWEEN ' || _id1 || ' AND ' || _id2;
		ELSIF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' AND lower(PR.last_name) like ''' || lower(_pattern) || '%''';
		END IF;
		_query = _query || ' GROUP BY PR.id, PR.last_name, PR.first_name, CN.code, TM.label, SP.label' || _lang || ', PR2.last_name || '', '' || PR2.first_name || '', '' || TM2.label, PR.ref';
		_query = _query || ' ORDER BY PR.last_name, PR.first_name LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label_rel2, _item.label_rel3, _item.label_rel4, _item.label_rel5, _item.label, _item.label_en, _item.count2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'PR';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- Teams
	IF (_entity = 'TM' OR _entity = '') THEN
		_query = 'SELECT TM.id, TM.label, SP.label' || _lang || ', CN.code, LG.label, string_agg(CAST (EL.id AS VARCHAR), '',''), TM2.label, TM.ref, (CASE WHEN TM.no_pic=true THEN 1 ELSE 0 END)';
		_query = _query || ' FROM "Team" TM';
		_query = _query || ' LEFT JOIN "Country" CN ON TM.id_country = CN.id';
		_query = _query || ' LEFT JOIN "Sport" SP ON TM.id_sport = SP.id';
		_query = _query || ' LEFT JOIN "League" LG ON TM.id_league = LG.id';
		_query = _query || ' LEFT JOIN "Team" TM2 ON TM.link = TM2.id';
		_query = _query || ' LEFT JOIN "~ExternalLink" EL ON (EL.id_item = TM.id AND EL.entity=''TM'')';
		_query = _query || ' WHERE 0=1';
		IF _id_sport > 0 THEN
			_query = _query || ' OR SP.id = ' || _id_sport;
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		IF (_id1 > 0 AND _id2 > 0) THEN
			_query = _query || ' AND TM.id BETWEEN ' || _id1 || ' AND ' || _id2;
		ELSIF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' AND lower(TM.label) like ''' || lower(_pattern) || '%''';
		END IF;
		_query = _query || ' GROUP BY TM.id, TM.label, CN.code, SP.label' || _lang || ', LG.label, TM2.label, TM.ref';
		_query = _query || ' ORDER BY TM.label LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label_rel2, _item.label_rel3, _item.label_rel4, _item.label, _item.label_en, _item.count2, _item.count3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'TM';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- Sports
	IF (_entity = 'SP' OR _entity = '') THEN
		_query = 'SELECT SP.id, SP.label' || _lang || ', string_agg(CAST (EL.id AS VARCHAR), '',''), SP.ref, (CASE WHEN SP.no_pic=true THEN 1 ELSE 0 END)';
		_query = _query || ' FROM "Sport" SP';
		_query = _query || ' LEFT JOIN "~ExternalLink" EL ON (EL.id_item = SP.id AND EL.entity=''SP'')';
		_query = _query || ' WHERE 0=1';
		IF _id_sport > 0 THEN
			_query = _query || ' OR SP.id = ' || _id_sport;
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		IF (_id1 > 0 AND _id2 > 0) THEN
			_query = _query || ' AND SP.id BETWEEN ' || _id1 || ' AND ' || _id2;
		ELSIF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' AND lower(SP.label' || _lang || ') like ''' || lower(_pattern) || '%''';
		END IF;
		_query = _query || ' GROUP BY SP.id, SP.label' || _lang || ', SP.ref';
		_query = _query || ' ORDER BY SP.label' || _lang || ' LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label, _item.count2, _item.count3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'SP';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- Championships
	IF (_entity = 'CP' OR _entity = '') THEN
		_query = 'SELECT CP.id, CP.label' || _lang || ', string_agg(CAST (EL.id AS VARCHAR), '',''), CP.ref, (CASE WHEN CP.no_pic=true THEN 1 ELSE 0 END)';
		_query = _query || ' FROM "Championship" CP';
		_query = _query || ' LEFT JOIN "~ExternalLink" EL ON (EL.id_item = CP.id AND EL.entity=''CP'')';
		_query = _query || ' WHERE 0=1';
		IF _id_sport > 0 THEN
			_query = _query || ' OR CP.id IN (SELECT id_championship FROM "Result" WHERE id_sport=' || _id_sport || ')';
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		IF (_id1 > 0 AND _id2 > 0) THEN
			_query = _query || ' AND CP.id BETWEEN ' || _id1 || ' AND ' || _id2;
		ELSIF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' AND lower(CP.label' || _lang || ') like ''' || lower(_pattern) || '%''';
		END IF;
		_query = _query || ' GROUP BY CP.id, CP.label' || _lang || ', CP.ref';
		_query = _query || ' ORDER BY CP.label' || _lang || ' LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label, _item.count2, _item.count3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CP';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- Events
	IF (_entity = 'EV' OR _entity = '') THEN
		_query = 'SELECT EV.id, EV.label' || _lang || ', string_agg(CAST (EL.id AS VARCHAR), '',''), EV.ref, (CASE WHEN EV.no_pic=true THEN 1 ELSE 0 END)';
		_query = _query || ' FROM "Event" EV';
		_query = _query || ' LEFT JOIN "~ExternalLink" EL ON (EL.id_item = EV.id AND EL.entity=''EV'')';
		_query = _query || ' WHERE 0=1';
		IF _id_sport > 0 THEN
			_query = _query || ' OR EV.id IN (SELECT id_event FROM "Result" WHERE id_sport=' || _id_sport || ' UNION SELECT id_subevent FROM "Result" WHERE id_sport=' || _id_sport || ' UNION SELECT id_subevent2 FROM "Result" WHERE id_sport=' || _id_sport || ')';
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		IF (_id1 > 0 AND _id2 > 0) THEN
			_query = _query || ' AND EV.id BETWEEN ' || _id1 || ' AND ' || _id2;
		ELSIF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' AND lower(EV.label' || _lang || ') like ''' || lower(_pattern) || '%''';
		END IF;
		_query = _query || ' GROUP BY EV.id, EV.label' || _lang || ', EV.ref';
		_query = _query || ' ORDER BY EV.label' || _lang || ' LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label, _item.count2, _item.count3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'EV';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- Cities
	IF (_entity = 'CT' OR _entity = '') THEN
		_query = 'SELECT CT.id, CT.label' || _lang || ', CN.code, string_agg(CAST (EL.id AS VARCHAR), '',''), CT2.label || '', '' || CN2.code, CT.ref';
		_query = _query || ' FROM "City" CT';
		_query = _query || ' LEFT JOIN "Country" CN ON CT.id_country = CN.id';
		_query = _query || ' LEFT JOIN "City" CT2 ON CT.link = CT2.id';
		_query = _query || ' LEFT JOIN "Country" CN2 ON CT2.id_country = CN2.id';
		_query = _query || ' LEFT JOIN "~ExternalLink" EL ON (EL.id_item = CT.id AND EL.entity=''CT'')';
		_query = _query || ' WHERE 0=1';
		IF _id_sport > 0 THEN
			_query = _query || ' OR CT.id IN (SELECT RS.id_city1 FROM "Result" RS WHERE id_sport=' || _id_sport || ' UNION SELECT RS.id_city2 FROM "Result" RS WHERE id_sport=' || _id_sport || ')';
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		IF (_id1 > 0 AND _id2 > 0) THEN
			_query = _query || ' AND CT.id BETWEEN ' || _id1 || ' AND ' || _id2;
		ELSIF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' AND lower(CT.label' || _lang || ') like ''' || lower(_pattern) || '%''';
		END IF;
		_query = _query || ' GROUP BY CT.id, CT.label' || _lang || ', CN.code, CT2.label || '', '' || CN2.code, CT.ref';
		_query = _query || ' ORDER BY CT.label' || _lang || ' LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label_rel2, _item.label, _item.label_en, _item.count2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CT';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- Complexes
	IF (_entity = 'CX' OR _entity = '') THEN
		_query = 'SELECT CX.id, CX.label, CT.label' || _lang || ', CN.code, string_agg(CAST (EL.id AS VARCHAR), '',''), CX2.label, CX.ref';
		_query = _query || ' FROM "Complex" CX';
		_query = _query || ' LEFT JOIN "City" CT ON CX.id_city = CT.id';
		_query = _query || ' LEFT JOIN "Country" CN ON CT.id_country = CN.id';
		_query = _query || ' LEFT JOIN "Complex" CX2 ON CX.link = CX2.id';
		_query = _query || ' LEFT JOIN "~ExternalLink" EL ON (EL.id_item = CX.id AND EL.entity=''CX'')';
		_query = _query || ' WHERE 0=1';
		IF _id_sport > 0 THEN
			_query = _query || ' OR CX.id IN (SELECT RS.id_complex1 FROM "Result" RS WHERE id_sport=' || _id_sport || ' UNION SELECT RS.id_complex2 FROM "Result" RS WHERE id_sport=' || _id_sport || ')';
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		IF (_id1 > 0 AND _id2 > 0) THEN
			_query = _query || ' AND CX.id BETWEEN ' || _id1 || ' AND ' || _id2;
		ELSIF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' AND lower(CX.label) like ''' || lower(_pattern) || '%''';
		END IF;
		_query = _query || ' GROUP BY CX.id, CX.label, CT.label' || _lang || ', CN.code, CX2.label, CX.ref';
		_query = _query || ' ORDER BY CX.label LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label_rel2, _item.label_rel3, _item.label, _item.label_en, _item.count2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CX';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	RETURN;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;

  
  
  
  
  CREATE TABLE "~Import"
(
  id integer  NOT NULL primary key,
  date timestamp,
  csv_content text
);
CREATE SEQUENCE "~SeqImport";





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
  
  
  
  
  CREATE OR REPLACE FUNCTION "TreeResults"(
    _filter character varying,
    _lang character varying)
  RETURNS SETOF "~TreeItem" AS
$BODY$
declare
	_item "~TreeItem"%rowtype;
	_c refcursor;
	_sp_id integer;
	_sp_label varchar(25);
	_sp_label_en varchar(25);
	_cp_id integer;
	_cp_label varchar(50);
	_cp_label_en varchar(50);
	_ev_id integer;
	_ev_label varchar(50);
	_ev_label_en varchar(50);
	_se_id integer;
	_se_label varchar(50);
	_se_label_en varchar(50);
	_se2_id integer;
	_se2_label varchar(50);
	_se2_label_en varchar(50);
	_index smallint;
	_current_sp_id integer;
	_current_cp_id integer;
	_current_ev_id integer;
	_current_se_id integer;
	_current_se2_id integer;
	_ii_championship integer;
	_ii_event integer;
	_ii_subevent integer;
	_ii_subevent2 integer;
begin
	_index := 1;
	_current_sp_id := 0;
	_current_cp_id := 0;
	_current_ev_id := 0;
	_current_se_id := 0;
	OPEN _c FOR EXECUTE
	'SELECT DISTINCT SP.id, SP.label' || _lang || ', SP.label, CP.id, CP.label' || _lang || ', CP.label, EV.id, EV.label' || _lang || ' COLLATE "fr_FR", EV.label, SE.id, SE.label' || _lang || ' COLLATE "fr_FR", SE.label, SE2.id, SE2.label' || _lang || ' COLLATE "fr_FR", SE2.label, II.id_championship, II.id_event, II.id_subevent, II.id_subevent2, CP.index, EV.index, SE.index, SE2.index, (CASE WHEN II.id_event IS NOT NULL AND II.id_subevent IS NULL AND II.id_subevent2 IS NULL THEN 1 ELSE 0 END) AS o_ii_ev, (CASE WHEN II.id_subevent IS NOT NULL AND II.id_subevent2 IS NULL THEN 1 ELSE 0 END) AS o_ii_se, (CASE WHEN II.id_subevent2 IS NOT NULL THEN 1 ELSE 0 END) AS o_ii_se2
	    FROM "Result" RS LEFT JOIN "Sport" SP ON RS.id_sport = SP.id
	    LEFT JOIN "Championship" CP ON RS.id_championship = CP.id
	    LEFT JOIN "Event" EV ON RS.id_event = EV.id
	    LEFT JOIN "Event" SE ON RS.id_subevent = SE.id
	    LEFT JOIN "Event" SE2 ON RS.id_subevent2 = SE2.id
	    LEFT JOIN "Olympics" OL ON OL.id_year = RS.id_year
	    LEFT JOIN "~InactiveItem" II ON (RS.id_sport = II.id_sport AND RS.id_championship = II.id_championship AND RS.id_event = II.id_event AND (RS.id_subevent = II.id_subevent OR RS.id_subevent IS NULL) AND (RS.id_subevent2 = II.id_subevent2 OR RS.id_subevent2 IS NULL))
	    ' || _filter || ' ORDER BY SP.label' || _lang || ', CP.index, CP.label' || _lang || ', o_ii_ev, EV.index, EV.label' || _lang || ' COLLATE "fr_FR", o_ii_se, SE.index, SE.label' || _lang || ' COLLATE "fr_FR", o_ii_se2, SE2.index, SE2.label' || _lang || ' COLLATE "fr_FR"';
	LOOP
		FETCH _c INTO _sp_id, _sp_label, _sp_label_en, _cp_id, _cp_label, _cp_label_en, _ev_id, _ev_label, _ev_label_en, _se_id, _se_label, _se_label_en, _se2_id, _se2_label, _se2_label_en, _ii_championship, _ii_event, _ii_subevent, _ii_subevent2;
		EXIT WHEN NOT FOUND;
		
		IF _sp_id <> _current_sp_id THEN
			_item.id = _index;
			_item.id_item = _sp_id;
			_item.label = _sp_label;
			_item.label_en = _sp_label_en;
			_item.level = 1;
			RETURN NEXT _item;
			_current_sp_id := _sp_id;
			_current_cp_id := 0;
			_index := _index + 1;
		END IF;
		IF _cp_id <> _current_cp_id THEN
			_item.id = _index;
			_item.id_item = _cp_id;
			_item.label = _cp_label;
			_item.label_en = _cp_label_en;
			IF _ii_championship IS NOT NULL AND _ii_event IS NULL AND _ii_subevent IS NULL AND _ii_subevent2 IS NULL THEN
				_item.label = '++' || _item.label;
			END IF;
			_item.level = 2;
			RETURN NEXT _item;
			_current_cp_id := _cp_id;
			_current_ev_id := 0;
			_index := _index + 1;
		END IF;
		IF _ev_id <> _current_ev_id AND _ev_label <> '//UNIQUE//' THEN
			_item.id = _index;
			_item.id_item = _ev_id;
			_item.label = _ev_label;
			_item.label_en = _ev_label_en;
			IF _ii_event IS NOT NULL AND _ii_subevent IS NULL AND _ii_subevent2 IS NULL THEN
				_item.label = '++' || _item.label;
			END IF;
			_item.level = 3;
			RETURN NEXT _item;
			_current_ev_id := _ev_id;
			_current_se_id := 0;
			_index := _index + 1;
		END IF;
		IF _se_id <> _current_se_id THEN
			_item.id = _index;
			_item.id_item = _se_id;
			_item.label = _se_label;
			_item.label_en = _se_label_en;
			IF _ii_subevent IS NOT NULL AND _ii_subevent2 IS NULL THEN
				_item.label = '++' || _item.label;
			END IF;
			_item.level = 4;
			RETURN NEXT _item;
			_current_se_id := _se_id;
			_current_se2_id := 0;
			_index := _index + 1;
		END IF;
		IF _se2_id <> _current_se2_id THEN
			_item.id = _index;
			_item.id_item = _se2_id;
			_item.label = _se2_label;
			_item.label_en = _se2_label_en;
			IF _ii_subevent2 IS NOT NULL THEN
				_item.label = '++' || _item.label;
			END IF;
			_item.level = 5;
			RETURN NEXT _item;
			_current_se2_id := _se2_id;
			_index := _index + 1;
		END IF;
	END LOOP;
	CLOSE _c;
	
	RETURN;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
  
  
  
  
  
  