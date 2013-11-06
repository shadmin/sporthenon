alter TABLE "RESULT" add EXA varchar(15);
update "RESULT" set comment = replace(comment, 'EX', '') where comment like '%EX%';
UPDATE "RESULT" set exa=comment  where comment is not null and comment !~* '[a-z]';
update "RESULT" set comment=null  where comment is not null and comment !~* '[a-z]';
update "RESULT" set exa='1', comment=null where id in (4547, 6621, 6640, 11719);
update "RESULT" set exa='1', comment=replace(comment, ';1', '') where id in (14526, 14381, 14509, 14500, 14526, 14520, 14499, 14533, 14512, 14535, 14513, 21040, 14489, 14536, 14496);
update "RESULT" set exa='1', comment=replace(comment, ';1', '') where id in (14503, 14532, 14495, 14521, 14501, 14505, 14511, 14494, 14460, 14538, 14522);
update "RESULT" set exa='2', comment=replace(comment, '2;', '') where id in (5922,10446,10758,10684,22143,10421,10425,10479,10505,10556);
update "RESULT" set exa='2', comment=null where id in (4906);
update "RESULT" set exa='3', comment=null where id in (10964);
update "RESULT" set exa='1-2', comment=replace(comment, ';1-2', '') where id in (14506,14497,14492,14518,14523,14515,14517,14525);
update "RESULT" set exa='2', comment=replace(comment, ';2', '') where id in (10438,10694,10508,10478,10525,10481);
update "RESULT" set exa='1-6', comment=replace(comment, '1-6;', '') where id in (18907);
update "RESULT" set exa='3', comment=replace(comment, '3;', '') where id in (3550,22138);
update "RESULT" set exa='2', comment=replace(comment, '2;', '') where id in (10512);
update "RESULT" set exa='2-3', comment=replace(comment, '2-3;', '') where id in (10697);
update "RESULT" set exa='3', comment=replace(comment, ';3', '') where id in (22153);
update "RESULT" set comment='Playoff' where id in (23915, 23907, 23914);
update "RESULT" set comment = null where id in (3550);
update "RESULT" set comment = '##Rank: Besseki' where id in (20671);
update "RESULT" set comment = '##Superbowl I' where id in (14332);
update "EVENT" set comment=null where comment ='';
update "RESULT" set comment = replace(comment, ';', '|') where comment ~* ';';
alter table "~REF_ITEM" add txt4 character varying(40);
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
begin
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
			IF _entity ~ 'CN|PR|TM' THEN
				IF _id1 = _id THEN _item.comment = '1';
				ELSIF _id2 = _id THEN _item.comment = '2';
				ELSIF _id3 = _id THEN _item.comment = '3';
				ELSIF _id4 = _id THEN _item.comment = '4';
				ELSIF _id5 = _id THEN _item.comment = '5';
				ELSIF _id6 = _id THEN _item.comment = '6';
				ELSIF _id7 = _id THEN _item.comment = '7';
				ELSIF _id8 = _id THEN _item.comment = '8';
				ELSIF _id9 = _id THEN _item.comment = '9';
				ELSIF _id10 = _id THEN _item.comment = '10'; END IF;
			END IF;
			IF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF _type1 <= 10 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, PR1.last_name || ', ' || PR1.first_name || ' (' || CN1.code || ')', PR2.last_name || ', ' || PR2.first_name || ' (' || CN2.code || ')', PR3.last_name || ', ' || PR3.first_name || ' (' || CN3.code || ')', PR4.last_name || ', ' || PR4.first_name || ' (' || CN4.code || ')', PR5.last_name || ', ' || PR5.first_name || ' (' || CN5.code || ')', RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "PERSON" PR1 ON RS.id_rank1 = PR1.id LEFT JOIN "PERSON" PR2 ON RS.id_rank2 = PR2.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3 = PR3.id LEFT JOIN "PERSON" PR4 ON RS.id_rank4 = PR4.id LEFT JOIN "PERSON" PR5 ON RS.id_rank5 = PR5.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "COUNTRY" CN3 ON PR3.id_country = CN3.id LEFT JOIN "COUNTRY" CN4 ON PR4.id_country = CN4.id LEFT JOIN "COUNTRY" CN5 ON PR5.id_country = CN5.id
				WHERE RS.id = _item.id_item;
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "TEAM" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "TEAM" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "TEAM" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "TEAM" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "TEAM" TM5 ON RS.id_rank5 = TM5.id
				WHERE RS.id = _item.id_item;
			ELSIF _type1 = 99 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, CN1.code, CN2.code, CN3.code, CN4.code, CN5.code, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "COUNTRY" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "COUNTRY" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "COUNTRY" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "COUNTRY" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "COUNTRY" CN5 ON RS.id_rank5 = CN5.id
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
	IF (_entity ~ 'SP|TM|CN' AND (_entity_ref = 'TM' OR _entity_ref = '')) THEN
		_query := 'SELECT TM.id, TM.label, CN.id, CN.label, SP.id, SP.label FROM "TEAM" TM';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON TM.id_country = CN.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON TM.id_sport = SP.id';
		IF _entity = 'SP' THEN
			_query := _query || ' WHERE TM.id_sport = ' || _id;
		ELSIF _entity = 'TM' THEN
			_query := _query || ' WHERE TM.id_parent = ' || _id;
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
		_query := 'SELECT HF.id, YR.id, YR.label, PR.id, PR.last_name || '', '' || PR.first_name, LG.label FROM "HALL_OF_FAME" HF';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.comment;
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
CREATE OR REPLACE FUNCTION "GET_OLYMPIC_MEDALS"(_olympics text, _id_sport integer, _events text, _subevents text)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _ol_type smallint;
    _ol_date1 varchar(10);
    _ol_date2 varchar(10);
    _id_year integer;
    _columns text;
    _joins text;
    _olympics_condition text;
    _event_condition text;
    _subevent_condition text;
