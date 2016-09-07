-- Function: "GetOlympicRankings"(text, text, character varying)

-- DROP FUNCTION "GetOlympicRankings"(text, text, character varying);

CREATE OR REPLACE FUNCTION "GetOlympicRankings"(
    _olympics text,
    _countries text,
    _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
	_c refcursor;
	_olympics_condition text;
	_country_condition text;
begin
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
		CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label' || _lang || ' AS cn1_label, CN1.label AS cn1_label_en,
		SUM(OR_.count_gold) AS or_count_gold, SUM(OR_.count_silver) AS or_count_silver, SUM(OR_.count_bronze) AS or_count_bronze
	FROM "OlympicRanking" OR_
		LEFT JOIN "Olympics" OL ON OR_.id_olympics = OL.id
		LEFT JOIN "Country" CN1 ON OR_.id_country = CN1.id
		LEFT JOIN "Year" YR ON OL.id_year = YR.id
		LEFT JOIN "City" CT ON OL.id_city = CT.id
		LEFT JOIN "State" ST ON CT.id_state = ST.id
		LEFT JOIN "Country" CN2 ON CT.id_country = CN2.id
	WHERE
		TRUE' || _olympics_condition || _country_condition || '
	GROUP BY
		CN1.id
	ORDER BY
		or_count_gold DESC, or_count_silver DESC, or_count_bronze DESC, CN1.label' || _lang;
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
