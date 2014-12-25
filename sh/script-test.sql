ALTER TABLE "CHAMPIONSHIP" ADD REF SMALLINT;
ALTER TABLE "CITY" ADD REF SMALLINT;
ALTER TABLE "COMPLEX" ADD REF SMALLINT;
ALTER TABLE "COUNTRY" ADD REF SMALLINT;
ALTER TABLE "EVENT" ADD REF SMALLINT;
ALTER TABLE "OLYMPICS" ADD REF SMALLINT;
ALTER TABLE "PERSON" ADD REF SMALLINT;
ALTER TABLE "SPORT" ADD REF SMALLINT;
ALTER TABLE "STATE" ADD REF SMALLINT;
ALTER TABLE "TEAM" ADD REF SMALLINT;
ALTER TABLE "YEAR" ADD REF SMALLINT;

CREATE OR REPLACE FUNCTION "UPDATE_REF"()
  RETURNS trigger AS
$BODY$
DECLARE
	_row record;
	_entity varchar(2);
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
	END IF;
	
        RETURN NULL;
    END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION "UPDATE_REF"() OWNER TO postgres;

CREATE TRIGGER TRIGGER_CT AFTER INSERT OR UPDATE OR DELETE ON "CITY" FOR EACH ROW EXECUTE PROCEDURE "UPDATE_REF"('CT');
CREATE TRIGGER TRIGGER_CX AFTER INSERT OR UPDATE OR DELETE ON "COMPLEX" FOR EACH ROW EXECUTE PROCEDURE "UPDATE_REF"('CX');
CREATE TRIGGER TRIGGER_DR AFTER INSERT OR UPDATE OR DELETE ON "DRAW" FOR EACH ROW EXECUTE PROCEDURE "UPDATE_REF"('DR');
CREATE TRIGGER TRIGGER_HF AFTER INSERT OR UPDATE OR DELETE ON "HALL_OF_FAME" FOR EACH ROW EXECUTE PROCEDURE "UPDATE_REF"('HF');
CREATE TRIGGER TRIGGER_OL AFTER INSERT OR UPDATE OR DELETE ON "OLYMPICS" FOR EACH ROW EXECUTE PROCEDURE "UPDATE_REF"('OL');
CREATE TRIGGER TRIGGER_OR AFTER INSERT OR UPDATE OR DELETE ON "OLYMPIC_RANKING" FOR EACH ROW EXECUTE PROCEDURE "UPDATE_REF"('OR');
CREATE TRIGGER TRIGGER_PR AFTER INSERT OR UPDATE OR DELETE ON "PERSON" FOR EACH ROW EXECUTE PROCEDURE "UPDATE_REF"('PR');
CREATE TRIGGER TRIGGER_RC AFTER INSERT OR UPDATE OR DELETE ON "RECORD" FOR EACH ROW EXECUTE PROCEDURE "UPDATE_REF"('RC');
CREATE TRIGGER TRIGGER_RS AFTER INSERT OR UPDATE OR DELETE ON "RESULT" FOR EACH ROW EXECUTE PROCEDURE "UPDATE_REF"('RS');
CREATE TRIGGER TRIGGER_RN AFTER INSERT OR UPDATE OR DELETE ON "RETIRED_NUMBER" FOR EACH ROW EXECUTE PROCEDURE "UPDATE_REF"('RN');
CREATE TRIGGER TRIGGER_TM AFTER INSERT OR UPDATE OR DELETE ON "TEAM" FOR EACH ROW EXECUTE PROCEDURE "UPDATE_REF"('TM');
CREATE TRIGGER TRIGGER_TS AFTER INSERT OR UPDATE OR DELETE ON "TEAM_STADIUM" FOR EACH ROW EXECUTE PROCEDURE "UPDATE_REF"('TS');
CREATE TRIGGER TRIGGER_WL AFTER INSERT OR UPDATE OR DELETE ON "WIN_LOSS" FOR EACH ROW EXECUTE PROCEDURE "UPDATE_REF"('WL');

CREATE OR REPLACE FUNCTION "UPDATE_REF"()
  RETURNS trigger AS
$BODY$
DECLARE
	_row record;
	_entity varchar(2);
	_id_event integer;
	_id_subevent integer;
	_id_subevent2 integer;
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
ALTER FUNCTION "UPDATE_REF"() OWNER TO postgres;


