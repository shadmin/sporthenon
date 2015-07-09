CREATE OR REPLACE FUNCTION "GET_MEDAL_COUNT"(_entity character varying, _id_sport integer, _idlist character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_ids integer[];
	_type integer;
	_sport_txt varchar(3);
	_index smallint;
	_nfl_cp1 smallint;
	_nfl_cp2 smallint;
	_nba_cp smallint;
	_nhl_cp smallint;
	_mlb_cp smallint;
	_label_tennis_slam1 varchar(45);
	_label_tennis_slam2 varchar(45);
	_label_tennis_slam3 varchar(45);
	_label_tennis_slam4 varchar(45);
	_label_golf_slam1 varchar(45);
	_label_golf_slam2 varchar(45);
	_label_golf_slam3 varchar(45);
	_label_golf_slam4 varchar(45);
	_label_cycling_tour1 varchar(45);
	_label_cycling_tour2 varchar(45);
	_label_cycling_tour3 varchar(45);
begin
	_ids := regexp_split_to_array(_idlist, E'\\-');	
	_label_tennis_slam1 := 'Australian Open';
	_label_tennis_slam2 := 'French Open';
	_label_tennis_slam3 := 'Wimbledon';
	_label_tennis_slam4 := 'US Open';
	_label_golf_slam1 := 'Masters';
	_label_golf_slam2 := 'US Open';
	_label_golf_slam3 := 'Open Championship';
	_label_golf_slam4 := 'PGA Championship';
	_label_cycling_tour1 := 'Giro d''Italia';
	_label_cycling_tour2 := 'Tour de France';
	_label_cycling_tour3 := 'Vuelta a España';
	_nfl_cp1 := 454;
	_nfl_cp2 := 453;
	_nba_cp := 530;
	_nhl_cp := 573;
	_mlb_cp := 624;
	_index := 1;
	_sport_txt := CAST(_id_sport AS varchar);
	IF (_entity = 'PR') THEN
		_type := 10;
	ELSIF (_entity = 'TM') THEN
		_type := 50;
	ELSIF (_entity = 'CN') THEN
		_type := 99;
	END IF;
	-- Olympics
	IF _sport_txt !~ '5|18|20|23|29|33|36|37|45' AND _type <> 50 THEN
		_item.id := _index;
		_item.label := 'OLYMP';
		_item.txt1 := '#GOLD#';
		_item.txt2 := '#SILVER#';
		_item.txt3 := '#BRONZE#';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank1 = ANY(_ids) OR (id_rank2 = ANY(_ids) AND exa ~* '.*1-(2|3|4|5|6).*') OR (id_rank3 = ANY(_ids) AND exa ~* '.*1-(3|4|5|6).*'));
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank2 = ANY(_ids) OR (id_rank3 = ANY(_ids) AND exa ~* '.*2-(3|4|5|6).*') OR (id_rank4 = ANY(_ids) AND exa ~* '.*2-(4|5|6).*'));
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank3 = ANY(_ids) OR (id_rank4 = ANY(_ids) AND exa ~* '.*3-(4|5|6).*') OR (id_rank5 = ANY(_ids) AND exa ~* '.*3-(5|6).*'));
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- World Championships
	IF _sport_txt !~ '20|22|23|26|29' AND _type <> 50 THEN
		_item.id := _index;	
		_item.label := 'WORLDCP';
		IF _sport_txt ~ '5|18|33|36' THEN
			_item.txt1 := 'rank.1';
			_item.txt2 := 'rank.2';
			_item.txt3 := 'rank.3';
		ELSE
			_item.txt1 := '#GOLD#';
			_item.txt2 := '#SILVER#';
			_item.txt3 := '#BRONZE#';
		END IF;
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank1 = ANY(_ids) OR (id_rank2 = ANY(_ids) AND exa ~* '.*1-(2|3|4|5|6).*') OR (id_rank3 = ANY(_ids) AND exa ~* '.*1-(3|4|5|6).*'));
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank2 = ANY(_ids) OR (id_rank3 = ANY(_ids) AND exa ~* '.*2-(3|4|5|6).*') OR (id_rank4 = ANY(_ids) AND exa ~* '.*2-(4|5|6).*'));
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank3 = ANY(_ids) OR (id_rank4 = ANY(_ids) AND exa ~* '.*3-(4|5|6).*') OR (id_rank5 = ANY(_ids) AND exa ~* '.*3-(5|6).*'));
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Grand Slam (Tennis)
	IF _id_sport = 22 AND _type <= 10 THEN
		_item.id := _index;
		_item.label := 'GSLAM';
		_item.txt1 := 'Aus';
		_item.txt2 := 'RG';
		_item.txt3 := 'Wim';
		_item.txt4 := 'US';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam1 OR SE2.label=_label_tennis_slam1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam2 OR SE2.label=_label_tennis_slam2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam3 OR SE2.label=_label_tennis_slam3) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count4 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam4 OR SE2.label=_label_tennis_slam4) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Majors (Golf)
	IF _id_sport = 20 AND _type <= 10 THEN
		_item.id := _index;
		_item.label := 'MAJORS';
		_item.txt1 := 'Mas';
		_item.txt2 := 'US';
		_item.txt3 := 'Brit';
		_item.txt4 := 'PGA';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam1 OR SE2.label=_label_golf_slam1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam2 OR SE2.label=_label_golf_slam2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam3 OR SE2.label=_label_golf_slam3) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count4 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam4 OR SE2.label=_label_golf_slam4) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Major Tours (Cycling)
	IF _id_sport = 19 AND _type <= 10 THEN
		_item.id := _index;
		_item.label := 'MTOURS';
		_item.txt1 := 'Gir';
		_item.txt2 := 'Tour';
		_item.txt3 := 'Vuel';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (EV.label=_label_cycling_tour1 OR SE.label=_label_cycling_tour1 OR SE2.label=_label_cycling_tour1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (EV.label=_label_cycling_tour2 OR SE.label=_label_cycling_tour2 OR SE2.label=_label_cycling_tour2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (EV.label=_label_cycling_tour3 OR SE.label=_label_cycling_tour3 OR SE2.label=_label_cycling_tour3) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- NFL Titles
	IF _id_sport = 23 AND _type = 50 THEN
		-- (Super Bowl)
		_item.id := _index;
		_item.label := 'NFLCP1';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp1 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp1 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp1) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
		-- (NFL Championships)
		_item.id := _index;
		_item.label := 'NFLCP2';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp2 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp2 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp2) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	-- NBA Titles
	IF _id_sport = 24 AND _type = 50 THEN
		_item.id := _index;
		_item.label := 'NBACP';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nba_cp AND id_subevent2 IS NULL) OR id_subevent2=_nba_cp) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nba_cp AND id_subevent2 IS NULL) OR id_subevent2=_nba_cp) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	-- NHL Titles
	IF _id_sport = 25 AND _type = 50 THEN
		_item.id := _index;
		_item.label := 'NHLCP';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nhl_cp AND id_subevent2 IS NULL) OR id_subevent2=_nhl_cp) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nhl_cp AND id_subevent2 IS NULL) OR id_subevent2=_nhl_cp) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	-- MLB Titles
	IF _id_sport = 26 AND _type = 50 THEN
		_item.id := _index;
		_item.label := 'MLBCP';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_mlb_cp AND id_subevent2 IS NULL) OR id_subevent2=_mlb_cp) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_mlb_cp AND id_subevent2 IS NULL) OR id_subevent2=_mlb_cp) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	RETURN;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
  
  
  
  
  CREATE TABLE "~CONTRIBUTION"
(
  id integer NOT NULL primary key,
id_item integer not null,
  id_member integer not null,
  type char,
  date timestamp
);

CREATE SEQUENCE "~SQ_CONTRIBUTION";


insert into "~CONTRIBUTION"
select nextval('"~SQ_CONTRIBUTION"'), id, id_member, 'A', first_update from "RESULT";

insert into "~CONTRIBUTION"
select nextval('"~SQ_CONTRIBUTION"'), id, id_member, 'U', last_update from "RESULT" where first_update<>last_update;

CREATE OR REPLACE FUNCTION "~CONTRIBUTORS"()
  RETURNS refcursor AS
$BODY$
declare
	_c refcursor;
begin
	OPEN _c FOR EXECUTE
	'SELECT MB.id AS id, login, public_name AS name, SUM(CASE WHEN CB.type=''A'' THEN 1 ELSE 0 END) count_a, SUM(CASE WHEN CB.type=''U'' THEN 1 ELSE 0 END) count_u
		FROM "~MEMBER" MB LEFT JOIN "~CONTRIBUTION" CB ON MB.ID=CB.ID_MEMBER
		GROUP BY MB.id, login, public_name
		ORDER BY 4 desc';
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;




CREATE OR REPLACE FUNCTION "GET_MEDAL_COUNT"(_entity character varying, _id_sport integer, _idlist character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_ids integer[];
	_type integer;
	_sport_txt varchar(3);
	_index smallint;
	_nfl_cp1 smallint;
	_nfl_cp2 smallint;
	_nba_cp smallint;
	_nhl_cp smallint;
	_mlb_cp smallint;
	_label_tennis_slam1 varchar(45);
	_label_tennis_slam2 varchar(45);
	_label_tennis_slam3 varchar(45);
	_label_tennis_slam4 varchar(45);
	_label_golf_slam1 varchar(45);
	_label_golf_slam2 varchar(45);
	_label_golf_slam3 varchar(45);
	_label_golf_slam4 varchar(45);
	_label_cycling_tour1 varchar(45);
	_label_cycling_tour2 varchar(45);
	_label_cycling_tour3 varchar(45);
begin
	_ids := regexp_split_to_array(_idlist, E'\\-');	
	_label_tennis_slam1 := 'Australian Open';
	_label_tennis_slam2 := 'French Open';
	_label_tennis_slam3 := 'Wimbledon';
	_label_tennis_slam4 := 'US Open';
	_label_golf_slam1 := 'Masters';
	_label_golf_slam2 := 'US Open';
	_label_golf_slam3 := 'Open Championship';
	_label_golf_slam4 := 'PGA Championship';
	_label_cycling_tour1 := 'Giro d''Italia';
	_label_cycling_tour2 := 'Tour de France';
	_label_cycling_tour3 := 'Vuelta a España';
	_nfl_cp1 := 454;
	_nfl_cp2 := 453;
	_nba_cp := 530;
	_nhl_cp := 573;
	_mlb_cp := 624;
	_index := 1;
	_sport_txt := CAST(_id_sport AS varchar);
	IF (_entity = 'PR') THEN
		_type := 10;
	ELSIF (_entity = 'TM') THEN
		_type := 50;
	ELSIF (_entity = 'CN') THEN
		_type := 99;
	END IF;
	-- Olympics
	IF _sport_txt !~ '5|18|20|23|29|33|36|37|45' AND _type <> 50 THEN
		_item.id := _index;
		_item.label := 'OLYMP';
		_item.txt1 := '#GOLD#';
		_item.txt2 := '#SILVER#';
		_item.txt3 := '#BRONZE#';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank1 = ANY(_ids) OR (id_rank2 = ANY(_ids) AND exa ~* '.*1-(2|3|4|5|6).*') OR (id_rank3 = ANY(_ids) AND exa ~* '.*1-(3|4|5|6).*'));
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank2 = ANY(_ids) OR (id_rank3 = ANY(_ids) AND exa ~* '.*2-(3|4|5|6).*') OR (id_rank4 = ANY(_ids) AND exa ~* '.*2-(4|5|6).*'));
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank3 = ANY(_ids) OR (id_rank4 = ANY(_ids) AND exa ~* '.*3-(4|5|6).*') OR (id_rank5 = ANY(_ids) AND exa ~* '.*3-(5|6).*'));
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- World Championships
	IF _sport_txt !~ '20|22|23|26|29' AND _type <= 10 THEN
		_item.id := _index;	
		_item.label := 'WORLDCP';
		IF _sport_txt ~ '5|18|33|36' THEN
			_item.txt1 := 'rank.1';
			_item.txt2 := 'rank.2';
			_item.txt3 := 'rank.3';
		ELSE
			_item.txt1 := '#GOLD#';
			_item.txt2 := '#SILVER#';
			_item.txt3 := '#BRONZE#';
		END IF;
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank1 = ANY(_ids) OR (id_rank2 = ANY(_ids) AND exa ~* '.*1-(2|3|4|5|6).*') OR (id_rank3 = ANY(_ids) AND exa ~* '.*1-(3|4|5|6).*'));
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank2 = ANY(_ids) OR (id_rank3 = ANY(_ids) AND exa ~* '.*2-(3|4|5|6).*') OR (id_rank4 = ANY(_ids) AND exa ~* '.*2-(4|5|6).*'));
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank3 = ANY(_ids) OR (id_rank4 = ANY(_ids) AND exa ~* '.*3-(4|5|6).*') OR (id_rank5 = ANY(_ids) AND exa ~* '.*3-(5|6).*'));
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Grand Slam (Tennis)
	IF _id_sport = 22 AND _type <= 10 THEN
		_item.id := _index;
		_item.label := 'GSLAM';
		_item.txt1 := 'Aus';
		_item.txt2 := 'RG';
		_item.txt3 := 'Wim';
		_item.txt4 := 'US';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam1 OR SE2.label=_label_tennis_slam1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam2 OR SE2.label=_label_tennis_slam2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam3 OR SE2.label=_label_tennis_slam3) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count4 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam4 OR SE2.label=_label_tennis_slam4) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Majors (Golf)
	IF _id_sport = 20 AND _type <= 10 THEN
		_item.id := _index;
		_item.label := 'MAJORS';
		_item.txt1 := 'Mas';
		_item.txt2 := 'US';
		_item.txt3 := 'Brit';
		_item.txt4 := 'PGA';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam1 OR SE2.label=_label_golf_slam1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam2 OR SE2.label=_label_golf_slam2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam3 OR SE2.label=_label_golf_slam3) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count4 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam4 OR SE2.label=_label_golf_slam4) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Major Tours (Cycling)
	IF _id_sport = 19 AND _type <= 10 THEN
		_item.id := _index;
		_item.label := 'MTOURS';
		_item.txt1 := 'Gir';
		_item.txt2 := 'Tour';
		_item.txt3 := 'Vuel';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (EV.label=_label_cycling_tour1 OR SE.label=_label_cycling_tour1 OR SE2.label=_label_cycling_tour1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (EV.label=_label_cycling_tour2 OR SE.label=_label_cycling_tour2 OR SE2.label=_label_cycling_tour2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (EV.label=_label_cycling_tour3 OR SE.label=_label_cycling_tour3 OR SE2.label=_label_cycling_tour3) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- NFL Titles
	IF _id_sport = 23 AND _type = 50 THEN
		-- (Super Bowl)
		_item.id := _index;
		_item.label := 'NFLCP1';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp1 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp1 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp1) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
		-- (NFL Championships)
		_item.id := _index;
		_item.label := 'NFLCP2';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp2 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp2 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp2) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	-- NBA Titles
	IF _id_sport = 24 AND _type = 50 THEN
		_item.id := _index;
		_item.label := 'NBACP';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nba_cp AND id_subevent2 IS NULL) OR id_subevent2=_nba_cp) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nba_cp AND id_subevent2 IS NULL) OR id_subevent2=_nba_cp) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	-- NHL Titles
	IF _id_sport = 25 AND _type = 50 THEN
		_item.id := _index;
		_item.label := 'NHLCP';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nhl_cp AND id_subevent2 IS NULL) OR id_subevent2=_nhl_cp) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nhl_cp AND id_subevent2 IS NULL) OR id_subevent2=_nhl_cp) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	-- MLB Titles
	IF _id_sport = 26 AND _type = 50 THEN
		_item.id := _index;
		_item.label := 'MLBCP';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_mlb_cp AND id_subevent2 IS NULL) OR id_subevent2=_mlb_cp) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_mlb_cp AND id_subevent2 IS NULL) OR id_subevent2=_mlb_cp) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	RETURN;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
  
  
  
  
  
  
  CREATE OR REPLACE FUNCTION "ENTITY_REF"(_entity character varying, _id integer, _entity_ref character varying, _limit character varying, _offset integer, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_c refcursor;
	__c refcursor;
	_query text;
	_link integer;
	_pr_list varchar(50);
	_tm_list varchar(50);
	_index integer;
	_type1 integer;
	_type2 integer;
	_type3 integer;
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
	_id11 integer;_id12 integer;_id13 integer;_id14 integer;
	_cn1 varchar(5);_cn2 varchar(5);_cn3 varchar(5);_cn4 varchar(5);_cn5 varchar(5);_cn6 varchar(5);
	_tm1 varchar(60);_tm2 varchar(60);_tm3 varchar(60);_tm4 varchar(60);_tm5 varchar(60);_tm6 varchar(60);
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'IF', _entity || '-' || _id, current_date);
	
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
	ELSIF _entity ~ 'TM' THEN
		SELECT LINK INTO _link FROM "TEAM" WHERE ID = _id AND (YEAR1 IS NULL OR YEAR1 = '');
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "TEAM" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_tm_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_tm_list := _tm_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_tm_list := cast(_id AS varchar);
		END IF;
	END IF;

	-- References in: [Draws] (MUST STAY AT FIRST)
	IF (_entity ~ 'CN|PR|TM' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT DR.id, DR.id_result, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, DR.id1_qf1, DR.id2_qf1, DR.id1_qf2, DR.id2_qf2, DR.id1_qf3, DR.id2_qf3, DR.id1_qf4, DR.id2_qf4, DR.id1_sf1, DR.id2_sf1, DR.id1_sf2, DR.id2_sf2, DR.id1_thd, DR.id2_thd, TP1.number, TP2.number, TP3.number FROM "DRAW" DR';
		_query := _query || ' LEFT JOIN "RESULT" RS ON DR.id_result = RS.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id';
		_query := _query || ' LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id';
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity = 'CN' THEN
			_query := _query || ' AND (DR.id1_sf1 <> ' || _id || ' AND DR.id1_sf2 <> ' || _id || ') AND (DR.id1_qf1 = ' || _id || ' OR DR.id2_qf1 = ' || _id || ' OR DR.id1_qf2 = ' || _id || ' OR DR.id2_qf2 = ' || _id || ' OR DR.id1_qf3 = ' || _id || ' OR DR.id2_qf3 = ' || _id || ' OR DR.id1_qf4 = ' || _id || ' OR DR.id2_qf4 = ' || _id || ' OR DR.id2_sf1 = ' || _id || ' OR DR.id2_sf2 = ' || _id || ' OR DR.id1_thd = ' || _id || ' OR DR.id2_thd = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _pr_list || ') AND DR.id1_sf2 NOT IN (' || _pr_list || ')) AND (DR.id1_qf1 IN (' || _pr_list || ') OR DR.id2_qf1 IN (' || _pr_list || ') OR DR.id1_qf2 IN (' || _pr_list || ') OR DR.id2_qf2 IN (' || _pr_list || ') OR DR.id1_qf3 IN (' || _pr_list || ') OR DR.id2_qf3 IN (' || _pr_list || ') OR DR.id1_qf4 IN (' || _pr_list || ') OR DR.id2_qf4 IN (' || _pr_list || ') OR DR.id2_sf1 IN (' || _pr_list || ') OR DR.id2_sf2 IN (' || _pr_list || ') OR DR.id1_thd IN (' || _pr_list || ') OR DR.id2_thd IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _tm_list || ') AND DR.id1_sf2 NOT IN (' || _tm_list || ')) AND (DR.id1_qf1 IN (' || _tm_list || ') OR DR.id2_qf1 IN (' || _tm_list || ') OR DR.id1_qf2 IN (' || _tm_list || ') OR DR.id2_qf2 IN (' || _tm_list || ') OR DR.id1_qf3 IN (' || _tm_list || ') OR DR.id2_qf3 IN (' || _tm_list || ') OR DR.id1_qf4 IN (' || _tm_list || ') OR DR.id2_qf4 IN (' || _tm_list || ') OR DR.id2_sf1 IN (' || _tm_list || ') OR DR.id2_sf2 IN (' || _tm_list || ') OR DR.id1_thd IN (' || _tm_list || ') OR DR.id2_thd IN (' || _tm_list || '))';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel10, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _id11, _id12, _id13, _id14, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF (_id13 = _id OR _id14 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)'))) THEN
				_item.txt2 := 'thd';
			ELSIF (_id9 = _id OR _id10 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)'))) THEN
				_item.txt2 := 'sf1';
			ELSIF (_id11 = _id OR _id12 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)'))) THEN
				_item.txt2 := 'sf2';
			ELSIF (_id1 = _id OR _id2 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)'))) THEN
				_item.txt2 := 'qf1';
			ELSIF (_id3 = _id OR _id4 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)'))) THEN
				_item.txt2 := 'qf2';
			ELSIF (_id5 = _id OR _id6 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)'))) THEN
				_item.txt2 := 'qf3';
			ELSIF (_id7 = _id OR _id8 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)'))) THEN
				_item.txt2 := 'qf4';
			END IF;
			IF _type1 <= 10 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN '', '' || PR1.first_name ELSE '''' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || '' '' ELSE '''' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN '', '' || PR2.first_name ELSE '''' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || '' '' ELSE '''' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, DR.result_' || _item.txt2;
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "PERSON" PR1 ON DR.id1_' || _item.txt2 || ' = PR1.id LEFT JOIN "PERSON" PR2 ON DR.id2_' || _item.txt2 || ' = PR2.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel8, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel9, _item.id_rel13, _cn2, _tm2, _item.txt1;
				CLOSE __c;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				_item.comment := 'PR';
			ELSIF _type1 = 50 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', TM1.label, TM2.label, NULL, NULL, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "TEAM" TM1 ON DR.id1_' || _item.txt2 || ' = TM1.id LEFT JOIN "TEAM" TM2 ON DR.id2_' || _item.txt2 || ' = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'TM';
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', CN1.label' || _lang || ', CN2.label' || _lang || ', CN1.label, CN2.label, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "COUNTRY" CN1 ON DR.id1_' || _item.txt2 || ' = CN1.id LEFT JOIN "COUNTRY" CN2 ON DR.id2_' || _item.txt2 || ' = CN2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'CN';
			END IF;
			_item.id = _index;
			_item.entity = 'RS';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Results]
	IF (_entity ~ 'CN|PR|TM|CP|EV|CT|SP|CX|OL|YR' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number, TP3.number FROM "RESULT" RS';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id';
		_query := _query || ' LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id';
		IF (_entity = 'OL') THEN
			_query := _query || ' LEFT JOIN "OLYMPICS" OL ON (OL.id_year = YR.id AND OL.type = SP.type)';
		END IF;
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RS.id_rank1 = ' || _id || ' OR RS.id_rank2 = ' || _id || ' OR RS.id_rank3 = ' || _id || ' OR RS.id_rank4 = ' || _id || ' OR RS.id_rank5 = ' || _id || ' OR RS.id_rank6 = ' || _id || ' OR RS.id_rank7 = ' || _id || ' OR RS.id_rank8 = ' || _id || ' OR RS.id_rank9 = ' || _id || ' OR RS.id_rank10 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RS.id_rank1 IN (' || _pr_list || ') OR RS.id_rank2 IN (' || _pr_list || ') OR RS.id_rank3 IN (' || _pr_list || ') OR RS.id_rank4 IN (' || _pr_list || ') OR RS.id_rank5 IN (' || _pr_list || ') OR RS.id_rank6 IN (' || _pr_list || ') OR RS.id_rank7 IN (' || _pr_list || ') OR RS.id_rank8 IN (' || _pr_list || ') OR RS.id_rank9 IN (' || _pr_list || ') OR RS.id_rank10 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RS.id_rank1 IN (' || _tm_list || ') OR RS.id_rank2 IN (' || _tm_list || ') OR RS.id_rank3 IN (' || _tm_list || ') OR RS.id_rank4 IN (' || _tm_list || ') OR RS.id_rank5 IN (' || _tm_list || ') OR RS.id_rank6 IN (' || _tm_list || ') OR RS.id_rank7 IN (' || _tm_list || ') OR RS.id_rank8 IN (' || _tm_list || ') OR RS.id_rank9 IN (' || _tm_list || ') OR RS.id_rank10 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND  (RS.id_city1 = ' || _id || ' OR RS.id_city2 = ' || _id || ')';
		ELSIF _entity = 'CX' THEN
			_query := _query || ' AND  (RS.id_complex1 = ' || _id || ' OR RS.id_complex2 = ' || _id || ')';
		ELSIF _entity = 'OL' THEN
			_query := _query || ' AND RS.id_championship=1 AND OL.id = ' || _id;
		ELSIF _entity = 'YR' THEN
			_query := _query || ' AND RS.id_year = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF _type1 <= 10 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, CN3.code, TM3.label, PR4.last_name || (CASE WHEN length(PR4.first_name) > 0 THEN ', ' || PR4.first_name ELSE '' END), (CASE WHEN length(PR4.first_name) > 0 THEN PR4.first_name || ' ' ELSE '' END) || PR4.last_name, CN4.id, CN4.code, TM4.label, PR5.last_name || (CASE WHEN length(PR5.first_name) > 0 THEN ', ' || PR5.first_name ELSE '' END), (CASE WHEN length(PR5.first_name) > 0 THEN PR5.first_name || ' ' ELSE '' END) || PR5.last_name, CN5.id, CN5.code, TM5.label, PR6.last_name || (CASE WHEN length(PR6.first_name) > 0 THEN ', ' || PR6.first_name ELSE '' END), (CASE WHEN length(PR6.first_name) > 0 THEN PR6.first_name || ' ' ELSE '' END) || PR6.last_name, CN6.id, CN6.code, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.label_rel23, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.label_rel24, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.label_rel25, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "PERSON" PR1 ON RS.id_rank1 = PR1.id LEFT JOIN "PERSON" PR2 ON RS.id_rank2 = PR2.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3 = PR3.id LEFT JOIN "PERSON" PR4 ON RS.id_rank4 = PR4.id LEFT JOIN "PERSON" PR5 ON RS.id_rank5 = PR5.id LEFT JOIN "PERSON" PR6 ON RS.id_rank6 = PR6.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "COUNTRY" CN3 ON PR3.id_country = CN3.id LEFT JOIN "COUNTRY" CN4 ON PR4.id_country = CN4.id LEFT JOIN "COUNTRY" CN5 ON PR5.id_country = CN5.id LEFT JOIN "COUNTRY" CN6 ON PR6.id_country = CN6.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id LEFT JOIN "TEAM" TM3 ON PR3.id_team = TM3.id LEFT JOIN "TEAM" TM4 ON PR4.id_team = TM4.id LEFT JOIN "TEAM" TM5 ON PR5.id_team = TM5.id LEFT JOIN "TEAM" TM6 ON PR6.id_team = TM6.id
				WHERE RS.id = _item.id_item;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				IF _cn3 IS NOT NULL THEN _item.label_rel8 := _item.label_rel8 || ' (' || _cn3 || ')';
				ELSIF _tm3 IS NOT NULL THEN _item.label_rel8 := _item.label_rel8 || ' (' || _tm3 || ')'; END IF;
				IF _cn4 IS NOT NULL THEN _item.label_rel9 := _item.label_rel9 || ' (' || _cn4 || ')';
				ELSIF _tm4 IS NOT NULL THEN _item.label_rel9 := _item.label_rel9 || ' (' || _tm4 || ')'; END IF;
				IF _cn5 IS NOT NULL THEN _item.label_rel10 := _item.label_rel10 || ' (' || _cn5 || ')';
				ELSIF _tm5 IS NOT NULL THEN _item.label_rel10 := _item.label_rel10 || ' (' || _tm5 || ')'; END IF;
				IF _cn6 IS NOT NULL THEN _item.label_rel11 := _item.label_rel11 || ' (' || _cn6 || ')';
				ELSIF _tm6 IS NOT NULL THEN _item.label_rel11 := _item.label_rel11 || ' (' || _tm6 || ')'; END IF;
				IF _type1 = 4 THEN
					_item.txt4 = '1-2/3-4/5-6';
				END IF;
				_item.comment := 'PR';
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, NULL, NULL, NULL, NULL, NULL, NULL, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "TEAM" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "TEAM" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "TEAM" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "TEAM" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "TEAM" TM5 ON RS.id_rank5 = TM5.id LEFT JOIN "TEAM" TM6 ON RS.id_rank6 = TM6.id
				WHERE RS.id = _item.id_item;
				_item.comment := 'TM';
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN4.label' || _lang || ', CN5.label' || _lang || ', CN6.label' || _lang || ', CN1.label, CN2.label, CN3.label, CN4.label, CN5.label, CN6.label, RS.result1, RS.result2, RS.comment, RS.exa';
				_query := _query || ' FROM "RESULT" RS LEFT JOIN "COUNTRY" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "COUNTRY" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "COUNTRY" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "COUNTRY" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "COUNTRY" CN5 ON RS.id_rank5 = CN5.id LEFT JOIN "COUNTRY" CN6 ON RS.id_rank6 = CN6.id';
				_query := _query || ' WHERE RS.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4;
				CLOSE __c;
				_item.comment := 'CN';
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
		_query := 'SELECT DISTINCT ON (PR.last_name, PR.first_name, CN.id, SP.id) PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "PERSON" PR';
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
		_query := _query || ' ORDER BY PR.last_name, PR.first_name, SP.id LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'PR';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Teams]
	IF (_entity ~ 'SP|CN' AND (_entity_ref = 'TM' OR _entity_ref = '')) THEN
		_query := 'SELECT TM.id, TM.label, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "TEAM" TM';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON TM.id_country = CN.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON TM.id_sport = SP.id';
		IF _entity = 'SP' THEN
			_query := _query || ' WHERE TM.id_sport = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query := _query || ' WHERE TM.id_country = ' || _id;
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', TM.label LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT CT.id, CT.label' || _lang || ', CT.label, CN.id, CN.label' || _lang || ', CN.label FROM "CITY" CT';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'CN' THEN
			_query := _query || ' WHERE CT.id_country = ' || _id;
		ELSIF _entity = 'ST' THEN
			_query := _query || ' WHERE CT.id_state = ' || _id;
		END IF;
		_query := _query || ' ORDER BY CT.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.label_rel2;
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
		_query := 'SELECT CX.id, CX.label' || _lang || ', CX.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label FROM "COMPLEX" CX';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' WHERE CX.id_city = ' || _id;
		_query := _query || ' ORDER BY CX.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OL.type FROM "OLYMPICS" OL';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query := _query || ' WHERE OL.id_city = ' || _id;
		END IF;
		_query := _query || ' ORDER BY OL.type, YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT OR_.id, OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OR_.count_gold || '','' || OR_.count_silver || '','' || OR_.count_bronze FROM "OLYMPIC_RANKING" OR_';
		_query := _query || ' LEFT JOIN "OLYMPICS" OL ON OR_.id_olympics = OL.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON OR_.id_country = CN.id';
		IF _entity = 'OL' THEN
			_query := _query || ' WHERE OR_.id_olympics = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query := _query || ' WHERE OR_.id_country = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT RC.id, RC.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, RC.type1, RC.type2, RC.record1, RC.id_rank1, RC.id_rank2, RC.id_rank3, RC.id_rank4, RC.id_rank5 FROM "RECORD" RC';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RC.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RC.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RC.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "TYPE" TP ON EV.id_type = TP.id';
		_query := _query || ' WHERE TP.number BETWEEN ' || _type1 || ' AND ' || _type2;
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RC.id_rank1 = ' || _id || ' OR RC.id_rank2 = ' || _id || ' OR RC.id_rank3 = ' || _id || ' OR RC.id_rank4 = ' || _id || ' OR RC.id_rank5 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _pr_list || ') OR RC.id_rank2 IN (' || _pr_list || ') OR RC.id_rank3 IN (' || _pr_list || ') OR RC.id_rank4 IN (' || _pr_list || ') OR RC.id_rank5 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _tm_list || ') OR RC.id_rank2 IN (' || _tm_list || ') OR RC.id_rank3 IN (' || _tm_list || ') OR RC.id_rank4 IN (' || _tm_list || ') OR RC.id_rank5 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RC.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RC.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RC.id_event = ' || _id || ' OR RC.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND RC.id_city = ' || _id;
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', RC.index LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.txt1, _item.txt2, _item.txt3, _id1, _id2, _id3, _id4, _id5;
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
		_query := 'SELECT HF.id, YR.id, YR.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, HF.position FROM "HALL_OF_FAME" HF';
		_query := _query || ' LEFT JOIN "YEAR" YR ON HF.id_year = YR.id';
		_query := _query || ' LEFT JOIN "PERSON" PR ON HF.id_person = PR.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON HF.id_league = LG.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE HF.id_year = ' || _id;
		ELSIF _entity = 'PR' THEN
			_query := _query || ' WHERE HF.id_person IN (' || _pr_list || ')';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.txt1;
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
		_query := 'SELECT RN.id, TM.id, TM.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, RN.number FROM "RETIRED_NUMBER" RN';
		_query := _query || ' LEFT JOIN "TEAM" TM ON RN.id_team = TM.id';
		_query := _query || ' LEFT JOIN "PERSON" PR ON RN.id_person = PR.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON RN.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE RN.id_team = ' || _id;
		ELSIF _entity = 'PR' THEN
			_query := _query || ' WHERE RN.id_person IN (' || _pr_list || ')';
		END IF;
		_query := _query || ' ORDER BY TM.label, RN.number LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.id_rel4;
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
		_query := 'SELECT TS.id, TM.id, TM.label, CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', CX.label, CT.label, ST.label, CN.label, LG.id, LG.label, TS.date1, TS.date2 FROM "TEAM_STADIUM" TS';
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
		_query := _query || ' ORDER BY TM.label, TS.date1 DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.id_rel6, _item.comment, _item.txt1, _item.txt2;
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
		_query := 'SELECT WL.id, TM.id, TM.label, LG.id, LG.label, WL.type, WL.count_win || '','' || WL.count_loss FROM "WIN_LOSS" WL';
		_query := _query || ' LEFT JOIN "TEAM" TM ON WL.id_team = TM.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON WL.id_league = LG.id';
		_query := _query || ' WHERE WL.id_team = ' || _id;
		_query := _query || ' ORDER BY TM.label DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'WL';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Contributions]
	IF (_entity ~ 'CB' AND (_entity_ref = 'CO' OR _entity_ref = '')) THEN
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, CO.type, CO.date';
		_query := _query || ' FROM "~CONTRIBUTION" CO';
		_query := _query || ' LEFT JOIN "RESULT" RS ON CO.id_item = RS.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' WHERE RS.id_member=' || _id || ' ORDER BY CO.date DESC, YR.id DESC, SP.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.date1;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CO';
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
  
  
  
  
  
  
  
  
  CREATE OR REPLACE FUNCTION "COUNT_REF"(_entity character varying, _id integer)
  RETURNS integer AS