begin
	-- Build entity columns/joins
	_columns := '';
	_joins := '';
	FOR i IN 1..5 LOOP
		-- Person
	        _columns := _columns || ', PR' || i || '.last_name AS pr' || i || '_last_name, PR' || i || '.first_name AS pr' || i || '_first_name';
	        _columns := _columns || ', PRCN' || i || '.id AS pr' || i || '_cn_id, PRCN' || i || '.code AS pr' || i || '_cn_code, PRCN' || i || '.label AS pr' || i || '_cn_label';
	        _joins := _joins || ' LEFT JOIN "PERSON" PR' || i || ' ON RS.id_rank' || i || ' = PR' || i || '.id';
	        _joins := _joins || ' LEFT JOIN "COUNTRY" PRCN' || i || ' ON PR' || i || '.id_country = PRCN' || i || '.id';
		-- Country
	        _columns := _columns || ', _CN' || i || '.code AS cn' || i || '_code, _CN' || i || '.label AS cn' || i || '_label';
	        _joins := _joins || ' LEFT JOIN "COUNTRY" _CN' || i || ' ON RS.id_rank' || i || ' = _CN' || i || '.id';
	END LOOP;

	-- Set year condition
	_olympics_condition := '';
	IF _olympics <> '0' THEN
		_olympics_condition := ' AND OL.id IN (' || _olympics || ')';
	END IF;

	-- Set event condition
	_event_condition := '';
	IF _events <> '0' THEN
		_event_condition := ' AND RS.id_event IN (' || _events || ')';
	END IF;

	-- Set subevent condition
	_subevent_condition := '';
	IF _subevents <> '0' THEN
		_subevent_condition := ' AND RS.id_subevent IN (' || _subevents || ')';
	END IF;
	
	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT 
		RS.id AS rs_id, EV.id AS ev_id, EV.label AS ev_label, SE.id AS se_id, SE.label AS se_label, YR.id as yr_id, YR.label as yr_label, RS.date1 AS rs_date1, RS.date2 AS rs_date2,
		CX.id AS cx_id, CX.label AS cx_label,CT1.id AS ct1_id, CT1.label AS ct1_label, CT2.id AS ct2_id, CT2.label AS ct2_label, ST1.id AS st1_id, ST1.code AS st1_code, ST1.label AS st1_label, ST2.id AS st2_id, ST2.code AS st2_code,
		ST2.label AS st2_label, CN1.id AS cn1_id, CN1.code AS cn1_code_, CN1.label AS cn1_label_, CN2.id AS cn2_id, CN2.code AS cn2_code_, CN2.label AS cn2_label_,
		RS.id_rank1 AS rs_rank1, RS.id_rank2 AS rs_rank2, RS.id_rank3 AS rs_rank3, RS.id_rank4 AS rs_rank4, RS.id_rank5 AS rs_rank5,
		RS.result1 AS rs_result1, RS.result2 AS rs_result2, RS.result3 AS rs_result3, TP1.number AS tp1_number, TP2.number AS tp2_number, OL.id AS ol_id, OL.type AS ol_type, OL.date1 AS ol_date1, OL.date2 AS ol_date2, CT3.label AS ol_city, RS.comment as rs_comment, RS.exa as rs_exa'
		|| _columns || '
	FROM
		"RESULT" RS
		LEFT JOIN "EVENT" EV ON RS.id_event = EV.id
		LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id
		LEFT JOIN "COMPLEX" CX ON RS.id_complex = CX.id
		LEFT JOIN "CITY" CT1 ON CX.id_city = CT1.id
		LEFT JOIN "CITY" CT2 ON RS.id_city = CT2.id
		LEFT JOIN "STATE" ST1 ON CT1.id_state = ST1.id
		LEFT JOIN "STATE" ST2 ON CT2.id_state = ST2.id
		LEFT JOIN "COUNTRY" CN1 ON CT1.id_country = CN1.id
		LEFT JOIN "COUNTRY" CN2 ON CT2.id_country = CN2.id
		LEFT JOIN "YEAR" YR ON RS.id_year = YR.id
		LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id
		LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id
		LEFT JOIN "OLYMPICS" OL ON RS.id_year = OL.id_year
		LEFT JOIN "CITY" CT3 ON OL.id_city = CT3.id'
		|| _joins || '
	WHERE 
		RS.id_championship = 1 AND RS.id_sport = ' || _id_sport
		|| _olympics_condition || _event_condition || _subevent_condition || '
	ORDER BY OL.id DESC, EV.id';
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION "GET_OLYMPIC_MEDALS"(text, integer, text, text) OWNER TO postgres;
CREATE OR REPLACE FUNCTION "GET_US_CHAMPIONSHIPS"(_id_championship integer, _years text)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _year_condition text;
begin
	_year_condition := CASE WHEN _years <> '0' THEN ' AND YR.id IN (' || _years || ')' ELSE '' END;
	
	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT
		RS.id AS rs_id, RS.date1 AS rs_date1, RS.date2 AS rs_date2, RS.id_rank1 AS rs_rank1, RS.id_rank2 AS rs_rank2, TM1.label as rs_team1, TM2.label as rs_team2, RS.result1 AS rs_result,
		RS.comment AS rs_comment, RS.exa AS rs_exa, YR.id AS yr_id, YR.label AS yr_label, CX.id AS cx_id, CX.label AS cx_label,
		CT.id AS ct_id, CT.label AS ct_label, ST.id AS st_id, ST.code AS st_code, ST.label AS st_label, CN.id AS cn_id, CN.code AS cn_code, CN.label AS cn_label
	FROM
		"RESULT" RS
		LEFT JOIN "TEAM" TM1 ON RS.id_rank1 = TM1.id
		LEFT JOIN "TEAM" TM2 ON RS.id_rank2 = TM2.id
		LEFT JOIN "YEAR" YR ON RS.id_year = YR.id
		LEFT JOIN "COMPLEX" CX ON RS.id_complex = CX.id
		LEFT JOIN "CITY" CT ON CX.id_city = CT.id
		LEFT JOIN "STATE" ST ON CT.id_state = ST.id
		LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id
	WHERE
		RS.id_championship = ' || _id_championship || ' AND 
		RS.id_event IN (455,532,572,621) AND (RS.id_subevent IS NULL OR RS.id_subevent IN (452,453,454,573,624)) ' || _year_condition || '
	ORDER BY RS.id_year';
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION "GET_US_CHAMPIONSHIPS"(integer, text) OWNER TO postgres;
CREATE OR REPLACE FUNCTION "GET_RESULTS"(_id_sport integer, _id_championship integer, _id_event integer, _id_subevent integer, _years text)
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
	IF _id_subevent <> 0 THEN
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
	        _columns := _columns || ', PRTM' || i || '.id AS en' || i || '_rel1_id, PRTM' || i || '.code AS en' || i || '_rel1_code, PRTM' || i || '.label AS en' || i || '_rel1_label';
	        _columns := _columns || ', PRCN' || i || '.id AS en' || i || '_rel2_id, PRCN' || i || '.code AS en' || i || '_rel2_code, PRCN' || i || '.label AS en' || i || '_rel2_label';
	        _joins := _joins || ' LEFT JOIN "PERSON" PR' || i || ' ON RS.id_rank' || i || ' = PR' || i || '.id';
	        _joins := _joins || ' LEFT JOIN "TEAM" PRTM' || i || ' ON PR' || i || '.id_team = PRTM' || i || '.id';
	        _joins := _joins || ' LEFT JOIN "COUNTRY" PRCN' || i || ' ON PR' || i || '.id_country = PRCN' || i || '.id';
	    ELSIF _type = 50 THEN -- Team
	        _columns := _columns || ', TM' || i || '.code AS en' || i || '_str1, TM' || i || '.label AS en' || i || '_str2';
	        _columns := _columns || ', NULL AS en' || i || '_rel1_id, NULL AS en' || i || '_rel1_code, NULL AS en' || i || '_rel1_label';
	        _columns := _columns || ', TMCN' || i || '.id AS en' || i || '_rel2_id, TMCN' || i || '.code AS en' || i || '_rel2_code, TMCN' || i || '.label AS en' || i || '_rel2_label';
	        _joins := _joins || ' LEFT JOIN "TEAM" TM' || i || ' ON RS.id_rank' || i || ' = TM' || i || '.id';
	        _joins := _joins || ' LEFT JOIN "COUNTRY" TMCN' || i || ' ON TM' || i || '.id_country = TMCN' || i || '.id';
	    ELSIF _type = 99 THEN -- Country
	        _columns := _columns || ', _CN' || i || '.code AS en' || i || '_str1, _CN' || i || '.label AS en' || i || '_str2';
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
		RS.result3 AS rs_result3, RS.result4 AS rs_result4, RS.result5 AS rs_result5, RS.comment AS rs_comment, RS.exa AS rs_exa, YR.id AS yr_id, YR.label AS yr_label, CX.id AS cx_id, CX.label AS cx_label,
		CT1.id AS ct1_id, CT1.label AS ct1_label, CT2.id AS ct2_id, CT2.label AS ct2_label, ST1.id AS st1_id, ST1.code AS st1_code, ST1.label AS st1_label, ST2.id AS st2_id, ST2.code AS st2_code,
		ST2.label AS st2_label, CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label AS cn1_label, CN2.id AS cn2_id, CN2.code AS cn2_code, CN2.label AS cn2_label, DR.id as dr_id' ||
		_columns || '
	FROM
		"RESULT" RS
		LEFT JOIN "YEAR" YR ON RS.id_year = YR.id
		LEFT JOIN "COMPLEX" CX ON RS.id_complex = CX.id
		LEFT JOIN "CITY" CT1 ON CX.id_city = CT1.id
		LEFT JOIN "CITY" CT2 ON RS.id_city = CT2.id
		LEFT JOIN "STATE" ST1 ON CT1.id_state = ST1.id
		LEFT JOIN "STATE" ST2 ON CT2.id_state = ST2.id
		LEFT JOIN "COUNTRY" CN1 ON CT1.id_country = CN1.id
		LEFT JOIN "COUNTRY" CN2 ON CT2.id_country = CN2.id
		LEFT JOIN "DRAW" DR ON DR.id_result = RS.id' ||
		_joins || '
	WHERE
		RS.id_sport = ' || _id_sport || ' AND
		RS.id_championship = ' || _id_championship ||
		_event_condition || _year_condition || '
	ORDER BY RS.id_year';
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION "GET_RESULTS"(integer, integer, integer, integer, text) OWNER TO postgres;
CREATE OR REPLACE FUNCTION "WIN_RECORDS"(_results text)
  RETURNS refcursor AS
