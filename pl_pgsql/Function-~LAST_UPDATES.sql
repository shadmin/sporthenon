-- Function: "~LAST_UPDATES"(integer, integer, character varying)

-- DROP FUNCTION "~LAST_UPDATES"(integer, integer, character varying);

CREATE OR REPLACE FUNCTION "~LAST_UPDATES"(_count integer, _offset integer, _lang character varying)
  RETURNS refcursor AS
$BODY$
declare
	_c refcursor;
begin
	OPEN _c FOR EXECUTE
	'SELECT RS.id AS rs_id, YR.label AS yr_label, SP.id AS sp_id, CP.id AS cp_id, EV.id AS ev_id, SE.id AS se_id, SE2.id AS se2_id, SP.label' || _lang || ' AS sp_label, CP.label' || _lang || ' AS cp_label, EV.label' || _lang || ' AS ev_label, SE.label' || _lang || ' AS se_label, SE2.label' || _lang || ' AS se2_label, SP.label AS sp_label_en, CP.label AS cp_label_en, EV.label AS ev_label_en, SE.label AS se_label_en, SE2.label AS se2_label_en, RS.first_update AS rs_update, TP1.number as tp1_number, TP2.number AS tp2_number, TP3.number AS tp3_number, PR1.id AS pr1_id, PR1.first_name AS pr1_first_name, PR1.last_name AS pr1_last_name, PR1.id_country AS pr1_country, PR2.id AS pr2_id, PR2.first_name AS pr2_first_name, PR2.last_name AS pr2_last_name, PR2.id_country AS pr2_country, TM1.id AS tm1_id, TM1.label AS tm1_label, TM2.id AS tm2_id, TM2.label AS tm2_label, CN1.id AS cn1_id, CN1.code AS cn1_code, CN1.label' || _lang || ' AS cn1_label, CN1.label AS cn1_label_en, CN2.id AS cn2_id, CN2.code AS cn2_code, CN2.label' || _lang || ' AS cn2_label, CN2.label AS cn2_label_en, RS.result1 AS rs_text1, RS.result2 AS rs_text2, RS.exa AS rs_text3, RS.date2 AS rs_date FROM "RESULT" RS
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
		LEFT JOIN "TEAM" TM1 ON RS.id_rank1=TM1.id
		LEFT JOIN "TEAM" TM2 ON RS.id_rank2=TM2.id
		LEFT JOIN "COUNTRY" CN1 ON RS.id_rank1=CN1.id
		LEFT JOIN "COUNTRY" CN2 ON RS.id_rank2=CN2.id
	ORDER BY YR.id DESC, RS.first_update DESC LIMIT ' || _count || ' OFFSET ' || _offset;
	RETURN  _c;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
