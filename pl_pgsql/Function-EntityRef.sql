-- Function: "EntityRef"(character varying, integer, character varying, character varying, integer, character varying)

-- DROP FUNCTION "EntityRef"(character varying, integer, character varying, character varying, integer, character varying);

CREATE OR REPLACE FUNCTION "EntityRef"(
    _entity character varying,
    _id integer,
    _entity_ref character varying,
    _limit character varying,
    _offset integer,
    _lang character varying)
  RETURNS SETOF "~RefItem" AS
$BODY$
declare
	_item "~RefItem"%rowtype;
	_entity varchar := _entity;
	_c refcursor;
	__c refcursor;
	_query text;
	_link integer;
	_rs "Result"%rowtype;
	_ct_list varchar(200);
	_cx_list varchar(200);
	_pr_list varchar(200);
	_tm_list varchar(200);
	_index integer;
	_type1 smallint;
	_type2 smallint;
	_type3 smallint;
	_date varchar(8);
	_date1 varchar(10);
	_date2 varchar(10);
	_array_id integer[];
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
	_id11 integer;_id12 integer;_id13 integer;_id14 integer;
	_cn1 varchar(5);_cn2 varchar(5);_cn3 varchar(5);_cn4 varchar(5);_cn5 varchar(5);_cn6 varchar(5);
	_tm1 varchar(60);_tm2 varchar(60);_tm3 varchar(60);_tm4 varchar(60);_tm5 varchar(60);_tm6 varchar(60);
