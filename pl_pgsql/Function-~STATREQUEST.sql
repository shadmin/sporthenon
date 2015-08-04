-- Function: "~StatRequest"(smallint)

-- DROP FUNCTION "~StatRequest"(smallint);

CREATE OR REPLACE FUNCTION "~StatRequest"(_index smallint)
  RETURNS refcursor AS
$BODY$
declare
	_c refcursor;
	_query text;
begin
	IF _index = 0 THEN
		_query := 'SELECT TYPE AS KEY, COUNT(*) AS VALUE FROM "~REQUEST" GROUP BY TYPE ORDER BY VALUE DESC, TYPE';
	ELSIF _index = 1 THEN
		_query := 'SELECT LABEL AS KEY, COUNT(RQ.ID) AS VALUE FROM "SPORT" SP LEFT JOIN "~REQUEST" RQ ON (RQ.TYPE = ''RS'' AND (SP.ID || '''') = SUBSTRING(PARAMS, 0, POSITION(''-'' IN PARAMS)))';
		_query := _query || ' GROUP BY LABEL ORDER BY VALUE DESC, LABEL';
	END IF;

	OPEN _c FOR EXECUTE _query;
	RETURN  _c;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;