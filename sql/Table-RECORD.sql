-- Table: "RECORD"

-- DROP TABLE "RECORD";

CREATE TABLE "RECORD"
(
  id integer NOT NULL,
  id_sport integer NOT NULL,
  id_championship integer,
  id_event integer,
  id_subevent integer,
  id_city integer,
  id_rank1 integer,
  id_rank2 integer,
  id_rank3 integer,
  id_rank4 integer,
  id_rank5 integer,
  record1 character varying(20) NOT NULL,
  record2 character varying(20) DEFAULT NULL::character varying,
  record3 character varying(20) DEFAULT NULL::character varying,
  record4 character varying(20) DEFAULT NULL::character varying,
  record5 character varying(20) DEFAULT NULL::character varying,
  date1 character varying(30) DEFAULT NULL::character varying,
  date2 character varying(30) DEFAULT NULL::character varying,
  date3 character varying(30) DEFAULT NULL::character varying,
  date4 character varying(30) DEFAULT NULL::character varying,
  date5 character varying(30) DEFAULT NULL::character varying,
  "index" numeric(10,0) DEFAULT NULL::numeric,
  type1 character varying(10) DEFAULT NULL::character varying,
  type2 character varying(15) DEFAULT NULL::character varying,
  "comment" character varying(500) DEFAULT NULL::character varying,
  label character varying(70) NOT NULL,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  counting boolean,
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  exa character varying(15),
  CONSTRAINT "RECORD_pkey" PRIMARY KEY (id),
  CONSTRAINT "RECORD_id_championship_fkey" FOREIGN KEY (id_championship)
      REFERENCES "CHAMPIONSHIP" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RECORD_id_city_fkey" FOREIGN KEY (id_city)
      REFERENCES "CITY" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RECORD_id_event_fkey" FOREIGN KEY (id_event)
      REFERENCES "EVENT" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RECORD_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RECORD_id_sport_fkey" FOREIGN KEY (id_sport)
      REFERENCES "SPORT" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RECORD_id_subevent_fkey" FOREIGN KEY (id_subevent)
      REFERENCES "EVENT" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);

-- Trigger: trigger_rc on "RECORD"

-- DROP TRIGGER trigger_rc ON "RECORD";

CREATE TRIGGER trigger_rc
  AFTER INSERT OR UPDATE OR DELETE
  ON "RECORD"
  FOR EACH ROW
  EXECUTE PROCEDURE "UPDATE_REF"('RC');

