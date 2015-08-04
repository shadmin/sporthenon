-- Function: "WinRecords"(text, character varying)

-- DROP FUNCTION "WinRecords"(text, character varying);

CREATE OR REPLACE FUNCTION "WinRecords"(_results text, _lang character varying)
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
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;