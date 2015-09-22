-- Table: "RetiredNumber"

-- DROP TABLE "RetiredNumber";

CREATE TABLE "RetiredNumber"
(
  id integer NOT NULL,
  id_league integer NOT NULL,
  id_team integer NOT NULL,
  id_person integer NOT NULL,
  "number" smallint NOT NULL,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  id_year integer,
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT "RETIRED_NUMBER_pkey" PRIMARY KEY (id),
  CONSTRAINT "RETIRED_NUMBER_id_league_fkey" FOREIGN KEY (id_league)
      REFERENCES "League" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RETIRED_NUMBER_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RETIRED_NUMBER_id_person_fkey" FOREIGN KEY (id_person)
      REFERENCES "Athlete" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RETIRED_NUMBER_id_team_fkey" FOREIGN KEY (id_team)
      REFERENCES "Team" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);

-- Trigger: TriggerRN on "RetiredNumber"

-- DROP TRIGGER "TriggerRN" ON "RetiredNumber";

CREATE TRIGGER "TriggerRN"
  AFTER INSERT OR UPDATE OR DELETE
  ON "RetiredNumber"
  FOR EACH ROW
  EXECUTE PROCEDURE "UpdateRef"('RN');

