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

CREATE OR REPLACE FUNCTION "ENTITY_REF"(_entity character varying, _id integer, _entity_ref character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_c refcursor;
	_query text;
	_link integer;
	_pr_list varchar(50);
	_index integer;
	_type1 integer;
	_type2 integer;
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
begin
	_index := 1;

	IF _entity ~ 'PR' THEN
		SELECT LINK INTO _link FROM "PERSON" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "PERSON" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_pr_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_pr_list := _pr_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_pr_list := cast(_id AS varchar);
		END IF;
	END IF;

	-- References in: [Results]
	IF (_entity ~ 'CN|PR|TM|CP|EV|CT|SP|CX|YR' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label, CP.id, CP.label, EV.id, EV.label, SE.id, SE.label, RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number FROM "RESULT" RS';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id';
		_query := _query || ' LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id';
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity ~ 'CN|TM' THEN
			_query := _query || ' AND (RS.id_rank1 = ' || _id || ' OR RS.id_rank2 = ' || _id || ' OR RS.id_rank3 = ' || _id || ' OR RS.id_rank4 = ' || _id || ' OR RS.id_rank5 = ' || _id || ' OR RS.id_rank6 = ' || _id || ' OR RS.id_rank7 = ' || _id || ' OR RS.id_rank8 = ' || _id || ' OR RS.id_rank9 = ' || _id || ' OR RS.id_rank10 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RS.id_rank1 in (' || _pr_list || ') OR RS.id_rank2 in (' || _pr_list || ') OR RS.id_rank3 in (' || _pr_list || ') OR RS.id_rank4 in (' || _pr_list || ') OR RS.id_rank5 in (' || _pr_list || ') OR RS.id_rank6 in (' || _pr_list || ') OR RS.id_rank7 in (' || _pr_list || ') OR RS.id_rank8 in (' || _pr_list || ') OR RS.id_rank9 in (' || _pr_list || ') OR RS.id_rank10 in (' || _pr_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND RS.id_city = ' || _id;
		ELSIF _entity = 'CX' THEN
			_query := _query || ' AND RS.id_complex = ' || _id;
		ELSIF _entity = 'YR' THEN
			_query := _query || ' AND RS.id_year = ' || _id;
		END IF;
		_query := _query || ' ORDER BY SP.label, CP.index, EV.index, SE.index, CP.label, EV.label, SE.label, YR.id';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2;
			EXIT WHEN NOT FOUND;
			IF _entity ~ 'CN|PR|TM' THEN
				IF _id1 = _id THEN _item.comment = '1';
				ELSIF _id2 = _id THEN _item.comment = '2';
				ELSIF _id3 = _id THEN _item.comment = '3';
				ELSIF _id4 = _id THEN _item.comment = '4';
				ELSIF _id5 = _id THEN _item.comment = '5';
				ELSIF _id6 = _id THEN _item.comment = '6';
				ELSIF _id7 = _id THEN _item.comment = '7';
				ELSIF _id8 = _id THEN _item.comment = '8';
				ELSIF _id9 = _id THEN _item.comment = '9';
				ELSIF _id10 = _id THEN _item.comment = '10'; END IF;
			END IF;
			IF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF _type1 <= 10 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, PR1.last_name || ', ' || PR1.first_name || ' (' || CN1.code || ')', PR2.last_name || ', ' || PR2.first_name || ' (' || CN2.code || ')', PR3.last_name || ', ' || PR3.first_name || ' (' || CN3.code || ')', PR4.last_name || ', ' || PR4.first_name || ' (' || CN4.code || ')', PR5.last_name || ', ' || PR5.first_name || ' (' || CN5.code || ')', RS.result1, RS.result2, RS.comment
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.txt2, _item.txt3
				FROM "RESULT" RS LEFT JOIN "PERSON" PR1 ON RS.id_rank1 = PR1.id LEFT JOIN "PERSON" PR2 ON RS.id_rank2 = PR2.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3 = PR3.id LEFT JOIN "PERSON" PR4 ON RS.id_rank4 = PR4.id LEFT JOIN "PERSON" PR5 ON RS.id_rank5 = PR5.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "COUNTRY" CN3 ON PR3.id_country = CN3.id LEFT JOIN "COUNTRY" CN4 ON PR4.id_country = CN4.id LEFT JOIN "COUNTRY" CN5 ON PR5.id_country = CN5.id
				WHERE RS.id = _item.id_item;
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, RS.result1, RS.result2, RS.comment
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.txt2, _item.txt3
				FROM "RESULT" RS LEFT JOIN "TEAM" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "TEAM" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "TEAM" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "TEAM" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "TEAM" TM5 ON RS.id_rank5 = TM5.id
				WHERE RS.id = _item.id_item;
			ELSIF _type1 = 99 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, CN1.code, CN2.code, CN3.code, CN4.code, CN5.code, RS.result1, RS.result2, RS.comment
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.txt2, _item.txt3
				FROM "RESULT" RS LEFT JOIN "COUNTRY" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "COUNTRY" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "COUNTRY" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "COUNTRY" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "COUNTRY" CN5 ON RS.id_rank5 = CN5.id
				WHERE RS.id = _item.id_item;
			END IF;
			_item.id = _index;
			_item.entity = 'RS';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;
	
	-- References in: [Athletes]
	IF (_entity ~ 'CN|SP|TM' AND (_entity_ref = 'PR' OR _entity_ref = '')) THEN
		_query := 'SELECT PR.id, PR.last_name || '', '' || PR.first_name, CN.id, CN.label, SP.id, SP.label, TM.id, TM.label FROM "PERSON" PR';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON PR.id_country = CN.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON PR.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "TEAM" TM ON PR.id_team = TM.id';
		IF _entity = 'CN' THEN
			_query := _query || ' WHERE PR.id_country = ' || _id;
		ELSIF _entity = 'SP' THEN
			_query := _query || ' WHERE PR.id_sport = ' || _id;
		ELSIF _entity = 'TM' THEN
			_query := _query || ' WHERE PR.id_team = ' || _id;
		END IF;
		_query := _query || ' ORDER BY PR.last_name, PR.first_name';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'PR';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Teams]
	IF (_entity ~ 'SP|TM|CN' AND (_entity_ref = 'TM' OR _entity_ref = '')) THEN
		_query := 'SELECT TM.id, TM.label, CN.id, CN.label, SP.id, SP.label FROM "TEAM" TM';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON TM.id_country = CN.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON TM.id_sport = SP.id';
		IF _entity = 'SP' THEN
			_query := _query || ' WHERE TM.id_sport = ' || _id;
		ELSIF _entity = 'TM' THEN
			_query := _query || ' WHERE TM.id_parent = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query := _query || ' WHERE TM.id_country = ' || _id;
		END IF;
		_query := _query || ' ORDER BY SP.label, TM.label';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'TM';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Cities]
	IF (_entity ~ 'CN|ST' AND (_entity_ref = 'CT' OR _entity_ref = '')) THEN
		_query := 'SELECT CT.id, CT.label, ST.id, ST.label, CN.id, CN.label FROM "CITY" CT';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'CN' THEN
			_query := _query || ' WHERE CT.id_country = ' || _id;
		ELSIF _entity = 'ST' THEN
			_query := _query || ' WHERE CT.id_state = ' || _id;
		END IF;
		_query := _query || ' ORDER BY CT.label';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CT';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Complexes]
	IF (_entity = 'CT' AND (_entity_ref = 'CX' OR _entity_ref = '')) THEN
		_query := 'SELECT CX.id, CX.label, CT.id, CT.label, ST.id, ST.label, CN.id, CN.label FROM "COMPLEX" CX';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' WHERE CX.id_city = ' || _id;
		_query := _query || ' ORDER BY CX.label';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CX';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Olympics]
	IF (_entity ~ 'YR|CT' AND (_entity_ref = 'OL' OR _entity_ref = '')) THEN
		_query := 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label, ST.id, ST.label, CN.id, CN.label, OL.type FROM "OLYMPICS" OL';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query := _query || ' WHERE OL.id_city = ' || _id;
		END IF;
		_query := _query || ' ORDER BY OL.type, YR.id';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.comment;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'OL';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Olympic Rankings]
	IF (_entity ~ 'OL|CN' AND (_entity_ref = 'OR' OR _entity_ref = '')) THEN
		_query := 'SELECT OR_.id, OL.id, YR.id, YR.label, CT.id, CT.label, CN.id, CN.label, OR_.count_gold || '','' || OR_.count_silver || '','' || OR_.count_bronze FROM "OLYMPIC_RANKING" OR_';
		_query := _query || ' LEFT JOIN "OLYMPICS" OL ON OR_.id_olympics = OL.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON OR_.id_country = CN.id';
		IF _entity = 'OL' THEN
			_query := _query || ' WHERE OR_.id_olympics = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query := _query || ' WHERE OR_.id_country = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.comment;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'OR';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Records]
	IF (_entity ~ 'CN|PR|TM|CP|EV|CT|SP' AND (_entity_ref = 'RC' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT RC.id, RC.label, SP.id, SP.label, CP.id, CP.label, EV.id, EV.label, SE.id, SE.label, RC.type1, RC.type2, RC.id_rank1, RC.id_rank2, RC.id_rank3, RC.id_rank4, RC.id_rank5 FROM "RECORD" RC';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RC.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RC.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RC.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "TYPE" TP ON EV.id_type = TP.id';
		_query := _query || ' WHERE TP.number BETWEEN ' || _type1 || ' AND ' || _type2;
		IF _entity ~ 'CN|TM' THEN
			_query := _query || ' AND (RC.id_rank1 = ' || _id || ' OR RC.id_rank2 = ' || _id || ' OR RC.id_rank3 = ' || _id || ' OR RC.id_rank4 = ' || _id || ' OR RC.id_rank5 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RC.id_rank1 in (' || _pr_list || ') OR RC.id_rank2 in (' || _pr_list || ') OR RC.id_rank3 in (' || _pr_list || ') OR RC.id_rank4 in (' || _pr_list || ') OR RC.id_rank5 in (' || _pr_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RC.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RC.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RC.id_event = ' || _id || ' OR RC.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND RC.id_city = ' || _id;
		END IF;
		_query := _query || ' ORDER BY SP.label, CP.label, EV.label, SE.label, RC.index';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.txt1, _item.txt2, _id1, _id2, _id3, _id4, _id5;
			EXIT WHEN NOT FOUND;
			IF _entity ~ 'CN|PR|TM' THEN
				IF _id1 = _id THEN _item.comment = '1';
				ELSIF _id2 = _id THEN _item.comment = '2';
				ELSIF _id3 = _id THEN _item.comment = '3';
				ELSIF _id4 = _id THEN _item.comment = '4';
				ELSIF _id5 = _id THEN _item.comment = '5'; END IF;
			END IF;
			_item.id = _index;
			_item.entity = 'RC';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Hall of Fame]
	IF (_entity ~ 'YR|PR' AND (_entity_ref = 'HF' OR _entity_ref = '')) THEN
		_query := 'SELECT HF.id, YR.id, YR.label, PR.id, PR.last_name || '', '' || PR.first_name, LG.label FROM "HALL_OF_FAME" HF';
		_query := _query || ' LEFT JOIN "YEAR" YR ON HF.id_year = YR.id';
		_query := _query || ' LEFT JOIN "PERSON" PR ON HF.id_person = PR.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON HF.id_league = LG.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE HF.id_year = ' || _id;
		ELSIF _entity = 'PR' THEN
			_query := _query || ' WHERE HF.id_person in (' || _pr_list || ')';
		END IF;
		_query := _query || ' ORDER BY YR.id';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.comment;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'HF';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Retired Numbers]
	IF (_entity ~ 'TM|PR' AND (_entity_ref = 'RN' OR _entity_ref = '')) THEN
		_query := 'SELECT RN.id, TM.id, TM.label, PR.id, PR.last_name || '', '' || PR.first_name, LG.label, RN.number FROM "RETIRED_NUMBER" RN';
		_query := _query || ' LEFT JOIN "TEAM" TM ON RN.id_team = TM.id';
		_query := _query || ' LEFT JOIN "PERSON" PR ON RN.id_person = PR.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON RN.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE RN.id_team = ' || _id;
		ELSIF _entity = 'PR' THEN
			_query := _query || ' WHERE RN.id_person in (' || _pr_list || ')';
		END IF;
		_query := _query || ' ORDER BY TM.label, RN.number';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.comment, _item.id_rel3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'RN';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Team Stadiums]
	IF (_entity ~ 'TM|CX' AND (_entity_ref = 'TS' OR _entity_ref = '')) THEN
		_query := 'SELECT TS.id, TM.id, TM.label, CX.id, CX.label, CT.id, CT.label, ST.id, ST.label, CN.id, CN.label, LG.label, TS.date1, TS.date2 FROM "TEAM_STADIUM" TS';
		_query := _query || ' LEFT JOIN "TEAM" TM ON TS.id_team = TM.id';
		_query := _query || ' LEFT JOIN "COMPLEX" CX ON TS.id_complex = CX.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON TS.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE TS.id_team = ' || _id;
		ELSIF _entity = 'CX' THEN
			_query := _query || ' WHERE TS.id_complex = ' || _id;
		END IF;
		_query := _query || ' ORDER BY TM.label, TS.date1 DESC';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'TS';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Wins/Losses]
	IF (_entity = 'TM' AND (_entity_ref = 'WL' OR _entity_ref = '')) THEN
		_query := 'SELECT WL.id, TM.id, TM.label, LG.label, WL.type, WL.count_win || '','' || WL.count_loss FROM "WIN_LOSS" WL';
		_query := _query || ' LEFT JOIN "TEAM" TM ON WL.id_team = TM.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON WL.id_league = LG.id';
		_query := _query || ' WHERE WL.id_team = ' || _id;
		_query := _query || ' ORDER BY TM.label DESC';
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'WL';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;
	
	RETURN;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION "ENTITY_REF"(character varying, integer, character varying) OWNER TO postgres;