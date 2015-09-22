-- Function: "UpdateRef"()

-- DROP FUNCTION "UpdateRef"();

CREATE OR REPLACE FUNCTION "UpdateRef"()
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
		UPDATE "State" SET REF="CountRef"('ST', _row.id_state) WHERE id=_row.id_state;
		UPDATE "Country" SET REF="CountRef"('CN', _row.id_country) WHERE id=_row.id_country;
	ELSIF _entity = 'CX' THEN
		UPDATE "City" SET REF="CountRef"('CT', _row.id_city) WHERE id=_row.id_city;
	ELSIF _entity = 'DR' THEN
		SELECT RS.id_event, RS.id_subevent, RS.id_subevent2 INTO _id_event, _id_subevent, _id_subevent2 FROM "Result" RS WHERE RS.id = _row.id_result;

		IF _id_subevent2 IS NOT NULL AND _id_subevent2 <> 0 THEN
			SELECT TP.number INTO _type FROM "Event" EV LEFT JOIN "Type" TP ON EV.id_type = TP.id WHERE EV.id = _id_subevent2;
		ELSIF _id_subevent IS NOT NULL AND _id_subevent <> 0 THEN
			SELECT TP.number INTO _type FROM "Event" EV LEFT JOIN "Type" TP ON EV.id_type = TP.id WHERE EV.id = _id_subevent;
		ELSE
			SELECT TP.number INTO _type FROM "Event" EV LEFT JOIN "Type" TP ON EV.id_type = TP.id WHERE EV.id = _id_event;
		END IF;

		IF _type < 10 THEN
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id1_qf1) WHERE id=_row.id1_qf1;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id2_qf1) WHERE id=_row.id2_qf1;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id1_qf2) WHERE id=_row.id1_qf2;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id2_qf2) WHERE id=_row.id2_qf2;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id1_qf3) WHERE id=_row.id1_qf3;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id2_qf3) WHERE id=_row.id2_qf3;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id1_qf4) WHERE id=_row.id1_qf4;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id2_qf4) WHERE id=_row.id2_qf4;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id1_sf1) WHERE id=_row.id1_sf1;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id2_sf1) WHERE id=_row.id2_sf1;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id1_sf2) WHERE id=_row.id1_sf2;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id2_sf2) WHERE id=_row.id2_sf2;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id1_thd) WHERE id=_row.id1_thd;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id2_thd) WHERE id=_row.id2_thd;
		ELSIF _type = 50 THEN
			UPDATE "Team" SET REF="CountRef"('TM', _row.id1_qf1) WHERE id=_row.id1_qf1;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id2_qf1) WHERE id=_row.id2_qf1;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id1_qf2) WHERE id=_row.id1_qf2;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id2_qf2) WHERE id=_row.id2_qf2;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id1_qf3) WHERE id=_row.id1_qf3;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id2_qf3) WHERE id=_row.id2_qf3;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id1_qf4) WHERE id=_row.id1_qf4;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id2_qf4) WHERE id=_row.id2_qf4;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id1_sf1) WHERE id=_row.id1_sf1;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id2_sf1) WHERE id=_row.id2_sf1;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id1_sf2) WHERE id=_row.id1_sf2;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id2_sf2) WHERE id=_row.id2_sf2;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id1_thd) WHERE id=_row.id1_thd;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id2_thd) WHERE id=_row.id2_thd;
		ELSIF _type = 99 THEN
			UPDATE "Country" SET REF="CountRef"('CN', _row.id1_qf1) WHERE id=_row.id1_qf1;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id2_qf1) WHERE id=_row.id2_qf1;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id1_qf2) WHERE id=_row.id1_qf2;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id2_qf2) WHERE id=_row.id2_qf2;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id1_qf3) WHERE id=_row.id1_qf3;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id2_qf3) WHERE id=_row.id2_qf3;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id1_qf4) WHERE id=_row.id1_qf4;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id2_qf4) WHERE id=_row.id2_qf4;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id1_sf1) WHERE id=_row.id1_sf1;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id2_sf1) WHERE id=_row.id2_sf1;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id1_sf2) WHERE id=_row.id1_sf2;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id2_sf2) WHERE id=_row.id2_sf2;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id1_thd) WHERE id=_row.id1_thd;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id2_thd) WHERE id=_row.id2_thd;
		END IF;
	ELSIF _entity = 'HF' THEN
		UPDATE "Year" SET REF="CountRef"('YR', _row.id_year) WHERE id=_row.id_year;
		UPDATE "Athlete" SET REF="CountRef"('PR', _row.id_person) WHERE id=_row.id_person;
	ELSIF _entity = 'OL' THEN
		UPDATE "Year" SET REF="CountRef"('YR', _row.id_year) WHERE id=_row.id_year;
		UPDATE "City" SET REF="CountRef"('CT', _row.id_city) WHERE id=_row.id_city;
	ELSIF _entity = 'OR' THEN
		UPDATE "Olympics" SET REF="CountRef"('OL', _row.id_olympics) WHERE id=_row.id_olympics;
		UPDATE "Country" SET REF="CountRef"('CN', _row.id_country) WHERE id=_row.id_country;
	ELSIF _entity = 'PL' THEN
		UPDATE "Athlete" SET REF="CountRef"('PR', _row.id_person) WHERE id=_row.id_person;
	ELSIF _entity = 'PR' THEN
		UPDATE "Country" SET REF="CountRef"('CN', _row.id_country) WHERE id=_row.id_country;
		UPDATE "Team" SET REF="CountRef"('TM', _row.id_team) WHERE id=_row.id_team;
		UPDATE "Sport" SET REF="CountRef"('SP', _row.id_sport) WHERE id=_row.id_sport;
	ELSIF _entity = 'RC' THEN
		UPDATE "Sport" SET REF="CountRef"('SP', _row.id_sport) WHERE id=_row.id_sport;
		UPDATE "Championship" SET REF="CountRef"('CP', _row.id_championship) WHERE id=_row.id_championship;
		UPDATE "Event" SET REF="CountRef"('EV', _row.id_event) WHERE id=_row.id_event;
		UPDATE "Event" SET REF="CountRef"('EV', _row.id_subevent) WHERE id=_row.id_subevent;
		UPDATE "City" SET REF="CountRef"('CT', _row.id_city) WHERE id=_row.id_city;

		IF _row.id_subevent IS NOT NULL AND _row.id_subevent <> 0 THEN
			SELECT TP.number INTO _type FROM "Event" EV LEFT JOIN "Type" TP ON EV.id_type = TP.id WHERE EV.id = _row.id_subevent;
		ELSE
			SELECT TP.number INTO _type FROM "Event" EV LEFT JOIN "Type" TP ON EV.id_type = TP.id WHERE EV.id = _row.id_event;
		END IF;

		IF _type < 10 THEN
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id_rank5) WHERE id=_row.id_rank5;
		ELSIF _type = 50 THEN
			UPDATE "Team" SET REF="CountRef"('TM', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id_rank5) WHERE id=_row.id_rank5;
		ELSIF _type = 99 THEN
			UPDATE "Country" SET REF="CountRef"('CN', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id_rank5) WHERE id=_row.id_rank5;
		END IF;
	ELSIF _entity = 'RS' THEN
		UPDATE "Year" SET REF="CountRef"('YR', _row.id_year) WHERE id=_row.id_year;
		UPDATE "Sport" SET REF="CountRef"('SP', _row.id_sport) WHERE id=_row.id_sport;
		UPDATE "Championship" SET REF="CountRef"('CP', _row.id_championship) WHERE id=_row.id_championship;
		UPDATE "Event" SET REF="CountRef"('EV', _row.id_event) WHERE id=_row.id_event;
		UPDATE "Event" SET REF="CountRef"('EV', _row.id_subevent) WHERE id=_row.id_subevent;
		UPDATE "Event" SET REF="CountRef"('EV', _row.id_subevent2) WHERE id=_row.id_subevent2;
		UPDATE "Complex" SET REF="CountRef"('CX', _row.id_complex1) WHERE id=_row.id_complex1;
		UPDATE "Complex" SET REF="CountRef"('CX', _row.id_complex2) WHERE id=_row.id_complex2;
		UPDATE "City" SET REF="CountRef"('CT', _row.id_city1) WHERE id=_row.id_city1;
		UPDATE "City" SET REF="CountRef"('CT', _row.id_city2) WHERE id=_row.id_city2;
		IF _row.id_championship = 1 THEN
			SELECT SP.type INTO _sp_type FROM "Sport" SP WHERE SP.id=_row.id_sport;
			SELECT OL.id INTO _id_olympics FROM "Olympics" OL WHERE OL.id_year=_row.id_year AND OL.type=_sp_type;
			UPDATE "Olympics" SET REF="CountRef"('OL', _id_olympics) WHERE id=_id_olympics;
		END IF;
		IF _row.id_subevent2 IS NOT NULL AND _row.id_subevent2 <> 0 THEN
			SELECT TP.number INTO _type FROM "Event" EV LEFT JOIN "Type" TP ON EV.id_type = TP.id WHERE EV.id = _row.id_subevent2;
		ELSIF _row.id_subevent IS NOT NULL AND _row.id_subevent <> 0 THEN
			SELECT TP.number INTO _type FROM "Event" EV LEFT JOIN "Type" TP ON EV.id_type = TP.id WHERE EV.id = _row.id_subevent;
		ELSE
			SELECT TP.number INTO _type FROM "Event" EV LEFT JOIN "Type" TP ON EV.id_type = TP.id WHERE EV.id = _row.id_event;
		END IF;

		IF _type < 10 THEN
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id_rank5) WHERE id=_row.id_rank5;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id_rank6) WHERE id=_row.id_rank6;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id_rank7) WHERE id=_row.id_rank7;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id_rank8) WHERE id=_row.id_rank8;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id_rank9) WHERE id=_row.id_rank9;
			UPDATE "Athlete" SET REF="CountRef"('PR', _row.id_rank10) WHERE id=_row.id_rank10;
		ELSIF _type = 50 THEN
			UPDATE "Team" SET REF="CountRef"('TM', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id_rank5) WHERE id=_row.id_rank5;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id_rank6) WHERE id=_row.id_rank6;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id_rank7) WHERE id=_row.id_rank7;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id_rank8) WHERE id=_row.id_rank8;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id_rank9) WHERE id=_row.id_rank9;
			UPDATE "Team" SET REF="CountRef"('TM', _row.id_rank10) WHERE id=_row.id_rank10;
		ELSIF _type = 99 THEN
			UPDATE "Country" SET REF="CountRef"('CN', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id_rank5) WHERE id=_row.id_rank5;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id_rank6) WHERE id=_row.id_rank6;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id_rank7) WHERE id=_row.id_rank7;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id_rank8) WHERE id=_row.id_rank8;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id_rank9) WHERE id=_row.id_rank9;
			UPDATE "Country" SET REF="CountRef"('CN', _row.id_rank10) WHERE id=_row.id_rank10;
		END IF;
	ELSIF _entity = 'RN' THEN
		UPDATE "Team" SET REF="CountRef"('TM', _row.id_team) WHERE id=_row.id_team;
		UPDATE "Athlete" SET REF="CountRef"('PR', _row.id_person) WHERE id=_row.id_person;
	ELSIF _entity = 'TM' THEN
		UPDATE "Team" SET REF="CountRef"('TM', _row.link) WHERE id=_row.link;
	ELSIF _entity = 'TS' THEN
		UPDATE "Team" SET REF="CountRef"('TM', _row.id_team) WHERE id=_row.id_team;
		UPDATE "Complex" SET REF="CountRef"('CX', _row.id_complex) WHERE id=_row.id_complex;
	ELSIF _entity = 'WL' THEN
		UPDATE "Team" SET REF="CountRef"('TM', _row.id_team) WHERE id=_row.id_team;
	END IF;
	
        RETURN NULL;
    END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;