-- Table: "TEAM"

-- DROP TABLE "TEAM";

CREATE TABLE "TEAM"
(
  id integer NOT NULL,
  label character varying(60) NOT NULL,
  id_sport integer NOT NULL,
  year1 character varying(4) DEFAULT NULL::character varying,
  year2 character varying(4) DEFAULT NULL::character varying,
  id_country integer,
  conference character varying(10) DEFAULT NULL::character varying,
  division character varying(10) DEFAULT NULL::character varying,
  "comment" character varying(500) DEFAULT NULL::character varying,
  id_member integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  link integer,
  inactive boolean,
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  ref smallint,
  CONSTRAINT "TEAM_pkey" PRIMARY KEY (id),
  CONSTRAINT "TEAM_id_country_fkey" FOREIGN KEY (id_country)
      REFERENCES "COUNTRY" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "TEAM_id_member_fkey" FOREIGN KEY (id_member)
      REFERENCES "~MEMBER" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "TEAM_id_sport_fkey" FOREIGN KEY (id_sport)
      REFERENCES "SPORT" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "TEAM_label_key" UNIQUE (label, id_sport, id_country, year1, year2)
)
WITH (
  OIDS=FALSE
);

-- Trigger: trigger_tm on "TEAM"

-- DROP TRIGGER trigger_tm ON "TEAM";

CREATE TRIGGER trigger_tm
  AFTER INSERT OR UPDATE OR DELETE
  ON "TEAM"
  FOR EACH ROW
  EXECUTE PROCEDURE "UPDATE_REF"('TM');

