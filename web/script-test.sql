 DROP FUNCTION "Search"(character varying, character varying, smallint, character varying);
 
 
 CREATE OR REPLACE FUNCTION "Search"(
    _pattern character varying,
    _scope character varying,
    _limit smallint,
    _word_match boolean,
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
	_comp char;
	__pattern text;
begin
	_i := 1;
	_index := 1;
	__pattern := "~PatternString"(_pattern);
	IF (_word_match = TRUE) THEN
		__pattern := __pattern || '(\s|$)';
	END IF;
	__pattern := __pattern || '.*';
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
				_rel_cols := _rel_cols || ', OL.type, OL.type, OL.type';
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
			IF (_s <> 'TM' AND _s <> 'YR' AND _s <> 'OL' AND _s <> 'CX') THEN
				_label := 'label' || _lang;
			END IF;
			_query := 'SELECT ' || _s || '.id, ' || _s || '.' || _label || ',' || _s || '.' || _label_en || ',' || _s || '.ref' || _rel_cols || ' FROM "' || _tables[_i] || '" ' || _s;
			_query := _query || _rel_joins || ' WHERE ' || (CASE _s WHEN 'CT' THEN '(CT.link = 0 OR CT.link IS NULL) AND ' WHEN 'CX' THEN '(CX.link = 0 OR CX.link IS NULL) AND ' WHEN 'TM' THEN '(TM.link = 0 OR TM.link IS NULL OR (TM.year1 IS NOT NULL AND TM.year1 <> '''')) AND ' ELSE '' END) || 'lower(' || _s || '.' || _label || ') ~ ''' || __pattern || '''' || (CASE _limit WHEN 10 THEN '' ELSE ' ORDER BY ' || _s || '.' || _label END);
			IF _s = 'PR' THEN
				_query := 'SELECT PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, PR.ref' || _rel_cols || ' FROM "Athlete" PR' || _rel_joins;
				_query := _query || ' WHERE (PR.link = 0 OR PR.link IS NULL) AND (lower(PR.last_name || '' '' || PR.first_name) ~ ''' || __pattern || ''' OR lower(PR.first_name || '' '' || PR.last_name) ~ ''' || __pattern || ''' OR lower(PR.last_name) ~ ''' || __pattern || ''' OR lower(PR.first_name) ~ ''' || __pattern || ''')';
				_query := _query || (CASE _limit WHEN 10 THEN '' ELSE ' ORDER BY PR.last_name, PR.first_name' END);
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
						SELECT SUM(ref) INTO _current_ref FROM "City" WHERE id=_current_id OR link=_current_id;
					ELSIF _s = 'CX' THEN
						SELECT SUM(ref) INTO _current_ref FROM "Complex" WHERE id=_current_id OR link=_current_id;
					ELSIF _s = 'PR' THEN
						SELECT SUM(ref) INTO _current_ref FROM "Athlete" WHERE id=_current_id OR link=_current_id;
					ELSIF _s = 'TM' THEN
						SELECT SUM(ref) INTO _current_ref FROM "Team" WHERE id=_current_id OR link=_current_id;
					END IF;
				END IF;
				_item.count_ref = (CASE WHEN _current_ref IS NOT NULL THEN _current_ref ELSE 0 END);
				_item.entity = _s;
				_item.label_rel1 = _current_label_rel1;
				_item.label_rel2 = _current_label_rel2;
				_item.label_rel3 = _current_label_rel3;
				IF _limit <> 10 THEN
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
 
 
 
 alter table "Round" add id_rank4 integer;
alter table "Round" add result4 character varying(20);
alter table "Round" add id_rank5 integer;
alter table "Round" add result5 character varying(20);





CREATE OR REPLACE FUNCTION "GetRounds"(
    _id_result integer,
    _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _id_sport integer;
    _id_championship integer;
    _id_event integer;
    _id_subevent integer;
    _id_subevent2 integer;
    _type integer;
    _columns text;
    _joins text;
begin
	SELECT RS.id_sport, RS.id_championship, RS.id_event, RS.id_subevent, RS.id_subevent2 INTO _id_sport, _id_championship, _id_event, _id_subevent, _id_subevent2 FROM "Result" RS WHERE RS.id = _id_result;

	SELECT id_result_type INTO _type FROM "Round" WHERE id_result = _id_result;

	-- Build entity-specific columns/joins
	_columns := '';
	_joins := '';
	FOR i IN 1..5 LOOP
		IF _type < 10 THEN -- Athlete
			_columns := _columns || ', PR' || i || '.id AS rk' || i || '_id, PR' || i || '.last_name AS rk' || i || '_str1, PR' || i || '.first_name AS rk' || i || '_str2, NULL AS rk' || i || '_str3';
			_columns := _columns || ', PRTM' || i || '.id AS rk' || i || '_rel1_id, NULL AS rk' || i || '_rel1_code, PRTM' || i || '.label AS rk' || i || '_rel1_label, NULL AS rk' || i || '_rel1_label_en';
			_columns := _columns || ', PRCN' || i || '.id AS rk' || i || '_rel2_id, PRCN' || i || '.code AS rk' || i || '_rel2_code, PRCN' || i || '.label' || _lang || ' AS rk' || i || '_rel2_label, PRCN' || i || '.label AS rk' || i || '_rel2_label_en';
			_joins := _joins || ' LEFT JOIN "Athlete" PR' || i || ' ON RD.id_rank' || i || ' = PR' || i || '.id';
			_joins := _joins || ' LEFT JOIN "Team" PRTM' || i || ' ON PR' || i || '.id_team = PRTM' || i || '.id';
			_joins := _joins || ' LEFT JOIN "Country" PRCN' || i || ' ON PR' || i || '.id_country = PRCN' || i || '.id';
		ELSIF _type = 50 THEN -- Team
			_columns := _columns || ', TM' || i || '.id AS rk' || i || '_id, NULL AS rk' || i || '_str1, TM' || i || '.label AS rk' || i || '_str2, NULL AS rk' || i || '_str3';
			_columns := _columns || ', NULL AS rk' || i || '_rel1_id, NULL AS rk' || i || '_rel1_code, NULL AS rk' || i || '_rel1_label, NULL AS rk' || i || '_rel1_label_en';
			_columns := _columns || ', TMCN' || i || '.id AS rk' || i || '_rel2_id, TMCN' || i || '.code AS rk' || i || '_rel2_code, TMCN' || i || '.label' || _lang || ' AS rk' || i || '_rel2_label, TMCN' || i || '.label AS rk' || i || '_rel2_label_en';
			_joins := _joins || ' LEFT JOIN "Team" TM' || i || ' ON RD.id_rank' || i || ' = TM' || i || '.id';
			_joins := _joins || ' LEFT JOIN "Country" TMCN' || i || ' ON TM' || i || '.id_country = TMCN' || i || '.id';
		ELSIF _type = 99 THEN -- Country
			_columns := _columns || ', ENCN' || i || '.id AS rk' || i || '_id, ENCN' || i || '.code AS rk' || i || '_str1, ENCN' || i || '.label' || _lang || ' AS rk' || i || '_str2, ENCN' || i || '.label AS rk' || i || '_str3';
			_columns := _columns || ', NULL AS rk' || i || '_rel1_id, NULL AS rk' || i || '_rel1_code, NULL AS rk' || i || '_rel1_label, NULL AS rk' || i || '_rel1_label_en';
			_columns := _columns || ', NULL AS rk' || i || '_rel2_id, NULL AS rk' || i || '_rel2_code, NULL AS rk' || i || '_rel2_label, NULL AS rk' || i || '_rel2_label_en';
			_joins := _joins || ' LEFT JOIN "Country" ENCN' || i || ' ON RD.id_rank' || i || ' = ENCN' || i || '.id';
		END IF;
	END LOOP;

	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT
		RD.id AS rd_id, RD.id_result_type AS rd_result_type, RT.id AS rt_id, RT.label' || _lang || ' AS rt_label, RT.index AS rt_index, RD.result1 AS rd_result1, RD.result2 AS rd_result2, RD.result3 AS rd_result3, RD.date1 AS rd_date1, RD.date AS rd_date2, RD.exa AS rd_exa, RD.comment AS rd_comment,
		CX1.id AS cx1_id, CX1.label AS cx1_label, CX2.id AS cx2_id, CX2.label AS cx2_label,
		CT1.id AS ct1_id, CT1.label' || _lang || ' AS ct1_label, CT1.label AS ct1_label_en, ST1.id AS st1_id, ST1.code AS st1_code, ST1.label AS st1_label_en, CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label AS cn1_label_en,
		CT2.id AS ct2_id, CT2.label' || _lang || ' AS ct2_label, CT2.label AS ct2_label_en, ST2.id AS st2_id, ST2.code AS st2_code, ST2.label AS st2_label_en, CN2.id AS cn2_id, CN2.code AS cn2_code, CN2.label AS cn2_label_en,
		CT3.id AS ct3_id, CT3.label' || _lang || ' AS ct3_label, CT3.label AS ct3_label_en, ST3.id AS st3_id, ST3.code AS st3_code, ST3.label AS st3_label_en, CN3.id AS cn3_id, CN3.code AS cn3_code, CN3.label AS cn3_label_en,
		CT4.id AS ct4_id, CT4.label' || _lang || ' AS ct4_label, CT4.label AS ct4_label_en, ST4.id AS st4_id, ST4.code AS st4_code, ST4.label AS st4_label_en, CN4.id AS cn4_id, CN4.code AS cn4_code, CN4.label AS cn4_label_en' ||
		_columns || '
	FROM
		"Round" RD
		LEFT JOIN "RoundType" RT ON RD.id_round_type = RT.id
		LEFT JOIN "Complex" CX1 ON RD.id_complex1 = CX1.id
		LEFT JOIN "City" CT1 ON CX1.id_city = CT1.id
		LEFT JOIN "State" ST1 ON CT1.id_state = ST1.id
		LEFT JOIN "Country" CN1 ON CT1.id_country = CN1.id
		LEFT JOIN "City" CT2 ON RD.id_city1 = CT2.id
		LEFT JOIN "State" ST2 ON CT2.id_state = ST2.id
		LEFT JOIN "Country" CN2 ON CT2.id_country = CN2.id
		LEFT JOIN "Complex" CX2 ON RD.id_complex = CX2.id
		LEFT JOIN "City" CT3 ON CX2.id_city = CT3.id
		LEFT JOIN "State" ST3 ON CT3.id_state = ST3.id
		LEFT JOIN "Country" CN3 ON CT3.id_country = CN3.id
		LEFT JOIN "City" CT4 ON RD.id_city = CT4.id
		LEFT JOIN "State" ST4 ON CT4.id_state = ST4.id
		LEFT JOIN "Country" CN4 ON CT4.id_country = CN4.id
		LEFT JOIN "Result" RS ON RD.id_result = RS.id' ||
		_joins || '
	WHERE
		RD.id_result = ' || _id_result || '
	ORDER BY
		RT.index, RT.label, RD.id';
	RETURN  _c;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
  
  
  CREATE OR REPLACE FUNCTION "GetRounds"(
    _id_result integer,
    _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _id_sport integer;
    _id_championship integer;
    _id_event integer;
    _id_subevent integer;
    _id_subevent2 integer;
    _type integer;
    _columns text;
    _joins text;
begin
	SELECT RS.id_sport, RS.id_championship, RS.id_event, RS.id_subevent, RS.id_subevent2 INTO _id_sport, _id_championship, _id_event, _id_subevent, _id_subevent2 FROM "Result" RS WHERE RS.id = _id_result;

	SELECT id_result_type INTO _type FROM "Round" WHERE id_result = _id_result;

	-- Build entity-specific columns/joins
	_columns := '';
	_joins := '';
	FOR i IN 1..5 LOOP
		IF _type < 10 THEN -- Athlete
			_columns := _columns || ', PR' || i || '.id AS rk' || i || '_id, PR' || i || '.last_name AS rk' || i || '_str1, PR' || i || '.first_name AS rk' || i || '_str2, NULL AS rk' || i || '_str3';
			_columns := _columns || ', PRTM' || i || '.id AS rk' || i || '_rel1_id, NULL AS rk' || i || '_rel1_code, PRTM' || i || '.label AS rk' || i || '_rel1_label, NULL AS rk' || i || '_rel1_label_en';
			_columns := _columns || ', PRCN' || i || '.id AS rk' || i || '_rel2_id, PRCN' || i || '.code AS rk' || i || '_rel2_code, PRCN' || i || '.label' || _lang || ' AS rk' || i || '_rel2_label, PRCN' || i || '.label AS rk' || i || '_rel2_label_en';
			_joins := _joins || ' LEFT JOIN "Athlete" PR' || i || ' ON RD.id_rank' || i || ' = PR' || i || '.id';
			_joins := _joins || ' LEFT JOIN "Team" PRTM' || i || ' ON PR' || i || '.id_team = PRTM' || i || '.id';
			_joins := _joins || ' LEFT JOIN "Country" PRCN' || i || ' ON PR' || i || '.id_country = PRCN' || i || '.id';
		ELSIF _type = 50 THEN -- Team
			_columns := _columns || ', TM' || i || '.id AS rk' || i || '_id, NULL AS rk' || i || '_str1, TM' || i || '.label AS rk' || i || '_str2, NULL AS rk' || i || '_str3';
			_columns := _columns || ', NULL AS rk' || i || '_rel1_id, NULL AS rk' || i || '_rel1_code, NULL AS rk' || i || '_rel1_label, NULL AS rk' || i || '_rel1_label_en';
			_columns := _columns || ', TMCN' || i || '.id AS rk' || i || '_rel2_id, TMCN' || i || '.code AS rk' || i || '_rel2_code, TMCN' || i || '.label' || _lang || ' AS rk' || i || '_rel2_label, TMCN' || i || '.label AS rk' || i || '_rel2_label_en';
			_joins := _joins || ' LEFT JOIN "Team" TM' || i || ' ON RD.id_rank' || i || ' = TM' || i || '.id';
			_joins := _joins || ' LEFT JOIN "Country" TMCN' || i || ' ON TM' || i || '.id_country = TMCN' || i || '.id';
		ELSIF _type = 99 THEN -- Country
			_columns := _columns || ', ENCN' || i || '.id AS rk' || i || '_id, ENCN' || i || '.code AS rk' || i || '_str1, ENCN' || i || '.label' || _lang || ' AS rk' || i || '_str2, ENCN' || i || '.label AS rk' || i || '_str3';
			_columns := _columns || ', NULL AS rk' || i || '_rel1_id, NULL AS rk' || i || '_rel1_code, NULL AS rk' || i || '_rel1_label, NULL AS rk' || i || '_rel1_label_en';
			_columns := _columns || ', NULL AS rk' || i || '_rel2_id, NULL AS rk' || i || '_rel2_code, NULL AS rk' || i || '_rel2_label, NULL AS rk' || i || '_rel2_label_en';
			_joins := _joins || ' LEFT JOIN "Country" ENCN' || i || ' ON RD.id_rank' || i || ' = ENCN' || i || '.id';
		END IF;
	END LOOP;

	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT
		RD.id AS rd_id, RD.id_result_type AS rd_result_type, RT.id AS rt_id, RT.label' || _lang || ' AS rt_label, RT.index AS rt_index, RD.result1 AS rd_result1, RD.result2 AS rd_result2, RD.result3 AS rd_result3, RD.result4 AS rd_result4, RD.result5 AS rd_result5, RD.date1 AS rd_date1, RD.date AS rd_date2, RD.exa AS rd_exa, RD.comment AS rd_comment,
		CX1.id AS cx1_id, CX1.label AS cx1_label, CX2.id AS cx2_id, CX2.label AS cx2_label,
		CT1.id AS ct1_id, CT1.label' || _lang || ' AS ct1_label, CT1.label AS ct1_label_en, ST1.id AS st1_id, ST1.code AS st1_code, ST1.label AS st1_label_en, CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label AS cn1_label_en,
		CT2.id AS ct2_id, CT2.label' || _lang || ' AS ct2_label, CT2.label AS ct2_label_en, ST2.id AS st2_id, ST2.code AS st2_code, ST2.label AS st2_label_en, CN2.id AS cn2_id, CN2.code AS cn2_code, CN2.label AS cn2_label_en,
		CT3.id AS ct3_id, CT3.label' || _lang || ' AS ct3_label, CT3.label AS ct3_label_en, ST3.id AS st3_id, ST3.code AS st3_code, ST3.label AS st3_label_en, CN3.id AS cn3_id, CN3.code AS cn3_code, CN3.label AS cn3_label_en,
		CT4.id AS ct4_id, CT4.label' || _lang || ' AS ct4_label, CT4.label AS ct4_label_en, ST4.id AS st4_id, ST4.code AS st4_code, ST4.label AS st4_label_en, CN4.id AS cn4_id, CN4.code AS cn4_code, CN4.label AS cn4_label_en' ||
		_columns || '
	FROM
		"Round" RD
		LEFT JOIN "RoundType" RT ON RD.id_round_type = RT.id
		LEFT JOIN "Complex" CX1 ON RD.id_complex1 = CX1.id
		LEFT JOIN "City" CT1 ON CX1.id_city = CT1.id
		LEFT JOIN "State" ST1 ON CT1.id_state = ST1.id
		LEFT JOIN "Country" CN1 ON CT1.id_country = CN1.id
		LEFT JOIN "City" CT2 ON RD.id_city1 = CT2.id
		LEFT JOIN "State" ST2 ON CT2.id_state = ST2.id
		LEFT JOIN "Country" CN2 ON CT2.id_country = CN2.id
		LEFT JOIN "Complex" CX2 ON RD.id_complex = CX2.id
		LEFT JOIN "City" CT3 ON CX2.id_city = CT3.id
		LEFT JOIN "State" ST3 ON CT3.id_state = ST3.id
		LEFT JOIN "Country" CN3 ON CT3.id_country = CN3.id
		LEFT JOIN "City" CT4 ON RD.id_city = CT4.id
		LEFT JOIN "State" ST4 ON CT4.id_state = ST4.id
		LEFT JOIN "Country" CN4 ON CT4.id_country = CN4.id
		LEFT JOIN "Result" RS ON RD.id_result = RS.id' ||
		_joins || '
	WHERE
		RD.id_result = ' || _id_result || '
	ORDER BY
		RT.index, RT.label, RD.id';
	RETURN  _c;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
  
  
  
  CREATE OR REPLACE FUNCTION "~LastUpdates"(
    _count integer,
    _offset integer,
    _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
	_c refcursor;
begin
	OPEN _c FOR EXECUTE
	'SELECT RS.id AS id, RS.id AS rs_id, YR.id AS yr_id, YR.label AS yr_label, SP.id AS sp_id, CP.id AS cp_id, EV.id AS ev_id, SE.id AS se_id, SE2.id AS se2_id, SP.label' || _lang || ' AS sp_label, CP.label' || _lang || ' AS cp_label, EV.label' || _lang || ' AS ev_label, SE.label' || _lang || ' AS se_label, SE2.label' || _lang || ' AS se2_label, SP.label AS sp_label_en, CP.label AS cp_label_en, EV.label AS ev_label_en, SE.label AS se_label_en, SE2.label AS se2_label_en, TP1.number as tp1_number, TP2.number AS tp2_number, TP3.number AS tp3_number,
	  PR1.id AS pr1_id, PR1.first_name AS pr1_first_name, PR1.last_name AS pr1_last_name, PR1.id_team AS pr1_team, PR1.id_country AS pr1_country, PRCN1.code AS pr1_country_code, TM1.id AS tm1_id, TM1.label AS tm1_label, CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label' || _lang || ' AS cn1_label, CN1.label AS cn1_label_en, 
	  PR2.id AS pr2_id, PR2.first_name AS pr2_first_name, PR2.last_name AS pr2_last_name, PR2.id_team AS pr2_team, PR2.id_country AS pr2_country, PRCN2.code AS pr2_country_code, TM2.id AS tm2_id, TM2.label AS tm2_label, CN2.id AS cn2_id, CN2.code AS cn2_code, CN2.label' || _lang || ' AS cn2_label, CN2.label AS cn2_label_en,
	  PR3.id AS pr3_id, PR3.first_name AS pr3_first_name, PR3.last_name AS pr3_last_name, PR3.id_team AS pr3_team, PR3.id_country AS pr3_country, PRCN3.code AS pr3_country_code, TM3.id AS tm3_id, TM3.label AS tm3_label, CN3.id AS cn3_id, CN3.code AS cn3_code, CN3.label' || _lang || ' AS cn3_label, CN3.label AS cn3_label_en,
	  PR4.id AS pr4_id, PR4.first_name AS pr4_first_name, PR4.last_name AS pr4_last_name, PR4.id_team AS pr4_team, PR4.id_country AS pr4_country, PRCN4.code AS pr4_country_code, TM4.id AS tm4_id, TM4.label AS tm4_label, CN4.id AS cn4_id, CN4.code AS cn4_code, CN4.label' || _lang || ' AS cn4_label, CN4.label AS cn4_label_en,
	  RS.result1 AS rs_text1, RS.result2 AS rs_text2, RS.exa AS rs_text3, RS.comment AS rs_text4, NULL AS rs_text5, (CASE WHEN RS.date2 IS NOT NULL AND RS.date2<>'''' THEN to_date(RS.date2, ''dd/MM/yyyy'') ELSE RS.first_update END) AS rs_date
	  FROM "Result" RS
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
	WHERE RS.draft = false
	UNION
	SELECT RD.id AS id, RS.id AS rs_id, YR.id AS yr_id, YR.label AS yr_label, SP.id AS sp_id, CP.id AS cp_id, EV.id AS ev_id, SE.id AS se_id, SE2.id AS se2_id, SP.label' || _lang || ' AS sp_label, CP.label' || _lang || ' AS cp_label, EV.label' || _lang || ' AS ev_label, SE.label' || _lang || ' AS se_label, SE2.label' || _lang || ' AS se2_label, SP.label AS sp_label_en, CP.label AS cp_label_en, EV.label AS ev_label_en, SE.label AS se_label_en, SE2.label AS se2_label_en, TP1.number as tp1_number, TP2.number AS tp2_number, TP3.number AS tp3_number,
	  PR1.id AS pr1_id, PR1.first_name AS pr1_first_name, PR1.last_name AS pr1_last_name, PR1.id_team AS pr1_team, PR1.id_country AS pr1_country, PRCN1.code AS pr1_country_code, TM1.id AS tm1_id, TM1.label AS tm1_label, CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label' || _lang || ' AS cn1_label, CN1.label AS cn1_label_en, 
	  PR2.id AS pr2_id, PR2.first_name AS pr2_first_name, PR2.last_name AS pr2_last_name, PR2.id_team AS pr2_team, PR2.id_country AS pr2_country, PRCN2.code AS pr2_country_code, TM2.id AS tm2_id, TM2.label AS tm2_label, CN2.id AS cn2_id, CN2.code AS cn2_code, CN2.label' || _lang || ' AS cn2_label, CN2.label AS cn2_label_en,
	  PR3.id AS pr3_id, PR3.first_name AS pr3_first_name, PR3.last_name AS pr3_last_name, PR3.id_team AS pr3_team, PR3.id_country AS pr3_country, PRCN3.code AS pr3_country_code, TM3.id AS tm3_id, TM3.label AS tm3_label, CN3.id AS cn3_id, CN3.code AS cn3_code, CN3.label' || _lang || ' AS cn3_label, CN3.label AS cn3_label_en, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
	  RD.result1 AS rs_text1, RD.result2 AS rs_text2, RD.exa AS rs_text3, RD.comment AS rs_text4, RT.label' || _lang || ' AS rs_text5, (CASE WHEN RD.date IS NOT NULL AND RD.date<>'''' THEN to_date(RD.date, ''dd/MM/yyyy'') ELSE RD.first_update END) AS rs_date
	  FROM "Round" RD
		LEFT JOIN "RoundType" RT ON RD.id_round_type=RT.id
		LEFT JOIN "Result" RS ON RD.id_result=RS.id
		LEFT JOIN "Year" YR ON RS.id_year=YR.id
		LEFT JOIN "Sport" SP ON RS.id_sport=SP.id
		LEFT JOIN "Championship" CP ON RS.id_championship=CP.id
		LEFT JOIN "Event" EV ON RS.id_event=EV.id
		LEFT JOIN "Event" SE ON RS.id_subevent=SE.id
		LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id
		LEFT JOIN "Type" TP1 ON EV.id_type=TP1.id
		LEFT JOIN "Type" TP2 ON SE.id_type=TP2.id
		LEFT JOIN "Type" TP3 ON SE2.id_type=TP3.id
		LEFT JOIN "Athlete" PR1 ON RD.id_rank1=PR1.id
		LEFT JOIN "Athlete" PR2 ON RD.id_rank2=PR2.id
		LEFT JOIN "Athlete" PR3 ON RD.id_rank3=PR3.id
		LEFT JOIN "Country" PRCN1 ON PR1.id_country=PRCN1.id
		LEFT JOIN "Country" PRCN2 ON PR2.id_country=PRCN2.id
		LEFT JOIN "Country" PRCN3 ON PR3.id_country=PRCN3.id
		LEFT JOIN "Team" TM1 ON RD.id_rank1=TM1.id
		LEFT JOIN "Team" TM2 ON RD.id_rank2=TM2.id
		LEFT JOIN "Team" TM3 ON RD.id_rank3=TM3.id
		LEFT JOIN "Country" CN1 ON RD.id_rank1=CN1.id
		LEFT JOIN "Country" CN2 ON RD.id_rank2=CN2.id
		LEFT JOIN "Country" CN3 ON RD.id_rank3=CN3.id
	ORDER BY yr_id DESC, rs_date DESC, rs_id DESC LIMIT ' || _count || ' OFFSET ' || _offset;
	RETURN  _c;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
  
  
  ALTER TABLE "~Config"
   ALTER COLUMN value TYPE character varying(300);
  
  
  
  
  
  CREATE OR REPLACE FUNCTION "~LastUpdates"(
    _count integer,
    _offset integer,
    _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
	_c refcursor;
begin
	OPEN _c FOR EXECUTE
	'SELECT RS.id AS id, RS.id AS rs_id, YR.id AS yr_id, YR.label AS yr_label, SP.id AS sp_id, CP.id AS cp_id, EV.id AS ev_id, SE.id AS se_id, SE2.id AS se2_id, SP.label' || _lang || ' AS sp_label, CP.label' || _lang || ' AS cp_label, EV.label' || _lang || ' AS ev_label, SE.label' || _lang || ' AS se_label, SE2.label' || _lang || ' AS se2_label, SP.label AS sp_label_en, CP.label AS cp_label_en, EV.label AS ev_label_en, SE.label AS se_label_en, SE2.label AS se2_label_en, TP1.number as tp1_number, TP2.number AS tp2_number, TP3.number AS tp3_number,
	  PR1.id AS pr1_id, PR1.first_name AS pr1_first_name, PR1.last_name AS pr1_last_name, PR1.id_team AS pr1_team, PR1.id_country AS pr1_country, PRCN1.code AS pr1_country_code, TM1.id AS tm1_id, TM1.label AS tm1_label, CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label' || _lang || ' AS cn1_label, CN1.label AS cn1_label_en, 
	  PR2.id AS pr2_id, PR2.first_name AS pr2_first_name, PR2.last_name AS pr2_last_name, PR2.id_team AS pr2_team, PR2.id_country AS pr2_country, PRCN2.code AS pr2_country_code, TM2.id AS tm2_id, TM2.label AS tm2_label, CN2.id AS cn2_id, CN2.code AS cn2_code, CN2.label' || _lang || ' AS cn2_label, CN2.label AS cn2_label_en,
	  PR3.id AS pr3_id, PR3.first_name AS pr3_first_name, PR3.last_name AS pr3_last_name, PR3.id_team AS pr3_team, PR3.id_country AS pr3_country, PRCN3.code AS pr3_country_code, TM3.id AS tm3_id, TM3.label AS tm3_label, CN3.id AS cn3_id, CN3.code AS cn3_code, CN3.label' || _lang || ' AS cn3_label, CN3.label AS cn3_label_en,
	  PR4.id AS pr4_id, PR4.first_name AS pr4_first_name, PR4.last_name AS pr4_last_name, PR4.id_team AS pr4_team, PR4.id_country AS pr4_country, PRCN4.code AS pr4_country_code, TM4.id AS tm4_id, TM4.label AS tm4_label, CN4.id AS cn4_id, CN4.code AS cn4_code, CN4.label' || _lang || ' AS cn4_label, CN4.label AS cn4_label_en,
	  RS.result1 AS rs_text1, RS.result2 AS rs_text2, RS.exa AS rs_text3, RS.comment AS rs_text4, NULL AS rs_text5, (CASE WHEN RS.date2 IS NOT NULL AND RS.date2<>'''' THEN to_date(RS.date2, ''dd/MM/yyyy'') ELSE RS.first_update END) AS rs_date, 1000 AS rt_index
	  FROM "Result" RS
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
	WHERE RS.draft = false
	UNION
	SELECT RD.id AS id, RS.id AS rs_id, YR.id AS yr_id, YR.label AS yr_label, SP.id AS sp_id, CP.id AS cp_id, EV.id AS ev_id, SE.id AS se_id, SE2.id AS se2_id, SP.label' || _lang || ' AS sp_label, CP.label' || _lang || ' AS cp_label, EV.label' || _lang || ' AS ev_label, SE.label' || _lang || ' AS se_label, SE2.label' || _lang || ' AS se2_label, SP.label AS sp_label_en, CP.label AS cp_label_en, EV.label AS ev_label_en, SE.label AS se_label_en, SE2.label AS se2_label_en, TP1.number as tp1_number, TP2.number AS tp2_number, TP3.number AS tp3_number,
	  PR1.id AS pr1_id, PR1.first_name AS pr1_first_name, PR1.last_name AS pr1_last_name, PR1.id_team AS pr1_team, PR1.id_country AS pr1_country, PRCN1.code AS pr1_country_code, TM1.id AS tm1_id, TM1.label AS tm1_label, CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label' || _lang || ' AS cn1_label, CN1.label AS cn1_label_en, 
	  PR2.id AS pr2_id, PR2.first_name AS pr2_first_name, PR2.last_name AS pr2_last_name, PR2.id_team AS pr2_team, PR2.id_country AS pr2_country, PRCN2.code AS pr2_country_code, TM2.id AS tm2_id, TM2.label AS tm2_label, CN2.id AS cn2_id, CN2.code AS cn2_code, CN2.label' || _lang || ' AS cn2_label, CN2.label AS cn2_label_en,
	  PR3.id AS pr3_id, PR3.first_name AS pr3_first_name, PR3.last_name AS pr3_last_name, PR3.id_team AS pr3_team, PR3.id_country AS pr3_country, PRCN3.code AS pr3_country_code, TM3.id AS tm3_id, TM3.label AS tm3_label, CN3.id AS cn3_id, CN3.code AS cn3_code, CN3.label' || _lang || ' AS cn3_label, CN3.label AS cn3_label_en, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
	  RD.result1 AS rs_text1, RD.result2 AS rs_text2, RD.exa AS rs_text3, RD.comment AS rs_text4, RT.label' || _lang || ' AS rs_text5, (CASE WHEN RD.date IS NOT NULL AND RD.date<>'''' THEN to_date(RD.date, ''dd/MM/yyyy'') ELSE RD.first_update END) AS rs_date, RT.index AS rt_index
	  FROM "Round" RD
		LEFT JOIN "RoundType" RT ON RD.id_round_type=RT.id
		LEFT JOIN "Result" RS ON RD.id_result=RS.id
		LEFT JOIN "Year" YR ON RS.id_year=YR.id
		LEFT JOIN "Sport" SP ON RS.id_sport=SP.id
		LEFT JOIN "Championship" CP ON RS.id_championship=CP.id
		LEFT JOIN "Event" EV ON RS.id_event=EV.id
		LEFT JOIN "Event" SE ON RS.id_subevent=SE.id
		LEFT JOIN "Event" SE2 ON RS.id_subevent2=SE2.id
		LEFT JOIN "Type" TP1 ON EV.id_type=TP1.id
		LEFT JOIN "Type" TP2 ON SE.id_type=TP2.id
		LEFT JOIN "Type" TP3 ON SE2.id_type=TP3.id
		LEFT JOIN "Athlete" PR1 ON RD.id_rank1=PR1.id
		LEFT JOIN "Athlete" PR2 ON RD.id_rank2=PR2.id
		LEFT JOIN "Athlete" PR3 ON RD.id_rank3=PR3.id
		LEFT JOIN "Country" PRCN1 ON PR1.id_country=PRCN1.id
		LEFT JOIN "Country" PRCN2 ON PR2.id_country=PRCN2.id
		LEFT JOIN "Country" PRCN3 ON PR3.id_country=PRCN3.id
		LEFT JOIN "Team" TM1 ON RD.id_rank1=TM1.id
		LEFT JOIN "Team" TM2 ON RD.id_rank2=TM2.id
		LEFT JOIN "Team" TM3 ON RD.id_rank3=TM3.id
		LEFT JOIN "Country" CN1 ON RD.id_rank1=CN1.id
		LEFT JOIN "Country" CN2 ON RD.id_rank2=CN2.id
		LEFT JOIN "Country" CN3 ON RD.id_rank3=CN3.id
	ORDER BY yr_id DESC, rs_date DESC, rt_index DESC, rs_id DESC LIMIT ' || _count || ' OFFSET ' || _offset;
	RETURN  _c;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
  
  
  
  
  