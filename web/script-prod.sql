ALTER TABLE "Result" DISABLE TRIGGER "TriggerRS";

update "Result" set date2=substring(date2, 4, 2) || '/' || substring(date2, 1, 2) || '/' || substring(date2, 7, 4)
where id_sport=50 and id_championship=1;

CREATE OR REPLACE FUNCTION "~Overview"(
    _entity character varying,
    _id_sport integer,
    _count integer,
    _pattern character varying,
    _lang character varying)
  RETURNS SETOF "~RefItem" AS
$BODY$
declare
	_item "~RefItem"%rowtype;
	_c refcursor;
	_index integer;
	_query text;
begin
	--RAISE NOTICE '%', '--'||_query;
	_index := 1;
	-- Results
	IF (_entity = 'RS' OR _entity = '') THEN
		_query = 'SELECT RS.id, YR.label, SP.label' || _lang || ', CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', SE2.label' || _lang || ',';
		_query = _query || 'concat_ws('','', RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, RS.id_rank11, RS.id_rank12, RS.id_rank13, RS.id_rank14, RS.id_rank15, RS.id_rank16, RS.id_rank17, RS.id_rank18, RS.id_rank19, RS.id_rank20) AS ranks,';
		_query = _query || 'concat_ws('','', RS.result1, RS.result2, RS.result3, RS.result4, RS.result5, RS.result6, RS.result7, RS.result8, RS.result9, RS.result10, RS.result11, RS.result12, RS.result13, RS.result14, RS.result15, RS.result16, RS.result17, RS.result18, RS.result19, RS.result20) AS results,';
		_query = _query || 'concat_ws('','', coalesce(id_complex1, ''0''), coalesce(id_complex2, ''0''), coalesce(id_city1, ''0''), coalesce(id_city2, ''0'')) AS places, concat_ws('','', coalesce(date1, ''0''), coalesce(date2, ''0'')) AS dates, TP1.number, TP2.number, TP3.number, string_agg(CAST (EL.id AS VARCHAR), '',''), string_agg(CAST (RD.id AS VARCHAR), '','')';	
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
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' AND (lower(SP.label' || _lang || ') like ''' || lower(_pattern) || '%'' OR lower(EV.label' || _lang || ') like ''' || lower(_pattern) || '%'' OR lower(EV.label' || _lang || ') like ''' || lower(_pattern) || '%'' OR lower(YR.label) = ''' || _pattern || ''')';
		END IF;
		_query = _query || ' GROUP BY RS.id, YR.id, YR.label, SP.label' || _lang || ', CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', SE2.label' || _lang || ', ranks, results, places, dates, TP1.number, TP2.number, TP3.number, CP.index, EV.index, SE.index';
		_query = _query || ' ORDER BY YR.id DESC, RS.first_update DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _count;

		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label_rel2, _item.label_rel3, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.txt3, _item.txt4, _item.txt1, _item.txt2, _item.id_rel1, _item.id_rel2, _item.id_rel3, _item.label, _item.label_en;
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
		_query = 'SELECT PR.id, PR.last_name, PR.first_name, CN.code, TM.label, SP.label' || _lang || ', string_agg(CAST (EL.id AS VARCHAR), '',''), PR.ref';
		_query = _query || ' FROM "Athlete" PR';
		_query = _query || ' LEFT JOIN "Country" CN ON PR.id_country = CN.id';
		_query = _query || ' LEFT JOIN "Team" TM ON PR.id_team = TM.id';
		_query = _query || ' LEFT JOIN "Sport" SP ON PR.id_sport = SP.id';
		_query = _query || ' LEFT JOIN "~ExternalLink" EL ON (EL.id_item = PR.id AND EL.entity=''PR'')';
		_query = _query || ' WHERE 0=1';
		IF _id_sport > 0 THEN
			_query = _query || ' OR PR.id_sport = ' || _id_sport;
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' AND lower(PR.last_name) like ''' || lower(_pattern) || '%''';
		END IF;	
		_query = _query || ' GROUP BY PR.id, PR.last_name, PR.first_name, CN.code, TM.label, SP.label' || _lang || ', PR.ref';
		_query = _query || ' ORDER BY PR.last_name, PR.first_name LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label_rel2, _item.label_rel3, _item.label_rel4, _item.label_rel5, _item.label, _item.count2;
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
		_query = 'SELECT TM.id, TM.label, SP.label' || _lang || ', CN.code, LG.label, string_agg(CAST (EL.id AS VARCHAR), '',''), TM.ref';
		_query = _query || ' FROM "Team" TM';
		_query = _query || ' LEFT JOIN "Country" CN ON TM.id_country = CN.id';
		_query = _query || ' LEFT JOIN "Sport" SP ON TM.id_sport = SP.id';
		_query = _query || ' LEFT JOIN "League" LG ON TM.id_league = LG.id';
		_query = _query || ' LEFT JOIN "~ExternalLink" EL ON (EL.id_item = TM.id AND EL.entity=''TM'')';
		_query = _query || ' WHERE 0=1';
		IF _id_sport > 0 THEN
			_query = _query || ' OR SP.id = ' || _id_sport;
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' AND lower(TM.label) like ''' || lower(_pattern) || '%''';
		END IF;
		_query = _query || ' GROUP BY TM.id, TM.label, CN.code, SP.label' || _lang || ', LG.label, TM.ref';
		_query = _query || ' ORDER BY TM.label LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label_rel2, _item.label_rel3, _item.label_rel4, _item.label, _item.count2;
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
		_query = 'SELECT SP.id, SP.label' || _lang || ', string_agg(CAST (EL.id AS VARCHAR), '',''), SP.ref';
		_query = _query || ' FROM "Sport" SP';
		_query = _query || ' LEFT JOIN "~ExternalLink" EL ON (EL.id_item = SP.id AND EL.entity=''SP'')';
		_query = _query || ' WHERE 0=1';
		IF _id_sport > 0 THEN
			_query = _query || ' OR SP.id = ' || _id_sport;
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' AND lower(SP.label' || _lang || ') like ''' || lower(_pattern) || '%''';
		END IF;	
		_query = _query || ' GROUP BY SP.id, SP.label' || _lang || ', SP.ref';
		_query = _query || ' ORDER BY SP.label' || _lang || ' LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label, _item.count2;
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
		_query = 'SELECT CP.id, CP.label' || _lang || ', string_agg(CAST (EL.id AS VARCHAR), '',''), CP.ref';
		_query = _query || ' FROM "Championship" CP';
		_query = _query || ' LEFT JOIN "~ExternalLink" EL ON (EL.id_item = CP.id AND EL.entity=''CP'')';
		_query = _query || ' WHERE 0=1';
		IF _id_sport > 0 THEN
			_query = _query || ' OR CP.id IN (SELECT id_championship FROM "Result" WHERE id_sport=' || _id_sport || ')';
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' AND lower(CP.label' || _lang || ') like ''' || lower(_pattern) || '%''';
		END IF;
		_query = _query || ' GROUP BY CP.id, CP.label' || _lang || ', CP.ref';
		_query = _query || ' ORDER BY CP.label' || _lang || ' LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label, _item.count2;
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
		_query = 'SELECT EV.id, EV.label' || _lang || ', string_agg(CAST (EL.id AS VARCHAR), '',''), EV.ref';
		_query = _query || ' FROM "Event" EV';
		_query = _query || ' LEFT JOIN "~ExternalLink" EL ON (EL.id_item = EV.id AND EL.entity=''EV'')';
		_query = _query || ' WHERE 0=1';
		IF _id_sport > 0 THEN
			_query = _query || ' OR EV.id IN (SELECT id_event FROM "Result" WHERE id_sport=' || _id_sport || ' UNION SELECT id_subevent FROM "Result" WHERE id_sport=' || _id_sport || ' UNION SELECT id_subevent2 FROM "Result" WHERE id_sport=' || _id_sport || ')';
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' AND lower(EV.label' || _lang || ') like ''' || lower(_pattern) || '%''';
		END IF;	
		_query = _query || ' GROUP BY EV.id, EV.label' || _lang || ', EV.ref';
		_query = _query || ' ORDER BY EV.label' || _lang || ' LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label, _item.count2;
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
		_query = 'SELECT CT.id, CT.label' || _lang || ', CN.code, string_agg(CAST (EL.id AS VARCHAR), '',''), CT.ref';
		_query = _query || ' FROM "City" CT';
		_query = _query || ' LEFT JOIN "Country" CN ON CT.id_country = CN.id';
		_query = _query || ' LEFT JOIN "~ExternalLink" EL ON (EL.id_item = CT.id AND EL.entity=''CT'')';
		_query = _query || ' WHERE 0=1';
		IF _id_sport > 0 THEN
			_query = _query || ' OR CT.id IN (SELECT id_city1 FROM "Result" WHERE id_sport=' || _id_sport || ' UNION SELECT id_city2 FROM "Result" WHERE id_sport=' || _id_sport || ')';
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' AND lower(CT.label' || _lang || ') like ''' || lower(_pattern) || '%''';
		END IF;
		_query = _query || ' GROUP BY CT.id, CT.label' || _lang || ', CN.code, CT.ref';
		_query = _query || ' ORDER BY CT.label' || _lang || ' LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label_rel2, _item.label, _item.count2;
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
		_query = 'SELECT CX.id, CX.label' || _lang || ', CT.label' || _lang || ', CN.code, string_agg(CAST (EL.id AS VARCHAR), '',''), CX.ref';
		_query = _query || ' FROM "Complex" CX';
		_query = _query || ' LEFT JOIN "City" CT ON CX.id_city = CT.id';
		_query = _query || ' LEFT JOIN "Country" CN ON CT.id_country = CN.id';
		_query = _query || ' LEFT JOIN "~ExternalLink" EL ON (EL.id_item = CX.id AND EL.entity=''CX'')';
		_query = _query || ' WHERE 0=1';
		IF _id_sport > 0 THEN
			_query = _query || ' OR CX.id IN (SELECT id_complex1 FROM "Result" WHERE id_sport=' || _id_sport || ' UNION SELECT id_complex2 FROM "Result" WHERE id_sport=' || _id_sport || ')';
		ELSE
			_query = _query || ' OR 1=1';
		END IF;
		IF (_pattern IS NOT NULL AND _pattern <> '') THEN
			_query = _query || ' AND lower(CX.label' || _lang || ') like ''' || lower(_pattern) || '%''';
		END IF;
		_query = _query || ' GROUP BY CX.id, CX.label' || _lang || ', CT.label' || _lang || ', CN.code, CX.ref';
		_query = _query || ' ORDER BY CX.label' || _lang || ' LIMIT ' || _count;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label_rel1, _item.label_rel2, _item.label_rel3, _item.label, _item.count2;
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
  
  CREATE OR REPLACE FUNCTION "Search"(
    _pattern character varying,
    _scope character varying,
    _limit smallint,
    _lang character varying)
  RETURNS SETOF "~RefItem" AS
