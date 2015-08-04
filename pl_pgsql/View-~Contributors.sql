-- View: "~Contributors"

-- DROP VIEW "~Contributors";

CREATE OR REPLACE VIEW "~Contributors" AS 
 SELECT t.id, t.login, t.name, array_to_string(array_agg(sp.label), '|'::text) AS sports, array_to_string(array_agg(sp.label_fr), '|'::text) AS sports_fr, t.count_a, t.count_u
   FROM ( SELECT cr.id, cr.login, cr.public_name AS name, cr.sports, sum(
                CASE
                    WHEN cb.type = 'A'::bpchar THEN 1
                    ELSE 0
                END) AS count_a, sum(
                CASE
                    WHEN cb.type = 'U'::bpchar THEN 1
                    ELSE 0
                END) AS count_u
           FROM "~Contributor" cr
      LEFT JOIN "~CONTRIBUTION" cb ON cr.id = cb.id_contributor
     GROUP BY cr.id, cr.login, cr.public_name, cr.sports) t
   LEFT JOIN "SPORT" sp ON sp.id = ANY (string_to_array(t.sports::text, ','::text)::integer[])
  GROUP BY t.id, t.login, t.name, t.count_a, t.count_u
  ORDER BY t.count_a DESC;
