-- Table: "PERSON"

-- DROP TABLE "PERSON";

CREATE TABLE "PERSON"
(
  id integer NOT NULL,
  last_name character varying(30) NOT NULL,
  first_name character varying(30) DEFAULT NULL::character varying,
  id_country integer,
  id_team integer,
  id_sport integer NOT NULL,
  id_member integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  link integer,
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  url_wiki character varying(200),
  url_olyref character varying(200),
  url_bktref character varying(200),
  url_bbref character varying(200),
  url_ftref character varying(200),
  url_hkref character varying(200),
  ref smallint,
  CONSTRAINT "PERSON_pkey" PRIMARY KEY (id),
  CONSTRAINT "PERSON_id_country_fkey" FOREIGN KEY (id_country)
      REFERENCES "COUNTRY" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "PERSON_id_member_fkey" FOREIGN KEY (id_member)
      REFERENCES "~MEMBER" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "PERSON_id_sport_fkey" FOREIGN KEY (id_sport)
      REFERENCES "SPORT" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "PERSON_id_team_fkey" FOREIGN KEY (id_team)
      REFERENCES "TEAM" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "PERSON_last_name_key" UNIQUE (last_name, first_name, id_country, id_sport, id_team)
)
WITH (
  OIDS=FALSE
);

-- Trigger: trigger_pr on "PERSON"

-- DROP TRIGGER trigger_pr ON "PERSON";

CREATE TRIGGER trigger_pr
  AFTER INSERT OR UPDATE OR DELETE
  ON "PERSON"
  FOR EACH ROW
  EXECUTE PROCEDURE "UPDATE_REF"('PR');
