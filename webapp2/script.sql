ALTER TABLE retired_number
    ALTER COLUMN id_team DROP NOT NULL;

ALTER TABLE retired_number
    ADD COLUMN number2 character varying(10);
    
UPDATE retired_number SET number2 = "number";

UPDATE retired_number set number2 = NULL where number2 = '-1';

ALTER TABLE retired_number DROP COLUMN "number";

ALTER TABLE retired_number
    RENAME number2 TO "number";
    
DROP SEQUENCE s_win_loss;

DROP TABLE win_loss;

CREATE OR REPLACE FUNCTION update_ref()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
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
		UPDATE state SET REF = count_ref('ST', _row.id_state) WHERE id=_row.id_state;
		UPDATE country SET REF = count_ref('CN', _row.id_country) WHERE id=_row.id_country;
	ELSIF _entity = 'CX' THEN
		UPDATE city SET REF = count_ref('CT', _row.id_city) WHERE id=_row.id_city;
	ELSIF _entity = 'HF' THEN
		UPDATE year SET REF = count_ref('YR', _row.id_year) WHERE id=_row.id_year;
		UPDATE athlete SET REF = count_ref('PR', _row.id_person) WHERE id=_row.id_person;
	ELSIF _entity = 'OL' THEN
		UPDATE year SET REF = count_ref('YR', _row.id_year) WHERE id=_row.id_year;
		UPDATE city SET REF = count_ref('CT', _row.id_city) WHERE id=_row.id_city;
	ELSIF _entity = 'OR' THEN
		UPDATE olympics SET REF = count_ref('OL', _row.id_olympics) WHERE id=_row.id_olympics;
		UPDATE country SET REF = count_ref('CN', _row.id_country) WHERE id=_row.id_country;
	ELSIF _entity = 'PL' THEN
		UPDATE athlete SET REF = count_ref('PR', _row.id_person) WHERE id=_row.id_person;
	ELSIF _entity = 'PR' THEN
		UPDATE country SET REF = count_ref('CN', _row.id_country) WHERE id=_row.id_country;
		UPDATE team SET REF = count_ref('TM', _row.id_team) WHERE id=_row.id_team;
		UPDATE sport SET REF = count_ref('SP', _row.id_sport) WHERE id=_row.id_sport;
	ELSIF _entity = 'RC' THEN
		UPDATE sport SET REF = count_ref('SP', _row.id_sport) WHERE id=_row.id_sport;
		UPDATE championship SET REF = count_ref('CP', _row.id_championship) WHERE id=_row.id_championship;
		UPDATE event SET REF = count_ref('EV', _row.id_event) WHERE id=_row.id_event;
		UPDATE event SET REF = count_ref('EV', _row.id_subevent) WHERE id=_row.id_subevent;
		UPDATE city SET REF = count_ref('CT', _row.id_city) WHERE id=_row.id_city;

		IF (lower(_row.type1) = 'individual') THEN
			UPDATE athlete SET REF = count_ref('PR', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE athlete SET REF = count_ref('PR', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE athlete SET REF = count_ref('PR', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE athlete SET REF = count_ref('PR', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE athlete SET REF = count_ref('PR', _row.id_rank5) WHERE id=_row.id_rank5;
		ELSIF (lower(_row.type1) = 'team') THEN
			UPDATE team SET REF = count_ref('TM', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE team SET REF = count_ref('TM', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE team SET REF = count_ref('TM', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE team SET REF = count_ref('TM', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE team SET REF = count_ref('TM', _row.id_rank5) WHERE id=_row.id_rank5;
		ELSIF (lower(_row.type1) = 'country') THEN
			UPDATE country SET REF = count_ref('CN', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE country SET REF = count_ref('CN', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE country SET REF = count_ref('CN', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE country SET REF = count_ref('CN', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE country SET REF = count_ref('CN', _row.id_rank5) WHERE id=_row.id_rank5;
		END IF;
	ELSIF _entity = 'RS' THEN
		UPDATE year SET REF = count_ref('YR', _row.id_year) WHERE id=_row.id_year;
		UPDATE sport SET REF = count_ref('SP', _row.id_sport) WHERE id=_row.id_sport;
		UPDATE championship SET REF = count_ref('CP', _row.id_championship) WHERE id=_row.id_championship;
		UPDATE event SET REF = count_ref('EV', _row.id_event) WHERE id=_row.id_event;
		UPDATE event SET REF = count_ref('EV', _row.id_subevent) WHERE id=_row.id_subevent;
		UPDATE event SET REF = count_ref('EV', _row.id_subevent2) WHERE id=_row.id_subevent2;
		UPDATE complex SET REF = count_ref('CX', _row.id_complex1) WHERE id=_row.id_complex1;
		UPDATE complex SET REF = count_ref('CX', _row.id_complex2) WHERE id=_row.id_complex2;
		UPDATE city SET REF = count_ref('CT', _row.id_city1) WHERE id=_row.id_city1;
		UPDATE city SET REF = count_ref('CT', _row.id_city2) WHERE id=_row.id_city2;
		IF _row.id_championship = 1 THEN
			SELECT SP.type INTO _sp_type FROM sport SP WHERE SP.id=_row.id_sport;
			SELECT OL.id INTO _id_olympics FROM olympics OL WHERE OL.id_year=_row.id_year AND OL.type=_sp_type;
			UPDATE olympics SET REF = count_ref('OL', _id_olympics) WHERE id=_id_olympics;
		END IF;
		IF _row.id_subevent2 IS NOT NULL AND _row.id_subevent2 <> 0 THEN
			SELECT TP.number INTO _type FROM event EV LEFT JOIN type TP ON EV.id_type = TP.id WHERE EV.id = _row.id_subevent2;
		ELSIF _row.id_subevent IS NOT NULL AND _row.id_subevent <> 0 THEN
			SELECT TP.number INTO _type FROM event EV LEFT JOIN type TP ON EV.id_type = TP.id WHERE EV.id = _row.id_subevent;
		ELSE
			SELECT TP.number INTO _type FROM event EV LEFT JOIN type TP ON EV.id_type = TP.id WHERE EV.id = _row.id_event;
		END IF;

		IF _type < 10 THEN
			UPDATE athlete SET REF = count_ref('PR', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE athlete SET REF = count_ref('PR', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE athlete SET REF = count_ref('PR', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE athlete SET REF = count_ref('PR', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE athlete SET REF = count_ref('PR', _row.id_rank5) WHERE id=_row.id_rank5;
			UPDATE athlete SET REF = count_ref('PR', _row.id_rank6) WHERE id=_row.id_rank6;
			UPDATE athlete SET REF = count_ref('PR', _row.id_rank7) WHERE id=_row.id_rank7;
			UPDATE athlete SET REF = count_ref('PR', _row.id_rank8) WHERE id=_row.id_rank8;
			UPDATE athlete SET REF = count_ref('PR', _row.id_rank9) WHERE id=_row.id_rank9;
			UPDATE athlete SET REF = count_ref('PR', _row.id_rank10) WHERE id=_row.id_rank10;
		ELSIF _type = 50 THEN
			UPDATE team SET REF = count_ref('TM', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE team SET REF = count_ref('TM', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE team SET REF = count_ref('TM', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE team SET REF = count_ref('TM', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE team SET REF = count_ref('TM', _row.id_rank5) WHERE id=_row.id_rank5;
			UPDATE team SET REF = count_ref('TM', _row.id_rank6) WHERE id=_row.id_rank6;
			UPDATE team SET REF = count_ref('TM', _row.id_rank7) WHERE id=_row.id_rank7;
			UPDATE team SET REF = count_ref('TM', _row.id_rank8) WHERE id=_row.id_rank8;
			UPDATE team SET REF = count_ref('TM', _row.id_rank9) WHERE id=_row.id_rank9;
			UPDATE team SET REF = count_ref('TM', _row.id_rank10) WHERE id=_row.id_rank10;
		ELSIF _type = 99 THEN
			UPDATE country SET REF = count_ref('CN', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE country SET REF = count_ref('CN', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE country SET REF = count_ref('CN', _row.id_rank3) WHERE id=_row.id_rank3;
			UPDATE country SET REF = count_ref('CN', _row.id_rank4) WHERE id=_row.id_rank4;
			UPDATE country SET REF = count_ref('CN', _row.id_rank5) WHERE id=_row.id_rank5;
			UPDATE country SET REF = count_ref('CN', _row.id_rank6) WHERE id=_row.id_rank6;
			UPDATE country SET REF = count_ref('CN', _row.id_rank7) WHERE id=_row.id_rank7;
			UPDATE country SET REF = count_ref('CN', _row.id_rank8) WHERE id=_row.id_rank8;
			UPDATE country SET REF = count_ref('CN', _row.id_rank9) WHERE id=_row.id_rank9;
			UPDATE country SET REF = count_ref('CN', _row.id_rank10) WHERE id=_row.id_rank10;
		END IF;
	ELSIF _entity = 'RD' THEN
		UPDATE complex SET REF = count_ref('CX', _row.id_complex) WHERE id=_row.id_complex;
		UPDATE city SET REF = count_ref('CT', _row.id_city) WHERE id=_row.id_city;
		IF _row.id_result_type < 10 THEN
			UPDATE athlete SET REF = count_ref('PR', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE athlete SET REF = count_ref('PR', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE athlete SET REF = count_ref('PR', _row.id_rank3) WHERE id=_row.id_rank3;
		ELSIF _type = 50 THEN
			UPDATE team SET REF = count_ref('TM', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE team SET REF = count_ref('TM', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE team SET REF = count_ref('TM', _row.id_rank3) WHERE id=_row.id_rank3;
		ELSIF _type = 99 THEN
			UPDATE country SET REF = count_ref('CN', _row.id_rank1) WHERE id=_row.id_rank1;
			UPDATE country SET REF = count_ref('CN', _row.id_rank2) WHERE id=_row.id_rank2;
			UPDATE country SET REF = count_ref('CN', _row.id_rank3) WHERE id=_row.id_rank3;
		END IF;
	ELSIF _entity = 'RN' THEN
		UPDATE team SET REF = count_ref('TM', _row.id_team) WHERE id=_row.id_team;
		UPDATE athlete SET REF = count_ref('PR', _row.id_person) WHERE id=_row.id_person;
	ELSIF _entity = 'TM' THEN
		UPDATE team SET REF = count_ref('TM', _row.link) WHERE id=_row.link;
	ELSIF _entity = 'TS' THEN
		UPDATE team SET REF = count_ref('TM', _row.id_team) WHERE id=_row.id_team;
		UPDATE complex SET REF = count_ref('CX', _row.id_complex) WHERE id=_row.id_complex;
	END IF;
	
        RETURN NULL;
    END;
$BODY$;

CREATE OR REPLACE FUNCTION count_ref(
	_entity character varying,
	_id integer)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
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
		SELECT COUNT(*) INTO _n FROM city CT WHERE CT.id_country = _id; _count := _count + _n;
	ELSIF _entity = 'ST' THEN -- State
		SELECT COUNT(*) INTO _n FROM city CT WHERE CT.id_state = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Complexes
	IF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM complex CX WHERE CX.id_city = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Hall of Fame
	IF _entity = 'YR' THEN -- Year
		SELECT COUNT(*) INTO _n FROM hall_of_fame HF WHERE HF.id_year = _id; _count := _count + _n;
	ELSIF _entity = 'PR' THEN -- Person
		SELECT COUNT(*) INTO _n FROM hall_of_fame HF WHERE HF.id_person = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Olympics
	IF _entity = 'YR' THEN -- Year
		SELECT COUNT(*) INTO _n FROM olympics OL WHERE OL.id_year = _id; _count := _count + _n;
	ELSIF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM olympics OL WHERE OL.id_city = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Olympic Rankings
	IF _entity = 'OL' THEN -- Olympics
		SELECT COUNT(*) INTO _n FROM olympic_ranking OR_ WHERE OR_.id_olympics = _id; _count := _count + _n;
	ELSIF _entity = 'CN' THEN -- Country
		SELECT COUNT(*) INTO _n FROM olympic_ranking OR_ WHERE OR_.id_country = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Athletes
	IF _entity = 'CN' THEN -- Country
		SELECT COUNT(*) INTO _n FROM athlete PR WHERE PR.id_country = _id; _count := _count + _n;
	ELSIF _entity = 'TM' THEN -- Team
		SELECT COUNT(*) INTO _n FROM athlete PR WHERE PR.id_team = _id; _count := _count + _n;
	ELSIF _entity = 'SP' THEN -- Sport
		SELECT COUNT(*) INTO _n FROM athlete PR WHERE PR.id_sport = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Person Lists
	IF _entity = 'PR' THEN -- Person
		SELECT COUNT(*) INTO _n FROM _person_list PL WHERE PL.id_person = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Records
	IF _entity ~ 'CN|PR|TM' THEN -- Country/Person/Team
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		SELECT COUNT(*) INTO _n FROM record RC
			LEFT JOIN event EV ON RC.id_event = EV.id
			LEFT JOIN type TP ON EV.id_type = TP.id
		WHERE (RC.ID_RANK1 = _id OR RC.ID_RANK2 = _id OR RC.ID_RANK3 = _id OR RC.ID_RANK4 = _id OR RC.ID_RANK5 = _id) AND lower(RC.type1) = (CASE WHEN _type1 = 50 THEN 'team' ELSE 'individual' END);
		_count := _count + _n;
	ELSIF _entity = 'SP' THEN -- Sport
		SELECT COUNT(*) INTO _n FROM record RC WHERE RC.id_sport = _id; _count := _count + _n;
	ELSIF _entity = 'CP' THEN -- Championship
		SELECT COUNT(*) INTO _n FROM record RC WHERE RC.id_championship = _id; _count := _count + _n;
	ELSIF _entity = 'EV' THEN -- Event
		SELECT COUNT(*) INTO _n FROM record RC WHERE RC.id_event = _id OR RC.id_subevent = _id; _count := _count + _n;
	ELSIF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM record RC WHERE RC.id_city = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Results
	IF _entity ~ 'CN|PR|TM' THEN -- Country/Person/Team
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		SELECT COUNT(*) INTO _n FROM result RS
			LEFT JOIN event EV ON RS.id_event = EV.id
			LEFT JOIN event SE ON RS.id_subevent = SE.id
			LEFT JOIN event SE2 ON RS.id_subevent2 = SE2.id
			LEFT JOIN type TP1 ON EV.id_type = TP1.id
			LEFT JOIN type TP2 ON SE.id_type = TP2.id
			LEFT JOIN type TP3 ON SE2.id_type = TP3.id
		WHERE (RS.id_rank1 = _id OR RS.id_rank2 = _id OR RS.id_rank3 = _id OR RS.id_rank4 = _id OR RS.id_rank5 = _id OR RS.id_rank6 = _id OR RS.id_rank7 = _id OR RS.id_rank8 = _id OR RS.id_rank9 = _id OR RS.id_rank10 = _id)
			AND ((TP1.number BETWEEN _type1 AND _type2 AND TP2.number IS NULL) OR (TP2.number BETWEEN _type1 AND _type2 AND TP3.number IS NULL) OR TP3.number BETWEEN _type1 AND _type2);
		_count := _count + _n;
	ELSIF _entity = 'SP' THEN -- Sport
		SELECT COUNT(*) INTO _n FROM result RS WHERE RS.id_sport = _id; _count := _count + _n;
	ELSIF _entity = 'CP' THEN -- Championship
		SELECT COUNT(*) INTO _n FROM result RS WHERE RS.id_championship = _id; _count := _count + _n;
	ELSIF _entity = 'EV' THEN -- Event
		SELECT COUNT(*) INTO _n FROM result RS WHERE RS.id_event = _id OR RS.id_subevent = _id OR RS.id_subevent2 = _id; _count := _count + _n;
	ELSIF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM result RS WHERE RS.id_city1 = _id OR RS.id_city2 = _id; _count := _count + _n;
	ELSIF _entity = 'CX' THEN -- Complex
		SELECT COUNT(*) INTO _n FROM result RS WHERE RS.id_complex1 = _id OR RS.id_complex2 = _id; _count := _count + _n;
	ELSIF _entity = 'YR' THEN -- Year
		SELECT COUNT(*) INTO _n FROM result RS WHERE RS.id_year = _id; _count := _count + _n;
	ELSIF _entity = 'OL' THEN -- Olympics
		SELECT COUNT(*) INTO _n FROM result RS LEFT JOIN olympics OL ON RS.id_year = OL.id_year LEFT JOIN sport SP ON RS.id_sport = SP.id WHERE OL.id = _id AND RS.id_championship = 1 AND SP.type = OL.type; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Retired Numbers
	IF _entity = 'TM' THEN -- Team
		SELECT COUNT(*) INTO _n FROM retired_number RN WHERE RN.id_team = _id; _count := _count + _n;
	ELSIF _entity = 'PR' THEN -- Person
		SELECT COUNT(*) INTO _n FROM retired_number RN WHERE RN.id_person = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Rounds
	IF _entity ~ 'CN|PR|TM' THEN -- Country/Person/Team
		IF _entity = 'CN' THEN _type1 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;
		ELSIF _entity = 'TM' THEN _type1 = 50; END IF;
		SELECT COUNT(*) INTO _n FROM round RD
		WHERE RD.id_result_type = _type1 AND RD.id_rank1 = _id OR RD.id_rank2 = _id OR RD.id_rank3 = _id;
		_count := _count + _n;
	ELSIF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM round RD WHERE RD.id_city = _id; _count := _count + _n;
	ELSIF _entity = 'CX' THEN -- Complex
		SELECT COUNT(*) INTO _n FROM round RD WHERE RD.id_complex = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Teams
	IF _entity = 'SP' THEN -- Sport
		SELECT COUNT(*) INTO _n FROM team TM WHERE TM.id_sport = _id; _count := _count + _n;
	ELSIF _entity = 'CN' THEN -- Country
		SELECT COUNT(*) INTO _n FROM team TM WHERE TM.id_country = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Team Stadiums
	IF _entity = 'TM' THEN -- Team
		SELECT COUNT(*) INTO _n FROM team_stadium TS WHERE TS.id_team = _id; _count := _count + _n;
	ELSIF _entity = 'CX' THEN -- Complex
		SELECT COUNT(*) INTO _n FROM team_stadium TS WHERE TS.id_complex = _id; _count := _count + _n;
	END IF;
	
	RETURN _count;
end;
$BODY$;
