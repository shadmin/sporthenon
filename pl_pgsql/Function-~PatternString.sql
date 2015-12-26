-- Function: "~PatternString"(character varying)

-- DROP FUNCTION "~PatternString"(character varying);

CREATE OR REPLACE FUNCTION "~PatternString"(_pattern character varying)
  RETURNS character varying AS
$BODY$
declare
	_result text;
begin
	_result := lower(_pattern);
	_result := replace(_result, '%', '.*');
	_result := regexp_replace(_result, '(a|á|à|ä|ă|ā|ã|å|â|Á|À|Ä|Ă|Ā|Ã|Å|Â)', '(a|á|à|ä|ă|ā|ã|å|â|Á|À|Ä|Ă|Ā|Ã|Å|Â)', 'g');
	_result := regexp_replace(_result, '(ae|æ|Æ)', '(ae|æ|Æ)', 'g');
	_result := regexp_replace(_result, '(c|ć|č|ç|Ć|Č|Ç)', '(c|ć|č|ç|Ć|Č|Ç)', 'g');
	_result := regexp_replace(_result, '(dj|đ|Đ)', '(dj|đ|Đ)', 'g');
	_result := regexp_replace(_result, '(e|ė|é|è|ê|ë|ě|ę|ē|Ė|É|È|Ê|Ë|Ě|Ę|Ē)', '(e|ė|é|è|ê|ë|ě|ę|ē|Ė|É|È|Ê|Ë|Ě|Ę|Ē)', 'g');
	_result := regexp_replace(_result, '(g|ğ|Ğ)', '(g|ğ|Ğ)', 'g');
	_result := regexp_replace(_result, '(i|î|ı|í|ï|Î|I|Í|Ï)', '(i|î|ı|í|ï|Î|I|Í|Ï)', 'g');
	_result := regexp_replace(_result, '(l|ł|Ł)', '(l|ł|Ł)', 'g');
	_result := regexp_replace(_result, '(n|ń|ñ|Ń|Ñ)', '(n|ń|ñ|Ń|Ñ)', 'g');
	_result := regexp_replace(_result, '(o|ô|ó|ò|ö|ō|ø|Ô|Ó|Ò|Ö|Ō|Ø)', '(o|ô|ó|ò|ö|ō|ø|Ô|Ó|Ò|Ö|Ō|Ø)', 'g');
	_result := regexp_replace(_result, '(r|ř|Ř)', '(r|ř|Ř)', 'g');
	_result := regexp_replace(_result, '(s|ś|š|ş|Ś|Š|Ş)', '(s|ś|š|ş|Ś|Š|Ş)', 'g');
	_result := regexp_replace(_result, '(ss|ß)', '(ss|ß)', 'g');
	_result := regexp_replace(_result, '(t|ţ|Ţ)', '(t|ţ|Ţ)', 'g');
	_result := regexp_replace(_result, '(u|û|ū|ú|ü|Û|Ū|Ú|Ü)', '(u|û|ū|ú|ü|Û|Ū|Ú|Ü)', 'g');
	_result := regexp_replace(_result, '(y|ÿ|ý|Ÿ|Ý)', '(y|ÿ|ý|Ÿ|Ý)', 'g');
	_result := regexp_replace(_result, '(z|ż|ź|ž|Ż|Ź|Ž)', '(z|ż|ź|ž|Ż|Ź|Ž)', 'g');

	RETURN _result;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
