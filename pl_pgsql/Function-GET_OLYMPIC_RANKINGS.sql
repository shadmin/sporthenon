-- Function: "GET_OLYMPIC_RANKINGS"(text, text, character varying)

-- DROP FUNCTION "GET_OLYMPIC_RANKINGS"(text, text, character varying);

CREATE OR REPLACE FUNCTION "GET_OLYMPIC_RANKINGS"(_olympics text, _countries text, _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
	_c refcursor;
	_olympics_condition text;
	_country_condition text;
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'OL', 'CN-' || _olympics, current_date);

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
  LANGUAGE plpgsql VOLATILE
  COST 100;
