-- Function: "GetOlympicMedals"(text, integer, text, text, text, character varying)

-- DROP FUNCTION "GetOlympicMedals"(text, integer, text, text, text, character varying);

CREATE OR REPLACE FUNCTION "GetOlympicMedals"(
    _olympics text,
    _id_sport integer,
    _events text,
    _subevents text,
    _subevents2 text,
    _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _ol_type smallint;
    _ol_date1 varchar(10);
    _ol_date2 varchar(10);
    _id_year integer;
    _columns text;
    _joins text;
    _olympics_condition text;
    _event_condition text;
    _subevent_condition text;
    _subevent2_condition text;
begin
	-- Build entity columns/joins
	_columns := '';
	_joins := '';
	FOR i IN 1..5 LOOP
		-- Person
	        _columns := _columns || ', PR' || i || '.last_name AS pr' || i || '_last_name, PR' || i || '.first_name AS pr' || i || '_first_name';
	        _columns := _columns || ', PRCN' || i || '.id AS pr' || i || '_cn_id, PRCN' || i || '.code AS pr' || i || '_cn_code, PRCN' || i || '.label' || _lang || ' AS pr' || i || '_cn_label, PRCN' || i || '.label AS pr' || i || '_cn_label_en';
	        _joins := _joins || ' LEFT JOIN "Athlete" PR' || i || ' ON RS.id_rank' || i || ' = PR' || i || '.id';
	        _joins := _joins || ' LEFT JOIN "Country" PRCN' || i || ' ON PR' || i || '.id_country = PRCN' || i || '.id';
		-- Country
	        _columns := _columns || ', _CN' || i || '.code AS cn' || i || '_code, _CN' || i || '.label' || _lang || ' AS cn' || i || '_label, _CN' || i || '.label AS cn' || i || '_label_en';
	        _joins := _joins || ' LEFT JOIN "Country" _CN' || i || ' ON RS.id_rank' || i || ' = _CN' || i || '.id';
	END LOOP;

	-- Set year condition
	_olympics_condition := '';
	IF _olympics <> '0' THEN
		_olympics_condition := ' AND OL.id IN (' || _olympics || ')';
	END IF;

	-- Set event condition
	_event_condition := '';
	IF _events <> '0' THEN
		_event_condition := ' AND RS.id_event IN (' || _events || ')';
	END IF;

	-- Set subevent condition
	_subevent_condition := '';
	IF _subevents <> '0' THEN
		_subevent_condition := ' AND RS.id_subevent IN (' || _subevents || ')';
	END IF;

	-- Set subevent(2) condition
	_subevent2_condition := '';
	IF _subevents2 <> '0' THEN
		_subevent2_condition := ' AND RS.id_subevent2 IN (' || _subevents2 || ')';
	END IF;
		
	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT 
		RS.id AS rs_id, EV.id AS ev_id, EV.label' || _lang || ' AS ev_label, SE.id AS se_id, SE.label' || _lang || ' AS se_label, SE2.id AS se2_id, SE2.label' || _lang || ' AS se2_label, YR.id as yr_id, YR.label as yr_label, RS.date1 AS rs_date1, RS.date2 AS rs_date2,
		CX.id AS cx_id, CX.label' || _lang || ' AS cx_label, CX.label AS cx_label_en, CT1.id AS ct1_id, CT1.label' || _lang || ' AS ct1_label, CT1.label AS ct1_label_en, CT2.id AS ct2_id, CT2.label' || _lang || ' AS ct2_label, ST1.id AS st1_id, ST1.code AS st1_code, ST1.label' || _lang || ' AS st1_label, ST1.label AS st1_label_en, ST2.id AS st2_id, ST2.code AS st2_code,
		ST2.label' || _lang || ' AS st2_label, CN1.id AS cn1_id, CN1.code AS cn1_code_, CN1.label' || _lang || ' AS cn1_label_, CN1.label AS cn1_label_en_, CN2.id AS cn2_id, CN2.code AS cn2_code_, CN2.label' || _lang || ' AS cn2_label_,
		RS.id_rank1 AS rs_rank1, RS.id_rank2 AS rs_rank2, RS.id_rank3 AS rs_rank3, RS.id_rank4 AS rs_rank4, RS.id_rank5 AS rs_rank5,
		RS.result1 AS rs_result1, RS.result2 AS rs_result2, RS.result3 AS rs_result3, TP1.number AS tp1_number, TP2.number AS tp2_number, TP3.number AS tp3_number, OL.id AS ol_id, OL.type AS ol_type, OL.date1 AS ol_date1, OL.date2 AS ol_date2, CT3.label' || _lang || ' AS ol_city, CT3.label AS ol_city_en, RS.comment as rs_comment, RS.exa as rs_exa'
		|| _columns || '
	FROM
		"Result" RS
		LEFT JOIN "Sport" SP ON RS.id_sport = SP.id
		LEFT JOIN "Event" EV ON RS.id_event = EV.id
		LEFT JOIN "Event" SE ON RS.id_subevent = SE.id
		LEFT JOIN "Event" SE2 ON RS.id_subevent2 = SE2.id
		LEFT JOIN "Complex" CX ON RS.id_complex2 = CX.id
		LEFT JOIN "City" CT1 ON CX.id_city = CT1.id
		LEFT JOIN "City" CT2 ON RS.id_city2 = CT2.id
		LEFT JOIN "State" ST1 ON CT1.id_state = ST1.id
		LEFT JOIN "State" ST2 ON CT2.id_state = ST2.id
		LEFT JOIN "Country" CN1 ON CT1.id_country = CN1.id
		LEFT JOIN "Country" CN2 ON CT2.id_country = CN2.id
		LEFT JOIN "Year" YR ON RS.id_year = YR.id
		LEFT JOIN "Type" TP1 ON EV.id_type = TP1.id
		LEFT JOIN "Type" TP2 ON SE.id_type = TP2.id
		LEFT JOIN "Type" TP3 ON SE2.id_type = TP3.id
		LEFT JOIN "Olympics" OL ON (RS.id_year = OL.id_year AND SP.type = OL.type)
		LEFT JOIN "City" CT3 ON OL.id_city = CT3.id'
		|| _joins || '
	WHERE 
		RS.id_championship = 1 AND RS.id_sport = ' || _id_sport
		|| _olympics_condition || _event_condition || _subevent_condition || _subevent2_condition || '
	ORDER BY OL.id DESC, EV.index, SE.index, EV.label' || _lang || ', SE.label' || _lang || ', SE2.label' || _lang;
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
