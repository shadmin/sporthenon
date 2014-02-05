CREATE OR REPLACE FUNCTION "~MERGE"(_alias character varying, _id1 integer, _id2 integer)
  RETURNS boolean AS
$BODY$
declare
begin
	IF _alias = 'CP' THEN
		UPDATE "RESULT" SET id_championship = _id2 WHERE id_championship = _id1;
		UPDATE "RECORD" SET id_championship = _id2 WHERE id_championship = _id1;
		DELETE FROM "CHAMPIONSHIP" WHERE id = _id1;
	ELSIF _alias = 'CT' THEN
		UPDATE "COMPLEX" SET id_city = _id2 WHERE id_city = _id1;
		UPDATE "OLYMPICS" SET id_city = _id2 WHERE id_city = _id1;
		UPDATE "RESULT" SET id_city = _id2 WHERE id_city = _id1;
		UPDATE "RECORD" SET id_city = _id2 WHERE id_city = _id1;
		DELETE FROM "CITY" WHERE id = _id1;
	ELSIF _alias = 'CN' THEN
		UPDATE "CITY" SET id_country = _id2 WHERE id_country = _id1;
		UPDATE "OLYMPIC_RANKING" SET id_country = _id2 WHERE id_country = _id1;
		UPDATE "PERSON" SET id_country = _id2 WHERE id_country = _id1;
		UPDATE "RESULT" SET id_rank1 = _id2 WHERE id_rank1 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE "RESULT" SET id_rank2 = _id2 WHERE id_rank2 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE "RESULT" SET id_rank3 = _id2 WHERE id_rank3 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE "RESULT" SET id_rank4 = _id2 WHERE id_rank4 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE "RESULT" SET id_rank5 = _id2 WHERE id_rank5 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE "RESULT" SET id_rank6 = _id2 WHERE id_rank6 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE "RESULT" SET id_rank7 = _id2 WHERE id_rank7 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE "RESULT" SET id_rank8 = _id2 WHERE id_rank8 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE "RESULT" SET id_rank9 = _id2 WHERE id_rank9 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE "RESULT" SET id_rank10 = _id2 WHERE id_rank10 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RS.id_subevent IS NULL AND TP1.number=99) OR (RS.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE "RECORD" SET id_rank1 = _id2 WHERE id_rank1 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=99) OR (RC.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE "RECORD" SET id_rank2 = _id2 WHERE id_rank2 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=99) OR (RC.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE "RECORD" SET id_rank3 = _id2 WHERE id_rank3 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=99) OR (RC.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE "RECORD" SET id_rank4 = _id2 WHERE id_rank4 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=99) OR (RC.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE "RECORD" SET id_rank5 = _id2 WHERE id_rank5 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=99) OR (RC.id_subevent IS NOT NULL AND TP2.number=99));
		UPDATE "TEAM" SET id_country = _id2 WHERE id_country = _id1;
		DELETE FROM "COUNTRY" WHERE id = _id1;
	ELSIF _alias = 'EV' THEN
		UPDATE "RESULT" SET id_event = _id2 WHERE id_event = _id1;
		UPDATE "RESULT" SET id_subevent = _id2 WHERE id_subevent = _id1;
		UPDATE "RECORD" SET id_event = _id2 WHERE id_event = _id1;
		UPDATE "RECORD" SET id_subevent = _id2 WHERE id_subevent = _id1;
		DELETE FROM "COUNTRY" WHERE id = _id1;
	ELSIF _alias = 'PR' THEN
		UPDATE "HALL_OF_FAME" SET id_person = _id2 WHERE id_person = _id1;
		UPDATE "RETIRED_NUMBER" SET id_person = _id2 WHERE id_person = _id1;
		DELETE FROM "PERSON" WHERE id = _id1;
	ELSIF _alias = 'SP' THEN
		UPDATE "RESULT" SET id_sport = _id2 WHERE id_sport = _id1;
		UPDATE "RECORD" SET id_sport = _id2 WHERE id_sport = _id1;
		DELETE FROM "SPORT" WHERE id = _id1;
	ELSIF _alias = 'ST' THEN
		UPDATE "CITY" SET id_state = _id2 WHERE id_state = _id1;
		DELETE FROM "STATE" WHERE id = _id1;
	ELSIF _alias = 'TM' THEN
		UPDATE "RESULT" SET id_rank1 = _id2 WHERE id_rank1 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE "RESULT" SET id_rank2 = _id2 WHERE id_rank2 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE "RESULT" SET id_rank3 = _id2 WHERE id_rank3 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE "RESULT" SET id_rank4 = _id2 WHERE id_rank4 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE "RESULT" SET id_rank5 = _id2 WHERE id_rank5 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE "RESULT" SET id_rank6 = _id2 WHERE id_rank6 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE "RESULT" SET id_rank7 = _id2 WHERE id_rank7 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE "RESULT" SET id_rank8 = _id2 WHERE id_rank8 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE "RESULT" SET id_rank9 = _id2 WHERE id_rank9 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE "RESULT" SET id_rank10 = _id2 WHERE id_rank10 = _id1 AND id IN (SELECT RS.id FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RS.id_subevent IS NULL AND TP1.number=50) OR (RS.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE "RECORD" SET id_rank1 = _id2 WHERE id_rank1 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=50) OR (RC.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE "RECORD" SET id_rank2 = _id2 WHERE id_rank2 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=50) OR (RC.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE "RECORD" SET id_rank3 = _id2 WHERE id_rank3 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=50) OR (RC.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE "RECORD" SET id_rank4 = _id2 WHERE id_rank4 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=50) OR (RC.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE "RECORD" SET id_rank5 = _id2 WHERE id_rank5 = _id1 AND id IN (SELECT RC.id FROM "RECORD" RC LEFT JOIN "EVENT" EV ON RC.id_event = EV.id LEFT JOIN "EVENT" SE ON RC.id_subevent = SE.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id WHERE (RC.id_subevent IS NULL AND TP1.number=50) OR (RC.id_subevent IS NOT NULL AND TP2.number=50));
		UPDATE "RETIRED_NUMBER" SET id_team = _id2 WHERE id_team = _id1;
		UPDATE "TEAM_STADIUM" SET id_team = _id2 WHERE id_team = _id1;
		UPDATE "WIN_LOSS" SET id_team = _id2 WHERE id_team = _id1;
		DELETE FROM "TEAM" WHERE id = _id1;
	END IF;
	RETURN true;
end;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION "~MERGE"(character varying, integer, integer) OWNER TO postgres;