$BODY$
declare
	_item "~RefItem"%rowtype;
	_index smallint;
	_current_id integer;
	_current_label varchar(100);
	_current_label_en varchar(100);
	_current_id_rel1 integer;
	_current_id_rel2 integer;
	_current_id_rel3 integer;
	_current_label_rel1 varchar(50);
	_current_label_rel2 varchar(50);
	_current_label_rel3 varchar(50);
	_current_label_rel4 varchar(50);
	_current_label_rel5 varchar(50);
	_current_label_rel6 varchar(50);
	_current_link integer;
	_current_ref smallint;
	_scopes varchar(2)[];
	_tables varchar(15)[];
	_label varchar(10);
	_label_en varchar(10);
	_i smallint;
	_s varchar(2);
	_c refcursor;
	_query text;
	_rel_cols text;
	_rel_joins text;
	_rel_count smallint;
	__pattern text;
begin
	INSERT INTO "~Request" VALUES (NEXTVAL('"~SeqRequest"'), 'SC', _pattern, current_date);
	
	_i := 1;
	_index := 1;
	__pattern := "~PatternString"(_pattern);
	_scopes := '{PR,CT,CX,CN,CP,EV,OL,SP,TM,ST,YR}';
	_tables := '{Athlete,City,Complex,Country,Championship,Event,Olympics,Sport,Team,State,Year}';
	FOR _s IN SELECT UNNEST(_scopes) LOOP
		IF _scope ~ ('(^|,)' || _s || '($|,)') OR _scope = '.' THEN
			_rel_cols := '';
			_rel_joins := '';
			_rel_count := 0;

			-- Get related fields
			IF (_s ~ 'PR|TM') THEN -- Relation: Country
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang || ' || '' ('' || CN.code || '')'', CN.code';
				_rel_joins := _rel_joins || ' LEFT JOIN "Country" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s ~ 'PR|TM') THEN -- Relation: Sport
				_rel_cols := _rel_cols || ', SP.id, SP.label' || _lang || ', SP.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "Sport" SP ON ' || _s || '.id_sport = SP.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'PR') THEN -- Relation: Team
				_rel_cols := _rel_cols || ', TM.id, TM.label, TM.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "Team" TM ON ' || _s || '.id_team = TM.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'CX') THEN -- Relation: City/State/Country
				_rel_cols := _rel_cols || ', CT.id, CT.label' || _lang || ', CT.label';
				_rel_cols := _rel_cols || ', ST.id, ST.label' || _lang || ', ST.label';
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang || ', CN.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "City" CT ON ' || _s || '.id_city = CT.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "State" ST ON CT.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "Country" CN ON CT.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			IF (_s = 'CT') THEN -- Relation: State/Country
				_rel_cols := _rel_cols || ', NULL, NULL, NULL';
				_rel_cols := _rel_cols || ', ST.id, ST.label' || _lang || ', ST.label';
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang || ', CN.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "State" ST ON ' || _s || '.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "Country" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			IF (_s = 'OL') THEN -- Relation: City/Year
				_rel_cols := _rel_cols || ', NULL, NULL, NULL';
				_rel_cols := _rel_cols || ', CT.id, CT.label' || _lang || ', CT.label';
				_rel_cols := _rel_cols || ', YR.id, YR.label, YR.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "City" CT ON ' || _s || '.id_city = CT.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "Year" YR ON ' || _s || '.id_year = YR.id';
				_rel_count := _rel_count + 3;
			END IF;
			FOR _j IN (_rel_count + 1)..3 LOOP
				_rel_cols := _rel_cols || ', NULL, NULL, NULL';
			END LOOP;
			IF (_s ~ 'CT|CX|PR|TM') THEN
				_rel_cols := _rel_cols || ', ' || _s || '.link';
			END IF;
			
			-- Execute query
			_label := 'label';
			_label_en := 'label';
			IF (_s <> 'TM' AND _s <> 'YR' AND _s <> 'OL') THEN
				_label := 'label' || _lang;
			END IF;
			_query := 'SELECT ' || _s || '.id, ' || _s || '.' || _label || ',' || _s || '.' || _label_en || ',' || _s || '.ref' || _rel_cols || ' FROM "' || _tables[_i] || '" ' || _s;
			_query := _query || _rel_joins || ' WHERE ' || (CASE _s WHEN 'CT' THEN '(CT.link = 0 OR CT.link IS NULL) AND ' WHEN 'CX' THEN '(CX.link = 0 OR CX.link IS NULL) AND ' WHEN 'TM' THEN '(TM.link = 0 OR TM.link IS NULL OR (TM.year1 IS NOT NULL AND TM.year1 <> '''')) AND ' ELSE '' END) || 'lower(' || _s || '.' || _label || ') ~ ''' || __pattern || '''' || (CASE _limit WHEN 0 THEN ' ORDER BY ' || _s || '.' || _label ELSE '' END);
			IF _s = 'PR' THEN
				_query := 'SELECT PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, PR.ref' || _rel_cols || ' FROM "Athlete" PR' || _rel_joins;
				_query := _query || ' WHERE (PR.link = 0 OR PR.link IS NULL) AND (lower(PR.last_name || '' '' || PR.first_name) ~ ''' || __pattern || ''' OR lower(PR.first_name || '' '' || PR.last_name) ~ ''' || __pattern || ''' OR lower(PR.last_name) ~ ''' || __pattern || ''' OR lower(PR.first_name) ~ ''' || __pattern || ''')';
				_query := _query || (CASE _limit WHEN 0 THEN ' ORDER BY PR.last_name, PR.first_name' ELSE '' END);
			ELSIF (_s = 'OL') THEN
				_query := 'SELECT OL.id, CT.label' || _lang || ' || '' '' || YR.label, CT.label || '' '' || YR.label, OL.ref' || _rel_cols || ' FROM "Olympics" OL' || _rel_joins;
				_query := _query || ' WHERE YR.label ~ ''' || __pattern || ''' OR lower(CT.label' || _lang || ') ~ ''' || __pattern || '''';
			END IF;
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _current_id, _current_label, _current_label_en, _current_ref, _current_id_rel1, _current_label_rel1, _current_label_rel4, _current_id_rel2, _current_label_rel2, _current_label_rel5, _current_id_rel3, _current_label_rel3, _current_label_rel6, _current_link;
				EXIT WHEN NOT FOUND;
				_item.id = _index;
				_item.id_item = _current_id;
				_item.label = _current_label;
				IF _current_link IS NOT NULL THEN
					IF _s = 'CT' THEN
						SELECT SUM(ref) INTO _item.count_ref FROM "City" WHERE id=_current_id OR link=_current_id;
					ELSIF _s = 'CX' THEN
						SELECT SUM(ref) INTO _item.count_ref FROM "Complex" WHERE id=_current_id OR link=_current_id;
					ELSIF _s = 'PR' THEN
						SELECT SUM(ref) INTO _item.count_ref FROM "Athlete" WHERE id=_current_id OR link=_current_id;
					ELSIF _s = 'TM' THEN
						SELECT SUM(ref) INTO _item.count_ref FROM "Team" WHERE id=_current_id OR link=_current_id;
					END IF;
				END IF;
				_item.count_ref = (CASE WHEN _current_ref IS NOT NULL THEN _current_ref ELSE 0 END);
				_item.entity = _s;
				_item.label_rel1 = _current_label_rel1;
				_item.label_rel2 = _current_label_rel2;
				_item.label_rel3 = _current_label_rel3;
				IF _limit = 0 THEN
					_item.label_en = _current_label_en;
					_item.id_rel1 = _current_id_rel1;
					_item.id_rel2 = _current_id_rel2;
					_item.id_rel3 = _current_id_rel3;
					_item.label_rel4 = _current_label_rel4;
					_item.label_rel5 = _current_label_rel5;
					_item.label_rel6 = _current_label_rel6;
					_item.link = _current_link;
				END IF;
				RETURN NEXT _item;
				_index := _index + 1;
			END LOOP;			
			CLOSE _c;
		END IF;
		_i := _i + 1;
	END LOOP;
	RETURN;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
  
  
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
  
  
  
ALTER TABLE "Result" ENABLE TRIGGER "TriggerRS";