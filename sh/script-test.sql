Alter TABLE "~RefItem" alter column txt4 type varchar(500);


CREATE OR REPLACE FUNCTION "~Overview"(_entity character varying, _id_sport integer, _count integer, _pattern character varying, _lang character varying)
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
		_query = 'SELECT RS.id, YR.label, SP.label' || _lang || ', CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', SE2.label' || _lang || ', COALESCE(id_rank1 || ''|'', '''') || COALESCE(id_rank2 || ''|'', '''') || COALESCE(id_rank3 || ''|'', '''') || COALESCE(id_rank4 || ''|'', '''') || COALESCE(id_rank5 || ''|'', '''') || COALESCE(id_rank6 || ''|'', '''') || COALESCE(id_rank7 || ''|'', '''') || COALESCE(id_rank8 || ''|'', '''') || COALESCE(id_rank9 || ''|'', '''') || COALESCE(id_rank10 || ''|'', '''') || COALESCE(id_rank11 || ''|'', '''') || COALESCE(id_rank12 || ''|'', '''') || COALESCE(id_rank13 || ''|'', '''') || COALESCE(id_rank14 || ''|'', '''') || COALESCE(id_rank15 || ''|'', '''') || COALESCE(id_rank16 || ''|'', '''') || COALESCE(id_rank17 || ''|'', '''') || COALESCE(id_rank18 || ''|'', '''') || COALESCE(id_rank19 || ''|'', '''') || COALESCE(id_rank20 || ''|'', ''''),';
		_query = _query || ' COALESCE(result1 || ''|'', '''') || COALESCE(result2 || ''|'', '''') || COALESCE(result3 || ''|'', '''') || COALESCE(result4 || ''|'', '''') || COALESCE(result5 || ''|'', '''') || COALESCE(result6 || ''|'', '''') || COALESCE(result7 || ''|'', '''') || COALESCE(result8 || ''|'', '''') || COALESCE(result9 || ''|'', '''') || COALESCE(result10 || ''|'', '''') || COALESCE(result11 || ''|'', '''') || COALESCE(result12 || ''|'', '''') || COALESCE(result13 || ''|'', '''') || COALESCE(result14 || ''|'', '''') || COALESCE(result15 || ''|'', '''') || COALESCE(result16 || ''|'', '''') || COALESCE(result17 || ''|'', '''') || COALESCE(result18 || ''|'', '''') || COALESCE(result19 || ''|'', '''') || COALESCE(result20 || ''|'', ''''),';
		_query = _query || ' COALESCE(id_complex1, ''0'') || ''|'' || COALESCE(id_complex2, ''0'') || ''|'' || COALESCE(id_city1, ''0'') || ''|'' || COALESCE(id_city2, ''0''), COALESCE(date1, ''0'') || ''|'' || COALESCE(date2, ''0''), TP1.number, TP2.number, TP3.number, DR.id, (SELECT COUNT(*) AS el_count FROM "~ExternalLink" EL WHERE EL.entity=''RS'' AND EL.id=RS.id)';
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
		_query = _query || ' LEFT JOIN "Draw" DR ON RS.id = DR.id_result';
		_query = _query || ' WHERE 0=1';
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' OR lower(SP.label' || _lang || ') like ''' || lower(_pattern) || '%''';
		ELSE
			_query = _query || ' OR SP.id = ' || _id_sport;
		END IF;
		_query = _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label_rel2, _item.label_rel3, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.txt3, _item.txt4, _item.txt1, _item.txt2, _item.id_rel1, _item.id_rel2, _item.id_rel3, _item.id_rel4, _item.count1;
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
		_query = 'SELECT PR.id, PR.last_name, PR.first_name, CN.code, TM.label, (SELECT COUNT(*) AS el_count FROM "~ExternalLink" EL WHERE EL.entity=''PR'' AND EL.id=PR.id), PR.ref';
		_query = _query || ' FROM "Athlete" PR';
		_query = _query || ' LEFT JOIN "Country" CN ON PR.id_country = CN.id';
		_query = _query || ' LEFT JOIN "Team" TM ON PR.id_team = TM.id';
		_query = _query || ' WHERE 0=1';
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' OR lower(PR.last_name) like ''' || lower(_pattern) || '%''';
		ELSE
			_query = _query || ' OR PR.id_sport = ' || _id_sport;
		END IF;
		_query = _query || ' ORDER BY PR.last_name, PR.first_name LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label_rel2, _item.label_rel3, _item.label_rel4, _item.count1, _item.count2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'PR';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;
	
	RETURN;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
  
  
  
  CREATE OR REPLACE FUNCTION "~Overview"(_entity character varying, _id_sport integer, _count integer, _pattern character varying, _lang character varying)
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
		_query = 'SELECT RS.id, YR.label, SP.label' || _lang || ', CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', SE2.label' || _lang || ', COALESCE(id_rank1 || ''|'', '''') || COALESCE(id_rank2 || ''|'', '''') || COALESCE(id_rank3 || ''|'', '''') || COALESCE(id_rank4 || ''|'', '''') || COALESCE(id_rank5 || ''|'', '''') || COALESCE(id_rank6 || ''|'', '''') || COALESCE(id_rank7 || ''|'', '''') || COALESCE(id_rank8 || ''|'', '''') || COALESCE(id_rank9 || ''|'', '''') || COALESCE(id_rank10 || ''|'', '''') || COALESCE(id_rank11 || ''|'', '''') || COALESCE(id_rank12 || ''|'', '''') || COALESCE(id_rank13 || ''|'', '''') || COALESCE(id_rank14 || ''|'', '''') || COALESCE(id_rank15 || ''|'', '''') || COALESCE(id_rank16 || ''|'', '''') || COALESCE(id_rank17 || ''|'', '''') || COALESCE(id_rank18 || ''|'', '''') || COALESCE(id_rank19 || ''|'', '''') || COALESCE(id_rank20 || ''|'', ''''),';
		_query = _query || ' COALESCE(result1 || ''|'', '''') || COALESCE(result2 || ''|'', '''') || COALESCE(result3 || ''|'', '''') || COALESCE(result4 || ''|'', '''') || COALESCE(result5 || ''|'', '''') || COALESCE(result6 || ''|'', '''') || COALESCE(result7 || ''|'', '''') || COALESCE(result8 || ''|'', '''') || COALESCE(result9 || ''|'', '''') || COALESCE(result10 || ''|'', '''') || COALESCE(result11 || ''|'', '''') || COALESCE(result12 || ''|'', '''') || COALESCE(result13 || ''|'', '''') || COALESCE(result14 || ''|'', '''') || COALESCE(result15 || ''|'', '''') || COALESCE(result16 || ''|'', '''') || COALESCE(result17 || ''|'', '''') || COALESCE(result18 || ''|'', '''') || COALESCE(result19 || ''|'', '''') || COALESCE(result20 || ''|'', ''''),';
		_query = _query || ' COALESCE(id_complex1, ''0'') || ''|'' || COALESCE(id_complex2, ''0'') || ''|'' || COALESCE(id_city1, ''0'') || ''|'' || COALESCE(id_city2, ''0''), COALESCE(date1, ''0'') || ''|'' || COALESCE(date2, ''0''), TP1.number, TP2.number, TP3.number, DR.id, (SELECT COUNT(*) AS el_count FROM "~ExternalLink" EL WHERE EL.entity=''RS'' AND EL.id_item=RS.id)';
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
		_query = _query || ' LEFT JOIN "Draw" DR ON RS.id = DR.id_result';
		_query = _query || ' WHERE 0=1';
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' OR lower(SP.label' || _lang || ') like ''' || lower(_pattern) || '%''';
		ELSIF _id_sport > 0 THEN
			_query = _query || ' OR SP.id = ' || _id_sport;
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		_query = _query || ' ORDER BY YR.id DESC, RS.first_update DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label_rel2, _item.label_rel3, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.txt3, _item.txt4, _item.txt1, _item.txt2, _item.id_rel1, _item.id_rel2, _item.id_rel3, _item.id_rel4, _item.count1;
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
		_query = 'SELECT PR.id, PR.last_name, PR.first_name, CN.code, TM.label, SP.label' || _lang || ', (SELECT COUNT(*) AS el_count FROM "~ExternalLink" EL WHERE EL.entity=''PR'' AND EL.id_item=PR.id), PR.ref';
		_query = _query || ' FROM "Athlete" PR';
		_query = _query || ' LEFT JOIN "Country" CN ON PR.id_country = CN.id';
		_query = _query || ' LEFT JOIN "Team" TM ON PR.id_team = TM.id';
		_query = _query || ' LEFT JOIN "Sport" SP ON PR.id_sport = SP.id';
		_query = _query || ' WHERE 0=1';
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' OR lower(PR.last_name) like ''' || lower(_pattern) || '%''';
		ELSIF _id_sport > 0 THEN
			_query = _query || ' OR PR.id_sport = ' || _id_sport;
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		_query = _query || ' ORDER BY PR.last_name, PR.first_name LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label_rel2, _item.label_rel3, _item.label_rel4, _item.label_rel5, _item.count1, _item.count2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'PR';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- Sports
	IF (_entity = 'SP' OR _entity = '') THEN
		_query = 'SELECT SP.id, SP.label' || _lang || ', (SELECT COUNT(*) AS el_count FROM "~ExternalLink" EL WHERE EL.entity=''SP'' AND EL.id_item=SP.id), SP.ref';
		_query = _query || ' FROM "Sport" SP';
		_query = _query || ' WHERE 0=1';
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' OR lower(SP.label' || _lang || ') like ''' || lower(_pattern) || '%''';
		ELSIF _id_sport > 0 THEN
			_query = _query || ' OR SP.id = ' || _id_sport;
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		_query = _query || ' ORDER BY SP.label' || _lang || ' LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.count1, _item.count2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'SP';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- Teams
	IF (_entity = 'TM' OR _entity = '') THEN
		_query = 'SELECT TM.id, TM.label, CN.code, SP.label' || _lang || ', LG.label, (SELECT COUNT(*) AS el_count FROM "~ExternalLink" EL WHERE EL.entity=''TM'' AND EL.id_item=TM.id), TM.ref';
		_query = _query || ' FROM "Team" TM';
		_query = _query || ' LEFT JOIN "Country" CN ON TM.id_country = CN.id';
		_query = _query || ' LEFT JOIN "Sport" SP ON TM.id_sport = SP.id';
		_query = _query || ' LEFT JOIN "League" LG ON TM.id_league = LG.id';
		_query = _query || ' WHERE 0=1';
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' OR lower(TM.label) like ''' || lower(_pattern) || '%''';
		ELSIF _id_sport > 0 THEN
			_query = _query || ' OR SP.id = ' || _id_sport;
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		_query = _query || ' ORDER BY TM.label LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label_rel2, _item.label_rel3, _item.label_rel4, _item.count1, _item.count2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'TM';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;
	
	RETURN;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
  
  
  
  
  CREATE OR REPLACE FUNCTION "~Overview"(_entity character varying, _id_sport integer, _count integer, _pattern character varying, _lang character varying)
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
		_query = 'SELECT RS.id, YR.label, SP.label' || _lang || ', CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', SE2.label' || _lang || ', COALESCE(id_rank1 || ''|'', '''') || COALESCE(id_rank2 || ''|'', '''') || COALESCE(id_rank3 || ''|'', '''') || COALESCE(id_rank4 || ''|'', '''') || COALESCE(id_rank5 || ''|'', '''') || COALESCE(id_rank6 || ''|'', '''') || COALESCE(id_rank7 || ''|'', '''') || COALESCE(id_rank8 || ''|'', '''') || COALESCE(id_rank9 || ''|'', '''') || COALESCE(id_rank10 || ''|'', '''') || COALESCE(id_rank11 || ''|'', '''') || COALESCE(id_rank12 || ''|'', '''') || COALESCE(id_rank13 || ''|'', '''') || COALESCE(id_rank14 || ''|'', '''') || COALESCE(id_rank15 || ''|'', '''') || COALESCE(id_rank16 || ''|'', '''') || COALESCE(id_rank17 || ''|'', '''') || COALESCE(id_rank18 || ''|'', '''') || COALESCE(id_rank19 || ''|'', '''') || COALESCE(id_rank20 || ''|'', ''''),';
		_query = _query || ' COALESCE(result1 || ''|'', '''') || COALESCE(result2 || ''|'', '''') || COALESCE(result3 || ''|'', '''') || COALESCE(result4 || ''|'', '''') || COALESCE(result5 || ''|'', '''') || COALESCE(result6 || ''|'', '''') || COALESCE(result7 || ''|'', '''') || COALESCE(result8 || ''|'', '''') || COALESCE(result9 || ''|'', '''') || COALESCE(result10 || ''|'', '''') || COALESCE(result11 || ''|'', '''') || COALESCE(result12 || ''|'', '''') || COALESCE(result13 || ''|'', '''') || COALESCE(result14 || ''|'', '''') || COALESCE(result15 || ''|'', '''') || COALESCE(result16 || ''|'', '''') || COALESCE(result17 || ''|'', '''') || COALESCE(result18 || ''|'', '''') || COALESCE(result19 || ''|'', '''') || COALESCE(result20 || ''|'', ''''),';
		_query = _query || ' COALESCE(id_complex1, ''0'') || ''|'' || COALESCE(id_complex2, ''0'') || ''|'' || COALESCE(id_city1, ''0'') || ''|'' || COALESCE(id_city2, ''0''), COALESCE(date1, ''0'') || ''|'' || COALESCE(date2, ''0''), TP1.number, TP2.number, TP3.number, DR.id, (SELECT COUNT(*) AS el_count FROM "~ExternalLink" EL WHERE EL.entity=''RS'' AND EL.id_item=RS.id)';
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
		_query = _query || ' LEFT JOIN "Draw" DR ON RS.id = DR.id_result';
		_query = _query || ' WHERE 0=1';
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' OR lower(SP.label' || _lang || ') like ''' || lower(_pattern) || '%'' OR lower(EV.label' || _lang || ') like ''' || lower(_pattern) || '%'' OR lower(EV.label' || _lang || ') like ''' || lower(_pattern) || '%'' OR lower(YR.label) = ''' || _pattern || '''';
		ELSIF _id_sport > 0 THEN
			_query = _query || ' OR SP.id = ' || _id_sport;
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		_query = _query || ' ORDER BY YR.id DESC, RS.first_update DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label_rel2, _item.label_rel3, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.txt3, _item.txt4, _item.txt1, _item.txt2, _item.id_rel1, _item.id_rel2, _item.id_rel3, _item.id_rel4, _item.count1;
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
		_query = 'SELECT PR.id, PR.last_name, PR.first_name, CN.code, TM.label, SP.label' || _lang || ', (SELECT COUNT(*) AS el_count FROM "~ExternalLink" EL WHERE EL.entity=''PR'' AND EL.id_item=PR.id), PR.ref';
		_query = _query || ' FROM "Athlete" PR';
		_query = _query || ' LEFT JOIN "Country" CN ON PR.id_country = CN.id';
		_query = _query || ' LEFT JOIN "Team" TM ON PR.id_team = TM.id';
		_query = _query || ' LEFT JOIN "Sport" SP ON PR.id_sport = SP.id';
		_query = _query || ' WHERE 0=1';
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' OR lower(PR.last_name) like ''' || lower(_pattern) || '%''';
		ELSIF _id_sport > 0 THEN
			_query = _query || ' OR PR.id_sport = ' || _id_sport;
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		_query = _query || ' ORDER BY PR.last_name, PR.first_name LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label_rel2, _item.label_rel3, _item.label_rel4, _item.label_rel5, _item.count1, _item.count2;
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
		_query = 'SELECT TM.id, TM.label, CN.code, SP.label' || _lang || ', LG.label, (SELECT COUNT(*) AS el_count FROM "~ExternalLink" EL WHERE EL.entity=''TM'' AND EL.id_item=TM.id), TM.ref';
		_query = _query || ' FROM "Team" TM';
		_query = _query || ' LEFT JOIN "Country" CN ON TM.id_country = CN.id';
		_query = _query || ' LEFT JOIN "Sport" SP ON TM.id_sport = SP.id';
		_query = _query || ' LEFT JOIN "League" LG ON TM.id_league = LG.id';
		_query = _query || ' WHERE 0=1';
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' OR lower(TM.label) like ''' || lower(_pattern) || '%''';
		ELSIF _id_sport > 0 THEN
			_query = _query || ' OR SP.id = ' || _id_sport;
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		_query = _query || ' ORDER BY TM.label LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label_rel2, _item.label_rel3, _item.label_rel4, _item.count1, _item.count2;
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
		_query = 'SELECT SP.id, SP.label' || _lang || ', (SELECT COUNT(*) AS el_count FROM "~ExternalLink" EL WHERE EL.entity=''SP'' AND EL.id_item=SP.id), SP.ref';
		_query = _query || ' FROM "Sport" SP';
		_query = _query || ' WHERE 0=1';
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' OR lower(SP.label' || _lang || ') like ''' || lower(_pattern) || '%''';
		ELSIF _id_sport > 0 THEN
			_query = _query || ' OR SP.id = ' || _id_sport;
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		_query = _query || ' ORDER BY SP.label' || _lang || ' LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.count1, _item.count2;
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
		_query = 'SELECT CP.id, CP.label' || _lang || ', (SELECT COUNT(*) AS el_count FROM "~ExternalLink" EL WHERE EL.entity=''CP'' AND EL.id_item=CP.id), CP.ref';
		_query = _query || ' FROM "Championship" CP';
		_query = _query || ' WHERE 0=1';
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' OR lower(CP.label' || _lang || ') like ''' || lower(_pattern) || '%''';
		ELSIF _id_sport > 0 THEN
			_query = _query || ' OR CP.id IN (SELECT id_championship FROM "Result" WHERE id_sport=' || _id_sport || ')';
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		_query = _query || ' ORDER BY CP.label' || _lang || ' LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.count1, _item.count2;
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
		_query = 'SELECT EV.id, EV.label' || _lang || ', (SELECT COUNT(*) AS el_count FROM "~ExternalLink" EL WHERE EL.entity=''EV'' AND EL.id_item=EV.id), EV.ref';
		_query = _query || ' FROM "Event" EV';
		_query = _query || ' WHERE 0=1';
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' OR lower(EV.label' || _lang || ') like ''' || lower(_pattern) || '%''';
		ELSIF _id_sport > 0 THEN
			_query = _query || ' OR EV.id IN (SELECT id_event FROM "Result" WHERE id_sport=' || _id_sport || ' UNION SELECT id_subevent FROM "Result" WHERE id_sport=' || _id_sport || ' UNION SELECT id_subevent2 FROM "Result" WHERE id_sport=' || _id_sport || ')';
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		_query = _query || ' ORDER BY EV.label' || _lang || ' LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.count1, _item.count2;
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
		_query = 'SELECT CT.id, CT.label' || _lang || ', CN.code, (SELECT COUNT(*) AS el_count FROM "~ExternalLink" EL WHERE EL.entity=''CT'' AND EL.id_item=CT.id), CT.ref';
		_query = _query || ' FROM "City" CT';
		_query = _query || ' LEFT JOIN "Country" CN ON CT.id_country = CN.id';
		_query = _query || ' WHERE 0=1';
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' OR lower(CT.label' || _lang || ') like ''' || lower(_pattern) || '%''';
		ELSIF _id_sport > 0 THEN
			_query = _query || ' OR CT.id IN (SELECT id_city1 FROM "Result" WHERE id_sport=' || _id_sport || ' UNION SELECT id_city2 FROM "Result" WHERE id_sport=' || _id_sport || ')';
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		_query = _query || ' ORDER BY CT.label' || _lang || ' LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label_rel2, _item.count1, _item.count2;
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
		_query = 'SELECT CX.id, CX.label' || _lang || ', CT.label' || _lang || ', CN.code, (SELECT COUNT(*) AS el_count FROM "~ExternalLink" EL WHERE EL.entity=''CX'' AND EL.id_item=CX.id), CX.ref';
		_query = _query || ' FROM "Complex" CX';
		_query = _query || ' LEFT JOIN "City" CT ON CX.id_city = CT.id';
		_query = _query || ' LEFT JOIN "Country" CN ON CT.id_country = CN.id';
		_query = _query || ' WHERE 0=1';
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' OR lower(CX.label' || _lang || ') like ''' || lower(_pattern) || '%''';
		ELSIF _id_sport > 0 THEN
			_query = _query || ' OR CX.id IN (SELECT id_complex1 FROM "Result" WHERE id_sport=' || _id_sport || ' UNION SELECT id_complex2 FROM "Result" WHERE id_sport=' || _id_sport || ')';
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		_query = _query || ' ORDER BY CX.label' || _lang || ' LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label_rel2, _item.label_rel3, _item.count1, _item.count2;
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
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
  
  
  
  
  CREATE OR REPLACE FUNCTION "~LastUpdates"(_count integer, _offset integer, _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
	_c refcursor;
begin
	OPEN _c FOR EXECUTE
	'SELECT RS.id AS rs_id, YR.id AS yr_id, YR.label AS yr_label, SP.id AS sp_id, CP.id AS cp_id, EV.id AS ev_id, SE.id AS se_id, SE2.id AS se2_id, SP.label' || _lang || ' AS sp_label, CP.label' || _lang || ' AS cp_label, EV.label' || _lang || ' AS ev_label, SE.label' || _lang || ' AS se_label, SE2.label' || _lang || ' AS se2_label, SP.label AS sp_label_en, CP.label AS cp_label_en, EV.label AS ev_label_en, SE.label AS se_label_en, SE2.label AS se2_label_en, RS.first_update AS rs_update, TP1.number as tp1_number, TP2.number AS tp2_number, TP3.number AS tp3_number,
	  PR1.id AS pr1_id, PR1.first_name AS pr1_first_name, PR1.last_name AS pr1_last_name, PR1.id_country AS pr1_country, PRCN1.code AS pr1_country_code, TM1.id AS tm1_id, TM1.label AS tm1_label, CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label' || _lang || ' AS cn1_label, CN1.label AS cn1_label_en, 
	  PR2.id AS pr2_id, PR2.first_name AS pr2_first_name, PR2.last_name AS pr2_last_name, PR2.id_country AS pr2_country, PRCN2.code AS pr2_country_code, TM2.id AS tm2_id, TM2.label AS tm2_label, CN2.id AS cn2_id, CN2.code AS cn2_code, CN2.label' || _lang || ' AS cn2_label, CN2.label AS cn2_label_en,
	  PR3.id AS pr3_id, PR3.first_name AS pr3_first_name, PR3.last_name AS pr3_last_name, PR3.id_country AS pr3_country, PRCN3.code AS pr3_country_code, TM3.id AS tm3_id, TM3.label AS tm3_label, CN3.id AS cn3_id, CN3.code AS cn3_code, CN3.label' || _lang || ' AS cn3_label, CN3.label AS cn3_label_en,
	  PR4.id AS pr4_id, PR4.first_name AS pr4_first_name, PR4.last_name AS pr4_last_name, PR4.id_country AS pr4_country, PRCN4.code AS pr4_country_code, TM4.id AS tm4_id, TM4.label AS tm4_label, CN4.id AS cn4_id, CN4.code AS cn4_code, CN4.label' || _lang || ' AS cn4_label, CN4.label AS cn4_label_en,
	  RS.result1 AS rs_text1, RS.result2 AS rs_text2, RS.exa AS rs_text3, RS.comment AS rs_text4, RS.date2 AS rs_date FROM "Result" RS
		LEFT JOIN "Year" YR ON RS.id_year=YR.id
		LEFT JOIN "Sport" SP ON RS.id_sport=SP.id
		LEFT JOIN "Championship" CP ON RS.id_championship=CP.id
		LEFT JOIN "Event" EV ON RS.id_event=EV.id
		LEFT JOIN "Event" SE ON RS.id_subevent=SE.id
		LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id
		LEFT JOIN "Type" TP1 ON EV.id_type=TP1.id
		LEFT JOIN "Type" TP2 ON SE.id_type=TP2.id
		LEFT JOIN "Type" TP3 ON SE2.id_type=TP3.id
		LEFT JOIN "Athlete" PR1 ON RS.id_rank1=PR1.id
		LEFT JOIN "Athlete" PR2 ON RS.id_rank2=PR2.id
		LEFT JOIN "Athlete" PR3 ON RS.id_rank3=PR3.id
		LEFT JOIN "Athlete" PR4 ON RS.id_rank4=PR4.id
		LEFT JOIN "Country" PRCN1 ON PR1.id_country=PRCN1.id
		LEFT JOIN "Country" PRCN2 ON PR2.id_country=PRCN2.id
		LEFT JOIN "Country" PRCN3 ON PR3.id_country=PRCN3.id
		LEFT JOIN "Country" PRCN4 ON PR4.id_country=PRCN4.id
		LEFT JOIN "Team" TM1 ON RS.id_rank1=TM1.id
		LEFT JOIN "Team" TM2 ON RS.id_rank2=TM2.id
		LEFT JOIN "Team" TM3 ON RS.id_rank3=TM3.id
		LEFT JOIN "Team" TM4 ON RS.id_rank4=TM4.id
		LEFT JOIN "Country" CN1 ON RS.id_rank1=CN1.id
		LEFT JOIN "Country" CN2 ON RS.id_rank2=CN2.id
		LEFT JOIN "Country" CN3 ON RS.id_rank3=CN3.id
		LEFT JOIN "Country" CN4 ON RS.id_rank4=CN4.id
	ORDER BY YR.id DESC, RS.first_update DESC LIMIT ' || _count || ' OFFSET ' || _offset;
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  
  