$BODY$
declare
	_count integer;
	_n integer;
	_type1 integer;
	_type2 integer;
begin
	IF _id IS NULL THEN
		RETURN 0;
	END IF;
	_count := 0;

	-- Count '_id' referenced in: Cities
	IF _entity = 'CN' THEN -- Country
		SELECT COUNT(*) INTO _n FROM "CITY" CT WHERE CT.id_country = _id; _count := _count + _n;
	ELSIF _entity = 'ST' THEN -- State
		SELECT COUNT(*) INTO _n FROM "CITY" CT WHERE CT.id_state = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Complexes
	IF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM "COMPLEX" CX WHERE CX.id_city = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Draws
	IF _entity ~ 'CN|PR|TM' THEN
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		IF _entity <> 'PR' THEN
			SELECT COUNT(*) INTO _n FROM "DRAW" DR
				LEFT JOIN "RESULT" RS ON DR.id_result = RS.id
				LEFT JOIN "EVENT" EV ON RS.id_event = EV.id
				LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id
				LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id
				LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id
				LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id
				LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id
			WHERE (DR.id1_qf1 = _id OR DR.id2_qf1 = _id OR DR.id1_qf2 = _id OR DR.id2_qf2 = _id OR DR.id1_qf3 = _id OR DR.id2_qf3 = _id OR DR.id1_qf4 = _id OR DR.id2_qf4 = _id OR DR.id1_sf1 = _id OR DR.id2_sf1 = _id OR DR.id1_sf2 = _id OR DR.id2_sf2 = _id OR DR.id1_thd = _id OR DR.id2_thd = _id)
				AND ((TP1.number BETWEEN _type1 AND _type2 AND TP2.number IS NULL) OR (TP2.number BETWEEN _type1 AND _type2 AND TP3.number IS NULL) OR TP3.number BETWEEN _type1 AND _type2);
		ELSE
			SELECT COUNT(*) INTO _n FROM "DRAW" DR
				LEFT JOIN "RESULT" RS ON DR.id_result = RS.id
				LEFT JOIN "EVENT" EV ON RS.id_event = EV.id
				LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id
				LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id
				LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id
				LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id
				LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id
			WHERE (DR.id1_qf1 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR DR.id2_qf1 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR DR.id1_qf2 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR DR.id2_qf2 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR DR.id1_qf3 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR DR.id2_qf3 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR DR.id1_qf4 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR DR.id2_qf4 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR DR.id1_sf1 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR DR.id2_sf1 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR DR.id1_sf2 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR DR.id2_sf2 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR DR.id1_thd IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR DR.id2_thd IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id))
				AND ((TP1.number BETWEEN _type1 AND _type2 AND TP2.number IS NULL) OR (TP2.number BETWEEN _type1 AND _type2 AND TP3.number IS NULL) OR TP3.number BETWEEN _type1 AND _type2);
		END IF;
		_count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Hall of Fame
	IF _entity = 'YR' THEN -- Year
		SELECT COUNT(*) INTO _n FROM "HALL_OF_FAME" HF WHERE HF.id_year = _id; _count := _count + _n;
	ELSIF _entity = 'PR' THEN -- Person
		SELECT COUNT(*) INTO _n FROM "HALL_OF_FAME" HF WHERE HF.id_person IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id); _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Olympics
	IF _entity = 'YR' THEN -- Year
		SELECT COUNT(*) INTO _n FROM "OLYMPICS" OL WHERE OL.id_year = _id; _count := _count + _n;
	ELSIF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM "OLYMPICS" OL WHERE OL.id_city = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Olympic Rankings
	IF _entity = 'OL' THEN -- Olympics
		SELECT COUNT(*) INTO _n FROM "OLYMPIC_RANKING" OR_ WHERE OR_.id_olympics = _id; _count := _count + _n;
	ELSIF _entity = 'CN' THEN -- Country
		SELECT COUNT(*) INTO _n FROM "OLYMPIC_RANKING" OR_ WHERE OR_.id_country = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Persons
	IF _entity = 'CN' THEN -- Country
		SELECT COUNT(*) INTO _n FROM "PERSON" PR WHERE PR.id_country = _id; _count := _count + _n;
	ELSIF _entity = 'TM' THEN -- Team
		SELECT COUNT(*) INTO _n FROM "PERSON" PR WHERE PR.id_team = _id; _count := _count + _n;
	ELSIF _entity = 'SP' THEN -- Sport
		SELECT COUNT(*) INTO _n FROM "PERSON" PR WHERE PR.id_sport = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Records
	IF _entity ~ 'CN|PR|TM' THEN -- Country/Person/Team
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		IF _entity <> 'PR' THEN
			SELECT COUNT(*) INTO _n FROM "RECORD" RC
				LEFT JOIN "EVENT" EV ON RC.id_event = EV.id
				LEFT JOIN "TYPE" TP ON EV.id_type = TP.id
			WHERE (RC.ID_RANK1 = _id OR RC.ID_RANK2 = _id OR RC.ID_RANK3 = _id OR RC.ID_RANK4 = _id OR RC.ID_RANK5 = _id)
				AND TP.number BETWEEN _type1 AND _type2;
		ELSE
			SELECT COUNT(*) INTO _n FROM "RECORD" RC
				LEFT JOIN "EVENT" EV ON RC.id_event = EV.id
				LEFT JOIN "TYPE" TP ON EV.id_type = TP.id
			WHERE (RC.ID_RANK1 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR RC.ID_RANK2 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR RC.ID_RANK3 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR RC.ID_RANK4 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR RC.ID_RANK5 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id))
				AND TP.number BETWEEN _type1 AND _type2;
		END IF;
		_count := _count + _n;
	ELSIF _entity = 'SP' THEN -- Sport
		SELECT COUNT(*) INTO _n FROM "RECORD" RC WHERE RC.id_sport = _id; _count := _count + _n;
	ELSIF _entity = 'CP' THEN -- Championship
		SELECT COUNT(*) INTO _n FROM "RECORD" RC WHERE RC.id_championship = _id; _count := _count + _n;
	ELSIF _entity = 'EV' THEN -- Event
		SELECT COUNT(*) INTO _n FROM "RECORD" RC WHERE RC.id_event = _id OR RC.id_subevent = _id; _count := _count + _n;
	ELSIF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM "RECORD" RC WHERE RC.id_city = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Results
	IF _entity ~ 'CN|PR|TM' THEN -- Country/Person/Team
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		IF _entity <> 'PR' THEN
			SELECT COUNT(*) INTO _n FROM "RESULT" RS
				LEFT JOIN "EVENT" EV ON RS.id_event = EV.id
				LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id
				LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id
				LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id
				LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id
				LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id
			WHERE (RS.id_rank1 = _id OR RS.id_rank2 = _id OR RS.id_rank3 = _id OR RS.id_rank4 = _id OR RS.id_rank5 = _id OR RS.id_rank6 = _id OR RS.id_rank7 = _id OR RS.id_rank8 = _id OR RS.id_rank9 = _id OR RS.id_rank10 = _id)
				AND ((TP1.number BETWEEN _type1 AND _type2 AND TP2.number IS NULL) OR (TP2.number BETWEEN _type1 AND _type2 AND TP3.number IS NULL) OR TP3.number BETWEEN _type1 AND _type2);
		ELSE
			SELECT COUNT(*) INTO _n FROM "RESULT" RS
				LEFT JOIN "EVENT" EV ON RS.id_event = EV.id
				LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id
				LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id
				LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id
				LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id
				LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id
			WHERE (RS.id_rank1 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR RS.id_rank2 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR RS.id_rank3 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR RS.id_rank4 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR RS.id_rank5 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR RS.id_rank6 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR RS.id_rank7 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR RS.id_rank8 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR RS.id_rank9 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id) OR RS.id_rank10 IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id))
				AND ((TP1.number BETWEEN _type1 AND _type2 AND TP2.number IS NULL) OR (TP2.number BETWEEN _type1 AND _type2 AND TP3.number IS NULL) OR TP3.number BETWEEN _type1 AND _type2);
		END IF;			
		_count := _count + _n;
	ELSIF _entity = 'SP' THEN -- Sport
		SELECT COUNT(*) INTO _n FROM "RESULT" RS WHERE RS.id_sport = _id; _count := _count + _n;
	ELSIF _entity = 'CP' THEN -- Championship
		SELECT COUNT(*) INTO _n FROM "RESULT" RS WHERE RS.id_championship = _id; _count := _count + _n;
	ELSIF _entity = 'EV' THEN -- Event
		SELECT COUNT(*) INTO _n FROM "RESULT" RS WHERE RS.id_event = _id OR RS.id_subevent = _id OR RS.id_subevent2 = _id; _count := _count + _n;
	ELSIF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM "RESULT" RS WHERE RS.id_city1 = _id OR RS.id_city2 = _id; _count := _count + _n;
	ELSIF _entity = 'CX' THEN -- Complex
		SELECT COUNT(*) INTO _n FROM "RESULT" RS WHERE RS.id_complex1 = _id OR RS.id_complex2 = _id; _count := _count + _n;
	ELSIF _entity = 'YR' THEN -- Year
		SELECT COUNT(*) INTO _n FROM "RESULT" RS WHERE RS.id_year = _id; _count := _count + _n;
	ELSIF _entity = 'OL' THEN -- Olympics
		SELECT COUNT(*) INTO _n FROM "RESULT" RS LEFT JOIN "OLYMPICS" OL ON RS.id_year = OL.id_year LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id WHERE OL.id = _id AND RS.id_championship = 1 AND SP.type = OL.type; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Retired Numbers
	IF _entity = 'TM' THEN -- Team
		SELECT COUNT(*) INTO _n FROM "RETIRED_NUMBER" RN WHERE RN.id_team = _id; _count := _count + _n;
	ELSIF _entity = 'PR' THEN -- Person
		SELECT COUNT(*) INTO _n FROM "RETIRED_NUMBER" RN WHERE RN.id_person IN (SELECT ID FROM "PERSON" PR WHERE PR.ID = _id OR PR.LINK = _id); _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Teams
	IF _entity = 'SP' THEN -- Sport
		SELECT COUNT(*) INTO _n FROM "TEAM" TM WHERE TM.id_sport = _id; _count := _count + _n;
	ELSIF _entity = 'CN' THEN -- Country
		SELECT COUNT(*) INTO _n FROM "TEAM" TM WHERE TM.id_country = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Team Stadiums
	IF _entity = 'TM' THEN -- Team
		SELECT COUNT(*) INTO _n FROM "TEAM_STADIUM" TS WHERE TS.id_team = _id; _count := _count + _n;
	ELSIF _entity = 'CX' THEN -- Complex
		SELECT COUNT(*) INTO _n FROM "TEAM_STADIUM" TS WHERE TS.id_complex = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Wins/Losses
	IF _entity = 'TM' THEN -- Team
		SELECT COUNT(*) INTO _n FROM "WIN_LOSS" WL WHERE WL.id_team = _id; _count := _count + _n;
	END IF;
	
	RETURN _count;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;

  
  
  
  
  
  CREATE OR REPLACE FUNCTION "UPDATE_REF"()
  RETURNS trigger AS
$BODY$
DECLARE
	_row record;
	_entity varchar(2);
	_id_event integer;
	_id_subevent integer;
	_id_subevent2 integer;
	_id_olympics integer;
	_sp_type integer;
	_type integer;
BEGIN
	_entity := TG_ARGV[0];
	IF (TG_OP = 'DELETE') THEN
		_row := OLD;
	ELSIF (TG_OP IN ('INSERT', 'UPDATE')) THEN
		_row := NEW;
	END IF;

	IF _entity = 'CT' THEN
		UPDATE "STATE" SET REF="COUNT_REF"('ST', _row.id_state) WHERE id=_row.id_state;
		UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_country) WHERE id=_row.id_country;
	ELSIF _entity = 'CX' THEN
		UPDATE "CITY" SET REF="COUNT_REF"('CT', _row.id_city) WHERE id=_row.id_city;
	ELSIF _entity = 'DR' THEN
		SELECT RS.id_event, RS.id_subevent, RS.id_subevent2 INTO _id_event, _id_subevent, _id_subevent2 FROM "RESULT" RS WHERE RS.id = _row.id_result;

		IF _id_subevent2 IS NOT NULL AND _id_subevent2 <> 0 THEN
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _id_subevent2;
		ELSIF _id_subevent IS NOT NULL AND _id_subevent <> 0 THEN
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _id_subevent;
		ELSE
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _id_event;
		END IF;

		IF _type < 10 THEN
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id1_qf1) WHERE id=_row.id1_qf1;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id2_qf1) WHERE id=_row.id2_qf1;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id1_qf2) WHERE id=_row.id1_qf2;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id2_qf2) WHERE id=_row.id2_qf2;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id1_qf3) WHERE id=_row.id1_qf3;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id2_qf3) WHERE id=_row.id2_qf3;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id1_qf4) WHERE id=_row.id1_qf4;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id2_qf4) WHERE id=_row.id2_qf4;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id1_sf1) WHERE id=_row.id1_sf1;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id2_sf1) WHERE id=_row.id2_sf1;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id1_sf2) WHERE id=_row.id1_sf2;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id2_sf2) WHERE id=_row.id2_sf2;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id1_thd) WHERE id=_row.id1_thd;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id2_thd) WHERE id=_row.id2_thd;
		ELSIF _type = 50 THEN
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id1_qf1) WHERE id=_row.id1_qf1;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id2_qf1) WHERE id=_row.id2_qf1;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id1_qf2) WHERE id=_row.id1_qf2;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id2_qf2) WHERE id=_row.id2_qf2;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id1_qf3) WHERE id=_row.id1_qf3;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id2_qf3) WHERE id=_row.id2_qf3;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id1_qf4) WHERE id=_row.id1_qf4;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id2_qf4) WHERE id=_row.id2_qf4;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id1_sf1) WHERE id=_row.id1_sf1;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id2_sf1) WHERE id=_row.id2_sf1;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id1_sf2) WHERE id=_row.id1_sf2;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id2_sf2) WHERE id=_row.id2_sf2;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id1_thd) WHERE id=_row.id1_thd;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id2_thd) WHERE id=_row.id2_thd;
		ELSIF _type = 99 THEN
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id1_qf1) WHERE id=_row.id1_qf1;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id2_qf1) WHERE id=_row.id2_qf1;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id1_qf2) WHERE id=_row.id1_qf2;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id2_qf2) WHERE id=_row.id2_qf2;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id1_qf3) WHERE id=_row.id1_qf3;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id2_qf3) WHERE id=_row.id2_qf3;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id1_qf4) WHERE id=_row.id1_qf4;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id2_qf4) WHERE id=_row.id2_qf4;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id1_sf1) WHERE id=_row.id1_sf1;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id2_sf1) WHERE id=_row.id2_sf1;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id1_sf2) WHERE id=_row.id1_sf2;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id2_sf2) WHERE id=_row.id2_sf2;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id1_thd) WHERE id=_row.id1_thd;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id2_thd) WHERE id=_row.id2_thd;
		END IF;
	ELSIF _entity = 'HF' THEN
		UPDATE "YEAR" SET REF="COUNT_REF"('YR', _row.id_year) WHERE id=_row.id_year;
		UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_person) WHERE id=_row.id_person;
	ELSIF _entity = 'OL' THEN
		UPDATE "YEAR" SET REF="COUNT_REF"('YR', _row.id_year) WHERE id=_row.id_year;
		UPDATE "CITY" SET REF="COUNT_REF"('CT', _row.id_city) WHERE id=_row.id_city;
	ELSIF _entity = 'OR' THEN
		UPDATE "OLYMPICS" SET REF="COUNT_REF"('OL', _row.id_olympics) WHERE id=_row.id_olympics;
		UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_country) WHERE id=_row.id_country;
	ELSIF _entity = 'PR' THEN
		UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_country) WHERE id=_row.id_country;
		UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_team) WHERE id=_row.id_team;
		UPDATE "SPORT" SET REF="COUNT_REF"('SP', _row.id_sport) WHERE id=_row.id_sport;
	ELSIF _entity = 'RC' THEN
		UPDATE "SPORT" SET REF="COUNT_REF"('SP', _row.id_sport) WHERE id=_row.id_sport;
		UPDATE "CHAMPIONSHIP" SET REF="COUNT_REF"('CP', _row.id_championship) WHERE id=_row.id_championship;
		UPDATE "EVENT" SET REF="COUNT_REF"('EV', _row.id_event) WHERE id=_row.id_event;
		UPDATE "EVENT" SET REF="COUNT_REF"('EV', _row.id_subevent) WHERE id=_row.id_subevent;
		UPDATE "CITY" SET REF="COUNT_REF"('CT', _row.id_city) WHERE id=_row.id_city;

		IF _row.id_subevent IS NOT NULL AND _row.id_subevent <> 0 THEN
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _row.id_subevent;
		ELSE
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _row.id_event;
		END IF;

		IF _type < 10 THEN
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank5) WHERE id=_row.id_rank5;
		ELSIF _type = 50 THEN
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank5) WHERE id=_row.id_rank5;
		ELSIF _type = 99 THEN
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank5) WHERE id=_row.id_rank5;
		END IF;
	ELSIF _entity = 'RS' THEN
		UPDATE "YEAR" SET REF="COUNT_REF"('YR', _row.id_year) WHERE id=_row.id_year;
		UPDATE "SPORT" SET REF="COUNT_REF"('SP', _row.id_sport) WHERE id=_row.id_sport;
		UPDATE "CHAMPIONSHIP" SET REF="COUNT_REF"('CP', _row.id_championship) WHERE id=_row.id_championship;
		UPDATE "EVENT" SET REF="COUNT_REF"('EV', _row.id_event) WHERE id=_row.id_event;
		UPDATE "EVENT" SET REF="COUNT_REF"('EV', _row.id_subevent) WHERE id=_row.id_subevent;
		UPDATE "EVENT" SET REF="COUNT_REF"('EV', _row.id_subevent2) WHERE id=_row.id_subevent2;
		UPDATE "COMPLEX" SET REF="COUNT_REF"('CX', _row.id_complex1) WHERE id=_row.id_complex1;
		UPDATE "COMPLEX" SET REF="COUNT_REF"('CX', _row.id_complex2) WHERE id=_row.id_complex2;
		UPDATE "CITY" SET REF="COUNT_REF"('CT', _row.id_city1) WHERE id=_row.id_city1;
		UPDATE "CITY" SET REF="COUNT_REF"('CT', _row.id_city2) WHERE id=_row.id_city2;
		IF _row.id_championship = 1 THEN
			SELECT SP.type INTO _sp_type FROM "SPORT" SP WHERE SP.id=_row.id_sport;
			SELECT OL.id INTO _id_olympics FROM "OLYMPICS" OL WHERE OL.id_year=_row.id_year AND OL.type=_sp_type;
			UPDATE "OLYMPICS" SET REF="COUNT_REF"('OL', _id_olympics) WHERE id=_id_olympics;
		END IF;
		IF _row.id_subevent2 IS NOT NULL AND _row.id_subevent2 <> 0 THEN
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _row.id_subevent2;
		ELSIF _row.id_subevent IS NOT NULL AND _row.id_subevent <> 0 THEN
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _row.id_subevent;
		ELSE
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _row.id_event;
		END IF;

		IF _type < 10 THEN
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank5) WHERE id=_row.id_rank5;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank6) WHERE id=_row.id_rank6;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank7) WHERE id=_row.id_rank7;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank8) WHERE id=_row.id_rank8;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank9) WHERE id=_row.id_rank9;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank10) WHERE id=_row.id_rank10;
		ELSIF _type = 50 THEN
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank5) WHERE id=_row.id_rank5;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank6) WHERE id=_row.id_rank6;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank7) WHERE id=_row.id_rank7;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank8) WHERE id=_row.id_rank8;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank9) WHERE id=_row.id_rank9;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank10) WHERE id=_row.id_rank10;
		ELSIF _type = 99 THEN
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank5) WHERE id=_row.id_rank5;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank6) WHERE id=_row.id_rank6;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank7) WHERE id=_row.id_rank7;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank8) WHERE id=_row.id_rank8;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank9) WHERE id=_row.id_rank9;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank10) WHERE id=_row.id_rank10;
		END IF;
	ELSIF _entity = 'RN' THEN
		UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_team) WHERE id=_row.id_team;
		UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_person) WHERE id=_row.id_person;
	ELSIF _entity = 'TM' THEN
		UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.link) WHERE id=_row.link;
	ELSIF _entity = 'TS' THEN
		UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_team) WHERE id=_row.id_team;
		UPDATE "COMPLEX" SET REF="COUNT_REF"('CX', _row.id_complex) WHERE id=_row.id_complex;
	ELSIF _entity = 'WL' THEN
		UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_team) WHERE id=_row.id_team;
	END IF;
	
        RETURN NULL;
    END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  
  
  CREATE OR REPLACE FUNCTION "COUNT_REF"(_entity character varying, _id integer)
  RETURNS integer AS
