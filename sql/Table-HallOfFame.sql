-- Table: "HallOfFame"

-- DROP TABLE "HallOfFame";

CREATE TABLE "HallOfFame"
(
  id integer NOT NULL,
  id_league integer NOT NULL,
  id_year integer NOT NULL,
  id_person integer NOT NULL,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  "position" character varying(30),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT "HALL_OF_FAME_pkey" PRIMARY KEY (id),
  CONSTRAINT "HALL_OF_FAME_id_league_fkey" FOREIGN KEY (id_league)
      REFERENCES "League" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "HALL_OF_FAME_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "HALL_OF_FAME_id_person_fkey" FOREIGN KEY (id_person)
      REFERENCES "Athlete" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "HALL_OF_FAME_id_year_fkey" FOREIGN KEY (id_year)
      REFERENCES "Year" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);

-- Trigger: TriggerHF on "HallOfFame"

-- DROP TRIGGER "TriggerHF" ON "HallOfFame";

CREATE TRIGGER "TriggerHF"
  AFTER INSERT OR UPDATE OR DELETE
  ON "HallOfFame"
  FOR EACH ROW
  EXECUTE PROCEDURE "UpdateRef"('HF');

