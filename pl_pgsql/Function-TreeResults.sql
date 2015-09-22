-- Function: "TreeResults"(character varying, character varying)

-- DROP FUNCTION "TreeResults"(character varying, character varying);

CREATE OR REPLACE FUNCTION "TreeResults"(_filter character varying, _lang character varying)
  RETURNS SETOF "~TreeItem" AS
$BODY$
declare
	_item "~TreeItem"%rowtype;
	_c refcursor;
	_sp_id integer;
	_sp_label varchar(25);
	_sp_label_en varchar(25);
	_cp_id integer;
	_cp_label varchar(50);
	_cp_label_en varchar(50);
	_ev_id integer;
	_ev_label varchar(50);
	_ev_label_en varchar(50);
	_se_id integer;
	_se_label varchar(50);
	_se_label_en varchar(50);
	_se2_id integer;
	_se2_label varchar(50);
	_se2_label_en varchar(50);
	_index smallint;
	_current_sp_id integer;
	_current_cp_id integer;
	_current_ev_id integer;
	_current_se_id integer;
	_current_se2_id integer;
	_ii_championship integer;
	_ii_event integer;
	_ii_subevent integer;
	_ii_subevent2 integer;
begin
	_index := 1;
	_current_sp_id := 0;
	_current_cp_id := 0;
	_current_ev_id := 0;
	_current_se_id := 0;
	OPEN _c FOR EXECUTE
	'SELECT DISTINCT SP.id, SP.label' || _lang || ', SP.label, CP.id, CP.label' || _lang || ', CP.label, EV.id, EV.label' || _lang || ', EV.label, SE.id, SE.label' || _lang || ', SE.label, SE2.id, SE2.label' || _lang || ', SE2.label, II.id_championship, II.id_event, II.id_subevent, II.id_subevent2, CP.index, EV.index, SE.index, SE2.index, (CASE WHEN II.id_event IS NOT NULL AND II.id_subevent IS NULL AND II.id_subevent2 IS NULL THEN 1 ELSE 0 END) AS o_ii_ev, (CASE WHEN II.id_subevent IS NOT NULL AND II.id_subevent2 IS NULL THEN 1 ELSE 0 END) AS o_ii_se, (CASE WHEN II.id_subevent2 IS NOT NULL THEN 1 ELSE 0 END) AS o_ii_se2
	    FROM "Result" RS LEFT JOIN "Sport" SP ON RS.id_sport = SP.id
	    LEFT JOIN "Championship" CP ON RS.id_championship = CP.id
	    LEFT JOIN "Event" EV ON RS.id_event = EV.id
	    LEFT JOIN "Event" SE ON RS.id_subevent = SE.id
	    LEFT JOIN "Event" SE2 ON RS.id_subevent2 = SE2.id
	    LEFT JOIN "Olympics" OL ON OL.id_year = RS.id_year
	    LEFT JOIN "~InactiveItem" II ON (RS.id_sport = II.id_sport AND RS.id_championship = II.id_championship AND RS.id_event = II.id_event AND (RS.id_subevent = II.id_subevent OR RS.id_subevent IS NULL) AND (RS.id_subevent2 = II.id_subevent2 OR RS.id_subevent2 IS NULL))
	    ' || _filter || ' ORDER BY SP.label' || _lang || ', CP.index, o_ii_ev, EV.index, o_ii_se, SE.index, o_ii_se2, SE2.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', SE2.label' || _lang;
	LOOP
		FETCH _c INTO _sp_id, _sp_label, _sp_label_en, _cp_id, _cp_label, _cp_label_en, _ev_id, _ev_label, _ev_label_en, _se_id, _se_label, _se_label_en, _se2_id, _se2_label, _se2_label_en, _ii_championship, _ii_event, _ii_subevent, _ii_subevent2;
		EXIT WHEN NOT FOUND;
		
		IF _sp_id <> _current_sp_id THEN
			_item.id = _index;
			_item.id_item = _sp_id;
			_item.label = _sp_label;
			_item.label_en = _sp_label_en;
			_item.level = 1;
			RETURN NEXT _item;
			_current_sp_id := _sp_id;
			_current_cp_id := 0;
			_index := _index + 1;
		END IF;
		IF _cp_id <> _current_cp_id THEN
			_item.id = _index;
			_item.id_item = _cp_id;
			_item.label = _cp_label;
			_item.label_en = _cp_label_en;
			IF _ii_championship IS NOT NULL AND _ii_event IS NULL AND _ii_subevent IS NULL AND _ii_subevent2 IS NULL THEN
				_item.label = '+' || _item.label;
			END IF;
			_item.level = 2;
			RETURN NEXT _item;
			_current_cp_id := _cp_id;
			_current_ev_id := 0;
			_index := _index + 1;
		END IF;
		IF _ev_id <> _current_ev_id AND _ev_label <> '//UNIQUE//' THEN
			_item.id = _index;
			_item.id_item = _ev_id;
			_item.label = _ev_label;
			_item.label_en = _ev_label_en;
			IF _ii_event IS NOT NULL AND _ii_subevent IS NULL AND _ii_subevent2 IS NULL THEN
				_item.label = '+' || _item.label;
			END IF;
			_item.level = 3;
			RETURN NEXT _item;
			_current_ev_id := _ev_id;
			_current_se_id := 0;
			_index := _index + 1;
		END IF;
		IF _se_id <> _current_se_id THEN
			_item.id = _index;
			_item.id_item = _se_id;
			_item.label = _se_label;
			_item.label_en = _se_label_en;
			IF _ii_subevent IS NOT NULL AND _ii_subevent2 IS NULL THEN
				_item.label = '+' || _item.label;
			END IF;
			_item.level = 4;
			RETURN NEXT _item;
			_current_se_id := _se_id;
			_current_se2_id := 0;
			_index := _index + 1;
		END IF;
		IF _se2_id <> _current_se2_id THEN
			_item.id = _index;
			_item.id_item = _se2_id;
			_item.label = _se2_label;
			_item.label_en = _se2_label_en;
			IF _ii_subevent2 IS NOT NULL THEN
				_item.label = '+' || _item.label;
			END IF;
			_item.level = 5;
			RETURN NEXT _item;
			_current_se2_id := _se2_id;
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