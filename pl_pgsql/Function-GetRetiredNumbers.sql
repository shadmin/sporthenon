-- Function: "GetRetiredNumbers"(integer, text, smallint)

-- DROP FUNCTION "GetRetiredNumbers"(integer, text, smallint);

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
  LANGUAGE plpgsql VOLATILE
  COST 100;