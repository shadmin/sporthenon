-- Table: "RESULT"

-- DROP TABLE "RESULT";

CREATE TABLE "RESULT"
(
  id integer NOT NULL,
  id_sport integer NOT NULL,
  id_championship integer NOT NULL,
  id_event integer,
  id_subevent integer,
  id_city2 integer,
  id_complex2 integer,
  id_year integer NOT NULL,
  date1 character varying(10) DEFAULT NULL::character varying,
  date2 character varying(10) DEFAULT NULL::character varying,
  id_rank1 integer,
  id_rank2 integer,
  id_rank3 integer,
  id_rank4 integer,
  id_rank5 integer,
  id_rank6 integer,
  id_rank7 integer,
  id_rank8 integer,
  id_rank9 integer,
  id_rank10 integer,
  result1 character varying(40) DEFAULT NULL::character varying,
  result2 character varying(20) DEFAULT NULL::character varying,
  result3 character varying(20) DEFAULT NULL::character varying,
  result4 character varying(20) DEFAULT NULL::character varying,
  result5 character varying(20) DEFAULT NULL::character varying,
  "comment" character varying(500) DEFAULT NULL::character varying,
  id_member integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  exa character varying(15),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  id_city1 integer,
  id_complex1 integer,
  id_subevent2 integer,
  CONSTRAINT "RESULT_pkey" PRIMARY KEY (id),
  CONSTRAINT "RESULT_id_championship_fkey" FOREIGN KEY (id_championship)
      REFERENCES "CHAMPIONSHIP" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RESULT_id_city_fkey" FOREIGN KEY (id_city2)
      REFERENCES "CITY" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RESULT_id_complex_fkey" FOREIGN KEY (id_complex2)
      REFERENCES "COMPLEX" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RESULT_id_event_fkey" FOREIGN KEY (id_event)
      REFERENCES "EVENT" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RESULT_id_member_fkey" FOREIGN KEY (id_member)
      REFERENCES "~MEMBER" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RESULT_id_sport_fkey" FOREIGN KEY (id_sport)
      REFERENCES "SPORT" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RESULT_id_subevent_fkey" FOREIGN KEY (id_subevent)
      REFERENCES "EVENT" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RESULT_id_year_fkey" FOREIGN KEY (id_year)
      REFERENCES "YEAR" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);

-- Trigger: trigger_rs on "RESULT"

-- DROP TRIGGER trigger_rs ON "RESULT";

CREATE TRIGGER trigger_rs
  AFTER INSERT OR UPDATE OR DELETE
  ON "RESULT"
  FOR EACH ROW
  EXECUTE PROCEDURE "UPDATE_REF"('RS');