$BODY$
declare
	_count integer;
	_n integer;
	_type1 integer;
	_type2 integer;
begin
	IF _id IS NULL THEN
		RETURN 0;
	END IF;
	_count := 0;

	-- Count '_id' referenced in: Cities
	IF _entity = 'CN' THEN -- Country
		SELECT COUNT(*) INTO _n FROM "CITY" CT WHERE CT.id_country = _id; _count := _count + _n;
	ELSIF _entity = 'ST' THEN -- State
		SELECT COUNT(*) INTO _n FROM "CITY" CT WHERE CT.id_state = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Complexes
	IF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM "COMPLEX" CX WHERE CX.id_city = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Draws
	IF _entity ~ 'CN|PR|TM' THEN
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		SELECT COUNT(*) INTO _n FROM "DRAW" DR
			LEFT JOIN "RESULT" RS ON DR.id_result = RS.id
			LEFT JOIN "EVENT" EV ON RS.id_event = EV.id
			LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id
			LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id
			LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id
			LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id
			LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id
		WHERE (DR.id1_qf1 = _id OR DR.id2_qf1 = _id OR DR.id1_qf2 = _id OR DR.id2_qf2 = _id OR DR.id1_qf3 = _id OR DR.id2_qf3 = _id OR DR.id1_qf4 = _id OR DR.id2_qf4 = _id OR DR.id1_sf1 = _id OR DR.id2_sf1 = _id OR DR.id1_sf2 = _id OR DR.id2_sf2 = _id OR DR.id1_thd = _id OR DR.id2_thd = _id)
			AND ((TP1.number BETWEEN _type1 AND _type2 AND TP2.number IS NULL) OR (TP2.number BETWEEN _type1 AND _type2 AND TP3.number IS NULL) OR TP3.number BETWEEN _type1 AND _type2);
		_count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Hall of Fame
	IF _entity = 'YR' THEN -- Year
		SELECT COUNT(*) INTO _n FROM "HALL_OF_FAME" HF WHERE HF.id_year = _id; _count := _count + _n;
	ELSIF _entity = 'PR' THEN -- Person
		SELECT COUNT(*) INTO _n FROM "HALL_OF_FAME" HF WHERE HF.id_person = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Olympics
	IF _entity = 'YR' THEN -- Year
		SELECT COUNT(*) INTO _n FROM "OLYMPICS" OL WHERE OL.id_year = _id; _count := _count + _n;
	ELSIF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM "OLYMPICS" OL WHERE OL.id_city = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Olympic Rankings
	IF _entity = 'OL' THEN -- Olympics
		SELECT COUNT(*) INTO _n FROM "OLYMPIC_RANKING" OR_ WHERE OR_.id_olympics = _id; _count := _count + _n;
	ELSIF _entity = 'CN' THEN -- Country
		SELECT COUNT(*) INTO _n FROM "OLYMPIC_RANKING" OR_ WHERE OR_.id_country = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Persons
	IF _entity = 'CN' THEN -- Country
		SELECT COUNT(*) INTO _n FROM "PERSON" PR WHERE PR.id_country = _id; _count := _count + _n;
	ELSIF _entity = 'TM' THEN -- Team
		SELECT COUNT(*) INTO _n FROM "PERSON" PR WHERE PR.id_team = _id; _count := _count + _n;
	ELSIF _entity = 'SP' THEN -- Sport
		SELECT COUNT(*) INTO _n FROM "PERSON" PR WHERE PR.id_sport = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Records
	IF _entity ~ 'CN|PR|TM' THEN -- Country/Person/Team
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		SELECT COUNT(*) INTO _n FROM "RECORD" RC
			LEFT JOIN "EVENT" EV ON RC.id_event = EV.id
			LEFT JOIN "TYPE" TP ON EV.id_type = TP.id
		WHERE (RC.ID_RANK1 = _id OR RC.ID_RANK2 = _id OR RC.ID_RANK3 = _id OR RC.ID_RANK4 = _id OR RC.ID_RANK5 = _id)
			AND TP.number BETWEEN _type1 AND _type2;
		_count := _count + _n;
	ELSIF _entity = 'SP' THEN -- Sport
		SELECT COUNT(*) INTO _n FROM "RECORD" RC WHERE RC.id_sport = _id; _count := _count + _n;
	ELSIF _entity = 'CP' THEN -- Championship
		SELECT COUNT(*) INTO _n FROM "RECORD" RC WHERE RC.id_championship = _id; _count := _count + _n;
	ELSIF _entity = 'EV' THEN -- Event
		SELECT COUNT(*) INTO _n FROM "RECORD" RC WHERE RC.id_event = _id OR RC.id_subevent = _id; _count := _count + _n;
	ELSIF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM "RECORD" RC WHERE RC.id_city = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Results
	IF _entity ~ 'CN|PR|TM' THEN -- Country/Person/Team
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		SELECT COUNT(*) INTO _n FROM "RESULT" RS
			LEFT JOIN "EVENT" EV ON RS.id_event = EV.id
			LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id
			LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id
			LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id
			LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id
			LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id
		WHERE (RS.id_rank1 = _id OR RS.id_rank2 = _id OR RS.id_rank3 = _id OR RS.id_rank4 = _id OR RS.id_rank5 = _id OR RS.id_rank6 = _id OR RS.id_rank7 = _id OR RS.id_rank8 = _id OR RS.id_rank9 = _id OR RS.id_rank10 = _id)
			AND ((TP1.number BETWEEN _type1 AND _type2 AND TP2.number IS NULL) OR (TP2.number BETWEEN _type1 AND _type2 AND TP3.number IS NULL) OR TP3.number BETWEEN _type1 AND _type2);
		_count := _count + _n;
	ELSIF _entity = 'SP' THEN -- Sport
		SELECT COUNT(*) INTO _n FROM "RESULT" RS WHERE RS.id_sport = _id; _count := _count + _n;
	ELSIF _entity = 'CP' THEN -- Championship
		SELECT COUNT(*) INTO _n FROM "RESULT" RS WHERE RS.id_championship = _id; _count := _count + _n;
	ELSIF _entity = 'EV' THEN -- Event
		SELECT COUNT(*) INTO _n FROM "RESULT" RS WHERE RS.id_event = _id OR RS.id_subevent = _id OR RS.id_subevent2 = _id; _count := _count + _n;
	ELSIF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM "RESULT" RS WHERE RS.id_city1 = _id OR RS.id_city2 = _id; _count := _count + _n;
	ELSIF _entity = 'CX' THEN -- Complex
		SELECT COUNT(*) INTO _n FROM "RESULT" RS WHERE RS.id_complex1 = _id OR RS.id_complex2 = _id; _count := _count + _n;
	ELSIF _entity = 'YR' THEN -- Year
		SELECT COUNT(*) INTO _n FROM "RESULT" RS WHERE RS.id_year = _id; _count := _count + _n;
	ELSIF _entity = 'OL' THEN -- Olympics
		SELECT COUNT(*) INTO _n FROM "RESULT" RS LEFT JOIN "OLYMPICS" OL ON RS.id_year = OL.id_year LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id WHERE OL.id = _id AND RS.id_championship = 1 AND SP.type = OL.type; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Retired Numbers
	IF _entity = 'TM' THEN -- Team
		SELECT COUNT(*) INTO _n FROM "RETIRED_NUMBER" RN WHERE RN.id_team = _id; _count := _count + _n;
	ELSIF _entity = 'PR' THEN -- Person
		SELECT COUNT(*) INTO _n FROM "RETIRED_NUMBER" RN WHERE RN.id_person = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Teams
	IF _entity = 'SP' THEN -- Sport
		SELECT COUNT(*) INTO _n FROM "TEAM" TM WHERE TM.id_sport = _id; _count := _count + _n;
	ELSIF _entity = 'CN' THEN -- Country
		SELECT COUNT(*) INTO _n FROM "TEAM" TM WHERE TM.id_country = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Team Stadiums
	IF _entity = 'TM' THEN -- Team
		SELECT COUNT(*) INTO _n FROM "TEAM_STADIUM" TS WHERE TS.id_team = _id; _count := _count + _n;
	ELSIF _entity = 'CX' THEN -- Complex
		SELECT COUNT(*) INTO _n FROM "TEAM_STADIUM" TS WHERE TS.id_complex = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Wins/Losses
	IF _entity = 'TM' THEN -- Team
		SELECT COUNT(*) INTO _n FROM "WIN_LOSS" WL WHERE WL.id_team = _id; _count := _count + _n;
	END IF;
	
	RETURN _count;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  
  
  CREATE OR REPLACE FUNCTION "TREE_RESULTS"(_filter character varying, _lang character varying)
  RETURNS SETOF "~TREE_ITEM" AS
$BODY$
declare
	_item "~TREE_ITEM"%rowtype;
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
	    FROM "RESULT" RS LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id
	    LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id
	    LEFT JOIN "EVENT" EV ON RS.id_event = EV.id
	    LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id
	    LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id
	    LEFT JOIN "OLYMPICS" OL ON OL.id_year = RS.id_year
	    LEFT JOIN "~INACTIVE_ITEM" II ON (RS.id_sport = II.id_sport AND RS.id_championship = II.id_championship AND RS.id_event = II.id_event AND (RS.id_subevent = II.id_subevent OR RS.id_subevent IS NULL) AND (RS.id_subevent2 = II.id_subevent2 OR RS.id_subevent2 IS NULL))
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
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
  
  
  
  
  
  CREATE TABLE "~ERROR_REPORT"
(
  id integer NOT NULL,
  url varchar(255),
  text_ text,
  date timestamp
);
CREATE SEQUENCE "~SQ_ERROR_REPORT";




CREATE OR REPLACE FUNCTION "~LAST_UPDATES"(_count integer, _offset integer, _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
	_c refcursor;
begin
	OPEN _c FOR EXECUTE
	'SELECT RS.id AS rs_id, YR.label AS yr_label, SP.id AS sp_id, CP.id AS cp_id, EV.id AS ev_id, SE.id AS se_id, SE2.id AS se2_id, SP.label' || _lang || ' AS sp_label, CP.label' || _lang || ' AS cp_label, EV.label' || _lang || ' AS ev_label, SE.label' || _lang || ' AS se_label, SE2.label' || _lang || ' AS se2_label, SP.label AS sp_label_en, CP.label AS cp_label_en, EV.label AS ev_label_en, SE.label AS se_label_en, SE2.label AS se2_label_en, RS.first_update AS rs_update, TP1.number as tp1_number, TP2.number AS tp2_number, TP3.number AS tp3_number,
	  PR1.id AS pr1_id, PR1.first_name AS pr1_first_name, PR1.last_name AS pr1_last_name, PR1.id_country AS pr1_country, PRCN1.code AS pr1_country_code, TM1.id AS tm1_id, TM1.label AS tm1_label, CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label' || _lang || ' AS cn1_label, CN1.label AS cn1_label_en, 
	  PR2.id AS pr2_id, PR2.first_name AS pr2_first_name, PR2.last_name AS pr2_last_name, PR2.id_country AS pr2_country, PRCN2.code AS pr2_country_code, TM2.id AS tm2_id, TM2.label AS tm2_label, CN2.id AS cn2_id, CN2.code AS cn2_code, CN2.label' || _lang || ' AS cn2_label, CN2.label AS cn2_label_en,
	  PR3.id AS pr3_id, PR3.first_name AS pr3_first_name, PR3.last_name AS pr3_last_name, PR3.id_country AS pr3_country, PRCN3.code AS pr3_country_code, TM3.id AS tm3_id, TM3.label AS tm3_label, CN3.id AS cn3_id, CN3.code AS cn3_code, CN3.label' || _lang || ' AS cn3_label, CN3.label AS cn3_label_en,
	  PR4.id AS pr4_id, PR4.first_name AS pr4_first_name, PR4.last_name AS pr4_last_name, PR4.id_country AS pr4_country, PRCN4.code AS pr4_country_code, TM4.id AS tm4_id, TM4.label AS tm4_label, CN4.id AS cn4_id, CN4.code AS cn4_code, CN4.label' || _lang || ' AS cn4_label, CN4.label AS cn4_label_en,
	  RS.result1 AS rs_text1, RS.result2 AS rs_text2, RS.exa AS rs_text3, RS.comment AS rs_text4, RS.date2 AS rs_date FROM "RESULT" RS
		LEFT JOIN "YEAR" YR ON RS.id_year=YR.id
		LEFT JOIN "SPORT" SP ON RS.id_sport=SP.id
		LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship=CP.id
		LEFT JOIN "EVENT" EV ON RS.id_event=EV.id
		LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id
		LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id
		LEFT JOIN "TYPE" TP1 ON EV.id_type=TP1.id
		LEFT JOIN "TYPE" TP2 ON SE.id_type=TP2.id
		LEFT JOIN "TYPE" TP3 ON SE2.id_type=TP3.id
		LEFT JOIN "PERSON" PR1 ON RS.id_rank1=PR1.id
		LEFT JOIN "PERSON" PR2 ON RS.id_rank2=PR2.id
		LEFT JOIN "PERSON" PR3 ON RS.id_rank3=PR3.id
		LEFT JOIN "PERSON" PR4 ON RS.id_rank4=PR4.id
		LEFT JOIN "COUNTRY" PRCN1 ON PR1.id_country=PRCN1.id
		LEFT JOIN "COUNTRY" PRCN2 ON PR2.id_country=PRCN2.id
		LEFT JOIN "COUNTRY" PRCN3 ON PR3.id_country=PRCN3.id
		LEFT JOIN "COUNTRY" PRCN4 ON PR4.id_country=PRCN4.id
		LEFT JOIN "TEAM" TM1 ON RS.id_rank1=TM1.id
		LEFT JOIN "TEAM" TM2 ON RS.id_rank2=TM2.id
		LEFT JOIN "TEAM" TM3 ON RS.id_rank3=TM3.id
		LEFT JOIN "TEAM" TM4 ON RS.id_rank4=TM4.id
		LEFT JOIN "COUNTRY" CN1 ON RS.id_rank1=CN1.id
		LEFT JOIN "COUNTRY" CN2 ON RS.id_rank2=CN2.id
		LEFT JOIN "COUNTRY" CN3 ON RS.id_rank3=CN3.id
		LEFT JOIN "COUNTRY" CN4 ON RS.id_rank4=CN4.id
	ORDER BY YR.id DESC, RS.first_update DESC LIMIT ' || _count || ' OFFSET ' || _offset;
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  

CREATE OR REPLACE FUNCTION "ENTITY_REF"(_entity character varying, _id integer, _entity_ref character varying, _limit character varying, _offset integer, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_c refcursor;
	__c refcursor;
	_query text;
	_link integer;
	_pr_list varchar(50);
	_tm_list varchar(50);
	_index integer;
	_type1 integer;
	_type2 integer;
	_type3 integer;
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
	_id11 integer;_id12 integer;_id13 integer;_id14 integer;
	_cn1 varchar(5);_cn2 varchar(5);_cn3 varchar(5);_cn4 varchar(5);_cn5 varchar(5);_cn6 varchar(5);
	_tm1 varchar(60);_tm2 varchar(60);_tm3 varchar(60);_tm4 varchar(60);_tm5 varchar(60);_tm6 varchar(60);
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'IF', _entity || '-' || _id, current_date);
	
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
	ELSIF _entity ~ 'TM' THEN
		SELECT LINK INTO _link FROM "TEAM" WHERE ID = _id AND (YEAR1 IS NULL OR YEAR1 = '');
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "TEAM" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_tm_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_tm_list := _tm_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_tm_list := cast(_id AS varchar);
		END IF;
	END IF;

	-- References in: [Draws] (MUST STAY AT FIRST)
	IF (_entity ~ 'CN|PR|TM' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT DR.id, DR.id_result, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, DR.id1_qf1, DR.id2_qf1, DR.id1_qf2, DR.id2_qf2, DR.id1_qf3, DR.id2_qf3, DR.id1_qf4, DR.id2_qf4, DR.id1_sf1, DR.id2_sf1, DR.id1_sf2, DR.id2_sf2, DR.id1_thd, DR.id2_thd, TP1.number, TP2.number, TP3.number FROM "DRAW" DR';
		_query := _query || ' LEFT JOIN "RESULT" RS ON DR.id_result = RS.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id';
		_query := _query || ' LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id';
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity = 'CN' THEN
			_query := _query || ' AND (DR.id1_sf1 <> ' || _id || ' AND DR.id1_sf2 <> ' || _id || ') AND (DR.id1_qf1 = ' || _id || ' OR DR.id2_qf1 = ' || _id || ' OR DR.id1_qf2 = ' || _id || ' OR DR.id2_qf2 = ' || _id || ' OR DR.id1_qf3 = ' || _id || ' OR DR.id2_qf3 = ' || _id || ' OR DR.id1_qf4 = ' || _id || ' OR DR.id2_qf4 = ' || _id || ' OR DR.id2_sf1 = ' || _id || ' OR DR.id2_sf2 = ' || _id || ' OR DR.id1_thd = ' || _id || ' OR DR.id2_thd = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _pr_list || ') AND DR.id1_sf2 NOT IN (' || _pr_list || ')) AND (DR.id1_qf1 IN (' || _pr_list || ') OR DR.id2_qf1 IN (' || _pr_list || ') OR DR.id1_qf2 IN (' || _pr_list || ') OR DR.id2_qf2 IN (' || _pr_list || ') OR DR.id1_qf3 IN (' || _pr_list || ') OR DR.id2_qf3 IN (' || _pr_list || ') OR DR.id1_qf4 IN (' || _pr_list || ') OR DR.id2_qf4 IN (' || _pr_list || ') OR DR.id2_sf1 IN (' || _pr_list || ') OR DR.id2_sf2 IN (' || _pr_list || ') OR DR.id1_thd IN (' || _pr_list || ') OR DR.id2_thd IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _tm_list || ') AND DR.id1_sf2 NOT IN (' || _tm_list || ')) AND (DR.id1_qf1 IN (' || _tm_list || ') OR DR.id2_qf1 IN (' || _tm_list || ') OR DR.id1_qf2 IN (' || _tm_list || ') OR DR.id2_qf2 IN (' || _tm_list || ') OR DR.id1_qf3 IN (' || _tm_list || ') OR DR.id2_qf3 IN (' || _tm_list || ') OR DR.id1_qf4 IN (' || _tm_list || ') OR DR.id2_qf4 IN (' || _tm_list || ') OR DR.id2_sf1 IN (' || _tm_list || ') OR DR.id2_sf2 IN (' || _tm_list || ') OR DR.id1_thd IN (' || _tm_list || ') OR DR.id2_thd IN (' || _tm_list || '))';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel10, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _id11, _id12, _id13, _id14, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF (_id13 = _id OR _id14 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)'))) THEN
				_item.txt2 := 'thd';
			ELSIF (_id9 = _id OR _id10 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)'))) THEN
				_item.txt2 := 'sf1';
			ELSIF (_id11 = _id OR _id12 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)'))) THEN
				_item.txt2 := 'sf2';
			ELSIF (_id1 = _id OR _id2 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)'))) THEN
				_item.txt2 := 'qf1';
			ELSIF (_id3 = _id OR _id4 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)'))) THEN
				_item.txt2 := 'qf2';
			ELSIF (_id5 = _id OR _id6 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)'))) THEN
				_item.txt2 := 'qf3';
			ELSIF (_id7 = _id OR _id8 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)'))) THEN
				_item.txt2 := 'qf4';
			END IF;
			IF _type1 <= 10 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN '', '' || PR1.first_name ELSE '''' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || '' '' ELSE '''' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN '', '' || PR2.first_name ELSE '''' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || '' '' ELSE '''' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, DR.result_' || _item.txt2;
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "PERSON" PR1 ON DR.id1_' || _item.txt2 || ' = PR1.id LEFT JOIN "PERSON" PR2 ON DR.id2_' || _item.txt2 || ' = PR2.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel8, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel9, _item.id_rel13, _cn2, _tm2, _item.txt1;
				CLOSE __c;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				_item.comment := 'PR';
			ELSIF _type1 = 50 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', TM1.label, TM2.label, NULL, NULL, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "TEAM" TM1 ON DR.id1_' || _item.txt2 || ' = TM1.id LEFT JOIN "TEAM" TM2 ON DR.id2_' || _item.txt2 || ' = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'TM';
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', CN1.label' || _lang || ', CN2.label' || _lang || ', CN1.label, CN2.label, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "COUNTRY" CN1 ON DR.id1_' || _item.txt2 || ' = CN1.id LEFT JOIN "COUNTRY" CN2 ON DR.id2_' || _item.txt2 || ' = CN2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'CN';
			END IF;
			_item.id = _index;
			_item.entity = 'RS';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Results]
	IF (_entity ~ 'CN|PR|TM|CP|EV|CT|SP|CX|OL|YR' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number, TP3.number FROM "RESULT" RS';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id';
		_query := _query || ' LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id';
		IF (_entity = 'OL') THEN
			_query := _query || ' LEFT JOIN "OLYMPICS" OL ON (OL.id_year = YR.id AND OL.type = SP.type)';
		END IF;
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RS.id_rank1 = ' || _id || ' OR RS.id_rank2 = ' || _id || ' OR RS.id_rank3 = ' || _id || ' OR RS.id_rank4 = ' || _id || ' OR RS.id_rank5 = ' || _id || ' OR RS.id_rank6 = ' || _id || ' OR RS.id_rank7 = ' || _id || ' OR RS.id_rank8 = ' || _id || ' OR RS.id_rank9 = ' || _id || ' OR RS.id_rank10 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RS.id_rank1 IN (' || _pr_list || ') OR RS.id_rank2 IN (' || _pr_list || ') OR RS.id_rank3 IN (' || _pr_list || ') OR RS.id_rank4 IN (' || _pr_list || ') OR RS.id_rank5 IN (' || _pr_list || ') OR RS.id_rank6 IN (' || _pr_list || ') OR RS.id_rank7 IN (' || _pr_list || ') OR RS.id_rank8 IN (' || _pr_list || ') OR RS.id_rank9 IN (' || _pr_list || ') OR RS.id_rank10 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RS.id_rank1 IN (' || _tm_list || ') OR RS.id_rank2 IN (' || _tm_list || ') OR RS.id_rank3 IN (' || _tm_list || ') OR RS.id_rank4 IN (' || _tm_list || ') OR RS.id_rank5 IN (' || _tm_list || ') OR RS.id_rank6 IN (' || _tm_list || ') OR RS.id_rank7 IN (' || _tm_list || ') OR RS.id_rank8 IN (' || _tm_list || ') OR RS.id_rank9 IN (' || _tm_list || ') OR RS.id_rank10 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND  (RS.id_city1 = ' || _id || ' OR RS.id_city2 = ' || _id || ')';
		ELSIF _entity = 'CX' THEN
			_query := _query || ' AND  (RS.id_complex1 = ' || _id || ' OR RS.id_complex2 = ' || _id || ')';
		ELSIF _entity = 'OL' THEN
			_query := _query || ' AND RS.id_championship=1 AND OL.id = ' || _id;
		ELSIF _entity = 'YR' THEN
			_query := _query || ' AND RS.id_year = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF _type1 <= 10 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, CN3.code, TM3.label, PR4.last_name || (CASE WHEN length(PR4.first_name) > 0 THEN ', ' || PR4.first_name ELSE '' END), (CASE WHEN length(PR4.first_name) > 0 THEN PR4.first_name || ' ' ELSE '' END) || PR4.last_name, CN4.id, CN4.code, TM4.label, PR5.last_name || (CASE WHEN length(PR5.first_name) > 0 THEN ', ' || PR5.first_name ELSE '' END), (CASE WHEN length(PR5.first_name) > 0 THEN PR5.first_name || ' ' ELSE '' END) || PR5.last_name, CN5.id, CN5.code, TM5.label, PR6.last_name || (CASE WHEN length(PR6.first_name) > 0 THEN ', ' || PR6.first_name ELSE '' END), (CASE WHEN length(PR6.first_name) > 0 THEN PR6.first_name || ' ' ELSE '' END) || PR6.last_name, CN6.id, CN6.code, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.label_rel23, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.label_rel24, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.label_rel25, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "PERSON" PR1 ON RS.id_rank1 = PR1.id LEFT JOIN "PERSON" PR2 ON RS.id_rank2 = PR2.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3 = PR3.id LEFT JOIN "PERSON" PR4 ON RS.id_rank4 = PR4.id LEFT JOIN "PERSON" PR5 ON RS.id_rank5 = PR5.id LEFT JOIN "PERSON" PR6 ON RS.id_rank6 = PR6.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "COUNTRY" CN3 ON PR3.id_country = CN3.id LEFT JOIN "COUNTRY" CN4 ON PR4.id_country = CN4.id LEFT JOIN "COUNTRY" CN5 ON PR5.id_country = CN5.id LEFT JOIN "COUNTRY" CN6 ON PR6.id_country = CN6.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id LEFT JOIN "TEAM" TM3 ON PR3.id_team = TM3.id LEFT JOIN "TEAM" TM4 ON PR4.id_team = TM4.id LEFT JOIN "TEAM" TM5 ON PR5.id_team = TM5.id LEFT JOIN "TEAM" TM6 ON PR6.id_team = TM6.id
				WHERE RS.id = _item.id_item;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				IF _cn3 IS NOT NULL THEN _item.label_rel8 := _item.label_rel8 || ' (' || _cn3 || ')';
				ELSIF _tm3 IS NOT NULL THEN _item.label_rel8 := _item.label_rel8 || ' (' || _tm3 || ')'; END IF;
				IF _cn4 IS NOT NULL THEN _item.label_rel9 := _item.label_rel9 || ' (' || _cn4 || ')';
				ELSIF _tm4 IS NOT NULL THEN _item.label_rel9 := _item.label_rel9 || ' (' || _tm4 || ')'; END IF;
				IF _cn5 IS NOT NULL THEN _item.label_rel10 := _item.label_rel10 || ' (' || _cn5 || ')';
				ELSIF _tm5 IS NOT NULL THEN _item.label_rel10 := _item.label_rel10 || ' (' || _tm5 || ')'; END IF;
				IF _cn6 IS NOT NULL THEN _item.label_rel11 := _item.label_rel11 || ' (' || _cn6 || ')';
				ELSIF _tm6 IS NOT NULL THEN _item.label_rel11 := _item.label_rel11 || ' (' || _tm6 || ')'; END IF;
				IF _type1 = 4 OR _item.txt3 = '#DOUBLE#' THEN
					_item.txt4 = '1-2/3-4/5-6';
				ELSIF _type1 = 5 OR _item.txt3 = '#TRIPLE#' THEN
					_item.txt4 = '1-3/4-6/7-9';
				END IF;
				_item.comment := 'PR';
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, NULL, NULL, NULL, NULL, NULL, NULL, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "TEAM" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "TEAM" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "TEAM" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "TEAM" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "TEAM" TM5 ON RS.id_rank5 = TM5.id LEFT JOIN "TEAM" TM6 ON RS.id_rank6 = TM6.id
				WHERE RS.id = _item.id_item;
				_item.comment := 'TM';
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN4.label' || _lang || ', CN5.label' || _lang || ', CN6.label' || _lang || ', CN1.label, CN2.label, CN3.label, CN4.label, CN5.label, CN6.label, RS.result1, RS.result2, RS.comment, RS.exa';
				_query := _query || ' FROM "RESULT" RS LEFT JOIN "COUNTRY" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "COUNTRY" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "COUNTRY" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "COUNTRY" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "COUNTRY" CN5 ON RS.id_rank5 = CN5.id LEFT JOIN "COUNTRY" CN6 ON RS.id_rank6 = CN6.id';
				_query := _query || ' WHERE RS.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4;
				CLOSE __c;
				_item.comment := 'CN';
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
		_query := 'SELECT DISTINCT ON (PR.last_name, PR.first_name, CN.id, SP.id) PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "PERSON" PR';
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
		_query := _query || ' ORDER BY PR.last_name, PR.first_name, SP.id LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'PR';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Teams]
	IF (_entity ~ 'SP|CN' AND (_entity_ref = 'TM' OR _entity_ref = '')) THEN
		_query := 'SELECT TM.id, TM.label, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "TEAM" TM';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON TM.id_country = CN.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON TM.id_sport = SP.id';
		IF _entity = 'SP' THEN
			_query := _query || ' WHERE TM.id_sport = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query := _query || ' WHERE TM.id_country = ' || _id;
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', TM.label LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT CT.id, CT.label' || _lang || ', CT.label, CN.id, CN.label' || _lang || ', CN.label FROM "CITY" CT';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'CN' THEN
			_query := _query || ' WHERE CT.id_country = ' || _id;
		ELSIF _entity = 'ST' THEN
			_query := _query || ' WHERE CT.id_state = ' || _id;
		END IF;
		_query := _query || ' ORDER BY CT.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.label_rel2;
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
		_query := 'SELECT CX.id, CX.label' || _lang || ', CX.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label FROM "COMPLEX" CX';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' WHERE CX.id_city = ' || _id;
		_query := _query || ' ORDER BY CX.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OL.type FROM "OLYMPICS" OL';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query := _query || ' WHERE OL.id_city = ' || _id;
		END IF;
		_query := _query || ' ORDER BY OL.type, YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT OR_.id, OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OR_.count_gold || '','' || OR_.count_silver || '','' || OR_.count_bronze FROM "OLYMPIC_RANKING" OR_';
		_query := _query || ' LEFT JOIN "OLYMPICS" OL ON OR_.id_olympics = OL.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON OR_.id_country = CN.id';
		IF _entity = 'OL' THEN
			_query := _query || ' WHERE OR_.id_olympics = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query := _query || ' WHERE OR_.id_country = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT RC.id, RC.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, RC.type1, RC.type2, RC.record1, RC.id_rank1, RC.id_rank2, RC.id_rank3, RC.id_rank4, RC.id_rank5 FROM "RECORD" RC';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RC.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RC.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RC.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "TYPE" TP ON EV.id_type = TP.id';
		_query := _query || ' WHERE TP.number BETWEEN ' || _type1 || ' AND ' || _type2;
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RC.id_rank1 = ' || _id || ' OR RC.id_rank2 = ' || _id || ' OR RC.id_rank3 = ' || _id || ' OR RC.id_rank4 = ' || _id || ' OR RC.id_rank5 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _pr_list || ') OR RC.id_rank2 IN (' || _pr_list || ') OR RC.id_rank3 IN (' || _pr_list || ') OR RC.id_rank4 IN (' || _pr_list || ') OR RC.id_rank5 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _tm_list || ') OR RC.id_rank2 IN (' || _tm_list || ') OR RC.id_rank3 IN (' || _tm_list || ') OR RC.id_rank4 IN (' || _tm_list || ') OR RC.id_rank5 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RC.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RC.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RC.id_event = ' || _id || ' OR RC.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND RC.id_city = ' || _id;
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', RC.index LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.txt1, _item.txt2, _item.txt3, _id1, _id2, _id3, _id4, _id5;
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
		_query := 'SELECT HF.id, YR.id, YR.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, HF.position FROM "HALL_OF_FAME" HF';
		_query := _query || ' LEFT JOIN "YEAR" YR ON HF.id_year = YR.id';
		_query := _query || ' LEFT JOIN "PERSON" PR ON HF.id_person = PR.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON HF.id_league = LG.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE HF.id_year = ' || _id;
		ELSIF _entity = 'PR' THEN
			_query := _query || ' WHERE HF.id_person IN (' || _pr_list || ')';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.txt1;
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
		_query := 'SELECT RN.id, TM.id, TM.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, RN.number FROM "RETIRED_NUMBER" RN';
		_query := _query || ' LEFT JOIN "TEAM" TM ON RN.id_team = TM.id';
		_query := _query || ' LEFT JOIN "PERSON" PR ON RN.id_person = PR.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON RN.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE RN.id_team = ' || _id;
		ELSIF _entity = 'PR' THEN
			_query := _query || ' WHERE RN.id_person IN (' || _pr_list || ')';
		END IF;
		_query := _query || ' ORDER BY TM.label, RN.number LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.id_rel4;
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
		_query := 'SELECT TS.id, TM.id, TM.label, CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', CX.label, CT.label, ST.label, CN.label, LG.id, LG.label, TS.date1, TS.date2 FROM "TEAM_STADIUM" TS';
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
		_query := _query || ' ORDER BY TM.label, TS.date1 DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.id_rel6, _item.comment, _item.txt1, _item.txt2;
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
		_query := 'SELECT WL.id, TM.id, TM.label, LG.id, LG.label, WL.type, WL.count_win || '','' || WL.count_loss FROM "WIN_LOSS" WL';
		_query := _query || ' LEFT JOIN "TEAM" TM ON WL.id_team = TM.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON WL.id_league = LG.id';
		_query := _query || ' WHERE WL.id_team = ' || _id;
		_query := _query || ' ORDER BY TM.label DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'WL';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Contributions]
	IF (_entity ~ 'CB' AND (_entity_ref = 'CO' OR _entity_ref = '')) THEN
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, CO.type, CO.date';
		_query := _query || ' FROM "~CONTRIBUTION" CO';
		_query := _query || ' LEFT JOIN "RESULT" RS ON CO.id_item = RS.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' WHERE RS.id_member=' || _id || ' ORDER BY CO.date DESC, YR.id DESC, SP.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.date1;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CO';
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
  