$BODY$
declare
	_id1 integer;
	_id2 integer;
	_id3 integer;
	_id4 integer;
	_id5 integer;
	_id6 integer;
	_id7 integer;
	_id8 integer;
	_id9 integer;
	_id10 integer;
	_link1 integer;
	_link2 integer;
	_link3 integer;
	_link4 integer;
	_link5 integer;
	_link6 integer;
	_link7 integer;
	_link8 integer;
	_link9 integer;
	_link10 integer;
	_type smallint;
	_type2 smallint;
	_exa text;
	_win_str text;
	_query text;
	_c refcursor;
begin
	SELECT DISTINCT
		TP1.number, TP2.number
	INTO
		_type, _type2
	FROM
		"RESULT" RS
		LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id
		LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id
	WHERE
		RS.id = CAST(CASE WHEN POSITION(',' IN _results) > 0 THEN SUBSTRING(_results FROM 1 FOR POSITION(',' IN _results) - 1) ELSE _results END AS integer);
	IF _type2 IS NOT NULL THEN
		_type := _type2;
	END IF;

	DELETE FROM "~FUNC_BUFFER";
	_win_str := '';
	
	OPEN _c FOR EXECUTE '
	SELECT RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, RS.exa
	FROM "RESULT" RS
	WHERE RS.id IN (' || _results || ')';
	LOOP	
		FETCH _c INTO _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _exa;
		EXIT WHEN NOT FOUND;
		IF (_type < 10) THEN
			SELECT PR.link INTO _link1 FROM "PERSON" PR WHERE PR.id = _id1;IF _link1 IS NOT NULL and _link1 > 0 THEN _id1 := _link1; END IF;
			SELECT PR.link INTO _link2 FROM "PERSON" PR WHERE PR.id = _id2;IF _link2 IS NOT NULL and _link2 > 0 THEN _id2 := _link2; END IF;
			SELECT PR.link INTO _link3 FROM "PERSON" PR WHERE PR.id = _id3;IF _link3 IS NOT NULL and _link3 > 0 THEN _id3 := _link3; END IF;
			SELECT PR.link INTO _link4 FROM "PERSON" PR WHERE PR.id = _id4;IF _link4 IS NOT NULL and _link4 > 0 THEN _id4 := _link4; END IF;
			SELECT PR.link INTO _link5 FROM "PERSON" PR WHERE PR.id = _id5;IF _link5 IS NOT NULL and _link5 > 0 THEN _id5 := _link5; END IF;
			SELECT PR.link INTO _link6 FROM "PERSON" PR WHERE PR.id = _id6;IF _link6 IS NOT NULL and _link6 > 0 THEN _id6 := _link6; END IF;
			SELECT PR.link INTO _link7 FROM "PERSON" PR WHERE PR.id = _id7;IF _link7 IS NOT NULL and _link7 > 0 THEN _id7 := _link7; END IF;
			SELECT PR.link INTO _link8 FROM "PERSON" PR WHERE PR.id = _id8;IF _link8 IS NOT NULL and _link8 > 0 THEN _id8 := _link8; END IF;
			SELECT PR.link INTO _link9 FROM "PERSON" PR WHERE PR.id = _id9;IF _link9 IS NOT NULL and _link9 > 0 THEN _id9 := _link9; END IF;
			SELECT PR.link INTO _link10 FROM "PERSON" PR WHERE PR.id = _id10;IF _link10 IS NOT NULL and _link10 > 0 THEN _id10 := _link10; END IF;
		END IF;
		IF (_id1 IS NOT NULL) THEN
			IF _win_str !~ ('(^|,)' || _id1 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id1, 0, NULL); _win_str = _win_str || ',' || _id1; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id1;
		END IF;
		IF (_id2 IS NOT NULL AND (_type = 4 OR _exa ~ '(^|;)1.*(;|$)')) THEN
			IF _win_str !~ ('(^|,)' || _id2 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id2, 0, NULL); _win_str = _win_str || ',' || _id2; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id2;
		END IF;
		IF (_id3 IS NOT NULL AND _exa ~ '(^|;)1-(3|4|5|6|7|8)(;|$)') THEN
			IF _win_str !~ ('(^|,)' || _id3 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id3, 0, NULL); _win_str = _win_str || ',' || _id3; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id3;
		END IF;
		IF (_id4 IS NOT NULL AND _exa ~ '(^|;)1-(4|5|6|7|8)(;|$)') THEN
			IF _win_str !~ ('(^|,)' || _id4 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id4, 0, NULL); _win_str = _win_str || ',' || _id4; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id4;
		END IF;
		IF (_id5 IS NOT NULL AND _exa ~ '(^|;)1-(5|6|7|8)(;|$)') THEN
			IF _win_str !~ ('(^|,)' || _id5 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id5, 0, NULL); _win_str = _win_str || ',' || _id5; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id5;
		END IF;
		IF (_id6 IS NOT NULL AND _exa ~ '(^|;)1-(6|7|8)(;|$)') THEN
			IF _win_str !~ ('(^|,)' || _id6 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id6, 0, NULL); _win_str = _win_str || ',' || _id6; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id6;
		END IF;
		IF (_id7 IS NOT NULL AND _exa ~ '(^|;)1-(7|8)(;|$)') THEN
			IF _win_str !~ ('(^|,)' || _id7 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id7, 0, NULL); _win_str = _win_str || ',' || _id7; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id7;
		END IF;
		IF (_id8 IS NOT NULL AND _exa ~ '(^|;)1-8(;|$)') THEN
			IF _win_str !~ ('(^|,)' || _id8 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id8, 0, NULL); _win_str = _win_str || ',' || _id8; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id8;
		END IF;
	END LOOP;
	CLOSE _c;

	_query = 'SELECT FB.id AS entity_id, ' || _type || ' AS entity_type, FB.int_value AS count_win';
	IF (_type < 10) THEN
		_query = _query || ', PR.last_name || CASE WHEN PR.first_name IS NOT NULL AND PR.first_name <> '''' THEN '', '' || PR.first_name ELSE '''' END AS entity_str';
		_query = _query || ' FROM "~FUNC_BUFFER" FB LEFT JOIN "PERSON" PR ON FB.id = PR.id';
	ELSIF (_type = 50) THEN
		_query = _query || ', TM.label AS entity_str';
		_query = _query || ' FROM "~FUNC_BUFFER" FB LEFT JOIN "TEAM" TM ON FB.id = TM.id';
	ELSIF (_type = 99) THEN
		_query = _query || ', CN.label AS entity_str';
		_query = _query || ' FROM "~FUNC_BUFFER" FB LEFT JOIN "COUNTRY" CN ON FB.id = CN.id';
	END IF;
	_query = _query || ' ORDER BY FB.int_value DESC, entity_str ASC';
	OPEN _c FOR EXECUTE _query;
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION "WIN_RECORDS"(text) OWNER TO postgres;
update "RESULT" set exa='3-4' where exa='3,4';
update "RESULT" set exa='3-7' where exa='3-6';
update "RESULT" set exa='3-6' where exa='3-5';
update "RESULT" set exa='3-5' where exa='3-4';
update "RESULT" set exa='3-4' where exa='3';
update "RESULT" set exa='2-3;4-5' where exa='2;4';
update "RESULT" set exa='2-9' where exa='2-8';
update "RESULT" set exa='2-5' where exa='2-4';
update "RESULT" set exa='2-4' where exa='2-3';
update "RESULT" set exa='2-3' where exa='2';
update "RESULT" set exa='1-2;4-5' where exa='1;4';
update "RESULT" set exa='1-2;3-4;5-6' where exa='1;3;5';
update "RESULT" set exa='1-2;3-4' where exa='1;3';
update "RESULT" set exa='1-2;3-5' where exa='1;3-4';
update "RESULT" set exa='1-8' where exa='1-7';
update "RESULT" set exa='1-7' where exa='1-6';
update "RESULT" set exa='1-6' where exa='1-5';
update "RESULT" set exa='1-5' where exa='1-4';
update "RESULT" set exa='1-4' where exa='1-3';
update "RESULT" set exa='1-3' where exa='1-2';
update "RESULT" set exa='1-2' where exa='1';
update "RESULT" set exa='1-3;4-6;7-9' where exa='1-2;4-5;7-8';