UPDATE "CITY" SET ID=ID;
UPDATE "COMPLEX" SET ID=ID;
UPDATE "DRAW" SET ID=ID;
UPDATE "HALL_OF_FAME" SET ID=ID;
UPDATE "OLYMPICS" SET ID=ID;
UPDATE "OLYMPIC_RANKING" SET ID=ID;
UPDATE "PERSON" SET ID=ID;
UPDATE "RECORD" SET ID=ID;
UPDATE "RETIRED_NUMBER" SET ID=ID;
UPDATE "TEAM" SET ID=ID;
UPDATE "TEAM_STADIUM" SET ID=ID;
UPDATE "WIN_LOSS" SET ID=ID;
UPDATE "RESULT" SET ID=ID;


CREATE OR REPLACE FUNCTION "SEARCH"(_pattern character varying, _scope character varying, _lang character varying)
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
	_current_ref smallint;
	_scopes varchar(2)[];
	_tables varchar(15)[];
	_label varchar(10);
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
		IF _scope ~ ('(^|,)' || _s || '($|,)') THEN
			_rel_cols := '';
			_rel_joins := '';
			_rel_count := 0;

			-- Get related fields
			IF (_s ~ 'PR|TM') THEN -- Relation: Country
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang || ' || '' ('' || CN.code || '')''';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s ~ 'PR|TM') THEN -- Relation: Sport
				_rel_cols := _rel_cols || ', SP.id, SP.label' || _lang;
				_rel_joins := _rel_joins || ' LEFT JOIN "SPORT" SP ON ' || _s || '.id_sport = SP.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'PR') THEN -- Relation: Team
				_rel_cols := _rel_cols || ', TM.id, TM.label, PR.link';
				_rel_joins := _rel_joins || ' LEFT JOIN "TEAM" TM ON ' || _s || '.id_team = TM.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'CX') THEN -- Relation: City/State/Country
				_rel_cols := _rel_cols || ', CT.id, CT.label' || _lang;
				_rel_cols := _rel_cols || ', ST.id, ST.label' || _lang;
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang;
				_rel_joins := _rel_joins || ' LEFT JOIN "CITY" CT ON ' || _s || '.id_city = CT.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			IF (_s = 'CT') THEN -- Relation: State/Country
				_rel_cols := _rel_cols || ', NULL, NULL';
				_rel_cols := _rel_cols || ', ST.id, ST.label' || _lang;
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang;
				_rel_joins := _rel_joins || ' LEFT JOIN "STATE" ST ON ' || _s || '.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			FOR _j IN 1.._rel_count LOOP
				_rel_cols := _rel_cols || ', NULL, NULL';
			END LOOP;

			-- Execute query
			_label := 'label';
			IF (_s <> 'TM' AND _s <> 'YR') THEN
				_label := 'label' || _lang;
			END IF;
			_query := 'SELECT ' || _s || '.id, ' || _s || '.' || _label || _rel_cols || ', ref FROM "' || _tables[_i] || '" ' || _s;
			_query := _query || _rel_joins || ' WHERE ' || _s || '.' || _label || ' ~* ''' || __pattern || ''' ORDER BY ' || _s || '.' || _label;
			IF _s = 'PR' THEN
				_query := 'SELECT PR.id, PR.last_name || '', '' || PR.first_name' || _rel_cols || ', PR.ref FROM "PERSON" PR' || _rel_joins;
				_query := _query || ' WHERE (PR.link = 0 OR PR.link IS NULL) AND (PR.last_name || '' '' || PR.first_name ~* ''' || __pattern || ''' OR PR.first_name || '' '' || PR.last_name ~* ''' || __pattern || ''' OR PR.last_name ~* ''' || __pattern || ''' OR PR.first_name ~* ''' || __pattern || ''')';
				_query := _query || ' ORDER BY PR.last_name, PR.first_name';
			END IF;
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _current_id, _current_label, _current_id_rel1, _current_label_rel1, _current_id_rel2, _current_label_rel2, _current_id_rel3, _current_label_rel3, _current_link, _current_ref;
				EXIT WHEN NOT FOUND;
				_item.id = _index;
				_item.id_item = _current_id;
				_item.label = _current_label;
				_item.entity = _s;
				_item.count_ref = _current_ref;
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
ALTER FUNCTION "SEARCH"(character varying, character varying, character varying) OWNER TO inachos;
DROP FUNCTION "SEARCH"(character varying, character varying, smallint, character varying);


CREATE OR REPLACE FUNCTION "SEARCH"(_pattern character varying, _scope character varying, _lang character varying)
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
	_current_ref smallint;
	_scopes varchar(2)[];
	_tables varchar(15)[];
	_label varchar(10);
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
		IF _scope ~ ('(^|,)' || _s || '($|,)') THEN
			_rel_cols := '';
			_rel_joins := '';
			_rel_count := 0;

			-- Get related fields
			IF (_s ~ 'PR|TM') THEN -- Relation: Country
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang || ' || '' ('' || CN.code || '')''';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s ~ 'PR|TM') THEN -- Relation: Sport
				_rel_cols := _rel_cols || ', SP.id, SP.label' || _lang;
				_rel_joins := _rel_joins || ' LEFT JOIN "SPORT" SP ON ' || _s || '.id_sport = SP.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'PR') THEN -- Relation: Team
				_rel_cols := _rel_cols || ', TM.id, TM.label, PR.link';
				_rel_joins := _rel_joins || ' LEFT JOIN "TEAM" TM ON ' || _s || '.id_team = TM.id';
				_rel_count := _rel_count + 1;
			END IF;
			IF (_s = 'CX') THEN -- Relation: City/State/Country
				_rel_cols := _rel_cols || ', CT.id, CT.label' || _lang;
				_rel_cols := _rel_cols || ', ST.id, ST.label' || _lang;
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang;
				_rel_joins := _rel_joins || ' LEFT JOIN "CITY" CT ON ' || _s || '.id_city = CT.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "STATE" ST ON CT.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON CT.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			IF (_s = 'CT') THEN -- Relation: State/Country
				_rel_cols := _rel_cols || ', NULL, NULL';
				_rel_cols := _rel_cols || ', ST.id, ST.label' || _lang;
				_rel_cols := _rel_cols || ', CN.id, CN.label' || _lang;
				_rel_joins := _rel_joins || ' LEFT JOIN "STATE" ST ON ' || _s || '.id_state = ST.id';
				_rel_joins := _rel_joins || ' LEFT JOIN "COUNTRY" CN ON ' || _s || '.id_country = CN.id';
				_rel_count := _rel_count + 3;
			END IF;
			FOR _j IN 1.._rel_count LOOP
				_rel_cols := _rel_cols || ', NULL, NULL';
			END LOOP;

			-- Execute query
			_label := 'label';
			IF (_s <> 'TM' AND _s <> 'YR') THEN
				_label := 'label' || _lang;
			END IF;
			_query := 'SELECT ' || _s || '.id, ' || _s || '.' || _label || ',' || _s || '.ref' || _rel_cols || ' FROM "' || _tables[_i] || '" ' || _s;
			_query := _query || _rel_joins || ' WHERE ' || _s || '.' || _label || ' ~* ''' || __pattern || ''' ORDER BY ' || _s || '.' || _label;
			IF _s = 'PR' THEN
				_query := 'SELECT PR.id, PR.last_name || '', '' || PR.first_name, PR.ref' || _rel_cols || ' FROM "PERSON" PR' || _rel_joins;
				_query := _query || ' WHERE (PR.link = 0 OR PR.link IS NULL) AND (PR.last_name || '' '' || PR.first_name ~* ''' || __pattern || ''' OR PR.first_name || '' '' || PR.last_name ~* ''' || __pattern || ''' OR PR.last_name ~* ''' || __pattern || ''' OR PR.first_name ~* ''' || __pattern || ''')';
				_query := _query || ' ORDER BY PR.last_name, PR.first_name';
			END IF;
			OPEN _c FOR EXECUTE _query;
			LOOP
				FETCH _c INTO _current_id, _current_label, _current_ref, _current_id_rel1, _current_label_rel1, _current_id_rel2, _current_label_rel2, _current_id_rel3, _current_label_rel3, _current_link;
				EXIT WHEN NOT FOUND;
				_item.id = _index;
				_item.id_item = _current_id;
				_item.label = _current_label;
				_item.entity = _s;
				_item.count_ref = _current_ref;
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
ALTER FUNCTION "SEARCH"(character varying, character varying, character varying) OWNER TO inachos;


