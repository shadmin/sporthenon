-- Function: "~CONTRIBUTORS"()

-- DROP FUNCTION "~CONTRIBUTORS"();

CREATE OR REPLACE FUNCTION "~CONTRIBUTORS"()
  RETURNS refcursor AS
$BODY$
declare
	_c refcursor;
begin
	OPEN _c FOR EXECUTE
	'SELECT CB.id AS id, login, public_name AS name, COUNT(RS.id_member) AS count
		FROM "~MEMBER" CB LEFT JOIN "RESULT" RS ON CB.ID=RS.ID_MEMBER
		GROUP BY CB.id, login, public_name
		ORDER BY 4 desc';
	RETURN  _c;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