ALTER TABLE "CITY" ADD link integer;
ALTER TABLE "COMPLEX" ADD link integer;
  
  
  

CREATE OR REPLACE FUNCTION "SEARCH"(_pattern character varying, _scope character varying, _limit smallint, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_index smallint;
	_current_id integer;
	_current_label varchar(100);
	_current_label_en varchar(100);
	_current_id_rel1 integer;
	_current_id_rel2 integer;
	_current_id_rel3 integer;
	_current_label_rel1 varchar(50);
	_current_label_rel2 varchar(50);
	_current_label_rel3 varchar(50);
	_current_label_rel4 varchar(50);
	_current_label_rel5 varchar(50);
	_current_label_rel6 varchar(50);
	_current_link integer;
	_current_ref smallint;
	_scopes varchar(2)[];
	_tables varchar(15)[];
	_label varchar(10);
	_label_en varchar(10);
	_i smallint;
	_s varchar(2);
	_c refcursor;
	_query text;
	_rel_cols text;
	_rel_joins text;
	_rel_count smallint;
	__pattern text;
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'SC', _pattern, current_date);
	
	_i := 1;
	_index := 1;
	__pattern := lower(_pattern);
	__pattern := replace(__pattern, 'a', '(a|á|Á|à|ä|Ä|ă|ā|ã|å|Å|â)');
	__pattern := replace(__pattern, 'ae', '(ae|æ)');
	__pattern := replace(__pattern, 'c', '(c|ć|č|ç|Č)');
	__pattern := replace(__pattern, 'dj', '(dj|Đ|đ)');
	__pattern := replace(__pattern, 'e', '(e|ė|é|É|è|ê|ë|ě|ę|ē)');
	__pattern := replace(__pattern, 'g', '(g|ğ)');
	__pattern := replace(__pattern, 'i', '(i|ı|í|ï)');
	__pattern := replace(__pattern, 'l', '(l|ł)');
	__pattern := replace(__pattern, 'n', '(n|ń|ñ)');
	__pattern := replace(__pattern, 'o', '(o|ó|ò|ö|Ö|ō|ø|Ø)');
	__pattern := replace(__pattern, 'r', '(r|ř)');
	__pattern := replace(__pattern, 's', '(s|ś|š|Š|ş|Ş)');
	__pattern := replace(__pattern, 'ss', '(ss|ß)');
	__pattern := replace(__pattern, 't', '(t|ţ)');
	__pattern := replace(__pattern, 'u', '(u|ū|ú|ü)');
	__pattern := replace(__pattern, 'y', '(y|ý)');
	__pattern := replace(__pattern, 'z', '(z|ż|ź|ž|Ž)');
	_scopes = '{PR,CT,CX,CN,CP,EV,SP,TM,ST,YR}';
	_tables = '{PERSON,CITY,COMPLEX,COUNTRY,CHAMPIONSHIP,EVENT,SPORT,TEAM,STATE,YEAR}';
	FOR _s IN SELECT UNNEST(_scopes) LOOP
		IF _scope ~ ('(^|,)' || _s || '($|,)') OR _scope = '.' THEN
			_rel_cols := '';
			_rel_joins := '';
			_rel_count := 0;

			-- Get related fields
			IF (_s ~ 'PR|TM') THEN -- Relation: Country
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang || ' || '' ('' || CN.code || '')'', CN.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s ~ 'PR|TM') THEN -- Relation: Sport
				_rel_cols := _rel_cols || ', SP.id, SP.label' || _lang || ', SP.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "SPORT" SP ON ' || _s || '.id_sport = SP.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'PR') THEN -- Relation: Team
				_rel_cols := _rel_cols || ', TM.id, TM.label, TM.label, PR.link';
				_rel_joins := _rel_joins || ' LEFT JOIN "TEAM" TM ON ' || _s || '.id_team = TM.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'CX') THEN -- Relation: City/State/Country
				_rel_cols := _rel_cols || ', CT.id, CT.label' || _lang || ', CT.label';
				_rel_cols := _rel_cols || ', ST.id, ST.label' || _lang || ', ST.label';
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang || ', CN.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "CITY" CT ON ' || _s || '.id_city = CT.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			IF (_s = 'CT') THEN -- Relation: State/Country
				_rel_cols := _rel_cols || ', NULL, NULL, NULL';
				_rel_cols := _rel_cols || ', ST.id, ST.label' || _lang || ', ST.label';
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang || ', CN.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "STATE" ST ON ' || _s || '.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			FOR _j IN 1.._rel_count LOOP
				_rel_cols := _rel_cols || ', NULL, NULL, NULL';
			END LOOP;

			-- Execute query
			_label := 'label';
			_label_en := 'label';
			IF (_s <> 'TM' AND _s <> 'YR') THEN
				_label := 'label' || _lang;
			END IF;
			_query := 'SELECT ' || _s || '.id, ' || _s || '.' || _label || ',' || _s || '.' || _label_en || ',' || _s || '.ref' || _rel_cols || ' FROM "' || _tables[_i] || '" ' || _s;
			_query := _query || _rel_joins || ' WHERE ' || (CASE _s WHEN 'CT' THEN '(CT.link = 0 OR CT.link IS NULL) AND ' WHEN 'CX' THEN '(CX.link = 0 OR CX.link IS NULL) AND ' WHEN 'TM' THEN '(TM.link = 0 OR TM.link IS NULL) AND ' ELSE '' END) || _s || '.' || _label || ' ~* ''' || __pattern || '''' || (CASE _limit WHEN 0 THEN ' ORDER BY ' || _s || '.' || _label ELSE '' END);
			IF _s = 'PR' THEN
				_query := 'SELECT PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, PR.ref' || _rel_cols || ' FROM "PERSON" PR' || _rel_joins;
				_query := _query || ' WHERE (PR.link = 0 OR PR.link IS NULL) AND (PR.last_name || '' '' || PR.first_name ~* ''' || __pattern || ''' OR PR.first_name || '' '' || PR.last_name ~* ''' || __pattern || ''' OR PR.last_name ~* ''' || __pattern || ''' OR PR.first_name ~* ''' || __pattern || ''')';
				_query := _query || (CASE _limit WHEN 0 THEN ' ORDER BY PR.last_name, PR.first_name' ELSE '' END);
			END IF;
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _current_id, _current_label, _current_label_en, _current_ref, _current_id_rel1, _current_label_rel1, _current_label_rel4, _current_id_rel2, _current_label_rel2, _current_label_rel5, _current_id_rel3, _current_label_rel3, _current_label_rel6, _current_link;
				EXIT WHEN NOT FOUND;
				_item.id = _index;
				_item.id_item = _current_id;
				_item.label = _current_label;
				_item.count_ref = _current_ref;
				_item.entity = _s;
				_item.label_rel1 = _current_label_rel1;
				_item.label_rel3 = _current_label_rel3;
				IF _limit = 0 THEN
					_item.label_en = _current_label_en;
					_item.id_rel1 = _current_id_rel1;
					_item.id_rel2 = _current_id_rel2;
					_item.id_rel3 = _current_id_rel3;
					_item.label_rel2 = _current_label_rel2;
					_item.label_rel4 = _current_label_rel4;
					_item.label_rel5 = _current_label_rel5;
					_item.label_rel6 = _current_label_rel6;
					_item.link = _current_link;
				END IF;
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
  
  
  
  CREATE OR REPLACE FUNCTION "ENTITY_REF"(_entity character varying, _id integer, _entity_ref character varying, _limit character varying, _offset integer, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_c refcursor;
	__c refcursor;
	_query text;
	_link integer;
	_ct_list varchar(200);
	_cx_list varchar(200);
	_pr_list varchar(200);
	_tm_list varchar(200);
	_index integer;
	_type1 integer;
	_type2 integer;
	_type3 integer;
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
	_id11 integer;_id12 integer;_id13 integer;_id14 integer;
	_cn1 varchar(5);_cn2 varchar(5);_cn3 varchar(5);_cn4 varchar(5);_cn5 varchar(5);_cn6 varchar(5);
	_tm1 varchar(60);_tm2 varchar(60);_tm3 varchar(60);_tm4 varchar(60);_tm5 varchar(60);_tm6 varchar(60);
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'IF', _entity || '-' || _id, current_date);
	
	_index := 1;

	IF _entity ~ 'CT' THEN
		SELECT LINK INTO _link FROM "CITY" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "CITY" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_ct_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_ct_list := _ct_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_ct_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'CX' THEN
		SELECT LINK INTO _link FROM "COMPLEX" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "COMPLEX" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_cx_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_cx_list := _cx_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_cx_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'PR' THEN
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
	ELSIF _entity ~ 'TM' THEN
		SELECT LINK INTO _link FROM "TEAM" WHERE ID = _id AND (YEAR1 IS NULL OR YEAR1 = '');
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "TEAM" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_tm_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_tm_list := _tm_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_tm_list := cast(_id AS varchar);
		END IF;
	END IF;

	-- References in: [Draws] (MUST STAY AT FIRST)
	IF (_entity ~ 'CN|PR|TM' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT DR.id, DR.id_result, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, DR.id1_qf1, DR.id2_qf1, DR.id1_qf2, DR.id2_qf2, DR.id1_qf3, DR.id2_qf3, DR.id1_qf4, DR.id2_qf4, DR.id1_sf1, DR.id2_sf1, DR.id1_sf2, DR.id2_sf2, DR.id1_thd, DR.id2_thd, TP1.number, TP2.number, TP3.number FROM "DRAW" DR';
		_query := _query || ' LEFT JOIN "RESULT" RS ON DR.id_result = RS.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id';
		_query := _query || ' LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id';
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity = 'CN' THEN
			_query := _query || ' AND (DR.id1_sf1 <> ' || _id || ' AND DR.id1_sf2 <> ' || _id || ') AND (DR.id1_qf1 = ' || _id || ' OR DR.id2_qf1 = ' || _id || ' OR DR.id1_qf2 = ' || _id || ' OR DR.id2_qf2 = ' || _id || ' OR DR.id1_qf3 = ' || _id || ' OR DR.id2_qf3 = ' || _id || ' OR DR.id1_qf4 = ' || _id || ' OR DR.id2_qf4 = ' || _id || ' OR DR.id2_sf1 = ' || _id || ' OR DR.id2_sf2 = ' || _id || ' OR DR.id1_thd = ' || _id || ' OR DR.id2_thd = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _pr_list || ') AND DR.id1_sf2 NOT IN (' || _pr_list || ')) AND (DR.id1_qf1 IN (' || _pr_list || ') OR DR.id2_qf1 IN (' || _pr_list || ') OR DR.id1_qf2 IN (' || _pr_list || ') OR DR.id2_qf2 IN (' || _pr_list || ') OR DR.id1_qf3 IN (' || _pr_list || ') OR DR.id2_qf3 IN (' || _pr_list || ') OR DR.id1_qf4 IN (' || _pr_list || ') OR DR.id2_qf4 IN (' || _pr_list || ') OR DR.id2_sf1 IN (' || _pr_list || ') OR DR.id2_sf2 IN (' || _pr_list || ') OR DR.id1_thd IN (' || _pr_list || ') OR DR.id2_thd IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _tm_list || ') AND DR.id1_sf2 NOT IN (' || _tm_list || ')) AND (DR.id1_qf1 IN (' || _tm_list || ') OR DR.id2_qf1 IN (' || _tm_list || ') OR DR.id1_qf2 IN (' || _tm_list || ') OR DR.id2_qf2 IN (' || _tm_list || ') OR DR.id1_qf3 IN (' || _tm_list || ') OR DR.id2_qf3 IN (' || _tm_list || ') OR DR.id1_qf4 IN (' || _tm_list || ') OR DR.id2_qf4 IN (' || _tm_list || ') OR DR.id2_sf1 IN (' || _tm_list || ') OR DR.id2_sf2 IN (' || _tm_list || ') OR DR.id1_thd IN (' || _tm_list || ') OR DR.id2_thd IN (' || _tm_list || '))';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel10, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _id11, _id12, _id13, _id14, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF (_id13 = _id OR _id14 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)'))) THEN
				_item.txt2 := 'thd';
			ELSIF (_id9 = _id OR _id10 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)'))) THEN
				_item.txt2 := 'sf1';
			ELSIF (_id11 = _id OR _id12 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)'))) THEN
				_item.txt2 := 'sf2';
			ELSIF (_id1 = _id OR _id2 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)'))) THEN
				_item.txt2 := 'qf1';
			ELSIF (_id3 = _id OR _id4 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)'))) THEN
				_item.txt2 := 'qf2';
			ELSIF (_id5 = _id OR _id6 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)'))) THEN
				_item.txt2 := 'qf3';
			ELSIF (_id7 = _id OR _id8 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)'))) THEN
				_item.txt2 := 'qf4';
			END IF;
			IF _type1 <= 10 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN '', '' || PR1.first_name ELSE '''' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || '' '' ELSE '''' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN '', '' || PR2.first_name ELSE '''' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || '' '' ELSE '''' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, DR.result_' || _item.txt2;
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "PERSON" PR1 ON DR.id1_' || _item.txt2 || ' = PR1.id LEFT JOIN "PERSON" PR2 ON DR.id2_' || _item.txt2 || ' = PR2.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel8, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel9, _item.id_rel13, _cn2, _tm2, _item.txt1;
				CLOSE __c;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				_item.comment := 'PR';
			ELSIF _type1 = 50 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', TM1.label, TM2.label, NULL, NULL, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "TEAM" TM1 ON DR.id1_' || _item.txt2 || ' = TM1.id LEFT JOIN "TEAM" TM2 ON DR.id2_' || _item.txt2 || ' = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'TM';
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', CN1.label' || _lang || ', CN2.label' || _lang || ', CN1.label, CN2.label, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "COUNTRY" CN1 ON DR.id1_' || _item.txt2 || ' = CN1.id LEFT JOIN "COUNTRY" CN2 ON DR.id2_' || _item.txt2 || ' = CN2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'CN';
			END IF;
			_item.id = _index;
			_item.entity = 'RS';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Results]
	IF (_entity ~ 'CN|PR|TM|CP|EV|CT|SP|CX|OL|YR' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number, TP3.number FROM "RESULT" RS';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id';
		_query := _query || ' LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id';
		IF (_entity = 'OL') THEN
			_query := _query || ' LEFT JOIN "OLYMPICS" OL ON (OL.id_year = YR.id AND OL.type = SP.type)';
		END IF;
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RS.id_rank1 = ' || _id || ' OR RS.id_rank2 = ' || _id || ' OR RS.id_rank3 = ' || _id || ' OR RS.id_rank4 = ' || _id || ' OR RS.id_rank5 = ' || _id || ' OR RS.id_rank6 = ' || _id || ' OR RS.id_rank7 = ' || _id || ' OR RS.id_rank8 = ' || _id || ' OR RS.id_rank9 = ' || _id || ' OR RS.id_rank10 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RS.id_rank1 IN (' || _pr_list || ') OR RS.id_rank2 IN (' || _pr_list || ') OR RS.id_rank3 IN (' || _pr_list || ') OR RS.id_rank4 IN (' || _pr_list || ') OR RS.id_rank5 IN (' || _pr_list || ') OR RS.id_rank6 IN (' || _pr_list || ') OR RS.id_rank7 IN (' || _pr_list || ') OR RS.id_rank8 IN (' || _pr_list || ') OR RS.id_rank9 IN (' || _pr_list || ') OR RS.id_rank10 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RS.id_rank1 IN (' || _tm_list || ') OR RS.id_rank2 IN (' || _tm_list || ') OR RS.id_rank3 IN (' || _tm_list || ') OR RS.id_rank4 IN (' || _tm_list || ') OR RS.id_rank5 IN (' || _tm_list || ') OR RS.id_rank6 IN (' || _tm_list || ') OR RS.id_rank7 IN (' || _tm_list || ') OR RS.id_rank8 IN (' || _tm_list || ') OR RS.id_rank9 IN (' || _tm_list || ') OR RS.id_rank10 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND  (RS.id_city1 IN (' || _ct_list || ') OR RS.id_city2 IN (' || _ct_list || '))';
		ELSIF _entity = 'CX' THEN
			_query := _query || ' AND  (RS.id_complex1 IN (' || _cx_list || ') OR RS.id_complex2 IN (' || _cx_list || '))';
		ELSIF _entity = 'OL' THEN
			_query := _query || ' AND RS.id_championship=1 AND OL.id = ' || _id;
		ELSIF _entity = 'YR' THEN
			_query := _query || ' AND RS.id_year = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF _type1 <= 10 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, CN3.code, TM3.label, PR4.last_name || (CASE WHEN length(PR4.first_name) > 0 THEN ', ' || PR4.first_name ELSE '' END), (CASE WHEN length(PR4.first_name) > 0 THEN PR4.first_name || ' ' ELSE '' END) || PR4.last_name, CN4.id, CN4.code, TM4.label, PR5.last_name || (CASE WHEN length(PR5.first_name) > 0 THEN ', ' || PR5.first_name ELSE '' END), (CASE WHEN length(PR5.first_name) > 0 THEN PR5.first_name || ' ' ELSE '' END) || PR5.last_name, CN5.id, CN5.code, TM5.label, PR6.last_name || (CASE WHEN length(PR6.first_name) > 0 THEN ', ' || PR6.first_name ELSE '' END), (CASE WHEN length(PR6.first_name) > 0 THEN PR6.first_name || ' ' ELSE '' END) || PR6.last_name, CN6.id, CN6.code, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.label_rel23, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.label_rel24, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.label_rel25, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "PERSON" PR1 ON RS.id_rank1 = PR1.id LEFT JOIN "PERSON" PR2 ON RS.id_rank2 = PR2.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3 = PR3.id LEFT JOIN "PERSON" PR4 ON RS.id_rank4 = PR4.id LEFT JOIN "PERSON" PR5 ON RS.id_rank5 = PR5.id LEFT JOIN "PERSON" PR6 ON RS.id_rank6 = PR6.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "COUNTRY" CN3 ON PR3.id_country = CN3.id LEFT JOIN "COUNTRY" CN4 ON PR4.id_country = CN4.id LEFT JOIN "COUNTRY" CN5 ON PR5.id_country = CN5.id LEFT JOIN "COUNTRY" CN6 ON PR6.id_country = CN6.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id LEFT JOIN "TEAM" TM3 ON PR3.id_team = TM3.id LEFT JOIN "TEAM" TM4 ON PR4.id_team = TM4.id LEFT JOIN "TEAM" TM5 ON PR5.id_team = TM5.id LEFT JOIN "TEAM" TM6 ON PR6.id_team = TM6.id
				WHERE RS.id = _item.id_item;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				IF _cn3 IS NOT NULL THEN _item.label_rel8 := _item.label_rel8 || ' (' || _cn3 || ')';
				ELSIF _tm3 IS NOT NULL THEN _item.label_rel8 := _item.label_rel8 || ' (' || _tm3 || ')'; END IF;
				IF _cn4 IS NOT NULL THEN _item.label_rel9 := _item.label_rel9 || ' (' || _cn4 || ')';
				ELSIF _tm4 IS NOT NULL THEN _item.label_rel9 := _item.label_rel9 || ' (' || _tm4 || ')'; END IF;
				IF _cn5 IS NOT NULL THEN _item.label_rel10 := _item.label_rel10 || ' (' || _cn5 || ')';
				ELSIF _tm5 IS NOT NULL THEN _item.label_rel10 := _item.label_rel10 || ' (' || _tm5 || ')'; END IF;
				IF _cn6 IS NOT NULL THEN _item.label_rel11 := _item.label_rel11 || ' (' || _cn6 || ')';
				ELSIF _tm6 IS NOT NULL THEN _item.label_rel11 := _item.label_rel11 || ' (' || _tm6 || ')'; END IF;
				IF _type1 = 4 OR _item.txt3 = '#DOUBLE#' THEN
					_item.txt4 = '1-2/3-4/5-6';
				ELSIF _type1 = 5 OR _item.txt3 = '#TRIPLE#' THEN
					_item.txt4 = '1-3/4-6/7-9';
				END IF;
				_item.comment := 'PR';
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, NULL, NULL, NULL, NULL, NULL, NULL, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "TEAM" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "TEAM" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "TEAM" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "TEAM" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "TEAM" TM5 ON RS.id_rank5 = TM5.id LEFT JOIN "TEAM" TM6 ON RS.id_rank6 = TM6.id
				WHERE RS.id = _item.id_item;
				_item.comment := 'TM';
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN4.label' || _lang || ', CN5.label' || _lang || ', CN6.label' || _lang || ', CN1.label, CN2.label, CN3.label, CN4.label, CN5.label, CN6.label, RS.result1, RS.result2, RS.comment, RS.exa';
				_query := _query || ' FROM "RESULT" RS LEFT JOIN "COUNTRY" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "COUNTRY" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "COUNTRY" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "COUNTRY" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "COUNTRY" CN5 ON RS.id_rank5 = CN5.id LEFT JOIN "COUNTRY" CN6 ON RS.id_rank6 = CN6.id';
				_query := _query || ' WHERE RS.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4;
				CLOSE __c;
				_item.comment := 'CN';
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
		_query := 'SELECT DISTINCT ON (PR.last_name, PR.first_name, CN.id, SP.id) PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "PERSON" PR';
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
		_query := _query || ' ORDER BY PR.last_name, PR.first_name, SP.id LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'PR';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Teams]
	IF (_entity ~ 'SP|CN' AND (_entity_ref = 'TM' OR _entity_ref = '')) THEN
		_query := 'SELECT TM.id, TM.label, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "TEAM" TM';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON TM.id_country = CN.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON TM.id_sport = SP.id';
		IF _entity = 'SP' THEN
			_query := _query || ' WHERE TM.id_sport = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query := _query || ' WHERE TM.id_country = ' || _id;
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', TM.label LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT CT.id, CT.label' || _lang || ', CT.label, CN.id, CN.label' || _lang || ', CN.label FROM "CITY" CT';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'CN' THEN
			_query := _query || ' WHERE CT.id_country = ' || _id;
		ELSIF _entity = 'ST' THEN
			_query := _query || ' WHERE CT.id_state = ' || _id;
		END IF;
		_query := _query || ' ORDER BY CT.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.label_rel2;
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
		_query := 'SELECT CX.id, CX.label' || _lang || ', CX.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label FROM "COMPLEX" CX';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' WHERE CX.id_city = ' || _id;
		_query := _query || ' ORDER BY CX.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OL.type FROM "OLYMPICS" OL';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query := _query || ' WHERE OL.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY OL.type, YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT OR_.id, OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OR_.count_gold || '','' || OR_.count_silver || '','' || OR_.count_bronze FROM "OLYMPIC_RANKING" OR_';
		_query := _query || ' LEFT JOIN "OLYMPICS" OL ON OR_.id_olympics = OL.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON OR_.id_country = CN.id';
		IF _entity = 'OL' THEN
			_query := _query || ' WHERE OR_.id_olympics = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query := _query || ' WHERE OR_.id_country = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT RC.id, RC.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, RC.type1, RC.type2, RC.record1, RC.id_rank1, RC.id_rank2, RC.id_rank3, RC.id_rank4, RC.id_rank5 FROM "RECORD" RC';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RC.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RC.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RC.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "TYPE" TP ON EV.id_type = TP.id';
		_query := _query || ' WHERE TP.number BETWEEN ' || _type1 || ' AND ' || _type2;
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RC.id_rank1 = ' || _id || ' OR RC.id_rank2 = ' || _id || ' OR RC.id_rank3 = ' || _id || ' OR RC.id_rank4 = ' || _id || ' OR RC.id_rank5 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _pr_list || ') OR RC.id_rank2 IN (' || _pr_list || ') OR RC.id_rank3 IN (' || _pr_list || ') OR RC.id_rank4 IN (' || _pr_list || ') OR RC.id_rank5 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _tm_list || ') OR RC.id_rank2 IN (' || _tm_list || ') OR RC.id_rank3 IN (' || _tm_list || ') OR RC.id_rank4 IN (' || _tm_list || ') OR RC.id_rank5 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RC.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RC.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RC.id_event = ' || _id || ' OR RC.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND RC.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', RC.index LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.txt1, _item.txt2, _item.txt3, _id1, _id2, _id3, _id4, _id5;
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
		_query := 'SELECT HF.id, YR.id, YR.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, HF.position FROM "HALL_OF_FAME" HF';
		_query := _query || ' LEFT JOIN "YEAR" YR ON HF.id_year = YR.id';
		_query := _query || ' LEFT JOIN "PERSON" PR ON HF.id_person = PR.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON HF.id_league = LG.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE HF.id_year = ' || _id;
		ELSIF _entity = 'PR' THEN
			_query := _query || ' WHERE HF.id_person IN (' || _pr_list || ')';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.txt1;
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
		_query := 'SELECT RN.id, TM.id, TM.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, RN.number FROM "RETIRED_NUMBER" RN';
		_query := _query || ' LEFT JOIN "TEAM" TM ON RN.id_team = TM.id';
		_query := _query || ' LEFT JOIN "PERSON" PR ON RN.id_person = PR.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON RN.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE RN.id_team = ' || _id;
		ELSIF _entity = 'PR' THEN
			_query := _query || ' WHERE RN.id_person IN (' || _pr_list || ')';
		END IF;
		_query := _query || ' ORDER BY TM.label, RN.number LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.id_rel4;
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
		_query := 'SELECT TS.id, TM.id, TM.label, CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', CX.label, CT.label, ST.label, CN.label, LG.id, LG.label, TS.date1, TS.date2 FROM "TEAM_STADIUM" TS';
		_query := _query || ' LEFT JOIN "TEAM" TM ON TS.id_team = TM.id';
		_query := _query || ' LEFT JOIN "COMPLEX" CX ON TS.id_complex = CX.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON TS.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE TS.id_team = ' || _id;
		ELSIF _entity = 'CX' THEN
			_query := _query || ' WHERE TS.id_complex IN (' || _cx_list || ')';
		END IF;
		_query := _query || ' ORDER BY TM.label, TS.date1 DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.id_rel6, _item.comment, _item.txt1, _item.txt2;
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
		_query := 'SELECT WL.id, TM.id, TM.label, LG.id, LG.label, WL.type, WL.count_win || '','' || WL.count_loss FROM "WIN_LOSS" WL';
		_query := _query || ' LEFT JOIN "TEAM" TM ON WL.id_team = TM.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON WL.id_league = LG.id';
		_query := _query || ' WHERE WL.id_team = ' || _id;
		_query := _query || ' ORDER BY TM.label DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'WL';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Contributions]
	IF (_entity ~ 'CB' AND (_entity_ref = 'CO' OR _entity_ref = '')) THEN
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, CO.type, CO.date';
		_query := _query || ' FROM "~CONTRIBUTION" CO';
		_query := _query || ' LEFT JOIN "RESULT" RS ON CO.id_item = RS.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' WHERE RS.id_member=' || _id || ' ORDER BY CO.date DESC, YR.id DESC, SP.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.date1;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CO';
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
  
  
  
  
  


CREATE OR REPLACE FUNCTION "WIN_RECORDS"(_results text, _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
	_id1 integer;
	_id2 integer;
	_id3 integer;
	_id4 integer;
	_id5 integer;
	_id6 integer;
	_id7 integer;
	_id8 integer;
	_id9 integer;
	_id10 integer;
	_link1 integer;
	_link2 integer;
	_link3 integer;
	_link4 integer;
	_link5 integer;
	_link6 integer;
	_link7 integer;
	_link8 integer;
	_link9 integer;
	_link10 integer;
	_type smallint;
	_type2 smallint;
	_type3 smallint;
	_cmt text;
	_exa text;
	_win_str text;
	_query text;
	_c refcursor;
begin
	SELECT DISTINCT
		TP1.number, TP2.number, TP3.number
	INTO
		_type, _type2, _type3
	FROM
		"RESULT" RS
		LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id
		LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id
		LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id
	WHERE
		RS.id = CAST(CASE WHEN POSITION(',' IN _results) > 0 THEN SUBSTRING(_results FROM 1 FOR POSITION(',' IN _results) - 1) ELSE _results END AS integer);
	IF _type3 IS NOT NULL THEN
		_type := _type3;
	ELSIF _type2 IS NOT NULL THEN
		_type := _type2;
	END IF;

	DELETE FROM "~FUNC_BUFFER";
	_win_str := '';
	
	OPEN _c FOR EXECUTE '
	SELECT RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, RS.exa, RS.comment
	FROM "RESULT" RS
	WHERE RS.id IN (' || _results || ')';
	LOOP	
		FETCH _c INTO _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _exa, _cmt;
		EXIT WHEN NOT FOUND;
		IF (_type < 10) THEN
			SELECT PR.link INTO _link1 FROM "PERSON" PR WHERE PR.id = _id1;IF _link1 IS NOT NULL and _link1 > 0 THEN _id1 := _link1; END IF;
			SELECT PR.link INTO _link2 FROM "PERSON" PR WHERE PR.id = _id2;IF _link2 IS NOT NULL and _link2 > 0 THEN _id2 := _link2; END IF;
			SELECT PR.link INTO _link3 FROM "PERSON" PR WHERE PR.id = _id3;IF _link3 IS NOT NULL and _link3 > 0 THEN _id3 := _link3; END IF;
			SELECT PR.link INTO _link4 FROM "PERSON" PR WHERE PR.id = _id4;IF _link4 IS NOT NULL and _link4 > 0 THEN _id4 := _link4; END IF;
			SELECT PR.link INTO _link5 FROM "PERSON" PR WHERE PR.id = _id5;IF _link5 IS NOT NULL and _link5 > 0 THEN _id5 := _link5; END IF;
			SELECT PR.link INTO _link6 FROM "PERSON" PR WHERE PR.id = _id6;IF _link6 IS NOT NULL and _link6 > 0 THEN _id6 := _link6; END IF;
			SELECT PR.link INTO _link7 FROM "PERSON" PR WHERE PR.id = _id7;IF _link7 IS NOT NULL and _link7 > 0 THEN _id7 := _link7; END IF;
			SELECT PR.link INTO _link8 FROM "PERSON" PR WHERE PR.id = _id8;IF _link8 IS NOT NULL and _link8 > 0 THEN _id8 := _link8; END IF;
			SELECT PR.link INTO _link9 FROM "PERSON" PR WHERE PR.id = _id9;IF _link9 IS NOT NULL and _link9 > 0 THEN _id9 := _link9; END IF;
			SELECT PR.link INTO _link10 FROM "PERSON" PR WHERE PR.id = _id10;IF _link10 IS NOT NULL and _link10 > 0 THEN _id10 := _link10; END IF;
		END IF;
		IF (_id1 IS NOT NULL) THEN
			IF _win_str !~ ('(^|,)' || _id1 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id1, 0, NULL); _win_str = _win_str || ',' || _id1; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id1;
		END IF;
		IF (_id2 IS NOT NULL AND (_type = 4 OR _type = 5 OR _cmt = '#DOUBLE#' OR _cmt = '#TRIPLE#' OR _exa ~ '(^|/)1.*(/|$)')) THEN
			IF _win_str !~ ('(^|,)' || _id2 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id2, 0, NULL); _win_str = _win_str || ',' || _id2; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id2;
		END IF;
		IF (_id3 IS NOT NULL AND (_type = 5 OR _cmt = '#TRIPLE#' OR _exa ~ '(^|/)1-(3|4|5|6|7|8)(/|$)')) THEN
			IF _win_str !~ ('(^|,)' || _id3 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id3, 0, NULL); _win_str = _win_str || ',' || _id3; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id3;
		END IF;
		IF (_id4 IS NOT NULL AND _exa ~ '(^|/)1-(4|5|6|7|8)(/|$)') THEN
			IF _win_str !~ ('(^|,)' || _id4 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id4, 0, NULL); _win_str = _win_str || ',' || _id4; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id4;
		END IF;
		IF (_id5 IS NOT NULL AND _exa ~ '(^|/)1-(5|6|7|8)(/|$)') THEN
			IF _win_str !~ ('(^|,)' || _id5 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id5, 0, NULL); _win_str = _win_str || ',' || _id5; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id5;
		END IF;
		IF (_id6 IS NOT NULL AND _exa ~ '(^|/)1-(6|7|8)(/|$)') THEN
			IF _win_str !~ ('(^|,)' || _id6 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id6, 0, NULL); _win_str = _win_str || ',' || _id6; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id6;
		END IF;
		IF (_id7 IS NOT NULL AND _exa ~ '(^|/)1-(7|8)(/|$)') THEN
			IF _win_str !~ ('(^|,)' || _id7 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id7, 0, NULL); _win_str = _win_str || ',' || _id7; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id7;
		END IF;
		IF (_id8 IS NOT NULL AND _exa ~ '(^|/)1-8(/|$)') THEN
			IF _win_str !~ ('(^|,)' || _id8 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id8, 0, NULL); _win_str = _win_str || ',' || _id8; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id8;
		END IF;
	END LOOP;
	CLOSE _c;

	_query = 'SELECT FB.id AS entity_id, ' || _type || ' AS entity_type, FB.int_value AS count_win';
	IF (_type < 10) THEN
		_query = _query || ', PR.last_name || '', '' || PR.first_name AS entity_str, PR.last_name || '', '' || PR.first_name AS entity_str_en, CN.code AS entity_rel';
		_query = _query || ' FROM "~FUNC_BUFFER" FB LEFT JOIN "PERSON" PR ON FB.id = PR.id LEFT JOIN "COUNTRY" CN ON PR.id_country = CN.id';
	ELSIF (_type = 50) THEN
		_query = _query || ', TM.label AS entity_str, TM.label AS entity_str_en, NULL AS entity_rel';
		_query = _query || ' FROM "~FUNC_BUFFER" FB LEFT JOIN "TEAM" TM ON FB.id = TM.id';
	ELSIF (_type = 99) THEN
		_query = _query || ', CN.label' || _lang || ' AS entity_str, CN.label AS entity_str_en, NULL AS entity_rel';
		_query = _query || ' FROM "~FUNC_BUFFER" FB LEFT JOIN "COUNTRY" CN ON FB.id = CN.id';
	END IF;
	_query = _query || ' ORDER BY FB.int_value DESC, entity_str ASC';
	OPEN _c FOR EXECUTE _query;
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  
  CREATE OR REPLACE FUNCTION "GET_MEDAL_COUNT"(_entity character varying, _id_sport integer, _idlist character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_ids integer[];
	_type1 integer;
	_type2 integer;
	_sport_txt varchar(3);
	_index smallint;
	_nfl_cp1 smallint;
	_nfl_cp2 smallint;
	_nba_cp smallint;
	_nhl_cp smallint;
	_mlb_cp smallint;
	_count1 integer;
	_count2 integer;
	_count3 integer;
	_label_tennis_slam1 varchar(45);
	_label_tennis_slam2 varchar(45);
	_label_tennis_slam3 varchar(45);
	_label_tennis_slam4 varchar(45);
	_label_golf_slam1 varchar(45);
	_label_golf_slam2 varchar(45);
	_label_golf_slam3 varchar(45);
	_label_golf_slam4 varchar(45);
	_label_cycling_tour1 varchar(45);
	_label_cycling_tour2 varchar(45);
	_label_cycling_tour3 varchar(45);
begin
	_ids := regexp_split_to_array(_idlist, E'\\-');	
	_label_tennis_slam1 := 'Australian Open';
	_label_tennis_slam2 := 'French Open';
	_label_tennis_slam3 := 'Wimbledon';
	_label_tennis_slam4 := 'US Open';
	_label_golf_slam1 := 'Masters';
	_label_golf_slam2 := 'US Open';
	_label_golf_slam3 := 'Open Championship';
	_label_golf_slam4 := 'PGA Championship';
	_label_cycling_tour1 := 'Giro d''Italia';
	_label_cycling_tour2 := 'Tour de France';
	_label_cycling_tour3 := 'Vuelta a España';
	_nfl_cp1 := 454;
	_nfl_cp2 := 453;
	_nba_cp := 530;
	_nhl_cp := 573;
	_mlb_cp := 624;
	_index := 1;
	_sport_txt := CAST(_id_sport AS varchar);
	IF (_entity = 'PR') THEN
		_type1 := 1;
		_type2 := 10;
	ELSIF (_entity = 'TM') THEN
		_type1 := 50;
		_type2 := 50;
	ELSIF (_entity = 'CN') THEN
		_type1 := 99;
		_type2 := 99;
	END IF;
	-- Olympics
	IF _sport_txt !~ '5|18|20|23|29|33|36|37|45' AND _type2 <> 50 THEN
		_item.id := _index;
		_item.label := 'OLYMP';
		_item.txt1 := '#GOLD#';
		_item.txt2 := '#SILVER#';
		_item.txt3 := '#BRONZE#';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND (id_rank1 = ANY(_ids) OR (id_rank2 = ANY(_ids) AND (RS.comment IN ('#DOUBLE#', '#TRIPLE#') OR exa ~* '.*1-(2|3|4|5|6).*')) OR (id_rank3 = ANY(_ids) AND (RS.comment = '#TRIPLE#' OR exa ~* '.*1-(3|4|5|6).*')));
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND ((id_rank2 = ANY(_ids) AND RS.comment NOT IN ('#DOUBLE#', '#TRIPLE#')) OR (id_rank3 = ANY(_ids) AND (RS.comment <> '#TRIPLE#' AND exa ~* '.*2-(3|4|5|6).*')) OR (id_rank4 = ANY(_ids) AND (RS.comment IN ('#DOUBLE#', '#TRIPLE#') OR exa ~* '.*2-(4|5|6).*')));
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND ((id_rank3 = ANY(_ids) AND RS.comment NOT IN ('#DOUBLE#', '#TRIPLE#')) OR (id_rank4 = ANY(_ids) AND (RS.comment NOT IN ('#DOUBLE#', '#TRIPLE#') AND exa ~* '.*3-(4|5|6).*')) OR (id_rank5 = ANY(_ids) AND (RS.comment <> '#TRIPLE#' AND exa ~* '.*3-(5|6).*')));
		IF _type1 = 99 THEN
			SELECT COUNT(*) INTO _count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id LEFT JOIN "PERSON" PR1 ON RS.id_rank1=PR1.id LEFT JOIN "PERSON" PR2 ON RS.id_rank2=PR2.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3=PR3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=10) OR (id_subevent IS NOT NULL AND T2.number<=10) OR (id_subevent IS NULL AND T1.number<=10))  AND (PR1.id_country = ANY(_ids) OR (PR2.id_country = ANY(_ids) AND (exa ~* '.*1-(2|3|4|5|6).*')) OR (PR3.id_country = ANY(_ids) AND (exa ~* '.*1-(3|4|5|6).*')));
			SELECT COUNT(*) INTO _count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id LEFT JOIN "PERSON" PR2 ON RS.id_rank2=PR2.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3=PR3.id LEFT JOIN "PERSON" PR4 ON RS.id_rank4=PR4.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=10) OR (id_subevent IS NOT NULL AND T2.number<=10) OR (id_subevent IS NULL AND T1.number<=10))  AND (PR2.id_country = ANY(_ids) OR (PR3.id_country = ANY(_ids) AND (exa ~* '.*2-(3|4|5|6).*')) OR (PR4.id_country = ANY(_ids) AND (exa ~* '.*2-(4|5|6).*')));
			SELECT COUNT(*) INTO _count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3=PR3.id LEFT JOIN "PERSON" PR4 ON RS.id_rank4=PR4.id LEFT JOIN "PERSON" PR5 ON RS.id_rank5=PR5.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=10) OR (id_subevent IS NOT NULL AND T2.number<=10) OR (id_subevent IS NULL AND T1.number<=10))  AND (PR3.id_country = ANY(_ids) OR (PR4.id_country = ANY(_ids) AND (exa ~* '.*3-(4|5|6).*')) OR (PR5.id_country = ANY(_ids) AND (exa ~* '.*3-(5|6).*')));
			_item.count1 := _item.count1 + _count1;
			_item.count2 := _item.count2 + _count2;
			_item.count3 := _item.count3 + _count3;
		END IF;
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- World Championships
	IF _sport_txt !~ '20|22|23|26|29' AND _type2 <= 10 THEN
		_item.id := _index;	
		_item.label := 'WORLDCP';
		IF _sport_txt ~ '5|18|33|36' THEN
			_item.txt1 := 'rank.1';
			_item.txt2 := 'rank.2';
			_item.txt3 := 'rank.3';
		ELSE
			_item.txt1 := '#GOLD#';
			_item.txt2 := '#SILVER#';
			_item.txt3 := '#BRONZE#';
		END IF;
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND (id_rank1 = ANY(_ids) OR (id_rank2 = ANY(_ids) AND (exa ~* '.*1-(2|3|4|5|6).*')) OR (id_rank3 = ANY(_ids) AND (exa ~* '.*1-(3|4|5|6).*')));
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND (id_rank2 = ANY(_ids) OR (id_rank3 = ANY(_ids) AND (exa ~* '.*2-(3|4|5|6).*')) OR (id_rank4 = ANY(_ids) AND (exa ~* '.*2-(4|5|6).*')));
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND (id_rank3 = ANY(_ids) OR (id_rank4 = ANY(_ids) AND (exa ~* '.*3-(4|5|6).*')) OR (id_rank5 = ANY(_ids) AND (exa ~* '.*3-(5|6).*')));
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Grand Slam (Tennis)
	IF _id_sport = 22 AND _type2 <= 10 THEN
		_item.id := _index;
		_item.label := 'GSLAM';
		_item.txt1 := 'Aus';
		_item.txt2 := 'RG';
		_item.txt3 := 'Wim';
		_item.txt4 := 'US';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam1 OR SE2.label=_label_tennis_slam1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam2 OR SE2.label=_label_tennis_slam2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam3 OR SE2.label=_label_tennis_slam3) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count4 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam4 OR SE2.label=_label_tennis_slam4) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Majors (Golf)
	IF _id_sport = 20 AND _type2 <= 10 THEN
		_item.id := _index;
		_item.label := 'MAJORS';
		_item.txt1 := 'Mas';
		_item.txt2 := 'US';
		_item.txt3 := 'Brit';
		_item.txt4 := 'PGA';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam1 OR SE2.label=_label_golf_slam1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam2 OR SE2.label=_label_golf_slam2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam3 OR SE2.label=_label_golf_slam3) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count4 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam4 OR SE2.label=_label_golf_slam4) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Major Tours (Cycling)
	IF _id_sport = 19 AND _type2 <= 10 THEN
		_item.id := _index;
		_item.label := 'MTOURS';
		_item.txt1 := 'Gir';
		_item.txt2 := 'Tour';
		_item.txt3 := 'Vuel';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (EV.label=_label_cycling_tour1 OR SE.label=_label_cycling_tour1 OR SE2.label=_label_cycling_tour1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (EV.label=_label_cycling_tour2 OR SE.label=_label_cycling_tour2 OR SE2.label=_label_cycling_tour2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (EV.label=_label_cycling_tour3 OR SE.label=_label_cycling_tour3 OR SE2.label=_label_cycling_tour3) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- NFL Titles
	IF _id_sport = 23 AND _type2 = 50 THEN
		-- (Super Bowl)
		_item.id := _index;
		_item.label := 'NFLCP1';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp1 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp1 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp1) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
		-- (NFL Championships)
		_item.id := _index;
		_item.label := 'NFLCP2';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp2 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp2 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp2) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	-- NBA Titles
	IF _id_sport = 24 AND _type2 = 50 THEN
		_item.id := _index;
		_item.label := 'NBACP';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nba_cp AND id_subevent2 IS NULL) OR id_subevent2=_nba_cp) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nba_cp AND id_subevent2 IS NULL) OR id_subevent2=_nba_cp) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	-- NHL Titles
	IF _id_sport = 25 AND _type2 = 50 THEN
		_item.id := _index;
		_item.label := 'NHLCP';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nhl_cp AND id_subevent2 IS NULL) OR id_subevent2=_nhl_cp) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nhl_cp AND id_subevent2 IS NULL) OR id_subevent2=_nhl_cp) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	-- MLB Titles
	IF _id_sport = 26 AND _type2 = 50 THEN
		_item.id := _index;
		_item.label := 'MLBCP';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_mlb_cp AND id_subevent2 IS NULL) OR id_subevent2=_mlb_cp) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_mlb_cp AND id_subevent2 IS NULL) OR id_subevent2=_mlb_cp) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	RETURN;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
  
  
  
  CREATE OR REPLACE FUNCTION "SEARCH"(_pattern character varying, _scope character varying, _limit smallint, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_index smallint;
	_current_id integer;
	_current_label varchar(100);
	_current_label_en varchar(100);
	_current_id_rel1 integer;
	_current_id_rel2 integer;
	_current_id_rel3 integer;
	_current_label_rel1 varchar(50);
	_current_label_rel2 varchar(50);
	_current_label_rel3 varchar(50);
	_current_label_rel4 varchar(50);
	_current_label_rel5 varchar(50);
	_current_label_rel6 varchar(50);
	_current_link integer;
	_current_ref smallint;
	_scopes varchar(2)[];
	_tables varchar(15)[];
	_label varchar(10);
	_label_en varchar(10);
	_i smallint;
	_s varchar(2);
	_c refcursor;
	_query text;
	_rel_cols text;
	_rel_joins text;
	_rel_count smallint;
	__pattern text;
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'SC', _pattern, current_date);
	
	_i := 1;
	_index := 1;
	__pattern := lower(_pattern);
	__pattern := replace(__pattern, 'a', '(a|á|Á|à|ä|Ä|ă|ā|ã|å|Å|â)');
	__pattern := replace(__pattern, 'ae', '(ae|æ)');
	__pattern := replace(__pattern, 'c', '(c|ć|č|ç|Č)');
	__pattern := replace(__pattern, 'dj', '(dj|Đ|đ)');
	__pattern := replace(__pattern, 'e', '(e|ė|é|É|è|ê|ë|ě|ę|ē)');
	__pattern := replace(__pattern, 'g', '(g|ğ)');
	__pattern := replace(__pattern, 'i', '(i|ı|í|ï)');
	__pattern := replace(__pattern, 'l', '(l|ł)');
	__pattern := replace(__pattern, 'n', '(n|ń|ñ)');
	__pattern := replace(__pattern, 'o', '(o|ó|ò|ö|Ö|ō|ø|Ø)');
	__pattern := replace(__pattern, 'r', '(r|ř)');
	__pattern := replace(__pattern, 's', '(s|ś|š|Š|ş|Ş)');
	__pattern := replace(__pattern, 'ss', '(ss|ß)');
	__pattern := replace(__pattern, 't', '(t|ţ)');
	__pattern := replace(__pattern, 'u', '(u|ū|ú|ü)');
	__pattern := replace(__pattern, 'y', '(y|ý)');
	__pattern := replace(__pattern, 'z', '(z|ż|ź|ž|Ž)');
	_scopes = '{PR,CT,CX,CN,CP,EV,SP,TM,ST,YR}';
	_tables = '{PERSON,CITY,COMPLEX,COUNTRY,CHAMPIONSHIP,EVENT,SPORT,TEAM,STATE,YEAR}';
	FOR _s IN SELECT UNNEST(_scopes) LOOP
		IF _scope ~ ('(^|,)' || _s || '($|,)') OR _scope = '.' THEN
			_rel_cols := '';
			_rel_joins := '';
			_rel_count := 0;

			-- Get related fields
			IF (_s ~ 'PR|TM') THEN -- Relation: Country
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang || ' || '' ('' || CN.code || '')'', CN.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s ~ 'PR|TM') THEN -- Relation: Sport
				_rel_cols := _rel_cols || ', SP.id, SP.label' || _lang || ', SP.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "SPORT" SP ON ' || _s || '.id_sport = SP.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'PR') THEN -- Relation: Team
				_rel_cols := _rel_cols || ', TM.id, TM.label, TM.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "TEAM" TM ON ' || _s || '.id_team = TM.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'CX') THEN -- Relation: City/State/Country
				_rel_cols := _rel_cols || ', CT.id, CT.label' || _lang || ', CT.label';
				_rel_cols := _rel_cols || ', ST.id, ST.label' || _lang || ', ST.label';
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang || ', CN.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "CITY" CT ON ' || _s || '.id_city = CT.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			IF (_s = 'CT') THEN -- Relation: State/Country
				_rel_cols := _rel_cols || ', NULL, NULL, NULL';
				_rel_cols := _rel_cols || ', ST.id, ST.label' || _lang || ', ST.label';
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang || ', CN.label';
				_rel_joins := _rel_joins || ' LEFT JOIN "STATE" ST ON ' || _s || '.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			IF (_s ~ 'CT|CX|PR|TM') THEN
				_rel_cols := _rel_cols || ', ' || _s || '.link';
			END IF;
			FOR _j IN 1.._rel_count LOOP
				_rel_cols := _rel_cols || ', NULL, NULL, NULL';
			END LOOP;

			-- Execute query
			_label := 'label';
			_label_en := 'label';
			IF (_s <> 'TM' AND _s <> 'YR') THEN
				_label := 'label' || _lang;
			END IF;
			_query := 'SELECT ' || _s || '.id, ' || _s || '.' || _label || ',' || _s || '.' || _label_en || ',' || _s || '.ref' || _rel_cols || ' FROM "' || _tables[_i] || '" ' || _s;
			_query := _query || _rel_joins || ' WHERE ' || (CASE _s WHEN 'CT' THEN '(CT.link = 0 OR CT.link IS NULL) AND ' WHEN 'CX' THEN '(CX.link = 0 OR CX.link IS NULL) AND ' WHEN 'TM' THEN '(TM.link = 0 OR TM.link IS NULL) AND ' ELSE '' END) || _s || '.' || _label || ' ~* ''' || __pattern || '''' || (CASE _limit WHEN 0 THEN ' ORDER BY ' || _s || '.' || _label ELSE '' END);
			IF _s = 'PR' THEN
				_query := 'SELECT PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, PR.ref' || _rel_cols || ' FROM "PERSON" PR' || _rel_joins;
				_query := _query || ' WHERE (PR.link = 0 OR PR.link IS NULL) AND (PR.last_name || '' '' || PR.first_name ~* ''' || __pattern || ''' OR PR.first_name || '' '' || PR.last_name ~* ''' || __pattern || ''' OR PR.last_name ~* ''' || __pattern || ''' OR PR.first_name ~* ''' || __pattern || ''')';
				_query := _query || (CASE _limit WHEN 0 THEN ' ORDER BY PR.last_name, PR.first_name' ELSE '' END);
			END IF;
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _current_id, _current_label, _current_label_en, _current_ref, _current_id_rel1, _current_label_rel1, _current_label_rel4, _current_id_rel2, _current_label_rel2, _current_label_rel5, _current_id_rel3, _current_label_rel3, _current_label_rel6, _current_link;
				EXIT WHEN NOT FOUND;
				_item.id = _index;
				_item.id_item = _current_id;
				_item.label = _current_label;
				IF _current_link IS NOT NULL THEN
					IF _s = 'CT' THEN
						SELECT SUM(ref) INTO _item.count_ref FROM "CITY" WHERE id=_current_id OR link=_current_id;
					ELSIF _s = 'CX' THEN
						SELECT SUM(ref) INTO _item.count_ref FROM "COMPLEX" WHERE id=_current_id OR link=_current_id;
					ELSIF _s = 'PR' THEN
						SELECT SUM(ref) INTO _item.count_ref FROM "PERSON" WHERE id=_current_id OR link=_current_id;
					ELSIF _s = 'TM' THEN
						SELECT SUM(ref) INTO _item.count_ref FROM "TEAM" WHERE id=_current_id OR link=_current_id;
					END IF;
				ELSE
					_item.count_ref = (CASE WHEN _current_ref IS NOT NULL THEN _current_ref ELSE 0 END);
				END IF;
				_item.entity = _s;
				_item.label_rel1 = _current_label_rel1;
				_item.label_rel3 = _current_label_rel3;
				IF _limit = 0 THEN
					_item.label_en = _current_label_en;
					_item.id_rel1 = _current_id_rel1;
					_item.id_rel2 = _current_id_rel2;
					_item.id_rel3 = _current_id_rel3;
					_item.label_rel2 = _current_label_rel2;
					_item.label_rel4 = _current_label_rel4;
					_item.label_rel5 = _current_label_rel5;
					_item.label_rel6 = _current_label_rel6;
					_item.link = _current_link;
				END IF;
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
  
  
  
  
  CREATE TABLE "~FOLDER_HISTORY"
(
  id integer NOT NULL,
  previous_params character varying(30),
  current_params character varying(30),
  current_path character varying(255),
  date timestamp
);

CREATE SEQUENCE "~SQ_FOLDER_HISTORY";

  
  
  
  
  CREATE OR REPLACE FUNCTION "WIN_RECORDS"(_results text, _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
	_id1 integer;
	_id2 integer;
	_id3 integer;
	_id4 integer;
	_id5 integer;
	_id6 integer;
	_id7 integer;
	_id8 integer;
	_id9 integer;
	_id10 integer;
	_link1 integer;
	_link2 integer;
	_link3 integer;
	_link4 integer;
	_link5 integer;
	_link6 integer;
	_link7 integer;
	_link8 integer;
	_link9 integer;
	_link10 integer;
	_type smallint;
	_type2 smallint;
	_type3 smallint;
	_cmt text;
	_exa text;
	_win_str text;
	_query text;
	_c refcursor;
begin
	SELECT DISTINCT
		TP1.number, TP2.number, TP3.number
	INTO
		_type, _type2, _type3
	FROM
		"RESULT" RS
		LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id
		LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id
		LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id
	WHERE
		RS.id = CAST(CASE WHEN POSITION(',' IN _results) > 0 THEN SUBSTRING(_results FROM 1 FOR POSITION(',' IN _results) - 1) ELSE _results END AS integer);
	IF _type3 IS NOT NULL THEN
		_type := _type3;
	ELSIF _type2 IS NOT NULL THEN
		_type := _type2;
	END IF;

	DELETE FROM "~FUNC_BUFFER";
	_win_str := '';
	
	OPEN _c FOR EXECUTE '
	SELECT RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, RS.exa, RS.comment
	FROM "RESULT" RS
	WHERE RS.id IN (' || _results || ')';
	LOOP	
		FETCH _c INTO _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _exa, _cmt;
		EXIT WHEN NOT FOUND;
		IF (_type < 10) THEN
			SELECT PR.link INTO _link1 FROM "PERSON" PR WHERE PR.id = _id1;IF _link1 IS NOT NULL and _link1 > 0 THEN _id1 := _link1; END IF;
			SELECT PR.link INTO _link2 FROM "PERSON" PR WHERE PR.id = _id2;IF _link2 IS NOT NULL and _link2 > 0 THEN _id2 := _link2; END IF;
			SELECT PR.link INTO _link3 FROM "PERSON" PR WHERE PR.id = _id3;IF _link3 IS NOT NULL and _link3 > 0 THEN _id3 := _link3; END IF;
			SELECT PR.link INTO _link4 FROM "PERSON" PR WHERE PR.id = _id4;IF _link4 IS NOT NULL and _link4 > 0 THEN _id4 := _link4; END IF;
			SELECT PR.link INTO _link5 FROM "PERSON" PR WHERE PR.id = _id5;IF _link5 IS NOT NULL and _link5 > 0 THEN _id5 := _link5; END IF;
			SELECT PR.link INTO _link6 FROM "PERSON" PR WHERE PR.id = _id6;IF _link6 IS NOT NULL and _link6 > 0 THEN _id6 := _link6; END IF;
			SELECT PR.link INTO _link7 FROM "PERSON" PR WHERE PR.id = _id7;IF _link7 IS NOT NULL and _link7 > 0 THEN _id7 := _link7; END IF;
			SELECT PR.link INTO _link8 FROM "PERSON" PR WHERE PR.id = _id8;IF _link8 IS NOT NULL and _link8 > 0 THEN _id8 := _link8; END IF;
			SELECT PR.link INTO _link9 FROM "PERSON" PR WHERE PR.id = _id9;IF _link9 IS NOT NULL and _link9 > 0 THEN _id9 := _link9; END IF;
			SELECT PR.link INTO _link10 FROM "PERSON" PR WHERE PR.id = _id10;IF _link10 IS NOT NULL and _link10 > 0 THEN _id10 := _link10; END IF;
		ELSIF (_type = 50) THEN
			SELECT TM.link INTO _link1 FROM "TEAM" TM WHERE TM.id = _id1;IF _link1 IS NOT NULL and _link1 > 0 THEN _id1 := _link1; END IF;
			SELECT TM.link INTO _link2 FROM "TEAM" TM WHERE TM.id = _id2;IF _link2 IS NOT NULL and _link2 > 0 THEN _id2 := _link2; END IF;
			SELECT TM.link INTO _link3 FROM "TEAM" TM WHERE TM.id = _id3;IF _link3 IS NOT NULL and _link3 > 0 THEN _id3 := _link3; END IF;
			SELECT TM.link INTO _link4 FROM "TEAM" TM WHERE TM.id = _id4;IF _link4 IS NOT NULL and _link4 > 0 THEN _id4 := _link4; END IF;
			SELECT TM.link INTO _link5 FROM "TEAM" TM WHERE TM.id = _id5;IF _link5 IS NOT NULL and _link5 > 0 THEN _id5 := _link5; END IF;
			SELECT TM.link INTO _link6 FROM "TEAM" TM WHERE TM.id = _id6;IF _link6 IS NOT NULL and _link6 > 0 THEN _id6 := _link6; END IF;
			SELECT TM.link INTO _link7 FROM "TEAM" TM WHERE TM.id = _id7;IF _link7 IS NOT NULL and _link7 > 0 THEN _id7 := _link7; END IF;
			SELECT TM.link INTO _link8 FROM "TEAM" TM WHERE TM.id = _id8;IF _link8 IS NOT NULL and _link8 > 0 THEN _id8 := _link8; END IF;
			SELECT TM.link INTO _link9 FROM "TEAM" TM WHERE TM.id = _id9;IF _link9 IS NOT NULL and _link9 > 0 THEN _id9 := _link9; END IF;
			SELECT TM.link INTO _link10 FROM "TEAM" TM WHERE TM.id = _id10;IF _link10 IS NOT NULL and _link10 > 0 THEN _id10 := _link10; END IF;
		END IF;
		IF (_id1 IS NOT NULL) THEN
			IF _win_str !~ ('(^|,)' || _id1 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id1, 0, NULL); _win_str = _win_str || ',' || _id1; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id1;
		END IF;
		IF (_id2 IS NOT NULL AND (_type = 4 OR _type = 5 OR _cmt = '#DOUBLE#' OR _cmt = '#TRIPLE#' OR _exa ~ '(^|/)1.*(/|$)')) THEN
			IF _win_str !~ ('(^|,)' || _id2 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id2, 0, NULL); _win_str = _win_str || ',' || _id2; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id2;
		END IF;
		IF (_id3 IS NOT NULL AND (_type = 5 OR _cmt = '#TRIPLE#' OR _exa ~ '(^|/)1-(3|4|5|6|7|8)(/|$)')) THEN
			IF _win_str !~ ('(^|,)' || _id3 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id3, 0, NULL); _win_str = _win_str || ',' || _id3; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id3;
		END IF;
		IF (_id4 IS NOT NULL AND _exa ~ '(^|/)1-(4|5|6|7|8)(/|$)') THEN
			IF _win_str !~ ('(^|,)' || _id4 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id4, 0, NULL); _win_str = _win_str || ',' || _id4; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id4;
		END IF;
		IF (_id5 IS NOT NULL AND _exa ~ '(^|/)1-(5|6|7|8)(/|$)') THEN
			IF _win_str !~ ('(^|,)' || _id5 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id5, 0, NULL); _win_str = _win_str || ',' || _id5; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id5;
		END IF;
		IF (_id6 IS NOT NULL AND _exa ~ '(^|/)1-(6|7|8)(/|$)') THEN
			IF _win_str !~ ('(^|,)' || _id6 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id6, 0, NULL); _win_str = _win_str || ',' || _id6; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id6;
		END IF;
		IF (_id7 IS NOT NULL AND _exa ~ '(^|/)1-(7|8)(/|$)') THEN
			IF _win_str !~ ('(^|,)' || _id7 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id7, 0, NULL); _win_str = _win_str || ',' || _id7; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id7;
		END IF;
		IF (_id8 IS NOT NULL AND _exa ~ '(^|/)1-8(/|$)') THEN
			IF _win_str !~ ('(^|,)' || _id8 || '($|,)') THEN INSERT INTO "~FUNC_BUFFER" VALUES (_id8, 0, NULL); _win_str = _win_str || ',' || _id8; END IF;
			UPDATE "~FUNC_BUFFER" SET int_value = int_value + 1 WHERE id = _id8;
		END IF;
	END LOOP;
	CLOSE _c;

	_query = 'SELECT FB.id AS entity_id, ' || _type || ' AS entity_type, FB.int_value AS count_win';
	IF (_type < 10) THEN
		_query = _query || ', PR.last_name || '', '' || PR.first_name AS entity_str, PR.last_name || '', '' || PR.first_name AS entity_str_en, CN.code AS entity_rel';
		_query = _query || ' FROM "~FUNC_BUFFER" FB LEFT JOIN "PERSON" PR ON FB.id = PR.id LEFT JOIN "COUNTRY" CN ON PR.id_country = CN.id';
	ELSIF (_type = 50) THEN
		_query = _query || ', TM.label AS entity_str, TM.label AS entity_str_en, NULL AS entity_rel';
		_query = _query || ' FROM "~FUNC_BUFFER" FB LEFT JOIN "TEAM" TM ON FB.id = TM.id';
	ELSIF (_type = 99) THEN
		_query = _query || ', CN.label' || _lang || ' AS entity_str, CN.label AS entity_str_en, NULL AS entity_rel';
		_query = _query || ' FROM "~FUNC_BUFFER" FB LEFT JOIN "COUNTRY" CN ON FB.id = CN.id';
	END IF;
	_query = _query || ' ORDER BY FB.int_value DESC, entity_str ASC';
	OPEN _c FOR EXECUTE _query;
	RETURN  _c;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  
  
  
  CREATE TRIGGER trigger_pl
  AFTER INSERT OR UPDATE OR DELETE
  ON "~PERSON_LIST"
  FOR EACH ROW
  EXECUTE PROCEDURE "UPDATE_REF"('PL');
  
  
  
  
  CREATE OR REPLACE FUNCTION "UPDATE_REF"()
  RETURNS trigger AS
$BODY$
DECLARE
	_row record;
	_entity varchar(2);
	_id_event integer;
	_id_subevent integer;
	_id_subevent2 integer;
	_id_olympics integer;
	_sp_type integer;
	_type integer;
BEGIN
	_entity := TG_ARGV[0];
	IF (TG_OP = 'DELETE') THEN
		_row := OLD;
	ELSIF (TG_OP IN ('INSERT', 'UPDATE')) THEN
		_row := NEW;
	END IF;

	IF _entity = 'CT' THEN
		UPDATE "STATE" SET REF="COUNT_REF"('ST', _row.id_state) WHERE id=_row.id_state;
		UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_country) WHERE id=_row.id_country;
	ELSIF _entity = 'CX' THEN
		UPDATE "CITY" SET REF="COUNT_REF"('CT', _row.id_city) WHERE id=_row.id_city;
	ELSIF _entity = 'DR' THEN
		SELECT RS.id_event, RS.id_subevent, RS.id_subevent2 INTO _id_event, _id_subevent, _id_subevent2 FROM "RESULT" RS WHERE RS.id = _row.id_result;

		IF _id_subevent2 IS NOT NULL AND _id_subevent2 <> 0 THEN
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _id_subevent2;
		ELSIF _id_subevent IS NOT NULL AND _id_subevent <> 0 THEN
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _id_subevent;
		ELSE
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _id_event;
		END IF;

		IF _type < 10 THEN
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id1_qf1) WHERE id=_row.id1_qf1;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id2_qf1) WHERE id=_row.id2_qf1;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id1_qf2) WHERE id=_row.id1_qf2;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id2_qf2) WHERE id=_row.id2_qf2;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id1_qf3) WHERE id=_row.id1_qf3;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id2_qf3) WHERE id=_row.id2_qf3;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id1_qf4) WHERE id=_row.id1_qf4;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id2_qf4) WHERE id=_row.id2_qf4;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id1_sf1) WHERE id=_row.id1_sf1;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id2_sf1) WHERE id=_row.id2_sf1;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id1_sf2) WHERE id=_row.id1_sf2;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id2_sf2) WHERE id=_row.id2_sf2;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id1_thd) WHERE id=_row.id1_thd;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id2_thd) WHERE id=_row.id2_thd;
		ELSIF _type = 50 THEN
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id1_qf1) WHERE id=_row.id1_qf1;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id2_qf1) WHERE id=_row.id2_qf1;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id1_qf2) WHERE id=_row.id1_qf2;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id2_qf2) WHERE id=_row.id2_qf2;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id1_qf3) WHERE id=_row.id1_qf3;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id2_qf3) WHERE id=_row.id2_qf3;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id1_qf4) WHERE id=_row.id1_qf4;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id2_qf4) WHERE id=_row.id2_qf4;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id1_sf1) WHERE id=_row.id1_sf1;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id2_sf1) WHERE id=_row.id2_sf1;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id1_sf2) WHERE id=_row.id1_sf2;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id2_sf2) WHERE id=_row.id2_sf2;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id1_thd) WHERE id=_row.id1_thd;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id2_thd) WHERE id=_row.id2_thd;
		ELSIF _type = 99 THEN
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id1_qf1) WHERE id=_row.id1_qf1;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id2_qf1) WHERE id=_row.id2_qf1;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id1_qf2) WHERE id=_row.id1_qf2;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id2_qf2) WHERE id=_row.id2_qf2;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id1_qf3) WHERE id=_row.id1_qf3;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id2_qf3) WHERE id=_row.id2_qf3;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id1_qf4) WHERE id=_row.id1_qf4;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id2_qf4) WHERE id=_row.id2_qf4;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id1_sf1) WHERE id=_row.id1_sf1;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id2_sf1) WHERE id=_row.id2_sf1;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id1_sf2) WHERE id=_row.id1_sf2;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id2_sf2) WHERE id=_row.id2_sf2;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id1_thd) WHERE id=_row.id1_thd;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id2_thd) WHERE id=_row.id2_thd;
		END IF;
	ELSIF _entity = 'HF' THEN
		UPDATE "YEAR" SET REF="COUNT_REF"('YR', _row.id_year) WHERE id=_row.id_year;
		UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_person) WHERE id=_row.id_person;
	ELSIF _entity = 'OL' THEN
		UPDATE "YEAR" SET REF="COUNT_REF"('YR', _row.id_year) WHERE id=_row.id_year;
		UPDATE "CITY" SET REF="COUNT_REF"('CT', _row.id_city) WHERE id=_row.id_city;
	ELSIF _entity = 'OR' THEN
		UPDATE "OLYMPICS" SET REF="COUNT_REF"('OL', _row.id_olympics) WHERE id=_row.id_olympics;
		UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_country) WHERE id=_row.id_country;
	ELSIF _entity = 'PL' THEN
		UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_person) WHERE id=_row.id_person;
	ELSIF _entity = 'PR' THEN
		UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_country) WHERE id=_row.id_country;
		UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_team) WHERE id=_row.id_team;
		UPDATE "SPORT" SET REF="COUNT_REF"('SP', _row.id_sport) WHERE id=_row.id_sport;
	ELSIF _entity = 'RC' THEN
		UPDATE "SPORT" SET REF="COUNT_REF"('SP', _row.id_sport) WHERE id=_row.id_sport;
		UPDATE "CHAMPIONSHIP" SET REF="COUNT_REF"('CP', _row.id_championship) WHERE id=_row.id_championship;
		UPDATE "EVENT" SET REF="COUNT_REF"('EV', _row.id_event) WHERE id=_row.id_event;
		UPDATE "EVENT" SET REF="COUNT_REF"('EV', _row.id_subevent) WHERE id=_row.id_subevent;
		UPDATE "CITY" SET REF="COUNT_REF"('CT', _row.id_city) WHERE id=_row.id_city;

		IF _row.id_subevent IS NOT NULL AND _row.id_subevent <> 0 THEN
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _row.id_subevent;
		ELSE
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _row.id_event;
		END IF;

		IF _type < 10 THEN
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank5) WHERE id=_row.id_rank5;
		ELSIF _type = 50 THEN
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank5) WHERE id=_row.id_rank5;
		ELSIF _type = 99 THEN
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank5) WHERE id=_row.id_rank5;
		END IF;
	ELSIF _entity = 'RS' THEN
		UPDATE "YEAR" SET REF="COUNT_REF"('YR', _row.id_year) WHERE id=_row.id_year;
		UPDATE "SPORT" SET REF="COUNT_REF"('SP', _row.id_sport) WHERE id=_row.id_sport;
		UPDATE "CHAMPIONSHIP" SET REF="COUNT_REF"('CP', _row.id_championship) WHERE id=_row.id_championship;
		UPDATE "EVENT" SET REF="COUNT_REF"('EV', _row.id_event) WHERE id=_row.id_event;
		UPDATE "EVENT" SET REF="COUNT_REF"('EV', _row.id_subevent) WHERE id=_row.id_subevent;
		UPDATE "EVENT" SET REF="COUNT_REF"('EV', _row.id_subevent2) WHERE id=_row.id_subevent2;
		UPDATE "COMPLEX" SET REF="COUNT_REF"('CX', _row.id_complex1) WHERE id=_row.id_complex1;
		UPDATE "COMPLEX" SET REF="COUNT_REF"('CX', _row.id_complex2) WHERE id=_row.id_complex2;
		UPDATE "CITY" SET REF="COUNT_REF"('CT', _row.id_city1) WHERE id=_row.id_city1;
		UPDATE "CITY" SET REF="COUNT_REF"('CT', _row.id_city2) WHERE id=_row.id_city2;
		IF _row.id_championship = 1 THEN
			SELECT SP.type INTO _sp_type FROM "SPORT" SP WHERE SP.id=_row.id_sport;
			SELECT OL.id INTO _id_olympics FROM "OLYMPICS" OL WHERE OL.id_year=_row.id_year AND OL.type=_sp_type;
			UPDATE "OLYMPICS" SET REF="COUNT_REF"('OL', _id_olympics) WHERE id=_id_olympics;
		END IF;
		IF _row.id_subevent2 IS NOT NULL AND _row.id_subevent2 <> 0 THEN
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _row.id_subevent2;
		ELSIF _row.id_subevent IS NOT NULL AND _row.id_subevent <> 0 THEN
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _row.id_subevent;
		ELSE
			SELECT TP.number INTO _type FROM "EVENT" EV LEFT JOIN "TYPE" TP ON EV.id_type = TP.id WHERE EV.id = _row.id_event;
		END IF;

		IF _type < 10 THEN
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank5) WHERE id=_row.id_rank5;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank6) WHERE id=_row.id_rank6;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank7) WHERE id=_row.id_rank7;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank8) WHERE id=_row.id_rank8;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank9) WHERE id=_row.id_rank9;
			UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_rank10) WHERE id=_row.id_rank10;
		ELSIF _type = 50 THEN
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank5) WHERE id=_row.id_rank5;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank6) WHERE id=_row.id_rank6;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank7) WHERE id=_row.id_rank7;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank8) WHERE id=_row.id_rank8;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank9) WHERE id=_row.id_rank9;
			UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_rank10) WHERE id=_row.id_rank10;
		ELSIF _type = 99 THEN
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank5) WHERE id=_row.id_rank5;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank6) WHERE id=_row.id_rank6;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank7) WHERE id=_row.id_rank7;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank8) WHERE id=_row.id_rank8;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank9) WHERE id=_row.id_rank9;
			UPDATE "COUNTRY" SET REF="COUNT_REF"('CN', _row.id_rank10) WHERE id=_row.id_rank10;
		END IF;
	ELSIF _entity = 'RN' THEN
		UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_team) WHERE id=_row.id_team;
		UPDATE "PERSON" SET REF="COUNT_REF"('PR', _row.id_person) WHERE id=_row.id_person;
	ELSIF _entity = 'TM' THEN
		UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.link) WHERE id=_row.link;
	ELSIF _entity = 'TS' THEN
		UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_team) WHERE id=_row.id_team;
		UPDATE "COMPLEX" SET REF="COUNT_REF"('CX', _row.id_complex) WHERE id=_row.id_complex;
	ELSIF _entity = 'WL' THEN
		UPDATE "TEAM" SET REF="COUNT_REF"('TM', _row.id_team) WHERE id=_row.id_team;
	END IF;
	
        RETURN NULL;
    END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  
CREATE OR REPLACE FUNCTION "COUNT_REF"(_entity character varying, _id integer)
  RETURNS integer AS
$BODY$
declare
	_count integer;
	_n integer;
	_type1 integer;
	_type2 integer;
begin
	IF _id IS NULL THEN
		RETURN 0;
	END IF;
	_count := 0;

	-- Count '_id' referenced in: Cities
	IF _entity = 'CN' THEN -- Country
		SELECT COUNT(*) INTO _n FROM "CITY" CT WHERE CT.id_country = _id; _count := _count + _n;
	ELSIF _entity = 'ST' THEN -- State
		SELECT COUNT(*) INTO _n FROM "CITY" CT WHERE CT.id_state = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Complexes
	IF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM "COMPLEX" CX WHERE CX.id_city = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Draws
	IF _entity ~ 'CN|PR|TM' THEN
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		SELECT COUNT(*) INTO _n FROM "DRAW" DR
			LEFT JOIN "RESULT" RS ON DR.id_result = RS.id
			LEFT JOIN "EVENT" EV ON RS.id_event = EV.id
			LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id
			LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id
			LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id
			LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id
			LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id
		WHERE (DR.id1_qf1 = _id OR DR.id2_qf1 = _id OR DR.id1_qf2 = _id OR DR.id2_qf2 = _id OR DR.id1_qf3 = _id OR DR.id2_qf3 = _id OR DR.id1_qf4 = _id OR DR.id2_qf4 = _id OR DR.id1_sf1 = _id OR DR.id2_sf1 = _id OR DR.id1_sf2 = _id OR DR.id2_sf2 = _id OR DR.id1_thd = _id OR DR.id2_thd = _id)
			AND ((TP1.number BETWEEN _type1 AND _type2 AND TP2.number IS NULL) OR (TP2.number BETWEEN _type1 AND _type2 AND TP3.number IS NULL) OR TP3.number BETWEEN _type1 AND _type2);
		_count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Hall of Fame
	IF _entity = 'YR' THEN -- Year
		SELECT COUNT(*) INTO _n FROM "HALL_OF_FAME" HF WHERE HF.id_year = _id; _count := _count + _n;
	ELSIF _entity = 'PR' THEN -- Person
		SELECT COUNT(*) INTO _n FROM "HALL_OF_FAME" HF WHERE HF.id_person = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Olympics
	IF _entity = 'YR' THEN -- Year
		SELECT COUNT(*) INTO _n FROM "OLYMPICS" OL WHERE OL.id_year = _id; _count := _count + _n;
	ELSIF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM "OLYMPICS" OL WHERE OL.id_city = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Olympic Rankings
	IF _entity = 'OL' THEN -- Olympics
		SELECT COUNT(*) INTO _n FROM "OLYMPIC_RANKING" OR_ WHERE OR_.id_olympics = _id; _count := _count + _n;
	ELSIF _entity = 'CN' THEN -- Country
		SELECT COUNT(*) INTO _n FROM "OLYMPIC_RANKING" OR_ WHERE OR_.id_country = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Persons
	IF _entity = 'CN' THEN -- Country
		SELECT COUNT(*) INTO _n FROM "PERSON" PR WHERE PR.id_country = _id; _count := _count + _n;
	ELSIF _entity = 'TM' THEN -- Team
		SELECT COUNT(*) INTO _n FROM "PERSON" PR WHERE PR.id_team = _id; _count := _count + _n;
	ELSIF _entity = 'SP' THEN -- Sport
		SELECT COUNT(*) INTO _n FROM "PERSON" PR WHERE PR.id_sport = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Person Lists
	IF _entity = 'PR' THEN -- Person
		SELECT COUNT(*) INTO _n FROM "~PERSON_LIST" PL WHERE PL.id_person = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Records
	IF _entity ~ 'CN|PR|TM' THEN -- Country/Person/Team
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		SELECT COUNT(*) INTO _n FROM "RECORD" RC
			LEFT JOIN "EVENT" EV ON RC.id_event = EV.id
			LEFT JOIN "TYPE" TP ON EV.id_type = TP.id
		WHERE (RC.ID_RANK1 = _id OR RC.ID_RANK2 = _id OR RC.ID_RANK3 = _id OR RC.ID_RANK4 = _id OR RC.ID_RANK5 = _id)
			AND TP.number BETWEEN _type1 AND _type2;
		_count := _count + _n;
	ELSIF _entity = 'SP' THEN -- Sport
		SELECT COUNT(*) INTO _n FROM "RECORD" RC WHERE RC.id_sport = _id; _count := _count + _n;
	ELSIF _entity = 'CP' THEN -- Championship
		SELECT COUNT(*) INTO _n FROM "RECORD" RC WHERE RC.id_championship = _id; _count := _count + _n;
	ELSIF _entity = 'EV' THEN -- Event
		SELECT COUNT(*) INTO _n FROM "RECORD" RC WHERE RC.id_event = _id OR RC.id_subevent = _id; _count := _count + _n;
	ELSIF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM "RECORD" RC WHERE RC.id_city = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Results
	IF _entity ~ 'CN|PR|TM' THEN -- Country/Person/Team
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		SELECT COUNT(*) INTO _n FROM "RESULT" RS
			LEFT JOIN "EVENT" EV ON RS.id_event = EV.id
			LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id
			LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id
			LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id
			LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id
			LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id
		WHERE (RS.id_rank1 = _id OR RS.id_rank2 = _id OR RS.id_rank3 = _id OR RS.id_rank4 = _id OR RS.id_rank5 = _id OR RS.id_rank6 = _id OR RS.id_rank7 = _id OR RS.id_rank8 = _id OR RS.id_rank9 = _id OR RS.id_rank10 = _id)
			AND ((TP1.number BETWEEN _type1 AND _type2 AND TP2.number IS NULL) OR (TP2.number BETWEEN _type1 AND _type2 AND TP3.number IS NULL) OR TP3.number BETWEEN _type1 AND _type2);
		_count := _count + _n;
	ELSIF _entity = 'SP' THEN -- Sport
		SELECT COUNT(*) INTO _n FROM "RESULT" RS WHERE RS.id_sport = _id; _count := _count + _n;
	ELSIF _entity = 'CP' THEN -- Championship
		SELECT COUNT(*) INTO _n FROM "RESULT" RS WHERE RS.id_championship = _id; _count := _count + _n;
	ELSIF _entity = 'EV' THEN -- Event
		SELECT COUNT(*) INTO _n FROM "RESULT" RS WHERE RS.id_event = _id OR RS.id_subevent = _id OR RS.id_subevent2 = _id; _count := _count + _n;
	ELSIF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM "RESULT" RS WHERE RS.id_city1 = _id OR RS.id_city2 = _id; _count := _count + _n;
	ELSIF _entity = 'CX' THEN -- Complex
		SELECT COUNT(*) INTO _n FROM "RESULT" RS WHERE RS.id_complex1 = _id OR RS.id_complex2 = _id; _count := _count + _n;
	ELSIF _entity = 'YR' THEN -- Year
		SELECT COUNT(*) INTO _n FROM "RESULT" RS WHERE RS.id_year = _id; _count := _count + _n;
	ELSIF _entity = 'OL' THEN -- Olympics
		SELECT COUNT(*) INTO _n FROM "RESULT" RS LEFT JOIN "OLYMPICS" OL ON RS.id_year = OL.id_year LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id WHERE OL.id = _id AND RS.id_championship = 1 AND SP.type = OL.type; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Retired Numbers
	IF _entity = 'TM' THEN -- Team
		SELECT COUNT(*) INTO _n FROM "RETIRED_NUMBER" RN WHERE RN.id_team = _id; _count := _count + _n;
	ELSIF _entity = 'PR' THEN -- Person
		SELECT COUNT(*) INTO _n FROM "RETIRED_NUMBER" RN WHERE RN.id_person = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Teams
	IF _entity = 'SP' THEN -- Sport
		SELECT COUNT(*) INTO _n FROM "TEAM" TM WHERE TM.id_sport = _id; _count := _count + _n;
	ELSIF _entity = 'CN' THEN -- Country
		SELECT COUNT(*) INTO _n FROM "TEAM" TM WHERE TM.id_country = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Team Stadiums
	IF _entity = 'TM' THEN -- Team
		SELECT COUNT(*) INTO _n FROM "TEAM_STADIUM" TS WHERE TS.id_team = _id; _count := _count + _n;
	ELSIF _entity = 'CX' THEN -- Complex
		SELECT COUNT(*) INTO _n FROM "TEAM_STADIUM" TS WHERE TS.id_complex = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Wins/Losses
	IF _entity = 'TM' THEN -- Team
		SELECT COUNT(*) INTO _n FROM "WIN_LOSS" WL WHERE WL.id_team = _id; _count := _count + _n;
	END IF;
	
	RETURN _count;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
  
  
  
  
  CREATE OR REPLACE FUNCTION "ENTITY_REF"(_entity character varying, _id integer, _entity_ref character varying, _limit character varying, _offset integer, _lang character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_c refcursor;
	__c refcursor;
	_query text;
	_link integer;
	_ct_list varchar(200);
	_cx_list varchar(200);
	_pr_list varchar(200);
	_tm_list varchar(200);
	_index integer;
	_type1 integer;
	_type2 integer;
	_type3 integer;
	_id1 integer;_id2 integer;_id3 integer;_id4 integer;_id5 integer;
	_id6 integer;_id7 integer;_id8 integer;_id9 integer;_id10 integer;
	_id11 integer;_id12 integer;_id13 integer;_id14 integer;
	_cn1 varchar(5);_cn2 varchar(5);_cn3 varchar(5);_cn4 varchar(5);_cn5 varchar(5);_cn6 varchar(5);
	_tm1 varchar(60);_tm2 varchar(60);_tm3 varchar(60);_tm4 varchar(60);_tm5 varchar(60);_tm6 varchar(60);
begin
	INSERT INTO "~REQUEST" VALUES (NEXTVAL('"~SQ_REQUEST"'), 'IF', _entity || '-' || _id, current_date);
	
	_index := 1;

	IF _entity ~ 'CT' THEN
		SELECT LINK INTO _link FROM "CITY" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "CITY" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_ct_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_ct_list := _ct_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_ct_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'CX' THEN
		SELECT LINK INTO _link FROM "COMPLEX" WHERE ID = _id;
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "COMPLEX" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_cx_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_cx_list := _cx_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_cx_list := cast(_id AS varchar);
		END IF;
	ELSIF _entity ~ 'PR' THEN
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
	ELSIF _entity ~ 'TM' THEN
		SELECT LINK INTO _link FROM "TEAM" WHERE ID = _id AND (YEAR1 IS NULL OR YEAR1 = '');
		IF _link IS NOT NULL THEN
			_query := 'SELECT ID FROM "TEAM" WHERE ';
			IF _link = 0 THEN
				_query := _query || 'ID = ' || _id || ' OR LINK = ' || _id;
			ELSE
				_query := _query || 'ID = ' || _link || ' OR LINK = ' || _link;
			END IF;
			_tm_list := '-1';
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _link;
				EXIT WHEN NOT FOUND;
				_tm_list := _tm_list || ',' || _link;
			END LOOP;
			CLOSE _c;
		ELSE
			_tm_list := cast(_id AS varchar);
		END IF;
	END IF;

	-- References in: [Draws] (MUST STAY AT FIRST)
	IF (_entity ~ 'CN|PR|TM' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT DR.id, DR.id_result, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, DR.id1_qf1, DR.id2_qf1, DR.id1_qf2, DR.id2_qf2, DR.id1_qf3, DR.id2_qf3, DR.id1_qf4, DR.id2_qf4, DR.id1_sf1, DR.id2_sf1, DR.id1_sf2, DR.id2_sf2, DR.id1_thd, DR.id2_thd, TP1.number, TP2.number, TP3.number FROM "DRAW" DR';
		_query := _query || ' LEFT JOIN "RESULT" RS ON DR.id_result = RS.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id';
		_query := _query || ' LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id';
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity = 'CN' THEN
			_query := _query || ' AND (DR.id1_sf1 <> ' || _id || ' AND DR.id1_sf2 <> ' || _id || ') AND (DR.id1_qf1 = ' || _id || ' OR DR.id2_qf1 = ' || _id || ' OR DR.id1_qf2 = ' || _id || ' OR DR.id2_qf2 = ' || _id || ' OR DR.id1_qf3 = ' || _id || ' OR DR.id2_qf3 = ' || _id || ' OR DR.id1_qf4 = ' || _id || ' OR DR.id2_qf4 = ' || _id || ' OR DR.id2_sf1 = ' || _id || ' OR DR.id2_sf2 = ' || _id || ' OR DR.id1_thd = ' || _id || ' OR DR.id2_thd = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _pr_list || ') AND DR.id1_sf2 NOT IN (' || _pr_list || ')) AND (DR.id1_qf1 IN (' || _pr_list || ') OR DR.id2_qf1 IN (' || _pr_list || ') OR DR.id1_qf2 IN (' || _pr_list || ') OR DR.id2_qf2 IN (' || _pr_list || ') OR DR.id1_qf3 IN (' || _pr_list || ') OR DR.id2_qf3 IN (' || _pr_list || ') OR DR.id1_qf4 IN (' || _pr_list || ') OR DR.id2_qf4 IN (' || _pr_list || ') OR DR.id2_sf1 IN (' || _pr_list || ') OR DR.id2_sf2 IN (' || _pr_list || ') OR DR.id1_thd IN (' || _pr_list || ') OR DR.id2_thd IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (DR.id1_sf1 NOT IN (' || _tm_list || ') AND DR.id1_sf2 NOT IN (' || _tm_list || ')) AND (DR.id1_qf1 IN (' || _tm_list || ') OR DR.id2_qf1 IN (' || _tm_list || ') OR DR.id1_qf2 IN (' || _tm_list || ') OR DR.id2_qf2 IN (' || _tm_list || ') OR DR.id1_qf3 IN (' || _tm_list || ') OR DR.id2_qf3 IN (' || _tm_list || ') OR DR.id1_qf4 IN (' || _tm_list || ') OR DR.id2_qf4 IN (' || _tm_list || ') OR DR.id2_sf1 IN (' || _tm_list || ') OR DR.id2_sf2 IN (' || _tm_list || ') OR DR.id1_thd IN (' || _tm_list || ') OR DR.id2_thd IN (' || _tm_list || '))';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel10, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _id11, _id12, _id13, _id14, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF (_id13 = _id OR _id14 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id13 || '|' || _id14 || ')($|,)'))) THEN
				_item.txt2 := 'thd';
			ELSIF (_id9 = _id OR _id10 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id9 || '|' || _id10 || ')($|,)'))) THEN
				_item.txt2 := 'sf1';
			ELSIF (_id11 = _id OR _id12 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id11 || '|' || _id12 || ')($|,)'))) THEN
				_item.txt2 := 'sf2';
			ELSIF (_id1 = _id OR _id2 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id1 || '|' || _id2 || ')($|,)'))) THEN
				_item.txt2 := 'qf1';
			ELSIF (_id3 = _id OR _id4 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id3 || '|' || _id4 || ')($|,)'))) THEN
				_item.txt2 := 'qf2';
			ELSIF (_id5 = _id OR _id6 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id5 || '|' || _id6 || ')($|,)'))) THEN
				_item.txt2 := 'qf3';
			ELSIF (_id7 = _id OR _id8 = _id OR (_pr_list IS NOT NULL AND _pr_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)')) OR (_tm_list IS NOT NULL AND _tm_list ~ ('(^|,)(' || _id7 || '|' || _id8 || ')($|,)'))) THEN
				_item.txt2 := 'qf4';
			END IF;
			IF _type1 <= 10 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN '', '' || PR1.first_name ELSE '''' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || '' '' ELSE '''' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN '', '' || PR2.first_name ELSE '''' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || '' '' ELSE '''' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, DR.result_' || _item.txt2;
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "PERSON" PR1 ON DR.id1_' || _item.txt2 || ' = PR1.id LEFT JOIN "PERSON" PR2 ON DR.id2_' || _item.txt2 || ' = PR2.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel8, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel9, _item.id_rel13, _cn2, _tm2, _item.txt1;
				CLOSE __c;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				_item.comment := 'PR';
			ELSIF _type1 = 50 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', TM1.label, TM2.label, NULL, NULL, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "TEAM" TM1 ON DR.id1_' || _item.txt2 || ' = TM1.id LEFT JOIN "TEAM" TM2 ON DR.id2_' || _item.txt2 || ' = TM2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'TM';
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id1_' || _item.txt2 || ', id2_' || _item.txt2 || ', CN1.label' || _lang || ', CN2.label' || _lang || ', CN1.label, CN2.label, DR.result_' || _item.txt2 || '';
				_query := _query || ' FROM "DRAW" DR LEFT JOIN "COUNTRY" CN1 ON DR.id1_' || _item.txt2 || ' = CN1.id LEFT JOIN "COUNTRY" CN2 ON DR.id2_' || _item.txt2 || ' = CN2.id';
				_query := _query || ' WHERE DR.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.txt1;
				CLOSE __c;
				_item.comment := 'CN';
			END IF;
			_item.id = _index;
			_item.entity = 'RS';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Results]
	IF (_entity ~ 'CN|PR|TM|CP|EV|CT|SP|CX|OL|YR' AND (_entity_ref = 'RS' OR _entity_ref = '')) THEN
		_type1 = 1;
		_type2 = 99;
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, RS.id_rank1, RS.id_rank2, RS.id_rank3, RS.id_rank4, RS.id_rank5, RS.id_rank6, RS.id_rank7, RS.id_rank8, RS.id_rank9, RS.id_rank10, TP1.number, TP2.number, TP3.number FROM "RESULT" RS';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id';
		_query := _query || ' LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id';
		_query := _query || ' LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id';
		IF (_entity = 'OL') THEN
			_query := _query || ' LEFT JOIN "OLYMPICS" OL ON (OL.id_year = YR.id AND OL.type = SP.type)';
		ELSIF (_entity = 'PR') THEN
			_type1 = 1;
			_type2 = 99;
			_query := _query || ' LEFT JOIN "~PERSON_LIST" PL ON PL.id_result = RS.id';
		END IF;
		_query := _query || ' WHERE ((TP1.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP2.number IS NULL) OR (TP2.number BETWEEN ' || _type1 || ' AND ' || _type2 || ' AND TP3.number IS NULL) OR (TP3.number BETWEEN ' || _type1 || ' AND ' || _type2 || '))';
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RS.id_rank1 = ' || _id || ' OR RS.id_rank2 = ' || _id || ' OR RS.id_rank3 = ' || _id || ' OR RS.id_rank4 = ' || _id || ' OR RS.id_rank5 = ' || _id || ' OR RS.id_rank6 = ' || _id || ' OR RS.id_rank7 = ' || _id || ' OR RS.id_rank8 = ' || _id || ' OR RS.id_rank9 = ' || _id || ' OR RS.id_rank10 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (PL.id_person IN (' || _pr_list || ') OR (((TP1.number BETWEEN 1 AND 10 AND TP2.number IS NULL) OR (TP2.number BETWEEN 1 AND 10 AND TP3.number IS NULL) OR (TP3.number BETWEEN 1 AND 10)) AND ((RS.id_rank1 IN (' || _pr_list || ') OR RS.id_rank2 IN (' || _pr_list || ') OR RS.id_rank3 IN (' || _pr_list || ') OR RS.id_rank4 IN (' || _pr_list || ') OR RS.id_rank5 IN (' || _pr_list || ') OR RS.id_rank6 IN (' || _pr_list || ') OR RS.id_rank7 IN (' || _pr_list || ') OR RS.id_rank8 IN (' || _pr_list || ') OR RS.id_rank9 IN (' || _pr_list || ') OR RS.id_rank10 IN (' || _pr_list || ')))))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RS.id_rank1 IN (' || _tm_list || ') OR RS.id_rank2 IN (' || _tm_list || ') OR RS.id_rank3 IN (' || _tm_list || ') OR RS.id_rank4 IN (' || _tm_list || ') OR RS.id_rank5 IN (' || _tm_list || ') OR RS.id_rank6 IN (' || _tm_list || ') OR RS.id_rank7 IN (' || _tm_list || ') OR RS.id_rank8 IN (' || _tm_list || ') OR RS.id_rank9 IN (' || _tm_list || ') OR RS.id_rank10 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RS.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RS.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RS.id_event = ' || _id || ' OR RS.id_subevent = ' || _id || ' OR RS.id_subevent2 = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND  (RS.id_city1 IN (' || _ct_list || ') OR RS.id_city2 IN (' || _ct_list || '))';
		ELSIF _entity = 'CX' THEN
			_query := _query || ' AND  (RS.id_complex1 IN (' || _cx_list || ') OR RS.id_complex2 IN (' || _cx_list || '))';
		ELSIF _entity = 'OL' THEN
			_query := _query || ' AND RS.id_championship=1 AND OL.id = ' || _id;
		ELSIF _entity = 'YR' THEN
			_query := _query || ' AND RS.id_year = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id DESC, SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel12, _item.label_rel13, _item.label_rel14, _item.label_rel15, _item.label_rel16, _id1, _id2, _id3, _id4, _id5, _id6, _id7, _id8, _id9, _id10, _type1, _type2, _type3;
			EXIT WHEN NOT FOUND;
			IF _type3 IS NOT NULL THEN
				_type1 := _type3;
			ELSIF _type2 IS NOT NULL THEN
				_type1 := _type2;
			END IF;
			IF _type1 <= 10 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, PR1.last_name || (CASE WHEN length(PR1.first_name) > 0 THEN ', ' || PR1.first_name ELSE '' END), (CASE WHEN length(PR1.first_name) > 0 THEN PR1.first_name || ' ' ELSE '' END) || PR1.last_name, CN1.id, CN1.code, TM1.label, PR2.last_name || (CASE WHEN length(PR2.first_name) > 0 THEN ', ' || PR2.first_name ELSE '' END), (CASE WHEN length(PR2.first_name) > 0 THEN PR2.first_name || ' ' ELSE '' END) || PR2.last_name, CN2.id, CN2.code, TM2.label, PR3.last_name || (CASE WHEN length(PR3.first_name) > 0 THEN ', ' || PR3.first_name ELSE '' END), (CASE WHEN length(PR3.first_name) > 0 THEN PR3.first_name || ' ' ELSE '' END) || PR3.last_name, CN3.id, CN3.code, TM3.label, PR4.last_name || (CASE WHEN length(PR4.first_name) > 0 THEN ', ' || PR4.first_name ELSE '' END), (CASE WHEN length(PR4.first_name) > 0 THEN PR4.first_name || ' ' ELSE '' END) || PR4.last_name, CN4.id, CN4.code, TM4.label, PR5.last_name || (CASE WHEN length(PR5.first_name) > 0 THEN ', ' || PR5.first_name ELSE '' END), (CASE WHEN length(PR5.first_name) > 0 THEN PR5.first_name || ' ' ELSE '' END) || PR5.last_name, CN5.id, CN5.code, TM5.label, PR6.last_name || (CASE WHEN length(PR6.first_name) > 0 THEN ', ' || PR6.first_name ELSE '' END), (CASE WHEN length(PR6.first_name) > 0 THEN PR6.first_name || ' ' ELSE '' END) || PR6.last_name, CN6.id, CN6.code, TM6.label, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel20, _item.id_rel12, _cn1, _tm1, _item.label_rel7, _item.label_rel21, _item.id_rel13, _cn2, _tm2, _item.label_rel8, _item.label_rel22, _item.id_rel14, _cn3, _tm3, _item.label_rel9, _item.label_rel23, _item.id_rel15, _cn4, _tm4, _item.label_rel10, _item.label_rel24, _item.id_rel16, _cn5, _tm5, _item.label_rel11, _item.label_rel25, _item.id_rel17, _cn6, _tm6, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "PERSON" PR1 ON RS.id_rank1 = PR1.id LEFT JOIN "PERSON" PR2 ON RS.id_rank2 = PR2.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3 = PR3.id LEFT JOIN "PERSON" PR4 ON RS.id_rank4 = PR4.id LEFT JOIN "PERSON" PR5 ON RS.id_rank5 = PR5.id LEFT JOIN "PERSON" PR6 ON RS.id_rank6 = PR6.id LEFT JOIN "COUNTRY" CN1 ON PR1.id_country = CN1.id LEFT JOIN "COUNTRY" CN2 ON PR2.id_country = CN2.id LEFT JOIN "COUNTRY" CN3 ON PR3.id_country = CN3.id LEFT JOIN "COUNTRY" CN4 ON PR4.id_country = CN4.id LEFT JOIN "COUNTRY" CN5 ON PR5.id_country = CN5.id LEFT JOIN "COUNTRY" CN6 ON PR6.id_country = CN6.id LEFT JOIN "TEAM" TM1 ON PR1.id_team = TM1.id LEFT JOIN "TEAM" TM2 ON PR2.id_team = TM2.id LEFT JOIN "TEAM" TM3 ON PR3.id_team = TM3.id LEFT JOIN "TEAM" TM4 ON PR4.id_team = TM4.id LEFT JOIN "TEAM" TM5 ON PR5.id_team = TM5.id LEFT JOIN "TEAM" TM6 ON PR6.id_team = TM6.id
				WHERE RS.id = _item.id_item;
				IF _cn1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _cn1 || ')';
				ELSIF _tm1 IS NOT NULL THEN _item.label_rel6 := _item.label_rel6 || ' (' || _tm1 || ')'; END IF;
				IF _cn2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _cn2 || ')';
				ELSIF _tm2 IS NOT NULL THEN _item.label_rel7 := _item.label_rel7 || ' (' || _tm2 || ')'; END IF;
				IF _cn3 IS NOT NULL THEN _item.label_rel8 := _item.label_rel8 || ' (' || _cn3 || ')';
				ELSIF _tm3 IS NOT NULL THEN _item.label_rel8 := _item.label_rel8 || ' (' || _tm3 || ')'; END IF;
				IF _cn4 IS NOT NULL THEN _item.label_rel9 := _item.label_rel9 || ' (' || _cn4 || ')';
				ELSIF _tm4 IS NOT NULL THEN _item.label_rel9 := _item.label_rel9 || ' (' || _tm4 || ')'; END IF;
				IF _cn5 IS NOT NULL THEN _item.label_rel10 := _item.label_rel10 || ' (' || _cn5 || ')';
				ELSIF _tm5 IS NOT NULL THEN _item.label_rel10 := _item.label_rel10 || ' (' || _tm5 || ')'; END IF;
				IF _cn6 IS NOT NULL THEN _item.label_rel11 := _item.label_rel11 || ' (' || _cn6 || ')';
				ELSIF _tm6 IS NOT NULL THEN _item.label_rel11 := _item.label_rel11 || ' (' || _tm6 || ')'; END IF;
				IF _type1 = 4 OR _item.txt3 = '#DOUBLE#' THEN
					_item.txt4 = '1-2/3-4/5-6';
				ELSIF _type1 = 5 OR _item.txt3 = '#TRIPLE#' THEN
					_item.txt4 = '1-3/4-6/7-9';
				END IF;
				_item.comment := 'PR';
			ELSIF _type1 = 50 THEN
				SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, TM1.label, TM2.label, TM3.label, TM4.label, TM5.label, TM6.label, NULL, NULL, NULL, NULL, NULL, NULL, RS.result1, RS.result2, RS.comment, RS.exa
				INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4
				FROM "RESULT" RS LEFT JOIN "TEAM" TM1 ON RS.id_rank1 = TM1.id LEFT JOIN "TEAM" TM2 ON RS.id_rank2 = TM2.id LEFT JOIN "TEAM" TM3 ON RS.id_rank3 = TM3.id LEFT JOIN "TEAM" TM4 ON RS.id_rank4 = TM4.id LEFT JOIN "TEAM" TM5 ON RS.id_rank5 = TM5.id LEFT JOIN "TEAM" TM6 ON RS.id_rank6 = TM6.id
				WHERE RS.id = _item.id_item;
				_item.comment := 'TM';
			ELSIF _type1 = 99 THEN
				_query := 'SELECT id_rank1, id_rank2, id_rank3, id_rank4, id_rank5, id_rank6, CN1.label' || _lang || ', CN2.label' || _lang || ', CN3.label' || _lang || ', CN4.label' || _lang || ', CN5.label' || _lang || ', CN6.label' || _lang || ', CN1.label, CN2.label, CN3.label, CN4.label, CN5.label, CN6.label, RS.result1, RS.result2, RS.comment, RS.exa';
				_query := _query || ' FROM "RESULT" RS LEFT JOIN "COUNTRY" CN1 ON RS.id_rank1 = CN1.id LEFT JOIN "COUNTRY" CN2 ON RS.id_rank2 = CN2.id LEFT JOIN "COUNTRY" CN3 ON RS.id_rank3 = CN3.id LEFT JOIN "COUNTRY" CN4 ON RS.id_rank4 = CN4.id LEFT JOIN "COUNTRY" CN5 ON RS.id_rank5 = CN5.id LEFT JOIN "COUNTRY" CN6 ON RS.id_rank6 = CN6.id';
				_query := _query || ' WHERE RS.id = ' || _item.id_item;
				OPEN __c FOR EXECUTE _query;
				FETCH __c INTO _item.id_rel6, _item.id_rel7, _item.id_rel8, _item.id_rel9, _item.id_rel10, _item.id_rel11, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.label_rel11, _item.label_rel20, _item.label_rel21, _item.label_rel22, _item.label_rel23, _item.label_rel24, _item.label_rel25, _item.txt1, _item.txt2, _item.txt3, _item.txt4;
				CLOSE __c;
				_item.comment := 'CN';
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
		_query := 'SELECT DISTINCT ON (PR.last_name, PR.first_name, CN.id, SP.id) PR.id, PR.last_name || (CASE WHEN length(PR.first_name) > 0 THEN '', '' || PR.first_name ELSE '''' END), (CASE WHEN length(PR.first_name) > 0 THEN PR.first_name || '' '' ELSE '''' END) || PR.last_name, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "PERSON" PR';
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
		_query := _query || ' ORDER BY PR.last_name, PR.first_name, SP.id LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'PR';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Teams]
	IF (_entity ~ 'SP|CN' AND (_entity_ref = 'TM' OR _entity_ref = '')) THEN
		_query := 'SELECT TM.id, TM.label, CN.id, CN.label' || _lang || ', SP.id, SP.label' || _lang || ', CN.label, SP.label FROM "TEAM" TM';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON TM.id_country = CN.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON TM.id_sport = SP.id';
		IF _entity = 'SP' THEN
			_query := _query || ' WHERE TM.id_sport = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query := _query || ' WHERE TM.id_country = ' || _id;
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', TM.label LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT CT.id, CT.label' || _lang || ', CT.label, CN.id, CN.label' || _lang || ', CN.label FROM "CITY" CT';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'CN' THEN
			_query := _query || ' WHERE CT.id_country = ' || _id;
		ELSIF _entity = 'ST' THEN
			_query := _query || ' WHERE CT.id_state = ' || _id;
		END IF;
		_query := _query || ' ORDER BY CT.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.label_rel2;
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
		_query := 'SELECT CX.id, CX.label' || _lang || ', CX.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label FROM "COMPLEX" CX';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' WHERE CX.id_city = ' || _id;
		_query := _query || ' ORDER BY CX.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.label_en, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.label_rel4;
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
		_query := 'SELECT OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OL.type FROM "OLYMPICS" OL';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE OL.id_year = ' || _id;
		ELSIF _entity = 'CT' THEN
			_query := _query || ' WHERE OL.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY OL.type, YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT OR_.id, OL.id, YR.id, YR.label, CT.id, CT.label' || _lang || ', CN.id, CN.label' || _lang || ', CT.label, CN.label, OR_.count_gold || '','' || OR_.count_silver || '','' || OR_.count_bronze FROM "OLYMPIC_RANKING" OR_';
		_query := _query || ' LEFT JOIN "OLYMPICS" OL ON OR_.id_olympics = OL.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON OL.id_year = YR.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON OL.id_city = CT.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON OR_.id_country = CN.id';
		IF _entity = 'OL' THEN
			_query := _query || ' WHERE OR_.id_olympics = ' || _id;
		ELSIF _entity = 'CN' THEN
			_query := _query || ' WHERE OR_.id_country = ' || _id;
		END IF;
		_query := _query || ' ORDER BY YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.comment;
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
		_query := 'SELECT RC.id, RC.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, RC.type1, RC.type2, RC.record1, RC.id_rank1, RC.id_rank2, RC.id_rank3, RC.id_rank4, RC.id_rank5 FROM "RECORD" RC';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RC.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RC.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RC.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "TYPE" TP ON EV.id_type = TP.id';
		_query := _query || ' WHERE TP.number BETWEEN ' || _type1 || ' AND ' || _type2;
		IF _entity = 'CN' THEN
			_query := _query || ' AND (RC.id_rank1 = ' || _id || ' OR RC.id_rank2 = ' || _id || ' OR RC.id_rank3 = ' || _id || ' OR RC.id_rank4 = ' || _id || ' OR RC.id_rank5 = ' || _id || ')';
		ELSIF _entity = 'PR' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _pr_list || ') OR RC.id_rank2 IN (' || _pr_list || ') OR RC.id_rank3 IN (' || _pr_list || ') OR RC.id_rank4 IN (' || _pr_list || ') OR RC.id_rank5 IN (' || _pr_list || '))';
		ELSIF _entity = 'TM' THEN
			_query := _query || ' AND (RC.id_rank1 IN (' || _tm_list || ') OR RC.id_rank2 IN (' || _tm_list || ') OR RC.id_rank3 IN (' || _tm_list || ') OR RC.id_rank4 IN (' || _tm_list || ') OR RC.id_rank5 IN (' || _tm_list || '))';
		ELSIF _entity = 'SP' THEN
			_query := _query || ' AND RC.id_sport = ' || _id;
		ELSIF _entity = 'CP' THEN
			_query := _query || ' AND RC.id_championship = ' || _id;
		ELSIF _entity = 'EV' THEN
			_query := _query || ' AND  (RC.id_event = ' || _id || ' OR RC.id_subevent = ' || _id || ')';
		ELSIF _entity = 'CT' THEN
			_query := _query || ' AND RC.id_city IN (' || _ct_list || ')';
		END IF;
		_query := _query || ' ORDER BY SP.label' || _lang || ', CP.index, EV.index, SE.index, CP.label' || _lang || ', EV.label' || _lang || ', SE.label' || _lang || ', RC.index LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.label, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.txt1, _item.txt2, _item.txt3, _id1, _id2, _id3, _id4, _id5;
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
		_query := 'SELECT HF.id, YR.id, YR.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, HF.position FROM "HALL_OF_FAME" HF';
		_query := _query || ' LEFT JOIN "YEAR" YR ON HF.id_year = YR.id';
		_query := _query || ' LEFT JOIN "PERSON" PR ON HF.id_person = PR.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON HF.id_league = LG.id';
		IF _entity = 'YR' THEN
			_query := _query || ' WHERE HF.id_year = ' || _id;
		ELSIF _entity = 'PR' THEN
			_query := _query || ' WHERE HF.id_person IN (' || _pr_list || ')';
		END IF;
		_query := _query || ' ORDER BY YR.id DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.txt1;
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
		_query := 'SELECT RN.id, TM.id, TM.label, PR.id, PR.last_name, PR.first_name, LG.id, LG.label, RN.number FROM "RETIRED_NUMBER" RN';
		_query := _query || ' LEFT JOIN "TEAM" TM ON RN.id_team = TM.id';
		_query := _query || ' LEFT JOIN "PERSON" PR ON RN.id_person = PR.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON RN.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE RN.id_team = ' || _id;
		ELSIF _entity = 'PR' THEN
			_query := _query || ' WHERE RN.id_person IN (' || _pr_list || ')';
		END IF;
		_query := _query || ' ORDER BY TM.label, RN.number LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.label_rel3, _item.id_rel3, _item.comment, _item.id_rel4;
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
		_query := 'SELECT TS.id, TM.id, TM.label, CX.id, CX.label' || _lang || ', CT.id, CT.label' || _lang || ', ST.id, ST.label' || _lang || ', CN.id, CN.label' || _lang || ', CX.label, CT.label, ST.label, CN.label, LG.id, LG.label, TS.date1, TS.date2 FROM "TEAM_STADIUM" TS';
		_query := _query || ' LEFT JOIN "TEAM" TM ON TS.id_team = TM.id';
		_query := _query || ' LEFT JOIN "COMPLEX" CX ON TS.id_complex = CX.id';
		_query := _query || ' LEFT JOIN "CITY" CT ON CX.id_city = CT.id';
		_query := _query || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
		_query := _query || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON TS.id_league = LG.id';
		IF _entity = 'TM' THEN
			_query := _query || ' WHERE TS.id_team = ' || _id;
		ELSIF _entity = 'CX' THEN
			_query := _query || ' WHERE TS.id_complex IN (' || _cx_list || ')';
		END IF;
		_query := _query || ' ORDER BY TM.label, TS.date1 DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.id_rel6, _item.comment, _item.txt1, _item.txt2;
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
		_query := 'SELECT WL.id, TM.id, TM.label, LG.id, LG.label, WL.type, WL.count_win || '','' || WL.count_loss FROM "WIN_LOSS" WL';
		_query := _query || ' LEFT JOIN "TEAM" TM ON WL.id_team = TM.id';
		_query := _query || ' LEFT JOIN "LEAGUE" LG ON WL.id_league = LG.id';
		_query := _query || ' WHERE WL.id_team = ' || _id;
		_query := _query || ' ORDER BY TM.label DESC LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.comment, _item.txt1, _item.txt2;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'WL';
			RETURN NEXT _item;
			_index := _index + 1;
		END LOOP;
		CLOSE _c;
	END IF;

	-- References in: [Contributions]
	IF (_entity ~ 'CB' AND (_entity_ref = 'CO' OR _entity_ref = '')) THEN
		_query := 'SELECT RS.id, YR.id, YR.label, SP.id, SP.label' || _lang || ', CP.id, CP.label' || _lang || ', EV.id, EV.label' || _lang || ', SE.id, SE.label' || _lang || ', SE2.id, SE2.label' || _lang || ', SP.label, CP.label, EV.label, SE.label, SE2.label, CO.type, CO.date';
		_query := _query || ' FROM "~CONTRIBUTION" CO';
		_query := _query || ' LEFT JOIN "RESULT" RS ON CO.id_item = RS.id';
		_query := _query || ' LEFT JOIN "YEAR" YR ON RS.id_year = YR.id';
		_query := _query || ' LEFT JOIN "SPORT" SP ON RS.id_sport = SP.id';
		_query := _query || ' LEFT JOIN "CHAMPIONSHIP" CP ON RS.id_championship = CP.id';
		_query := _query || ' LEFT JOIN "EVENT" EV ON RS.id_event = EV.id';
		_query := _query || ' LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id';
		_query := _query || ' LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id';
		_query := _query || ' WHERE RS.id_member=' || _id || ' ORDER BY CO.date DESC, YR.id DESC, SP.label' || _lang || ' LIMIT ' || _limit || ' OFFSET ' || _offset;
		OPEN _c FOR EXECUTE _query;
		LOOP
			FETCH _c INTO _item.id_item, _item.id_rel1, _item.label_rel1, _item.id_rel2, _item.label_rel2, _item.id_rel3, _item.label_rel3, _item.id_rel4, _item.label_rel4, _item.id_rel5, _item.label_rel5, _item.id_rel18, _item.label_rel18, _item.label_rel6, _item.label_rel7, _item.label_rel8, _item.label_rel9, _item.label_rel10, _item.txt1, _item.date1;
			EXIT WHEN NOT FOUND;
			_item.id = _index;
			_item.entity = 'CO';
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
  
  
  
  CREATE OR REPLACE FUNCTION "GET_MEDAL_COUNT"(_entity character varying, _id_sport integer, _idlist character varying)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_ids integer[];
	_type1 integer;
	_type2 integer;
	_sport_txt varchar(3);
	_index smallint;
	_nfl_cp1 smallint;
	_nfl_cp2 smallint;
	_nba_cp smallint;
	_nhl_cp smallint;
	_mlb_cp smallint;
	_count1 integer;
	_count2 integer;
	_count3 integer;
	_label_tennis_slam1 varchar(45);
	_label_tennis_slam2 varchar(45);
	_label_tennis_slam3 varchar(45);
	_label_tennis_slam4 varchar(45);
	_label_golf_slam1 varchar(45);
	_label_golf_slam2 varchar(45);
	_label_golf_slam3 varchar(45);
	_label_golf_slam4 varchar(45);
	_label_cycling_tour1 varchar(45);
	_label_cycling_tour2 varchar(45);
	_label_cycling_tour3 varchar(45);
begin
	_ids := regexp_split_to_array(_idlist, E'\\-');	
	_label_tennis_slam1 := 'Australian Open';
	_label_tennis_slam2 := 'French Open';
	_label_tennis_slam3 := 'Wimbledon';
	_label_tennis_slam4 := 'US Open';
	_label_golf_slam1 := 'Masters';
	_label_golf_slam2 := 'US Open';
	_label_golf_slam3 := 'Open Championship';
	_label_golf_slam4 := 'PGA Championship';
	_label_cycling_tour1 := 'Giro d''Italia';
	_label_cycling_tour2 := 'Tour de France';
	_label_cycling_tour3 := 'Vuelta a España';
	_nfl_cp1 := 454;
	_nfl_cp2 := 453;
	_nba_cp := 530;
	_nhl_cp := 573;
	_mlb_cp := 624;
	_index := 1;
	_sport_txt := CAST(_id_sport AS varchar);
	IF (_entity = 'PR') THEN
		_type1 := 1;
		_type2 := 10;
	ELSIF (_entity = 'TM') THEN
		_type1 := 50;
		_type2 := 50;
	ELSIF (_entity = 'CN') THEN
		_type1 := 99;
		_type2 := 99;
	END IF;
	-- Olympics
	IF _sport_txt !~ '5|18|20|23|29|33|36|37|45' AND _type2 <> 50 THEN
		_item.id := _index;
		_item.label := 'OLYMP';
		_item.txt1 := '#GOLD#';
		_item.txt2 := '#SILVER#';
		_item.txt3 := '#BRONZE#';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND (id_rank1 = ANY(_ids) OR (id_rank2 = ANY(_ids) AND (RS.comment IN ('#DOUBLE#', '#TRIPLE#') OR exa ~* '.*1-(2|3|4|5|6).*')) OR (id_rank3 = ANY(_ids) AND (RS.comment = '#TRIPLE#' OR exa ~* '.*1-(3|4|5|6).*')));
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND ((id_rank2 = ANY(_ids) AND RS.comment NOT IN ('#DOUBLE#', '#TRIPLE#')) OR (id_rank3 = ANY(_ids) AND (RS.comment <> '#TRIPLE#' AND exa ~* '.*2-(3|4|5|6).*')) OR (id_rank4 = ANY(_ids) AND (RS.comment IN ('#DOUBLE#', '#TRIPLE#') OR exa ~* '.*2-(4|5|6).*')));
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND ((id_rank3 = ANY(_ids) AND RS.comment NOT IN ('#DOUBLE#', '#TRIPLE#')) OR (id_rank4 = ANY(_ids) AND (RS.comment NOT IN ('#DOUBLE#', '#TRIPLE#') AND exa ~* '.*3-(4|5|6).*')) OR (id_rank5 = ANY(_ids) AND (RS.comment <> '#TRIPLE#' AND exa ~* '.*3-(5|6).*')));
		IF _type1 = 99 THEN
			SELECT COUNT(*) INTO _count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id LEFT JOIN "PERSON" PR1 ON RS.id_rank1=PR1.id LEFT JOIN "PERSON" PR2 ON RS.id_rank2=PR2.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3=PR3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=10) OR (id_subevent IS NOT NULL AND T2.number<=10) OR (id_subevent IS NULL AND T1.number<=10))  AND (PR1.id_country = ANY(_ids) OR (PR2.id_country = ANY(_ids) AND (exa ~* '.*1-(2|3|4|5|6).*')) OR (PR3.id_country = ANY(_ids) AND (exa ~* '.*1-(3|4|5|6).*')));
			SELECT COUNT(*) INTO _count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id LEFT JOIN "PERSON" PR2 ON RS.id_rank2=PR2.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3=PR3.id LEFT JOIN "PERSON" PR4 ON RS.id_rank4=PR4.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=10) OR (id_subevent IS NOT NULL AND T2.number<=10) OR (id_subevent IS NULL AND T1.number<=10))  AND (PR2.id_country = ANY(_ids) OR (PR3.id_country = ANY(_ids) AND (exa ~* '.*2-(3|4|5|6).*')) OR (PR4.id_country = ANY(_ids) AND (exa ~* '.*2-(4|5|6).*')));
			SELECT COUNT(*) INTO _count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id LEFT JOIN "PERSON" PR3 ON RS.id_rank3=PR3.id LEFT JOIN "PERSON" PR4 ON RS.id_rank4=PR4.id LEFT JOIN "PERSON" PR5 ON RS.id_rank5=PR5.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=10) OR (id_subevent IS NOT NULL AND T2.number<=10) OR (id_subevent IS NULL AND T1.number<=10))  AND (PR3.id_country = ANY(_ids) OR (PR4.id_country = ANY(_ids) AND (exa ~* '.*3-(4|5|6).*')) OR (PR5.id_country = ANY(_ids) AND (exa ~* '.*3-(5|6).*')));
			_item.count1 := _item.count1 + _count1;
			_item.count2 := _item.count2 + _count2;
			_item.count3 := _item.count3 + _count3;
		ELSE
			SELECT COUNT(*) INTO _count1 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE id_championship=1 AND PL.rank=1 AND id_person = ANY(_ids);
			SELECT COUNT(*) INTO _count2 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE id_championship=1 AND PL.rank=2 AND id_person = ANY(_ids);
			SELECT COUNT(*) INTO _count3 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE id_championship=1 AND PL.rank=3 AND id_person = ANY(_ids);
			_item.count1 := _item.count1 + _count1;
			_item.count2 := _item.count2 + _count2;
			_item.count3 := _item.count3 + _count3;
		END IF;
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- World Championships
	IF _sport_txt !~ '20|22|23|26|29' AND _type2 <= 10 THEN
		_item.id := _index;	
		_item.label := 'WORLDCP';
		IF _sport_txt ~ '5|18|33|36' THEN
			_item.txt1 := 'rank.1';
			_item.txt2 := 'rank.2';
			_item.txt3 := 'rank.3';
		ELSE
			_item.txt1 := '#GOLD#';
			_item.txt2 := '#SILVER#';
			_item.txt3 := '#BRONZE#';
		END IF;
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND (id_rank1 = ANY(_ids) OR (id_rank2 = ANY(_ids) AND (exa ~* '.*1-(2|3|4|5|6).*')) OR (id_rank3 = ANY(_ids) AND (exa ~* '.*1-(3|4|5|6).*')));
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND (id_rank2 = ANY(_ids) OR (id_rank3 = ANY(_ids) AND (exa ~* '.*2-(3|4|5|6).*')) OR (id_rank4 = ANY(_ids) AND (exa ~* '.*2-(4|5|6).*')));
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND ((id_subevent2 IS NOT NULL AND T3.number BETWEEN _type1 AND _type2) OR (id_subevent IS NOT NULL AND T2.number BETWEEN _type1 AND _type2) OR (id_subevent IS NULL AND T1.number BETWEEN _type1 AND _type2)) AND (id_rank3 = ANY(_ids) OR (id_rank4 = ANY(_ids) AND (exa ~* '.*3-(4|5|6).*')) OR (id_rank5 = ANY(_ids) AND (exa ~* '.*3-(5|6).*')));
		SELECT COUNT(*) INTO _count1 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND PL.rank=1 AND id_person = ANY(_ids);
		SELECT COUNT(*) INTO _count2 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND PL.rank=2 AND id_person = ANY(_ids);
		SELECT COUNT(*) INTO _count3 FROM "~PERSON_LIST" PL LEFT JOIN "RESULT" RS ON PL.id_result=RS.id WHERE (id_championship IN (3, 70, 71, 72) OR (id_championship IN (9, 28, 30, 45) AND id_event IN (193, 194, 692, 728))) AND PL.rank=3 AND id_person = ANY(_ids);
		_item.count1 := _item.count1 + _count1;
		_item.count2 := _item.count2 + _count2;
		_item.count3 := _item.count3 + _count3;
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Grand Slam (Tennis)
	IF _id_sport = 22 AND _type2 <= 10 THEN
		_item.id := _index;
		_item.label := 'GSLAM';
		_item.txt1 := 'Aus';
		_item.txt2 := 'RG';
		_item.txt3 := 'Wim';
		_item.txt4 := 'US';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam1 OR SE2.label=_label_tennis_slam1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam2 OR SE2.label=_label_tennis_slam2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam3 OR SE2.label=_label_tennis_slam3) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count4 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=22 AND (SE.label=_label_tennis_slam4 OR SE2.label=_label_tennis_slam4) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Majors (Golf)
	IF _id_sport = 20 AND _type2 <= 10 THEN
		_item.id := _index;
		_item.label := 'MAJORS';
		_item.txt1 := 'Mas';
		_item.txt2 := 'US';
		_item.txt3 := 'Brit';
		_item.txt4 := 'PGA';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam1 OR SE2.label=_label_golf_slam1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam2 OR SE2.label=_label_golf_slam2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam3 OR SE2.label=_label_golf_slam3) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count4 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=20 AND (SE.label=_label_golf_slam4 OR SE2.label=_label_golf_slam4) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Major Tours (Cycling)
	IF _id_sport = 19 AND _type2 <= 10 THEN
		_item.id := _index;
		_item.label := 'MTOURS';
		_item.txt1 := 'Gir';
		_item.txt2 := 'Tour';
		_item.txt3 := 'Vuel';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (EV.label=_label_cycling_tour1 OR SE.label=_label_cycling_tour1 OR SE2.label=_label_cycling_tour1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (EV.label=_label_cycling_tour2 OR SE.label=_label_cycling_tour2 OR SE2.label=_label_cycling_tour2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_sport=19 AND (EV.label=_label_cycling_tour3 OR SE.label=_label_cycling_tour3 OR SE2.label=_label_cycling_tour3) AND id_rank1 = ANY(_ids);
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- NFL Titles
	IF _id_sport = 23 AND _type2 = 50 THEN
		-- (Super Bowl)
		_item.id := _index;
		_item.label := 'NFLCP1';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp1 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp1) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp1 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp1) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
		-- (NFL Championships)
		_item.id := _index;
		_item.label := 'NFLCP2';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp2 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp2) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp2 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp2) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	-- NBA Titles
	IF _id_sport = 24 AND _type2 = 50 THEN
		_item.id := _index;
		_item.label := 'NBACP';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nba_cp AND id_subevent2 IS NULL) OR id_subevent2=_nba_cp) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nba_cp AND id_subevent2 IS NULL) OR id_subevent2=_nba_cp) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	-- NHL Titles
	IF _id_sport = 25 AND _type2 = 50 THEN
		_item.id := _index;
		_item.label := 'NHLCP';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nhl_cp AND id_subevent2 IS NULL) OR id_subevent2=_nhl_cp) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nhl_cp AND id_subevent2 IS NULL) OR id_subevent2=_nhl_cp) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	-- MLB Titles
	IF _id_sport = 26 AND _type2 = 50 THEN
		_item.id := _index;
		_item.label := 'MLBCP';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_mlb_cp AND id_subevent2 IS NULL) OR id_subevent2=_mlb_cp) AND id_rank1 = ANY(_ids);
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_mlb_cp AND id_subevent2 IS NULL) OR id_subevent2=_mlb_cp) AND id_rank2 = ANY(_ids);
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
	END IF;
	RETURN;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100
  ROWS 1000;
  
  
  


  
  
  