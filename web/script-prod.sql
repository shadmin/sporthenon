insert into "~Config" values ('max_photo_width', '300');

alter TABLE "~PersonList" add index varchar(20);

CREATE OR REPLACE FUNCTION "GetPersonList"(_results text)
  RETURNS refcursor AS
$BODY$
declare
	_query text;
	_c refcursor;
begin
	_query = 'SELECT PL.id AS pl_id, PL.id_result AS rs_id, PL.rank AS pl_rank, PL.index AS pl_index, PL.id_person AS pr_id, PR.last_name AS pr_last_name, PR.first_name AS pr_first_name, CN.id AS pr_country_id, CN.code AS pr_country_code';
	_query = _query || ' FROM "~PersonList" PL';
	_query = _query || ' LEFT JOIN "Athlete" PR ON PL.id_person = PR.id';
	_query = _query || ' LEFT JOIN "Country" CN ON PR.id_country = CN.id';
	_query = _query || ' WHERE PL.id_result IN (' || _results || ')';
	_query = _query || ' ORDER BY PL.id_result, PL.rank, PL.id';
	OPEN _c FOR EXECUTE _query;
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;

  
  
  
  
  
  CREATE OR REPLACE FUNCTION "EntityRef"(_entity character varying, _id integer, _entity_ref character varying, _limit character varying, _offset integer, _lang character varying)
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

	INSERT INTO "~Request" VALUES (NEXTVAL('"~SeqRequest"'), 'IF', _entity || '-' || _id, current_date);
	
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
			FETCH _c INTO _item.id_rel1, _item.label_rel1, _item.label_rel2, _item.id_rel2, _item.label_rel3, _item.label_rel4, _item.id_rel3, _item.label_rel5, _item.label_rel6, _item.id_rel4, _item.label_rel7, _item.label_rel8, _item.id_rel5, _item.label_rel9, _item.label_rel10;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'EV';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Draws] (MUST STAY AT FIRST)
	IF (_entity ~ 'CN|PR|TM' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query = 'SELECT DR.id, DR.id_result, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, DR.id1_qf1, DR.id2_qf1, DR.id1_qf2, DR.id2_qf2, DR.id1_qf3, DR.id2_qf3, DR.id1_qf4, DR.id2_qf4, DR.id1_sf1, DR.id2_sf1, DR.id1_sf2, DR.id2_sf2, DR.id1_thd, DR.id2_thd, TP1.number, TP2.number, TP3.number FROM "Draw" DR';
		_query = _query || ' LEFT JOIN "Result" RS ON DR.id_result = RS.id';
		_query = _query || ' LEFT JOIN "Year" YR ON RS.id_year = YR.id';
		_query = _query || ' LEFT JOIN "Sport" SP ON RS.id_sport = SP.id';
		_query = _query || ' LEFT JOIN "Championship" CP ON RS.id_championship = CP.id';
		_query = _query || ' LEFT JOIN "Event" EV ON RS.id_event = EV.id';
		_query = _query || ' LEFT JOIN "Event" SE ON RS.id_subevent = SE.id';
		_query = _query || ' LEFT JOIN "Event" SE2 ON RS.id_subevent2 = SE2.id';
		_query = _query || ' LEFT JOIN "Type" TP1 ON EV.id_type = TP1.id';
		_query = _query || ' LEFT JOIN "Type" TP2 ON SE.id_type = TP2.id';
		_query = _query || ' LEFT JOIN "Type" TP3 ON SE2.id_type = TP3.id';
		_query = _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity = 'CN' THEN
			_query = _query || ' AND (DR.id1_sf1 <> ' || _id || ' AND DR.id1_sf2 <> ' || _id || ') AND (DR.id1_qf1 = ' || _id || ' OR DR.id2_qf1 = ' || _id || ' OR DR.id1_qf2 = ' || _id || ' OR DR.id2_qf2 = ' || _id || ' OR DR.id1_qf3 = ' || _id || ' OR DR.id2_qf3 = ' || _id || ' OR DR.id1_qf4 = ' || _id || ' OR DR.id2_qf4 = ' || _id || ' OR DR.id2_sf1 = ' || _id || ' OR DR.id2_sf2 = ' || _id || ' OR DR.id1_thd = ' || _id || ' OR DR.id2_thd = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query = _query || ' AND (DR.id1_sf1 NOT IN (' || _pr_list || ') AND DR.id1_sf2 NOT IN (' || _pr_list || ')) AND (DR.id1_qf1 IN (' || _pr_list || ') OR DR.id2_qf1 IN (' || _pr_list || ') OR DR.id1_qf2 IN (' || _pr_list || ') OR DR.id2_qf2 IN (' || _pr_list || ') OR DR.id1_qf3 IN (' || _pr_list || ') OR DR.id2_qf3 IN (' || _pr_list || ') OR DR.id1_qf4 IN (' || _pr_list || ') OR DR.id2_qf4 IN (' || _pr_list || ') OR DR.id2_sf1 IN (' || _pr_list || ') OR DR.id2_sf2 IN (' || _pr_list || ') OR DR.id1_thd IN (' || _pr_list || ') OR DR.id2_thd IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query = _query || ' AND (DR.id1_sf1 NOT IN (' || _tm_list || ') AND DR.id1_sf2 NOT IN (' || _tm_list || ')) AND (DR.id1_qf1 IN (' || _tm_list || ') OR DR.id2_qf1 IN (' || _tm_list || ') OR DR.id1_qf2 IN (' || _tm_list || ') OR DR.id2_qf2 IN (' || _tm_list || ') OR DR.id1_qf3 IN (' || _tm_list || ') OR DR.id2_qf3 IN (' || _tm_list || ') OR DR.id1_qf4 IN (' || _tm_list || ') OR DR.id2_qf4 IN (' || _tm_list || ') OR DR.id2_sf1 IN (' || _tm_list || ') OR DR.id2_sf2 IN (' || _tm_list || ') OR DR.id1_thd IN (' || _tm_list || ') OR DR.id2_thd IN (' || _tm_list || '))';
		END IF;
		_query = _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel10, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _id11, _id12, _id13, _id14, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 = _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 = _type2;
			END IF;
			IF (_id13 = _id OR _id14 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)'))) THEN
				_item.txt2 = 'thd';
			ELSIF (_id9 = _id OR _id10 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)'))) THEN
				_item.txt2 = 'sf1';
			ELSIF (_id11 = _id OR _id12 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)'))) THEN
				_item.txt2 = 'sf2';
			ELSIF (_id1 = _id OR _id2 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)'))) THEN
				_item.txt2 = 'qf1';
			ELSIF (_id3 = _id OR _id4 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)'))) THEN
				_item.txt2 = 'qf2';
			ELSIF (_id5 = _id OR _id6 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)'))) THEN
				_item.txt2 = 'qf3';
			ELSIF (_id7 = _id OR _id8 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)'))) THEN
				_item.txt2 = 'qf4';
			END IF;
			IF _type1 <= 10 THEN
				_query = 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN '', '' || PR1.first_name ELSE '''' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || '' '' ELSE '''' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN '', '' || PR2.first_name ELSE '''' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || '' '' ELSE '''' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, DR.result_' || _item.txt2;
				_query = _query || ' FROM "Draw" DR LEFT JOIN "Athlete" PR1 ON DR.id1_' || _item.txt2 || ' = PR1.id LEFT JOIN "Athlete" PR2 ON DR.id2_' || _item.txt2 || ' = PR2.id LEFT JOIN "Country" CN1 ON PR1.id_country = CN1.id LEFT JOIN "Country" CN2 ON PR2.id_country = CN2.id LEFT JOIN "Team" TM1 ON PR1.id_team = TM1.id LEFT JOIN "Team" TM2 ON PR2.id_team = TM2.id';
				_query = _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel8, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel9, _item.id_rel13, _cn2, _tm2, _item.txt1;
				CLOSE __c;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 = _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 = _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 = _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 = _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				_item.comment = 'PR';
			ELSIF _type1 = 50 THEN
				_query = 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', TM1.label, TM2.label, NULL, NULL, DR.result_' || _item.txt2 || '';
				_query = _query || ' FROM "Draw" DR LEFT JOIN "Team" TM1 ON DR.id1_' || _item.txt2 || ' = TM1.id LEFT JOIN "Team" TM2 ON DR.id2_' || _item.txt2 || ' = TM2.id';
				_query = _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment = 'TM';
			ELSIF _type1 = 99 THEN
				_query = 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', CN1.label' || _lang || ', CN2.label' || _lang || ', CN1.label, CN2.label, DR.result_' || _item.txt2 || '';
				_query = _query || ' FROM "Draw" DR LEFT JOIN "Country" CN1 ON DR.id1_' || _item.txt2 || ' = CN1.id LEFT JOIN "Country" CN2 ON DR.id2_' || _item.txt2 || ' = CN2.id';
				_query = _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment = 'CN';
			END IF;
			_item.id = _index;
			_item.entity = 'RS';
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
		_query = _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
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
			IF _entity = 'DT' THEN
				_item.date1 := to_date(_date1, 'DD/MM/YYYY');
				_item.date2 := to_date(_date2, 'DD/MM/YYYY');
			END IF;
			SELECT * INTO _rs FROM "Result" RS WHERE RS.id = _item.id_item;
			SELECT "GetRank"(_rs, _type1, _array_id) INTO _item.count1;
			_item.id = _index;
			_item.entity = 'RS';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Athletes]
	IF (_entity ~ 'CN|SP|TM' AND (_entity_ref = 'PR' OR _entity_ref = '')) THEN
		_query = 'SELECT DISTINCT ON (PR.last_name, PR.first_name, CN.id, SP.id) PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "Athlete" PR';
		_query = _query || ' LEFT JOIN "Country" CN ON PR.id_country = CN.id';
		_query = _query || ' LEFT JOIN "Sport" SP ON PR.id_sport = SP.id';
		_query = _query || ' LEFT JOIN "Team" TM ON PR.id_team = TM.id';
		IF _entity = 'CN' THEN
			_query = _query || ' WHERE PR.id_country = ' || _id;
		ELSIF _entity = 'SP' THEN
			_query = _query || ' WHERE PR.id_sport = ' || _id;
		ELSIF _entity = 'TM' THEN
			_query = _query || ' WHERE PR.id_team IN (' || _tm_list || ')';
		END IF;
		_query = _query || ' ORDER BY PR.last_name, PR.first_name, SP.id LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'PR';
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
		_query = _query || ' ORDER BY CT.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
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
		_query = _query || ' ORDER BY CX.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
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
		_query = _query || ' LEFT JOIN "Type" TP ON EV.id_type = TP.id';
		_query = _query || ' WHERE TP.number BETWEEN ' || _type1 || ' AND ' || _type2;
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
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
  
  
  
  
  
  
  CREATE OR REPLACE FUNCTION "GetRank"(_rs "Result", _type smallint, _ids integer[])
  RETURNS smallint AS
$BODY$
DECLARE
	_double boolean;
	_triple boolean;
	_double_triple boolean;
BEGIN
	IF (_type = 4 OR _rs.comment = '#DOUBLE#')
	THEN _double := true; ELSE _double := false; END IF;
	IF (_type = 5 OR _rs.comment = '#TRIPLE#')
	THEN _triple := true; ELSE _triple := false; END IF;
	IF (_type IN (4, 5) OR _rs.comment IN ('#DOUBLE#', '#TRIPLE#'))
	THEN _double_triple := true; ELSE _double_triple := false; END IF;
	
	IF _rs.id_rank1 = ANY(_ids) OR
	  (_rs.id_rank2 = ANY(_ids) AND (_double_triple = true OR _rs.exa ~* '(^|/)1-(2|3|4|5|6|7|8|9|10)(/|$)')) OR
	  (_rs.id_rank3 = ANY(_ids) AND (_triple = true OR _rs.exa ~* '(^|/)1-(3|4|5|6|7|8|9|10)(/|$)')) OR
	  (_rs.id_rank4 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(4|5|6|7|8|9|10)(/|$)') OR
	  (_rs.id_rank5 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(5|6|7|8|9|10)(/|$)') OR
	  (_rs.id_rank6 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(6|7|8|9|10)(/|$)') OR
	  (_rs.id_rank7 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(7|8|9|10)(/|$)') OR
	  (_rs.id_rank8 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(8|9|10)(/|$)') OR
	  (_rs.id_rank9 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(9|10)(/|$)') OR
	  (_rs.id_rank10 = ANY(_ids) AND _rs.exa ~* '(^|/)1-10(/|$)') THEN
		RETURN 1;
	ELSIF (_rs.id_rank2 = ANY(_ids) AND _double_triple = false) OR
	  (_rs.id_rank3 = ANY(_ids) AND ((_double = true OR _rs.exa ~* '(^|/)2-(3|4|5|6|7|8|9|10)(/|$)') AND _triple = false)) OR
	  (_rs.id_rank4 = ANY(_ids) AND (_double_triple = true OR _rs.exa ~* '(^|/)2-(4|5|6|7|8|9|10)(/|$)')) OR 
	  (_rs.id_rank5 = ANY(_ids) AND (_triple = true OR _rs.exa ~* '(^|/)2-(5|6|7|8|9|10)(/|$)')) OR
	  (_rs.id_rank6 = ANY(_ids) AND (_triple = true OR _rs.exa ~* '(^|/)2-(6|7|8|9|10)(/|$)')) OR
	  (_rs.id_rank7 = ANY(_ids) AND _rs.exa ~* '(^|/)2-(7|8|9|10)(/|$)') OR
	  (_rs.id_rank8 = ANY(_ids) AND _rs.exa ~* '(^|/)2-(8|9|10)(/|$)') OR
	  (_rs.id_rank9 = ANY(_ids) AND _rs.exa ~* '(^|/)2-(9|10)(/|$)') OR
	  (_rs.id_rank10 = ANY(_ids) AND _rs.exa ~* '(^|/)2-10(/|$)') THEN
		RETURN 2;
	ELSIF (_rs.id_rank3 = ANY(_ids) AND _double_triple = false) OR
	  (_rs.id_rank4 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)3-(4|5|6|7|8|9|10)(/|$)') OR
	  (_rs.id_rank5 = ANY(_ids) AND _triple = false AND (_double = true OR _rs.exa ~* '(^|/)3-(5|6|7|8|9|10)(/|$)')) OR
	  (_rs.id_rank6 = ANY(_ids) AND _triple = false AND (_double = true OR _rs.exa ~* '(^|/)3-(6|7|8|9|10)(/|$)')) OR
	  (_rs.id_rank7 = ANY(_ids) AND (_triple = true OR _rs.exa ~* '(^|/)3-(7|8|9|10)(/|$)')) OR
	  (_rs.id_rank8 = ANY(_ids) AND (_triple = true OR _rs.exa ~* '(^|/)3-(8|9|10)(/|$)')) OR
	  (_rs.id_rank9 = ANY(_ids) AND (_triple = true OR _rs.exa ~* '(^|/)3-(9|10)(/|$)')) OR
	  (_rs.id_rank10 = ANY(_ids) AND _rs.exa ~* '(^|/)3-10(/|$)') THEN
		RETURN 3;
	END IF;
	IF (_rs.id_rank4 IS NULL) THEN
		RETURN 0;
	END IF;
	IF (_rs.id_rank4 = ANY(_ids) AND _double_triple = false) OR
	  (_rs.id_rank5 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)4-(5|6|7|8|9|10)(/|$)') OR
	  (_rs.id_rank6 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)4-(6|7|8|9|10)(/|$)') OR
	  (_rs.id_rank7 = ANY(_ids) AND (_double = true OR _rs.exa ~* '(^|/)4-(7|8|9|10)(/|$)')) OR
	  (_rs.id_rank8 = ANY(_ids) AND (_double = true OR _rs.exa ~* '(^|/)4-(8|9|10)(/|$)')) OR
	  (_rs.id_rank9 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)4-(9|10)(/|$)') OR
	  (_rs.id_rank10 = ANY(_ids) AND _rs.exa ~* '(^|/)4-10(/|$)') THEN
		RETURN 4;
	END IF;
	IF (_rs.id_rank5 IS NULL) THEN
		RETURN 0;
	END IF;
	IF (_rs.id_rank5 = ANY(_ids) AND _double_triple = false) OR
	  (_rs.id_rank6 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)5-(6|7|8|9|10)(/|$)') OR
	  (_rs.id_rank7 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)5-(7|8|9|10)(/|$)') OR
	  (_rs.id_rank8 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)5-(8|9|10)(/|$)') OR
	  (_rs.id_rank9 = ANY(_ids) AND (_double = true OR _rs.exa ~* '(^|/)5-(9|10)(/|$)')) OR
	  (_rs.id_rank10 = ANY(_ids) AND _rs.exa ~* '(^|/)5-10(/|$)') THEN
		RETURN 5;
	END IF;
	IF (_rs.id_rank6 IS NULL) THEN
		RETURN 0;
	END IF;
	IF (_rs.id_rank6 = ANY(_ids) AND _double_triple = false) OR
	  (_rs.id_rank7 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)6-(7|8|9|10)(/|$)') OR
	  (_rs.id_rank8 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)6-(8|9|10)(/|$)') OR
	  (_rs.id_rank9 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)6-(9|10)(/|$)') OR
	  (_rs.id_rank10 = ANY(_ids) AND _rs.exa ~* '(^|/)6-10(/|$)') THEN
		RETURN 6;
	END IF;
	IF (_rs.id_rank7 IS NULL) THEN
		RETURN 0;
	END IF;
	IF (_rs.id_rank7 = ANY(_ids) AND _double_triple = false) OR
	  (_rs.id_rank8 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)7-(8|9|10)(/|$)') OR
	  (_rs.id_rank9 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)7-(9|10)(/|$)') OR
	  (_rs.id_rank10 = ANY(_ids) AND _rs.exa ~* '(^|/)7-10(/|$)') THEN
		RETURN 7;
	END IF;
	IF (_rs.id_rank8 IS NULL) THEN
		RETURN 0;
	END IF;
	IF (_rs.id_rank8 = ANY(_ids) AND _double_triple = false) OR
	  (_rs.id_rank9 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)8-(9|10)(/|$)') OR
	  (_rs.id_rank10 = ANY(_ids) AND _rs.exa ~* '(^|/)8-10(/|$)') THEN
		RETURN 8;
	END IF;
	IF (_rs.id_rank9 IS NULL) THEN
		RETURN 0;
	END IF;
	IF (_rs.id_rank9 = ANY(_ids) AND _double_triple = false) OR
	  (_rs.id_rank10 = ANY(_ids) AND _rs.exa ~* '(^|/)9-10(/|$)') THEN
		RETURN 9;
	END IF;
	IF (_rs.id_rank10 IS NULL) THEN
		RETURN 0;
	END IF;
	IF (_rs.id_rank10 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 10;
	END IF;
	IF (_rs.id_rank11 IS NULL) THEN
		RETURN 0;
	END IF;
	IF (_rs.id_rank11 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 11;
	ELSIF (_rs.id_rank12 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 12;
	ELSIF (_rs.id_rank13 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 13;
	ELSIF (_rs.id_rank14 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 14;
	ELSIF (_rs.id_rank15 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 15;
	ELSIF (_rs.id_rank16 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 16;
	ELSIF (_rs.id_rank17 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 17;
	ELSIF (_rs.id_rank18 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 18;
	ELSIF (_rs.id_rank19 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 19;
	ELSIF (_rs.id_rank20 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 20;
	END IF;
	RETURN 0;
END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  
  
  
  
CREATE SEQUENCE "SeqCalendar";


CREATE TABLE "Calendar"
(
  id integer NOT NULL not null primary key,
  id_sport integer NOT NULL,
  id_championship integer NOT NULL,
  id_event integer,
  id_subevent integer,
  id_subevent2 integer,
  id_complex integer,
  id_city integer,
  date1 character varying(10) DEFAULT NULL::character varying,
  date2 character varying(10) DEFAULT NULL::character varying,
  id_contributor integer NOT NULL,
first_update timestamp without time zone NOT NULL DEFAULT now(),
  last_update timestamp without time zone NOT NULL DEFAULT now()
);






CREATE OR REPLACE FUNCTION "GetCalendarResults"(_date1 character varying, _date2 character varying, _lang character varying)
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
	_cn1 varchar(5);_cn2 varchar(5);_cn3 varchar(5);_cn4 varchar(5);_cn5 varchar(5);_cn6 varchar(5);
	_tm1 varchar(60);_tm2 varchar(60);_tm3 varchar(60);_tm4 varchar(60);_tm5 varchar(60);_tm6 varchar(60);
begin
	_where := ' WHERE to_date(date2, ''DD/MM/YYYY'') >= to_date(''' || _date1 || ''', ''YYYYMMDD'') AND to_date(date2, ''DD/MM/YYYY'') <= to_date(''' || _date2 || ''', ''YYYYMMDD'')';

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
		IF _date1 IS NOT NULL AND _date1 <> '' THEN
			_item.date1 := to_date(_date1, 'DD/MM/YYYY');
		END IF;
		IF _date2 IS NOT NULL AND _date2 <> '' THEN
			_item.date2 := to_date(_date2, 'DD/MM/YYYY');
		END IF;
		_item.id := _index;
		_item.entity := 'RS';
		RETURN NEXT _item;
		_index = _index + 1;
	END LOOP;
	CLOSE _c;

	-- Future events
	_query := 'SELECT CL.id, NULL, NULL, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, CX.id, CX.label' || _lang || ', CX.label, CT1.id, CT1.label' || _lang || ' || '', '' || CN1.code, CT1.label, CT2.id, CT2.label' || _lang || ' || '', '' || CN2.code, CT2.label, CL.date1, CL.date2';
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
	_query := _query || ' LEFT JOIN "Country" CN2 ON CT2.id_country = CN2.id' || _where;
	OPEN _c FOR EXECUTE _query;
	LOOP
		FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _item.id_rel6, _item.label_rel6, _item.label_rel9, _item.id_rel7, _item.label_rel7, _item.label_rel10, _item.id_rel8, _item.label_rel8, _item.label_rel11, _date1, _date2;
		EXIT WHEN NOT FOUND;
		IF _date1 IS NOT NULL AND _date1 <> '' THEN
			_item.date1 := to_date(_date1, 'DD/MM/YYYY');
		END IF;
		IF _date2 IS NOT NULL AND _date2 <> '' THEN
			_item.date2 := to_date(_date2, 'DD/MM/YYYY');
		END IF;
		_item.id := _index;
		_item.entity := 'CL';
		RETURN NEXT _item;
		_index = _index + 1;
	END LOOP;
	CLOSE _c;

	RETURN;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
  
  
  
  
  
  
  
  
  DROP FUNCTION "GetWinLoss"(integer, text);
  
  
  
  CREATE OR REPLACE FUNCTION "GetYearlyStats"(_id_championship integer, _years text, _categories text, _individual boolean, _team boolean, _lang varchar)
  RETURNS refcursor AS
$BODY$
declare
    _cr refcursor;
    _c text;
    _j text;
    _columns text;
    _joins text;
    _year_condition text;
    _category_condition text;
    _type_condition text;
begin
	INSERT INTO "~Request" VALUES (NEXTVAL('"~SeqRequest"'), 'US', 'ST-' || _id_championship, current_date);

	-- Set year condition ('All teams' = Empty condition)
	_year_condition := '';
	IF _years <> '0' THEN
		_year_condition := ' AND YR.id IN (' || _years || ')';
	END IF;
	-- Set category condition ('All categories' = Empty condition)
	_category_condition := '';
	IF _categories <> '0' THEN
		_category_condition := ' AND SE2.id IN (' || _categories || ')';
	END IF;
	-- Set type condition
	_type_condition := ' AND SE.label IN (''.''' || (CASE WHEN _individual = true THEN ',''Individual''' ELSE '' END) || (CASE WHEN _team = true THEN ',''Team''' ELSE '' END) || ')';

	_columns := '';
	_joins := '';
	FOR i IN 1..8 LOOP
	        _c := ', RS.id_rank# AS rs_rank#';
	        _c := _c || ', PR#.last_name || '', '' || PR#.first_name AS rs_person#, TM#.label AS rs_team#, PRTM#.id AS rs_idprteam#, PRTM#.label AS rs_prteam#';
	        _j := ' LEFT JOIN "Athlete" PR# ON RS.id_rank# = PR#.id';
	        _j := _j || ' LEFT JOIN "Team" TM# ON RS.id_rank# = TM#.id';
	        _j := _j || ' LEFT JOIN "Team" PRTM# ON PR#.id_team = PRTM#.id';
	        _columns := _columns || regexp_replace(_c, '#', CAST(i AS varchar), 'g');
	        _joins := _joins || regexp_replace(_j, '#', CAST(i AS varchar), 'g');
	END LOOP;

	-- Open cursor
	OPEN _cr FOR EXECUTE
	'SELECT
		RS.id AS rs_id, YR.id AS yr_id, YR.label AS yr_label, SE.id AS tp_id, SE.label' || _lang || ' AS tp_label, SE2.id AS ct_id, SE2.label' || _lang || ' AS ct_label, RS.exa AS rs_exa, result1, result2, result3' || _columns || '
	FROM
		"Result" RS
		LEFT JOIN "Year" YR ON RS.id_year = YR.id
		LEFT JOIN "Event" EV ON RS.id_event = EV.id
		LEFT JOIN "Event" SE ON RS.id_subevent = SE.id
		LEFT JOIN "Event" SE2 ON RS.id_subevent2 = SE2.id' || _joins || '
	WHERE
		EV.label like ''%Season Leaders%'' AND RS.id_championship = ' || _id_championship || _year_condition || _category_condition || _type_condition || '
	ORDER BY
		YR.label DESC, SE.label, SE2.index, SE2.label' || _lang;
	
	RETURN _cr;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  
  
  
  
  insert into "~Config" values ('max_contributor_sports', '5');
  
  
  
  