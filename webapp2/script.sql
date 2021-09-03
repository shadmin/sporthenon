CREATE TABLE public._position
(
    id_sport integer,
    code character varying(10),
    label character varying(50)
);

ALTER TABLE public._position
    OWNER to shadmin;
    
insert into _position(id_sport,code,label) values(23,'AD','Administrator');
insert into _position(id_sport,code,label) values(23,'C','Center');
insert into _position(id_sport,code,label) values(23,'CB','Cornerback');
insert into _position(id_sport,code,label) values(23,'CH','Coach');
insert into _position(id_sport,code,label) values(23,'CM','Commissioner');
insert into _position(id_sport,code,label) values(23,'CT','Contributor');
insert into _position(id_sport,code,label) values(23,'DE','Defensive End');
insert into _position(id_sport,code,label) values(23,'DG','Defensive Guard');
insert into _position(id_sport,code,label) values(23,'DT','Defensive Tackle');
insert into _position(id_sport,code,label) values(23,'E','End');
insert into _position(id_sport,code,label) values(23,'FB','Fullback');
insert into _position(id_sport,code,label) values(23,'FK','Flanker');
insert into _position(id_sport,code,label) values(23,'G','Guard');
insert into _position(id_sport,code,label) values(23,'HB','Halfback');
insert into _position(id_sport,code,label) values(23,'K','Kicker');
insert into _position(id_sport,code,label) values(23,'KR','Kick Returner');
insert into _position(id_sport,code,label) values(23,'LB','Linebacker');
insert into _position(id_sport,code,label) values(23,'O','Franchise Owner');
insert into _position(id_sport,code,label) values(23,'OE','Offensive End');
insert into _position(id_sport,code,label) values(23,'OF','Official');
insert into _position(id_sport,code,label) values(23,'OG','Offensive Guard');
insert into _position(id_sport,code,label) values(23,'OT','Offensive Tackle');
insert into _position(id_sport,code,label) values(23,'QB','Quarterback');
insert into _position(id_sport,code,label) values(23,'RB','Runningback');
insert into _position(id_sport,code,label) values(23,'S','Safety');
insert into _position(id_sport,code,label) values(23,'T','Tackle');
insert into _position(id_sport,code,label) values(23,'TE','Tight End');
insert into _position(id_sport,code,label) values(23,'WR','Wide Receiver');
insert into _position(id_sport,code,label) values(23,'GM','General Manager');
insert into _position(id_sport,code,label) values(23,'P','Punter');
update hall_of_fame set position = 'DE-LB' where position = 'LB-DE';
insert into _position(id_sport,code,label) values(23,'DE-LB','Defensive End, Linebacker');
insert into _position(id_sport,code,label) values(23,'CB-KR','Cornerback, Kick Returner');
insert into _position(id_sport,code,label) values(23,'HB-K','Halfback, Kicker');
insert into _position(id_sport,code,label) values(23,'QB-K','Quarterback, Kicker');
update hall_of_fame set position = 'CB-S' where position = 'CB-S-KR';
insert into _position(id_sport,code,label) values(23,'CB-S','Cornerback, Safety');
insert into _position(id_sport,code,label) values(23,'WR-PR','Wide Receiver, Punt Returner');
insert into _position(id_sport,code,label) values(23,'OG-T','Offensive Guard, Tackle');
insert into _position(id_sport,code,label) values(23,'T-G','Tackle, Guard');
insert into _position(id_sport,code,label) values(23,'S-P','Safety, Punter');
insert into _position(id_sport,code,label) values(23,'OT-G','Offensive Tackle, Guard');
insert into _position(id_sport,code,label) values(23,'OT-K','Offensive Tackle, Kicker');
insert into _position(id_sport,code,label) values(23,'S-KR','Safety, Kick Returner');
update hall_of_fame set position = 'E-CH' where position in ('CH-OE', 'OE-CH');
insert into _position(id_sport,code,label) values(23,'E-CH','End, Coach');
insert into _position(id_sport,code,label) values(23,'DE-T','Defensive End, Tackle');
insert into _position(id_sport,code,label) values(23,'DT-E','Defensive Tackle, End');
insert into _position(id_sport,code,label) values(23,'GM-PT','General Manager, President');
update hall_of_fame set position = 'RB-CH' where position = 'O-CH-HB';
insert into _position(id_sport,code,label) values(23,'RB-CH','Runningback, Coach');
update hall_of_fame set position = 'LB-G-K' where position = 'LB-C-K';
insert into _position(id_sport,code,label) values(23,'LB-G-K','Linebacker, Guard, Kicker');

insert into _position(id_sport,code,label) values(24,'C','Center');
insert into _position(id_sport,code,label) values(24,'CH','Coach');
insert into _position(id_sport,code,label) values(24,'F','Forward');
insert into _position(id_sport,code,label) values(24,'G','Guard');
insert into _position(id_sport,code,label) values(24,'PF','Power Forward');
insert into _position(id_sport,code,label) values(24,'PG','Point Guard');
insert into _position(id_sport,code,label) values(24,'SF','Small Forward');
insert into _position(id_sport,code,label) values(24,'SG','Shooting Guard');
insert into _position(id_sport,code,label) values(24,'CT','Contributor');
insert into _position(id_sport,code,label) values(24,'F-G','Forward, Guard');
insert into _position(id_sport,code,label) values(24,'F-C','Forward, Center');
insert into _position(id_sport,code,label) values(24,'PG-F','Point Guard, Forward');
insert into _position(id_sport,code,label) values(24,'C-F','Center, Forward');
insert into _position(id_sport,code,label) values(24,'SG-SF','Shooting Guard, Small Forward');
insert into _position(id_sport,code,label) values(24,'G-F','Guard, Forward');

insert into _position(id_sport,code,label) values(25,'C','Center');
insert into _position(id_sport,code,label) values(25,'D','Defenseman');
insert into _position(id_sport,code,label) values(25,'F','Forward');
insert into _position(id_sport,code,label) values(25,'G','Goaltender');
insert into _position(id_sport,code,label) values(25,'LW','Left Wing');
insert into _position(id_sport,code,label) values(25,'OC','Official');
insert into _position(id_sport,code,label) values(25,'RW','Right Wing');
insert into _position(id_sport,code,label) values(25,'R','Rover');
update hall_of_fame set position = 'C-RW' where position = 'C-R';
insert into _position(id_sport,code,label) values(25,'C-RW','Center, Right Wing');
insert into _position(id_sport,code,label) values(25,'C-D','Center, Defenseman');
insert into _position(id_sport,code,label) values(25,'D-LW','Defenseman, Left Wing');
update hall_of_fame set position = 'D-RW' where position = 'D-R';
insert into _position(id_sport,code,label) values(25,'D-RW','Defenseman, Right Wing');
insert into _position(id_sport,code,label) values(25,'LW-RW','Left Wing, Right Wing');
insert into _position(id_sport,code,label) values(25,'C-LW','Center, Left Wing');
insert into _position(id_sport,code,label) values(25,'CH','Coach');

insert into _position(id_sport,code,label) values(26,'P','Pitcher');
insert into _position(id_sport,code,label) values(26,'C','Catcher');
insert into _position(id_sport,code,label) values(26,'1B','1st Baseman');
insert into _position(id_sport,code,label) values(26,'2B','2nd Baseman');
insert into _position(id_sport,code,label) values(26,'3B','3rd Baseman');
insert into _position(id_sport,code,label) values(26,'SS','Shortstop');
insert into _position(id_sport,code,label) values(26,'LF','Left Fielder');
insert into _position(id_sport,code,label) values(26,'CF','Center Fielder');
insert into _position(id_sport,code,label) values(26,'RF','Right Fielder');
insert into _position(id_sport,code,label) values(26,'MGR','Manager');
insert into _position(id_sport,code,label) values(26,'EX','Executive');
insert into _position(id_sport,code,label) values(26,'PIO','Pioneer Contributor');
insert into _position(id_sport,code,label) values(26,'OF','Official');
insert into _position(id_sport,code,label) values(26,'DH','Designated Hitter');
insert into _position(id_sport,code,label) values(26,'UMP','Umpire');
insert into _position(id_sport,code,label) values(26,'EX-PIO','Executive / Pioneer contributor');

ALTER TABLE public.hall_of_fame
    ADD COLUMN text character varying(100);
    
ALTER TABLE public.hall_of_fame
    ALTER COLUMN id_person DROP NOT NULL;
    
ALTER TABLE result
    RENAME draft TO in_progress;
    
