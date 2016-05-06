-- Function: "GetYearlyStats"(integer, text, text, boolean, boolean, character varying)

-- DROP FUNCTION "GetYearlyStats"(integer, text, text, boolean, boolean, character varying);

CREATE OR REPLACE FUNCTION "GetYearlyStats"(
    _id_championship integer,
    _years text,
    _categories text,
    _individual boolean,
    _team boolean,
    _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
    _cr refcursor;
    _c text;
    _j text;
    _columns text;
    _joins text;
    _year_condition text;
    _category_condition text;
    _type_condition text;
    _usl_stat_event_label varchar(100);
begin
	SELECT value INTO _usl_stat_event_label FROM "~Config" WHERE key = 'USL_STATS_EVENT_LABEL';

	-- Set year condition ('All teams' = Empty condition)
	_year_condition := '';
	IF _years <> '0' THEN
		_year_condition := ' AND YR.id IN (' || _years || ')';
	END IF;
	-- Set category condition ('All categories' = Empty condition)
	_category_condition := '';
	IF _categories <> '0' THEN
		_category_condition := ' AND SE2.id IN (' || _categories || ')';
	END IF;
	-- Set type condition
	_type_condition := ' AND SE.label IN (''.''' || (CASE WHEN _individual = true THEN ',''Individual''' ELSE '' END) || (CASE WHEN _team = true THEN ',''Team''' ELSE '' END) || ')';

	_columns := '';
	_joins := '';
	FOR i IN 1..8 LOOP
	        _c := ', RS.id_rank# AS rs_rank#';
	        _c := _c || ', PR#.last_name || '', '' || PR#.first_name AS rs_person#, TM#.label AS rs_team#, PRTM#.id AS rs_idprteam#, PRTM#.label AS rs_prteam#';
	        _j := ' LEFT JOIN "Athlete" PR# ON RS.id_rank# = PR#.id';
	        _j := _j || ' LEFT JOIN "Team" TM# ON RS.id_rank# = TM#.id';
	        _j := _j || ' LEFT JOIN "Team" PRTM# ON PR#.id_team = PRTM#.id';
	        _columns := _columns || regexp_replace(_c, '#', CAST(i AS varchar), 'g');
	        _joins := _joins || regexp_replace(_j, '#', CAST(i AS varchar), 'g');
	END LOOP;

	-- Open cursor
	OPEN _cr FOR EXECUTE
	'SELECT
		RS.id AS rs_id, YR.id AS yr_id, YR.label AS yr_label, SE.id AS tp_id, SE.label' || _lang || ' AS tp_label, SE2.id AS ct_id, SE2.label' || _lang || ' AS ct_label, RS.exa AS rs_exa, result1, result2, result3' || _columns || '
	FROM
		"Result" RS
		LEFT JOIN "Year" YR ON RS.id_year = YR.id
		LEFT JOIN "Event" EV ON RS.id_event = EV.id
		LEFT JOIN "Event" SE ON RS.id_subevent = SE.id
		LEFT JOIN "Event" SE2 ON RS.id_subevent2 = SE2.id' || _joins || '
	WHERE
		EV.label like ''%' || _usl_stat_event_label || '%'' AND RS.id_championship = ' || _id_championship || _year_condition || _category_condition || _type_condition || '
	ORDER BY
		YR.label DESC, SE.label, SE2.index, SE2.label' || _lang;
	
	RETURN _cr;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
