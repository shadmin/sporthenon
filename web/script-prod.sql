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
  
  
  
  CREATE OR REPLACE FUNCTION "GetCalendarResults"(
    _date1 character varying,
    _date2 character varying,
    _sp integer,
    _lang character varying)
  RETURNS SETOF "~RefItem" AS
$BODY$
declare
	_item "~RefItem"%rowtype;
	_c refcursor;
	__c refcursor;
	_query text;
	_where text;
	_index integer;
	_type1 smallint;
	_type2 smallint;
	_type3 smallint;
	_date1 varchar(10) := _date1;
	_date2 varchar(10) := _date2;
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
	_cn1 varchar(35);_cn2 varchar(35);_cn3 varchar(35);_cn4 varchar(35);_cn5 varchar(35);_cn6 varchar(35);
	_tm1 varchar(60);_tm2 varchar(60);_tm3 varchar(60);_tm4 varchar(60);_tm5 varchar(60);_tm6 varchar(60);
begin
	_where := ' WHERE RS.draft = false';
	IF length(_date1) = 4 THEN
		_where := _where || ' AND YR.label=''' || _date1 || ''' AND (date1 IS NULL OR date1='''') AND (date2 IS NULL OR date2='''')';
	ELSE
		_where := _where || ' AND to_date(date2, ''DD/MM/YYYY'') >= to_date(''' || _date1 || ''', ''YYYYMMDD'') AND to_date(date2, ''DD/MM/YYYY'') <= to_date(''' || _date2 || ''', ''YYYYMMDD'')';
	END IF;
	IF (_sp > 0) THEN
		_where := _where || ' AND SP.id = ' || _sp;
	END IF;

	-- Past events
	_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number, TP3.number, RS.date1, RS.date2';
	_query := _query || ' FROM "Result" RS';
	_query := _query || ' LEFT JOIN "Year" YR ON RS.id_year = YR.id';
	_query := _query || ' LEFT JOIN "Sport" SP ON RS.id_sport = SP.id';
	_query := _query || ' LEFT JOIN "Championship" CP ON RS.id_championship = CP.id';
	_query := _query || ' LEFT JOIN "Event" EV ON RS.id_event = EV.id';
	_query := _query || ' LEFT JOIN "Event" SE ON RS.id_subevent = SE.id';
	_query := _query || ' LEFT JOIN "Event" SE2 ON RS.id_subevent2 = SE2.id';
	_query := _query || ' LEFT JOIN "Type" TP1 ON EV.id_type = TP1.id';
	_query := _query || ' LEFT JOIN "Type" TP2 ON SE.id_type = TP2.id';
	_query := _query || ' LEFT JOIN "Type" TP3 ON SE2.id_type = TP3.id' || _where;
	_index := 1;
	OPEN _c FOR EXECUTE _query;
	LOOP
		FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2, _type3, _date1, _date2, _item.count1;
		EXIT WHEN NOT FOUND;
		IF _type3 IS NOT NULL THEN
			_type1 = _type3;
		ELSIF _type2 IS NOT NULL THEN
			_type1 = _type2;
		END IF;
		IF _type1 <= 10 THEN
			SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, (CASE WHEN _lang = '_fr' THEN CN1.label_fr ELSE CN1.label END), TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, (CASE WHEN _lang = '_fr' THEN CN2.label_fr ELSE CN2.label END), TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, (CASE WHEN _lang = '_fr' THEN CN3.label_fr ELSE CN3.label END), TM3.label, PR4.last_name || (CASE WHEN length(PR4.first_name) > 0 THEN ', ' || PR4.first_name ELSE '' END), (CASE WHEN length(PR4.first_name) > 0 THEN PR4.first_name || ' ' ELSE '' END) || PR4.last_name, CN4.id, (CASE WHEN _lang = '_fr' THEN CN4.label_fr ELSE CN4.label END), TM4.label, PR5.last_name || (CASE WHEN length(PR5.first_name) > 0 THEN ', ' || PR5.first_name ELSE '' END), (CASE WHEN length(PR5.first_name) > 0 THEN PR5.first_name || ' ' ELSE '' END) || PR5.last_name, CN5.id, (CASE WHEN _lang = '_fr' THEN CN5.label_fr ELSE CN5.label END), TM5.label, PR6.last_name || (CASE WHEN length(PR6.first_name) > 0 THEN ', ' || PR6.first_name ELSE '' END), (CASE WHEN length(PR6.first_name) > 0 THEN PR6.first_name || ' ' ELSE '' END) || PR6.last_name, CN6.id, (CASE WHEN _lang = '_fr' THEN CN6.label_fr ELSE CN6.label END), TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
			INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.label_rel23, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.label_rel24, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.label_rel25, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt3, _item.txt4
			FROM "Result" RS LEFT JOIN "Athlete" PR1 ON RS.id_rank1 = PR1.id LEFT JOIN "Athlete" PR2 ON RS.id_rank2 = PR2.id LEFT JOIN "Athlete" PR3 ON RS.id_rank3 = PR3.id LEFT JOIN "Athlete" PR4 ON RS.id_rank4 = PR4.id LEFT JOIN "Athlete" PR5 ON RS.id_rank5 = PR5.id LEFT JOIN "Athlete" PR6 ON RS.id_rank6 = PR6.id LEFT JOIN "Country" CN1 ON PR1.id_country = CN1.id LEFT JOIN "Country" CN2 ON PR2.id_country = CN2.id LEFT JOIN "Country" CN3 ON PR3.id_country = CN3.id LEFT JOIN "Country" CN4 ON PR4.id_country = CN4.id LEFT JOIN "Country" CN5 ON PR5.id_country = CN5.id LEFT JOIN "Country" CN6 ON PR6.id_country = CN6.id LEFT JOIN "Team" TM1 ON PR1.id_team = TM1.id LEFT JOIN "Team" TM2 ON PR2.id_team = TM2.id LEFT JOIN "Team" TM3 ON PR3.id_team = TM3.id LEFT JOIN "Team" TM4 ON PR4.id_team = TM4.id LEFT JOIN "Team" TM5 ON PR5.id_team = TM5.id LEFT JOIN "Team" TM6 ON PR6.id_team = TM6.id
			WHERE RS.id = _item.id_item;
			IF _cn1 IS NOT NULL THEN _item.label_rel6 = _item.label_rel6 || '|' || _cn1;
			ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 = _item.label_rel6 || '|' || _tm1; END IF;
			IF _cn2 IS NOT NULL THEN _item.label_rel7 = _item.label_rel7 || '|' || _cn2;
			ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 = _item.label_rel7 || '|' || _tm2; END IF;
			IF _cn3 IS NOT NULL THEN _item.label_rel8 = _item.label_rel8 || '|' || _cn3;
			ELSIF _tm3 IS NOT NULL THEN _item.label_rel8 = _item.label_rel8 || '|' || _tm3; END IF;
			IF _cn4 IS NOT NULL THEN _item.label_rel9 = _item.label_rel9 || '|' || _cn4;
			ELSIF _tm4 IS NOT NULL THEN _item.label_rel9 = _item.label_rel9 || '|' || _tm4; END IF;
			IF _cn5 IS NOT NULL THEN _item.label_rel10 = _item.label_rel10 || '|' || _cn5;
			ELSIF _tm5 IS NOT NULL THEN _item.label_rel10 = _item.label_rel10 || '|' || _tm5; END IF;
			IF _cn6 IS NOT NULL THEN _item.label_rel11 = _item.label_rel11 || '|' || _cn6;
			ELSIF _tm6 IS NOT NULL THEN _item.label_rel11 = _item.label_rel11 || '|' || _tm6; END IF;
			IF _type1 = 4 OR _item.txt3 = '#DOUBLE#' THEN
				_item.txt4 = '1-2/3-4/5-6';
			ELSIF _type1 = 5 OR _item.txt3 = '#TRIPLE#' THEN
				_item.txt4 = '1-3/4-6/7-9';
			END IF;
			_item.comment = 'PR';
		ELSIF _type1 = 50 THEN
			SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, NULL, NULL, NULL, NULL, NULL, NULL, RS.result1, RS.result2, RS.comment, RS.exa
			INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4
			FROM "Result" RS LEFT JOIN "Team" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "Team" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "Team" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "Team" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "Team" TM5 ON RS.id_rank5 = TM5.id LEFT JOIN "Team" TM6 ON RS.id_rank6 = TM6.id
			WHERE RS.id = _item.id_item;
			_item.comment = 'TM';
		ELSIF _type1 = 99 THEN
			_query = 'SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN4.label' || _lang || ', CN5.label' || _lang || ', CN6.label' || _lang || ', CN1.label, CN2.label, CN3.label, CN4.label, CN5.label, CN6.label, RS.result1, RS.result2, RS.comment, RS.exa';
			_query = _query || ' FROM "Result" RS LEFT JOIN "Country" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "Country" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "Country" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "Country" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "Country" CN5 ON RS.id_rank5 = CN5.id LEFT JOIN "Country" CN6 ON RS.id_rank6 = CN6.id';
			_query = _query || ' WHERE RS.id = ' || _item.id_item;
			OPEN __c FOR EXECUTE _query;
			FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4;
			CLOSE __c;
			_item.comment = 'CN';
		END IF;
		_item.date1 := to_date(_date1, 'DD/MM/YYYY');
		_item.date2 := to_date(_date2, 'DD/MM/YYYY');
		_item.id := _index;
		_item.entity := 'RS';
		RETURN NEXT _item;
		_index = _index + 1;
	END LOOP;
	CLOSE _c;

	-- Future events
	IF length(_date1) > 4 THEN
		_query := 'SELECT CL.id, NULL, NULL, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, CX.id, CX.label, CX.label, CT1.id, CT1.label' || _lang || ' || '', '' || CN1.code, CT1.label, CT2.id, CT2.label' || _lang || ' || '', '' || CN2.code, CT2.label, CN1.id, CN2.id, CN3.id, CN3.label' || _lang || ', CN3.label, CL.date1, CL.date2';
		_query := _query || ' FROM "Calendar" CL';
		_query := _query || ' LEFT JOIN "Sport" SP ON CL.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "Championship" CP ON CL.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "Event" EV ON CL.id_event = EV.id';
		_query := _query || ' LEFT JOIN "Event" SE ON CL.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "Event" SE2 ON CL.id_subevent2 = SE2.id';
		_query := _query || ' LEFT JOIN "Complex" CX ON CL.id_complex = CX.id';
		_query := _query || ' LEFT JOIN "City" CT1 ON CX.id_city = CT1.id';
		_query := _query || ' LEFT JOIN "State" ST1 ON CT1.id_state = ST1.id';
		_query := _query || ' LEFT JOIN "Country" CN1 ON CT1.id_country = CN1.id';
		_query := _query || ' LEFT JOIN "City" CT2 ON CL.id_city = CT2.id';
		_query := _query || ' LEFT JOIN "State" ST2 ON CT2.id_state = ST2.id';
		_query := _query || ' LEFT JOIN "Country" CN2 ON CT2.id_country = CN2.id';
		_query := _query || ' LEFT JOIN "Country" CN3 ON CL.id_country = CN3.id' || _where;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _item.id_rel6, _item.label_rel6, _item.label_rel9, _item.id_rel7, _item.label_rel7, _item.label_rel10, _item.id_rel8, _item.label_rel8, _item.label_rel11, _item.id_rel10, _item.id_rel11, _item.id_rel9, _item.label_rel19, _item.label_rel20, _date1, _date2;
			EXIT WHEN NOT FOUND;
			_item.date1 := to_date(_date1, 'DD/MM/YYYY');
			_item.date2 := to_date(_date2, 'DD/MM/YYYY');
			_item.id := _index;
			_item.entity := 'CL';
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
  
  
  
  
  DROP VIEW "~Statistics";
CREATE OR REPLACE VIEW "~Statistics" AS 
 SELECT ( SELECT count(*) AS count
           FROM "Athlete") AS count_person,
    ( SELECT count(*) AS count
           FROM "City") AS count_city,
    ( SELECT count(*) AS count
           FROM "Complex") AS count_complex,
    ( SELECT count(*) AS count
           FROM "Country") AS count_country,
    ( SELECT count(*) AS count
           FROM "Event") AS count_event,
    ( SELECT count(*) AS count
           FROM "Result") AS count_result,
    ( SELECT count(*) AS count
           FROM "Round") AS count_round,
    ( SELECT count(*) AS count
           FROM "Sport") AS count_sport,
    ( SELECT count(*) AS count
           FROM "Team") AS count_team;
           
           
           
           
           
           
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
		_query = _query || 'concat_ws('','', coalesce(RS.id_complex1, ''0''), coalesce(RS.id_complex2, ''0''), coalesce(RS.id_city1, ''0''), coalesce(RS.id_city2, ''0'')) AS places, concat_ws('','', coalesce(date1, ''0''), coalesce(date2, ''0'')) AS dates, TP1.number, TP2.number, TP3.number, string_agg(DISTINCT(CAST (EL.id AS VARCHAR)), '',''), string_agg(DISTINCT(CAST (RD.id AS VARCHAR)), '','')';	
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
			_query = _query || ' OR CT.id IN (SELECT RS.id_city1 FROM "Result" WHERE id_sport=' || _id_sport || ' UNION SELECT RS.id_city2 FROM "Result" WHERE id_sport=' || _id_sport || ')';
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
			_query = _query || ' OR CX.id IN (SELECT RS.id_complex1 FROM "Result" WHERE id_sport=' || _id_sport || ' UNION SELECT RS.id_complex2 FROM "Result" WHERE id_sport=' || _id_sport || ')';
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
  
  
  
  
  