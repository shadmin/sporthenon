-- Function: "GetUSRecords"(integer, text, text, text, text, character varying)

-- DROP FUNCTION "GetUSRecords"(integer, text, text, text, text, character varying);

CREATE OR REPLACE FUNCTION "GetUSRecords"(
    _id_championship integer,
    _events text,
    _subevents text,
    _types1 text,
    _types2 text,
    _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
    _cr refcursor;
    _c text;
    _j text;
    _columns text;
    _joins text;
    _event_condition text;
    _subevent_condition text;
    _type1_condition text;
    _type2_condition text;
begin
	_event_condition := CASE WHEN _events <> '0' THEN ' AND RC.id_event IN (' || _events || ')' ELSE '' END;
	_subevent_condition := CASE WHEN _subevents <> '0' THEN ' AND RC.id_subevent IN (' || _subevents || ')' ELSE '' END;
	_type1_condition := CASE WHEN _types1 <> '0' THEN ' AND RC.type1 IN (' || _types1 || ')' ELSE '' END;
	_type2_condition := CASE WHEN _types2 <> '0' THEN ' AND RC.type2 IN (' || _types2 || ')' ELSE '' END;

	_columns := '';
	_joins := '';
	FOR i IN 1..5 LOOP
	        _c := ', RC.id_rank# AS rc_rank#, RC.record# AS rc_record#, RC.date# AS rc_date#';
	        _c := _c || ', PR#.last_name || '', '' || PR#.first_name AS rc_person#, TM#.label AS rc_team#, PRTM#.id AS rc_idprteam#, PRTM#.label AS rc_prteam#';
	        _j := ' LEFT JOIN "Athlete" PR# ON RC.id_rank# = PR#.id';
	        _j := _j || ' LEFT JOIN "Team" TM# ON RC.id_rank# = TM#.id';
	        _j := _j || ' LEFT JOIN "Team" PRTM# ON PR#.id_team = PRTM#.id';
	        _columns := _columns || regexp_replace(_c, '#', CAST(i AS varchar), 'g');
	        _joins := _joins || regexp_replace(_j, '#', CAST(i AS varchar), 'g');
	END LOOP;
	
	-- Open cursor
	OPEN _cr FOR EXECUTE
	'SELECT
		RC.id AS rc_id, RC.last_update AS rc_last_update, RC.label AS rc_label, EV.id AS ev_id, EV.label' || _lang || ' AS ev_label, SE.id AS se_id, SE.label' || _lang || ' AS se_label,
		RC.type1 AS rc_type1, RC.type2 AS rc_type2, TP1.number AS rc_number1, TP2.number AS rc_number2, RC.exa AS rc_exa, RC.comment AS rc_comment' ||
		_columns || '
	FROM
		"Record" RC
		LEFT JOIN "Event" EV ON RC.id_event = EV.id
		LEFT JOIN "Event" SE ON RC.id_subevent = SE.id
		LEFT JOIN "Type" TP1 ON EV.id_type = TP1.id
		LEFT JOIN "Type" TP2 ON SE.id_type = TP2.id' ||
		_joins || '
	WHERE
		RC.id_championship = ' || _id_championship || _event_condition || _subevent_condition || _type1_condition || _type2_condition || '
	ORDER BY
		SE.index, EV.index, RC.type1, RC.type2, RC.index';
	
	RETURN  _cr;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
