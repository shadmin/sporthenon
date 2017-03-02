-- Table: "Result"

-- DROP TABLE "Result";

CREATE TABLE "Result"
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
  comment character varying(500) DEFAULT NULL::character varying,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  exa character varying(15),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  id_city1 integer,
  id_complex1 integer,
  id_subevent2 integer,
  result6 character varying(20),
  result7 character varying(20),
  result8 character varying(20),
  result9 character varying(20),
  result10 character varying(20),
  id_rank11 integer,
  result11 character varying(20),
  id_rank12 integer,
  result12 character varying(20),
  id_rank13 integer,
  result13 character varying(20),
  id_rank14 integer,
  result14 character varying(20),
  id_rank15 integer,
  result15 character varying(20),
  id_rank16 integer,
  result16 character varying(20),
  id_rank17 integer,
  result17 character varying(20),
  id_rank18 integer,
  result18 character varying(20),
  id_rank19 integer,
  result19 character varying(20),
  id_rank20 integer,
  result20 character varying(20),
  id_country1 integer,
  id_country2 integer,
  draft boolean,
  no_place boolean,
  no_date boolean,
  CONSTRAINT "RESULT_pkey" PRIMARY KEY (id),
  CONSTRAINT "RESULT_id_championship_fkey" FOREIGN KEY (id_championship)
      REFERENCES "Championship" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RESULT_id_city_fkey" FOREIGN KEY (id_city2)
      REFERENCES "City" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RESULT_id_complex_fkey" FOREIGN KEY (id_complex2)
      REFERENCES "Complex" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RESULT_id_event_fkey" FOREIGN KEY (id_event)
      REFERENCES "Event" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RESULT_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RESULT_id_sport_fkey" FOREIGN KEY (id_sport)
      REFERENCES "Sport" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RESULT_id_subevent_fkey" FOREIGN KEY (id_subevent)
      REFERENCES "Event" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RESULT_id_year_fkey" FOREIGN KEY (id_year)
      REFERENCES "Year" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);

-- Trigger: TriggerRS on "Result"

-- DROP TRIGGER "TriggerRS" ON "Result";

CREATE TRIGGER "TriggerRS"
  AFTER INSERT OR UPDATE OR DELETE
  ON "Result"
  FOR EACH ROW
  EXECUTE PROCEDURE "UpdateRef"('RS');
