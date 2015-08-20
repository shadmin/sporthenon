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
  
  
  
  