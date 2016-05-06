-- Table: "Record"

-- DROP TABLE "Record";

CREATE TABLE "Record"
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
  index numeric(10,0) DEFAULT NULL::numeric,
  type1 character varying(10) DEFAULT NULL::character varying,
  type2 character varying(15) DEFAULT NULL::character varying,
  comment character varying(500) DEFAULT NULL::character varying,
  label character varying(100) NOT NULL,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  counting boolean,
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  exa character varying(15),
  CONSTRAINT "RECORD_pkey" PRIMARY KEY (id),
  CONSTRAINT "RECORD_id_championship_fkey" FOREIGN KEY (id_championship)
      REFERENCES "Championship" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RECORD_id_city_fkey" FOREIGN KEY (id_city)
      REFERENCES "City" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RECORD_id_event_fkey" FOREIGN KEY (id_event)
      REFERENCES "Event" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RECORD_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RECORD_id_sport_fkey" FOREIGN KEY (id_sport)
      REFERENCES "Sport" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RECORD_id_subevent_fkey" FOREIGN KEY (id_subevent)
      REFERENCES "Event" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);

-- Trigger: TriggerRC on "Record"

-- DROP TRIGGER "TriggerRC" ON "Record";

CREATE TRIGGER "TriggerRC"
  AFTER INSERT OR UPDATE OR DELETE
  ON "Record"
  FOR EACH ROW
  EXECUTE PROCEDURE "UpdateRef"('RC');
