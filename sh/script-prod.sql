ALTER TABLE "EVENT" DROP COMMENT;
ALTER TABLE "CHAMPIONSHIP" DROP COMMENT;

CREATE OR REPLACE FUNCTION "GET_MEDAL_COUNT"(_entity character varying, _id_sport integer, _id integer)
  RETURNS SETOF "~REF_ITEM" AS
$BODY$
declare
	_item "~REF_ITEM"%rowtype;
	_type integer;
	_sport_txt varchar(3);
	_index smallint;
	_nfl_cp1 smallint;
	_nfl_cp2 smallint;
	_nba_cp smallint;
	_nhl_cp smallint;
	_mlb_cp smallint;
begin
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
	IF _sport_txt !~ '20' AND _type <> 50 THEN
		_item.id := _index;
		_item.label := 'OLYMP';
		_item.txt1 := '#GOLD#';
		_item.txt2 := '#SILVER#';
		_item.txt3 := '#BRONZE#';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank1=_id OR (id_rank2=_id AND exa ~* '.*1-(2|3|4|5|6).*') OR (id_rank3=_id AND exa ~* '.*1-(3|4|5|6).*'));
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank2=_id OR (id_rank3=_id AND exa ~* '.*2-(3|4|5|6).*') OR (id_rank4=_id AND exa ~* '.*2-(4|5|6).*'));
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=1 AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank3=_id OR (id_rank4=_id AND exa ~* '.*3-(4|5|6).*') OR (id_rank5=_id AND exa ~* '.*3-(5|6).*'));
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- World Championships
	IF _sport_txt !~ '20|22' AND _type <> 50 THEN
		_item.id := _index;	
		_item.label := 'WORLDCP';
		_item.txt1 := '#GOLD#';
		_item.txt2 := '#SILVER#';
		_item.txt3 := '#BRONZE#';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=3 AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank1=_id OR (id_rank2=_id AND exa ~* '.*1-(2|3|4|5|6).*') OR (id_rank3=_id AND exa ~* '.*1-(3|4|5|6).*'));
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=3 AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank2=_id OR (id_rank3=_id AND exa ~* '.*2-(3|4|5|6).*') OR (id_rank4=_id AND exa ~* '.*2-(4|5|6).*'));
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=3 AND ((id_subevent2 IS NOT NULL AND T3.number<=_type) OR (id_subevent IS NOT NULL AND T2.number<=_type) OR (id_subevent IS NULL AND T1.number<=_type)) AND (id_rank3=_id OR (id_rank4=_id AND exa ~* '.*3-(4|5|6).*') OR (id_rank5=_id AND exa ~* '.*3-(5|6).*'));
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Grand Slam (Tennis)
	IF _id_sport = 22 AND _type <> 50 THEN
		_item.id := _index;
		_item.label := 'GSLAM';
		_item.txt1 := 'Aus';
		_item.txt2 := 'RG';
		_item.txt3 := 'Wim';
		_item.txt4 := 'US';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=35 AND SE.label='Australian Open' AND id_rank1=_id;
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=35 AND SE.label='French Open' AND id_rank1=_id;
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=35 AND SE.label='British Open' AND id_rank1=_id;
		SELECT COUNT(*) INTO _item.count4 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=35 AND SE.label='US Open' AND id_rank1=_id;
		RETURN NEXT _item;
		_index := _index + 1;
	END IF;
	-- Majors (Golf)
	IF _id_sport = 20 AND _type <> 50 THEN
		_item.id := _index;
		_item.label := 'MAJORS';
		_item.txt1 := 'Mas';
		_item.txt2 := 'US';
		_item.txt3 := 'Brit';
		_item.txt4 := 'PGA';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=69 AND SE.label='Masters' AND id_rank1=_id;
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=69 AND SE.label='US Open' AND id_rank1=_id;
		SELECT COUNT(*) INTO _item.count3 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=69 AND SE.label='British Open' AND id_rank1=_id;
		SELECT COUNT(*) INTO _item.count4 FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event=EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent=SE.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2=SE2.id LEFT JOIN "TYPE" T1 ON EV.id_type=T1.id LEFT JOIN "TYPE" T2 ON SE.id_type=T2.id LEFT JOIN "TYPE" T3 ON SE2.id_type=T3.id WHERE id_championship=69 AND SE.label='PGA Championship' AND id_rank1=_id;
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
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp1 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp1) AND id_rank1=_id;
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp1 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp1) AND id_rank2=_id;
		IF (_item.count1 > 0 OR _item.count2 > 0) THEN
			RETURN NEXT _item;
			_index := _index + 1;
		END IF;
		-- (NFL Championships)
		_item.id := _index;
		_item.label := 'NFLCP2';
		_item.txt1 := 'Wins';
		_item.txt2 := 'Finals';
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp2 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp2) AND id_rank1=_id;
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nfl_cp2 AND id_subevent2 IS NULL) OR id_subevent2=_nfl_cp2) AND id_rank2=_id;
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
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nba_cp AND id_subevent2 IS NULL) OR id_subevent2=_nba_cp) AND id_rank1=_id;
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nba_cp AND id_subevent2 IS NULL) OR id_subevent2=_nba_cp) AND id_rank2=_id;
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
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_nhl_cp AND id_subevent2 IS NULL) OR id_subevent2=_nhl_cp) AND id_rank1=_id;
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_nhl_cp AND id_subevent2 IS NULL) OR id_subevent2=_nhl_cp) AND id_rank2=_id;
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
		SELECT COUNT(*) INTO _item.count1 FROM "RESULT" RS WHERE ((id_subevent=_mlb_cp AND id_subevent2 IS NULL) OR id_subevent2=_mlb_cp) AND id_rank1=_id;
		SELECT COUNT(*) INTO _item.count2 FROM "RESULT" RS WHERE ((id_subevent=_mlb_cp AND id_subevent2 IS NULL) OR id_subevent2=_mlb_cp) AND id_rank2=_id;
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
ALTER FUNCTION "GET_MEDAL_COUNT"(character varying, integer, integer) OWNER TO postgres;


