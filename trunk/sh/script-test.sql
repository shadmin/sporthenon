CREATE OR REPLACE FUNCTION "GET_RESULTS"(_id_sport integer, _id_championship integer, _id_event integer, _id_subevent integer, _id_subevent2 integer, _years text, _lang character varying)
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
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'RS', _id_sport || '-' || _id_championship, current_date);

	-- Get entity type (person, country, team)
	IF _id_subevent2 <> 0 THEN
	    SELECT
	        TP.number
	    INTO
	        _type
	    FROM
	        "EVENT" EV
	        LEFT JOIN "TYPE" TP ON EV.id_type = TP.id
	    WHERE
	        EV.id = _id_subevent2;
	ELSIF _id_subevent <> 0 THEN
	    SELECT
	        TP.number
	    INTO
	        _type
	    FROM
	        "EVENT" EV
	        LEFT JOIN "TYPE" TP ON EV.id_type = TP.id
	    WHERE
	        EV.id = _id_subevent;
	ELSIF _id_event <> 0 THEN
	    SELECT
	        TP.number
	    INTO
	        _type
	    FROM
	        "EVENT" EV
	        LEFT JOIN "TYPE" TP ON EV.id_type = TP.id
	    WHERE
	        EV.id = _id_event;	        
	ELSE
	    SELECT DISTINCT
	        TP.number
	    INTO
	        _type
	    FROM
	        "RESULT" RS
	        LEFT JOIN "EVENT" EV ON RS.id_event = EV.id
	        LEFT JOIN "TYPE" TP ON EV.id_type = TP.id
	    WHERE
	         RS.id_sport = _id_sport AND RS.id_championship = _id_championship;
	END IF;

	-- Build entity-specific columns/joins
	_columns := '';
	_joins := '';
	FOR i IN 1..10 LOOP
	    IF _type < 10 THEN -- Person
	        _columns := _columns || ', PR' || i || '.last_name AS en' || i || '_str1, PR' || i || '.first_name AS en' || i || '_str2';
	        _columns := _columns || ', PRTM' || i || '.id AS en' || i || '_rel1_id, NULL AS en' || i || '_rel1_code, PRTM' || i || '.label AS en' || i || '_rel1_label';
	        _columns := _columns || ', PRCN' || i || '.id AS en' || i || '_rel2_id, PRCN' || i || '.code AS en' || i || '_rel2_code, PRCN' || i || '.label' || _lang || ' AS en' || i || '_rel2_label';
	        _joins := _joins || ' LEFT JOIN "PERSON" PR' || i || ' ON RS.id_rank' || i || ' = PR' || i || '.id';
	        _joins := _joins || ' LEFT JOIN "TEAM" PRTM' || i || ' ON PR' || i || '.id_team = PRTM' || i || '.id';
	        _joins := _joins || ' LEFT JOIN "COUNTRY" PRCN' || i || ' ON PR' || i || '.id_country = PRCN' || i || '.id';
	    ELSIF _type = 50 THEN -- Team
	        _columns := _columns || ', NULL AS en' || i || '_str1, TM' || i || '.label AS en' || i || '_str2';
	        _columns := _columns || ', NULL AS en' || i || '_rel1_id, NULL AS en' || i || '_rel1_code, NULL AS en' || i || '_rel1_label';
	        _columns := _columns || ', TMCN' || i || '.id AS en' || i || '_rel2_id, TMCN' || i || '.code AS en' || i || '_rel2_code, TMCN' || i || '.label' || _lang || ' AS en' || i || '_rel2_label';
	        _joins := _joins || ' LEFT JOIN "TEAM" TM' || i || ' ON RS.id_rank' || i || ' = TM' || i || '.id';
	        _joins := _joins || ' LEFT JOIN "COUNTRY" TMCN' || i || ' ON TM' || i || '.id_country = TMCN' || i || '.id';
	    ELSIF _type = 99 THEN -- Country
	        _columns := _columns || ', _CN' || i || '.code AS en' || i || '_str1, _CN' || i || '.label' || _lang || ' AS en' || i || '_str2';
	        _columns := _columns || ', NULL AS en' || i || '_rel1_id, NULL AS en' || i || '_rel1_code, NULL AS en' || i || '_rel1_label';
	        _columns := _columns || ', NULL AS en' || i || '_rel2_id, NULL AS en' || i || '_rel2_code, NULL AS en' || i || '_rel2_label';
	        _joins := _joins || ' LEFT JOIN "COUNTRY" _CN' || i || ' ON RS.id_rank' || i || ' = _CN' || i || '.id';
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
		RS.id AS rs_id, RS.date1 AS rs_date1, RS.date2 AS rs_date2, RS.id_rank1 AS rs_rank1, RS.id_rank2 AS rs_rank2, RS.id_rank3 AS rs_rank3, RS.id_rank4 AS rs_rank4, RS.id_rank5 AS rs_rank5,
		RS.id_rank6 AS rs_rank6, RS.id_rank7 AS rs_rank7, RS.id_rank8 AS rs_rank8, RS.id_rank9 AS rs_rank9, RS.id_rank10 AS rs_rank10, RS.result1 AS rs_result1, RS.result2 AS rs_result2,
		RS.result3 AS rs_result3, RS.result4 AS rs_result4, RS.result5 AS rs_result5, RS.comment AS rs_comment, RS.exa AS rs_exa, YR.id AS yr_id, YR.label AS yr_label, CX1.id AS cx1_id, CX1.label' || _lang || ' AS cx1_label, CX2.id AS cx2_id, CX2.label' || _lang || ' AS cx2_label,
		CT1.id AS ct1_id, CT1.label' || _lang || ' AS ct1_label, CT2.id AS ct2_id, CT2.label' || _lang || ' AS ct2_label, CT3.id AS ct3_id, CT3.label' || _lang || ' AS ct3_label, CT4.id AS ct4_id, CT4.label' || _lang || ' AS ct4_label, ST1.id AS st1_id, ST1.code AS st1_code, ST1.label' || _lang || ' AS st1_label, ST2.id AS st2_id, ST2.code AS st2_code,
		ST2.label' || _lang || ' AS st2_label, ST3.id AS st3_id, ST3.code AS st3_code, ST3.label' || _lang || ' AS st3_label, ST4.id AS st4_id, ST4.code AS st4_code, ST4.label' || _lang || ' AS st4_label, CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label' || _lang || ' AS cn1_label, CN2.id AS cn2_id, CN2.code AS cn2_code, CN2.label' || _lang || ' AS cn2_label, CN3.id AS cn3_id, CN3.code AS cn3_code, CN3.label' || _lang || ' AS cn3_label, CN4.id AS cn4_id, CN4.code AS cn4_code, CN4.label' || _lang || ' AS cn4_label, DR.id as dr_id' ||
		_columns || '
	FROM
		"RESULT" RS
		LEFT JOIN "YEAR" YR ON RS.id_year = YR.id
		LEFT JOIN "COMPLEX" CX1 ON RS.id_complex1 = CX1.id
		LEFT JOIN "COMPLEX" CX2 ON RS.id_complex2 = CX2.id
		LEFT JOIN "CITY" CT1 ON CX1.id_city = CT1.id
		LEFT JOIN "CITY" CT2 ON RS.id_city1 = CT2.id
		LEFT JOIN "CITY" CT3 ON CX2.id_city = CT3.id
		LEFT JOIN "CITY" CT4 ON RS.id_city2 = CT4.id
		LEFT JOIN "STATE" ST1 ON CT1.id_state = ST1.id
		LEFT JOIN "STATE" ST2 ON CT2.id_state = ST2.id
		LEFT JOIN "STATE" ST3 ON CT3.id_state = ST3.id
		LEFT JOIN "STATE" ST4 ON CT4.id_state = ST4.id
		LEFT JOIN "COUNTRY" CN1 ON CT1.id_country = CN1.id
		LEFT JOIN "COUNTRY" CN2 ON CT2.id_country = CN2.id
		LEFT JOIN "COUNTRY" CN3 ON CT3.id_country = CN3.id
		LEFT JOIN "COUNTRY" CN4 ON CT4.id_country = CN4.id
		LEFT JOIN "DRAW" DR ON DR.id_result = RS.id' ||
		_joins || '
	WHERE
		RS.id_sport = ' || _id_sport || ' AND
		RS.id_championship = ' || _id_championship ||
		_event_condition || _year_condition || '
	ORDER BY RS.id_year DESC';
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;

  
CREATE OR REPLACE FUNCTION "GET_HALL_OF_FAME"(_id_league integer, _years text, _position character varying)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _year_condition text;
    _position_condition text;
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'US', 'HF-' || _id_league, current_date);

	-- Set year condition ('All years' = Empty condition)
	_year_condition := '';
	IF _years <> '0' THEN
		_year_condition := ' AND YR.id IN (' || _years || ')';
	END IF;

	-- Set position condition
	_position_condition := '';
	IF _position <> '' THEN
		_position_condition := ' AND HF.position ~* ''(^|-)' || _position || '($|-)''';
	END IF;
	
	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT
		HF.id AS hf_id, HF.id_league AS lg_id, YR.id AS yr_id, YR.label AS yr_label, PR.id AS pr_id, PR.last_name AS pr_last_name,
		PR.first_name AS pr_first_name, HF.position AS hf_position
	FROM
		"HALL_OF_FAME" HF
		LEFT JOIN "YEAR" YR ON HF.id_year = YR.id
		LEFT JOIN "PERSON" PR ON HF.id_person = PR.id
	WHERE
		HF.id_league = ' || _id_league || _year_condition || _position_condition || '
	ORDER BY
		YR.id DESC, PR.last_name';
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;



