ALTER TABLE "~REF_ITEM" ADD id_rel12 integer;
ALTER TABLE "~REF_ITEM" ADD id_rel13 integer;
ALTER TABLE "~REF_ITEM" ADD id_rel14 integer;
ALTER TABLE "~REF_ITEM" ADD id_rel15 integer;
ALTER TABLE "~REF_ITEM" ADD id_rel16 integer;
ALTER TABLE "~REF_ITEM" ADD id_rel17 integer;

CREATE OR REPLACE FUNCTION "ENTITY_REF"(_entity character varying, _id integer, _entity_ref character varying)
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
	IF (_entity ~ 'CN|PR|TM|CP|EV|CT|SP|CX|YR' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label, CP.id, CP.label, EV.id, EV.label, SE.id, SE.label, RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number FROM "RESULT" RS';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id';
		_query := _query || ' LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id';
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity ~ 'CN|TM' THEN
			_query := _query || ' AND (RS.id_rank1 = ' || _id || ' OR RS.id_rank2 = ' || _id || ' OR RS.id_rank3 = ' || _id || ' OR RS.id_rank4 = ' || _id || ' OR RS.id_rank5 = ' || _id || ' OR RS.id_rank6 = ' || _id || ' OR RS.id_rank7 = ' || _id || ' OR RS.id_rank8 = ' || _id || ' OR RS.id_rank9 = ' || _id || ' OR RS.id_rank10 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RS.id_rank1 in (' || _pr_list || ') OR RS.id_rank2 in (' || _pr_list || ') OR RS.id_rank3 in (' || _pr_list || ') OR RS.id_rank4 in (' || _pr_list || ') OR RS.id_rank5 in (' || _pr_list || ') OR RS.id_rank6 in (' || _pr_list || ') OR RS.id_rank7 in (' || _pr_list || ') OR RS.id_rank8 in (' || _pr_list || ') OR RS.id_rank9 in (' || _pr_list || ') OR RS.id_rank10 in (' || _pr_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND RS.id_city = ' || _id;
		ELSIF _entity = 'CX' THEN
			_query := _query || ' AND RS.id_complex = ' || _id;
		ELSIF _entity = 'YR' THEN
			_query := _query || ' AND RS.id_year = ' || _id;
		END IF;
		_query := _query || ' ORDER BY SP.label, CP.index, EV.index, SE.index, CP.label, EV.label, SE.label, YR.id';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2;
			EXIT WHEN NOT FOUND;
			IF _type2 IS NOT NULL THEN
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
					_item.txt4 = '1-2;3-4;5-6';
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
		_query := 'SELECT PR.id, PR.last_name || '', '' || PR.first_name, CN.id, CN.label, SP.id, SP.label, TM.id, TM.label FROM "PERSON" PR';
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
		_query := 'SELECT TM.id, TM.label, CN.id, CN.label, SP.id, SP.label FROM "TEAM" TM';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON TM.id_country = CN.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON TM.id_sport = SP.id';
		IF _entity = 'SP' THEN
			_query := _query || ' WHERE TM.id_sport = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query := _query || ' WHERE TM.id_country = ' || _id;
		END IF;
		_query := _query || ' ORDER BY SP.label, TM.label';
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
		_query := 'SELECT CT.id, CT.label, ST.id, ST.label, CN.id, CN.label FROM "CITY" CT';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'CN' THEN
			_query := _query || ' WHERE CT.id_country = ' || _id;
		ELSIF _entity = 'ST' THEN
			_query := _query || ' WHERE CT.id_state = ' || _id;
		END IF;
		_query := _query || ' ORDER BY CT.label';
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
		_query := 'SELECT CX.id, CX.label, CT.id, CT.label, ST.id, ST.label, CN.id, CN.label FROM "COMPLEX" CX';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' WHERE CX.id_city = ' || _id;
		_query := _query || ' ORDER BY CX.label';
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
		_query := 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label, ST.id, ST.label, CN.id, CN.label, OL.type FROM "OLYMPICS" OL';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query := _query || ' WHERE OL.id_city = ' || _id;
		END IF;
		_query := _query || ' ORDER BY OL.type, YR.id';
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
		_query := 'SELECT OR_.id, OL.id, YR.id, YR.label, CT.id, CT.label, CN.id, CN.label, OR_.count_gold || '','' || OR_.count_silver || '','' || OR_.count_bronze FROM "OLYMPIC_RANKING" OR_';
		_query := _query || ' LEFT JOIN "OLYMPICS" OL ON OR_.id_olympics = OL.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON OR_.id_country = CN.id';
		IF _entity = 'OL' THEN
			_query := _query || ' WHERE OR_.id_olympics = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query := _query || ' WHERE OR_.id_country = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id';
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
		_query := 'SELECT RC.id, RC.label, SP.id, SP.label, CP.id, CP.label, EV.id, EV.label, SE.id, SE.label, RC.type1, RC.type2, RC.id_rank1, RC.id_rank2, RC.id_rank3, RC.id_rank4, RC.id_rank5 FROM "RECORD" RC';
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
		_query := _query || ' ORDER BY SP.label, CP.label, EV.label, SE.label, RC.index';
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
		_query := _query || ' ORDER BY YR.id';
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
		_query := 'SELECT TS.id, TM.id, TM.label, CX.id, CX.label, CT.id, CT.label, ST.id, ST.label, CN.id, CN.label, LG.label, TS.date1, TS.date2 FROM "TEAM_STADIUM" TS';
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
ALTER FUNCTION "ENTITY_REF"(character varying, integer, character varying) OWNER TO postgres;

CREATE OR REPLACE FUNCTION "SEARCH"(_pattern character varying, _scope character varying, _ref smallint)
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
	_scopes varchar(2)[];
	_tables varchar(15)[];
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
	__pattern := replace(__pattern, 'd', '(d|Đ)');
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
	_scopes = '{CP,CT,CX,CN,EV,PR,SP,ST,TM,YR}';
	_tables = '{CHAMPIONSHIP,CITY,COMPLEX,COUNTRY,EVENT,PERSON,SPORT,STATE,TEAM,YEAR}';
	FOR _s IN SELECT UNNEST(_scopes) LOOP
		IF _scope ~ ('(^|,)' || _s || '($|,)') THEN
			_rel_cols := '';
			_rel_joins := '';
			_rel_count := 0;

			-- Get related fields
			IF (_s ~ 'PR|TM') THEN -- Relation: Country
				_rel_cols := _rel_cols || ', CN.id, CN.label || '' ('' || CN.code || '')''';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s ~ 'PR|TM') THEN -- Relation: Sport
				_rel_cols := _rel_cols || ', SP.id, SP.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "SPORT" SP ON ' || _s || '.id_sport = SP.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'PR') THEN -- Relation: Team
				_rel_cols := _rel_cols || ', TM.id, TM.label, PR.link';
				_rel_joins := _rel_joins || ' LEFT JOIN "TEAM" TM ON ' || _s || '.id_team = TM.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'CX') THEN -- Relation: City/State/Country
				_rel_cols := _rel_cols || ', CT.id, CT.label';
				_rel_cols := _rel_cols || ', ST.id, ST.label';
				_rel_cols := _rel_cols || ', CN.id, CN.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "CITY" CT ON ' || _s || '.id_city = CT.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			IF (_s = 'CT') THEN -- Relation: State/Country
				_rel_cols := _rel_cols || ', NULL, NULL';
				_rel_cols := _rel_cols || ', ST.id, ST.label';
				_rel_cols := _rel_cols || ', CN.id, CN.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "STATE" ST ON ' || _s || '.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			FOR _j IN 1.._rel_count LOOP
				_rel_cols := _rel_cols || ', NULL, NULL';
			END LOOP;

			-- Execute query
			_query := 'SELECT ' || _s || '.id, ' || _s || '.label' || _rel_cols || ' FROM "' || _tables[_i] || '" ' || _s;
			_query := _query || _rel_joins || ' WHERE ' || _s || '.label ~* ''' || __pattern || ''' ORDER BY ' || _s || '.label';
			IF _s = 'PR' THEN
				_query := 'SELECT PR.id, PR.last_name || '', '' || PR.first_name' || _rel_cols || ' FROM "PERSON" PR' || _rel_joins;
				_query := _query || ' WHERE (PR.link = 0 OR PR.link IS NULL) AND (PR.first_name || '' '' || PR.last_name ~* ''' || __pattern || ''' OR PR.last_name ~* ''' || __pattern || ''' OR PR.first_name ~* ''' || __pattern || ''')';
				_query := _query || ' ORDER BY PR.last_name, PR.first_name';
			END IF;
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _current_id, _current_label, _current_id_rel1, _current_label_rel1, _current_id_rel2, _current_label_rel2, _current_id_rel3, _current_label_rel3, _current_link;
				EXIT WHEN NOT FOUND;
				_item.id = _index;
				_item.id_item = _current_id;
				_item.label = _current_label;
				_item.entity = _s;
				IF _ref = 1 THEN
					SELECT "COUNT_REF"(_s, _current_id) INTO _item.count_ref;
				END IF;
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
ALTER FUNCTION "SEARCH"(character varying, character varying, smallint) OWNER TO inachos;

ALTER TABLE "CHAMPIONSHIP" add first_update timestamp without time zone NOT NULL DEFAULT now();
ALTER TABLE "CITY" add first_update timestamp without time zone NOT NULL DEFAULT now();
ALTER TABLE "COMPLEX" add first_update timestamp without time zone NOT NULL DEFAULT now();
ALTER TABLE "COUNTRY" add first_update timestamp without time zone NOT NULL DEFAULT now();
ALTER TABLE "DRAW" add first_update timestamp without time zone NOT NULL DEFAULT now();
ALTER TABLE "EVENT" add first_update timestamp without time zone NOT NULL DEFAULT now();
ALTER TABLE "HALL_OF_FAME" add first_update timestamp without time zone NOT NULL DEFAULT now();
ALTER TABLE "LEAGUE" add first_update timestamp without time zone NOT NULL DEFAULT now();
ALTER TABLE "OLYMPICS" add first_update timestamp without time zone NOT NULL DEFAULT now();
ALTER TABLE "OLYMPIC_RANKING" add first_update timestamp without time zone NOT NULL DEFAULT now();
ALTER TABLE "PERSON" add first_update timestamp without time zone NOT NULL DEFAULT now();
ALTER TABLE "RECORD" add first_update timestamp without time zone NOT NULL DEFAULT now();
ALTER TABLE "RESULT" add first_update timestamp without time zone NOT NULL DEFAULT now();
ALTER TABLE "RETIRED_NUMBER" add first_update timestamp without time zone NOT NULL DEFAULT now();
ALTER TABLE "SPORT" add first_update timestamp without time zone NOT NULL DEFAULT now();
ALTER TABLE "STATE" add first_update timestamp without time zone NOT NULL DEFAULT now();
ALTER TABLE "TEAM" add first_update timestamp without time zone NOT NULL DEFAULT now();
ALTER TABLE "TEAM_STADIUM" add first_update timestamp without time zone NOT NULL DEFAULT now();
ALTER TABLE "TYPE" add first_update timestamp without time zone NOT NULL DEFAULT now();
ALTER TABLE "WIN_LOSS" add first_update timestamp without time zone NOT NULL DEFAULT now();
ALTER TABLE "YEAR" add first_update timestamp without time zone NOT NULL DEFAULT now();
UPDATE "CHAMPIONSHIP" SET first_update=last_update;
UPDATE "CITY" SET first_update=last_update;
UPDATE "COMPLEX" SET first_update=last_update;
UPDATE "COUNTRY" SET first_update=last_update;
UPDATE "DRAW" SET first_update=last_update;
UPDATE "EVENT" SET first_update=last_update;
UPDATE "HALL_OF_FAME" SET first_update=last_update;
UPDATE "LEAGUE" SET first_update=last_update;
UPDATE "OLYMPICS" SET first_update=last_update;
UPDATE "OLYMPIC_RANKING" SET first_update=last_update;
UPDATE "PERSON" SET first_update=last_update;
UPDATE "RECORD" SET first_update=last_update;
UPDATE "RESULT" SET first_update=last_update;
UPDATE "RETIRED_NUMBER" SET first_update=last_update;
UPDATE "SPORT" SET first_update=last_update;
UPDATE "STATE" SET first_update=last_update;
UPDATE "TEAM" SET first_update=last_update;
UPDATE "TEAM_STADIUM" SET first_update=last_update;
UPDATE "TYPE" SET first_update=last_update;
UPDATE "WIN_LOSS" SET first_update=last_update;
UPDATE "YEAR" SET first_update=last_update;

CREATE OR REPLACE FUNCTION "~LAST_UPDATES"(_count integer)
  RETURNS refcursor AS
$BODY$
declare
	_c refcursor;
begin
	OPEN _c FOR EXECUTE
	'SELECT RS.id AS rs_id, YR.label AS yr_label, SP.label AS sp_label, CP.label AS cp_label, EV.label AS ev_label, SE.label AS se_label, RS.first_update AS rs_update, TP1.number as tp1_number, TP2.number AS tp2_number, PR.first_name AS pr_first_name, PR.last_name AS pr_last_name, TM.label AS tm_label, CN.code AS cn_code FROM "RESULT" RS
		LEFT JOIN "YEAR" YR ON RS.id_year=YR.id
		LEFT JOIN "SPORT" SP ON RS.id_sport=SP.id
		LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship=CP.id
		LEFT JOIN "EVENT" EV ON RS.id_event=EV.id
		LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id
		LEFT JOIN "TYPE" TP1 ON EV.id_type=TP1.id
		LEFT JOIN "TYPE" TP2 ON SE.id_type=TP2.id
		LEFT JOIN "PERSON" PR ON RS.id_rank1=PR.id
		LEFT JOIN "TEAM" TM ON RS.id_rank1=TM.id
		LEFT JOIN "COUNTRY" CN ON RS.id_rank1=CN.id
	ORDER BY RS.first_update DESC LIMIT ' || _count;
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION "~LAST_UPDATES"(integer) OWNER TO postgres;

ALTER  TABLE "RECORD" ADD exa character varying(15);

update "RECORD" set comment = replace(comment, 'Times-', 'T-');
update "RECORD" set comment = replace(comment, 'Time-', 'T-');
update "RECORD" set comment = regexp_replace(comment, 'at-...', '', 'g');
update "RECORD" set comment = regexp_replace(comment, 'vs-...', '', 'g');
update "RECORD" set comment = regexp_replace(comment, 'vs-..', '', 'g');
update "RECORD" set comment = replace(comment, ';', '');

CREATE OR REPLACE FUNCTION "GET_US_RECORDS"(_id_championship integer, _events text, _subevents text, _types1 text, _types2 text)
  RETURNS refcursor AS
$BODY$
declare
    _cr refcursor;
    _c text;
    _j text;
    _columns text;
    _joins text;
    _event_condition text;
    _subevent_condition text;
    _type1_condition text;
    _type2_condition text;
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'US', 'RC-' || _id_championship, current_date);

	_event_condition := CASE WHEN _events <> '0' THEN ' AND RC.id_event IN (' || _events || ')' ELSE '' END;
	_subevent_condition := CASE WHEN _subevents <> '0' THEN ' AND RC.id_subevent IN (' || _subevents || ')' ELSE '' END;
	_type1_condition := CASE WHEN _types1 <> '0' THEN ' AND RC.type1 IN (' || _types1 || ')' ELSE '' END;
	_type2_condition := CASE WHEN _types2 <> '0' THEN ' AND RC.type2 IN (' || _types2 || ')' ELSE '' END;

	_columns := '';
	_joins := '';
	FOR i IN 1..5 LOOP
	        _c := ', RC.id_rank# AS rc_rank#, RC.record# AS rc_record#, RC.date# AS rc_date#';
	        _c := _c || ', PR#.last_name || '', '' || PR#.first_name AS rc_person#, TM#.label AS rc_team#, PRTM#.label AS rc_prteam#';
	        _j := ' LEFT JOIN "PERSON" PR# ON RC.id_rank# = PR#.id';
	        _j := _j || ' LEFT JOIN "TEAM" TM# ON RC.id_rank# = TM#.id';
	        _j := _j || ' LEFT JOIN "TEAM" PRTM# ON PR#.id_team = PRTM#.id';
	        _columns := _columns || regexp_replace(_c, '#', CAST(i AS varchar), 'g');
	        _joins := _joins || regexp_replace(_j, '#', CAST(i AS varchar), 'g');
	END LOOP;
	
	-- Open cursor
	OPEN _cr FOR EXECUTE
	'SELECT
		RC.id AS rc_id, RC.label AS rc_label, EV.id AS ev_id, EV.label AS ev_label, SE.id AS se_id, SE.label AS se_label,
		RC.type1 AS rc_type1, RC.type2 AS rc_type2, TP1.number AS rc_number1, TP2.number AS rc_number2, RC.exa AS rc_exa, RC.comment AS rc_comment' ||
		_columns || '
	FROM
		"RECORD" RC
		LEFT JOIN "EVENT" EV ON RC.id_event = EV.id
		LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id
		LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id
		LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id' ||
		_joins || '
	WHERE
		RC.id_championship = ' || _id_championship || _event_condition || _subevent_condition || _type1_condition || _type2_condition || '
	ORDER BY
		SE.index, EV.index, RC.type1, RC.type2, RC.index';
	
	RETURN  _cr;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION "GET_US_RECORDS"(integer, text, text, text, text) OWNER TO postgres;

update "RECORD" set exa='1-5' where record1 is not null and record2 is not null and record3 is not null and record4 is not null and record5 is not null and record1 = record2 and record1 = record3 and record1 = record4 and record1 = record5;
update "RECORD" set exa='1-4' where record1 is not null and record2 is not null and record3 is not null and record4 is not null and record5 is null and record1 = record2 and record1 = record3 and record1 = record4;
update "RECORD" set exa='1-3' where record1 is not null and record2 is not null and record3 is not null and record4 is null and record5 is null and record1 = record2 and record1 = record3;
update "RECORD" set exa='1-2' where record1 is not null and record2 is not null and record3 is null and record4 is null and record5 is null and record1 = record2;

CREATE OR REPLACE FUNCTION "GET_US_RECORDS"(_id_championship integer, _events text, _subevents text, _types1 text, _types2 text)
  RETURNS refcursor AS
$BODY$
declare
    _cr refcursor;
    _c text;
    _j text;
    _columns text;
    _joins text;
    _event_condition text;
    _subevent_condition text;
    _type1_condition text;
    _type2_condition text;
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'US', 'RC-' || _id_championship, current_date);

	_event_condition := CASE WHEN _events <> '0' THEN ' AND RC.id_event IN (' || _events || ')' ELSE '' END;
	_subevent_condition := CASE WHEN _subevents <> '0' THEN ' AND RC.id_subevent IN (' || _subevents || ')' ELSE '' END;
	_type1_condition := CASE WHEN _types1 <> '0' THEN ' AND RC.type1 IN (' || _types1 || ')' ELSE '' END;
	_type2_condition := CASE WHEN _types2 <> '0' THEN ' AND RC.type2 IN (' || _types2 || ')' ELSE '' END;

	_columns := '';
	_joins := '';
	FOR i IN 1..5 LOOP
	        _c := ', RC.id_rank# AS rc_rank#, RC.record# AS rc_record#, RC.date# AS rc_date#';
	        _c := _c || ', PR#.last_name || '', '' || PR#.first_name AS rc_person#, TM#.label AS rc_team#, PRTM#.id AS rc_idprteam#, PRTM#.label AS rc_prteam#';
	        _j := ' LEFT JOIN "PERSON" PR# ON RC.id_rank# = PR#.id';
	        _j := _j || ' LEFT JOIN "TEAM" TM# ON RC.id_rank# = TM#.id';
	        _j := _j || ' LEFT JOIN "TEAM" PRTM# ON PR#.id_team = PRTM#.id';
	        _columns := _columns || regexp_replace(_c, '#', CAST(i AS varchar), 'g');
	        _joins := _joins || regexp_replace(_j, '#', CAST(i AS varchar), 'g');
	END LOOP;
	
	-- Open cursor
	OPEN _cr FOR EXECUTE
	'SELECT
		RC.id AS rc_id, RC.label AS rc_label, EV.id AS ev_id, EV.label AS ev_label, SE.id AS se_id, SE.label AS se_label,
		RC.type1 AS rc_type1, RC.type2 AS rc_type2, TP1.number AS rc_number1, TP2.number AS rc_number2, RC.exa AS rc_exa, RC.comment AS rc_comment' ||
		_columns || '
	FROM
		"RECORD" RC
		LEFT JOIN "EVENT" EV ON RC.id_event = EV.id
		LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id
		LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id
		LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id' ||
		_joins || '
	WHERE
		RC.id_championship = ' || _id_championship || _event_condition || _subevent_condition || _type1_condition || _type2_condition || '
	ORDER BY
		SE.index, EV.index, RC.type1, RC.type2, RC.index';
	
	RETURN  _cr;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION "GET_US_RECORDS"(integer, text, text, text, text) OWNER TO postgres;
