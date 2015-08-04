CREATE OR REPLACE FUNCTION "GetRank"(_rs "RESULT", _ids integer[])
  RETURNS smallint AS
$BODY$
DECLARE
	_type1 smallint;
	_type2 smallint;
	_type3 smallint;
BEGIN
	-- Get result type
	SELECT DISTINCT TP1.number, TP2.number, TP3.number
	INTO _type1, _type2, _type3
	FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id
	WHERE RS.id = _rs.id;
	IF _type3 IS NOT NULL THEN
		_type1 := _type3;
	ELSIF _type2 IS NOT NULL THEN
		_type1 := _type2;
	END IF;

	-- Calculate rank
	IF _rs.id_rank1 = ANY(_ids) OR
	  (_rs.id_rank2 = ANY(_ids) AND (_type1 IN (4, 5) OR _rs.comment IN ('#DOUBLE#', '#TRIPLE#') OR _rs.exa ~* '(^|/)1-(2|3|4|5|6|7|8|9)(/|$)')) OR
	  (_rs.id_rank3 = ANY(_ids) AND (_type1 = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)1-(3|4|5|6|7|8|9)(/|$)')) OR
	  (_rs.id_rank4 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(4|5|6|7|8|9)(/|$)') OR
	  (_rs.id_rank5 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(5|6|7|8|9)(/|$)') OR
	  (_rs.id_rank6 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(6|7|8|9)(/|$)') OR
	  (_rs.id_rank7 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(7|8|9)(/|$)') OR
	  (_rs.id_rank8 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(8|9)(/|$)') OR
	  (_rs.id_rank9 = ANY(_ids) AND _rs.exa ~* '(^|/)1-9(/|$)') THEN
		RETURN 1;
	ELSIF (_rs.id_rank2 = ANY(_ids) AND _type1 NOT IN (4, 5) AND (_rs.comment IS NULL OR _rs.comment NOT IN ('#DOUBLE#', '#TRIPLE#'))) OR
	  (_rs.id_rank3 = ANY(_ids) AND ((_type1 = 4 OR _rs.comment = '#DOUBLE#' OR _rs.exa ~* '(^|/)2-(3|4|5|6|7|8|9)(/|$)') AND _type1 <> 5 AND _rs.comment <> '#TRIPLE#')) OR
	  (_rs.id_rank4 = ANY(_ids) AND ((_type1 = 4 OR _rs.comment = '#DOUBLE#' OR _rs.exa ~* '(^|/)2-(4|5|6|7|8|9)(/|$)') AND _type1 <> 5 AND _rs.comment <> '#TRIPLE#')) OR 
	  (_rs.id_rank5 = ANY(_ids) AND (_type1 = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)2-(5|6|7|8|9)(/|$)')) OR
	  (_rs.id_rank6 = ANY(_ids) AND (_type1 = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)2-(6|7|8|9)(/|$)')) OR
	  (_rs.id_rank7 = ANY(_ids) AND _rs.exa ~* '(^|/)2-(7|8|9)(/|$)') OR
	  (_rs.id_rank8 = ANY(_ids) AND _rs.exa ~* '(^|/)2-(8|9)(/|$)') OR
	  (_rs.id_rank9 = ANY(_ids) AND _rs.exa ~* '(^|/)2-9(/|$)') THEN
		RETURN 2;
	ELSIF (_rs.id_rank3 = ANY(_ids) AND _type1 NOT IN (4, 5) AND (_rs.comment IS NULL OR _rs.comment NOT IN ('#DOUBLE#', '#TRIPLE#'))) OR
	  (_rs.id_rank4 = ANY(_ids) AND (_type1 NOT IN (4, 5) AND _rs.comment NOT IN ('#DOUBLE#', '#TRIPLE#') AND _rs.exa ~* '(^|/)3-(4|5|6|7|8|9)(/|$)')) OR
	  (_rs.id_rank5 = ANY(_ids) AND (_type1 <> 5 AND _rs.comment <> '#TRIPLE#' AND _rs.exa ~* '(^|/)3-(5|6|7|8|9)(/|$)')) OR
	  (_rs.id_rank6 = ANY(_ids) AND (_type1 <> 5 AND _rs.comment <> '#TRIPLE#' AND _rs.exa ~* '(^|/)3-(6|7|8|9)(/|$)')) OR
	  (_rs.id_rank7 = ANY(_ids) AND (_type1 = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)3-(7|8|9)(/|$)')) OR
	  (_rs.id_rank8 = ANY(_ids) AND (_type1 = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)3-(8|9)(/|$)')) OR
	  (_rs.id_rank9 = ANY(_ids) AND (_type1 = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)3-9(/|$)')) THEN
		RETURN 3;
	END IF;
	RETURN 0;
END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  
  
  
  
  
  
  
  ALTER TABLE "CITY" add img_url varchar(255);
ALTER TABLE "COMPLEX" add img_url varchar(255);
ALTER TABLE "PERSON" add img_url varchar(255);
ALTER TABLE "RESULT" add img_url varchar(255);
ALTER TABLE "SPORT" add img_url varchar(255);


DROP TABLE "~FUNC_BUFFER";


DROP FUNCTION "WIN_RECORDS"(_results text, _lang character varying);
	
		
	CREATE OR REPLACE FUNCTION "WIN_RECORDS"(_results text, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
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
	_str varchar;
	_type smallint;
	_type2 smallint;
	_type3 smallint;
	_cmt text;
	_exa text;
	_query text;
	_c refcursor;
begin
	SELECT DISTINCT
		TP1.number, TP2.number, TP3.number
	INTO
		_type, _type2, _type3
	FROM
		"RESULT" RS
		LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id
		LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id
		LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id
	WHERE
		RS.id = CAST(CASE WHEN POSITION(',' IN _results) > 0 THEN SUBSTRING(_results FROM 1 FOR POSITION(',' IN _results) - 1) ELSE _results END AS integer);
	IF _type3 IS NOT NULL THEN
		_type := _type3;
	ELSIF _type2 IS NOT NULL THEN
		_type := _type2;
	END IF;

	_str := '';
	
	OPEN _c FOR EXECUTE '
	SELECT RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, RS.exa, RS.comment
	FROM "RESULT" RS
	WHERE RS.id IN (' || _results || ')';
	LOOP	
		FETCH _c INTO _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _exa, _cmt;
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
		ELSIF (_type = 50) THEN
			SELECT TM.link INTO _link1 FROM "TEAM" TM WHERE TM.id = _id1;IF _link1 IS NOT NULL and _link1 > 0 THEN _id1 := _link1; END IF;
			SELECT TM.link INTO _link2 FROM "TEAM" TM WHERE TM.id = _id2;IF _link2 IS NOT NULL and _link2 > 0 THEN _id2 := _link2; END IF;
			SELECT TM.link INTO _link3 FROM "TEAM" TM WHERE TM.id = _id3;IF _link3 IS NOT NULL and _link3 > 0 THEN _id3 := _link3; END IF;
			SELECT TM.link INTO _link4 FROM "TEAM" TM WHERE TM.id = _id4;IF _link4 IS NOT NULL and _link4 > 0 THEN _id4 := _link4; END IF;
			SELECT TM.link INTO _link5 FROM "TEAM" TM WHERE TM.id = _id5;IF _link5 IS NOT NULL and _link5 > 0 THEN _id5 := _link5; END IF;
			SELECT TM.link INTO _link6 FROM "TEAM" TM WHERE TM.id = _id6;IF _link6 IS NOT NULL and _link6 > 0 THEN _id6 := _link6; END IF;
			SELECT TM.link INTO _link7 FROM "TEAM" TM WHERE TM.id = _id7;IF _link7 IS NOT NULL and _link7 > 0 THEN _id7 := _link7; END IF;
			SELECT TM.link INTO _link8 FROM "TEAM" TM WHERE TM.id = _id8;IF _link8 IS NOT NULL and _link8 > 0 THEN _id8 := _link8; END IF;
			SELECT TM.link INTO _link9 FROM "TEAM" TM WHERE TM.id = _id9;IF _link9 IS NOT NULL and _link9 > 0 THEN _id9 := _link9; END IF;
			SELECT TM.link INTO _link10 FROM "TEAM" TM WHERE TM.id = _id10;IF _link10 IS NOT NULL and _link10 > 0 THEN _id10 := _link10; END IF;
		END IF;
		IF (_id1 IS NOT NULL) THEN
			_str := _str || (CASE WHEN _str <> '' THEN ',' ELSE '' END) || _id1;
		END IF;
		IF (_id2 IS NOT NULL AND (_type = 4 OR _type = 5 OR _cmt = '#DOUBLE#' OR _cmt = '#TRIPLE#' OR _exa ~ '(^|/)1.*(/|$)')) THEN
			_str := _str || (CASE WHEN _str <> '' THEN ',' ELSE '' END) || _id2;
		END IF;
		IF (_id3 IS NOT NULL AND (_type = 5 OR _cmt = '#TRIPLE#' OR _exa ~ '(^|/)1-(3|4|5|6|7|8)(/|$)')) THEN
			_str := _str || (CASE WHEN _str <> '' THEN ',' ELSE '' END) || _id3;
		END IF;
		IF (_id4 IS NOT NULL AND _exa ~ '(^|/)1-(4|5|6|7|8)(/|$)') THEN
			_str := _str || (CASE WHEN _str <> '' THEN ',' ELSE '' END) || _id4;
		END IF;
		IF (_id5 IS NOT NULL AND _exa ~ '(^|/)1-(5|6|7|8)(/|$)') THEN
			_str := _str || (CASE WHEN _str <> '' THEN ',' ELSE '' END) || _id5;
		END IF;
		IF (_id6 IS NOT NULL AND _exa ~ '(^|/)1-(6|7|8)(/|$)') THEN
			_str := _str || (CASE WHEN _str <> '' THEN ',' ELSE '' END) || _id6;
		END IF;
		IF (_id7 IS NOT NULL AND _exa ~ '(^|/)1-(7|8)(/|$)') THEN
			_str := _str || (CASE WHEN _str <> '' THEN ',' ELSE '' END) || _id7;
		END IF;
		IF (_id8 IS NOT NULL AND _exa ~ '(^|/)1-8(/|$)') THEN
			_str := _str || (CASE WHEN _str <> '' THEN ',' ELSE '' END) || _id8;
		END IF;
	END LOOP;
	CLOSE _c;

	_query := 'SELECT ';
	IF (_type < 10) THEN
		_query := _query || 'PR.id, PR.last_name || '', '' || PR.first_name, PR.last_name || '', '' || PR.first_name, CN.code';
		_query := _query || ' FROM "PERSON" PR LEFT JOIN "COUNTRY" CN ON PR.id_country = CN.id WHERE PR.id IN (' || _str || ')';
	ELSIF (_type = 50) THEN
		_query := _query || 'TM.id, TM.label, TM.label, NULL';
		_query := _query || ' FROM "TEAM" TM WHERE TM.id IN (' || _str || ')';
	ELSIF (_type = 99) THEN
		_query := _query || 'CN.id, CN.label' || _lang || ', CN.label, NULL';
		_query := _query || ' FROM "COUNTRY" CN WHERE CN.id IN (' || _str || ')';
	END IF;
	OPEN _c FOR EXECUTE _query;
	LOOP
		FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.label_rel1;
		EXIT WHEN NOT FOUND;
		SELECT COUNT(*) INTO _item.count1 FROM unnest(string_to_array(_str, ',')) item WHERE item = CAST(_item.id_item AS varchar);
		_item.id_rel1 = _type;
		RETURN NEXT _item;
	END LOOP;
	RETURN;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  
  
  CREATE OR REPLACE FUNCTION "ENTITY_REF"(_entity character varying, _id integer, _entity_ref character varying, _limit character varying, _offset integer, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_c refcursor;
	__c refcursor;
	_query text;
	_link integer;
	_ct_list varchar(200);
	_cx_list varchar(200);
	_pr_list varchar(200);
	_tm_list varchar(200);
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

	IF _entity ~ 'CT' THEN
		SELECT LINK INTO _link FROM "CITY" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "CITY" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_ct_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_ct_list := _ct_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_ct_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'CX' THEN
		SELECT LINK INTO _link FROM "COMPLEX" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "COMPLEX" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_cx_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_cx_list := _cx_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_cx_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'PR' THEN
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
	ELSIF _entity ~ 'TM' THEN
		SELECT LINK INTO _link FROM "TEAM" WHERE ID = _id AND (YEAR1 IS NULL OR YEAR1 = '');
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "TEAM" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_tm_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_tm_list := _tm_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_tm_list := cast(_id AS varchar);
		END IF;
	END IF;

	-- References in: [Draws] (MUST STAY AT FIRST)
	IF (_entity ~ 'CN|PR|TM' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT DR.id, DR.id_result, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, DR.id1_qf1, DR.id2_qf1, DR.id1_qf2, DR.id2_qf2, DR.id1_qf3, DR.id2_qf3, DR.id1_qf4, DR.id2_qf4, DR.id1_sf1, DR.id2_sf1, DR.id1_sf2, DR.id2_sf2, DR.id1_thd, DR.id2_thd, TP1.number, TP2.number, TP3.number FROM "DRAW" DR';
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
		IF _entity = 'CN' THEN
			_query := _query || ' AND (DR.id1_sf1 <> ' || _id || ' AND DR.id1_sf2 <> ' || _id || ') AND (DR.id1_qf1 = ' || _id || ' OR DR.id2_qf1 = ' || _id || ' OR DR.id1_qf2 = ' || _id || ' OR DR.id2_qf2 = ' || _id || ' OR DR.id1_qf3 = ' || _id || ' OR DR.id2_qf3 = ' || _id || ' OR DR.id1_qf4 = ' || _id || ' OR DR.id2_qf4 = ' || _id || ' OR DR.id2_sf1 = ' || _id || ' OR DR.id2_sf2 = ' || _id || ' OR DR.id1_thd = ' || _id || ' OR DR.id2_thd = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _pr_list || ') AND DR.id1_sf2 NOT IN (' || _pr_list || ')) AND (DR.id1_qf1 IN (' || _pr_list || ') OR DR.id2_qf1 IN (' || _pr_list || ') OR DR.id1_qf2 IN (' || _pr_list || ') OR DR.id2_qf2 IN (' || _pr_list || ') OR DR.id1_qf3 IN (' || _pr_list || ') OR DR.id2_qf3 IN (' || _pr_list || ') OR DR.id1_qf4 IN (' || _pr_list || ') OR DR.id2_qf4 IN (' || _pr_list || ') OR DR.id2_sf1 IN (' || _pr_list || ') OR DR.id2_sf2 IN (' || _pr_list || ') OR DR.id1_thd IN (' || _pr_list || ') OR DR.id2_thd IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _tm_list || ') AND DR.id1_sf2 NOT IN (' || _tm_list || ')) AND (DR.id1_qf1 IN (' || _tm_list || ') OR DR.id2_qf1 IN (' || _tm_list || ') OR DR.id1_qf2 IN (' || _tm_list || ') OR DR.id2_qf2 IN (' || _tm_list || ') OR DR.id1_qf3 IN (' || _tm_list || ') OR DR.id2_qf3 IN (' || _tm_list || ') OR DR.id1_qf4 IN (' || _tm_list || ') OR DR.id2_qf4 IN (' || _tm_list || ') OR DR.id2_sf1 IN (' || _tm_list || ') OR DR.id2_sf2 IN (' || _tm_list || ') OR DR.id1_thd IN (' || _tm_list || ') OR DR.id2_thd IN (' || _tm_list || '))';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel10, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _id11, _id12, _id13, _id14, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF (_id13 = _id OR _id14 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)'))) THEN
				_item.txt2 := 'thd';
			ELSIF (_id9 = _id OR _id10 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)'))) THEN
				_item.txt2 := 'sf1';
			ELSIF (_id11 = _id OR _id12 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)'))) THEN
				_item.txt2 := 'sf2';
			ELSIF (_id1 = _id OR _id2 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)'))) THEN
				_item.txt2 := 'qf1';
			ELSIF (_id3 = _id OR _id4 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)'))) THEN
				_item.txt2 := 'qf2';
			ELSIF (_id5 = _id OR _id6 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)'))) THEN
				_item.txt2 := 'qf3';
			ELSIF (_id7 = _id OR _id8 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)'))) THEN
				_item.txt2 := 'qf4';
			END IF;
			IF _type1 <= 10 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN '', '' || PR1.first_name ELSE '''' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || '' '' ELSE '''' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN '', '' || PR2.first_name ELSE '''' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || '' '' ELSE '''' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, DR.result_' || _item.txt2;
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "PERSON" PR1 ON DR.id1_' || _item.txt2 || ' = PR1.id LEFT JOIN "PERSON" PR2 ON DR.id2_' || _item.txt2 || ' = PR2.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel8, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel9, _item.id_rel13, _cn2, _tm2, _item.txt1;
				CLOSE __c;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				_item.comment := 'PR';
			ELSIF _type1 = 50 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', TM1.label, TM2.label, NULL, NULL, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "TEAM" TM1 ON DR.id1_' || _item.txt2 || ' = TM1.id LEFT JOIN "TEAM" TM2 ON DR.id2_' || _item.txt2 || ' = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'TM';
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', CN1.label' || _lang || ', CN2.label' || _lang || ', CN1.label, CN2.label, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "COUNTRY" CN1 ON DR.id1_' || _item.txt2 || ' = CN1.id LEFT JOIN "COUNTRY" CN2 ON DR.id2_' || _item.txt2 || ' = CN2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'CN';
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
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number, TP3.number FROM "RESULT" RS';
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
		ELSIF (_entity = 'PR') THEN
			_type1 = 1;
			_type2 = 99;
			_query := _query || ' LEFT JOIN "~PERSON_LIST" PL ON PL.id_result = RS.id';
		END IF;
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RS.id_rank1 = ' || _id || ' OR RS.id_rank2 = ' || _id || ' OR RS.id_rank3 = ' || _id || ' OR RS.id_rank4 = ' || _id || ' OR RS.id_rank5 = ' || _id || ' OR RS.id_rank6 = ' || _id || ' OR RS.id_rank7 = ' || _id || ' OR RS.id_rank8 = ' || _id || ' OR RS.id_rank9 = ' || _id || ' OR RS.id_rank10 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (PL.id_person IN (' || _pr_list || ') OR (((TP1.number BETWEEN 1 AND 10 AND TP2.number IS NULL) OR (TP2.number BETWEEN 1 AND 10 AND TP3.number IS NULL) OR (TP3.number BETWEEN 1 AND 10)) AND ((RS.id_rank1 IN (' || _pr_list || ') OR RS.id_rank2 IN (' || _pr_list || ') OR RS.id_rank3 IN (' || _pr_list || ') OR RS.id_rank4 IN (' || _pr_list || ') OR RS.id_rank5 IN (' || _pr_list || ') OR RS.id_rank6 IN (' || _pr_list || ') OR RS.id_rank7 IN (' || _pr_list || ') OR RS.id_rank8 IN (' || _pr_list || ') OR RS.id_rank9 IN (' || _pr_list || ') OR RS.id_rank10 IN (' || _pr_list || ')))))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RS.id_rank1 IN (' || _tm_list || ') OR RS.id_rank2 IN (' || _tm_list || ') OR RS.id_rank3 IN (' || _tm_list || ') OR RS.id_rank4 IN (' || _tm_list || ') OR RS.id_rank5 IN (' || _tm_list || ') OR RS.id_rank6 IN (' || _tm_list || ') OR RS.id_rank7 IN (' || _tm_list || ') OR RS.id_rank8 IN (' || _tm_list || ') OR RS.id_rank9 IN (' || _tm_list || ') OR RS.id_rank10 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND  (RS.id_city1 IN (' || _ct_list || ') OR RS.id_city2 IN (' || _ct_list || '))';
		ELSIF _entity = 'CX' THEN
			_query := _query || ' AND  (RS.id_complex1 IN (' || _cx_list || ') OR RS.id_complex2 IN (' || _cx_list || '))';
		ELSIF _entity = 'OL' THEN
			_query := _query || ' AND RS.id_championship=1 AND OL.id = ' || _id;
		ELSIF _entity = 'YR' THEN
			_query := _query || ' AND RS.id_year = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF _type1 <= 10 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, CN3.code, TM3.label, PR4.last_name || (CASE WHEN length(PR4.first_name) > 0 THEN ', ' || PR4.first_name ELSE '' END), (CASE WHEN length(PR4.first_name) > 0 THEN PR4.first_name || ' ' ELSE '' END) || PR4.last_name, CN4.id, CN4.code, TM4.label, PR5.last_name || (CASE WHEN length(PR5.first_name) > 0 THEN ', ' || PR5.first_name ELSE '' END), (CASE WHEN length(PR5.first_name) > 0 THEN PR5.first_name || ' ' ELSE '' END) || PR5.last_name, CN5.id, CN5.code, TM5.label, PR6.last_name || (CASE WHEN length(PR6.first_name) > 0 THEN ', ' || PR6.first_name ELSE '' END), (CASE WHEN length(PR6.first_name) > 0 THEN PR6.first_name || ' ' ELSE '' END) || PR6.last_name, CN6.id, CN6.code, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.label_rel23, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.label_rel24, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.label_rel25, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt3, _item.txt4
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
				IF _type1 = 4 OR _item.txt3 = '#DOUBLE#' THEN
					_item.txt4 = '1-2/3-4/5-6';
				ELSIF _type1 = 5 OR _item.txt3 = '#TRIPLE#' THEN
					_item.txt4 = '1-3/4-6/7-9';
				END IF;
				_item.comment := 'PR';
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, NULL, NULL, NULL, NULL, NULL, NULL, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "TEAM" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "TEAM" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "TEAM" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "TEAM" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "TEAM" TM5 ON RS.id_rank5 = TM5.id LEFT JOIN "TEAM" TM6 ON RS.id_rank6 = TM6.id
				WHERE RS.id = _item.id_item;
				_item.comment := 'TM';
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN4.label' || _lang || ', CN5.label' || _lang || ', CN6.label' || _lang || ', CN1.label, CN2.label, CN3.label, CN4.label, CN5.label, CN6.label, RS.result1, RS.result2, RS.comment, RS.exa';
				_query := _query || ' FROM "RESULT" RS LEFT JOIN "COUNTRY" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "COUNTRY" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "COUNTRY" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "COUNTRY" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "COUNTRY" CN5 ON RS.id_rank5 = CN5.id LEFT JOIN "COUNTRY" CN6 ON RS.id_rank6 = CN6.id';
				_query := _query || ' WHERE RS.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4;
				CLOSE __c;
				_item.comment := 'CN';
			END IF;
			IF _item.id_rel6 = _id OR (_item.id_rel7 = _id AND (_type1 IN (4,5) OR _item.txt3 IN ('#DOUBLE#', '#TRIPLE#') OR _item.txt4 ~ '(^|/)1.*(/|$)')) OR (_item.id_rel8 = _id AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)1-(3|4|5|6|7|8)(/|$)')) OR (_item.id_rel9 = _id AND _item.txt4 ~ '(^|/)1-(4|5|6|7|8)(/|$)') OR (_item.id_rel10 = _id AND _item.txt4 ~ '(^|/)1-(5|6|7|8)(/|$)') OR (_item.id_rel11 = _id AND _item.txt4 ~ '(^|/)1-(6|7|8)(/|$)') THEN
				_item.count1 := 1;
			ELSIF _item.id_rel7 = _id OR (_item.id_rel8 = _id AND (_type1 = 4 OR _item.txt3 = '#DOUBLE#' OR _item.txt4 ~ '(^|/)2.*(/|$)')) OR (_item.id_rel9 = _id AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)2-(4|5|6|7|8).*(/|$)')) OR (_item.id_rel10 = _id AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)2-(5|6|7|8).*(/|$)')) OR (_item.id_rel11 = _id AND _item.txt4 ~ '(^|/)2-(6|7|8).*(/|$)') THEN
				_item.count1 := 2;
			ELSIF _item.id_rel8 = _id/* OR (_item.id_rel9 = _id AND (_type1 = 4 OR _item.txt3 = '#DOUBLE#' OR _item.txt4 ~ '(^|/)2.*(/|$)')) OR (_item.id_rel9 = _id AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)2-(4|5|6|7|8).*(/|$)')) OR (_item.id_rel10 = _id AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)2-(5|6|7|8).*(/|$)')) OR (_item.id_rel11 = _id AND _item.txt4 ~ '(^|/)2-(6|7|8).*(/|$)') */THEN
				_item.count1 := 3;
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
		_query := 'SELECT DISTINCT ON (PR.last_name, PR.first_name, CN.id, SP.id) PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "PERSON" PR';
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
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT TM.id, TM.label, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "TEAM" TM';
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
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT CT.id, CT.label' || _lang || ', CT.label, CN.id, CN.label' || _lang || ', CN.label FROM "CITY" CT';
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
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.label_rel2;
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
		_query := 'SELECT CX.id, CX.label' || _lang || ', CX.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label FROM "COMPLEX" CX';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' WHERE CX.id_city = ' || _id;
		_query := _query || ' ORDER BY CX.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OL.type FROM "OLYMPICS" OL';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query := _query || ' WHERE OL.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY OL.type, YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT OR_.id, OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OR_.count_gold || '','' || OR_.count_silver || '','' || OR_.count_bronze FROM "OLYMPIC_RANKING" OR_';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT RC.id, RC.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, RC.type1, RC.type2, RC.record1, RC.id_rank1, RC.id_rank2, RC.id_rank3, RC.id_rank4, RC.id_rank5 FROM "RECORD" RC';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RC.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RC.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RC.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "TYPE" TP ON EV.id_type = TP.id';
		_query := _query || ' WHERE TP.number BETWEEN ' || _type1 || ' AND ' || _type2;
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RC.id_rank1 = ' || _id || ' OR RC.id_rank2 = ' || _id || ' OR RC.id_rank3 = ' || _id || ' OR RC.id_rank4 = ' || _id || ' OR RC.id_rank5 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _pr_list || ') OR RC.id_rank2 IN (' || _pr_list || ') OR RC.id_rank3 IN (' || _pr_list || ') OR RC.id_rank4 IN (' || _pr_list || ') OR RC.id_rank5 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _tm_list || ') OR RC.id_rank2 IN (' || _tm_list || ') OR RC.id_rank3 IN (' || _tm_list || ') OR RC.id_rank4 IN (' || _tm_list || ') OR RC.id_rank5 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RC.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RC.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RC.id_event = ' || _id || ' OR RC.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND RC.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', RC.index LIMIT ' || _limit || ' OFFSET ' || _offset;
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
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Hall of Fame]
	IF (_entity ~ 'YR|PR' AND (_entity_ref = 'HF' OR _entity_ref = '')) THEN
		_query := 'SELECT HF.id, YR.id, YR.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, HF.position FROM "HALL_OF_FAME" HF';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.txt1;
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
		_query := 'SELECT RN.id, TM.id, TM.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, RN.number FROM "RETIRED_NUMBER" RN';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.id_rel4;
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
		_query := 'SELECT TS.id, TM.id, TM.label, CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', CX.label, CT.label, ST.label, CN.label, LG.id, LG.label, TS.date1, TS.date2 FROM "TEAM_STADIUM" TS';
		_query := _query || ' LEFT JOIN "TEAM" TM ON TS.id_team = TM.id';
		_query := _query || ' LEFT JOIN "COMPLEX" CX ON TS.id_complex = CX.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON TS.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE TS.id_team = ' || _id;
		ELSIF _entity = 'CX' THEN
			_query := _query || ' WHERE TS.id_complex IN (' || _cx_list || ')';
		END IF;
		_query := _query || ' ORDER BY TM.label, TS.date1 DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.id_rel6, _item.comment, _item.txt1, _item.txt2;
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
		_query := 'SELECT WL.id, TM.id, TM.label, LG.id, LG.label, WL.type, WL.count_win || '','' || WL.count_loss FROM "WIN_LOSS" WL';
		_query := _query || ' LEFT JOIN "TEAM" TM ON WL.id_team = TM.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON WL.id_league = LG.id';
		_query := _query || ' WHERE WL.id_team = ' || _id;
		_query := _query || ' ORDER BY TM.label DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'WL';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Contributions]
	IF (_entity ~ 'CB' AND (_entity_ref = 'CO' OR _entity_ref = '')) THEN
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, CO.type, CO.date';
		_query := _query || ' FROM "~CONTRIBUTION" CO';
		_query := _query || ' LEFT JOIN "RESULT" RS ON CO.id_item = RS.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' WHERE RS.id_member=' || _id || ' ORDER BY CO.date DESC, YR.id DESC, SP.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.date1;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CO';
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
  
  
  
  
  
  
  
  
  
  CREATE OR REPLACE FUNCTION "ENTITY_REF"(_entity character varying, _id integer, _entity_ref character varying, _limit character varying, _offset integer, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_c refcursor;
	__c refcursor;
	_query text;
	_link integer;
	_ct_list varchar(200);
	_cx_list varchar(200);
	_pr_list varchar(200);
	_tm_list varchar(200);
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

	IF _entity ~ 'CT' THEN
		SELECT LINK INTO _link FROM "CITY" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "CITY" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_ct_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_ct_list := _ct_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_ct_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'CX' THEN
		SELECT LINK INTO _link FROM "COMPLEX" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "COMPLEX" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_cx_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_cx_list := _cx_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_cx_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'PR' THEN
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
	ELSIF _entity ~ 'TM' THEN
		SELECT LINK INTO _link FROM "TEAM" WHERE ID = _id AND (YEAR1 IS NULL OR YEAR1 = '');
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "TEAM" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_tm_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_tm_list := _tm_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_tm_list := cast(_id AS varchar);
		END IF;
	END IF;

	-- References in: [Draws] (MUST STAY AT FIRST)
	IF (_entity ~ 'CN|PR|TM' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT DR.id, DR.id_result, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, DR.id1_qf1, DR.id2_qf1, DR.id1_qf2, DR.id2_qf2, DR.id1_qf3, DR.id2_qf3, DR.id1_qf4, DR.id2_qf4, DR.id1_sf1, DR.id2_sf1, DR.id1_sf2, DR.id2_sf2, DR.id1_thd, DR.id2_thd, TP1.number, TP2.number, TP3.number FROM "DRAW" DR';
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
		IF _entity = 'CN' THEN
			_query := _query || ' AND (DR.id1_sf1 <> ' || _id || ' AND DR.id1_sf2 <> ' || _id || ') AND (DR.id1_qf1 = ' || _id || ' OR DR.id2_qf1 = ' || _id || ' OR DR.id1_qf2 = ' || _id || ' OR DR.id2_qf2 = ' || _id || ' OR DR.id1_qf3 = ' || _id || ' OR DR.id2_qf3 = ' || _id || ' OR DR.id1_qf4 = ' || _id || ' OR DR.id2_qf4 = ' || _id || ' OR DR.id2_sf1 = ' || _id || ' OR DR.id2_sf2 = ' || _id || ' OR DR.id1_thd = ' || _id || ' OR DR.id2_thd = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _pr_list || ') AND DR.id1_sf2 NOT IN (' || _pr_list || ')) AND (DR.id1_qf1 IN (' || _pr_list || ') OR DR.id2_qf1 IN (' || _pr_list || ') OR DR.id1_qf2 IN (' || _pr_list || ') OR DR.id2_qf2 IN (' || _pr_list || ') OR DR.id1_qf3 IN (' || _pr_list || ') OR DR.id2_qf3 IN (' || _pr_list || ') OR DR.id1_qf4 IN (' || _pr_list || ') OR DR.id2_qf4 IN (' || _pr_list || ') OR DR.id2_sf1 IN (' || _pr_list || ') OR DR.id2_sf2 IN (' || _pr_list || ') OR DR.id1_thd IN (' || _pr_list || ') OR DR.id2_thd IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _tm_list || ') AND DR.id1_sf2 NOT IN (' || _tm_list || ')) AND (DR.id1_qf1 IN (' || _tm_list || ') OR DR.id2_qf1 IN (' || _tm_list || ') OR DR.id1_qf2 IN (' || _tm_list || ') OR DR.id2_qf2 IN (' || _tm_list || ') OR DR.id1_qf3 IN (' || _tm_list || ') OR DR.id2_qf3 IN (' || _tm_list || ') OR DR.id1_qf4 IN (' || _tm_list || ') OR DR.id2_qf4 IN (' || _tm_list || ') OR DR.id2_sf1 IN (' || _tm_list || ') OR DR.id2_sf2 IN (' || _tm_list || ') OR DR.id1_thd IN (' || _tm_list || ') OR DR.id2_thd IN (' || _tm_list || '))';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel10, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _id11, _id12, _id13, _id14, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF (_id13 = _id OR _id14 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)'))) THEN
				_item.txt2 := 'thd';
			ELSIF (_id9 = _id OR _id10 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)'))) THEN
				_item.txt2 := 'sf1';
			ELSIF (_id11 = _id OR _id12 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)'))) THEN
				_item.txt2 := 'sf2';
			ELSIF (_id1 = _id OR _id2 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)'))) THEN
				_item.txt2 := 'qf1';
			ELSIF (_id3 = _id OR _id4 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)'))) THEN
				_item.txt2 := 'qf2';
			ELSIF (_id5 = _id OR _id6 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)'))) THEN
				_item.txt2 := 'qf3';
			ELSIF (_id7 = _id OR _id8 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)'))) THEN
				_item.txt2 := 'qf4';
			END IF;
			IF _type1 <= 10 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN '', '' || PR1.first_name ELSE '''' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || '' '' ELSE '''' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN '', '' || PR2.first_name ELSE '''' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || '' '' ELSE '''' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, DR.result_' || _item.txt2;
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "PERSON" PR1 ON DR.id1_' || _item.txt2 || ' = PR1.id LEFT JOIN "PERSON" PR2 ON DR.id2_' || _item.txt2 || ' = PR2.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel8, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel9, _item.id_rel13, _cn2, _tm2, _item.txt1;
				CLOSE __c;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				_item.comment := 'PR';
			ELSIF _type1 = 50 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', TM1.label, TM2.label, NULL, NULL, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "TEAM" TM1 ON DR.id1_' || _item.txt2 || ' = TM1.id LEFT JOIN "TEAM" TM2 ON DR.id2_' || _item.txt2 || ' = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'TM';
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', CN1.label' || _lang || ', CN2.label' || _lang || ', CN1.label, CN2.label, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "COUNTRY" CN1 ON DR.id1_' || _item.txt2 || ' = CN1.id LEFT JOIN "COUNTRY" CN2 ON DR.id2_' || _item.txt2 || ' = CN2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'CN';
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
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number, TP3.number';
		IF (_entity = 'PR') THEN
			_query := _query || ', PL.rank';
		ELSE
			_query := _query || ', 0';
		END IF;
		_query := _query || ' FROM "RESULT" RS';
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
		ELSIF (_entity = 'PR') THEN
			_type1 = 1;
			_type2 = 99;
			_query := _query || ' LEFT JOIN "~PERSON_LIST" PL ON PL.id_result = RS.id';
		END IF;
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RS.id_rank1 = ' || _id || ' OR RS.id_rank2 = ' || _id || ' OR RS.id_rank3 = ' || _id || ' OR RS.id_rank4 = ' || _id || ' OR RS.id_rank5 = ' || _id || ' OR RS.id_rank6 = ' || _id || ' OR RS.id_rank7 = ' || _id || ' OR RS.id_rank8 = ' || _id || ' OR RS.id_rank9 = ' || _id || ' OR RS.id_rank10 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (PL.id_person IN (' || _pr_list || ') OR (((TP1.number BETWEEN 1 AND 10 AND TP2.number IS NULL) OR (TP2.number BETWEEN 1 AND 10 AND TP3.number IS NULL) OR (TP3.number BETWEEN 1 AND 10)) AND ((RS.id_rank1 IN (' || _pr_list || ') OR RS.id_rank2 IN (' || _pr_list || ') OR RS.id_rank3 IN (' || _pr_list || ') OR RS.id_rank4 IN (' || _pr_list || ') OR RS.id_rank5 IN (' || _pr_list || ') OR RS.id_rank6 IN (' || _pr_list || ') OR RS.id_rank7 IN (' || _pr_list || ') OR RS.id_rank8 IN (' || _pr_list || ') OR RS.id_rank9 IN (' || _pr_list || ') OR RS.id_rank10 IN (' || _pr_list || ')))))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RS.id_rank1 IN (' || _tm_list || ') OR RS.id_rank2 IN (' || _tm_list || ') OR RS.id_rank3 IN (' || _tm_list || ') OR RS.id_rank4 IN (' || _tm_list || ') OR RS.id_rank5 IN (' || _tm_list || ') OR RS.id_rank6 IN (' || _tm_list || ') OR RS.id_rank7 IN (' || _tm_list || ') OR RS.id_rank8 IN (' || _tm_list || ') OR RS.id_rank9 IN (' || _tm_list || ') OR RS.id_rank10 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND  (RS.id_city1 IN (' || _ct_list || ') OR RS.id_city2 IN (' || _ct_list || '))';
		ELSIF _entity = 'CX' THEN
			_query := _query || ' AND  (RS.id_complex1 IN (' || _cx_list || ') OR RS.id_complex2 IN (' || _cx_list || '))';
		ELSIF _entity = 'OL' THEN
			_query := _query || ' AND RS.id_championship=1 AND OL.id = ' || _id;
		ELSIF _entity = 'YR' THEN
			_query := _query || ' AND RS.id_year = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2, _type3, _item.count1;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF _type1 <= 10 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, CN3.code, TM3.label, PR4.last_name || (CASE WHEN length(PR4.first_name) > 0 THEN ', ' || PR4.first_name ELSE '' END), (CASE WHEN length(PR4.first_name) > 0 THEN PR4.first_name || ' ' ELSE '' END) || PR4.last_name, CN4.id, CN4.code, TM4.label, PR5.last_name || (CASE WHEN length(PR5.first_name) > 0 THEN ', ' || PR5.first_name ELSE '' END), (CASE WHEN length(PR5.first_name) > 0 THEN PR5.first_name || ' ' ELSE '' END) || PR5.last_name, CN5.id, CN5.code, TM5.label, PR6.last_name || (CASE WHEN length(PR6.first_name) > 0 THEN ', ' || PR6.first_name ELSE '' END), (CASE WHEN length(PR6.first_name) > 0 THEN PR6.first_name || ' ' ELSE '' END) || PR6.last_name, CN6.id, CN6.code, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.label_rel23, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.label_rel24, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.label_rel25, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt3, _item.txt4
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
				IF _type1 = 4 OR _item.txt3 = '#DOUBLE#' THEN
					_item.txt4 = '1-2/3-4/5-6';
				ELSIF _type1 = 5 OR _item.txt3 = '#TRIPLE#' THEN
					_item.txt4 = '1-3/4-6/7-9';
				END IF;
				_item.comment := 'PR';
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, NULL, NULL, NULL, NULL, NULL, NULL, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "TEAM" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "TEAM" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "TEAM" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "TEAM" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "TEAM" TM5 ON RS.id_rank5 = TM5.id LEFT JOIN "TEAM" TM6 ON RS.id_rank6 = TM6.id
				WHERE RS.id = _item.id_item;
				_item.comment := 'TM';
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN4.label' || _lang || ', CN5.label' || _lang || ', CN6.label' || _lang || ', CN1.label, CN2.label, CN3.label, CN4.label, CN5.label, CN6.label, RS.result1, RS.result2, RS.comment, RS.exa';
				_query := _query || ' FROM "RESULT" RS LEFT JOIN "COUNTRY" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "COUNTRY" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "COUNTRY" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "COUNTRY" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "COUNTRY" CN5 ON RS.id_rank5 = CN5.id LEFT JOIN "COUNTRY" CN6 ON RS.id_rank6 = CN6.id';
				_query := _query || ' WHERE RS.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4;
				CLOSE __c;
				_item.comment := 'CN';
			END IF;
			IF _item.id_rel6 = _id OR (_item.id_rel7 = _id AND (_type1 IN (4,5) OR _item.txt3 IN ('#DOUBLE#', '#TRIPLE#') OR _item.txt4 ~ '(^|/)1.*(/|$)')) OR (_item.id_rel8 = _id AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)1-(3|4|5|6|7|8)(/|$)')) OR (_item.id_rel9 = _id AND _item.txt4 ~ '(^|/)1-(4|5|6|7|8)(/|$)') OR (_item.id_rel10 = _id AND _item.txt4 ~ '(^|/)1-(5|6|7|8)(/|$)') OR (_item.id_rel11 = _id AND _item.txt4 ~ '(^|/)1-(6|7|8)(/|$)') THEN
				_item.count1 := 1;
			ELSIF _item.id_rel7 = _id OR (_item.id_rel8 = _id AND (_type1 = 4 OR _item.txt3 = '#DOUBLE#' OR _item.txt4 ~ '(^|/)2.*(/|$)')) OR (_item.id_rel9 = _id AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)2-(4|5|6|7|8).*(/|$)')) OR (_item.id_rel10 = _id AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)2-(5|6|7|8).*(/|$)')) OR (_item.id_rel11 = _id AND _item.txt4 ~ '(^|/)2-(6|7|8).*(/|$)') THEN
				_item.count1 := 2;
			ELSIF _item.id_rel8 = _id/* OR (_item.id_rel9 = _id AND (_type1 = 4 OR _item.txt3 = '#DOUBLE#' OR _item.txt4 ~ '(^|/)2.*(/|$)')) OR (_item.id_rel9 = _id AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)2-(4|5|6|7|8).*(/|$)')) OR (_item.id_rel10 = _id AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)2-(5|6|7|8).*(/|$)')) OR (_item.id_rel11 = _id AND _item.txt4 ~ '(^|/)2-(6|7|8).*(/|$)') */THEN
				_item.count1 := 3;
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
		_query := 'SELECT DISTINCT ON (PR.last_name, PR.first_name, CN.id, SP.id) PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "PERSON" PR';
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
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT TM.id, TM.label, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "TEAM" TM';
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
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT CT.id, CT.label' || _lang || ', CT.label, CN.id, CN.label' || _lang || ', CN.label FROM "CITY" CT';
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
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.label_rel2;
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
		_query := 'SELECT CX.id, CX.label' || _lang || ', CX.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label FROM "COMPLEX" CX';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' WHERE CX.id_city = ' || _id;
		_query := _query || ' ORDER BY CX.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OL.type FROM "OLYMPICS" OL';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query := _query || ' WHERE OL.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY OL.type, YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT OR_.id, OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OR_.count_gold || '','' || OR_.count_silver || '','' || OR_.count_bronze FROM "OLYMPIC_RANKING" OR_';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT RC.id, RC.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, RC.type1, RC.type2, RC.record1, RC.id_rank1, RC.id_rank2, RC.id_rank3, RC.id_rank4, RC.id_rank5 FROM "RECORD" RC';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RC.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RC.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RC.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "TYPE" TP ON EV.id_type = TP.id';
		_query := _query || ' WHERE TP.number BETWEEN ' || _type1 || ' AND ' || _type2;
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RC.id_rank1 = ' || _id || ' OR RC.id_rank2 = ' || _id || ' OR RC.id_rank3 = ' || _id || ' OR RC.id_rank4 = ' || _id || ' OR RC.id_rank5 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _pr_list || ') OR RC.id_rank2 IN (' || _pr_list || ') OR RC.id_rank3 IN (' || _pr_list || ') OR RC.id_rank4 IN (' || _pr_list || ') OR RC.id_rank5 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _tm_list || ') OR RC.id_rank2 IN (' || _tm_list || ') OR RC.id_rank3 IN (' || _tm_list || ') OR RC.id_rank4 IN (' || _tm_list || ') OR RC.id_rank5 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RC.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RC.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RC.id_event = ' || _id || ' OR RC.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND RC.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', RC.index LIMIT ' || _limit || ' OFFSET ' || _offset;
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
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Hall of Fame]
	IF (_entity ~ 'YR|PR' AND (_entity_ref = 'HF' OR _entity_ref = '')) THEN
		_query := 'SELECT HF.id, YR.id, YR.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, HF.position FROM "HALL_OF_FAME" HF';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.txt1;
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
		_query := 'SELECT RN.id, TM.id, TM.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, RN.number FROM "RETIRED_NUMBER" RN';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.id_rel4;
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
		_query := 'SELECT TS.id, TM.id, TM.label, CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', CX.label, CT.label, ST.label, CN.label, LG.id, LG.label, TS.date1, TS.date2 FROM "TEAM_STADIUM" TS';
		_query := _query || ' LEFT JOIN "TEAM" TM ON TS.id_team = TM.id';
		_query := _query || ' LEFT JOIN "COMPLEX" CX ON TS.id_complex = CX.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON TS.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE TS.id_team = ' || _id;
		ELSIF _entity = 'CX' THEN
			_query := _query || ' WHERE TS.id_complex IN (' || _cx_list || ')';
		END IF;
		_query := _query || ' ORDER BY TM.label, TS.date1 DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.id_rel6, _item.comment, _item.txt1, _item.txt2;
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
		_query := 'SELECT WL.id, TM.id, TM.label, LG.id, LG.label, WL.type, WL.count_win || '','' || WL.count_loss FROM "WIN_LOSS" WL';
		_query := _query || ' LEFT JOIN "TEAM" TM ON WL.id_team = TM.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON WL.id_league = LG.id';
		_query := _query || ' WHERE WL.id_team = ' || _id;
		_query := _query || ' ORDER BY TM.label DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'WL';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Contributions]
	IF (_entity ~ 'CB' AND (_entity_ref = 'CO' OR _entity_ref = '')) THEN
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, CO.type, CO.date';
		_query := _query || ' FROM "~CONTRIBUTION" CO';
		_query := _query || ' LEFT JOIN "RESULT" RS ON CO.id_item = RS.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' WHERE RS.id_member=' || _id || ' ORDER BY CO.date DESC, YR.id DESC, SP.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.date1;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CO';
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
  
  
  
  
  
  
  
  ALTER  TABLE "WIN_LOSS" drop column average;
  
  
  
  
  CREATE OR REPLACE FUNCTION "GET_WIN_LOSS"(_id_league integer, _teams text)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _team_condition text;
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'US', 'WL-' || _id_league, current_date);

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
		WL.count_otloss AS wl_count_otloss
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
  
  
  
  
  CREATE OR REPLACE FUNCTION "ENTITY_REF"(_entity character varying, _id integer, _entity_ref character varying, _limit character varying, _offset integer, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_c refcursor;
	__c refcursor;
	_query text;
	_link integer;
	_ct_list varchar(200);
	_cx_list varchar(200);
	_pr_list varchar(200);
	_tm_list varchar(200);
	_index integer;
	_type1 integer;
	_type2 integer;
	_type3 integer;
	_array_id integer[];
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
	_id11 integer;_id12 integer;_id13 integer;_id14 integer;
	_cn1 varchar(5);_cn2 varchar(5);_cn3 varchar(5);_cn4 varchar(5);_cn5 varchar(5);_cn6 varchar(5);
	_tm1 varchar(60);_tm2 varchar(60);_tm3 varchar(60);_tm4 varchar(60);_tm5 varchar(60);_tm6 varchar(60);
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'IF', _entity || '-' || _id, current_date);
	
	_index := 1;

	IF _entity ~ 'CT' THEN
		SELECT LINK INTO _link FROM "CITY" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "CITY" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_ct_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_ct_list := _ct_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_ct_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'CX' THEN
		SELECT LINK INTO _link FROM "COMPLEX" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "COMPLEX" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_cx_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_cx_list := _cx_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_cx_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'PR' THEN
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
	ELSIF _entity ~ 'TM' THEN
		SELECT LINK INTO _link FROM "TEAM" WHERE ID = _id AND (YEAR1 IS NULL OR YEAR1 = '');
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "TEAM" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_tm_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_tm_list := _tm_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_tm_list := cast(_id AS varchar);
		END IF;
	END IF;

	-- References in: [Draws] (MUST STAY AT FIRST)
	IF (_entity ~ 'CN|PR|TM' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT DR.id, DR.id_result, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, DR.id1_qf1, DR.id2_qf1, DR.id1_qf2, DR.id2_qf2, DR.id1_qf3, DR.id2_qf3, DR.id1_qf4, DR.id2_qf4, DR.id1_sf1, DR.id2_sf1, DR.id1_sf2, DR.id2_sf2, DR.id1_thd, DR.id2_thd, TP1.number, TP2.number, TP3.number FROM "DRAW" DR';
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
		IF _entity = 'CN' THEN
			_query := _query || ' AND (DR.id1_sf1 <> ' || _id || ' AND DR.id1_sf2 <> ' || _id || ') AND (DR.id1_qf1 = ' || _id || ' OR DR.id2_qf1 = ' || _id || ' OR DR.id1_qf2 = ' || _id || ' OR DR.id2_qf2 = ' || _id || ' OR DR.id1_qf3 = ' || _id || ' OR DR.id2_qf3 = ' || _id || ' OR DR.id1_qf4 = ' || _id || ' OR DR.id2_qf4 = ' || _id || ' OR DR.id2_sf1 = ' || _id || ' OR DR.id2_sf2 = ' || _id || ' OR DR.id1_thd = ' || _id || ' OR DR.id2_thd = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _pr_list || ') AND DR.id1_sf2 NOT IN (' || _pr_list || ')) AND (DR.id1_qf1 IN (' || _pr_list || ') OR DR.id2_qf1 IN (' || _pr_list || ') OR DR.id1_qf2 IN (' || _pr_list || ') OR DR.id2_qf2 IN (' || _pr_list || ') OR DR.id1_qf3 IN (' || _pr_list || ') OR DR.id2_qf3 IN (' || _pr_list || ') OR DR.id1_qf4 IN (' || _pr_list || ') OR DR.id2_qf4 IN (' || _pr_list || ') OR DR.id2_sf1 IN (' || _pr_list || ') OR DR.id2_sf2 IN (' || _pr_list || ') OR DR.id1_thd IN (' || _pr_list || ') OR DR.id2_thd IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _tm_list || ') AND DR.id1_sf2 NOT IN (' || _tm_list || ')) AND (DR.id1_qf1 IN (' || _tm_list || ') OR DR.id2_qf1 IN (' || _tm_list || ') OR DR.id1_qf2 IN (' || _tm_list || ') OR DR.id2_qf2 IN (' || _tm_list || ') OR DR.id1_qf3 IN (' || _tm_list || ') OR DR.id2_qf3 IN (' || _tm_list || ') OR DR.id1_qf4 IN (' || _tm_list || ') OR DR.id2_qf4 IN (' || _tm_list || ') OR DR.id2_sf1 IN (' || _tm_list || ') OR DR.id2_sf2 IN (' || _tm_list || ') OR DR.id1_thd IN (' || _tm_list || ') OR DR.id2_thd IN (' || _tm_list || '))';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel10, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _id11, _id12, _id13, _id14, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF (_id13 = _id OR _id14 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)'))) THEN
				_item.txt2 := 'thd';
			ELSIF (_id9 = _id OR _id10 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)'))) THEN
				_item.txt2 := 'sf1';
			ELSIF (_id11 = _id OR _id12 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)'))) THEN
				_item.txt2 := 'sf2';
			ELSIF (_id1 = _id OR _id2 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)'))) THEN
				_item.txt2 := 'qf1';
			ELSIF (_id3 = _id OR _id4 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)'))) THEN
				_item.txt2 := 'qf2';
			ELSIF (_id5 = _id OR _id6 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)'))) THEN
				_item.txt2 := 'qf3';
			ELSIF (_id7 = _id OR _id8 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)'))) THEN
				_item.txt2 := 'qf4';
			END IF;
			IF _type1 <= 10 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN '', '' || PR1.first_name ELSE '''' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || '' '' ELSE '''' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN '', '' || PR2.first_name ELSE '''' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || '' '' ELSE '''' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, DR.result_' || _item.txt2;
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "PERSON" PR1 ON DR.id1_' || _item.txt2 || ' = PR1.id LEFT JOIN "PERSON" PR2 ON DR.id2_' || _item.txt2 || ' = PR2.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel8, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel9, _item.id_rel13, _cn2, _tm2, _item.txt1;
				CLOSE __c;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				_item.comment := 'PR';
			ELSIF _type1 = 50 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', TM1.label, TM2.label, NULL, NULL, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "TEAM" TM1 ON DR.id1_' || _item.txt2 || ' = TM1.id LEFT JOIN "TEAM" TM2 ON DR.id2_' || _item.txt2 || ' = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'TM';
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', CN1.label' || _lang || ', CN2.label' || _lang || ', CN1.label, CN2.label, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "COUNTRY" CN1 ON DR.id1_' || _item.txt2 || ' = CN1.id LEFT JOIN "COUNTRY" CN2 ON DR.id2_' || _item.txt2 || ' = CN2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'CN';
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
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number, TP3.number';
		IF (_entity = 'PR') THEN
			_query := _query || ', PL.rank';
		ELSE
			_query := _query || ', 0';
		END IF;
		_query := _query || ' FROM "RESULT" RS';
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
		ELSIF (_entity = 'PR') THEN
			_type1 = 1;
			_type2 = 99;
			_query := _query || ' LEFT JOIN "~PERSON_LIST" PL ON PL.id_result = RS.id';
		END IF;
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RS.id_rank1 = ' || _id || ' OR RS.id_rank2 = ' || _id || ' OR RS.id_rank3 = ' || _id || ' OR RS.id_rank4 = ' || _id || ' OR RS.id_rank5 = ' || _id || ' OR RS.id_rank6 = ' || _id || ' OR RS.id_rank7 = ' || _id || ' OR RS.id_rank8 = ' || _id || ' OR RS.id_rank9 = ' || _id || ' OR RS.id_rank10 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (PL.id_person IN (' || _pr_list || ') OR (((TP1.number BETWEEN 1 AND 10 AND TP2.number IS NULL) OR (TP2.number BETWEEN 1 AND 10 AND TP3.number IS NULL) OR (TP3.number BETWEEN 1 AND 10)) AND ((RS.id_rank1 IN (' || _pr_list || ') OR RS.id_rank2 IN (' || _pr_list || ') OR RS.id_rank3 IN (' || _pr_list || ') OR RS.id_rank4 IN (' || _pr_list || ') OR RS.id_rank5 IN (' || _pr_list || ') OR RS.id_rank6 IN (' || _pr_list || ') OR RS.id_rank7 IN (' || _pr_list || ') OR RS.id_rank8 IN (' || _pr_list || ') OR RS.id_rank9 IN (' || _pr_list || ') OR RS.id_rank10 IN (' || _pr_list || ')))))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RS.id_rank1 IN (' || _tm_list || ') OR RS.id_rank2 IN (' || _tm_list || ') OR RS.id_rank3 IN (' || _tm_list || ') OR RS.id_rank4 IN (' || _tm_list || ') OR RS.id_rank5 IN (' || _tm_list || ') OR RS.id_rank6 IN (' || _tm_list || ') OR RS.id_rank7 IN (' || _tm_list || ') OR RS.id_rank8 IN (' || _tm_list || ') OR RS.id_rank9 IN (' || _tm_list || ') OR RS.id_rank10 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND  (RS.id_city1 IN (' || _ct_list || ') OR RS.id_city2 IN (' || _ct_list || '))';
		ELSIF _entity = 'CX' THEN
			_query := _query || ' AND  (RS.id_complex1 IN (' || _cx_list || ') OR RS.id_complex2 IN (' || _cx_list || '))';
		ELSIF _entity = 'OL' THEN
			_query := _query || ' AND RS.id_championship=1 AND OL.id = ' || _id;
		ELSIF _entity = 'YR' THEN
			_query := _query || ' AND RS.id_year = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2, _type3, _item.count1;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF _type1 <= 10 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, CN3.code, TM3.label, PR4.last_name || (CASE WHEN length(PR4.first_name) > 0 THEN ', ' || PR4.first_name ELSE '' END), (CASE WHEN length(PR4.first_name) > 0 THEN PR4.first_name || ' ' ELSE '' END) || PR4.last_name, CN4.id, CN4.code, TM4.label, PR5.last_name || (CASE WHEN length(PR5.first_name) > 0 THEN ', ' || PR5.first_name ELSE '' END), (CASE WHEN length(PR5.first_name) > 0 THEN PR5.first_name || ' ' ELSE '' END) || PR5.last_name, CN5.id, CN5.code, TM5.label, PR6.last_name || (CASE WHEN length(PR6.first_name) > 0 THEN ', ' || PR6.first_name ELSE '' END), (CASE WHEN length(PR6.first_name) > 0 THEN PR6.first_name || ' ' ELSE '' END) || PR6.last_name, CN6.id, CN6.code, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.label_rel23, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.label_rel24, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.label_rel25, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt3, _item.txt4
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
				IF _type1 = 4 OR _item.txt3 = '#DOUBLE#' THEN
					_item.txt4 = '1-2/3-4/5-6';
				ELSIF _type1 = 5 OR _item.txt3 = '#TRIPLE#' THEN
					_item.txt4 = '1-3/4-6/7-9';
				END IF;
				_item.comment := 'PR';
				_array_id := string_to_array(_pr_list, ',')::integer[];
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, NULL, NULL, NULL, NULL, NULL, NULL, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "TEAM" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "TEAM" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "TEAM" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "TEAM" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "TEAM" TM5 ON RS.id_rank5 = TM5.id LEFT JOIN "TEAM" TM6 ON RS.id_rank6 = TM6.id
				WHERE RS.id = _item.id_item;
				_item.comment := 'TM';
				_array_id := string_to_array(_tm_list, ',')::integer[];
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN4.label' || _lang || ', CN5.label' || _lang || ', CN6.label' || _lang || ', CN1.label, CN2.label, CN3.label, CN4.label, CN5.label, CN6.label, RS.result1, RS.result2, RS.comment, RS.exa';
				_query := _query || ' FROM "RESULT" RS LEFT JOIN "COUNTRY" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "COUNTRY" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "COUNTRY" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "COUNTRY" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "COUNTRY" CN5 ON RS.id_rank5 = CN5.id LEFT JOIN "COUNTRY" CN6 ON RS.id_rank6 = CN6.id';
				_query := _query || ' WHERE RS.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4;
				CLOSE __c;
				_item.comment := 'CN';
				_array_id[0] := ARRAY[_id];
			END IF;
			IF _item.id_rel6 = ANY(_array_id) OR (_item.id_rel7 = ANY(_array_id) AND (_type1 IN (4,5) OR _item.txt3 IN ('#DOUBLE#', '#TRIPLE#') OR _item.txt4 ~ '(^|/)1.*(/|$)')) OR (_item.id_rel8 = ANY(_array_id) AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)1-(3|4|5|6|7|8)(/|$)')) OR (_item.id_rel9 = ANY(_array_id) AND _item.txt4 ~ '(^|/)1-(4|5|6|7|8)(/|$)') OR (_item.id_rel10 = ANY(_array_id) AND _item.txt4 ~ '(^|/)1-(5|6|7|8)(/|$)') OR (_item.id_rel11 = ANY(_array_id) AND _item.txt4 ~ '(^|/)1-(6|7|8)(/|$)') THEN
				_item.count1 := 1;
			ELSIF _item.id_rel7 = ANY(_array_id) OR (_item.id_rel8 = ANY(_array_id) AND (_type1 = 4 OR _item.txt3 = '#DOUBLE#' OR _item.txt4 ~ '(^|/)2.*(/|$)')) OR (_item.id_rel9 = ANY(_array_id) AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)2-(4|5|6|7|8).*(/|$)')) OR (_item.id_rel10 = ANY(_array_id) AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)2-(5|6|7|8).*(/|$)')) OR (_item.id_rel11 = ANY(_array_id) AND _item.txt4 ~ '(^|/)2-(6|7|8).*(/|$)') THEN
				_item.count1 := 2;
			ELSIF _item.id_rel8 = ANY(_array_id)/* OR (_item.id_rel9 = ANY(_array_id) AND (_type1 = 4 OR _item.txt3 = '#DOUBLE#' OR _item.txt4 ~ '(^|/)2.*(/|$)')) OR (_item.id_rel9 = ANY(_array_id) AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)2-(4|5|6|7|8).*(/|$)')) OR (_item.id_rel10 = ANY(_array_id) AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)2-(5|6|7|8).*(/|$)')) OR (_item.id_rel11 = ANY(_array_id) AND _item.txt4 ~ '(^|/)2-(6|7|8).*(/|$)') */THEN
				_item.count1 := 3;
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
		_query := 'SELECT DISTINCT ON (PR.last_name, PR.first_name, CN.id, SP.id) PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "PERSON" PR';
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
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT TM.id, TM.label, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "TEAM" TM';
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
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT CT.id, CT.label' || _lang || ', CT.label, CN.id, CN.label' || _lang || ', CN.label FROM "CITY" CT';
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
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.label_rel2;
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
		_query := 'SELECT CX.id, CX.label' || _lang || ', CX.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label FROM "COMPLEX" CX';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' WHERE CX.id_city = ' || _id;
		_query := _query || ' ORDER BY CX.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OL.type FROM "OLYMPICS" OL';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query := _query || ' WHERE OL.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY OL.type, YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT OR_.id, OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OR_.count_gold || '','' || OR_.count_silver || '','' || OR_.count_bronze FROM "OLYMPIC_RANKING" OR_';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT RC.id, RC.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, RC.type1, RC.type2, RC.record1, RC.id_rank1, RC.id_rank2, RC.id_rank3, RC.id_rank4, RC.id_rank5 FROM "RECORD" RC';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RC.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RC.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RC.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "TYPE" TP ON EV.id_type = TP.id';
		_query := _query || ' WHERE TP.number BETWEEN ' || _type1 || ' AND ' || _type2;
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RC.id_rank1 = ' || _id || ' OR RC.id_rank2 = ' || _id || ' OR RC.id_rank3 = ' || _id || ' OR RC.id_rank4 = ' || _id || ' OR RC.id_rank5 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _pr_list || ') OR RC.id_rank2 IN (' || _pr_list || ') OR RC.id_rank3 IN (' || _pr_list || ') OR RC.id_rank4 IN (' || _pr_list || ') OR RC.id_rank5 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _tm_list || ') OR RC.id_rank2 IN (' || _tm_list || ') OR RC.id_rank3 IN (' || _tm_list || ') OR RC.id_rank4 IN (' || _tm_list || ') OR RC.id_rank5 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RC.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RC.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RC.id_event = ' || _id || ' OR RC.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND RC.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', RC.index LIMIT ' || _limit || ' OFFSET ' || _offset;
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
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Hall of Fame]
	IF (_entity ~ 'YR|PR' AND (_entity_ref = 'HF' OR _entity_ref = '')) THEN
		_query := 'SELECT HF.id, YR.id, YR.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, HF.position FROM "HALL_OF_FAME" HF';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.txt1;
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
		_query := 'SELECT RN.id, TM.id, TM.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, RN.number FROM "RETIRED_NUMBER" RN';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.id_rel4;
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
		_query := 'SELECT TS.id, TM.id, TM.label, CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', CX.label, CT.label, ST.label, CN.label, LG.id, LG.label, TS.date1, TS.date2 FROM "TEAM_STADIUM" TS';
		_query := _query || ' LEFT JOIN "TEAM" TM ON TS.id_team = TM.id';
		_query := _query || ' LEFT JOIN "COMPLEX" CX ON TS.id_complex = CX.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON TS.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE TS.id_team = ' || _id;
		ELSIF _entity = 'CX' THEN
			_query := _query || ' WHERE TS.id_complex IN (' || _cx_list || ')';
		END IF;
		_query := _query || ' ORDER BY TM.label, TS.date1 DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.id_rel6, _item.comment, _item.txt1, _item.txt2;
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
		_query := 'SELECT WL.id, TM.id, TM.label, LG.id, LG.label, WL.type, WL.count_win || '','' || WL.count_loss FROM "WIN_LOSS" WL';
		_query := _query || ' LEFT JOIN "TEAM" TM ON WL.id_team = TM.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON WL.id_league = LG.id';
		_query := _query || ' WHERE WL.id_team = ' || _id;
		_query := _query || ' ORDER BY TM.label DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'WL';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Contributions]
	IF (_entity ~ 'CB' AND (_entity_ref = 'CO' OR _entity_ref = '')) THEN
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, CO.type, CO.date';
		_query := _query || ' FROM "~CONTRIBUTION" CO';
		_query := _query || ' LEFT JOIN "RESULT" RS ON CO.id_item = RS.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' WHERE RS.id_member=' || _id || ' ORDER BY CO.date DESC, YR.id DESC, SP.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.date1;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CO';
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
  
  
  
  
  CREATE OR REPLACE FUNCTION "ENTITY_REF"(_entity character varying, _id integer, _entity_ref character varying, _limit character varying, _offset integer, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_c refcursor;
	__c refcursor;
	_query text;
	_link integer;
	_ct_list varchar(200);
	_cx_list varchar(200);
	_pr_list varchar(200);
	_tm_list varchar(200);
	_index integer;
	_type1 integer;
	_type2 integer;
	_type3 integer;
	_array_id integer[];
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
	_id11 integer;_id12 integer;_id13 integer;_id14 integer;
	_cn1 varchar(5);_cn2 varchar(5);_cn3 varchar(5);_cn4 varchar(5);_cn5 varchar(5);_cn6 varchar(5);
	_tm1 varchar(60);_tm2 varchar(60);_tm3 varchar(60);_tm4 varchar(60);_tm5 varchar(60);_tm6 varchar(60);
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'IF', _entity || '-' || _id, current_date);
	
	_index := 1;

	IF _entity ~ 'CT' THEN
		SELECT LINK INTO _link FROM "CITY" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "CITY" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_ct_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_ct_list := _ct_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_ct_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'CX' THEN
		SELECT LINK INTO _link FROM "COMPLEX" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "COMPLEX" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_cx_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_cx_list := _cx_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_cx_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'PR' THEN
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
	ELSIF _entity ~ 'TM' THEN
		SELECT LINK INTO _link FROM "TEAM" WHERE ID = _id AND (YEAR1 IS NULL OR YEAR1 = '');
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "TEAM" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_tm_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_tm_list := _tm_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_tm_list := cast(_id AS varchar);
		END IF;
	END IF;

	-- References in: [Draws] (MUST STAY AT FIRST)
	IF (_entity ~ 'CN|PR|TM' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT DR.id, DR.id_result, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, DR.id1_qf1, DR.id2_qf1, DR.id1_qf2, DR.id2_qf2, DR.id1_qf3, DR.id2_qf3, DR.id1_qf4, DR.id2_qf4, DR.id1_sf1, DR.id2_sf1, DR.id1_sf2, DR.id2_sf2, DR.id1_thd, DR.id2_thd, TP1.number, TP2.number, TP3.number FROM "DRAW" DR';
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
		IF _entity = 'CN' THEN
			_query := _query || ' AND (DR.id1_sf1 <> ' || _id || ' AND DR.id1_sf2 <> ' || _id || ') AND (DR.id1_qf1 = ' || _id || ' OR DR.id2_qf1 = ' || _id || ' OR DR.id1_qf2 = ' || _id || ' OR DR.id2_qf2 = ' || _id || ' OR DR.id1_qf3 = ' || _id || ' OR DR.id2_qf3 = ' || _id || ' OR DR.id1_qf4 = ' || _id || ' OR DR.id2_qf4 = ' || _id || ' OR DR.id2_sf1 = ' || _id || ' OR DR.id2_sf2 = ' || _id || ' OR DR.id1_thd = ' || _id || ' OR DR.id2_thd = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _pr_list || ') AND DR.id1_sf2 NOT IN (' || _pr_list || ')) AND (DR.id1_qf1 IN (' || _pr_list || ') OR DR.id2_qf1 IN (' || _pr_list || ') OR DR.id1_qf2 IN (' || _pr_list || ') OR DR.id2_qf2 IN (' || _pr_list || ') OR DR.id1_qf3 IN (' || _pr_list || ') OR DR.id2_qf3 IN (' || _pr_list || ') OR DR.id1_qf4 IN (' || _pr_list || ') OR DR.id2_qf4 IN (' || _pr_list || ') OR DR.id2_sf1 IN (' || _pr_list || ') OR DR.id2_sf2 IN (' || _pr_list || ') OR DR.id1_thd IN (' || _pr_list || ') OR DR.id2_thd IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _tm_list || ') AND DR.id1_sf2 NOT IN (' || _tm_list || ')) AND (DR.id1_qf1 IN (' || _tm_list || ') OR DR.id2_qf1 IN (' || _tm_list || ') OR DR.id1_qf2 IN (' || _tm_list || ') OR DR.id2_qf2 IN (' || _tm_list || ') OR DR.id1_qf3 IN (' || _tm_list || ') OR DR.id2_qf3 IN (' || _tm_list || ') OR DR.id1_qf4 IN (' || _tm_list || ') OR DR.id2_qf4 IN (' || _tm_list || ') OR DR.id2_sf1 IN (' || _tm_list || ') OR DR.id2_sf2 IN (' || _tm_list || ') OR DR.id1_thd IN (' || _tm_list || ') OR DR.id2_thd IN (' || _tm_list || '))';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel10, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _id11, _id12, _id13, _id14, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF (_id13 = _id OR _id14 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)'))) THEN
				_item.txt2 := 'thd';
			ELSIF (_id9 = _id OR _id10 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)'))) THEN
				_item.txt2 := 'sf1';
			ELSIF (_id11 = _id OR _id12 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)'))) THEN
				_item.txt2 := 'sf2';
			ELSIF (_id1 = _id OR _id2 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)'))) THEN
				_item.txt2 := 'qf1';
			ELSIF (_id3 = _id OR _id4 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)'))) THEN
				_item.txt2 := 'qf2';
			ELSIF (_id5 = _id OR _id6 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)'))) THEN
				_item.txt2 := 'qf3';
			ELSIF (_id7 = _id OR _id8 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)'))) THEN
				_item.txt2 := 'qf4';
			END IF;
			IF _type1 <= 10 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN '', '' || PR1.first_name ELSE '''' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || '' '' ELSE '''' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN '', '' || PR2.first_name ELSE '''' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || '' '' ELSE '''' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, DR.result_' || _item.txt2;
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "PERSON" PR1 ON DR.id1_' || _item.txt2 || ' = PR1.id LEFT JOIN "PERSON" PR2 ON DR.id2_' || _item.txt2 || ' = PR2.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel8, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel9, _item.id_rel13, _cn2, _tm2, _item.txt1;
				CLOSE __c;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				_item.comment := 'PR';
			ELSIF _type1 = 50 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', TM1.label, TM2.label, NULL, NULL, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "TEAM" TM1 ON DR.id1_' || _item.txt2 || ' = TM1.id LEFT JOIN "TEAM" TM2 ON DR.id2_' || _item.txt2 || ' = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'TM';
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', CN1.label' || _lang || ', CN2.label' || _lang || ', CN1.label, CN2.label, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "COUNTRY" CN1 ON DR.id1_' || _item.txt2 || ' = CN1.id LEFT JOIN "COUNTRY" CN2 ON DR.id2_' || _item.txt2 || ' = CN2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'CN';
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
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number, TP3.number';
		IF (_entity = 'PR') THEN
			_query := _query || ', PL.rank';
		ELSE
			_query := _query || ', 0';
		END IF;
		_query := _query || ' FROM "RESULT" RS';
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
		ELSIF (_entity = 'PR') THEN
			_type1 = 1;
			_type2 = 99;
			_query := _query || ' LEFT JOIN "~PERSON_LIST" PL ON PL.id_result = RS.id';
		END IF;
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RS.id_rank1 = ' || _id || ' OR RS.id_rank2 = ' || _id || ' OR RS.id_rank3 = ' || _id || ' OR RS.id_rank4 = ' || _id || ' OR RS.id_rank5 = ' || _id || ' OR RS.id_rank6 = ' || _id || ' OR RS.id_rank7 = ' || _id || ' OR RS.id_rank8 = ' || _id || ' OR RS.id_rank9 = ' || _id || ' OR RS.id_rank10 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (PL.id_person IN (' || _pr_list || ') OR (((TP1.number BETWEEN 1 AND 10 AND TP2.number IS NULL) OR (TP2.number BETWEEN 1 AND 10 AND TP3.number IS NULL) OR (TP3.number BETWEEN 1 AND 10)) AND ((RS.id_rank1 IN (' || _pr_list || ') OR RS.id_rank2 IN (' || _pr_list || ') OR RS.id_rank3 IN (' || _pr_list || ') OR RS.id_rank4 IN (' || _pr_list || ') OR RS.id_rank5 IN (' || _pr_list || ') OR RS.id_rank6 IN (' || _pr_list || ') OR RS.id_rank7 IN (' || _pr_list || ') OR RS.id_rank8 IN (' || _pr_list || ') OR RS.id_rank9 IN (' || _pr_list || ') OR RS.id_rank10 IN (' || _pr_list || ')))))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RS.id_rank1 IN (' || _tm_list || ') OR RS.id_rank2 IN (' || _tm_list || ') OR RS.id_rank3 IN (' || _tm_list || ') OR RS.id_rank4 IN (' || _tm_list || ') OR RS.id_rank5 IN (' || _tm_list || ') OR RS.id_rank6 IN (' || _tm_list || ') OR RS.id_rank7 IN (' || _tm_list || ') OR RS.id_rank8 IN (' || _tm_list || ') OR RS.id_rank9 IN (' || _tm_list || ') OR RS.id_rank10 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND  (RS.id_city1 IN (' || _ct_list || ') OR RS.id_city2 IN (' || _ct_list || '))';
		ELSIF _entity = 'CX' THEN
			_query := _query || ' AND  (RS.id_complex1 IN (' || _cx_list || ') OR RS.id_complex2 IN (' || _cx_list || '))';
		ELSIF _entity = 'OL' THEN
			_query := _query || ' AND RS.id_championship=1 AND OL.id = ' || _id;
		ELSIF _entity = 'YR' THEN
			_query := _query || ' AND RS.id_year = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2, _type3, _item.count1;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF _type1 <= 10 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, CN3.code, TM3.label, PR4.last_name || (CASE WHEN length(PR4.first_name) > 0 THEN ', ' || PR4.first_name ELSE '' END), (CASE WHEN length(PR4.first_name) > 0 THEN PR4.first_name || ' ' ELSE '' END) || PR4.last_name, CN4.id, CN4.code, TM4.label, PR5.last_name || (CASE WHEN length(PR5.first_name) > 0 THEN ', ' || PR5.first_name ELSE '' END), (CASE WHEN length(PR5.first_name) > 0 THEN PR5.first_name || ' ' ELSE '' END) || PR5.last_name, CN5.id, CN5.code, TM5.label, PR6.last_name || (CASE WHEN length(PR6.first_name) > 0 THEN ', ' || PR6.first_name ELSE '' END), (CASE WHEN length(PR6.first_name) > 0 THEN PR6.first_name || ' ' ELSE '' END) || PR6.last_name, CN6.id, CN6.code, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.label_rel23, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.label_rel24, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.label_rel25, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt3, _item.txt4
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
				IF _type1 = 4 OR _item.txt3 = '#DOUBLE#' THEN
					_item.txt4 = '1-2/3-4/5-6';
				ELSIF _type1 = 5 OR _item.txt3 = '#TRIPLE#' THEN
					_item.txt4 = '1-3/4-6/7-9';
				END IF;
				_item.comment := 'PR';
				_array_id := string_to_array(_pr_list, ',')::integer[];
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, NULL, NULL, NULL, NULL, NULL, NULL, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "TEAM" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "TEAM" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "TEAM" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "TEAM" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "TEAM" TM5 ON RS.id_rank5 = TM5.id LEFT JOIN "TEAM" TM6 ON RS.id_rank6 = TM6.id
				WHERE RS.id = _item.id_item;
				_item.comment := 'TM';
				_array_id := string_to_array(_tm_list, ',')::integer[];
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN4.label' || _lang || ', CN5.label' || _lang || ', CN6.label' || _lang || ', CN1.label, CN2.label, CN3.label, CN4.label, CN5.label, CN6.label, RS.result1, RS.result2, RS.comment, RS.exa';
				_query := _query || ' FROM "RESULT" RS LEFT JOIN "COUNTRY" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "COUNTRY" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "COUNTRY" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "COUNTRY" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "COUNTRY" CN5 ON RS.id_rank5 = CN5.id LEFT JOIN "COUNTRY" CN6 ON RS.id_rank6 = CN6.id';
				_query := _query || ' WHERE RS.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4;
				CLOSE __c;
				_item.comment := 'CN';
				_array_id := ARRAY[_id];
			END IF;
			IF _item.id_rel6 = ANY(_array_id) OR (_item.id_rel7 = ANY(_array_id) AND (_type1 IN (4,5) OR _item.txt3 IN ('#DOUBLE#', '#TRIPLE#') OR _item.txt4 ~ '(^|/)1.*(/|$)')) OR (_item.id_rel8 = ANY(_array_id) AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)1-(3|4|5|6|7|8)(/|$)')) OR (_item.id_rel9 = ANY(_array_id) AND _item.txt4 ~ '(^|/)1-(4|5|6|7|8)(/|$)') OR (_item.id_rel10 = ANY(_array_id) AND _item.txt4 ~ '(^|/)1-(5|6|7|8)(/|$)') OR (_item.id_rel11 = ANY(_array_id) AND _item.txt4 ~ '(^|/)1-(6|7|8)(/|$)') THEN
				_item.count1 := 1;
			ELSIF _item.id_rel7 = ANY(_array_id) OR (_item.id_rel8 = ANY(_array_id) AND (_type1 = 4 OR _item.txt3 = '#DOUBLE#' OR _item.txt4 ~ '(^|/)2.*(/|$)')) OR (_item.id_rel9 = ANY(_array_id) AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)2-(4|5|6|7|8).*(/|$)')) OR (_item.id_rel10 = ANY(_array_id) AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)2-(5|6|7|8).*(/|$)')) OR (_item.id_rel11 = ANY(_array_id) AND _item.txt4 ~ '(^|/)2-(6|7|8).*(/|$)') THEN
				_item.count1 := 2;
			ELSIF _item.id_rel8 = ANY(_array_id)/* OR (_item.id_rel9 = ANY(_array_id) AND (_type1 = 4 OR _item.txt3 = '#DOUBLE#' OR _item.txt4 ~ '(^|/)2.*(/|$)')) OR (_item.id_rel9 = ANY(_array_id) AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)2-(4|5|6|7|8).*(/|$)')) OR (_item.id_rel10 = ANY(_array_id) AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)2-(5|6|7|8).*(/|$)')) OR (_item.id_rel11 = ANY(_array_id) AND _item.txt4 ~ '(^|/)2-(6|7|8).*(/|$)') */THEN
				_item.count1 := 3;
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
		_query := 'SELECT DISTINCT ON (PR.last_name, PR.first_name, CN.id, SP.id) PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "PERSON" PR';
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
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT TM.id, TM.label, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "TEAM" TM';
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
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT CT.id, CT.label' || _lang || ', CT.label, CN.id, CN.label' || _lang || ', CN.label FROM "CITY" CT';
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
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.label_rel2;
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
		_query := 'SELECT CX.id, CX.label' || _lang || ', CX.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label FROM "COMPLEX" CX';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' WHERE CX.id_city = ' || _id;
		_query := _query || ' ORDER BY CX.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OL.type FROM "OLYMPICS" OL';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query := _query || ' WHERE OL.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY OL.type, YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT OR_.id, OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OR_.count_gold || '','' || OR_.count_silver || '','' || OR_.count_bronze FROM "OLYMPIC_RANKING" OR_';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT RC.id, RC.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, RC.type1, RC.type2, RC.record1, RC.id_rank1, RC.id_rank2, RC.id_rank3, RC.id_rank4, RC.id_rank5 FROM "RECORD" RC';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RC.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RC.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RC.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "TYPE" TP ON EV.id_type = TP.id';
		_query := _query || ' WHERE TP.number BETWEEN ' || _type1 || ' AND ' || _type2;
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RC.id_rank1 = ' || _id || ' OR RC.id_rank2 = ' || _id || ' OR RC.id_rank3 = ' || _id || ' OR RC.id_rank4 = ' || _id || ' OR RC.id_rank5 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _pr_list || ') OR RC.id_rank2 IN (' || _pr_list || ') OR RC.id_rank3 IN (' || _pr_list || ') OR RC.id_rank4 IN (' || _pr_list || ') OR RC.id_rank5 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _tm_list || ') OR RC.id_rank2 IN (' || _tm_list || ') OR RC.id_rank3 IN (' || _tm_list || ') OR RC.id_rank4 IN (' || _tm_list || ') OR RC.id_rank5 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RC.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RC.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RC.id_event = ' || _id || ' OR RC.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND RC.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', RC.index LIMIT ' || _limit || ' OFFSET ' || _offset;
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
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Hall of Fame]
	IF (_entity ~ 'YR|PR' AND (_entity_ref = 'HF' OR _entity_ref = '')) THEN
		_query := 'SELECT HF.id, YR.id, YR.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, HF.position FROM "HALL_OF_FAME" HF';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.txt1;
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
		_query := 'SELECT RN.id, TM.id, TM.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, RN.number FROM "RETIRED_NUMBER" RN';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.id_rel4;
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
		_query := 'SELECT TS.id, TM.id, TM.label, CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', CX.label, CT.label, ST.label, CN.label, LG.id, LG.label, TS.date1, TS.date2 FROM "TEAM_STADIUM" TS';
		_query := _query || ' LEFT JOIN "TEAM" TM ON TS.id_team = TM.id';
		_query := _query || ' LEFT JOIN "COMPLEX" CX ON TS.id_complex = CX.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON TS.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE TS.id_team = ' || _id;
		ELSIF _entity = 'CX' THEN
			_query := _query || ' WHERE TS.id_complex IN (' || _cx_list || ')';
		END IF;
		_query := _query || ' ORDER BY TM.label, TS.date1 DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.id_rel6, _item.comment, _item.txt1, _item.txt2;
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
		_query := 'SELECT WL.id, TM.id, TM.label, LG.id, LG.label, WL.type, WL.count_win || '','' || WL.count_loss FROM "WIN_LOSS" WL';
		_query := _query || ' LEFT JOIN "TEAM" TM ON WL.id_team = TM.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON WL.id_league = LG.id';
		_query := _query || ' WHERE WL.id_team = ' || _id;
		_query := _query || ' ORDER BY TM.label DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'WL';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Contributions]
	IF (_entity ~ 'CB' AND (_entity_ref = 'CO' OR _entity_ref = '')) THEN
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, CO.type, CO.date';
		_query := _query || ' FROM "~CONTRIBUTION" CO';
		_query := _query || ' LEFT JOIN "RESULT" RS ON CO.id_item = RS.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' WHERE RS.id_member=' || _id || ' ORDER BY CO.date DESC, YR.id DESC, SP.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.date1;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CO';
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
  
  
  
  CREATE OR REPLACE FUNCTION "ENTITY_REF"(_entity character varying, _id integer, _entity_ref character varying, _limit character varying, _offset integer, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_c refcursor;
	__c refcursor;
	_query text;
	_link integer;
	_ct_list varchar(200);
	_cx_list varchar(200);
	_pr_list varchar(200);
	_tm_list varchar(200);
	_index integer;
	_type1 integer;
	_type2 integer;
	_type3 integer;
	_array_id integer[];
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
	_id11 integer;_id12 integer;_id13 integer;_id14 integer;
	_cn1 varchar(5);_cn2 varchar(5);_cn3 varchar(5);_cn4 varchar(5);_cn5 varchar(5);_cn6 varchar(5);
	_tm1 varchar(60);_tm2 varchar(60);_tm3 varchar(60);_tm4 varchar(60);_tm5 varchar(60);_tm6 varchar(60);
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'IF', _entity || '-' || _id, current_date);
	
	_index := 1;

	IF _entity ~ 'CT' THEN
		SELECT LINK INTO _link FROM "CITY" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "CITY" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_ct_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_ct_list := _ct_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_ct_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'CX' THEN
		SELECT LINK INTO _link FROM "COMPLEX" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "COMPLEX" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_cx_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_cx_list := _cx_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_cx_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'PR' THEN
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
	ELSIF _entity ~ 'TM' THEN
		SELECT LINK INTO _link FROM "TEAM" WHERE ID = _id AND (YEAR1 IS NULL OR YEAR1 = '');
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "TEAM" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_tm_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_tm_list := _tm_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_tm_list := cast(_id AS varchar);
		END IF;
	END IF;

	-- References in: [Draws] (MUST STAY AT FIRST)
	IF (_entity ~ 'CN|PR|TM' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT DR.id, DR.id_result, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, DR.id1_qf1, DR.id2_qf1, DR.id1_qf2, DR.id2_qf2, DR.id1_qf3, DR.id2_qf3, DR.id1_qf4, DR.id2_qf4, DR.id1_sf1, DR.id2_sf1, DR.id1_sf2, DR.id2_sf2, DR.id1_thd, DR.id2_thd, TP1.number, TP2.number, TP3.number FROM "DRAW" DR';
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
		IF _entity = 'CN' THEN
			_query := _query || ' AND (DR.id1_sf1 <> ' || _id || ' AND DR.id1_sf2 <> ' || _id || ') AND (DR.id1_qf1 = ' || _id || ' OR DR.id2_qf1 = ' || _id || ' OR DR.id1_qf2 = ' || _id || ' OR DR.id2_qf2 = ' || _id || ' OR DR.id1_qf3 = ' || _id || ' OR DR.id2_qf3 = ' || _id || ' OR DR.id1_qf4 = ' || _id || ' OR DR.id2_qf4 = ' || _id || ' OR DR.id2_sf1 = ' || _id || ' OR DR.id2_sf2 = ' || _id || ' OR DR.id1_thd = ' || _id || ' OR DR.id2_thd = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _pr_list || ') AND DR.id1_sf2 NOT IN (' || _pr_list || ')) AND (DR.id1_qf1 IN (' || _pr_list || ') OR DR.id2_qf1 IN (' || _pr_list || ') OR DR.id1_qf2 IN (' || _pr_list || ') OR DR.id2_qf2 IN (' || _pr_list || ') OR DR.id1_qf3 IN (' || _pr_list || ') OR DR.id2_qf3 IN (' || _pr_list || ') OR DR.id1_qf4 IN (' || _pr_list || ') OR DR.id2_qf4 IN (' || _pr_list || ') OR DR.id2_sf1 IN (' || _pr_list || ') OR DR.id2_sf2 IN (' || _pr_list || ') OR DR.id1_thd IN (' || _pr_list || ') OR DR.id2_thd IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _tm_list || ') AND DR.id1_sf2 NOT IN (' || _tm_list || ')) AND (DR.id1_qf1 IN (' || _tm_list || ') OR DR.id2_qf1 IN (' || _tm_list || ') OR DR.id1_qf2 IN (' || _tm_list || ') OR DR.id2_qf2 IN (' || _tm_list || ') OR DR.id1_qf3 IN (' || _tm_list || ') OR DR.id2_qf3 IN (' || _tm_list || ') OR DR.id1_qf4 IN (' || _tm_list || ') OR DR.id2_qf4 IN (' || _tm_list || ') OR DR.id2_sf1 IN (' || _tm_list || ') OR DR.id2_sf2 IN (' || _tm_list || ') OR DR.id1_thd IN (' || _tm_list || ') OR DR.id2_thd IN (' || _tm_list || '))';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel10, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _id11, _id12, _id13, _id14, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF (_id13 = _id OR _id14 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)'))) THEN
				_item.txt2 := 'thd';
			ELSIF (_id9 = _id OR _id10 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)'))) THEN
				_item.txt2 := 'sf1';
			ELSIF (_id11 = _id OR _id12 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)'))) THEN
				_item.txt2 := 'sf2';
			ELSIF (_id1 = _id OR _id2 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)'))) THEN
				_item.txt2 := 'qf1';
			ELSIF (_id3 = _id OR _id4 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)'))) THEN
				_item.txt2 := 'qf2';
			ELSIF (_id5 = _id OR _id6 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)'))) THEN
				_item.txt2 := 'qf3';
			ELSIF (_id7 = _id OR _id8 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)'))) THEN
				_item.txt2 := 'qf4';
			END IF;
			IF _type1 <= 10 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN '', '' || PR1.first_name ELSE '''' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || '' '' ELSE '''' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN '', '' || PR2.first_name ELSE '''' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || '' '' ELSE '''' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, DR.result_' || _item.txt2;
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "PERSON" PR1 ON DR.id1_' || _item.txt2 || ' = PR1.id LEFT JOIN "PERSON" PR2 ON DR.id2_' || _item.txt2 || ' = PR2.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel8, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel9, _item.id_rel13, _cn2, _tm2, _item.txt1;
				CLOSE __c;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				_item.comment := 'PR';
			ELSIF _type1 = 50 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', TM1.label, TM2.label, NULL, NULL, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "TEAM" TM1 ON DR.id1_' || _item.txt2 || ' = TM1.id LEFT JOIN "TEAM" TM2 ON DR.id2_' || _item.txt2 || ' = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'TM';
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', CN1.label' || _lang || ', CN2.label' || _lang || ', CN1.label, CN2.label, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "COUNTRY" CN1 ON DR.id1_' || _item.txt2 || ' = CN1.id LEFT JOIN "COUNTRY" CN2 ON DR.id2_' || _item.txt2 || ' = CN2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'CN';
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
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number, TP3.number';
		IF (_entity = 'PR') THEN
			_query := _query || ', PL.rank';
		ELSE
			_query := _query || ', 0';
		END IF;
		_query := _query || ' FROM "RESULT" RS';
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
		ELSIF (_entity = 'PR') THEN
			_type1 = 1;
			_type2 = 99;
			_query := _query || ' LEFT JOIN "~PERSON_LIST" PL ON PL.id_result = RS.id';
		END IF;
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RS.id_rank1 = ' || _id || ' OR RS.id_rank2 = ' || _id || ' OR RS.id_rank3 = ' || _id || ' OR RS.id_rank4 = ' || _id || ' OR RS.id_rank5 = ' || _id || ' OR RS.id_rank6 = ' || _id || ' OR RS.id_rank7 = ' || _id || ' OR RS.id_rank8 = ' || _id || ' OR RS.id_rank9 = ' || _id || ' OR RS.id_rank10 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (PL.id_person IN (' || _pr_list || ') OR (((TP1.number BETWEEN 1 AND 10 AND TP2.number IS NULL) OR (TP2.number BETWEEN 1 AND 10 AND TP3.number IS NULL) OR (TP3.number BETWEEN 1 AND 10)) AND ((RS.id_rank1 IN (' || _pr_list || ') OR RS.id_rank2 IN (' || _pr_list || ') OR RS.id_rank3 IN (' || _pr_list || ') OR RS.id_rank4 IN (' || _pr_list || ') OR RS.id_rank5 IN (' || _pr_list || ') OR RS.id_rank6 IN (' || _pr_list || ') OR RS.id_rank7 IN (' || _pr_list || ') OR RS.id_rank8 IN (' || _pr_list || ') OR RS.id_rank9 IN (' || _pr_list || ') OR RS.id_rank10 IN (' || _pr_list || ')))))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RS.id_rank1 IN (' || _tm_list || ') OR RS.id_rank2 IN (' || _tm_list || ') OR RS.id_rank3 IN (' || _tm_list || ') OR RS.id_rank4 IN (' || _tm_list || ') OR RS.id_rank5 IN (' || _tm_list || ') OR RS.id_rank6 IN (' || _tm_list || ') OR RS.id_rank7 IN (' || _tm_list || ') OR RS.id_rank8 IN (' || _tm_list || ') OR RS.id_rank9 IN (' || _tm_list || ') OR RS.id_rank10 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND  (RS.id_city1 IN (' || _ct_list || ') OR RS.id_city2 IN (' || _ct_list || '))';
		ELSIF _entity = 'CX' THEN
			_query := _query || ' AND  (RS.id_complex1 IN (' || _cx_list || ') OR RS.id_complex2 IN (' || _cx_list || '))';
		ELSIF _entity = 'OL' THEN
			_query := _query || ' AND RS.id_championship=1 AND OL.id = ' || _id;
		ELSIF _entity = 'YR' THEN
			_query := _query || ' AND RS.id_year = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2, _type3, _item.count1;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF _type1 <= 10 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, CN3.code, TM3.label, PR4.last_name || (CASE WHEN length(PR4.first_name) > 0 THEN ', ' || PR4.first_name ELSE '' END), (CASE WHEN length(PR4.first_name) > 0 THEN PR4.first_name || ' ' ELSE '' END) || PR4.last_name, CN4.id, CN4.code, TM4.label, PR5.last_name || (CASE WHEN length(PR5.first_name) > 0 THEN ', ' || PR5.first_name ELSE '' END), (CASE WHEN length(PR5.first_name) > 0 THEN PR5.first_name || ' ' ELSE '' END) || PR5.last_name, CN5.id, CN5.code, TM5.label, PR6.last_name || (CASE WHEN length(PR6.first_name) > 0 THEN ', ' || PR6.first_name ELSE '' END), (CASE WHEN length(PR6.first_name) > 0 THEN PR6.first_name || ' ' ELSE '' END) || PR6.last_name, CN6.id, CN6.code, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.label_rel23, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.label_rel24, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.label_rel25, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt3, _item.txt4
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
				IF _type1 = 4 OR _item.txt3 = '#DOUBLE#' THEN
					_item.txt4 = '1-2/3-4/5-6';
				ELSIF _type1 = 5 OR _item.txt3 = '#TRIPLE#' THEN
					_item.txt4 = '1-3/4-6/7-9';
				END IF;
				_item.comment := 'PR';
				_array_id := string_to_array(_pr_list, ',')::integer[];
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, NULL, NULL, NULL, NULL, NULL, NULL, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "TEAM" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "TEAM" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "TEAM" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "TEAM" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "TEAM" TM5 ON RS.id_rank5 = TM5.id LEFT JOIN "TEAM" TM6 ON RS.id_rank6 = TM6.id
				WHERE RS.id = _item.id_item;
				_item.comment := 'TM';
				_array_id := string_to_array(_tm_list, ',')::integer[];
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN4.label' || _lang || ', CN5.label' || _lang || ', CN6.label' || _lang || ', CN1.label, CN2.label, CN3.label, CN4.label, CN5.label, CN6.label, RS.result1, RS.result2, RS.comment, RS.exa';
				_query := _query || ' FROM "RESULT" RS LEFT JOIN "COUNTRY" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "COUNTRY" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "COUNTRY" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "COUNTRY" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "COUNTRY" CN5 ON RS.id_rank5 = CN5.id LEFT JOIN "COUNTRY" CN6 ON RS.id_rank6 = CN6.id';
				_query := _query || ' WHERE RS.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4;
				CLOSE __c;
				_item.comment := 'CN';
				_array_id := ARRAY[_id];
			END IF;
			IF _item.id_rel6 = ANY(_array_id) OR (_item.id_rel7 = ANY(_array_id) AND (_type1 IN (4,5) OR _item.txt3 IN ('#DOUBLE#', '#TRIPLE#') OR _item.txt4 ~ '(^|/)1.*(/|$)')) OR (_item.id_rel8 = ANY(_array_id) AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)1-(3|4|5|6|7|8)(/|$)')) OR (_item.id_rel9 = ANY(_array_id) AND _item.txt4 ~ '(^|/)1-(4|5|6|7|8)(/|$)') OR (_item.id_rel10 = ANY(_array_id) AND _item.txt4 ~ '(^|/)1-(5|6|7|8)(/|$)') OR (_item.id_rel11 = ANY(_array_id) AND _item.txt4 ~ '(^|/)1-(6|7|8)(/|$)') THEN
				_item.count1 := 1;
			ELSIF _item.id_rel7 = ANY(_array_id) OR (_item.id_rel8 = ANY(_array_id) AND (_type1 = 4 OR _item.txt3 = '#DOUBLE#' OR _item.txt4 ~ '(^|/)2.*(/|$)')) OR (_item.id_rel9 = ANY(_array_id) AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)2-(4|5|6|7|8).*(/|$)')) OR (_item.id_rel10 = ANY(_array_id) AND (_type1 = 5 OR _item.txt3 = '#TRIPLE#' OR _item.txt4 ~ '(^|/)2-(5|6|7|8).*(/|$)')) OR (_item.id_rel11 = ANY(_array_id) AND _item.txt4 ~ '(^|/)2-(6|7|8).*(/|$)') THEN
				_item.count1 := 2;
			ELSIF _item.id_rel8 = ANY(_array_id) OR (_item.id_rel9 = ANY(_array_id) AND (_item.txt4 ~ '(^|/)3.*(/|$)')) OR (_item.id_rel10 = ANY(_array_id) AND (_type1 = 4 OR _item.txt3 = '#DOUBLE#' OR _item.txt4 ~ '(^|/)3-(5|6|7|8).*(/|$)')) OR (_item.id_rel11 = ANY(_array_id) AND (_type1 = 4 OR _item.txt3 = '#DOUBLE#' OR _item.txt4 ~ '(^|/)3-(6|7|8).*(/|$)')) THEN
				_item.count1 := 3;
			ELSE
				_item.count1 := 0;
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
		_query := 'SELECT DISTINCT ON (PR.last_name, PR.first_name, CN.id, SP.id) PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "PERSON" PR';
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
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT TM.id, TM.label, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "TEAM" TM';
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
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT CT.id, CT.label' || _lang || ', CT.label, CN.id, CN.label' || _lang || ', CN.label FROM "CITY" CT';
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
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.label_rel2;
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
		_query := 'SELECT CX.id, CX.label' || _lang || ', CX.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label FROM "COMPLEX" CX';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' WHERE CX.id_city = ' || _id;
		_query := _query || ' ORDER BY CX.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OL.type FROM "OLYMPICS" OL';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query := _query || ' WHERE OL.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY OL.type, YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT OR_.id, OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OR_.count_gold || '','' || OR_.count_silver || '','' || OR_.count_bronze FROM "OLYMPIC_RANKING" OR_';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT RC.id, RC.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, RC.type1, RC.type2, RC.record1, RC.id_rank1, RC.id_rank2, RC.id_rank3, RC.id_rank4, RC.id_rank5 FROM "RECORD" RC';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RC.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RC.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RC.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "TYPE" TP ON EV.id_type = TP.id';
		_query := _query || ' WHERE TP.number BETWEEN ' || _type1 || ' AND ' || _type2;
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RC.id_rank1 = ' || _id || ' OR RC.id_rank2 = ' || _id || ' OR RC.id_rank3 = ' || _id || ' OR RC.id_rank4 = ' || _id || ' OR RC.id_rank5 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _pr_list || ') OR RC.id_rank2 IN (' || _pr_list || ') OR RC.id_rank3 IN (' || _pr_list || ') OR RC.id_rank4 IN (' || _pr_list || ') OR RC.id_rank5 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _tm_list || ') OR RC.id_rank2 IN (' || _tm_list || ') OR RC.id_rank3 IN (' || _tm_list || ') OR RC.id_rank4 IN (' || _tm_list || ') OR RC.id_rank5 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RC.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RC.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RC.id_event = ' || _id || ' OR RC.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND RC.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', RC.index LIMIT ' || _limit || ' OFFSET ' || _offset;
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
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Hall of Fame]
	IF (_entity ~ 'YR|PR' AND (_entity_ref = 'HF' OR _entity_ref = '')) THEN
		_query := 'SELECT HF.id, YR.id, YR.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, HF.position FROM "HALL_OF_FAME" HF';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.txt1;
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
		_query := 'SELECT RN.id, TM.id, TM.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, RN.number FROM "RETIRED_NUMBER" RN';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.id_rel4;
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
		_query := 'SELECT TS.id, TM.id, TM.label, CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', CX.label, CT.label, ST.label, CN.label, LG.id, LG.label, TS.date1, TS.date2 FROM "TEAM_STADIUM" TS';
		_query := _query || ' LEFT JOIN "TEAM" TM ON TS.id_team = TM.id';
		_query := _query || ' LEFT JOIN "COMPLEX" CX ON TS.id_complex = CX.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON TS.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE TS.id_team = ' || _id;
		ELSIF _entity = 'CX' THEN
			_query := _query || ' WHERE TS.id_complex IN (' || _cx_list || ')';
		END IF;
		_query := _query || ' ORDER BY TM.label, TS.date1 DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.id_rel6, _item.comment, _item.txt1, _item.txt2;
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
		_query := 'SELECT WL.id, TM.id, TM.label, LG.id, LG.label, WL.type, WL.count_win || '','' || WL.count_loss FROM "WIN_LOSS" WL';
		_query := _query || ' LEFT JOIN "TEAM" TM ON WL.id_team = TM.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON WL.id_league = LG.id';
		_query := _query || ' WHERE WL.id_team = ' || _id;
		_query := _query || ' ORDER BY TM.label DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'WL';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Contributions]
	IF (_entity ~ 'CB' AND (_entity_ref = 'CO' OR _entity_ref = '')) THEN
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, CO.type, CO.date';
		_query := _query || ' FROM "~CONTRIBUTION" CO';
		_query := _query || ' LEFT JOIN "RESULT" RS ON CO.id_item = RS.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' WHERE RS.id_member=' || _id || ' ORDER BY CO.date DESC, YR.id DESC, SP.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.date1;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CO';
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
  
  
  
  CREATE OR REPLACE FUNCTION "GET_MEDAL_COUNT"(_entity character varying, _id_sport integer, _idlist character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_ids integer[];
	_type1 integer;
	_type2 integer;
	_sport_txt varchar(3);
	_index smallint;
	_nfl_cp1 smallint;
	_nfl_cp2 smallint;
	_nba_cp smallint;
	_nhl_cp smallint;
	_mlb_cp smallint;
	_count1 integer;
	_count2 integer;
	_count3 integer;
	_label_tennis_slam1 varchar(45);
	_label_tennis_slam2 varchar(45);
	_label_tennis_slam3 varchar(45);
	_label_tennis_slam4 varchar(45);
	_label_golf_slam1 varchar(45);
	_label_golf_slam2 varchar(45);
	_label_golf_slam3 varchar(45);
	_label_golf_slam4 varchar(45);
	_label_cycling_tour1 varchar(45);
	_label_cycling_tour2 varchar(45);
	_label_cycling_tour3 varchar(45);
begin
	_ids := regexp_split_to_array(_idlist, E'\\-');	
	_label_tennis_slam1 := 'Australian Open';
	_label_tennis_slam2 := 'French Open';
	_label_tennis_slam3 := 'Wimbledon';
	_label_tennis_slam4 := 'US Open';
	_label_golf_slam1 := 'Masters';
	_label_golf_slam2 := 'US Open';
	_label_golf_slam3 := 'Open Championship';
	_label_golf_slam4 := 'PGA Championship';
	_label_cycling_tour1 := 'Giro d''Italia';
	_label_cycling_tour2 := 'Tour de France';
	_label_cycling_tour3 := 'Vuelta a España';
	_nfl_cp1 := 454;
	_nfl_cp2 := 453;
	_nba_cp := 530;
	_nhl_cp := 573;
	_mlb_cp := 624;
	_index := 1;
	_sport_txt := CAST(_id_sport AS varchar);
	IF (_entity = 'PR') THEN
		_type1 := 1;
		_type2 := 10;
	ELSIF (_entity = 'TM') THEN
		_type1 := 50;
		_type2 := 50;
	ELSIF (_entity = 'CN') THEN
		_type1 := 99;
		_type2 := 99;
	END IF;
	-- Olympics
	IF _sport_txt !~ '5|18|20|23|29|33|36|37|45' AND _type2 <> 50 THEN
		_item.id := _index;
		_item.label := 'OLYMP';
		_item.txt1 := '#GOLD#';
		_item.txt2 := '#SILVER#';
		_item.txt3 := '#BRONZE#';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, _ids) = 1;
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, _ids) = 2;
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, _ids) = 3;
		IF _type1 = 99 THEN
			SELECT COUNT(*) INTO _count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id LEFT JOIN "PERSON" PR1 ON RS.id_rank1=PR1.id LEFT JOIN "PERSON" PR2 ON RS.id_rank2=PR2.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3=PR3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=10) OR (id_subevent IS NOT NULL AND T2.number<=10) OR (id_subevent IS NULL AND T1.number<=10))  AND (PR1.id_country = ANY(_ids) OR (PR2.id_country = ANY(_ids) AND (exa ~* '.*1-(2|3|4|5|6).*')) OR (PR3.id_country = ANY(_ids) AND (exa ~* '.*1-(3|4|5|6).*')));
			SELECT COUNT(*) INTO _count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id LEFT JOIN "PERSON" PR2 ON RS.id_rank2=PR2.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3=PR3.id LEFT JOIN "PERSON" PR4 ON RS.id_rank4=PR4.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=10) OR (id_subevent IS NOT NULL AND T2.number<=10) OR (id_subevent IS NULL AND T1.number<=10))  AND (PR2.id_country = ANY(_ids) OR (PR3.id_country = ANY(_ids) AND (exa ~* '.*2-(3|4|5|6).*')) OR (PR4.id_country = ANY(_ids) AND (exa ~* '.*2-(4|5|6).*')));
			SELECT COUNT(*) INTO _count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3=PR3.id LEFT JOIN "PERSON" PR4 ON RS.id_rank4=PR4.id LEFT JOIN "PERSON" PR5 ON RS.id_rank5=PR5.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=10) OR (id_subevent IS NOT NULL AND T2.number<=10) OR (id_subevent IS NULL AND T1.number<=10))  AND (PR3.id_country = ANY(_ids) OR (PR4.id_country = ANY(_ids) AND (exa ~* '.*3-(4|5|6).*')) OR (PR5.id_country = ANY(_ids) AND (exa ~* '.*3-(5|6).*')));
			_item.count1 := _item.count1 + _count1;
			_item.count2 := _item.count2 + _count2;
			_item.count3 := _item.count3 + _count3;
		ELSE
			SELECT COUNT(*) INTO _count1 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE id_championship=1 AND PL.rank=1 AND id_person = ANY(_ids);
			SELECT COUNT(*) INTO _count2 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE id_championship=1 AND PL.rank=2 AND id_person = ANY(_ids);
			SELECT COUNT(*) INTO _count3 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE id_championship=1 AND PL.rank=3 AND id_person = ANY(_ids);
			_item.count1 := _item.count1 + _count1;
			_item.count2 := _item.count2 + _count2;
			_item.count3 := _item.count3 + _count3;
		END IF;
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- World Championships
	IF _sport_txt !~ '20|22|23|26|29' AND _type2 <= 10 THEN
		_item.id := _index;	
		_item.label := 'WORLDCP';
		IF _sport_txt ~ '5|18|33|36' THEN
			_item.txt1 := 'rank.1';
			_item.txt2 := 'rank.2';
			_item.txt3 := 'rank.3';
		ELSE
			_item.txt1 := '#GOLD#';
			_item.txt2 := '#SILVER#';
			_item.txt3 := '#BRONZE#';
		END IF;
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, _ids) = 1;
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, _ids) = 2;
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, _ids) = 3;
		SELECT COUNT(*) INTO _count1 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND PL.rank=1 AND id_person = ANY(_ids);
		SELECT COUNT(*) INTO _count2 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND PL.rank=2 AND id_person = ANY(_ids);
		SELECT COUNT(*) INTO _count3 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND PL.rank=3 AND id_person = ANY(_ids);
		_item.count1 := _item.count1 + _count1;
		_item.count2 := _item.count2 + _count2;
		_item.count3 := _item.count3 + _count3;
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Grand Slam (Tennis)
	IF _id_sport = 22 AND _type2 <= 10 THEN
		_item.id := _index;
		_item.label := 'GSLAM';
		_item.txt1 := 'Aus';
		_item.txt2 := 'RG';
		_item.txt3 := 'Wim';
		_item.txt4 := 'US';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam1 OR SE2.label=_label_tennis_slam1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam2 OR SE2.label=_label_tennis_slam2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam3 OR SE2.label=_label_tennis_slam3) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count4 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam4 OR SE2.label=_label_tennis_slam4) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Majors (Golf)
	IF _id_sport = 20 AND _type2 <= 10 THEN
		_item.id := _index;
		_item.label := 'MAJORS';
		_item.txt1 := 'Mas';
		_item.txt2 := 'US';
		_item.txt3 := 'Brit';
		_item.txt4 := 'PGA';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam1 OR SE2.label=_label_golf_slam1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam2 OR SE2.label=_label_golf_slam2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam3 OR SE2.label=_label_golf_slam3) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count4 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam4 OR SE2.label=_label_golf_slam4) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Major Tours (Cycling)
	IF _id_sport = 19 AND _type2 <= 10 THEN
		_item.id := _index;
		_item.label := 'MTOURS';
		_item.txt1 := 'Gir';
		_item.txt2 := 'Tour';
		_item.txt3 := 'Vuel';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (EV.label=_label_cycling_tour1 OR SE.label=_label_cycling_tour1 OR SE2.label=_label_cycling_tour1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (EV.label=_label_cycling_tour2 OR SE.label=_label_cycling_tour2 OR SE2.label=_label_cycling_tour2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (EV.label=_label_cycling_tour3 OR SE.label=_label_cycling_tour3 OR SE2.label=_label_cycling_tour3) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- NFL Titles
	IF _id_sport = 23 AND _type2 = 50 THEN
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
	IF _id_sport = 24 AND _type2 = 50 THEN
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
	IF _id_sport = 25 AND _type2 = 50 THEN
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
	IF _id_sport = 26 AND _type2 = 50 THEN
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
  
  
  
  
  CREATE OR REPLACE FUNCTION "WIN_RECORDS"(_results text, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
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
	_str varchar;
	_type smallint;
	_type2 smallint;
	_type3 smallint;
	_cmt text;
	_exa text;
	_query text;
	_c refcursor;
begin
	SELECT DISTINCT
		TP1.number, TP2.number, TP3.number
	INTO
		_type, _type2, _type3
	FROM
		"RESULT" RS
		LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id
		LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id
		LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id
	WHERE
		RS.id = CAST(CASE WHEN POSITION(',' IN _results) > 0 THEN SUBSTRING(_results FROM 1 FOR POSITION(',' IN _results) - 1) ELSE _results END AS integer);
	IF _type3 IS NOT NULL THEN
		_type := _type3;
	ELSIF _type2 IS NOT NULL THEN
		_type := _type2;
	END IF;

	_str := '';
	
	OPEN _c FOR EXECUTE '
	SELECT RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, RS.exa, RS.comment
	FROM "RESULT" RS
	WHERE RS.id IN (' || _results || ')';
	LOOP	
		FETCH _c INTO _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _exa, _cmt;
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
		ELSIF (_type = 50) THEN
			SELECT TM.link INTO _link1 FROM "TEAM" TM WHERE TM.id = _id1;IF _link1 IS NOT NULL and _link1 > 0 THEN _id1 := _link1; END IF;
			SELECT TM.link INTO _link2 FROM "TEAM" TM WHERE TM.id = _id2;IF _link2 IS NOT NULL and _link2 > 0 THEN _id2 := _link2; END IF;
			SELECT TM.link INTO _link3 FROM "TEAM" TM WHERE TM.id = _id3;IF _link3 IS NOT NULL and _link3 > 0 THEN _id3 := _link3; END IF;
			SELECT TM.link INTO _link4 FROM "TEAM" TM WHERE TM.id = _id4;IF _link4 IS NOT NULL and _link4 > 0 THEN _id4 := _link4; END IF;
			SELECT TM.link INTO _link5 FROM "TEAM" TM WHERE TM.id = _id5;IF _link5 IS NOT NULL and _link5 > 0 THEN _id5 := _link5; END IF;
			SELECT TM.link INTO _link6 FROM "TEAM" TM WHERE TM.id = _id6;IF _link6 IS NOT NULL and _link6 > 0 THEN _id6 := _link6; END IF;
			SELECT TM.link INTO _link7 FROM "TEAM" TM WHERE TM.id = _id7;IF _link7 IS NOT NULL and _link7 > 0 THEN _id7 := _link7; END IF;
			SELECT TM.link INTO _link8 FROM "TEAM" TM WHERE TM.id = _id8;IF _link8 IS NOT NULL and _link8 > 0 THEN _id8 := _link8; END IF;
			SELECT TM.link INTO _link9 FROM "TEAM" TM WHERE TM.id = _id9;IF _link9 IS NOT NULL and _link9 > 0 THEN _id9 := _link9; END IF;
			SELECT TM.link INTO _link10 FROM "TEAM" TM WHERE TM.id = _id10;IF _link10 IS NOT NULL and _link10 > 0 THEN _id10 := _link10; END IF;
		END IF;
		IF (_id1 IS NOT NULL) THEN
			_str := _str || (CASE WHEN _str <> '' THEN ',' ELSE '' END) || _id1;
		END IF;
		IF (_id2 IS NOT NULL AND (_type IN (4, 5) OR _cmt IN ('#DOUBLE#', '#TRIPLE#') OR _exa ~ '(^|/)1.*(/|$)')) THEN
			_str := _str || (CASE WHEN _str <> '' THEN ',' ELSE '' END) || _id2;
		END IF;
		IF (_id3 IS NOT NULL AND (_type = 5 OR _cmt = '#TRIPLE#' OR _exa ~ '(^|/)1-(3|4|5|6|7|8|9)(/|$)')) THEN
			_str := _str || (CASE WHEN _str <> '' THEN ',' ELSE '' END) || _id3;
		END IF;
		IF (_id4 IS NOT NULL AND _exa ~ '(^|/)1-(4|5|6|7|8|9)(/|$)') THEN
			_str := _str || (CASE WHEN _str <> '' THEN ',' ELSE '' END) || _id4;
		END IF;
		IF (_id5 IS NOT NULL AND _exa ~ '(^|/)1-(5|6|7|8|9)(/|$)') THEN
			_str := _str || (CASE WHEN _str <> '' THEN ',' ELSE '' END) || _id5;
		END IF;
		IF (_id6 IS NOT NULL AND _exa ~ '(^|/)1-(6|7|8|9)(/|$)') THEN
			_str := _str || (CASE WHEN _str <> '' THEN ',' ELSE '' END) || _id6;
		END IF;
		IF (_id7 IS NOT NULL AND _exa ~ '(^|/)1-(7|8|9)(/|$)') THEN
			_str := _str || (CASE WHEN _str <> '' THEN ',' ELSE '' END) || _id7;
		END IF;
		IF (_id8 IS NOT NULL AND _exa ~ '(^|/)1-(8|9)(/|$)') THEN
			_str := _str || (CASE WHEN _str <> '' THEN ',' ELSE '' END) || _id8;
		END IF;
		IF (_id9 IS NOT NULL AND _exa ~ '(^|/)1-9(/|$)') THEN
			_str := _str || (CASE WHEN _str <> '' THEN ',' ELSE '' END) || _id9;
		END IF;
	END LOOP;
	CLOSE _c;

	_query := 'SELECT ';
	IF (_type < 10) THEN
		_query := _query || 'PR.id, PR.last_name || '', '' || PR.first_name, PR.last_name || '', '' || PR.first_name, CN.code';
		_query := _query || ' FROM "PERSON" PR LEFT JOIN "COUNTRY" CN ON PR.id_country = CN.id WHERE PR.id IN (' || _str || ')';
	ELSIF (_type = 50) THEN
		_query := _query || 'TM.id, TM.label, TM.label, NULL';
		_query := _query || ' FROM "TEAM" TM WHERE TM.id IN (' || _str || ')';
	ELSIF (_type = 99) THEN
		_query := _query || 'CN.id, CN.label' || _lang || ', CN.label, NULL';
		_query := _query || ' FROM "COUNTRY" CN WHERE CN.id IN (' || _str || ')';
	END IF;
	OPEN _c FOR EXECUTE _query;
	LOOP
		FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.label_rel1;
		EXIT WHEN NOT FOUND;
		SELECT COUNT(*) INTO _item.count1 FROM unnest(string_to_array(_str, ',')) item WHERE item = CAST(_item.id_item AS varchar);
		_item.id := _item.id_item;
		_item.id_rel1 = _type;
		RETURN NEXT _item;
	END LOOP;
	RETURN;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
  
  
  
  
  
  
  CREATE OR REPLACE FUNCTION "ENTITY_REF"(_entity character varying, _id integer, _entity_ref character varying, _limit character varying, _offset integer, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_c refcursor;
	__c refcursor;
	_query text;
	_link integer;
	_rs "RESULT"%rowtype;
	_ct_list varchar(200);
	_cx_list varchar(200);
	_pr_list varchar(200);
	_tm_list varchar(200);
	_index integer;
	_type1 integer;
	_type2 integer;
	_type3 integer;
	_array_id integer[];
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
	_id11 integer;_id12 integer;_id13 integer;_id14 integer;
	_cn1 varchar(5);_cn2 varchar(5);_cn3 varchar(5);_cn4 varchar(5);_cn5 varchar(5);_cn6 varchar(5);
	_tm1 varchar(60);_tm2 varchar(60);_tm3 varchar(60);_tm4 varchar(60);_tm5 varchar(60);_tm6 varchar(60);
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'IF', _entity || '-' || _id, current_date);
	
	_index := 1;

	IF _entity ~ 'CT' THEN
		SELECT LINK INTO _link FROM "CITY" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "CITY" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_ct_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_ct_list := _ct_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_ct_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'CX' THEN
		SELECT LINK INTO _link FROM "COMPLEX" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "COMPLEX" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_cx_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_cx_list := _cx_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_cx_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'PR' THEN
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
	ELSIF _entity ~ 'TM' THEN
		SELECT LINK INTO _link FROM "TEAM" WHERE ID = _id AND (YEAR1 IS NULL OR YEAR1 = '');
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "TEAM" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_tm_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_tm_list := _tm_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_tm_list := cast(_id AS varchar);
		END IF;
	END IF;

	-- References in: [Draws] (MUST STAY AT FIRST)
	IF (_entity ~ 'CN|PR|TM' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT DR.id, DR.id_result, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, DR.id1_qf1, DR.id2_qf1, DR.id1_qf2, DR.id2_qf2, DR.id1_qf3, DR.id2_qf3, DR.id1_qf4, DR.id2_qf4, DR.id1_sf1, DR.id2_sf1, DR.id1_sf2, DR.id2_sf2, DR.id1_thd, DR.id2_thd, TP1.number, TP2.number, TP3.number FROM "DRAW" DR';
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
		IF _entity = 'CN' THEN
			_query := _query || ' AND (DR.id1_sf1 <> ' || _id || ' AND DR.id1_sf2 <> ' || _id || ') AND (DR.id1_qf1 = ' || _id || ' OR DR.id2_qf1 = ' || _id || ' OR DR.id1_qf2 = ' || _id || ' OR DR.id2_qf2 = ' || _id || ' OR DR.id1_qf3 = ' || _id || ' OR DR.id2_qf3 = ' || _id || ' OR DR.id1_qf4 = ' || _id || ' OR DR.id2_qf4 = ' || _id || ' OR DR.id2_sf1 = ' || _id || ' OR DR.id2_sf2 = ' || _id || ' OR DR.id1_thd = ' || _id || ' OR DR.id2_thd = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _pr_list || ') AND DR.id1_sf2 NOT IN (' || _pr_list || ')) AND (DR.id1_qf1 IN (' || _pr_list || ') OR DR.id2_qf1 IN (' || _pr_list || ') OR DR.id1_qf2 IN (' || _pr_list || ') OR DR.id2_qf2 IN (' || _pr_list || ') OR DR.id1_qf3 IN (' || _pr_list || ') OR DR.id2_qf3 IN (' || _pr_list || ') OR DR.id1_qf4 IN (' || _pr_list || ') OR DR.id2_qf4 IN (' || _pr_list || ') OR DR.id2_sf1 IN (' || _pr_list || ') OR DR.id2_sf2 IN (' || _pr_list || ') OR DR.id1_thd IN (' || _pr_list || ') OR DR.id2_thd IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _tm_list || ') AND DR.id1_sf2 NOT IN (' || _tm_list || ')) AND (DR.id1_qf1 IN (' || _tm_list || ') OR DR.id2_qf1 IN (' || _tm_list || ') OR DR.id1_qf2 IN (' || _tm_list || ') OR DR.id2_qf2 IN (' || _tm_list || ') OR DR.id1_qf3 IN (' || _tm_list || ') OR DR.id2_qf3 IN (' || _tm_list || ') OR DR.id1_qf4 IN (' || _tm_list || ') OR DR.id2_qf4 IN (' || _tm_list || ') OR DR.id2_sf1 IN (' || _tm_list || ') OR DR.id2_sf2 IN (' || _tm_list || ') OR DR.id1_thd IN (' || _tm_list || ') OR DR.id2_thd IN (' || _tm_list || '))';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel10, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _id11, _id12, _id13, _id14, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF (_id13 = _id OR _id14 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)'))) THEN
				_item.txt2 := 'thd';
			ELSIF (_id9 = _id OR _id10 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)'))) THEN
				_item.txt2 := 'sf1';
			ELSIF (_id11 = _id OR _id12 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)'))) THEN
				_item.txt2 := 'sf2';
			ELSIF (_id1 = _id OR _id2 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)'))) THEN
				_item.txt2 := 'qf1';
			ELSIF (_id3 = _id OR _id4 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)'))) THEN
				_item.txt2 := 'qf2';
			ELSIF (_id5 = _id OR _id6 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)'))) THEN
				_item.txt2 := 'qf3';
			ELSIF (_id7 = _id OR _id8 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)'))) THEN
				_item.txt2 := 'qf4';
			END IF;
			IF _type1 <= 10 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN '', '' || PR1.first_name ELSE '''' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || '' '' ELSE '''' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN '', '' || PR2.first_name ELSE '''' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || '' '' ELSE '''' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, DR.result_' || _item.txt2;
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "PERSON" PR1 ON DR.id1_' || _item.txt2 || ' = PR1.id LEFT JOIN "PERSON" PR2 ON DR.id2_' || _item.txt2 || ' = PR2.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel8, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel9, _item.id_rel13, _cn2, _tm2, _item.txt1;
				CLOSE __c;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				_item.comment := 'PR';
			ELSIF _type1 = 50 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', TM1.label, TM2.label, NULL, NULL, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "TEAM" TM1 ON DR.id1_' || _item.txt2 || ' = TM1.id LEFT JOIN "TEAM" TM2 ON DR.id2_' || _item.txt2 || ' = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'TM';
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', CN1.label' || _lang || ', CN2.label' || _lang || ', CN1.label, CN2.label, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "COUNTRY" CN1 ON DR.id1_' || _item.txt2 || ' = CN1.id LEFT JOIN "COUNTRY" CN2 ON DR.id2_' || _item.txt2 || ' = CN2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'CN';
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
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number, TP3.number';
		IF (_entity = 'PR') THEN
			_query := _query || ', PL.rank';
		ELSE
			_query := _query || ', 0';
		END IF;
		_query := _query || ' FROM "RESULT" RS';
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
		ELSIF (_entity = 'PR') THEN
			_type1 = 1;
			_type2 = 99;
			_query := _query || ' LEFT JOIN "~PERSON_LIST" PL ON PL.id_result = RS.id';
		END IF;
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RS.id_rank1 = ' || _id || ' OR RS.id_rank2 = ' || _id || ' OR RS.id_rank3 = ' || _id || ' OR RS.id_rank4 = ' || _id || ' OR RS.id_rank5 = ' || _id || ' OR RS.id_rank6 = ' || _id || ' OR RS.id_rank7 = ' || _id || ' OR RS.id_rank8 = ' || _id || ' OR RS.id_rank9 = ' || _id || ' OR RS.id_rank10 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (PL.id_person IN (' || _pr_list || ') OR (((TP1.number BETWEEN 1 AND 10 AND TP2.number IS NULL) OR (TP2.number BETWEEN 1 AND 10 AND TP3.number IS NULL) OR (TP3.number BETWEEN 1 AND 10)) AND ((RS.id_rank1 IN (' || _pr_list || ') OR RS.id_rank2 IN (' || _pr_list || ') OR RS.id_rank3 IN (' || _pr_list || ') OR RS.id_rank4 IN (' || _pr_list || ') OR RS.id_rank5 IN (' || _pr_list || ') OR RS.id_rank6 IN (' || _pr_list || ') OR RS.id_rank7 IN (' || _pr_list || ') OR RS.id_rank8 IN (' || _pr_list || ') OR RS.id_rank9 IN (' || _pr_list || ') OR RS.id_rank10 IN (' || _pr_list || ')))))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RS.id_rank1 IN (' || _tm_list || ') OR RS.id_rank2 IN (' || _tm_list || ') OR RS.id_rank3 IN (' || _tm_list || ') OR RS.id_rank4 IN (' || _tm_list || ') OR RS.id_rank5 IN (' || _tm_list || ') OR RS.id_rank6 IN (' || _tm_list || ') OR RS.id_rank7 IN (' || _tm_list || ') OR RS.id_rank8 IN (' || _tm_list || ') OR RS.id_rank9 IN (' || _tm_list || ') OR RS.id_rank10 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND  (RS.id_city1 IN (' || _ct_list || ') OR RS.id_city2 IN (' || _ct_list || '))';
		ELSIF _entity = 'CX' THEN
			_query := _query || ' AND  (RS.id_complex1 IN (' || _cx_list || ') OR RS.id_complex2 IN (' || _cx_list || '))';
		ELSIF _entity = 'OL' THEN
			_query := _query || ' AND RS.id_championship=1 AND OL.id = ' || _id;
		ELSIF _entity = 'YR' THEN
			_query := _query || ' AND RS.id_year = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2, _type3, _item.count1;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF _type1 <= 10 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, CN3.code, TM3.label, PR4.last_name || (CASE WHEN length(PR4.first_name) > 0 THEN ', ' || PR4.first_name ELSE '' END), (CASE WHEN length(PR4.first_name) > 0 THEN PR4.first_name || ' ' ELSE '' END) || PR4.last_name, CN4.id, CN4.code, TM4.label, PR5.last_name || (CASE WHEN length(PR5.first_name) > 0 THEN ', ' || PR5.first_name ELSE '' END), (CASE WHEN length(PR5.first_name) > 0 THEN PR5.first_name || ' ' ELSE '' END) || PR5.last_name, CN5.id, CN5.code, TM5.label, PR6.last_name || (CASE WHEN length(PR6.first_name) > 0 THEN ', ' || PR6.first_name ELSE '' END), (CASE WHEN length(PR6.first_name) > 0 THEN PR6.first_name || ' ' ELSE '' END) || PR6.last_name, CN6.id, CN6.code, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.label_rel23, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.label_rel24, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.label_rel25, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt3, _item.txt4
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
				IF _type1 = 4 OR _item.txt3 = '#DOUBLE#' THEN
					_item.txt4 = '1-2/3-4/5-6';
				ELSIF _type1 = 5 OR _item.txt3 = '#TRIPLE#' THEN
					_item.txt4 = '1-3/4-6/7-9';
				END IF;
				_item.comment := 'PR';
				_array_id := string_to_array(_pr_list, ',')::integer[];
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, NULL, NULL, NULL, NULL, NULL, NULL, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "TEAM" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "TEAM" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "TEAM" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "TEAM" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "TEAM" TM5 ON RS.id_rank5 = TM5.id LEFT JOIN "TEAM" TM6 ON RS.id_rank6 = TM6.id
				WHERE RS.id = _item.id_item;
				_item.comment := 'TM';
				_array_id := string_to_array(_tm_list, ',')::integer[];
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN4.label' || _lang || ', CN5.label' || _lang || ', CN6.label' || _lang || ', CN1.label, CN2.label, CN3.label, CN4.label, CN5.label, CN6.label, RS.result1, RS.result2, RS.comment, RS.exa';
				_query := _query || ' FROM "RESULT" RS LEFT JOIN "COUNTRY" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "COUNTRY" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "COUNTRY" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "COUNTRY" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "COUNTRY" CN5 ON RS.id_rank5 = CN5.id LEFT JOIN "COUNTRY" CN6 ON RS.id_rank6 = CN6.id';
				_query := _query || ' WHERE RS.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4;
				CLOSE __c;
				_item.comment := 'CN';
				_array_id := ARRAY[_id];
			END IF;
			SELECT * INTO _rs FROM "RESULT" RS WHERE RS.id = _item.id_item;
			SELECT "GetRank"(_rs, _array_id) INTO _item.count1;
			_item.id = _index;
			_item.entity = 'RS';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Athletes]
	IF (_entity ~ 'CN|SP|TM' AND (_entity_ref = 'PR' OR _entity_ref = '')) THEN
		_query := 'SELECT DISTINCT ON (PR.last_name, PR.first_name, CN.id, SP.id) PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "PERSON" PR';
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
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT TM.id, TM.label, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "TEAM" TM';
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
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT CT.id, CT.label' || _lang || ', CT.label, CN.id, CN.label' || _lang || ', CN.label FROM "CITY" CT';
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
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.label_rel2;
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
		_query := 'SELECT CX.id, CX.label' || _lang || ', CX.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label FROM "COMPLEX" CX';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' WHERE CX.id_city = ' || _id;
		_query := _query || ' ORDER BY CX.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OL.type FROM "OLYMPICS" OL';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query := _query || ' WHERE OL.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY OL.type, YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT OR_.id, OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OR_.count_gold || '','' || OR_.count_silver || '','' || OR_.count_bronze FROM "OLYMPIC_RANKING" OR_';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT RC.id, RC.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, RC.type1, RC.type2, RC.record1, RC.id_rank1, RC.id_rank2, RC.id_rank3, RC.id_rank4, RC.id_rank5 FROM "RECORD" RC';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RC.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RC.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RC.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "TYPE" TP ON EV.id_type = TP.id';
		_query := _query || ' WHERE TP.number BETWEEN ' || _type1 || ' AND ' || _type2;
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RC.id_rank1 = ' || _id || ' OR RC.id_rank2 = ' || _id || ' OR RC.id_rank3 = ' || _id || ' OR RC.id_rank4 = ' || _id || ' OR RC.id_rank5 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _pr_list || ') OR RC.id_rank2 IN (' || _pr_list || ') OR RC.id_rank3 IN (' || _pr_list || ') OR RC.id_rank4 IN (' || _pr_list || ') OR RC.id_rank5 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _tm_list || ') OR RC.id_rank2 IN (' || _tm_list || ') OR RC.id_rank3 IN (' || _tm_list || ') OR RC.id_rank4 IN (' || _tm_list || ') OR RC.id_rank5 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RC.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RC.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RC.id_event = ' || _id || ' OR RC.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND RC.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', RC.index LIMIT ' || _limit || ' OFFSET ' || _offset;
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
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Hall of Fame]
	IF (_entity ~ 'YR|PR' AND (_entity_ref = 'HF' OR _entity_ref = '')) THEN
		_query := 'SELECT HF.id, YR.id, YR.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, HF.position FROM "HALL_OF_FAME" HF';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.txt1;
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
		_query := 'SELECT RN.id, TM.id, TM.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, RN.number FROM "RETIRED_NUMBER" RN';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.id_rel4;
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
		_query := 'SELECT TS.id, TM.id, TM.label, CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', CX.label, CT.label, ST.label, CN.label, LG.id, LG.label, TS.date1, TS.date2 FROM "TEAM_STADIUM" TS';
		_query := _query || ' LEFT JOIN "TEAM" TM ON TS.id_team = TM.id';
		_query := _query || ' LEFT JOIN "COMPLEX" CX ON TS.id_complex = CX.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON TS.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE TS.id_team = ' || _id;
		ELSIF _entity = 'CX' THEN
			_query := _query || ' WHERE TS.id_complex IN (' || _cx_list || ')';
		END IF;
		_query := _query || ' ORDER BY TM.label, TS.date1 DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.id_rel6, _item.comment, _item.txt1, _item.txt2;
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
		_query := 'SELECT WL.id, TM.id, TM.label, LG.id, LG.label, WL.type, WL.count_win || '','' || WL.count_loss FROM "WIN_LOSS" WL';
		_query := _query || ' LEFT JOIN "TEAM" TM ON WL.id_team = TM.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON WL.id_league = LG.id';
		_query := _query || ' WHERE WL.id_team = ' || _id;
		_query := _query || ' ORDER BY TM.label DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'WL';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Contributions]
	IF (_entity ~ 'CB' AND (_entity_ref = 'CO' OR _entity_ref = '')) THEN
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, CO.type, CO.date';
		_query := _query || ' FROM "~CONTRIBUTION" CO';
		_query := _query || ' LEFT JOIN "RESULT" RS ON CO.id_item = RS.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' WHERE RS.id_member=' || _id || ' ORDER BY CO.date DESC, YR.id DESC, SP.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.date1;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CO';
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
  
  
  
  
  
  
  ALTER FUNCTION "COUNT_REF"(character varying, integer) RENAME TO "CountRef";
  
  
  CREATE OR REPLACE FUNCTION "UPDATE_REF"()
  RETURNS trigger AS
