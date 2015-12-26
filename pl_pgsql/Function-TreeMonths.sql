-- Function: "TreeMonths"(character varying, character varying)

-- DROP FUNCTION "TreeMonths"(character varying, character varying);

CREATE OR REPLACE FUNCTION "TreeMonths"(
    _filter character varying,
    _lang character varying)
  RETURNS SETOF "~TreeItem" AS
$BODY$
declare
	_item "~TreeItem"%rowtype;
	_c refcursor;
	_month_label varchar(8);
	_sp_id integer;
	_sp_label varchar(50);
	_sp_label_en varchar(50);
	_count integer;
	_index smallint;
	_current_month varchar(8);
	_current_sp integer;
begin
	_index := 1;
	_current_month := '';
	_current_sp := 0;
	OPEN _c FOR EXECUTE
	'SELECT (CASE WHEN RS.date2 IS NOT NULL AND RS.date2<>'''' THEN substring(RS.date2, 4) ELSE ''nodate'' END) AS M, SP.id, SP.label, SP.label' || _lang || ', COUNT(*)
		FROM "Result" RS LEFT JOIN "Sport" SP ON RS.id_sport = SP.id
		LEFT JOIN "Year" YR ON RS.id_year = YR.id
		WHERE ' || _filter || '
		GROUP BY M, SP.id, SP.label' || _lang || '
		ORDER BY M, SP.label' || _lang;
	LOOP
		FETCH _c INTO _month_label, _sp_id, _sp_label, _sp_label_en, _count;
		EXIT WHEN NOT FOUND;
		
		IF _month_label <> _current_month THEN
			_item.id = _index;
			_item.id_item = (CASE WHEN _month_label = 'nodate' THEN 0 ELSE substring(_month_label, 0, 3)::integer END);
			_item.label = _month_label;
			_item.level = 1;
			RETURN NEXT _item;
			_current_month := _month_label;
			_current_sp := 0;
			_index := _index + 1;
		END IF;
		IF _sp_id <> _current_sp THEN
			_item.id = _index;
			_item.id_item = _sp_id;
			_item.label = _sp_label || ' (' || _count || ')';
			_item.label_en = _sp_label_en;
			_item.level = 2;
			RETURN NEXT _item;
			_current_sp := _sp_id;
			_index := _index + 1;
		END IF;
	END LOOP;
	CLOSE _c;
	
	RETURN;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
