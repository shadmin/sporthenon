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


update "RESULT" set comment = 'EX1-2;EX4-5;EX7-8' where comment = 'TRI';
update "RESULT" set comment = '##' || comment where comment like 'World Bowl%';
update "RESULT" set comment = '##' || comment where comment like 'Superbowl%';
update "RESULT" set comment = '##QB' where comment = 'Pos-QB';
update "RESULT" set comment = '##RB' where comment = 'Pos-RB';
update "RESULT" set comment = '##WR' where comment = 'Pos-WR';
update "RESULT" set comment = '##DE' where comment = 'Pos-DE';
update "RESULT" set comment = '##S' where comment = 'Pos-S';
update "RESULT" set comment = '##LB' where comment = 'Pos-LB';
update "RESULT" set comment = '##CB' where comment = 'Pos-CB';
update "RESULT" set comment = '##C' where comment = 'Pos-C';
update "RESULT" set comment = '##LW' where comment = 'Pos-LW';
update "RESULT" set comment = '##RW' where comment = 'Pos-RW';
update "RESULT" set comment = '##G' where comment = 'Pos-G';
update "RESULT" set comment = '##DT' where comment = 'Pos-DT';
update "RESULT" set comment = '##K' where comment = 'Pos-K';
update "RESULT" set comment = '##HB' where comment = 'Pos-HB';
update "RESULT" set comment = '##P' where comment = 'Pos-P';
update "RESULT" set comment = '##B' where comment = 'Pos-B';
update "RESULT" set comment = '##FB' where comment = 'Pos-FB';
update "RESULT" set comment = '##KR' where comment = 'Pos-KR';
update "RESULT" set comment = '##DB' where comment = 'Pos-DB';
update "RESULT" set comment = '##OT' where comment = 'Pos-OT';
update "RESULT" set comment = '##D' where comment = 'Pos-D';
update "RESULT" set comment = '##F' where comment = 'Pos-F';
update "RESULT" set comment = '##T' where comment = 'Pos-T';
update "RESULT" set comment = '##OLB' where comment = 'Pos-OLB';
update "RESULT" set comment = '##W' where comment = 'Pos-W';
update "RESULT" set comment = '##SS' where comment = 'Pos-SS';
update "RESULT" set comment = '##Grass' where comment like 'S-GSS%';
update "RESULT" set comment = '##Clay' where comment like 'S-CLY%';
update "RESULT" set comment = '##Rebound Ace' where comment like 'S-REB%';
update "RESULT" set comment = '##Decoturf' where comment like 'S-DEC%';
update "RESULT" set comment = '##Tarmac' where comment like 'S-TMC%';
update "RESULT" set comment = '##Snow' where comment like 'S-SNW%';
update "RESULT" set comment = '##Gravel/Tarmac' where comment = 'S-GRV;S-TMC';
update "RESULT" set comment = '##Gravel' where comment like 'S-GRV%';
update "RESULT" set comment = '##Hard' where comment like 'S-HRD%';
update "RESULT" set comment = '##Rank: Yokozuna' where comment like 'Rk-Y%';
update "RESULT" set comment = '##Rank: Ozeki' where comment like 'Rk-O%';
update "RESULT" set comment = '##Rank: Sekiwake' where comment like 'Rk-S%';
update "RESULT" set comment = '##Rank: Komusubi' where comment like 'Rk-K%';
update "RESULT" set comment = '##Rank: Maegashira 1' where comment like 'Rk-M1%';
update "RESULT" set comment = '##Rank: Maegashira 2' where comment like 'Rk-M2%';
update "RESULT" set comment = '##Rank: Maegashira 3' where comment like 'Rk-M3%';
update "RESULT" set comment = '##Rank: Maegashira 4' where comment like 'Rk-M4%';
update "RESULT" set comment = '##Rank: Maegashira 5' where comment like 'Rk-M5%';
update "RESULT" set comment = '##Rank: Maegashira 6' where comment like 'Rk-M6%';
update "RESULT" set comment = '##Rank: Maegashira 7' where comment like 'Rk-M7%';
update "RESULT" set comment = '##Rank: Maegashira 8' where comment like 'Rk-M8%';
update "RESULT" set comment = '##Rank: Maegashira 9' where comment like 'Rk-M9%';
update "RESULT" set comment = '##Rank: Maegashira 10' where comment like 'Rk-M10%';
update "RESULT" set comment = '##Rank: Maegashira 11' where comment like 'Rk-M11%';
update "RESULT" set comment = '##Rank: Maegashira 12' where comment like 'Rk-M12%';
update "RESULT" set comment = '##Rank: Maegashira 13' where comment like 'Rk-M13%';
update "RESULT" set comment = '##Rank: Maegashira 14' where comment like 'Rk-M14%';
update "RESULT" set comment = '##Rank: Maegashira 15' where comment like 'Rk-M15%';
update "RESULT" set comment = '##Rank: Maegashira 16' where comment like 'Rk-M16%';
update "RESULT" set comment = '##Rank: Maegashira 17' where comment like 'Rk-M17%';
update "RESULT" set comment = null where comment like 'Zone %';
update "RESULT" set comment = null where comment = '';
update "RESULT" set comment = null where id in (
6187,
10841,
10857,
13920,
13935,
13976,
13977,
4554,
10948,
10955,
10956,
10982,
11078,
11109,
11111,
11118,
7827,
9059,
10239,
10242,
10255,
9445,
9730,
9737,
9827,
9914,
9917,
10925,
4947,
5006,
5022,
12156,
12172,
12176,
12178,
11203,
14299,
8642,
7500,
6735,
6873,
16449,
9045,
17952,
5043,
5044,
11049,
11262,
11364,
11440,
12138,
12205,
10902,
10904,
20015,
6551,
6570,
19730,
4686,
23234,
23235,
17875,
23509
);

