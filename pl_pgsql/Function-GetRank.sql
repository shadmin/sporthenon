-- Function: "GetRank"("Result", smallint, integer[])

-- DROP FUNCTION "GetRank"("Result", smallint, integer[]);

CREATE OR REPLACE FUNCTION "GetRank"(_rs "Result", _type smallint, _ids integer[])
  RETURNS smallint AS
$BODY$
DECLARE
BEGIN
	IF _rs.id_rank1 = ANY(_ids) OR
	  (_rs.id_rank2 = ANY(_ids) AND (_type IN (4, 5) OR _rs.comment IN ('#DOUBLE#', '#TRIPLE#') OR _rs.exa ~* '(^|/)1-(2|3|4|5|6|7|8|9)(/|$)')) OR
	  (_rs.id_rank3 = ANY(_ids) AND (_type = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)1-(3|4|5|6|7|8|9)(/|$)')) OR
	  (_rs.id_rank4 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(4|5|6|7|8|9)(/|$)') OR
	  (_rs.id_rank5 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(5|6|7|8|9)(/|$)') OR
	  (_rs.id_rank6 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(6|7|8|9)(/|$)') OR
	  (_rs.id_rank7 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(7|8|9)(/|$)') OR
	  (_rs.id_rank8 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(8|9)(/|$)') OR
	  (_rs.id_rank9 = ANY(_ids) AND _rs.exa ~* '(^|/)1-9(/|$)') THEN
		RETURN 1;
	ELSIF (_rs.id_rank2 = ANY(_ids) AND _type NOT IN (4, 5) AND (_rs.comment IS NULL OR _rs.comment NOT IN ('#DOUBLE#', '#TRIPLE#'))) OR
	  (_rs.id_rank3 = ANY(_ids) AND ((_type = 4 OR _rs.comment = '#DOUBLE#' OR _rs.exa ~* '(^|/)2-(3|4|5|6|7|8|9)(/|$)') AND _type <> 5 AND (_rs.comment IS NULL OR _rs.comment <> '#TRIPLE#'))) OR
	  (_rs.id_rank4 = ANY(_ids) AND ((_type = 4 OR _rs.comment = '#DOUBLE#' OR _rs.exa ~* '(^|/)2-(4|5|6|7|8|9)(/|$)') AND _type <> 5 AND (_rs.comment IS NULL OR _rs.comment <> '#TRIPLE#'))) OR 
	  (_rs.id_rank5 = ANY(_ids) AND (_type = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)2-(5|6|7|8|9)(/|$)')) OR
	  (_rs.id_rank6 = ANY(_ids) AND (_type = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)2-(6|7|8|9)(/|$)')) OR
	  (_rs.id_rank7 = ANY(_ids) AND _rs.exa ~* '(^|/)2-(7|8|9)(/|$)') OR
	  (_rs.id_rank8 = ANY(_ids) AND _rs.exa ~* '(^|/)2-(8|9)(/|$)') OR
	  (_rs.id_rank9 = ANY(_ids) AND _rs.exa ~* '(^|/)2-9(/|$)') THEN
		RETURN 2;
	ELSIF (_rs.id_rank3 = ANY(_ids) AND _type NOT IN (4, 5) AND (_rs.comment IS NULL OR _rs.comment NOT IN ('#DOUBLE#', '#TRIPLE#'))) OR
	  (_rs.id_rank4 = ANY(_ids) AND (_type NOT IN (4, 5) AND (_rs.comment IS NULL OR _rs.comment NOT IN ('#DOUBLE#', '#TRIPLE#')) AND _rs.exa ~* '(^|/)3-(4|5|6|7|8|9)(/|$)')) OR
	  (_rs.id_rank5 = ANY(_ids) AND (_type <> 5 AND (_rs.comment IS NULL OR _rs.comment <> '#TRIPLE#') AND _rs.exa ~* '(^|/)3-(5|6|7|8|9)(/|$)')) OR
	  (_rs.id_rank6 = ANY(_ids) AND (_type <> 5 AND (_rs.comment IS NULL OR _rs.comment <> '#TRIPLE#') AND _rs.exa ~* '(^|/)3-(6|7|8|9)(/|$)')) OR
	  (_rs.id_rank7 = ANY(_ids) AND (_type = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)3-(7|8|9)(/|$)')) OR
	  (_rs.id_rank8 = ANY(_ids) AND (_type = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)3-(8|9)(/|$)')) OR
	  (_rs.id_rank9 = ANY(_ids) AND (_type = 5 OR _rs.comment = '#TRIPLE#' OR _rs.exa ~* '(^|/)3-9(/|$)')) THEN
		RETURN 3;
	END IF;
	RETURN 0;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;