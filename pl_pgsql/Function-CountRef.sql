-- Function: "CountRef"(character varying, integer)

-- DROP FUNCTION "CountRef"(character varying, integer);

CREATE OR REPLACE FUNCTION "CountRef"(
    _entity character varying,
    _id integer)
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
		SELECT COUNT(*) INTO _n FROM "City" CT WHERE CT.id_country = _id; _count := _count + _n;
	ELSIF _entity = 'ST' THEN -- State
		SELECT COUNT(*) INTO _n FROM "City" CT WHERE CT.id_state = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Complexes
	IF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM "Complex" CX WHERE CX.id_city = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Hall of Fame
	IF _entity = 'YR' THEN -- Year
		SELECT COUNT(*) INTO _n FROM "HallOfFame" HF WHERE HF.id_year = _id; _count := _count + _n;
	ELSIF _entity = 'PR' THEN -- Person
		SELECT COUNT(*) INTO _n FROM "HallOfFame" HF WHERE HF.id_person = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Olympics
	IF _entity = 'YR' THEN -- Year
		SELECT COUNT(*) INTO _n FROM "Olympics" OL WHERE OL.id_year = _id; _count := _count + _n;
	ELSIF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM "Olympics" OL WHERE OL.id_city = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Olympic Rankings
	IF _entity = 'OL' THEN -- Olympics
		SELECT COUNT(*) INTO _n FROM "OlympicRanking" OR_ WHERE OR_.id_olympics = _id; _count := _count + _n;
	ELSIF _entity = 'CN' THEN -- Country
		SELECT COUNT(*) INTO _n FROM "OlympicRanking" OR_ WHERE OR_.id_country = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Athletes
	IF _entity = 'CN' THEN -- Country
		SELECT COUNT(*) INTO _n FROM "Athlete" PR WHERE PR.id_country = _id; _count := _count + _n;
	ELSIF _entity = 'TM' THEN -- Team
		SELECT COUNT(*) INTO _n FROM "Athlete" PR WHERE PR.id_team = _id; _count := _count + _n;
	ELSIF _entity = 'SP' THEN -- Sport
		SELECT COUNT(*) INTO _n FROM "Athlete" PR WHERE PR.id_sport = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Person Lists
	IF _entity = 'PR' THEN -- Person
		SELECT COUNT(*) INTO _n FROM "~PersonList" PL WHERE PL.id_person = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Records
	IF _entity ~ 'CN|PR|TM' THEN -- Country/Person/Team
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		SELECT COUNT(*) INTO _n FROM "Record" RC
			LEFT JOIN "Event" EV ON RC.id_event = EV.id
			LEFT JOIN "Type" TP ON EV.id_type = TP.id
		WHERE (RC.ID_RANK1 = _id OR RC.ID_RANK2 = _id OR RC.ID_RANK3 = _id OR RC.ID_RANK4 = _id OR RC.ID_RANK5 = _id) AND lower(RC.type1) = (CASE WHEN _type1 = 50 THEN 'team' ELSE 'individual' END);
		_count := _count + _n;
	ELSIF _entity = 'SP' THEN -- Sport
		SELECT COUNT(*) INTO _n FROM "Record" RC WHERE RC.id_sport = _id; _count := _count + _n;
	ELSIF _entity = 'CP' THEN -- Championship
		SELECT COUNT(*) INTO _n FROM "Record" RC WHERE RC.id_championship = _id; _count := _count + _n;
	ELSIF _entity = 'EV' THEN -- Event
		SELECT COUNT(*) INTO _n FROM "Record" RC WHERE RC.id_event = _id OR RC.id_subevent = _id; _count := _count + _n;
	ELSIF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM "Record" RC WHERE RC.id_city = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Results
	IF _entity ~ 'CN|PR|TM' THEN -- Country/Person/Team
		IF _entity = 'CN' THEN _type1 = 99;_type2 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;_type2 = 10;
		ELSIF _entity = 'TM' THEN _type1 = 50;_type2 = 50; END IF;
		SELECT COUNT(*) INTO _n FROM "Result" RS
			LEFT JOIN "Event" EV ON RS.id_event = EV.id
			LEFT JOIN "Event" SE ON RS.id_subevent = SE.id
			LEFT JOIN "Event" SE2 ON RS.id_subevent2 = SE2.id
			LEFT JOIN "Type" TP1 ON EV.id_type = TP1.id
			LEFT JOIN "Type" TP2 ON SE.id_type = TP2.id
			LEFT JOIN "Type" TP3 ON SE2.id_type = TP3.id
		WHERE (RS.id_rank1 = _id OR RS.id_rank2 = _id OR RS.id_rank3 = _id OR RS.id_rank4 = _id OR RS.id_rank5 = _id OR RS.id_rank6 = _id OR RS.id_rank7 = _id OR RS.id_rank8 = _id OR RS.id_rank9 = _id OR RS.id_rank10 = _id)
			AND ((TP1.number BETWEEN _type1 AND _type2 AND TP2.number IS NULL) OR (TP2.number BETWEEN _type1 AND _type2 AND TP3.number IS NULL) OR TP3.number BETWEEN _type1 AND _type2);
		_count := _count + _n;
	ELSIF _entity = 'SP' THEN -- Sport
		SELECT COUNT(*) INTO _n FROM "Result" RS WHERE RS.id_sport = _id; _count := _count + _n;
	ELSIF _entity = 'CP' THEN -- Championship
		SELECT COUNT(*) INTO _n FROM "Result" RS WHERE RS.id_championship = _id; _count := _count + _n;
	ELSIF _entity = 'EV' THEN -- Event
		SELECT COUNT(*) INTO _n FROM "Result" RS WHERE RS.id_event = _id OR RS.id_subevent = _id OR RS.id_subevent2 = _id; _count := _count + _n;
	ELSIF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM "Result" RS WHERE RS.id_city1 = _id OR RS.id_city2 = _id; _count := _count + _n;
	ELSIF _entity = 'CX' THEN -- Complex
		SELECT COUNT(*) INTO _n FROM "Result" RS WHERE RS.id_complex1 = _id OR RS.id_complex2 = _id; _count := _count + _n;
	ELSIF _entity = 'YR' THEN -- Year
		SELECT COUNT(*) INTO _n FROM "Result" RS WHERE RS.id_year = _id; _count := _count + _n;
	ELSIF _entity = 'OL' THEN -- Olympics
		SELECT COUNT(*) INTO _n FROM "Result" RS LEFT JOIN "Olympics" OL ON RS.id_year = OL.id_year LEFT JOIN "Sport" SP ON RS.id_sport = SP.id WHERE OL.id = _id AND RS.id_championship = 1 AND SP.type = OL.type; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Retired Numbers
	IF _entity = 'TM' THEN -- Team
		SELECT COUNT(*) INTO _n FROM "RetiredNumber" RN WHERE RN.id_team = _id; _count := _count + _n;
	ELSIF _entity = 'PR' THEN -- Person
		SELECT COUNT(*) INTO _n FROM "RetiredNumber" RN WHERE RN.id_person = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Rounds
	IF _entity ~ 'CN|PR|TM' THEN -- Country/Person/Team
		IF _entity = 'CN' THEN _type1 = 99;
		ELSIF _entity = 'PR' THEN _type1 = 1;
		ELSIF _entity = 'TM' THEN _type1 = 50; END IF;
		SELECT COUNT(*) INTO _n FROM "Round" RD
		WHERE RD.id_result_type = _type1 AND RD.id_rank1 = _id OR RD.id_rank2 = _id OR RD.id_rank3 = _id;
		_count := _count + _n;
	ELSIF _entity = 'CT' THEN -- City
		SELECT COUNT(*) INTO _n FROM "Round" RD WHERE RD.id_city = _id; _count := _count + _n;
	ELSIF _entity = 'CX' THEN -- Complex
		SELECT COUNT(*) INTO _n FROM "Round" RD WHERE RD.id_complex = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Teams
	IF _entity = 'SP' THEN -- Sport
		SELECT COUNT(*) INTO _n FROM "Team" TM WHERE TM.id_sport = _id; _count := _count + _n;
	ELSIF _entity = 'CN' THEN -- Country
		SELECT COUNT(*) INTO _n FROM "Team" TM WHERE TM.id_country = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Team Stadiums
	IF _entity = 'TM' THEN -- Team
		SELECT COUNT(*) INTO _n FROM "TeamStadium" TS WHERE TS.id_team = _id; _count := _count + _n;
	ELSIF _entity = 'CX' THEN -- Complex
		SELECT COUNT(*) INTO _n FROM "TeamStadium" TS WHERE TS.id_complex = _id; _count := _count + _n;
	END IF;

	-- Count '_id' referenced in: Wins/Losses
	IF _entity = 'TM' THEN -- Team
		SELECT COUNT(*) INTO _n FROM "WinLoss" WL WHERE WL.id_team = _id; _count := _count + _n;
	END IF;
	
	RETURN _count;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
