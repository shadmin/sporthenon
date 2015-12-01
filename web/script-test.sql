CREATE OR REPLACE FUNCTION "~LastUpdates"(_count integer, _offset integer, _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
	_c refcursor;
begin
	OPEN _c FOR EXECUTE
	'SELECT RS.id AS rs_id, YR.id AS yr_id, YR.label AS yr_label, SP.id AS sp_id, CP.id AS cp_id, EV.id AS ev_id, SE.id AS se_id, SE2.id AS se2_id, SP.label' || _lang || ' AS sp_label, CP.label' || _lang || ' AS cp_label, EV.label' || _lang || ' AS ev_label, SE.label' || _lang || ' AS se_label, SE2.label' || _lang || ' AS se2_label, SP.label AS sp_label_en, CP.label AS cp_label_en, EV.label AS ev_label_en, SE.label AS se_label_en, SE2.label AS se2_label_en, TP1.number as tp1_number, TP2.number AS tp2_number, TP3.number AS tp3_number,
	  PR1.id AS pr1_id, PR1.first_name AS pr1_first_name, PR1.last_name AS pr1_last_name, PR1.id_country AS pr1_country, PRCN1.code AS pr1_country_code, TM1.id AS tm1_id, TM1.label AS tm1_label, CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label' || _lang || ' AS cn1_label, CN1.label AS cn1_label_en, 
	  PR2.id AS pr2_id, PR2.first_name AS pr2_first_name, PR2.last_name AS pr2_last_name, PR2.id_country AS pr2_country, PRCN2.code AS pr2_country_code, TM2.id AS tm2_id, TM2.label AS tm2_label, CN2.id AS cn2_id, CN2.code AS cn2_code, CN2.label' || _lang || ' AS cn2_label, CN2.label AS cn2_label_en,
	  PR3.id AS pr3_id, PR3.first_name AS pr3_first_name, PR3.last_name AS pr3_last_name, PR3.id_country AS pr3_country, PRCN3.code AS pr3_country_code, TM3.id AS tm3_id, TM3.label AS tm3_label, CN3.id AS cn3_id, CN3.code AS cn3_code, CN3.label' || _lang || ' AS cn3_label, CN3.label AS cn3_label_en,
	  PR4.id AS pr4_id, PR4.first_name AS pr4_first_name, PR4.last_name AS pr4_last_name, PR4.id_country AS pr4_country, PRCN4.code AS pr4_country_code, TM4.id AS tm4_id, TM4.label AS tm4_label, CN4.id AS cn4_id, CN4.code AS cn4_code, CN4.label' || _lang || ' AS cn4_label, CN4.label AS cn4_label_en,
	  RS.result1 AS rs_text1, RS.result2 AS rs_text2, RS.exa AS rs_text3, RS.comment AS rs_text4, (CASE WHEN RS.date2 IS NOT NULL AND RS.date2<>'''' THEN to_date(RS.date2, ''dd/MM/yyyy'') ELSE RS.first_update END) AS rs_date FROM "Result" RS
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
	ORDER BY YR.id DESC, rs_date DESC LIMIT ' || _count || ' OFFSET ' || _offset;
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;

  
  
  
  
  
  
  CREATE OR REPLACE FUNCTION "~Normalize"(_pattern character varying)
  RETURNS character varying AS
$BODY$
declare
	_result text;
begin
	_result = lower(_pattern);
	_result = regexp_replace(_result, '[áàäăāãåâ]', 'a', 'g');
	_result = regexp_replace(_result, '[æ]', 'ae', 'g');
	_result = regexp_replace(_result, '[ćčç]', 'c', 'g');
	_result = regexp_replace(_result, '[đ]', 'dj', 'g');
	_result = regexp_replace(_result, '[ėéèêëěęē]', 'e', 'g');
	_result = regexp_replace(_result, '[ğ]', 'g', 'g');
	_result = regexp_replace(_result, '[ıíï]', 'i', 'g');
	_result = regexp_replace(_result, '[ł]', 'l', 'g');
	_result = regexp_replace(_result, '[ńñ]', 'n', 'g');
	_result = regexp_replace(_result, '[óòöōø]', 'o', 'g');
	_result = regexp_replace(_result, '[ř]', 'r', 'g');
	_result = regexp_replace(_result, '[śšş]', 's', 'g');
	_result = regexp_replace(_result, '[ß]', 'ss', 'g');
	_result = regexp_replace(_result, '[ţ]', 't', 'g');
	_result = regexp_replace(_result, '[uūúü]', 'u', 'g');
	_result = regexp_replace(_result, '[ý]', 'y', 'g');
	_result = regexp_replace(_result, '[żźžŽ]', 'z', 'g');

	RETURN _result;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  
  
  
  
 CREATE OR REPLACE FUNCTION "Search"(_pattern character varying, _scope character varying, _limit smallint, _lang character varying)
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
	_scopes := '{PR,CT,CX,CN,CP,EV,SP,TM,ST,YR}';
	_tables := '{Athlete,City,Complex,Country,Championship,Event,Sport,Team,State,Year}';
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
			FOR _j IN (_rel_count + 1)..3 LOOP
				_rel_cols := _rel_cols || ', NULL, NULL, NULL';
			END LOOP;
			IF (_s ~ 'CT|CX|PR|TM') THEN
				_rel_cols := _rel_cols || ', ' || _s || '.link';
			END IF;
			
			-- Execute query
			_label := 'label';
			_label_en := 'label';
			IF (_s <> 'TM' AND _s <> 'YR') THEN
				_label := 'label' || _lang;
			END IF;
			_query := 'SELECT ' || _s || '.id, ' || _s || '.' || _label || ',' || _s || '.' || _label_en || ',' || _s || '.ref' || _rel_cols || ' FROM "' || _tables[_i] || '" ' || _s;
			_query := _query || _rel_joins || ' WHERE ' || (CASE _s WHEN 'CT' THEN '(CT.link = 0 OR CT.link IS NULL) AND ' WHEN 'CX' THEN '(CX.link = 0 OR CX.link IS NULL) AND ' WHEN 'TM' THEN '(TM.link = 0 OR TM.link IS NULL OR (TM.year1 IS NOT NULL AND TM.year1 <> '''')) AND ' ELSE '' END) || 'lower(' || _s || '.' || _label || ') ~ ''' || __pattern || '''' || (CASE _limit WHEN 0 THEN ' ORDER BY ' || _s || '.' || _label ELSE '' END);
			IF _s = 'PR' THEN
				_query := 'SELECT PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, PR.ref' || _rel_cols || ' FROM "Athlete" PR' || _rel_joins;
				_query := _query || ' WHERE (PR.link = 0 OR PR.link IS NULL) AND (lower(PR.last_name || '' '' || PR.first_name) ~ ''' || __pattern || ''' OR lower(PR.first_name || '' '' || PR.last_name) ~ ''' || __pattern || ''' OR lower(PR.last_name) ~ ''' || __pattern || ''' OR lower(PR.first_name) ~ ''' || __pattern || ''')';
				_query := _query || (CASE _limit WHEN 0 THEN ' ORDER BY PR.last_name, PR.first_name' ELSE '' END);
				RAISE NOTICE '%', _query;
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
				ELSE
					_item.count_ref = (CASE WHEN _current_ref IS NOT NULL THEN _current_ref ELSE 0 END);
				END IF;
				_item.entity = _s;
				_item.label_rel1 = _current_label_rel1;
				_item.label_rel3 = _current_label_rel3;
				IF _limit = 0 THEN
					_item.label_en = _current_label_en;
					_item.id_rel1 = _current_id_rel1;
					_item.id_rel2 = _current_id_rel2;
					_item.id_rel3 = _current_id_rel3;
					_item.label_rel2 = _current_label_rel2;
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
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
  
  
  
  

    CREATE OR REPLACE FUNCTION "~PatternString"(_pattern character varying)
  RETURNS character varying AS
$BODY$
declare
	_result text;
begin
	_result := lower(_pattern);
	_result := replace(_result, '%', '.*');
	_result := regexp_replace(_result, '(a|á|à|ä|ă|ā|ã|å|â)', '(a|á|à|ä|ă|ā|ã|å|â)', 'g');
	_result := regexp_replace(_result, '(ae|æ)', '(ae|æ)', 'g');
	_result := regexp_replace(_result, '(c|ć|č|ç)', '(c|ć|č|ç)', 'g');
	_result := regexp_replace(_result, '(dj|đ)', '(dj|đ)', 'g');
	_result := regexp_replace(_result, '(e|ė|é|è|ê|ë|ě|ę|ē)', '(e|ė|é|è|ê|ë|ě|ę|ē)', 'g');
	_result := regexp_replace(_result, '(g|ğ)', '(g|ğ)', 'g');
	_result := regexp_replace(_result, '(i|ı|í|ï)', '(i|ı|í|ï)', 'g');
	_result := regexp_replace(_result, '(l|ł)', '(l|ł)', 'g');
	_result := regexp_replace(_result, '(n|ń|ñ)', '(n|ń|ñ)', 'g');
	_result := regexp_replace(_result, '(o|ó|ò|ö|ō|ø)', '(o|ó|ò|ö|ō|ø)', 'g');
	_result := regexp_replace(_result, '(r|ř)', '(r|ř)', 'g');
	_result := regexp_replace(_result, '(s|ś|š|ş)', '(s|ś|š|ş)', 'g');
	_result := regexp_replace(_result, '(ss|ß)', '(ss|ß)', 'g');
	_result := regexp_replace(_result, '(t|ţ)', '(t|ţ)', 'g');
	_result := regexp_replace(_result, '(u|ū|ú|ü)', '(u|ū|ú|ü)', 'g');
	_result := regexp_replace(_result, '(y|ý)', '(y|ý)', 'g');
	_result := regexp_replace(_result, '(z|ż|ź|ž)', '(z|ż|ź|ž)', 'g');

	RETURN _result;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  
  
  
  