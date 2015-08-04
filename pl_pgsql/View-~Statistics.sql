-- View: "~Statistics"

-- DROP VIEW "~Statistics";

CREATE OR REPLACE VIEW "~Statistics" AS 
 SELECT ( SELECT count(*) AS count
           FROM "CITY") AS count_city, ( SELECT count(*) AS count
           FROM "COMPLEX") AS count_complex, ( SELECT count(*) AS count
           FROM "COUNTRY") AS count_country, ( SELECT count(*) AS count
           FROM "EVENT") AS count_event, ( SELECT count(*) AS count
           FROM "PERSON") AS count_person, ( SELECT count(*) AS count
           FROM "RESULT") AS count_result, ( SELECT count(*) AS count
           FROM "SPORT") AS count_sport, ( SELECT count(*) AS count
           FROM "TEAM") AS count_team;
