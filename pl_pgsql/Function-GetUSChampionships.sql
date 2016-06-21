-- Function: "GetUSChampionships"(integer, text, character varying)

-- DROP FUNCTION "GetUSChampionships"(integer, text, character varying);

CREATE OR REPLACE FUNCTION "GetUSChampionships"(
    _id_championship integer,
    _years text,
    _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _year_condition text;
    _usl_events varchar(200);
    _usl_subevents varchar(200);
begin
	_year_condition := CASE WHEN _years <> '0' THEN ' AND YR.id IN (' || _years || ')' ELSE '' END;

	SELECT value INTO _usl_events FROM "~Config" WHERE key = 'USL_CHAMP_EVENTS';
	SELECT value INTO _usl_subevents FROM "~Config" WHERE key = 'USL_CHAMP_SUBEVENTS';
	
	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT
		RS.id AS rs_id, RS.date1 AS rs_date1, RS.date2 AS rs_date2, RS.id_rank1 AS rs_rank1, RS.id_rank2 AS rs_rank2, TM1.label as rs_team1, TM2.label as rs_team2, RS.result1 AS rs_result,
		RS.comment AS rs_comment, RS.exa AS rs_exa, YR.id AS yr_id, YR.label AS yr_label, CX.id AS cx_id, CX.label AS cx_label,
		CT.id AS ct_id, CT.label' || _lang || ' AS ct_label, CT.label AS ct_label_en, ST.id AS st_id, ST.code AS st_code, ST.label' || _lang || ' AS st_label, ST.label AS st_label_en, CN.id AS cn_id, CN.code AS cn_code, CN.label' || _lang || ' AS cn_label, CN.label AS cn_label_en
	FROM
		"Result" RS
		LEFT JOIN "Team" TM1 ON RS.id_rank1 = TM1.id
		LEFT JOIN "Team" TM2 ON RS.id_rank2 = TM2.id
		LEFT JOIN "Year" YR ON RS.id_year = YR.id
		LEFT JOIN "Complex" CX ON RS.id_complex2 = CX.id
		LEFT JOIN "City" CT ON CX.id_city = CT.id
		LEFT JOIN "State" ST ON CT.id_state = ST.id
		LEFT JOIN "Country" CN ON CT.id_country = CN.id
	WHERE
		RS.id_championship = ' || _id_championship || ' AND 
		RS.id_event IN (' || _usl_events || ') AND (RS.id_subevent IS NULL OR RS.id_subevent IN (' || _usl_subevents || ')) AND (RS.id_subevent2 IS NULL OR RS.id_subevent2 IN (' || _usl_subevents || ')) ' || _year_condition || '
	ORDER BY RS.id_year DESC';
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
