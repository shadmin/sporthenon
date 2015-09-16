CREATE OR REPLACE FUNCTION "GetCalendarResults"(_date1 character varying, _date2 character varying, _lang character varying)
  RETURNS SETOF "~RefItem" AS
$BODY$
declare
	_item "~RefItem"%rowtype;
	_c refcursor;
	__c refcursor;
	_query text;
	_index integer;
	_type1 smallint;
	_type2 smallint;
	_type3 smallint;
	_date1 varchar(10) := _date1;
	_date2 varchar(10) := _date2;
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
	_cn1 varchar(5);_cn2 varchar(5);_cn3 varchar(5);_cn4 varchar(5);_cn5 varchar(5);_cn6 varchar(5);
	_tm1 varchar(60);_tm2 varchar(60);_tm3 varchar(60);_tm4 varchar(60);_tm5 varchar(60);_tm6 varchar(60);
begin
	_query = 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number, TP3.number, RS.date1, RS.date2';
	_query = _query || ' FROM "Result" RS';
	_query = _query || ' LEFT JOIN "Year" YR ON RS.id_year = YR.id';
	_query = _query || ' LEFT JOIN "Sport" SP ON RS.id_sport = SP.id';
	_query = _query || ' LEFT JOIN "Championship" CP ON RS.id_championship = CP.id';
	_query = _query || ' LEFT JOIN "Event" EV ON RS.id_event = EV.id';
	_query = _query || ' LEFT JOIN "Event" SE ON RS.id_subevent = SE.id';
	_query = _query || ' LEFT JOIN "Event" SE2 ON RS.id_subevent2 = SE2.id';
	_query = _query || ' LEFT JOIN "Type" TP1 ON EV.id_type = TP1.id';
	_query = _query || ' LEFT JOIN "Type" TP2 ON SE.id_type = TP2.id';
	_query = _query || ' LEFT JOIN "Type" TP3 ON SE2.id_type = TP3.id';
	_query = _query || ' WHERE 1=1';
	_query = _query || ' AND to_date(RS.date2, ''DD/MM/YYYY'') >= to_date(''' || _date1 || ''', ''YYYYMMDD'')';
	_query = _query || ' AND to_date(RS.date2, ''DD/MM/YYYY'') <= to_date(''' || _date2 || ''', ''YYYYMMDD'')';
	_index := 1;
	OPEN _c FOR EXECUTE _query;
	LOOP
		FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2, _type3, _date1, _date2, _item.count1;
		EXIT WHEN NOT FOUND;
		IF _type3 IS NOT NULL THEN
			_type1 = _type3;
		ELSIF _type2 IS NOT NULL THEN
			_type1 = _type2;
		END IF;
		IF _type1 <= 10 THEN
			SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, CN3.code, TM3.label, PR4.last_name || (CASE WHEN length(PR4.first_name) > 0 THEN ', ' || PR4.first_name ELSE '' END), (CASE WHEN length(PR4.first_name) > 0 THEN PR4.first_name || ' ' ELSE '' END) || PR4.last_name, CN4.id, CN4.code, TM4.label, PR5.last_name || (CASE WHEN length(PR5.first_name) > 0 THEN ', ' || PR5.first_name ELSE '' END), (CASE WHEN length(PR5.first_name) > 0 THEN PR5.first_name || ' ' ELSE '' END) || PR5.last_name, CN5.id, CN5.code, TM5.label, PR6.last_name || (CASE WHEN length(PR6.first_name) > 0 THEN ', ' || PR6.first_name ELSE '' END), (CASE WHEN length(PR6.first_name) > 0 THEN PR6.first_name || ' ' ELSE '' END) || PR6.last_name, CN6.id, CN6.code, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
			INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.label_rel23, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.label_rel24, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.label_rel25, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt3, _item.txt4
			FROM "Result" RS LEFT JOIN "Athlete" PR1 ON RS.id_rank1 = PR1.id LEFT JOIN "Athlete" PR2 ON RS.id_rank2 = PR2.id LEFT JOIN "Athlete" PR3 ON RS.id_rank3 = PR3.id LEFT JOIN "Athlete" PR4 ON RS.id_rank4 = PR4.id LEFT JOIN "Athlete" PR5 ON RS.id_rank5 = PR5.id LEFT JOIN "Athlete" PR6 ON RS.id_rank6 = PR6.id LEFT JOIN "Country" CN1 ON PR1.id_country = CN1.id LEFT JOIN "Country" CN2 ON PR2.id_country = CN2.id LEFT JOIN "Country" CN3 ON PR3.id_country = CN3.id LEFT JOIN "Country" CN4 ON PR4.id_country = CN4.id LEFT JOIN "Country" CN5 ON PR5.id_country = CN5.id LEFT JOIN "Country" CN6 ON PR6.id_country = CN6.id LEFT JOIN "Team" TM1 ON PR1.id_team = TM1.id LEFT JOIN "Team" TM2 ON PR2.id_team = TM2.id LEFT JOIN "Team" TM3 ON PR3.id_team = TM3.id LEFT JOIN "Team" TM4 ON PR4.id_team = TM4.id LEFT JOIN "Team" TM5 ON PR5.id_team = TM5.id LEFT JOIN "Team" TM6 ON PR6.id_team = TM6.id
			WHERE RS.id = _item.id_item;
			IF _cn1 IS NOT NULL THEN _item.label_rel6 = _item.label_rel6 || ' (' || _cn1 || ')';
			ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 = _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
			IF _cn2 IS NOT NULL THEN _item.label_rel7 = _item.label_rel7 || ' (' || _cn2 || ')';
			ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 = _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
			IF _cn3 IS NOT NULL THEN _item.label_rel8 = _item.label_rel8 || ' (' || _cn3 || ')';
			ELSIF _tm3 IS NOT NULL THEN _item.label_rel8 = _item.label_rel8 || ' (' || _tm3 || ')'; END IF;
			IF _cn4 IS NOT NULL THEN _item.label_rel9 = _item.label_rel9 || ' (' || _cn4 || ')';
			ELSIF _tm4 IS NOT NULL THEN _item.label_rel9 = _item.label_rel9 || ' (' || _tm4 || ')'; END IF;
			IF _cn5 IS NOT NULL THEN _item.label_rel10 = _item.label_rel10 || ' (' || _cn5 || ')';
			ELSIF _tm5 IS NOT NULL THEN _item.label_rel10 = _item.label_rel10 || ' (' || _tm5 || ')'; END IF;
			IF _cn6 IS NOT NULL THEN _item.label_rel11 = _item.label_rel11 || ' (' || _cn6 || ')';
			ELSIF _tm6 IS NOT NULL THEN _item.label_rel11 = _item.label_rel11 || ' (' || _tm6 || ')'; END IF;
			IF _type1 = 4 OR _item.txt3 = '#DOUBLE#' THEN
				_item.txt4 = '1-2/3-4/5-6';
			ELSIF _type1 = 5 OR _item.txt3 = '#TRIPLE#' THEN
				_item.txt4 = '1-3/4-6/7-9';
			END IF;
			_item.comment = 'PR';
		ELSIF _type1 = 50 THEN
			SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, NULL, NULL, NULL, NULL, NULL, NULL, RS.result1, RS.result2, RS.comment, RS.exa
			INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4
			FROM "Result" RS LEFT JOIN "Team" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "Team" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "Team" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "Team" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "Team" TM5 ON RS.id_rank5 = TM5.id LEFT JOIN "Team" TM6 ON RS.id_rank6 = TM6.id
			WHERE RS.id = _item.id_item;
			_item.comment = 'TM';
		ELSIF _type1 = 99 THEN
			_query = 'SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN4.label' || _lang || ', CN5.label' || _lang || ', CN6.label' || _lang || ', CN1.label, CN2.label, CN3.label, CN4.label, CN5.label, CN6.label, RS.result1, RS.result2, RS.comment, RS.exa';
			_query = _query || ' FROM "Result" RS LEFT JOIN "Country" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "Country" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "Country" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "Country" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "Country" CN5 ON RS.id_rank5 = CN5.id LEFT JOIN "Country" CN6 ON RS.id_rank6 = CN6.id';
			_query = _query || ' WHERE RS.id = ' || _item.id_item;
			OPEN __c FOR EXECUTE _query;
			FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4;
			CLOSE __c;
			_item.comment = 'CN';
		END IF;
		_item.date1 := to_date(_date1, 'DD/MM/YYYY');
		_item.date2 := to_date(_date2, 'DD/MM/YYYY');
		_item.id = _index;
		RETURN NEXT _item;
		_index = _index + 1;
	END LOOP;
	CLOSE _c;

	RETURN;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;

  
  
  
  
  CREATE OR REPLACE FUNCTION "GetCalendarResults"(_date1 character varying, _date2 character varying, _lang character varying)
  RETURNS SETOF "~RefItem" AS
$BODY$
declare
	_item "~RefItem"%rowtype;
	_c refcursor;
	__c refcursor;
	_query text;
	_index integer;
	_type1 smallint;
	_type2 smallint;
	_type3 smallint;
	_date1 varchar(10) := _date1;
	_date2 varchar(10) := _date2;
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
	_cn1 varchar(5);_cn2 varchar(5);_cn3 varchar(5);_cn4 varchar(5);_cn5 varchar(5);_cn6 varchar(5);
	_tm1 varchar(60);_tm2 varchar(60);_tm3 varchar(60);_tm4 varchar(60);_tm5 varchar(60);_tm6 varchar(60);
begin
	_query = 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number, TP3.number, RS.date1, RS.date2';
	_query = _query || ' FROM "Result" RS';
	_query = _query || ' LEFT JOIN "Year" YR ON RS.id_year = YR.id';
	_query = _query || ' LEFT JOIN "Sport" SP ON RS.id_sport = SP.id';
	_query = _query || ' LEFT JOIN "Championship" CP ON RS.id_championship = CP.id';
	_query = _query || ' LEFT JOIN "Event" EV ON RS.id_event = EV.id';
	_query = _query || ' LEFT JOIN "Event" SE ON RS.id_subevent = SE.id';
	_query = _query || ' LEFT JOIN "Event" SE2 ON RS.id_subevent2 = SE2.id';
	_query = _query || ' LEFT JOIN "Type" TP1 ON EV.id_type = TP1.id';
	_query = _query || ' LEFT JOIN "Type" TP2 ON SE.id_type = TP2.id';
	_query = _query || ' LEFT JOIN "Type" TP3 ON SE2.id_type = TP3.id';
	_query = _query || ' WHERE 1=1';
	_query = _query || ' AND to_date(RS.date2, ''DD/MM/YYYY'') >= to_date(''' || _date1 || ''', ''YYYYMMDD'')';
	_query = _query || ' AND to_date(RS.date2, ''DD/MM/YYYY'') <= to_date(''' || _date2 || ''', ''YYYYMMDD'')';
	_index := 1;
	OPEN _c FOR EXECUTE _query;
	LOOP
		FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2, _type3, _date1, _date2, _item.count1;
		EXIT WHEN NOT FOUND;
		IF _type3 IS NOT NULL THEN
			_type1 = _type3;
		ELSIF _type2 IS NOT NULL THEN
			_type1 = _type2;
		END IF;
		IF _type1 <= 10 THEN
			SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, CN3.code, TM3.label, PR4.last_name || (CASE WHEN length(PR4.first_name) > 0 THEN ', ' || PR4.first_name ELSE '' END), (CASE WHEN length(PR4.first_name) > 0 THEN PR4.first_name || ' ' ELSE '' END) || PR4.last_name, CN4.id, CN4.code, TM4.label, PR5.last_name || (CASE WHEN length(PR5.first_name) > 0 THEN ', ' || PR5.first_name ELSE '' END), (CASE WHEN length(PR5.first_name) > 0 THEN PR5.first_name || ' ' ELSE '' END) || PR5.last_name, CN5.id, CN5.code, TM5.label, PR6.last_name || (CASE WHEN length(PR6.first_name) > 0 THEN ', ' || PR6.first_name ELSE '' END), (CASE WHEN length(PR6.first_name) > 0 THEN PR6.first_name || ' ' ELSE '' END) || PR6.last_name, CN6.id, CN6.code, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
			INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.label_rel23, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.label_rel24, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.label_rel25, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt3, _item.txt4
			FROM "Result" RS LEFT JOIN "Athlete" PR1 ON RS.id_rank1 = PR1.id LEFT JOIN "Athlete" PR2 ON RS.id_rank2 = PR2.id LEFT JOIN "Athlete" PR3 ON RS.id_rank3 = PR3.id LEFT JOIN "Athlete" PR4 ON RS.id_rank4 = PR4.id LEFT JOIN "Athlete" PR5 ON RS.id_rank5 = PR5.id LEFT JOIN "Athlete" PR6 ON RS.id_rank6 = PR6.id LEFT JOIN "Country" CN1 ON PR1.id_country = CN1.id LEFT JOIN "Country" CN2 ON PR2.id_country = CN2.id LEFT JOIN "Country" CN3 ON PR3.id_country = CN3.id LEFT JOIN "Country" CN4 ON PR4.id_country = CN4.id LEFT JOIN "Country" CN5 ON PR5.id_country = CN5.id LEFT JOIN "Country" CN6 ON PR6.id_country = CN6.id LEFT JOIN "Team" TM1 ON PR1.id_team = TM1.id LEFT JOIN "Team" TM2 ON PR2.id_team = TM2.id LEFT JOIN "Team" TM3 ON PR3.id_team = TM3.id LEFT JOIN "Team" TM4 ON PR4.id_team = TM4.id LEFT JOIN "Team" TM5 ON PR5.id_team = TM5.id LEFT JOIN "Team" TM6 ON PR6.id_team = TM6.id
			WHERE RS.id = _item.id_item;
			IF _cn1 IS NOT NULL THEN _item.label_rel6 = _item.label_rel6 || ' (' || _cn1 || ')';
			ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 = _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
			IF _cn2 IS NOT NULL THEN _item.label_rel7 = _item.label_rel7 || ' (' || _cn2 || ')';
			ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 = _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
			IF _cn3 IS NOT NULL THEN _item.label_rel8 = _item.label_rel8 || ' (' || _cn3 || ')';
			ELSIF _tm3 IS NOT NULL THEN _item.label_rel8 = _item.label_rel8 || ' (' || _tm3 || ')'; END IF;
			IF _cn4 IS NOT NULL THEN _item.label_rel9 = _item.label_rel9 || ' (' || _cn4 || ')';
			ELSIF _tm4 IS NOT NULL THEN _item.label_rel9 = _item.label_rel9 || ' (' || _tm4 || ')'; END IF;
			IF _cn5 IS NOT NULL THEN _item.label_rel10 = _item.label_rel10 || ' (' || _cn5 || ')';
			ELSIF _tm5 IS NOT NULL THEN _item.label_rel10 = _item.label_rel10 || ' (' || _tm5 || ')'; END IF;
			IF _cn6 IS NOT NULL THEN _item.label_rel11 = _item.label_rel11 || ' (' || _cn6 || ')';
			ELSIF _tm6 IS NOT NULL THEN _item.label_rel11 = _item.label_rel11 || ' (' || _tm6 || ')'; END IF;
			IF _type1 = 4 OR _item.txt3 = '#DOUBLE#' THEN
				_item.txt4 = '1-2/3-4/5-6';
			ELSIF _type1 = 5 OR _item.txt3 = '#TRIPLE#' THEN
				_item.txt4 = '1-3/4-6/7-9';
			END IF;
			_item.comment = 'PR';
		ELSIF _type1 = 50 THEN
			SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, NULL, NULL, NULL, NULL, NULL, NULL, RS.result1, RS.result2, RS.comment, RS.exa
			INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4
			FROM "Result" RS LEFT JOIN "Team" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "Team" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "Team" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "Team" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "Team" TM5 ON RS.id_rank5 = TM5.id LEFT JOIN "Team" TM6 ON RS.id_rank6 = TM6.id
			WHERE RS.id = _item.id_item;
			_item.comment = 'TM';
		ELSIF _type1 = 99 THEN
			_query = 'SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN4.label' || _lang || ', CN5.label' || _lang || ', CN6.label' || _lang || ', CN1.label, CN2.label, CN3.label, CN4.label, CN5.label, CN6.label, RS.result1, RS.result2, RS.comment, RS.exa';
			_query = _query || ' FROM "Result" RS LEFT JOIN "Country" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "Country" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "Country" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "Country" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "Country" CN5 ON RS.id_rank5 = CN5.id LEFT JOIN "Country" CN6 ON RS.id_rank6 = CN6.id';
			_query = _query || ' WHERE RS.id = ' || _item.id_item;
			OPEN __c FOR EXECUTE _query;
			FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4;
			CLOSE __c;
			_item.comment = 'CN';
		END IF;
		IF _date1 IS NOT NULL AND _date1 <> '' THEN
			_item.date1 := to_date(_date1, 'DD/MM/YYYY');
		END IF;
		IF _date2 IS NOT NULL AND _date2 <> '' THEN
			_item.date2 := to_date(_date2, 'DD/MM/YYYY');
		END IF;
		_item.id = _index;
		RETURN NEXT _item;
		_index = _index + 1;
	END LOOP;
	CLOSE _c;

	RETURN;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
  
  
  
  
  ALTER TABLE "Athlete" add photo_copyright varchar(100);
ALTER TABLE "Complex" add photo_copyright varchar(100);
ALTER TABLE "City" add photo_copyright varchar(100);





ALTER TABLE "Result" add photo_copyright varchar(100);