ALTER TABLE "HALL_OF_FAME" DROP DECEASED;
ALTER TABLE "HALL_OF_FAME" ADD POSITION varchar(10);
ALTER TABLE "RETIRED_NUMBER" ADD ID_YEAR INTEGER;
ALTER TABLE "TEAM" DROP CODE;
CREATE OR REPLACE FUNCTION "GET_RESULTS"(_id_sport integer, _id_championship integer, _id_event integer, _id_subevent integer, _years text)
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
	IF _id_subevent <> 0 THEN
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
	        _columns := _columns || ', PRCN' || i || '.id AS en' || i || '_rel2_id, PRCN' || i || '.code AS en' || i || '_rel2_code, PRCN' || i || '.label AS en' || i || '_rel2_label';
	        _joins := _joins || ' LEFT JOIN "PERSON" PR' || i || ' ON RS.id_rank' || i || ' = PR' || i || '.id';
	        _joins := _joins || ' LEFT JOIN "TEAM" PRTM' || i || ' ON PR' || i || '.id_team = PRTM' || i || '.id';
	        _joins := _joins || ' LEFT JOIN "COUNTRY" PRCN' || i || ' ON PR' || i || '.id_country = PRCN' || i || '.id';
	    ELSIF _type = 50 THEN -- Team
	        _columns := _columns || ', NULL AS en' || i || '_str1, TM' || i || '.label AS en' || i || '_str2';
	        _columns := _columns || ', NULL AS en' || i || '_rel1_id, NULL AS en' || i || '_rel1_code, NULL AS en' || i || '_rel1_label';
	        _columns := _columns || ', TMCN' || i || '.id AS en' || i || '_rel2_id, TMCN' || i || '.code AS en' || i || '_rel2_code, TMCN' || i || '.label AS en' || i || '_rel2_label';
	        _joins := _joins || ' LEFT JOIN "TEAM" TM' || i || ' ON RS.id_rank' || i || ' = TM' || i || '.id';
	        _joins := _joins || ' LEFT JOIN "COUNTRY" TMCN' || i || ' ON TM' || i || '.id_country = TMCN' || i || '.id';
	    ELSIF _type = 99 THEN -- Country
	        _columns := _columns || ', _CN' || i || '.code AS en' || i || '_str1, _CN' || i || '.label AS en' || i || '_str2';
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
		RS.result3 AS rs_result3, RS.result4 AS rs_result4, RS.result5 AS rs_result5, RS.comment AS rs_comment, RS.exa AS rs_exa, YR.id AS yr_id, YR.label AS yr_label, CX.id AS cx_id, CX.label AS cx_label,
		CT1.id AS ct1_id, CT1.label AS ct1_label, CT2.id AS ct2_id, CT2.label AS ct2_label, ST1.id AS st1_id, ST1.code AS st1_code, ST1.label AS st1_label, ST2.id AS st2_id, ST2.code AS st2_code,
		ST2.label AS st2_label, CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label AS cn1_label, CN2.id AS cn2_id, CN2.code AS cn2_code, CN2.label AS cn2_label, DR.id as dr_id' ||
		_columns || '
	FROM
		"RESULT" RS
		LEFT JOIN "YEAR" YR ON RS.id_year = YR.id
		LEFT JOIN "COMPLEX" CX ON RS.id_complex = CX.id
		LEFT JOIN "CITY" CT1 ON CX.id_city = CT1.id
		LEFT JOIN "CITY" CT2 ON RS.id_city = CT2.id
		LEFT JOIN "STATE" ST1 ON CT1.id_state = ST1.id
		LEFT JOIN "STATE" ST2 ON CT2.id_state = ST2.id
		LEFT JOIN "COUNTRY" CN1 ON CT1.id_country = CN1.id
		LEFT JOIN "COUNTRY" CN2 ON CT2.id_country = CN2.id
		LEFT JOIN "DRAW" DR ON DR.id_result = RS.id' ||
		_joins || '
	WHERE
		RS.id_sport = ' || _id_sport || ' AND
		RS.id_championship = ' || _id_championship ||
		_event_condition || _year_condition || '
	ORDER BY RS.id_year';
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION "GET_RESULTS"(integer, integer, integer, integer, text) OWNER TO postgres;