$BODY$
DECLARE
	_row record;
	_entity varchar(2);
	_id_event integer;
	_id_subevent integer;
	_id_subevent2 integer;
	_id_olympics integer;
	_sp_type integer;
	_type integer;
BEGIN
	_entity := TG_ARGV[0];
	IF (TG_OP = 'DELETE') THEN
		_row := OLD;
	ELSIF (TG_OP IN ('INSERT', 'UPDATE')) THEN
		_row := NEW;
	END IF;

	IF _entity = 'CT' THEN
		UPDATE "STATE" SET REF="CountRef"('ST', _row.id_state) WHERE id=_row.id_state;
		UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id_country) WHERE id=_row.id_country;
	ELSIF _entity = 'CX' THEN
		UPDATE "CITY" SET REF="CountRef"('CT', _row.id_city) WHERE id=_row.id_city;
	ELSIF _entity = 'DR' THEN
		SELECT RS.id_event, RS.id_subevent, RS.id_subevent2 INTO _id_event, _id_subevent, _id_subevent2 FROM "RESULT" RS WHERE RS.id = _row.id_result;

		IF _id_subevent2 IS NOT NULL AND _id_subevent2 <> 0 THEN
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _id_subevent2;
		ELSIF _id_subevent IS NOT NULL AND _id_subevent <> 0 THEN
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _id_subevent;
		ELSE
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _id_event;
		END IF;

		IF _type < 10 THEN
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id1_qf1) WHERE id=_row.id1_qf1;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id2_qf1) WHERE id=_row.id2_qf1;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id1_qf2) WHERE id=_row.id1_qf2;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id2_qf2) WHERE id=_row.id2_qf2;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id1_qf3) WHERE id=_row.id1_qf3;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id2_qf3) WHERE id=_row.id2_qf3;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id1_qf4) WHERE id=_row.id1_qf4;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id2_qf4) WHERE id=_row.id2_qf4;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id1_sf1) WHERE id=_row.id1_sf1;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id2_sf1) WHERE id=_row.id2_sf1;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id1_sf2) WHERE id=_row.id1_sf2;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id2_sf2) WHERE id=_row.id2_sf2;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id1_thd) WHERE id=_row.id1_thd;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id2_thd) WHERE id=_row.id2_thd;
		ELSIF _type = 50 THEN
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id1_qf1) WHERE id=_row.id1_qf1;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id2_qf1) WHERE id=_row.id2_qf1;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id1_qf2) WHERE id=_row.id1_qf2;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id2_qf2) WHERE id=_row.id2_qf2;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id1_qf3) WHERE id=_row.id1_qf3;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id2_qf3) WHERE id=_row.id2_qf3;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id1_qf4) WHERE id=_row.id1_qf4;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id2_qf4) WHERE id=_row.id2_qf4;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id1_sf1) WHERE id=_row.id1_sf1;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id2_sf1) WHERE id=_row.id2_sf1;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id1_sf2) WHERE id=_row.id1_sf2;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id2_sf2) WHERE id=_row.id2_sf2;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id1_thd) WHERE id=_row.id1_thd;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id2_thd) WHERE id=_row.id2_thd;
		ELSIF _type = 99 THEN
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id1_qf1) WHERE id=_row.id1_qf1;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id2_qf1) WHERE id=_row.id2_qf1;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id1_qf2) WHERE id=_row.id1_qf2;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id2_qf2) WHERE id=_row.id2_qf2;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id1_qf3) WHERE id=_row.id1_qf3;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id2_qf3) WHERE id=_row.id2_qf3;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id1_qf4) WHERE id=_row.id1_qf4;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id2_qf4) WHERE id=_row.id2_qf4;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id1_sf1) WHERE id=_row.id1_sf1;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id2_sf1) WHERE id=_row.id2_sf1;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id1_sf2) WHERE id=_row.id1_sf2;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id2_sf2) WHERE id=_row.id2_sf2;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id1_thd) WHERE id=_row.id1_thd;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id2_thd) WHERE id=_row.id2_thd;
		END IF;
	ELSIF _entity = 'HF' THEN
		UPDATE "YEAR" SET REF="CountRef"('YR', _row.id_year) WHERE id=_row.id_year;
		UPDATE "PERSON" SET REF="CountRef"('PR', _row.id_person) WHERE id=_row.id_person;
	ELSIF _entity = 'OL' THEN
		UPDATE "YEAR" SET REF="CountRef"('YR', _row.id_year) WHERE id=_row.id_year;
		UPDATE "CITY" SET REF="CountRef"('CT', _row.id_city) WHERE id=_row.id_city;
	ELSIF _entity = 'OR' THEN
		UPDATE "OLYMPICS" SET REF="CountRef"('OL', _row.id_olympics) WHERE id=_row.id_olympics;
		UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id_country) WHERE id=_row.id_country;
	ELSIF _entity = 'PL' THEN
		UPDATE "PERSON" SET REF="CountRef"('PR', _row.id_person) WHERE id=_row.id_person;
	ELSIF _entity = 'PR' THEN
		UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id_country) WHERE id=_row.id_country;
		UPDATE "TEAM" SET REF="CountRef"('TM', _row.id_team) WHERE id=_row.id_team;
		UPDATE "SPORT" SET REF="CountRef"('SP', _row.id_sport) WHERE id=_row.id_sport;
	ELSIF _entity = 'RC' THEN
		UPDATE "SPORT" SET REF="CountRef"('SP', _row.id_sport) WHERE id=_row.id_sport;
		UPDATE "CHAMPIONSHIP" SET REF="CountRef"('CP', _row.id_championship) WHERE id=_row.id_championship;
		UPDATE "EVENT" SET REF="CountRef"('EV', _row.id_event) WHERE id=_row.id_event;
		UPDATE "EVENT" SET REF="CountRef"('EV', _row.id_subevent) WHERE id=_row.id_subevent;
		UPDATE "CITY" SET REF="CountRef"('CT', _row.id_city) WHERE id=_row.id_city;

		IF _row.id_subevent IS NOT NULL AND _row.id_subevent <> 0 THEN
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _row.id_subevent;
		ELSE
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _row.id_event;
		END IF;

		IF _type < 10 THEN
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id_rank5) WHERE id=_row.id_rank5;
		ELSIF _type = 50 THEN
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id_rank5) WHERE id=_row.id_rank5;
		ELSIF _type = 99 THEN
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id_rank5) WHERE id=_row.id_rank5;
		END IF;
	ELSIF _entity = 'RS' THEN
		UPDATE "YEAR" SET REF="CountRef"('YR', _row.id_year) WHERE id=_row.id_year;
		UPDATE "SPORT" SET REF="CountRef"('SP', _row.id_sport) WHERE id=_row.id_sport;
		UPDATE "CHAMPIONSHIP" SET REF="CountRef"('CP', _row.id_championship) WHERE id=_row.id_championship;
		UPDATE "EVENT" SET REF="CountRef"('EV', _row.id_event) WHERE id=_row.id_event;
		UPDATE "EVENT" SET REF="CountRef"('EV', _row.id_subevent) WHERE id=_row.id_subevent;
		UPDATE "EVENT" SET REF="CountRef"('EV', _row.id_subevent2) WHERE id=_row.id_subevent2;
		UPDATE "COMPLEX" SET REF="CountRef"('CX', _row.id_complex1) WHERE id=_row.id_complex1;
		UPDATE "COMPLEX" SET REF="CountRef"('CX', _row.id_complex2) WHERE id=_row.id_complex2;
		UPDATE "CITY" SET REF="CountRef"('CT', _row.id_city1) WHERE id=_row.id_city1;
		UPDATE "CITY" SET REF="CountRef"('CT', _row.id_city2) WHERE id=_row.id_city2;
		IF _row.id_championship = 1 THEN
			SELECT SP.type INTO _sp_type FROM "SPORT" SP WHERE SP.id=_row.id_sport;
			SELECT OL.id INTO _id_olympics FROM "OLYMPICS" OL WHERE OL.id_year=_row.id_year AND OL.type=_sp_type;
			UPDATE "OLYMPICS" SET REF="CountRef"('OL', _id_olympics) WHERE id=_id_olympics;
		END IF;
		IF _row.id_subevent2 IS NOT NULL AND _row.id_subevent2 <> 0 THEN
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _row.id_subevent2;
		ELSIF _row.id_subevent IS NOT NULL AND _row.id_subevent <> 0 THEN
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _row.id_subevent;
		ELSE
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _row.id_event;
		END IF;

		IF _type < 10 THEN
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id_rank5) WHERE id=_row.id_rank5;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id_rank6) WHERE id=_row.id_rank6;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id_rank7) WHERE id=_row.id_rank7;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id_rank8) WHERE id=_row.id_rank8;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id_rank9) WHERE id=_row.id_rank9;
			UPDATE "PERSON" SET REF="CountRef"('PR', _row.id_rank10) WHERE id=_row.id_rank10;
		ELSIF _type = 50 THEN
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id_rank5) WHERE id=_row.id_rank5;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id_rank6) WHERE id=_row.id_rank6;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id_rank7) WHERE id=_row.id_rank7;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id_rank8) WHERE id=_row.id_rank8;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id_rank9) WHERE id=_row.id_rank9;
			UPDATE "TEAM" SET REF="CountRef"('TM', _row.id_rank10) WHERE id=_row.id_rank10;
		ELSIF _type = 99 THEN
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id_rank5) WHERE id=_row.id_rank5;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id_rank6) WHERE id=_row.id_rank6;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id_rank7) WHERE id=_row.id_rank7;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id_rank8) WHERE id=_row.id_rank8;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id_rank9) WHERE id=_row.id_rank9;
			UPDATE "COUNTRY" SET REF="CountRef"('CN', _row.id_rank10) WHERE id=_row.id_rank10;
		END IF;
	ELSIF _entity = 'RN' THEN
		UPDATE "TEAM" SET REF="CountRef"('TM', _row.id_team) WHERE id=_row.id_team;
		UPDATE "PERSON" SET REF="CountRef"('PR', _row.id_person) WHERE id=_row.id_person;
	ELSIF _entity = 'TM' THEN
		UPDATE "TEAM" SET REF="CountRef"('TM', _row.link) WHERE id=_row.link;
	ELSIF _entity = 'TS' THEN
		UPDATE "TEAM" SET REF="CountRef"('TM', _row.id_team) WHERE id=_row.id_team;
		UPDATE "COMPLEX" SET REF="CountRef"('CX', _row.id_complex) WHERE id=_row.id_complex;
	ELSIF _entity = 'WL' THEN
		UPDATE "TEAM" SET REF="CountRef"('TM', _row.id_team) WHERE id=_row.id_team;
	END IF;
	
        RETURN NULL;
    END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  
  
    ALTER FUNCTION "ENTITY_REF"(character varying, integer, character varying, character varying, integer, character varying) RENAME TO "EntityRef";
  ALTER FUNCTION "SEARCH"(character varying, character varying, smallint, character varying) RENAME to "Search";
  
  
  
  
  
  
  CREATE OR REPLACE FUNCTION "Search"(_pattern character varying, _scope character varying, _limit smallint, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
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
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang || ' || '' ('' || CN.code || '')'', CN.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s ~ 'PR|TM') THEN -- Relation: Sport
				_rel_cols := _rel_cols || ', SP.id, SP.label' || _lang || ', SP.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "SPORT" SP ON ' || _s || '.id_sport = SP.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'PR') THEN -- Relation: Team
				_rel_cols := _rel_cols || ', TM.id, TM.label, TM.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "TEAM" TM ON ' || _s || '.id_team = TM.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'CX') THEN -- Relation: City/State/Country
				_rel_cols := _rel_cols || ', CT.id, CT.label' || _lang || ', CT.label';
				_rel_cols := _rel_cols || ', ST.id, ST.label' || _lang || ', ST.label';
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang || ', CN.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "CITY" CT ON ' || _s || '.id_city = CT.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			IF (_s = 'CT') THEN -- Relation: State/Country
				_rel_cols := _rel_cols || ', NULL, NULL, NULL';
				_rel_cols := _rel_cols || ', ST.id, ST.label' || _lang || ', ST.label';
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang || ', CN.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "STATE" ST ON ' || _s || '.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			IF (_s ~ 'CT|CX|PR|TM') THEN
				_rel_cols := _rel_cols || ', ' || _s || '.link';
			END IF;
			FOR _j IN 1.._rel_count LOOP
				_rel_cols := _rel_cols || ', NULL, NULL, NULL';
			END LOOP;

			-- Execute query
			_label := 'label';
			_label_en := 'label';
			IF (_s <> 'TM' AND _s <> 'YR') THEN
				_label := 'label' || _lang;
			END IF;
			_query := 'SELECT ' || _s || '.id, ' || _s || '.' || _label || ',' || _s || '.' || _label_en || ',' || _s || '.ref' || _rel_cols || ' FROM "' || _tables[_i] || '" ' || _s;
			_query := _query || _rel_joins || ' WHERE ' || (CASE _s WHEN 'CT' THEN '(CT.link = 0 OR CT.link IS NULL) AND ' WHEN 'CX' THEN '(CX.link = 0 OR CX.link IS NULL) AND ' WHEN 'TM' THEN '(TM.link = 0 OR TM.link IS NULL) AND ' ELSE '' END) || _s || '.' || _label || ' ~* ''' || __pattern || '''' || (CASE _limit WHEN 0 THEN ' ORDER BY ' || _s || '.' || _label ELSE '' END);
			IF _s = 'PR' THEN
				_query := 'SELECT PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, PR.ref' || _rel_cols || ' FROM "PERSON" PR' || _rel_joins;
				_query := _query || ' WHERE (PR.link = 0 OR PR.link IS NULL) AND (PR.last_name || '' '' || PR.first_name ~* ''' || __pattern || ''' OR PR.first_name || '' '' || PR.last_name ~* ''' || __pattern || ''' OR PR.last_name ~* ''' || __pattern || ''' OR PR.first_name ~* ''' || __pattern || ''')';
				_query := _query || (CASE _limit WHEN 0 THEN ' ORDER BY PR.last_name, PR.first_name' ELSE '' END);
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
						SELECT SUM(ref) INTO _item.count_ref FROM "CITY" WHERE id=_current_id OR link=_current_id;
					ELSIF _s = 'CX' THEN
						SELECT SUM(ref) INTO _item.count_ref FROM "COMPLEX" WHERE id=_current_id OR link=_current_id;
					ELSIF _s = 'PR' THEN
						SELECT SUM(ref) INTO _item.count_ref FROM "PERSON" WHERE id=_current_id OR link=_current_id;
					ELSIF _s = 'TM' THEN
						SELECT SUM(ref) INTO _item.count_ref FROM "TEAM" WHERE id=_current_id OR link=_current_id;
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
  
  
  
  
  alter TABLE "~MEMBER" rename to "~Contributor";

alter table "CHAMPIONSHIP" rename id_member to id_contributor;
alter table "CITY" rename id_member to id_contributor;
alter table "COMPLEX" rename id_member to id_contributor;
alter table "COUNTRY" rename id_member to id_contributor;
alter table "DRAW" rename id_member to id_contributor;
alter table "EVENT" rename id_member to id_contributor;
alter table "HALL_OF_FAME" rename id_member to id_contributor;
alter table "LEAGUE" rename id_member to id_contributor;
alter table "OLYMPICS" rename id_member to id_contributor;
alter table "OLYMPIC_RANKING" rename id_member to id_contributor;
alter table "PERSON" rename id_member to id_contributor;
alter table "RECORD" rename id_member to id_contributor;
alter table "RESULT" rename id_member to id_contributor;
alter table "RETIRED_NUMBER" rename id_member to id_contributor;
alter table "SPORT" rename id_member to id_contributor;
alter table "STATE" rename id_member to id_contributor;
alter table "TEAM" rename id_member to id_contributor;
alter table "TEAM_STADIUM" rename id_member to id_contributor;
alter table "TYPE" rename id_member to id_contributor;
alter table "WIN_LOSS" rename id_member to id_contributor;
alter table "YEAR" rename id_member to id_contributor;
alter table "~CONTRIBUTION" rename id_member to id_contributor;



ALTER TABLE "~EXTERNAL_LINK" ADD checked boolean;

UPDATE "~EXTERNAL_LINK" set checked=FALSE;


alter table "SPORT" drop column wiki_pattern;



CREATE OR REPLACE VIEW "~Contributors" AS 
 SELECT CR.id, CR.login, CR.public_name AS name, sum(
        CASE
            WHEN cb.type = 'A'::bpchar THEN 1
            ELSE 0
        END) AS count_a, sum(
        CASE
            WHEN cb.type = 'U'::bpchar THEN 1
            ELSE 0
        END) AS count_u
   FROM "~Contributor" CR
   LEFT JOIN "~CONTRIBUTION" cb ON CR.id = cb.id_contributor
  GROUP BY CR.id, CR.login, CR.public_name
  ORDER BY sum(
   CASE
       WHEN cb.type = 'A'::bpchar THEN 1
       ELSE 0
   END) DESC;
   


DROP FUNCTION "~CONTRIBUTORS"();



CREATE OR REPLACE VIEW "~Statistics" AS
SELECT (SELECT COUNT(*) FROM "CITY") AS COUNT_CITY,
(SELECT COUNT(*) FROM "COMPLEX") AS COUNT_COMPLEX,
(SELECT COUNT(*) FROM "COUNTRY") AS COUNT_COUNTRY,
(SELECT COUNT(*) FROM "EVENT") AS COUNT_EVENT,
(SELECT COUNT(*) FROM "PERSON") AS COUNT_PERSON,
(SELECT COUNT(*) FROM "RESULT") AS COUNT_RESULT,
(SELECT COUNT(*) FROM "SPORT") AS COUNT_SPORT,
(SELECT COUNT(*) FROM "TEAM") AS COUNT_TEAM;


DROP FUNCTION "~STATISTICS"();

alter SEQUENCE "~SQ_MEMBER" rename to "~SqContributor";


CREATE OR REPLACE FUNCTION "EntityRef"(_entity character varying, _id integer, _entity_ref character varying, _limit character varying, _offset integer, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_c refcursor;
	__c refcursor;
	_query text;
	_link integer;
	_rs "RESULT"%rowtype;
	_ct_list varchar(200);
	_cx_list varchar(200);
	_pr_list varchar(200);
	_tm_list varchar(200);
	_index integer;
	_type1 integer;
	_type2 integer;
	_type3 integer;
	_array_id integer[];
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
	_id11 integer;_id12 integer;_id13 integer;_id14 integer;
	_cn1 varchar(5);_cn2 varchar(5);_cn3 varchar(5);_cn4 varchar(5);_cn5 varchar(5);_cn6 varchar(5);
	_tm1 varchar(60);_tm2 varchar(60);_tm3 varchar(60);_tm4 varchar(60);_tm5 varchar(60);_tm6 varchar(60);
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'IF', _entity || '-' || _id, current_date);
	
	_index := 1;

	IF _entity ~ 'CT' THEN
		SELECT LINK INTO _link FROM "CITY" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "CITY" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_ct_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_ct_list := _ct_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_ct_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'CX' THEN
		SELECT LINK INTO _link FROM "COMPLEX" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "COMPLEX" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_cx_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_cx_list := _cx_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_cx_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'PR' THEN
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
	ELSIF _entity ~ 'TM' THEN
		SELECT LINK INTO _link FROM "TEAM" WHERE ID = _id AND (YEAR1 IS NULL OR YEAR1 = '');
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "TEAM" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_tm_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_tm_list := _tm_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_tm_list := cast(_id AS varchar);
		END IF;
	END IF;

	-- References in: [Draws] (MUST STAY AT FIRST)
	IF (_entity ~ 'CN|PR|TM' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT DR.id, DR.id_result, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, DR.id1_qf1, DR.id2_qf1, DR.id1_qf2, DR.id2_qf2, DR.id1_qf3, DR.id2_qf3, DR.id1_qf4, DR.id2_qf4, DR.id1_sf1, DR.id2_sf1, DR.id1_sf2, DR.id2_sf2, DR.id1_thd, DR.id2_thd, TP1.number, TP2.number, TP3.number FROM "DRAW" DR';
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
		IF _entity = 'CN' THEN
			_query := _query || ' AND (DR.id1_sf1 <> ' || _id || ' AND DR.id1_sf2 <> ' || _id || ') AND (DR.id1_qf1 = ' || _id || ' OR DR.id2_qf1 = ' || _id || ' OR DR.id1_qf2 = ' || _id || ' OR DR.id2_qf2 = ' || _id || ' OR DR.id1_qf3 = ' || _id || ' OR DR.id2_qf3 = ' || _id || ' OR DR.id1_qf4 = ' || _id || ' OR DR.id2_qf4 = ' || _id || ' OR DR.id2_sf1 = ' || _id || ' OR DR.id2_sf2 = ' || _id || ' OR DR.id1_thd = ' || _id || ' OR DR.id2_thd = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _pr_list || ') AND DR.id1_sf2 NOT IN (' || _pr_list || ')) AND (DR.id1_qf1 IN (' || _pr_list || ') OR DR.id2_qf1 IN (' || _pr_list || ') OR DR.id1_qf2 IN (' || _pr_list || ') OR DR.id2_qf2 IN (' || _pr_list || ') OR DR.id1_qf3 IN (' || _pr_list || ') OR DR.id2_qf3 IN (' || _pr_list || ') OR DR.id1_qf4 IN (' || _pr_list || ') OR DR.id2_qf4 IN (' || _pr_list || ') OR DR.id2_sf1 IN (' || _pr_list || ') OR DR.id2_sf2 IN (' || _pr_list || ') OR DR.id1_thd IN (' || _pr_list || ') OR DR.id2_thd IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _tm_list || ') AND DR.id1_sf2 NOT IN (' || _tm_list || ')) AND (DR.id1_qf1 IN (' || _tm_list || ') OR DR.id2_qf1 IN (' || _tm_list || ') OR DR.id1_qf2 IN (' || _tm_list || ') OR DR.id2_qf2 IN (' || _tm_list || ') OR DR.id1_qf3 IN (' || _tm_list || ') OR DR.id2_qf3 IN (' || _tm_list || ') OR DR.id1_qf4 IN (' || _tm_list || ') OR DR.id2_qf4 IN (' || _tm_list || ') OR DR.id2_sf1 IN (' || _tm_list || ') OR DR.id2_sf2 IN (' || _tm_list || ') OR DR.id1_thd IN (' || _tm_list || ') OR DR.id2_thd IN (' || _tm_list || '))';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel10, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _id11, _id12, _id13, _id14, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF (_id13 = _id OR _id14 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)'))) THEN
				_item.txt2 := 'thd';
			ELSIF (_id9 = _id OR _id10 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)'))) THEN
				_item.txt2 := 'sf1';
			ELSIF (_id11 = _id OR _id12 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)'))) THEN
				_item.txt2 := 'sf2';
			ELSIF (_id1 = _id OR _id2 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)'))) THEN
				_item.txt2 := 'qf1';
			ELSIF (_id3 = _id OR _id4 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)'))) THEN
				_item.txt2 := 'qf2';
			ELSIF (_id5 = _id OR _id6 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)'))) THEN
				_item.txt2 := 'qf3';
			ELSIF (_id7 = _id OR _id8 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)'))) THEN
				_item.txt2 := 'qf4';
			END IF;
			IF _type1 <= 10 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN '', '' || PR1.first_name ELSE '''' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || '' '' ELSE '''' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN '', '' || PR2.first_name ELSE '''' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || '' '' ELSE '''' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, DR.result_' || _item.txt2;
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "PERSON" PR1 ON DR.id1_' || _item.txt2 || ' = PR1.id LEFT JOIN "PERSON" PR2 ON DR.id2_' || _item.txt2 || ' = PR2.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel8, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel9, _item.id_rel13, _cn2, _tm2, _item.txt1;
				CLOSE __c;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				_item.comment := 'PR';
			ELSIF _type1 = 50 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', TM1.label, TM2.label, NULL, NULL, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "TEAM" TM1 ON DR.id1_' || _item.txt2 || ' = TM1.id LEFT JOIN "TEAM" TM2 ON DR.id2_' || _item.txt2 || ' = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'TM';
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', CN1.label' || _lang || ', CN2.label' || _lang || ', CN1.label, CN2.label, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "COUNTRY" CN1 ON DR.id1_' || _item.txt2 || ' = CN1.id LEFT JOIN "COUNTRY" CN2 ON DR.id2_' || _item.txt2 || ' = CN2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'CN';
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
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number, TP3.number';
		IF (_entity = 'PR') THEN
			_query := _query || ', PL.rank';
		ELSE
			_query := _query || ', 0';
		END IF;
		_query := _query || ' FROM "RESULT" RS';
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
		ELSIF (_entity = 'PR') THEN
			_type1 = 1;
			_type2 = 99;
			_query := _query || ' LEFT JOIN "~PERSON_LIST" PL ON PL.id_result = RS.id';
		END IF;
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RS.id_rank1 = ' || _id || ' OR RS.id_rank2 = ' || _id || ' OR RS.id_rank3 = ' || _id || ' OR RS.id_rank4 = ' || _id || ' OR RS.id_rank5 = ' || _id || ' OR RS.id_rank6 = ' || _id || ' OR RS.id_rank7 = ' || _id || ' OR RS.id_rank8 = ' || _id || ' OR RS.id_rank9 = ' || _id || ' OR RS.id_rank10 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (PL.id_person IN (' || _pr_list || ') OR (((TP1.number BETWEEN 1 AND 10 AND TP2.number IS NULL) OR (TP2.number BETWEEN 1 AND 10 AND TP3.number IS NULL) OR (TP3.number BETWEEN 1 AND 10)) AND ((RS.id_rank1 IN (' || _pr_list || ') OR RS.id_rank2 IN (' || _pr_list || ') OR RS.id_rank3 IN (' || _pr_list || ') OR RS.id_rank4 IN (' || _pr_list || ') OR RS.id_rank5 IN (' || _pr_list || ') OR RS.id_rank6 IN (' || _pr_list || ') OR RS.id_rank7 IN (' || _pr_list || ') OR RS.id_rank8 IN (' || _pr_list || ') OR RS.id_rank9 IN (' || _pr_list || ') OR RS.id_rank10 IN (' || _pr_list || ')))))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RS.id_rank1 IN (' || _tm_list || ') OR RS.id_rank2 IN (' || _tm_list || ') OR RS.id_rank3 IN (' || _tm_list || ') OR RS.id_rank4 IN (' || _tm_list || ') OR RS.id_rank5 IN (' || _tm_list || ') OR RS.id_rank6 IN (' || _tm_list || ') OR RS.id_rank7 IN (' || _tm_list || ') OR RS.id_rank8 IN (' || _tm_list || ') OR RS.id_rank9 IN (' || _tm_list || ') OR RS.id_rank10 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND  (RS.id_city1 IN (' || _ct_list || ') OR RS.id_city2 IN (' || _ct_list || '))';
		ELSIF _entity = 'CX' THEN
			_query := _query || ' AND  (RS.id_complex1 IN (' || _cx_list || ') OR RS.id_complex2 IN (' || _cx_list || '))';
		ELSIF _entity = 'OL' THEN
			_query := _query || ' AND RS.id_championship=1 AND OL.id = ' || _id;
		ELSIF _entity = 'YR' THEN
			_query := _query || ' AND RS.id_year = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2, _type3, _item.count1;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF _type1 <= 10 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, CN3.code, TM3.label, PR4.last_name || (CASE WHEN length(PR4.first_name) > 0 THEN ', ' || PR4.first_name ELSE '' END), (CASE WHEN length(PR4.first_name) > 0 THEN PR4.first_name || ' ' ELSE '' END) || PR4.last_name, CN4.id, CN4.code, TM4.label, PR5.last_name || (CASE WHEN length(PR5.first_name) > 0 THEN ', ' || PR5.first_name ELSE '' END), (CASE WHEN length(PR5.first_name) > 0 THEN PR5.first_name || ' ' ELSE '' END) || PR5.last_name, CN5.id, CN5.code, TM5.label, PR6.last_name || (CASE WHEN length(PR6.first_name) > 0 THEN ', ' || PR6.first_name ELSE '' END), (CASE WHEN length(PR6.first_name) > 0 THEN PR6.first_name || ' ' ELSE '' END) || PR6.last_name, CN6.id, CN6.code, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.label_rel23, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.label_rel24, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.label_rel25, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt3, _item.txt4
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
				IF _type1 = 4 OR _item.txt3 = '#DOUBLE#' THEN
					_item.txt4 = '1-2/3-4/5-6';
				ELSIF _type1 = 5 OR _item.txt3 = '#TRIPLE#' THEN
					_item.txt4 = '1-3/4-6/7-9';
				END IF;
				_item.comment := 'PR';
				_array_id := string_to_array(_pr_list, ',')::integer[];
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, NULL, NULL, NULL, NULL, NULL, NULL, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "TEAM" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "TEAM" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "TEAM" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "TEAM" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "TEAM" TM5 ON RS.id_rank5 = TM5.id LEFT JOIN "TEAM" TM6 ON RS.id_rank6 = TM6.id
				WHERE RS.id = _item.id_item;
				_item.comment := 'TM';
				_array_id := string_to_array(_tm_list, ',')::integer[];
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN4.label' || _lang || ', CN5.label' || _lang || ', CN6.label' || _lang || ', CN1.label, CN2.label, CN3.label, CN4.label, CN5.label, CN6.label, RS.result1, RS.result2, RS.comment, RS.exa';
				_query := _query || ' FROM "RESULT" RS LEFT JOIN "COUNTRY" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "COUNTRY" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "COUNTRY" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "COUNTRY" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "COUNTRY" CN5 ON RS.id_rank5 = CN5.id LEFT JOIN "COUNTRY" CN6 ON RS.id_rank6 = CN6.id';
				_query := _query || ' WHERE RS.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4;
				CLOSE __c;
				_item.comment := 'CN';
				_array_id := ARRAY[_id];
			END IF;
			SELECT * INTO _rs FROM "RESULT" RS WHERE RS.id = _item.id_item;
			SELECT "GetRank"(_rs, _array_id) INTO _item.count1;
			_item.id = _index;
			_item.entity = 'RS';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Athletes]
	IF (_entity ~ 'CN|SP|TM' AND (_entity_ref = 'PR' OR _entity_ref = '')) THEN
		_query := 'SELECT DISTINCT ON (PR.last_name, PR.first_name, CN.id, SP.id) PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "PERSON" PR';
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
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT TM.id, TM.label, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "TEAM" TM';
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
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT CT.id, CT.label' || _lang || ', CT.label, CN.id, CN.label' || _lang || ', CN.label FROM "CITY" CT';
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
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.label_rel2;
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
		_query := 'SELECT CX.id, CX.label' || _lang || ', CX.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label FROM "COMPLEX" CX';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' WHERE CX.id_city = ' || _id;
		_query := _query || ' ORDER BY CX.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OL.type FROM "OLYMPICS" OL';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query := _query || ' WHERE OL.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY OL.type, YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT OR_.id, OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OR_.count_gold || '','' || OR_.count_silver || '','' || OR_.count_bronze FROM "OLYMPIC_RANKING" OR_';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT RC.id, RC.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, RC.type1, RC.type2, RC.record1, RC.id_rank1, RC.id_rank2, RC.id_rank3, RC.id_rank4, RC.id_rank5 FROM "RECORD" RC';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RC.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RC.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RC.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "TYPE" TP ON EV.id_type = TP.id';
		_query := _query || ' WHERE TP.number BETWEEN ' || _type1 || ' AND ' || _type2;
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RC.id_rank1 = ' || _id || ' OR RC.id_rank2 = ' || _id || ' OR RC.id_rank3 = ' || _id || ' OR RC.id_rank4 = ' || _id || ' OR RC.id_rank5 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _pr_list || ') OR RC.id_rank2 IN (' || _pr_list || ') OR RC.id_rank3 IN (' || _pr_list || ') OR RC.id_rank4 IN (' || _pr_list || ') OR RC.id_rank5 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _tm_list || ') OR RC.id_rank2 IN (' || _tm_list || ') OR RC.id_rank3 IN (' || _tm_list || ') OR RC.id_rank4 IN (' || _tm_list || ') OR RC.id_rank5 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RC.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RC.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RC.id_event = ' || _id || ' OR RC.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND RC.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', RC.index LIMIT ' || _limit || ' OFFSET ' || _offset;
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
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Hall of Fame]
	IF (_entity ~ 'YR|PR' AND (_entity_ref = 'HF' OR _entity_ref = '')) THEN
		_query := 'SELECT HF.id, YR.id, YR.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, HF.position FROM "HALL_OF_FAME" HF';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.txt1;
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
		_query := 'SELECT RN.id, TM.id, TM.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, RN.number FROM "RETIRED_NUMBER" RN';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.id_rel4;
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
		_query := 'SELECT TS.id, TM.id, TM.label, CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', CX.label, CT.label, ST.label, CN.label, LG.id, LG.label, TS.date1, TS.date2 FROM "TEAM_STADIUM" TS';
		_query := _query || ' LEFT JOIN "TEAM" TM ON TS.id_team = TM.id';
		_query := _query || ' LEFT JOIN "COMPLEX" CX ON TS.id_complex = CX.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON TS.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE TS.id_team = ' || _id;
		ELSIF _entity = 'CX' THEN
			_query := _query || ' WHERE TS.id_complex IN (' || _cx_list || ')';
		END IF;
		_query := _query || ' ORDER BY TM.label, TS.date1 DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.id_rel6, _item.comment, _item.txt1, _item.txt2;
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
		_query := 'SELECT WL.id, TM.id, TM.label, LG.id, LG.label, WL.type, WL.count_win || '','' || WL.count_loss FROM "WIN_LOSS" WL';
		_query := _query || ' LEFT JOIN "TEAM" TM ON WL.id_team = TM.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON WL.id_league = LG.id';
		_query := _query || ' WHERE WL.id_team = ' || _id;
		_query := _query || ' ORDER BY TM.label DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'WL';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Contributions]
	IF (_entity ~ 'CB' AND (_entity_ref = 'CO' OR _entity_ref = '')) THEN
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, CO.type, CO.date';
		_query := _query || ' FROM "~CONTRIBUTION" CO';
		_query := _query || ' LEFT JOIN "RESULT" RS ON CO.id_item = RS.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' WHERE RS.id_contributor=' || _id || ' ORDER BY CO.date DESC, YR.id DESC, SP.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.date1;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CO';
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
  
  

ALTER FUNCTION "~STATREQUEST"(smallint) RENAME TO "~StatRequest";
alter FUNCTION "~MERGE"(character varying, integer, integer) RENAME TO "~Merge";
alter FUNCTION "~LAST_UPDATES"(integer, integer, character varying) RENAME TO "~LastUpdates";
ALTER FUNCTION "GET_DRAW"(integer, character varying) RENAME TO "GetDraw";
alter  FUNCTION "GET_HALL_OF_FAME"(integer, text, character varying) rename to "GetHallOfFame";
alter FUNCTION "GET_MEDAL_COUNT"(character varying, integer, character varying) rename to "GetMedalCount";
alter FUNCTION "GET_OLYMPIC_MEDALS"(text, integer, text, text, text, character varying) rename to "GetOlympicMedals";
alter FUNCTION "GET_OLYMPIC_RANKINGS"(text, text, character varying) rename to "GetOlympicRankings";
alter FUNCTION "GET_PERSON_LIST"(text) rename to "GetPersonList";
alter function "GET_RESULTS"(integer, integer, integer, integer, integer, text, character varying) rename to "GetResults";
alter FUNCTION "GET_RETIRED_NUMBER"(integer, text, smallint) rename to "GetRetiredNumbers";
alter FUNCTION "GET_TEAM_STADIUM"(integer, text, character varying) rename to "GetTeamStadiums";
alter FUNCTION "GET_US_CHAMPIONSHIPS"(integer, text, character varying) rename to "GetUSChampionships";
alter FUNCTION "GET_US_RECORDS"(integer, text, text, text, text, character varying) rename to "GetUSRecords"; 
alter FUNCTION "GET_WIN_LOSS"(integer, text) rename to "GetWinLoss";
alter FUNCTION "TREE_RESULTS"(character varying, character varying) rename to "TreeResults";
ALTER FUNCTION "WIN_RECORDS"(text, character varying) rename to "WinRecords";




alter TABLE "~Contributor" ADD admin boolean;
alter TABLE "~Contributor" ADD sports varchar(50);




DROP VIEW "~Contributors";


CREATE OR REPLACE VIEW "~Contributors" AS 
 SELECT T.id, T.login, T.name, array_to_string(array_agg(sp.label), '|') AS sports, array_to_string(array_agg(sp.label_fr), '|') AS sports_fr, count_a, count_u FROM
 ( SELECT cr.id, cr.login, cr.public_name AS name, sports, sum(
        CASE
            WHEN cb.type = 'A'::bpchar THEN 1
            ELSE 0
        END) AS count_a, sum(
        CASE
            WHEN cb.type = 'U'::bpchar THEN 1
            ELSE 0
        END) AS count_u
   FROM "~Contributor" cr
   LEFT JOIN "~CONTRIBUTION" cb ON cr.id = cb.id_contributor
  GROUP BY cr.id, cr.login, name, sports
 ) AS T LEFT JOIN "SPORT" sp ON sp.id = ANY(string_to_array(T.sports, ',')::integer[])
 GROUP BY T.id, T.login, T.name, T.count_a, T.count_u
  ORDER BY count_a DESC;
  
  
  
  
CREATE OR REPLACE FUNCTION "EntityRef"(_entity character varying, _id integer, _entity_ref character varying, _limit character varying, _offset integer, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_c refcursor;
	__c refcursor;
	_query text;
	_link integer;
	_rs "RESULT"%rowtype;
	_ct_list varchar(200);
	_cx_list varchar(200);
	_pr_list varchar(200);
	_tm_list varchar(200);
	_index integer;
	_type1 integer;
	_type2 integer;
	_type3 integer;
	_array_id integer[];
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
	_id11 integer;_id12 integer;_id13 integer;_id14 integer;
	_cn1 varchar(5);_cn2 varchar(5);_cn3 varchar(5);_cn4 varchar(5);_cn5 varchar(5);_cn6 varchar(5);
	_tm1 varchar(60);_tm2 varchar(60);_tm3 varchar(60);_tm4 varchar(60);_tm5 varchar(60);_tm6 varchar(60);
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'IF', _entity || '-' || _id, current_date);
	
	_index := 1;

	IF _entity ~ 'CT' THEN
		SELECT LINK INTO _link FROM "CITY" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "CITY" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_ct_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_ct_list := _ct_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_ct_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'CX' THEN
		SELECT LINK INTO _link FROM "COMPLEX" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "COMPLEX" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_cx_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_cx_list := _cx_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_cx_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'PR' THEN
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
	ELSIF _entity ~ 'TM' THEN
		SELECT LINK INTO _link FROM "TEAM" WHERE ID = _id AND (YEAR1 IS NULL OR YEAR1 = '');
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "TEAM" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_tm_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_tm_list := _tm_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_tm_list := cast(_id AS varchar);
		END IF;
	END IF;

	-- References in: [Draws] (MUST STAY AT FIRST)
	IF (_entity ~ 'CN|PR|TM' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT DR.id, DR.id_result, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, DR.id1_qf1, DR.id2_qf1, DR.id1_qf2, DR.id2_qf2, DR.id1_qf3, DR.id2_qf3, DR.id1_qf4, DR.id2_qf4, DR.id1_sf1, DR.id2_sf1, DR.id1_sf2, DR.id2_sf2, DR.id1_thd, DR.id2_thd, TP1.number, TP2.number, TP3.number FROM "DRAW" DR';
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
		IF _entity = 'CN' THEN
			_query := _query || ' AND (DR.id1_sf1 <> ' || _id || ' AND DR.id1_sf2 <> ' || _id || ') AND (DR.id1_qf1 = ' || _id || ' OR DR.id2_qf1 = ' || _id || ' OR DR.id1_qf2 = ' || _id || ' OR DR.id2_qf2 = ' || _id || ' OR DR.id1_qf3 = ' || _id || ' OR DR.id2_qf3 = ' || _id || ' OR DR.id1_qf4 = ' || _id || ' OR DR.id2_qf4 = ' || _id || ' OR DR.id2_sf1 = ' || _id || ' OR DR.id2_sf2 = ' || _id || ' OR DR.id1_thd = ' || _id || ' OR DR.id2_thd = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _pr_list || ') AND DR.id1_sf2 NOT IN (' || _pr_list || ')) AND (DR.id1_qf1 IN (' || _pr_list || ') OR DR.id2_qf1 IN (' || _pr_list || ') OR DR.id1_qf2 IN (' || _pr_list || ') OR DR.id2_qf2 IN (' || _pr_list || ') OR DR.id1_qf3 IN (' || _pr_list || ') OR DR.id2_qf3 IN (' || _pr_list || ') OR DR.id1_qf4 IN (' || _pr_list || ') OR DR.id2_qf4 IN (' || _pr_list || ') OR DR.id2_sf1 IN (' || _pr_list || ') OR DR.id2_sf2 IN (' || _pr_list || ') OR DR.id1_thd IN (' || _pr_list || ') OR DR.id2_thd IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _tm_list || ') AND DR.id1_sf2 NOT IN (' || _tm_list || ')) AND (DR.id1_qf1 IN (' || _tm_list || ') OR DR.id2_qf1 IN (' || _tm_list || ') OR DR.id1_qf2 IN (' || _tm_list || ') OR DR.id2_qf2 IN (' || _tm_list || ') OR DR.id1_qf3 IN (' || _tm_list || ') OR DR.id2_qf3 IN (' || _tm_list || ') OR DR.id1_qf4 IN (' || _tm_list || ') OR DR.id2_qf4 IN (' || _tm_list || ') OR DR.id2_sf1 IN (' || _tm_list || ') OR DR.id2_sf2 IN (' || _tm_list || ') OR DR.id1_thd IN (' || _tm_list || ') OR DR.id2_thd IN (' || _tm_list || '))';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel10, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _id11, _id12, _id13, _id14, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF (_id13 = _id OR _id14 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)'))) THEN
				_item.txt2 := 'thd';
			ELSIF (_id9 = _id OR _id10 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)'))) THEN
				_item.txt2 := 'sf1';
			ELSIF (_id11 = _id OR _id12 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)'))) THEN
				_item.txt2 := 'sf2';
			ELSIF (_id1 = _id OR _id2 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)'))) THEN
				_item.txt2 := 'qf1';
			ELSIF (_id3 = _id OR _id4 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)'))) THEN
				_item.txt2 := 'qf2';
			ELSIF (_id5 = _id OR _id6 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)'))) THEN
				_item.txt2 := 'qf3';
			ELSIF (_id7 = _id OR _id8 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)'))) THEN
				_item.txt2 := 'qf4';
			END IF;
			IF _type1 <= 10 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN '', '' || PR1.first_name ELSE '''' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || '' '' ELSE '''' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN '', '' || PR2.first_name ELSE '''' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || '' '' ELSE '''' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, DR.result_' || _item.txt2;
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "PERSON" PR1 ON DR.id1_' || _item.txt2 || ' = PR1.id LEFT JOIN "PERSON" PR2 ON DR.id2_' || _item.txt2 || ' = PR2.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel8, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel9, _item.id_rel13, _cn2, _tm2, _item.txt1;
				CLOSE __c;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				_item.comment := 'PR';
			ELSIF _type1 = 50 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', TM1.label, TM2.label, NULL, NULL, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "TEAM" TM1 ON DR.id1_' || _item.txt2 || ' = TM1.id LEFT JOIN "TEAM" TM2 ON DR.id2_' || _item.txt2 || ' = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'TM';
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', CN1.label' || _lang || ', CN2.label' || _lang || ', CN1.label, CN2.label, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "COUNTRY" CN1 ON DR.id1_' || _item.txt2 || ' = CN1.id LEFT JOIN "COUNTRY" CN2 ON DR.id2_' || _item.txt2 || ' = CN2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'CN';
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
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number, TP3.number';
		IF (_entity = 'PR') THEN
			_query := _query || ', PL.rank';
		ELSE
			_query := _query || ', 0';
		END IF;
		_query := _query || ' FROM "RESULT" RS';
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
		ELSIF (_entity = 'PR') THEN
			_type1 = 1;
			_type2 = 99;
			_query := _query || ' LEFT JOIN "~PERSON_LIST" PL ON PL.id_result = RS.id';
		END IF;
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RS.id_rank1 = ' || _id || ' OR RS.id_rank2 = ' || _id || ' OR RS.id_rank3 = ' || _id || ' OR RS.id_rank4 = ' || _id || ' OR RS.id_rank5 = ' || _id || ' OR RS.id_rank6 = ' || _id || ' OR RS.id_rank7 = ' || _id || ' OR RS.id_rank8 = ' || _id || ' OR RS.id_rank9 = ' || _id || ' OR RS.id_rank10 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (PL.id_person IN (' || _pr_list || ') OR (((TP1.number BETWEEN 1 AND 10 AND TP2.number IS NULL) OR (TP2.number BETWEEN 1 AND 10 AND TP3.number IS NULL) OR (TP3.number BETWEEN 1 AND 10)) AND ((RS.id_rank1 IN (' || _pr_list || ') OR RS.id_rank2 IN (' || _pr_list || ') OR RS.id_rank3 IN (' || _pr_list || ') OR RS.id_rank4 IN (' || _pr_list || ') OR RS.id_rank5 IN (' || _pr_list || ') OR RS.id_rank6 IN (' || _pr_list || ') OR RS.id_rank7 IN (' || _pr_list || ') OR RS.id_rank8 IN (' || _pr_list || ') OR RS.id_rank9 IN (' || _pr_list || ') OR RS.id_rank10 IN (' || _pr_list || ')))))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RS.id_rank1 IN (' || _tm_list || ') OR RS.id_rank2 IN (' || _tm_list || ') OR RS.id_rank3 IN (' || _tm_list || ') OR RS.id_rank4 IN (' || _tm_list || ') OR RS.id_rank5 IN (' || _tm_list || ') OR RS.id_rank6 IN (' || _tm_list || ') OR RS.id_rank7 IN (' || _tm_list || ') OR RS.id_rank8 IN (' || _tm_list || ') OR RS.id_rank9 IN (' || _tm_list || ') OR RS.id_rank10 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND  (RS.id_city1 IN (' || _ct_list || ') OR RS.id_city2 IN (' || _ct_list || '))';
		ELSIF _entity = 'CX' THEN
			_query := _query || ' AND  (RS.id_complex1 IN (' || _cx_list || ') OR RS.id_complex2 IN (' || _cx_list || '))';
		ELSIF _entity = 'OL' THEN
			_query := _query || ' AND RS.id_championship=1 AND OL.id = ' || _id;
		ELSIF _entity = 'YR' THEN
			_query := _query || ' AND RS.id_year = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2, _type3, _item.count1;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF _type1 <= 10 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, CN3.code, TM3.label, PR4.last_name || (CASE WHEN length(PR4.first_name) > 0 THEN ', ' || PR4.first_name ELSE '' END), (CASE WHEN length(PR4.first_name) > 0 THEN PR4.first_name || ' ' ELSE '' END) || PR4.last_name, CN4.id, CN4.code, TM4.label, PR5.last_name || (CASE WHEN length(PR5.first_name) > 0 THEN ', ' || PR5.first_name ELSE '' END), (CASE WHEN length(PR5.first_name) > 0 THEN PR5.first_name || ' ' ELSE '' END) || PR5.last_name, CN5.id, CN5.code, TM5.label, PR6.last_name || (CASE WHEN length(PR6.first_name) > 0 THEN ', ' || PR6.first_name ELSE '' END), (CASE WHEN length(PR6.first_name) > 0 THEN PR6.first_name || ' ' ELSE '' END) || PR6.last_name, CN6.id, CN6.code, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.label_rel23, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.label_rel24, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.label_rel25, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt3, _item.txt4
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
				IF _type1 = 4 OR _item.txt3 = '#DOUBLE#' THEN
					_item.txt4 = '1-2/3-4/5-6';
				ELSIF _type1 = 5 OR _item.txt3 = '#TRIPLE#' THEN
					_item.txt4 = '1-3/4-6/7-9';
				END IF;
				_item.comment := 'PR';
				_array_id := string_to_array(_pr_list, ',')::integer[];
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, NULL, NULL, NULL, NULL, NULL, NULL, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "TEAM" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "TEAM" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "TEAM" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "TEAM" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "TEAM" TM5 ON RS.id_rank5 = TM5.id LEFT JOIN "TEAM" TM6 ON RS.id_rank6 = TM6.id
				WHERE RS.id = _item.id_item;
				_item.comment := 'TM';
				_array_id := string_to_array(_tm_list, ',')::integer[];
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN4.label' || _lang || ', CN5.label' || _lang || ', CN6.label' || _lang || ', CN1.label, CN2.label, CN3.label, CN4.label, CN5.label, CN6.label, RS.result1, RS.result2, RS.comment, RS.exa';
				_query := _query || ' FROM "RESULT" RS LEFT JOIN "COUNTRY" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "COUNTRY" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "COUNTRY" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "COUNTRY" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "COUNTRY" CN5 ON RS.id_rank5 = CN5.id LEFT JOIN "COUNTRY" CN6 ON RS.id_rank6 = CN6.id';
				_query := _query || ' WHERE RS.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4;
				CLOSE __c;
				_item.comment := 'CN';
				_array_id := ARRAY[_id];
			END IF;
			SELECT * INTO _rs FROM "RESULT" RS WHERE RS.id = _item.id_item;
			SELECT "GetRank"(_rs, _array_id) INTO _item.count1;
			_item.id = _index;
			_item.entity = 'RS';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Athletes]
	IF (_entity ~ 'CN|SP|TM' AND (_entity_ref = 'PR' OR _entity_ref = '')) THEN
		_query := 'SELECT DISTINCT ON (PR.last_name, PR.first_name, CN.id, SP.id) PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "PERSON" PR';
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
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT TM.id, TM.label, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "TEAM" TM';
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
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT CT.id, CT.label' || _lang || ', CT.label, CN.id, CN.label' || _lang || ', CN.label FROM "CITY" CT';
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
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.label_rel2;
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
		_query := 'SELECT CX.id, CX.label' || _lang || ', CX.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label FROM "COMPLEX" CX';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' WHERE CX.id_city = ' || _id;
		_query := _query || ' ORDER BY CX.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OL.type FROM "OLYMPICS" OL';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query := _query || ' WHERE OL.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY OL.type, YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT OR_.id, OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OR_.count_gold || '','' || OR_.count_silver || '','' || OR_.count_bronze FROM "OLYMPIC_RANKING" OR_';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT RC.id, RC.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, RC.type1, RC.type2, RC.record1, RC.id_rank1, RC.id_rank2, RC.id_rank3, RC.id_rank4, RC.id_rank5 FROM "RECORD" RC';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RC.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RC.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RC.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "TYPE" TP ON EV.id_type = TP.id';
		_query := _query || ' WHERE TP.number BETWEEN ' || _type1 || ' AND ' || _type2;
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RC.id_rank1 = ' || _id || ' OR RC.id_rank2 = ' || _id || ' OR RC.id_rank3 = ' || _id || ' OR RC.id_rank4 = ' || _id || ' OR RC.id_rank5 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _pr_list || ') OR RC.id_rank2 IN (' || _pr_list || ') OR RC.id_rank3 IN (' || _pr_list || ') OR RC.id_rank4 IN (' || _pr_list || ') OR RC.id_rank5 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _tm_list || ') OR RC.id_rank2 IN (' || _tm_list || ') OR RC.id_rank3 IN (' || _tm_list || ') OR RC.id_rank4 IN (' || _tm_list || ') OR RC.id_rank5 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RC.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RC.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RC.id_event = ' || _id || ' OR RC.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND RC.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', RC.index LIMIT ' || _limit || ' OFFSET ' || _offset;
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
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Hall of Fame]
	IF (_entity ~ 'YR|PR' AND (_entity_ref = 'HF' OR _entity_ref = '')) THEN
		_query := 'SELECT HF.id, YR.id, YR.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, HF.position FROM "HALL_OF_FAME" HF';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.txt1;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'HF';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Retired Numbers]
	IF (_entity ~ 'PR' AND (_entity_ref = 'RN' OR _entity_ref = '')) THEN
		_query := 'SELECT RN.id, TM.id, TM.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, RN.number FROM "RETIRED_NUMBER" RN';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.id_rel4;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'RN';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Team Stadiums]
	IF (_entity ~ 'CX' AND (_entity_ref = 'TS' OR _entity_ref = '')) THEN
		_query := 'SELECT TS.id, TM.id, TM.label, CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', CX.label, CT.label, ST.label, CN.label, LG.id, LG.label, TS.date1, TS.date2 FROM "TEAM_STADIUM" TS';
		_query := _query || ' LEFT JOIN "TEAM" TM ON TS.id_team = TM.id';
		_query := _query || ' LEFT JOIN "COMPLEX" CX ON TS.id_complex = CX.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON TS.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE TS.id_team = ' || _id;
		ELSIF _entity = 'CX' THEN
			_query := _query || ' WHERE TS.id_complex IN (' || _cx_list || ')';
		END IF;
		_query := _query || ' ORDER BY TM.label, TS.date1 DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.id_rel6, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'TS';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Contributions]
	IF (_entity ~ 'CB' AND (_entity_ref = 'CO' OR _entity_ref = '')) THEN
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, CO.type, CO.date';
		_query := _query || ' FROM "~CONTRIBUTION" CO';
		_query := _query || ' LEFT JOIN "RESULT" RS ON CO.id_item = RS.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' WHERE RS.id_contributor=' || _id || ' ORDER BY CO.date DESC, YR.id DESC, SP.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.date1;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CO';
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
  
  
  
  
  
  
  
  alter sequence "SQ_CHAMPIONSHIP" rename to "SeqChampionship";
alter sequence "SQ_CITY" rename to "SeqCity";
alter sequence "SQ_COMPLEX" rename to "SeqComplex";
alter sequence "SQ_COUNTRY" rename to "SeqCountry";
alter sequence "SQ_DRAW" rename to "SeqDraw";
alter sequence "SQ_EVENT" rename to "SeqEvent";
alter sequence "SQ_HALL_OF_FAME" rename to "SeqHallOfFame";
alter sequence "SQ_LEAGUE" rename to "SeqLeague";
alter sequence "SQ_OLYMPICS" rename to "SeqOlympics";
alter sequence "SQ_OLYMPIC_RANKING" rename to "SeqOlympicRanking";
alter sequence "SQ_PERSON" rename to "SeqPerson";
alter sequence "SQ_RECORD" rename to "SeqRecord";
alter sequence "SQ_RESULT" rename to "SeqResult";
alter sequence "SQ_RETIRED_NUMBER" rename to "SeqRetiredNumber";
alter sequence "SQ_SPORT" rename to "SeqSport";
alter sequence "SQ_STATE" rename to "SeqState";
alter sequence "SQ_TEAM" rename to "SeqTeam";
alter sequence "SQ_TEAM_STADIUM" rename to "SeqTeamStadium";
alter sequence "SQ_TYPE" rename to "SeqType";
alter sequence "SQ_WIN_LOSS" rename to "SeqWinLoss";
alter sequence "SQ_YEAR" rename to "SeqYear";
alter sequence "~SQ_CONTRIBUTION" rename to "~SeqContribution";

alter sequence "~SQ_ERROR_REPORT" rename to "~SeqErrorReport";
alter sequence "~SQ_EXTERNAL_LINK" rename to "~SeqExternalLink";
alter sequence "~SQ_FOLDER_HISTORY" rename to "~SeqFolderHistory";
alter sequence "~SQ_INACTIVE_ITEM" rename to "~SeqInactiveItem";
alter sequence "~SQ_PERSON_LIST" rename to "~SeqPersonList";
alter sequence "~SQ_REQUEST" rename to "~SeqRequest";


alter SEQUENCE "~SqContributor" rename to "~SeqContributor";


CREATE OR REPLACE FUNCTION "Search"(_pattern character varying, _scope character varying, _limit smallint, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
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
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SeqRequest"'), 'SC', _pattern, current_date);
	
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
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang || ' || '' ('' || CN.code || '')'', CN.code';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s ~ 'PR|TM') THEN -- Relation: Sport
				_rel_cols := _rel_cols || ', SP.id, SP.label' || _lang || ', SP.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "SPORT" SP ON ' || _s || '.id_sport = SP.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'PR') THEN -- Relation: Team
				_rel_cols := _rel_cols || ', TM.id, TM.label, TM.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "TEAM" TM ON ' || _s || '.id_team = TM.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'CX') THEN -- Relation: City/State/Country
				_rel_cols := _rel_cols || ', CT.id, CT.label' || _lang || ', CT.label';
				_rel_cols := _rel_cols || ', ST.id, ST.label' || _lang || ', ST.label';
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang || ', CN.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "CITY" CT ON ' || _s || '.id_city = CT.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			IF (_s = 'CT') THEN -- Relation: State/Country
				_rel_cols := _rel_cols || ', NULL, NULL, NULL';
				_rel_cols := _rel_cols || ', ST.id, ST.label' || _lang || ', ST.label';
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang || ', CN.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "STATE" ST ON ' || _s || '.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			IF (_s ~ 'CT|CX|PR|TM') THEN
				_rel_cols := _rel_cols || ', ' || _s || '.link';
			END IF;
			FOR _j IN 1.._rel_count LOOP
				_rel_cols := _rel_cols || ', NULL, NULL, NULL';
			END LOOP;

			-- Execute query
			_label := 'label';
			_label_en := 'label';
			IF (_s <> 'TM' AND _s <> 'YR') THEN
				_label := 'label' || _lang;
			END IF;
			_query := 'SELECT ' || _s || '.id, ' || _s || '.' || _label || ',' || _s || '.' || _label_en || ',' || _s || '.ref' || _rel_cols || ' FROM "' || _tables[_i] || '" ' || _s;
			_query := _query || _rel_joins || ' WHERE ' || (CASE _s WHEN 'CT' THEN '(CT.link = 0 OR CT.link IS NULL) AND ' WHEN 'CX' THEN '(CX.link = 0 OR CX.link IS NULL) AND ' WHEN 'TM' THEN '(TM.link = 0 OR TM.link IS NULL) AND ' ELSE '' END) || _s || '.' || _label || ' ~* ''' || __pattern || '''' || (CASE _limit WHEN 0 THEN ' ORDER BY ' || _s || '.' || _label ELSE '' END);
			IF _s = 'PR' THEN
				_query := 'SELECT PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, PR.ref' || _rel_cols || ' FROM "PERSON" PR' || _rel_joins;
				_query := _query || ' WHERE (PR.link = 0 OR PR.link IS NULL) AND (PR.last_name || '' '' || PR.first_name ~* ''' || __pattern || ''' OR PR.first_name || '' '' || PR.last_name ~* ''' || __pattern || ''' OR PR.last_name ~* ''' || __pattern || ''' OR PR.first_name ~* ''' || __pattern || ''')';
				_query := _query || (CASE _limit WHEN 0 THEN ' ORDER BY PR.last_name, PR.first_name' ELSE '' END);
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
						SELECT SUM(ref) INTO _item.count_ref FROM "CITY" WHERE id=_current_id OR link=_current_id;
					ELSIF _s = 'CX' THEN
						SELECT SUM(ref) INTO _item.count_ref FROM "COMPLEX" WHERE id=_current_id OR link=_current_id;
					ELSIF _s = 'PR' THEN
						SELECT SUM(ref) INTO _item.count_ref FROM "PERSON" WHERE id=_current_id OR link=_current_id;
					ELSIF _s = 'TM' THEN
						SELECT SUM(ref) INTO _item.count_ref FROM "TEAM" WHERE id=_current_id OR link=_current_id;
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

  CREATE OR REPLACE FUNCTION "GetTeamStadiums"(_id_league integer, _teams text, _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _team_condition text;
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SeqRequest"'), 'US', 'TS-' || _id_league, current_date);
	
	-- Set team condition ('All teams' = Empty condition)
	_team_condition := '';
	IF _teams <> '0' THEN
		_team_condition := ' AND TM.id IN (' || _teams || ')';
	END IF;
	
	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT
		TS.id AS ts_id, TM.id AS tm_id, TM.label AS tm_label, TS.renamed AS ts_renamed, TS.comment AS ts_comment,
		CX.id AS cx_id, CX.label' || _lang || ' AS cx_label, CX.label AS cx_label_en, CT.id AS ct_id, CT.label' || _lang || ' AS ct_label_en, CT.label AS ct_label, ST.id AS st_id, ST.code AS st_code, ST.label' || _lang || ' AS st_label, ST.label AS st_label_en,
		CN.id AS cn_id, CN.code AS cn_code, CN.label' || _lang || ' AS cn_label, CN.label AS cn_label_en, TS.date1 AS ts_date1, TS.date2 AS ts_date2
	FROM
		"TEAM_STADIUM" TS
		LEFT JOIN "TEAM" TM ON TS.id_team = TM.id
		LEFT JOIN "COMPLEX" CX ON TS.id_complex = CX.id
		LEFT JOIN "CITY" CT ON CX.id_city = CT.id
		LEFT JOIN "STATE" ST ON CT.id_state = ST.id
		LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id
	WHERE
		TS.id_league = ' || _id_league || _team_condition || '
	ORDER BY
		TM.label, TS.date1 DESC';
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  CREATE OR REPLACE FUNCTION "GetUSChampionships"(_id_championship integer, _years text, _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _year_condition text;
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SeqRequest"'), 'US', 'CP-' || _id_championship, current_date);

	_year_condition := CASE WHEN _years <> '0' THEN ' AND YR.id IN (' || _years || ')' ELSE '' END;
	
	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT
		RS.id AS rs_id, RS.date1 AS rs_date1, RS.date2 AS rs_date2, RS.id_rank1 AS rs_rank1, RS.id_rank2 AS rs_rank2, TM1.label as rs_team1, TM2.label as rs_team2, RS.result1 AS rs_result,
		RS.comment AS rs_comment, RS.exa AS rs_exa, YR.id AS yr_id, YR.label AS yr_label, CX.id AS cx_id, CX.label' || _lang || ' AS cx_label, CX.label AS cx_label_en,
		CT.id AS ct_id, CT.label' || _lang || ' AS ct_label, CT.label AS ct_label_en, ST.id AS st_id, ST.code AS st_code, ST.label' || _lang || ' AS st_label, ST.label AS st_label_en, CN.id AS cn_id, CN.code AS cn_code, CN.label' || _lang || ' AS cn_label, CN.label AS cn_label_en
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
		RS.id_event IN (455,532,572,621) AND (RS.id_subevent IS NULL OR RS.id_subevent IN (452,453,454,573,624,530)) AND (RS.id_subevent2 IS NULL OR RS.id_subevent2 IN (452,453,454,573,624,530)) ' || _year_condition || '
	ORDER BY RS.id_year DESC';
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  CREATE OR REPLACE FUNCTION "GetUSRecords"(_id_championship integer, _events text, _subevents text, _types1 text, _types2 text, _lang character varying)
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
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SeqRequest"'), 'US', 'RC-' || _id_championship, current_date);

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
		RC.id AS rc_id, RC.label AS rc_label, EV.id AS ev_id, EV.label' || _lang || ' AS ev_label, SE.id AS se_id, SE.label' || _lang || ' AS se_label,
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
  
  
  CREATE OR REPLACE FUNCTION "GetWinLoss"(_id_league integer, _teams text)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _team_condition text;
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SeqRequest"'), 'US', 'WL-' || _id_league, current_date);

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
		WL.count_otloss AS wl_count_otloss
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
  
  
  CREATE OR REPLACE FUNCTION "GetRetiredNumbers"(_id_league integer, _teams text, _number smallint)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _team_condition text;
    _number_condition text;
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SeqRequest"'), 'US', 'RN-' || _id_league, current_date);

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
  
  
  CREATE OR REPLACE FUNCTION "GetResults"(_id_sport integer, _id_championship integer, _id_event integer, _id_subevent integer, _id_subevent2 integer, _years text, _lang character varying)
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
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SeqRequest"'), 'RS', _id_sport || '-' || _id_championship, current_date);

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
	        _columns := _columns || ', PR' || i || '.last_name AS en' || i || '_str1, PR' || i || '.first_name AS en' || i || '_str2, NULL AS en' || i || '_str3';
	        _columns := _columns || ', PRTM' || i || '.id AS en' || i || '_rel1_id, NULL AS en' || i || '_rel1_code, PRTM' || i || '.label AS en' || i || '_rel1_label';
	        _columns := _columns || ', PRCN' || i || '.id AS en' || i || '_rel2_id, PRCN' || i || '.code AS en' || i || '_rel2_code, PRCN' || i || '.label' || _lang || ' AS en' || i || '_rel2_label, PRCN' || i || '.label AS en' || i || '_rel2_label_en';
	        _joins := _joins || ' LEFT JOIN "PERSON" PR' || i || ' ON RS.id_rank' || i || ' = PR' || i || '.id';
	        _joins := _joins || ' LEFT JOIN "TEAM" PRTM' || i || ' ON PR' || i || '.id_team = PRTM' || i || '.id';
	        _joins := _joins || ' LEFT JOIN "COUNTRY" PRCN' || i || ' ON PR' || i || '.id_country = PRCN' || i || '.id';
	    ELSIF _type = 50 THEN -- Team
	        _columns := _columns || ', NULL AS en' || i || '_str1, TM' || i || '.label AS en' || i || '_str2, NULL AS en' || i || '_str3';
	        _columns := _columns || ', NULL AS en' || i || '_rel1_id, NULL AS en' || i || '_rel1_code, NULL AS en' || i || '_rel1_label';
	        _columns := _columns || ', TMCN' || i || '.id AS en' || i || '_rel2_id, TMCN' || i || '.code AS en' || i || '_rel2_code, TMCN' || i || '.label' || _lang || ' AS en' || i || '_rel2_label, TMCN' || i || '.label AS en' || i || '_rel2_label_en';
	        _joins := _joins || ' LEFT JOIN "TEAM" TM' || i || ' ON RS.id_rank' || i || ' = TM' || i || '.id';
	        _joins := _joins || ' LEFT JOIN "COUNTRY" TMCN' || i || ' ON TM' || i || '.id_country = TMCN' || i || '.id';
	    ELSIF _type = 99 THEN -- Country
	        _columns := _columns || ', _CN' || i || '.code AS en' || i || '_str1, _CN' || i || '.label' || _lang || ' AS en' || i || '_str2, _CN' || i || '.label AS en' || i || '_str3';
	        _columns := _columns || ', NULL AS en' || i || '_rel1_id, NULL AS en' || i || '_rel1_code, NULL AS en' || i || '_rel1_label';
	        _columns := _columns || ', NULL AS en' || i || '_rel2_id, NULL AS en' || i || '_rel2_code, NULL AS en' || i || '_rel2_label, NULL AS en' || i || '_rel2_label_en';
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
		RS.result3 AS rs_result3, RS.result4 AS rs_result4, RS.result5 AS rs_result5, RS.comment AS rs_comment, RS.exa AS rs_exa, YR.id AS yr_id, YR.label AS yr_label, CX1.id AS cx1_id, CX1.label' || _lang || ' AS cx1_label, CX1.label AS cx1_label_en, CX2.id AS cx2_id, CX2.label' || _lang || ' AS cx2_label, CX2.label AS cx2_label_en,
		CT1.id AS ct1_id, CT1.label' || _lang || ' AS ct1_label, CT1.label AS ct1_label_en, CT2.id AS ct2_id, CT2.label' || _lang || ' AS ct2_label, CT2.label AS ct2_label_en, CT3.id AS ct3_id, CT3.label' || _lang || ' AS ct3_label, CT3.label AS ct3_label_en, CT4.id AS ct4_id, CT4.label' || _lang || ' AS ct4_label, CT4.label AS ct4_label_en, ST1.id AS st1_id, ST1.code AS st1_code, ST1.label' || _lang || ' AS st1_label, ST1.label AS st1_label_en, ST2.id AS st2_id, ST2.code AS st2_code,
		ST2.label' || _lang || ' AS st2_label, ST2.label AS st2_label_en, ST3.id AS st3_id, ST3.code AS st3_code, ST3.label' || _lang || ' AS st3_label, ST3.label AS st3_label_en, ST4.id AS st4_id, ST4.code AS st4_code, ST4.label' || _lang || ' AS st4_label, ST4.label AS st4_label_en, CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label' || _lang || ' AS cn1_label, CN1.label AS cn1_label_en, CN2.id AS cn2_id, CN2.code AS cn2_code, CN2.label' || _lang || ' AS cn2_label, CN2.label AS cn2_label_en, CN3.id AS cn3_id, CN3.code AS cn3_code, CN3.label' || _lang || ' AS cn3_label, CN3.label AS cn3_label_en, CN4.id AS cn4_id, CN4.code AS cn4_code, CN4.label' || _lang || ' AS cn4_label, CN4.label AS cn4_label_en, DR.id as dr_id' ||
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
  
  CREATE OR REPLACE FUNCTION "GetOlympicRankings"(_olympics text, _countries text, _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
	_c refcursor;
	_olympics_condition text;
	_country_condition text;
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SeqRequest"'), 'OL', 'CN-' || _olympics, current_date);

	-- Set olympics condition
	_olympics_condition := '';
	IF _olympics <> '0' THEN
		_olympics_condition := ' AND OL.id IN (' || _olympics || ')';
	END IF;
	
	-- Set country condition
	_country_condition := '';
	IF _countries <> '0' THEN
		_country_condition := ' AND CN1.id IN (' || _countries || ')';
	END IF;
	
	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT
		OR_.id AS or_id, CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label' || _lang || ' AS cn1_label, CN1.label AS cn1_label_en,
		OL.id AS ol_id, OL.type AS ol_type, OL.date1 AS ol_date1, OL.date2 AS ol_date2, YR.id AS yr_id, YR.label AS yr_label, CT.id AS ct_id, CT.label' || _lang || ' AS ct_label, CT.label AS ct_label_en,
		ST.id AS st_id, ST.code AS st_code, ST.label' || _lang || ' AS st_label, CN2.id AS cn2_id, CN2.code AS cn2_code, CN2.label' || _lang || ' AS cn2_label,
		OR_.count_gold AS or_count_gold, OR_.count_silver AS or_count_silver, OR_.count_bronze AS or_count_bronze
	FROM "OLYMPIC_RANKING" OR_
		LEFT JOIN "OLYMPICS" OL ON OR_.id_olympics = OL.id
		LEFT JOIN "COUNTRY" CN1 ON OR_.id_country = CN1.id
		LEFT JOIN "YEAR" YR ON OL.id_year = YR.id
		LEFT JOIN "CITY" CT ON OL.id_city = CT.id
		LEFT JOIN "STATE" ST ON CT.id_state = ST.id
		LEFT JOIN "COUNTRY" CN2 ON CT.id_country = CN2.id
	WHERE
		TRUE' || _olympics_condition || _country_condition || '
	ORDER BY
		OL.id DESC, OR_.count_gold DESC, OR_.count_silver DESC, OR_.count_bronze DESC';
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  CREATE OR REPLACE FUNCTION "GetOlympicMedals"(_olympics text, _id_sport integer, _events text, _subevents text, _subevents2 text, _lang character varying)
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
    _subevent2_condition text;
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SeqRequest"'), 'OL', 'IN-' || _id_sport, current_date);

	-- Build entity columns/joins
	_columns := '';
	_joins := '';
	FOR i IN 1..5 LOOP
		-- Person
	        _columns := _columns || ', PR' || i || '.last_name AS pr' || i || '_last_name, PR' || i || '.first_name AS pr' || i || '_first_name';
	        _columns := _columns || ', PRCN' || i || '.id AS pr' || i || '_cn_id, PRCN' || i || '.code AS pr' || i || '_cn_code, PRCN' || i || '.label' || _lang || ' AS pr' || i || '_cn_label, PRCN' || i || '.label AS pr' || i || '_cn_label_en';
	        _joins := _joins || ' LEFT JOIN "PERSON" PR' || i || ' ON RS.id_rank' || i || ' = PR' || i || '.id';
	        _joins := _joins || ' LEFT JOIN "COUNTRY" PRCN' || i || ' ON PR' || i || '.id_country = PRCN' || i || '.id';
		-- Country
	        _columns := _columns || ', _CN' || i || '.code AS cn' || i || '_code, _CN' || i || '.label' || _lang || ' AS cn' || i || '_label, _CN' || i || '.label AS cn' || i || '_label_en';
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

	-- Set subevent(2) condition
	_subevent2_condition := '';
	IF _subevents2 <> '0' THEN
		_subevent2_condition := ' AND RS.id_subevent2 IN (' || _subevents2 || ')';
	END IF;
		
	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT 
		RS.id AS rs_id, EV.id AS ev_id, EV.label' || _lang || ' AS ev_label, SE.id AS se_id, SE.label' || _lang || ' AS se_label, SE2.id AS se2_id, SE2.label' || _lang || ' AS se2_label, YR.id as yr_id, YR.label as yr_label, RS.date1 AS rs_date1, RS.date2 AS rs_date2,
		CX.id AS cx_id, CX.label' || _lang || ' AS cx_label, CX.label AS cx_label_en, CT1.id AS ct1_id, CT1.label' || _lang || ' AS ct1_label, CT1.label AS ct1_label_en, CT2.id AS ct2_id, CT2.label' || _lang || ' AS ct2_label, ST1.id AS st1_id, ST1.code AS st1_code, ST1.label' || _lang || ' AS st1_label, ST1.label AS st1_label_en, ST2.id AS st2_id, ST2.code AS st2_code,
		ST2.label' || _lang || ' AS st2_label, CN1.id AS cn1_id, CN1.code AS cn1_code_, CN1.label' || _lang || ' AS cn1_label_, CN1.label AS cn1_label_en_, CN2.id AS cn2_id, CN2.code AS cn2_code_, CN2.label' || _lang || ' AS cn2_label_,
		RS.id_rank1 AS rs_rank1, RS.id_rank2 AS rs_rank2, RS.id_rank3 AS rs_rank3, RS.id_rank4 AS rs_rank4, RS.id_rank5 AS rs_rank5,
		RS.result1 AS rs_result1, RS.result2 AS rs_result2, RS.result3 AS rs_result3, TP1.number AS tp1_number, TP2.number AS tp2_number, TP3.number AS tp3_number, OL.id AS ol_id, OL.type AS ol_type, OL.date1 AS ol_date1, OL.date2 AS ol_date2, CT3.label' || _lang || ' AS ol_city, CT3.label AS ol_city_en, RS.comment as rs_comment, RS.exa as rs_exa'
		|| _columns || '
	FROM
		"RESULT" RS
		LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id
		LEFT JOIN "EVENT" EV ON RS.id_event = EV.id
		LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id
		LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id
		LEFT JOIN "COMPLEX" CX ON RS.id_complex2 = CX.id
		LEFT JOIN "CITY" CT1 ON CX.id_city = CT1.id
		LEFT JOIN "CITY" CT2 ON RS.id_city2 = CT2.id
		LEFT JOIN "STATE" ST1 ON CT1.id_state = ST1.id
		LEFT JOIN "STATE" ST2 ON CT2.id_state = ST2.id
		LEFT JOIN "COUNTRY" CN1 ON CT1.id_country = CN1.id
		LEFT JOIN "COUNTRY" CN2 ON CT2.id_country = CN2.id
		LEFT JOIN "YEAR" YR ON RS.id_year = YR.id
		LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id
		LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id
		LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id
		LEFT JOIN "OLYMPICS" OL ON (RS.id_year = OL.id_year AND SP.type = OL.type)
		LEFT JOIN "CITY" CT3 ON OL.id_city = CT3.id'
		|| _joins || '
	WHERE 
		RS.id_championship = 1 AND RS.id_sport = ' || _id_sport
		|| _olympics_condition || _event_condition || _subevent_condition || _subevent2_condition || '
	ORDER BY OL.id DESC, EV.index, SE.index, EV.label' || _lang || ', SE.label' || _lang || ', SE2.label' || _lang;
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  CREATE OR REPLACE FUNCTION "GetHallOfFame"(_id_league integer, _years text, _position character varying)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _year_condition text;
    _position_condition text;
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SeqRequest"'), 'US', 'HF-' || _id_league, current_date);

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
  
  
  
  CREATE OR REPLACE FUNCTION "GetDraw"(_id_result integer, _lang character varying)
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
    _level varchar(3)[];
    _entity varchar(3)[];
    _entity_id varchar(15)[];
begin
	SELECT
		RS.id_sport, RS.id_championship, RS.id_event, RS.id_subevent, RS.id_subevent2
	INTO
		_id_sport, _id_championship, _id_event, _id_subevent, _id_subevent2
	FROM
		"RESULT" RS
	WHERE
		RS.id = _id_result;
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

	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SeqRequest"'), 'DR', _id_sport || '-' || _id_championship || '-' || _id_event, current_date);

	-- Build entity-specific columns/joins
	_level = '{qf1,qf1,qf2,qf2,qf3,qf3,qf4,qf4,sf1,sf1,sf2,sf2,f,f,thd,thd}';
	_entity = '{en1,en2,en1,en2,en1,en2,en1,en2,en1,en2,en1,en2,en1,en2,en1,en2}';
	_entity_id = '{DR.id1_qf1,DR.id2_qf1,DR.id1_qf2,DR.id2_qf2,DR.id1_qf3,DR.id2_qf3,DR.id1_qf4,DR.id2_qf4,DR.id1_sf1,DR.id2_sf1,DR.id1_sf2,DR.id2_sf2,RS.id_rank1,RS.id_rank2,DR.id1_thd,DR.id2_thd}';
	_columns := '';
	_joins := '';
	FOR i IN 1..16 LOOP
		IF _type < 10 THEN -- Athlete
			_columns := _columns || ', PR' || i || '.id AS ' || _entity[i] || '_' || _level[i] || '_id, PR' || i || '.last_name AS ' || _entity[i] || '_' || _level[i] || '_str1, PR' || i || '.first_name AS ' || _entity[i] || '_' || _level[i] || '_str2, NULL AS ' || _entity[i] || '_' || _level[i] || '_str3';
			_columns := _columns || ', PRTM' || i || '.id AS ' || _entity[i] || '_' || _level[i] || '_rel1_id, NULL AS ' || _entity[i] || '_' || _level[i] || '_rel1_code, PRTM' || i || '.label AS ' || _entity[i] || '_' || _level[i] || '_rel1_label, NULL AS ' || _entity[i] || '_' || _level[i] || '_rel1_label_en';
			_columns := _columns || ', PRCN' || i || '.id AS ' || _entity[i] || '_' || _level[i] || '_rel2_id, PRCN' || i || '.code AS ' || _entity[i] || '_' || _level[i] || '_rel2_code, PRCN' || i || '.label' || _lang || ' AS ' || _entity[i] || '_' || _level[i] || '_rel2_label, PRCN' || i || '.label AS ' || _entity[i] || '_' || _level[i] || '_rel2_label_en';
			_joins := _joins || ' LEFT JOIN "PERSON" PR' || i || ' ON ' || _entity_id[i] || ' = PR' || i || '.id';
			_joins := _joins || ' LEFT JOIN "TEAM" PRTM' || i || ' ON PR' || i || '.id_team = PRTM' || i || '.id';
			_joins := _joins || ' LEFT JOIN "COUNTRY" PRCN' || i || ' ON PR' || i || '.id_country = PRCN' || i || '.id';
		ELSIF _type = 50 THEN -- Team
			_columns := _columns || ', TM' || i || '.id AS ' || _entity[i] || '_' || _level[i] || '_id, NULL AS ' || _entity[i] || '_' || _level[i] || '_str1, TM' || i || '.label AS ' || _entity[i] || '_' || _level[i] || '_str2, NULL AS ' || _entity[i] || '_' || _level[i] || '_str3';
			_columns := _columns || ', NULL AS ' || _entity[i] || '_' || _level[i] || '_rel1_id, NULL AS ' || _entity[i] || '_' || _level[i] || '_rel1_code, NULL AS ' || _entity[i] || '_' || _level[i] || '_rel1_label, NULL AS ' || _entity[i] || '_' || _level[i] || '_rel1_label_en';
			_columns := _columns || ', TMCN' || i || '.id AS ' || _entity[i] || '_' || _level[i] || '_rel2_id, TMCN' || i || '.code AS ' || _entity[i] || '_' || _level[i] || '_rel2_code, TMCN' || i || '.label' || _lang || ' AS ' || _entity[i] || '_' || _level[i] || '_rel2_label, TMCN' || i || '.label AS ' || _entity[i] || '_' || _level[i] || '_rel2_label_en';
			_joins := _joins || ' LEFT JOIN "TEAM" TM' || i || ' ON ' || _entity_id[i] || ' = TM' || i || '.id';
			_joins := _joins || ' LEFT JOIN "COUNTRY" TMCN' || i || ' ON TM' || i || '.id_country = TMCN' || i || '.id';
		ELSIF _type = 99 THEN -- Country
			_columns := _columns || ', CN' || i || '.id AS ' || _entity[i] || '_' || _level[i] || '_id, CN' || i || '.code AS ' || _entity[i] || '_' || _level[i] || '_str1, CN' || i || '.label' || _lang || ' AS ' || _entity[i] || '_' || _level[i] || '_str2, CN' || i || '.label AS ' || _entity[i] || '_' || _level[i] || '_str3';
			_columns := _columns || ', NULL AS ' || _entity[i] || '_' || _level[i] || '_rel1_id, NULL AS ' || _entity[i] || '_' || _level[i] || '_rel1_code, NULL AS ' || _entity[i] || '_' || _level[i] || '_rel1_label, NULL AS ' || _entity[i] || '_' || _level[i] || '_rel1_label_en';
			_columns := _columns || ', NULL AS ' || _entity[i] || '_' || _level[i] || '_rel2_id, NULL AS ' || _entity[i] || '_' || _level[i] || '_rel2_code, NULL AS ' || _entity[i] || '_' || _level[i] || '_rel2_label, NULL AS ' || _entity[i] || '_' || _level[i] || '_rel2_label_en';
			_joins := _joins || ' LEFT JOIN "COUNTRY" CN' || i || ' ON ' || _entity_id[i] || ' = CN' || i || '.id';
		END IF;
	END LOOP;

	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT
		DR.id AS dr_id, ' || _type || ' AS dr_type, YR.label AS yr_label, DR.result_qf1 AS dr_result_qf1, DR.result_qf2 AS dr_result_qf2, DR.result_qf3 AS dr_result_qf3, DR.result_qf4 AS dr_result_qf4,
		DR.result_sf1 AS dr_result_sf1, DR.result_sf2 AS dr_result_sf2, RS.result1 AS rs_result_f, DR.result_thd AS dr_result_thd' ||
		_columns || '
	FROM
		"DRAW" DR
		LEFT JOIN "RESULT" RS ON DR.id_result = RS.id
		LEFT JOIN "YEAR" YR ON RS.id_year = YR.id' ||
		_joins || '
	WHERE
		DR.id_result = ' || _id_result;
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  CREATE OR REPLACE FUNCTION "EntityRef"(_entity character varying, _id integer, _entity_ref character varying, _limit character varying, _offset integer, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_c refcursor;
	__c refcursor;
	_query text;
	_link integer;
	_rs "RESULT"%rowtype;
	_ct_list varchar(200);
	_cx_list varchar(200);
	_pr_list varchar(200);
	_tm_list varchar(200);
	_index integer;
	_type1 integer;
	_type2 integer;
	_type3 integer;
	_array_id integer[];
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
	_id11 integer;_id12 integer;_id13 integer;_id14 integer;
	_cn1 varchar(5);_cn2 varchar(5);_cn3 varchar(5);_cn4 varchar(5);_cn5 varchar(5);_cn6 varchar(5);
	_tm1 varchar(60);_tm2 varchar(60);_tm3 varchar(60);_tm4 varchar(60);_tm5 varchar(60);_tm6 varchar(60);
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SeqRequest"'), 'IF', _entity || '-' || _id, current_date);
	
	_index := 1;

	IF _entity ~ 'CT' THEN
		SELECT LINK INTO _link FROM "CITY" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "CITY" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_ct_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_ct_list := _ct_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_ct_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'CX' THEN
		SELECT LINK INTO _link FROM "COMPLEX" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "COMPLEX" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_cx_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_cx_list := _cx_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_cx_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'PR' THEN
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
	ELSIF _entity ~ 'TM' THEN
		SELECT LINK INTO _link FROM "TEAM" WHERE ID = _id AND (YEAR1 IS NULL OR YEAR1 = '');
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "TEAM" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_tm_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_tm_list := _tm_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_tm_list := cast(_id AS varchar);
		END IF;
	END IF;

	-- References in: [Draws] (MUST STAY AT FIRST)
	IF (_entity ~ 'CN|PR|TM' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT DR.id, DR.id_result, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, DR.id1_qf1, DR.id2_qf1, DR.id1_qf2, DR.id2_qf2, DR.id1_qf3, DR.id2_qf3, DR.id1_qf4, DR.id2_qf4, DR.id1_sf1, DR.id2_sf1, DR.id1_sf2, DR.id2_sf2, DR.id1_thd, DR.id2_thd, TP1.number, TP2.number, TP3.number FROM "DRAW" DR';
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
		IF _entity = 'CN' THEN
			_query := _query || ' AND (DR.id1_sf1 <> ' || _id || ' AND DR.id1_sf2 <> ' || _id || ') AND (DR.id1_qf1 = ' || _id || ' OR DR.id2_qf1 = ' || _id || ' OR DR.id1_qf2 = ' || _id || ' OR DR.id2_qf2 = ' || _id || ' OR DR.id1_qf3 = ' || _id || ' OR DR.id2_qf3 = ' || _id || ' OR DR.id1_qf4 = ' || _id || ' OR DR.id2_qf4 = ' || _id || ' OR DR.id2_sf1 = ' || _id || ' OR DR.id2_sf2 = ' || _id || ' OR DR.id1_thd = ' || _id || ' OR DR.id2_thd = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _pr_list || ') AND DR.id1_sf2 NOT IN (' || _pr_list || ')) AND (DR.id1_qf1 IN (' || _pr_list || ') OR DR.id2_qf1 IN (' || _pr_list || ') OR DR.id1_qf2 IN (' || _pr_list || ') OR DR.id2_qf2 IN (' || _pr_list || ') OR DR.id1_qf3 IN (' || _pr_list || ') OR DR.id2_qf3 IN (' || _pr_list || ') OR DR.id1_qf4 IN (' || _pr_list || ') OR DR.id2_qf4 IN (' || _pr_list || ') OR DR.id2_sf1 IN (' || _pr_list || ') OR DR.id2_sf2 IN (' || _pr_list || ') OR DR.id1_thd IN (' || _pr_list || ') OR DR.id2_thd IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _tm_list || ') AND DR.id1_sf2 NOT IN (' || _tm_list || ')) AND (DR.id1_qf1 IN (' || _tm_list || ') OR DR.id2_qf1 IN (' || _tm_list || ') OR DR.id1_qf2 IN (' || _tm_list || ') OR DR.id2_qf2 IN (' || _tm_list || ') OR DR.id1_qf3 IN (' || _tm_list || ') OR DR.id2_qf3 IN (' || _tm_list || ') OR DR.id1_qf4 IN (' || _tm_list || ') OR DR.id2_qf4 IN (' || _tm_list || ') OR DR.id2_sf1 IN (' || _tm_list || ') OR DR.id2_sf2 IN (' || _tm_list || ') OR DR.id1_thd IN (' || _tm_list || ') OR DR.id2_thd IN (' || _tm_list || '))';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel10, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _id11, _id12, _id13, _id14, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF (_id13 = _id OR _id14 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)'))) THEN
				_item.txt2 := 'thd';
			ELSIF (_id9 = _id OR _id10 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)'))) THEN
				_item.txt2 := 'sf1';
			ELSIF (_id11 = _id OR _id12 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)'))) THEN
				_item.txt2 := 'sf2';
			ELSIF (_id1 = _id OR _id2 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)'))) THEN
				_item.txt2 := 'qf1';
			ELSIF (_id3 = _id OR _id4 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)'))) THEN
				_item.txt2 := 'qf2';
			ELSIF (_id5 = _id OR _id6 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)'))) THEN
				_item.txt2 := 'qf3';
			ELSIF (_id7 = _id OR _id8 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)'))) THEN
				_item.txt2 := 'qf4';
			END IF;
			IF _type1 <= 10 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN '', '' || PR1.first_name ELSE '''' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || '' '' ELSE '''' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN '', '' || PR2.first_name ELSE '''' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || '' '' ELSE '''' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, DR.result_' || _item.txt2;
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "PERSON" PR1 ON DR.id1_' || _item.txt2 || ' = PR1.id LEFT JOIN "PERSON" PR2 ON DR.id2_' || _item.txt2 || ' = PR2.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel8, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel9, _item.id_rel13, _cn2, _tm2, _item.txt1;
				CLOSE __c;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				_item.comment := 'PR';
			ELSIF _type1 = 50 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', TM1.label, TM2.label, NULL, NULL, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "TEAM" TM1 ON DR.id1_' || _item.txt2 || ' = TM1.id LEFT JOIN "TEAM" TM2 ON DR.id2_' || _item.txt2 || ' = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'TM';
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', CN1.label' || _lang || ', CN2.label' || _lang || ', CN1.label, CN2.label, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "COUNTRY" CN1 ON DR.id1_' || _item.txt2 || ' = CN1.id LEFT JOIN "COUNTRY" CN2 ON DR.id2_' || _item.txt2 || ' = CN2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'CN';
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
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number, TP3.number';
		IF (_entity = 'PR') THEN
			_query := _query || ', PL.rank';
		ELSE
			_query := _query || ', 0';
		END IF;
		_query := _query || ' FROM "RESULT" RS';
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
		ELSIF (_entity = 'PR') THEN
			_type1 = 1;
			_type2 = 99;
			_query := _query || ' LEFT JOIN "~PERSON_LIST" PL ON PL.id_result = RS.id';
		END IF;
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RS.id_rank1 = ' || _id || ' OR RS.id_rank2 = ' || _id || ' OR RS.id_rank3 = ' || _id || ' OR RS.id_rank4 = ' || _id || ' OR RS.id_rank5 = ' || _id || ' OR RS.id_rank6 = ' || _id || ' OR RS.id_rank7 = ' || _id || ' OR RS.id_rank8 = ' || _id || ' OR RS.id_rank9 = ' || _id || ' OR RS.id_rank10 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (PL.id_person IN (' || _pr_list || ') OR (((TP1.number BETWEEN 1 AND 10 AND TP2.number IS NULL) OR (TP2.number BETWEEN 1 AND 10 AND TP3.number IS NULL) OR (TP3.number BETWEEN 1 AND 10)) AND ((RS.id_rank1 IN (' || _pr_list || ') OR RS.id_rank2 IN (' || _pr_list || ') OR RS.id_rank3 IN (' || _pr_list || ') OR RS.id_rank4 IN (' || _pr_list || ') OR RS.id_rank5 IN (' || _pr_list || ') OR RS.id_rank6 IN (' || _pr_list || ') OR RS.id_rank7 IN (' || _pr_list || ') OR RS.id_rank8 IN (' || _pr_list || ') OR RS.id_rank9 IN (' || _pr_list || ') OR RS.id_rank10 IN (' || _pr_list || ')))))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RS.id_rank1 IN (' || _tm_list || ') OR RS.id_rank2 IN (' || _tm_list || ') OR RS.id_rank3 IN (' || _tm_list || ') OR RS.id_rank4 IN (' || _tm_list || ') OR RS.id_rank5 IN (' || _tm_list || ') OR RS.id_rank6 IN (' || _tm_list || ') OR RS.id_rank7 IN (' || _tm_list || ') OR RS.id_rank8 IN (' || _tm_list || ') OR RS.id_rank9 IN (' || _tm_list || ') OR RS.id_rank10 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND  (RS.id_city1 IN (' || _ct_list || ') OR RS.id_city2 IN (' || _ct_list || '))';
		ELSIF _entity = 'CX' THEN
			_query := _query || ' AND  (RS.id_complex1 IN (' || _cx_list || ') OR RS.id_complex2 IN (' || _cx_list || '))';
		ELSIF _entity = 'OL' THEN
			_query := _query || ' AND RS.id_championship=1 AND OL.id = ' || _id;
		ELSIF _entity = 'YR' THEN
			_query := _query || ' AND RS.id_year = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2, _type3, _item.count1;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF _type1 <= 10 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, CN3.code, TM3.label, PR4.last_name || (CASE WHEN length(PR4.first_name) > 0 THEN ', ' || PR4.first_name ELSE '' END), (CASE WHEN length(PR4.first_name) > 0 THEN PR4.first_name || ' ' ELSE '' END) || PR4.last_name, CN4.id, CN4.code, TM4.label, PR5.last_name || (CASE WHEN length(PR5.first_name) > 0 THEN ', ' || PR5.first_name ELSE '' END), (CASE WHEN length(PR5.first_name) > 0 THEN PR5.first_name || ' ' ELSE '' END) || PR5.last_name, CN5.id, CN5.code, TM5.label, PR6.last_name || (CASE WHEN length(PR6.first_name) > 0 THEN ', ' || PR6.first_name ELSE '' END), (CASE WHEN length(PR6.first_name) > 0 THEN PR6.first_name || ' ' ELSE '' END) || PR6.last_name, CN6.id, CN6.code, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.label_rel23, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.label_rel24, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.label_rel25, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt3, _item.txt4
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
				IF _type1 = 4 OR _item.txt3 = '#DOUBLE#' THEN
					_item.txt4 = '1-2/3-4/5-6';
				ELSIF _type1 = 5 OR _item.txt3 = '#TRIPLE#' THEN
					_item.txt4 = '1-3/4-6/7-9';
				END IF;
				_item.comment := 'PR';
				_array_id := string_to_array(_pr_list, ',')::integer[];
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, NULL, NULL, NULL, NULL, NULL, NULL, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "TEAM" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "TEAM" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "TEAM" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "TEAM" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "TEAM" TM5 ON RS.id_rank5 = TM5.id LEFT JOIN "TEAM" TM6 ON RS.id_rank6 = TM6.id
				WHERE RS.id = _item.id_item;
				_item.comment := 'TM';
				_array_id := string_to_array(_tm_list, ',')::integer[];
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN4.label' || _lang || ', CN5.label' || _lang || ', CN6.label' || _lang || ', CN1.label, CN2.label, CN3.label, CN4.label, CN5.label, CN6.label, RS.result1, RS.result2, RS.comment, RS.exa';
				_query := _query || ' FROM "RESULT" RS LEFT JOIN "COUNTRY" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "COUNTRY" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "COUNTRY" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "COUNTRY" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "COUNTRY" CN5 ON RS.id_rank5 = CN5.id LEFT JOIN "COUNTRY" CN6 ON RS.id_rank6 = CN6.id';
				_query := _query || ' WHERE RS.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4;
				CLOSE __c;
				_item.comment := 'CN';
				_array_id := ARRAY[_id];
			END IF;
			SELECT * INTO _rs FROM "RESULT" RS WHERE RS.id = _item.id_item;
			SELECT "GetRank"(_rs, _array_id) INTO _item.count1;
			_item.id = _index;
			_item.entity = 'RS';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Athletes]
	IF (_entity ~ 'CN|SP|TM' AND (_entity_ref = 'PR' OR _entity_ref = '')) THEN
		_query := 'SELECT DISTINCT ON (PR.last_name, PR.first_name, CN.id, SP.id) PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "PERSON" PR';
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
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT TM.id, TM.label, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "TEAM" TM';
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
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT CT.id, CT.label' || _lang || ', CT.label, CN.id, CN.label' || _lang || ', CN.label FROM "CITY" CT';
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
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.label_rel2;
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
		_query := 'SELECT CX.id, CX.label' || _lang || ', CX.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label FROM "COMPLEX" CX';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' WHERE CX.id_city = ' || _id;
		_query := _query || ' ORDER BY CX.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OL.type FROM "OLYMPICS" OL';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query := _query || ' WHERE OL.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY OL.type, YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT OR_.id, OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OR_.count_gold || '','' || OR_.count_silver || '','' || OR_.count_bronze FROM "OLYMPIC_RANKING" OR_';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT RC.id, RC.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, RC.type1, RC.type2, RC.record1, RC.id_rank1, RC.id_rank2, RC.id_rank3, RC.id_rank4, RC.id_rank5 FROM "RECORD" RC';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RC.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RC.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RC.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "TYPE" TP ON EV.id_type = TP.id';
		_query := _query || ' WHERE TP.number BETWEEN ' || _type1 || ' AND ' || _type2;
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RC.id_rank1 = ' || _id || ' OR RC.id_rank2 = ' || _id || ' OR RC.id_rank3 = ' || _id || ' OR RC.id_rank4 = ' || _id || ' OR RC.id_rank5 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _pr_list || ') OR RC.id_rank2 IN (' || _pr_list || ') OR RC.id_rank3 IN (' || _pr_list || ') OR RC.id_rank4 IN (' || _pr_list || ') OR RC.id_rank5 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _tm_list || ') OR RC.id_rank2 IN (' || _tm_list || ') OR RC.id_rank3 IN (' || _tm_list || ') OR RC.id_rank4 IN (' || _tm_list || ') OR RC.id_rank5 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RC.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RC.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RC.id_event = ' || _id || ' OR RC.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND RC.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', RC.index LIMIT ' || _limit || ' OFFSET ' || _offset;
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
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Hall of Fame]
	IF (_entity ~ 'YR|PR' AND (_entity_ref = 'HF' OR _entity_ref = '')) THEN
		_query := 'SELECT HF.id, YR.id, YR.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, HF.position FROM "HALL_OF_FAME" HF';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.txt1;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'HF';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Retired Numbers]
	IF (_entity ~ 'PR' AND (_entity_ref = 'RN' OR _entity_ref = '')) THEN
		_query := 'SELECT RN.id, TM.id, TM.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, RN.number FROM "RETIRED_NUMBER" RN';
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
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.id_rel4;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'RN';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Team Stadiums]
	IF (_entity ~ 'CX' AND (_entity_ref = 'TS' OR _entity_ref = '')) THEN
		_query := 'SELECT TS.id, TM.id, TM.label, CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', CX.label, CT.label, ST.label, CN.label, LG.id, LG.label, TS.date1, TS.date2 FROM "TEAM_STADIUM" TS';
		_query := _query || ' LEFT JOIN "TEAM" TM ON TS.id_team = TM.id';
		_query := _query || ' LEFT JOIN "COMPLEX" CX ON TS.id_complex = CX.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON TS.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE TS.id_team = ' || _id;
		ELSIF _entity = 'CX' THEN
			_query := _query || ' WHERE TS.id_complex IN (' || _cx_list || ')';
		END IF;
		_query := _query || ' ORDER BY TM.label, TS.date1 DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.id_rel6, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'TS';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Contributions]
	IF (_entity ~ 'CB' AND (_entity_ref = 'CO' OR _entity_ref = '')) THEN
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, CO.type, CO.date';
		_query := _query || ' FROM "~CONTRIBUTION" CO';
		_query := _query || ' LEFT JOIN "RESULT" RS ON CO.id_item = RS.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' WHERE RS.id_contributor=' || _id || ' ORDER BY CO.date DESC, YR.id DESC, SP.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.date1;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CO';
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
  
  
  
  CREATE OR REPLACE FUNCTION "GetMedalCount"(_entity character varying, _id_sport integer, _idlist character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_ids integer[];
	_type1 integer;
	_type2 integer;
	_sport_txt varchar(3);
	_index smallint;
	_nfl_cp1 smallint;
	_nfl_cp2 smallint;
	_nba_cp smallint;
	_nhl_cp smallint;
	_mlb_cp smallint;
	_count1 integer;
	_count2 integer;
	_count3 integer;
	_label_tennis_slam1 varchar(45);
	_label_tennis_slam2 varchar(45);
	_label_tennis_slam3 varchar(45);
	_label_tennis_slam4 varchar(45);
	_label_golf_slam1 varchar(45);
	_label_golf_slam2 varchar(45);
	_label_golf_slam3 varchar(45);
	_label_golf_slam4 varchar(45);
	_label_cycling_tour1 varchar(45);
	_label_cycling_tour2 varchar(45);
	_label_cycling_tour3 varchar(45);
begin
	_ids := regexp_split_to_array(_idlist, E'\\-');	
	_label_tennis_slam1 := 'Australian Open';
	_label_tennis_slam2 := 'French Open';
	_label_tennis_slam3 := 'Wimbledon';
	_label_tennis_slam4 := 'US Open';
	_label_golf_slam1 := 'Masters';
	_label_golf_slam2 := 'US Open';
	_label_golf_slam3 := 'Open Championship';
	_label_golf_slam4 := 'PGA Championship';
	_label_cycling_tour1 := 'Giro d''Italia';
	_label_cycling_tour2 := 'Tour de France';
	_label_cycling_tour3 := 'Vuelta a España';
	_nfl_cp1 := 454;
	_nfl_cp2 := 453;
	_nba_cp := 530;
	_nhl_cp := 573;
	_mlb_cp := 624;
	_index := 1;
	_sport_txt := CAST(_id_sport AS varchar);
	IF (_entity = 'PR') THEN
		_type1 := 1;
		_type2 := 10;
	ELSIF (_entity = 'TM') THEN
		_type1 := 50;
		_type2 := 50;
	ELSIF (_entity = 'CN') THEN
		_type1 := 99;
		_type2 := 99;
	END IF;
	-- Olympics
	IF _type2 <> 50 THEN
		_item.id := _index;
		_item.label := 'OLYMP';
		_item.txt1 := '#GOLD#';
		_item.txt2 := '#SILVER#';
		_item.txt3 := '#BRONZE#';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, _ids) = 1;
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, _ids) = 2;
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, _ids) = 3;
		IF _type1 = 99 THEN
			SELECT COUNT(*) INTO _count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id LEFT JOIN "PERSON" PR1 ON RS.id_rank1=PR1.id LEFT JOIN "PERSON" PR2 ON RS.id_rank2=PR2.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3=PR3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=10) OR (id_subevent IS NOT NULL AND T2.number<=10) OR (id_subevent IS NULL AND T1.number<=10))  AND (PR1.id_country = ANY(_ids) OR (PR2.id_country = ANY(_ids) AND (exa ~* '.*1-(2|3|4|5|6).*')) OR (PR3.id_country = ANY(_ids) AND (exa ~* '.*1-(3|4|5|6).*')));
			SELECT COUNT(*) INTO _count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id LEFT JOIN "PERSON" PR2 ON RS.id_rank2=PR2.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3=PR3.id LEFT JOIN "PERSON" PR4 ON RS.id_rank4=PR4.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=10) OR (id_subevent IS NOT NULL AND T2.number<=10) OR (id_subevent IS NULL AND T1.number<=10))  AND (PR2.id_country = ANY(_ids) OR (PR3.id_country = ANY(_ids) AND (exa ~* '.*2-(3|4|5|6).*')) OR (PR4.id_country = ANY(_ids) AND (exa ~* '.*2-(4|5|6).*')));
			SELECT COUNT(*) INTO _count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3=PR3.id LEFT JOIN "PERSON" PR4 ON RS.id_rank4=PR4.id LEFT JOIN "PERSON" PR5 ON RS.id_rank5=PR5.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=10) OR (id_subevent IS NOT NULL AND T2.number<=10) OR (id_subevent IS NULL AND T1.number<=10))  AND (PR3.id_country = ANY(_ids) OR (PR4.id_country = ANY(_ids) AND (exa ~* '.*3-(4|5|6).*')) OR (PR5.id_country = ANY(_ids) AND (exa ~* '.*3-(5|6).*')));
			_item.count1 := _item.count1 + _count1;
			_item.count2 := _item.count2 + _count2;
			_item.count3 := _item.count3 + _count3;
		ELSE
			SELECT COUNT(*) INTO _count1 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE id_championship=1 AND PL.rank=1 AND id_person = ANY(_ids);
			SELECT COUNT(*) INTO _count2 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE id_championship=1 AND PL.rank=2 AND id_person = ANY(_ids);
			SELECT COUNT(*) INTO _count3 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE id_championship=1 AND PL.rank=3 AND id_person = ANY(_ids);
			_item.count1 := _item.count1 + _count1;
			_item.count2 := _item.count2 + _count2;
			_item.count3 := _item.count3 + _count3;
		END IF;
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- World Championships
	IF _type2 <= 10 THEN
		_item.id := _index;	
		_item.label := 'WORLDCP';
		IF _sport_txt ~ '5|18|33|36' THEN
			_item.txt1 := 'rank.1';
			_item.txt2 := 'rank.2';
			_item.txt3 := 'rank.3';
		ELSE
			_item.txt1 := '#GOLD#';
			_item.txt2 := '#SILVER#';
			_item.txt3 := '#BRONZE#';
		END IF;
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, _ids) = 1;
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, _ids) = 2;
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, _ids) = 3;
		SELECT COUNT(*) INTO _count1 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND PL.rank=1 AND id_person = ANY(_ids);
		SELECT COUNT(*) INTO _count2 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND PL.rank=2 AND id_person = ANY(_ids);
		SELECT COUNT(*) INTO _count3 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND PL.rank=3 AND id_person = ANY(_ids);
		_item.count1 := _item.count1 + _count1;
		_item.count2 := _item.count2 + _count2;
		_item.count3 := _item.count3 + _count3;
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- European Championships
	IF _type2 <= 10 THEN
		_item.id := _index;
		_item.label := 'EURCP';
		_item.txt1 := '#GOLD#';
		_item.txt2 := '#SILVER#';
		_item.txt3 := '#BRONZE#';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship = 4 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, _ids) = 1;
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship = 4 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, _ids) = 2;
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship = 4 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND "GetRank"(RS.*, _ids) = 3;
		SELECT COUNT(*) INTO _count1 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE id_championship = 4 AND PL.rank=1 AND id_person = ANY(_ids);
		SELECT COUNT(*) INTO _count2 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE id_championship = 4 AND PL.rank=2 AND id_person = ANY(_ids);
		SELECT COUNT(*) INTO _count3 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE id_championship = 4 AND PL.rank=3 AND id_person = ANY(_ids);
		_item.count1 := _item.count1 + _count1;
		_item.count2 := _item.count2 + _count2;
		_item.count3 := _item.count3 + _count3;
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Grand Slam (Tennis)
	IF _id_sport = 22 AND _type2 <= 10 THEN
		_item.id := _index;
		_item.label := 'GSLAM';
		_item.txt1 := 'Aus';
		_item.txt2 := 'RG';
		_item.txt3 := 'Wim';
		_item.txt4 := 'US';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam1 OR SE2.label=_label_tennis_slam1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam2 OR SE2.label=_label_tennis_slam2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam3 OR SE2.label=_label_tennis_slam3) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count4 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam4 OR SE2.label=_label_tennis_slam4) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Majors (Golf)
	IF _id_sport = 20 AND _type2 <= 10 THEN
		_item.id := _index;
		_item.label := 'MAJORS';
		_item.txt1 := 'Mas';
		_item.txt2 := 'US';
		_item.txt3 := 'Brit';
		_item.txt4 := 'PGA';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam1 OR SE2.label=_label_golf_slam1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam2 OR SE2.label=_label_golf_slam2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam3 OR SE2.label=_label_golf_slam3) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count4 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam4 OR SE2.label=_label_golf_slam4) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Major Tours (Cycling)
	IF _id_sport = 19 AND _type2 <= 10 THEN
		_item.id := _index;
		_item.label := 'MTOURS';
		_item.txt1 := 'Gir';
		_item.txt2 := 'Tour';
		_item.txt3 := 'Vuel';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (EV.label=_label_cycling_tour1 OR SE.label=_label_cycling_tour1 OR SE2.label=_label_cycling_tour1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (EV.label=_label_cycling_tour2 OR SE.label=_label_cycling_tour2 OR SE2.label=_label_cycling_tour2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (EV.label=_label_cycling_tour3 OR SE.label=_label_cycling_tour3 OR SE2.label=_label_cycling_tour3) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- NFL Titles
	IF _id_sport = 23 AND _type2 = 50 THEN
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
	IF _id_sport = 24 AND _type2 = 50 THEN
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
	IF _id_sport = 25 AND _type2 = 50 THEN
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
	IF _id_sport = 26 AND _type2 = 50 THEN
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
  
  
  CREATE OR REPLACE FUNCTION "GetRank"(_rs "RESULT", _ids integer[])
  RETURNS smallint AS
$BODY$
DECLARE
	_type1 smallint;
	_type2 smallint;
	_type3 smallint;
BEGIN
	-- Get result type
	SELECT DISTINCT TP1.number, TP2.number, TP3.number
	INTO _type1, _type2, _type3
	FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id
	WHERE RS.id = _rs.id;
	IF _type3 IS NOT NULL THEN
		_type1 := _type3;
	ELSIF _type2 IS NOT NULL THEN
		_type1 := _type2;
	END IF;

	-- Calculate rank
	IF _rs.id_rank1 = ANY(_ids) OR
	  (_rs.id_rank2 = ANY(_ids) AND (_type1 IN (4, 5) OR _rs.comment IN ('#DOUBLE#', '#TRIPLE#') OR _rs.exa ~* '(^|/)1-(2|3|4|5|6|7|8|9)(/|$)')) OR
	  (_rs.id_rank3 = ANY(_ids) AND (_type1 = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)1-(3|4|5|6|7|8|9)(/|$)')) OR
	  (_rs.id_rank4 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(4|5|6|7|8|9)(/|$)') OR
	  (_rs.id_rank5 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(5|6|7|8|9)(/|$)') OR
	  (_rs.id_rank6 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(6|7|8|9)(/|$)') OR
	  (_rs.id_rank7 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(7|8|9)(/|$)') OR
	  (_rs.id_rank8 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(8|9)(/|$)') OR
	  (_rs.id_rank9 = ANY(_ids) AND _rs.exa ~* '(^|/)1-9(/|$)') THEN
		RETURN 1;
	ELSIF (_rs.id_rank2 = ANY(_ids) AND _type1 NOT IN (4, 5) AND (_rs.comment IS NULL OR _rs.comment NOT IN ('#DOUBLE#', '#TRIPLE#'))) OR
	  (_rs.id_rank3 = ANY(_ids) AND ((_type1 = 4 OR _rs.comment = '#DOUBLE#' OR _rs.exa ~* '(^|/)2-(3|4|5|6|7|8|9)(/|$)') AND _type1 <> 5 AND (_rs.comment IS NULL OR _rs.comment <> '#TRIPLE#'))) OR
	  (_rs.id_rank4 = ANY(_ids) AND ((_type1 = 4 OR _rs.comment = '#DOUBLE#' OR _rs.exa ~* '(^|/)2-(4|5|6|7|8|9)(/|$)') AND _type1 <> 5 AND (_rs.comment IS NULL OR _rs.comment <> '#TRIPLE#'))) OR 
	  (_rs.id_rank5 = ANY(_ids) AND (_type1 = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)2-(5|6|7|8|9)(/|$)')) OR
	  (_rs.id_rank6 = ANY(_ids) AND (_type1 = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)2-(6|7|8|9)(/|$)')) OR
	  (_rs.id_rank7 = ANY(_ids) AND _rs.exa ~* '(^|/)2-(7|8|9)(/|$)') OR
	  (_rs.id_rank8 = ANY(_ids) AND _rs.exa ~* '(^|/)2-(8|9)(/|$)') OR
	  (_rs.id_rank9 = ANY(_ids) AND _rs.exa ~* '(^|/)2-9(/|$)') THEN
		RETURN 2;
	ELSIF (_rs.id_rank3 = ANY(_ids) AND _type1 NOT IN (4, 5) AND (_rs.comment IS NULL OR _rs.comment NOT IN ('#DOUBLE#', '#TRIPLE#'))) OR
	  (_rs.id_rank4 = ANY(_ids) AND (_type1 NOT IN (4, 5) AND (_rs.comment IS NULL OR _rs.comment NOT IN ('#DOUBLE#', '#TRIPLE#')) AND _rs.exa ~* '(^|/)3-(4|5|6|7|8|9)(/|$)')) OR
	  (_rs.id_rank5 = ANY(_ids) AND (_type1 <> 5 AND (_rs.comment IS NULL OR _rs.comment <> '#TRIPLE#') AND _rs.exa ~* '(^|/)3-(5|6|7|8|9)(/|$)')) OR
	  (_rs.id_rank6 = ANY(_ids) AND (_type1 <> 5 AND (_rs.comment IS NULL OR _rs.comment <> '#TRIPLE#') AND _rs.exa ~* '(^|/)3-(6|7|8|9)(/|$)')) OR
	  (_rs.id_rank7 = ANY(_ids) AND (_type1 = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)3-(7|8|9)(/|$)')) OR
	  (_rs.id_rank8 = ANY(_ids) AND (_type1 = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)3-(8|9)(/|$)')) OR
	  (_rs.id_rank9 = ANY(_ids) AND (_type1 = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)3-9(/|$)')) THEN
		RETURN 3;
	END IF;
	RETURN 0;
END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  
  
  
  
  ALTER TABLE "RESULT" DISABLE TRIGGER trigger_rs;
update "RESULT" set comment = replace(comment, ' kg', 'kg') where comment like '% kg%';
ALTER TABLE "RESULT" ENABLE TRIGGER trigger_rs;

  
  
  
  