begin
	IF _entity ~ E'^\\d{8}' THEN
		_date := _entity;
		_entity := 'DT';
	END IF;

	_index := 1;

	IF _entity ~ 'CT' THEN
		SELECT LINK INTO _link FROM "City" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query = 'SELECT ID FROM "City" WHERE ';
			IF _link = 0 THEN
				_query = _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query = _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_ct_list = '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_ct_list = _ct_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_ct_list = cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'CX' THEN
		SELECT LINK INTO _link FROM "Complex" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query = 'SELECT ID FROM "Complex" WHERE ';
			IF _link = 0 THEN
				_query = _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query = _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_cx_list = '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_cx_list = _cx_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_cx_list = cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'PR' THEN
		SELECT LINK INTO _link FROM "Athlete" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query = 'SELECT ID FROM "Athlete" WHERE ';
			IF _link = 0 THEN
				_query = _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query = _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_pr_list = '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_pr_list = _pr_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_pr_list = cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'TM' THEN
		SELECT LINK INTO _link FROM "Team" WHERE ID = _id AND (YEAR1 IS NULL OR YEAR1 = '');
		IF _link IS NOT NULL THEN
			_query = 'SELECT ID FROM "Team" WHERE ';
			IF _link = 0 THEN
				_query = _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query = _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_tm_list = '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_tm_list = _tm_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_tm_list = cast(_id AS varchar);
		END IF;
	END IF;
	
	-- References in: [Events]
	IF (_entity ~ 'CP|EV|SP' AND (_entity_ref = 'EV' OR _entity_ref = '')) THEN
		_query = 'SELECT DISTINCT SP.id, SP.label' || _lang || ', SP.label, CP.id, CP.label' || _lang || ', CP.label, EV.id, EV.label' || _lang || ', EV.label, SE.id, SE.label' || _lang || ', SE.label, SE2.id, SE2.label' || _lang || ', SE2.label, II.id_championship, II.id_event, II.id_subevent, II.id_subevent2, CP.index, EV.index, SE.index, SE2.index, (CASE WHEN II.id_event IS NOT NULL AND II.id_subevent IS NULL AND II.id_subevent2 IS NULL THEN 1 ELSE 0 END) AS o_ii_ev, (CASE WHEN II.id_subevent IS NOT NULL AND II.id_subevent2 IS NULL THEN 1 ELSE 0 END) AS o_ii_se, (CASE WHEN II.id_subevent2 IS NOT NULL THEN 1 ELSE 0 END) AS o_ii_se2';
		_query = _query || ' FROM "Result" RS LEFT JOIN "Sport" SP ON RS.id_sport = SP.id';
		_query = _query || ' LEFT JOIN "Championship" CP ON RS.id_championship = CP.id';
		_query = _query || ' LEFT JOIN "Event" EV ON RS.id_event = EV.id';
		_query = _query || ' LEFT JOIN "Event" SE ON RS.id_subevent = SE.id';
		_query = _query || ' LEFT JOIN "Event" SE2 ON RS.id_subevent2 = SE2.id';
		_query = _query || ' LEFT JOIN "~InactiveItem" II ON (RS.id_sport = II.id_sport AND RS.id_championship = II.id_championship AND RS.id_event = II.id_event AND (RS.id_subevent = II.id_subevent OR RS.id_subevent IS NULL) AND (RS.id_subevent2 = II.id_subevent2 OR RS.id_subevent2 IS NULL))';
		IF _entity = 'SP' THEN
			_query = _query || ' WHERE RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query = _query || ' WHERE RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query = _query || ' WHERE RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id;
		END IF;
		_query = _query || ' ORDER BY SP.label' || _lang || ', CP.index, o_ii_ev, EV.index, o_ii_se, SE.index, o_ii_se2, SE2.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', SE2.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_rel1, _item.label_rel1, _item.label_rel2, _item.id_rel2, _item.label_rel3, _item.label_rel4, _item.id_rel3, _item.label_rel5, _item.label_rel6, _item.id_rel4, _item.label_rel7, _item.label_rel8, _item.id_rel5, _item.label_rel9, _item.label_rel10, _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'EV';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Results]
	IF (_entity ~ 'CN|DT|PR|TM|CP|EV|CT|SP|CX|OL|YR' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query = 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number, TP3.number, RS.date1, RS.date2';
		IF (_entity = 'PR') THEN
			_query = _query || ', PL.rank';
		ELSE
			_query = _query || ', 0';
		END IF;
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
		IF (_entity = 'OL') THEN
			_query = _query || ' LEFT JOIN "Olympics" OL ON (OL.id_year = YR.id AND OL.type = SP.type)';
		ELSIF (_entity = 'PR') THEN
			_type1 = 1;
			_type2 = 99;
			_query = _query || ' LEFT JOIN "~PersonList" PL ON PL.id_result = RS.id';
		END IF;
		_query = _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity = 'CN' THEN
			_query = _query || ' AND (RS.id_rank1 = ' || _id || ' OR RS.id_rank2 = ' || _id || ' OR RS.id_rank3 = ' || _id || ' OR RS.id_rank4 = ' || _id || ' OR RS.id_rank5 = ' || _id || ' OR RS.id_rank6 = ' || _id || ' OR RS.id_rank7 = ' || _id || ' OR RS.id_rank8 = ' || _id || ' OR RS.id_rank9 = ' || _id || ' OR RS.id_rank10 = ' || _id || ' OR RS.id_rank11 = ' || _id || ' OR RS.id_rank12 = ' || _id || ' OR RS.id_rank13 = ' || _id || ' OR RS.id_rank14 = ' || _id || ' OR RS.id_rank15 = ' || _id || ' OR RS.id_rank16 = ' || _id || ' OR RS.id_rank17 = ' || _id || ' OR RS.id_rank18 = ' || _id || ' OR RS.id_rank19 = ' || _id || ' OR RS.id_rank20 = ' || _id || ')';
		ELSIF _entity = 'DT' THEN
			_query = _query || ' AND to_date(RS.date2, ''DD/MM/YYYY'') = to_date(''' || _date || ''', ''YYYYMMDD'')';
		ELSIF _entity = 'PR' THEN
			_query = _query || ' AND (PL.id_person IN (' || _pr_list || ') OR (((TP1.number BETWEEN 1 AND 10 AND TP2.number IS NULL) OR (TP2.number BETWEEN 1 AND 10 AND TP3.number IS NULL) OR (TP3.number BETWEEN 1 AND 10)) AND ((RS.id_rank1 IN (' || _pr_list || ') OR RS.id_rank2 IN (' || _pr_list || ') OR RS.id_rank3 IN (' || _pr_list || ') OR RS.id_rank4 IN (' || _pr_list || ') OR RS.id_rank5 IN (' || _pr_list || ') OR RS.id_rank6 IN (' || _pr_list || ') OR RS.id_rank7 IN (' || _pr_list || ') OR RS.id_rank8 IN (' || _pr_list || ') OR RS.id_rank9 IN (' || _pr_list || ') OR RS.id_rank10 IN (' || _pr_list || ') OR RS.id_rank11 IN (' || _pr_list || ') OR RS.id_rank12 IN (' || _pr_list || ') OR RS.id_rank13 IN (' || _pr_list || ') OR RS.id_rank14 IN (' || _pr_list || ') OR RS.id_rank15 IN (' || _pr_list || ') OR RS.id_rank16 IN (' || _pr_list || ') OR RS.id_rank17 IN (' || _pr_list || ') OR RS.id_rank18 IN (' || _pr_list || ') OR RS.id_rank19 IN (' || _pr_list || ') OR RS.id_rank20 IN (' || _pr_list || ')))))';
		ELSIF _entity = 'TM' THEN
			_query = _query || ' AND (RS.id_rank1 IN (' || _tm_list || ') OR RS.id_rank2 IN (' || _tm_list || ') OR RS.id_rank3 IN (' || _tm_list || ') OR RS.id_rank4 IN (' || _tm_list || ') OR RS.id_rank5 IN (' || _tm_list || ') OR RS.id_rank6 IN (' || _tm_list || ') OR RS.id_rank7 IN (' || _tm_list || ') OR RS.id_rank8 IN (' || _tm_list || ') OR RS.id_rank9 IN (' || _tm_list || ') OR RS.id_rank10 IN (' || _tm_list || ') OR RS.id_rank11 IN (' || _tm_list || ') OR RS.id_rank12 IN (' || _tm_list || ') OR RS.id_rank13 IN (' || _tm_list || ') OR RS.id_rank14 IN (' || _tm_list || ') OR RS.id_rank15 IN (' || _tm_list || ') OR RS.id_rank16 IN (' || _tm_list || ') OR RS.id_rank17 IN (' || _tm_list || ') OR RS.id_rank18 IN (' || _tm_list || ') OR RS.id_rank19 IN (' || _tm_list || ') OR RS.id_rank20 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query = _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query = _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query = _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query = _query || ' AND  (RS.id_city1 IN (' || _ct_list || ') OR RS.id_city2 IN (' || _ct_list || '))';
		ELSIF _entity = 'CX' THEN
			_query = _query || ' AND  (RS.id_complex1 IN (' || _cx_list || ') OR RS.id_complex2 IN (' || _cx_list || '))';
		ELSIF _entity = 'OL' THEN
			_query = _query || ' AND RS.id_championship=1 AND OL.id = ' || _id;
		ELSIF _entity = 'YR' THEN
			_query = _query || ' AND RS.id_year = ' || _id;
		END IF;
		_query = _query || ' ORDER BY YR.id DESC, (CASE WHEN RS.date2 IS NOT NULL AND RS.date2<>'''' THEN to_date(RS.date2, ''dd/MM/yyyy'') ELSE RS.first_update END) DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
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
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, CN3.code, TM3.label, PR4.last_name || (CASE WHEN length(PR4.first_name) > 0 THEN ', ' || PR4.first_name ELSE '' END), (CASE WHEN length(PR4.first_name) > 0 THEN PR4.first_name || ' ' ELSE '' END) || PR4.last_name, CN4.id, CN4.code, TM4.label, PR5.last_name || (CASE WHEN length(PR5.first_name) > 0 THEN ', ' || PR5.first_name ELSE '' END), (CASE WHEN length(PR5.first_name) > 0 THEN PR5.first_name || ' ' ELSE '' END) || PR5.last_name, CN5.id, CN5.code, TM5.label, PR6.last_name || (CASE WHEN length(PR6.first_name) > 0 THEN ', ' || PR6.first_name ELSE '' END), (CASE WHEN length(PR6.first_name) > 0 THEN PR6.first_name || ' ' ELSE '' END) || PR6.last_name, CN6.id, CN6.code, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.label_rel23, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.label_rel24, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.label_rel25, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "Result" RS LEFT JOIN "Athlete" PR1 ON RS.id_rank1 = PR1.id LEFT JOIN "Athlete" PR2 ON RS.id_rank2 = PR2.id LEFT JOIN "Athlete" PR3 ON RS.id_rank3 = PR3.id LEFT JOIN "Athlete" PR4 ON RS.id_rank4 = PR4.id LEFT JOIN "Athlete" PR5 ON RS.id_rank5 = PR5.id LEFT JOIN "Athlete" PR6 ON RS.id_rank6 = PR6.id LEFT JOIN "Country" CN1 ON PR1.id_country = CN1.id LEFT JOIN "Country" CN2 ON PR2.id_country = CN2.id LEFT JOIN "Country" CN3 ON PR3.id_country = CN3.id LEFT JOIN "Country" CN4 ON PR4.id_country = CN4.id LEFT JOIN "Country" CN5 ON PR5.id_country = CN5.id LEFT JOIN "Country" CN6 ON PR6.id_country = CN6.id LEFT JOIN "Team" TM1 ON PR1.id_team = TM1.id LEFT JOIN "Team" TM2 ON PR2.id_team = TM2.id LEFT JOIN "Team" TM3 ON PR3.id_team = TM3.id LEFT JOIN "Team" TM4 ON PR4.id_team = TM4.id LEFT JOIN "Team" TM5 ON PR5.id_team = TM5.id LEFT JOIN "Team" TM6 ON PR6.id_team = TM6.id
				WHERE RS.id = _item.id_item;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 = _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 = _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 = _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 = _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				IF _cn3 IS NOT NULL THEN _item.label_rel8 = _item.label_rel8 || ' (' || _cn3 || ')';
				ELSIF _tm3 IS NOT NULL THEN _item.label_rel8 = _item.label_rel8 || ' (' || _tm3 || ')'; END IF;
				IF _cn4 IS NOT NULL THEN _item.label_rel9 = _item.label_rel9 || ' (' || _cn4 || ')';
				ELSIF _tm4 IS NOT NULL THEN _item.label_rel9 = _item.label_rel9 || ' (' || _tm4 || ')'; END IF;
				IF _cn5 IS NOT NULL THEN _item.label_rel10 = _item.label_rel10 || ' (' || _cn5 || ')';
				ELSIF _tm5 IS NOT NULL THEN _item.label_rel10 = _item.label_rel10 || ' (' || _tm5 || ')'; END IF;
				IF _cn6 IS NOT NULL THEN _item.label_rel11 = _item.label_rel11 || ' (' || _cn6 || ')';
				ELSIF _tm6 IS NOT NULL THEN _item.label_rel11 = _item.label_rel11 || ' (' || _tm6 || ')'; END IF;
				IF _type1 = 4 OR _item.txt3 = '#DOUBLE#' THEN
					_item.txt4 = '1-2/3-4/5-6';
				ELSIF _type1 = 5 OR _item.txt3 = '#TRIPLE#' THEN
					_item.txt4 = '1-3/4-6/7-9';
				END IF;
				_item.comment = 'PR';
				_array_id = string_to_array(_pr_list, ',')::integer[];
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, NULL, NULL, NULL, NULL, NULL, NULL, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "Result" RS LEFT JOIN "Team" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "Team" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "Team" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "Team" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "Team" TM5 ON RS.id_rank5 = TM5.id LEFT JOIN "Team" TM6 ON RS.id_rank6 = TM6.id
				WHERE RS.id = _item.id_item;
				_item.comment = 'TM';
				_array_id = string_to_array(_tm_list, ',')::integer[];
			ELSIF _type1 = 99 THEN
				_query = 'SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN4.label' || _lang || ', CN5.label' || _lang || ', CN6.label' || _lang || ', CN1.label, CN2.label, CN3.label, CN4.label, CN5.label, CN6.label, RS.result1, RS.result2, RS.comment, RS.exa';
				_query = _query || ' FROM "Result" RS LEFT JOIN "Country" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "Country" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "Country" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "Country" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "Country" CN5 ON RS.id_rank5 = CN5.id LEFT JOIN "Country" CN6 ON RS.id_rank6 = CN6.id';
				_query = _query || ' WHERE RS.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4;
				CLOSE __c;
				_item.comment = 'CN';
				_array_id = ARRAY[_id];
			END IF;
			_item.date1 := to_date(_date1, 'DD/MM/YYYY');
			_item.date2 := to_date(_date2, 'DD/MM/YYYY');
			SELECT * INTO _rs FROM "Result" RS WHERE RS.id = _item.id_item;
			SELECT "GetRank"(_rs, _type1, _array_id) INTO _item.count1;
			_item.id = _index;
			_item.entity = 'RS';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Rounds]
	IF (_entity ~ 'CN|DT|PR|TM|CP|EV|SP|CX|CT|YR' AND (_entity_ref = 'RD' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query = 'SELECT RD.id, RD.id_result, RD.id_result_type, RT.label' || _lang || ', YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RD.id_rank1, RD.id_rank2, RD.id_rank3, RD.date FROM "Round" RD';
		_query = _query || ' LEFT JOIN "Result" RS ON RD.id_result = RS.id';
		_query = _query || ' LEFT JOIN "RoundType" RT ON RD.id_round_type = RT.id';
		_query = _query || ' LEFT JOIN "Year" YR ON RS.id_year = YR.id';
		_query = _query || ' LEFT JOIN "Sport" SP ON RS.id_sport = SP.id';
		_query = _query || ' LEFT JOIN "Championship" CP ON RS.id_championship = CP.id';
		_query = _query || ' LEFT JOIN "Event" EV ON RS.id_event = EV.id';
		_query = _query || ' LEFT JOIN "Event" SE ON RS.id_subevent = SE.id';
		_query = _query || ' LEFT JOIN "Event" SE2 ON RS.id_subevent2 = SE2.id';
		_query = _query || ' WHERE (id_result_type BETWEEN ' || _type1 || ' AND ' || _type2 || ')';
		IF _entity = 'CN' THEN
			_query = _query || ' AND (RD.id_rank1 = ' || _id || ' OR RD.id_rank2 = ' || _id || ' OR RD.id_rank3 = ' || _id || ')';
		ELSIF _entity = 'DT' THEN
			_query = _query || ' AND to_date(RD.date, ''DD/MM/YYYY'') = to_date(''' || _date || ''', ''YYYYMMDD'')';
		ELSIF _entity = 'PR' THEN
			_query = _query || ' AND (RD.id_rank1 IN (' || _pr_list || ') OR RD.id_rank2 IN (' || _pr_list || ') OR RD.id_rank3 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query = _query || ' AND (RD.id_rank1 IN (' || _tm_list || ') OR RD.id_rank2 IN (' || _tm_list || ') OR RD.id_rank3 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query = _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query = _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query = _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query = _query || ' AND  (RD.id_city IN (' || _ct_list || ') OR RD.id_city IN (' || _ct_list || '))';
		ELSIF _entity = 'CX' THEN
			_query = _query || ' AND  (RD.id_complex IN (' || _cx_list || ') OR RD.id_complex IN (' || _cx_list || '))';
		ELSIF _entity = 'YR' THEN
			_query = _query || ' AND RS.id_year = ' || _id;
		END IF;
		_query = _query || ' ORDER BY YR.id DESC, RD.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel10, _type1, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _date2;
			EXIT WHEN NOT FOUND;
			IF _type1 <= 10 THEN
				SELECT id_rank1, id_rank2, id_rank3, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, CN3.code, TM3.label, RD.result1, RD.result2, RD.result3
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.txt1, _item.txt2, _item.txt3
				FROM "Round" RD LEFT JOIN "Athlete" PR1 ON RD.id_rank1 = PR1.id LEFT JOIN "Athlete" PR2 ON RD.id_rank2 = PR2.id LEFT JOIN "Athlete" PR3 ON RD.id_rank3 = PR3.id LEFT JOIN "Country" CN1 ON PR1.id_country = CN1.id LEFT JOIN "Country" CN2 ON PR2.id_country = CN2.id LEFT JOIN "Country" CN3 ON PR3.id_country = CN3.id LEFT JOIN "Team" TM1 ON PR1.id_team = TM1.id LEFT JOIN "Team" TM2 ON PR2.id_team = TM2.id LEFT JOIN "Team" TM3 ON PR3.id_team = TM3.id
				WHERE RD.id = _item.id_item;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 = _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 = _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 = _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 = _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				IF _cn3 IS NOT NULL THEN _item.label_rel8 = _item.label_rel8 || ' (' || _cn3 || ')';
				ELSIF _tm3 IS NOT NULL THEN _item.label_rel8 = _item.label_rel8 || ' (' || _tm3 || ')'; END IF;
				_item.comment = 'PR';
				_array_id = string_to_array(_pr_list, ',')::integer[];
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, TM1.label, TM2.label, TM3.label, RD.result1, RD.result2, RD.result3
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.txt1, _item.txt2, _item.txt3
				FROM "Round" RD LEFT JOIN "Team" TM1 ON RD.id_rank1 = TM1.id LEFT JOIN "Team" TM2 ON RD.id_rank2 = TM2.id LEFT JOIN "Team" TM3 ON RD.id_rank3 = TM3.id
				WHERE RD.id = _item.id_item;
				_item.comment = 'TM';
				_array_id = string_to_array(_tm_list, ',')::integer[];
			ELSIF _type1 = 99 THEN
				_query = 'SELECT id_rank1, id_rank2, id_rank3, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN1.label, CN2.label, CN3.label, RD.result1, RD.result2, RD.result3';
				_query = _query || ' FROM "Round" RD LEFT JOIN "Country" CN1 ON RD.id_rank1 = CN1.id LEFT JOIN "Country" CN2 ON RD.id_rank2 = CN2.id LEFT JOIN "Country" CN3 ON RD.id_rank3 = CN3.id';
				_query = _query || ' WHERE RD.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.txt1, _item.txt2, _item.txt3;
				CLOSE __c;
				_item.comment = 'CN';
				_array_id = ARRAY[_id];
			END IF;
			IF _entity = 'DT' THEN
				_item.date2 := to_date(_date2, 'DD/MM/YYYY');
			END IF;
			_item.id = _index;
			_item.entity = 'RD';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;
	
	-- References in: [Athletes]
	IF (_entity ~ 'CN|SP|TM' AND (_entity_ref = 'PR' OR _entity_ref = '')) THEN
		_query = 'SELECT DISTINCT ON (PR.last_name COLLATE "en_EN", PR.first_name COLLATE "en_EN", CN.id, SP.id) PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label, PR.link FROM "Athlete" PR';
		_query = _query || ' LEFT JOIN "Country" CN ON PR.id_country = CN.id';
		_query = _query || ' LEFT JOIN "Sport" SP ON PR.id_sport = SP.id';
		IF _entity = 'CN' THEN
			_query = _query || ' WHERE PR.id_country = ' || _id;
		ELSIF _entity = 'SP' THEN
			_query = _query || ' WHERE PR.id_sport = ' || _id || ' AND (PR.link = 0 OR PR.link IS NULL)';
		ELSIF _entity = 'TM' THEN
			_query = _query || ' WHERE PR.id_team IN (' || _tm_list || ')';
		END IF;
		_query = _query || ' ORDER BY PR.last_name COLLATE "en_EN", PR.first_name COLLATE "en_EN", SP.id LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4, _item.id_rel11;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'PR';
			_item.txt3 := '';
			IF (_entity = 'SP' AND _item.id_rel11 = 0) THEN
				SELECT string_agg(CN.id || ',' || CN.label || ',' || CN.label_fr, '|') INTO _item.txt3 FROM "Athlete" PR LEFT JOIN "Country" CN ON PR.id_country=CN.id WHERE PR.link=_item.id_item;
			END IF;
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Teams]
	IF (_entity ~ 'SP|CN' AND (_entity_ref = 'TM' OR _entity_ref = '')) THEN
		_query = 'SELECT TM.id, TM.label, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "Team" TM';
		_query = _query || ' LEFT JOIN "Country" CN ON TM.id_country = CN.id';
		_query = _query || ' LEFT JOIN "Sport" SP ON TM.id_sport = SP.id';
		IF _entity = 'SP' THEN
			_query = _query || ' WHERE TM.id_sport = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query = _query || ' WHERE TM.id_country = ' || _id;
		END IF;
		_query = _query || ' ORDER BY SP.label' || _lang || ', TM.label LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'TM';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Cities]
	IF (_entity ~ 'CN|ST' AND (_entity_ref = 'CT' OR _entity_ref = '')) THEN
		_query = 'SELECT CT.id, CT.label' || _lang || ', CT.label, CN.id, CN.label' || _lang || ', CN.label FROM "City" CT';
		_query = _query || ' LEFT JOIN "State" ST ON CT.id_state = ST.id';
		_query = _query || ' LEFT JOIN "Country" CN ON CT.id_country = CN.id';
		IF _entity = 'CN' THEN
			_query = _query || ' WHERE CT.id_country = ' || _id;
		ELSIF _entity = 'ST' THEN
			_query = _query || ' WHERE CT.id_state = ' || _id;
		END IF;
		_query = _query || ' ORDER BY CT.label' || _lang || ' COLLATE "en_EN" LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.label_rel2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CT';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Complexes]
	IF (_entity = 'CT' AND (_entity_ref = 'CX' OR _entity_ref = '')) THEN
		_query = 'SELECT CX.id, CX.label' || _lang || ', CX.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label FROM "Complex" CX';
		_query = _query || ' LEFT JOIN "City" CT ON CX.id_city = CT.id';
		_query = _query || ' LEFT JOIN "State" ST ON CT.id_state = ST.id';
		_query = _query || ' LEFT JOIN "Country" CN ON CT.id_country = CN.id';
		_query = _query || ' WHERE CX.id_city = ' || _id;
		_query = _query || ' ORDER BY CX.label' || _lang || ' COLLATE "en_EN" LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CX';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Olympics]
	IF (_entity ~ 'YR|CT' AND (_entity_ref = 'OL' OR _entity_ref = '')) THEN
		_query = 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OL.type FROM "Olympics" OL';
		_query = _query || ' LEFT JOIN "Year" YR ON OL.id_year = YR.id';
		_query = _query || ' LEFT JOIN "City" CT ON OL.id_city = CT.id';
		_query = _query || ' LEFT JOIN "State" ST ON CT.id_state = ST.id';
		_query = _query || ' LEFT JOIN "Country" CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query = _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query = _query || ' WHERE OL.id_city IN (' || _ct_list || ')';
		END IF;
		_query = _query || ' ORDER BY OL.type, YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'OL';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Olympic Rankings]
	IF (_entity ~ 'OL|CN' AND (_entity_ref = 'OR' OR _entity_ref = '')) THEN
		_query = 'SELECT OR_.id, OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OR_.count_gold || '','' || OR_.count_silver || '','' || OR_.count_bronze FROM "OlympicRanking" OR_';
		_query = _query || ' LEFT JOIN "Olympics" OL ON OR_.id_olympics = OL.id';
		_query = _query || ' LEFT JOIN "Year" YR ON OL.id_year = YR.id';
		_query = _query || ' LEFT JOIN "City" CT ON OL.id_city = CT.id';
		_query = _query || ' LEFT JOIN "Country" CN ON OR_.id_country = CN.id';
		IF _entity = 'OL' THEN
			_query = _query || ' WHERE OR_.id_olympics = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query = _query || ' WHERE OR_.id_country = ' || _id;
		END IF;
		_query = _query || ' ORDER BY YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'OR';
			RETURN NEXT _item;
			_index = _index + 1;
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
		_query = 'SELECT RC.id, RC.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, RC.type1, RC.type2, RC.record1, RC.id_rank1, RC.id_rank2, RC.id_rank3, RC.id_rank4, RC.id_rank5 FROM "Record" RC';
		_query = _query || ' LEFT JOIN "Sport" SP ON RC.id_sport = SP.id';
		_query = _query || ' LEFT JOIN "Championship" CP ON RC.id_championship = CP.id';
		_query = _query || ' LEFT JOIN "Event" EV ON RC.id_event = EV.id';
		_query = _query || ' LEFT JOIN "Event" SE ON RC.id_subevent = SE.id';
		_query = _query || ' WHERE lower(RC.type1) = ''' || (CASE WHEN _type1 = 50 THEN 'team' ELSE 'individual' END) || '''';
		IF _entity = 'CN' THEN
			_query = _query || ' AND (RC.id_rank1 = ' || _id || ' OR RC.id_rank2 = ' || _id || ' OR RC.id_rank3 = ' || _id || ' OR RC.id_rank4 = ' || _id || ' OR RC.id_rank5 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query = _query || ' AND (RC.id_rank1 IN (' || _pr_list || ') OR RC.id_rank2 IN (' || _pr_list || ') OR RC.id_rank3 IN (' || _pr_list || ') OR RC.id_rank4 IN (' || _pr_list || ') OR RC.id_rank5 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query = _query || ' AND (RC.id_rank1 IN (' || _tm_list || ') OR RC.id_rank2 IN (' || _tm_list || ') OR RC.id_rank3 IN (' || _tm_list || ') OR RC.id_rank4 IN (' || _tm_list || ') OR RC.id_rank5 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query = _query || ' AND RC.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query = _query || ' AND RC.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query = _query || ' AND  (RC.id_event = ' || _id || ' OR RC.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query = _query || ' AND RC.id_city IN (' || _ct_list || ')';
		END IF;
		_query = _query || ' ORDER BY SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', RC.index LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.txt1, _item.txt2, _item.txt3, _id1, _id2, _id3, _id4, _id5;
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
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Hall of Fame]
	IF (_entity ~ 'YR|PR' AND (_entity_ref = 'HF' OR _entity_ref = '')) THEN
		_query = 'SELECT HF.id, YR.id, YR.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, HF.position FROM "HallOfFame" HF';
		_query = _query || ' LEFT JOIN "Year" YR ON HF.id_year = YR.id';
		_query = _query || ' LEFT JOIN "Athlete" PR ON HF.id_person = PR.id';
		_query = _query || ' LEFT JOIN "League" LG ON HF.id_league = LG.id';
		IF _entity = 'YR' THEN
			_query = _query || ' WHERE HF.id_year = ' || _id;
		ELSIF _entity = 'PR' THEN
			_query = _query || ' WHERE HF.id_person IN (' || _pr_list || ')';
		END IF;
		_query = _query || ' ORDER BY YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.txt1;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'HF';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Retired Numbers]
	IF (_entity ~ 'PR' AND (_entity_ref = 'RN' OR _entity_ref = '')) THEN
		_query = 'SELECT RN.id, TM.id, TM.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, RN.number FROM "RetiredNumber" RN';
		_query = _query || ' LEFT JOIN "Team" TM ON RN.id_team = TM.id';
		_query = _query || ' LEFT JOIN "Athlete" PR ON RN.id_person = PR.id';
		_query = _query || ' LEFT JOIN "League" LG ON RN.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query = _query || ' WHERE RN.id_team IN (' || _tm_list || ')';
		ELSIF _entity = 'PR' THEN
			_query = _query || ' WHERE RN.id_person IN (' || _pr_list || ')';
		END IF;
		_query = _query || ' ORDER BY TM.label, RN.number LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.id_rel4;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'RN';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Team Stadiums]
	IF (_entity ~ 'CX' AND (_entity_ref = 'TS' OR _entity_ref = '')) THEN
		_query = 'SELECT TS.id, TM.id, TM.label, CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', CX.label, CT.label, ST.label, CN.label, LG.id, LG.label, TS.date1, TS.date2 FROM "TeamStadium" TS';
		_query = _query || ' LEFT JOIN "Team" TM ON TS.id_team = TM.id';
		_query = _query || ' LEFT JOIN "Complex" CX ON TS.id_complex = CX.id';
		_query = _query || ' LEFT JOIN "City" CT ON CX.id_city = CT.id';
		_query = _query || ' LEFT JOIN "State" ST ON CT.id_state = ST.id';
		_query = _query || ' LEFT JOIN "Country" CN ON CT.id_country = CN.id';
		_query = _query || ' LEFT JOIN "League" LG ON TS.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query = _query || ' WHERE TS.id_team IN (' || _tm_list || ')';
		ELSIF _entity = 'CX' THEN
			_query = _query || ' WHERE TS.id_complex IN (' || _cx_list || ')';
		END IF;
		_query = _query || ' ORDER BY TM.label, TS.date1 DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.id_rel6, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'TS';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Contributions]
	IF (_entity ~ 'CB' AND (_entity_ref = 'CO' OR _entity_ref = '')) THEN
		_query = 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, CO.type, CO.date';
		_query = _query || ' FROM "~Contribution" CO';
		_query = _query || ' LEFT JOIN "Result" RS ON CO.id_item = RS.id';
		_query = _query || ' LEFT JOIN "Year" YR ON RS.id_year = YR.id';
		_query = _query || ' LEFT JOIN "Sport" SP ON RS.id_sport = SP.id';
		_query = _query || ' LEFT JOIN "Championship" CP ON RS.id_championship = CP.id';
		_query = _query || ' LEFT JOIN "Event" EV ON RS.id_event = EV.id';
		_query = _query || ' LEFT JOIN "Event" SE ON RS.id_subevent = SE.id';
		_query = _query || ' LEFT JOIN "Event" SE2 ON RS.id_subevent2 = SE2.id';
		_query = _query || ' WHERE RS.id_contributor=' || _id || ' ORDER BY CO.date DESC, YR.id DESC, SP.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.date1; 
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CO';
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
