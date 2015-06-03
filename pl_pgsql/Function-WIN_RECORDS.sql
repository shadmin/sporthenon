-- Function: "WIN_RECORDS"(text, character varying)

-- DROP FUNCTION "WIN_RECORDS"(text, character varying);

CREATE OR REPLACE FUNCTION "WIN_RECORDS"(_results text, _lang character varying)
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
	_type3 smallint;
	_exa text;
	_win_str text;
	_query text;
	_c refcursor;
begin
	SELECT DISTINCT
		TP1.number, TP2.number
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
		IF (_id2 IS NOT NULL AND (_type = 4 OR _exa ~ '(^|/)1.*(/|$)')) THEN
			IF _win_str !~ ('(^|,)' || _id2 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id2, 0, NULL); _win_str = _win_str || ',' || _id2; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id2;
		END IF;
		IF (_id3 IS NOT NULL AND _exa ~ '(^|/)1-(3|4|5|6|7|8)(/|$)') THEN
			IF _win_str !~ ('(^|,)' || _id3 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id3, 0, NULL); _win_str = _win_str || ',' || _id3; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id3;
		END IF;
		IF (_id4 IS NOT NULL AND _exa ~ '(^|/)1-(4|5|6|7|8)(/|$)') THEN
			IF _win_str !~ ('(^|,)' || _id4 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id4, 0, NULL); _win_str = _win_str || ',' || _id4; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id4;
		END IF;
		IF (_id5 IS NOT NULL AND _exa ~ '(^|/)1-(5|6|7|8)(/|$)') THEN
			IF _win_str !~ ('(^|,)' || _id5 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id5, 0, NULL); _win_str = _win_str || ',' || _id5; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id5;
		END IF;
		IF (_id6 IS NOT NULL AND _exa ~ '(^|/)1-(6|7|8)(/|$)') THEN
			IF _win_str !~ ('(^|,)' || _id6 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id6, 0, NULL); _win_str = _win_str || ',' || _id6; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id6;
		END IF;
		IF (_id7 IS NOT NULL AND _exa ~ '(^|/)1-(7|8)(/|$)') THEN
			IF _win_str !~ ('(^|,)' || _id7 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id7, 0, NULL); _win_str = _win_str || ',' || _id7; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id7;
		END IF;
		IF (_id8 IS NOT NULL AND _exa ~ '(^|/)1-8(/|$)') THEN
			IF _win_str !~ ('(^|,)' || _id8 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id8, 0, NULL); _win_str = _win_str || ',' || _id8; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id8;
		END IF;
	END LOOP;
	CLOSE _c;

	_query = 'SELECT FB.id AS entity_id, ' || _type || ' AS entity_type, FB.int_value AS count_win';
	IF (_type < 10) THEN
		_query = _query || ', (CASE WHEN PR.first_name IS NOT NULL AND PR.first_name <> '''' THEN PR.first_name || '' '' ELSE '''' END) || UPPER(PR.last_name) AS entity_str, (CASE WHEN PR.first_name IS NOT NULL AND PR.first_name <> '''' THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name AS entity_str_en';
		_query = _query || ' FROM "~FUNC_BUFFER" FB LEFT JOIN "PERSON" PR ON FB.id = PR.id';
	ELSIF (_type = 50) THEN
		_query = _query || ', TM.label AS entity_str, TM.label AS entity_str_en';
		_query = _query || ' FROM "~FUNC_BUFFER" FB LEFT JOIN "TEAM" TM ON FB.id = TM.id';
	ELSIF (_type = 99) THEN
		_query = _query || ', CN.label' || _lang || ' AS entity_str, CN.label AS entity_str_en';
		_query = _query || ' FROM "~FUNC_BUFFER" FB LEFT JOIN "COUNTRY" CN ON FB.id = CN.id';
	END IF;
	_query = _query || ' ORDER BY FB.int_value DESC, entity_str ASC';
	OPEN _c FOR EXECUTE _query;
	RETURN  _c;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
