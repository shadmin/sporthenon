-- Function: "GetTeamStadiums"(integer, text, character varying)

-- DROP FUNCTION "GetTeamStadiums"(integer, text, character varying);

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
  LANGUAGE plpgsql VOLATILE
  COST 100;