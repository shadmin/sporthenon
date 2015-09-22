-- Table: "Athlete"

-- DROP TABLE "Athlete";

CREATE TABLE "Athlete"
(
  id integer NOT NULL,
  last_name character varying(30) NOT NULL,
  first_name character varying(30) DEFAULT NULL::character varying,
  id_country integer,
  id_team integer,
  id_sport integer NOT NULL,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  link integer,
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  ref smallint,
  photo_copyright character varying(100),
  CONSTRAINT "PERSON_pkey" PRIMARY KEY (id),
  CONSTRAINT "PERSON_id_country_fkey" FOREIGN KEY (id_country)
      REFERENCES "Country" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "PERSON_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "PERSON_id_sport_fkey" FOREIGN KEY (id_sport)
      REFERENCES "Sport" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "PERSON_id_team_fkey" FOREIGN KEY (id_team)
      REFERENCES "Team" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "PERSON_last_name_key" UNIQUE (last_name, first_name, id_country, id_sport, id_team)
)
WITH (
  OIDS=FALSE
);

-- Index: "PR_LAST_NAME_INDEX"

-- DROP INDEX "PR_LAST_NAME_INDEX";

CREATE INDEX "PR_LAST_NAME_INDEX"
  ON "Athlete"
  USING btree
  (lower(last_name::text));


-- Trigger: TriggerPR on "Athlete"

-- DROP TRIGGER "TriggerPR" ON "Athlete";

CREATE TRIGGER "TriggerPR"
  AFTER INSERT OR UPDATE OR DELETE
  ON "Athlete"
  FOR EACH ROW
  EXECUTE PROCEDURE "UpdateRef"('PR');