CREATE OR REPLACE FUNCTION "GET_US_CHAMPIONSHIPS"(_id_championship integer, _years text, _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _year_condition text;
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'US', 'CP-' || _id_championship, current_date);

	_year_condition := CASE WHEN _years <> '0' THEN ' AND YR.id IN (' || _years || ')' ELSE '' END;
	
	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT
		RS.id AS rs_id, RS.date1 AS rs_date1, RS.date2 AS rs_date2, RS.id_rank1 AS rs_rank1, RS.id_rank2 AS rs_rank2, TM1.label as rs_team1, TM2.label as rs_team2, RS.result1 AS rs_result,
		RS.comment AS rs_comment, RS.exa AS rs_exa, YR.id AS yr_id, YR.label AS yr_label, CX.id AS cx_id, CX.label' || _lang || ' AS cx_label,
		CT.id AS ct_id, CT.label' || _lang || ' AS ct_label, ST.id AS st_id, ST.code AS st_code, ST.label' || _lang || ' AS st_label, CN.id AS cn_id, CN.code AS cn_code, CN.label' || _lang || ' AS cn_label
	FROM
		"RESULT" RS
		LEFT JOIN "TEAM" TM1 ON RS.id_rank1 = TM1.id
		LEFT JOIN "TEAM" TM2 ON RS.id_rank2 = TM2.id
		LEFT JOIN "YEAR" YR ON RS.id_year = YR.id
		LEFT JOIN "COMPLEX" CX ON RS.id_complex2 = CX.id
		LEFT JOIN "CITY" CT ON CX.id_city = CT.id
		LEFT JOIN "STATE" ST ON CT.id_state = ST.id
		LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id
	WHERE
		RS.id_championship = ' || _id_championship || ' AND 
		RS.id_event IN (455,532,572,621) AND (RS.id_subevent IS NULL OR RS.id_subevent IN (452,453,454,573,624,530)) ' || _year_condition || '
	ORDER BY RS.id_year DESC';
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;

  
CREATE OR REPLACE FUNCTION "ENTITY_REF"(_entity character varying, _id integer, _entity_ref character varying, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_c refcursor;
	_query text;
	_link integer;
	_pr_list varchar(50);
	_index integer;
	_type1 integer;
	_type2 integer;
	_type3 integer;
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
	_cn1 varchar(5);_cn2 varchar(5);_cn3 varchar(5);_cn4 varchar(5);_cn5 varchar(5);_cn6 varchar(5);
	_tm1 varchar(60);_tm2 varchar(60);_tm3 varchar(60);_tm4 varchar(60);_tm5 varchar(60);_tm6 varchar(60);
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'IF', _entity || '-' || _id, current_date);
	
	_index := 1;

	IF _entity ~ 'PR' THEN
		SELECT LINK INTO _link FROM "PERSON" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "PERSON" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_pr_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_pr_list := _pr_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_pr_list := cast(_id AS varchar);
		END IF;
	END IF;

	-- References in: [Results]
	IF (_entity ~ 'CN|PR|TM|CP|EV|CT|SP|CX|OL|YR' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number, TP3.number FROM "RESULT" RS';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id';
		_query := _query || ' LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id';
		IF (_entity = 'OL') THEN
			_query := _query || ' LEFT JOIN "OLYMPICS" OL ON (OL.id_year = YR.id AND OL.type = SP.type)';
		END IF;
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity ~ 'CN|TM' THEN
			_query := _query || ' AND (RS.id_rank1 = ' || _id || ' OR RS.id_rank2 = ' || _id || ' OR RS.id_rank3 = ' || _id || ' OR RS.id_rank4 = ' || _id || ' OR RS.id_rank5 = ' || _id || ' OR RS.id_rank6 = ' || _id || ' OR RS.id_rank7 = ' || _id || ' OR RS.id_rank8 = ' || _id || ' OR RS.id_rank9 = ' || _id || ' OR RS.id_rank10 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RS.id_rank1 in (' || _pr_list || ') OR RS.id_rank2 in (' || _pr_list || ') OR RS.id_rank3 in (' || _pr_list || ') OR RS.id_rank4 in (' || _pr_list || ') OR RS.id_rank5 in (' || _pr_list || ') OR RS.id_rank6 in (' || _pr_list || ') OR RS.id_rank7 in (' || _pr_list || ') OR RS.id_rank8 in (' || _pr_list || ') OR RS.id_rank9 in (' || _pr_list || ') OR RS.id_rank10 in (' || _pr_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND  (RS.id_city1 = ' || _id || ' OR RS.id_city2 = ' || _id || ')';
		ELSIF _entity = 'CX' THEN
			_query := _query || ' AND  (RS.id_complex1 = ' || _id || ' OR RS.id_complex2 = ' || _id || ')';
		ELSIF _entity = 'OL' THEN
			_query := _query || ' AND OL.id = ' || _id;
		ELSIF _entity = 'YR' THEN
			_query := _query || ' AND RS.id_year = ' || _id;
		END IF;
		--_query := _query || ' ORDER BY SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', YR.id';
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF _type1 <= 10 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || ', ' || PR1.first_name, CN1.id, CN1.code, TM1.label, PR2.last_name || ', ' || PR2.first_name, CN2.id, CN2.code, TM2.label, PR3.last_name || ', ' || PR3.first_name, CN3.id, CN3.code, TM3.label, PR4.last_name || ', ' || PR4.first_name, CN4.id, CN4.code, TM4.label, PR5.last_name || ', ' || PR5.first_name, CN5.id, CN5.code, TM5.label, PR6.last_name || ', ' || PR6.first_name, CN6.id, CN6.code, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "PERSON" PR1 ON RS.id_rank1 = PR1.id LEFT JOIN "PERSON" PR2 ON RS.id_rank2 = PR2.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3 = PR3.id LEFT JOIN "PERSON" PR4 ON RS.id_rank4 = PR4.id LEFT JOIN "PERSON" PR5 ON RS.id_rank5 = PR5.id LEFT JOIN "PERSON" PR6 ON RS.id_rank6 = PR6.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "COUNTRY" CN3 ON PR3.id_country = CN3.id LEFT JOIN "COUNTRY" CN4 ON PR4.id_country = CN4.id LEFT JOIN "COUNTRY" CN5 ON PR5.id_country = CN5.id LEFT JOIN "COUNTRY" CN6 ON PR6.id_country = CN6.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id LEFT JOIN "TEAM" TM3 ON PR3.id_team = TM3.id LEFT JOIN "TEAM" TM4 ON PR4.id_team = TM4.id LEFT JOIN "TEAM" TM5 ON PR5.id_team = TM5.id LEFT JOIN "TEAM" TM6 ON PR6.id_team = TM6.id
				WHERE RS.id = _item.id_item;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				IF _cn3 IS NOT NULL THEN _item.label_rel8 := _item.label_rel8 || ' (' || _cn3 || ')';
				ELSIF _tm3 IS NOT NULL THEN _item.label_rel8 := _item.label_rel8 || ' (' || _tm3 || ')'; END IF;
				IF _cn4 IS NOT NULL THEN _item.label_rel9 := _item.label_rel9 || ' (' || _cn4 || ')';
				ELSIF _tm4 IS NOT NULL THEN _item.label_rel9 := _item.label_rel9 || ' (' || _tm4 || ')'; END IF;
				IF _cn5 IS NOT NULL THEN _item.label_rel10 := _item.label_rel10 || ' (' || _cn5 || ')';
				ELSIF _tm5 IS NOT NULL THEN _item.label_rel10 := _item.label_rel10 || ' (' || _tm5 || ')'; END IF;
				IF _cn6 IS NOT NULL THEN _item.label_rel11 := _item.label_rel11 || ' (' || _cn6 || ')';
				ELSIF _tm6 IS NOT NULL THEN _item.label_rel11 := _item.label_rel11 || ' (' || _tm6 || ')'; END IF;
				IF _type1 = 4 THEN
					_item.txt4 = '1-2/3-4/5-6';
				END IF;
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "TEAM" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "TEAM" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "TEAM" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "TEAM" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "TEAM" TM5 ON RS.id_rank5 = TM5.id LEFT JOIN "TEAM" TM6 ON RS.id_rank6 = TM6.id
				WHERE RS.id = _item.id_item;
			ELSIF _type1 = 99 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.code, CN2.code, CN3.code, CN4.code, CN5.code, CN6.code, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "COUNTRY" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "COUNTRY" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "COUNTRY" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "COUNTRY" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "COUNTRY" CN5 ON RS.id_rank5 = CN5.id LEFT JOIN "COUNTRY" CN6 ON RS.id_rank6 = CN6.id
				WHERE RS.id = _item.id_item;
			END IF;
			_item.id = _index;
			_item.entity = 'RS';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;
	
	-- References in: [Athletes]
	IF (_entity ~ 'CN|SP|TM' AND (_entity_ref = 'PR' OR _entity_ref = '')) THEN
		_query := 'SELECT PR.id, PR.last_name || '', '' || PR.first_name, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', TM.id, TM.label FROM "PERSON" PR';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON PR.id_country = CN.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON PR.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "TEAM" TM ON PR.id_team = TM.id';
		IF _entity = 'CN' THEN
			_query := _query || ' WHERE PR.id_country = ' || _id;
		ELSIF _entity = 'SP' THEN
			_query := _query || ' WHERE PR.id_sport = ' || _id;
		ELSIF _entity = 'TM' THEN
			_query := _query || ' WHERE PR.id_team = ' || _id;
		END IF;
		_query := _query || ' ORDER BY PR.last_name, PR.first_name';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'PR';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Teams]
	IF (_entity ~ 'SP|CN' AND (_entity_ref = 'TM' OR _entity_ref = '')) THEN
		_query := 'SELECT TM.id, TM.label, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ' FROM "TEAM" TM';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON TM.id_country = CN.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON TM.id_sport = SP.id';
		IF _entity = 'SP' THEN
			_query := _query || ' WHERE TM.id_sport = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query := _query || ' WHERE TM.id_country = ' || _id;
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', TM.label';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'TM';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Cities]
	IF (_entity ~ 'CN|ST' AND (_entity_ref = 'CT' OR _entity_ref = '')) THEN
		_query := 'SELECT CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ' FROM "CITY" CT';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'CN' THEN
			_query := _query || ' WHERE CT.id_country = ' || _id;
		ELSIF _entity = 'ST' THEN
			_query := _query || ' WHERE CT.id_state = ' || _id;
		END IF;
		_query := _query || ' ORDER BY CT.label' || _lang;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CT';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Complexes]
	IF (_entity = 'CT' AND (_entity_ref = 'CX' OR _entity_ref = '')) THEN
		_query := 'SELECT CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ' FROM "COMPLEX" CX';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' WHERE CX.id_city = ' || _id;
		_query := _query || ' ORDER BY CX.label' || _lang;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CX';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Olympics]
	IF (_entity ~ 'YR|CT' AND (_entity_ref = 'OL' OR _entity_ref = '')) THEN
		_query := 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', OL.type FROM "OLYMPICS" OL';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query := _query || ' WHERE OL.id_city = ' || _id;
		END IF;
		_query := _query || ' ORDER BY OL.type, YR.id DESC';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.comment;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'OL';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Olympic Rankings]
	IF (_entity ~ 'OL|CN' AND (_entity_ref = 'OR' OR _entity_ref = '')) THEN
		_query := 'SELECT OR_.id, OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', OR_.count_gold || '','' || OR_.count_silver || '','' || OR_.count_bronze FROM "OLYMPIC_RANKING" OR_';
		_query := _query || ' LEFT JOIN "OLYMPICS" OL ON OR_.id_olympics = OL.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON OR_.id_country = CN.id';
		IF _entity = 'OL' THEN
			_query := _query || ' WHERE OR_.id_olympics = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query := _query || ' WHERE OR_.id_country = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id DESC';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.comment;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'OR';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Records]
	IF (_entity ~ 'CN|PR|TM|CP|EV|CT|SP' AND (_entity_ref = 'RC' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT RC.id, RC.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', RC.type1, RC.type2, RC.id_rank1, RC.id_rank2, RC.id_rank3, RC.id_rank4, RC.id_rank5 FROM "RECORD" RC';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RC.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RC.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RC.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "TYPE" TP ON EV.id_type = TP.id';
		_query := _query || ' WHERE TP.number BETWEEN ' || _type1 || ' AND ' || _type2;
		IF _entity ~ 'CN|TM' THEN
			_query := _query || ' AND (RC.id_rank1 = ' || _id || ' OR RC.id_rank2 = ' || _id || ' OR RC.id_rank3 = ' || _id || ' OR RC.id_rank4 = ' || _id || ' OR RC.id_rank5 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RC.id_rank1 in (' || _pr_list || ') OR RC.id_rank2 in (' || _pr_list || ') OR RC.id_rank3 in (' || _pr_list || ') OR RC.id_rank4 in (' || _pr_list || ') OR RC.id_rank5 in (' || _pr_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RC.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RC.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RC.id_event = ' || _id || ' OR RC.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND RC.id_city = ' || _id;
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', RC.index';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.txt1, _item.txt2, _id1, _id2, _id3, _id4, _id5;
			EXIT WHEN NOT FOUND;
			IF _entity ~ 'CN|PR|TM' THEN
				IF _id1 = _id THEN _item.comment = '1';
				ELSIF _id2 = _id THEN _item.comment = '2';
				ELSIF _id3 = _id THEN _item.comment = '3';
				ELSIF _id4 = _id THEN _item.comment = '4';
				ELSIF _id5 = _id THEN _item.comment = '5'; END IF;
			END IF;
			_item.id = _index;
			_item.entity = 'RC';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Hall of Fame]
	IF (_entity ~ 'YR|PR' AND (_entity_ref = 'HF' OR _entity_ref = '')) THEN
		_query := 'SELECT HF.id, YR.id, YR.label, PR.id, PR.last_name || '', '' || PR.first_name, LG.id, LG.label, HF.position FROM "HALL_OF_FAME" HF';
		_query := _query || ' LEFT JOIN "YEAR" YR ON HF.id_year = YR.id';
		_query := _query || ' LEFT JOIN "PERSON" PR ON HF.id_person = PR.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON HF.id_league = LG.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE HF.id_year = ' || _id;
		ELSIF _entity = 'PR' THEN
			_query := _query || ' WHERE HF.id_person in (' || _pr_list || ')';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.comment, _item.txt1;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'HF';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Retired Numbers]
	IF (_entity ~ 'TM|PR' AND (_entity_ref = 'RN' OR _entity_ref = '')) THEN
		_query := 'SELECT RN.id, TM.id, TM.label, PR.id, PR.last_name || '', '' || PR.first_name, LG.label, RN.number FROM "RETIRED_NUMBER" RN';
		_query := _query || ' LEFT JOIN "TEAM" TM ON RN.id_team = TM.id';
		_query := _query || ' LEFT JOIN "PERSON" PR ON RN.id_person = PR.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON RN.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE RN.id_team = ' || _id;
		ELSIF _entity = 'PR' THEN
			_query := _query || ' WHERE RN.id_person in (' || _pr_list || ')';
		END IF;
		_query := _query || ' ORDER BY TM.label, RN.number';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.comment, _item.id_rel3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'RN';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Team Stadiums]
	IF (_entity ~ 'TM|CX' AND (_entity_ref = 'TS' OR _entity_ref = '')) THEN
		_query := 'SELECT TS.id, TM.id, TM.label, CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', LG.label, TS.date1, TS.date2 FROM "TEAM_STADIUM" TS';
		_query := _query || ' LEFT JOIN "TEAM" TM ON TS.id_team = TM.id';
		_query := _query || ' LEFT JOIN "COMPLEX" CX ON TS.id_complex = CX.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON TS.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE TS.id_team = ' || _id;
		ELSIF _entity = 'CX' THEN
			_query := _query || ' WHERE TS.id_complex = ' || _id;
		END IF;
		_query := _query || ' ORDER BY TM.label, TS.date1 DESC';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'TS';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Wins/Losses]
	IF (_entity = 'TM' AND (_entity_ref = 'WL' OR _entity_ref = '')) THEN
		_query := 'SELECT WL.id, TM.id, TM.label, LG.label, WL.type, WL.count_win || '','' || WL.count_loss FROM "WIN_LOSS" WL';
		_query := _query || ' LEFT JOIN "TEAM" TM ON WL.id_team = TM.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON WL.id_league = LG.id';
		_query := _query || ' WHERE WL.id_team = ' || _id;
		_query := _query || ' ORDER BY TM.label DESC';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'WL';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;
	
	RETURN;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
  
