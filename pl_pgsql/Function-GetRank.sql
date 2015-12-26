-- Function: "GetRank"("Result", smallint, integer[])

-- DROP FUNCTION "GetRank"("Result", smallint, integer[]);

CREATE OR REPLACE FUNCTION "GetRank"(
    _rs "Result",
    _type smallint,
    _ids integer[])
  RETURNS smallint AS
$BODY$
DECLARE
	_double boolean;
	_triple boolean;
	_double_triple boolean;
BEGIN
	IF (_type = 4 OR _rs.comment = '#DOUBLE#')
	THEN _double := true; ELSE _double := false; END IF;
	IF (_type = 5 OR _rs.comment = '#TRIPLE#')
	THEN _triple := true; ELSE _triple := false; END IF;
	IF (_type IN (4, 5) OR _rs.comment IN ('#DOUBLE#', '#TRIPLE#'))
	THEN _double_triple := true; ELSE _double_triple := false; END IF;
	
	IF _rs.id_rank1 = ANY(_ids) OR
	  (_rs.id_rank2 = ANY(_ids) AND (_double_triple = true OR _rs.exa ~* '(^|/)1-(2|3|4|5|6|7|8|9|10)(/|$)')) OR
	  (_rs.id_rank3 = ANY(_ids) AND (_triple = true OR _rs.exa ~* '(^|/)1-(3|4|5|6|7|8|9|10)(/|$)')) OR
	  (_rs.id_rank4 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(4|5|6|7|8|9|10)(/|$)') OR
	  (_rs.id_rank5 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(5|6|7|8|9|10)(/|$)') OR
	  (_rs.id_rank6 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(6|7|8|9|10)(/|$)') OR
	  (_rs.id_rank7 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(7|8|9|10)(/|$)') OR
	  (_rs.id_rank8 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(8|9|10)(/|$)') OR
	  (_rs.id_rank9 = ANY(_ids) AND _rs.exa ~* '(^|/)1-(9|10)(/|$)') OR
	  (_rs.id_rank10 = ANY(_ids) AND _rs.exa ~* '(^|/)1-10(/|$)') THEN
		RETURN 1;
	ELSIF (_rs.id_rank2 = ANY(_ids) AND _double_triple = false) OR
	  (_rs.id_rank3 = ANY(_ids) AND ((_double = true OR _rs.exa ~* '(^|/)2-(3|4|5|6|7|8|9|10)(/|$)') AND _triple = false)) OR
	  (_rs.id_rank4 = ANY(_ids) AND (_double_triple = true OR _rs.exa ~* '(^|/)2-(4|5|6|7|8|9|10)(/|$)')) OR 
	  (_rs.id_rank5 = ANY(_ids) AND (_triple = true OR _rs.exa ~* '(^|/)2-(5|6|7|8|9|10)(/|$)')) OR
	  (_rs.id_rank6 = ANY(_ids) AND (_triple = true OR _rs.exa ~* '(^|/)2-(6|7|8|9|10)(/|$)')) OR
	  (_rs.id_rank7 = ANY(_ids) AND _rs.exa ~* '(^|/)2-(7|8|9|10)(/|$)') OR
	  (_rs.id_rank8 = ANY(_ids) AND _rs.exa ~* '(^|/)2-(8|9|10)(/|$)') OR
	  (_rs.id_rank9 = ANY(_ids) AND _rs.exa ~* '(^|/)2-(9|10)(/|$)') OR
	  (_rs.id_rank10 = ANY(_ids) AND _rs.exa ~* '(^|/)2-10(/|$)') THEN
		RETURN 2;
	ELSIF (_rs.id_rank3 = ANY(_ids) AND _double_triple = false) OR
	  (_rs.id_rank4 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)3-(4|5|6|7|8|9|10)(/|$)') OR
	  (_rs.id_rank5 = ANY(_ids) AND _triple = false AND (_double = true OR _rs.exa ~* '(^|/)3-(5|6|7|8|9|10)(/|$)')) OR
	  (_rs.id_rank6 = ANY(_ids) AND _triple = false AND (_double = true OR _rs.exa ~* '(^|/)3-(6|7|8|9|10)(/|$)')) OR
	  (_rs.id_rank7 = ANY(_ids) AND (_triple = true OR _rs.exa ~* '(^|/)3-(7|8|9|10)(/|$)')) OR
	  (_rs.id_rank8 = ANY(_ids) AND (_triple = true OR _rs.exa ~* '(^|/)3-(8|9|10)(/|$)')) OR
	  (_rs.id_rank9 = ANY(_ids) AND (_triple = true OR _rs.exa ~* '(^|/)3-(9|10)(/|$)')) OR
	  (_rs.id_rank10 = ANY(_ids) AND _rs.exa ~* '(^|/)3-10(/|$)') THEN
		RETURN 3;
	END IF;
	IF (_rs.id_rank4 IS NULL) THEN
		RETURN 0;
	END IF;
	IF (_rs.id_rank4 = ANY(_ids) AND _double_triple = false) OR
	  (_rs.id_rank5 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)4-(5|6|7|8|9|10)(/|$)') OR
	  (_rs.id_rank6 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)4-(6|7|8|9|10)(/|$)') OR
	  (_rs.id_rank7 = ANY(_ids) AND (_double = true OR _rs.exa ~* '(^|/)4-(7|8|9|10)(/|$)')) OR
	  (_rs.id_rank8 = ANY(_ids) AND (_double = true OR _rs.exa ~* '(^|/)4-(8|9|10)(/|$)')) OR
	  (_rs.id_rank9 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)4-(9|10)(/|$)') OR
	  (_rs.id_rank10 = ANY(_ids) AND _rs.exa ~* '(^|/)4-10(/|$)') THEN
		RETURN 4;
	END IF;
	IF (_rs.id_rank5 IS NULL) THEN
		RETURN 0;
	END IF;
	IF (_rs.id_rank5 = ANY(_ids) AND _double_triple = false) OR
	  (_rs.id_rank6 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)5-(6|7|8|9|10)(/|$)') OR
	  (_rs.id_rank7 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)5-(7|8|9|10)(/|$)') OR
	  (_rs.id_rank8 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)5-(8|9|10)(/|$)') OR
	  (_rs.id_rank9 = ANY(_ids) AND (_double = true OR _rs.exa ~* '(^|/)5-(9|10)(/|$)')) OR
	  (_rs.id_rank10 = ANY(_ids) AND _rs.exa ~* '(^|/)5-10(/|$)') THEN
		RETURN 5;
	END IF;
	IF (_rs.id_rank6 IS NULL) THEN
		RETURN 0;
	END IF;
	IF (_rs.id_rank6 = ANY(_ids) AND _double_triple = false) OR
	  (_rs.id_rank7 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)6-(7|8|9|10)(/|$)') OR
	  (_rs.id_rank8 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)6-(8|9|10)(/|$)') OR
	  (_rs.id_rank9 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)6-(9|10)(/|$)') OR
	  (_rs.id_rank10 = ANY(_ids) AND _rs.exa ~* '(^|/)6-10(/|$)') THEN
		RETURN 6;
	END IF;
	IF (_rs.id_rank7 IS NULL) THEN
		RETURN 0;
	END IF;
	IF (_rs.id_rank7 = ANY(_ids) AND _double_triple = false) OR
	  (_rs.id_rank8 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)7-(8|9|10)(/|$)') OR
	  (_rs.id_rank9 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)7-(9|10)(/|$)') OR
	  (_rs.id_rank10 = ANY(_ids) AND _rs.exa ~* '(^|/)7-10(/|$)') THEN
		RETURN 7;
	END IF;
	IF (_rs.id_rank8 IS NULL) THEN
		RETURN 0;
	END IF;
	IF (_rs.id_rank8 = ANY(_ids) AND _double_triple = false) OR
	  (_rs.id_rank9 = ANY(_ids) AND _double_triple = false AND _rs.exa ~* '(^|/)8-(9|10)(/|$)') OR
	  (_rs.id_rank10 = ANY(_ids) AND _rs.exa ~* '(^|/)8-10(/|$)') THEN
		RETURN 8;
	END IF;
	IF (_rs.id_rank9 IS NULL) THEN
		RETURN 0;
	END IF;
	IF (_rs.id_rank9 = ANY(_ids) AND _double_triple = false) OR
	  (_rs.id_rank10 = ANY(_ids) AND _rs.exa ~* '(^|/)9-10(/|$)') THEN
		RETURN 9;
	END IF;
	IF (_rs.id_rank10 IS NULL) THEN
		RETURN 0;
	END IF;
	IF (_rs.id_rank10 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 10;
	END IF;
	IF (_rs.id_rank11 IS NULL) THEN
		RETURN 0;
	END IF;
	IF (_rs.id_rank11 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 11;
	ELSIF (_rs.id_rank12 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 12;
	ELSIF (_rs.id_rank13 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 13;
	ELSIF (_rs.id_rank14 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 14;
	ELSIF (_rs.id_rank15 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 15;
	ELSIF (_rs.id_rank16 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 16;
	ELSIF (_rs.id_rank17 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 17;
	ELSIF (_rs.id_rank18 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 18;
	ELSIF (_rs.id_rank19 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 19;
	ELSIF (_rs.id_rank20 = ANY(_ids) AND _double_triple = false) THEN
		RETURN 20;
	END IF;
	RETURN 0;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