CREATE OR REPLACE FUNCTION "GET_HALL_OF_FAME"(_id_league integer, _years text)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _year_condition text;
begin
	-- Set year condition ('All years' = Empty condition)
	_year_condition := '';
	IF _years <> '0' THEN
		_year_condition := ' AND YR.id IN (' || _years || ')';
	END IF;
	
	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT
		HF.id AS hf_id, YR.id AS yr_id, YR.label AS yr_label, PR.id AS pr_id, PR.last_name AS pr_last_name,
		PR.first_name AS pr_first_name, HF.position AS hf_position
	FROM
		"HALL_OF_FAME" HF
		LEFT JOIN "YEAR" YR ON HF.id_year = YR.id
		LEFT JOIN "PERSON" PR ON HF.id_person = PR.id
	WHERE
		HF.id_league = ' || _id_league || _year_condition || '
	ORDER BY
		YR.id, PR.last_name';
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION "GET_HALL_OF_FAME"(integer, text, smallint) OWNER TO postgres;

CREATE OR REPLACE FUNCTION "GET_WIN_LOSS"(_id_league integer, _teams text)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _team_condition text;
begin
	-- Set team condition ('All teams' = Empty condition)
	_team_condition := '';
	IF _teams <> '0' THEN
		_team_condition := ' AND TM.id IN (' || _teams || ')';
	END IF;
	
	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT
		WL.id AS wl_id, TM.id AS tm_id, TM.label AS tm_label, 
		WL.type AS wl_type, WL.count_win AS wl_count_win, WL.count_loss AS wl_count_loss, WL.count_tie AS wl_count_tie,
		WL.count_otloss AS wl_count_otloss, WL.average AS wl_average
	FROM
		"WIN_LOSS" WL
		LEFT JOIN "TEAM" TM ON WL.id_team = TM.id
	WHERE
		WL.id_league = ' || _id_league || _team_condition || '
	ORDER BY
		TM.label, WL.type, WL.count_win DESC, WL.count_loss';
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION "GET_WIN_LOSS"(integer, text) OWNER TO postgres;