CREATE OR REPLACE FUNCTION "SEARCH"(_pattern character varying, _scope character varying, _ref smallint)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_index smallint;
	_current_id integer;
	_current_label varchar(100);
	_current_id_rel1 integer;
	_current_id_rel2 integer;
	_current_id_rel3 integer;
	_current_label_rel1 varchar(50);
	_current_label_rel2 varchar(50);
	_current_label_rel3 varchar(50);
	_current_link integer;
	_scopes varchar(2)[];
	_tables varchar(15)[];
	_i smallint;
	_s varchar(2);
	_c refcursor;
	_query text;
	_rel_cols text;
	_rel_joins text;
	_rel_count smallint;
begin
	_i := 1;
	_index := 1;
	_scopes = '{CP,CT,CX,CN,EV,PR,SP,ST,TM,YR}';
	_tables = '{CHAMPIONSHIP,CITY,COMPLEX,COUNTRY,EVENT,PERSON,SPORT,STATE,TEAM,YEAR}';
	FOR _s IN SELECT UNNEST(_scopes) LOOP
		IF _scope ~ ('(^|,)' || _s || '($|,)') THEN
			_rel_cols := '';
			_rel_joins := '';
			_rel_count := 0;

			-- Get related fields
			IF (_s ~ 'PR|TM') THEN -- Relation: Country
				_rel_cols := _rel_cols || ', CN.id, CN.label || '' ('' || CN.code || '')''';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s ~ 'PR|TM') THEN -- Relation: Sport
				_rel_cols := _rel_cols || ', SP.id, SP.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "SPORT" SP ON ' || _s || '.id_sport = SP.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'PR') THEN -- Relation: Team
				_rel_cols := _rel_cols || ', TM.id, TM.label, PR.link';
				_rel_joins := _rel_joins || ' LEFT JOIN "TEAM" TM ON ' || _s || '.id_team = TM.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'CX') THEN -- Relation: City/State/Country
				_rel_cols := _rel_cols || ', CT.id, CT.label';
				_rel_cols := _rel_cols || ', ST.id, ST.label';
				_rel_cols := _rel_cols || ', CN.id, CN.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "CITY" CT ON ' || _s || '.id_city = CT.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			IF (_s = 'CT') THEN -- Relation: State/Country
				_rel_cols := _rel_cols || ', NULL, NULL';
				_rel_cols := _rel_cols || ', ST.id, ST.label';
				_rel_cols := _rel_cols || ', CN.id, CN.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "STATE" ST ON ' || _s || '.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			FOR _j IN 1.._rel_count LOOP
				_rel_cols := _rel_cols || ', NULL, NULL';
			END LOOP;

			-- Execute query
			_query := 'SELECT ' || _s || '.id, ' || _s || '.label' || _rel_cols || ' FROM "' || _tables[_i] || '" ' || _s;
			_query := _query || _rel_joins || ' WHERE ' || _s || '.label ~* ''' || _pattern || ''' ORDER BY ' || _s || '.label';
			IF _s = 'PR' THEN
				_query := 'SELECT PR.id, PR.last_name || '', '' || PR.first_name' || _rel_cols || ' FROM "PERSON" PR' || _rel_joins;
				_query := _query || ' WHERE (PR.link = 0 OR PR.link IS NULL) AND (PR.last_name ~* ''' || _pattern || ''' OR PR.first_name ~* ''' || _pattern || ''')';
				_query := _query || ' ORDER BY PR.last_name, PR.first_name';
			END IF;
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _current_id, _current_label, _current_id_rel1, _current_label_rel1, _current_id_rel2, _current_label_rel2, _current_id_rel3, _current_label_rel3, _current_link;
				EXIT WHEN NOT FOUND;
				_item.id = _index;
				_item.id_item = _current_id;
				_item.label = _current_label;
				_item.entity = _s;
				IF _ref = 1 THEN
					SELECT "COUNT_REF"(_s, _current_id) INTO _item.count_ref;
				END IF;
				_item.id_rel1 = _current_id_rel1;
				_item.id_rel2 = _current_id_rel2;
				_item.id_rel3 = _current_id_rel3;
				_item.label_rel1 = _current_label_rel1;
				_item.label_rel2 = _current_label_rel2;
				_item.label_rel3 = _current_label_rel3;
				_item.link = _current_link;
				RETURN NEXT _item;
				_index := _index + 1;
			END LOOP;			
			CLOSE _c;
		END IF;
		_i := _i + 1;
	END LOOP;
	RETURN;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION "SEARCH"(character varying, character varying) OWNER TO postgres;
