alter TABLE "TEAM" add deleted_ boolean;
update "TEAM" set deleted_=TRUE where deleted=0;
update "TEAM" set deleted_=FALSE where deleted=1;
alter table "TEAM" drop deleted;
alter table "TEAM" RENAME COLUMN deleted_ TO deleted;

alter TABLE "HALL_OF_FAME" add deceased_ boolean;
update "HALL_OF_FAME" set deceased_=TRUE where deceased=0;
update "HALL_OF_FAME" set deceased_=FALSE where deceased=1;
alter table "HALL_OF_FAME" drop deceased;
alter table "HALL_OF_FAME" RENAME COLUMN deceased_ TO deceased;

alter TABLE "RECORD" add counting_ boolean;
update "RECORD" set counting_=TRUE where counting=0;
update "RECORD" set counting_=FALSE where counting=1;
alter table "RECORD" drop counting;
alter table "RECORD" RENAME COLUMN counting_ TO counting;

alter TABLE "TEAM_STADIUM" add renamed_ boolean;
update "TEAM_STADIUM" set renamed_=TRUE where renamed=0;
update "TEAM_STADIUM" set renamed_=FALSE where renamed=1;
alter table "TEAM_STADIUM" drop renamed;
alter table "TEAM_STADIUM" RENAME COLUMN renamed_ TO renamed;

alter table "CHAMPIONSHIP" add inactive boolean;
alter table "EVENT" add inactive boolean;
update "CHAMPIONSHIP" SET inactive=FALSE;
update "EVENT" SET inactive=FALSE;

CREATE OR REPLACE FUNCTION "TREE_RESULTS"()
  RETURNS SETOF "~TREE_ITEM" AS
$BODY$
declare
	_item "~TREE_ITEM"%rowtype;
	_c CURSOR FOR
	    SELECT DISTINCT SP.id, SP.label, CP.id, CP.label, EV.id, EV.label, SE.id, SE.label, CP.inactive, EV.inactive, SE.inactive, CP.index, EV.index, SE.index
	    FROM "RESULT" RS LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id
	    LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id
	    LEFT JOIN "EVENT" EV ON RS.id_event = EV.id
	    LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id
	    ORDER BY SP.label, CP.inactive, CP.index, EV.inactive, EV.index, SE.inactive, SE.index, CP.label, EV.label, SE.label;
	_sp_id integer;
	_sp_label varchar(25);
	_cp_id integer;
	_cp_label varchar(40);
	_cp_inactive boolean;
	_ev_id integer;
	_ev_label varchar(45);
	_ev_inactive boolean;
	_se_id integer;
	_se_label varchar(45);
	_se_inactive boolean;
	_index smallint;
	_current_sp_id integer;
	_current_cp_id integer;
	_current_ev_id integer;
	_current_se_id integer;
begin
	_index := 1;
	_current_sp_id := 0;
	_current_cp_id := 0;
	_current_ev_id := 0;
	_current_se_id := 0;
	OPEN _c;
	LOOP
		FETCH _c INTO _sp_id, _sp_label, _cp_id, _cp_label, _ev_id, _ev_label, _se_id, _se_label, _cp_inactive, _ev_inactive, _se_inactive;
		EXIT WHEN NOT FOUND;

		IF _sp_id <> _current_sp_id THEN
			_item.id = _index;
			_item.id_item = _sp_id;
			_item.label = _sp_label;
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
			IF _cp_inactive = TRUE THEN
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
			IF _ev_inactive = TRUE THEN
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
			IF _se_inactive = TRUE THEN
				_item.label = '+' || _item.label;
			END IF;
			_item.level = 4;
			RETURN NEXT _item;
			_current_se_id := _se_id;
			_index := _index + 1;
		END IF;
	END LOOP;
	CLOSE _c;
	
	RETURN;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION "TREE_RESULTS"() OWNER TO postgres;


CREATE OR REPLACE FUNCTION "GET_WIN_LOSS"(_id_league integer, _teams text)
  RETURNS refcursor AS
$BODY$
declare
    _c refcursor;
    _team_condition text;
begin
	-- Set team condition ('All teams' = Empty condition)
	_team_condition := '';
	IF _teams <> '0' THEN
		_team_condition := ' AND TM.id IN (' || _teams || ')';
	END IF;
	
	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT
		WL.id AS wl_id, TM.id AS tm_id, TM.code AS tm_code, TM.label AS tm_label, 
		WL.type AS wl_type, WL.count_win AS wl_count_win, WL.count_loss AS wl_count_loss, WL.count_tie AS wl_count_tie,
		WL.count_otloss AS wl_count_otloss, WL.average AS wl_average
	FROM
		"WIN_LOSS" WL
		LEFT JOIN "TEAM" TM ON WL.id_team = TM.id
	WHERE
		WL.id_league = ' || _id_league || _team_condition || '
	ORDER BY
		TM.label, WL.type, WL.count_win DESC, WL.count_loss';
	
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION "GET_WIN_LOSS"(integer, text) OWNER TO postgres;