CREATE OR REPLACE FUNCTION public.tree_months(
	_filter character varying,
	_lang character varying)
    RETURNS SETOF _tree_item 
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
declare
	_item _tree_item%rowtype;
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
	'SELECT (CASE WHEN RS.date2 IS NOT NULL AND RS.date2<>'''' THEN substring(RS.date2, 4) ELSE YR.label END) AS M, SP.id, SP.label' || _lang || ', SP.label, COUNT(*)
		FROM result RS LEFT JOIN sport SP ON RS.id_sport = SP.id
		LEFT JOIN year YR ON RS.id_year = YR.id
		WHERE RS.in_progress = false AND ' || _filter || '
		GROUP BY M, SP.id, SP.label' || _lang || '
		ORDER BY M, SP.label' || _lang;
	LOOP
		FETCH _c INTO _month_label, _sp_id, _sp_label, _sp_label_en, _count;
		EXIT WHEN NOT FOUND;
		
		IF _month_label <> _current_month THEN
			_item.id = _index;
			_item.id_item = (CASE WHEN length(_month_label) = 4 THEN 0 ELSE substring(_month_label, 0, 3)::integer END);
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
$BODY$;

CREATE OR REPLACE FUNCTION public.get_results(
	_id_sport integer,
	_id_championship integer,
	_id_event integer,
	_id_subevent integer,
	_id_subevent2 integer,
	_years text,
	_id_result integer,
	_lang character varying)
    RETURNS refcursor
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
declare
    _c refcursor;
    _type integer;
    _columns text;
    _joins text;
    _ev integer;
    _ev1 integer := _id_event;
    _ev2 integer := _id_subevent;
    _ev3 integer := _id_subevent2;
    _event_condition text;
    _year_condition text;
begin
	-- Get entity type (person, country, team)
	IF _id_result <> 0 THEN
	    SELECT RS.id_event, RS.id_subevent, RS.id_subevent2
	    INTO _ev1, _ev2, _ev3
	    FROM result RS
	    WHERE RS.id = _id_result;
	    --WHERE RS.id = _id_result OR (RS.id_sport = _id_sport AND RS.id_championship = _id_championship);
	END IF;
	IF _ev3 IS NOT NULL AND _ev3 <> 0 THEN _ev := _ev3;
	ELSIF _ev2 IS NOT NULL AND _ev2 <> 0 THEN _ev := _ev2;
	ELSE _ev := _ev1; END IF;
	SELECT TP.number INTO _type FROM event EV LEFT JOIN type TP ON EV.id_type = TP.id WHERE EV.id = _ev;
	
	-- Build entity-specific columns/joins
	_columns := '';
	_joins := '';
	FOR i IN 1..20 LOOP
	    IF _type < 10 THEN -- Person
	        _columns := _columns || ', PR' || i || '.last_name AS en' || i || '_str1, PR' || i || '.first_name AS en' || i || '_str2, NULL AS en' || i || '_str3';
	        _columns := _columns || ', PRTM' || i || '.id AS en' || i || '_rel1_id, NULL AS en' || i || '_rel1_code, PRTM' || i || '.label AS en' || i || '_rel1_label';
	        _columns := _columns || ', PRCN' || i || '.id AS en' || i || '_rel2_id, PRCN' || i || '.code AS en' || i || '_rel2_code, PRCN' || i || '.label' || _lang || ' AS en' || i || '_rel2_label, PRCN' || i || '.label AS en' || i || '_rel2_label_en';
	        _joins := _joins || ' LEFT JOIN athlete PR' || i || ' ON RS.id_rank' || i || ' = PR' || i || '.id';
	        _joins := _joins || ' LEFT JOIN team PRTM' || i || ' ON PR' || i || '.id_team = PRTM' || i || '.id';
	        _joins := _joins || ' LEFT JOIN country PRCN' || i || ' ON PR' || i || '.id_country = PRCN' || i || '.id';
	    ELSIF _type = 50 THEN -- Team
	        _columns := _columns || ', NULL AS en' || i || '_str1, TM' || i || '.label AS en' || i || '_str2, NULL AS en' || i || '_str3';
	        _columns := _columns || ', NULL AS en' || i || '_rel1_id, NULL AS en' || i || '_rel1_code, NULL AS en' || i || '_rel1_label';
	        _columns := _columns || ', TMCN' || i || '.id AS en' || i || '_rel2_id, TMCN' || i || '.code AS en' || i || '_rel2_code, TMCN' || i || '.label' || _lang || ' AS en' || i || '_rel2_label, TMCN' || i || '.label AS en' || i || '_rel2_label_en';
	        _joins := _joins || ' LEFT JOIN team TM' || i || ' ON RS.id_rank' || i || ' = TM' || i || '.id';
	        _joins := _joins || ' LEFT JOIN country TMCN' || i || ' ON TM' || i || '.id_country = TMCN' || i || '.id';
	    ELSIF _type = 99 THEN -- Country
	        _columns := _columns || ', _CN' || i || '.code AS en' || i || '_str1, _CN' || i || '.label' || _lang || ' AS en' || i || '_str2, _CN' || i || '.label AS en' || i || '_str3';
	        _columns := _columns || ', NULL AS en' || i || '_rel1_id, NULL AS en' || i || '_rel1_code, NULL AS en' || i || '_rel1_label';
	        _columns := _columns || ', NULL AS en' || i || '_rel2_id, NULL AS en' || i || '_rel2_code, NULL AS en' || i || '_rel2_label, NULL AS en' || i || '_rel2_label_en';
	        _joins := _joins || ' LEFT JOIN country _CN' || i || ' ON RS.id_rank' || i || ' = _CN' || i || '.id';
	    END IF;
	END LOOP;

	-- Handle null event/subevent
	_event_condition := '';
	IF _id_event <> 0 THEN
	    _event_condition := ' AND RS.id_event = ' ||_id_event;
	END IF;
	IF _id_subevent <> 0 THEN
	    _event_condition := _event_condition || ' AND RS.id_subevent = ' ||_id_subevent;
	END IF;
	IF _id_subevent2 <> 0 THEN
	    _event_condition := _event_condition || ' AND RS.id_subevent2 = ' ||_id_subevent2;
	END IF;

	-- Set year condition
	_year_condition := '';
	IF _years <> '0' THEN
		_year_condition := ' AND YR.id IN (' || _years || ')';
	END IF;
	
	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT
		RS.id AS rs_id, RS.last_update AS rs_last_update, RS.date1 AS rs_date1, RS.date2 AS rs_date2, RS.id_rank1 AS rs_rank1, RS.id_rank2 AS rs_rank2, RS.id_rank3 AS rs_rank3, RS.id_rank4 AS rs_rank4, RS.id_rank5 AS rs_rank5, RS.id_rank6 AS rs_rank6, RS.id_rank7 AS rs_rank7, RS.id_rank8 AS rs_rank8, RS.id_rank9 AS rs_rank9, RS.id_rank10 AS rs_rank10, RS.id_rank11 AS rs_rank11, RS.id_rank12 AS rs_rank12, RS.id_rank13 AS rs_rank13, RS.id_rank14 AS rs_rank14, RS.id_rank15 AS rs_rank15, RS.id_rank16 AS rs_rank16, RS.id_rank17 AS rs_rank17, RS.id_rank18 AS rs_rank18, RS.id_rank19 AS rs_rank19, RS.id_rank20 AS rs_rank20,
		RS.result1 AS rs_result1, RS.result2 AS rs_result2, RS.result3 AS rs_result3, RS.result4 AS rs_result4, RS.result5 AS rs_result5, RS.result6 AS rs_result6, RS.result7 AS rs_result7, RS.result8 AS rs_result8, RS.result9 AS rs_result9, RS.result10 AS rs_result10, RS.result11 AS rs_result11, RS.result12 AS rs_result12, RS.result13 AS rs_result13, RS.result14 AS rs_result14, RS.result15 AS rs_result15, RS.result16 AS rs_result16, RS.result17 AS rs_result17, RS.result18 AS rs_result18, RS.result19 AS rs_result19, RS.result20 AS rs_result20,
		RS.comment AS rs_comment, RS.exa AS rs_exa, RS.in_progress AS rs_in_progress, YR.id AS yr_id, YR.label AS yr_label, CX1.id AS cx1_id, CX1.label AS cx1_label, CX2.id AS cx2_id, CX2.label AS cx2_label,
		CT1.id AS ct1_id, CT1.label' || _lang || ' AS ct1_label, CT1.label AS ct1_label_en, CT2.id AS ct2_id, CT2.label' || _lang || ' AS ct2_label, CT2.label AS ct2_label_en, CT3.id AS ct3_id, CT3.label' || _lang || ' AS ct3_label, CT3.label AS ct3_label_en, CT4.id AS ct4_id, CT4.label' || _lang || ' AS ct4_label, CT4.label AS ct4_label_en, ST1.id AS st1_id, ST1.code AS st1_code, ST1.label' || _lang || ' AS st1_label, ST1.label AS st1_label_en, ST2.id AS st2_id, ST2.code AS st2_code,
		ST2.label' || _lang || ' AS st2_label, ST2.label AS st2_label_en, ST3.id AS st3_id, ST3.code AS st3_code, ST3.label' || _lang || ' AS st3_label, ST3.label AS st3_label_en, ST4.id AS st4_id, ST4.code AS st4_code, ST4.label' || _lang || ' AS st4_label, ST4.label AS st4_label_en, CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label' || _lang || ' AS cn1_label, CN1.label AS cn1_label_en, CN2.id AS cn2_id, CN2.code AS cn2_code, CN2.label' || _lang || ' AS cn2_label, CN2.label AS cn2_label_en, CN3.id AS cn3_id, CN3.code AS cn3_code, CN3.label' || _lang || ' AS cn3_label, CN3.label AS cn3_label_en, CN4.id AS cn4_id, CN4.code AS cn4_code, CN4.label' || _lang || ' AS cn4_label, CN4.label AS cn4_label_en, CN5.id AS cn5_id, CN5.code AS cn5_code, CN5.label' || _lang || ' AS cn5_label, CN5.label AS cn5_label_en, CN6.id AS cn6_id, CN6.code AS cn6_code, CN6.label' || _lang || ' AS cn6_label, CN6.label AS cn6_label_en' ||
		_columns || '
	FROM
		result RS
		LEFT JOIN year YR ON RS.id_year = YR.id
		LEFT JOIN complex CX1 ON RS.id_complex1 = CX1.id
		LEFT JOIN complex CX2 ON RS.id_complex2 = CX2.id
		LEFT JOIN city CT1 ON CX1.id_city = CT1.id
		LEFT JOIN city CT2 ON RS.id_city1 = CT2.id
		LEFT JOIN city CT3 ON CX2.id_city = CT3.id
		LEFT JOIN city CT4 ON RS.id_city2 = CT4.id
		LEFT JOIN state ST1 ON CT1.id_state = ST1.id
		LEFT JOIN state ST2 ON CT2.id_state = ST2.id
		LEFT JOIN state ST3 ON CT3.id_state = ST3.id
		LEFT JOIN state ST4 ON CT4.id_state = ST4.id
		LEFT JOIN country CN1 ON CT1.id_country = CN1.id
		LEFT JOIN country CN2 ON CT2.id_country = CN2.id
		LEFT JOIN country CN3 ON CT3.id_country = CN3.id
		LEFT JOIN country CN4 ON CT4.id_country = CN4.id
		LEFT JOIN country CN5 ON RS.id_country1 = CN5.id
		LEFT JOIN country CN6 ON RS.id_country2 = CN6.id' ||
		_joins || '
	WHERE
		(RS.id = ' || _id_result || ' OR (RS.id_sport = ' || _id_sport || ' AND
		RS.id_championship = ' || _id_championship ||
		_event_condition || _year_condition || '))
	ORDER BY RS.id_year DESC, RS.id DESC';
	
	RETURN  _c;
end;
$BODY$;

CREATE OR REPLACE FUNCTION public.entity_ref(
	_entity character varying,
	_id integer,
	_entity_ref character varying,
	_limit character varying,
	_offset integer,
	_lang character varying)
    RETURNS SETOF _ref_item 
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
declare
	_item _ref_item%rowtype;
	_entity varchar := _entity;
	_c refcursor;
	__c refcursor;
	_query text;
	_link integer;
	_rs result%rowtype;
	_ct_list varchar(200);
	_cx_list varchar(200);
	_pr_list varchar(200);
	_tm_list varchar(200);
	_index integer;
	_type1 smallint;
	_type2 smallint;
	_type3 smallint;
	_date varchar(8);
	_date1 varchar(10);
	_date2 varchar(10);
	_array_id integer[];
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
	_id11 integer;_id12 integer;_id13 integer;_id14 integer;
	_cn1 varchar(35);_cn2 varchar(35);_cn3 varchar(35);_cn4 varchar(35);_cn5 varchar(35);_cn6 varchar(35);
	_tm1 varchar(60);_tm2 varchar(60);_tm3 varchar(60);_tm4 varchar(60);_tm5 varchar(60);_tm6 varchar(60);
begin
	IF _entity ~ E'^\\d{8}' THEN
		_date := _entity;
		_entity := 'DT';
	END IF;

	_index := 1;

	IF _entity ~ 'CT' THEN
		SELECT LINK INTO _link FROM city WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query = 'SELECT ID FROM city WHERE ';
			IF _link = 0 THEN
				_query = _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query = _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_ct_list = '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_ct_list = _ct_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_ct_list = cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'CX' THEN
		SELECT LINK INTO _link FROM complex WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query = 'SELECT ID FROM complex WHERE ';
			IF _link = 0 THEN
				_query = _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query = _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_cx_list = '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_cx_list = _cx_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_cx_list = cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'PR' THEN
		SELECT LINK INTO _link FROM athlete WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query = 'SELECT ID FROM athlete WHERE ';
			IF _link = 0 THEN
				_query = _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query = _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_pr_list = '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_pr_list = _pr_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_pr_list = cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'TM' THEN
		SELECT LINK INTO _link FROM team WHERE ID = _id AND (YEAR1 IS NULL OR YEAR1 = '');
		IF _link IS NOT NULL THEN
			_query = 'SELECT ID FROM team WHERE ';
			IF _link = 0 THEN
				_query = _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query = _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_tm_list = '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_tm_list = _tm_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_tm_list = cast(_id AS varchar);
		END IF;
	END IF;
	
	-- References in: [Results/Rounds]
	IF (_entity ~ 'CN|DT|PR|TM|CP|EV|CT|SP|CX|OL|YR' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		
		_query = 'SELECT RS.id, YR.id AS __yr_id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, RS.date1, RS.date2, RS.first_update AS __first_update, RS.last_update, TP1.number, TP2.number, TP3.number, NULL, NULL';
		IF (_entity = 'PR') THEN
			_query = _query || ', PL.rank';
		ELSE
			_query = _query || ', 0';
		END IF;
		_query = _query || ' ,(CASE WHEN RS.date2 IS NOT NULL AND RS.date2 <> '''' THEN to_date(RS.date2, ''dd/MM/yyyy'') ELSE (''-infinity''::timestamptz) END) AS __sort_date ';
		_query = _query || ' FROM result RS';
		_query = _query || ' LEFT JOIN year YR ON RS.id_year = YR.id';
		_query = _query || ' LEFT JOIN sport SP ON RS.id_sport = SP.id';
		_query = _query || ' LEFT JOIN championship CP ON RS.id_championship = CP.id';
		_query = _query || ' LEFT JOIN event EV ON RS.id_event = EV.id';
		_query = _query || ' LEFT JOIN event SE ON RS.id_subevent = SE.id';
		_query = _query || ' LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id';
		_query = _query || ' LEFT JOIN type TP1 ON EV.id_type = TP1.id';
		_query = _query || ' LEFT JOIN type TP2 ON SE.id_type = TP2.id';
		_query = _query || ' LEFT JOIN type TP3 ON SE2.id_type = TP3.id';
		IF (_entity = 'OL') THEN
			_query = _query || ' LEFT JOIN olympics OL ON (OL.id_year = YR.id AND OL.type = SP.type)';
		ELSIF (_entity = 'PR') THEN
			_type1 = 1;
			_type2 = 99;
			_query = _query || ' LEFT JOIN _person_list PL ON PL.id_result = RS.id';
		END IF;
		_query = _query || ' WHERE RS.in_progress = false AND ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity = 'CN' THEN
			_query = _query || ' AND (RS.id_rank1 = ' || _id || ' OR RS.id_rank2 = ' || _id || ' OR RS.id_rank3 = ' || _id || ' OR RS.id_rank4 = ' || _id || ' OR RS.id_rank5 = ' || _id || ' OR RS.id_rank6 = ' || _id || ' OR RS.id_rank7 = ' || _id || ' OR RS.id_rank8 = ' || _id || ' OR RS.id_rank9 = ' || _id || ' OR RS.id_rank10 = ' || _id || ' OR RS.id_rank11 = ' || _id || ' OR RS.id_rank12 = ' || _id || ' OR RS.id_rank13 = ' || _id || ' OR RS.id_rank14 = ' || _id || ' OR RS.id_rank15 = ' || _id || ' OR RS.id_rank16 = ' || _id || ' OR RS.id_rank17 = ' || _id || ' OR RS.id_rank18 = ' || _id || ' OR RS.id_rank19 = ' || _id || ' OR RS.id_rank20 = ' || _id || ')';
		ELSIF _entity = 'DT' THEN
			_query = _query || ' AND to_date(RS.date2, ''DD/MM/YYYY'') = to_date(''' || _date || ''', ''YYYYMMDD'')';
		ELSIF _entity = 'PR' THEN
			_query = _query || ' AND (PL.id_person IN (' || _pr_list || ') OR (((TP1.number BETWEEN 1 AND 10 AND TP2.number IS NULL) OR (TP2.number BETWEEN 1 AND 10 AND TP3.number IS NULL) OR (TP3.number BETWEEN 1 AND 10)) AND ((RS.id_rank1 IN (' || _pr_list || ') OR RS.id_rank2 IN (' || _pr_list || ') OR RS.id_rank3 IN (' || _pr_list || ') OR RS.id_rank4 IN (' || _pr_list || ') OR RS.id_rank5 IN (' || _pr_list || ') OR RS.id_rank6 IN (' || _pr_list || ') OR RS.id_rank7 IN (' || _pr_list || ') OR RS.id_rank8 IN (' || _pr_list || ') OR RS.id_rank9 IN (' || _pr_list || ') OR RS.id_rank10 IN (' || _pr_list || ') OR RS.id_rank11 IN (' || _pr_list || ') OR RS.id_rank12 IN (' || _pr_list || ') OR RS.id_rank13 IN (' || _pr_list || ') OR RS.id_rank14 IN (' || _pr_list || ') OR RS.id_rank15 IN (' || _pr_list || ') OR RS.id_rank16 IN (' || _pr_list || ') OR RS.id_rank17 IN (' || _pr_list || ') OR RS.id_rank18 IN (' || _pr_list || ') OR RS.id_rank19 IN (' || _pr_list || ') OR RS.id_rank20 IN (' || _pr_list || ')))))';
		ELSIF _entity = 'TM' THEN
			_query = _query || ' AND (RS.id_rank1 IN (' || _tm_list || ') OR RS.id_rank2 IN (' || _tm_list || ') OR RS.id_rank3 IN (' || _tm_list || ') OR RS.id_rank4 IN (' || _tm_list || ') OR RS.id_rank5 IN (' || _tm_list || ') OR RS.id_rank6 IN (' || _tm_list || ') OR RS.id_rank7 IN (' || _tm_list || ') OR RS.id_rank8 IN (' || _tm_list || ') OR RS.id_rank9 IN (' || _tm_list || ') OR RS.id_rank10 IN (' || _tm_list || ') OR RS.id_rank11 IN (' || _tm_list || ') OR RS.id_rank12 IN (' || _tm_list || ') OR RS.id_rank13 IN (' || _tm_list || ') OR RS.id_rank14 IN (' || _tm_list || ') OR RS.id_rank15 IN (' || _tm_list || ') OR RS.id_rank16 IN (' || _tm_list || ') OR RS.id_rank17 IN (' || _tm_list || ') OR RS.id_rank18 IN (' || _tm_list || ') OR RS.id_rank19 IN (' || _tm_list || ') OR RS.id_rank20 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query = _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query = _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query = _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query = _query || ' AND  (RS.id_city1 IN (' || _ct_list || ') OR RS.id_city2 IN (' || _ct_list || '))';
		ELSIF _entity = 'CX' THEN
			_query = _query || ' AND  (RS.id_complex1 IN (' || _cx_list || ') OR RS.id_complex2 IN (' || _cx_list || '))';
		ELSIF _entity = 'OL' THEN
			_query = _query || ' AND RS.id_championship=1 AND OL.id = ' || _id;
		ELSIF _entity = 'YR' THEN
			_query = _query || ' AND RS.id_year = ' || _id;
		END IF;
		
		_query = _query || ' UNION SELECT RD.id, YR.id AS __yr_id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RD.id_rank1, RD.id_rank2, RD.id_rank3, RD.id_rank4, RD.id_rank5, NULL, NULL, NULL, NULL, NULL, RD.date1, RD.date, RD.first_update AS __first_update, RD.last_update, RD.id_result_type, NULL, NULL, RD.id_result, RT.label' || _lang || ', 0 ';
		_query = _query || ' ,(CASE WHEN RD.date IS NOT NULL AND RD.date <> '''' THEN to_date(RD.date, ''dd/MM/yyyy'') ELSE (''-infinity''::timestamptz) END) AS __sort_date ';
		_query = _query || ' FROM round RD';
		_query = _query || ' LEFT JOIN result RS ON RD.id_result = RS.id';
		_query = _query || ' LEFT JOIN round_type RT ON RD.id_round_type = RT.id';
		_query = _query || ' LEFT JOIN year YR ON RS.id_year = YR.id';
		_query = _query || ' LEFT JOIN sport SP ON RS.id_sport = SP.id';
		_query = _query || ' LEFT JOIN championship CP ON RS.id_championship = CP.id';
		_query = _query || ' LEFT JOIN event EV ON RS.id_event = EV.id';
		_query = _query || ' LEFT JOIN event SE ON RS.id_subevent = SE.id';
		_query = _query || ' LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id';
		_query = _query || ' WHERE (id_result_type BETWEEN ' || _type1 || ' AND ' || _type2 || ')';
		IF _entity = 'CN' THEN
			_query = _query || ' AND (RD.id_rank1 = ' || _id || ' OR RD.id_rank2 = ' || _id || ' OR RD.id_rank3 = ' || _id || ')';
		ELSIF _entity = 'DT' THEN
			_query = _query || ' AND to_date(RD.date, ''DD/MM/YYYY'') = to_date(''' || _date || ''', ''YYYYMMDD'')';
		ELSIF _entity = 'PR' THEN
			_query = _query || ' AND (RD.id_rank1 IN (' || _pr_list || ') OR RD.id_rank2 IN (' || _pr_list || ') OR RD.id_rank3 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query = _query || ' AND (RD.id_rank1 IN (' || _tm_list || ') OR RD.id_rank2 IN (' || _tm_list || ') OR RD.id_rank3 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query = _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query = _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query = _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query = _query || ' AND  (RD.id_city IN (' || _ct_list || ') OR RD.id_city IN (' || _ct_list || '))';
		ELSIF _entity = 'CX' THEN
			_query = _query || ' AND  (RD.id_complex IN (' || _cx_list || ') OR RD.id_complex IN (' || _cx_list || '))';
		ELSIF _entity = 'YR' THEN
			_query = _query || ' AND RS.id_year = ' || _id;
		END IF;

		_query = _query || ' ORDER BY __yr_id DESC, __sort_date DESC, __first_update DESC ';
		_query = _query || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _date1, _date2, _item.date3, _item.date4, _type1, _type2, _type3, _item.id_rel19, _item.label, _item.count1;
			EXIT WHEN NOT FOUND;
			IF _item.id_rel19 IS NULL THEN -- Result
				IF _type3 IS NOT NULL THEN
					_type1 = _type3;
				ELSIF _type2 IS NOT NULL THEN
					_type1 = _type2;
				END IF;
				IF _type1 <= 10 THEN
					SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, (CASE WHEN _lang = '_fr' THEN CN1.label_fr ELSE CN1.label END), TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, (CASE WHEN _lang = '_fr' THEN CN2.label_fr ELSE CN2.label END), TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, (CASE WHEN _lang = '_fr' THEN CN3.label_fr ELSE CN3.label END), TM3.label, PR4.last_name || (CASE WHEN length(PR4.first_name) > 0 THEN ', ' || PR4.first_name ELSE '' END), (CASE WHEN length(PR4.first_name) > 0 THEN PR4.first_name || ' ' ELSE '' END) || PR4.last_name, CN4.id, (CASE WHEN _lang = '_fr' THEN CN4.label_fr ELSE CN4.label END), TM4.label, PR5.last_name || (CASE WHEN length(PR5.first_name) > 0 THEN ', ' || PR5.first_name ELSE '' END), (CASE WHEN length(PR5.first_name) > 0 THEN PR5.first_name || ' ' ELSE '' END) || PR5.last_name, CN5.id, (CASE WHEN _lang = '_fr' THEN CN5.label_fr ELSE CN5.label END), TM5.label, PR6.last_name || (CASE WHEN length(PR6.first_name) > 0 THEN ', ' || PR6.first_name ELSE '' END), (CASE WHEN length(PR6.first_name) > 0 THEN PR6.first_name || ' ' ELSE '' END) || PR6.last_name, CN6.id, (CASE WHEN _lang = '_fr' THEN CN6.label_fr ELSE CN6.label END), TM6.label, RS.result1, RS.result2, RS.result3, RS.comment, RS.exa
					INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.label_rel23, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.label_rel24, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.label_rel25, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt5, _item.txt3, _item.txt4
					FROM result RS LEFT JOIN athlete PR1 ON RS.id_rank1 = PR1.id LEFT JOIN athlete PR2 ON RS.id_rank2 = PR2.id LEFT JOIN athlete PR3 ON RS.id_rank3 = PR3.id LEFT JOIN athlete PR4 ON RS.id_rank4 = PR4.id LEFT JOIN athlete PR5 ON RS.id_rank5 = PR5.id LEFT JOIN athlete PR6 ON RS.id_rank6 = PR6.id LEFT JOIN country CN1 ON PR1.id_country = CN1.id LEFT JOIN country CN2 ON PR2.id_country = CN2.id LEFT JOIN country CN3 ON PR3.id_country = CN3.id LEFT JOIN country CN4 ON PR4.id_country = CN4.id LEFT JOIN country CN5 ON PR5.id_country = CN5.id LEFT JOIN country CN6 ON PR6.id_country = CN6.id LEFT JOIN team TM1 ON PR1.id_team = TM1.id LEFT JOIN team TM2 ON PR2.id_team = TM2.id LEFT JOIN team TM3 ON PR3.id_team = TM3.id LEFT JOIN team TM4 ON PR4.id_team = TM4.id LEFT JOIN team TM5 ON PR5.id_team = TM5.id LEFT JOIN team TM6 ON PR6.id_team = TM6.id
					WHERE RS.id = _item.id_item;
					IF _cn1 IS NOT NULL THEN _item.label_rel6 = _item.label_rel6 || '|' || _cn1;
					ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 = _item.label_rel6 || '|' || _tm1; END IF;
					IF _cn2 IS NOT NULL THEN _item.label_rel7 = _item.label_rel7 || '|' || _cn2;
					ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 = _item.label_rel7 || '|' || _tm2; END IF;
					IF _cn3 IS NOT NULL THEN _item.label_rel8 = _item.label_rel8 || '|' || _cn3;
					ELSIF _tm3 IS NOT NULL THEN _item.label_rel8 = _item.label_rel8 || '|' || _tm3; END IF;
					IF _cn4 IS NOT NULL THEN _item.label_rel9 = _item.label_rel9 || '|' || _cn4;
					ELSIF _tm4 IS NOT NULL THEN _item.label_rel9 = _item.label_rel9 || '|' || _tm4; END IF;
					IF _cn5 IS NOT NULL THEN _item.label_rel10 = _item.label_rel10 || '|' || _cn5;
					ELSIF _tm5 IS NOT NULL THEN _item.label_rel10 = _item.label_rel10 || '|' || _tm5; END IF;
					IF _cn6 IS NOT NULL THEN _item.label_rel11 = _item.label_rel11 || '|' || _cn6;
					ELSIF _tm6 IS NOT NULL THEN _item.label_rel11 = _item.label_rel11 || '|' || _tm6; END IF;
					IF _type1 = 4 OR _item.txt3 = '#DOUBLE#' THEN
						_item.txt4 = '1-2/3-4/5-6';
					ELSIF _type1 = 5 OR _item.txt3 = '#TRIPLE#' THEN
						_item.txt4 = '1-3/4-6/7-9';
					END IF;
					_item.comment = 'PR';
					_array_id = string_to_array(_pr_list, ',')::integer[];
				ELSIF _type1 = 50 THEN
					SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, NULL, NULL, NULL, NULL, NULL, NULL, RS.result1, RS.result2, RS.result3, RS.comment, RS.exa
					INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt5, _item.txt3, _item.txt4
					FROM result RS LEFT JOIN team TM1 ON RS.id_rank1 = TM1.id LEFT JOIN team TM2 ON RS.id_rank2 = TM2.id LEFT JOIN team TM3 ON RS.id_rank3 = TM3.id LEFT JOIN team TM4 ON RS.id_rank4 = TM4.id LEFT JOIN team TM5 ON RS.id_rank5 = TM5.id LEFT JOIN team TM6 ON RS.id_rank6 = TM6.id
					WHERE RS.id = _item.id_item;
					_item.comment = 'TM';
					_array_id = string_to_array(_tm_list, ',')::integer[];
				ELSIF _type1 = 99 THEN
					_query = 'SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN4.label' || _lang || ', CN5.label' || _lang || ', CN6.label' || _lang || ', CN1.label, CN2.label, CN3.label, CN4.label, CN5.label, CN6.label, RS.result1, RS.result2, RS.result3, RS.comment, RS.exa';
					_query = _query || ' FROM result RS LEFT JOIN country CN1 ON RS.id_rank1 = CN1.id LEFT JOIN country CN2 ON RS.id_rank2 = CN2.id LEFT JOIN country CN3 ON RS.id_rank3 = CN3.id LEFT JOIN country CN4 ON RS.id_rank4 = CN4.id LEFT JOIN country CN5 ON RS.id_rank5 = CN5.id LEFT JOIN country CN6 ON RS.id_rank6 = CN6.id';
					_query = _query || ' WHERE RS.id = ' || _item.id_item;
					OPEN __c FOR EXECUTE _query;
					FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt5, _item.txt3, _item.txt4;
					CLOSE __c;
					_item.comment = 'CN';
					_array_id = ARRAY[_id];
				END IF;
				IF ((_item.count1 IS NULL OR _item.count1 = 0) AND _entity ~ 'CN|PR|TM') THEN
					SELECT * INTO _rs FROM result RS WHERE RS.id = _item.id_item;
					SELECT get_rank(_rs, _type1, _array_id) INTO _item.count1;
				END IF;
			ELSE -- Round
				IF _type1 <= 10 THEN
					SELECT id_rank1, id_rank2, id_rank3, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, (CASE WHEN _lang = '_fr' THEN CN1.label_fr ELSE CN1.label END), TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, (CASE WHEN _lang = '_fr' THEN CN2.label_fr ELSE CN2.label END), TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, (CASE WHEN _lang = '_fr' THEN CN3.label_fr ELSE CN3.label END), TM3.label, RD.result1, RD.result2, RD.result3
					INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.txt1, _item.txt2, _item.txt3
					FROM round RD LEFT JOIN athlete PR1 ON RD.id_rank1 = PR1.id LEFT JOIN athlete PR2 ON RD.id_rank2 = PR2.id LEFT JOIN athlete PR3 ON RD.id_rank3 = PR3.id LEFT JOIN country CN1 ON PR1.id_country = CN1.id LEFT JOIN country CN2 ON PR2.id_country = CN2.id LEFT JOIN country CN3 ON PR3.id_country = CN3.id LEFT JOIN team TM1 ON PR1.id_team = TM1.id LEFT JOIN team TM2 ON PR2.id_team = TM2.id LEFT JOIN team TM3 ON PR3.id_team = TM3.id
					WHERE RD.id = _item.id_item;
					IF _cn1 IS NOT NULL THEN _item.label_rel6 = _item.label_rel6 || '|' || _cn1;
					ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 = _item.label_rel6 || '|' || _tm1; END IF;
					IF _cn2 IS NOT NULL THEN _item.label_rel7 = _item.label_rel7 || '|' || _cn2;
					ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 = _item.label_rel7 || '|' || _tm2; END IF;
					IF _cn3 IS NOT NULL THEN _item.label_rel8 = _item.label_rel8 || '|' || _cn3;
					ELSIF _tm3 IS NOT NULL THEN _item.label_rel8 = _item.label_rel8 || '|' || _tm3; END IF;
					_item.comment = 'PR';
					_array_id = string_to_array(_pr_list, ',')::integer[];
				ELSIF _type1 = 50 THEN
					SELECT id_rank1, id_rank2, id_rank3, TM1.label, TM2.label, TM3.label, RD.result1, RD.result2, RD.result3
					INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.txt1, _item.txt2, _item.txt3
					FROM round RD LEFT JOIN team TM1 ON RD.id_rank1 = TM1.id LEFT JOIN team TM2 ON RD.id_rank2 = TM2.id LEFT JOIN team TM3 ON RD.id_rank3 = TM3.id
					WHERE RD.id = _item.id_item;
					_item.comment = 'TM';
					_array_id = string_to_array(_tm_list, ',')::integer[];
				ELSIF _type1 = 99 THEN
					_query = 'SELECT id_rank1, id_rank2, id_rank3, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN1.label, CN2.label, CN3.label, RD.result1, RD.result2, RD.result3';
					_query = _query || ' FROM round RD LEFT JOIN country CN1 ON RD.id_rank1 = CN1.id LEFT JOIN country CN2 ON RD.id_rank2 = CN2.id LEFT JOIN country CN3 ON RD.id_rank3 = CN3.id';
					_query = _query || ' WHERE RD.id = ' || _item.id_item;
					OPEN __c FOR EXECUTE _query;
					FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.txt1, _item.txt2, _item.txt3;
					CLOSE __c;
					_item.comment = 'CN';
					_array_id = ARRAY[_id];
				END IF;
			END IF;
			_item.date1 := to_date(_date1, 'DD/MM/YYYY');
			_item.date2 := to_date(_date2, 'DD/MM/YYYY');
			_item.id = _index;
			_item.entity = 'RS';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Rounds]
	/*IF (_entity ~ 'ZZZ' AND (_entity_ref = 'RD' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query = 'SELECT RD.id, RD.id_result, RD.id_result_type, RT.label' || _lang || ', YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RD.id_rank1, RD.id_rank2, RD.id_rank3, RD.date, RD.last_update FROM round RD';
		_query = _query || ' LEFT JOIN result RS ON RD.id_result = RS.id';
		_query = _query || ' LEFT JOIN round_type RT ON RD.id_round_type = RT.id';
		_query = _query || ' LEFT JOIN year YR ON RS.id_year = YR.id';
		_query = _query || ' LEFT JOIN sport SP ON RS.id_sport = SP.id';
		_query = _query || ' LEFT JOIN championship CP ON RS.id_championship = CP.id';
		_query = _query || ' LEFT JOIN event EV ON RS.id_event = EV.id';
		_query = _query || ' LEFT JOIN event SE ON RS.id_subevent = SE.id';
		_query = _query || ' LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id';
		_query = _query || ' WHERE (id_result_type BETWEEN ' || _type1 || ' AND ' || _type2 || ')';
		IF _entity = 'CN' THEN
			_query = _query || ' AND (RD.id_rank1 = ' || _id || ' OR RD.id_rank2 = ' || _id || ' OR RD.id_rank3 = ' || _id || ')';
		ELSIF _entity = 'DT' THEN
			_query = _query || ' AND to_date(RD.date, ''DD/MM/YYYY'') = to_date(''' || _date || ''', ''YYYYMMDD'')';
		ELSIF _entity = 'PR' THEN
			_query = _query || ' AND (RD.id_rank1 IN (' || _pr_list || ') OR RD.id_rank2 IN (' || _pr_list || ') OR RD.id_rank3 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query = _query || ' AND (RD.id_rank1 IN (' || _tm_list || ') OR RD.id_rank2 IN (' || _tm_list || ') OR RD.id_rank3 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query = _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query = _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query = _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query = _query || ' AND  (RD.id_city IN (' || _ct_list || ') OR RD.id_city IN (' || _ct_list || '))';
		ELSIF _entity = 'CX' THEN
			_query = _query || ' AND  (RD.id_complex IN (' || _cx_list || ') OR RD.id_complex IN (' || _cx_list || '))';
		ELSIF _entity = 'YR' THEN
			_query = _query || ' AND RS.id_year = ' || _id;
		END IF;
		_query = _query || ' ORDER BY YR.id DESC, RD.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel10, _type1, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _date2, _item.date3;
			EXIT WHEN NOT FOUND;
			IF _type1 <= 10 THEN
				SELECT id_rank1, id_rank2, id_rank3, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, (CASE WHEN _lang = '_fr' THEN CN1.label_fr ELSE CN1.label END), TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, (CASE WHEN _lang = '_fr' THEN CN2.label_fr ELSE CN2.label END), TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, (CASE WHEN _lang = '_fr' THEN CN3.label_fr ELSE CN3.label END), TM3.label, RD.result1, RD.result2, RD.result3
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.txt1, _item.txt2, _item.txt3
				FROM round RD LEFT JOIN athlete PR1 ON RD.id_rank1 = PR1.id LEFT JOIN athlete PR2 ON RD.id_rank2 = PR2.id LEFT JOIN athlete PR3 ON RD.id_rank3 = PR3.id LEFT JOIN country CN1 ON PR1.id_country = CN1.id LEFT JOIN country CN2 ON PR2.id_country = CN2.id LEFT JOIN country CN3 ON PR3.id_country = CN3.id LEFT JOIN team TM1 ON PR1.id_team = TM1.id LEFT JOIN team TM2 ON PR2.id_team = TM2.id LEFT JOIN team TM3 ON PR3.id_team = TM3.id
				WHERE RD.id = _item.id_item;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 = _item.label_rel6 || '|' || _cn1;
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 = _item.label_rel6 || '|' || _tm1; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 = _item.label_rel7 || '|' || _cn2;
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 = _item.label_rel7 || '|' || _tm2; END IF;
				IF _cn3 IS NOT NULL THEN _item.label_rel8 = _item.label_rel8 || '|' || _cn3;
				ELSIF _tm3 IS NOT NULL THEN _item.label_rel8 = _item.label_rel8 || '|' || _tm3; END IF;
				_item.comment = 'PR';
				_array_id = string_to_array(_pr_list, ',')::integer[];
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, TM1.label, TM2.label, TM3.label, RD.result1, RD.result2, RD.result3
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.txt1, _item.txt2, _item.txt3
				FROM round RD LEFT JOIN team TM1 ON RD.id_rank1 = TM1.id LEFT JOIN team TM2 ON RD.id_rank2 = TM2.id LEFT JOIN team TM3 ON RD.id_rank3 = TM3.id
				WHERE RD.id = _item.id_item;
				_item.comment = 'TM';
				_array_id = string_to_array(_tm_list, ',')::integer[];
			ELSIF _type1 = 99 THEN
				_query = 'SELECT id_rank1, id_rank2, id_rank3, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN1.label, CN2.label, CN3.label, RD.result1, RD.result2, RD.result3';
				_query = _query || ' FROM round RD LEFT JOIN country CN1 ON RD.id_rank1 = CN1.id LEFT JOIN country CN2 ON RD.id_rank2 = CN2.id LEFT JOIN country CN3 ON RD.id_rank3 = CN3.id';
				_query = _query || ' WHERE RD.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.txt1, _item.txt2, _item.txt3;
				CLOSE __c;
				_item.comment = 'CN';
				_array_id = ARRAY[_id];
			END IF;
			_item.date2 := to_date(_date2, 'DD/MM/YYYY');
			_item.id = _index;
			_item.entity = 'RD';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;*/

	-- References in: [Events]
	IF (_entity ~ 'CP|EV|SP' AND (_entity_ref = 'EV' OR _entity_ref = '')) THEN
		_query = 'SELECT DISTINCT SP.id, SP.label' || _lang || ', SP.label, CP.id, CP.label' || _lang || ', CP.label, EV.id, EV.label' || _lang || ', EV.label, EV.last_update, SE.id, SE.label' || _lang || ', SE.label, SE2.id, SE2.label' || _lang || ', SE2.label, II.id_championship, II.id_event, II.id_subevent, II.id_subevent2, CP.index, EV.index, SE.index, SE2.index, (CASE WHEN II.id_event IS NOT NULL AND II.id_subevent IS NULL AND II.id_subevent2 IS NULL THEN 1 ELSE 0 END) AS o_ii_ev, (CASE WHEN II.id_subevent IS NOT NULL AND II.id_subevent2 IS NULL THEN 1 ELSE 0 END) AS o_ii_se, (CASE WHEN II.id_subevent2 IS NOT NULL THEN 1 ELSE 0 END) AS o_ii_se2';
		_query = _query || ' FROM result RS LEFT JOIN sport SP ON RS.id_sport = SP.id';
		_query = _query || ' LEFT JOIN championship CP ON RS.id_championship = CP.id';
		_query = _query || ' LEFT JOIN event EV ON RS.id_event = EV.id';
		_query = _query || ' LEFT JOIN event SE ON RS.id_subevent = SE.id';
		_query = _query || ' LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id';
		_query = _query || ' LEFT JOIN _inactive_item II ON (RS.id_sport = II.id_sport AND RS.id_championship = II.id_championship AND RS.id_event = II.id_event AND (RS.id_subevent = II.id_subevent OR RS.id_subevent IS NULL) AND (RS.id_subevent2 = II.id_subevent2 OR RS.id_subevent2 IS NULL))';
		IF _entity = 'SP' THEN
			_query = _query || ' WHERE RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query = _query || ' WHERE RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query = _query || ' WHERE RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id;
		END IF;
		_query = _query || ' ORDER BY SP.label' || _lang || ', CP.index, o_ii_ev, EV.index, o_ii_se, SE.index, o_ii_se2, SE2.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', SE2.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_rel1, _item.label_rel1, _item.label_rel2, _item.id_rel2, _item.label_rel3, _item.label_rel4, _item.id_rel3, _item.label_rel5, _item.label_rel6, _item.date3, _item.id_rel4, _item.label_rel7, _item.label_rel8, _item.id_rel5, _item.label_rel9, _item.label_rel10, _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'EV';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;
	
	-- References in: [Athletes]
	IF (_entity ~ 'CN|SP|TM' AND (_entity_ref = 'PR' OR _entity_ref = '')) THEN
		_query = 'SELECT DISTINCT ON (PR.last_name COLLATE "en_EN", PR.first_name COLLATE "en_EN", CN.id, SP.id) PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label, PR.link, PR.last_update FROM athlete PR';
		_query = _query || ' LEFT JOIN country CN ON PR.id_country = CN.id';
		_query = _query || ' LEFT JOIN sport SP ON PR.id_sport = SP.id';
		IF _entity = 'CN' THEN
			_query = _query || ' WHERE PR.id_country = ' || _id;
		ELSIF _entity = 'SP' THEN
			_query = _query || ' WHERE PR.id_sport = ' || _id || ' AND (PR.link = 0 OR PR.link IS NULL)';
		ELSIF _entity = 'TM' THEN
			_query = _query || ' WHERE PR.id_team IN (' || _tm_list || ')';
		END IF;
		_query = _query || ' ORDER BY PR.last_name COLLATE "en_EN", PR.first_name COLLATE "en_EN", SP.id LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4, _item.id_rel11, _item.date3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'PR';
			_item.txt3 := '';
			IF (_entity = 'SP' AND _item.id_rel11 = 0) THEN
				SELECT string_agg(CN.id || ',' || CN.label || ',' || CN.label_fr, '|') INTO _item.txt3 FROM athlete PR LEFT JOIN country CN ON PR.id_country=CN.id WHERE PR.link=_item.id_item;
			END IF;
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Teams]
	IF (_entity ~ 'SP|CN' AND (_entity_ref = 'TM' OR _entity_ref = '')) THEN
		_query = 'SELECT TM.id, TM.label, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label, TM.last_update FROM team TM';
		_query = _query || ' LEFT JOIN country CN ON TM.id_country = CN.id';
		_query = _query || ' LEFT JOIN sport SP ON TM.id_sport = SP.id';
		IF _entity = 'SP' THEN
			_query = _query || ' WHERE TM.id_sport = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query = _query || ' WHERE TM.id_country = ' || _id;
		END IF;
		_query = _query || ' ORDER BY SP.label' || _lang || ', TM.label LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4, _item.date3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'TM';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Cities]
	IF (_entity ~ 'CN|ST' AND (_entity_ref = 'CT' OR _entity_ref = '')) THEN
		_query = 'SELECT CT.id, CT.label' || _lang || ', CT.label, CN.id, CN.label' || _lang || ', CN.label, CT.last_update FROM city CT';
		_query = _query || ' LEFT JOIN state ST ON CT.id_state = ST.id';
		_query = _query || ' LEFT JOIN country CN ON CT.id_country = CN.id';
		IF _entity = 'CN' THEN
			_query = _query || ' WHERE CT.id_country = ' || _id;
		ELSIF _entity = 'ST' THEN
			_query = _query || ' WHERE CT.id_state = ' || _id;
		END IF;
		_query = _query || ' ORDER BY CT.label' || _lang || ' COLLATE "en_EN" LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.label_rel2, _item.date3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CT';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Complexes]
	IF (_entity = 'CT' AND (_entity_ref = 'CX' OR _entity_ref = '')) THEN
		_query = 'SELECT CX.id, CX.label, CX.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, CX.last_update FROM complex CX';
		_query = _query || ' LEFT JOIN city CT ON CX.id_city = CT.id';
		_query = _query || ' LEFT JOIN state ST ON CT.id_state = ST.id';
		_query = _query || ' LEFT JOIN country CN ON CT.id_country = CN.id';
		_query = _query || ' WHERE CX.id_city = ' || _id;
		_query = _query || ' ORDER BY CX.label COLLATE "en_EN" LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4, _item.date3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CX';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Olympics]
	IF (_entity ~ 'YR|CT' AND (_entity_ref = 'OL' OR _entity_ref = '')) THEN
		_query = 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OL.type, OL.last_update FROM olympics OL';
		_query = _query || ' LEFT JOIN year YR ON OL.id_year = YR.id';
		_query = _query || ' LEFT JOIN city CT ON OL.id_city = CT.id';
		_query = _query || ' LEFT JOIN state ST ON CT.id_state = ST.id';
		_query = _query || ' LEFT JOIN country CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query = _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query = _query || ' WHERE OL.id_city IN (' || _ct_list || ')';
		END IF;
		_query = _query || ' ORDER BY OL.type, YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment, _item.date3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'OL';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Olympic Rankings]
	IF (_entity ~ 'OL|CN' AND (_entity_ref = 'OR_' OR _entity_ref = '')) THEN
		_query = 'SELECT OR_.id, OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OR_.count_gold || '','' || OR_.count_silver || '','' || OR_.count_bronze, OR_.last_update FROM olympic_ranking OR_';
		_query = _query || ' LEFT JOIN olympics OL ON OR_.id_olympics = OL.id';
		_query = _query || ' LEFT JOIN year YR ON OL.id_year = YR.id';
		_query = _query || ' LEFT JOIN city CT ON OL.id_city = CT.id';
		_query = _query || ' LEFT JOIN country CN ON OR_.id_country = CN.id';
		IF _entity = 'OL' THEN
			_query = _query || ' WHERE OR_.id_olympics = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query = _query || ' WHERE OR_.id_country = ' || _id;
		END IF;
		_query = _query || ' ORDER BY YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment, _item.date3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'OR_';
			RETURN NEXT _item;
			_index = _index + 1;
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
		_query = 'SELECT RC.id, RC.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, RC.type1, RC.type2, RC.record1, RC.id_rank1, RC.id_rank2, RC.id_rank3, RC.id_rank4, RC.id_rank5, RC.last_update FROM record RC';
		_query = _query || ' LEFT JOIN sport SP ON RC.id_sport = SP.id';
		_query = _query || ' LEFT JOIN championship CP ON RC.id_championship = CP.id';
		_query = _query || ' LEFT JOIN event EV ON RC.id_event = EV.id';
		_query = _query || ' LEFT JOIN event SE ON RC.id_subevent = SE.id';
		_query = _query || ' WHERE lower(RC.type1) = ''' || (CASE WHEN _type1 = 50 THEN 'team' ELSE 'individual' END) || '''';
		IF _entity = 'CN' THEN
			_query = _query || ' AND (RC.id_rank1 = ' || _id || ' OR RC.id_rank2 = ' || _id || ' OR RC.id_rank3 = ' || _id || ' OR RC.id_rank4 = ' || _id || ' OR RC.id_rank5 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query = _query || ' AND (RC.id_rank1 IN (' || _pr_list || ') OR RC.id_rank2 IN (' || _pr_list || ') OR RC.id_rank3 IN (' || _pr_list || ') OR RC.id_rank4 IN (' || _pr_list || ') OR RC.id_rank5 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query = _query || ' AND (RC.id_rank1 IN (' || _tm_list || ') OR RC.id_rank2 IN (' || _tm_list || ') OR RC.id_rank3 IN (' || _tm_list || ') OR RC.id_rank4 IN (' || _tm_list || ') OR RC.id_rank5 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query = _query || ' AND RC.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query = _query || ' AND RC.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query = _query || ' AND  (RC.id_event = ' || _id || ' OR RC.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query = _query || ' AND RC.id_city IN (' || _ct_list || ')';
		END IF;
		_query = _query || ' ORDER BY SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', RC.index LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.txt1, _item.txt2, _item.txt3, _id1, _id2, _id3, _id4, _id5, _item.date3;
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
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Hall of Fame]
	IF (_entity ~ 'YR|PR' AND (_entity_ref = 'HF' OR _entity_ref = '')) THEN
		_query = 'SELECT HF.id, YR.id, YR.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, HF.position, HF.last_update FROM hall_of_fame HF';
		_query = _query || ' LEFT JOIN year YR ON HF.id_year = YR.id';
		_query = _query || ' LEFT JOIN athlete PR ON HF.id_person = PR.id';
		_query = _query || ' LEFT JOIN league LG ON HF.id_league = LG.id';
		IF _entity = 'YR' THEN
			_query = _query || ' WHERE HF.id_year = ' || _id;
		ELSIF _entity = 'PR' THEN
			_query = _query || ' WHERE HF.id_person IN (' || _pr_list || ')';
		END IF;
		_query = _query || ' ORDER BY YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.txt1, _item.date3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'HF';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Retired Numbers]
	IF (_entity ~ 'PR' AND (_entity_ref = 'RN' OR _entity_ref = '')) THEN
		_query = 'SELECT RN.id, TM.id, TM.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, RN.number, RN.last_update FROM retired_number RN';
		_query = _query || ' LEFT JOIN team TM ON RN.id_team = TM.id';
		_query = _query || ' LEFT JOIN athlete PR ON RN.id_person = PR.id';
		_query = _query || ' LEFT JOIN league LG ON RN.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query = _query || ' WHERE RN.id_team IN (' || _tm_list || ')';
		ELSIF _entity = 'PR' THEN
			_query = _query || ' WHERE RN.id_person IN (' || _pr_list || ')';
		END IF;
		_query = _query || ' ORDER BY TM.label, RN.number LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.label_rel4, _item.date3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'RN';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Team Stadiums]
	IF (_entity ~ 'CX' AND (_entity_ref = 'TS' OR _entity_ref = '')) THEN
		_query = 'SELECT TS.id, TM.id, TM.label, CX.id, CX.label, CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', CX.label, CT.label, ST.label, CN.label, LG.id, LG.label, TS.date1, TS.date2, TS.last_update FROM team_stadium TS';
		_query = _query || ' LEFT JOIN team TM ON TS.id_team = TM.id';
		_query = _query || ' LEFT JOIN complex CX ON TS.id_complex = CX.id';
		_query = _query || ' LEFT JOIN city CT ON CX.id_city = CT.id';
		_query = _query || ' LEFT JOIN state ST ON CT.id_state = ST.id';
		_query = _query || ' LEFT JOIN country CN ON CT.id_country = CN.id';
		_query = _query || ' LEFT JOIN league LG ON TS.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query = _query || ' WHERE TS.id_team IN (' || _tm_list || ')';
		ELSIF _entity = 'CX' THEN
			_query = _query || ' WHERE TS.id_complex IN (' || _cx_list || ')';
		END IF;
		_query = _query || ' ORDER BY TM.label, TS.date1 DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.id_rel6, _item.comment, _item.txt1, _item.txt2, _item.date3;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'TS';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Contributions]
	IF (_entity ~ 'CB' AND (_entity_ref = 'CO' OR _entity_ref = '')) THEN
		_query = 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, CO.type, CO.date';
		_query = _query || ' FROM _contribution CO';
		_query = _query || ' LEFT JOIN result RS ON CO.id_item = RS.id';
		_query = _query || ' LEFT JOIN year YR ON RS.id_year = YR.id';
		_query = _query || ' LEFT JOIN sport SP ON RS.id_sport = SP.id';
		_query = _query || ' LEFT JOIN championship CP ON RS.id_championship = CP.id';
		_query = _query || ' LEFT JOIN event EV ON RS.id_event = EV.id';
		_query = _query || ' LEFT JOIN event SE ON RS.id_subevent = SE.id';
		_query = _query || ' LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id';
		_query = _query || ' WHERE RS.id_contributor=' || _id || ' ORDER BY CO.date DESC, YR.id DESC, SP.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.date1;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CO';
			RETURN NEXT _item;
			_index = _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;
	
	RETURN;
end;
$BODY$;

DROP FUNCTION public.get_retired_numbers(integer, text, smallint);

CREATE OR REPLACE FUNCTION public.get_retired_numbers(
	_id_league integer,
	_teams text,
	_number varchar)
    RETURNS refcursor
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
declare
    _c refcursor;
    _team_condition text;
    _number_condition text;
begin
	-- Set team condition ('All teams' = Empty condition)
	_team_condition := '';
	IF _teams <> '0' THEN
		_team_condition := ' AND TM.id IN (' || _teams || ')';
	END IF;
	-- Set deceased condition
	_number_condition := '';
	IF _number <> '-1' THEN
		_number_condition := ' AND RN.number = ''' || _number || '''';
	END IF;
	
	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT
		RN.id AS rn_id, RN.last_update AS rn_last_update, TM.id AS tm_id, TM.label AS tm_label, 
		PR.id AS pr_id, PR.last_name AS pr_last_name, PR.first_name AS pr_first_name, YR.id AS yr_id, YR.label AS yr_label, RN.number AS rn_number,
		REGEXP_REPLACE(rn.number, ''\D'', ''9'', ''g'')::integer AS sort_col
	FROM
		retired_number RN
		LEFT JOIN team TM ON RN.id_team = TM.id
		LEFT JOIN athlete PR ON RN.id_person = PR.id
		LEFT JOIN year YR ON RN.id_year = YR.id
	WHERE
		RN.id_league = ' || _id_league || _team_condition || _number_condition || '
	ORDER BY
		TM.label, sort_col';
	
	RETURN  _c;
end;
$BODY$;

ALTER FUNCTION public.get_retired_numbers(integer, text, varchar)
    OWNER TO shadmin;
		

CREATE OR REPLACE FUNCTION public._last_updates(
	_sport integer,
	_count integer,
	_offset integer,
	_lang character varying)
    RETURNS refcursor
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
declare
	_c refcursor;
begin
	OPEN _c FOR EXECUTE
	'SELECT RS.id AS id, RS.last_update AS last_update, RS.id AS rs_id, YR.id AS yr_id, YR.label AS yr_label, SP.id AS sp_id, CP.id AS cp_id, EV.id AS ev_id, SE.id AS se_id, SE2.id AS se2_id, SP.label' || _lang || ' AS sp_label, CP.label' || _lang || ' AS cp_label, EV.label' || _lang || ' AS ev_label, SE.label' || _lang || ' AS se_label, SE2.label' || _lang || ' AS se2_label, SP.label AS sp_label_en, CP.label AS cp_label_en, EV.label AS ev_label_en, SE.label AS se_label_en, SE2.label AS se2_label_en, TP1.number as tp1_number, TP2.number AS tp2_number, TP3.number AS tp3_number,
	  PR1.id AS pr1_id, PR1.first_name AS pr1_first_name, PR1.last_name AS pr1_last_name, PR1.id_team AS pr1_team, PR1.id_country AS pr1_country, PRCN1.code AS pr1_country_code, TM1.id AS tm1_id, TM1.label AS tm1_label, CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label' || _lang || ' AS cn1_label, CN1.label AS cn1_label_en, 
	  PR2.id AS pr2_id, PR2.first_name AS pr2_first_name, PR2.last_name AS pr2_last_name, PR2.id_team AS pr2_team, PR2.id_country AS pr2_country, PRCN2.code AS pr2_country_code, TM2.id AS tm2_id, TM2.label AS tm2_label, CN2.id AS cn2_id, CN2.code AS cn2_code, CN2.label' || _lang || ' AS cn2_label, CN2.label AS cn2_label_en,
	  PR3.id AS pr3_id, PR3.first_name AS pr3_first_name, PR3.last_name AS pr3_last_name, PR3.id_team AS pr3_team, PR3.id_country AS pr3_country, PRCN3.code AS pr3_country_code, TM3.id AS tm3_id, TM3.label AS tm3_label, CN3.id AS cn3_id, CN3.code AS cn3_code, CN3.label' || _lang || ' AS cn3_label, CN3.label AS cn3_label_en,
	  PR4.id AS pr4_id, PR4.first_name AS pr4_first_name, PR4.last_name AS pr4_last_name, PR4.id_team AS pr4_team, PR4.id_country AS pr4_country, PRCN4.code AS pr4_country_code, TM4.id AS tm4_id, TM4.label AS tm4_label, CN4.id AS cn4_id, CN4.code AS cn4_code, CN4.label' || _lang || ' AS cn4_label, CN4.label AS cn4_label_en,
	  RS.result1 AS rs_text1, RS.result2 AS rs_text2, RS.exa AS rs_text3, RS.comment AS rs_text4, NULL AS rs_text5, (CASE WHEN RS.date2 IS NOT NULL AND RS.date2<>'''' THEN to_date(RS.date2, ''dd/MM/yyyy'') ELSE RS.first_update END) AS rs_date, 1000 AS rt_index
	  FROM result RS
		LEFT JOIN year YR ON RS.id_year=YR.id
		LEFT JOIN sport SP ON RS.id_sport=SP.id
		LEFT JOIN championship CP ON RS.id_championship=CP.id
		LEFT JOIN event EV ON RS.id_event=EV.id
		LEFT JOIN event SE ON RS.id_subevent=SE.id
		LEFT JOIN event SE2 ON RS.id_subevent2=SE2.id
		LEFT JOIN type TP1 ON EV.id_type=TP1.id
		LEFT JOIN type TP2 ON SE.id_type=TP2.id
		LEFT JOIN type TP3 ON SE2.id_type=TP3.id
		LEFT JOIN athlete PR1 ON RS.id_rank1=PR1.id
		LEFT JOIN athlete PR2 ON RS.id_rank2=PR2.id
		LEFT JOIN athlete PR3 ON RS.id_rank3=PR3.id
		LEFT JOIN athlete PR4 ON RS.id_rank4=PR4.id
		LEFT JOIN country PRCN1 ON PR1.id_country=PRCN1.id
		LEFT JOIN country PRCN2 ON PR2.id_country=PRCN2.id
		LEFT JOIN country PRCN3 ON PR3.id_country=PRCN3.id
		LEFT JOIN country PRCN4 ON PR4.id_country=PRCN4.id
		LEFT JOIN team TM1 ON RS.id_rank1=TM1.id
		LEFT JOIN team TM2 ON RS.id_rank2=TM2.id
		LEFT JOIN team TM3 ON RS.id_rank3=TM3.id
		LEFT JOIN team TM4 ON RS.id_rank4=TM4.id
		LEFT JOIN country CN1 ON RS.id_rank1=CN1.id
		LEFT JOIN country CN2 ON RS.id_rank2=CN2.id
		LEFT JOIN country CN3 ON RS.id_rank3=CN3.id
		LEFT JOIN country CN4 ON RS.id_rank4=CN4.id
	WHERE RS.in_progress = false' || (CASE WHEN _sport > 0 THEN ' AND SP.id=' || _sport ELSE '' END) ||'
	UNION
	SELECT RD.id AS id, RD.last_update AS last_update, RS.id AS rs_id, YR.id AS yr_id, YR.label AS yr_label, SP.id AS sp_id, CP.id AS cp_id, EV.id AS ev_id, SE.id AS se_id, SE2.id AS se2_id, SP.label' || _lang || ' AS sp_label, CP.label' || _lang || ' AS cp_label, EV.label' || _lang || ' AS ev_label, SE.label' || _lang || ' AS se_label, SE2.label' || _lang || ' AS se2_label, SP.label AS sp_label_en, CP.label AS cp_label_en, EV.label AS ev_label_en, SE.label AS se_label_en, SE2.label AS se2_label_en, TP1.number as tp1_number, TP2.number AS tp2_number, TP3.number AS tp3_number,
	  PR1.id AS pr1_id, PR1.first_name AS pr1_first_name, PR1.last_name AS pr1_last_name, PR1.id_team AS pr1_team, PR1.id_country AS pr1_country, PRCN1.code AS pr1_country_code, TM1.id AS tm1_id, TM1.label AS tm1_label, CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label' || _lang || ' AS cn1_label, CN1.label AS cn1_label_en, 
	  PR2.id AS pr2_id, PR2.first_name AS pr2_first_name, PR2.last_name AS pr2_last_name, PR2.id_team AS pr2_team, PR2.id_country AS pr2_country, PRCN2.code AS pr2_country_code, TM2.id AS tm2_id, TM2.label AS tm2_label, CN2.id AS cn2_id, CN2.code AS cn2_code, CN2.label' || _lang || ' AS cn2_label, CN2.label AS cn2_label_en,
	  PR3.id AS pr3_id, PR3.first_name AS pr3_first_name, PR3.last_name AS pr3_last_name, PR3.id_team AS pr3_team, PR3.id_country AS pr3_country, PRCN3.code AS pr3_country_code, TM3.id AS tm3_id, TM3.label AS tm3_label, CN3.id AS cn3_id, CN3.code AS cn3_code, CN3.label' || _lang || ' AS cn3_label, CN3.label AS cn3_label_en, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
	  RD.result1 AS rs_text1, RD.result2 AS rs_text2, RD.exa AS rs_text3, RD.comment AS rs_text4, RT.label' || _lang || ' AS rs_text5, (CASE WHEN RD.date IS NOT NULL AND RD.date<>'''' THEN to_date(RD.date, ''dd/MM/yyyy'') ELSE RD.first_update END) AS rs_date, RT.index AS rt_index
	  FROM round RD
		LEFT JOIN round_type RT ON RD.id_round_type=RT.id
		LEFT JOIN result RS ON RD.id_result=RS.id
		LEFT JOIN year YR ON RS.id_year=YR.id
		LEFT JOIN sport SP ON RS.id_sport=SP.id
		LEFT JOIN championship CP ON RS.id_championship=CP.id
		LEFT JOIN event EV ON RS.id_event=EV.id
		LEFT JOIN event SE ON RS.id_subevent=SE.id
		LEFT JOIN event SE2 ON RS.id_subevent2=SE2.id
		LEFT JOIN type TP1 ON EV.id_type=TP1.id
		LEFT JOIN type TP2 ON SE.id_type=TP2.id
		LEFT JOIN type TP3 ON SE2.id_type=TP3.id
		LEFT JOIN athlete PR1 ON RD.id_rank1=PR1.id
		LEFT JOIN athlete PR2 ON RD.id_rank2=PR2.id
		LEFT JOIN athlete PR3 ON RD.id_rank3=PR3.id
		LEFT JOIN country PRCN1 ON PR1.id_country=PRCN1.id
		LEFT JOIN country PRCN2 ON PR2.id_country=PRCN2.id
		LEFT JOIN country PRCN3 ON PR3.id_country=PRCN3.id
		LEFT JOIN team TM1 ON RD.id_rank1=TM1.id
		LEFT JOIN team TM2 ON RD.id_rank2=TM2.id
		LEFT JOIN team TM3 ON RD.id_rank3=TM3.id
		LEFT JOIN country CN1 ON RD.id_rank1=CN1.id
		LEFT JOIN country CN2 ON RD.id_rank2=CN2.id
		LEFT JOIN country CN3 ON RD.id_rank3=CN3.id' || (CASE WHEN _sport > 0 THEN ' WHERE SP.id=' || _sport ELSE '' END) ||'
	ORDER BY yr_id DESC, rs_date DESC, rt_index DESC, rs_id DESC LIMIT ' || _count || ' OFFSET ' || _offset;
	RETURN  _c;
end;
$BODY$;


CREATE OR REPLACE FUNCTION public.get_hall_of_fame(
	_id_league integer,
	_years text,
	_position character varying)
    RETURNS refcursor
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
declare
    _c refcursor;
    _year_condition text;
    _position_condition text;
begin
	-- Set year condition ('All years' = Empty condition)
	_year_condition := '';
	IF _years <> '0' THEN
		_year_condition := ' AND YR.id IN (' || _years || ')';
	END IF;

	-- Set position condition
	_position_condition := '';
	IF _position <> '' THEN
		_position_condition := ' AND HF.position ~* ''(^|-)' || _position || '($|-)''';
	END IF;
	
	-- Open cursor
	OPEN _c FOR EXECUTE
	'SELECT
		HF.id AS hf_id, HF.last_update AS hf_last_update, HF.id_league AS lg_id, YR.id AS yr_id, YR.label AS yr_label, PR.id AS pr_id, PR.last_name AS pr_last_name,
		PR.first_name AS pr_first_name, HF.text AS hf_text, COALESCE(PS.label, HF.position) AS hf_position
	FROM
		hall_of_fame HF
		LEFT JOIN year YR ON HF.id_year = YR.id
		LEFT JOIN athlete PR ON HF.id_person = PR.id
		LEFT JOIN _position PS ON PS.code = HF.position AND PS.id_sport =
			CASE WHEN HF.id_league = 1 THEN 23 WHEN HF.id_league = 2 THEN 24 WHEN HF.id_league = 3 THEN 25 WHEN HF.id_league = 4 THEN 26 END
	WHERE
		HF.id_league = ' || _id_league || _year_condition || _position_condition || '
	ORDER BY
		YR.id DESC, PR.last_name';
	
	RETURN  _c;
end;
$BODY$;


CREATE OR REPLACE FUNCTION public._merge(
	_alias character varying,
	_id1 integer,
	_id2 integer)
    RETURNS boolean
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
declare
begin
	IF _alias = 'CP' THEN
		UPDATE result SET id_championship = _id2 WHERE id_championship = _id1;
		UPDATE record SET id_championship = _id2 WHERE id_championship = _id1;
		DELETE FROM championship WHERE id = _id1;
	ELSIF _alias = 'CT' THEN
		UPDATE complex SET id_city = _id2 WHERE id_city = _id1;
		UPDATE olympics SET id_city = _id2 WHERE id_city = _id1;
		UPDATE result SET id_city1 = _id2 WHERE id_city1 = _id1;
		UPDATE result SET id_city2 = _id2 WHERE id_city2 = _id1;
		UPDATE record SET id_city = _id2 WHERE id_city = _id1;
		UPDATE round SET id_city = _id2 WHERE id_city = _id1;
		DELETE FROM city WHERE id = _id1;
	ELSIF _alias = 'CN' THEN
		UPDATE city SET id_country = _id2 WHERE id_country = _id1;
		UPDATE olympic_ranking SET id_country = _id2 WHERE id_country = _id1;
		UPDATE athlete SET id_country = _id2 WHERE id_country = _id1;
		UPDATE round SET id_rank1 = _id2 WHERE id_rank1 = _id1 AND id_result_type=99;
		UPDATE round SET id_rank2 = _id2 WHERE id_rank2 = _id1 AND id_result_type=99;
		UPDATE round SET id_rank3 = _id2 WHERE id_rank3 = _id1 AND id_result_type=99;
		UPDATE result SET id_rank1 = _id2 WHERE id_rank1 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE result SET id_rank2 = _id2 WHERE id_rank2 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE result SET id_rank3 = _id2 WHERE id_rank3 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE result SET id_rank4 = _id2 WHERE id_rank4 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE result SET id_rank5 = _id2 WHERE id_rank5 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE result SET id_rank6 = _id2 WHERE id_rank6 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE result SET id_rank7 = _id2 WHERE id_rank7 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE result SET id_rank8 = _id2 WHERE id_rank8 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE result SET id_rank9 = _id2 WHERE id_rank9 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE result SET id_rank10 = _id2 WHERE id_rank10 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=99) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=99));
		UPDATE record SET id_rank1 = _id2 WHERE id_rank1 = _id1 AND id IN (SELECT RC.id FROM record RC LEFT JOIN event EV ON RC.id_event = EV.id LEFT JOIN event SE ON RC.id_subevent = SE.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=99) OR (RC.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE record SET id_rank2 = _id2 WHERE id_rank2 = _id1 AND id IN (SELECT RC.id FROM record RC LEFT JOIN event EV ON RC.id_event = EV.id LEFT JOIN event SE ON RC.id_subevent = SE.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=99) OR (RC.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE record SET id_rank3 = _id2 WHERE id_rank3 = _id1 AND id IN (SELECT RC.id FROM record RC LEFT JOIN event EV ON RC.id_event = EV.id LEFT JOIN event SE ON RC.id_subevent = SE.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=99) OR (RC.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE record SET id_rank4 = _id2 WHERE id_rank4 = _id1 AND id IN (SELECT RC.id FROM record RC LEFT JOIN event EV ON RC.id_event = EV.id LEFT JOIN event SE ON RC.id_subevent = SE.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=99) OR (RC.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE record SET id_rank5 = _id2 WHERE id_rank5 = _id1 AND id IN (SELECT RC.id FROM record RC LEFT JOIN event EV ON RC.id_event = EV.id LEFT JOIN event SE ON RC.id_subevent = SE.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=99) OR (RC.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE team SET id_country = _id2 WHERE id_country = _id1;
		DELETE FROM country WHERE id = _id1;
	ELSIF _alias = 'CX' THEN
		UPDATE result SET id_complex1 = _id2 WHERE id_complex1 = _id1;
		UPDATE result SET id_complex2 = _id2 WHERE id_complex2 = _id1;
		UPDATE round SET id_complex = _id2 WHERE id_complex = _id1;
		UPDATE team_stadium SET id_complex = _id2 WHERE id_complex = _id1;
		DELETE FROM complex WHERE id = _id1;
	ELSIF _alias = 'EV' THEN
		UPDATE result SET id_event = _id2 WHERE id_event = _id1;
		UPDATE result SET id_subevent = _id2 WHERE id_subevent = _id1;
		UPDATE result SET id_subevent2 = _id2 WHERE id_subevent2 = _id1;
		UPDATE record SET id_event = _id2 WHERE id_event = _id1;
		UPDATE record SET id_subevent = _id2 WHERE id_subevent = _id1;
		DELETE FROM event WHERE id = _id1;
	ELSIF _alias = 'PR' THEN
		UPDATE hall_of_fame SET id_person = _id2 WHERE id_person = _id1;
		UPDATE round SET id_rank1 = _id2 WHERE id_rank1 = _id1 AND id_result_type<10;
		UPDATE round SET id_rank2 = _id2 WHERE id_rank2 = _id1 AND id_result_type<10;
		UPDATE round SET id_rank3 = _id2 WHERE id_rank3 = _id1 AND id_result_type<10;
		UPDATE result SET id_rank1 = _id2 WHERE id_rank1 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE result SET id_rank2 = _id2 WHERE id_rank2 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE result SET id_rank3 = _id2 WHERE id_rank3 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE result SET id_rank4 = _id2 WHERE id_rank4 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE result SET id_rank5 = _id2 WHERE id_rank5 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE result SET id_rank6 = _id2 WHERE id_rank6 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE result SET id_rank7 = _id2 WHERE id_rank7 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE result SET id_rank8 = _id2 WHERE id_rank8 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE result SET id_rank9 = _id2 WHERE id_rank9 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE result SET id_rank10 = _id2 WHERE id_rank10 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number<10) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number<10) OR (RS.id_subevent2 IS NOT NULL AND TP3.number<10));
		UPDATE record SET id_rank1 = _id2 WHERE id_rank1 = _id1 AND id IN (SELECT RC.id FROM record RC LEFT JOIN event EV ON RC.id_event = EV.id LEFT JOIN event SE ON RC.id_subevent = SE.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number<10) OR (RC.id_subevent IS NOT NULL AND TP2.number<10));
		UPDATE record SET id_rank2 = _id2 WHERE id_rank2 = _id1 AND id IN (SELECT RC.id FROM record RC LEFT JOIN event EV ON RC.id_event = EV.id LEFT JOIN event SE ON RC.id_subevent = SE.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number<10) OR (RC.id_subevent IS NOT NULL AND TP2.number<10));
		UPDATE record SET id_rank3 = _id2 WHERE id_rank3 = _id1 AND id IN (SELECT RC.id FROM record RC LEFT JOIN event EV ON RC.id_event = EV.id LEFT JOIN event SE ON RC.id_subevent = SE.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number<10) OR (RC.id_subevent IS NOT NULL AND TP2.number<10));
		UPDATE record SET id_rank4 = _id2 WHERE id_rank4 = _id1 AND id IN (SELECT RC.id FROM record RC LEFT JOIN event EV ON RC.id_event = EV.id LEFT JOIN event SE ON RC.id_subevent = SE.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number<10) OR (RC.id_subevent IS NOT NULL AND TP2.number<10));
		UPDATE record SET id_rank5 = _id2 WHERE id_rank5 = _id1 AND id IN (SELECT RC.id FROM record RC LEFT JOIN event EV ON RC.id_event = EV.id LEFT JOIN event SE ON RC.id_subevent = SE.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number<10) OR (RC.id_subevent IS NOT NULL AND TP2.number<10));
		UPDATE retired_number SET id_person = _id2 WHERE id_person = _id1;
		UPDATE _person_list SET id_person = _id2 WHERE id_person = _id1;
		DELETE FROM athlete WHERE id = _id1;
	ELSIF _alias = 'SP' THEN
		UPDATE athlete SET id_sport = _id2 WHERE id_sport = _id1;
		UPDATE result SET id_sport = _id2 WHERE id_sport = _id1;
		UPDATE record SET id_sport = _id2 WHERE id_sport = _id1;
		DELETE FROM sport WHERE id = _id1;
	ELSIF _alias = 'ST' THEN
		UPDATE city SET id_state = _id2 WHERE id_state = _id1;
		DELETE FROM state WHERE id = _id1;
	ELSIF _alias = 'TM' THEN
		UPDATE round SET id_rank1 = _id2 WHERE id_rank1 = _id1 AND id_result_type=50;
		UPDATE round SET id_rank2 = _id2 WHERE id_rank2 = _id1 AND id_result_type=50;
		UPDATE round SET id_rank3 = _id2 WHERE id_rank3 = _id1 AND id_result_type=50;
		UPDATE result SET id_rank1 = _id2 WHERE id_rank1 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE result SET id_rank2 = _id2 WHERE id_rank2 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE result SET id_rank3 = _id2 WHERE id_rank3 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE result SET id_rank4 = _id2 WHERE id_rank4 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE result SET id_rank5 = _id2 WHERE id_rank5 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE result SET id_rank6 = _id2 WHERE id_rank6 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE result SET id_rank7 = _id2 WHERE id_rank7 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE result SET id_rank8 = _id2 WHERE id_rank8 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE result SET id_rank9 = _id2 WHERE id_rank9 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE result SET id_rank10 = _id2 WHERE id_rank10 = _id1 AND id IN (SELECT RS.id FROM result RS LEFT JOIN event EV ON RS.id_event = EV.id LEFT JOIN event SE ON RS.id_subevent = SE.id LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id LEFT JOIN type TP3 ON SE2.id_type = TP3.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND RS.id_subevent2 IS NULL AND TP2.number=50) OR (RS.id_subevent2 IS NOT NULL AND TP3.number=50));
		UPDATE record SET id_rank1 = _id2 WHERE id_rank1 = _id1 AND id IN (SELECT RC.id FROM record RC LEFT JOIN event EV ON RC.id_event = EV.id LEFT JOIN event SE ON RC.id_subevent = SE.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=50) OR (RC.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE record SET id_rank2 = _id2 WHERE id_rank2 = _id1 AND id IN (SELECT RC.id FROM record RC LEFT JOIN event EV ON RC.id_event = EV.id LEFT JOIN event SE ON RC.id_subevent = SE.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=50) OR (RC.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE record SET id_rank3 = _id2 WHERE id_rank3 = _id1 AND id IN (SELECT RC.id FROM record RC LEFT JOIN event EV ON RC.id_event = EV.id LEFT JOIN event SE ON RC.id_subevent = SE.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=50) OR (RC.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE record SET id_rank4 = _id2 WHERE id_rank4 = _id1 AND id IN (SELECT RC.id FROM record RC LEFT JOIN event EV ON RC.id_event = EV.id LEFT JOIN event SE ON RC.id_subevent = SE.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=50) OR (RC.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE record SET id_rank5 = _id2 WHERE id_rank5 = _id1 AND id IN (SELECT RC.id FROM record RC LEFT JOIN event EV ON RC.id_event = EV.id LEFT JOIN event SE ON RC.id_subevent = SE.id LEFT JOIN type TP1 ON EV.id_type = TP1.id LEFT JOIN type TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=50) OR (RC.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE athlete SET id_team = _id2 WHERE id_team = _id1;
		UPDATE retired_number SET id_team = _id2 WHERE id_team = _id1;
		UPDATE team_stadium SET id_team = _id2 WHERE id_team = _id1;
		UPDATE win_loss SET id_team = _id2 WHERE id_team = _id1;
		DELETE FROM team WHERE id = _id1;
	END IF;
	UPDATE _external_link SET id_item = _id2 WHERE entity = _alias AND id_item = _id1;
	UPDATE _picture SET id_item = _id2 WHERE entity = _alias AND id_item = _id1;
	RETURN true;
end;
$BODY$;


