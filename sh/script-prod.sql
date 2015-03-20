CREATE OR REPLACE FUNCTION "ENTITY_REF"(_entity character varying, _id integer, _entity_ref character varying, _limit character varying, _offset integer, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_c refcursor;
	__c refcursor;
	_query text;
	_link integer;
	_pr_list varchar(50);
	_index integer;
	_type1 integer;
	_type2 integer;
	_type3 integer;
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
	_id11 integer;_id12 integer;_id13 integer;_id14 integer;
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

	-- References in: [Draws]
	IF (_entity ~ 'CN|PR|TM' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT DR.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', DR.id1_qf1, DR.id2_qf1, DR.id1_qf2, DR.id2_qf2, DR.id1_qf3, DR.id2_qf3, DR.id1_qf4, DR.id2_qf4, DR.id1_sf1, DR.id2_sf1, DR.id1_sf2, DR.id2_sf2, DR.id1_thd, DR.id2_thd, TP1.number, TP2.number, TP3.number FROM "DRAW" DR';
		_query := _query || ' LEFT JOIN "RESULT" RS ON DR.id_result = RS.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id';
		_query := _query || ' LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id';
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity ~ 'CN|TM' THEN
			_query := _query || ' AND (DR.id1_sf1 <> ' || _id || ' AND DR.id1_sf2 <> ' || _id || ') AND (DR.id1_qf1 = ' || _id || ' OR DR.id2_qf1 = ' || _id || ' OR DR.id1_qf2 = ' || _id || ' OR DR.id2_qf2 = ' || _id || ' OR DR.id1_qf3 = ' || _id || ' OR DR.id2_qf3 = ' || _id || ' OR DR.id1_qf4 = ' || _id || ' OR DR.id2_qf4 = ' || _id || ' OR DR.id2_sf1 = ' || _id || ' OR DR.id2_sf2 = ' || _id || ' OR DR.id1_thd = ' || _id || ' OR DR.id2_thd = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _pr_list || ') AND DR.id1_sf2 NOT IN (' || _pr_list || ')) AND (DR.id1_qf1 IN (' || _pr_list || ') OR DR.id2_qf1 IN (' || _pr_list || ') OR DR.id1_qf2 IN (' || _pr_list || ') OR DR.id2_qf2 IN (' || _pr_list || ') OR DR.id1_qf3 IN (' || _pr_list || ') OR DR.id2_qf3 IN (' || _pr_list || ') OR DR.id1_qf4 IN (' || _pr_list || ') OR DR.id2_qf4 IN (' || _pr_list || ') OR DR.id2_sf1 IN (' || _pr_list || ') OR DR.id2_sf2 IN (' || _pr_list || ') OR DR.id1_thd IN (' || _pr_list || ') OR DR.id2_thd IN (' || _pr_list || '))';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _id11, _id12, _id13, _id14, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF (_id13 = _id OR _id14 = _id) THEN
				_item.txt2 := 'thd';
			ELSIF (_id9 = _id OR _id10 = _id) THEN
				_item.txt2 := 'sf1';
			ELSIF (_id11 = _id OR _id12 = _id) THEN
				_item.txt2 := 'sf2';
			ELSIF (_id1 = _id OR _id2 = _id) THEN
				_item.txt2 := 'qf1';
			ELSIF (_id3 = _id OR _id4 = _id) THEN
				_item.txt2 := 'qf2';
			ELSIF (_id5 = _id OR _id6 = _id) THEN
				_item.txt2 := 'qf3';
			ELSIF (_id7 = _id OR _id8 = _id) THEN
				_item.txt2 := 'qf4';
			END IF;
			IF _type1 <= 10 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', PR1.last_name || '', '' || PR1.first_name, CN1.id, CN1.code, TM1.label, PR2.last_name || '', '' || PR2.first_name, CN2.id, CN2.code, TM2.label, DR.result_' || _item.txt2;
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "PERSON" PR1 ON DR.id1_' || _item.txt2 || ' = PR1.id LEFT JOIN "PERSON" PR2 ON DR.id2_' || _item.txt2 || ' = PR2.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.id_rel13, _cn2, _tm2, _item.txt1;
				CLOSE __c;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
			ELSIF _type1 = 50 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', TM1.label, TM2.label, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "TEAM" TM1 ON DR.id1_' || _item.txt2 || ' = TM1.id LEFT JOIN "TEAM" TM2 ON DR.id2_' || _item.txt2 || ' = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.txt1;
				CLOSE __c;
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', CN1.code, CN2.code, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "COUNTRY" CN1 ON DR.id1_' || _item.txt2 || ' = CN1.id LEFT JOIN "COUNTRY" CN2 ON DR.id2_' || _item.txt2 || ' = CN2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.txt1;
				CLOSE __c;
			END IF;
			_item.id = _index;
			_item.entity = 'RS';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
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
			_query := _query || ' AND (RS.id_rank1 IN (' || _pr_list || ') OR RS.id_rank2 IN (' || _pr_list || ') OR RS.id_rank3 IN (' || _pr_list || ') OR RS.id_rank4 IN (' || _pr_list || ') OR RS.id_rank5 IN (' || _pr_list || ') OR RS.id_rank6 IN (' || _pr_list || ') OR RS.id_rank7 IN (' || _pr_list || ') OR RS.id_rank8 IN (' || _pr_list || ') OR RS.id_rank9 IN (' || _pr_list || ') OR RS.id_rank10 IN (' || _pr_list || '))';
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
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
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
		_query := 'SELECT DISTINCT ON (PR.last_name, PR.first_name, CN.id, SP.id) PR.id, PR.last_name || '', '' || PR.first_name, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ' FROM "PERSON" PR';
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
		_query := _query || ' ORDER BY PR.last_name, PR.first_name, SP.id LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2;
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
		_query := _query || ' ORDER BY SP.label' || _lang || ', TM.label LIMIT ' || _limit || ' OFFSET ' || _offset;
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
		_query := 'SELECT CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ' FROM "CITY" CT';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'CN' THEN
			_query := _query || ' WHERE CT.id_country = ' || _id;
		ELSIF _entity = 'ST' THEN
			_query := _query || ' WHERE CT.id_state = ' || _id;
		END IF;
		_query := _query || ' ORDER BY CT.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1;
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
		_query := 'SELECT CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ' FROM "COMPLEX" CX';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' WHERE CX.id_city = ' || _id;
		_query := _query || ' ORDER BY CX.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2;
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
		_query := 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', OL.type FROM "OLYMPICS" OL';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query := _query || ' WHERE OL.id_city = ' || _id;
		END IF;
		_query := _query || ' ORDER BY OL.type, YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel4, _item.label_rel4, _item.comment;
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
		_query := _query || ' ORDER BY YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
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
			_query := _query || ' AND (RC.id_rank1 IN (' || _pr_list || ') OR RC.id_rank2 IN (' || _pr_list || ') OR RC.id_rank3 IN (' || _pr_list || ') OR RC.id_rank4 IN (' || _pr_list || ') OR RC.id_rank5 IN (' || _pr_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RC.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RC.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RC.id_event = ' || _id || ' OR RC.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND RC.id_city = ' || _id;
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', RC.index LIMIT ' || _limit || ' OFFSET ' || _offset;
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
			_query := _query || ' WHERE HF.id_person IN (' || _pr_list || ')';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
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
			_query := _query || ' WHERE RN.id_person IN (' || _pr_list || ')';
		END IF;
		_query := _query || ' ORDER BY TM.label, RN.number LIMIT ' || _limit || ' OFFSET ' || _offset;
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
		_query := _query || ' ORDER BY TM.label, TS.date1 DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
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
		_query := _query || ' ORDER BY TM.label DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
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

  
   
  
ALTER TABLE "RECORD" ALTER COLUMN record1 TYPE varchar(20);
ALTER TABLE "RECORD" ALTER COLUMN record2 TYPE varchar(20);
ALTER TABLE "RECORD" ALTER COLUMN record3 TYPE varchar(20);
ALTER TABLE "RECORD" ALTER COLUMN record4 TYPE varchar(20);
ALTER TABLE "RECORD" ALTER COLUMN record5 TYPE varchar(20);
ALTER TABLE "RECORD" ALTER COLUMN date1 TYPE varchar(30);
ALTER TABLE "RECORD" ALTER COLUMN date2 TYPE varchar(30);
ALTER TABLE "RECORD" ALTER COLUMN date3 TYPE varchar(30);
ALTER TABLE "RECORD" ALTER COLUMN date4 TYPE varchar(30);
ALTER TABLE "RECORD" ALTER COLUMN date5 TYPE varchar(30);



ALTER TABLE "RECORD" ALTER COLUMN type2 TYPE varchar(15);
UPDATE "RECORD" SET type2='Alltime/Career' WHERE type2 in ('Career', 'History');



UPDATE "TYPE" SET label_fr='Individuel' where number=1;
UPDATE "TYPE" SET label_fr='Doubles' where number=4;
UPDATE "TYPE" SET label_fr='Equipes' where number=50;
UPDATE "TYPE" SET label_fr='Pays' where number=99;





