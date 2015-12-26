-- Table: "Team"

-- DROP TABLE "Team";

CREATE TABLE "Team"
(
  id integer NOT NULL,
  label character varying(60) NOT NULL,
  id_sport integer NOT NULL,
  year1 character varying(4) DEFAULT NULL::character varying,
  year2 character varying(4) DEFAULT NULL::character varying,
  id_country integer,
  conference character varying(10) DEFAULT NULL::character varying,
  division character varying(10) DEFAULT NULL::character varying,
  comment character varying(500) DEFAULT NULL::character varying,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  link integer,
  inactive boolean,
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  ref smallint,
  id_league integer,
  CONSTRAINT "TEAM_pkey" PRIMARY KEY (id),
  CONSTRAINT "TEAM_id_country_fkey" FOREIGN KEY (id_country)
      REFERENCES "Country" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "TEAM_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "TEAM_id_sport_fkey" FOREIGN KEY (id_sport)
      REFERENCES "Sport" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "TEAM_label_key" UNIQUE (label, id_sport, id_country, year1, year2)
)
WITH (
  OIDS=FALSE
);

-- Index: "TM_LABEL_INDEX"

-- DROP INDEX "TM_LABEL_INDEX";

CREATE INDEX "TM_LABEL_INDEX"
  ON "Team"
  USING btree
  (lower(label::text) COLLATE pg_catalog."default");


-- Trigger: TriggerTM on "Team"

-- DROP TRIGGER "TriggerTM" ON "Team";

CREATE TRIGGER "TriggerTM"
  AFTER INSERT OR UPDATE OR DELETE
  ON "Team"
  FOR EACH ROW
  EXECUTE PROCEDURE "UpdateRef"('TM');
