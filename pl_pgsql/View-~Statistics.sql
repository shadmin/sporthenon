-- View: "~Statistics"

-- DROP VIEW "~Statistics";

CREATE OR REPLACE VIEW "~Statistics" AS 
 SELECT ( SELECT count(*) AS count
           FROM "Athlete") AS count_person,
    ( SELECT count(*) AS count
           FROM "City") AS count_city,
    ( SELECT count(*) AS count
           FROM "Complex") AS count_complex,
    ( SELECT count(*) AS count
           FROM "Country") AS count_country,
    ( SELECT count(*) AS count
           FROM "Event") AS count_event,
    ( SELECT count(*) AS count
           FROM "Result") AS count_result,
    ( SELECT count(*) AS count
           FROM "Sport") AS count_sport,
    ( SELECT count(*) AS count
           FROM "Team") AS count_team;