CREATE OR REPLACE FUNCTION "GET_TEAM_STADIUM"(_id_league integer, _teams text, _date1 character varying, _date2 character varying)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _team_condition text;
    _date_condition text;
begin
	-- Set team condition ('All teams' = Empty condition)
	_team_condition := '';
	IF _teams <> '0' THEN
		_team_condition := ' AND TM.id IN (' || _teams || ')';
	END IF;
	-- Set dates condition
	_date_condition := '';
	IF _date1 <> '' THEN
		_date_condition := ' AND TS.date1 >= ''' || _date1 || '''';
	END IF;
	IF _date2 <> '' THEN
		_date_condition := ' AND TS.date2 <= ''' || _date2 || '''';
	END IF;
	
	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT
		TS.id AS ts_id, TM.id AS tm_id, TM.label AS tm_label, TS.renamed AS ts_renamed, TS.comment AS ts_comment,
		CX.id AS cx_id, CX.label AS cx_label, CT.id AS ct_id, CT.label AS ct_label, ST.id AS st_id, ST.code AS st_code, ST.label AS st_label,
		CN.id AS cn_id, CN.code AS cn_code, CN.label AS cn_label, TS.date1 AS ts_date1, TS.date2 AS ts_date2
	FROM
		"TEAM_STADIUM" TS
		LEFT JOIN "TEAM" TM ON TS.id_team = TM.id
		LEFT JOIN "COMPLEX" CX ON TS.id_complex = CX.id
		LEFT JOIN "CITY" CT ON CX.id_city = CT.id
		LEFT JOIN "STATE" ST ON CT.id_state = ST.id
		LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id
	WHERE
		TS.id_league = ' || _id_league || _team_condition || _date_condition || '
	ORDER BY
		TM.label, TS.date1 DESC';
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION "GET_TEAM_STADIUM"(integer, text, character varying, character varying) OWNER TO postgres;

CREATE OR REPLACE FUNCTION "GET_RETIRED_NUMBER"(_id_league integer, _teams text, _number smallint)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _team_condition text;
    _number_condition text;
begin
	-- Set team condition ('All teams' = Empty condition)
	_team_condition := '';
	IF _teams <> '0' THEN
		_team_condition := ' AND TM.id IN (' || _teams || ')';
	END IF;
	-- Set deceased condition
	_number_condition := '';
	IF _number <> -1 THEN
		_number_condition := ' AND RN.number = ' || _number;
	END IF;
	
	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT
		RN.id AS rn_id, TM.id AS tm_id, TM.label AS tm_label, 
		PR.id AS pr_id, PR.last_name AS pr_last_name, PR.first_name AS pr_first_name, YR.id AS yr_id, YR.label AS yr_label, RN.number AS rn_number
	FROM
		"RETIRED_NUMBER" RN
		LEFT JOIN "TEAM" TM ON RN.id_team = TM.id
		LEFT JOIN "PERSON" PR ON RN.id_person = PR.id
		LEFT JOIN "YEAR" YR ON RN.id_year = YR.id
	WHERE
		RN.id_league = ' || _id_league || _team_condition || _number_condition || '
	ORDER BY
		TM.label, RN.number';
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION "GET_RETIRED_NUMBER"(integer, text, smallint) OWNER TO postgres;
