-- Function: "UPDATE_REF"()

-- DROP FUNCTION "UPDATE_REF"();

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
  LANGUAGE plpgsql VOLATILE
  COST 100;
