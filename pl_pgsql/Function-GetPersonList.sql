-- Function: "GetPersonList"(text)

-- DROP FUNCTION "GetPersonList"(text);

CREATE OR REPLACE FUNCTION "GetPersonList"(_results text)
  RETURNS refcursor AS
$BODY$
declare
	_query text;
	_c refcursor;
begin
	_query = 'SELECT PL.id AS pl_id, PL.id_result AS rs_id, PL.rank AS pl_rank, PL.id_person AS pr_id, PR.last_name AS pr_last_name, PR.first_name AS pr_first_name, CN.id AS pr_country_id, CN.code AS pr_country_code';
	_query = _query || ' FROM "~PERSON_LIST" PL';
	_query = _query || ' LEFT JOIN "PERSON" PR ON PL.id_person = PR.id';
	_query = _query || ' LEFT JOIN "COUNTRY" CN ON PR.id_country = CN.id';
	_query = _query || ' WHERE PL.id_result IN (' || _results || ')';
	_query = _query || ' ORDER BY PL.id_result, PL.rank, PL.id';
	OPEN _c FOR EXECUTE _query;
	RETURN  _c;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;