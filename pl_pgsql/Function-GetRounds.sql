-- Function: "GetRounds"(integer, character varying)

-- DROP FUNCTION "GetRounds"(integer, character varying);

CREATE OR REPLACE FUNCTION "GetRounds"(
    _id_result integer,
    _lang character varying)
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
begin
	SELECT RS.id_sport, RS.id_championship, RS.id_event, RS.id_subevent, RS.id_subevent2 INTO _id_sport, _id_championship, _id_event, _id_subevent, _id_subevent2 FROM "Result" RS WHERE RS.id = _id_result;

	SELECT id_result_type INTO _type FROM "Round" WHERE id_result = _id_result;

	-- Build entity-specific columns/joins
	_columns := '';
	_joins := '';
	FOR i IN 1..5 LOOP
		IF _type < 10 THEN -- Athlete
			_columns := _columns || ', PR' || i || '.id AS rk' || i || '_id, PR' || i || '.last_name AS rk' || i || '_str1, PR' || i || '.first_name AS rk' || i || '_str2, NULL AS rk' || i || '_str3';
			_columns := _columns || ', PRTM' || i || '.id AS rk' || i || '_rel1_id, NULL AS rk' || i || '_rel1_code, PRTM' || i || '.label AS rk' || i || '_rel1_label, NULL AS rk' || i || '_rel1_label_en';
			_columns := _columns || ', PRCN' || i || '.id AS rk' || i || '_rel2_id, PRCN' || i || '.code AS rk' || i || '_rel2_code, PRCN' || i || '.label' || _lang || ' AS rk' || i || '_rel2_label, PRCN' || i || '.label AS rk' || i || '_rel2_label_en';
			_joins := _joins || ' LEFT JOIN "Athlete" PR' || i || ' ON RD.id_rank' || i || ' = PR' || i || '.id';
			_joins := _joins || ' LEFT JOIN "Team" PRTM' || i || ' ON PR' || i || '.id_team = PRTM' || i || '.id';
			_joins := _joins || ' LEFT JOIN "Country" PRCN' || i || ' ON PR' || i || '.id_country = PRCN' || i || '.id';
		ELSIF _type = 50 THEN -- Team
			_columns := _columns || ', TM' || i || '.id AS rk' || i || '_id, NULL AS rk' || i || '_str1, TM' || i || '.label AS rk' || i || '_str2, NULL AS rk' || i || '_str3';
			_columns := _columns || ', NULL AS rk' || i || '_rel1_id, NULL AS rk' || i || '_rel1_code, NULL AS rk' || i || '_rel1_label, NULL AS rk' || i || '_rel1_label_en';
			_columns := _columns || ', TMCN' || i || '.id AS rk' || i || '_rel2_id, TMCN' || i || '.code AS rk' || i || '_rel2_code, TMCN' || i || '.label' || _lang || ' AS rk' || i || '_rel2_label, TMCN' || i || '.label AS rk' || i || '_rel2_label_en';
			_joins := _joins || ' LEFT JOIN "Team" TM' || i || ' ON RD.id_rank' || i || ' = TM' || i || '.id';
			_joins := _joins || ' LEFT JOIN "Country" TMCN' || i || ' ON TM' || i || '.id_country = TMCN' || i || '.id';
		ELSIF _type = 99 THEN -- Country
			_columns := _columns || ', ENCN' || i || '.id AS rk' || i || '_id, ENCN' || i || '.code AS rk' || i || '_str1, ENCN' || i || '.label' || _lang || ' AS rk' || i || '_str2, ENCN' || i || '.label AS rk' || i || '_str3';
			_columns := _columns || ', NULL AS rk' || i || '_rel1_id, NULL AS rk' || i || '_rel1_code, NULL AS rk' || i || '_rel1_label, NULL AS rk' || i || '_rel1_label_en';
			_columns := _columns || ', NULL AS rk' || i || '_rel2_id, NULL AS rk' || i || '_rel2_code, NULL AS rk' || i || '_rel2_label, NULL AS rk' || i || '_rel2_label_en';
			_joins := _joins || ' LEFT JOIN "Country" ENCN' || i || ' ON RD.id_rank' || i || ' = ENCN' || i || '.id';
		END IF;
	END LOOP;

	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT
		RD.id AS rd_id, RD.id_result_type AS rd_result_type, RT.id AS rt_id, RT.label' || _lang || ' AS rt_label, RT.index AS rt_index, RD.result1 AS rd_result1, RD.result2 AS rd_result2, RD.result3 AS rd_result3, RD.result4 AS rd_result4, RD.result5 AS rd_result5, RD.date1 AS rd_date1, RD.date AS rd_date2, RD.exa AS rd_exa, RD.comment AS rd_comment,
		CX1.id AS cx1_id, CX1.label AS cx1_label, CX2.id AS cx2_id, CX2.label AS cx2_label,
		CT1.id AS ct1_id, CT1.label' || _lang || ' AS ct1_label, CT1.label AS ct1_label_en, ST1.id AS st1_id, ST1.code AS st1_code, ST1.label AS st1_label_en, CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label AS cn1_label_en,
		CT2.id AS ct2_id, CT2.label' || _lang || ' AS ct2_label, CT2.label AS ct2_label_en, ST2.id AS st2_id, ST2.code AS st2_code, ST2.label AS st2_label_en, CN2.id AS cn2_id, CN2.code AS cn2_code, CN2.label AS cn2_label_en,
		CT3.id AS ct3_id, CT3.label' || _lang || ' AS ct3_label, CT3.label AS ct3_label_en, ST3.id AS st3_id, ST3.code AS st3_code, ST3.label AS st3_label_en, CN3.id AS cn3_id, CN3.code AS cn3_code, CN3.label AS cn3_label_en,
		CT4.id AS ct4_id, CT4.label' || _lang || ' AS ct4_label, CT4.label AS ct4_label_en, ST4.id AS st4_id, ST4.code AS st4_code, ST4.label AS st4_label_en, CN4.id AS cn4_id, CN4.code AS cn4_code, CN4.label AS cn4_label_en' ||
		_columns || '
	FROM
		"Round" RD
		LEFT JOIN "RoundType" RT ON RD.id_round_type = RT.id
		LEFT JOIN "Complex" CX1 ON RD.id_complex1 = CX1.id
		LEFT JOIN "City" CT1 ON CX1.id_city = CT1.id
		LEFT JOIN "State" ST1 ON CT1.id_state = ST1.id
		LEFT JOIN "Country" CN1 ON CT1.id_country = CN1.id
		LEFT JOIN "City" CT2 ON RD.id_city1 = CT2.id
		LEFT JOIN "State" ST2 ON CT2.id_state = ST2.id
		LEFT JOIN "Country" CN2 ON CT2.id_country = CN2.id
		LEFT JOIN "Complex" CX2 ON RD.id_complex = CX2.id
		LEFT JOIN "City" CT3 ON CX2.id_city = CT3.id
		LEFT JOIN "State" ST3 ON CT3.id_state = ST3.id
		LEFT JOIN "Country" CN3 ON CT3.id_country = CN3.id
		LEFT JOIN "City" CT4 ON RD.id_city = CT4.id
		LEFT JOIN "State" ST4 ON CT4.id_state = ST4.id
		LEFT JOIN "Country" CN4 ON CT4.id_country = CN4.id
		LEFT JOIN "Result" RS ON RD.id_result = RS.id' ||
		_joins || '
	WHERE
		RD.id_result = ' || _id_result || '
	ORDER BY
		RT.index, RT.label, RD.id';
	RETURN  _c;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
