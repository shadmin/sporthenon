CREATE OR REPLACE FUNCTION "GetResults"(
    _id_sport integer,
    _id_championship integer,
    _id_event integer,
    _id_subevent integer,
    _id_subevent2 integer,
    _years text,
    _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _type integer;
    _columns text;
    _joins text;
    _event_condition text;
    _year_condition text;
begin
	-- Get entity type (person, country, team)
	IF _id_subevent2 <> 0 THEN
	    SELECT
	        TP.number
	    INTO
	        _type
	    FROM
	        "Event" EV
	        LEFT JOIN "Type" TP ON EV.id_type = TP.id
	    WHERE
	        EV.id = _id_subevent2;
	ELSIF _id_subevent <> 0 THEN
	    SELECT
	        TP.number
	    INTO
	        _type
	    FROM
	        "Event" EV
	        LEFT JOIN "Type" TP ON EV.id_type = TP.id
	    WHERE
	        EV.id = _id_subevent;
	ELSIF _id_event <> 0 THEN
	    SELECT
	        TP.number
	    INTO
	        _type
	    FROM
	        "Event" EV
	        LEFT JOIN "Type" TP ON EV.id_type = TP.id
	    WHERE
	        EV.id = _id_event;	        
	ELSE
	    SELECT DISTINCT
	        TP.number
	    INTO
	        _type
	    FROM
	        "Result" RS
	        LEFT JOIN "Event" EV ON RS.id_event = EV.id
	        LEFT JOIN "Type" TP ON EV.id_type = TP.id
	    WHERE
	         RS.id_sport = _id_sport AND RS.id_championship = _id_championship;
	END IF;

	-- Build entity-specific columns/joins
	_columns := '';
	_joins := '';
	FOR i IN 1..20 LOOP
	    IF _type < 10 THEN -- Person
	        _columns := _columns || ', PR' || i || '.last_name AS en' || i || '_str1, PR' || i || '.first_name AS en' || i || '_str2, NULL AS en' || i || '_str3';
	        _columns := _columns || ', PRTM' || i || '.id AS en' || i || '_rel1_id, NULL AS en' || i || '_rel1_code, PRTM' || i || '.label AS en' || i || '_rel1_label';
	        _columns := _columns || ', PRCN' || i || '.id AS en' || i || '_rel2_id, PRCN' || i || '.code AS en' || i || '_rel2_code, PRCN' || i || '.label' || _lang || ' AS en' || i || '_rel2_label, PRCN' || i || '.label AS en' || i || '_rel2_label_en';
	        _joins := _joins || ' LEFT JOIN "Athlete" PR' || i || ' ON RS.id_rank' || i || ' = PR' || i || '.id';
	        _joins := _joins || ' LEFT JOIN "Team" PRTM' || i || ' ON PR' || i || '.id_team = PRTM' || i || '.id';
	        _joins := _joins || ' LEFT JOIN "Country" PRCN' || i || ' ON PR' || i || '.id_country = PRCN' || i || '.id';
	    ELSIF _type = 50 THEN -- Team
	        _columns := _columns || ', NULL AS en' || i || '_str1, TM' || i || '.label AS en' || i || '_str2, NULL AS en' || i || '_str3';
	        _columns := _columns || ', NULL AS en' || i || '_rel1_id, NULL AS en' || i || '_rel1_code, NULL AS en' || i || '_rel1_label';
	        _columns := _columns || ', TMCN' || i || '.id AS en' || i || '_rel2_id, TMCN' || i || '.code AS en' || i || '_rel2_code, TMCN' || i || '.label' || _lang || ' AS en' || i || '_rel2_label, TMCN' || i || '.label AS en' || i || '_rel2_label_en';
	        _joins := _joins || ' LEFT JOIN "Team" TM' || i || ' ON RS.id_rank' || i || ' = TM' || i || '.id';
	        _joins := _joins || ' LEFT JOIN "Country" TMCN' || i || ' ON TM' || i || '.id_country = TMCN' || i || '.id';
	    ELSIF _type = 99 THEN -- Country
	        _columns := _columns || ', _CN' || i || '.code AS en' || i || '_str1, _CN' || i || '.label' || _lang || ' AS en' || i || '_str2, _CN' || i || '.label AS en' || i || '_str3';
	        _columns := _columns || ', NULL AS en' || i || '_rel1_id, NULL AS en' || i || '_rel1_code, NULL AS en' || i || '_rel1_label';
	        _columns := _columns || ', NULL AS en' || i || '_rel2_id, NULL AS en' || i || '_rel2_code, NULL AS en' || i || '_rel2_label, NULL AS en' || i || '_rel2_label_en';
	        _joins := _joins || ' LEFT JOIN "Country" _CN' || i || ' ON RS.id_rank' || i || ' = _CN' || i || '.id';
	    END IF;
	END LOOP;

	-- Handle null event/subevent
	_event_condition := '';
	IF _id_event <> 0 THEN
	    _event_condition := ' AND RS.id_event = ' ||_id_event;
	END IF;
	IF _id_subevent <> 0 THEN
	    _event_condition := _event_condition || ' AND RS.id_subevent = ' ||_id_subevent;
	END IF;
	IF _id_subevent2 <> 0 THEN
	    _event_condition := _event_condition || ' AND RS.id_subevent2 = ' ||_id_subevent2;
	END IF;

	-- Set year condition
	_year_condition := '';
	IF _years <> '0' THEN
		_year_condition := ' AND YR.id IN (' || _years || ')';
	END IF;
	
	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT
		RS.id AS rs_id, RS.date1 AS rs_date1, RS.date2 AS rs_date2, RS.id_rank1 AS rs_rank1, RS.id_rank2 AS rs_rank2, RS.id_rank3 AS rs_rank3, RS.id_rank4 AS rs_rank4, RS.id_rank5 AS rs_rank5, RS.id_rank6 AS rs_rank6, RS.id_rank7 AS rs_rank7, RS.id_rank8 AS rs_rank8, RS.id_rank9 AS rs_rank9, RS.id_rank10 AS rs_rank10, RS.id_rank11 AS rs_rank11, RS.id_rank12 AS rs_rank12, RS.id_rank13 AS rs_rank13, RS.id_rank14 AS rs_rank14, RS.id_rank15 AS rs_rank15, RS.id_rank16 AS rs_rank16, RS.id_rank17 AS rs_rank17, RS.id_rank18 AS rs_rank18, RS.id_rank19 AS rs_rank19, RS.id_rank20 AS rs_rank20,
		RS.result1 AS rs_result1, RS.result2 AS rs_result2, RS.result3 AS rs_result3, RS.result4 AS rs_result4, RS.result5 AS rs_result5, RS.result6 AS rs_result6, RS.result7 AS rs_result7, RS.result8 AS rs_result8, RS.result9 AS rs_result9, RS.result10 AS rs_result10, RS.result11 AS rs_result11, RS.result12 AS rs_result12, RS.result13 AS rs_result13, RS.result14 AS rs_result14, RS.result15 AS rs_result15, RS.result16 AS rs_result16, RS.result17 AS rs_result17, RS.result18 AS rs_result18, RS.result19 AS rs_result19, RS.result20 AS rs_result20,
		RS.comment AS rs_comment, RS.exa AS rs_exa, YR.id AS yr_id, YR.label AS yr_label, CX1.id AS cx1_id, CX1.label AS cx1_label, CX2.id AS cx2_id, CX2.label AS cx2_label,
		CT1.id AS ct1_id, CT1.label' || _lang || ' AS ct1_label, CT1.label AS ct1_label_en, CT2.id AS ct2_id, CT2.label' || _lang || ' AS ct2_label, CT2.label AS ct2_label_en, CT3.id AS ct3_id, CT3.label' || _lang || ' AS ct3_label, CT3.label AS ct3_label_en, CT4.id AS ct4_id, CT4.label' || _lang || ' AS ct4_label, CT4.label AS ct4_label_en, ST1.id AS st1_id, ST1.code AS st1_code, ST1.label' || _lang || ' AS st1_label, ST1.label AS st1_label_en, ST2.id AS st2_id, ST2.code AS st2_code,
		ST2.label' || _lang || ' AS st2_label, ST2.label AS st2_label_en, ST3.id AS st3_id, ST3.code AS st3_code, ST3.label' || _lang || ' AS st3_label, ST3.label AS st3_label_en, ST4.id AS st4_id, ST4.code AS st4_code, ST4.label' || _lang || ' AS st4_label, ST4.label AS st4_label_en, CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label' || _lang || ' AS cn1_label, CN1.label AS cn1_label_en, CN2.id AS cn2_id, CN2.code AS cn2_code, CN2.label' || _lang || ' AS cn2_label, CN2.label AS cn2_label_en, CN3.id AS cn3_id, CN3.code AS cn3_code, CN3.label' || _lang || ' AS cn3_label, CN3.label AS cn3_label_en, CN4.id AS cn4_id, CN4.code AS cn4_code, CN4.label' || _lang || ' AS cn4_label, CN4.label AS cn4_label_en, CN5.id AS cn5_id, CN5.code AS cn5_code, CN5.label' || _lang || ' AS cn5_label, CN5.label AS cn5_label_en, CN6.id AS cn6_id, CN6.code AS cn6_code, CN6.label' || _lang || ' AS cn6_label, CN6.label AS cn6_label_en' ||
		_columns || '
	FROM
		"Result" RS
		LEFT JOIN "Year" YR ON RS.id_year = YR.id
		LEFT JOIN "Complex" CX1 ON RS.id_complex1 = CX1.id
		LEFT JOIN "Complex" CX2 ON RS.id_complex2 = CX2.id
		LEFT JOIN "City" CT1 ON CX1.id_city = CT1.id
		LEFT JOIN "City" CT2 ON RS.id_city1 = CT2.id
		LEFT JOIN "City" CT3 ON CX2.id_city = CT3.id
		LEFT JOIN "City" CT4 ON RS.id_city2 = CT4.id
		LEFT JOIN "State" ST1 ON CT1.id_state = ST1.id
		LEFT JOIN "State" ST2 ON CT2.id_state = ST2.id
		LEFT JOIN "State" ST3 ON CT3.id_state = ST3.id
		LEFT JOIN "State" ST4 ON CT4.id_state = ST4.id
		LEFT JOIN "Country" CN1 ON CT1.id_country = CN1.id
		LEFT JOIN "Country" CN2 ON CT2.id_country = CN2.id
		LEFT JOIN "Country" CN3 ON CT3.id_country = CN3.id
		LEFT JOIN "Country" CN4 ON CT4.id_country = CN4.id
		LEFT JOIN "Country" CN5 ON RS.id_country1 = CN5.id
		LEFT JOIN "Country" CN6 ON RS.id_country2 = CN6.id' ||
		_joins || '
	WHERE
		RS.draft = false AND RS.id_sport = ' || _id_sport || ' AND
		RS.id_championship = ' || _id_championship ||
		_event_condition || _year_condition || '
	ORDER BY RS.id_year DESC, RS.id DESC';
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
  
  
  