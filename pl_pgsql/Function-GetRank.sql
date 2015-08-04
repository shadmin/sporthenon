-- Function: "GetRank"("RESULT", integer[])

-- DROP FUNCTION "GetRank"("RESULT", integer[]);

CREATE OR REPLACE FUNCTION "GetRank"(_rs "RESULT", _ids integer[])
  RETURNS smallint AS
$BODY$
DECLARE
	_type1 smallint;
	_type2 smallint;
	_type3 smallint;
BEGIN
	-- Get result type
	SELECT DISTINCT TP1.number, TP2.number, TP3.number
	INTO _type1, _type2, _type3
	FROM "RESULT" RS LEFT JOIN "EVENT" EV ON RS.id_event = EV.id LEFT JOIN "TYPE" TP1 ON EV.id_type = TP1.id LEFT JOIN "EVENT" SE ON RS.id_subevent = SE.id LEFT JOIN "TYPE" TP2 ON SE.id_type = TP2.id LEFT JOIN "EVENT" SE2 ON RS.id_subevent2 = SE2.id LEFT JOIN "TYPE" TP3 ON SE2.id_type = TP3.id
	WHERE RS.id = _rs.id;
	IF _type3 IS NOT NULL THEN
		_type1 := _type3;
	ELSIF _type2 IS NOT NULL THEN
		_type1 := _type2;
	END IF;

	-- Calculate rank
	IF _rs.id_rank1 = ANY(_ids) OR
	  (_rs.id_rank2 = ANY(_ids) AND (_type1 IN (4, 5) OR _rs.comment IN ('#DOUBLE#', '#TRIPLE#') OR _rs.exa ~* '(^|/)1-(2|3|4|5|6|7|8|9)(/|$)')) OR
	  (_rs.id_rank3 = ANY(_ids) AND (_type1 = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)1-(3|4|5|6|7|8|9)(/|$)')) OR
	  (_rs.id_rank4 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(4|5|6|7|8|9)(/|$)') OR
	  (_rs.id_rank5 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(5|6|7|8|9)(/|$)') OR
	  (_rs.id_rank6 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(6|7|8|9)(/|$)') OR
	  (_rs.id_rank7 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(7|8|9)(/|$)') OR
	  (_rs.id_rank8 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(8|9)(/|$)') OR
	  (_rs.id_rank9 = ANY(_ids) AND _rs.exa ~* '(^|/)1-9(/|$)') THEN
		RETURN 1;
	ELSIF (_rs.id_rank2 = ANY(_ids) AND _type1 NOT IN (4, 5) AND (_rs.comment IS NULL OR _rs.comment NOT IN ('#DOUBLE#', '#TRIPLE#'))) OR
	  (_rs.id_rank3 = ANY(_ids) AND ((_type1 = 4 OR _rs.comment = '#DOUBLE#' OR _rs.exa ~* '(^|/)2-(3|4|5|6|7|8|9)(/|$)') AND _type1 <> 5 AND (_rs.comment IS NULL OR _rs.comment <> '#TRIPLE#'))) OR
	  (_rs.id_rank4 = ANY(_ids) AND ((_type1 = 4 OR _rs.comment = '#DOUBLE#' OR _rs.exa ~* '(^|/)2-(4|5|6|7|8|9)(/|$)') AND _type1 <> 5 AND (_rs.comment IS NULL OR _rs.comment <> '#TRIPLE#'))) OR 
	  (_rs.id_rank5 = ANY(_ids) AND (_type1 = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)2-(5|6|7|8|9)(/|$)')) OR
	  (_rs.id_rank6 = ANY(_ids) AND (_type1 = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)2-(6|7|8|9)(/|$)')) OR
	  (_rs.id_rank7 = ANY(_ids) AND _rs.exa ~* '(^|/)2-(7|8|9)(/|$)') OR
	  (_rs.id_rank8 = ANY(_ids) AND _rs.exa ~* '(^|/)2-(8|9)(/|$)') OR
	  (_rs.id_rank9 = ANY(_ids) AND _rs.exa ~* '(^|/)2-9(/|$)') THEN
		RETURN 2;
	ELSIF (_rs.id_rank3 = ANY(_ids) AND _type1 NOT IN (4, 5) AND (_rs.comment IS NULL OR _rs.comment NOT IN ('#DOUBLE#', '#TRIPLE#'))) OR
	  (_rs.id_rank4 = ANY(_ids) AND (_type1 NOT IN (4, 5) AND (_rs.comment IS NULL OR _rs.comment NOT IN ('#DOUBLE#', '#TRIPLE#')) AND _rs.exa ~* '(^|/)3-(4|5|6|7|8|9)(/|$)')) OR
	  (_rs.id_rank5 = ANY(_ids) AND (_type1 <> 5 AND (_rs.comment IS NULL OR _rs.comment <> '#TRIPLE#') AND _rs.exa ~* '(^|/)3-(5|6|7|8|9)(/|$)')) OR
	  (_rs.id_rank6 = ANY(_ids) AND (_type1 <> 5 AND (_rs.comment IS NULL OR _rs.comment <> '#TRIPLE#') AND _rs.exa ~* '(^|/)3-(6|7|8|9)(/|$)')) OR
	  (_rs.id_rank7 = ANY(_ids) AND (_type1 = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)3-(7|8|9)(/|$)')) OR
	  (_rs.id_rank8 = ANY(_ids) AND (_type1 = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)3-(8|9)(/|$)')) OR
	  (_rs.id_rank9 = ANY(_ids) AND (_type1 = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)3-9(/|$)')) THEN
		RETURN 3;
	END IF;
	RETURN 0;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;