CREATE SEQUENCE "~SQ_NEWS";
ALTER TABLE "~SQ_NEWS" OWNER TO postgres;


ALTER TABLE "~NEWS" ADD id integer not null default 0;
ALTER TABLE "~NEWS" RENAME date_text to title;


UPDATE "~NEWS" SET ID=NEXTVAL('"~SQ_NEWS"') WHERE TEXT_HTML LIKE '%0.1.0%';
UPDATE "~NEWS" SET ID=NEXTVAL('"~SQ_NEWS"') WHERE TEXT_HTML LIKE '%0.2.0%';
UPDATE "~NEWS" SET ID=NEXTVAL('"~SQ_NEWS"') WHERE TEXT_HTML LIKE '%0.3.0%';
UPDATE "~NEWS" SET ID=NEXTVAL('"~SQ_NEWS"') WHERE TEXT_HTML LIKE '%0.4.0%';
UPDATE "~NEWS" SET ID=NEXTVAL('"~SQ_NEWS"') WHERE TEXT_HTML LIKE '%0.5.0%';
UPDATE "~NEWS" SET ID=NEXTVAL('"~SQ_NEWS"') WHERE TEXT_HTML LIKE '%0.5.1%';
UPDATE "~NEWS" SET ID=NEXTVAL('"~SQ_NEWS"') WHERE TEXT_HTML LIKE '%0.6.0%';
UPDATE "~NEWS" SET ID=NEXTVAL('"~SQ_NEWS"') WHERE TEXT_HTML LIKE '%0.7.0%';
UPDATE "~NEWS" SET ID=NEXTVAL('"~SQ_NEWS"') WHERE TEXT_HTML LIKE '%0.8.0%';


ALTER TABLE "~NEWS" ALTER title TYPE varchar(100);


ALTER TABLE "~NEWS" ADD title_fr varchar(100);
ALTER TABLE "~NEWS" ADD text_html_fr varchar(300);