CREATE OR REPLACE FUNCTION "SEARCH"(_pattern character varying, _scope character varying, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_index smallint;
	_current_id integer;
	_current_label varchar(100);
	_current_id_rel1 integer;
	_current_id_rel2 integer;
	_current_id_rel3 integer;
	_current_label_rel1 varchar(50);
	_current_label_rel2 varchar(50);
	_current_label_rel3 varchar(50);
	_current_link integer;
	_current_ref smallint;
	_scopes varchar(2)[];
	_tables varchar(15)[];
	_label varchar(10);
	_i smallint;
	_s varchar(2);
	_c refcursor;
	_query text;
	_rel_cols text;
	_rel_joins text;
	_rel_count smallint;
	__pattern text;
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'SC', _pattern, current_date);
	
	_i := 1;
	_index := 1;
	__pattern := lower(_pattern);
	__pattern := replace(__pattern, 'a', '(a|á|Á|à|ä|Ä|ă|ā|ã|å|Å|â)');
	__pattern := replace(__pattern, 'ae', '(ae|æ)');
	__pattern := replace(__pattern, 'c', '(c|ć|č|ç|Č)');
	__pattern := replace(__pattern, 'dj', '(dj|Đ|đ)');
	__pattern := replace(__pattern, 'e', '(e|ė|é|É|è|ê|ë|ě|ę|ē)');
	__pattern := replace(__pattern, 'g', '(g|ğ)');
	__pattern := replace(__pattern, 'i', '(i|ı|í|ï)');
	__pattern := replace(__pattern, 'l', '(l|ł)');
	__pattern := replace(__pattern, 'n', '(n|ń|ñ)');
	__pattern := replace(__pattern, 'o', '(o|ó|ò|ö|Ö|ō|ø|Ø)');
	__pattern := replace(__pattern, 'r', '(r|ř)');
	__pattern := replace(__pattern, 's', '(s|ś|š|Š|ş|Ş)');
	__pattern := replace(__pattern, 'ss', '(ss|ß)');
	__pattern := replace(__pattern, 't', '(t|ţ)');
	__pattern := replace(__pattern, 'u', '(u|ū|ú|ü)');
	__pattern := replace(__pattern, 'y', '(y|ý)');
	__pattern := replace(__pattern, 'z', '(z|ż|ź|ž|Ž)');
	_scopes = '{PR,CT,CX,CN,CP,EV,SP,TM,ST,YR}';
	_tables = '{PERSON,CITY,COMPLEX,COUNTRY,CHAMPIONSHIP,EVENT,SPORT,TEAM,STATE,YEAR}';
	FOR _s IN SELECT UNNEST(_scopes) LOOP
		IF _scope ~ ('(^|,)' || _s || '($|,)') THEN
			_rel_cols := '';
			_rel_joins := '';
			_rel_count := 0;

			-- Get related fields
			IF (_s ~ 'PR|TM') THEN -- Relation: Country
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang || ' || '' ('' || CN.code || '')''';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s ~ 'PR|TM') THEN -- Relation: Sport
				_rel_cols := _rel_cols || ', SP.id, SP.label' || _lang;
				_rel_joins := _rel_joins || ' LEFT JOIN "SPORT" SP ON ' || _s || '.id_sport = SP.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'PR') THEN -- Relation: Team
				_rel_cols := _rel_cols || ', TM.id, TM.label, PR.link';
				_rel_joins := _rel_joins || ' LEFT JOIN "TEAM" TM ON ' || _s || '.id_team = TM.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'CX') THEN -- Relation: City/State/Country
				_rel_cols := _rel_cols || ', CT.id, CT.label' || _lang;
				_rel_cols := _rel_cols || ', ST.id, ST.label' || _lang;
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang;
				_rel_joins := _rel_joins || ' LEFT JOIN "CITY" CT ON ' || _s || '.id_city = CT.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			IF (_s = 'CT') THEN -- Relation: State/Country
				_rel_cols := _rel_cols || ', NULL, NULL';
				_rel_cols := _rel_cols || ', ST.id, ST.label' || _lang;
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang;
				_rel_joins := _rel_joins || ' LEFT JOIN "STATE" ST ON ' || _s || '.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			FOR _j IN 1.._rel_count LOOP
				_rel_cols := _rel_cols || ', NULL, NULL';
			END LOOP;

			-- Execute query
			_label := 'label';
			IF (_s <> 'TM' AND _s <> 'YR') THEN
				_label := 'label' || _lang;
			END IF;
			_query := 'SELECT ' || _s || '.id, ' || _s || '.' || _label || ',' || _s || '.ref' || _rel_cols || ' FROM "' || _tables[_i] || '" ' || _s;
			_query := _query || _rel_joins || ' WHERE ' || _s || '.' || _label || ' ~* ''' || __pattern || ''' ORDER BY ' || _s || '.' || _label;
			IF _s = 'PR' THEN
				_query := 'SELECT PR.id, UPPER(PR.last_name) || '', '' || PR.first_name, PR.ref' || _rel_cols || ' FROM "PERSON" PR' || _rel_joins;
				_query := _query || ' WHERE (PR.link = 0 OR PR.link IS NULL) AND (PR.last_name || '' '' || PR.first_name ~* ''' || __pattern || ''' OR PR.first_name || '' '' || PR.last_name ~* ''' || __pattern || ''' OR PR.last_name ~* ''' || __pattern || ''' OR PR.first_name ~* ''' || __pattern || ''')';
				_query := _query || ' ORDER BY PR.last_name, PR.first_name';
			END IF;
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _current_id, _current_label, _current_ref, _current_id_rel1, _current_label_rel1, _current_id_rel2, _current_label_rel2, _current_id_rel3, _current_label_rel3, _current_link;
				EXIT WHEN NOT FOUND;
				_item.id = _index;
				_item.id_item = _current_id;
				_item.label = _current_label;
				_item.entity = _s;
				_item.count_ref = _current_ref;
				_item.id_rel1 = _current_id_rel1;
				_item.id_rel2 = _current_id_rel2;
				_item.id_rel3 = _current_id_rel3;
				_item.label_rel1 = _current_label_rel1;
				_item.label_rel2 = _current_label_rel2;
				_item.label_rel3 = _current_label_rel3;
				_item.link = _current_link;
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
  
DROP FUNCTION "GET_MEDAL_COUNT"(character varying, integer, integer);
CREATE OR REPLACE FUNCTION "GET_MEDAL_COUNT"(_entity character varying, _id_sport integer, _idlist varchar)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_ids integer[];
	_type integer;
	_sport_txt varchar(3);
	_index smallint;
	_nfl_cp1 smallint;
	_nfl_cp2 smallint;
	_nba_cp smallint;
	_nhl_cp smallint;
	_mlb_cp smallint;
begin
	_ids := regexp_split_to_array(_idlist, E'\\-');	
	_nfl_cp1 := 454;
	_nfl_cp2 := 453;
	_nba_cp := 530;
	_nhl_cp := 573;
	_mlb_cp := 624;
	_index := 1;
	_sport_txt := CAST(_id_sport AS varchar);
	IF (_entity = 'PR') THEN
		_type := 10;
	ELSIF (_entity = 'TM') THEN
		_type := 50;
	ELSIF (_entity = 'CN') THEN
		_type := 99;
	END IF;
	-- Olympics
	IF _sport_txt !~ '20' AND _type <> 50 THEN
		_item.id := _index;
		_item.label := 'OLYMP';
		_item.txt1 := '#GOLD#';
		_item.txt2 := '#SILVER#';
		_item.txt3 := '#BRONZE#';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank1 = ANY(_ids) OR (id_rank2 = ANY(_ids) AND exa ~* '.*1-(2|3|4|5|6).*') OR (id_rank3 = ANY(_ids) AND exa ~* '.*1-(3|4|5|6).*'));
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank2 = ANY(_ids) OR (id_rank3 = ANY(_ids) AND exa ~* '.*2-(3|4|5|6).*') OR (id_rank4 = ANY(_ids) AND exa ~* '.*2-(4|5|6).*'));
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank3 = ANY(_ids) OR (id_rank4 = ANY(_ids) AND exa ~* '.*3-(4|5|6).*') OR (id_rank5 = ANY(_ids) AND exa ~* '.*3-(5|6).*'));
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- World Championships
	IF _sport_txt !~ '20|22' AND _type <> 50 THEN
		_item.id := _index;	
		_item.label := 'WORLDCP';
		_item.txt1 := '#GOLD#';
		_item.txt2 := '#SILVER#';
		_item.txt3 := '#BRONZE#';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=3 AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank1 = ANY(_ids) OR (id_rank2 = ANY(_ids) AND exa ~* '.*1-(2|3|4|5|6).*') OR (id_rank3 = ANY(_ids) AND exa ~* '.*1-(3|4|5|6).*'));
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=3 AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank2 = ANY(_ids) OR (id_rank3 = ANY(_ids) AND exa ~* '.*2-(3|4|5|6).*') OR (id_rank4 = ANY(_ids) AND exa ~* '.*2-(4|5|6).*'));
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=3 AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank3 = ANY(_ids) OR (id_rank4 = ANY(_ids) AND exa ~* '.*3-(4|5|6).*') OR (id_rank5 = ANY(_ids) AND exa ~* '.*3-(5|6).*'));
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Grand Slam (Tennis)
	IF _id_sport = 22 AND _type <> 50 THEN
		_item.id := _index;
		_item.label := 'GSLAM';
		_item.txt1 := 'Aus';
		_item.txt2 := 'RG';
		_item.txt3 := 'Wim';
		_item.txt4 := 'US';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=35 AND SE.label='Australian Open' AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=35 AND SE.label='French Open' AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=35 AND SE.label='British Open' AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count4 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=35 AND SE.label='US Open' AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Majors (Golf)
	IF _id_sport = 20 AND _type <> 50 THEN
		_item.id := _index;
		_item.label := 'MAJORS';
		_item.txt1 := 'Mas';
		_item.txt2 := 'US';
		_item.txt3 := 'Brit';
		_item.txt4 := 'PGA';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=69 AND SE.label='Masters' AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=69 AND SE.label='US Open' AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=69 AND SE.label='British Open' AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count4 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=69 AND SE.label='PGA Championship' AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- NFL Titles
	IF _id_sport = 23 AND _type = 50 THEN
		-- (Super Bowl)
		_item.id := _index;
		_item.label := 'NFLCP1';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp1 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp1 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp1) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
		-- (NFL Championships)
		_item.id := _index;
		_item.label := 'NFLCP2';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp2 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp2 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp2) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	-- NBA Titles
	IF _id_sport = 24 AND _type = 50 THEN
		_item.id := _index;
		_item.label := 'NBACP';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nba_cp AND id_subevent2 IS NULL) OR id_subevent2=_nba_cp) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nba_cp AND id_subevent2 IS NULL) OR id_subevent2=_nba_cp) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	-- NHL Titles
	IF _id_sport = 25 AND _type = 50 THEN
		_item.id := _index;
		_item.label := 'NHLCP';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nhl_cp AND id_subevent2 IS NULL) OR id_subevent2=_nhl_cp) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nhl_cp AND id_subevent2 IS NULL) OR id_subevent2=_nhl_cp) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	-- MLB Titles
	IF _id_sport = 26 AND _type = 50 THEN
		_item.id := _index;
		_item.label := 'MLBCP';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_mlb_cp AND id_subevent2 IS NULL) OR id_subevent2=_mlb_cp) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_mlb_cp AND id_subevent2 IS NULL) OR id_subevent2=_mlb_cp) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	RETURN;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION "GET_MEDAL_COUNT"(character varying, integer, varchar) OWNER TO inachos;


CREATE OR REPLACE FUNCTION "ENTITY_REF"(_entity character varying, _id integer, _entity_ref character varying, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_c refcursor;
	_query text;
	_link integer;
	_pr_list varchar(50);
	_index integer;
	_type1 integer;
	_type2 integer;
	_type3 integer;
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
	_cn1 varchar(5);_cn2 varchar(5);_cn3 varchar(5);_cn4 varchar(5);_cn5 varchar(5);_cn6 varchar(5);
	_tm1 varchar(60);_tm2 varchar(60);_tm3 varchar(60);_tm4 varchar(60);_tm5 varchar(60);_tm6 varchar(60);
	_LIMIT smallint;
begin
	_LIMIT := 20;
	
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'IF', _entity || '-' || _id, current_date);
	
	_index := 1;

	IF _entity ~ 'PR' THEN
		SELECT LINK INTO _link FROM "PERSON" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "PERSON" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_pr_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_pr_list := _pr_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_pr_list := cast(_id AS varchar);
		END IF;
	END IF;

	-- References in: [Results]
	IF (_entity ~ 'CN|PR|TM|CP|EV|CT|SP|CX|OL|YR' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number, TP3.number FROM "RESULT" RS';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id';
		_query := _query || ' LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id';
		IF (_entity = 'OL') THEN
			_query := _query || ' LEFT JOIN "OLYMPICS" OL ON (OL.id_year = YR.id AND OL.type = SP.type)';
		END IF;
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity ~ 'CN|TM' THEN
			_query := _query || ' AND (RS.id_rank1 = ' || _id || ' OR RS.id_rank2 = ' || _id || ' OR RS.id_rank3 = ' || _id || ' OR RS.id_rank4 = ' || _id || ' OR RS.id_rank5 = ' || _id || ' OR RS.id_rank6 = ' || _id || ' OR RS.id_rank7 = ' || _id || ' OR RS.id_rank8 = ' || _id || ' OR RS.id_rank9 = ' || _id || ' OR RS.id_rank10 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RS.id_rank1 in (' || _pr_list || ') OR RS.id_rank2 in (' || _pr_list || ') OR RS.id_rank3 in (' || _pr_list || ') OR RS.id_rank4 in (' || _pr_list || ') OR RS.id_rank5 in (' || _pr_list || ') OR RS.id_rank6 in (' || _pr_list || ') OR RS.id_rank7 in (' || _pr_list || ') OR RS.id_rank8 in (' || _pr_list || ') OR RS.id_rank9 in (' || _pr_list || ') OR RS.id_rank10 in (' || _pr_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND  (RS.id_city1 = ' || _id || ' OR RS.id_city2 = ' || _id || ')';
		ELSIF _entity = 'CX' THEN
			_query := _query || ' AND  (RS.id_complex1 = ' || _id || ' OR RS.id_complex2 = ' || _id || ')';
		ELSIF _entity = 'OL' THEN
			_query := _query || ' AND OL.id = ' || _id;
		ELSIF _entity = 'YR' THEN
			_query := _query || ' AND RS.id_year = ' || _id;
		END IF;
		--_query := _query || ' ORDER BY SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', YR.id';
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _LIMIT;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF _type1 <= 10 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || ', ' || PR1.first_name, CN1.id, CN1.code, TM1.label, PR2.last_name || ', ' || PR2.first_name, CN2.id, CN2.code, TM2.label, PR3.last_name || ', ' || PR3.first_name, CN3.id, CN3.code, TM3.label, PR4.last_name || ', ' || PR4.first_name, CN4.id, CN4.code, TM4.label, PR5.last_name || ', ' || PR5.first_name, CN5.id, CN5.code, TM5.label, PR6.last_name || ', ' || PR6.first_name, CN6.id, CN6.code, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "PERSON" PR1 ON RS.id_rank1 = PR1.id LEFT JOIN "PERSON" PR2 ON RS.id_rank2 = PR2.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3 = PR3.id LEFT JOIN "PERSON" PR4 ON RS.id_rank4 = PR4.id LEFT JOIN "PERSON" PR5 ON RS.id_rank5 = PR5.id LEFT JOIN "PERSON" PR6 ON RS.id_rank6 = PR6.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "COUNTRY" CN3 ON PR3.id_country = CN3.id LEFT JOIN "COUNTRY" CN4 ON PR4.id_country = CN4.id LEFT JOIN "COUNTRY" CN5 ON PR5.id_country = CN5.id LEFT JOIN "COUNTRY" CN6 ON PR6.id_country = CN6.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id LEFT JOIN "TEAM" TM3 ON PR3.id_team = TM3.id LEFT JOIN "TEAM" TM4 ON PR4.id_team = TM4.id LEFT JOIN "TEAM" TM5 ON PR5.id_team = TM5.id LEFT JOIN "TEAM" TM6 ON PR6.id_team = TM6.id
				WHERE RS.id = _item.id_item;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				IF _cn3 IS NOT NULL THEN _item.label_rel8 := _item.label_rel8 || ' (' || _cn3 || ')';
				ELSIF _tm3 IS NOT NULL THEN _item.label_rel8 := _item.label_rel8 || ' (' || _tm3 || ')'; END IF;
				IF _cn4 IS NOT NULL THEN _item.label_rel9 := _item.label_rel9 || ' (' || _cn4 || ')';
				ELSIF _tm4 IS NOT NULL THEN _item.label_rel9 := _item.label_rel9 || ' (' || _tm4 || ')'; END IF;
				IF _cn5 IS NOT NULL THEN _item.label_rel10 := _item.label_rel10 || ' (' || _cn5 || ')';
				ELSIF _tm5 IS NOT NULL THEN _item.label_rel10 := _item.label_rel10 || ' (' || _tm5 || ')'; END IF;
				IF _cn6 IS NOT NULL THEN _item.label_rel11 := _item.label_rel11 || ' (' || _cn6 || ')';
				ELSIF _tm6 IS NOT NULL THEN _item.label_rel11 := _item.label_rel11 || ' (' || _tm6 || ')'; END IF;
				IF _type1 = 4 THEN
					_item.txt4 = '1-2/3-4/5-6';
				END IF;
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "TEAM" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "TEAM" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "TEAM" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "TEAM" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "TEAM" TM5 ON RS.id_rank5 = TM5.id LEFT JOIN "TEAM" TM6 ON RS.id_rank6 = TM6.id
				WHERE RS.id = _item.id_item;
			ELSIF _type1 = 99 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.code, CN2.code, CN3.code, CN4.code, CN5.code, CN6.code, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "COUNTRY" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "COUNTRY" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "COUNTRY" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "COUNTRY" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "COUNTRY" CN5 ON RS.id_rank5 = CN5.id LEFT JOIN "COUNTRY" CN6 ON RS.id_rank6 = CN6.id
				WHERE RS.id = _item.id_item;
			END IF;
			_item.id = _index;
			_item.entity = 'RS';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;
	
	-- References in: [Athletes]
	IF (_entity ~ 'CN|SP|TM' AND (_entity_ref = 'PR' OR _entity_ref = '')) THEN
		_query := 'SELECT PR.id, PR.last_name || '', '' || PR.first_name, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', TM.id, TM.label FROM "PERSON" PR';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON PR.id_country = CN.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON PR.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "TEAM" TM ON PR.id_team = TM.id';
		IF _entity = 'CN' THEN
			_query := _query || ' WHERE PR.id_country = ' || _id;
		ELSIF _entity = 'SP' THEN
			_query := _query || ' WHERE PR.id_sport = ' || _id;
		ELSIF _entity = 'TM' THEN
			_query := _query || ' WHERE PR.id_team = ' || _id;
		END IF;
		_query := _query || ' ORDER BY PR.last_name, PR.first_name LIMIT ' || _LIMIT;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'PR';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Teams]
	IF (_entity ~ 'SP|CN' AND (_entity_ref = 'TM' OR _entity_ref = '')) THEN
		_query := 'SELECT TM.id, TM.label, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ' FROM "TEAM" TM';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON TM.id_country = CN.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON TM.id_sport = SP.id';
		IF _entity = 'SP' THEN
			_query := _query || ' WHERE TM.id_sport = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query := _query || ' WHERE TM.id_country = ' || _id;
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', TM.label LIMIT ' || _LIMIT;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'TM';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Cities]
	IF (_entity ~ 'CN|ST' AND (_entity_ref = 'CT' OR _entity_ref = '')) THEN
		_query := 'SELECT CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ' FROM "CITY" CT';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'CN' THEN
			_query := _query || ' WHERE CT.id_country = ' || _id;
		ELSIF _entity = 'ST' THEN
			_query := _query || ' WHERE CT.id_state = ' || _id;
		END IF;
		_query := _query || ' ORDER BY CT.label' || _lang || ' LIMIT ' || _LIMIT;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CT';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Complexes]
	IF (_entity = 'CT' AND (_entity_ref = 'CX' OR _entity_ref = '')) THEN
		_query := 'SELECT CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ' FROM "COMPLEX" CX';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' WHERE CX.id_city = ' || _id;
		_query := _query || ' ORDER BY CX.label' || _lang || ' LIMIT ' || _LIMIT;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CX';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Olympics]
	IF (_entity ~ 'YR|CT' AND (_entity_ref = 'OL' OR _entity_ref = '')) THEN
		_query := 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', OL.type FROM "OLYMPICS" OL';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query := _query || ' WHERE OL.id_city = ' || _id;
		END IF;
		_query := _query || ' ORDER BY OL.type, YR.id DESC LIMIT ' || _LIMIT;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.comment;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'OL';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Olympic Rankings]
	IF (_entity ~ 'OL|CN' AND (_entity_ref = 'OR' OR _entity_ref = '')) THEN
		_query := 'SELECT OR_.id, OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', OR_.count_gold || '','' || OR_.count_silver || '','' || OR_.count_bronze FROM "OLYMPIC_RANKING" OR_';
		_query := _query || ' LEFT JOIN "OLYMPICS" OL ON OR_.id_olympics = OL.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON OR_.id_country = CN.id';
		IF _entity = 'OL' THEN
			_query := _query || ' WHERE OR_.id_olympics = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query := _query || ' WHERE OR_.id_country = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id DESC LIMIT ' || _LIMIT;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.comment;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'OR';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Records]
	IF (_entity ~ 'CN|PR|TM|CP|EV|CT|SP' AND (_entity_ref = 'RC' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT RC.id, RC.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', RC.type1, RC.type2, RC.id_rank1, RC.id_rank2, RC.id_rank3, RC.id_rank4, RC.id_rank5 FROM "RECORD" RC';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RC.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RC.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RC.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "TYPE" TP ON EV.id_type = TP.id';
		_query := _query || ' WHERE TP.number BETWEEN ' || _type1 || ' AND ' || _type2;
		IF _entity ~ 'CN|TM' THEN
			_query := _query || ' AND (RC.id_rank1 = ' || _id || ' OR RC.id_rank2 = ' || _id || ' OR RC.id_rank3 = ' || _id || ' OR RC.id_rank4 = ' || _id || ' OR RC.id_rank5 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RC.id_rank1 in (' || _pr_list || ') OR RC.id_rank2 in (' || _pr_list || ') OR RC.id_rank3 in (' || _pr_list || ') OR RC.id_rank4 in (' || _pr_list || ') OR RC.id_rank5 in (' || _pr_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RC.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RC.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RC.id_event = ' || _id || ' OR RC.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND RC.id_city = ' || _id;
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', RC.index LIMIT ' || _LIMIT;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.txt1, _item.txt2, _id1, _id2, _id3, _id4, _id5;
			EXIT WHEN NOT FOUND;
			IF _entity ~ 'CN|PR|TM' THEN
				IF _id1 = _id THEN _item.comment = '1';
				ELSIF _id2 = _id THEN _item.comment = '2';
				ELSIF _id3 = _id THEN _item.comment = '3';
				ELSIF _id4 = _id THEN _item.comment = '4';
				ELSIF _id5 = _id THEN _item.comment = '5'; END IF;
			END IF;
			_item.id = _index;
			_item.entity = 'RC';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Hall of Fame]
	IF (_entity ~ 'YR|PR' AND (_entity_ref = 'HF' OR _entity_ref = '')) THEN
		_query := 'SELECT HF.id, YR.id, YR.label, PR.id, PR.last_name || '', '' || PR.first_name, LG.id, LG.label, HF.position FROM "HALL_OF_FAME" HF';
		_query := _query || ' LEFT JOIN "YEAR" YR ON HF.id_year = YR.id';
		_query := _query || ' LEFT JOIN "PERSON" PR ON HF.id_person = PR.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON HF.id_league = LG.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE HF.id_year = ' || _id;
		ELSIF _entity = 'PR' THEN
			_query := _query || ' WHERE HF.id_person in (' || _pr_list || ')';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC LIMIT ' || _LIMIT;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.comment, _item.txt1;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'HF';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Retired Numbers]
	IF (_entity ~ 'TM|PR' AND (_entity_ref = 'RN' OR _entity_ref = '')) THEN
		_query := 'SELECT RN.id, TM.id, TM.label, PR.id, PR.last_name || '', '' || PR.first_name, LG.label, RN.number FROM "RETIRED_NUMBER" RN';
		_query := _query || ' LEFT JOIN "TEAM" TM ON RN.id_team = TM.id';
		_query := _query || ' LEFT JOIN "PERSON" PR ON RN.id_person = PR.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON RN.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE RN.id_team = ' || _id;
		ELSIF _entity = 'PR' THEN
			_query := _query || ' WHERE RN.id_person in (' || _pr_list || ')';
		END IF;
		_query := _query || ' ORDER BY TM.label, RN.number LIMIT ' || _LIMIT;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.comment, _item.id_rel3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'RN';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Team Stadiums]
	IF (_entity ~ 'TM|CX' AND (_entity_ref = 'TS' OR _entity_ref = '')) THEN
		_query := 'SELECT TS.id, TM.id, TM.label, CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', LG.label, TS.date1, TS.date2 FROM "TEAM_STADIUM" TS';
		_query := _query || ' LEFT JOIN "TEAM" TM ON TS.id_team = TM.id';
		_query := _query || ' LEFT JOIN "COMPLEX" CX ON TS.id_complex = CX.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON TS.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE TS.id_team = ' || _id;
		ELSIF _entity = 'CX' THEN
			_query := _query || ' WHERE TS.id_complex = ' || _id;
		END IF;
		_query := _query || ' ORDER BY TM.label, TS.date1 DESC LIMIT ' || _LIMIT;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'TS';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Wins/Losses]
	IF (_entity = 'TM' AND (_entity_ref = 'WL' OR _entity_ref = '')) THEN
		_query := 'SELECT WL.id, TM.id, TM.label, LG.label, WL.type, WL.count_win || '','' || WL.count_loss FROM "WIN_LOSS" WL';
		_query := _query || ' LEFT JOIN "TEAM" TM ON WL.id_team = TM.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON WL.id_league = LG.id';
		_query := _query || ' WHERE WL.id_team = ' || _id;
		_query := _query || ' ORDER BY TM.label DESC LIMIT ' || _LIMIT;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'WL';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;
	
	RETURN;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;


CREATE OR REPLACE FUNCTION "ENTITY_REF"(_entity character varying, _id integer, _entity_ref character varying, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_c refcursor;
	_query text;
	_link integer;
	_pr_list varchar(50);
	_index integer;
	_type1 integer;
	_type2 integer;
	_type3 integer;
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
	_cn1 varchar(5);_cn2 varchar(5);_cn3 varchar(5);_cn4 varchar(5);_cn5 varchar(5);_cn6 varchar(5);
	_tm1 varchar(60);_tm2 varchar(60);_tm3 varchar(60);_tm4 varchar(60);_tm5 varchar(60);_tm6 varchar(60);
	_limit varchar(3);
begin
	_limit := CASE _entity_ref WHEN '' THEN '10' ELSE 'ALL' END;
	
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'IF', _entity || '-' || _id, current_date);
	
	_index := 1;

	IF _entity ~ 'PR' THEN
		SELECT LINK INTO _link FROM "PERSON" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "PERSON" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_pr_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_pr_list := _pr_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_pr_list := cast(_id AS varchar);
		END IF;
	END IF;

	-- References in: [Results]
	IF (_entity ~ 'CN|PR|TM|CP|EV|CT|SP|CX|OL|YR' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number, TP3.number FROM "RESULT" RS';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id';
		_query := _query || ' LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id';
		IF (_entity = 'OL') THEN
			_query := _query || ' LEFT JOIN "OLYMPICS" OL ON (OL.id_year = YR.id AND OL.type = SP.type)';
		END IF;
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity ~ 'CN|TM' THEN
			_query := _query || ' AND (RS.id_rank1 = ' || _id || ' OR RS.id_rank2 = ' || _id || ' OR RS.id_rank3 = ' || _id || ' OR RS.id_rank4 = ' || _id || ' OR RS.id_rank5 = ' || _id || ' OR RS.id_rank6 = ' || _id || ' OR RS.id_rank7 = ' || _id || ' OR RS.id_rank8 = ' || _id || ' OR RS.id_rank9 = ' || _id || ' OR RS.id_rank10 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RS.id_rank1 in (' || _pr_list || ') OR RS.id_rank2 in (' || _pr_list || ') OR RS.id_rank3 in (' || _pr_list || ') OR RS.id_rank4 in (' || _pr_list || ') OR RS.id_rank5 in (' || _pr_list || ') OR RS.id_rank6 in (' || _pr_list || ') OR RS.id_rank7 in (' || _pr_list || ') OR RS.id_rank8 in (' || _pr_list || ') OR RS.id_rank9 in (' || _pr_list || ') OR RS.id_rank10 in (' || _pr_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND  (RS.id_city1 = ' || _id || ' OR RS.id_city2 = ' || _id || ')';
		ELSIF _entity = 'CX' THEN
			_query := _query || ' AND  (RS.id_complex1 = ' || _id || ' OR RS.id_complex2 = ' || _id || ')';
		ELSIF _entity = 'OL' THEN
			_query := _query || ' AND OL.id = ' || _id;
		ELSIF _entity = 'YR' THEN
			_query := _query || ' AND RS.id_year = ' || _id;
		END IF;
		--_query := _query || ' ORDER BY SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', YR.id';
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF _type1 <= 10 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || ', ' || PR1.first_name, CN1.id, CN1.code, TM1.label, PR2.last_name || ', ' || PR2.first_name, CN2.id, CN2.code, TM2.label, PR3.last_name || ', ' || PR3.first_name, CN3.id, CN3.code, TM3.label, PR4.last_name || ', ' || PR4.first_name, CN4.id, CN4.code, TM4.label, PR5.last_name || ', ' || PR5.first_name, CN5.id, CN5.code, TM5.label, PR6.last_name || ', ' || PR6.first_name, CN6.id, CN6.code, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "PERSON" PR1 ON RS.id_rank1 = PR1.id LEFT JOIN "PERSON" PR2 ON RS.id_rank2 = PR2.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3 = PR3.id LEFT JOIN "PERSON" PR4 ON RS.id_rank4 = PR4.id LEFT JOIN "PERSON" PR5 ON RS.id_rank5 = PR5.id LEFT JOIN "PERSON" PR6 ON RS.id_rank6 = PR6.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "COUNTRY" CN3 ON PR3.id_country = CN3.id LEFT JOIN "COUNTRY" CN4 ON PR4.id_country = CN4.id LEFT JOIN "COUNTRY" CN5 ON PR5.id_country = CN5.id LEFT JOIN "COUNTRY" CN6 ON PR6.id_country = CN6.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id LEFT JOIN "TEAM" TM3 ON PR3.id_team = TM3.id LEFT JOIN "TEAM" TM4 ON PR4.id_team = TM4.id LEFT JOIN "TEAM" TM5 ON PR5.id_team = TM5.id LEFT JOIN "TEAM" TM6 ON PR6.id_team = TM6.id
				WHERE RS.id = _item.id_item;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				IF _cn3 IS NOT NULL THEN _item.label_rel8 := _item.label_rel8 || ' (' || _cn3 || ')';
				ELSIF _tm3 IS NOT NULL THEN _item.label_rel8 := _item.label_rel8 || ' (' || _tm3 || ')'; END IF;
				IF _cn4 IS NOT NULL THEN _item.label_rel9 := _item.label_rel9 || ' (' || _cn4 || ')';
				ELSIF _tm4 IS NOT NULL THEN _item.label_rel9 := _item.label_rel9 || ' (' || _tm4 || ')'; END IF;
				IF _cn5 IS NOT NULL THEN _item.label_rel10 := _item.label_rel10 || ' (' || _cn5 || ')';
				ELSIF _tm5 IS NOT NULL THEN _item.label_rel10 := _item.label_rel10 || ' (' || _tm5 || ')'; END IF;
				IF _cn6 IS NOT NULL THEN _item.label_rel11 := _item.label_rel11 || ' (' || _cn6 || ')';
				ELSIF _tm6 IS NOT NULL THEN _item.label_rel11 := _item.label_rel11 || ' (' || _tm6 || ')'; END IF;
				IF _type1 = 4 THEN
					_item.txt4 = '1-2/3-4/5-6';
				END IF;
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "TEAM" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "TEAM" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "TEAM" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "TEAM" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "TEAM" TM5 ON RS.id_rank5 = TM5.id LEFT JOIN "TEAM" TM6 ON RS.id_rank6 = TM6.id
				WHERE RS.id = _item.id_item;
			ELSIF _type1 = 99 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.code, CN2.code, CN3.code, CN4.code, CN5.code, CN6.code, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "COUNTRY" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "COUNTRY" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "COUNTRY" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "COUNTRY" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "COUNTRY" CN5 ON RS.id_rank5 = CN5.id LEFT JOIN "COUNTRY" CN6 ON RS.id_rank6 = CN6.id
				WHERE RS.id = _item.id_item;
			END IF;
			_item.id = _index;
			_item.entity = 'RS';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;
	
	-- References in: [Athletes]
	IF (_entity ~ 'CN|SP|TM' AND (_entity_ref = 'PR' OR _entity_ref = '')) THEN
		_query := 'SELECT PR.id, PR.last_name || '', '' || PR.first_name, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', TM.id, TM.label FROM "PERSON" PR';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON PR.id_country = CN.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON PR.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "TEAM" TM ON PR.id_team = TM.id';
		IF _entity = 'CN' THEN
			_query := _query || ' WHERE PR.id_country = ' || _id;
		ELSIF _entity = 'SP' THEN
			_query := _query || ' WHERE PR.id_sport = ' || _id;
		ELSIF _entity = 'TM' THEN
			_query := _query || ' WHERE PR.id_team = ' || _id;
		END IF;
		_query := _query || ' ORDER BY PR.last_name, PR.first_name LIMIT ' || _limit;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'PR';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Teams]
	IF (_entity ~ 'SP|CN' AND (_entity_ref = 'TM' OR _entity_ref = '')) THEN
		_query := 'SELECT TM.id, TM.label, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ' FROM "TEAM" TM';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON TM.id_country = CN.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON TM.id_sport = SP.id';
		IF _entity = 'SP' THEN
			_query := _query || ' WHERE TM.id_sport = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query := _query || ' WHERE TM.id_country = ' || _id;
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', TM.label LIMIT ' || _limit;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'TM';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Cities]
	IF (_entity ~ 'CN|ST' AND (_entity_ref = 'CT' OR _entity_ref = '')) THEN
		_query := 'SELECT CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ' FROM "CITY" CT';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'CN' THEN
			_query := _query || ' WHERE CT.id_country = ' || _id;
		ELSIF _entity = 'ST' THEN
			_query := _query || ' WHERE CT.id_state = ' || _id;
		END IF;
		_query := _query || ' ORDER BY CT.label' || _lang || ' LIMIT ' || _limit;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CT';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Complexes]
	IF (_entity = 'CT' AND (_entity_ref = 'CX' OR _entity_ref = '')) THEN
		_query := 'SELECT CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ' FROM "COMPLEX" CX';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' WHERE CX.id_city = ' || _id;
		_query := _query || ' ORDER BY CX.label' || _lang || ' LIMIT ' || _limit;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CX';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Olympics]
	IF (_entity ~ 'YR|CT' AND (_entity_ref = 'OL' OR _entity_ref = '')) THEN
		_query := 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', OL.type FROM "OLYMPICS" OL';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query := _query || ' WHERE OL.id_city = ' || _id;
		END IF;
		_query := _query || ' ORDER BY OL.type, YR.id DESC LIMIT ' || _limit;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.comment;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'OL';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Olympic Rankings]
	IF (_entity ~ 'OL|CN' AND (_entity_ref = 'OR' OR _entity_ref = '')) THEN
		_query := 'SELECT OR_.id, OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', OR_.count_gold || '','' || OR_.count_silver || '','' || OR_.count_bronze FROM "OLYMPIC_RANKING" OR_';
		_query := _query || ' LEFT JOIN "OLYMPICS" OL ON OR_.id_olympics = OL.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON OR_.id_country = CN.id';
		IF _entity = 'OL' THEN
			_query := _query || ' WHERE OR_.id_olympics = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query := _query || ' WHERE OR_.id_country = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id DESC LIMIT ' || _limit;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.comment;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'OR';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Records]
	IF (_entity ~ 'CN|PR|TM|CP|EV|CT|SP' AND (_entity_ref = 'RC' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT RC.id, RC.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', RC.type1, RC.type2, RC.id_rank1, RC.id_rank2, RC.id_rank3, RC.id_rank4, RC.id_rank5 FROM "RECORD" RC';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RC.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RC.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RC.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "TYPE" TP ON EV.id_type = TP.id';
		_query := _query || ' WHERE TP.number BETWEEN ' || _type1 || ' AND ' || _type2;
		IF _entity ~ 'CN|TM' THEN
			_query := _query || ' AND (RC.id_rank1 = ' || _id || ' OR RC.id_rank2 = ' || _id || ' OR RC.id_rank3 = ' || _id || ' OR RC.id_rank4 = ' || _id || ' OR RC.id_rank5 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RC.id_rank1 in (' || _pr_list || ') OR RC.id_rank2 in (' || _pr_list || ') OR RC.id_rank3 in (' || _pr_list || ') OR RC.id_rank4 in (' || _pr_list || ') OR RC.id_rank5 in (' || _pr_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RC.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RC.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RC.id_event = ' || _id || ' OR RC.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND RC.id_city = ' || _id;
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', RC.index LIMIT ' || _limit;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.txt1, _item.txt2, _id1, _id2, _id3, _id4, _id5;
			EXIT WHEN NOT FOUND;
			IF _entity ~ 'CN|PR|TM' THEN
				IF _id1 = _id THEN _item.comment = '1';
				ELSIF _id2 = _id THEN _item.comment = '2';
				ELSIF _id3 = _id THEN _item.comment = '3';
				ELSIF _id4 = _id THEN _item.comment = '4';
				ELSIF _id5 = _id THEN _item.comment = '5'; END IF;
			END IF;
			_item.id = _index;
			_item.entity = 'RC';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Hall of Fame]
	IF (_entity ~ 'YR|PR' AND (_entity_ref = 'HF' OR _entity_ref = '')) THEN
		_query := 'SELECT HF.id, YR.id, YR.label, PR.id, PR.last_name || '', '' || PR.first_name, LG.id, LG.label, HF.position FROM "HALL_OF_FAME" HF';
		_query := _query || ' LEFT JOIN "YEAR" YR ON HF.id_year = YR.id';
		_query := _query || ' LEFT JOIN "PERSON" PR ON HF.id_person = PR.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON HF.id_league = LG.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE HF.id_year = ' || _id;
		ELSIF _entity = 'PR' THEN
			_query := _query || ' WHERE HF.id_person in (' || _pr_list || ')';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC LIMIT ' || _limit;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.comment, _item.txt1;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'HF';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Retired Numbers]
	IF (_entity ~ 'TM|PR' AND (_entity_ref = 'RN' OR _entity_ref = '')) THEN
		_query := 'SELECT RN.id, TM.id, TM.label, PR.id, PR.last_name || '', '' || PR.first_name, LG.label, RN.number FROM "RETIRED_NUMBER" RN';
		_query := _query || ' LEFT JOIN "TEAM" TM ON RN.id_team = TM.id';
		_query := _query || ' LEFT JOIN "PERSON" PR ON RN.id_person = PR.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON RN.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE RN.id_team = ' || _id;
		ELSIF _entity = 'PR' THEN
			_query := _query || ' WHERE RN.id_person in (' || _pr_list || ')';
		END IF;
		_query := _query || ' ORDER BY TM.label, RN.number LIMIT ' || _limit;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.comment, _item.id_rel3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'RN';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Team Stadiums]
	IF (_entity ~ 'TM|CX' AND (_entity_ref = 'TS' OR _entity_ref = '')) THEN
		_query := 'SELECT TS.id, TM.id, TM.label, CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', LG.label, TS.date1, TS.date2 FROM "TEAM_STADIUM" TS';
		_query := _query || ' LEFT JOIN "TEAM" TM ON TS.id_team = TM.id';
		_query := _query || ' LEFT JOIN "COMPLEX" CX ON TS.id_complex = CX.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON TS.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE TS.id_team = ' || _id;
		ELSIF _entity = 'CX' THEN
			_query := _query || ' WHERE TS.id_complex = ' || _id;
		END IF;
		_query := _query || ' ORDER BY TM.label, TS.date1 DESC LIMIT ' || _limit;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'TS';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Wins/Losses]
	IF (_entity = 'TM' AND (_entity_ref = 'WL' OR _entity_ref = '')) THEN
		_query := 'SELECT WL.id, TM.id, TM.label, LG.label, WL.type, WL.count_win || '','' || WL.count_loss FROM "WIN_LOSS" WL';
		_query := _query || ' LEFT JOIN "TEAM" TM ON WL.id_team = TM.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON WL.id_league = LG.id';
		_query := _query || ' WHERE WL.id_team = ' || _id;
		_query := _query || ' ORDER BY TM.label DESC LIMIT ' || _limit;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'WL';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;
	
	RETURN;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
  

  CREATE OR REPLACE FUNCTION "~MERGE"(_alias character varying, _id1 integer, _id2 integer)
  RETURNS boolean AS
$BODY$
declare
begin
	IF _alias = 'CP' THEN
		UPDATE "RESULT" SET id_championship = _id2 WHERE id_championship = _id1;
		UPDATE "RECORD" SET id_championship = _id2 WHERE id_championship = _id1;
		DELETE FROM "CHAMPIONSHIP" WHERE id = _id1;
	ELSIF _alias = 'CT' THEN
		UPDATE "COMPLEX" SET id_city = _id2 WHERE id_city = _id1;
		UPDATE "OLYMPICS" SET id_city = _id2 WHERE id_city = _id1;
		UPDATE "RESULT" SET id_city1 = _id2 WHERE id_city1 = _id1;
		UPDATE "RESULT" SET id_city2 = _id2 WHERE id_city2 = _id1;
		UPDATE "RECORD" SET id_city = _id2 WHERE id_city = _id1;
		DELETE FROM "CITY" WHERE id = _id1;
	ELSIF _alias = 'CN' THEN
		UPDATE "CITY" SET id_country = _id2 WHERE id_country = _id1;
		UPDATE "OLYMPIC_RANKING" SET id_country = _id2 WHERE id_country = _id1;
		UPDATE "PERSON" SET id_country = _id2 WHERE id_country = _id1;
		UPDATE "DRAW" SET id1_qf1 = _id2 WHERE id1_qf1 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "DRAW" SET id2_qf1 = _id2 WHERE id2_qf1 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "DRAW" SET id1_qf2 = _id2 WHERE id1_qf2 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "DRAW" SET id2_qf2 = _id2 WHERE id2_qf2 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "DRAW" SET id1_qf3 = _id2 WHERE id1_qf3 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "DRAW" SET id2_qf3 = _id2 WHERE id2_qf3 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "DRAW" SET id1_qf4 = _id2 WHERE id1_qf4 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "DRAW" SET id2_qf4 = _id2 WHERE id2_qf4 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "DRAW" SET id1_sf1 = _id2 WHERE id1_sf1 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "DRAW" SET id2_sf1 = _id2 WHERE id2_sf1 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "DRAW" SET id1_sf2 = _id2 WHERE id1_sf2 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "DRAW" SET id2_sf2 = _id2 WHERE id2_sf2 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "DRAW" SET id1_thd = _id2 WHERE id1_thd = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "DRAW" SET id2_thd = _id2 WHERE id2_thd = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "RESULT" SET id_rank1 = _id2 WHERE id_rank1 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "RESULT" SET id_rank2 = _id2 WHERE id_rank2 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "RESULT" SET id_rank3 = _id2 WHERE id_rank3 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "RESULT" SET id_rank4 = _id2 WHERE id_rank4 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "RESULT" SET id_rank5 = _id2 WHERE id_rank5 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "RESULT" SET id_rank6 = _id2 WHERE id_rank6 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "RESULT" SET id_rank7 = _id2 WHERE id_rank7 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "RESULT" SET id_rank8 = _id2 WHERE id_rank8 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "RESULT" SET id_rank9 = _id2 WHERE id_rank9 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "RESULT" SET id_rank10 = _id2 WHERE id_rank10 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE "RECORD" SET id_rank1 = _id2 WHERE id_rank1 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=99) OR (RC.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE "RECORD" SET id_rank2 = _id2 WHERE id_rank2 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=99) OR (RC.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE "RECORD" SET id_rank3 = _id2 WHERE id_rank3 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=99) OR (RC.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE "RECORD" SET id_rank4 = _id2 WHERE id_rank4 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=99) OR (RC.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE "RECORD" SET id_rank5 = _id2 WHERE id_rank5 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=99) OR (RC.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE "TEAM" SET id_country = _id2 WHERE id_country = _id1;
		DELETE FROM "COUNTRY" WHERE id = _id1;
	ELSIF _alias = 'CX' THEN
		UPDATE "RESULT" SET id_complex1 = _id2 WHERE id_complex1 = _id1;
		UPDATE "RESULT" SET id_complex2 = _id2 WHERE id_complex2 = _id1;
		UPDATE "TEAM_STADIUM" SET id_complex = _id2 WHERE id_complex = _id1;
		DELETE FROM "COMPLEX" WHERE id = _id1;
	ELSIF _alias = 'EV' THEN
		UPDATE "RESULT" SET id_event = _id2 WHERE id_event = _id1;
		UPDATE "RESULT" SET id_subevent = _id2 WHERE id_subevent = _id1;
		UPDATE "RECORD" SET id_event = _id2 WHERE id_event = _id1;
		UPDATE "RECORD" SET id_subevent = _id2 WHERE id_subevent = _id1;
		DELETE FROM "COUNTRY" WHERE id = _id1;
	ELSIF _alias = 'PR' THEN
		UPDATE "HALL_OF_FAME" SET id_person = _id2 WHERE id_person = _id1;
		UPDATE "DRAW" SET id1_qf1 = _id2 WHERE id1_qf1 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "DRAW" SET id2_qf1 = _id2 WHERE id2_qf1 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "DRAW" SET id1_qf2 = _id2 WHERE id1_qf2 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "DRAW" SET id2_qf2 = _id2 WHERE id2_qf2 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "DRAW" SET id1_qf3 = _id2 WHERE id1_qf3 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "DRAW" SET id2_qf3 = _id2 WHERE id2_qf3 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "DRAW" SET id1_qf4 = _id2 WHERE id1_qf4 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "DRAW" SET id2_qf4 = _id2 WHERE id2_qf4 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "DRAW" SET id1_sf1 = _id2 WHERE id1_sf1 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "DRAW" SET id2_sf1 = _id2 WHERE id2_sf1 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "DRAW" SET id1_sf2 = _id2 WHERE id1_sf2 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "DRAW" SET id2_sf2 = _id2 WHERE id2_sf2 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "DRAW" SET id1_thd = _id2 WHERE id1_thd = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "DRAW" SET id2_thd = _id2 WHERE id2_thd = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "RESULT" SET id_rank1 = _id2 WHERE id_rank1 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "RESULT" SET id_rank2 = _id2 WHERE id_rank2 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "RESULT" SET id_rank3 = _id2 WHERE id_rank3 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "RESULT" SET id_rank4 = _id2 WHERE id_rank4 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "RESULT" SET id_rank5 = _id2 WHERE id_rank5 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "RESULT" SET id_rank6 = _id2 WHERE id_rank6 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "RESULT" SET id_rank7 = _id2 WHERE id_rank7 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "RESULT" SET id_rank8 = _id2 WHERE id_rank8 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "RESULT" SET id_rank9 = _id2 WHERE id_rank9 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "RESULT" SET id_rank10 = _id2 WHERE id_rank10 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE "RECORD" SET id_rank1 = _id2 WHERE id_rank1 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number<10) OR (RC.id_subevent IS NOT NULL AND TP2.number<10));
		UPDATE "RECORD" SET id_rank2 = _id2 WHERE id_rank2 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number<10) OR (RC.id_subevent IS NOT NULL AND TP2.number<10));
		UPDATE "RECORD" SET id_rank3 = _id2 WHERE id_rank3 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number<10) OR (RC.id_subevent IS NOT NULL AND TP2.number<10));
		UPDATE "RECORD" SET id_rank4 = _id2 WHERE id_rank4 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number<10) OR (RC.id_subevent IS NOT NULL AND TP2.number<10));
		UPDATE "RECORD" SET id_rank5 = _id2 WHERE id_rank5 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number<10) OR (RC.id_subevent IS NOT NULL AND TP2.number<10));
		UPDATE "RETIRED_NUMBER" SET id_person = _id2 WHERE id_person = _id1;
		DELETE FROM "PERSON" WHERE id = _id1;
	ELSIF _alias = 'SP' THEN
		UPDATE "RESULT" SET id_sport = _id2 WHERE id_sport = _id1;
		UPDATE "RECORD" SET id_sport = _id2 WHERE id_sport = _id1;
		DELETE FROM "SPORT" WHERE id = _id1;
	ELSIF _alias = 'ST' THEN
		UPDATE "CITY" SET id_state = _id2 WHERE id_state = _id1;
		DELETE FROM "STATE" WHERE id = _id1;
	ELSIF _alias = 'TM' THEN
		UPDATE "DRAW" SET id1_qf1 = _id2 WHERE id1_qf1 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "DRAW" SET id2_qf1 = _id2 WHERE id2_qf1 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "DRAW" SET id1_qf2 = _id2 WHERE id1_qf2 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "DRAW" SET id2_qf2 = _id2 WHERE id2_qf2 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "DRAW" SET id1_qf3 = _id2 WHERE id1_qf3 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "DRAW" SET id2_qf3 = _id2 WHERE id2_qf3 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "DRAW" SET id1_qf4 = _id2 WHERE id1_qf4 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "DRAW" SET id2_qf4 = _id2 WHERE id2_qf4 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "DRAW" SET id1_sf1 = _id2 WHERE id1_sf1 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "DRAW" SET id2_sf1 = _id2 WHERE id2_sf1 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "DRAW" SET id1_sf2 = _id2 WHERE id1_sf2 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "DRAW" SET id2_sf2 = _id2 WHERE id2_sf2 = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "DRAW" SET id1_thd = _id2 WHERE id1_thd = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "DRAW" SET id2_thd = _id2 WHERE id2_thd = _id1 AND id_result IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "RESULT" SET id_rank1 = _id2 WHERE id_rank1 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "RESULT" SET id_rank2 = _id2 WHERE id_rank2 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "RESULT" SET id_rank3 = _id2 WHERE id_rank3 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "RESULT" SET id_rank4 = _id2 WHERE id_rank4 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "RESULT" SET id_rank5 = _id2 WHERE id_rank5 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "RESULT" SET id_rank6 = _id2 WHERE id_rank6 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "RESULT" SET id_rank7 = _id2 WHERE id_rank7 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "RESULT" SET id_rank8 = _id2 WHERE id_rank8 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "RESULT" SET id_rank9 = _id2 WHERE id_rank9 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "RESULT" SET id_rank10 = _id2 WHERE id_rank10 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE "RECORD" SET id_rank1 = _id2 WHERE id_rank1 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=50) OR (RC.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE "RECORD" SET id_rank2 = _id2 WHERE id_rank2 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=50) OR (RC.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE "RECORD" SET id_rank3 = _id2 WHERE id_rank3 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=50) OR (RC.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE "RECORD" SET id_rank4 = _id2 WHERE id_rank4 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=50) OR (RC.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE "RECORD" SET id_rank5 = _id2 WHERE id_rank5 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=50) OR (RC.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE "RETIRED_NUMBER" SET id_team = _id2 WHERE id_team = _id1;
		UPDATE "TEAM_STADIUM" SET id_team = _id2 WHERE id_team = _id1;
		UPDATE "WIN_LOSS" SET id_team = _id2 WHERE id_team = _id1;
		DELETE FROM "TEAM" WHERE id = _id1;
	END IF;
	RETURN true;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;


CREATE OR REPLACE FUNCTION "SEARCH"(_pattern character varying, _scope character varying, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_index smallint;
	_current_id integer;
	_current_label varchar(100);
	_current_id_rel1 integer;
	_current_id_rel2 integer;
	_current_id_rel3 integer;
	_current_label_rel1 varchar(50);
	_current_label_rel2 varchar(50);
	_current_label_rel3 varchar(50);
	_current_link integer;
	_current_ref smallint;
	_scopes varchar(2)[];
	_tables varchar(15)[];
	_label varchar(10);
	_i smallint;
	_s varchar(2);
	_c refcursor;
	_query text;
	_rel_cols text;
	_rel_joins text;
	_rel_count smallint;
	__pattern text;
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'SC', _pattern, current_date);
	
	_i := 1;
	_index := 1;
	__pattern := lower(_pattern);
	__pattern := replace(__pattern, 'a', '(a|á|Á|à|ä|Ä|ă|ā|ã|å|Å|â)');
	__pattern := replace(__pattern, 'ae', '(ae|æ)');
	__pattern := replace(__pattern, 'c', '(c|ć|č|ç|Č)');
	__pattern := replace(__pattern, 'dj', '(dj|Đ|đ)');
	__pattern := replace(__pattern, 'e', '(e|ė|é|É|è|ê|ë|ě|ę|ē)');
	__pattern := replace(__pattern, 'g', '(g|ğ)');
	__pattern := replace(__pattern, 'i', '(i|ı|í|ï)');
	__pattern := replace(__pattern, 'l', '(l|ł)');
	__pattern := replace(__pattern, 'n', '(n|ń|ñ)');
	__pattern := replace(__pattern, 'o', '(o|ó|ò|ö|Ö|ō|ø|Ø)');
	__pattern := replace(__pattern, 'r', '(r|ř)');
	__pattern := replace(__pattern, 's', '(s|ś|š|Š|ş|Ş)');
	__pattern := replace(__pattern, 'ss', '(ss|ß)');
	__pattern := replace(__pattern, 't', '(t|ţ)');
	__pattern := replace(__pattern, 'u', '(u|ū|ú|ü)');
	__pattern := replace(__pattern, 'y', '(y|ý)');
	__pattern := replace(__pattern, 'z', '(z|ż|ź|ž|Ž)');
	_scopes = '{PR,CT,CX,CN,CP,EV,SP,TM,ST,YR}';
	_tables = '{PERSON,CITY,COMPLEX,COUNTRY,CHAMPIONSHIP,EVENT,SPORT,TEAM,STATE,YEAR}';
	FOR _s IN SELECT UNNEST(_scopes) LOOP
		IF _scope ~ ('(^|,)' || _s || '($|,)') OR _scope = '.' THEN
			_rel_cols := '';
			_rel_joins := '';
			_rel_count := 0;

			-- Get related fields
			IF (_s ~ 'PR|TM') THEN -- Relation: Country
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang || ' || '' ('' || CN.code || '')''';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s ~ 'PR|TM') THEN -- Relation: Sport
				_rel_cols := _rel_cols || ', SP.id, SP.label' || _lang;
				_rel_joins := _rel_joins || ' LEFT JOIN "SPORT" SP ON ' || _s || '.id_sport = SP.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'PR') THEN -- Relation: Team
				_rel_cols := _rel_cols || ', TM.id, TM.label, PR.link';
				_rel_joins := _rel_joins || ' LEFT JOIN "TEAM" TM ON ' || _s || '.id_team = TM.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'CX') THEN -- Relation: City/State/Country
				_rel_cols := _rel_cols || ', CT.id, CT.label' || _lang;
				_rel_cols := _rel_cols || ', ST.id, ST.label' || _lang;
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang;
				_rel_joins := _rel_joins || ' LEFT JOIN "CITY" CT ON ' || _s || '.id_city = CT.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			IF (_s = 'CT') THEN -- Relation: State/Country
				_rel_cols := _rel_cols || ', NULL, NULL';
				_rel_cols := _rel_cols || ', ST.id, ST.label' || _lang;
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang;
				_rel_joins := _rel_joins || ' LEFT JOIN "STATE" ST ON ' || _s || '.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			FOR _j IN 1.._rel_count LOOP
				_rel_cols := _rel_cols || ', NULL, NULL';
			END LOOP;

			-- Execute query
			_label := 'label';
			IF (_s <> 'TM' AND _s <> 'YR') THEN
				_label := 'label' || _lang;
			END IF;
			_query := 'SELECT ' || _s || '.id, ' || _s || '.' || _label || ',' || _s || '.ref' || _rel_cols || ' FROM "' || _tables[_i] || '" ' || _s;
			_query := _query || _rel_joins || ' WHERE ' || _s || '.' || _label || ' ~* ''' || __pattern || ''' ORDER BY ' || _s || '.' || _label;
			IF _s = 'PR' THEN
				_query := 'SELECT PR.id, UPPER(PR.last_name) || '', '' || PR.first_name, PR.ref' || _rel_cols || ' FROM "PERSON" PR' || _rel_joins;
				_query := _query || ' WHERE (PR.link = 0 OR PR.link IS NULL) AND (PR.last_name || '' '' || PR.first_name ~* ''' || __pattern || ''' OR PR.first_name || '' '' || PR.last_name ~* ''' || __pattern || ''' OR PR.last_name ~* ''' || __pattern || ''' OR PR.first_name ~* ''' || __pattern || ''')';
				_query := _query || ' ORDER BY PR.last_name, PR.first_name';
			END IF;
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _current_id, _current_label, _current_ref, _current_id_rel1, _current_label_rel1, _current_id_rel2, _current_label_rel2, _current_id_rel3, _current_label_rel3, _current_link;
				EXIT WHEN NOT FOUND;
				_item.id = _index;
				_item.id_item = _current_id;
				_item.label = _current_label;
				_item.entity = _s;
				_item.count_ref = _current_ref;
				_item.id_rel1 = _current_id_rel1;
				_item.id_rel2 = _current_id_rel2;
				_item.id_rel3 = _current_id_rel3;
				_item.label_rel1 = _current_label_rel1;
				_item.label_rel2 = _current_label_rel2;
				_item.label_rel3 = _current_label_rel3;
				_item.link = _current_link;
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

  
  