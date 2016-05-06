-- Function: "GetHallOfFame"(integer, text, character varying)

-- DROP FUNCTION "GetHallOfFame"(integer, text, character varying);

CREATE OR REPLACE FUNCTION "GetHallOfFame"(
    _id_league integer,
    _years text,
    _position character varying)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _year_condition text;
    _position_condition text;
begin
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
		"HallOfFame" HF
		LEFT JOIN "Year" YR ON HF.id_year = YR.id
		LEFT JOIN "Athlete" PR ON HF.id_person = PR.id
	WHERE
		HF.id_league = ' || _id_league || _year_condition || _position_condition || '
	ORDER BY
		YR.id DESC, PR.last_name';
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
